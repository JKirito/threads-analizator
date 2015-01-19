package servicios_Pagina12;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import servicios.NoteProcessor;
import Utils.StoreFile;
import entities.DiarioDigital;

public class NoteProcessorPagina12 extends NoteProcessor {

	public NoteProcessorPagina12(String archivo, Element elem, String pathAGuardar, DiarioDigital diario) {
		super(archivo, elem, pathAGuardar, diario);
	}

	@Override
	public void run() {
		Document doc = null;
		try {
			String url = "http://pagina12.com.ar" + this.getElem().attr("href");
			doc = Jsoup.connect(url).timeout(0).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			return;
		}
		Element encabezado = doc.getElementsByAttributeValue("class", "nota top12").first();
		Elements titulo = encabezado.getAllElements().select("h2");
		String nombreArchivo = titulo.text();

		guardarNota(doc, nombreArchivo);

	}

	public void guardarNota(Document doc, String titulo) {
		String nombreArchivo = this.getNombreArchivoAParsear().replace(".html", "_" + titulo);
		if (nombreArchivo.contains("/"))
			nombreArchivo = nombreArchivo.replace("/", "-");
		if (nombreArchivo.contains(";"))
			nombreArchivo = nombreArchivo.replaceAll(";", ",");
		// TODO! si contiene ciertos caracteres,guarda caracteres "raros" en el
		// nombrearchivo
		// "temporalmente", si tiene estos caracteres, los saco
		if (nombreArchivo.contains("")) {
			nombreArchivo = nombreArchivo.replaceAll("", "");
		}
		if (nombreArchivo.contains("")) {
			nombreArchivo = nombreArchivo.replaceAll("", "");
		}
		// File a = new File(this.getPathAGuardar() + nombreArchivo +
		// ".html//");
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, "iso-8859-1");
		try {
			sf.store(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
