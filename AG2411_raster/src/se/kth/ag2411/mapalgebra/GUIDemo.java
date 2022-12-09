package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
public class GUIDemo {
	public static void main(String[] args) {
		// Create a JFrame, which will be the main window of this application and contain GUI components
		JFrame appFrame = new JFrame();
		// Create GUI components
		JButton northButton = new JButton("North");
		JButton southButton = new JButton("South");
		JLabel eastLabel = new JLabel("East");
		JLabel westLabel = new JLabel("West");
		JTextArea centerTextArea = new JTextArea("Center");
		// Set the background color of centerTextArea to green
		centerTextArea.setBackground(Color.GREEN);
		// Set the preferredSize of centerTextArea
		Dimension dimension = new Dimension(300, 200);

		centerTextArea.setPreferredSize(dimension);
		// Make centerTextArea wrap text lines
		centerTextArea.setLineWrap(true);
		// Add the GUI component created above to appFrame
		appFrame.add(northButton, BorderLayout.PAGE_START);
		appFrame.add(southButton, BorderLayout.PAGE_END);
		appFrame.add(westLabel, BorderLayout.LINE_START);
		appFrame.add(eastLabel, BorderLayout.LINE_END);
		appFrame.add(centerTextArea, BorderLayout.CENTER);
		// place appFrame in the center of the screen
		appFrame.setLocationRelativeTo(null);
		// Optimize the arrangement the components contained by appFrame
		appFrame.pack();
		// make appFrame visible
		appFrame.setVisible(true);
		// make appFrame close when its close button is clicked
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}