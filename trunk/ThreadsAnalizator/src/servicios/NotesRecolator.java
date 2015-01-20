package servicios;

import java.io.File;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import servicios_LaNacion.NoteProcessorLaNacion;
import servicios_Pagina12.NoteProcessorPagina12;
import entities.DiarioDigital;
import entities.FormatoSalida;

public class NotesRecolator extends Observable {

	private String pathAGuardar;
	private String pathOrigen;
	private Integer THREADS_NUMBER;
	private DiarioDigital diario;
	private FormatoSalida formatoSalida;
	ExecutorService executor;
	/**
	 * Si true, detiene la descarga
	 */
	private boolean detener = false;

	public NotesRecolator(String carpetaOrigen, String carpetaDestino, int cantHilos, DiarioDigital diario,
			FormatoSalida formato) {
		this.pathOrigen = carpetaOrigen;
		this.pathAGuardar = carpetaDestino;
		this.THREADS_NUMBER = cantHilos;
		this.diario = diario;
		this.formatoSalida = formato;
		executor = Executors.newFixedThreadPool(THREADS_NUMBER);
	}

	public void iniciar() {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		if (carpeta.isDirectory()) {
			int i = 1;
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile() && !detener) {

					if (i % 10 == 0) {
						this.setChanged();
						this.notifyObservers(i);
					}
					i++;

					// Obtener los links asociados a las notas de cada archivo
					Elements notasABuscar = diario.getElementNotasABuscar(file);

					for (Element E : notasABuscar) {
						NoteProcessor np = null;
						if (diario.isPagina12()) {
							np = new NoteProcessorPagina12(archivo, E, pathAGuardar, diario, formatoSalida);
						}
						if (diario.isLaNacion()) {
							np = new NoteProcessorLaNacion(archivo, E, pathAGuardar, diario, formatoSalida);
						}

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

	public void detenerEjecucion() {
		this.detener = true;
	}

}
