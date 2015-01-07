package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.ModeloDescargaNotas;
import modelo.ModeloDescargaTapas;
import vista.SeleccionarDescarga;
import vista.VistaDescargas;

public class controladorSeleccionarDescarga implements ActionListener {

	SeleccionarDescarga vistaSeleccionarDescarga;
	private static final String MODO_DESCARGA_NOTA = "Notas";
	private static final String MODO_DESCARGA_TAPA = "Tapas";

	public controladorSeleccionarDescarga(SeleccionarDescarga vistaSeleccionarDescarga) {
		//TODO: hacer esta vista que no extienda de JFrame!
		this.vistaSeleccionarDescarga = vistaSeleccionarDescarga;
		actionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// SE ELIGE MODO DESCARGA NOTAS
		if (e.getSource() == vistaSeleccionarDescarga.getBtnDescargarNotas()) {
			VistaDescargas vistaDescargasNotas = new VistaDescargas();
			ModeloDescargaNotas modeloDescargaNotas = new ModeloDescargaNotas();
			modeloDescargaNotas.setModoDescarga(MODO_DESCARGA_NOTA);

			ControladorDescargas controlador = new ControladorDescargas(modeloDescargaNotas, vistaDescargasNotas);

			this.vistaSeleccionarDescarga.setVisible(false);
			this.vistaSeleccionarDescarga.dispose();
			vistaDescargasNotas.getFrame().setVisible(true);
		}
		// SE ELIGE MODO DESCARGA TAPAS
		if (e.getSource() == vistaSeleccionarDescarga.getBtnDescargarTapas()) {
			VistaDescargas vistaDescargasNotas = new VistaDescargas();
			ModeloDescargaTapas modeloDescargaTapas = new ModeloDescargaTapas();
			modeloDescargaTapas.setModoDescarga(MODO_DESCARGA_TAPA);

			ControladorDescargas controlador = new ControladorDescargas(modeloDescargaTapas, vistaDescargasNotas);

			this.vistaSeleccionarDescarga.setVisible(false);
			this.vistaSeleccionarDescarga.dispose();
			vistaDescargasNotas.getFrame().setVisible(true);
		}
	}

	public void actionListener(ActionListener escuchador) {
		vistaSeleccionarDescarga.getBtnDescargarNotas().addActionListener(escuchador);
		vistaSeleccionarDescarga.getBtnDescargarTapas().addActionListener(escuchador);
	}

}
