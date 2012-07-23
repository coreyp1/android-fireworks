/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionStar Class
 * Creates a star shaped explosion
 * with a randomized number of sides
 * and a randomized angle used for the
 * rotation angle
 */
public class ExplosionStar extends Explosion {

	/**
	 * Constructor
	 */
	public ExplosionStar(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		Random random = new Random();
		numParticles = 225;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = 2.5;
		double radians;
		double angle;
		double velX;
		double velY;
		double scaleFactor;
		double differenceMagnitude = 23;
		double numOfPoints = random.nextInt(4) + 4;
		double numOfPointDegrees = 360 / numOfPoints;
	
		for (int i = 0; i < numParticles; i++) {

				angle = i * numOfPointDegrees;
	
				angle = angle / numOfPointDegrees;
				angle = Math.round(angle);
				if (numOfPoints == 5) {
					angle = angle * numOfPointDegrees + 18;
				}
				angle = angle * numOfPointDegrees + ((1 / (random.nextInt(3)+1)) * numOfPointDegrees);

				radians = (angle * Math.PI) / 180;
				radius = maxRadius;
	
				particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
				particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
				particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();

			
		}
		
	}

}
