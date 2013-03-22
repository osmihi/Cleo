
public class Cleo {
	
	private FarmHouse cleosFarm;
	
	public Cleo(FarmHouse fh) {
		cleosFarm = fh;
	}
	
	public void collectEggs() {}
	
	public void deliver() {}
	
	public void hatch() {
		cleosFarm.removeFromStash(1);
		cleosFarm.addHen(new Hen());
	}
	
	public void kill() throws NoHenException {
		cleosFarm.removeAnyHen();
	}
	
}
