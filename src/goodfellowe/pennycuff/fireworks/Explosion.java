/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Collection;
import java.util.Random;

import android.graphics.Canvas;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 */
public class Explosion {
	protected Explosion.Particle[] particles;
	protected int numParticles;
	protected int screenHeight;
	private Random random = new Random();

	// Constants
	static final public boolean ALIVE = true;
	static final public boolean DEAD = false;

	// State Variables
	private boolean state;

	/**
	 * Explosion Constructor
	 */
	public Explosion(int x, int y, int screenHeight) {
		this.screenHeight = screenHeight;
		Random random = new Random();
		numParticles = 500;
		particles = new Explosion.Particle[numParticles];
		int radius = 5;
		double radians;
		int count;
		for (int i = 0; i < numParticles; i++) {
			
			radians = random.nextDouble() * 2 * Math.PI;
			particles[i] = new Explosion.Particle(x,  y, random.nextInt(Ember.LIGHTS_TOTAL), screenHeight);
			particles[i].setGravity(0, 0);
			particles[i].velocityX = Math.cos(radians) * radius * random.nextDouble();
			particles[i].velocityY = Math.sin(radians) * radius * random.nextDouble();
			
		}
	}
	
	public boolean draw(Canvas canvas, long currentTime) {
		boolean isAlive = false;
		for (Explosion.Particle particle : particles) {
			if (particle.isAlive()) {
				isAlive = particle.draw(canvas, currentTime) || isAlive;
			}
		}
		return isAlive;
	}
	
	public void move(long currentTime) {
		for(Explosion.Particle particle : particles) {
			particle.move(currentTime);
		}
	}
	
	public void makeAlive(long startTime) {
		state = ALIVE;
		for (Explosion.Particle particle : particles) {
			particle.makeAlive(startTime, random.nextInt(6000) + 1000);
		}
	}
	
	public boolean isAlive() {
		return state;
	}
	
	static public class Particle {
		// Constants
		static final public boolean ALIVE = true;
		static final public boolean DEAD = false;

		public double currentX;
		public double currentY;
		public double previousX;
		public double previousY;
		public double gravityX;
		public double gravityY;
		public double velocityX;
		public double velocityY;
		
		private int screenHeight;
		private boolean state;
		private long previousUpdate;
		private long endTime;
		
		int count = 0;
		
		Ember ember = new Ember();

		/**
		 * Particle Constructor
		 */
		
		public Particle(int x, int y, int emberColor, int screenHeight) {
			previousX = currentX = (float) x;
			previousY = currentY = (float) y;
			velocityX = 0;
			velocityY = 0;
			ember.setEmberColor(emberColor);
			
			this.screenHeight = screenHeight;
		}
		
		/*
		public Particle(int x, int y, int emberColor, int screenHeight) {
						
			
			previousX = currentX = (float) x;
			previousY = currentY = (float) y;
			
			//ember.setEmberColor(emberColor);
			
			int numOfParticles = 500;
			double angle = (Math.PI * 2) / count;
			for(int i = 0; i < numOfParticles; i++) {
				double randomVelocity = 4 + Math.random() * 4;
				velocityX = randomVelocity;
				randomVelocity = 4 + Math.random() * 4;
				velocityY = randomVelocity;
				double particleAngle = count * angle;
				//Fireworks.createParticle(firework.pos, null,{
				x = (int) (Math.cos(particleAngle) * velocityX); 
				y = (int) (Math.sin(particleAngle) * velocityY);
				//}
				ember.setEmberColor(emberColor);
			}
			
			
			this.screenHeight = screenHeight;
		}
		*/
		
		
		/*
		public Particle(int x, int y, int emberColor, int screenHeight) {
			
			previousX = currentX = (float) x;
			previousY = currentY = (float) y;
			velocityX = 0;
			velocityY = 0;
			ember.setEmberColor(emberColor);
			
			this.screenHeight = screenHeight;
			//star: function(firework) {    
			// set up how many points the firework    
			// should have as well as the velocity    
			// of the exploded particles etc    
			float points          = 6 + Math.round(Math.random() * 15);    
			float jump            = 3 + Math.round(Math.random() * 7);    
			float subdivisions    = 10;    
			float radius          = 80;    
			float randomVelocity  = (float) -(Math.random() * 3 - 6);   
			float start           = 0;    
			float end             = 0;    
			float circle          = (float) Math.PI * 2;    
			float adjustment      = (float) Math.random() * circle;    
			
			// work out the start, end      
			// and change values      
			while(start != end)
			start = end;      
			end = (end + jump) % points;      
			float sAngle = (start / points) * circle - adjustment;      
			float eAngle = ((start + jump) / points) * circle - adjustment;      
			double startPosX = x + Math.cos(sAngle) * radius;        
			double startPosY = y + Math.sin(sAngle) * radius;      
			      
			double endPosX = x + Math.cos(eAngle) * radius;        
			double endPosY  = y + Math.sin(eAngle) * radius;      
			float diffPosX = (int) endPosX - (int) startPosX;        
			float diffPosY = (int) endPosY - (int) startPosY;        
			float diffPosA = eAngle - sAngle;      
				      
			// now linearly interpolate across      
			// the subdivisions to get to a final      
			// set of particles      
			for(float s = 0; s < subdivisions; s++) {        
				float sub = s / subdivisions;        
				float subAngleX = sAngle + (sub * diffPosX);        
				float subAngleY = sAngle + (sub * diffPosY);        
				// createParticle          
					            
						x = (int) startPosX + (int) (sub * diffPosX);            
						y = (int) startPosX + (int) (sub * diffPosY);          
					         
					//null,          
					           
						//x: Math.cos(subAngleX) * randomVelocity,            
						//y: Math.sin(subAngleY) * randomVelocity               
			}    
				// loop until we're back at the start    
		 

		
			
			//previousX = currentX = (float) x;
			//previousY = currentY = (float) y;
			velocityX = 0;
			velocityY = 0;
			ember.setEmberColor(emberColor);
			
			this.screenHeight = screenHeight;
		}
		*/
		
		
		/**
		 * Set the Gravity
		 */
		public void setGravity(double newGravityX, double newGravityY) {
			gravityX = newGravityX;
			gravityY = newGravityY;
		}
		
		/**
		 * Draw the ember
		 */
		public boolean draw(Canvas canvas, long currentTime) {
			ember.setPosition(new Ember.Point((int)currentX, screenHeight - (int)currentY));
			ember.draw(canvas);
			if (currentTime >= endTime) {
				state = DEAD;
			}
			return state;
		}
		
		/**
		 * Move the ember
		 */
		public void move(long currentTime) {
			// Only go as far as the time limit
			if (currentTime > endTime) {
				currentTime = endTime;
			}

			// Store the previous values
			previousX = currentX;
			previousY = currentY;
			
			// Adjust the velocity
			velocityX += (gravityX * ((double)(currentTime - previousUpdate) / 1000));
			velocityY += (gravityY * ((double)(currentTime - previousUpdate) / 1000));
			
			// Move the particle
			currentX += velocityX;
			currentY += velocityY;
			
			previousUpdate = currentTime;
		}

		public void makeAlive(long startTime, long lifeLength) {
			state = ALIVE;
			previousUpdate = startTime;
			endTime = startTime + lifeLength;
		}
		
		public boolean isAlive() {
			return state;
		}
	}
}
