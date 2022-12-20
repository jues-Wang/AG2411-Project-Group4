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
		newWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\media\\GIShead-05.png"));
		newWindow.setBounds(400, 100, 310, 420);
		
		JPanel panel = new JPanel();
		panel.setBackground(TestGUI.mainColor2);
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);
		
		// Input files
		JLabel lblInput = new JLabel("Choose input raster:");
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(23, 32, 239, 14);
		panel.add(lblInput);
		
		final JComboBox <String> cbInputFile = new JComboBox<String>();
		cbInputFile.setBounds(20, 57, 250, 23);
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
		
		JLabel lblOutput = new JLabel("Output file name and location:");
		lblOutput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblOutput.setBounds(23, 267, 239, 14);
		panel.add(lblOutput);
		
		// Output file
		tfOutputFile = new JTextField();
		tfOutputFile.setBounds(23, 292, 146, 23);
		panel.add(tfOutputFile);
		tfOutputFile.setColumns(10);

		JButton btnOutputFile = new JButton("Browse");
		btnOutputFile.setBounds(183, 292, 88, 23);
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
				int result = fileChooser.showSaveDialog(FocalWindow.this);
				if (result == JFileChooser.APPROVE_OPTION) {					
					outputFileName=fileChooser.getSelectedFile().getPath();
					
					fileName=fileChooser.getSelectedFile().getName();
					outLayerName = fileName;
					
					// If name already exists
					// Initial condition
					boolean inList = false;
					for (int i = 0; i < TestGUI.layerNameList.size(); i++) {
						if (fileName.equals(TestGUI.layerNameList.getElementAt(i))) {
							inList = true;
						}
					}
					// Find new name
					while (inList) {
						String tempName = fileName;
						String tempDir = outputFileName;
						int counter = 1;
						
						for(int i = 0; i < TestGUI.layerNameList.size(); i++) {
							if(fileName.equals(TestGUI.layerNameList.getElementAt(i))) {
								fileName = tempName + "_" + counter;
								outputFileName = tempDir + "_" + counter;
								outLayerName = fileName;
								counter++;
								inList = false;
							}
						}
						
						for(int i = 0; i < TestGUI.layerNameList.size(); i++) {
							if(fileName.equals(TestGUI.layerNameList.getElementAt(i))) {
								inList = true;
							}
						}
					}
					
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
		lblNeighborhoodType.setBounds(23, 108, 120, 14);
		panel.add(lblNeighborhoodType);
		
		JComboBox<String> cbNeighborhood = new JComboBox<String>();
		cbNeighborhood.setBounds(149, 105, 120, 23);
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
		lblRadius.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblRadius.setBounds(23, 140, 120, 14);
		panel.add(lblRadius);
		
		txtRadius = new JTextField();
		txtRadius.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRadius.setBounds(149, 139, 121, 23);
		panel.add(txtRadius);
		
		txtRadius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = txtRadius.getText();
				if(!text.equals("")) {
					radius = Integer.parseInt(text);
				}
			}
		});
		
		// Statistic operation
		JLabel lblStatisticOperation = new JLabel("Statistic Operation:");
		lblStatisticOperation.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblStatisticOperation.setBounds(24, 198, 239, 14);
		panel.add(lblStatisticOperation);
		
		final JComboBox <String> cbStatisticType = new JComboBox<String>();
		cbStatisticType.setBounds(24, 223, 247, 23);
		panel.add(cbStatisticType);
		
		cbStatisticType.addItem("SUM");
		cbStatisticType.addItem("VARIETY");
		cbStatisticType.addItem("PRODUCT");
		cbStatisticType.addItem("RANKING");
				
		statisticType=(String) cbStatisticType.getItemAt(0); 

		cbStatisticType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				statisticType = (String) cbStatisticType.getSelectedItem();
			}			
		});
		
		// Run or Cancel
		JButton btnRun = new JButton("RUN");
		btnRun.setFont(new Font(mainFont, Font.BOLD, 14));
		btnRun.setBounds(183, 349, 88, 23);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(radius < 0) {
					JOptionPane.showMessageDialog(new JFrame(),"Error when trying to perform the operation: Incorrect radius. Remember to press the Enter key after entering the radius.");
					return;
				} else if (outputFileName == null) {
					JOptionPane.showMessageDialog(new JFrame(),"Error when trying to perform the operation: No layer loaded.");
					return;
				}
				
				Layer inputLayer = TestGUI.layerList.get(0);
			
				// Match the layer name with the correct layer
				for(int i = 0; i < TestGUI.layerList.size(); i++) {
					if(TestGUI.layerList.get(i).name == inputFile) {
						inputLayer = TestGUI.layerList.get(i);
					}
				}
				
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
				else if (statisticType == "RANKING") {
					outputLayer = inputLayer.focalRanking(radius, isSquare, outLayerName);
				}
				
				TestGUI.layerList.add(outputLayer);
				TestGUI.imageList.add(outputLayer.toImage());
				TestGUI.layerNameList.addElement(outLayerName);
				
				TestGUI.aboveLayer = outputLayer;
				
				// Reset map pan changes
				TestGUI.mapMovedX = 0;
				TestGUI.mapMovedY = 0;
				
				TestGUI.layeredPane.remove(TestGUI.mPanel);
				
				TestGUI.getScale(outputLayer);
				TestGUI.mPanel = new MapPanel(outputLayer.toImage(), TestGUI.scale);
				
				TestGUI.getMapStartX();
				TestGUI.getMapStartY();
				TestGUI.mPanel.setBounds(TestGUI.mapStartX, TestGUI.mapStartY, 2000, 2000);	
				TestGUI.mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				TestGUI.layeredPane.add(TestGUI.mPanel);
				TestGUI.layeredPane.revalidate();
				TestGUI.layeredPane.repaint();
				
				outputLayer.save(outputFileName);
				
				newWindow.dispose();
			}
			});
		panel.add(btnRun);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.setBounds(81, 349, 88, 23);
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