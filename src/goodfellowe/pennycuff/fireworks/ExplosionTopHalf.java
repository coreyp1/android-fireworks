package goodfellowe.pennycuff.fireworks;

import java.util.Random;

public class ExplosionTopHalf extends Explosion {

	public ExplosionTopHalf(int x, int y, int screenHeight) {
		super(x, y, screenHeight);


		Random random = new Random();
		numParticles = 100;
		particles = new Explosion.Particle[numParticles];
		int radius;
		double maxRadius = 10;
		double radians;
		double angle;
		double velX;
		double velY;
		double scaleFactor;
				
		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle > 180){
				radius = 1;
				
				
			}
			else {
				radius = 10;
			}
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].setGravity(0, -.5);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		
			}
	}

}
