package servicios;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoTexto;
import entities.Note;

public class LimpiarHtml {

	private String pathDestino;
	private DiarioDigital diario;

	public LimpiarHtml(String pathDestino, DiarioDigital diario) {
		super();
		this.pathDestino = pathDestino;
		this.diario = diario;
	}

	public void limpiarArchivos(String pathOrigen) throws IOException {

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

				Document doc = Jsoup.parse(file, diario.getCharsetName());
				if (doc == null) {
					System.out.println();
					System.out.println("DOC NULL: " + file.getName());
					continue;
				}

				Note nota = diario.getNotaFromDocument(doc);

				guardarNota(nota, archivo);
			}
		}
	}

	public boolean limpiarDocumentoYGuardar(Document doc, String nombreArchivo) {
		return guardarNota(diario.getNotaFromDocument(doc), nombreArchivo);
	}

	/**
	 * Devuelve true en caso que se haya guardado la nota correctamente
	 *
	 * @param nota
	 * @param archivo
	 *            nombre del archivo a guardar
	 * @return
	 */
	private boolean guardarNota(Note nota, String archivo) {
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
