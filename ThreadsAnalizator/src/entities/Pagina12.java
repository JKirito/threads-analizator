package entities;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Utils.Utils;


public class Pagina12 extends DiarioDigital {

	private static final String LINK_PAGINA12 = "http://www.pagina12.com.ar/diario/";
	private static final String CHARSETNAME_PAGINA12 = "iso-8859-1";
	private static final String NOMBREPREFIJO_AGUARDAR = "Pagina12";
	public static final String NOMBRE_DIARIO = "Página 12";
	public static final String NOMBRE_GRUPO_NOTICIAS = "seccionxblanco";
	private static String classMinirecortesEliminar = "minirecorte";

	public Pagina12() {
		super.setCharsetName(CHARSETNAME_PAGINA12);
		super.setLINK(LINK_PAGINA12);
		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
		super.setNombreGrupoNoticias(NOMBRE_GRUPO_NOTICIAS);
		super.setNombreDiario(NOMBRE_DIARIO);
	}

	@Override
	public String armarLinkActual(String fecha, Seccion seccion) {
		return LINK_PAGINA12 + seccion.getCodigoSeccion() + fecha + ".html";
	}

	@Override
	public String getNombreArchivo(String fecha) {
		return this.nombrePrefijoAGuardar + "_" + fecha;
	}

	@Override
	public boolean esValido(Document doc) {
		Element noticias = doc.getElementsByClass(this.getNombreGrupoNoticias()).first();
		return noticias != null && noticias.getAllElements().size() > 1;
	}

	@Override
	public Element getSoloGrupoNoticias(Document page) {
		// Elimino "basura"
		page.getElementsByClass(classMinirecortesEliminar).remove();
		// Dejo sólo lo que me importa
		return page.getElementsByClass(this.getNombreGrupoNoticias()).first();
	}

	@Override // Formato es yyyy-MM-dd
	public String getFechaConFormato(Date fechaDate) {
		return Utils.dtoYYYY_MM_DD(fechaDate);
	}
}
