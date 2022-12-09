package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Ex02 {
	public static void main(String[] args) {
		try {
			if (args.length == 3) {
				Layer layer = new Layer(args[0], args[1]);
				double dScale = Double.parseDouble(args[2]);
				int scale = (int)Math.round(dScale);
				BufferedImage image = layer.toImage();
				
				MapPanel map = new MapPanel(image, scale);
				
				// Create a JFrame, which will be the main window of this demo application
				JFrame appFrame = new JFrame();
				// Create GUI components
				JButton northButton = new JButton("North");
				JButton southButton = new JButton("South");
				JLabel eastLabel = new JLabel("East");
				JLabel westLabel = new JLabel("West");
				JTextArea centerTextArea = new JTextArea("Center");
				// Set centerTextArea’s background color to green
				// centerTextArea.setBackground(Color.GREEN);
				// Set centerTextArea’s preferredSize to 300 x 200
				Dimension dimension = new Dimension(300 , 200);
				centerTextArea.setPreferredSize(dimension);
				// Make centerTextArea wrap text lines
				centerTextArea.setLineWrap(true);
				// Add the GUI components to appFrame
				appFrame.add(northButton, BorderLayout.PAGE_START);
				appFrame.add(southButton, BorderLayout.PAGE_END);
				appFrame.add(westLabel, BorderLayout.LINE_START);
				appFrame.add(eastLabel, BorderLayout.LINE_END);
				appFrame.add(map);
				// appFrame.add(centerTextArea, BorderLayout.CENTER);
				// Optimize the arrangement the components contained by appFrame
				appFrame.pack();
				// make appFrame visible
				appFrame.setVisible(true);
				appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			else {
				int length = args.length - 3;
				double[] inputs = new double[length];
				
				for (int i = 0; i < length; i++) {
					inputs[i] = Double.parseDouble(args[i + 3]);
				}
				
				Layer layer = new Layer(args[0], args[1]);
				
				double dScale = Double.parseDouble(args[2]);
				int scale = (int)Math.round(dScale);
				BufferedImage image = layer.toImage(inputs);
				
				MapPanel map = new MapPanel(image, scale);
				
				// Create a JFrame, which will be the main window of this demo application
				JFrame appFrame = new JFrame();
				// Create GUI components
				JButton northButton = new JButton("North");
				JButton southButton = new JButton("South");
				JLabel eastLabel = new JLabel("East");
				JLabel westLabel = new JLabel("West");
				JTextArea centerTextArea = new JTextArea("Center");
				// Set centerTextArea’s background color to green
				// centerTextArea.setBackground(Color.GREEN);
				// Set centerTextArea’s preferredSize to 300 x 200
				Dimension dimension = new Dimension(300 , 200);
				centerTextArea.setPreferredSize(dimension);
				// Make centerTextArea wrap text lines
				centerTextArea.setLineWrap(true);
				// Add the GUI components to appFrame
				appFrame.add(northButton, BorderLayout.PAGE_START);
				appFrame.add(southButton, BorderLayout.PAGE_END);
				appFrame.add(westLabel, BorderLayout.LINE_START);
				appFrame.add(eastLabel, BorderLayout.LINE_END);
				appFrame.add(map);
				// appFrame.add(centerTextArea, BorderLayout.CENTER);
				// Optimize the arrangement the components contained by appFrame
				appFrame.pack();
				// make appFrame visible
				appFrame.setVisible(true);
				appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Incorrect input");
		}
		
	}
}