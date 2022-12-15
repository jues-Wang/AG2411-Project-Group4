package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JToggleButton;
import java.awt.Button;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLabel;
//import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Label;
import java.awt.Point;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class TestGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	public static MapPanel mPanel;
	public static int zoomLvl = 3;
    public static TestGUI app;
    public static Layer aboveLayer; // the layer that is shown currently 
	public String[] pixel = {"Nan","Nan","Nan","Nan",};
	
	// For operations in other windows
	public static DefaultListModel<String> layerNameList = new DefaultListModel<String>();
	public static LinkedList<Layer> layerList = new LinkedList<Layer>();
	public static LinkedList<BufferedImage> imageList = new LinkedList<BufferedImage>();
	
	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI frame = new TestGUI();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void render(BufferedImage image, int scale){
		System.out.println("render triggered");
		mPanel = new MapPanel(image, scale);

		app.getContentPane().add(mPanel, BorderLayout.CENTER);
		app.revalidate();
	}

	public static void zoom(int change){
		if (mPanel != null){
			zoomLvl = Math.max(zoomLvl + change, 1);
			mPanel.scale = zoomLvl;
			mPanel.revalidate();
			mPanel.repaint();

		} else {
			JOptionPane.showMessageDialog(
					app,
					"Unable to perform action: no layer loaded",
					"No Layer",
					JOptionPane.OK_OPTION
					);
		}
	}
	
	private String getFileName(String path) {
		String[] tokens = path.split("\\\\");
		int i = tokens.length - 1;
		String fileName = tokens[i];
		String[] nameTokens = fileName.split("[.]", 0);
		String name = nameTokens[0];
		return name;
	}
	
	public TestGUI() {
		setTitle("My Little FunGIS");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\AG2411\\AG2411-Project-Group4\\AG2411_raster\\media\\logo-04.png"));
		
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
		Color mainColor = projectDarkBlue4;		// for background color in head & bottom panel
		Color mainColor2 = projectLightGreen;	// for text, borders, ...
		
		Color buttonColor = new Color(238, 238, 238);
		Color highlightColor = projectYellow;
		Color crazyColor = projectRed;
		
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
		
		// Create the content panel.
		contentPanel = new JPanel();
		contentPanel.setForeground(Color.WHITE);
	//	contentPanel.setBackground(Color.DARK_GRAY);
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		// SPLIT PANEL = TOC (left) + Map Panel (right)
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(new Color(255, 255, 255));
		contentPanel.add(splitPane);
		
		final JPanel panelTOC = new JPanel();
		panelTOC.setBackground(mainColor2);
		splitPane.setLeftComponent(panelTOC);

		final JPanel panelMAP = new JPanel();
		splitPane.setRightComponent(panelMAP);
		panelMAP.setLayout(new CardLayout(0, 0));

		final JLayeredPane layeredPane = new JLayeredPane();	// to LAYER the maps ??
		layeredPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				zoom(e.getWheelRotation());
			}
		});
		panelMAP.add(layeredPane, "name_927277592538900");
		
		// Defining the pop-up menu on right click
		final JPopupMenu popupMenuRC = new JPopupMenu();
		JMenuItem mntmSaveRC = new JMenuItem("Save");
		popupMenuRC.add(mntmSaveRC);
		JMenuItem mntmDeleteRC = new JMenuItem("Delete");
		popupMenuRC.add(mntmDeleteRC);
		JMenuItem mntmExportRC = new JMenuItem("Export");
		popupMenuRC.add(mntmExportRC);
		
		// Display TOC
		JList<String> displayList = new JList<String>(layerNameList);
		displayList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		        // Visualize a layer by double clicking it
				if (e.getClickCount() == 2) {
		        	layeredPane.remove(mPanel);
		            int index = displayList.locationToIndex(e.getPoint());
		            int scale = 3;
		            layeredPane.remove(mPanel);
		            
		            aboveLayer = layerList.get(index);
		            
					BufferedImage layerImage = imageList.get(index);
					mPanel = new MapPanel(layerImage, scale);
					layeredPane.add(mPanel, BorderLayout.CENTER);
					mPanel.setBounds(0, 0, 2000, 2000);	
					mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
					
		        }
		        
		        // Open pop-up menu
		        if (SwingUtilities.isRightMouseButton(e)) {
		        	int index = displayList.locationToIndex(e.getPoint());
		        	popupMenuRC.setLocation(getMousePosition());
		        	popupMenuRC.setVisible(true);
		        	popupMenuRC.setLabel(index + "");
		        }
		    }
		});
		panelTOC.setLayout(new BorderLayout(0, 0));
		displayList.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
		displayList.setBackground(mainColor2);
		panelTOC.add(displayList, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("   Table of Content          ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panelTOC.add(lblNewLabel, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("   ");
		panelTOC.add(lblNewLabel_1, BorderLayout.WEST);
		
		// Save event handler
		mntmSaveRC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = Integer.parseInt(popupMenuRC.getLabel());
				popupMenuRC.setVisible(false);
				mntmSaveRC.setBackground(buttonColor);
				layerList.get(index).save(layerNameList.get(index) + ".txt");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mntmSaveRC.setBackground(crazyColor);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mntmSaveRC.setBackground(buttonColor);
			}
		});
		
		// Delete event handler
		mntmDeleteRC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = Integer.parseInt(popupMenuRC.getLabel());
				popupMenuRC.setVisible(false);
				mntmDeleteRC.setBackground(buttonColor);
				
				// Remove image if shown
				int scale = 3;
				boolean shownLayer = false;
				if(layerNameList.get(index).equals(aboveLayer.name)) {
					shownLayer = true;
					layeredPane.remove(mPanel);
					layeredPane.setVisible(true);
				}
				
				// Remove the layer
				layerNameList.remove(index);
				layerList.remove(index);
				imageList.remove(index);
				
				// Display new image if needed (layer top of TOC)
				if (! layerList.isEmpty() && shownLayer) {
					aboveLayer = layerList.get(0);
					BufferedImage layerImage = imageList.get(0);
					
					mPanel = new MapPanel(layerImage, scale);
					layeredPane.add(mPanel, BorderLayout.CENTER);
					mPanel.setBounds(0, 0, 2000, 2000);	
					mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				mntmDeleteRC.setBackground(crazyColor);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mntmDeleteRC.setBackground(buttonColor);
			}
		});
		
		// Export event handler
		mntmExportRC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = Integer.parseInt(popupMenuRC.getLabel());
				popupMenuRC.setVisible(false);
				mntmExportRC.setBackground(buttonColor);
				
				BufferedImage outputImage = imageList.get(index);
				File outputfile = new File(layerNameList.get(index) + ".jpg");
				try {
					ImageIO.write(outputImage, "jpg", outputfile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mntmExportRC.setBackground(crazyColor);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mntmExportRC.setBackground(buttonColor);
			}
		});
		
		// Remove pop-up menu
		panelTOC.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		        if (SwingUtilities.isLeftMouseButton(e)) {
		        	popupMenuRC.setVisible(false);
		        }
		    }
		});
		
		// Create head Panel.
		JPanel headPanel = new JPanel();
		headPanel.setBackground(mainColor);
		contentPanel.add(headPanel, BorderLayout.NORTH);
		headPanel.setLayout(new BorderLayout(0, 0));
		
			// Fill head panel:
			// Menu bar
			int radius = 20;  	// Define radius for Buttons in the menu bar.
			
			javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
			menuBar.setBorderPainted(false);
			menuBar.setBackground(mainColor);
			headPanel.add(menuBar);
			
			Component gap1 = Box.createHorizontalStrut(8);
			menuBar.add(gap1);
			
			//FILE
			JMenu mnFile = new JMenu("File");
			mnFile.setHorizontalAlignment(SwingConstants.CENTER);
			mnFile.setForeground(highlightColor);
			mnFile.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
			mnFile.setBorder(new LineBorder(highlightColor, 1, true));
			mnFile.setBorder(new RoundedBorder(radius));
			menuBar.add(mnFile);
			
			Component gap2 = Box.createHorizontalStrut(20);
			menuBar.add(gap2);
			
			// EDIT
			JMenu mnEdit = new JMenu("Edit");
			mnEdit.setForeground(highlightColor);
			mnEdit.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
			mnEdit.setBorder(new LineBorder(highlightColor, 1, true));
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
			mnToolbox.setForeground(highlightColor);
			mnToolbox.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
			mnToolbox.setBorder(new LineBorder(highlightColor, 1, true));
			mnToolbox.setBorder(new RoundedBorder(radius));
			menuBar.add(mnToolbox);
			
			// Local operations
			JMenuItem mnLocal = new JMenuItem("Local operations");
			mnToolbox.add(mnLocal);
			
			mnLocal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LocalWindow.main();
				}
			});

			// Focal operations
			JMenuItem mnFocal = new JMenuItem("Focal operations");
			mnToolbox.add(mnFocal);
			mnFocal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FocalWindow.main();
				}
			});
			
			// Zonal operations
			JMenuItem mnZonal = new JMenuItem("Zonal operations");
			mnToolbox.add(mnZonal);
			mnZonal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ZonalWindow.main();
				}
			});
			
			JMenuItem mnDistance = new JMenuItem("Distance");
			mnToolbox.add(mnDistance);
			
			JMenuItem mnShortestPath = new JMenuItem("Shortest Path");
			mnToolbox.add(mnShortestPath);
			
			Component gap4 = Box.createHorizontalStrut(20);
			menuBar.add(gap4);
			
			// HELP = MANUAL
			JMenu mnHelp = new JMenu("Help");
			mnHelp.setIcon(new ImageIcon("C:\\AG2411\\AG2411-Project-Group4\\AG2411_raster\\media\\icon-06.png"));
			mnHelp.setForeground(highlightColor);
			//mnHelp.setBackground(mainColor);
			mnHelp.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
			mnHelp.setBorder(new LineBorder(highlightColor, 1, true));
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
					
					if (result == JFileChooser.APPROVE_OPTION) {
						if (mPanel != null) {
							layeredPane.remove(mPanel);
						}
						
						File[] selectedFiles = fileChooser.getSelectedFiles();
						for (int i = 0; i < selectedFiles.length; i++) {
							if (mPanel != null) {
								layeredPane.remove(mPanel);
							}
//							System.out.println("Selected file: " + selectedFiles[i].getAbsolutePath());
							String layerName = getFileName(selectedFiles[i].getAbsolutePath());
							Layer imageLayer = new Layer(layerName, selectedFiles[i].getAbsolutePath());
							aboveLayer = new Layer(layerName, selectedFiles[i].getAbsolutePath());//abovelayer = layer
							
							BufferedImage layerImage;
							layerImage = imageLayer.toImage();
							
							mPanel = new MapPanel(layerImage, scale);
							layeredPane.add(mPanel, BorderLayout.CENTER);
							mPanel.setBounds(0, 0, 2000, 2000);	
							mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
							
							// Add to TOC if does not exist already
							boolean inList = false;
							for (int j = 0; j < layerNameList.size(); j++) {
								if (layerNameList.get(j).equals(layerName)) {
									inList = true;
									break;
								}
							}
							if (! inList) {
								layerNameList.addElement(layerName);
								layerList.add(new Layer (layerName, selectedFiles[i].getAbsolutePath()));
								imageList.add(imageLayer.toImage());
							}
						}
					}
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
			
			JToggleButton modeOnOff = new JToggleButton("OBSERVATION MODE (on/off)");
			modeOnOff.setSelected(true);
			modeOnOff.setBackground(crazyColor);
			modeOnOff.setFont(new Font("Sitka Heading", Font.BOLD, 14));
			modeOnOff.setForeground(mainColor);
			headPanel.add(modeOnOff, BorderLayout.EAST);
			contentPanel.setBounds(10,10,10,10);	
		
		// Create bottom Panel.
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(mainColor);
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);
			
			// Fill up bottom panel:
			Button fullExtent = new Button("Full Extent");
			fullExtent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mPanel.scale = 3;
					mPanel.revalidate();
					mPanel.repaint();
				}
			});
			fullExtent.setBackground(mainColor2);
			fullExtent.setForeground(mainColor);
			fullExtent.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 14));
			bottomPanel.add(fullExtent);
			
			Component horizontalStrut = Box.createHorizontalStrut(40);
			bottomPanel.add(horizontalStrut);
			
			JButton btnZoomIn = new JButton("+");
			bottomPanel.add(btnZoomIn);
			
			JButton btnZoomOut = new JButton("-");
			bottomPanel.add(btnZoomOut);
			
			JTextArea magnifier = new JTextArea();
			magnifier.setText("Magnifier (%):");
			magnifier.setTabSize(40);
			magnifier.setOpaque(false);
			magnifier.setForeground(mainColor2);
			magnifier.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(magnifier);
			
			JSpinner spinner = new JSpinner();
			spinner.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			spinner.setModel(new SpinnerNumberModel(100, 0, 500, 25));	// insert % ??
			bottomPanel.add(spinner);
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			bottomPanel.add(horizontalStrut_1);
			
			JTextArea txtrScale = new JTextArea();
			txtrScale.setText("Scale 1:");
			txtrScale.setTabSize(40);
			txtrScale.setOpaque(false);
			txtrScale.setForeground(new Color(187, 202, 192));
			txtrScale.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(txtrScale);
			
			JSpinner spinner_1 = new JSpinner();
			spinner_1.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			spinner_1.setModel(new SpinnerNumberModel(1000, 1, 100000, 500));
			bottomPanel.add(spinner_1);
			
			Component horizontalStrut_2 = Box.createHorizontalStrut(40);
			bottomPanel.add(horizontalStrut_2);
			
			JTextArea txtrSelectedCell = new JTextArea();
			txtrSelectedCell.setTabSize(40);
			txtrSelectedCell.setOpaque(false);
			txtrSelectedCell.setForeground(new Color(187, 202, 192));
			txtrSelectedCell.setFont(new Font("Brandon Grotesque Regular", Font.BOLD, 12));
			bottomPanel.add(txtrSelectedCell);
			
			JTextArea txtrValue = new JTextArea();
			txtrValue.setText("value:");
			txtrValue.setTabSize(40);
			txtrValue.setOpaque(false);
			txtrValue.setForeground(new Color(187, 202, 192));
			txtrValue.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(txtrValue);
			
			Label label_3 = new Label(pixel[0]);//value
			label_3.setText(pixel[0]);
			label_3.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			label_3.setBackground(Color.WHITE);
			label_3.setAlignment(Label.RIGHT);
			bottomPanel.add(label_3);
			
			JTextArea txtrId = new JTextArea();
			txtrId.setText("ID:");
			txtrId.setTabSize(40);
			txtrId.setOpaque(false);
			txtrId.setForeground(new Color(187, 202, 192));
			txtrId.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(txtrId);
			
			Label label = new Label(pixel[1]);//id
			label.setText(pixel[1]);
			label.setAlignment(Label.RIGHT);
			label.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			label.setBackground(Color.WHITE);
			bottomPanel.add(label);
			
			JTextArea txtrX = new JTextArea();
			txtrX.setText("x:");
			txtrX.setTabSize(40);
			txtrX.setOpaque(false);
			txtrX.setForeground(new Color(187, 202, 192));
			txtrX.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(txtrX);
			
			Label label_1 = new Label(pixel[2]);
			label_1.setText(pixel[2]);
			label_1.setAlignment(Label.RIGHT);
			label_1.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			label_1.setBackground(Color.WHITE);
			bottomPanel.add(label_1);
			
			JTextArea txtrY = new JTextArea();
			txtrY.setText("y:");
			txtrY.setTabSize(40);
			txtrY.setOpaque(false);
			txtrY.setForeground(new Color(187, 202, 192));
			txtrY.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			bottomPanel.add(txtrY);
			
			Label label_1_1 = new Label(pixel[3]);
			label_1_1.setText(pixel[3]);
			label_1_1.setAlignment(Label.RIGHT);
			label_1_1.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			label_1_1.setBackground(Color.WHITE);
			bottomPanel.add(label_1_1);
			
			layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					if(mPanel!=null) {
						int x_screen = e.getX();//x&y are started from the left-up corner of layeredPanel
						int y_screen = e.getY();
						Point mPanelLocation = mPanel.getLocation();//[x=0,y=0]

						int x_start = mPanelLocation.x; // from left to right
						int x_end = mPanelLocation.x + aboveLayer.nCols*zoomLvl;
						int y_start = mPanelLocation.y; //from up to bottom
						int y_end = mPanelLocation.y + aboveLayer.nRows*zoomLvl;
						
						if(x_screen > x_start & x_screen < x_end & y_screen > y_start & y_screen < y_end) {
							// index 
							int x = (x_screen - x_start)/zoomLvl;
							int y = (y_screen - y_start)/zoomLvl;

							//message of this pixel
							pixel[0] = Double.toString(aboveLayer.values[x][y]);//value
							pixel[1] = Integer.toString(x*aboveLayer.nCols+y);//id
							pixel[2] = Integer.toString(x);
							pixel[3] = Integer.toString(y);

							//visualization
							label_3.setText(pixel[0]);//value
							label.setText(pixel[1]);//id
							label_1.setText(pixel[2]);//x
							label_1_1.setText(pixel[3]);//y
						}
					}
				}
			});
	}
}