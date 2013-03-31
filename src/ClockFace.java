/* Othman Smihi - ICS 462 Program 2
 * 
 * ClockFace.java
 * 
 * This interface specifies the methods that will need to be implemented by a class
 * that can be used with an object that extends Clock. Also, the enum of available
 * modes (speeds) that a Clock can be in are provided in this interface.
 * 
 */
public interface ClockFace {
	public enum ClockMode {SLOW, MEDIUM, FAST, INSTANT}

	public void setTime(long time);
	public void setSpeed(ClockMode spd);
	public ClockMode getSpeed();
}
