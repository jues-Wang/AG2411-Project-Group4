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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

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
import java.awt.Rectangle;
import java.awt.Toolkit;

public class TestGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static MapPanel mPanel;
	public static int scale = 3;
	public static int zoomLvl = scale;
	
    public static TestGUI app;
    public static Layer aboveLayer; // the layer that is shown currently 
	public String[] pixel = {"   ","   ","   ","   ",};
	public String[] pixel_select = {"   ","   ","   ","   ",};
	
	public static JLayeredPane layeredPane = new JLayeredPane();
	public static JLabel lblLocationChoice;
	
	// For coloring other windows
	public static Color mainColor2;
	public static Color mainColor;
	public static Color highlightColor;
	public static Color crazyColor;
	
	// For operations in other windows
	public static DefaultListModel<String> layerNameList = new DefaultListModel<String>();
	public static LinkedList<Layer> layerList = new LinkedList<Layer>();
	public static LinkedList<BufferedImage> imageList = new LinkedList<BufferedImage>();
	
	// For saving and exporting in external windows
	public static int chosenIndex = 0;
	
	// For dragging map
	public int dx, dy;
	public static int mapMovedX, mapMovedY;
	
	// Map start position
	public static int mapStartX = 0;
	public static int mapStartY = 0;
	
	public static void getMapStartX() {
		mapStartX = layeredPane.getWidth() / 2 - aboveLayer.nCols * scale / 2;
	}
	
	public static void getMapStartY() {
		mapStartY = layeredPane.getHeight() / 2 - aboveLayer.nRows * scale / 2;
	}
	
	public static void getScale(Layer layer) {
		double ratio = 0.8;
		int rowScale = (int) layeredPane.getHeight() / layer.nRows;
		rowScale *= ratio;
		
		int colScale = (int) layeredPane.getWidth() / layer.nCols;
		colScale *= ratio;
		int tempScale = Math.min(rowScale, colScale);
		scale = Math.max(tempScale, 1);
		zoomLvl = scale;
	}
	
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
			scale = zoomLvl;
			
			// Zooming centered around the map's current midpoint
			getMapStartX();
			getMapStartY();
			int mapX = mapStartX - mapMovedX;
			int mapY = mapStartY - mapMovedY;
			
			mPanel.setBounds(mapX, mapY, 2000, 2000);
			mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
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
	
	private String getFileType(String path) {
		String[] tokens = path.split("\\\\");
		int i = tokens.length - 1;
		String fileName = tokens[i];
		String[] nameTokens = fileName.split("[.]", 0);
		String fileType = nameTokens[1];
		return fileType;
	}
	
	public TestGUI() {
		setTitle("My Little FunGIS");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\AG2411\\AG2411-Project-Group4\\AG2411_raster\\media\\logo-04.png"));
		
		// Create the frame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 900, 500); // For working in the window builder
		// spinners in bottom panel
		JSpinner zoomSpinner = new JSpinner(); // for %
		JSpinner scaleSpinner = new JSpinner(); //for scale
		
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
		mainColor = projectDarkBlue4;		// for background color in head & bottom panel
		mainColor2 = projectLightGreen;	// for text, borders, ...
		
		Color buttonColor = new Color(238, 238, 238);
		highlightColor = projectYellow;
		crazyColor = projectRed;
		
		String mainFont = new String ("Brandon Grotesque Regular");
		
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
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// SPLIT PANEL = TOC (left) + Map Panel (right)
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(new Color(255, 255, 255));
		contentPane.add(splitPane);
		
		final JPanel panelTOC = new JPanel();
		panelTOC.setBackground(mainColor2);
		splitPane.setLeftComponent(panelTOC);
		panelTOC.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 5));

		final JPanel panelMAP = new JPanel();
		splitPane.setRightComponent(panelMAP);
		panelMAP.setLayout(new CardLayout(0, 0));
		// Panning the map around
		layeredPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dx = e.getX() - mPanel.getLocation().x;
				dy = e.getY() - mPanel.getLocation().y;
			}
		});
		layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				mPanel.setLocation(mouseX - dx, mouseY - dy);

				// Keep track of map displacement for zooming purposes
				mapMovedX = mapStartX - mPanel.getLocation().x;
				mapMovedY = mapStartY - mPanel.getLocation().y;
			}
		});
		panelMAP.add(layeredPane, "name_927277592538900");
		
		lblLocationChoice = new JLabel("New label");
		lblLocationChoice.setBackground(new Color(255, 255, 255));
		lblLocationChoice.setBounds(12, 12, 293, 34);
		layeredPane.add(lblLocationChoice);
		lblLocationChoice.setForeground(new Color(0, 0, 0));
		lblLocationChoice.setFont(new Font("Dialog", Font.BOLD, 26));
		lblLocationChoice.setVisible(false);
		
		// Defining the pop-up menu on right click
		final JPopupMenu popupMenuRC = new JPopupMenu();
		JMenuItem mntmSaveRC = new JMenuItem("Save file");
		popupMenuRC.add(mntmSaveRC);
		JMenuItem mntmDeleteRC = new JMenuItem("Delete");
		popupMenuRC.add(mntmDeleteRC);
		JMenuItem mntmExportRC = new JMenuItem("Export to JPG");
		popupMenuRC.add(mntmExportRC);
		
		// Display TOC
		JList<String> displayList = new JList<String>(layerNameList);
		displayList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		displayList.setLayoutOrientation(JList.VERTICAL_WRAP);
		displayList.setVisibleRowCount(-1);
		displayList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		        // Visualize a layer by double clicking it
				if (e.getClickCount() == 2) {
		        	layeredPane.remove(mPanel);
					layeredPane.revalidate();
		            layeredPane.repaint();
		            int index = displayList.locationToIndex(e.getPoint());
		            
		            aboveLayer = layerList.get(index);
		            getScale(aboveLayer);
		            
					BufferedImage layerImage = imageList.get(index);
					mPanel = new MapPanel(layerImage, scale);
					
					layeredPane.add(mPanel, BorderLayout.CENTER);
					
					getMapStartX();
					getMapStartY();
					mPanel.setBounds(mapStartX, mapStartY, 2000, 2000);	
					mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
					
					zoomSpinner.setValue(100*zoomLvl);
					scaleSpinner.setValue((int)zoomLvl*aboveLayer.resolution);
		        }
		        
		        // Open pop-up menu
		        if (SwingUtilities.isRightMouseButton(e)) {
		        	int index = displayList.locationToIndex(e.getPoint());
		        	displayList.setSelectedIndex(index);
		        	popupMenuRC.setLocation(getMousePosition());
		        	popupMenuRC.setVisible(true);
		        	popupMenuRC.setLabel(index + "");
		        }
		    }
		});
		panelTOC.setLayout(new BorderLayout(0, 0));
		displayList.setFont(new Font(mainFont, Font.PLAIN, 12));
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
				chosenIndex = Integer.parseInt(popupMenuRC.getLabel());
				popupMenuRC.setVisible(false);
				mntmSaveRC.setBackground(buttonColor);
				SaveWindow.main();
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
				boolean shownLayer = false;
				if(layerNameList.get(index).equals(aboveLayer.name)) {
					shownLayer = true;
					layeredPane.remove(mPanel);
					layeredPane.revalidate();
		            layeredPane.repaint();
				}
				
				// Remove the layer
				layerNameList.remove(index);
				layerList.remove(index);
				imageList.remove(index);
				
				// Display new image if needed (layer top of TOC)
				if (! layerList.isEmpty() && shownLayer) {
					aboveLayer = layerList.getLast();
					getScale(aboveLayer);
					BufferedImage layerImage = imageList.getLast();
					mPanel = new MapPanel(layerImage, scale);
					
					layeredPane.add(mPanel, BorderLayout.CENTER);
					
					getMapStartX();
					getMapStartY();
					mPanel.setBounds(mapStartX, mapStartY, 2000, 2000);	
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
				chosenIndex = Integer.parseInt(popupMenuRC.getLabel());
				popupMenuRC.setVisible(false);
				mntmExportRC.setBackground(buttonColor);
				ExportWindow.main();
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
		contentPane.add(headPanel, BorderLayout.NORTH);
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
			mnFile.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnFile.setBorder(new LineBorder(highlightColor, 1, true));
			mnFile.setBorder(new RoundedBorder(radius));
			menuBar.add(mnFile);
			
			Component gap2 = Box.createHorizontalStrut(20);
			menuBar.add(gap2);
			
			// TOOLBOX
			JMenu mnToolbox = new JMenu("Toolbox");
			mnToolbox.setForeground(highlightColor);
			mnToolbox.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnToolbox.setBorder(new LineBorder(highlightColor, 1, true));
			mnToolbox.setBorder(new RoundedBorder(radius));
			menuBar.add(mnToolbox);
			
			// Local operations
			JMenuItem mnLocal = new JMenuItem("Local operations");
			mnLocal.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnToolbox.add(mnLocal);
			
			mnLocal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LocalWindow.main();
				}
			});
			
			// Focal operations
			JMenuItem mnFocal = new JMenuItem("Focal operations");
			mnFocal.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnToolbox.add(mnFocal);
			mnFocal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FocalWindow.main();
				}
			});

			// Zonal operations
			JMenuItem mnZonal = new JMenuItem("Zonal operations");
			mnZonal.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnToolbox.add(mnZonal);
			mnZonal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ZonalWindow.main();
				}
			});

			JMenuItem mnDistance = new JMenuItem("Distance");
			mnDistance.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnToolbox.add(mnDistance);
			mnDistance.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DistanceWindow.main();
				}
			});
			

			JMenuItem mnShortestPath = new JMenuItem("Shortest Path");
			mnShortestPath.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnToolbox.add(mnShortestPath);
			mnShortestPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ShortestPathWindow.main();
				}
			});

			Component gap3 = Box.createHorizontalStrut(20);
			menuBar.add(gap3);
			
			// RECOLOR
			JMenu mnRecolor = new JMenu("Recolor");
			mnRecolor.setForeground(highlightColor);
			mnRecolor.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnRecolor.setBorder(new LineBorder(highlightColor, 1, true));
			mnRecolor.setBorder(new RoundedBorder(radius));
			menuBar.add(mnRecolor);
			
			JMenuItem colorscheme1 = new JMenuItem("Color scheme");
			colorscheme1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ColorWindow.main();
				}
			});
			colorscheme1.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnRecolor.add(colorscheme1);
			
			Component gap4 = Box.createHorizontalStrut(20);
			menuBar.add(gap4);
			
			// HELP = MANUAL
			JMenu mnHelp = new JMenu("Help");
			mnHelp.setIcon(new ImageIcon(".\\media\\icon-06.png"));
			mnHelp.setForeground(highlightColor);
			mnHelp.setFont(new Font(mainFont, Font.PLAIN, 14));
			mnHelp.setBorder(new LineBorder(highlightColor, 1, true));
			mnHelp.setBorder(new RoundedBorder(radius));
			menuBar.add(mnHelp);
			
			JMenuItem mnManual = new JMenuItem("Open App Manual");
			mnManual.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File pdfFile = new File("manual.pdf");
					try {
						Desktop.getDesktop().open(pdfFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			mnManual.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnHelp.add(mnManual);
			
			//Open file
			JMenuItem mntmOpen = new JMenuItem("Open file");
			mntmOpen.setFont(new Font(mainFont, Font.PLAIN, 12));
			
			mntmOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int result = fileChooser.showOpenDialog(TestGUI.this);
					
					if (result == JFileChooser.APPROVE_OPTION) {
						if (mPanel != null) {
							layeredPane.remove(mPanel);
						}
						
						File[] selectedFiles = fileChooser.getSelectedFiles();
						
						for (int i = 0; i < selectedFiles.length; i++) {
							String fileType = getFileType(selectedFiles[i].getAbsolutePath());
							if (! fileType.equals("txt")) {
								// JFileChooser.APPROVE_OPTION does not work properly for some reason, so this is needed instead.
								JOptionPane.showMessageDialog(new JFrame(),"Error when loading file: Incorrect file type, please provide a .txt file.");
								return;
							}
							// Catching incorrectly formatted .txt files. How to make the error messages go away?
							try {
								new Layer("", selectedFiles[i].getAbsolutePath()).toImage();
							} catch (Exception ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(new JFrame(),"Error when loading file: Incorrectly formatted .txt file.");
								return;
							}
						}
						
						for (int i = 0; i < selectedFiles.length; i++) {
							String layerName = getFileName(selectedFiles[i].getAbsolutePath());
							Layer imageLayer = new Layer(layerName, selectedFiles[i].getAbsolutePath());
							
							// If trying to add a file with the name of an already added file
							// Initial condition
							boolean inList = false;
							for (int j = 0; j < layerNameList.size(); j++) {
								if (layerNameList.get(j).equals(layerName)) {
									inList = true;
								}
							}
							// Find a new name
							while (inList) {
								String tempName = layerName;
								int counter = 1;
								for (int j = 0; j < layerNameList.size(); j++) {
									if (layerNameList.get(j).equals(layerName)) {
										layerName = tempName + "_" + counter;
										inList = false;
										counter++;
									}
								}
								for (int j = 0; j < layerNameList.size(); j++) {
									if (layerNameList.get(j).equals(layerName)) {
										inList = true;
									}
								}
							}
							
							// Add to TOC etc.
							layerNameList.addElement(layerName);
							layerList.add(new Layer (layerName, selectedFiles[i].getAbsolutePath()));
							imageList.add(imageLayer.toImage());
						}
						BufferedImage layerImage = imageList.getLast();
						aboveLayer = layerList.getLast();//aboveLayer = last layer
						getScale(aboveLayer);
						
						mPanel = new MapPanel(layerImage, scale);
						layeredPane.add(mPanel, BorderLayout.CENTER);
						
						getMapStartX();
						getMapStartY();
						mPanel.setBounds(mapStartX, mapStartY, 2000, 2000);	
						mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
						
						layeredPane.revalidate();
			            layeredPane.repaint();
						
						zoomSpinner.setValue(100*scale);
						scaleSpinner.setValue((int)aboveLayer.resolution*scale);
					}
				}
			});
			
			mnFile.add(mntmOpen);
			
			JMenuItem mntmSave = new JMenuItem("Save file");
			mntmSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SaveWindow.main();
				}
			});
			mntmSave.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnFile.add(mntmSave);
			
			JMenuItem mntmExport = new JMenuItem("Export to JPG");
			mntmExport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ExportWindow.main();
				}
			});
			mntmExport.setFont(new Font(mainFont, Font.PLAIN, 12));
			mnFile.add(mntmExport);
			
			
			
			JButton btnGetLearnt = new JButton("GET LEARNT");
			btnGetLearnt.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					GetLearntWindow.main();
				}
			});
			btnGetLearnt.setBackground(crazyColor);
			btnGetLearnt.setFont(new Font("Sitka Heading", Font.BOLD, 14));
			headPanel.add(btnGetLearnt, BorderLayout.EAST);
			
			Component verticalStrut = Box.createVerticalStrut(9);
			headPanel.add(verticalStrut, BorderLayout.NORTH);
			
			Component verticalStrut_1 = Box.createVerticalStrut(9);
			headPanel.add(verticalStrut_1, BorderLayout.SOUTH);
			contentPane.setBounds(10,10,10,10);	
		
			// Create bottom Panel.
			JPanel bottomPanel = new JPanel();
			bottomPanel.setBackground(mainColor);
			contentPane.add(bottomPanel, BorderLayout.SOUTH);
			
			// Fill up bottom panel:
			Button fullExtent = new Button("Full Extent");
			fullExtent.setBackground(mainColor2);
			fullExtent.setForeground(mainColor);
			fullExtent.setFont(new Font(mainFont, Font.BOLD, 12));
			fullExtent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					getScale(aboveLayer);
					mPanel.scale = scale;
					
					// Reset map pan changes
					mapMovedX = 0;
					mapMovedY = 0;
					
					// Move it back
					getMapStartX();
					getMapStartY();
					mPanel.setBounds(mapStartX, mapStartY, 2000, 2000);
					mPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
					
					mPanel.revalidate();
					mPanel.repaint();
					
					zoomSpinner.setValue(zoomLvl*100);
					scaleSpinner.setValue((int)zoomLvl*aboveLayer.resolution);
				}
			});
			bottomPanel.add(fullExtent);
			
			JButton btnZoomIn = new JButton("+");
			btnZoomIn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					zoom(1);
					zoomSpinner.setValue(zoomLvl*100);
					scaleSpinner.setValue((int)zoomLvl*aboveLayer.resolution);
				}
			});
			btnZoomIn.setForeground(new Color(0, 41, 61));
			btnZoomIn.setFont(new Font(mainFont, Font.BOLD, 12));
			btnZoomIn.setBackground(new Color(187, 202, 192));
			bottomPanel.add(btnZoomIn);
			
			JButton btnZoomOut = new JButton("-");
			btnZoomOut.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					zoom(-1);
					zoomSpinner.setValue(zoomLvl*100);
					scaleSpinner.setValue((int)zoomLvl*aboveLayer.resolution);
				}
			});
			btnZoomOut.setForeground(new Color(0, 41, 61));
			btnZoomOut.setFont(new Font(mainFont, Font.BOLD, 12));
			btnZoomOut.setBackground(new Color(187, 202, 192));
			bottomPanel.add(btnZoomOut);
			

			// Zooming with mouse wheel
			layeredPane.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					zoom(- e.getWheelRotation());
					zoomSpinner.setValue(zoomLvl*100);
					scaleSpinner.setValue((int)zoomLvl*aboveLayer.resolution);
				}
			});
			
			Component bottomGap1 = Box.createHorizontalStrut(40);
			bottomPanel.add(bottomGap1);
			
			JLabel magnifier = new JLabel();
			magnifier.setText("Magnifier (%):");
			magnifier.setOpaque(false);
			magnifier.setForeground(mainColor2);
			magnifier.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(magnifier);
			
			zoomSpinner.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			zoomSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 100));	// insert % ??
			bottomPanel.add(zoomSpinner);
			
			Component bottomGap2 = Box.createHorizontalStrut(20);
			bottomPanel.add(bottomGap2);
			
			JLabel txtrScale = new JLabel();
			txtrScale.setText("Scale:");
			txtrScale.setOpaque(false);
			txtrScale.setForeground(new Color(187, 202, 192));
			txtrScale.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrScale);
			
			scaleSpinner.setFont(new Font(mainFont, Font.PLAIN, 12));
			scaleSpinner.setModel(new SpinnerNumberModel(0, 0, 100000, 500));
			bottomPanel.add(scaleSpinner);
			
			Component bottomGap3 = Box.createHorizontalStrut(40);
			bottomPanel.add(bottomGap3);
			
			JLabel txtrValue_1 = new JLabel();
			txtrValue_1.setIcon(new ImageIcon("C:\\AG2411\\AG2411-Project-Group4\\AG2411_raster\\media\\cursor-06.png"));
			txtrValue_1.setOpaque(false);
			txtrValue_1.setForeground(new Color(187, 202, 192));
			bottomPanel.add(txtrValue_1);

			JLabel txtrId = new JLabel();
			txtrId.setText("ID:");
			txtrId.setOpaque(false);
			txtrId.setForeground(new Color(187, 202, 192));
			txtrId.setFont(new Font(mainFont, Font.ITALIC, 12));
			bottomPanel.add(txtrId);

			Label hoverIDLabel = new Label("       ");
			hoverIDLabel.setFont(new Font(mainFont, Font.PLAIN, 12));
			hoverIDLabel.setBackground(Color.WHITE);
			hoverIDLabel.setAlignment(Label.RIGHT);
			bottomPanel.add(hoverIDLabel);
			
			Label selectedIDLabel = new Label("       ");
			selectedIDLabel.setAlignment(Label.RIGHT);
			selectedIDLabel.setFont(new Font("Brandon Grotesque Regular", Font.PLAIN, 12));
			selectedIDLabel.setBackground(Color.WHITE);
			bottomPanel.add(selectedIDLabel);
			
			JLabel txtrValue = new JLabel();
			txtrValue.setText("value:");
			txtrValue.setOpaque(false);
			txtrValue.setForeground(new Color(187, 202, 192));
			txtrValue.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrValue);
			
			Label valueLabel = new Label(pixel[0]);//value
			valueLabel.setText(pixel[0]);
			valueLabel.setFont(new Font(mainFont, Font.PLAIN, 12));
			valueLabel.setBackground(Color.WHITE);
			valueLabel.setAlignment(Label.RIGHT);
			bottomPanel.add(valueLabel);
			
			JLabel txtrX = new JLabel();
			txtrX.setText("x:");
			txtrX.setOpaque(false);
			txtrX.setForeground(new Color(187, 202, 192));
			txtrX.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrX);
			
			Label xLabel = new Label(pixel[2]);
			xLabel.setText(pixel[2]);
			xLabel.setAlignment(Label.RIGHT);
			xLabel.setFont(new Font(mainFont, Font.PLAIN, 12));
			xLabel.setBackground(Color.WHITE);
			bottomPanel.add(xLabel);
			
			JLabel txtrY = new JLabel();
			txtrY.setText("y:");
			txtrY.setOpaque(false);
			txtrY.setForeground(new Color(187, 202, 192));
			txtrY.setFont(new Font(mainFont, Font.PLAIN, 12));
			bottomPanel.add(txtrY);
			
			Label yLabel = new Label(pixel[3]);
			yLabel.setText(pixel[3]);
			yLabel.setAlignment(Label.RIGHT);
			yLabel.setFont(new Font(mainFont, Font.PLAIN, 12));
			yLabel.setBackground(Color.WHITE);
			yLabel.setBounds(130, 130, 130, 130);
			bottomPanel.add(yLabel);
			
			layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					if(mPanel != null && aboveLayer != null) {
						int x_screen = e.getY();//x&y are started from the left-up corner of layeredPanel
						int y_screen = e.getX();
						Point mPanelLocation = mPanel.getLocation();//[x=0,y=0]

						int y_start = mPanelLocation.x; // from left to right
						int y_end = mPanelLocation.x + aboveLayer.nCols*zoomLvl;
						int x_start = mPanelLocation.y; //from up to bottom
						int x_end = mPanelLocation.y + aboveLayer.nRows*zoomLvl;
						
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
							valueLabel.setText(pixel[0]);//value
							hoverIDLabel.setText(pixel[1]);//id
							xLabel.setText(pixel[3]);//x
							yLabel.setText(pixel[2]);//y
						}
					}
				}
			});

			layeredPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(mPanel!=null & aboveLayer!=null) {
						int x_screen = e.getY();//x&y are started from the left-up corner of layeredPanel
						int y_screen = e.getX();
						Point mPanelLocation = mPanel.getLocation();//[x=0,y=0]

						int y_start = mPanelLocation.x; // from left to right
						int y_end = mPanelLocation.x + aboveLayer.nCols*zoomLvl;
						int x_start = mPanelLocation.y; //from up to bottom
						int x_end = mPanelLocation.y + aboveLayer.nRows*zoomLvl;

						if(x_screen > x_start & x_screen < x_end & y_screen > y_start & y_screen < y_end) {
							// index 
							int x = (x_screen - x_start)/zoomLvl;
							int y = (y_screen - y_start)/zoomLvl;

							//message of this pixel
							pixel_select[0] = Double.toString(aboveLayer.values[x][y]);//value
							pixel_select[1] = Integer.toString(x*aboveLayer.nCols+y);//id
							pixel_select[2] = Integer.toString(x);
							pixel_select[3] = Integer.toString(y);

							//visualization
							selectedIDLabel.setText(pixel_select[1]);//id
							
							// x and y have to be flipped for some reason, Viva is not available to answer questions about her code
							if (ShortestPathWindow.choosingOrigin) {
								ShortestPathWindow.originX = y;
								ShortestPathWindow.textFieldOriginX.setText(Integer.toString(y));
								
								ShortestPathWindow.originY = x;
								ShortestPathWindow.textFieldOriginY.setText(Integer.toString(x));
								ShortestPathWindow.newWindow.setVisible(true);
								
								lblLocationChoice.setVisible(false);
								ShortestPathWindow.choosingOrigin = false;
							} 
							else if (ShortestPathWindow.choosingDestination) {
								ShortestPathWindow.destinationX = y;
								ShortestPathWindow.textFieldDestinationX.setText(Integer.toString(y));
								
								ShortestPathWindow.destinationY = x;
								ShortestPathWindow.textFieldDestinationY.setText(Integer.toString(x));
								ShortestPathWindow.newWindow.setVisible(true);
								
								lblLocationChoice.setVisible(false);
								ShortestPathWindow.choosingDestination = false;
							}
							else if (DistanceWindow.choosingOrigin) {
								DistanceWindow.originX = y;
								DistanceWindow.textFieldOriginX.setText(Integer.toString(y));
								
								DistanceWindow.originY = x;
								DistanceWindow.textFieldOriginY.setText(Integer.toString(x));
								DistanceWindow.newWindow.setVisible(true);
								
								lblLocationChoice.setVisible(false);
								DistanceWindow.choosingOrigin = false;
							}
							else if (DistanceWindow.choosingDestination) {
								DistanceWindow.destinationX = y;
								DistanceWindow.textFieldDestinationX.setText(Integer.toString(y));
								
								DistanceWindow.destinationY = x;
								DistanceWindow.textFieldDestinationY.setText(Integer.toString(x));
								DistanceWindow.newWindow.setVisible(true);
								
								lblLocationChoice.setVisible(false);
								DistanceWindow.choosingDestination = false;
							}
						}
					}
				}
			});
	}
}