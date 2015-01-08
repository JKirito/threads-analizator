package app;

import vista.VistaDescargas;
import controlador.ControladorDescargas;

public class Init {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				try {
					VistaDescargas vistaDescarga = new VistaDescargas();
					ControladorDescargas controlador = new ControladorDescargas(vistaDescarga);
					vistaDescarga.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

}
