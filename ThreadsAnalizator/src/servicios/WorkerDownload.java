package servicios;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import modelo.ModeloDescarga;
import modelo.ModeloDescargaNotas;
import modelo.ModeloDescargaTapas;

import org.jsoup.select.Elements;

public class WorkerDownload extends SwingWorker<Void, Integer> implements Observer {
	private final JLabel lblProgreso;
	private final JProgressBar progressBar;
	private ModeloDescarga modeloDescargas;

	public WorkerDownload(JLabel etiquetaProgreso, JProgressBar barraProgreso, ModeloDescarga modelo) {
		lblProgreso = etiquetaProgreso;
		progressBar = barraProgreso;
		this.modeloDescargas = modelo;
		if (this.modeloDescargas instanceof ModeloDescargaNotas) {
			this.modeloDescargas.setCantTapasDescargar(getCantNotasDescargar());
		}
		this.progressBar.setMaximum(this.modeloDescargas.getCantTapasDescargar());
		this.progressBar.setValue(0);
	}

	@Override
	protected Void doInBackground() throws Exception {
		assert (this.modeloDescargas != null);

		// Comienzo descarga. En el transcurso de la descarga me avisa la cant.
		// que va descargando para
		// actualizar el progressBar
		this.modeloDescargas.descargar();
		return null;
	}

	/**
	 * Tarea terminada, SwingWorker llama a este metodo en el hilo de despacho
	 * de eventos.
	 */
	@Override
	protected void done() {
		progressBar.setValue(this.modeloDescargas.getCantTapasDescargar());
		String msjDescarga = this.modeloDescargas.isDescargaDetenida() ? "Descarga Detenida" : "Descarga Completa! :D";
		lblProgreso.setText(msjDescarga);
	}

	/**
	 * SwingWorker llama a este metodo en el hilo de despacho de eventos cuando
	 * llamamos a publish() y le pasa los mismos parametros que nosotros
	 * pongamos en publish().<br>
	 * En este ejemplo, nosotros pasamos el valor de la barra de progreso.
	 */
	@Override
	protected void process(List<Integer> chunks) {
		int cantPorDescargar = chunks.get(0);
		String msjDescargando = "";
		this.progressBar.setValue(cantPorDescargar);
		int total = this.modeloDescargas.getCantTapasDescargar();
		if (modeloDescargas instanceof ModeloDescargaTapas) {
			msjDescargando = "Descargando.. " + cantPorDescargar + " de " + total + " tapas";
		} else if (modeloDescargas instanceof ModeloDescargaNotas) {
			msjDescargando = "Descargando.. " + cantPorDescargar + " de " + total + " notas";
		}
		this.lblProgreso.setText(msjDescargando);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Integer) {
			publish((Integer) arg);
		} else {
			publish(((AtomicInteger) arg).intValue());
		}
	}

	private int getCantNotasDescargar() {
		int i = 0;
		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(((ModeloDescargaNotas) this.modeloDescargas).getRutaOrigen());
		if (carpeta.isDirectory()) {
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + File.separator + archivo);
				if (file.isFile()) {
					// Obtener los links asociados a las notas de cada archivo
					Elements notasABuscar = ((ModeloDescargaNotas) this.modeloDescargas).getDiarioDescarga()
							.getElementNotasABuscar(file);
					i += notasABuscar.size();
				}
			}
		}
		return i;
	}
}
