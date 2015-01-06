package lanacion;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import servicios.StoreFile;

public class NoteProcessor implements Runnable {

	private final static Logger logger = LogManager.getLogger(NoteProcessor.class.getName());
	private Element elem;
	private String archivo;
	private String pathAGuardar;
	ExecutorService pool;

	public NoteProcessor(String archivo, Element elem, String pathAGuardar) {
		super();
		this.archivo = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
	}

	@Override
	public void run() {
		long inicioDescargarUnaNota = new Date().getTime();
		Document doc = null;
		try {
			doc = Jsoup.connect(elem.attr("href")).timeout(0).get();
		} catch (IOException e) {
			run();
		}
		long tardoEnDescargarUnaNota = new Date().getTime() - inicioDescargarUnaNota;
		if (doc == null) {
			return;
		}
		long inicioParsearUnaNota = new Date().getTime();
		Element encabezado = doc.getElementById("encabezado");
		Elements titulo = encabezado.getAllElements().select("h1");
		String nombreArchivo = titulo.text();
		long tardoEnParsearUnaNota = new Date().getTime() - inicioParsearUnaNota;

		guardarNota(doc, nombreArchivo);

		logger.info(archivo.substring(0, archivo.indexOf(".")) + ";  " + nombreArchivo + "; " + tardoEnParsearUnaNota
				+ "; " + tardoEnDescargarUnaNota);
	}

//	public Note getNotaFromDocument(Document doc) {
//		if (doc.getElementById("encabezado") == null) {
////			logger.error("Fail to process file {}");
//			return null;
//		}
//		Element encabezado = doc.getElementById("encabezado");
//		// Elements firma = encabezado.getElementsByAttributeValue("class",
//		// "firma");
//		encabezado.getElementsByClass("firma").remove();
//		encabezado.getElementsByClass("bajada").remove();
//		Elements volanta = encabezado.getElementsByAttributeValue("class", "volanta");
//		Elements titulo = encabezado.getAllElements().select("h1");
//		Elements descripcion = encabezado.getAllElements().select("p");
//		descripcion.removeAll(volanta);
//		Element cuerpo = doc.getElementById("cuerpo");
//		Elements archRel = cuerpo.getElementsByAttributeValue("class", "archivos-relacionados");
//		Elements fin = cuerpo.getElementsByAttributeValue("class", "fin");
//
//		return new Note(volanta.text(), titulo.text(), descripcion.text(), cuerpo.text().replace(archRel.text(), "")
//				.replace(fin.text(), ""), "", null);
//	}

	public void guardarNota(Document doc, String titulo) {
		String nombreArchivo = archivo.replace(".html", "_" + titulo);
		if(nombreArchivo.contains("/"))
			nombreArchivo = nombreArchivo.replace("/", "-");
		if(nombreArchivo.contains(";"))
			nombreArchivo = nombreArchivo.replace(";", ",");
		File a = new File(pathAGuardar+nombreArchivo+".html//");
		StoreFile sf = new StoreFile(pathAGuardar, ".html", doc.html(), nombreArchivo, "utf-8");
		try {
			sf.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
