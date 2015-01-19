package servicios_LaNacion;

import servicios.NotesRecolator;
import entities.LaNacion;

public class DescargarNotasEconomiaLaNacion {

	/**
	 * Ya tengo descargadas las "Tapas" de la sección Economía. Ahora descargo
	 * cada nota html de cada "Tapa" y las guardo como html.
	 */
	public static void main(String[] args) {
		String pathOrigen = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/LaNacion/Tapas/Economia/";
		String pathAGuardar = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/LaNacion/Notas/Economia/";

		int cantHilos = 20;// Integer.parseInt(args[0]);

		NotesRecolator nt = new NotesRecolator(pathOrigen, pathAGuardar, cantHilos, new LaNacion());
		nt.iniciar();
	}
}
