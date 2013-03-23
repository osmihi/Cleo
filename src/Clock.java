
public class Clock implements Runnable {
	private long time;
	private ClockFace face;
	
	public Clock(ClockFace cf) {
		time = 0;
		face = cf;
	}
	
	@Override
	public void run() {
		while (time < 100000) {
			try {
				face.setTime(time);
				Thread.sleep(1);
				time++;
			} catch (InterruptedException e) {}
		}	
	}
	
	public long getTime() {
		return time;
	}
}
