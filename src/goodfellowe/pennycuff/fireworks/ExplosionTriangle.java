/**
 * by Corey Pennycuff and Rob Goodfellowe
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionTriangle Class
 * Creates an explosion in the shape of an Isosolese triangle
 */
public class ExplosionTriangle extends Explosion {

	/**
	 * Constructor
	 */
	public ExplosionTriangle(int x, int y, int screenHeight) {
		super(x, y, screenHeight);

		Random random = new Random();
		numParticles = 75;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = .3;
		double radians;
		double angle;
		double scaleFactor;
		double differenceMagnitude = 4;

		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle <= 45){
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 45 && angle <= 90){
				scaleFactor = Math.pow(1+Math.sin(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 90 && angle <= 135){
				scaleFactor = Math.pow(1+Math.sin(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 135 && angle <= 180){
				scaleFactor = Math.pow(1+(-Math.cos(radians)), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else {
				radius = 0;
			}
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}
}
