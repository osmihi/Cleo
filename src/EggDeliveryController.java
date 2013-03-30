import java.io.File;
import java.io.IOException;
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
	private long collectionDuration;
	private int collected;
	
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

		// TODO let's get rid of the gui dependencies in these classes.
		// better practice is to use this EggDeliveryController class as the controller.
		farm = new FarmHouse();
		farm.setGUI(gui);
		
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
		collectionDuration = 0;
		collected = 0;
		
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
		// TODO what to do at the end

		try {
			cleoThread.join();
			clockThread.join();
		} catch (InterruptedException e) {}

		// TODO log when hens are killed
 
	}
	
	@Override
	public synchronized void runAction() {
		// a while loop is used instead of an if statement because it is possible that the next order will be at the same time.
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

		while (getTime() == farm.nextEggTime()) {
			if ( farm.henLaysEgg(getTime() + nextEgg()) ) {

				logger.log(getTime(), "Egg Laid", farm.countStash(), farm.countHens(), farm.countHenEggs());
				
				synchronized(cleo) {
					cleo.notify(); // now that there are some eggs to be collected, tell cleo there's something to do
				}
			}
		}
		
		// being delivery
		if (cleo.getState() == Cleo.CleoState.DELIVERING && deliveryDoneTime < getTime()) {
			beginDelivery();
		}

		// complete delivery
		if (getTime() == deliveryDoneTime) {
			completeDelivery();
		}

		if (cleo.getState() == Cleo.CleoState.COLLECTING && collectionDoneTime < getTime()) {
			beginEggCollection();
		}

		if (getTime() == collectionDoneTime) {
			completeEggCollection();
		}

		// refresh gui
		gui.setStash(farm.countStash());
		gui.setOrders(farm.countOrders());
		gui.setHens(farm.countHens(), farm.countHenEggs());
		gui.setCleoState(cleo.getState());
		
	}

	private synchronized void beginDelivery() {
		gui.setCleoState(Cleo.CleoState.DELIVERING);
		
		farm.removeFromStash(12); // take away 12 eggs from stash
		
		farm.completeOrder(); // take away 1 order

		deliveryDuration = deliveryTime();
		deliveryDoneTime = getTime() + deliveryDuration;
	}
	
	private synchronized void completeDelivery() {
		logger.log(getTime(), "Delivery (" + deliveryDuration + ")", farm.countStash(), farm.countHens(), farm.countHenEggs());
		
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
		
		deliveryDuration = 0;
		
		// after serving a customer, if stash <= 13, cleo hatches one egg into a hen.
		if (farm.countStash() <= 13 && farm.countStash() > 0) {
			farm.hatchEgg(new Hen(getTime() + nextEgg()));
			logger.log(getTime(), "New hen", farm.countStash(), farm.countHens(), farm.countHenEggs());
		}

		synchronized(cleo) {
			gui.setCleoState(Cleo.CleoState.IDLE);
			cleo.idle(); // ok cleo, you're free to do something else now
		}

	}
	
	private synchronized void beginEggCollection() {
		gui.setCleoState(Cleo.CleoState.COLLECTING);

		// note: Eggs laid during collection are not collected.
		
		collected  = farm.collectEggs(getTime());
		collectionDuration = 2; // it always takes 2 time units to collect eggs

		collectionDoneTime = getTime() + collectionDuration;
	}

	private synchronized void completeEggCollection() {
		farm.addEggs(collected); 							// put them in the stash
		
		logger.log(getTime(), "Collect " + collected + " (" + collectionDuration + ")", farm.countStash(), farm.countHens(), farm.countHenEggs());			

		collected = 0;
		collectionDuration = 0;
		
		synchronized(cleo) {
			gui.setCleoState(Cleo.CleoState.IDLE);
			cleo.idle(); // ok cleo, you're free to do something else now
		}		
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
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryController app = new EggDeliveryController();
	}
}
