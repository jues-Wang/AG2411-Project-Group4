package se.kth.ag2411.mapalgebra;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
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

import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ExportWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	public String inputFile;
	public String outputFileName;
	
	// Launch the application
		public static void main() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ExportWindow window = new ExportWindow();
						window.newWindow.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		public ExportWindow() {
			newWindow = new JFrame();
			newWindow.setTitle("Export File");
			newWindow.setBounds(400, 100, 427, 217);

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(TestGUI.mainColor2);
			newWindow.setContentPane(panel);
			
			JLabel lblInputFile = new JLabel("Choose layer to save:");
			lblInputFile.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblInputFile.setBounds(60, 10, 250, 23);
			panel.add(lblInputFile);
			
			JComboBox comboBox = new JComboBox();
			comboBox.setBounds(60, 37, 250, 23);
			panel.add(comboBox);
			
			for (int i = 0; i < TestGUI.layerList.size(); i++) {
				String layerName = TestGUI.layerList.get(i).name;
				comboBox.addItem(layerName);
			}
			
			inputFile = (String) comboBox.getItemAt(TestGUI.chosenIndex);
			if (! TestGUI.layerList.isEmpty()) {
				comboBox.setSelectedIndex(TestGUI.chosenIndex);
			}
			
			comboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputFile = (String) comboBox.getSelectedItem();
				}			
			});
			
			JLabel lblOutputFileName = new JLabel("Output file name and location:");
			lblOutputFileName.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblOutputFileName.setBounds(60, 70, 250, 23);
			panel.add(lblOutputFileName);
			
			JTextField tfOutputFile = new JTextField();
			tfOutputFile.setColumns(10);
			tfOutputFile.setBounds(60, 103, 250, 23);
			panel.add(tfOutputFile);
			
			JButton btnOutputFile = new JButton("Browse");
			btnOutputFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			btnOutputFile.setBounds(320, 102, 88, 23);
			panel.add(btnOutputFile);
			
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.addChoosableFileFilter(new FileFilter() {
				public String getDescription() {
					return "JPG Image (*.jpg)";
				}
				public boolean accept(File f) {
					if (f.isDirectory()) {

						return true;
					} else {
						return f.getName().toLowerCase().endsWith(".jpg");
					}
				}
			});
			btnOutputFile.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {		
					int result = fileChooser.showSaveDialog(ExportWindow.this);
					if (result == JFileChooser.APPROVE_OPTION) {					
						outputFileName=fileChooser.getSelectedFile().getPath();
						
						String fileName=fileChooser.getSelectedFile().getName();
						if(fileName.indexOf(".jpg")==-1) {
							outputFileName=outputFileName+".jpg";
						}
						fileChooser.setVisible(true);
						tfOutputFile.setText(outputFileName);
					}				
				}
			});
			
			JButton btnExport = new JButton("EXPORT");
			btnExport.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(outputFileName == null) {
						JOptionPane.showMessageDialog(new JFrame(),"Error when trying to export: No layer loaded.");
						return;
					}
					
					int exportIndex = comboBox.getSelectedIndex();
					BufferedImage outputImage = TestGUI.imageList.get(exportIndex);
					File outputfile = new File(outputFileName);
					try {
						ImageIO.write(outputImage, "jpg", outputfile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					TestGUI.chosenIndex = 0;
					newWindow.dispose();
				}
			});
			btnExport.setFont(new Font("Dialog", Font.BOLD, 12));
			btnExport.setBounds(320, 149, 88, 23);
			panel.add(btnExport);
			
			JButton btnCancel = new JButton("CANCEL");
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					newWindow.dispose();
				}
			});
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.setBounds(222, 149, 88, 23);
			panel.add(btnCancel);
		}
}