/**
 * by Corey Pennycuff and Rob Goodfellowe
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionTopHalf Class
 * Creates an explosion whose upper half is large and prominent
 */
public class ExplosionTopHalf extends Explosion {

	/**
	 * Constructor
	 */
	public ExplosionTopHalf(int x, int y, int screenHeight) {
		super(x, y, screenHeight);

		Random random = new Random();
		numParticles = 75;
		particles = new Explosion.Particle[numParticles];
		int radius;
		double radians;
		double angle;
		int color1 = random.nextInt(Ember.LIGHTS_TOTAL);
		int color2 = random.nextInt(Ember.LIGHTS_TOTAL);

		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle > 180){
				radius = 1;
			}
			else {
				radius = 10;
			}
			particles[i] = new Explosion.Particle(x,  y, radius == 1 ? color1 : color2, screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}
}
