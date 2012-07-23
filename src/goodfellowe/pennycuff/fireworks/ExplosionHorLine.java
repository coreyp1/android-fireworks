/**
 * by Corey Pennycuff and Rob Goodfellowe
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

/**
 * ExplosionHorLine class
 * Creates an explosion whose Particles are initially in a horizontal area
 */
public class ExplosionHorLine extends Explosion {

	/**
	 * Constructor
	 */
	public ExplosionHorLine(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		
		Random random = new Random();
		numParticles = 75;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = .00000001;
		double radians;
		double angle;
		double scaleFactor;
		double differenceMagnitude = 30;
		int color = random.nextInt(Ember.LIGHTS_TOTAL);
				
		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle <= 45){
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			else if (angle > 45 && angle <= 90){
				scaleFactor = Math.pow(1+Math.sin(radians), differenceMagnitude);
				radius = 0;	
			}
			else if (angle > 90 && angle <= 135){
				scaleFactor = Math.pow(1+Math.sin(radians), differenceMagnitude);
				radius = 0;	
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
				radius = 0;	
			}
			else if (angle > 270 && angle <= 315){
				scaleFactor = Math.pow(1+(-Math.sin(radians)), differenceMagnitude);
				radius = 0;	
			}
			else {
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = scaleFactor * maxRadius;	
			}
			
			particles[i] = new Explosion.Particle(x,  y, color, screenHeight);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
		}
	}
}
