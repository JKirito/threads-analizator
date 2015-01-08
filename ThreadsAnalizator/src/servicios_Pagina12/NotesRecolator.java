package servicios_Pagina12;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NotesRecolator {

	private final static Logger logger = LogManager.getLogger(NotesRecolator.class.getName());
	private String pathAGuardar;
	private String pathOrigen;
	private Integer THREADS_NUMBER = 100;
	private final static String GRUPO_NOTICIAS = "seccionxblanco";
	private final static String CHARSETNAME_DIARIO = "iso-8859-1";

	public NotesRecolator(String carpetaOrigen, String carpetaDestino, int cantHilos) {
		this.pathOrigen = carpetaOrigen;
		this.pathAGuardar = carpetaDestino;
		this.THREADS_NUMBER = cantHilos;
	}

	public void iniciar() {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		long init = new Date().getTime();
		if (carpeta.isDirectory()) {
			ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
			logger.info("PUBLICACION; NOTA; TIEMPO PROCESAMIENTO(ms); TIEMPO DESCARGA(ms)");
			int i = 1;
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {

					if (i % 250 == 0) {
						long now = new Date().getTime();
						System.out.println("Archivos procesados hasta el momento: '" + i + "' en " + (now - init)
								/ 1000 + " segs.");
					}
					i++;
					// if(seProcesoArchivo(file)){
					// continue;
					// }
					// Obtener los links asociados a las notas de cada archivo
					try {
						Element notasABuscar = Jsoup.parse(file, CHARSETNAME_DIARIO).getElementsByClass(GRUPO_NOTICIAS).first();
						if (notasABuscar == null) {
							System.out.println("nota no encontrada: "+file.getName());
							continue;
						}

						Elements nota = notasABuscar.getElementsByTag("h2").select("[href]");
						//Agrego subnotas en caso de existir
						String classSubNota = "subnotas_secc";
						Elements subnotas = notasABuscar.getElementsByClass(classSubNota);
						nota.addAll(subnotas.select("[href]"));


						for (Element E : nota) {
							NoteProcessor np = new NoteProcessor(archivo, E, pathAGuardar);
							while (((ThreadPoolExecutor) executor).getActiveCount() == THREADS_NUMBER) {
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							executor.submit(np);
						}
					} catch (IOException e) {
						e.printStackTrace();
						// System.out.println("Se estaba procesando el archivo "
						// + archivo);
						continue;
					}
				}
			}
		}
		System.out.println();
		long finalTime = new Date().getTime();
		System.out.println("Se tardo en procesar todo: " + (finalTime - init) / 1000 + " segs.");
	}

//	private static boolean seProcesoArchivo(File file) {
//		File carpetaConProcesados = new File(pathAGuardar);
//		for(String arch : carpetaConProcesados.list()){
//			String archivoYAProcesado = arch.substring(0 , arch.lastIndexOf("_"));
//			String archivoAParsear = file.getName().substring(0 , file.getName().indexOf("."));
//			if(archivoYAProcesado.equals(archivoAParsear))
//				return true;
//		}
//		return false;
//	}

}
