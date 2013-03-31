import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class EggDeliveryController extends Clock {

	private static Random random;
	
	private EggDeliveryGUI gui;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;
	
	Thread cleoThread;
	Thread clockThread;

	private long nextOrderTime;
	private long deliveryDoneTime;
	private long deliveryDuration;

	private Queue<Long> orderTimes;				// time at which each order came in 
	private List<Long> orderFillTimes;			// time it took to fill each order
	private int ordersFilled;
	private double orderFillMean;
	private double orderFillStdDev;
	
	private long collectionDoneTime;
	
	public EggDeliveryController() {
		random = new Random();
		
		gui = new EggDeliveryGUI();
		setClockFace(gui);
		
		logger = new Logger();
		logger.setReadout(gui);
		try {
			logger.setLogFile(new File("log.txt"));
		} catch (IOException e) {
			System.out.println("Error creating log file.");
		}

		farm = new FarmHouse();
		
		gui.setOrders(0);
		gui.setStash(0);
		gui.setHens(farm.countHens(), farm.countHenEggs());
		
		// start with 100 hens
		for (int i = 0; i < 100; i++) {
			farm.addHen(new Hen(getTime() + nextEgg()));
			
			gui.setHens(farm.countHens(), farm.countHenEggs());
			logger.log(getTime(), "New hen", farm.countStash(), farm.countHens(), farm.countHenEggs());
		}

		deliveryDoneTime = -1;
		collectionDoneTime = -1;

		deliveryDuration = 0;
		
		cleo = new Cleo(farm);
		gui.setCleoState(Cleo.CleoState.IDLE);
		
		nextOrderTime = getTime() + nextOrder();

		orderTimes = new LinkedList<Long>();
		orderFillTimes = new ArrayList<Long>();
		ordersFilled = 0;
		orderFillMean = 0;
		orderFillStdDev = 0;

		cleoThread = new Thread(cleo);
		cleoThread.start();
		
		// start the clock
		clockThread = new Thread(this);
		clockThread.start();

	}

	@Override
	protected boolean continueCondition() {
		return getTime() <= 100000;
	}

	@Override
	public void endAction() {
		gui.setCleoState(Cleo.CleoState.IDLE);

		DecimalFormat df = new DecimalFormat("#.##");
		
		logger.log("");
		logger.log("==================================================");
		logger.log("");
		logger.log("Simulation complete!");
		logger.log("");
		logger.log("Eggs left in the stash: " + farm.countStash() + "");
		logger.log("");
		logger.log("Number of orders filled: " + ordersFilled + "");
		logger.log("");
		logger.log("Mean time to fill an order: " + Double.valueOf(df.format(orderFillMean)) + " time units.");
		logger.log("");
		logger.log("Standard deviation of order fill time: " + Double.valueOf(df.format(orderFillStdDev)) + " time units.");
		logger.log("");


		// Show exit dialog and attempt to open log file.
		try {
			Thread.sleep(5000);
			gui.quit();
			logger.showLog();
		} catch (Exception e) {} finally {
			System.exit(0);
		}
	}
	
	@Override
	public synchronized void runAction() {
		// While loops are used instead of an if statement because it is possible that the next order/egg is at the same time.

		// Did any orders come in? If so, tell cleo!
		while (getTime() == nextOrderTime) {
			// signal a new order to be made
			farm.receiveOrder();

			logger.log(getTime(), "Order in", farm.countStash(), farm.countHens(), farm.countHenEggs());

			nextOrderTime = getTime() + nextOrder();

			orderTimes.add(getTime());
			
			synchronized(cleo) {
				cleo.notify(); // now that there's an order to deliver, tell cleo there's something to do
			}
		}

		// Should any eggs be laid? If one is laid, tell cleo!
		while (getTime() == farm.nextEggTime()) {
			if ( farm.henLaysEgg(getTime() + nextEgg()) ) {

				logger.log(getTime(), "Egg Laid", farm.countStash(), farm.countHens(), farm.countHenEggs());
				
				synchronized(cleo) {
					cleo.notify(); // now that there are some eggs to be collected, tell cleo there's something to do
				}
			}
		}
		
		// Begin the delivery process
		if (cleo.getState() == Cleo.CleoState.DELIVERING && deliveryDoneTime < getTime()) {
			gui.setCleoState(Cleo.CleoState.DELIVERING);

			deliveryDuration = deliveryTime();
			deliveryDoneTime = getTime() + deliveryDuration;
		}

		// Complete the delivery process
		if (getTime() == deliveryDoneTime) {
			cleo.completeDelivery();
			
			logger.log(getTime(), "Delivery took: " + deliveryDuration + "", farm.countStash(), farm.countHens(), farm.countHenEggs());
			
			updateOrders();
			
			deliveryDuration = 0;
			
			// after serving a customer, if stash <= 13, cleo hatches one egg into a hen.
			if (cleo.checkHens(getTime())) {
				logger.log(getTime(), "New hen", farm.countStash(), farm.countHens(), farm.countHenEggs());
			}

			synchronized(cleo) {
				gui.setCleoState(Cleo.CleoState.IDLE);
				cleo.idle(); // ok cleo, you're free to do something else now
				cleo.notify();
			}
		}

		// Begin the egg collection process
		if (cleo.getState() == Cleo.CleoState.COLLECTING && collectionDoneTime < getTime()) {
			gui.setCleoState(Cleo.CleoState.COLLECTING);

			// note: Eggs laid during collection are not collected.
			cleo.beginEggCollection(getTime());

			collectionDoneTime = getTime() + 2;
		}

		// Complete the egg collection process
		if (getTime() == collectionDoneTime) {
			int collected = cleo.completeEggCollection();
			
			logger.log(getTime(), "Collected " + collected, farm.countStash(), farm.countHens(), farm.countHenEggs());			

			synchronized(cleo) {
				gui.setCleoState(Cleo.CleoState.IDLE);
				cleo.idle(); // ok cleo, you're free to do something else now
				cleo.notify();
			}	
		}

		if (cleo.henKilled()) {
			logger.log(getTime(), "Hen killed!", farm.countStash(), farm.countHens(), farm.countHenEggs());	
			gui.setHens(farm.countHens(), farm.countHenEggs());
		}
		
		// Refresh the GUI values
		gui.setStash(farm.countStash());
		gui.setOrders(farm.countOrders());
		gui.setHens(farm.countHens(), farm.countHenEggs());
		gui.setCleoState(cleo.getState());
	}
	
	public static long nextOrder() {
		return (long)( -100 * Math.log( random.nextDouble() ) );
	}

	public static long deliveryTime() {
		return (long)( 10 * random.nextGaussian() + 50 ); 
	}

	public static long nextEgg() {
		return (long)( 100 * random.nextGaussian() + 500 );
	}
	
	private void updateOrders() {
		long orderFillTime = getTime() - orderTimes.poll();
		orderFillTimes.add(orderFillTime);
		
		ordersFilled++;
	
		// some inefficient calculations of mean & standard deviation to follow...
		orderFillMean = 0;
		for (long oft : orderFillTimes) {
			orderFillMean += (double)oft;
		}
		orderFillMean = orderFillMean / ordersFilled;
		
		orderFillStdDev = 0;
		
		for (long oft : orderFillTimes) {
			orderFillStdDev += Math.pow( (((double)oft) - orderFillMean), 2);
		}
		
		orderFillStdDev = Math.sqrt( orderFillStdDev / ordersFilled );

		gui.setOrderStats(ordersFilled, orderFillMean, orderFillStdDev);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryController app = new EggDeliveryController();
	}
}
