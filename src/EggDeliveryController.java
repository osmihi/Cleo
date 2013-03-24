import java.io.File;
import java.io.IOException;
import java.util.Random;

public class EggDeliveryController extends Clock {

	private static Random random;
	
	private EggDeliveryGUI gui;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;

	private long nextOrderTime;
	private long deliveryDoneTime;
	private long deliveryDuration;
	
	private long collectionDoneTime;
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
			farm.addHen();
			
			gui.setHens(farm.countHens(), farm.countHenEggs());
			logger.log(getTime(), "New hen!", farm.countStash(), farm.countHens());
		}
		
		deliveryDuration = 0;
		collected = 0;
		
		cleo = new Cleo(farm);
		gui.setCleoState(Cleo.CleoState.IDLE);

		nextOrderTime = getTime() + nextOrder();

		Thread cleoThread = new Thread(cleo);
		cleoThread.start();
		
		// start the clock
		Thread clockThread = new Thread(this);
		clockThread.start();

	}

	@Override
	public synchronized void clockAction() {
		// a while loop is used instead of an if statement because it is possible that the next generated time will be 0.
		while (getTime() == nextOrderTime) {
			// signal a new order to be made
			farm.receiveOrder();

			logger.log(getTime(), "Order in!", farm.countStash(), farm.countHens());
			
			nextOrderTime = getTime() + nextOrder();

			synchronized(cleo) {
				cleo.notify(); // now that there's an order to deliver, tell cleo there's something to do
			}
		}

		while (getTime() == farm.nextEggTime()) {
			if ( farm.henLaysEgg(getTime() + nextEgg()) ) {

				logger.log(getTime(), "Hen laid egg.", farm.countStash(), farm.countHens());
				
				synchronized(cleo) {
					cleo.notify(); // now that there are some eggs to be collected, tell cleo there's something to do
				}
			}
		}
		
		if (cleo.getState() == Cleo.CleoState.DELIVERING && deliveryDoneTime < getTime()) {
			
			gui.setCleoState(Cleo.CleoState.DELIVERING);
			
			farm.removeFromStash(12); // take away 12 eggs from stash
			
			farm.completeOrder(); // take away 1 order

			deliveryDuration = deliveryTime();
			deliveryDoneTime = getTime() + deliveryDuration;
		}

		if (getTime() == deliveryDoneTime) {
			logger.log(getTime(), "Delivery completed (" + deliveryDuration + ").", farm.countStash(), farm.countHens());
			
			deliveryDuration = 0;
			
			synchronized(cleo) {
				gui.setCleoState(Cleo.CleoState.IDLE);
				cleo.idle(); // ok cleo, you're free to do something else now
			}
		}

		if (cleo.getState() == Cleo.CleoState.COLLECTING && collectionDoneTime < getTime()) {
			
			gui.setCleoState(Cleo.CleoState.COLLECTING);
			
			collected = 0;
			collectionDoneTime = getTime();
			
			for (Hen h: farm.getHens()) {
				if (h.getEggs() > 0) {
					collected += h.collectEggs(); 				// get all the eggs from the hens
					h.setNextEggTime(getTime() + nextEgg()); 	// give all the hens next egg times
					farm.addHenToQueue(h);
					collectionDoneTime += 2;					// wait for 2 time units per hen
				}
			}
		}

		if (getTime() == collectionDoneTime) {
			farm.addEggs(collected); 							// put them in the stash
			
			
			logger.log(getTime(), collected + " eggs collected.", farm.countStash(), farm.countHens());			

			collected = 0;
			
			synchronized(cleo) {
				gui.setCleoState(Cleo.CleoState.IDLE);
				cleo.idle(); // ok cleo, you're free to do something else now
			}
		}

		// refresh gui
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
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryController app = new EggDeliveryController();
	}

}
