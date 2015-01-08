package entities;


public class Pagina12 extends DiarioDigital {

	private static final String LINK_PAGINA12 = "http://www.pagina12.com.ar/diario/";
	private static final String CHARSETNAME_PAGINA12 = "iso-8859-1";
	private static final String NOMBREPREFIJO_AGUARDAR = "Pagina12";
	public static final String NOMBRE_DIARIO = "Página 12";

	public Pagina12() {
		super.setCharsetName(CHARSETNAME_PAGINA12);
		super.setLINK(LINK_PAGINA12);
		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
	}

	@Override
	public String armarLinkActual(String fecha, Seccion seccion) {
		return LINK_PAGINA12 + seccion.getNombreSeccion() + fecha + ".html";
	}

	@Override
	public String getNombreArchivo(String fecha) {
		return this.nombrePrefijoAGuardar + "_" + fecha;
	}
}
