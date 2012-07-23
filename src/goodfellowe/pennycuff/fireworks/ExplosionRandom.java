/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionRandom Class
 * Creates a standard, random explosion
 */
public class ExplosionRandom extends Explosion {

	/**
	 * Constructor
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
