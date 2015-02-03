package servicios;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoTexto;
import entities.Note;

public class LimpiarHtml {

	private String pathDestino;
	private DiarioDigital diario;
	private boolean override;

	public LimpiarHtml(String pathDestino, DiarioDigital diario, boolean override) {
		super();
		this.pathDestino = pathDestino;
		this.diario = diario;
		this.override = override;
	}

	public void limpiarArchivos(String pathOrigen) throws IOException {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		assert (carpeta.isDirectory());
		// Recorrer cada archivo de la carpeta
		for (String archivo : carpeta.list()) {
			File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
			if (file.isFile()) {


				Document doc = Jsoup.parse(file, diario.getCharsetName());
				if (doc == null) {
					System.out.println();
					System.out.println("DOC NULL: " + file.getName());
					continue;
				}

				Note nota = diario.getNotaProcesadaFromDocument(doc);

				guardarNota(nota, archivo);
			}
		}
	}

	public boolean limpiarDocumentoYGuardar(Document doc, String nombreArchivo) {
		return guardarNota(diario.getNotaProcesadaFromDocument(doc), nombreArchivo);
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
			sf.store(this.override);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
