package se.kth.ag2411.mapalgebra;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class RoundedBorder implements Border {
	
	private int radius;
	
	public RoundedBorder(int radius){
		this.radius = radius;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		g.drawRoundRect(x, y, width-1, height-1, radius, radius);
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(0, 15, 0, 15);
	}

	public boolean isBorderOpaque() {
		// TODO Auto-generated method stub
		return true;
	}

}
