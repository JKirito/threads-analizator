package modelo;

import java.io.File;
import java.util.Date;

import servicios.PageDownloader;
import Utils.Utils;
import entities.DiarioDigital;
import entities.EconomiaLaNacion;
import entities.EconomiaPagina12;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.LaNacion;
import entities.Pagina12;
import entities.Seccion;
import entities.SeccionEconomia;

public class ModeloDescargaTapas extends ModeloDescarga {

	private static final String MSJ_FECHA_VACIO = "Debe completar la fecha hasta donde descargar las tapas";
	private static final String MSJ_FECHA_FUTURO = "La fecha máxima es la de hoy, no puedes descargar lo que no existe!";
	private Date fechaDescargaHasta;
	private PageDownloader pageDownloader;

	public ModeloDescargaTapas(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			boolean override, FormatoSalida formatoOutput, Date fechaDescargaHasta, int cantTapasDescargar) {
		super(rutaDestino, modoDescarga, diarioDescarga, seccionDescarga, override, formatoOutput);
		this.fechaDescargaHasta = fechaDescargaHasta;
	}

	public ModeloDescargaTapas() {
		super();
	}

	public Date getFechaDescargaHasta() {
		return fechaDescargaHasta;
	}

	public void setFechaDescargaHasta(Date fechaDescargaHasta) {
		this.fechaDescargaHasta = fechaDescargaHasta;
	}

	@Override
	public void descargar() {
		this.setDescargando(true);
		this.descargaDetenida = false;
		System.out.println("Descargar TAPAS!!!!!!!!!!!!!!");
		DiarioDigital diario = null;
		Seccion seccion = null;
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

		String pathAGuardar = this.getRutaDestino() + File.separatorChar;
		Date fechaHasta = this.getFechaDescargaHasta();
		int diasARecopílar = this.getCantTapasDescargar();
		FormatoSalida formatoSalida = new FormatoHtml();
		this.pageDownloader = new PageDownloader(diario, seccion, formatoSalida, pathAGuardar, fechaHasta,
				diasARecopílar, this.isOverride());
		this.pageDownloader.addObserver(this.getSwingWorker());
		this.pageDownloader.download();
		this.setDescargando(false);
		this.controladorDescargas.descargaFinalizada();
	}

	@Override
	public int getCantOptimaHilos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String validarDatos() {
		String errores = super.validarDatos();
		if (this.fechaDescargaHasta == null) {
			errores += "-" + MSJ_FECHA_VACIO + "\n";
		}
		if (this.fechaDescargaHasta != null) {
			if (this.fechaDescargaHasta.after(new Date()))
				errores += "-" + MSJ_FECHA_FUTURO + "\n";
		}
		return errores;
	}

	@Override
	public String getInformeDescarga() {
		String informe = "INFORME DESCARGA: "
				+ (this.descargaDetenida ? "La descarga fue detenida.\r\n" : "Descargas Finalizada con éxito.\r\n");
		informe += "\r\nDESCARGAS REALIZADAS: " + this.pageDownloader.getDescargasRealizadas() + "\r\n";
		informe += "\r\nDESCARGAS NO NECESARIAS (YA EXISTÍA EL ARCHIVO): "
				+ this.pageDownloader.getDescargasNoNecesarias() + "\r\n";
		informe += "\r\nDESCARGAS FALLIDAS: " + this.pageDownloader.getDescargasFallidas() + "\r\n";
		informe += this.pageDownloader.getErroresDescarga().isEmpty() ? ""
				: "\r\n\r\nInfo Adicional por descargas fallidas: " + "\r\n\r\n"
						+ Utils.stringListToString(this.pageDownloader.getErroresDescarga(), "-") + "\r\n";

		return informe;
	}

	@Override
	public void detenerDescarga() {
		this.pageDownloader.detenerEjecucion();
		this.descargaDetenida = true;
		this.isDescargando = false;
	}

}
