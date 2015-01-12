package vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class VistaResultadoDescarga extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnGuardar;
	private JButton btnCerrar;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Create the frame.
	 */
	public VistaResultadoDescarga() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBounds(10, 11, 600, 265);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setTitle("R");

		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(121, 291, 95, 23);
		contentPane.add(btnGuardar);

		btnCerrar = new JButton("Cerrar");
		btnCerrar.setBounds(281, 291, 89, 23);
		contentPane.add(btnCerrar);


		textArea = new JTextArea();

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 0, 600, 265);
		contentPane.add(scrollPane);
	}
}
