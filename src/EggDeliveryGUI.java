/* 
 * Othman Smihi - ICS 462 Program 2 
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class EggDeliveryGUI extends JFrame implements ClockFace, Readout {
	private static final long serialVersionUID = 1254856031749675648L;

	// Color palette
	// http://kuler.adobe.com/#themeID/2301655
	private Color[] colors = {
		new Color(137,161,144),		// dark gray
		new Color(172,184,161),		// medium gray 
		new Color(212,207,177),		// light gray
		new Color(252,228,182),		// beige
		new Color(232,146,136)		// lipstick color
	};
	
	private JPanel mainPanel;

	private EggGUIPanel cleoPanel;
	private EggGUIPanel timePanel;
	
	private JPanel farmPanel;
	private EggGUIPanel orderPanel;
	private EggGUIPanel stashPanel;
	private EggGUIPanel hensPanel;
	
	private JPanel textPanel;
	private JTextArea textArea;
	
	public EggDeliveryGUI() {
		// set up main panel
		makeMainPanel();
		
		makeFarmPanel();
		makeCleoPanel();
		makeTextPanel();		
		makeTimePanel();

		mainPanel.add(cleoPanel);
		mainPanel.add(timePanel);
		mainPanel.add(farmPanel);
		mainPanel.add(textPanel);				
		
		// set up frame
		makeFrame();
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					cleoPanel.flip();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					hensPanel.flip();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
		
	}

	private void makeMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(720,600));
		mainPanel.setLayout(new GridLayout(2,2,10,10));
		mainPanel.setBorder(new LineBorder(colors[0],4));
		mainPanel.setBackground(colors[2]);
	}
	
	private void makeFrame() {
		add(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ICS 462 Program 2 - Othman Smihi");
		setVisible(true);
	}
	
	private void makeCleoPanel() {
		cleoPanel = new EggGUIPanel("res/cleo1.png", "res/cleo2.gif", 128);
	}

	private void makeFarmPanel() {
		farmPanel = new JPanel();
		farmPanel.setOpaque(false);
		farmPanel.setLayout(new BorderLayout(0,0));
		
		orderPanel = new EggGUIPanel("res/order.png");
		stashPanel = new EggGUIPanel("res/stash.png");
		hensPanel = new EggGUIPanel("res/hen1.gif", "res/hen.gif");
		
		farmPanel.add(orderPanel, BorderLayout.NORTH);
		farmPanel.add(stashPanel, BorderLayout.CENTER);
		farmPanel.add(hensPanel, BorderLayout.SOUTH);
	}
	
	private void makeTextPanel() {
		textPanel = new JPanel(); 
		textPanel.setPreferredSize(new Dimension(310,248));
		textPanel.setLayout(new BorderLayout(4,4));
		textArea = new JTextArea("");
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane tScroll = new JScrollPane(textArea);
		tScroll.setPreferredSize(textPanel.getPreferredSize());
		tScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(tScroll, BorderLayout.CENTER);
	}
	
	public void writeText(String text) {
		textArea.append("\n" + text);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	private void makeTimePanel() {
		timePanel = new EggGUIPanel("res/clock.gif");
		timePanel.setValue("0");
	}
	
	public void setTime(long time) {
		timePanel.setValue("" + time);
	}
}
