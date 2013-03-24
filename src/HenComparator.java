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
