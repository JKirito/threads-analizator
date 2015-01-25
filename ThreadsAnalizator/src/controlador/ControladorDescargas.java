package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modelo.ModeloDescarga;
import modelo.ModeloDescargaNotas;
import modelo.ModeloDescargaTapas;
import servicios.WorkerDownload;
import vista.SetupGetDirVista;
import vista.VistaDescargas;
import vista.VistaResultadoDescarga;
import Utils.SwingUtils;
import entities.EconomiaLaNacion;
import entities.EconomiaPagina12;
import entities.FormatoHtml;
import entities.FormatoTexto;
import entities.LaNacion;
import entities.Pagina12;

public class ControladorDescargas implements ActionListener {

	private VistaDescargas vistaDescargas;
	private VistaResultadoDescarga resultadoDescargas;
	private ModeloDescarga modeloDescarga;

	private boolean mostrarOpcionCrearCarpetaDestino = true;

	public ControladorDescargas(VistaDescargas vista, VistaResultadoDescarga resultadoDescarga) {
		this.vistaDescargas = vista;
		this.resultadoDescargas = resultadoDescarga;
		cargarDatosIniciales();
		actionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// SE ELIGE MODO DESCARGA
		if (e.getSource() == vistaDescargas.getRadioBtnDescargarNotas()
				|| e.getSource() == vistaDescargas.getRadioBtnDescargarTapas()) {
			mostrarOpcionCrearCarpetaDestino = true;

			if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				deshabilitarCamposTapa();
			} else if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				deshabilitarCamposNota();
			}
		}
		// SE ELIGE DIARIO PAGINA 12
		if (e.getSource() == vistaDescargas.getRadioBtnPagina12()) {
			mostrarOpcionCrearCarpetaDestino = true;
		}

		// SE ELIGE DIARIO LA NACION
		if (e.getSource() == vistaDescargas.getRadioBtnLaNacion()) {
			mostrarOpcionCrearCarpetaDestino = true;
		}

		// SE ELIGE SECCION ECONOMIA
		if (e.getSource() == vistaDescargas.getChckbxEconomia()) {
			mostrarOpcionCrearCarpetaDestino = true;
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
			mostrarOpcionCrearCarpetaDestino = true;
			SetupGetDirVista mkdir = new SetupGetDirVista(vistaDescargas.getFrame(), true, true);
			int returnVal = mkdir.getJFileChooser1().showOpenDialog(vistaDescargas.getFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String nuevoPath = ofrecerAgregarCarpetaDestino(mkdir.getSelected().toString());
				this.vistaDescargas.getTextFieldCarpetaDestino().setText(nuevoPath);
			}
		}

		// SALIR
		if (e.getSource() == vistaDescargas.getBtnSalir()) {
			boolean salir = false;
			if (this.modeloDescarga != null && this.modeloDescarga.isDescargando()) {
				salir = this.vistaDescargas
						.solicitarRespuestaAUsuario("Hay descargas en proceso, si sale, se cancelarán. ¿Desea salir del sistema de todas formas?");
			} else {
				salir = this.vistaDescargas.solicitarRespuestaAUsuario("¿Desea salir del sistema?");
			}

			if (salir) {
				boolean guardarOpciones = this.vistaDescargas
						.solicitarRespuestaAUsuario("¿Desea guardar las opciones elegidas para la próxima vez que use el programa? :)");
				if (guardarOpciones) {
					try {
						guardarOpciones();
					} catch (IOException e1) {
						vistaDescargas.mostrarMsjAUsuario("No pude guardar las opciones, lo siento :(",
								"Error al guardar", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		}

		// DESCARGAR
		if (e.getSource() == vistaDescargas.getButtonDescargar()) {
			String msjValidacion = "";
			if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				this.modeloDescarga = new ModeloDescargaNotas();
				cargarDatosModoDescargaNotas();

				if (!validacionCarpetasOk(((ModeloDescargaNotas) this.modeloDescarga).getRutaOrigen())) {
					vistaDescargas.getBtnAgregarCarpetaOrigen().doClick();
					return;
				}
			} else if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				this.modeloDescarga = new ModeloDescargaTapas();
				cargarDatosModoDescargaTapas();

			} else {
				String modosDescarga = "Nota - Tapa";
				mostrarMsjAUsuario("Debe seleccionar el modo de descarga: " + modosDescarga, "Seleccionar Modo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			String nuevoPath = ofrecerAgregarCarpetaDestino(this.modeloDescarga.getRutaDestino());
			this.modeloDescarga.setRutaDestino(nuevoPath);

			if (this.modeloDescarga instanceof ModeloDescargaTapas) {
				msjValidacion = ((ModeloDescargaTapas) this.modeloDescarga).validarDatos();
			} else {
				msjValidacion = ((ModeloDescargaNotas) this.modeloDescarga).validarDatos();
			}

			if (!msjValidacion.isEmpty()) {
				mostrarMsjAUsuario(msjValidacion, "Alerta", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (!validacionCarpetasOk(this.modeloDescarga.getRutaDestino())) {
				vistaDescargas.getBtnAgregarCarpetaDestino().doClick();
				return;
			}
			// PROCESAR
			try {
				SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaDestino(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaOrigen(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getPanelDiario(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getPanelModoDescarga(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getPanelFecha(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getPanelSeccion(), false);
				SwingUtils.setEnableContainer(vistaDescargas.getButtonDescargar(), false);
				// TODO: Agregar opción de buscar cant optimizada de hilos a
				// usar!!!
				this.modeloDescarga.setControlador(this);
				resultadoDescargas.setTitle("Descargando...");
				resultadoDescargas.getTextArea().setText("");
				resultadoDescargas.getBtnCerrar().setText("Cancelar");
				resultadoDescargas.getBtnGuardar().setVisible(false);
				resultadoDescargas.setVisible(true);

				WorkerDownload downloader = new WorkerDownload(this.resultadoDescargas.getLblProgreso(),
						this.resultadoDescargas.getProgressBar(), modeloDescarga);
				modeloDescarga.setSwingWorker(downloader);
				downloader.execute();
				// Para que comience a descargar y no quede congelada la
				// pantalla
				// ExecutorService executor = Executors.newFixedThreadPool(1);
				// executor.submit((ModeloDescargaTapas) modeloDescarga);
				// ((ModeloDescargaTapas) modeloDescarga).descargar();

			} catch (Exception e1) {
				mostrarMsjAUsuario(e1.getMessage(), "Error al procesar", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}

		// DESCARGA FINALIZADA - Guardar
		if (e.getSource() == resultadoDescargas.getBtnGuardar()) {
			// Guardar resultado proceso a archivo
			SetupGetDirVista mkdir = new SetupGetDirVista(null, true, false);
			int returnVal = mkdir.getJFileChooser1().showSaveDialog(null);
			String rutaArchivo = mkdir.getSelected().getAbsolutePath();
			if (rutaArchivo == null) {
				return;
			}
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (new File(rutaArchivo).isFile() && new File(rutaArchivo).exists()) {
					mostrarMsjAUsuario("Ya existe un archivo con ese nombre", "Archivo existente",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						this.modeloDescarga.guardarResultadoProceso(rutaArchivo);
						mostrarMsjAUsuario("El archivo " + rutaArchivo + " se guardó correctamente",
								"Operacion completada", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						mostrarMsjAUsuario(e1.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

		// CERRAR
		if (e.getSource() == resultadoDescargas.getBtnCerrar()) {
			boolean salir = false;
			if (this.modeloDescarga.isDescargando()) {
				salir = this.vistaDescargas.solicitarRespuestaAUsuario("¿Desea detener las descargas en proceso?");
			} else {
				resultadoDescargas.dispose();
			}
			if (salir) {
				this.modeloDescarga.detenerDescarga();
				// resultadoDescargas.dispose();
			}

		}
	}

	private void cargarDatosModoDescargaComunes() {


		// Cargar Diario y seccion
		if (vistaDescargas.getRadioBtnLaNacion().isSelected()) {
			this.modeloDescarga.setDiarioDescarga(new LaNacion());
			if (vistaDescargas.getChckbxEconomia().isSelected()) {
				this.modeloDescarga.setSeccionDescarga(new EconomiaLaNacion());
			}
		} else if (vistaDescargas.getRadioBtnPagina12().isSelected()) {
			this.modeloDescarga.setDiarioDescarga(new Pagina12());
			if (vistaDescargas.getChckbxEconomia().isSelected()) {
				this.modeloDescarga.setSeccionDescarga(new EconomiaPagina12());
			}
		}
		// Cargar formato Salida
		if (vistaDescargas.getRadioBtnHTML().isSelected()) {
			this.modeloDescarga.setFormatoOutput(new FormatoHtml());
		} else if (vistaDescargas.getRadioBtnTXT().isSelected()) {
			this.modeloDescarga.setFormatoOutput(new FormatoTexto());
		}

		// Cargar carpeta destino
		this.modeloDescarga.setRutaDestino(vistaDescargas.getTextFieldCarpetaDestino().getText());

		// Cargar cantidad de tapas
		this.modeloDescarga.setCantTapasDescargar((Integer) vistaDescargas.getSpinnerCantDias().getValue());

		// Cargar opcion override
		this.modeloDescarga.setOverride(vistaDescargas.getChckbxSobreescribir().isSelected());
	}

	private void cargarDatosModoDescargaNotas() {
		cargarDatosModoDescargaComunes();
		((ModeloDescargaNotas) this.modeloDescarga).setRutaOrigen(vistaDescargas.getTextFieldCarpetaOrigen().getText());
	}

	private void cargarDatosModoDescargaTapas() {
		cargarDatosModoDescargaComunes();
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

		this.resultadoDescargas.getBtnCerrar().addActionListener(escuchador);
		this.resultadoDescargas.getBtnGuardar().addActionListener(escuchador);
	}

	public boolean solicitarRespuestaAUsuario(String consulta) {
		return vistaDescargas.solicitarRespuestaAUsuario(consulta);
	}

	public void mostrarMsjAUsuario(String msj, String title, int warningMessage) {
		vistaDescargas.mostrarMsjAUsuario(msj, title, warningMessage);
	}

	private void guardarOpciones() throws IOException {
		FileWriter opcionesInicialesFile = new FileWriter("conf" + File.separator + "opcionesIniciales.cfg");
		if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
			opcionesInicialesFile.write("MD:N\n");
		} else {
			opcionesInicialesFile.write("MD:T\n");
		}
		if (vistaDescargas.getRadioBtnPagina12().isSelected()) {
			opcionesInicialesFile.write("D:P12\n");
		} else {
			opcionesInicialesFile.write("D:LN\n");
		}
		if (vistaDescargas.getChckbxEconomia().isSelected()) {
			opcionesInicialesFile.write("S:ECO\n");
		}
		if (vistaDescargas.getRadioBtnHTML().isSelected()) {
			opcionesInicialesFile.write("F:HTML\n");
		} else {
			opcionesInicialesFile.write("F:TXT\n");
		}
		String ruta = vistaDescargas.getTextFieldCarpetaOrigen().getText().toString();
		if (!ruta.isEmpty()) {
			opcionesInicialesFile.write("OR:" + ruta + "\n");
		}
		ruta = vistaDescargas.getTextFieldCarpetaDestino().getText().toString();
		opcionesInicialesFile.write("DES:" + ruta + "\n");
		opcionesInicialesFile.write("CD:" + vistaDescargas.getSpinnerCantDias().getValue());
		opcionesInicialesFile.close();

	}

	private void cargarDatosIniciales() {
		boolean cargarDatosPorDefecto = vistaDescargas
				.solicitarRespuestaAUsuario("¿Desea cargar las opciones por defecto?");
		if (cargarDatosPorDefecto) {
			String fileName = "opcionesIniciales.cfg";
			File datosInicialesFile = new File("conf" + File.separator + fileName);
			if (datosInicialesFile.exists()) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(datosInicialesFile.getAbsolutePath());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader bur = new BufferedReader(new InputStreamReader(dis));
				String linea = "";
				try {
					while ((linea = bur.readLine()) != null) {
						String[] opcion = linea.split(":");

						if (opcion.length == 2) {
							if (opcion[0].equals("MD")) {
								if (opcion[1].equals("T")) {
									this.vistaDescargas.getRadioBtnDescargarTapas().setSelected(true);
								} else {
									this.vistaDescargas.getRadioBtnDescargarNotas().setSelected(true);
								}
							}
							if (opcion[0].equals("D")) {
								if (opcion[1].equals("P12")) {
									this.vistaDescargas.getRadioBtnPagina12().setSelected(true);
								} else {
									this.vistaDescargas.getRadioBtnLaNacion().setSelected(true);
								}
							}
							if (opcion[0].equals("S")) {
								if (opcion[1].equals("ECO")) {
									this.vistaDescargas.getChckbxEconomia().setSelected(true);
								}
							}
							if (opcion[0].equals("F")) {
								if (opcion[1].equals("HTML")) {
									this.vistaDescargas.getRadioBtnHTML().setSelected(true);
								} else {
									this.vistaDescargas.getRadioBtnTXT().setSelected(true);
								}
							}
							if (opcion[0].equals("OR")) {
								this.vistaDescargas.getTextFieldCarpetaOrigen().setText(opcion[1].toString());
							}
							if (opcion[0].equals("DES")) {
								this.vistaDescargas.getTextFieldCarpetaDestino().setText(opcion[1].toString());
							}
							if (opcion[0].equals("CD")) {
								this.vistaDescargas.getSpinnerCantDias()
										.setValue(Integer.valueOf(opcion[1].toString()));
							}
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			this.vistaDescargas.getDateChooser().setDate(new Date());

			if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				this.deshabilitarCamposNota();
			} else if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				this.deshabilitarCamposTapa();
			}
		}
	}

	// Deshabilito los campos que se usan para descargar Notas
	private void deshabilitarCamposNota() {
		habilitarCamposTapa();
		SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaOrigen(), false);
		SwingUtils.setEnableContainer(vistaDescargas.getRadioBtnHTML(), false);
		SwingUtils.setEnableContainer(vistaDescargas.getRadioBtnTXT(), false);
	}

	// Deshabilito los campos que se usan para descargar Tapas
	private void deshabilitarCamposTapa() {
		habilitarCamposNota();
		SwingUtils.setEnableContainer(vistaDescargas.getPanelFecha(), false);
	}

	private void habilitarCamposNota() {
		SwingUtils.setEnableContainer(vistaDescargas.getPanelCarpetaOrigen(), true);
		SwingUtils.setEnableContainer(vistaDescargas.getRadioBtnHTML(), true);
		SwingUtils.setEnableContainer(vistaDescargas.getRadioBtnTXT(), true);
	}

	private void habilitarCamposTapa() {
		SwingUtils.setEnableContainer(vistaDescargas.getPanelFecha(), true);
	}

	public void descargaFinalizada() {
		SwingUtils.setEnableContainer(this.vistaDescargas.getFrame(), true);
		if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
			vistaDescargas.getRadioBtnDescargarTapas().doClick();
		}
		String msjDescarga = this.modeloDescarga.isDescargaDetenida() ? "Detenido" : "Descarga Finalizada";
		this.resultadoDescargas.setTitle(msjDescarga);
		resultadoDescargas.getBtnCerrar().setText("Cerrar");
		this.resultadoDescargas.getTextArea().setText(this.modeloDescarga.getInformeDescarga().toString());
		this.resultadoDescargas.getBtnGuardar().setVisible(true);
	}

	private boolean validacionCarpetasOk(String path) {
		File carpeta = new File(path);
		if (!carpeta.exists()) {
			mostrarMsjAUsuario("La carpeta " + path + " no existe!", "Alerta", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!carpeta.canRead() || !carpeta.canWrite()) {
			mostrarMsjAUsuario("No tiene los permisos suficientes en la carpeta " + path + ". Elija otra carpeta",
					"Alerta", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private String ofrecerAgregarCarpetaDestino(String path) {
		if (!mostrarOpcionCrearCarpetaDestino) {
			return path;
		}
		boolean agregarCarpeta = vistaDescargas
				.solicitarRespuestaAUsuario("¿Desea agregar carpetas extras para separar las descargas? Si eligió la ruta a su gusto, elija NO");
		mostrarOpcionCrearCarpetaDestino = false;
		if (agregarCarpeta) {
			String nombreDiarioAAgregar = "";
			if (vistaDescargas.getRadioBtnLaNacion().isSelected()) {
				nombreDiarioAAgregar = vistaDescargas.getRadioBtnLaNacion().getText();
			} else if (vistaDescargas.getRadioBtnPagina12().isSelected()) {
				nombreDiarioAAgregar = vistaDescargas.getRadioBtnPagina12().getText();
			}

			String newPath = path + File.separator + nombreDiarioAAgregar;

			String modoDescarga = "";
			if (vistaDescargas.getRadioBtnDescargarTapas().isSelected()) {
				modoDescarga = vistaDescargas.getRadioBtnDescargarTapas().getText();
			} else if (vistaDescargas.getRadioBtnDescargarNotas().isSelected()) {
				modoDescarga = vistaDescargas.getRadioBtnDescargarNotas().getText();
				if (vistaDescargas.getRadioBtnHTML().isSelected()) {
					modoDescarga += File.separator + vistaDescargas.getRadioBtnHTML().getText();
				} else if (vistaDescargas.getRadioBtnTXT().isSelected()) {
					modoDescarga += File.separator + vistaDescargas.getRadioBtnTXT().getText();
				}
			}
			newPath += File.separator + modoDescarga;

			// Quizá ya existe la ruta... si es así, devuelvo esa ruta!
			File newDirs = new File(newPath);
			if (newDirs.exists()) {
				return newPath;
			}

			boolean creacionExitosa = new File(newPath).mkdirs();
			if (!creacionExitosa) {
				vistaDescargas
						.mostrarMsjAUsuario(
								"No se pudo crear la carpeta! Puede que no tenga permisos suficientes o ya exista la carpeta. Elija otra",
								"Alerta!", JOptionPane.WARNING_MESSAGE);
				vistaDescargas.getBtnAgregarCarpetaDestino().doClick();
				return "";
			} else {
				path = newPath;
			}
		}
		return path;
	}
}
