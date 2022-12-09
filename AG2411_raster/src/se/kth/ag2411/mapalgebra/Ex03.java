package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Ex03 {
	public static void main(String[] args) {
		String operation = args[0];
		String outLayerName = args[3];
		int scale = 3;
		BufferedImage image;
		MapPanel map = null;
		
		JFrame appFrame = new JFrame("GIS");
		
		JButton northButton = new JButton("North");
		appFrame.add(northButton, BorderLayout.PAGE_START);
		
		Dimension dimension = new Dimension(800, 500);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if (operation.equals("localSum")) {
			Layer inLayer1 = new Layer("", args[1]);
			Layer inLayer2 = new Layer("", args[2]);
			Layer outLayer = inLayer1.localSum(inLayer2, "");
			outLayer.save(outLayerName);
			image = outLayer.toImage();
			map = new MapPanel(image, scale);
		}
		else if (operation.equals("focalVariety")) {
			Layer inLayer = new Layer("", args[1]);
			Layer outLayer = inLayer.focalVariety(1, true, "");
			outLayer.save(outLayerName);
			image = outLayer.toImage();
			map = new MapPanel(image, scale);
		}
		else if (operation.equals("zonalMinimum")) {
			Layer valueLayer = new Layer("", args[1]);
			Layer zoneLayer = new Layer("", args[2]);
			Layer outLayer = valueLayer.zonalMinimum(zoneLayer, outLayerName);
			outLayer.save(outLayerName);
			image = outLayer.toImage();
			map = new MapPanel(image, scale);
		}
		// For testing the getNeighborhood method
		else if (operation.equals("neighborhoodSize")) {
			Layer inLayer = new Layer("", args[1]);
			Layer outLayer = inLayer.neighborhoodSize(1, true, "");
			outLayer.save(outLayerName);
			image = outLayer.toImage();
			map = new MapPanel(image, scale);
		}
		else {
			System.out.print(operation + " is not currently available.");
			System.exit(0);
		}
		appFrame.add(map);
		map.setPreferredSize(dimension);
		appFrame.pack();
		appFrame.setVisible(true);
	}
}