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

public class DistanceWindow extends JFrame {
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
	private JTextField textFieldManhattan;
	
	public static boolean choosingOrigin;
	public static boolean choosingDestination;
	private JTextField textFieldEuclidean;
	
	// Launch the application
		public static void main() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						DistanceWindow window = new DistanceWindow();
						window.newWindow.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		public DistanceWindow() {
			newWindow = new JFrame();
			newWindow.setTitle("Distance");
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
			
			JLabel lblEuclidean = new JLabel("Euclidean distance:");
			lblEuclidean.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblEuclidean.setBounds(60, 215, 250, 23);
			panel.add(lblEuclidean);
			
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
			
			JLabel lblManhattan = new JLabel("Manhattan distance:");
			lblManhattan.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblManhattan.setBounds(60, 281, 239, 14);
			panel.add(lblManhattan);
			
			textFieldManhattan = new JTextField();
			textFieldManhattan.setColumns(10);
			textFieldManhattan.setBounds(60, 305, 266, 23);
			panel.add(textFieldManhattan);
			
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
					
					double[] distances = selectedLayer.getDistance(origin, destination);
					textFieldEuclidean.setText(Double.toString(distances[0]));
					textFieldManhattan.setText(Double.toString(distances[1]));
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
			
			textFieldEuclidean = new JTextField();
			textFieldEuclidean.setColumns(10);
			textFieldEuclidean.setBounds(60, 248, 266, 23);
			panel.add(textFieldEuclidean);
		}
}