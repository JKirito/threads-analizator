package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import servicios.WorkerDownload;
import controlador.ControladorDescargas;
import entities.FormatoSalida;

public abstract class ModeloDescarga {

	private String rutaDestino;
	private String modoDescarga;
	private String diarioDescarga;
	private String seccionDescarga;
	/**
	 * El el modo de descarga de Tapas, representa la cantidad que el usuario elige a descargar.
	 * En el modo de descarga de Notas, representa la cantidad de archivos que hay en la carpeta origen,
	 * 	suponiendo que son todos archivos de las tapas descargadas anteriormente.
	 */
	private int cantTapasDescargar;
	private boolean override;
	private FormatoSalida formatoOutput; // Html / txt
	protected ControladorDescargas controladorDescargas;
	protected boolean isDescargando;
	private WorkerDownload swingWorker;
	protected boolean descargaDetenida;
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

	public int getCantTapasDescargar() {
		return cantTapasDescargar;
	}

	public void setCantTapasDescargar(int cantTapasDescargar) {
		this.cantTapasDescargar = cantTapasDescargar;
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

	public void setDescargando(boolean isDescargando) {
		this.isDescargando = isDescargando;
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

	public abstract void detenerDescarga();

	public abstract int getCantOptimaHilos();

	public void guardarResultadoProceso(String ruta) throws IOException {
		ruta = ruta + ".txt";
		File file = new File(ruta);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			throw new IOException("Ya existe un archivo con ese nombre");
		}
		if (!file.canWrite()) {
			throw new IOException("No tiene permiso para escribir el archivo " + ruta);
		}
		FileWriter outputFile = new FileWriter(file);
		String resultadoProceso = getInformeDescarga();
		outputFile.write(resultadoProceso);
		outputFile.close();
		// Verifico que exista
		if (!file.exists()) {
			throw new IOException("Error al guardar el archivo");
		}

	}

	public abstract String getInformeDescarga();

	public void setControlador(ControladorDescargas controladorDescargas) {
		this.controladorDescargas = controladorDescargas;
	}

	public boolean isDescargando() {
		return this.isDescargando;
	}

	public WorkerDownload getSwingWorker() {
		return swingWorker;
	}

	public void setSwingWorker(WorkerDownload downloader) {
		this.swingWorker = downloader;
	}

	public boolean isDescargaDetenida() {
		return descargaDetenida;
	}
}
