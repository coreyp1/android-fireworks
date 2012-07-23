/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;
import android.graphics.Canvas;

/**
 * Explosion Class
 * Superclass for handling all explosions.
 */
public class Explosion {
	protected Explosion.Particle[] particles; // Array for storing particles
	protected int numParticles; // Store the number of particles
	private int screenHeight; // Store the height of the screen
	private Random random = new Random(); // Generate random values
	protected long sleep; // Delay display of the Particles
	private MainActivity activity; // Link to the parent activity

	// Constants
	static final public boolean ALIVE = true;
	static final public boolean DEAD = false;

	// State Variables
	private boolean state;

	/**
	 * Explosion Constructor
	 */
	public Explosion(int x, int y, int screenHeight) {
		this.screenHeight = screenHeight;
		particles = new Explosion.Particle[0];
		sleep = 0;
		activity = null;
	}
	
	/**
	 * Set the reference to the MainActivity.
	 */
	public void setActivity(MainActivity activity) {
		this.activity = activity;
	}
	
	/**
	 * Draw the Explosion
	 */
	public boolean draw(Canvas canvas, long currentTime) {
		boolean isAlive = false;
		for (Explosion.Particle particle : particles) {
			if (particle.isAlive()) {
				// Ensure that the Explosion should still be alive
				isAlive = particle.draw(canvas, currentTime) || isAlive;
			}
		}
		return isAlive;
	}
	
	/**
	 * Move the Particles of the explosion by the specified gravity
	 */
	public void move(long currentTime, double gravityX, double gravityY) {
		for(Explosion.Particle particle : particles) {
			particle.move(currentTime, gravityX, gravityY);
		}
	}
	
	/**
	 * Bring the Explosion to life.
	 */
	public void makeAlive(long startTime) {
		state = ALIVE;
		for (Explosion.Particle particle : particles) {
			// The particles will die, each at a random time
			particle.makeAlive(startTime + sleep, random.nextInt(6000) + 1000);
		}
		activity.soundThread.play(MainActivity.SOUND_EXPLOSION);
	}
	
	/**
	 * Report whether the Explosion is alive of dead
	 */
	public boolean isAlive() {
		return state;
	}
	
	/**
	 * Particle Subclass
	 * Handles the moving, drawing, and state tracking of individual Particles
	 */
	static public class Particle {
		// Constants
		static final public boolean ALIVE = true;
		static final public boolean DEAD = false;

		// State variables
		public double currentX;
		public double currentY;
		public double previousX;
		public double previousY;
		public double velocityX;
		public double velocityY;
		
		private int screenHeight;
		private boolean state;
		private long previousUpdate;
		private long startTime;
		private long endTime;
		
		Ember ember = new Ember(); // the Ember that represents this particle

		/**
		 * Particle Constructor
		 */
		public Particle(int x, int y, int emberColor, int screenHeight) {
			previousX = currentX = (double) x;
			previousY = currentY = (double) y;
			velocityX = 0;
			velocityY = 0;
			ember.setEmberColor(emberColor);
			
			this.screenHeight = screenHeight;
			state = DEAD;
		}
		
		/**
		 * Draw the ember.
		 */
		public boolean draw(Canvas canvas, long currentTime) {
			if (currentTime >= startTime) {
				ember.setPosition(new Ember.Point((int) currentX, screenHeight
						- (int) currentY));
				ember.draw(canvas);
				if (currentTime >= endTime) {
					state = DEAD;
				}
			}
			return state;
		}
		
		/**
		 * Move the ember.
		 */
		public void move(long currentTime, double gravityX, double gravityY) {
			// Only move the particle if it is done sleeping
			if (currentTime > startTime) {
				// Only go as far as the time limit
				if (currentTime > endTime) {
					currentTime = endTime;
				}
				
				// Only allow movement since the startTime
				if (previousUpdate < startTime) {
					previousUpdate = startTime;
				}
	
				// Store the previous values
				previousX = currentX;
				previousY = currentY;
				
				// Adjust the velocity
				velocityX += (gravityX * ((double)(currentTime - previousUpdate) / 1000));
				velocityY += (gravityY * ((double)(currentTime - previousUpdate) / 1000));
				
				// Move the particle
				currentX += velocityX;
				currentY += velocityY;
			}
			previousUpdate = currentTime;
		}

		/**
		 * Bring the Particle to life.
		 */
		public void makeAlive(long startTime, long lifeLength) {
			state = ALIVE;
			previousUpdate = startTime;
			this.startTime = startTime;
			endTime = this.startTime + lifeLength;
		}
		
		/**
		 * Return the state of the Particle, alive or dead
		 */
		public boolean isAlive() {
			return state;
		}
	}
}
