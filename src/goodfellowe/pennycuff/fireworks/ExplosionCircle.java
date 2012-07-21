package goodfellowe.pennycuff.fireworks;

import java.util.Random;

public class ExplosionCircle extends Explosion {

	public ExplosionCircle (int x, int y, int screenHeight){
		super(x, y, screenHeight);
		
		Random random = new Random();
		numParticles = 100;
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
