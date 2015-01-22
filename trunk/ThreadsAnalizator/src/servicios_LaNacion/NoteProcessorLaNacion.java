package servicios_LaNacion;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import servicios.LimpiarHtml;
import servicios.NoteProcessor;
import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.FormatoTexto;

public class NoteProcessorLaNacion extends NoteProcessor {

	public NoteProcessorLaNacion(String archivo, Element elem, String pathAGuardar, DiarioDigital diario,
			FormatoSalida formato) {
		super(archivo, elem, pathAGuardar, diario, formato);
	}

	@Override
	public void run() {
		Document doc = null;
		try {
			doc = Jsoup.connect(this.getElem().attr("href")).timeout(0).get();
		} catch (IOException e) {
			run();
		}
		System.out.println("haoaksdlaskdjlaksjd");
		System.out.println(doc.text());
		System.out.println("AAAAAAAAAAAAAAAAA");
		if (doc == null) {
			return;
		}
		Element encabezado = doc.getElementById("encabezado");
		Elements titulo = encabezado.getAllElements().select("h1");
		String nombreArchivo = titulo.text();

		if (this.getFormatoSalida() instanceof FormatoHtml) {
			guardarNotaHTML(doc, nombreArchivo);
		}

		if (this.getFormatoSalida() instanceof FormatoTexto) {
			LimpiarHtml limpiador = new LimpiarHtml(this.getPathAGuardar(), this.getDiario());
			limpiador.limpiarDocumentoYGuardar(doc, nombreArchivo);
			return;
		}

	}

	public void guardarNotaHTML(Document doc, String titulo) {
		String nombreArchivo = this.getNombreArchivoAParsear().replace(".html", "_" + titulo);
		if (nombreArchivo.contains("/"))
			nombreArchivo = nombreArchivo.replace("/", "-");
		if (nombreArchivo.contains(";"))
			nombreArchivo = nombreArchivo.replace(";", ",");
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, "utf-8");
		try {
			sf.store(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
