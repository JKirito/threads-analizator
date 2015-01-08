package vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SeleccionarDescarga extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnDescargarTapas;
	JButton btnDescargarNotas;

	public SeleccionarDescarga() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		initialize();
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JButton getBtnDescargarTapas() {
		return btnDescargarTapas;
	}

	public void setBtnDescargarTapas(JButton btnDescargarTapas) {
		this.btnDescargarTapas = btnDescargarTapas;
	}

	public JButton getBtnDescargarNotas() {
		return btnDescargarNotas;
	}

	public void setBtnDescargarNotas(JButton btnDescargarNotas) {
		this.btnDescargarNotas = btnDescargarNotas;
	}

	private void initialize() {
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		btnDescargarTapas = new JButton("Descargar Tapas!");
		btnDescargarTapas.setToolTipText("Descargue los encabezados html del periódico que desee");
		btnDescargarTapas.setBounds(40, 105, 175, 80);
		contentPane.add(btnDescargarTapas);

		btnDescargarNotas = new JButton("Descargar Notas!");
		btnDescargarNotas.setToolTipText("Si ya posee las \"tapas\", descargue las notas!");
		btnDescargarNotas.setBounds(225, 105, 175, 80);
		contentPane.add(btnDescargarNotas);
	}
}
