
public class Cleo implements Runnable {
	
	public enum CleoState {
		IDLE, COLLECTING, DELIVERING
	}
	
	private FarmHouse farm;
	
	private CleoState state;
	
	private boolean doCollect;
	
	private int collected;
	private boolean henKilled;
	
	public Cleo(FarmHouse fh) {
		farm = fh;
		state = CleoState.IDLE;
		doCollect = true;
		collected = 0;
		henKilled = false;
	}

	@Override
	public synchronized void run() {
		while (true) {
			// when our stash surpasses 1200, kill one hen.
			if (farm.countStash() >= 1100 && doCollect) {
				killHen();
				doCollect = false;
			}
			
			if (!doCollect && farm.countStash() <= 1000) {
				doCollect = true;
			}
			
			// if there are eggs to collect, get them
			if (farm.countHenEggs() > 0 && doCollect) {
				collectEggs();
			}
			
			// if there is an order to deliver, deliver it
			if (farm.countOrders() > 0 && farm.countStash() >= 12) {
				deliver();
			}
			
			try {
				state = CleoState.IDLE;
				
				if ( !(farm.countHenEggs() > 0 && doCollect) && !(farm.countOrders() > 0 && farm.countStash() >= 12) ) {
					wait(); // if there's nothing to do, wait
				}
 
			} catch (InterruptedException e) {}
		}
	}
	
	public synchronized void collectEggs() {
		state = CleoState.COLLECTING;
		
		while (state == CleoState.COLLECTING) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public synchronized void beginEggCollection(long time) {
		collected = farm.collectEggs(time);
	}
	
	public synchronized int completeEggCollection() {
		farm.addEggs(collected); 							// put them in the stash
		
		int returnCollected = collected;
		collected = 0;

		return returnCollected;
	}
	
	public synchronized void deliver() {
		state = CleoState.DELIVERING;
		
		while (state == CleoState.DELIVERING) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public synchronized void completeDelivery() {
		farm.removeFromStash(12); // take away 12 eggs from stash
		
		farm.completeOrder(); // take away 1 order
	}
	
	public synchronized boolean checkHens(long time) {
		if (farm.countStash() <= 13 && farm.countStash() > 0) {
			farm.hatchEgg(new Hen(time + EggDeliveryController.nextEgg()));
			return true;
		}
		return false;
	}

	public CleoState getState() {
		return state;
	}
	
	public synchronized void idle() {
		state = CleoState.IDLE;
	}
	
	private void killHen() throws NoHenException {
		farm.removeAnyHen();
		henKilled = true;
	}
	
	public boolean henKilled() {
		boolean hk = henKilled;
		henKilled = false;
		return hk;
	}
}
