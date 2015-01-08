package entities;


public abstract class DiarioDigital {

	protected String LINK;
	protected String charsetName;
	protected String nombrePrefijoAGuardar;

	public abstract String armarLinkActual(String fecha, Seccion seccion);

	public String getLINK() {
		return LINK;
	}

	public void setLINK(String lINK) {
		LINK = lINK;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public String getNombrePrefijoAGuardar() {
		return nombrePrefijoAGuardar;
	}

	public void setNombrePrefijoAGuardar(String nombrePrefijoAGuardar) {
		this.nombrePrefijoAGuardar = nombrePrefijoAGuardar;
	}

	public abstract String getNombreArchivo(String fecha);
}
