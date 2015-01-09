package entities;

import org.jsoup.nodes.Document;



public class LaNacion extends DiarioDigital{

	private static final String LINK_LANACION = "http://servicios.lanacion.com.ar/archivo-f";// 04/09/2014-c30"
	private static final String CHARSETNAME_LANACION = "utf-8";
	private static final String NOMBREPREFIJO_AGUARDAR = "LaNacion";
	public static final String NOMBRE_DIARIO = "La Nación";
	public static final String GRUPO_NOTICIAS = "archivo-notas-272";

	public LaNacion() {
		super.setCharsetName(CHARSETNAME_LANACION);
		super.setLINK(LINK_LANACION);
		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
		super.setGrupoNoticias(GRUPO_NOTICIAS);
		super.setNombreDiario(NOMBRE_DIARIO);
	}

	@Override
	public String armarLinkActual(String fecha, Seccion seccion) {
		return LINK_LANACION + fecha + seccion.getCodigoSeccion() + ".html";
	}

	@Override
	public String getNombreArchivo(String fecha) {
		return this.nombrePrefijoAGuardar + "_" + (fecha.contains("//") ? fecha.replace("/", "-"): "");
	}

	@Override
	public boolean esValido(Document doc) {
		return doc.getElementById(this.getGrupoNoticias()) != null;
	}
}