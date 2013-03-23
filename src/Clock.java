import java.util.Random;


public class Clock implements Runnable {	
	
	private long time;
	private ClockFace face;
	
	private Random random;
	
	public Clock(ClockFace cf) {
		time = 0;
		face = cf;
	}

	@Override
	public void run() {
		while (time < 100000) {
			try {
				ClockFace.ClockMode speed = face.getSpeed();
				face.setTime(time);

				if (speed == ClockFace.ClockMode.SLOW) {
					Thread.sleep(10);
				} else if (speed == ClockFace.ClockMode.FAST) {
					Thread.sleep(1);
				}

				if (time == nextOrderTime) {
					// signal a new order to be made
				}
				if (time == deliveryDoneTime) {
					// signal that the delivery is done
				}
				if (time == nextEggTime) {
					// signal that a new egg should be made
				}
				
				time++;
			} catch (InterruptedException e) {}
		}
	}
	
	public long getTime() {
		return time;
	}
	
	public int nextOrder() {
		return (int)( -100 * Math.log( random.nextDouble() ) );
	}
	
	public int deliveryTime() {
		return (int)( 10 * random.nextGaussian() + 50 ); 
	}
	
	public int nextEgg() {
		return (int)( 100 * random.nextGaussian() + 500 );
	}
}
