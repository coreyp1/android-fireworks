/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionDiamond
 * Originally created an explosion in the shape of a diamon, but after some
 * system-wide changes to gravity, now appears like a four-petal flower shape.
 */
public class ExplosionDiamond extends Explosion {

	/**
	 * Constructor
	 */
	public ExplosionDiamond(int x, int y, int screenHeight) {
		super(x, y, screenHeight);

		Random random = new Random();
		numParticles = 75;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = .0000005;
		double radians;
		double angle;
		double scaleFactor;
		double differenceMagnitude = 23;
		int[] color = {random.nextInt(Ember.LIGHTS_TOTAL), random.nextInt(Ember.LIGHTS_TOTAL)};

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
			else if (angle > 180 && angle <= 225){
				scaleFactor = Math.pow(1+(-Math.cos(radians)), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 225 && angle <= 270){
				scaleFactor = Math.pow(1+(-Math.sin(radians)), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 270 && angle <= 315){
				scaleFactor = Math.pow(1+(-Math.sin(radians)), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else {
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}

			particles[i] = new Explosion.Particle(x,  y, color[i % 2], screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius  + ((random.nextDouble() - .5) / 4);
			particles[i].velocityY = Math.sin(radians) * radius  + ((random.nextDouble() - .5) / 4);
		}
	}
}
