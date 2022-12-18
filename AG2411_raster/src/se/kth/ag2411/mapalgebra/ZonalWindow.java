package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

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
		newWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\media\\GIShead-05.png"));
		newWindow.setBounds(400, 100, 310, 420);

		JPanel panel = new JPanel();
		panel.setBackground(TestGUI.mainColor2);
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);

		// Input files
		// Value layer
		JLabel lblInputValue = new JLabel("Choose value raster:");
		lblInputValue.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInputValue.setBounds(18, 47, 257, 14);
		panel.add(lblInputValue);

		JComboBox <String> cbInputValFile = new JComboBox<String>();
		cbInputValFile.setBounds(18, 66, 257, 23);
		panel.add(cbInputValFile);
		
		for (int i = 0; i < TestGUI.layerList.size(); i++) {
			String layerName = TestGUI.layerList.get(i).name;
			cbInputValFile.addItem(layerName);
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
		lblInputZone.setBounds(18, 102, 257, 14);
		panel.add(lblInputZone);

		JComboBox<String> cbInputZonFile = new JComboBox<String>();
		cbInputZonFile.setBounds(18, 122, 257, 23);
		panel.add(cbInputZonFile);

		for (int i = 0; i < TestGUI.layerList.size(); i++) {
			String layerName = TestGUI.layerList.get(i).name;
			cbInputZonFile.addItem(layerName);
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
		lblOutput.setBounds(18, 256, 257, 14);
		panel.add(lblOutput);

		tfOutputFile = new JTextField();
		tfOutputFile.setBounds(18, 275, 148, 23);
		panel.add(tfOutputFile);

		JButton btnOutputFile = new JButton("Browse");
		btnOutputFile.setBounds(190, 273, 88, 23);
		btnOutputFile.setFont(new Font(mainFont, Font.PLAIN, 14));
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
		lblStatisticOperation.setBounds(18, 177, 257, 14);
		panel.add(lblStatisticOperation);

		final JComboBox <String> cbStatisticType = new JComboBox<String>();
		cbStatisticType.setBounds(18, 196, 257, 23);
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
		btnRun.setBounds(190, 346, 88, 23);
		btnRun.setFont(new Font(mainFont, Font.BOLD, 12));
		panel.add(btnRun);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(outputFileName == null) {
					JOptionPane.showMessageDialog(new JFrame(),"Error when trying to perform the operation: No layer loaded.");
					return;
				}
				
				Layer valueLayer = TestGUI.layerList.get(0);
				Layer zoneLayer = TestGUI.layerList.get(0);
				
				// Match the layer name with the correct layer
				for(int i = 0; i < TestGUI.layerList.size(); i++) {
					if(TestGUI.layerList.get(i).name == inputFile) {
						valueLayer = TestGUI.layerList.get(i);
					}
				}
				
				for(int i = 0; i < TestGUI.layerList.size(); i++) {
					if(TestGUI.layerList.get(i).name == inputFile2) {
						zoneLayer = TestGUI.layerList.get(i);
					}
				}
				
				int scale = 3;
				Layer outputLayer = TestGUI.layerList.get(0);
			
				// Perform the selected operation
				if(statisticType == "SUM") { 
					outputLayer = valueLayer.zonalSum(zoneLayer, outLayerName);
				} 
				else if (statisticType == "VARIETY") {
					outputLayer = valueLayer.zonalVariety(zoneLayer, outLayerName);
				} 
				else if (statisticType == "PRODUCT") {
					outputLayer = valueLayer.zonalProduct(zoneLayer, outLayerName);
				} 
				else if (statisticType == "MEAN") {
					outputLayer = valueLayer.zonalMean(zoneLayer, outLayerName);
				} 
				else if (statisticType == "MIN") {
					outputLayer = valueLayer.zonalMinimum(zoneLayer, outLayerName);
				} 
				else if (statisticType == "MAX") {
					outputLayer = valueLayer.zonalMaximum(zoneLayer, outLayerName);
				}
				
				TestGUI.layerList.add(outputLayer);
				TestGUI.imageList.add(outputLayer.toImage());
				TestGUI.layerNameList.addElement(outLayerName);
				
				TestGUI.mPanel = new MapPanel(outputLayer.toImage(), scale);
				TestGUI.aboveLayer = outputLayer;
				TestGUI.mPanel.repaint();
				
				outputLayer.save(outputFileName);
				
				newWindow.dispose();
				// HERE IS A LOT STILL MISSING
			}
		});

		JButton btnCancel = new JButton("CANCEL");
		btnCancel.setBounds(80, 346, 88, 23);
		btnCancel.setFont(new Font(mainFont, Font.BOLD, 12));
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