package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.FormatoTexto;
import entities.Seccion;

public class NoteProcessor implements Runnable {

	private Element elem;
	private String nombreArchivoAParsear;
	private String pathAGuardar;
	private volatile DiarioDigital diario;
	private FormatoSalida formatoSalida;
	private NotesRecolator recolector;
	private boolean override;
	private Seccion seccion;

	public NoteProcessor(NotesRecolator recolector, String archivo, Element elem, String pathAGuardar,
			DiarioDigital diario, Seccion seccion, FormatoSalida formato, boolean override) {
		super();
		this.nombreArchivoAParsear = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formato;
		this.recolector = recolector;
		this.override = override;
	}

	public Element getElem() {
		return elem;
	}

	public String getNombreArchivoAParsear() {
		return nombreArchivoAParsear;
	}

	public String getPathAGuardar() {
		return pathAGuardar;
	}

	public DiarioDigital getDiario() {
		return diario;
	}

	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}

	public NotesRecolator getRecolector() {
		return recolector;
	}

	public boolean isOverride() {
		return this.override;
	}

	public Seccion getSeccion() {
		return this.seccion;
	}

	public void run() {
//		this.getRecolector().incrementDescargasARealizar();
		//TODO si esta al ppio avanza demasiado rapido la barra de jprogres!

		String nombreArchivoAGuarduar = getNombreArchivoAGuardar();

		if (!this.isOverride()) {
			if (StoreFile.fileExists(this.getPathAGuardar(), nombreArchivoAGuarduar, this.getFormatoSalida()
					.getExtension())) {
				this.getRecolector().incrementDescargasNoNecesarias();
				this.getRecolector().incrementDescargasARealizar();
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
			doc = Jsoup.connect(this.getDiario().getlinkNota(this.getElem().attr("href"))).timeout(0).get();
//			String url = this.getDiario().getlinkNota(this.getElem().attr("href"));
//			doc = Jsoup.parse(new URL(url).openStream(), this.diario.getCharsetName(), url);
			this.getRecolector().incrementDescargasARealizar();
		} catch (UnknownHostException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario " + this.getDiario().getNombreDiario() + ", sección "
							+ this.getSeccion().getNombreSeccion() + ": " + this.getElem().attr("href")
							+ ". Esto puede deberse a una desconexión de internet.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			this.getRecolector().incrementDescargasARealizar();
			return;
		} catch (SocketTimeoutException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario del diario " + this.getDiario().getNombreDiario()
							+ ", sección " + this.getSeccion().getNombreSeccion() + ": " + this.getElem().attr("href")
							+ ". Error por Time out.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			this.getRecolector().incrementDescargasARealizar();
			return;
		} catch (IOException e) {
			this.getRecolector().addErroresDescarga(
					"No se pudo descargar la nota del diario del diario " + this.getDiario().getNombreDiario()
							+ ", sección " + this.getSeccion().getNombreSeccion() + ": " + this.getElem().attr("href")
							+ ". Error desconocido.\r\n");
			this.getRecolector().incrementDescargasFallidas();
			this.getRecolector().incrementDescargasARealizar();
			return;
		}

		if (doc == null) {
			this.getRecolector().incrementDescargasFallidas();
			this.getRecolector().incrementDescargasARealizar();
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
		Document nuevoDoc = this.getDiario().getNotaPreProcesadaFromDocument(doc);

		if (this.getFormatoSalida() instanceof FormatoHtml) {
			guardarNotaHTML(nuevoDoc, nombreArchivoAGuarduar);
			this.getRecolector().incrementDescargasRealizadas();
		} else if (this.getFormatoSalida() instanceof FormatoTexto) {
			LimpiarHtml limpiador = new LimpiarHtml(this.getPathAGuardar(), this.getDiario(), this.isOverride());
			limpiador.limpiarDocumentoYGuardar(nuevoDoc, nombreArchivoAGuarduar);
			this.getRecolector().incrementDescargasRealizadas();
		}

	}

	public void guardarNotaHTML(Document doc, String nombreArchivo) {
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, this.getDiario().getCharsetName());
		try {
			sf.store(this.isOverride());
		} catch (IOException e) {
			this.getRecolector().incrementDescargasFallidas();
			e.printStackTrace();
		}
	}

	public String getNombreArchivoAGuardar() {
		String nombreArchivoAGuarduar = this.getNombreArchivoAParsear().replace(".html", "_" + this.getElem().text());

		if (nombreArchivoAGuarduar.contains("/"))
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replace("/", "-");
		if (nombreArchivoAGuarduar.contains(";"))
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replace(";", ",");
		// TODO! si contiene ciertos caracteres,guarda caracteres "raros" en el
		// nombrearchivo
		// "temporalmente", si tiene estos caracteres, los saco
		if (nombreArchivoAGuarduar.contains("")) {
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replaceAll("", "");
		}
		if (nombreArchivoAGuarduar.contains("")) {
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replaceAll("", "");
		}

		return nombreArchivoAGuarduar;
	}
}
