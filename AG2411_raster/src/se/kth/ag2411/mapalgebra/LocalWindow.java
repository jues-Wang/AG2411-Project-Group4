package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Choice;

public class LocalWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public void NewWindow(String operationName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LocalWindow window = new LocalWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LocalWindow() {
		
		final String mainFont = new String ("Brandon Grotesque Regular");
		
		frame = new JFrame();
		frame.setBounds(400, 200, 443, 336);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel headPanel = new JPanel();
		headPanel.setBounds(0, 0, 436, 43);
		headPanel.setBackground(new Color(0, 41, 61));
		frame.getContentPane().add(headPanel);
		headPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("LocalSum");	// TestGUI.JMenuItem. ....
		lblNewLabel.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 81, 24);
		lblNewLabel.setForeground(new Color (187, 202, 192));	// change to TestGUI.mainColor2 new Color (187, 202, 192)
		headPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Input raster 1:");
		lblNewLabel_1.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 69, 79, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Output raster:");
		lblNewLabel_1_1.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(10, 139, 89, 14);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Input raster 2:");
		lblNewLabel_1_2.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblNewLabel_1_2.setBounds(10, 94, 79, 21);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(337, 66, 89, 23);
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setBounds(95, 95, 232, 20);
		frame.getContentPane().add(formattedTextField_1);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.setBounds(337, 94, 89, 23);
		frame.getContentPane().add(btnBrowse_1);
		
		JFormattedTextField formattedTextField_1_1 = new JFormattedTextField();
		formattedTextField_1_1.setBounds(95, 137, 232, 20);
		frame.getContentPane().add(formattedTextField_1_1);
		
		JButton btnBrowse_1_1 = new JButton("Browse");
		btnBrowse_1_1.setBounds(337, 136, 89, 23);
		frame.getContentPane().add(btnBrowse_1_1);
		
		Choice choice = new Choice();
		choice.setBounds(95, 69, 232, 18);
		// add all the file names from TOC 
		choice.add("vegetation.txt");	// show every item of a list that stores all the filenames of TOC
		choice.add("development.txt");
		choice.add("hydrology.txt");
		
		frame.getContentPane().add(choice);
	

	
	// Choosing a file.
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileFilter() {
				
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
			});
		
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setMultiSelectionEnabled(true);
	
			
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int result = fileChooser.showOpenDialog(LocalWindow.this);
					
					if (result == JFileChooser.APPROVE_OPTION) {
						File[] selectedFiles = fileChooser.getSelectedFiles();
						for (int i = 0; i < selectedFiles.length; i++) {
							System.out.println("Selected file: " + selectedFiles[i].getAbsolutePath());
							
							}
						
						}
				}});
			frame.getContentPane().add(btnBrowse);
	}
}
