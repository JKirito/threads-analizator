package vista;

import java.awt.Font;
import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import entities.FormatoHtml;
import entities.FormatoTexto;
import entities.LaNacion;
import entities.Pagina12;
import entities.SeccionEconomia;

public class VistaDescargas {

	private JFrame frmPagedownloader;
	private JPanel panelModoDescarga;
	private JPanel panelDiario;
	private ButtonGroup grupoModoDescarga;
	private ButtonGroup grupoDiario;
	private ButtonGroup grupoOutput;
	private JRadioButton radioBtnDescargarTapas;
	private JRadioButton radioBtnDescargarNotas;
	private JRadioButton radioBtnPagina12;
	private JRadioButton radioBtnLaNacion;
	private JRadioButton radioBtnHTML;
	private JRadioButton radioBtnTXT;
	private JPanel panelSeccion;
	private JCheckBox chckbxEconomia;
	private JCheckBox chckbxSobreescribir;
	private JPanel panelCarpetaOrigen;
	private JButton btnAgregarCarpetaOrigen;
	private JPanel panelCarpetaDestino;
	private JButton btnAgregarCarpetaDestino;
	private JPanel panelFecha;
	private JSpinner spinnerCantDias;
	private JPanel panelBotonesProcesar_Salir;
	private JDateChooser dateChooser;
	private JButton buttonDescargar;
	private JButton btnSalir;
	private JTextField textFieldCarpetaOrigen;
	private JTextField textFieldCarpetaDestino;

	public VistaDescargas() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
	}

	public JFrame getFrame() {
		return frmPagedownloader;
	}

	public JDateChooser getDateChooser() {
		return dateChooser;
	}

	public JButton getButtonDescargar() {
		return buttonDescargar;
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}

	public JPanel getPanelModoDescarga() {
		return panelModoDescarga;
	}

	public JPanel getPanelDiario() {
		return panelDiario;
	}

	public ButtonGroup getGrupoModoDescarga() {
		return grupoModoDescarga;
	}

	public ButtonGroup getGrupoDiario() {
		return grupoDiario;
	}

	public ButtonGroup getGrupoOutput() {
		return grupoOutput;
	}

	public JRadioButton getRadioBtnDescargarTapas() {
		return radioBtnDescargarTapas;
	}

	public JRadioButton getRadioBtnDescargarNotas() {
		return radioBtnDescargarNotas;
	}

	public JRadioButton getRadioBtnPagina12() {
		return radioBtnPagina12;
	}

	public JRadioButton getRadioBtnLaNacion() {
		return radioBtnLaNacion;
	}

	public JPanel getPanelSeccion() {
		return panelSeccion;
	}

	public JCheckBox getChckbxEconomia() {
		return chckbxEconomia;
	}

	public JPanel getPanelCarpetaOrigen() {
		return panelCarpetaOrigen;
	}

	public JButton getBtnAgregarCarpetaOrigen() {
		return btnAgregarCarpetaOrigen;
	}

	public JPanel getPanelCarpetaDestino() {
		return panelCarpetaDestino;
	}

	public JButton getBtnAgregarCarpetaDestino() {
		return btnAgregarCarpetaDestino;
	}

	public JPanel getPanelFecha() {
		return panelFecha;
	}

	public JSpinner getSpinnerCantDias() {
		return spinnerCantDias;
	}

	public JPanel getPanelBotonesProcesar_Salir() {
		return panelBotonesProcesar_Salir;
	}

	public JTextField getTextFieldCarpetaOrigen() {
		return textFieldCarpetaOrigen;
	}

	public JTextField getTextFieldCarpetaDestino() {
		return textFieldCarpetaDestino;
	}

	public JRadioButton getRadioBtnHTML() {
		return radioBtnHTML;
	}

	public JRadioButton getRadioBtnTXT() {
		return radioBtnTXT;
	}

	public JCheckBox getChckbxSobreescribir() {
		return chckbxSobreescribir;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPagedownloader = new JFrame();
		frmPagedownloader.setBounds(100, 100, 650, 500);
		frmPagedownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPagedownloader.setResizable(false);
		frmPagedownloader.getContentPane().setLayout(null);
		frmPagedownloader.setTitle("PageDownloader");
		frmPagedownloader.setFont(new Font("Arial", Font.PLAIN, 12));

		// MODO DESCARGA
		panelModoDescarga = new JPanel();
		panelModoDescarga.setSize(400, 40);
		panelModoDescarga.setLocation(22, 10);
		panelModoDescarga.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelModoDescarga.setLayout(null);
		frmPagedownloader.getContentPane().add(panelModoDescarga);

		JLabel lblModoDescarga = new JLabel("Modo Descarga");
		lblModoDescarga.setFont(new Font("Arial", Font.BOLD, 14));
		lblModoDescarga.setBounds(0, 0, 120, 22);
		panelModoDescarga.add(lblModoDescarga);

		JSeparator separatorModoDescarga = new JSeparator();
		separatorModoDescarga.setBounds(0, 27, 500, 3);
		panelModoDescarga.add(separatorModoDescarga);

		grupoModoDescarga = new ButtonGroup();

		radioBtnDescargarTapas = new JRadioButton("Tapas");
		radioBtnDescargarTapas.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnDescargarTapas.setBounds(120, 0, 70, 20);
		panelModoDescarga.add(radioBtnDescargarTapas);

		radioBtnDescargarNotas = new JRadioButton("Notas");
		radioBtnDescargarNotas.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnDescargarNotas.setBounds(200, 0, 70, 20);
		panelModoDescarga.add(radioBtnDescargarNotas);

		grupoModoDescarga.add(radioBtnDescargarTapas);
		grupoModoDescarga.add(radioBtnDescargarNotas);

		// DIARIO
		panelDiario = new JPanel();
		panelDiario.setSize(500, 40);
		panelDiario.setLocation(22, 50);
		panelDiario.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelDiario.setLayout(null);
		frmPagedownloader.getContentPane().add(panelDiario);

		JLabel lblDiario = new JLabel("Diario");
		lblDiario.setFont(new Font("Arial", Font.BOLD, 14));
		lblDiario.setBounds(0, 0, 65, 22);
		panelDiario.add(lblDiario);

		JSeparator separatorDiario = new JSeparator();
		separatorDiario.setBounds(0, 27, 500, 3);
		panelDiario.add(separatorDiario);

		grupoDiario = new ButtonGroup();

		radioBtnPagina12 = new JRadioButton(Pagina12.NOMBRE_DIARIO);
		radioBtnPagina12.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnPagina12.setBounds(80, 0, 100, 20);
		panelDiario.add(radioBtnPagina12);

		radioBtnLaNacion = new JRadioButton(LaNacion.NOMBRE_DIARIO);
		radioBtnLaNacion.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnLaNacion.setBounds(185, 0, 100, 20);
		panelDiario.add(radioBtnLaNacion);

		grupoDiario.add(radioBtnLaNacion);
		grupoDiario.add(radioBtnPagina12);

		// SECCION
		panelSeccion = new JPanel();
		panelSeccion.setSize(500, 40);
		panelSeccion.setLocation(22, 90);
		panelSeccion.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelSeccion.setLayout(null);
		frmPagedownloader.getContentPane().add(panelSeccion);

		JLabel lblSeccion = new JLabel("Sección");
		lblSeccion.setFont(new Font("Arial", Font.BOLD, 14));
		lblSeccion.setBounds(0, 0, 65, 22);
		panelSeccion.add(lblSeccion);

		JSeparator separatorSeccion = new JSeparator();
		separatorSeccion.setBounds(0, 27, 500, 3);
		panelSeccion.add(separatorSeccion);

		chckbxEconomia = new JCheckBox(SeccionEconomia.NOMBRE_SECCION);
		chckbxEconomia.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxEconomia.setBounds(80, 0, 100, 20);
		panelSeccion.add(chckbxEconomia);

		// CARPETA ORIGEN
		panelCarpetaOrigen = new JPanel();
		panelCarpetaOrigen.setSize(630, 60);
		panelCarpetaOrigen.setLocation(22, 130);
		panelCarpetaOrigen.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelCarpetaOrigen.setLayout(null);
		frmPagedownloader.getContentPane().add(panelCarpetaOrigen);

		JLabel lblCarpetaOrigen = new JLabel("Carpeta Origen");
		lblCarpetaOrigen.setToolTipText("Ruta donde se encuentran las \"Tapas\" html descargadas");
		lblCarpetaOrigen.setFont(new Font("Arial", Font.BOLD, 14));
		lblCarpetaOrigen.setToolTipText("Ruta donde se encuentran las 'Tapas' html ya descargadas");
		lblCarpetaOrigen.setBounds(0, 0, 120, 23);
		panelCarpetaOrigen.add(lblCarpetaOrigen);

		JSeparator separatorCarpetaOrigen = new JSeparator();
		separatorCarpetaOrigen.setBounds(0, 25, 500, 3);
		panelCarpetaOrigen.add(separatorCarpetaOrigen);

		textFieldCarpetaOrigen = new JTextField();
		textFieldCarpetaOrigen.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldCarpetaOrigen.setEditable(false);
		textFieldCarpetaOrigen.setColumns(10);
		textFieldCarpetaOrigen.setBounds(0, 30, 500, 23);
		panelCarpetaOrigen.add(textFieldCarpetaOrigen);

		btnAgregarCarpetaOrigen = new JButton("Agregar");
		btnAgregarCarpetaOrigen.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAgregarCarpetaOrigen.setBounds(520, 30, 85, 20);
		panelCarpetaOrigen.add(btnAgregarCarpetaOrigen);

		// CARPETA DESTINO
		panelCarpetaDestino = new JPanel();
		panelCarpetaDestino.setSize(630, 60);
		panelCarpetaDestino.setLocation(22, 190);
		panelCarpetaDestino.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelCarpetaDestino.setLayout(null);
		frmPagedownloader.getContentPane().add(panelCarpetaDestino);

		JLabel lblCarpetaDestino = new JLabel("Carpeta Destino");
		lblCarpetaDestino.setToolTipText("Ruta donde se guardarán las notas descargadas");
		lblCarpetaDestino.setFont(new Font("Arial", Font.BOLD, 14));
		lblCarpetaDestino.setBounds(0, 0, 125, 23);
		panelCarpetaDestino.add(lblCarpetaDestino);

		JSeparator separatorCarpetaDestino = new JSeparator();
		separatorCarpetaDestino.setBounds(0, 25, 500, 3);
		panelCarpetaDestino.add(separatorCarpetaDestino);

		textFieldCarpetaDestino = new JTextField();
		textFieldCarpetaDestino.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldCarpetaDestino.setEditable(false);
		textFieldCarpetaDestino.setColumns(10);
		textFieldCarpetaDestino.setBounds(0, 30, 500, 23);
		panelCarpetaDestino.add(textFieldCarpetaDestino);

		btnAgregarCarpetaDestino = new JButton("Agregar");
		btnAgregarCarpetaDestino.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAgregarCarpetaDestino.setBounds(520, 30, 85, 20);
		panelCarpetaDestino.add(btnAgregarCarpetaDestino);

		// FECHA
		panelFecha = new JPanel();
		panelFecha.setSize(600, 30);
		panelFecha.setLocation(22, 280);
		panelFecha.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelFecha.setLayout(null);
		frmPagedownloader.getContentPane().add(panelFecha);

		JLabel lblFechaArchivosDesde = new JLabel("Descargar Tapas hasta:");
		lblFechaArchivosDesde.setFont(new Font("Arial", Font.PLAIN, 12));
		lblFechaArchivosDesde.setBounds(0, 0, 140, 20);
		panelFecha.add(lblFechaArchivosDesde);

		dateChooser = new JDateChooser();
		dateChooser.getCalendarButton().setFont(new Font("Arial", Font.PLAIN, 12));
		dateChooser.setDateFormatString("dd-MM-yyyy");
		dateChooser.setBounds(145, 0, 110, 20);
		dateChooser.setFont(new Font("Arial", Font.PLAIN, 12));
		panelFecha.add(dateChooser);

		Label lblCantidadDias = new Label("Cantidad de dias a descargar:");
		lblCantidadDias.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCantidadDias.setBounds(270, 0, 180, 20);
		panelFecha.add(lblCantidadDias);

		spinnerCantDias = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
		spinnerCantDias.setFont(new Font("Arial", Font.PLAIN, 12));
		spinnerCantDias.setBounds(450, 0, 70, 20);
		panelFecha.add(spinnerCantDias);

		// PANEL OPCIONES
		JPanel panelOpciones = new JPanel();
		panelOpciones.setLayout(null);
		panelOpciones.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelOpciones.setBounds(22, 320, 600, 30);
		frmPagedownloader.getContentPane().add(panelOpciones);

		Label lblformatoSalida = new Label("Formato:");
		lblformatoSalida.setFont(new Font("Arial", Font.PLAIN, 12));
		lblformatoSalida.setBounds(0, 0, 58, 20);
		panelOpciones.add(lblformatoSalida);

		grupoOutput = new ButtonGroup();

		radioBtnHTML = new JRadioButton(FormatoHtml.NOMBRE);
		radioBtnHTML.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnHTML.setBounds(60, 0, 60, 20);
		panelOpciones.add(radioBtnHTML);

		radioBtnTXT = new JRadioButton(FormatoTexto.NOMBRE);
		radioBtnTXT.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnTXT.setBounds(125, 0, 60, 20);
		panelOpciones.add(radioBtnTXT);

		grupoOutput.add(radioBtnHTML);
		grupoOutput.add(radioBtnTXT);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(190, 0, 8, 20);
		panelOpciones.add(separator);

		chckbxSobreescribir = new JCheckBox("Sobreescribir");
		chckbxSobreescribir
				.setToolTipText("Si el archivo ya existe y se tilda esta opción, entonces sobreescribe el contenido. Sino, evita la descarga y pasa al próximo");
		chckbxSobreescribir.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxSobreescribir.setBounds(195, 0, 100, 20);
		panelOpciones.add(chckbxSobreescribir);

		// BOTONES DESCARGAR-SALIR
		panelBotonesProcesar_Salir = new JPanel();
		panelBotonesProcesar_Salir.setSize(230, 40);
		panelBotonesProcesar_Salir.setLocation(180, 400);
		panelBotonesProcesar_Salir.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesProcesar_Salir.setLayout(null);
		frmPagedownloader.getContentPane().add(panelBotonesProcesar_Salir);

		buttonDescargar = new JButton("Descargar");
		buttonDescargar.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonDescargar.setBounds(10, 0, 100, 35);
		panelBotonesProcesar_Salir.add(buttonDescargar);

		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
		btnSalir.setBounds(120, 0, 100, 35);
		panelBotonesProcesar_Salir.add(btnSalir);
	}

	public boolean solicitarRespuestaAUsuario(String consulta) {
		int dialogButton = JOptionPane.showConfirmDialog(null, consulta, "Alerta", JOptionPane.YES_NO_OPTION);
		return dialogButton == 0 ? true : false;
	}

	public void mostrarMsjAUsuario(String msj, String title, int warningMessage) {
		JOptionPane.showMessageDialog(null, msj, title, warningMessage);
	}

}
