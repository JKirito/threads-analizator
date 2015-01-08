package modelo;

public abstract class ModeloDescarga {

	private String rutaDestino;
	private String modoDescarga;
	private String diarioDescarga;
	private String seccionDescarga;
	private static final String MSJ_DIARIO_DESCARGA_VACIO = "Debe seleccionar el diario del cual desea descargar";
	private static final String MSJ_CARPETADESTINO_VACIO = "Debe seleccionar la carpeta de destino";
	private static final String MSJ_SECCION_DESCARGA_VACIO = "Debe seleccionar la sección que desea descargar";

	public ModeloDescarga() {
		super();
	}

	public ModeloDescarga(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga) {
		this.modoDescarga = modoDescarga;
		this.diarioDescarga = diarioDescarga;
		this.seccionDescarga = seccionDescarga;
		this.rutaDestino = rutaDestino;
	}

	public String getRutaDestino() {
		return rutaDestino;
	}

	public void setRutaDestino(String rutaDestino) {
		this.rutaDestino = rutaDestino;
	}

	public String getModoDescarga() {
		return modoDescarga;
	}

	public void setModoDescarga(String modoDescarga) {
		this.modoDescarga = modoDescarga;
	}

	public String getDiarioDescarga() {
		return diarioDescarga;
	}

	public void setDiarioDescarga(String diarioDescarga) {
		this.diarioDescarga = diarioDescarga;
	}

	public String getSeccionDescarga() {
		return seccionDescarga;
	}

	public void setSeccionDescarga(String seccionDescarga) {
		this.seccionDescarga = seccionDescarga;
	}

	public String validarDatos(){
		String errores = "";
		if (this.getDiarioDescarga() == null || this.getDiarioDescarga().isEmpty()) {
			errores += "-" + MSJ_DIARIO_DESCARGA_VACIO + "\n";
		}
		if (this.getRutaDestino() == null || this.getRutaDestino().isEmpty()) {
			errores += "-" + MSJ_CARPETADESTINO_VACIO + "\n";
		}
		if (this.getSeccionDescarga() == null || this.getSeccionDescarga().isEmpty()) {
			errores += "-" + MSJ_SECCION_DESCARGA_VACIO + "\n";
		}
		return errores;
	}

	public abstract void descargar();

	public abstract int getCantOptimaHilos();
}
