import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

import javax.swing.JComponent;

/**
 * 
 * @author Sam Maynard
 * @version 1.0
 * @date February 12th, 2013
 * @period 4
 * @teacher Fowler
 * Curve class.  Holds data for drawing point, and drawComponent() to do so.
 *
 */
@SuppressWarnings("serial")
public class Curve extends JComponent {
	
	float iterations = 20; // pursuit iterations
	// how far in you want it to follow
	int sides = 14;
	double spacingFactor = 4; //arbitrary factor for niceness
	
	Point[] points = new Point[sides]; // points in the polygon
	Polygon p;
	
	// aesthetics values
	double saturationStart = .8;
	double brightnessStart = .5;
	double hueChange = .009;
	double saturationChange = .001;
	double brightnessChange = 0;
	int alpha = 100;
	
	boolean predefined = false;
	
	// instantiates the size and the data variables.
	public Curve(int width, int height) {
		this.setSize(width, height);
		
		for(int i = 0; i < sides; i++) {
			points[i] = new Point();
		}
		p = new Polygon();
	}
	
	// generates points approximately randomly around a circle
	// divides a circle into evenly spaced slices and generates a random point within that slice
	public void generateCircluarPoints() {
		int i = 0;
		double slice = (360.0 / sides);
		for(double angleTick = 0; angleTick < 360.0; angleTick += slice) {
			double x = 0, y = 0;
			double rand = Math.random() * slice;
			double angle = angleTick + rand;
			double hyp = this.getHeight() / 2;
			x = (this.getWidth() / 2.0) + Math.cos(angle * Math.PI / 180.0) * hyp;
			y = (this.getHeight() / 2.0) + Math.sin(angle * Math.PI / 180.0) * hyp;
			points[i].setLocation(x, y);
			p.addPoint((int) x, (int) y);
			i++;
		}
	}
	
	// generates points completely randomly
	public void generateRandomPoints() {
		for(int i = 0; i < sides; i++) {
			double x = 0, y = 0;
			x = Math.random() * this.getWidth();
			y = Math.random() * this.getHeight();
			x = x * 2 - this.getWidth() / 2; // scale factor to get the object to take up more of the screen
			y = y * 2 - this.getHeight() / 2; // "                                                          "
			points[i].setLocation(x, y);
			p.addPoint((int) x, (int) y);
			i++;
		}
	}
	
	public void usePredefinedPoints(List<Point> points) {
		this.sides = points.size();
		this.points = points.toArray(new Point[1]);
		for(Point point : points) {
			p.addPoint((int) point.getX(), (int) point.getY());
		}
		predefined = true;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * paints the Curve
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE); //clearing
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		float hue = (float) Math.random(); // random start color
		float saturation = (float) saturationStart; // set up top
		float brightness = (float) brightnessStart; // set up top
		Color color = makeTransparent(hue, saturation, brightness, alpha); // makes the HSB value transparent
		
		double minorPercentage = (1.0 / iterations) * spacingFactor;
		double majorPercentage = 1.0 - minorPercentage;
		
		if(predefined) {
			g.setColor(color);
			g.drawPolygon(p);
		}
		for(int i = 0; i < iterations; i++) { // makes the pursuit curve
			g.setColor(color);
			
			p = new Polygon();
			for(int j = 0; j < points.length; j++) { // one iteration of pursuit curve
				Point a = points[j];
				Point b;
				if(j + 1 >= points.length) {
					b = points[0];
				} else {
					b = points[j + 1];
				}
				double x = majorPercentage * a.getX() + minorPercentage * b.getX();
				double y = majorPercentage * a.getY() + minorPercentage * b.getY();
				Point c = new Point();
				c.setLocation(x, y);
				points[j] = c;
				p.addPoint((int) x, (int) y);
			}
			
			g.drawPolygon(p);
			
			// change by predetermined values
			hue += hueChange;
			saturation += saturationChange;
			brightness += brightnessChange;
			color = makeTransparent(hue, saturation, brightness, alpha);
			
		}
		
	}
	
	/**
	 * @param hue The hue of the HSB color
	 * @param saturation The saturation of the HSB color
	 * @param brightness The brightness of the HSB color
	 * @param alpha The alpha of the color to be made from the HSB color
	 * Takes a HSB color and an alpha value and turns it into a RGB color with an alpha vlaue
	 */
	private Color makeTransparent(float hue, float saturation, float brightness, int alpha) {
		Color color = Color.getHSBColor(hue, saturation, brightness);
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		color = new Color(red, green, blue, alpha);
		return color;
	}
}
