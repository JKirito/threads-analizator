package app;

import vista.VistaResultadoDescarga;
import vista.VistaDescargas;
import controlador.ControladorDescargas;

public class Init {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				try {
					VistaDescargas vistaDescarga = new VistaDescargas();
					VistaResultadoDescarga resultadoDescarga = new VistaResultadoDescarga();
					ControladorDescargas controlador = new ControladorDescargas(vistaDescarga, resultadoDescarga);
					vistaDescarga.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

}
