package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.StoreFile;
import Utils.Utils;
import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Seccion;

public class PageDownloader {

	private DiarioDigital diario;
	private Seccion seccion;
	private FormatoSalida formatoSalida;
	private String pathAGuardar;
	private Date fechaDate;
	private int cantDiasARecopilar;
	private String erroresDescarga = "";
	private boolean override;
	private Integer threads_number = 10; //Por defecto uso 10 hilos

	public PageDownloader(DiarioDigital diario, Seccion seccion, FormatoSalida formatoSalida, String pathAGuardar, Date fechaHasta,
			int diasARecopílar, boolean override) {
		super();
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formatoSalida;
		this.pathAGuardar = pathAGuardar;
		this.fechaDate = fechaHasta;
		this.cantDiasARecopilar = diasARecopílar;
		this.override = override;
	}

	public PageDownloader(DiarioDigital diario, Seccion seccion, FormatoSalida formatoSalida, String pathAGuardar, Date fechaHasta,
			int diasARecopílar, boolean override, int numeroHilos) {
		super();
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formatoSalida;
		this.pathAGuardar = pathAGuardar;
		this.fechaDate = fechaHasta;
		this.cantDiasARecopilar = diasARecopílar;
		this.override = override;
		this.threads_number = numeroHilos;
	}


	public class ThreadDownload implements Runnable{
		private String linkActual;
		private String fecha;

		public ThreadDownload(String linkActual, String fecha) {
			this.linkActual = linkActual;
			this.fecha = fecha;
		}
		@Override
		public void run() {
			String nombreArchivo = diario.getNombreArchivo(fecha);

			if(!override){
				if(StoreFile.existeFile(pathAGuardar, nombreArchivo, formatoSalida.getExtension())){
					return;
				}
			}

			Document page = null;
			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
				if (!diario.esValido(page)) {
					erroresDescarga += "AL PARECER " + diario.getNombreDiario() + " NO TIENE NOTICIAS EN LA SECCIÓN "
							+ seccion.getCodigoSeccion() + " EL DIARIO EL DIA: " + fecha + ".\r\n";
				}
			} catch (UnknownHostException e) {
				erroresDescarga += "NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha
						+ ". ESTO PUEDE DEBERSE A UNA DESCONEXIÓN DE INTERNET.\r\n";
			} catch (SocketTimeoutException e) {
				erroresDescarga += "NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha
						+ ". Error por Time out.\r\n";
			} catch (IOException e) {
				erroresDescarga += "NO SE PUDO DESCARGAR EL DIARIO " + diario.getNombreDiario() + ", SECCIÓN "
						+ seccion.getNombreSección() + " DEL DIA: " + fecha + ".\r\n";
				e.printStackTrace();
			}

			//TODO: modificar en caso que se quiera guardar txt ? si para tapas no está esa opción, eliminar este "todo"
			String datosAGuardar = page.html();

			try {
				StoreFile sf = new StoreFile(pathAGuardar, formatoSalida.getExtension(), datosAGuardar, nombreArchivo, diario.getCharsetName());
				sf.store(override);
			} catch (IOException e) {
				erroresDescarga += "ERROR AL QUERER GUARDAR EL ARCHIVO " + nombreArchivo + "\r\n";
				// e.printStackTrace();
			}

		}

	}

	// private final String linkPagina12 =
	// "http://www.pagina12.com.ar/diario/economia/index-";// yyyy-MM-dd;
	// private final String linkLaNacion =
	// "http://servicios.lanacion.com.ar/archivo-f";// 04/09/2014-c30"
	// private final String charsetNamePagina12 = "iso-8859-1";
	// public final String seccionEconomiaPagina12 = "";



	public String getErroresDescarga() {
		return erroresDescarga;
	}

	public void download() {
		for (int i = cantDiasARecopilar; i > 0; i--) {
			// Para que empiece a restar después de descargar el primer día
			if (i != cantDiasARecopilar) {
				fechaDate = Utils.sumarRestarDiasFecha(fechaDate, -1);
			}
			String fecha = Utils.dtoYYYY_MM_DD(fechaDate);
			String linkActual = diario.armarLinkActual(fecha, seccion);


			ThreadDownload newDownload = new ThreadDownload(linkActual, fecha);
			ExecutorService executor = Executors.newFixedThreadPool(threads_number);
			while (((ThreadPoolExecutor) executor).getActiveCount() == threads_number) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			executor.submit(newDownload);

		}
	}

}