package modelo;

import java.io.File;

import entities.DiarioDigital;
import entities.EconomiaLaNacion;
import entities.EconomiaPagina12;
import entities.FormatoSalida;
import entities.LaNacion;
import entities.Pagina12;
import entities.Seccion;
import entities.SeccionEconomia;

public class ModeloDescargaNotas extends ModeloDescarga{

	private static final String MSJ_CARPETAORIGEN_VACIO = "Debe seleccionar la carpeta de origen";
	private String rutaOrigen;


	public ModeloDescargaNotas(String rutaDestino, String modoDescarga, String diarioDescarga, String seccionDescarga,
			boolean override, FormatoSalida formatoOutput, String rutaOrigen) {
		super(rutaDestino, modoDescarga, diarioDescarga, seccionDescarga, override, formatoOutput);
		this.rutaOrigen = rutaOrigen;
	}

	public ModeloDescargaNotas() {
		super();
	}

	public String getRutaOrigen() {
		return rutaOrigen;
	}

	public void setRutaOrigen(String rutaOrigen) {
		this.rutaOrigen = rutaOrigen;
	}

	@Override
	public void descargar() {
		this.setDescargando(true);
		System.out.println("DESCARGAR NOTAS!!!");
		this.descargaDetenida = false;

		DiarioDigital diario = null;
		Seccion seccion = null;
		if (this.getDiarioDescarga().equals(Pagina12.NOMBRE_DIARIO)) {
			diario = new Pagina12();
			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
				seccion = new EconomiaPagina12();
			}
		} else if (this.getDiarioDescarga().equals(LaNacion.NOMBRE_DIARIO)) {
			diario = new LaNacion();
			if (this.getSeccionDescarga().equals(SeccionEconomia.NOMBRE_SECCION)) {
				seccion = new EconomiaLaNacion();
			}
		}

		String pathAGuardar = this.getRutaDestino() + File.separatorChar;
		int diasARecopílar = this.getCantTapasDescargar();

//		this.pageDownloader = new PageDownloader(diario, seccion, this.getFormatoOutput(),pathAGuardar, fechaHasta, diasARecopílar, this.isOverride());
		this.pageDownloader.addObserver(this.getSwingWorker());
		this.pageDownloader.download();
		this.setDescargando(false);
		this.controladorDescargas.descargaFinalizada();


		this.setDescargando(false);
	}

	@Override
	public int getCantOptimaHilos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String validarDatos() {
		String errores = super.validarDatos();
		if (this.rutaOrigen == null || this.rutaOrigen.isEmpty()) {
			errores += "-" + MSJ_CARPETAORIGEN_VACIO + "\n";
		}
		return errores;
	}

}
