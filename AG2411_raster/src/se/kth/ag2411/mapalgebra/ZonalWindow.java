package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFormattedTextField;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.BorderLayout;
import java.awt.Choice;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class ZonalWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	final String mainFont = new String ("Brandon Grotesque Regular");
	public String inputFile;
	public String inputFile2;
	public String outputFileName;
	public String fileName;
	public String outLayerName;
	private JTextField tfOutputFile;
	public String statisticType;
	public static Layer outLayer;

	
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ZonalWindow window = new ZonalWindow();
					window.newWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ZonalWindow() {

		newWindow = new JFrame();
		newWindow.setTitle("MAP ALGEBRA: Zonal Operation");
		newWindow.setBounds(400, 100, 400, 420);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);
		
		// Input files
		// Value layer
		JLabel lblInputValue = new JLabel("Choose value raster:");
		lblInputValue.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInputValue.setBounds(10, 42, 239, 14);
		panel.add(lblInputValue);

		JComboBox <String> cbInputValFile = new JComboBox<String>();
		cbInputValFile.setBounds(10, 67, 250, 23);
		panel.add(cbInputValFile);

		for(String i:TestGUI.layerNames) {
			cbInputValFile.addItem(i);
		}

		inputFile = (String) cbInputValFile.getItemAt(0); 	//default

		cbInputValFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputFile = (String) cbInputValFile.getSelectedItem();							
			}			
		});	

		// Zone layer
		JLabel lblInputZone = new JLabel("Choose zone raster:");
		lblInputZone.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInputZone.setBounds(10, 98, 239, 14);
		panel.add(lblInputZone);

		JComboBox<String> cbInputZonFile = new JComboBox<String>();
		cbInputZonFile.setBounds(10, 123, 250, 23);
		panel.add(cbInputZonFile);
		
		for(String i:TestGUI.layerNames) {
			cbInputZonFile.addItem(i);
		}

		inputFile2 = (String) cbInputZonFile.getItemAt(0); 	//default

		cbInputZonFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputFile2 = (String) cbInputZonFile.getSelectedItem();							
			}			
		});	
		
		// Output file
		JLabel lblOutput = new JLabel("Output file name and location:");
		lblOutput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblOutput.setBounds(10, 290, 239, 14);
		panel.add(lblOutput);

		tfOutputFile = new JTextField();
		tfOutputFile.setBounds(10, 315, 247, 23);
		panel.add(tfOutputFile);

		JButton btnOutputFile = new JButton("Choose");
		btnOutputFile.setBounds(277, 315, 88, 23);
		panel.add(btnOutputFile);
		
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("."));
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
		btnOutputFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {		
				int result = fileChooser.showSaveDialog(ZonalWindow.this);
				if (result == JFileChooser.APPROVE_OPTION) {					
					outputFileName=fileChooser.getSelectedFile().getPath();
					
					fileName=fileChooser.getSelectedFile().getName();
					outLayerName = fileName;
					if(fileName.indexOf(".txt")==-1) {
						outputFileName=outputFileName+".txt";
						fileName=fileName+".txt";
					}
					fileChooser.setVisible(true);
					tfOutputFile.setText(outputFileName);
				}				
			}
		});
			
		// Statistic operation
		JLabel lblStatisticOperation = new JLabel("Statistic Operation:");
		lblStatisticOperation.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblStatisticOperation.setBounds(10, 223, 239, 14);
		panel.add(lblStatisticOperation);
		
		final JComboBox <String> cbStatisticType = new JComboBox<String>();
		cbStatisticType.setBounds(10, 248, 247, 23);
		panel.add(cbStatisticType);
		
		cbStatisticType.addItem("SUM");
		cbStatisticType.addItem("VARIETY");
		cbStatisticType.addItem("PRODUCT");
		cbStatisticType.addItem("MEAN");
		cbStatisticType.addItem("MIN");
		cbStatisticType.addItem("MAX");
				
		statisticType=(String) cbStatisticType.getItemAt(0); 

		cbStatisticType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				statisticType = (String) cbStatisticType.getSelectedItem();
				System.out.println(statisticType);
			}			
		});
		
		// Run or Cancel
		JButton btnRun = new JButton("RUN");
		btnRun.setBounds(277, 349, 88, 23);
		panel.add(btnRun);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// HERE IS A LOT STILL MISSING
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(167, 349, 88, 23);
		btnCancel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				newWindow.dispose();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panel.add(btnCancel);
	}
}