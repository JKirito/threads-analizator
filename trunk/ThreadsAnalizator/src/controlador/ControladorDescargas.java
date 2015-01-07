package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modelo.ModeloDescarga;
import vista.SetupGetDirVista;
import vista.VistaDescargas;

public class ControladorDescargas implements ActionListener {

	ModeloDescarga modelo;
	VistaDescargas vistaDescargas;

	public ControladorDescargas(ModeloDescarga modelo, VistaDescargas vista) {
		this.modelo = modelo;
		this.vistaDescargas = vista;
		cargarDatosIniciales();
		actionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Agregar carpeta a la lista de irigen
		if (e.getSource() == vistaDescargas.getBtnAgregarOrigen()) {
			SetupGetDirVista mkdir = new SetupGetDirVista(vistaDescargas.getFrame(), true, true);
			int returnVal = mkdir.getJFileChooser1().showOpenDialog(vistaDescargas.getFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.modelo.getListaCarpetasOrigen().add(mkdir.getSelected().toString());
				this.vistaDescargas.getListCarpetasOrigen().setListData(this.modelo.getArrayCarpetasOrigen());
			}
		}
		// Eliminar carpeta de la lista de origen
		if (e.getSource() == vistaDescargas.getBtnEliminarOrigen()) {
			if (this.vistaDescargas.getListCarpetasOrigen().getSelectedIndex() != -1) {
				this.modelo.getListaCarpetasOrigen().remove(vistaDescargas.getListCarpetasOrigen().getSelectedValue());
				this.vistaDescargas.getListCarpetasOrigen().setListData(this.modelo.getArrayCarpetasOrigen());
			} else {// TODO: Necesario??
				mostrarMsjAUsuario("Debe seleccionar una carpeta de la lista", "Alerta", JOptionPane.WARNING_MESSAGE);
			}
		}
		// Agregar carpeta de destino
		if (e.getSource() == vistaDescargas.getBtnAgregarDestino()) {
			SetupGetDirVista mkdir = new SetupGetDirVista(vistaDescargas.getFrame(), true, true);
			int returnVal = mkdir.getJFileChooser1().showOpenDialog(vistaDescargas.getFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.modelo.setCarpetaDestino(mkdir.getSelected().toString());
				this.vistaDescargas.getLblCarpetaDest().setText(this.modelo.getCarpetaDestino());
			}
		}
		// Procesar
		if (e.getSource() == vistaDescargas.getButtonProcesar()) {
			this.modelo.setFechaArchivosDesde(this.vistaDescargas.getDateChooser().getDate());
			if (!this.modelo.validarProcesar().isEmpty()) {
				mostrarMsjAUsuario(this.modelo.validarProcesar(), "Alerta", JOptionPane.WARNING_MESSAGE);
			} else {
				//Motrar resultado proceso
				try {
					this.vistaDescargas.getButtonProcesar().setEnabled(false);
					this.modelo.procesar();
					this.vistaDescargas.getButtonProcesar().setEnabled(true);
					vistaResultado.getTextArea().setText(this.modelo.getResultadoProceso());
					vistaResultado.setVisible(true);
				} catch (Exception e1) {
					mostrarMsjAUsuario(e1.getMessage(), "Error al procesar", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
		//Cerrar ventana con resultado proceso
		if (e.getSource() == vistaResultado.getBtnCerrar()) {
			vistaResultado.setVisible(false);
		}
		//Guardar resultado proceso a archivo
		if (e.getSource() == vistaResultado.getBtnGuardar()) {
			SetupGetDirVista mkdir = new SetupGetDirVista(null, true, false);
			int returnVal = mkdir.getJFileChooser1().showSaveDialog(null);
			String rutaArchivo = mkdir.getSelected().getAbsolutePath();
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (new File(rutaArchivo).isFile() && new File(rutaArchivo).exists()) {
					mostrarMsjAUsuario("Ya existe un archivo con ese nombre", "Archivo existente", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						this.modelo.guardarResultadoProceso(rutaArchivo);
						mostrarMsjAUsuario("El archivo se guardó correctamente", "Operacion completada", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						vistaResultado.dispose();
						mostrarMsjAUsuario(e1.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	public void actionListener(ActionListener escuchador) {
		vistaDescargas.getBtnAgregarOrigen().addActionListener(escuchador);
		vistaDescargas.getBtnEliminarOrigen().addActionListener(escuchador);
		vistaDescargas.getBtnAgregarDestino().addActionListener(escuchador);
		vistaDescargas.getButtonProcesar().addActionListener(escuchador);
	}

	public static boolean solicitarRespuestaAUsuario(String consulta) {
		return PrincipalVista.solicitarRespuestaAUsuario(consulta);
	}

	public static void mostrarMsjAUsuario(String msj, String title, int warningMessage) {
		PrincipalVista.mostrarMsjAUsuario(msj, title, warningMessage);
	}

	private void cargarDatosIniciales() {
		if(!this.modelo.getListaCarpetasOrigen().isEmpty()){
			this.vistaDescargas.getListCarpetasOrigen().setListData(this.modelo.getArrayCarpetasOrigen());
		}
		if(!this.modelo.getCarpetaDestino().isEmpty()){
			this.vistaDescargas.getLblCarpetaDest().setText(this.modelo.getCarpetaDestino());
		}
		if(this.modelo.getFechaArchivosDesde() != null){
			this.vistaDescargas.getDateChooser().setDate(this.modelo.getFechaArchivosDesde());
		}
	}
}
