
public class Cleo implements Runnable {
	
	public enum CleoState {
		IDLE, COLLECTING, DELIVERING
	}
	
	private FarmHouse farm;
	
	private CleoState state;
	
	private boolean doCollect;
	
	public Cleo(FarmHouse fh) {
		farm = fh;
		state = CleoState.IDLE;
		doCollect = true;
	}

	@Override
	public synchronized void run() {
		while (true) {
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
	
	public synchronized void deliver() {
		state = CleoState.DELIVERING;
		
		while (state == CleoState.DELIVERING) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public CleoState getState() {
		return state;
	}
	
	public synchronized void idle() {
		state = CleoState.IDLE;
		notify();
	}
	
	public void killHen() throws NoHenException {
		farm.removeAnyHen();
	}
	
}
