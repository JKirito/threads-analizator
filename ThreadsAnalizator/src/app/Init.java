package app;

import vista.SeleccionarDescarga;
import controlador.controladorSeleccionarDescarga;

public class Init {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				try {
					SeleccionarDescarga vistaSeleccionarDescarga = new SeleccionarDescarga();
					controladorSeleccionarDescarga controlador = new controladorSeleccionarDescarga(vistaSeleccionarDescarga);

					vistaSeleccionarDescarga.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

}
