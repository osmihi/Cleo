import java.util.Random;

public class EggDeliveryApp extends Clock {

	private Random random;
	
	private EggDeliveryGUI gui;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;

	private long nextOrderTime;
	private long deliveryDoneTime;
	private long nextEggTime;

	public EggDeliveryApp() {
		random = new Random();
		
		gui = new EggDeliveryGUI();
		setClockFace(gui);
		
		logger = new Logger();
		logger.setReadout(gui);
		//logger.setLogFile(outFile);
		
		farm = new FarmHouse();
		farm.setGUI(gui);
		
		cleo = new Cleo(farm);
		cleo.setGUI(gui);

		nextOrderTime = getTime() + nextOrder();
		
		// start the clock
		Thread clockThread = new Thread(this);
		clockThread.start();

		//cleo.collectEggs();

//		// test the text area
//		new Thread(new Runnable() {
//			public void run() {
//				while (true) {
//					int len = random.nextInt(16);
//					String str = "";
//					for (int i = 0; i < len; i++) str += (char)(random.nextInt(64) + 64);
//
//					try {
//						logger.log("" + getTime() + " | Next order at: " + nextOrderTime + " | " + str);
//						Thread.sleep(100);
//					} catch (InterruptedException e) {}
//				}				
//			}
//		}).start();

	}

	@Override
	public void clockAction() {
		// a while loops is used instead of an if statement because it is possible that generated time will be 0.
		while (getTime() == nextOrderTime) {
			// signal a new order to be made
			farm.receiveOrder();
			if (gui != null) gui.setOrders(farm.countOrders());
			logger.log(getTime(), "Order in!", farm.countStash(), farm.countHens());
			nextOrderTime = getTime() + nextOrder();
		}
//		while (getTime() == deliveryDoneTime) {
//			// signal that the delivery is done
//			// ??
//		}
//		while (getTime() == nextEggTime) {
//			// signal that a new egg should be made
//			// ??
//		}
		
	}
	
	public long nextOrder() {
		return (long)( -100 * Math.log( random.nextDouble() ) );
	}

	public long deliveryTime() {
		return (long)( 10 * random.nextGaussian() + 50 ); 
	}

	public long nextEgg() {
		return (long)( 100 * random.nextGaussian() + 500 );
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryApp app = new EggDeliveryApp();
	}

}
