package vista;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class DescargarNotas {

	private JFrame frmPagedownloader;
	private JButton buttonProcesar;
	private JButton btnSalir;
	private JTextField textFieldCarpetaOrigen;
	private JTextField textFieldCarpetaDestino;
	public static String MODO_DESCARGA = "Notas";

	public DescargarNotas() {
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

	public JButton getButtonProcesar() {
		return buttonProcesar;
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPagedownloader = new JFrame();
		frmPagedownloader.setBounds(100, 100, 650, 350);
		frmPagedownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPagedownloader.getContentPane().setLayout(null);
		frmPagedownloader.setTitle("Descargar Notas");
		frmPagedownloader.setFont(new Font("Arial", Font.PLAIN, 12));

		// DIARIO
		JPanel panelDiario = new JPanel();
		panelDiario.setSize(500, 40);
		panelDiario.setLocation(22, 10);
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

		ButtonGroup grupoDiario = new ButtonGroup();

		JRadioButton radioBtnPagina12 = new JRadioButton("Página 12");
		radioBtnPagina12.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnPagina12.setBounds(80, 0, 100, 20);
		panelDiario.add(radioBtnPagina12);

		JRadioButton radioBtnLaNacion = new JRadioButton("La Nación");
		radioBtnLaNacion.setFont(new Font("Arial", Font.PLAIN, 12));
		radioBtnLaNacion.setBounds(185, 0, 100, 20);
		panelDiario.add(radioBtnLaNacion);

		grupoDiario.add(radioBtnLaNacion);
		grupoDiario.add(radioBtnPagina12);

		// SECCION
		JPanel panelSeccion = new JPanel();
		panelSeccion.setSize(500, 40);
		panelSeccion.setLocation(22, 60);
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

		JCheckBox chckbxEconomia = new JCheckBox("Economía");
		chckbxEconomia.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxEconomia.setBounds(80, 0, 100, 20);
		panelSeccion.add(chckbxEconomia);

		// CARPETA ORIGEN
		JPanel panelcarpetaOrigen = new JPanel();
		panelcarpetaOrigen.setSize(630, 60);
		panelcarpetaOrigen.setLocation(22, 110);
		panelcarpetaOrigen.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelcarpetaOrigen.setLayout(null);
		frmPagedownloader.getContentPane().add(panelcarpetaOrigen);

		JLabel lblCarpetaOrigen = new JLabel("Carpeta Origen");
		lblCarpetaOrigen.setToolTipText("Ruta donde se encuentran las \"Tapas\" html descargadas");
		lblCarpetaOrigen.setFont(new Font("Arial", Font.BOLD, 14));
		lblCarpetaOrigen.setBounds(0, 0, 120, 23);
		panelcarpetaOrigen.add(lblCarpetaOrigen);

		JSeparator separatorCarpetaOrigen = new JSeparator();
		separatorCarpetaOrigen.setBounds(0, 25, 500, 3);
		panelcarpetaOrigen.add(separatorCarpetaOrigen);

		textFieldCarpetaOrigen = new JTextField();
		textFieldCarpetaOrigen.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldCarpetaOrigen.setEditable(false);
		textFieldCarpetaOrigen.setColumns(10);
		textFieldCarpetaOrigen.setBounds(0, 30, 500, 23);
		panelcarpetaOrigen.add(textFieldCarpetaOrigen);

		JButton btnAgregarCarpetaOrigen = new JButton("Agregar");
		btnAgregarCarpetaOrigen.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAgregarCarpetaOrigen.setBounds(520, 30, 85, 20);
		panelcarpetaOrigen.add(btnAgregarCarpetaOrigen);

		// CARPETA DESTINO
		JPanel panelCarpetaDestino = new JPanel();
		panelCarpetaDestino.setSize(630, 60);
		panelCarpetaDestino.setLocation(22, 180);
		panelCarpetaDestino.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelCarpetaDestino.setLayout(null);
		frmPagedownloader.getContentPane().add(panelCarpetaDestino);

		JLabel lblCarpetaDestino = new JLabel("Carpeta Destino");
		lblCarpetaDestino.setToolTipText("Ruta donde se guardarán las tapas descargadas");
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

		JButton btnAgregarCarpetaDestino = new JButton("Agregar");
		btnAgregarCarpetaDestino.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAgregarCarpetaDestino.setBounds(520, 30, 85, 20);
		panelCarpetaDestino.add(btnAgregarCarpetaDestino);

		// BOTONES PROCESAR-SALIR
		JPanel panelBotonesProcesar_Salir = new JPanel();
		panelBotonesProcesar_Salir.setSize(230, 40);
		panelBotonesProcesar_Salir.setLocation(175, 260);
		panelBotonesProcesar_Salir.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesProcesar_Salir.setLayout(null);
		frmPagedownloader.getContentPane().add(panelBotonesProcesar_Salir);

		buttonProcesar = new JButton("Procesar");
		buttonProcesar.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonProcesar.setBounds(10, 0, 100, 35);
		panelBotonesProcesar_Salir.add(buttonProcesar);

		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		btnSalir.setBounds(120, 0, 100, 35);
		panelBotonesProcesar_Salir.add(btnSalir);
	}

	public static boolean solicitarRespuestaAUsuario(String consulta) {
		int dialogButton = JOptionPane.showConfirmDialog(null, consulta, "Alerta", JOptionPane.YES_NO_OPTION);
		return dialogButton == 0 ? true : false;
	}

	public static void mostrarMsjAUsuario(String msj, String title, int warningMessage) {
		JOptionPane.showMessageDialog(null, msj, title, warningMessage);
	}

	private void close() {
		if (JOptionPane.showConfirmDialog(null, "¿Desea realmente salir del sistema?", "Salir del sistema",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			System.exit(0);
	}
}
