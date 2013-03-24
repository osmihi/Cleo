import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public class FarmHouse {
	
	private EggDeliveryGUI gui;
	
	private int orders;									// number of orders
	private int stash;									// number of eggs in stash
	private ArrayList<Hen> hens;						// hens in the FarmHouse
	private PriorityBlockingQueue<Hen> nextEggQueue;	// tracks hens that are going to lay eggs
	
	public FarmHouse() {
		orders = 0;
		stash = 0;
		hens = new ArrayList<Hen>();
		nextEggQueue = new PriorityBlockingQueue<Hen>(100, new HenComparator());
	}
	
	public void setGUI(EggDeliveryGUI gui) {
		this.gui = gui;
		if (gui != null) gui.setOrders(0);
		if (gui != null) gui.setStash(0);
		if (gui != null) gui.setHens(countHens(), countHenEggs());
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
			henEggs += h.getEggs();
		}
		return henEggs;
	}
	
	public void addHen() {
		Hen newHen = new Hen(EggDeliveryController.nextEgg());
		hens.add(newHen);
		nextEggQueue.add(newHen);
	}
	
	public long nextEggTime() {
		if (nextEggQueue.peek() == null) return 0;
		return nextEggQueue.peek().getNextEggTime();
		
	}
	
	public boolean henLaysEgg(long nextEgg) {
		Hen h = nextEggQueue.poll();
		boolean eggWasLaid = h.layEgg();
		if (eggWasLaid) {
			h.setNextEggTime(nextEgg);
			nextEggQueue.add(h);
		}
		return eggWasLaid;
	}
	
	public Hen removeAnyHen() throws NoHenException {
		if (!hens.isEmpty()) {
			Hen h = hens.remove(0);
			if (gui != null) gui.setHens(countHens(), countHenEggs());
			return h;
		} else {
			throw new NoHenException("There are no hens in the farmhouse!");
		}
	}
	
	public Hen removeSpecificHen(Hen specificHen) throws NoHenException {
		if (hens.contains(specificHen)) {
			hens.remove(specificHen);
			if (gui != null) gui.setHens(countHens(), countHenEggs());
			return specificHen;
		} else {
			throw new NoHenException("That hen is not in the farmhouse!");
		}
	}
	
	public void addEggs(int numEggs) {
		stash = stash + numEggs;
		if (gui != null) gui.setStash(stash);
	}
	
	public int removeFromStash(int numEggs) {
		stash = stash - numEggs;
		if (gui != null) gui.setStash(stash);
		return numEggs;
	}
	
	public void receiveOrder() {
		orders++;
	}
	
	public void closeOrder() {
		orders--;
		if (gui != null) gui.setOrders(orders);
	}
}
