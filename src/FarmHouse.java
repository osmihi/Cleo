import java.util.ArrayList;

public class FarmHouse {
	private int orders;				// number of orders
	private int stash;				// number of eggs in stash
	private ArrayList<Hen> hens;	// 
	
	public FarmHouse() {
		orders = 0;
		stash = 0;
		hens = new ArrayList<Hen>();
		
		for (int i = 0; i < 100; i++) {
			hens.add(new Hen());
		}
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
	
	public void addHen(Hen newHen) {
		hens.add(newHen);
	}
	
	public Hen removeAnyHen() throws NoHenException {
		if (!hens.isEmpty()) {
			return hens.remove(0);
		} else {
			throw new NoHenException("There are no hens in the farmhouse!");
		}
	}
	
	public Hen removeSpecificHen(Hen specificHen) throws NoHenException {
		if (hens.contains(specificHen)) {
			hens.remove(specificHen);
			return specificHen;
		} else {
			throw new NoHenException("That hen is not in the farmhouse!");
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
	
	public void closeOrder() {
		orders--;
	}

}
