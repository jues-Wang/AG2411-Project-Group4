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
import java.awt.Toolkit;

public class LocalWindow extends JFrame {

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
	
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LocalWindow window = new LocalWindow();
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
	public LocalWindow() {
		setTitle("MAP ALGEBRA: Local Operations");

		newWindow = new JFrame();
		newWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\media\\GIShead-05.png"));
		newWindow.setTitle("MAP ALGEBRA: Local Operation");
		newWindow.setBounds(400, 100, 400, 420);
		
		JPanel panel = new JPanel();
		panel.setBackground(TestGUI.mainColor2);
		panel.setBounds(0, 49, 465, 373);
		panel.setLayout(null);
		newWindow.setContentPane(panel);
		
		// Input files
		JLabel lblInput = new JLabel("Choose two input layers:");
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(23, 42, 239, 14);
		panel.add(lblInput);
		
		final JComboBox <String> cbInputFile = new JComboBox<String>();
		cbInputFile.setBounds(20, 67, 250, 23);
		GridBagConstraints gbc_cbInputFile = new GridBagConstraints();
		panel.add(cbInputFile, gbc_cbInputFile);
	
		// !!!   Drop down input files (not working. no access to the list?)  !!!!!
		// RUN IT FIRST WITHOUT THIS PART ;)
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
					//System.out.println("选择的选项是: " + selectedItem);								
				}			
			});	
				
		final DefaultListModel<String> fileListModel = new DefaultListModel<String>();
		final JList<String> listFileList = new JList<String>(fileListModel);
		listFileList.setBounds(20, 101, 250, 47);
		GridBagConstraints gbc_listFileList = new GridBagConstraints();
		panel.add(listFileList, gbc_listFileList);
		
		JButton btnAdd = new JButton ("Add");
		btnAdd.setBounds(277, 67, 88, 23);
		btnAdd.setFont(new Font(mainFont, Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(fileListModel.getSize()<2) {
					String selectItem=(String) cbInputFile.getSelectedItem();
					fileListModel.addElement(selectItem);
				}
			}

		});
		panel.add(btnAdd);	
		
		JLabel lblOutput = new JLabel("Output file name and location:");
		lblOutput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblOutput.setBounds(23, 168, 239, 14);
		panel.add(lblOutput);
		
		JButton btnDelete = new JButton ("Delete");
		btnDelete.setBounds(277, 101, 88, 23);
		btnDelete.setFont(new Font(mainFont, Font.PLAIN, 12));
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = listFileList.getSelectedIndex();
				if(index>=0) {
					fileListModel.remove(index);
				}				
			}

		});
		panel.add(btnDelete);
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = listFileList.getSelectedIndex();
				if(index>=0) {
					fileListModel.remove(index);
				}				
			}

		});

		tfOutputFile = new JTextField();
		tfOutputFile.setBounds(20, 193, 250, 23);
		
		// Output file
		GridBagConstraints gbc_tfOutputFile = new GridBagConstraints();
		gbc_tfOutputFile.insets = new Insets(0, 0, 5, 5);
		gbc_tfOutputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfOutputFile.gridx = 0;
		gbc_tfOutputFile.gridy = 6;
		panel.add(tfOutputFile, gbc_tfOutputFile);
		tfOutputFile.setColumns(10);

		JButton btnOutputFile = new JButton("Browse");
		btnOutputFile.setBounds(277, 193, 88, 23);
		btnOutputFile.setFont(new Font(mainFont, Font.PLAIN, 12));
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
				int result = fileChooser.showSaveDialog(LocalWindow.this);
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
		lblStatisticOperation.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
		lblStatisticOperation.setBounds(23, 227, 239, 14);
		panel.add(lblStatisticOperation);
		
		final JComboBox <String> cbStatisticType = new JComboBox<String>();
		cbStatisticType.setBounds(20, 252, 250, 23);
		panel.add(cbStatisticType);
		

		
		cbStatisticType.addItem("SUM");
		cbStatisticType.addItem("DIFF");
		
		statisticType=(String) cbStatisticType.getItemAt(0); 

		cbStatisticType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub				
				statisticType = (String) cbStatisticType.getSelectedItem();
				System.out.println(statisticType);
			}			
		});
		
		// Run or Cancel
		JButton btnRun = new JButton("RUN");
		btnRun.setFont(new Font(mainFont, Font.BOLD, 12));
		
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(outputFileName == null) {
					JOptionPane.showMessageDialog(new JFrame(),"Epic fail.");
					return;
				}
				Layer layer1 = TestGUI.layerList.get(0);
				Layer layer2 = TestGUI.layerList.get(0);
				// Match the layer names with the layers
				for(int i = 0; i < TestGUI.layerList.size(); i++) {
					if(TestGUI.layerList.get(i).name == fileListModel.get(0)) {
						layer1 = TestGUI.layerList.get(i);
					}
					if(TestGUI.layerList.get(i).name == fileListModel.get(1)) {
						layer2 = TestGUI.layerList.get(i);
					}
				}
				
				if (layer1.nCols != layer2.nCols || layer1.nRows != layer2.nRows) {
					JOptionPane.showMessageDialog(new JFrame(),"Size error: Input layers are of different sizes, please input two layers of the same size.");
					return;
				}
				
				Layer outputLayer = TestGUI.layerList.get(0);
				
				// Perform the selected operation
				if(statisticType == "SUM") {
					outputLayer = layer1.localSum(layer2, outLayerName);
				} 
				else if (statisticType == "DIFF") {
					outputLayer = layer1.localDifference(layer2, outLayerName);
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
		btnRun.setBounds(277, 349, 88, 23);
		panel.add(btnRun);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.setFont(new Font(mainFont, Font.BOLD, 12));
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
