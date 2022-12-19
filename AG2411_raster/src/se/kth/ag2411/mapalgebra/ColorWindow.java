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

public class ColorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame newWindow;
	public String inputFile;
	public String inputColor;
	public String outputFileName;
	
	// Launch the application
		public static void main() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ColorWindow window = new ColorWindow();
						window.newWindow.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		public ColorWindow() {
			newWindow = new JFrame();
			newWindow.setTitle("Recolor");
			newWindow.setBounds(400, 100, 382, 217);

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(TestGUI.mainColor2);
			newWindow.setContentPane(panel);
			
			JLabel lblInputFile = new JLabel("Choose layer to recolor:");
			lblInputFile.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblInputFile.setBounds(60, 17, 250, 23);
			panel.add(lblInputFile);
			
			JComboBox<String> comboBoxLayer = new JComboBox<String>();
			comboBoxLayer.setBounds(60, 50, 250, 23);
			panel.add(comboBoxLayer);
			
			for (int i = 0; i < TestGUI.layerList.size(); i++) {
				String layerName = TestGUI.layerList.get(i).name;
				comboBoxLayer.addItem(layerName);
			}
			
			inputFile = (String) comboBoxLayer.getItemAt(TestGUI.chosenIndex);
			if (! TestGUI.layerList.isEmpty()) {
				comboBoxLayer.setSelectedIndex(TestGUI.chosenIndex);
			}
			
			comboBoxLayer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputFile = (String) comboBoxLayer.getSelectedItem();
				}			
			});
			
			JComboBox<String> comboBoxColor = new JComboBox<String>();
			comboBoxColor.setBounds(60, 116, 250, 23);
			comboBoxColor.addItem("Red");
			comboBoxColor.addItem("Blue");
			comboBoxColor.addItem("Green");
			comboBoxColor.addItem("Yellow");
			comboBoxColor.addItem("Grayscale");
			panel.add(comboBoxColor);
			
			comboBoxColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					inputColor = (String) comboBoxColor.getSelectedItem();
				}			
			});
			
			inputColor = comboBoxColor.getItemAt(0);
			
			JLabel lblColorScheme = new JLabel("Choose color scheme:");
			lblColorScheme.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblColorScheme.setBounds(60, 83, 250, 23);
			panel.add(lblColorScheme);
			
			// Color hashmap
			HashMap<String, Color> hmColor = new HashMap<String, Color>();
			hmColor.put("Red", new Color(255, 0, 0));
			hmColor.put("Blue", new Color(0, 0, 255));
			hmColor.put("Green", new Color(0, 255, 0));
			hmColor.put("Yellow", new Color(255, 255, 0));
			hmColor.put("Grayscale", new Color(0, 0, 0));
			
			JButton btnRecolor = new JButton("RECOLOR");
			btnRecolor.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(inputFile == null) {
						JOptionPane.showMessageDialog(new JFrame(),"Error when trying to export: No layer loaded.");
						return;
					}
					
					int recolorIndex = comboBoxLayer.getSelectedIndex();
					TestGUI.imageList.remove(recolorIndex);
					Layer recolorLayer = TestGUI.layerList.get(recolorIndex);
					BufferedImage outputImage = recolorLayer.toImageColor(hmColor.get(inputColor));
					TestGUI.imageList.add(recolorIndex, outputImage);
					
					TestGUI.chosenIndex = 0;
					
					TestGUI.aboveLayer = TestGUI.layerList.get(recolorIndex);
					
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
					
					
					
					newWindow.dispose();
				}
			});
			btnRecolor.setFont(new Font("Dialog", Font.BOLD, 12));
			btnRecolor.setBounds(270, 149, 88, 23);
			panel.add(btnRecolor);
			
			JButton btnCancel = new JButton("CANCEL");
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					newWindow.dispose();
				}
			});
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.setBounds(172, 149, 88, 23);
			panel.add(btnCancel);
			
			
		}
}