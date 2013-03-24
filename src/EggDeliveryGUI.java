/* 
 * Othman Smihi - ICS 462 Program 2 
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
	
	private ClockMode speed = ClockMode.SLOW;
	
	private JPanel mainPanel;

	private EggGUIPanel cleoPanel;
	
	private JPanel timePanel;
	private EggGUIPanel clockPanel;
	
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
		cleoPanel = new EggGUIPanel("res/cleo1.png", "res/cleo2.gif", 128);
		makeTextPanel();		
		makeTimePanel();

		mainPanel.add(cleoPanel);
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

	private void makeFarmPanel() {
		farmPanel = new JPanel();
		farmPanel.setOpaque(false);
		farmPanel.setLayout(new GridLayout(0,1,2,2));
		
		//JLabel farmLabel = new JLabel("Farmhouse");
		orderPanel = new EggGUIPanel("res/order.png");
		stashPanel = new EggGUIPanel("res/stash.png");
		hensPanel = new EggGUIPanel("res/hen1.gif", "res/hen.gif");
		
		//farmPanel.add(farmLabel);
		farmPanel.add(orderPanel);
		farmPanel.add(stashPanel);
		farmPanel.add(hensPanel);
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
		timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout(5,5));
		timePanel.setOpaque(false);
		
		clockPanel = new EggGUIPanel("res/clock.gif");
		clockPanel.setValue("0");
		
		JPanel timeControlPanel = new JPanel();
		timeControlPanel.setLayout(new GridLayout(1,3,5,5));
		timeControlPanel.setOpaque(false);
		
		JButton slowBtn = new JButton(" Slow ");
		JButton fastBtn = new JButton(" Fast ");
		JButton instBtn = new JButton("Instant");
		
		slowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = ClockMode.SLOW;
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
		timeControlPanel.add(fastBtn);
		timeControlPanel.add(instBtn);
		
		timePanel.add(clockPanel, BorderLayout.NORTH);
		timePanel.add(timeControlPanel, BorderLayout.SOUTH);		
	}
	
	public void setCleoState(String cleoState) {
		if ("".equals(cleoState)) {
			setCleoState();
		} else {
			cleoPanel.setValue(cleoState);
			if (cleoPanel.flip()) {
				cleoPanel.flip();
			}
		}
		
	}
	
	public void setCleoState() {
		cleoPanel.setValue("Idle");
		if (!cleoPanel.flip()) {
			cleoPanel.flip();
		}
	}
	
	public void setTime(long time) {
		clockPanel.setValue("" + time);
	}
	
	public void setHens(int numHens, int numHenEggs) {
		if ( (numHens & 1) != 1) hensPanel.flip(); // for fun, animate hen pic on even # hens
		hensPanel.setValue("" + numHens + " (" + numHenEggs + ")");
	}
	
	public void setOrders(int numOrders) {
		orderPanel.setValue("" + numOrders);
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
}
