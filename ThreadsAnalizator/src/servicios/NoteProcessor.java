package servicios;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Note;

public abstract class NoteProcessor implements Runnable {

	private Element elem;
	private String nombreArchivoAParsear;
	private String pathAGuardar;
	private DiarioDigital diario;
	private FormatoSalida formatoSalida;

	public NoteProcessor(String archivo, Element elem, String pathAGuardar, DiarioDigital diario, FormatoSalida formato) {
		super();
		this.nombreArchivoAParsear = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
		this.diario = diario;
		this.formatoSalida = formato;
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

	@Override
	public abstract void run();

	public abstract void guardarNotaHTML(Document doc, String titulo);

	public abstract void guardarNotaTXT(Note nota, String archivo, String pathAGuardar);
}
