package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFormattedTextField;
import javax.swing.JButton;
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
		
		// add that when clicking X only the localWindow closes, NOT the whole application
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
		
		// Heading of the local window
		JLabel lblOperation = new JLabel("LocalSum");	
		lblOperation.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblOperation.setBounds(10, 11, 81, 24);
		lblOperation.setForeground(new Color (187, 202, 192));	
		headPanel.add(lblOperation);
		
		JLabel lblInput = new JLabel("Input raster 1:");
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(10, 69, 79, 14);
		frame.getContentPane().add(lblInput);
		
		JLabel lblInput2 = new JLabel("Input raster 2:");
		lblInput2.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput2.setBounds(10, 94, 79, 21);
		frame.getContentPane().add(lblInput2);
		
		JLabel lblOutput = new JLabel("Output raster:");
		lblOutput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblOutput.setBounds(10, 139, 89, 14);
		frame.getContentPane().add(lblOutput);
		
		// choosing the input raster: drop down menu (files from TOC AND Browse button?)
		Choice choice = new Choice();
		choice.setBounds(95, 69, 232, 18);
		// instead add all the file names from TOC to the drop down list 
		choice.add("vegetation.txt");
		choice.add("development.txt");
		choice.add("hydrology.txt");
		frame.getContentPane().add(choice);		
		
		// should be same as above! (Input raster 2)
		JFormattedTextField formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setBounds(95, 95, 232, 20);
		frame.getContentPane().add(formattedTextField_1);
		
		// Output raster: select Folder location & write in a name...
		JFormattedTextField formattedTextField_1_1 = new JFormattedTextField();
		formattedTextField_1_1.setBounds(95, 137, 232, 20);
		frame.getContentPane().add(formattedTextField_1_1);
		
		// Browse Buttons for the inputs also or is Drop down from TOC enough?
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(337, 66, 89, 23);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.setBounds(337, 94, 89, 23);
		frame.getContentPane().add(btnBrowse_1);
	
		JButton btnBrowse_1_1 = new JButton("Browse");
		btnBrowse_1_1.setBounds(337, 136, 89, 23);
		frame.getContentPane().add(btnBrowse_1_1);

	
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
						// show file name in the text field ; iteration [i] may not be necessary?
						}
				}});
			
			frame.getContentPane().add(btnBrowse);

			JButton btnRun = new JButton("Run");
			btnRun.setBounds(337, 265, 89, 23);

			btnRun.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// RUN LOCAL SUM from Layer class using the inputs from this window
				}
			});

			frame.getContentPane().add(btnRun);
	}
}
