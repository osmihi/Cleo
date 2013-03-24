public abstract class Clock implements Runnable {	

	private long time;
	private ClockFace face;

	public Clock() {
		time = 0;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (face != null) {
					ClockFace.ClockMode speed = face.getSpeed();
					face.setTime(time);

					if (speed == ClockFace.ClockMode.SLOW) {
						Thread.sleep(10);
					} else if (speed == ClockFace.ClockMode.FAST) {
						Thread.sleep(1);
					}
				}

				clockAction();
				
				time++;
			} catch (InterruptedException e) {}
		}
	}

	public void setClockFace(ClockFace cf) {
		face = cf;
	}
	
	public long getTime() {
		return time;
	}

	// extend Clock and implement this method to define what the clock does
	// (Design Pattern: Template Method)
	public abstract void clockAction();

}
