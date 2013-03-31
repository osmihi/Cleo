/* Othman Smihi - ICS 462 Program 2
 * 
 * HenComparator.java
 * This implementation of the Comparator interface allows us to compare
 * hens to each other so they can be used in a priority queue. 
 * Hens are compared by their next egg time.
 */

import java.util.Comparator;

public class HenComparator implements Comparator<Hen> {

	@Override
	public int compare(Hen hen1, Hen hen2) {
		if (hen1 == null || hen2 == null) throw new NoHenException("Cannot compare null hens.");
		if (hen1.getNextEggTime() < hen2.getNextEggTime()) return -1;
		if (hen1.getNextEggTime() > hen2.getNextEggTime()) return 1;
		return 0;
	}

}
