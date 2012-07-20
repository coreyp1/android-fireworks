/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Collection;
import java.util.Random;

import android.graphics.Canvas;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 */
public class Explosion {
	protected Explosion.Particle[] particles;
	protected int numParticles;
	private int screenHeight;
	private Random random = new Random();

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
		Random random = new Random();
		numParticles = 500;
		particles = new Explosion.Particle[numParticles];
		int radius = 5;
		double radians;
		for (int i = 0; i < numParticles; i++) {
			radians = random.nextDouble() * 2 * Math.PI;
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].setGravity(0, -2);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}
	
	public boolean draw(Canvas canvas, long currentTime) {
		boolean isAlive = false;
		for (Explosion.Particle particle : particles) {
			if (particle.isAlive()) {
				isAlive = particle.draw(canvas, currentTime) || isAlive;
			}
		}
		return isAlive;
	}
	
	public void move(long currentTime) {
		for(Explosion.Particle particle : particles) {
			particle.move(currentTime);
		}
	}
	
	public void makeAlive(long startTime) {
		state = ALIVE;
		for (Explosion.Particle particle : particles) {
			particle.makeAlive(startTime, random.nextInt(6000) + 1000);
		}
	}
	
	public boolean isAlive() {
		return state;
	}
	
	static public class Particle {
		// Constants
		static final public boolean ALIVE = true;
		static final public boolean DEAD = false;

		public double currentX;
		public double currentY;
		public double previousX;
		public double previousY;
		public double gravityX;
		public double gravityY;
		public double velocityX;
		public double velocityY;
		
		private int screenHeight;
		private boolean state;
		private long previousUpdate;
		private long endTime;
		
		int count = 0;
		
		Ember ember = new Ember();

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
		 * Set the Gravity
		 */
		public void setGravity(double newGravityX, double newGravityY) {
			gravityX = newGravityX;
			gravityY = newGravityY;
		}
		
		/**
		 * Draw the ember
		 */
		public boolean draw(Canvas canvas, long currentTime) {
			ember.setPosition(new Ember.Point((int)currentX, screenHeight - (int)currentY));
			ember.draw(canvas);
			if (currentTime >= endTime) {
				state = DEAD;
			}
			return state;
		}
		
		/**
		 * Move the ember
		 */
		public void move(long currentTime) {
			// Only go as far as the time limit
			if (currentTime > endTime) {
				currentTime = endTime;
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
			
			previousUpdate = currentTime;
		}

		public void makeAlive(long startTime, long lifeLength) {
			state = ALIVE;
			previousUpdate = startTime;
			endTime = startTime + lifeLength;
		}
		
		public boolean isAlive() {
			return state;
		}
	}
}
