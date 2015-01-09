package entities;

public abstract class Seccion {
	private String codigoSeccion;
	// private String nombreDiario;
	private String nombreSección;

	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(String nombreSeccion) {
		this.codigoSeccion = nombreSeccion;
	}

	public String getNombreSección() {
		return nombreSección;
	}

	public void setNombreSección(String nombreSección) {
		this.nombreSección = nombreSección;
	}
}
