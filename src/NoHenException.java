/* Othman Smihi - ICS 462 Program 2
 * 
 * There are several situations where we're dealing with Hens in collections
 * and it seemed appropriate to create an Exception for these cases. It does
 * absolutely nothing special.
 * 
 */

public class NoHenException extends IllegalArgumentException {
	private static final long serialVersionUID = 5146311518509563134L;

	public NoHenException() {
		super();
	}
	
	public NoHenException(String message) {
		super(message);
	}
}
