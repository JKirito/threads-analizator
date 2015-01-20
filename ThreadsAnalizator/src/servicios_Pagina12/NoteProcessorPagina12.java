package servicios_Pagina12;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import servicios.NoteProcessor;
import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.FormatoTexto;
import entities.Note;

public class NoteProcessorPagina12 extends NoteProcessor {

	public NoteProcessorPagina12(String archivo, Element elem, String pathAGuardar, DiarioDigital diario, FormatoSalida formato) {
		super(archivo, elem, pathAGuardar, diario, formato);
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

		if(this.getFormatoSalida() instanceof FormatoTexto){
//			LimpiarHtml limpiador = new LimpiarHtml();//TODO agregar limpiador por diario
		}
		Element encabezado = doc.getElementsByAttributeValue("class", "nota top12").first();
		Elements titulo = encabezado.getAllElements().select("h2");
		String nombreArchivo = titulo.text();

		guardarNotaHTML(doc, nombreArchivo);

	}

	public void guardarNotaHTML(Document doc, String titulo) {
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
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, this.getDiario().getCharsetName());
		try {
			sf.store(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void guardarNotaTXT(Note nota, String archivo, String pathAGuardar) {
		// TODO Auto-generated method stub

	}

}
