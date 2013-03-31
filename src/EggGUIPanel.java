/* Othman Smihi - ICS 462 Program 2
 * 
 * EggGUIPanel.java
 * 
 * This is a GUI element I made to pair a flippable picture with some text.
 * It gets used several times in the GUI.
 */

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EggGUIPanel extends JPanel {
	private static final long serialVersionUID = -3120365435274336510L;

	private PicLabel pic;					// a PicLabel is an inner class defined below
	private JLabel val;						// display for text
	
	public EggGUIPanel(String picFile) {
		this(picFile, null, 64);
	}
	
	public EggGUIPanel(String picFile, String altPicFile) {
		this(picFile, altPicFile, 64);
	}
	
	public EggGUIPanel(String picFile, int size) {
		this(picFile, null, size);
	}

	public EggGUIPanel(String picFile, String altPicFile, int size, int fontSize) {
		super();
		this.setLayout(new GridLayout(1,2,5,5));
		this.setOpaque(false);
		
		pic = new PicLabel(picFile, size);
		if (altPicFile != null) pic.setAltPic(altPicFile);
		
		val = new JLabel("");
		val.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
		
		this.add(pic);
		this.add(val);
	}
	
	public EggGUIPanel(String picFile, String altPicFile, int size) {
		this(picFile, altPicFile, size, size / 4);
	}
	
	// toggle between main and alternate picture
	public boolean flip() {
		return pic.flip();
	}
	
	public void setValue(String value) {
		val.setText(value);
	}

	private class PicLabel extends JLabel {
		private static final long serialVersionUID = -1287588379795601986L;

		private ImageIcon img1;					// picture A
		private ImageIcon img2;					// picture B
		private int picSize;					// picture size
		
		public PicLabel(String imgLoc, int sz) {
			picSize = sz;
			setOpaque(false);
			ClassLoader cl = this.getClass().getClassLoader();
			java.net.URL rsc = cl.getResource(imgLoc); 
			img1 = new ImageIcon(rsc);
			Image image1 = img1.getImage().getScaledInstance(picSize, picSize, java.awt.Image.SCALE_FAST);
			img1 = new ImageIcon(image1);
			this.setIcon(img1);
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setVerticalAlignment(JLabel.CENTER);
		}
		
		public void setAltPic(String imgLoc) {
			ClassLoader cl = this.getClass().getClassLoader();
			img2 = new ImageIcon(cl.getResource(imgLoc));
			Image image2 = img2.getImage().getScaledInstance(picSize, picSize, java.awt.Image.SCALE_FAST);
			img2 = new ImageIcon(image2);
		}
		
		// switch between picture A and B
		public boolean flip() {
			if (this.getIcon() == img1) {
				this.setIcon(img2);
				return false;
			} else {
				this.setIcon(img1);
				return true;
			}
		}
	}
}
