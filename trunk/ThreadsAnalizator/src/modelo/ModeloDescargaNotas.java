package modelo;

import java.io.File;

import servicios.NotesRecolator;
import Utils.Utils;
import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Seccion;

public class ModeloDescargaNotas extends ModeloDescarga {

	private static final String MSJ_CARPETAORIGEN_VACIO = "Debe seleccionar la carpeta de origen";
	private String rutaOrigen;
	private NotesRecolator noteRecolator;

	public ModeloDescargaNotas(String rutaDestino, String modoDescarga, DiarioDigital diarioDescarga, Seccion seccionDescarga,
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

//		DiarioDigital diario = null;
//		Seccion seccion = null; // TODO: necesario para la nota??
//		if (this.getDiarioDescarga().equals(Pagina12.NOMBRE_DIARIO)) {
//			diario = new Pagina12();
//			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
//				seccion = new EconomiaPagina12();
//			}
//		} else if (this.getDiarioDescarga().equals(LaNacion.NOMBRE_DIARIO)) {
//			diario = new LaNacion();
//			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
//				seccion = new EconomiaLaNacion();
//			}
//		}
		String pathOrigen = this.getRutaOrigen() + File.separatorChar;
		String pathAGuardar = this.getRutaDestino() + File.separatorChar;
		int cantHilos = 20;// TODO: pedir por pantalla!!
		noteRecolator = new NotesRecolator(pathOrigen, pathAGuardar, cantHilos, this.getDiarioDescarga(), this.getSeccionDescarga(),
				this.getFormatoOutput(), this.isOverride());
		noteRecolator.addObserver(this.getSwingWorker());
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
			errores += "-" + MSJ_CARPETAORIGEN_VACIO + "\r\n";
		}
		return errores;
	}

	@Override
	public String getInformeDescarga() {
		// TODO! este es el informe de Notas...
		String informe = "INFORME DESCARGA: "
				+ (this.descargaDetenida ? "La descarga fue detenida.\r\n" : "Descargas Finalizada con éxito.\r\n");
		informe += "\r\nDESCARGAS REALIZADAS: " + this.noteRecolator.getDescargasRealizadas() + "\r\n";
		informe += "\r\nDESCARGAS NO NECESARIAS (YA EXISTÍA EL ARCHIVO): "
				+ this.noteRecolator.getDescargasNoNecesarias() + "\r\n";
		informe += "\r\nDESCARGAS FALLIDAS: " + this.noteRecolator.getDescargasFallidas() + "\r\n";
		informe += this.noteRecolator.getErroresDescarga().isEmpty() ? ""
				: "\r\n\r\nInfo Adicional por descargas fallidas: " + "\r\n\r\n"
						+ Utils.stringListToString(this.noteRecolator.getErroresDescarga(), "-") + "\r\n";

		return informe;
	}

	@Override
	public void detenerDescarga() {
		this.noteRecolator.detenerEjecucion();
		this.descargaDetenida = true;
		this.isDescargando = false;
	}

}
