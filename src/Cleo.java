
public class Cleo {
	
	private FarmHouse cleosFarm;
	private EggDeliveryGUI gui;
	
	public Cleo(FarmHouse fh) {
		cleosFarm = fh;
	}
	
	public void setGUI(EggDeliveryGUI gui) {
		this.gui = gui;
		gui.setCleoState();
	}
	
	public void collectEggs() {
		if (gui != null) gui.setCleoState("Collecting");
	}
	
	public void deliver() {
		if (gui != null) gui.setCleoState("Delivering");
	}
	
	public void hatch() {
		cleosFarm.removeFromStash(1);
		cleosFarm.addHen();
	}
	
	public void kill() throws NoHenException {
		cleosFarm.removeAnyHen();
	}
	
}
