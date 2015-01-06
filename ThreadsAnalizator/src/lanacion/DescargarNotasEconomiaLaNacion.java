package lanacion;


public class DescargarNotasEconomiaLaNacion {

	/**
	 * Ya tengo descargadas las "Tapas" de la sección Economía.
	 * Ahora descargo cada nota html de cada "Tapa" y las guardo como html.
	 */
	public static void main(String[] args) {
		String pathOrigen = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/LaNacion/Tapas/Economia/";
		String pathAGuardar = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/LaNacion/Notas/Economia/";

		NotesRecolator nt = new NotesRecolator(pathOrigen, pathAGuardar);
		nt.iniciar();
	}
}
