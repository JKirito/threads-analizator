package entities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Utils.Utils;

public class Pagina12 extends DiarioDigital {

	private static final String LINK_PAGINA12 = "http://www.pagina12.com.ar/diario/";
	private static final String CHARSETNAME_PAGINA12 = "iso-8859-1";
	private static final String NOMBREPREFIJO_AGUARDAR = "Pagina12";
	public static final String NOMBRE_DIARIO = "Página 12";
	public static final String NOMBRE_GRUPO_NOTICIAS = "seccionxblanco";
	private static String classMinirecortesEliminar = "minirecorte";
	private static String classSubNotas = "subnotas_secc";

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

	@Override
	// Formato es yyyy-MM-dd
	public String getFechaConFormato(Date fechaDate) {
		return Utils.dtoYYYY_MM_DD(fechaDate);
	}

	@Override
	public Elements getElementNotasABuscar(File file) {
		Element notasABuscar = null;
		try {
			// notasABuscar = Jsoup.parse(file,
			// this.getCharsetName()).getElementsByClass(this.getNombreGrupoNoticias())
			// .first();
			notasABuscar = Jsoup.parse(file, this.getCharsetName()).body();
		} catch (IOException e) {
			return null;
		}
		if (notasABuscar == null) {
			return null;
		}

		Elements notas = notasABuscar.getElementsByTag("h2").select("[href]");
		// Agrego subnotas en caso de existir
		Elements subnotas = notasABuscar.getElementsByClass(classSubNotas);
		notas.addAll(subnotas.select("[href]"));

		return notas;
	}

	@Override
	public boolean isPagina12() {
		return true;
	}

	@Override
	public boolean isLaNacion() {
		return false;
	}

	@Override
	public Note getNotaProcesadaFromDocument(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getNotaFromDocument(Document doc) {
		if (doc.getElementsByAttributeValue("class", "nota top12") == null) {
			return null;
		}
		Elements nota = doc.getElementsByAttributeValue("class", "nota top12");

		// Eliminar datos innecesarios
		for (int i = 0; i < nota.size(); i++) {
			Element e = nota.get(i);
			if (e.getElementsByClass("botones") != null) {
				e.getElementsByClass("botones").remove();
			}
			if (e.getElementsByClass("pienota") != null) {
				e.getElementsByClass("pienota").remove();
			}
		}

		Document nd = new Document("");
		nd.append(nota.html());

		return nd;
	}

	@Override
	public String getlinkNota(String link) {
		return "http://pagina12.com.ar" + link;
	}
}
