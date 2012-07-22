/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * @author Corey
 * 
 */
public class ExplosionCircleOutline extends Explosion {

	/**
	 * @param x
	 * @param y
	 * @param screenHeight
	 */
	public ExplosionCircleOutline(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		sleep = 1000;
		int numParticles = 80;
		Random random = new Random();
		int color1 = random.nextInt(Ember.LIGHTS_TOTAL);
		int color2 = random.nextInt(Ember.LIGHTS_TOTAL);
		double radius1 = (random.nextDouble() * 50) + 100;
		double radius2 = (random.nextDouble() * 50) + 50;
		double radians;
		particles = new Explosion.Particle[numParticles];
		int particleX, particleY;
		for (int i = 0; i < 40; i++) {
			radians = 360 / 40 * i;
			particleX = (int)(x + (Math.cos(radians) * radius1));
			particleY = (int)(y + (Math.sin(radians) * radius1));
			particles[i] = new Explosion.Particle(particleX, particleY, color1, screenHeight);
			particles[i].velocityX = Math.cos(radians) * 3 * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * 3 * random.nextDouble();
		}
		for (int i = 0; i < 40; i++) {
			radians = 360 / 40 * i;
			particleX = (int)(x + (Math.cos(radians) * radius2));
			particleY = (int)(y + (Math.sin(radians) * radius2));
			particles[i + 40] = new Explosion.Particle(particleX, particleY, color2, screenHeight);
			particles[i + 40].velocityX = Math.cos(radians) * 3 * random.nextDouble();
			particles[i + 40].velocityY = Math.sin(radians) * 3 * random.nextDouble();
		}
	}
}
