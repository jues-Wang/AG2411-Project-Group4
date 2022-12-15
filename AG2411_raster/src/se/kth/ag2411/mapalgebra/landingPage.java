package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Button;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class landingPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	int xx,xy;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					landingPage frame = new landingPage();
					frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public landingPage() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 350, 450);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(new Color (0, 41, 61));
		panel.setBounds(0, 0, 350, 450);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(".\\media\\landing-07.png"));
		lblNewLabel.setBounds(54, 256, 83, 77);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(".\\media\\landing-10.png"));
		lblNewLabel_1.setBounds(253, 245, 83, 77);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("");
		lblNewLabel_1_1.setIcon(new ImageIcon(".\\media\\landing-09.png"));
		lblNewLabel_1_1.setBounds(0, 242, 83, 77);
		panel.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2_1 = new JLabel("");
		lblNewLabel_1_2_1.setIcon(new ImageIcon(".\\media\\landing-11.png"));
		lblNewLabel_1_2_1.setBounds(166, 231, 83, 77);
		panel.add(lblNewLabel_1_2_1);

		// Adding slogan or title + description
		JLabel slogan = new JLabel("<html>My Little FunGIS<html>");
		slogan.setHorizontalAlignment(SwingConstants.RIGHT);
		slogan.setFont(new Font("Ravie", Font.BOLD, 36));
		slogan.setForeground(new Color(255, 255, 255));
		slogan.setBounds(20, 97, 288, 107);
		panel.add(slogan);

		JLabel description = new JLabel("<html>Must have for 2023! Perfect as a christmas present for your colleagues. What can I do with the Application? Slogan slogan slogan.<html>");
		description.setVerticalAlignment(SwingConstants.TOP);
		description.setHorizontalAlignment(SwingConstants.RIGHT);
		description.setForeground(new Color(240, 248, 255));
		description.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
		description.setBounds(20, 334, 268, 59);
		panel.add(description);

		// Close "button"
		JLabel close = new JLabel("EXIT");
		close.setForeground(Color.WHITE);
		close.setBounds(295, 0, 51, 27);
		panel.add(close);
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				System.exit(0);
			}
		});
		close.setHorizontalAlignment(SwingConstants.CENTER);
		close.setForeground(Color.BLACK);
		close.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 15));

		// Adding the picture
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 350, 300);
		panel.add(label);
		label.setIcon(new ImageIcon(".\\media\\color2.png"));
		label.setVerticalAlignment(SwingConstants.TOP);

		Button button = new Button("Get started!");
		button.setBounds(180, 404, 156, 27);
		panel.add(button);
		button.setActionCommand("GetStarted");
		button.setForeground(new Color (0, 41, 61));
		button.setBackground(new Color (255, 208, 47));
		panel.add(button);

		JLabel description_1 = new JLabel("<html>Carl Vilhelm Boström \nAgni Kontorini \nWeihua Wang  \nMing Yang \nJasmin Zdovc<html>");
		description_1.setVerticalAlignment(SwingConstants.TOP);
		description_1.setHorizontalAlignment(SwingConstants.RIGHT);
		description_1.setForeground(new Color(240, 248, 255));
		description_1.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 6));
		description_1.setBounds(20, 398, 63, 64);
		panel.add(description_1);
		
		// Open the MAIN Application when clicking this button
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TestGUI.main(null);
			}
		});

		// Moving the whole window when mouse pressed
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				xx = e.getX();
				xy = e.getY();
			}
		});
		label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				int x = arg0.getXOnScreen();
				int y = arg0.getYOnScreen();
				landingPage.this.setLocation(x - xx, y - xy);  
			}
		});
	}
}