/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 *
 */
public class ExplosionRandom extends Explosion {

	/**
	 * @param x
	 * @param y
	 * @param screenHeight
	 */
	public ExplosionRandom(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		Random random = new Random();
		numParticles = 60;
		particles = new Explosion.Particle[numParticles];
		int radius = 5;
		double radians;
		for (int i = 0; i < numParticles; i++) {
			radians = random.nextDouble() * 2 * Math.PI;
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}
}
