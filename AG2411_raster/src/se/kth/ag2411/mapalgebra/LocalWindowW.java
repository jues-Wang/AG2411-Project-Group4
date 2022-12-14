package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class LocalWindowW extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	public String inputFile;
	public String outputFileName;
	public String fileName;
	public String statisticType;
	private JTextField tfOutputFile;

	/**
	 * Launch the application.
	 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						LocalWindow frame = new LocalWindow();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

	/**
	 * Create the frame.
	 * @param actionListener 
	 * @param acActionListenertionListener 
	 */
	public LocalWindowW(TestGUI gui) {
		setTitle("LocalStatistic");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 663, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));	

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{375, 86, 78, 78, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Input file");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		final JComboBox cbInputFile = new JComboBox();
		GridBagConstraints gbc_cbInputFile = new GridBagConstraints();
		gbc_cbInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_cbInputFile.gridwidth = 2;
		gbc_cbInputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbInputFile.gridx = 0;
		gbc_cbInputFile.gridy = 1;
		contentPane.add(cbInputFile, gbc_cbInputFile);

		JButton btnAdd = new JButton("add");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);

		final DefaultListModel fileListModel = new DefaultListModel();
		final JList listFileList = new JList(fileListModel);
		GridBagConstraints gbc_listFileList = new GridBagConstraints();
		gbc_listFileList.gridheight = 3;
		gbc_listFileList.insets = new Insets(0, 0, 5, 5);
		gbc_listFileList.fill = GridBagConstraints.BOTH;
		gbc_listFileList.gridx = 0;
		gbc_listFileList.gridy = 2;
		contentPane.add(listFileList, gbc_listFileList);

		JButton btnDelete = new JButton("delete");
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 3;
		contentPane.add(btnDelete, gbc_btnDelete);

		JLabel lblNewLabel_1 = new JLabel("Output file");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 5;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		tfOutputFile = new JTextField();
		GridBagConstraints gbc_tfOutputFile = new GridBagConstraints();
		gbc_tfOutputFile.insets = new Insets(0, 0, 5, 5);
		gbc_tfOutputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfOutputFile.gridx = 0;
		gbc_tfOutputFile.gridy = 6;
		contentPane.add(tfOutputFile, gbc_tfOutputFile);
		tfOutputFile.setColumns(10);

		JButton btnOutputFile = new JButton("choose");
		GridBagConstraints gbc_btnOutputFile = new GridBagConstraints();
		gbc_btnOutputFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnOutputFile.gridx = 1;
		gbc_btnOutputFile.gridy = 6;
		contentPane.add(btnOutputFile, gbc_btnOutputFile);

		JLabel lblNewLabel_2 = new JLabel("Statistice Type");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 7;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

		final JComboBox cbStatisticType = new JComboBox();
		GridBagConstraints gbc_cbStatisticType = new GridBagConstraints();
		gbc_cbStatisticType.gridwidth = 2;
		gbc_cbStatisticType.insets = new Insets(0, 0, 5, 5);
		gbc_cbStatisticType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbStatisticType.gridx = 0;
		gbc_cbStatisticType.gridy = 8;
		contentPane.add(cbStatisticType, gbc_cbStatisticType);

		JButton btnOk = new JButton("Ok");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 2;
		gbc_btnOk.gridy = 13;
		contentPane.add(btnOk, gbc_btnOk);

		JButton btnCancle = new JButton("Cancel");
		GridBagConstraints gbc_btnCancle = new GridBagConstraints();
		gbc_btnCancle.gridx = 3;
		gbc_btnCancle.gridy = 13;
		contentPane.add(btnCancle, gbc_btnCancle);

		//-------------------------input file 
		for(String k:gui.hm.keySet()) {
			cbInputFile.addItem(k);
		}
		
		inputFile=(String) cbInputFile.getItemAt(0); //default

		cbInputFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputFile = (String) cbInputFile.getSelectedItem();
				//System.out.println("选择的选项是: " + selectedItem);								
			}			
		});

		//-------------------------file list	
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(fileListModel.getSize()<2) {
					String selectItem=(String) cbInputFile.getSelectedItem();
					fileListModel.addElement(selectItem);
				}
			}

		});

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

		//-------------------------output file
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
				int result = fileChooser.showSaveDialog(LocalWindowW.this);
				if (result == JFileChooser.APPROVE_OPTION) {					
					outputFileName=fileChooser.getSelectedFile().getPath();
					
					fileName=fileChooser.getSelectedFile().getName();					
					if(fileName.indexOf(".txt")==-1) {
						outputFileName=outputFileName+".txt";
						fileName=fileName+".txt";
					}
					fileChooser.setVisible(true);
					tfOutputFile.setText(outputFileName);
				}				
			}
		});

		//-------------------------statistic type
		cbStatisticType.addItem("SUM");
		cbStatisticType.addItem("VARIETY");
		cbStatisticType.addItem("MAXIMUM");
		cbStatisticType.addItem("MINIMUM");		
		cbStatisticType.addItem("MEAN");

		//the first item added is default
		statisticType=(String) cbStatisticType.getItemAt(0); 

		cbStatisticType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub				
				statisticType = (String) cbStatisticType.getSelectedItem();				
			}			
		});
		
		
		//-------------------------click ok
		btnOk.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
				int count=fileListModel.getSize();
				if(count==0 || outputFileName==null) {
					JOptionPane.showMessageDialog(new JFrame(), "fail");
					return;
				}	
				
//				System.out.println("local"+":   "+fileName);
				
				if(count==1) {
					String file1Name=(String)fileListModel.getElementAt(0);
					Layer layer1=gui.layers.get(gui.hm.get(file1Name));
					layer1.save(outputFileName);
					gui.newFile(outputFileName, fileName);
					dispose();
					return;
				}			
				
				switch(statisticType) {
				case "VARIETY":
					if(count == 2) {
						String file1Name=(String)fileListModel.getElementAt(0);
						Layer layer1=gui.layers.get(gui.hm.get(file1Name));
						String file2Name=(String)fileListModel.getElementAt(1);
						Layer layer2=gui.layers.get(gui.hm.get(file2Name));
						Layer[] inLayers = {layer1,layer2};
						Layer newLayer = xxx.Algorithm.localVariety(inLayers,"layer");
						newLayer.save(outputFileName);
						System.out.println("localVariety:Success");
					}
					break;
				case "MAXIMUM":
					if(count == 2) {
						String file1Name=(String)fileListModel.getElementAt(0);
						Layer layer1=gui.layers.get(gui.hm.get(file1Name));
						String file2Name=(String)fileListModel.getElementAt(1);
						Layer layer2=gui.layers.get(gui.hm.get(file2Name));
						Layer newLayer = layer1.localMaximum(layer2, "layer");
						newLayer.save(outputFileName);
					}
					break;
				case "MINIMUM":	
					if(count == 2) {
						String file1Name=(String)fileListModel.getElementAt(0);
						Layer layer1=gui.layers.get(gui.hm.get(file1Name));
						String file2Name=(String)fileListModel.getElementAt(1);
						Layer layer2=gui.layers.get(gui.hm.get(file2Name));
						Layer newLayer = layer1.localMinimum(layer2, "layer");
						newLayer.save(outputFileName);
					}
					break;
				case "SUM":
					if(count==2) {
						String file1Name=(String)fileListModel.getElementAt(0);
						Layer layer1=gui.layers.get(gui.hm.get(file1Name));
						String file2Name=(String)fileListModel.getElementAt(1);
						Layer layer2=gui.layers.get(gui.hm.get(file2Name));
						Layer newLayer=layer1.localSum(layer2, "layer");
						newLayer.save(outputFileName);
					}
					break;
				case "MEAN":
					if(count==2) {
						String file1Name=(String)fileListModel.getElementAt(0);
						Layer layer1=gui.layers.get(gui.hm.get(file1Name));
						String file2Name=(String)fileListModel.getElementAt(1);
						Layer layer2=gui.layers.get(gui.hm.get(file2Name));
						Layer newLayer = layer1.localMean(layer2, "layer");
						newLayer.save(outputFileName);
					}
					break;
				default:
					break;
				}
				
				gui.newFile(outputFileName, fileName);
				
				dispose();
//				dispatchEvent(new WindowEvent(LocalWindow.this,WindowEvent.WINDOW_CLOSING));

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

		//-------------------------click cancle
		btnCancle.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				dispose();
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


	}
	
	public String getFilePath() {
		return outputFileName;
	}
	
	public String getLayerName() {
		return fileName;
	}

}