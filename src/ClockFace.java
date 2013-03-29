
public interface ClockFace {
	public enum ClockMode {SLOW, MEDIUM, FAST, INSTANT}

	public void setTime(long time);
	public void setSpeed(ClockMode spd);
	public ClockMode getSpeed();
}
