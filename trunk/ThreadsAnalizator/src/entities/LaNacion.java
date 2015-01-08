package entities;



public class LaNacion extends DiarioDigital{

	private static final String LINK_LANACION = "http://servicios.lanacion.com.ar/archivo-f";// 04/09/2014-c30"
	private static final String CHARSETNAME_LANACION = "utf-8";
	private static final String NOMBREPREFIJO_AGUARDAR = "LaNacion";


	public LaNacion() {
		super.setCharsetName(CHARSETNAME_LANACION);
		super.setLINK(LINK_LANACION);
		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
	}

	@Override
	public String armarLinkActual(String fecha, Seccion seccion) {
		return LINK_LANACION + fecha + seccion.getNombreSeccion() + ".html";
	}

	@Override
	public String getNombreArchivo(String fecha) {
		return this.nombrePrefijoAGuardar + "_" + (fecha.contains("//") ? fecha.replace("/", "-"): "");
	}
}
