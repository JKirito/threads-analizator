package servicios;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import modelo.ModeloDescarga;

public class WorkerDownload extends SwingWorker<Void, Integer> implements Observer {
	private final JLabel lblProgreso;
	private final JProgressBar progressBar;
	private ModeloDescarga modeloDescargas;

	public WorkerDownload(JLabel etiquetaProgreso, JProgressBar barraProgreso, ModeloDescarga modelo) {
		lblProgreso = etiquetaProgreso;
		progressBar = barraProgreso;
		this.modeloDescargas = modelo;
		this.progressBar.setMaximum(this.modeloDescargas.getCantTapasDescargar());
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
		int cantDescargados = chunks.get(0);
		int total = this.modeloDescargas.getCantTapasDescargar();
		progressBar.setValue(cantDescargados);
		this.lblProgreso.setText("Descargando.. "+ cantDescargados + " de " + total);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg == null){
			this.lblProgreso.setText("Deteniendo... ");
		}
		publish(((AtomicInteger) arg).intValue());
	}
}
