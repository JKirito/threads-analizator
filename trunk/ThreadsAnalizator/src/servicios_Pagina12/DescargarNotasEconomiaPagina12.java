package servicios_Pagina12;


public class DescargarNotasEconomiaPagina12 {

	/**
	 * Ya tengo descargadas las "Tapas" de la sección Economía.
	 * Ahora descargo cada nota html de cada "Tapa" y las guardo como html.
	 */
	public static void main(String[] args) {
		String pathOrigen = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/Pagina12/Tapas/tmp/";
		String pathAGuardar = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/Pagina12/Notas/Economia/";

		int cantHilos = 20;//Integer.parseInt(args[0]);

		NotesRecolator nt = new NotesRecolator(pathOrigen, pathAGuardar, cantHilos);
		nt.iniciar();
	}
}
