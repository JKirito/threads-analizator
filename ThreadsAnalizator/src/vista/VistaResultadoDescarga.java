package vista;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VistaResultadoDescarga extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnGuardar;
	private JButton btnCerrar;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JPanel panelProgreso;
	private JProgressBar progressBar;
	private JLabel lblProgreso;

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JPanel getPanelProgreso() {
		return panelProgreso;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JLabel getLblProgreso() {
		return lblProgreso;
	}

	/**
	 * Create the frame.
	 */
	public VistaResultadoDescarga() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBounds(10, 11, 600, 265);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setTitle("R");

		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(150, 406, 95, 23);
		contentPane.add(btnGuardar);

		btnCerrar = new JButton("Cerrar");
		btnCerrar.setBounds(283, 406, 89, 23);
		contentPane.add(btnCerrar);

		textArea = new JTextArea();

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 100, 580, 250);
		contentPane.add(scrollPane);

		panelProgreso = new JPanel();
		panelProgreso.setBounds(10, 0, 550, 70);
		panelProgreso.setLayout(null);
		contentPane.add(panelProgreso);

		progressBar = new JProgressBar();
		progressBar.setBounds(25, 10, 500, 30);
		panelProgreso.add(progressBar);

		lblProgreso = new JLabel("0%");
		lblProgreso.setFont(new Font("Arial", Font.BOLD, 12));
		lblProgreso.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgreso.setBounds(150, 43, 250, 15);
		panelProgreso.add(lblProgreso);
	}
}
