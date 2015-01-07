package modelo;


public class ModeloDescargaNotas extends ModeloDescarga{

	private String rutaOrigen;

	public ModeloDescargaNotas(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			String rutaOrigen) {
		super(rutaDestino, modoDescarga, diarioDescarga, seccionDescarga);
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
		// TODO Auto-generated method stub

	}

	@Override
	public int getCantOptimaHilos() {
		// TODO Auto-generated method stub
		return 0;
	}

}
