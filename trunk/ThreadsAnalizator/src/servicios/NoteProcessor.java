package servicios;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Seccion;

public abstract class NoteProcessor implements Runnable {

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

	@Override
	public abstract void run();

	public abstract void guardarNotaHTML(Document doc, String titulo);

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
