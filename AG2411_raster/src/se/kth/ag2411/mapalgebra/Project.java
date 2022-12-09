package se.kth.ag2411.mapalgebra;

import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import java.io.File;
//import java.io.FileFilter;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

public class Project extends JFrame {

	private JPanel contentPane;
	private int no = 0;
	private JTextField textCalculus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Project frame = new Project();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Project() {
		// Creating jFileChooser object
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		FileFilter filter = new FileFilter() {
			public String getDescription() {
				return "ASCII (*.txt)";
			}
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".txt");
				}
			}
		};
		fileChooser.addChoosableFileFilter(filter);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton openButton = new JButton("BUTTON");
		openButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
				}
//				System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			}
		});
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		openButton.setBounds(10, 38, 83, 21);
		contentPane.add(openButton);
		
		textCalculus = new JTextField();
		textCalculus.setBounds(139, 117, 217, 19);
		contentPane.add(textCalculus);
		textCalculus.setColumns(10);
	}
}
