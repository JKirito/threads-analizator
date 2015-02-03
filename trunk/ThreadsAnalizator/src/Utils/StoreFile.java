package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class StoreFile {
	private String path;
	private String extension;
	private String textoAGuardar;
	private String nombreArchivo;
	private String charset;

	/**
	 *
	 * @param path
	 *            : ruta donde se va a guardar el archivo
	 * @param extension
	 *            : puede ser vacío [""]. En caso de agregarlo, agregar el punto
	 *            '.'
	 * @param textoAGuardar
	 *            : Cadena de texto que se desea guardar en disco
	 * @param nombreArchivo
	 *            : nombre con el cual se guardará el archivo (SIN extensión)
	 * @param charset
	 *            : formato charset con el que se guardará el archivo. Ej:
	 *            "utf-8"
	 */
	public StoreFile(String path, String extension, String textoAGuardar, String nombreArchivo, String charset) {
		super();
		this.path = path;
		this.extension = extension;
		this.textoAGuardar = textoAGuardar;
		this.nombreArchivo = nombreArchivo;
		this.charset = charset;
	}

	/**
	 *
	 * @param override
	 *            : si true, sobreescribe el archivo a guardar (en caso de
	 *            existir)
	 * @throws IOException
	 */
	public void store(boolean override) throws IOException {
		if (override || !(new File(this.path + this.nombreArchivo + this.extension).exists())) {
			store();
		}
	}

	private void store() throws IOException {
		//TODO: Por ahora necesario para no mostrar estos caracteres "raros" en el text de página 12
		if(this.textoAGuardar.contains("")){
			this.textoAGuardar = this.textoAGuardar.replaceAll("", "\"");
		}
		if(this.textoAGuardar.contains("")){
			this.textoAGuardar = this.textoAGuardar.replaceAll("", "\"");
		}

		if(this.textoAGuardar.contains("")){
			this.textoAGuardar = this.textoAGuardar.replaceAll("", "'");
		}
		if(this.textoAGuardar.contains("")){
			this.textoAGuardar = this.textoAGuardar.replaceAll("", "'");
		}

		if(this.textoAGuardar.contains("")){
			this.textoAGuardar = this.textoAGuardar.replaceAll("", "-");
		}


		File page = new File(this.path + this.nombreArchivo + this.extension);
//		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(page), this.charset));
		Writer out = new FileWriter(page);
		try {
			out.write(this.textoAGuardar);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	public static boolean fileExists(String ruta, String nombreArchivo, String extension) {
		return new File(ruta + nombreArchivo + extension).exists();
	}
}
