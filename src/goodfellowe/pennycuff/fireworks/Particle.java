/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

/**
 * @author Corey
 *
 */
public class Particle {
	// Constants
	final public boolean ALIVE = true;
	final public boolean DEAD = false;
	final public int GRAVITY = 3;
	
	// State variables
	private boolean state;

	/**
	 * 
	 */
	public Particle() {
		state = DEAD;
	}

}
