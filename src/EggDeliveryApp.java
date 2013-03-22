
public class EggDeliveryApp {

	private EggDeliveryGUI gui;
	private Clock clock;
	private Logger logger;
	private FarmHouse farm;
	private Cleo cleo;
	
	public EggDeliveryApp() {
		gui = new EggDeliveryGUI();
		
		clock = new Clock(gui);
		
		logger = new Logger();
		logger.setReadout(gui);
		//logger.setLogFile(outFile);
		
		farm = new FarmHouse();
		
		cleo = new Cleo(farm);
		
		// start the clock
		Thread clockThread = new Thread(clock);
		clockThread.start();		
		
		// test the text area
		for (int i = 0; i < 100; i++) {
			try {
				logger.log("" + clock.getTime());
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		EggDeliveryApp app = new EggDeliveryApp();
	}
}
