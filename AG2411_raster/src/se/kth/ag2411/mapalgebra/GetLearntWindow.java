package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

public class GetLearntWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	final String mainFont = new String ("Brandon Grotesque Regular");
	
	// Launch the application
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetLearntWindow window = new GetLearntWindow();
					window.newWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Create the application
	public GetLearntWindow() {
		newWindow = new JFrame();
		newWindow.setTitle("GET LEARNT");
		newWindow.setBounds(400, 100, 800, 420);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);
		
		JLabel lblInput = new JLabel("Choose input raster:");
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(23, 42, 239, 14);
		panel.add(lblInput);


	}
}
