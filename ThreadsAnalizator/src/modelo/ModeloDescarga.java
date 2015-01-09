package modelo;

import entities.FormatoSalida;


public abstract class ModeloDescarga {

	private String rutaDestino;
	private String modoDescarga;
	private String diarioDescarga;
	private String seccionDescarga;
	private boolean override;
	private FormatoSalida formatoOutput; // Html / txt
	private static final String MSJ_DIARIO_DESCARGA_VACIO = "Debe seleccionar el diario del cual desea descargar";
	private static final String MSJ_CARPETADESTINO_VACIO = "Debe seleccionar la carpeta de destino";
	private static final String MSJ_SECCION_DESCARGA_VACIO = "Debe seleccionar la sección que desea descargar";
	private static final String MSJ_FORMATO_OUTPUT_VACIO = "Debe seleccionar el formato de salida que desea descargar";

	public ModeloDescarga() {
		super();
	}

	public ModeloDescarga(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			boolean override, FormatoSalida formatoOutput) {
		super();
		this.rutaDestino = rutaDestino;
		this.modoDescarga = modoDescarga;
		this.diarioDescarga = diarioDescarga;
		this.seccionDescarga = seccionDescarga;
		this.override = override;
		this.formatoOutput = formatoOutput;
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

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public FormatoSalida getFormatoOutput() {
		return formatoOutput;
	}

	public void setFormatoOutput(FormatoSalida formatoOutput) {
		this.formatoOutput = formatoOutput;
	}

	public String validarDatos() {
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
		if (this.getFormatoOutput() == null) {
			errores += "-" + MSJ_FORMATO_OUTPUT_VACIO + "\n";
		}
		return errores;
	}

	public abstract void descargar();

	public abstract int getCantOptimaHilos();
}
