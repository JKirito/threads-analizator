package modelo;

import java.util.Date;

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

	public ModeloDescargaTapas(){
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
			if(this.fechaDescargaHasta.after(new Date()))
			errores += "-" + MSJ_FECHA_FUTURO + "\n";
		}
		return errores;
	}

}
