package modelo;

import java.io.File;

import servicios.NotesRecolator;
import entities.DiarioDigital;
import entities.EconomiaLaNacion;
import entities.EconomiaPagina12;
import entities.FormatoSalida;
import entities.LaNacion;
import entities.Pagina12;
import entities.Seccion;
import entities.SeccionEconomia;

public class ModeloDescargaNotas extends ModeloDescarga {

	private static final String MSJ_CARPETAORIGEN_VACIO = "Debe seleccionar la carpeta de origen";
	private String rutaOrigen;
	private NotesRecolator noteRecolator;

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
		this.setDescargando(true);
		System.out.println("DESCARGAR NOTAS!!!");
		this.descargaDetenida = false;

		DiarioDigital diario = null;
		Seccion seccion = null; // TODO: necesario para la nota??
		if (this.getDiarioDescarga().equals(Pagina12.NOMBRE_DIARIO)) {
			diario = new Pagina12();
			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
				seccion = new EconomiaPagina12();
			}
		} else if (this.getDiarioDescarga().equals(LaNacion.NOMBRE_DIARIO)) {
			diario = new LaNacion();
			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
				seccion = new EconomiaLaNacion();
			}
		}
		String pathOrigen = this.getRutaOrigen() + File.separatorChar;
		String pathAGuardar = this.getRutaDestino() + File.separatorChar;
		int cantHilos = 20;// TODO: pedir por pantalla!!
		noteRecolator = new NotesRecolator(pathOrigen, pathAGuardar, cantHilos, diario, seccion,
				this.getFormatoOutput(), this.isOverride());
		noteRecolator.addObserver(this.getSwingWorker());
		System.out.println("por iniciar...");
		noteRecolator.iniciar();
		this.setDescargando(false);
		this.controladorDescargas.descargaFinalizada();
		System.out.println("DESCARGA COMPLETAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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

	@Override
	public String getInformeDescarga() {
		// //TODO! esta es el informe de tapas...
		// String informe = "INFORME DESCARGA: "
		// + (this.descargaDetenida ? "La descarga fue detenida.\r\n" :
		// "Descargas Finalizada con éxito.\r\n");
		// informe += "\r\nDESCARGAS REALIZADAS: " +
		// this.pageDownloader.getDescargasRealizadas() + "\r\n";
		// informe += "\r\nDESCARGAS NO NECESARIAS (YA EXISTÍA EL ARCHIVO): "
		// + this.pageDownloader.getDescargasNoNecesarias() + "\r\n";
		// informe += "\r\nDESCARGAS FALLIDAS: " +
		// this.pageDownloader.getDescargasFallidas() + "\r\n";
		// informe += this.pageDownloader.getErroresDescarga().isEmpty() ? "" :
		// "\r\n\r\nInfo Adicional por descargas fallidas: " + "\r\n\r\n"
		// + Utils.stringListToString(this.pageDownloader.getErroresDescarga(),
		// "-") + "\r\n";
		//
		// return informe;
		return "informe por hacer...";
	}

	@Override
	public void detenerDescarga() {
		this.noteRecolator.detenerEjecucion();
		this.descargaDetenida = true;
		this.isDescargando = false;
	}

}
