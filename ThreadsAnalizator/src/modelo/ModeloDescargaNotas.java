package modelo;

import entities.FormatoSalida;

public class ModeloDescargaNotas extends ModeloDescarga{

	private static final String MSJ_CARPETAORIGEN_VACIO = "Debe seleccionar la carpeta de origen";
	private String rutaOrigen;


	public ModeloDescargaNotas(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			boolean override, FormatoSalida formatoOutput, String rutaOrigen) {
		super(rutaDestino, modoDescarga, diarioDescarga, seccionDescarga, override, formatoOutput);
		this.rutaOrigen = rutaOrigen;
	}

	public ModeloDescargaNotas() {
		super();
	}

	public String getRutaOrigen() {
		return rutaOrigen;
	}

	public void setRutaOrigen(String rutaOrigen) {
		this.rutaOrigen = rutaOrigen;
	}

	@Override
	public void descargar() {
		System.out.println("DESCARGAR NOTAS!!!");
	}

	@Override
	public int getCantOptimaHilos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String validarDatos() {
		String errores = super.validarDatos();
		if (this.rutaOrigen == null || this.rutaOrigen.isEmpty()) {
			errores += "-" + MSJ_CARPETAORIGEN_VACIO + "\n";
		}
		return errores;
	}

}
