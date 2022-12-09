package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ProjectTest {
	public static void main(String[] args) throws InterruptedException {
		// Local animation
//		Layer layer = new Layer("", args[0]);
//		Layer inLayer = new Layer("", args[1]);
//		layer.localSumLearning(inLayer, "", 3, -10000, args[0]);
		
		// Focal animation
//		Layer layer = new Layer("", args[0]);
//		layer.focalVarietyLearning(10, false, "", 3, -10000, args[0]);
//		
		// Zonal animation
//		Layer layer = new Layer("", args[0]);
//		Layer zoneLayer = new Layer("", args[1]);
//		layer.zonalMinimumLearning(zoneLayer, "", 50, -10000, -20000, args[0]);
		
		// Dijkstra
		Layer layer = new Layer("", args[0]);
		Layer outLayer = layer.dijkstra("", false, 0);
//		outLayer.print();
		outLayer.save(args[1]);
	}
}
