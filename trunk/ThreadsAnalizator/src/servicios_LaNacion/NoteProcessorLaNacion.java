package servicios_LaNacion;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import servicios.LimpiarHtml;
import servicios.NoteProcessor;
import servicios.NotesRecolator;
import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.FormatoTexto;
import entities.Seccion;

public class NoteProcessorLaNacion extends NoteProcessor {

	// TODO: si es el processor de la nacion, no tiene sentido que le pase qué
	// diario es! xD
	public NoteProcessorLaNacion(NotesRecolator recolector, String nombreArchivo, Element elem, String pathAGuardar,
			DiarioDigital diario, Seccion seccion, FormatoSalida formato, boolean override) {
		super(recolector, nombreArchivo, elem, pathAGuardar, diario, seccion, formato, override);
	}

	@Override
	public void run() {
//		this.getRecolector().incrementDescargasARealizar();
		//TODO si esta al ppio avanza demasiado rapido la barra de jprogres!

		String nombreArchivoAGuarduar = getNombreArchivoAGuardar();

		if (!this.isOverride()) {
			if (StoreFile.fileExists(this.getPathAGuardar(), nombreArchivoAGuarduar, this.getFormatoSalida()
					.getExtension())) {
				this.getRecolector().incrementDescargasNoNecesarias();
				return;
			}
		}
		// Verifico que tenga que seguir ejecutandose o detener la ejecución
		// del hilo actual
		if (this.getRecolector().isDetener()) {
			return;
		}

		Document doc = null;
		try {
			doc = Jsoup.connect(this.getElem().attr("href")).timeout(0).get();
			this.getRecolector().incrementDescargasARealizar();
		} catch (UnknownHostException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario " + this.getDiario().getNombreDiario() + ", sección "
							+ this.getSeccion().getNombreSección() + ": " + this.getElem().attr("href")
							+ ". Esto puede deberse a una desconexión de internet.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			return;
		} catch (SocketTimeoutException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario del diario " + this.getDiario().getNombreDiario()
							+ ", sección " + this.getSeccion().getNombreSección() + ": " + this.getElem().attr("href")
							+ ". Error por Time out.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			return;
		} catch (IOException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario del diario " + this.getDiario().getNombreDiario()
							+ ", sección " + this.getSeccion().getNombreSección() + ": " + this.getElem().attr("href")
							+ ". Error desconocido.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			return;
		}

		if (doc == null) {
			this.getRecolector().incrementDescargasFallidas();
			return;
		}

		// Verifico que tenga que seguir ejecutandose o detener la ejecución
		// del hilo actual
		if (this.getRecolector().isDetener()) {
			return;
		}
		// Element encabezado = doc.getElementById("encabezado");
		// Elements titulo = encabezado.getAllElements().select("h1");
		// String nombreArchivo = titulo.text();

		Document nuevoDoc = this.getDiario().getNotaFromDocument(doc);

		if (this.getFormatoSalida() instanceof FormatoHtml) {
			guardarNotaHTML(nuevoDoc, nombreArchivoAGuarduar);
			this.getRecolector().incrementDescargasRealizadas();
		} else if (this.getFormatoSalida() instanceof FormatoTexto) {
			LimpiarHtml limpiador = new LimpiarHtml(this.getPathAGuardar(), this.getDiario());
			limpiador.limpiarDocumentoYGuardar(nuevoDoc, nombreArchivoAGuarduar);
			this.getRecolector().incrementDescargasRealizadas();
		}

	}

	public void guardarNotaHTML(Document doc, String nombreArchivo) {
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, "utf-8");
		try {
			sf.store(this.isOverride());
		} catch (IOException e) {
			this.getRecolector().incrementDescargasFallidas();
			e.printStackTrace();
		}
	}

}
