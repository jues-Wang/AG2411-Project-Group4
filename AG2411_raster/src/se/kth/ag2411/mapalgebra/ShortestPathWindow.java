package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ShortestPathWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public static JFrame newWindow;
	public String inputFile;
	public String inputAlgorithm;
	public static JTextField textFieldOriginX;
	public static JTextField textFieldOriginY;
	public static JTextField textFieldDestinationX;
	public static JTextField textFieldDestinationY;

	public String outputFileName;
	public String fileName;
	public String outLayerName;
	
	public static int originX;
	public static int originY;
	public static int origin;
	
	public static int destinationX;
	public static int destinationY;
	public static int destination;
	private JTextField textFieldOutput;
	
	public static boolean isVisible;
	public static boolean choosingOrigin;
	public static boolean choosingDestination;
	
	// Launch the application
		public static void main() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ShortestPathWindow window = new ShortestPathWindow();
						window.newWindow.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		public ShortestPathWindow() {
			newWindow = new JFrame();
			newWindow.setTitle("Shortest Path");
			newWindow.setBounds(400, 100, 386, 426);

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(TestGUI.mainColor2);
			newWindow.setContentPane(panel);
			
			JLabel lblInputFile = new JLabel("Choose input layer:");
			lblInputFile.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblInputFile.setBounds(60, 17, 250, 23);
			panel.add(lblInputFile);
			
			JComboBox<String> comboBoxLayer = new JComboBox<String>();
			comboBoxLayer.setBounds(60, 50, 266, 23);
			panel.add(comboBoxLayer);
			
			for (int i = 0; i < TestGUI.layerList.size(); i++) {
				String layerName = TestGUI.layerList.get(i).name;
				comboBoxLayer.addItem(layerName);
			}
			
			inputFile = (String) comboBoxLayer.getItemAt(0);
			
			comboBoxLayer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputFile = (String) comboBoxLayer.getSelectedItem();
				}			
			});
			
			JComboBox<String> comboBoxAlgorithm = new JComboBox<String>();
			comboBoxAlgorithm.setBounds(60, 248, 266, 23);
			comboBoxAlgorithm.addItem("Dijkstra");
			panel.add(comboBoxAlgorithm);
			
			comboBoxAlgorithm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputAlgorithm = (String) comboBoxAlgorithm.getSelectedItem();
				}			
			});
			
			inputAlgorithm = comboBoxAlgorithm.getItemAt(0);
			
			JLabel lblOrigin = new JLabel("Choose origin (x, y):");
			lblOrigin.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblOrigin.setBounds(60, 83, 250, 23);
			panel.add(lblOrigin);
			
			JLabel lblDestination = new JLabel("Choose destination (x, y):");
			lblDestination.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblDestination.setBounds(60, 149, 250, 23);
			panel.add(lblDestination);
			
			JLabel lblOriginX = new JLabel("X:");
			lblOriginX.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblOriginX.setBounds(60, 116, 18, 23);
			panel.add(lblOriginX);
			
			JLabel lblOriginY = new JLabel("Y:");
			lblOriginY.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblOriginY.setBounds(129, 116, 18, 23);
			panel.add(lblOriginY);
			
			textFieldOriginX = new JTextField();
			textFieldOriginX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = textFieldOriginX.getText();
					if(!text.equals("")) {
						originX = Integer.parseInt(text);
					}
				}
			});
			textFieldOriginX.setBounds(83, 120, 30, 19);
			panel.add(textFieldOriginX);
			textFieldOriginX.setColumns(10);
			
			textFieldOriginY = new JTextField();
			textFieldOriginY.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = textFieldOriginY.getText();
					if(!text.equals("")) {
						originY = Integer.parseInt(text);
					}
				}
			});
			textFieldOriginY.setColumns(10);
			textFieldOriginY.setBounds(157, 120, 30, 19);
			panel.add(textFieldOriginY);
			
			JLabel lblDestinationX = new JLabel("X:");
			lblDestinationX.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblDestinationX.setBounds(60, 182, 18, 23);
			panel.add(lblDestinationX);
			
			textFieldDestinationX = new JTextField();
			textFieldDestinationX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = textFieldDestinationX.getText();
					if(!text.equals("")) {
						destinationX = Integer.parseInt(text);
					}
				}
			});
			textFieldDestinationX.setColumns(10);
			textFieldDestinationX.setBounds(83, 186, 30, 19);
			panel.add(textFieldDestinationX);
			
			JLabel lblDestinationY = new JLabel("Y:");
			lblDestinationY.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblDestinationY.setBounds(129, 182, 18, 23);
			panel.add(lblDestinationY);
			
			textFieldDestinationY = new JTextField();
			textFieldDestinationY.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = textFieldDestinationY.getText();
					if(!text.equals("")) {
						destinationY = Integer.parseInt(text);
					}
				}
			});
			textFieldDestinationY.setColumns(10);
			textFieldDestinationY.setBounds(157, 186, 30, 19);
			panel.add(textFieldDestinationY);
			
			JLabel lblAlgorithm = new JLabel("Choose algorithm:");
			lblAlgorithm.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblAlgorithm.setBounds(60, 215, 250, 23);
			panel.add(lblAlgorithm);
			
			JButton btnOriginPick = new JButton("PICK FROM MAP");
			btnOriginPick.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (TestGUI.mPanel == null) {
						JOptionPane.showMessageDialog(new JFrame(),"Error: No layer to choose origin from.");
						return;
					}
					TestGUI.lblLocationChoice.setText("Choose origin:");
					TestGUI.lblLocationChoice.setVisible(true);
					choosingOrigin = true;
					newWindow.setVisible(false);
				}
			});
			btnOriginPick.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			btnOriginPick.setFont(new Font("Dialog", Font.BOLD, 12));
			btnOriginPick.setBounds(197, 117, 129, 23);
			panel.add(btnOriginPick);
			
			JButton btnDestinationPick = new JButton("PICK FROM MAP");
			btnDestinationPick.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (TestGUI.mPanel == null) {
						JOptionPane.showMessageDialog(new JFrame(),"Error: No layer to choose origin from.");
						return;
					}
					TestGUI.lblLocationChoice.setText("Choose destination:");
					TestGUI.lblLocationChoice.setVisible(true);
					choosingDestination = true;
					newWindow.setVisible(false);
				}
			});
			btnDestinationPick.setFont(new Font("Dialog", Font.BOLD, 12));
			btnDestinationPick.setBounds(197, 183, 129, 23);
			panel.add(btnDestinationPick);
			
			JLabel lblOutput = new JLabel("Output file name and location:");
			lblOutput.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblOutput.setBounds(60, 281, 239, 14);
			panel.add(lblOutput);
			
			textFieldOutput = new JTextField();
			textFieldOutput.setColumns(10);
			textFieldOutput.setBounds(60, 305, 168, 23);
			panel.add(textFieldOutput);
			
			JButton btnOutputFile = new JButton("Browse");
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
					int result = fileChooser.showSaveDialog(ShortestPathWindow.this);
					if (result == JFileChooser.APPROVE_OPTION) {					
						outputFileName=fileChooser.getSelectedFile().getPath();
						
						fileName=fileChooser.getSelectedFile().getName();
						outLayerName = fileName;
						if(fileName.indexOf(".txt")==-1) {
							outputFileName=outputFileName+".txt";
							fileName=fileName+".txt";
						}
						fileChooser.setVisible(true);
						textFieldOutput.setText(outputFileName);
					}				
				}
			});
			btnOutputFile.setBounds(238, 305, 88, 23);
			panel.add(btnOutputFile);
			
			JButton btnRun = new JButton("RUN");
			btnRun.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(inputFile == null) {
						JOptionPane.showMessageDialog(new JFrame(),"Error when trying to export: No layer loaded.");
						return;
					}
					
					int layerIndex = comboBoxLayer.getSelectedIndex();
					Layer selectedLayer = TestGUI.layerList.get(layerIndex);
					
					origin = originX + originY * selectedLayer.nCols;
					destination = destinationX + destinationY * selectedLayer.nCols;
					
					Layer outputLayer = selectedLayer.dijkstra(outLayerName, origin, destination);
					
					BufferedImage outputImage = outputLayer.toImage();
					
					TestGUI.layerList.add(outputLayer);
					TestGUI.imageList.add(outputLayer.toImage());
					TestGUI.layerNameList.addElement(outLayerName);
					
					// Reset map pan changes
					TestGUI.mapMovedX = 0;
					TestGUI.mapMovedY = 0;
					
					TestGUI.layeredPane.remove(TestGUI.mPanel);
					
					TestGUI.getScale(TestGUI.aboveLayer);
					TestGUI.mPanel = new MapPanel(outputImage, TestGUI.scale);
					
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
			btnRun.setFont(new Font("Dialog", Font.BOLD, 12));
			btnRun.setBounds(274, 356, 88, 23);
			panel.add(btnRun);
			
			JButton btnCancel = new JButton("CANCEL");
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					newWindow.dispose();
				}
			});
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.setBounds(176, 356, 88, 23);
			panel.add(btnCancel);
			
			
		}
}