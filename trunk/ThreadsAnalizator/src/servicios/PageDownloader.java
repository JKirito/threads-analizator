package servicios;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.StoreFile;
import Utils.Utils;
import entities.DiarioDigital;
import entities.Seccion;

public class PageDownloader {

	// private final String linkPagina12 =
	// "http://www.pagina12.com.ar/diario/economia/index-";// yyyy-MM-dd;
	// private final String linkLaNacion =
	// "http://servicios.lanacion.com.ar/archivo-f";// 04/09/2014-c30"
	// private final String charsetNamePagina12 = "iso-8859-1";
	// public final String seccionEconomiaPagina12 = "";

	private DiarioDigital diario;
	private Seccion seccion;
	private String pathAGuardar;
	private Date fechaDate;
	private int cantDiasARecopilar;
	private String erroresDescarga = "";

	public PageDownloader(DiarioDigital diario, Seccion seccion, String pathAGuardar, Date fechaHasta,
			int diasARecopílar) {
		super();
		this.diario = diario;
		this.seccion = seccion;
		this.pathAGuardar = pathAGuardar;
		this.fechaDate = fechaHasta;
		this.cantDiasARecopilar = diasARecopílar;
	}

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
			Document page = null;
			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
			} catch (IOException e) {
				erroresDescarga += "AL PARECER NO EXISTE EL DIARIO DEL DIA: " + fecha + "\r\n";
				// e.printStackTrace();
				continue;
			}
			String nombreArchivo = diario.getNombreArchivo(fecha);
			try {
				StoreFile sf = new StoreFile(pathAGuardar, ".html", page.html(), nombreArchivo, diario.getCharsetName());
				sf.store();
			} catch (IOException e) {
				erroresDescarga += "ERROR AL QUERER GUARDAR EL ARCHIVO " + nombreArchivo + "\r\n";
				// e.printStackTrace();
				continue;
			}
			// Sin HTML
			// guardarPageHtml(Jsoup.parse(page).text(), "pageHTML_" + fecha);
		}
	}

}