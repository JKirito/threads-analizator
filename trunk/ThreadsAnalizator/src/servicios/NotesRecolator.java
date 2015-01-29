package servicios;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Seccion;

public class NotesRecolator extends Observable {

	private String pathAGuardar;
	private String pathOrigen;
	private Integer THREADS_NUMBER;
	private DiarioDigital diario;
	private FormatoSalida formatoSalida;
	ExecutorService executor;
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
	 * Si true, detiene la descarga
	 */
	private boolean detener = false;
	private List<String> erroresDescarga = new ArrayList<String>();
	private boolean override;
	private Seccion seccion;

	public NotesRecolator(String carpetaOrigen, String carpetaDestino, int cantHilos, DiarioDigital diario,
			Seccion seccion, FormatoSalida formato, boolean override) {
		this.pathOrigen = carpetaOrigen;
		this.pathAGuardar = carpetaDestino;
		this.THREADS_NUMBER = cantHilos;
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formato;
		executor = Executors.newFixedThreadPool(THREADS_NUMBER);
		this.override = override;
	}

	public void iniciar() {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		if (carpeta.isDirectory()) {
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile() && !detener) {

					// Obtener los links asociados a las notas de cada archivo
					Elements notasABuscar = diario.getElementNotasABuscar(file);
					for (Element E : notasABuscar) {
						NoteProcessor np = null;
						np = new NoteProcessor(this, archivo, E, pathAGuardar, diario, seccion, formatoSalida, override);
						while (((ThreadPoolExecutor) executor).getActiveCount() == THREADS_NUMBER) {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// Verifico que tenga que seguir ejecutandose o detener
						// la ejecución
						// del hilo actual
						if (detener) {
							return;
						}
						executor.submit(np);
					}
				}
			}
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

	public void mostrarCambiosParaJprogres(Integer cantActual) {
		if (detener) {
			return;
		}
		this.setChanged();
		this.notifyObservers(cantActual);
		// Doy un pequeño tiempo para que se actualice el contador
		// del Jprogress
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void detenerEjecucion() {
		this.detener = true;
	}

	public boolean isDetener() {
		return this.detener;
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

	public void incrementDescargasARealizar() {
		this.descargasARealizar.incrementAndGet();
		mostrarCambiosParaJprogres(getDescargasARealizar().intValue());
	}

	public void incrementDescargasRealizadas() {
		this.descargasRealizadas.incrementAndGet();
	}

	public void incrementDescargasNoNecesarias() {
		this.descargasNoNecesarias.incrementAndGet();
	}

	public void incrementDescargasFallidas() {
		this.descargasFallidas.incrementAndGet();
	}

	public void addErroresDescarga(String error) {
		this.erroresDescarga.add(error);
	}

	public List<String> getErroresDescarga() {
		return erroresDescarga;
	}

}
