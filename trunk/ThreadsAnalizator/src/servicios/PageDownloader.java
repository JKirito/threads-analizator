package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

public class PageDownloader {

	private DiarioDigital diario;
	private Seccion seccion;
	private FormatoSalida formatoSalida;
	private String pathAGuardar;
	private Date fechaDate;
	private int cantDiasARecopilar;
	private List<String> erroresDescarga = new ArrayList<String>();
	private boolean override;
	private Integer threads_number = 20; // Por defecto uso 10 hilos

	public PageDownloader() {
		System.out.println("PAGEDONWLOader");
	}

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
		this.threads_number = numeroHilos;
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

			if (!override) {
				if (StoreFile.existeFile(pathAGuardar, nombreArchivo, formatoSalida.getExtension())) {
					return;
				}
			}

			Document page = null;
			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
				if (!diario.esValido(page)) {
					erroresDescarga.add("AL PARECER " + diario.getNombreDiario() + " NO TIENE NOTICIAS EN LA SECCIÓN "
							+ seccion.getNombreSección() + " DEL DIA: " + fecha + ".\r\n");
					return;
				}
			} catch (UnknownHostException e) {
				erroresDescarga.add("NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha
						+ ". ESTO PUEDE DEBERSE A UNA DESCONEXIÓN DE INTERNET.\r\n");
				return;
			} catch (SocketTimeoutException e) {
				erroresDescarga.add("NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha + ". Error por Time out.\r\n");
				return;
			} catch (IOException e) {
				erroresDescarga.add("NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha + ".\r\n");
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
			} catch (IOException e) {
				erroresDescarga.add("ERROR AL QUERER GUARDAR EL ARCHIVO " + nombreArchivo + "\r\n");
				// e.printStackTrace();
			}

		}
	}

	public void download() {
		ExecutorService executor = Executors.newFixedThreadPool(threads_number);
		for (int i = cantDiasARecopilar; i > 0; i--) {
			// Para que empiece a restar después de descargar el primer día
			if (i != cantDiasARecopilar) {
				fechaDate = Utils.sumarRestarDiasFecha(fechaDate, -1);
			}
			String fecha = Utils.dtoYYYY_MM_DD(fechaDate);
			String linkActual = diario.armarLinkActual(fecha, seccion);

			ThreadDownload newDownload = new ThreadDownload(linkActual, fecha);

			while (((ThreadPoolExecutor) executor).getActiveCount() == threads_number) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			executor.submit(newDownload);
		}
		//Hasta que no termine de descargar todos, que espere..
		while (((ThreadPoolExecutor) executor).getActiveCount() != 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}