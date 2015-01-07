package vista;

import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

public class DescargarTapas {

	private JFrame frmPagedownloader;
	private JDateChooser dateChooser;
	private JButton buttonProcesar;
	private JButton btnSalir;
	private JTextField textFieldCarpetaDestino;
	public static final String MODO_DESCARGA = "Tapas";

	public DescargarTapas() {
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
		frmPagedownloader.setTitle("PageDownloader");
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

		JCheckBox chckbxPagina12 = new JCheckBox("Página 12");
		chckbxPagina12.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxPagina12.setBounds(80, 0, 100, 20);
		panelDiario.add(chckbxPagina12);

		JCheckBox chckbxLaNacion = new JCheckBox("La Nación");
		chckbxLaNacion.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxLaNacion.setBounds(185, 0, 100, 20);
		panelDiario.add(chckbxLaNacion);

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

		// CARPETA DESTINO
		JPanel panelCarpetaDestino = new JPanel();
		panelCarpetaDestino.setSize(630, 60);
		panelCarpetaDestino.setLocation(22, 110);
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



		// FECHA
		JPanel panelFecha = new JPanel();
		panelFecha.setSize(600, 40);
		panelFecha.setLocation(22, 185);
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

		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Arial", Font.PLAIN, 12));
		spinner.setBounds(450, 0, 70, 20);
		panelFecha.add(spinner);



		//BOTONES PROCESAR-SALIR
		JPanel panelBotonesProcesar_Salir = new JPanel();
		panelBotonesProcesar_Salir.setSize(230, 40);
		panelBotonesProcesar_Salir.setLocation(175, 270);
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
		if (JOptionPane.showConfirmDialog(null, "¿Desea realmente salir del sistema?", "Salir del sistema", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			System.exit(0);
	}
}
