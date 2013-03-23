
public class Hen {
	private int eggs;
	
	public Hen() {
		eggs = 0;
	}

	public boolean layEgg() {
		if (eggs < 2) {
			eggs++;
			return true;
		} else {
			return false;
		}
	}
	
	public int getEggs() {
		int numEggs = eggs;
		eggs = 0;
		return numEggs;
	}
	
}
