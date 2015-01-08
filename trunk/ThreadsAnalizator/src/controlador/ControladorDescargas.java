package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modelo.ModeloDescarga;
import modelo.ModeloDescargaNotas;
import modelo.ModeloDescargaTapas;
import vista.SetupGetDirVista;
import vista.VistaDescargas;
import Utils.SwingUtils;

public class ControladorDescargas implements ActionListener {

	private VistaDescargas vistaDescargas;
	private ModeloDescarga modeloDescarga;

	// private ModeloDescargaNotas modeloDescargaNotas;
	// private ModeloDescargaTapas modeloDescargaTapas;

	public ControladorDescargas(VistaDescargas vista) {
		this.vistaDescargas = vista;
		// this.modeloDescargaNotas = new ModeloDescargaNotas();
		// this.modeloDescargaTapas = new ModeloDescargaTapas();
		cargarDatosIniciales();
		actionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// SE ELIGE MODO DESCARGA
		if (e.getSource() == vistaDescargas.getRadioBtnDescargarNotas()
				|| e.getSource() == vistaDescargas.getRadioBtnDescargarTapas()) {

			if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				deshabilitarCamposTapa();
			} else if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				deshabilitarCamposNota();
			}
		}
		// SE ELIGE DIARIO PAGINA 12
		if (e.getSource() == vistaDescargas.getRadioBtnPagina12()) {

		}

		// SE ELIGE DIARIO LA NACION
		if (e.getSource() == vistaDescargas.getRadioBtnLaNacion()) {

		}

		// SE ELIGE SECCION ECONOMIA
		if (e.getSource() == vistaDescargas.getChckbxEconomia()) {

		}

		// SE ELIGE ORIGEN
		if (e.getSource() == vistaDescargas.getBtnAgregarCarpetaOrigen()) {
			SetupGetDirVista mkdir = new SetupGetDirVista(vistaDescargas.getFrame(), true, true);
			int returnVal = mkdir.getJFileChooser1().showOpenDialog(vistaDescargas.getFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.vistaDescargas.getTextFieldCarpetaOrigen().setText(mkdir.getSelected().toString());
			}
		}
		// SE ELIGE DESTINO
		if (e.getSource() == vistaDescargas.getBtnAgregarCarpetaDestino()) {
			SetupGetDirVista mkdir = new SetupGetDirVista(vistaDescargas.getFrame(), true, true);
			int returnVal = mkdir.getJFileChooser1().showOpenDialog(vistaDescargas.getFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.vistaDescargas.getTextFieldCarpetaDestino().setText(mkdir.getSelected().toString());
			}
		}

		// SALIR
		if (e.getSource() == vistaDescargas.getBtnSalir()) {
			vistaDescargas.getFrame().setVisible(false);
			System.exit(0);
		}

		// Procesar
		if (e.getSource() == vistaDescargas.getButtonDescargar()) {
			boolean procesar = true;
			if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				this.modeloDescarga = new ModeloDescargaNotas();
				cargarDatosModoDescargaNotas();

				String msjValidacion = ((ModeloDescargaNotas) this.modeloDescarga).validarDatos();
				if (!msjValidacion.isEmpty()) {
					procesar = false;
					mostrarMsjAUsuario(msjValidacion, "Alerta", JOptionPane.WARNING_MESSAGE);
				}

			} else if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				this.modeloDescarga = new ModeloDescargaTapas();
				cargarDatosModoDescargaTapas();

				String msjValidacion = ((ModeloDescargaTapas) this.modeloDescarga).validarDatos();
				if (!msjValidacion.isEmpty()) {
					procesar = false;
					mostrarMsjAUsuario(msjValidacion, "Alerta", JOptionPane.WARNING_MESSAGE);
				}

			} else {
				procesar = false;
				String modosDescarga = "Nota - Tapa";
				mostrarMsjAUsuario("Debe seleccionar el modo de descarga: " + modosDescarga, "Seleccionar Modo",
						JOptionPane.WARNING_MESSAGE);
			}

			if(!procesar)
				return;

			// PROCESAR
			try {
				String txtButnDescargar = this.vistaDescargas.getButtonDescargar().getText();
				this.vistaDescargas.getButtonDescargar().setText("Descargando..");
				SwingUtils.setEnableContainer(vistaDescargas.getButtonDescargar(), false);

				this.mostrarMsjAUsuario("El proceso puede tardar muuuucho tiempo...", "Descargando..", JOptionPane.INFORMATION_MESSAGE);
				// TODO: Agregar opción de buscar cant optimizada de hilos a
				// usar!!!
				this.modeloDescarga.descargar();

				this.vistaDescargas.getButtonDescargar().setText(txtButnDescargar);
				this.vistaDescargas.getButtonDescargar().setEnabled(true);

			} catch (Exception e1) {
				mostrarMsjAUsuario(e1.getMessage(), "Error al procesar", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}



		// Guardar resultado proceso a archivo
		// if (e.getSource() == vistaResultado.getBtnGuardar()) {
		// SetupGetDirVista mkdir = new SetupGetDirVista(null, true, false);
		// int returnVal = mkdir.getJFileChooser1().showSaveDialog(null);
		// String rutaArchivo = mkdir.getSelected().getAbsolutePath();
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// if (new File(rutaArchivo).isFile() && new File(rutaArchivo).exists())
		// {
		// mostrarMsjAUsuario("Ya existe un archivo con ese nombre",
		// "Archivo existente",
		// JOptionPane.ERROR_MESSAGE);
		// } else {
		// try {
		// this.modeloDescargaNotas.guardarResultadoProceso(rutaArchivo);
		// mostrarMsjAUsuario("El archivo se guardó correctamente",
		// "Operacion completada",
		// JOptionPane.INFORMATION_MESSAGE);
		// } catch (Exception e1) {
		// vistaResultado.dispose();
		// mostrarMsjAUsuario(e1.getMessage(), "Error al guardar",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// }
		// }
		// }
	}

	private void cargarDatosModoDescargaComunes() {
		if (vistaDescargas.getChckbxEconomia().isSelected()) {
			this.modeloDescarga.setSeccionDescarga(vistaDescargas.getChckbxEconomia().getText());
		}
		if (vistaDescargas.getRadioBtnLaNacion().isSelected()) {
			this.modeloDescarga.setDiarioDescarga(vistaDescargas.getRadioBtnLaNacion().getText());
		} else if (vistaDescargas.getRadioBtnPagina12().isSelected()) {
			this.modeloDescarga.setDiarioDescarga(vistaDescargas.getRadioBtnPagina12().getText());
		}
		this.modeloDescarga.setRutaDestino(vistaDescargas.getTextFieldCarpetaDestino().getText());
	}

	private void cargarDatosModoDescargaNotas() {
		cargarDatosModoDescargaComunes();
		((ModeloDescargaNotas) this.modeloDescarga).setRutaOrigen(vistaDescargas.getTextFieldCarpetaOrigen().getText());
	}

	private void cargarDatosModoDescargaTapas() {
		cargarDatosModoDescargaComunes();
		((ModeloDescargaTapas) this.modeloDescarga).setCantTapasDescargar((Integer) vistaDescargas.getSpinnerCantDias()
				.getValue());
		((ModeloDescargaTapas) this.modeloDescarga).setFechaDescargaHasta(vistaDescargas.getDateChooser().getDate());
	}

	public void actionListener(ActionListener escuchador) {
		vistaDescargas.getRadioBtnDescargarNotas().addActionListener(escuchador);
		vistaDescargas.getRadioBtnDescargarTapas().addActionListener(escuchador);
		vistaDescargas.getRadioBtnPagina12().addActionListener(escuchador);
		vistaDescargas.getRadioBtnLaNacion().addActionListener(escuchador);
		vistaDescargas.getChckbxEconomia().addActionListener(escuchador);
		vistaDescargas.getBtnAgregarCarpetaOrigen().addActionListener(escuchador);
		vistaDescargas.getBtnAgregarCarpetaDestino().addActionListener(escuchador);
		vistaDescargas.getButtonDescargar().addActionListener(escuchador);
		vistaDescargas.getBtnSalir().addActionListener(escuchador);
	}

	public boolean solicitarRespuestaAUsuario(String consulta) {
		return vistaDescargas.solicitarRespuestaAUsuario(consulta);
	}

	public void mostrarMsjAUsuario(String msj, String title, int warningMessage) {
		vistaDescargas.mostrarMsjAUsuario(msj, title, warningMessage);
	}

	private void cargarDatosIniciales() {
		// TODO: comienza alguno seleccionado del modo descarga?
		if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
			this.deshabilitarCamposNota();
		} else if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
			this.deshabilitarCamposTapa();
		}

		// TODO: podría guardar las últimas opciones utilizadas como
		// predeterminado

	}

	// Deshabilito los campos que se usan para descargar Notas
	private void deshabilitarCamposNota() {
		habilitarCamposTapa();
		SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaOrigen(), false);
	}

	// Deshabilito los campos que se usan para descargar Tapas
	private void deshabilitarCamposTapa() {
		habilitarCamposNota();
		SwingUtils.setEnableContainer(vistaDescargas.getPanelFecha(), false);
	}

	private void habilitarCamposNota() {
		SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaOrigen(), true);
	}

	private void habilitarCamposTapa() {
		SwingUtils.setEnableContainer(vistaDescargas.getPanelFecha(), true);
	}
}
