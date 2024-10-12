package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import businesslogic.BLFacade;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabelSelectOption;

	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	public AdminGUI(String username) {
		AdminGUI.setBussinessLogic(LoginGUI.getBusinessLogic());

		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Admin"));
		this.setSize(495, 290);

		setjLabelSelectOption();

		JButton deskontu =setjButtonDeskontu(username);

		JButton kude= setJButtonkude(username);

		JButton ezabatu = setjButtonEzabatu();

		JButton itxi = setjButtonItxi();

		setjContentPane(itxi, kude, ezabatu, deskontu);

		setContentPane(jContentPane);

	}

	private void setjContentPane(JButton jButtonItxi, JButton jButtonkude, JButton jButtonEzabatu, JButton jButtonDeskontu) {
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(6, 1, 0, 0));
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonDeskontu);
		jContentPane.add(jButtonkude);
		jContentPane.add(jButtonEzabatu);
		jContentPane.add(jButtonItxi);
	}

	private JButton setjButtonItxi() {
		JButton jButtonItxi = new JButton();
		jButtonItxi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});
		jButtonItxi.setText(ResourceBundle.getBundle("Etiquetas").getString("EgoeraGUI.Close"));
		
		return jButtonItxi;
	}

	private JButton setjButtonEzabatu() {
		JButton jButtonEzabatu = new JButton();
		jButtonEzabatu.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Ezab"));
		jButtonEzabatu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new EzabatuGUI();
				a.setVisible(true);
			}
		});
		
		return jButtonEzabatu;
	}

	private JButton setJButtonkude(String username) {
		JButton jButtonkude = new JButton();
		jButtonkude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new DeskontuKudeatuGUI(username);
				a.setVisible(true);
			}
		});
		jButtonkude.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Kudea"));
		
		return jButtonkude;
	}

	private JButton setjButtonDeskontu(String username) {
		JButton jButtonDeskontu = new JButton();
		jButtonDeskontu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new DeskontuaGUI(username);
				a.setVisible(true);
			}
		});
		jButtonDeskontu.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Deskontua"));
		return jButtonDeskontu;
	}

	private void setjLabelSelectOption() {
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Admin"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

}
