package goodfellowe.pennycuff.fireworks;

import java.util.Random;

public class ExplosionVertLine extends Explosion {

	public ExplosionVertLine(int x, int y, int screenHeight) {
		super(x, y, screenHeight);
		

		Random random = new Random();
		numParticles = 100;
		particles = new Explosion.Particle[numParticles];
		double radius;
		double maxRadius = .00000001;
		double radians;
		double angle;
		double velX;
		double velY;
		double scaleFactor;
		double differenceMagnitude = 30;
		
				
		for (int i = 0; i < numParticles; i++) {
			angle = random.nextDouble() * 360;
			radians = (angle * Math.PI) / 180;
			if (angle <= 45){
				scaleFactor = Math.pow(1+Math.cos(radians), differenceMagnitude);
				radius = 0;	
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
				radius = 0;	
			}
			else if (angle > 180 && angle <= 225){
				scaleFactor = Math.pow(1+(-Math.cos(radians)), differenceMagnitude);
				radius = 0;	
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
				radius = 0;	
			}
			
			
			
			//radians = random.nextDouble() * 2 * Math.PI;
			particles[i] = new Explosion.Particle(x,  y, 100, screenHeight);
			//particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			//particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
			}
		

	}

}
