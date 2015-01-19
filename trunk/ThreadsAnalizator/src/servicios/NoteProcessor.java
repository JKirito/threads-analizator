package servicios;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import entities.DiarioDigital;

public abstract class NoteProcessor implements Runnable {

	private Element elem;
	private String nombreArchivoAParsear;
	private String pathAGuardar;
	private DiarioDigital diario;

	public NoteProcessor(String archivo, Element elem, String pathAGuardar, DiarioDigital diario) {
		super();
		this.nombreArchivoAParsear = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
		this.diario = diario;
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

	@Override
	public abstract void run();

	public abstract void guardarNota(Document doc, String titulo);
}
