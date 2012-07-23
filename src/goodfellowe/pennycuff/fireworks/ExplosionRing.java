/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/*
 * Creates a ring shaped explosion
 */
public class ExplosionRing extends Explosion {

	public ExplosionRing(int x, int y, int screenHeight) {
		super(x, y, screenHeight);

		Random random = new Random();
		numParticles = 40;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = .0000005;
		double radians;
		double angle;
		double velX;
		double velY;
		double scaleFactor;
		double differenceMagnitude = 23;
				
		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			angle = i * 10;
			
			angle = angle / 90;
			angle = Math.round(angle);
			angle = angle * 90;

			radians = (angle * Math.PI) / 180;
			if (angle <= 360){
				radius = 10;
			}
			else {
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = 1;	
			}

			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}

}