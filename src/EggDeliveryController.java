import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class EggDeliveryController extends Clock {

	private static Random random;
	
	private EggDeliveryGUI gui;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;

	private long nextOrderTime;
	private long deliveryDoneTime;
	
	public EggDeliveryController() {
		random = new Random();
		
		gui = new EggDeliveryGUI();
		setClockFace(gui);
		
		logger = new Logger();
		logger.setReadout(gui);
		//logger.setLogFile(outFile);


		// note: let's get rid of the gui dependencies in these classes.
		// better practice is to use this EggDeliveryController class as the controller.
		farm = new FarmHouse();
		farm.setGUI(gui);
		
		// add 100 Hens to start
		for (int i = 0; i < 100; i++) {
			farm.addHen();
			if (gui != null) gui.setHens(farm.countHens(), farm.countHenEggs());
			logger.log(getTime(), "New hen!", farm.countStash(), farm.countHens());
		}
		
		cleo = new Cleo(farm);
		cleo.setGUI(gui);

		nextOrderTime = getTime() + nextOrder();

		// start the clock
		Thread clockThread = new Thread(this);
		clockThread.start();

	}

	@Override
	public void clockAction() {
		// a while loop is used instead of an if statement because it is possible that generated time will be 0.
		while (getTime() == nextOrderTime) {
			// signal a new order to be made
			farm.receiveOrder();
			if (gui != null) gui.setOrders(farm.countOrders());
			logger.log(getTime(), "Order in!", farm.countStash(), farm.countHens());
			nextOrderTime = getTime() + nextOrder();
		}

//		while (getTime() == deliveryDoneTime) {
//			// TODO signal that the delivery is done
//			// ??
//		}

		while (getTime() == farm.nextEggTime()) {
			if ( farm.henLaysEgg(getTime() + nextEgg()) ) {
				// TODO signal cleo that an egg is waiting
				if (gui != null) gui.setHens(farm.countHens(), farm.countHenEggs());
				logger.log(getTime(), "Hen laid egg.", farm.countStash(), farm.countHens());
			}
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
