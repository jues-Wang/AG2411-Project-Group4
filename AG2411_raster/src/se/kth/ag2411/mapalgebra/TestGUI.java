package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.event.ActionEvent;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JToggleButton;
import java.awt.Button;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Label;
import se.kth.ag2411.mapalgebra.RoundedBorder;


public class TestGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	
	
	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI frame = new TestGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public TestGUI() {
		
		// Create the frame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		
		// NICE COLORS:
		Color projectDarkBlue = new Color (39, 55, 115);
		Color projectGreen = new Color (138, 215, 188);
		Color projectDarkBlue2 = new Color (39, 55, 115);
		Color projectRed = new Color (223, 81, 79);
		Color projectLightGreen = new Color (187, 202, 192);
		Color projectDarkGreen = new Color (139, 153, 146);
		Color projectDarkBlue3 = new Color (5, 0, 56);
		Color projectDarkBlue4 = new Color (0, 41, 61);
		Color projectYellow = new Color (255, 208, 47);
		
		// Choose main colors for GUI:
		final Color mainColor = projectDarkBlue4;		// for background color in head & bottom panel
		final Color mainColor2 = projectLightGreen;	// for text, borders, ...
		
		final String mainFont = new String ("Brandon Grotesque Regular");
		
		// Choosing a file.
		JFileChooser fileChooser = new JFileChooser();
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
		
		// Create the content panel.
		contentPanel = new JPanel();
		contentPanel.setForeground(Color.WHITE);
		contentPanel.setBackground(Color.DARK_GRAY);
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		// SPLIT PANEL = TOC (left) + Map Panel (right)
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(new Color(255, 255, 255));
		contentPanel.add(splitPane);
		
		final JPanel panelTOC = new JPanel();
		panelTOC.setBackground(mainColor2);
		splitPane.setLeftComponent(panelTOC);
		panelTOC.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JTextArea txtTOC = new JTextArea();	// need to change it (now we can edit the text lol)
		txtTOC.setFont(new Font(mainFont, Font.PLAIN, 12));
		txtTOC.setText("Observation rasters");
		txtTOC.setOpaque(false);
		txtTOC.setForeground(mainColor);
		txtTOC.setTabSize(40);
		panelTOC.add(txtTOC);

		final JPanel panelMAP = new JPanel();
//		panelMAP.setBackground(new Color(255, 255, 255));
		splitPane.setRightComponent(panelMAP);
		panelMAP.setLayout(new CardLayout(0, 0));

		final JLayeredPane layeredPanel = new JLayeredPane();	// to LAYER the maps ??
		panelMAP.add(layeredPanel, "name_927277592538900");
//		layeredPanel.setLayout(new CardLayout(0, 0));			// Layout is important
		
		// Initializing
		int[] backgroundColor = new int[3];
		backgroundColor[0] = 255;
		backgroundColor[1] = 255;
		backgroundColor[2] = 255;
		int width = 600;
		int height = 400;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // Dimensions?
		WritableRaster raster = image.getRaster();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				raster.setPixel(i, j, backgroundColor);
			}
		}
		int scale = 3;
		MapPanel mapPanel = new MapPanel(image, scale);
		layeredPanel.add(mapPanel, BorderLayout.CENTER);
		mapPanel.setBounds(200, 200, 300, 300);	
		mapPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// Create head Panel.
		JPanel headPanel = new JPanel();
		headPanel.setBackground(mainColor);
		contentPanel.add(headPanel, BorderLayout.NORTH);
		headPanel.setLayout(new BorderLayout(0, 0));
		
			// Fill head panel:
			// Menu bar
			int radius = 20;  	// Define radius for border of buttons in the menu bar.
			
			javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
			menuBar.setBorderPainted(false);
			menuBar.setBackground(mainColor);
			headPanel.add(menuBar);
			
			Component gap1 = Box.createHorizontalStrut(8);
			menuBar.add(gap1);
			
			//FILE
			JMenu mnFile = new JMenu("File");
			mnFile.setHorizontalAlignment(SwingConstants.CENTER);
			mnFile.setForeground(mainColor2);
			mnFile.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnFile.setBorder(new LineBorder(mainColor2, 1, true));
			mnFile.setBorder(new RoundedBorder(radius));
			menuBar.add(mnFile);
			
			Component gap2 = Box.createHorizontalStrut(20);
			menuBar.add(gap2);
			
			// EDIT
			JMenu mnEdit = new JMenu("Edit");
			mnEdit.setForeground(mainColor2);
			mnEdit.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnEdit.setBorder(new LineBorder(mainColor2, 1, true));
			mnEdit.setBorder(new RoundedBorder(radius));
			menuBar.add(mnEdit);
			
			JMenuItem mnUndo = new JMenuItem("Undo");
			mnEdit.add(mnUndo);
			
			JMenuItem mnRedo = new JMenuItem("Redo");
			mnEdit.add(mnRedo);
			
			JMenuItem mnCut = new JMenuItem("Cut");
			mnEdit.add(mnCut);
			
			JMenuItem mnCopy = new JMenuItem("Copy");
			mnEdit.add(mnCopy);
			
			JMenuItem mnPaste = new JMenuItem("Paste");
			mnEdit.add(mnPaste);
			
			JMenuItem mnDelete = new JMenuItem("Delete");
			mnEdit.add(mnDelete);
			
			Component gap3 = Box.createHorizontalStrut(20);
			menuBar.add(gap3);
			
			// TOOLBOX
			JMenu mnToolbox = new JMenu("Toolbox");
			mnToolbox.setForeground(mainColor2);
			mnToolbox.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnToolbox.setBorder(new LineBorder(mainColor2, 1, true));
			mnToolbox.setBorder(new RoundedBorder(radius));
			menuBar.add(mnToolbox);
			
			// Local operations
			JMenu mnLocal = new JMenu("Local operations");
			mnToolbox.add(mnLocal);
			
			JMenuItem mnLocalSum = new JMenuItem("LocalSum");
			mnLocalSum.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LocalWindow frame = new LocalWindow();	
					frame.NewWindow(null);
				}
			});
			
			mnLocal.add(mnLocalSum);
			
			JMenuItem mnLocalDiff = new JMenuItem("LocalDiff");
			mnLocal.add(mnLocalDiff);
			
			// Focal operations
			JMenu mnFocal = new JMenu("Focal operations");
			mnToolbox.add(mnFocal);
			
			JMenuItem mnFocalSum = new JMenuItem("FocalSum");
			mnFocal.add(mnFocalSum);
			
			JMenuItem mnFocalVariety = new JMenuItem("FocalVariety");
			mnFocal.add(mnFocalVariety);
			
			JMenuItem mnFocalProduct = new JMenuItem("FocalProduct");
			mnFocal.add(mnFocalProduct);
			
			JMenuItem mnFocalRanking = new JMenuItem("FocalRanking");
			mnFocal.add(mnFocalRanking);
			
			// Zonal operations
			JMenu mnZonal = new JMenu("Zonal operations");
			mnToolbox.add(mnZonal);
			
			JMenuItem mnZonalSum = new JMenuItem("ZonalSum");
			mnZonal.add(mnZonalSum);
			
			JMenuItem mnZonalVariety = new JMenuItem("ZonalVariety");
			mnZonal.add(mnZonalVariety);
			
			JMenuItem mnZonalProduct = new JMenuItem("ZonalProduct");
			mnZonal.add(mnZonalProduct);
			
			JMenuItem mnZonalMean = new JMenuItem("ZonalMean");
			mnZonal.add(mnZonalMean);
			
			JMenuItem mnZonalMin = new JMenuItem("ZonalMinimum");
			mnZonal.add(mnZonalMin);
			
			JMenuItem mnZonalMax = new JMenuItem("ZonalMaximum");
			mnZonal.add(mnZonalMax);
			
			Component gap4 = Box.createHorizontalStrut(20);
			menuBar.add(gap4);
			
			// HELP = MANUAL
			JMenu mnHelp = new JMenu("Help");
			mnHelp.setForeground(mainColor2);
			mnHelp.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnHelp.setBorder(new LineBorder(mainColor2, 1, true));
			mnHelp.setBorder(new RoundedBorder(radius));
			menuBar.add(mnHelp);
			
			JMenuItem mnManual = new JMenuItem("Open App Manual (heeeelp)");
			mnHelp.add(mnManual);
			
			//Open file
			JMenuItem mntmOpen = new JMenuItem("Open file");
			
			mntmOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int result = fileChooser.showOpenDialog(TestGUI.this);
					int scale = 3;
					if (result != JFileChooser.APPROVE_OPTION) { // File chooser accepts .java and .class files for some reason
						System.out.println("Incorrect file type.");
						System.exit(0);
					}
//					layeredPanel.remove(mapPanel);
					File[] selectedFiles = fileChooser.getSelectedFiles();
					for (int i = 0; i < selectedFiles.length; i++) {
						System.out.println("Selected file: " + selectedFiles[i].getAbsolutePath());
						Layer layer = new Layer ("layer", selectedFiles[i].getAbsolutePath());
						
						BufferedImage layerImage;
						layerImage = layer.toImage();
						
						MapPanel mapPanel = new MapPanel(layerImage, scale);
						layeredPanel.add(mapPanel, i); // BorderLayout.CENTER
						mapPanel.setBounds(200, 200, 300, 300);	
						mapPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
					}
					
//					if (result == JFileChooser.APPROVE_OPTION) {
//						layeredPanel.remove(mapPanel);
//						File[] selectedFiles = fileChooser.getSelectedFiles();
//						for (int i = 0; i < selectedFiles.length; i++) {
//							System.out.println("Selected file: " + selectedFiles[i].getAbsolutePath());
//							Layer layer = new Layer ("layer", selectedFiles[i].getAbsolutePath());
//							
//							BufferedImage layerImage;
//							layerImage = layer.toImage();
//							
//							MapPanel mapPanel = new MapPanel(layerImage, scale);
//							layeredPanel.add(mapPanel, BorderLayout.CENTER);
//							mapPanel.setBounds(200, 200, 300, 300);	
//							mapPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
//							layeredPanel.pack();
//							panelTOC.add(, selectedFiles[i].getName());
//						}
//					}
				}
			});
			
			mnFile.add(mntmOpen);
			
			JMenuItem mntmSave = new JMenuItem("Save");
			mnFile.add(mntmSave);
			
			JMenuItem mntmExit = new JMenuItem("Exit");
			mnFile.add(mntmExit);
			
			Component verticalStrut = Box.createVerticalStrut(9);
			headPanel.add(verticalStrut, BorderLayout.NORTH);
			
			Component verticalStrut_1 = Box.createVerticalStrut(9);
			headPanel.add(verticalStrut_1, BorderLayout.SOUTH);
			
			JToggleButton modeOnOff = new JToggleButton("LEARNING MODE (on/off)");
			modeOnOff.setSelected(true);
			modeOnOff.setBackground(projectYellow);
			modeOnOff.setFont(new Font(mainFont, Font.BOLD, 14));
			modeOnOff.setForeground(mainColor);
			headPanel.add(modeOnOff, BorderLayout.EAST);
			contentPanel.setBounds(10,10,10,10);	
		
		// Create bottom Panel.
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(mainColor);
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);
			
			// Fill up bottom panel:
			Button fullExtent = new Button("Full Extent");
			fullExtent.setBackground(mainColor2);
			fullExtent.setForeground(mainColor);
			fullExtent.setFont(new Font(mainFont, Font.PLAIN, 14));
			bottomPanel.add(fullExtent);
			
			Component horizontalStrut = Box.createHorizontalStrut(40);
			bottomPanel.add(horizontalStrut);
			
			JTextArea magnifier = new JTextArea();
			magnifier.setText("Magnifier (%):");
			magnifier.setTabSize(40);
			magnifier.setOpaque(false);
			magnifier.setForeground(mainColor2);
			magnifier.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(magnifier);
			
			JSpinner spinner = new JSpinner();
			spinner.setFont(new Font(mainFont, Font.PLAIN, 12));
			spinner.setModel(new SpinnerNumberModel(100, 0, 500, 25));	// insert % ??
			bottomPanel.add(spinner);
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			bottomPanel.add(horizontalStrut_1);
			
			JTextArea txtrScale = new JTextArea();
			txtrScale.setText("Scale 1:");
			txtrScale.setTabSize(40);
			txtrScale.setOpaque(false);
			txtrScale.setForeground(new Color(187, 202, 192));
			txtrScale.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrScale);
			
			JSpinner spinner_1 = new JSpinner();
			spinner_1.setFont(new Font(mainFont, Font.PLAIN, 12));
			spinner_1.setModel(new SpinnerNumberModel(1000, 1, 100000, 500));
			bottomPanel.add(spinner_1);
			
			Component horizontalStrut_2 = Box.createHorizontalStrut(40);
			bottomPanel.add(horizontalStrut_2);
			
			JTextArea txtr_sC = new JTextArea();
			txtr_sC.setText("Selected Cell");
			txtr_sC.setTabSize(40);
			txtr_sC.setOpaque(false);
			txtr_sC.setForeground(new Color(187, 202, 192));
			txtr_sC.setFont(new Font(mainFont, Font.BOLD, 12));
			bottomPanel.add(txtr_sC);
			
			JTextArea txtrValue = new JTextArea();
			txtrValue.setText("value:");
			txtrValue.setTabSize(40);
			txtrValue.setOpaque(false);
			txtrValue.setForeground(new Color(187, 202, 192));
			txtrValue.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrValue);
			
			Label sC_value_label = new Label("01");
			sC_value_label.setAlignment(Label.RIGHT);
			sC_value_label.setFont(new Font(mainFont, Font.PLAIN, 12));
			sC_value_label.setBackground(Color.WHITE);
			bottomPanel.add(sC_value_label);
			
			JTextArea sC_ID = new JTextArea();
			sC_ID.setText("ID:");
			sC_ID.setTabSize(40);
			sC_ID.setOpaque(false);
			sC_ID.setForeground(new Color(187, 202, 192));
			sC_ID.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(sC_ID);
			
			Label sC_ID_label = new Label("01");
			sC_ID_label.setAlignment(Label.RIGHT);
			sC_ID_label.setFont(new Font(mainFont, Font.PLAIN, 12));
			sC_ID_label.setBackground(Color.WHITE);
			bottomPanel.add(sC_ID_label);
			
			JTextArea sC_x = new JTextArea();
			sC_x.setText("x:");
			sC_x.setTabSize(40);
			sC_x.setOpaque(false);
			sC_x.setForeground(new Color(187, 202, 192));
			sC_x.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(sC_x);
			
			Label sC_x_label = new Label("333");
			sC_x_label.setAlignment(Label.RIGHT);
			sC_x_label.setFont(new Font(mainFont, Font.PLAIN, 12));
			sC_x_label.setBackground(Color.WHITE);
			bottomPanel.add(sC_x_label);
			
			JTextArea sC_y = new JTextArea();
			sC_y.setText("y:");
			sC_y.setTabSize(40);
			sC_y.setOpaque(false);
			sC_y.setForeground(new Color(187, 202, 192));
			sC_y.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(sC_y);
			
			Label sC_y_label = new Label("2");
			sC_y_label.setAlignment(Label.RIGHT);
			sC_y_label.setFont(new Font(mainFont, Font.PLAIN, 12));
			sC_y_label.setBackground(Color.WHITE);
			bottomPanel.add(sC_y_label);
	}
}