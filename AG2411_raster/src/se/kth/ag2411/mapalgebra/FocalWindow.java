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

public class FocalWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	final String mainFont = new String ("Brandon Grotesque Regular");
	public String inputFile;
	public String outputFileName;
	public String fileName;
	public String outLayerName;
	private JTextField tfOutputFile;
	public String statisticType;
	public JTextField txtRadius;
	public int radius;
	public boolean isSquare;
	
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FocalWindow window = new FocalWindow();
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
	public FocalWindow() {

		newWindow = new JFrame();
		newWindow.setTitle("MAP ALGEBRA: Focal Operation");
		newWindow.setBounds(400, 100, 400, 420);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);
		
		// Input files
		JLabel lblInput = new JLabel("Choose input raster:");
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(23, 42, 239, 14);
		panel.add(lblInput);
		
		final JComboBox <String> cbInputFile = new JComboBox<String>();
		cbInputFile.setBounds(20, 67, 250, 23);
		GridBagConstraints gbc_cbInputFile = new GridBagConstraints();
		panel.add(cbInputFile, gbc_cbInputFile);
		
		for (int i = 0; i < TestGUI.layerList.size(); i++) {
			String layerName = TestGUI.layerList.get(i).name;
			cbInputFile.addItem(layerName);
		}

		inputFile=(String) cbInputFile.getItemAt(0); 	//default

			cbInputFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputFile = (String) cbInputFile.getSelectedItem();							
				}			
			});	
				
		final DefaultListModel<String> fileListModel = new DefaultListModel<String>();
		
		JLabel lblOutput = new JLabel("Output file name and location:");
		lblOutput.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
		lblOutput.setBounds(23, 290, 239, 14);
		panel.add(lblOutput);

		tfOutputFile = new JTextField();
		tfOutputFile.setBounds(23, 315, 247, 23);
		
		// Output file
		GridBagConstraints gbc_tfOutputFile = new GridBagConstraints();
		gbc_tfOutputFile.insets = new Insets(0, 0, 5, 5);
		gbc_tfOutputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfOutputFile.gridx = 0;
		gbc_tfOutputFile.gridy = 6;
		panel.add(tfOutputFile, gbc_tfOutputFile);
		tfOutputFile.setColumns(10);

		JButton btnOutputFile = new JButton("Choose");
		btnOutputFile.setBounds(277, 315, 88, 23);
		GridBagConstraints gbc_btnOutputFile = new GridBagConstraints();
		gbc_btnOutputFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnOutputFile.gridx = 1;
		gbc_btnOutputFile.gridy = 6;
		panel.add(btnOutputFile, gbc_btnOutputFile);
		
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
				int result = fileChooser.showSaveDialog(FocalWindow.this);
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

		// Parameters
		JLabel lblNeighborhoodType = new JLabel("Neighborhood type:");
		lblNeighborhoodType.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
		lblNeighborhoodType.setBounds(23, 143, 120, 14);
		panel.add(lblNeighborhoodType);
		
		JComboBox<String> cbNeighborhood = new JComboBox<String>();
		cbNeighborhood.setBounds(149, 140, 120, 23);
		panel.add(cbNeighborhood);
		
		cbNeighborhood.addItem("SQUARE");
		cbNeighborhood.addItem("CIRCLE");
		
		isSquare = false;
		cbNeighborhood.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) cbNeighborhood.getSelectedItem();
				if (selectedItem.equals("Square")) {
					isSquare = true;
				} else {
					isSquare = false;
				}
			}
		});
		
		JLabel lblRadius = new JLabel("Radius:");
		lblRadius.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
		lblRadius.setBounds(23, 175, 120, 14);
		panel.add(lblRadius);
		
		txtRadius = new JTextField();
		txtRadius.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRadius.setBounds(149, 174, 121, 23);
		panel.add(txtRadius);
		
		txtRadius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = txtRadius.getText();
				if(!text.equals("")) {
					radius = Integer.parseInt(text);
					System.out.println(radius);
				}
			}
		});
		
		// Statistic operation
		JLabel lblStatisticOperation = new JLabel("Statistic Operation:");
		lblStatisticOperation.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
		lblStatisticOperation.setBounds(23, 223, 239, 14);
		panel.add(lblStatisticOperation);
		
		final JComboBox <String> cbStatisticType = new JComboBox<String>();
		cbStatisticType.setBounds(23, 248, 247, 23);
		panel.add(cbStatisticType);
		
		cbStatisticType.addItem("SUM");
		cbStatisticType.addItem("VARIETY");
		cbStatisticType.addItem("PRODUCT");
//		cbStatisticType.addItem("RANKING");
				
		statisticType=(String) cbStatisticType.getItemAt(0); 

		cbStatisticType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				statisticType = (String) cbStatisticType.getSelectedItem();
			}			
		});
		
		// Run or Cancel
		JButton btnRun = new JButton("RUN");
		btnRun.setBounds(277, 349, 88, 23);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(radius < 0 || outputFileName == null) {
					JOptionPane.showMessageDialog(new JFrame(),"fail");
					return;
				}
			
				Layer inputLayer = TestGUI.layerList.get(0);
			
			
				// Match the layer name with the correct layer
				for(int i = 0; i < TestGUI.layerList.size(); i++) {
					if(TestGUI.layerList.get(i).name == inputFile) {
						inputLayer = TestGUI.layerList.get(i);
					}
				}
				
				int scale = 3;
				Layer outputLayer = TestGUI.layerList.get(0);
			
				// Perform the selected operation
				if(statisticType == "SUM") { 
					outputLayer = inputLayer.focalSum(radius, isSquare, outLayerName);
				} 
				else if (statisticType == "VARIETY") {
					outputLayer = inputLayer.focalVariety(radius, isSquare, outLayerName);
				} 
				else if (statisticType == "PRODUCT") {
					outputLayer = inputLayer.focalProduct(radius, isSquare, outLayerName);
				}
				
				TestGUI.layerList.add(outputLayer);
				TestGUI.imageList.add(outputLayer.toImage());
				TestGUI.layerNameList.addElement(outLayerName);
				
				TestGUI.mPanel = new MapPanel(outputLayer.toImage(), scale);
				TestGUI.aboveLayer = outputLayer;
				TestGUI.mPanel.repaint();
				
				outputLayer.save(outputFileName);
				
				newWindow.dispose();
			}
			});
		panel.add(btnRun);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(175, 349, 88, 23);
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