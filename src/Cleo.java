/* Othman Smihi - ICS 462 Program 2
 * 
 *  Cleo.java
 *  
 *  This class represents our bunny hero, Cleo. It runs in its own thread and does Cleo's
 *  actions as described in the requirements. The goal was to have neither Cleo nor the 
 *  Farm need to be aware of the Controller class, although Cleo does have a reference to
 *  the farm. 
 */

public class Cleo implements Runnable {
	
	public enum CleoState { 			// Cleo can be in one of these three states at any given time
		IDLE, COLLECTING, DELIVERING
	}
	
	private FarmHouse farm;				// reference to the farm object
	
	private CleoState state;			// Cleo's current state
	
	private boolean doCollect;			// is collection currently enabled? it turns off after stash = 1100
	 									// and re-enables when stash <= 1000 again
	
	private int collected;				// number of eggs collected during last egg collection

	private boolean henKilled;			// did we just kill a hen?
	
	public Cleo(FarmHouse fh) {			// initialize our variables
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
			
			// if we have suspended collection, re-enable it when stash reaches 1000
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
			
			try {	// end of loop. set cleo to idle state, but loop again in case there's still more to do
					// otherwise, wait until cleo is notified that there's work to do
				state = CleoState.IDLE;
				
				if ( !(farm.countHenEggs() > 0 && doCollect) && !(farm.countOrders() > 0 && farm.countStash() >= 12) ) {
					wait(); // if there's nothing to do, wait
				}
 
			} catch (InterruptedException e) {}
		}
	}
	
	// put cleo into egg collection mode
	public synchronized void collectEggs() {
		state = CleoState.COLLECTING;
		
		while (state == CleoState.COLLECTING) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	// collect eggs from the henhouse
	public synchronized void beginEggCollection(long time) {
		collected = farm.collectEggs(time);
	}
	
	// finish egg collection activities.
	public synchronized int completeEggCollection() {
		farm.addEggs(collected); 							// put them in the stash
		
		int returnCollected = collected;
		collected = 0;

		return returnCollected;
	}
	
	// put cleo into delivery mode
	public synchronized void deliver() {
		state = CleoState.DELIVERING;
		
		while (state == CleoState.DELIVERING) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	// complete the delivery process by filling a customer order
	public synchronized void completeDelivery() {
		farm.removeFromStash(12); // take away 12 eggs from stash
		
		farm.completeOrder(); // take away 1 order
	}
	
	// check if we should hatch an egg
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
