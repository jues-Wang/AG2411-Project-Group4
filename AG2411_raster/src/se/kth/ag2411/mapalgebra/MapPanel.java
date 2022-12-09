package se.kth.ag2411.mapalgebra;

import java.awt.Graphics;
import java.awt.image.*;
import javax.swing.JPanel;

public class MapPanel extends JPanel {
	// MapPanel is a subclass of JPanel and thus inherits all its attributes and methods.
	
	// ATTRIBUTES
	// All the attributes of JPanel (which are automatically inherited), plus:
	public BufferedImage image;
	public int scale;
	
	// CONSRUCTORS
	// All the constructors of JPanel (which are automatically inherited), plus:
	public MapPanel(BufferedImage image, int scale) {
		super(); // first, instantiate a MapPanel in the same way JPanel does.
		this.image = image; // then, initialize additional attributes
		this.scale = scale;
	}
	
	// All the other methods of JPanel (which are automatically inherited), plus: 
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // first, do what JPanel would normally do. Then do:
		g.drawImage(image, 0, 0, image.getWidth()*scale, image.getHeight()*scale, this);
	}
	// The @Override tag will be ignored by the complier. It just signifies that MapPanel modifies JPanel’s paintComponent() method.
}
