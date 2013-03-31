/* Othman Smihi - ICS 462 Program 2
 * 
 *  Clock.java
 *  This is the abstract superclass of our EggDeliveryController.
 *  Basically it's a loop that increments a counter (the time) after each iteration.
 *  There's a speed setting that can be controlled via the GUI to affect how much 
 *  real time is takes to get through an iteration. This is helpful for watching what's
 *  going on in the program. 
 *  
 *  There are abstract methods for the continueCondition(), runAction() and endAction()
 *  that must be implemented by any extending class. This would allow any class to extend
 *  the clock and specify when it should end, what is should do each cycle, and what it 
 *  should do when it ends. This is the "template method" design pattern.
 *  
 *  A Clock also keeps an instance of a ClockFace, which is an interface that should
 *  be implemented by a GUI element. The Clock gets its speed from the ClockFace and also
 *  updates it with the time periodically. 
 */

public abstract class Clock implements Runnable {	

	private long time;				// current time
	private ClockFace face;			// gui element
	ClockFace.ClockMode speed;		// current speed of the clock
	
	public Clock() {
		time = 0;
	}

	@Override
	public void run() {
		while (continueCondition()) {				// subclass-defined condition for continuation of clock
			try {
				if (face != null) {
					speed = face.getSpeed();		// get speed from the gui element
					face.setTime(time);				// update the gui element with the current time 

					// depending on the current speed, wait some amount of real time each cycle (or not)
					if (speed == ClockFace.ClockMode.SLOW) {
						Thread.sleep(100);
					} else if (speed == ClockFace.ClockMode.MEDIUM) {
						Thread.sleep(10);
					} else if (speed == ClockFace.ClockMode.FAST) {
						Thread.sleep(1);
					}
				}

				runAction();						// subclass-defined code to run during each cycle
				
				time++;								// increment the time by one unit
			} catch (InterruptedException e) {}
		}
		
		endAction();								// subclass-defined code to terminate the clock
	}

	// set the gui element that the Clock should interact with.
	// if none is defined, the Clock should still work, but will run in "fastest" mode 
	public void setClockFace(ClockFace cf) {		
		face = cf;
	}
	
	// simply return the current time
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
