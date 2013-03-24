
public class Hen {
	
	private int eggs;
	private long nextEggTime;
	
	public Hen() {
		eggs = 0;
		nextEggTime = 0;
	}
	
	public Hen(long firstEgg) {
		nextEggTime = firstEgg;
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
		return eggs;
	}
	
	public int collectEggs() {
		int eggsCollected = eggs;
		eggs = 0;
		return eggsCollected;
	}
	
	public long getNextEggTime() {
		return nextEggTime;
	}
	
	public void setNextEggTime(long time) {
		nextEggTime = time;
	}
}
