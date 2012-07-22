/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 *
 */
public class Rocket {
	// Constants
	static final public boolean ALIVE = true;
	static final public boolean DEAD = false;
	static final public int GRAVITY = 0;
	
	// State variables
	private boolean state;
	private int stage;
	private int lastX, lastY;
	private int screenWidth, screenHeight;
	
	// Mathematical variables for computing position
	private float y0, x2, x2a, y2, x1, y1; // position variables
	private long started; // Timestamp of when the animation started;
	private long lastUpdate; // Timestamp of last update in milliseconds
	private long duration; // Desirable length of the animation
	private long cutoff; // Timestamp of animation end
	private float coefA, coefB;
	private double quadA, quadB, quadC, quadTag;
	
	// Constants
	final private int STAGE_ROCKET = 0;
	final private int STAGE_EXPLOSION = 1;
	
	private Ember ember = new Ember();
	private Explosion explosion;

	/**
	 * 
	 */
	public Rocket() {
		state = DEAD;
	}
	
	public boolean defineCriticalPoints(int y0, int y2, int x1, int y1, long duration, int screenWidth, int screenHeight) {
		lastX = 0;
		lastY = (int) y0;
		
		this.y0 = y0;
		this.y2 = y2;
		this.x1 = x1;
		this.y1 = y1;
		
		this.duration = duration;
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		// Original equation of y = Ax^2 + Bx + C
		// Values of A and B are achieved algebraically using a starting
		// point (0, y0), ending at point (x1, y1), and having a maximum
		// at the unknown value (x2, y2).  5 variables are known, with x2 being
		// the only unknown.  Algebraic manipulation reveals the necessary
		// values and coefficients.
		quadA = (y1 - y0);
		quadB = (-2 * x1 * y2) + (2 * x1 * y0);
		quadC = (x1 * x1 * y2) - (x1 * y0);
		double first = quadB * quadB;
		double second = 4 * quadA * quadC;
		double third = first - second;
		quadTag = Math.sqrt(third);
		x2 = (float)((-quadB - quadTag) / (2 * quadA));
		coefA = (y1 - y0) / ((x1 * x1) - (2 * x2 * x1));
		coefB = -2 * coefA * x2;
		if (third < 0) {
			return false;
		}
		return true;
	}
	
	private double position(float x) {
		return (x * ((coefA * x) + coefB)) + y0;
	}
	
	/**
	 * Return the value that should be used for x at the specified time.
	 * @param time
	 * @return
	 */
	private float xval(long time) {
		/**
		 * Uses the cross-multiplication equivalence
		 * x1             ??
		 * -------- = ----------
		 * duration   time - started
		 */
		return (x1) * (time - started) / duration;
	}
	
	public void makeAlive(long lastUpdate) {
		state = ALIVE;
		this.lastUpdate = lastUpdate;
		cutoff = lastUpdate + duration;
		started = lastUpdate;
		Random random = new Random();
		ember.setEmberColor(random.nextInt(Ember.LIGHTS_TOTAL));
		stage = STAGE_ROCKET;
		
	}
	
	public boolean isAlive() {
		return state;
	}
	
	public void draw(Canvas canvas, long currentTime, double gravityX, double gravityY) {
		if (state == ALIVE) {
			if (stage == STAGE_ROCKET) {
				float tempX = xval(currentTime);
				if (tempX > x1) {
					tempX = x1;
				}
				int newX = (int) tempX;
				int newY = (int) position(tempX);
				for (int x = lastX; x <= newX; x++) {
					ember.setPosition(new Ember.Point(lastX, screenHeight - (int) position(lastX)));
					ember.draw(canvas);
					lastX = x;
				}
				lastX = newX;
				lastY = newY;
				if (currentTime > cutoff && stage == STAGE_ROCKET) {
					Random random = new Random();
					stage = STAGE_EXPLOSION;
					switch (random.nextInt(11)) {
					case 0:
						explosion = (Explosion) new ExplosionRandom(lastX, lastY, screenHeight);
						break;
					case 1:
						explosion = (Explosion) new ExplosionCircle(lastX, lastY, screenHeight);
						break;
					case 2:
						explosion = (Explosion) new ExplosionTopHalf(lastX, lastY, screenHeight);
						break;
					case 3:
						explosion = (Explosion) new ExplosionDiamond(lastX, lastY, screenHeight);
						break;
					case 4:
						explosion = (Explosion) new ExplosionTriangle(lastX, lastY, screenHeight);
						break;
					case 5:
						explosion = (Explosion) new ExplosionVertLine(lastX, lastY, screenHeight);
						break;
					case 6:
						explosion = (Explosion) new ExplosionHorLine(lastX, lastY, screenHeight);
						break;
					case 7:
						explosion = (Explosion) new ExplosionCircleOutline(lastX, lastY, screenHeight);
						break;
					case 8:
						explosion = (Explosion) new ExplosionStar(lastX, lastY, screenHeight);
						break;
					case 9:
						explosion = (Explosion) new ExplosionStar(lastX, lastY, screenHeight);
						break;
					case 10:
						explosion = (Explosion) new ExplosionStar(lastX, lastY, screenHeight);
						break;
					//case 11:
					//explosion = (Explosion) new ExplosionRing(lastX, lastY, screenHeight);
					//break;
					}
					explosion.makeAlive(currentTime);
				}
			}
			else if (stage == STAGE_EXPLOSION) {
				boolean isAlive = false;
				explosion.move(currentTime, gravityX, gravityY);
				isAlive = explosion.draw(canvas, currentTime);
				if (!isAlive) {
					state = DEAD;
				}
			}
			else {
				state = DEAD;
			}
		}
	}
	
	public int getCurrentX() {
		return lastX;
	}
	
	public int getCurrentY() {
		return lastY;
	}

}
