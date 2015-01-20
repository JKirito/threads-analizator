package servicios;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoTexto;
import entities.Note;

public abstract class LimpiarHtml {

	private String pathOrigen;
	private String pathDestino;
	private Document doc;
	private DiarioDigital diario;

	public LimpiarHtml(String pathOrigen, String pathDestino, DiarioDigital diario, Document doc) {
		super();
		this.pathOrigen = pathOrigen;
		this.pathDestino = pathDestino;
		this.diario = diario;
		this.doc = doc;
	}

	public void limpiarArchivos() throws IOException {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		long init = new Date().getTime();
		assert (carpeta.isDirectory());
		int i = 1;
		// Recorrer cada archivo de la carpeta
		for (String archivo : carpeta.list()) {
			File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
			if (file.isFile()) {

				if (i % 11 == 0) {
					System.exit(0);
				}

				if (i % 250 == 0) {
					long now = new Date().getTime();
					System.out.println("Archivos procesados hasta el momento: '" + i + "' en " + (now - init) / 1000
							+ " segs.");
				}
				i++;

				Document doc = Jsoup.parse(file, "utf-8");
				if (doc == null) {
					System.out.println();
					System.out.println("DOC NULL: " + file.getName());
					continue;
				}

				Note nota = limpiarDocument();

				// long inicioGuardarUnaNota = new Date().getTime();
				guardarNota(nota, archivo);
				// long tardoEnGuardarUnaNota = new Date().getTime() -
				// inicioGuardarUnaNota;
			}
		}
	}

	public Note limpiarDocument() {
		Note nota = getNotaFromDocument(doc);
		return nota;
	}

	public boolean limpiarDocumentoYGuardar(Document doc, String nombreArchivo) {
		return guardarNota(getNotaFromDocument(doc), nombreArchivo);
	}

	public static Note getNotaFromDocument(Document doc) {
		if (doc.getElementById("encabezado") == null) {
			System.out.println("No tiene encabezado");
			// logger.error("Fail to process file {}");
			return null;
		}
		Element encabezado = doc.getElementById("encabezado");
		// Elements firma = encabezado.getElementsByAttributeValue("class",
		// "firma");
		encabezado.getElementsByClass("firma").remove();
		encabezado.getElementsByClass("bajada").remove();
		Elements volanta = encabezado.getElementsByAttributeValue("class", "volanta");
		Elements titulo = encabezado.getAllElements().select("h1");
		Elements descripcion = encabezado.getAllElements().select("p");
		descripcion.removeAll(volanta);
		Element cuerpo = doc.getElementById("cuerpo");
		Elements archRel = cuerpo.getElementsByAttributeValue("class", "archivos-relacionados");
		Elements fin = cuerpo.getElementsByAttributeValue("class", "fin");

		return new Note(volanta.text(), titulo.text(), descripcion.text(), cuerpo.text().replace(archRel.text(), "")
				.replace(fin.text(), ""), "", null);
	}

	/**
	 * Devuelve true en caso que se haya guardado la nota correctamente
	 *
	 * @param nota
	 * @param archivo
	 *            nombre del archivo a guardar
	 * @return
	 */
	public boolean guardarNota(Note nota, String archivo) {
		String nombreArchivo = archivo.replace(".html", "");
		if (nombreArchivo.contains("/")) {
			nombreArchivo = nombreArchivo.replace("/", "-");
		}
		StoreFile sf = new StoreFile(pathDestino, FormatoTexto.EXTENSION, nota.toString(), nombreArchivo,
				diario.getCharsetName());
		try {
			sf.store(true);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
