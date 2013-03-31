/* Othman Smihi - ICS 462 Program 2
 * 
 * Hen.java
 * 
 * This class represents a hen. It's pretty straightforward.
 * Each hen has a number of eggs it's sitting on and a time that they'll
 * lay their next egg.
 */
public class Hen {
	
	private int eggs;				// number of eggs the hen is currently sitting on
	private long nextEggTime;		// time that this hen should lay its next egg
	
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
