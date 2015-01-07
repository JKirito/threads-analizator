package modelo;

import java.util.Date;

public class ModeloDescargaTapas extends ModeloDescarga {

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
		// TODO Auto-generated method stub

	}

	@Override
	public int getCantOptimaHilos() {
		// TODO Auto-generated method stub
		return 0;
	}

}
