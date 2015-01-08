package modelo;

import java.io.File;
import java.util.Date;

import servicios.PageDownloader;
import entities.DiarioDigital;
import entities.EconomiaLN;
import entities.EconomiaP12;
import entities.LaNacion;
import entities.Pagina12;
import entities.Seccion;

public class ModeloDescargaTapas extends ModeloDescarga {

	private static final String MSJ_FECHA_VACIO = "Debe completar la fecha hasta donde descargar las tapas";
	private static final String MSJ_FECHA_FUTURO = "La fecha máxima es la de hoy, no puedes descargar lo que no existe!";
	private Date fechaDescargaHasta;
	private int cantTapasDescargar;

	public ModeloDescargaTapas(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			Date fechaDescargaHasta, int cantTapasDescargar) {
		super(rutaDestino, modoDescarga, diarioDescarga, seccionDescarga);
		this.fechaDescargaHasta = fechaDescargaHasta;
		this.cantTapasDescargar = cantTapasDescargar;
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

	public int getCantTapasDescargar() {
		return cantTapasDescargar;
	}

	public void setCantTapasDescargar(int cantTapasDescargar) {
		this.cantTapasDescargar = cantTapasDescargar;
	}

	@Override
	public void descargar() {
		System.out.println("Descargar TAPAS!!!!!!!!!!!!!!");
		DiarioDigital diario = null;
		Seccion seccion = null;
		if (this.getDiarioDescarga().equals(Pagina12.NOMBRE_DIARIO)) {
			diario = new Pagina12();
			if (this.getSeccionDescarga().equals(EconomiaP12.nombreSeccion)) {
				seccion = new EconomiaP12();
			}
		} else if (this.getDiarioDescarga().equals(LaNacion.NOMBRE_DIARIO)) {
			diario = new LaNacion();
			if (this.getSeccionDescarga().equals(EconomiaLN.nombreSeccion)) {
				seccion = new EconomiaLN();
			}
		}

		String pathAGuardar = this.getRutaDestino()+File.separatorChar;
		Date fechaHasta = this.getFechaDescargaHasta();
		int diasARecopílar = this.getCantTapasDescargar();
		PageDownloader pd = new PageDownloader(diario, seccion, pathAGuardar, fechaHasta, diasARecopílar);
		pd.download();
		System.out.println(pd.getErroresDescarga()+"AAAAAAAAAAAAAAAAAAAa");
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

}
