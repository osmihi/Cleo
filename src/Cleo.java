
public class Cleo implements Runnable {
	
	public enum CleoState {
		IDLE, COLLECTING, DELIVERING
	}
	
	private FarmHouse farm;
	
	private CleoState state;
	
	public Cleo(FarmHouse fh) {
		farm = fh;
		state = CleoState.IDLE;
	}

	@Override
	public synchronized void run() {
		while (true) {			
			// if there are eggs to collect, get them
			if (farm.countHenEggs() > 0) {
				collectEggs();
			}

			// if there is an order to deliver, deliver it
			if (farm.countOrders() > 0 && farm.countStash() >= 12) {
				deliver();
			}
			
			try {
				state = CleoState.IDLE;
				if (farm.countHenEggs() < 1 && ( farm.countOrders() < 1 || farm.countStash() < 12 ) ) wait(); // if there's nothing to do, wait
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
	
	public void hatch() {
		farm.removeFromStash(1);
		farm.addHen();
	}
	
	public void kill() throws NoHenException {
		// TODO farm.removeAnyHen();
	}
	
}
