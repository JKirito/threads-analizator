package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Utils.StoreFile;
import Utils.Utils;
import entities.DiarioDigital;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.FormatoTexto;
import entities.Seccion;

public class PageDownloader extends Observable {

	private DiarioDigital diario;
	private Seccion seccion;
	private FormatoSalida formatoSalida;
	private String pathAGuardar;
	private Date fechaDate;
	private int cantDiasARecopilar;
	private List<String> erroresDescarga = new ArrayList<String>();
	private boolean override;
	private Integer threads_number = 20; // Por defecto uso 20 hilos
	/**
	 * Contador para todas las descargas - Utilzado para JProgress
	 */
	private AtomicInteger descargasARealizar = new AtomicInteger(0);
	/**
	 * Contador para las descargas que realmente se descargaron
	 */
	private AtomicInteger descargasRealizadas = new AtomicInteger(0);
	/**
	 * Contador para las descargas que no fueron necesarias porque ya existían
	 * los archivos
	 */
	private AtomicInteger descargasNoNecesarias = new AtomicInteger(0);
	/**
	 * Contador para las descargas fallidas-No realizadas
	 */
	private AtomicInteger descargasFallidas = new AtomicInteger(0);
	/**
	 * Encargado de llevar a cabo el pool de Threads, c/u a descargar algo
	 * diferente
	 */
	private ExecutorService executor;
	/**
	 * Si true, detiene la descarga
	 */
	private boolean detener = false;

	public PageDownloader(DiarioDigital diario, Seccion seccion, FormatoSalida formatoSalida, String pathAGuardar,
			Date fechaHasta, int diasARecopílar, boolean override) {
		super();
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formatoSalida;
		this.pathAGuardar = pathAGuardar;
		this.fechaDate = fechaHasta;
		this.cantDiasARecopilar = diasARecopílar;
		this.override = override;
		this.executor = Executors.newFixedThreadPool(threads_number);// TODO!!!!!!!!
	}

	public PageDownloader(DiarioDigital diario, Seccion seccion, FormatoSalida formatoSalida, String pathAGuardar,
			Date fechaHasta, int diasARecopílar, boolean override, int numeroHilos) {
		super();
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formatoSalida;
		this.pathAGuardar = pathAGuardar;
		this.fechaDate = fechaHasta;
		this.cantDiasARecopilar = diasARecopílar;
		this.override = override;
		// this.threads_number = numeroHilos;
	}

	public List<String> getErroresDescarga() {
		return erroresDescarga;
	}

	public class ThreadDownload implements Runnable {
		private String linkActual;
		private String fecha;

		public ThreadDownload(String linkActual, String fecha) {
			this.linkActual = linkActual;
			this.fecha = fecha;
		}

		@Override
		public void run() {
			String nombreArchivo = diario.getNombreArchivo(fecha);
			descargasARealizar.incrementAndGet();

			// Notificar avance de descargas para el Jprogress
			setChanged();
			notifyObservers(descargasARealizar);

			// Doy un pequeño tiempo para que se actualice el contador del
			// Jprogress
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			if (!override) {
				if (StoreFile.fileExists(pathAGuardar, nombreArchivo, formatoSalida.getExtension())) {
					descargasNoNecesarias.incrementAndGet();
					return;
				}
			}

			Document page = null;

			// Verifico que tenga que seguir ejecutandose o detener la ejecución
			// del hilo actual
			if (detener) {
				return;
			}

			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
				if (!diario.esValido(page)) {
					erroresDescarga.add("Al parecer " + diario.getNombreDiario() + " no tiene noticias en la sección "
							+ seccion.getNombreSeccion() + " del día: " + fecha + ".\r\n");
					descargasFallidas.incrementAndGet();
					return;
				}
			} catch (UnknownHostException e) {
				erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + ", sección "
						+ seccion.getNombreSeccion() + " del día: " + fecha
						+ ". Esto puede deberse a una desconexión de internet.\r\n");
				descargasFallidas.incrementAndGet();
				return;
			} catch (SocketTimeoutException e) {
				erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + ", sección "
						+ seccion.getNombreSeccion() + " del día: " + fecha + ". Error por Time out.\r\n");
				descargasFallidas.incrementAndGet();
				return;
			} catch (IOException e) {
				erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + ", sección "
						+ seccion.getNombreSeccion() + " del día: " + fecha + ".\r\n");
				descargasFallidas.incrementAndGet();
				return;
			}

			// Verifico que tenga que seguir ejecutandose o detener la ejecución
			// del hilo actual
			if (detener) {
				return;
			}

			// TODO: modificar en caso que se quiera guardar txt ? si para tapas
			// no está esa opción, eliminar este "todo"
			Element tapas = diario.getSoloGrupoNoticias(page);

			String datosAGuardar = null;
			if (formatoSalida.getNombre().equals(FormatoHtml.NOMBRE)) {
				datosAGuardar = tapas.html();
			} else if (formatoSalida.getNombre().equals(FormatoTexto.NOMBRE)) {
				datosAGuardar = tapas.text();
			}

			try {
				StoreFile sf = new StoreFile(pathAGuardar, formatoSalida.getExtension(), datosAGuardar, nombreArchivo,
						diario.getCharsetName());
				sf.store(override);
				descargasRealizadas.incrementAndGet();
			} catch (IOException e) {
				erroresDescarga.add("ERROR AL QUERER GUARDAR EL ARCHIVO " + nombreArchivo + "\r\n");
				descargasFallidas.incrementAndGet();
			}
		}
	}

	public void download() {
		for (int i = cantDiasARecopilar; i > 0 && !detener; i--) {
			// Para que empiece a restar después de descargar el primer día
			if (i != cantDiasARecopilar) {
				fechaDate = Utils.sumarRestarDiasFecha(fechaDate, -1);
			}
			String fecha = diario.getFechaConFormato(fechaDate);
			String linkActual = diario.armarLinkActual(fecha, seccion);

			ThreadDownload newDownload = new ThreadDownload(linkActual, fecha);

			while (((ThreadPoolExecutor) executor).getActiveCount() == ((ThreadPoolExecutor) executor)
					.getCorePoolSize()) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			executor.submit(newDownload);
		}
		// Hasta que no termine de descargar todos, que espere..
		while (((ThreadPoolExecutor) executor).getActiveCount() != 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (detener) {
			detener = false;
		}

	}

	public AtomicInteger getDescargasARealizar() {
		return descargasARealizar;
	}

	public AtomicInteger getDescargasRealizadas() {
		return descargasRealizadas;
	}

	public AtomicInteger getDescargasNoNecesarias() {
		return descargasNoNecesarias;
	}

	public AtomicInteger getDescargasFallidas() {
		return descargasFallidas;
	}

	public void detenerEjecucion() {
		this.detener = true;
	}

}