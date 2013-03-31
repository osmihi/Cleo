/* 
 * Othman Smihi - ICS 462 Program 2 
 * 
 * 
 * http://www.java-forums.org/advanced-java/4130-rounding-double-two-decimal-places.html
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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
	
	private ClockMode speed = ClockMode.MEDIUM;
	
	private JPanel mainPanel;

	private JPanel titlePanel;
	private EggGUIPanel cleoPanel;
	
	private JPanel timePanel;
	private EggGUIPanel clockPanel;
	private JLabel ordersFilled;
	private JLabel ordersFilledMean;
	private JLabel ordersFilledStdDev;
	
	private DecimalFormat df;
	
	private JPanel farmPanel;
	private EggGUIPanel orderPanel;
	private EggGUIPanel stashPanel;
	private EggGUIPanel hensPanel;
	
	private JPanel textPanel;
	private JTextArea textArea;
	
	public EggDeliveryGUI() {
		df = new DecimalFormat("#.##");
		
		// set up main panel
		makeMainPanel();
		
		makeFarmPanel();
		makeTitlePanel();
		makeTextPanel();		
		makeTimePanel();

		mainPanel.add(titlePanel);
		mainPanel.add(timePanel);
		mainPanel.add(farmPanel);
		mainPanel.add(textPanel);				
		
		// set up frame
		makeFrame();		
	}

	private void makeMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(800,600));
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

	private void makeTitlePanel() {
		titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BorderLayout(5,5));

		JLabel titleLabel = new JLabel(" Cleo's Farm");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
		JLabel subTitleLabel = new JLabel("   Othman Smihi - ICS 462 Operating Systems");
		subTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		
		JPanel titleBox = new JPanel();
		titleBox.setLayout(new BorderLayout(4,4));
		titleBox.setOpaque(false);
		titleBox.add(titleLabel, BorderLayout.NORTH);
		titleBox.add(subTitleLabel, BorderLayout.CENTER);
		
		cleoPanel = new EggGUIPanel("res/cleo1.png", "res/cleo2.gif", 128, 28);
		
		titlePanel.add(titleBox, BorderLayout.NORTH);
		titlePanel.add(cleoPanel, BorderLayout.CENTER);
		titlePanel.add(new JLabel(), BorderLayout.SOUTH);
	}
	
	private void makeFarmPanel() {
		farmPanel = new JPanel();
		farmPanel.setOpaque(false);
		farmPanel.setLayout(new GridLayout(0,1,2,2));
		
		orderPanel = new EggGUIPanel("res/order.png", null, 64, 24);
		stashPanel = new EggGUIPanel("res/stash.png", null, 64, 24);
		hensPanel = new EggGUIPanel("res/hen1.gif", "res/hen.gif", 64, 24);
		
		farmPanel.add(orderPanel);
		farmPanel.add(stashPanel);
		farmPanel.add(hensPanel);
	}
	
	private void makeTextPanel() {
		textPanel = new JPanel(); 
		textPanel.setPreferredSize(new Dimension(310,248));
		textPanel.setLayout(new BorderLayout(4,4));
		textPanel.setOpaque(false);
		textArea = new JTextArea("");
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JLabel logNote = new JLabel("note: Complete program output is written to log.txt, located in the working directory.");
		logNote.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
		
		JScrollPane tScroll = new JScrollPane(textArea);
		tScroll.setPreferredSize(textPanel.getPreferredSize());
		tScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(tScroll, BorderLayout.CENTER);
		textPanel.add(logNote, BorderLayout.SOUTH);
	}
	
	public void writeText(String text) {
		String txt = textArea.getText();
		txt = txt.length() < 1000 ? txt : txt.substring(txt.length() - 1000);
		textArea.setText(txt);
		
		textArea.append("\n" + text);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	private void makeTimePanel() {
		timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout(10,25));
		timePanel.setOpaque(false);
		
		clockPanel = new EggGUIPanel("res/clock.gif", null, 64, 32);
		clockPanel.setValue("0");
		
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BorderLayout(5,5));
		statsPanel.setOpaque(false);
		
		JLabel statsLabel = new JLabel("Order statistics");
		statsLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
		
		JPanel lStats = new JPanel();
		lStats.setLayout(new GridLayout(0,1,3,3));
		lStats.setOpaque(false);
		
		JPanel rStats = new JPanel();
		rStats.setLayout(new GridLayout(0,1,3,3));
		rStats.setOpaque(false);
		
		ordersFilled = new JLabel(strFmt(0,8));
		ordersFilled.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		ordersFilledMean = new JLabel(strFmt(0,8));
		ordersFilledMean.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		ordersFilledStdDev = new JLabel(strFmt(0,8));
		ordersFilledStdDev.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		
		lStats.add(new JLabel("Orders filled")).setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		lStats.add(new JLabel("Mean order time")).setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		lStats.add(new JLabel("Standard Deviation")).setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		rStats.add(ordersFilled);
		rStats.add(ordersFilledMean);
		rStats.add(ordersFilledStdDev);
		
		statsPanel.add(statsLabel, BorderLayout.NORTH);
		statsPanel.add(lStats, BorderLayout.WEST);
		statsPanel.add(rStats, BorderLayout.EAST);

		JPanel timeControlPanel = new JPanel();
		timeControlPanel.setLayout(new GridLayout(1,0,5,5));
		timeControlPanel.setOpaque(false);
		
		JButton slowBtn = new JButton(" Slow ");
		JButton medBtn = new JButton(" Medium ");
		JButton fastBtn = new JButton(" Fast ");
		JButton instBtn = new JButton("Fastest");
		
		slowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = ClockMode.SLOW;
			}
		});
		
		medBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = ClockMode.MEDIUM;
			}
		});

		fastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = ClockMode.FAST;
			}
		});
		
		instBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = ClockMode.INSTANT;
			}
		});
		
		timeControlPanel.add(slowBtn);
		timeControlPanel.add(medBtn);
		timeControlPanel.add(fastBtn);
		timeControlPanel.add(instBtn);
		
		timePanel.add(clockPanel, BorderLayout.NORTH);
		timePanel.add(statsPanel, BorderLayout.CENTER);
		timePanel.add(timeControlPanel, BorderLayout.SOUTH);		
	}
	
	public void setCleoState(Cleo.CleoState cleoState) {
		String cleoStr = "";
		if (cleoState == Cleo.CleoState.IDLE) {
			cleoStr = "Idle";
			if (!cleoPanel.flip()) cleoPanel.flip();
		} else {
			if (cleoState == Cleo.CleoState.COLLECTING) cleoStr = "Collecting";
			if (cleoState == Cleo.CleoState.DELIVERING) cleoStr = "Delivering";
			if (cleoPanel.flip()) cleoPanel.flip();
		}

		cleoPanel.setValue(cleoStr);
	}
	
	public void setTime(long time) {
		clockPanel.setValue("" + time);
	}
	
	public void setHens(int numHens, int numHenEggs) {
		// for fun, animate hen pic sometimes
		if ( numHenEggs > 0 ) {
			if (hensPanel.flip()) hensPanel.flip();
		} else {
			if (!hensPanel.flip()) hensPanel.flip();
		}
		hensPanel.setValue("" + numHens + " (" + numHenEggs + " eggs)");
	}

	public void setOrders(int numOrders) {
		orderPanel.setValue("" + numOrders);
	}
	
	public void setOrderStats(int count, double mean, double stdDev) {
		ordersFilled.setText(strFmt(count, 8));
		ordersFilledMean.setText(strFmt(mean, 8));
		ordersFilledStdDev.setText(strFmt(stdDev, 8));
	}
	
	// format our numbers to a string 
	private String strFmt(double num, int len) {
		// Round number to 2 decimal places
		String str = String.format("%.2f", Double.valueOf(df.format(num)));
		// fill spaces to len
		for (int i = len - str.length(); i > 0; i--) str = " " + str;
		return str + " ";
	}
	
	private String strFmt(int num, int len) {
		// fill spaces after
		String str = "" + num + "   ";
		// fill spaces before
		for (int i = len - str.length(); i > 0; i--) str = " " + str;
		return str + " ";
	}
	
	public void setStash(int stashSize) {
		stashPanel.setValue("" + stashSize);
	}
	
	@Override
	public ClockMode getSpeed() {
		return speed;
	}

	@Override
	public void setSpeed(ClockMode spd) {
		speed = spd;
	}
	
	public void quit() {
			JOptionPane.showMessageDialog(this, "Program complete. Press OK to quit.", "Program complete.", JOptionPane.ERROR_MESSAGE);
	}
}
