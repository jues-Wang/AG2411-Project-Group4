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
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 350, 450);
		contentPane.add(panel);
		panel.setLayout(null);

		// Adding slogan or title + description
		JLabel slogan = new JLabel("<html>Follow <br>the grid ...<html>");
		slogan.setHorizontalAlignment(SwingConstants.LEFT);
		slogan.setFont(new Font("Showcard Gothic", Font.BOLD, 36));
		slogan.setForeground(Color.BLACK);
		slogan.setBounds(20, 183, 254, 118);
		panel.add(slogan);

		JLabel description = new JLabel("<html>Must have for 2023! Perfect as a christmas present for your colleagues. What can I do with the Application? Slogan slogan slogan.<html>");
		description.setVerticalAlignment(SwingConstants.TOP);
		description.setHorizontalAlignment(SwingConstants.LEFT);
		description.setForeground(new Color(240, 248, 255));
		description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		description.setBounds(20, 319, 268, 77);
		panel.add(description);

		// Close "button"
		JLabel close = new JLabel("EXIT");
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
		close.setFont(new Font("Segoe UI", Font.PLAIN, 15));

		// Adding the picture
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 350, 300);
		panel.add(label);
		label.setIcon(new ImageIcon(".\\data\\network.jpg"));	// DON'T FORGET TO INSERT JPG IN data FOLDER !!!!
		label.setVerticalAlignment(SwingConstants.TOP);

		Button button = new Button("Get started!");
		button.setBounds(180, 404, 156, 27);
		panel.add(button);
		button.setActionCommand("GetStarted");
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(139, 153, 146));
		
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