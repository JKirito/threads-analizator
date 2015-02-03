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
		Elements volanta = doc.getElementsByClass("volanta");
		// Eliminar datos innecesarios
		for (int i = 0; i < volanta.size(); i++) {
			Element e = volanta.get(i);
			if (e.getElementsByClass("cprincipal") != null) {
				e.getElementsByClass("cprincipal").remove();
			}
			if (e.getElementsByClass("autor") != null) {
				e.getElementsByClass("autor").remove();
			}
		}
		Elements titulo = doc.getAllElements().select("h2");
		Elements descripcion = doc.getElementsByClass("intro");
		Element cuerpo = doc.getElementById("cuerpo");

		if (cuerpo.getElementsByClass("margen0") != null) {
			if (cuerpo.getElementsByClass("margen0").text().length() < 70) {
				cuerpo.getElementsByClass("margen0").remove();
			}
		}
		if (cuerpo.getElementsByTag("a") != null) {
			cuerpo.getElementsByTag("a").remove();
		}
		if (cuerpo.getElementsByClass("autor") != null) {
			cuerpo.getElementsByClass("autor").remove();
		}

		return new Note(volanta.text().replace("›", ""), titulo.text(), descripcion.text(), cuerpo.text(), "", null);
	}

	@Override
	public Document getNotaPreProcesadaFromDocument(Document doc) {
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
		nd.append(doc.head().html()).append(nota.html());
		return nd;
	}

	@Override
	public String getlinkNota(String link) {
		return "http://pagina12.com.ar" + link;
	}
}
