package goodfellowe.pennycuff.fireworks;

import java.util.Random;

public class ExplosionStar extends Explosion {

	public ExplosionStar(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		

		Random random = new Random();
		numParticles = 100;
		particles = new Explosion.Particle[numParticles];
		int radius;
		double maxRadius = 1;
		double radians;
		double angle;
		double velX;
		double velY;
		double scaleFactor;
				
		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle < 360){
				scaleFactor = 1/(angle/360);
				radius = (int) scaleFactor * (int) maxRadius;
				
			}
			else {
				radius = 10;
			}
			
			
			//radians = random.nextDouble() * 2 * Math.PI;
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].setGravity(0, -.5);
			//particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			//particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		
			}
		
	}

}
