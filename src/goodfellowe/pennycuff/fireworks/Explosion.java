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

	/**
	 * Explosion Constructor
	 */
	public Explosion(int x, int y, int screenHeight) {
		this.screenHeight = screenHeight;
		Random random = new Random();
		numParticles = 20;
		particles = new Explosion.Particle[20];
		for (int i = 0; i < numParticles; i++) {
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].setGravity(0, (float) -.05);
			particles[i].velocityX = (random.nextFloat() * 3) - (float)1.5;
			particles[i].velocityY = (random.nextFloat() * 3) - (float)1.5;
		}
	}
	
	public void draw(Canvas canvas) {
		for(Explosion.Particle particle : particles) {
			particle.draw(canvas);
		}
	}
	
	public void move() {
		for(Explosion.Particle particle : particles) {
			particle.move();
		}
	}
	
	static public class Particle {
		public float currentX;
		public float currentY;
		public float previousX;
		public float previousY;
		public float gravityX;
		public float gravityY;
		public float velocityX;
		public float velocityY;
		
		private int screenHeight;
		
		int count = 0;
		
		Ember ember = new Ember();

		/**
		 * Particle Constructor
		 */
		public Particle(int x, int y, int emberColor, int screenHeight) {
			previousX = currentX = (float) x;
			previousY = currentY = (float) y;
			velocityX = 0;
			velocityY = 0;
			ember.setEmberColor(emberColor);
			
			this.screenHeight = screenHeight;
		}
		
		/**
		 * Set the Gravity
		 */
		public void setGravity(float newGravityX, float newGravityY) {
			gravityX = newGravityX;
			gravityY = newGravityY;
		}
		
		/**
		 * Draw the ember
		 */
		public void draw(Canvas canvas) {
			//if ((count % 100) == 0) {
				ember.setPosition(new Ember.Point((int)currentX, screenHeight - (int)currentY));
				ember.draw(canvas);
			//}
			//count++;
		}
		
		/**
		 * Move the ember
		 */
		public void move() {
			// Store the previous values
			previousX = currentX;
			previousY = currentY;
			
			// Adjust the velocity
			velocityX += gravityX;
			velocityY += gravityY;
			
			// Move the particle
			currentX += velocityX;
			currentY += velocityY;
		}
	}

}
