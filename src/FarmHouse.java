/* Othman Smihi - ICS 462 Program 2
 * 
 * FarmHouse.java
 * 
 * This class holds the orders, stash, and hen data, as well as operations to manipulate them.
 * When using queues, I used the PriorityBlockingQueue from the java.util.concurrent package,
 * although I'm not sure if this is totally necessary or not. Since we're doing a bunch of
 * concurrent stuff, I figured it's good to be on the safe side.
 * 
 * Most of what's going on in this class is pretty self-explanatory.
 */

import java.util.concurrent.PriorityBlockingQueue;

public class FarmHouse {
	
	private int orders;									// number of orders
	private int stash;									// number of eggs in stash
	private PriorityBlockingQueue<Hen> hens;			// hens in the FarmHouse
	private PriorityBlockingQueue<Hen> nextEggQueue;	// tracks hens that are going to lay eggs
	
	public FarmHouse() {
		orders = 0;
		stash = 0;
		hens = new PriorityBlockingQueue<Hen>(100, new HenComparator());
		nextEggQueue = new PriorityBlockingQueue<Hen>(100, new HenComparator());
	}
	
	public int countOrders() {
		return orders;
	}
	
	public int countStash() {
		return stash;
	}
	
	public int countHens() {
		return hens.size();
	}
	
	public int countHenEggs() {
		int henEggs = 0;
		for (Hen h : hens) {
			if (h != null) henEggs += h.getEggs();
		}
		return henEggs;
	}
	
	public void addHen(Hen newHen) {
		hens.add(newHen);
		nextEggQueue.add(newHen);
	}
	
	public int collectEggs(long currentTime) {
		int eggsCollected = 0;

		for (Hen h: hens) {
			if (h.getEggs() > 0) {
				eggsCollected += h.collectEggs(); 				// get all the eggs from the hens
				nextEggQueue.remove(h);
				h.setNextEggTime(currentTime + EggDeliveryController.nextEgg()); 	// give the hen next egg time
				nextEggQueue.add(h);						// put them back in the next egg queue
			}
		}
		
		return eggsCollected;
	} 
	
	public void hatchEgg(Hen h) {
		stash--;
		addHen(h);
	}
	
	public long nextEggTime() {
		if (nextEggQueue.peek() == null) return 0;
		return nextEggQueue.peek().getNextEggTime();
		
	}
	
	// Hen lays an egg
	public boolean henLaysEgg(long nextEgg) {
		Hen h = nextEggQueue.poll();
		boolean eggWasLaid = h.layEgg();
		if (eggWasLaid) {
			h.setNextEggTime(nextEgg);
			nextEggQueue.add(h);
		}
		return eggWasLaid;
	}
	
	// Hen is killed
	public Hen removeAnyHen() throws NoHenException {
		if (!hens.isEmpty()) {
			Hen h = hens.poll();
			return h;
		} else {
			throw new NoHenException("There are no hens in the farmhouse!");
		}
	}
	
	public void addEggs(int numEggs) {
		stash = stash + numEggs;
	}
	
	public int removeFromStash(int numEggs) {
		stash = stash - numEggs;
		return numEggs;
	}
	
	public void receiveOrder() {
		orders++;
	}
	
	public void completeOrder() {
		orders--;
	}
}
