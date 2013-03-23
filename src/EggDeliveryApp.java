import java.util.Random;


public class EggDeliveryApp {

	private Random random;
	
	private EggDeliveryGUI gui;
	private Clock clock;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;
	
	public EggDeliveryApp() {
		random = new Random();
		
		gui = new EggDeliveryGUI();
		
		clock = new Clock(gui);
		
		logger = new Logger();
		logger.setReadout(gui);
		//logger.setLogFile(outFile);
		
		farm = new FarmHouse();
		farm.setGUI(gui);
		
		cleo = new Cleo(farm);
		cleo.setGUI(gui);

		// start the clock
		Thread clockThread = new Thread(clock);
		clockThread.start();

		//cleo.collectEggs();

		// test the text area
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					int len = random.nextInt(16);
					String str = "";
					for (int i = 0; i < len; i++) str += (char)(random.nextInt(64) + 64);

					try {
						//logger.log("" + clock.getTime() + " | " + str);
						logger.log("Next order: " + (int)( -100 * Math.log( random.nextDouble() ) ) );
						logger.log("Order time: " + (int)( 10 * random.nextGaussian() + 50 ) );
						logger.log("Next egg: " + (int)( 100 * random.nextGaussian() + 500 ) );
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}				
			}
		}).start();

	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryApp app = new EggDeliveryApp();
	}
}
