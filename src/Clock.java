public abstract class Clock implements Runnable {	

	private long time;
	private ClockFace face;
	
	public Clock() {
		time = 0;
	}

	@Override
	public void run() {
		while (continueCondition()) {
			try {
				if (face != null) {
					ClockFace.ClockMode speed = face.getSpeed();
					face.setTime(time);

					if (speed == ClockFace.ClockMode.SLOW) {
						Thread.sleep(10);
					} else if (speed == ClockFace.ClockMode.MEDIUM) {
						Thread.sleep(1);
					} else if (speed == ClockFace.ClockMode.FAST) {
						if (time % 10 == 0) Thread.sleep(1);
					}
				}

				runAction();
				
				time++;
			} catch (InterruptedException e) {}
		}
		
		endAction();
	}

	public void setClockFace(ClockFace cf) {
		face = cf;
	}
	
	public long getTime() {
		return time;
	}

	// extend Clock and implement this method to define what the clock does
	// (Design Pattern: Template Method)
	public abstract void runAction();

	// similarly, define what happens when the clock stops
	public abstract void endAction();

	// allows us to define when the timer should stop
	protected abstract boolean continueCondition();
		
}
