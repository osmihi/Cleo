import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EggGUIPanel extends JPanel {
	private static final long serialVersionUID = -3120365435274336510L;

	private PicLabel pic;
	private JLabel val;
	
	public EggGUIPanel(String picFile) {
		this(picFile, null, 64);
	}
	
	public EggGUIPanel(String picFile, String altPicFile) {
		this(picFile, altPicFile, 64);
	}
	
	public EggGUIPanel(String picFile, int size) {
		this(picFile, null, size);
	}

	public EggGUIPanel(String picFile, String altPicFile, int size) {
		super();
		this.setLayout(new GridLayout(1,2,5,5));
		this.setOpaque(false);
		
		pic = new PicLabel(picFile, size);
		if (altPicFile != null) pic.setAltPic(altPicFile);
		
		val = new JLabel("");
		val.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 32));
		
		this.add(pic);
		this.add(val);
	}
	
	public void flip() {
		pic.flip();
	}
	
	public void setVal(String value) {
		val.setText(value);
	}

	private class PicLabel extends JPanel {
		private static final long serialVersionUID = -1287588379795601986L;

		private JLabel pic;
		private ImageIcon img1;
		private ImageIcon img2;
		private int size;
		
		public PicLabel(String imgLoc, int size) {
			this.size = size;
			setLayout(new BorderLayout(0,0));
			setOpaque(false);
			ClassLoader cl = this.getClass().getClassLoader();
			img1 = new ImageIcon(cl.getResource(imgLoc));
			Image image1 = img1.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
			img1 = new ImageIcon(image1);
			pic = new JLabel(img1, JLabel.CENTER);
			add(pic, BorderLayout.CENTER);
		}
		
		public void setAltPic(String imgLoc) {
			ClassLoader cl = this.getClass().getClassLoader();
			img2 = new ImageIcon(cl.getResource(imgLoc));
			Image image2 = img2.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
			img2 = new ImageIcon(image2);
		}
		
		public void flip() {
			if (pic.getIcon() == img1) {
				pic.setIcon(img2);
			} else {
				pic.setIcon(img1);
			}
		}
	}
}
