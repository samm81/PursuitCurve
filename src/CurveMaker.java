import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * 
 * @author Sam Maynard
 * @version 1.0
 * @date February 12th, 2013
 * @period 4
 * @teacher Fowler
 * Class to instantiate, create images of, and save Curve(s).
 *
 */
@SuppressWarnings("serial")
public class CurveMaker extends JFrame {
	
	enum PointGeneration {
		RANDOM, CIRCLE, PREDEFINED
	}
	
	PointGeneration generationStyle = PointGeneration.RANDOM;
	
	Curve curve; // the curve
	
	final int width = 800;
	final int height = 800;
	
	int imageNum = 1; // used to keep track of file names
	BufferedImage img;
	
	private List<Point> predefinedPoints = Arrays.asList(new Point[] { new Point(100, 100), new Point(100, 700), new Point(150, 700), new Point(150, 100) });
	
	/*
	 * Instantiates a CurveMaker
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		CurveMaker cm = new CurveMaker();
	}
	
	/*
	 * default constructor
	 * does the bulk of the work.
	 */
	public CurveMaker() {
		this.setSize(width, height);
		this.setTitle("Left Click for Next, Right Click to Save (WARNING: WILL OVERWRITE)");
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) { // left click
					System.out.println("next");
					next();
				} else if(e.getButton() == MouseEvent.BUTTON3) { // right click
					System.out.println("saved");
					save();
				}
			}
			
		});
		
		curve = new Curve(this.getWidth(), this.getHeight()); // instantiate curve
		
		next(); // generates a Curve
		this.setVisible(true); // shows the frame
	}
	
	/*
	 * generates a Curve and stores it in img
	 */
	private void next() {
		switch(generationStyle) {
		case RANDOM:
			curve.generateRandomPoints();
			break;
		case CIRCLE:
			curve.generateCircluarPoints();
			break;
		case PREDEFINED:
			curve.usePredefinedPoints(predefinedPoints);
			break;
		}
		
		img = getScreenShot(curve); // the BufferedImage is a 'screen shot' of the generated Curve
		this.repaint();
	}
	
	/*
	 * saves img into a file
	 */
	private void save() {
		try {
			ImageIO.write(img, "png", new File("curve" + imageNum + ".png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		imageNum++;
	}
	
	/*
	 * captures a 'screen shot' of the Curve
	 */
	public static BufferedImage getScreenShot(JComponent component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
		component.paint(image.getGraphics()); // has the Curve paint itself into img
		return image;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 * Draws the BufferedImage generated in next()
	 */
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}
}