package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;

public class GetLearntWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	final String mainFont = new String ("Brandon Grotesque Regular");
	private JTextField textFieldRadius;
	public String inputFile1;
	public String inputFile2;
	public static MapPanel mPanel;
	public static JLayeredPane layeredPane;
	
	public int radius;
	public boolean isSquare;
	
	// For visualizations
	public static int previewScale = 60;
	public static Layer previewLayer1 = new Layer("", "raster3x4.txt");
	public static Layer previewLayer2 = new Layer("", "zoneRaster3x4.txt");
	private int xStart = 300;
	private int yStart = 109;
	private int xWidth = previewLayer1.nCols * previewScale;
	private int yWidth = previewLayer1.nRows * previewScale;
	
	// Launch the application
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetLearntWindow window = new GetLearntWindow();
					window.newWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Create the application
	public GetLearntWindow() {
		Color mainColor = TestGUI.mainColor;
		Color crazyColor = TestGUI.crazyColor;
		Color highlightColor = TestGUI.highlightColor;
		
		newWindow = new JFrame();
		newWindow.setTitle("GET LEARNT");
		newWindow.setBounds(400, 100, 800, 420);
		
		layeredPane = new JLayeredPane();
		newWindow.setContentPane(layeredPane);
		layeredPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 786, 383);
		panel.setLayout(null);
		panel.setBackground(mainColor);
		layeredPane.add(panel);
		
		// Layer inputs
		JLabel lblInput = new JLabel("Choose input layer:");
		lblInput.setForeground(new Color(255, 255, 255));
		lblInput.setFont(new Font(mainFont, Font.PLAIN, 14));
		lblInput.setBounds(10, 109, 239, 14);
		lblInput.setVisible(false);
		panel.add(lblInput);
		
		JLabel lblInput2 = new JLabel("Choose input layer:");
		lblInput2.setForeground(Color.WHITE);
		lblInput2.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblInput2.setBounds(10, 164, 239, 14);
		lblInput2.setVisible(false);
		panel.add(lblInput2);
		
		JComboBox comboBoxInput1 = new JComboBox();
		comboBoxInput1.setBounds(10, 133, 162, 21);
		comboBoxInput1.setVisible(false);
		panel.add(comboBoxInput1);
		
		JComboBox comboBoxInput2 = new JComboBox();
		comboBoxInput2.setBounds(10, 188, 162, 21);
		comboBoxInput2.setVisible(false);
		panel.add(comboBoxInput2);
		
		// Radius inputs
		JLabel lblRadius = new JLabel("Radius");
		lblRadius.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRadius.setForeground(new Color(255, 255, 255));
		lblRadius.setBounds(10, 161, 58, 21);
		lblRadius.setVisible(false);
		panel.add(lblRadius);
		
		textFieldRadius = new JTextField();
		textFieldRadius.setBounds(76, 164, 96, 19);
		textFieldRadius.setVisible(false);
		panel.add(textFieldRadius);
		textFieldRadius.setColumns(10);
		
		textFieldRadius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textFieldRadius.getText();
				if(!text.equals("")) {
					radius = Integer.parseInt(text);
				}
			}
		});
		
		// Neighborhood type inputs
		JLabel lblNeighborhoodType = new JLabel("Neighborhood type");
		lblNeighborhoodType.setForeground(Color.WHITE);
		lblNeighborhoodType.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNeighborhoodType.setBounds(10, 188, 131, 21);
		lblNeighborhoodType.setVisible(false);
		panel.add(lblNeighborhoodType);
		
		JComboBox comboBoxNeighborhood = new JComboBox();
		comboBoxNeighborhood.setBounds(10, 219, 162, 21);
		comboBoxNeighborhood.setVisible(false);
		panel.add(comboBoxNeighborhood);
		
		comboBoxNeighborhood.addItem("SQUARE");
		comboBoxNeighborhood.addItem("CIRCLE");
		
		isSquare = false;
		comboBoxNeighborhood.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) comboBoxNeighborhood.getSelectedItem();
				if (selectedItem.equals("Square")) {
					isSquare = true;
				} else {
					isSquare = false;
				}
			}
		});
		
		// Run buttons
		JButton btnLocalRun = new JButton("Run");
		btnLocalRun.setBounds(97, 352, 75, 21);
		btnLocalRun.setBackground(crazyColor);
		btnLocalRun.setVisible(false);
		panel.add(btnLocalRun);
		
		JButton btnFocalRun = new JButton("Run");
		btnFocalRun.setBounds(97, 352, 75, 21);
		btnFocalRun.setBackground(crazyColor);
		btnFocalRun.setVisible(false);
		panel.add(btnFocalRun);
		
		JButton btnZonalRun = new JButton("Run");
		btnZonalRun.setBounds(97, 352, 75, 21);
		btnZonalRun.setBackground(crazyColor);
		btnZonalRun.setVisible(false);
		panel.add(btnZonalRun);
		
		// Cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel.setBounds(10, 352, 75, 21);
		btnCancel.setBackground(highlightColor);
		btnCancel.setVisible(false);
		panel.add(btnCancel);
		
		// Choosing operation
		JButton btnLocal = new JButton("Local Operations");
		btnLocal.setBounds(10, 10, 225, 68);
		btnLocal.setBackground(crazyColor);
		panel.add(btnLocal);
		
		JButton btnFocal = new JButton("Focal Operations");
		btnFocal.setBounds(280, 10, 225, 68);
		btnFocal.setBackground(crazyColor);
		panel.add(btnFocal);
		
		JButton btnZonal = new JButton("Zonal Operations");
		btnZonal.setBounds(551, 10, 225, 68);
		btnZonal.setBackground(crazyColor);
		panel.add(btnZonal);
		
		// Event handlers
		btnLocal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Button colors
				btnLocal.setBackground(highlightColor);
				btnFocal.setBackground(crazyColor);
				btnZonal.setBackground(crazyColor);
				
				// Input options
				lblInput.setVisible(true);
				lblInput.setText("Choose input layer:");
				comboBoxInput1.setVisible(true);
				
				lblInput2.setVisible(true);
				lblInput2.setText("Choose input layer:");
				comboBoxInput2.setVisible(true);
				
				lblRadius.setVisible(false);
				textFieldRadius.setVisible(false);
				lblNeighborhoodType.setVisible(false);
				comboBoxNeighborhood.setVisible(false);
				
				// Run button
				btnLocalRun.setVisible(true);
				btnFocalRun.setVisible(false);
				btnZonalRun.setVisible(false);
				
				// Cancel button
				btnCancel.setVisible(true);

				// Visualization
				if (mPanel != null) {
					layeredPane.remove(mPanel);
				}
				mPanel = new MapPanel(previewLayer1.toImage(), previewScale);
				layeredPane.add(mPanel);
				mPanel.setBounds(xStart, yStart, xWidth, yWidth);
				layeredPane.revalidate();
	            layeredPane.repaint();
			}
		});
		
		btnFocal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Button colors
				btnLocal.setBackground(crazyColor);
				btnFocal.setBackground(highlightColor);
				btnZonal.setBackground(crazyColor);
				
				// Input layer options
				lblInput.setVisible(true);
				lblInput.setText("Choose input layer:");
				comboBoxInput1.setVisible(true);
				lblInput2.setVisible(false);
				comboBoxInput2.setVisible(false);
				
				lblRadius.setVisible(true);
				textFieldRadius.setVisible(true);
				lblNeighborhoodType.setVisible(true);
				comboBoxNeighborhood.setVisible(true);
				
				// Run button
				btnLocalRun.setVisible(false);
				btnFocalRun.setVisible(true);
				btnZonalRun.setVisible(false);
				
				// Cancel button
				btnCancel.setVisible(true);

				// Visualization
				if (mPanel != null) {
					layeredPane.remove(mPanel);
				}
				mPanel = new MapPanel(previewLayer1.toImage(), previewScale);
				layeredPane.add(mPanel);
				mPanel.setBounds(xStart, yStart, xWidth, yWidth);
				layeredPane.revalidate();
	            layeredPane.repaint();
			}
		});

		btnZonal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Button colors
				btnLocal.setBackground(crazyColor);
				btnFocal.setBackground(crazyColor);
				btnZonal.setBackground(highlightColor);
				
				// Input layer options
				lblInput.setVisible(true);
				lblInput.setText("Choose value layer:");
				comboBoxInput1.setVisible(true);
				lblInput2.setVisible(true);
				lblInput2.setText("Choose zone layer:");
				comboBoxInput2.setVisible(true);
				
				lblRadius.setVisible(false);
				textFieldRadius.setVisible(false);
				lblNeighborhoodType.setVisible(false);
				comboBoxNeighborhood.setVisible(false);
				
				// Run button
				btnLocalRun.setVisible(false);
				btnFocalRun.setVisible(false);
				btnZonalRun.setVisible(true);
				
				// Cancel button
				btnCancel.setVisible(true);

				// Visualization
				if (mPanel != null) {
					layeredPane.remove(mPanel);
				}
				mPanel = new MapPanel(previewLayer1.toImage(), previewScale);
				layeredPane.add(mPanel);
				mPanel.setBounds(xStart, yStart, xWidth, yWidth);
				layeredPane.revalidate();
	            layeredPane.repaint();
			}
		});
		
		// Handling layer inputs
		for (int i = 0; i < TestGUI.layerList.size(); i++) {
			String layerName = TestGUI.layerList.get(i).name;
			comboBoxInput1.addItem(layerName);
		}

		inputFile1 = (String) comboBoxInput1.getItemAt(0); 	//default

		comboBoxInput1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputFile1 = (String) comboBoxInput1.getSelectedItem();							
			}			
		});
		
		for (int i = 0; i < TestGUI.layerList.size(); i++) {
			String layerName = TestGUI.layerList.get(i).name;
			comboBoxInput2.addItem(layerName);
		}

		inputFile2 = (String) comboBoxInput2.getItemAt(0); 	//default

		comboBoxInput2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputFile2 = (String) comboBoxInput2.getSelectedItem();							
			}			
		});
		
		// Running the operation
		// Local
		btnLocalRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int layer1Index = comboBoxInput1.getSelectedIndex();
				Layer layer1 = TestGUI.layerList.get(layer1Index);

				int layer2Index = comboBoxInput1.getSelectedIndex();
				Layer layer2 = TestGUI.layerList.get(layer2Index);
				
				TestGUI.getScale(layer2);
				
				newWindow.setVisible(false);
				
//				try {
//					layer1.localSumLearningTest(
//							layer2, "",
//							TestGUI.scale, -10000,
//							"", TestGUI.layeredPane);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				layer1.localSumTest(layer2, "", TestGUI.layeredPane);
				
				newWindow.dispose();
			}
		});
		
		// Focal
		btnFocalRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		// Zonal
		btnZonalRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		// Cancel
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newWindow.dispose();
			}
		});
	}
}
