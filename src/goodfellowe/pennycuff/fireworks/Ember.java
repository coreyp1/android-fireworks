/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.FloatMath;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 * 
 */
public class Ember {
	public Ember.Point current = new Ember.Point();
	public Ember.Point previous = new Ember.Point();

	// Constants
	static final public boolean ALIVE = true;
	static final public boolean DEAD = false;
	static final public int LIGHTS_ACROSS = 20;
	static final public int LIGHTS_TOTAL = LIGHTS_ACROSS * LIGHTS_ACROSS;
	static final public int LIGHT_SIZE = 8;
	static final public int LIGHT_MASTER_SIZE = LIGHT_SIZE * LIGHTS_ACROSS;
	static final public int LIGHT_SIZE_OFFSET = LIGHT_SIZE / 2;
	static final public float LIGHT_ALPHA = (float) .8;
	static public Bitmap lightBitmap = Bitmap.createBitmap(LIGHT_MASTER_SIZE,
			LIGHT_MASTER_SIZE, Bitmap.Config.ARGB_8888);
	static public Canvas lightCanvas = new Canvas(lightBitmap);
	static public boolean lightCreated = false;
	static private Random random = new Random();

	private Rect from = new Rect();
	private Rect to = new Rect();

	// State Variables
	private int emberColor;
	private Paint paint;

	/**
	 * Constructor
	 */
	public Ember() {
		// Create the Ember source bitmap, if it is not yet created
		if (!lightCreated) {
			createEmber();
		}
		// Set state variables
		setEmberColor(0);
		paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
	}
	
	/**
	 * Set the ember color to a value of 0 through 99
	 * @param newColor
	 * @return
	 */
	public int setEmberColor(int newColor) {
		if (newColor >= 0 && newColor < LIGHTS_TOTAL) {
			emberColor = newColor;
			int offsetX = LIGHT_SIZE * (newColor % LIGHTS_ACROSS);
			int offsetY = LIGHT_SIZE * (newColor / LIGHTS_ACROSS);
			from.set(offsetX, offsetY, offsetX + LIGHT_SIZE, offsetY + LIGHT_SIZE);
		}
		return emberColor;
	}

	/**
	 * Create the Ember Bitmap programmatically.
	 */
	private void createEmber() {
		synchronized (lightBitmap) {
			if (!lightCreated) {
				lightCreated = true;
				Paint paint = new Paint();
				int lightNumber;
				int innerX, innerY;
				float radius;
				float distance;
				int alpha;
				float[] hsv = {0, 1, LIGHT_ALPHA};

				// Loop through the image and create the lights pixel by pixel;
				radius = LIGHT_SIZE / 2;
				for (int i = 0; i < LIGHT_MASTER_SIZE; i++) { // row
					for (int j = 0; j < LIGHT_MASTER_SIZE; j++) { // column
						innerX = j % LIGHT_SIZE;
						innerY = i % LIGHT_SIZE;
						distance = FloatMath
								.sqrt(((radius - innerX) * (radius - innerX))
										+ ((radius - innerY) * (radius - innerY)));
						lightNumber = ((i / LIGHT_SIZE) * LIGHTS_ACROSS)
								+ (j / LIGHT_SIZE);
						if (distance > radius) {
							alpha = 0;
						} else {
							alpha = (int) (Math.cos((distance / radius) * (Math.PI / 2)) * 255);
						}
						hsv[0] = (float) (lightNumber * 359 / LIGHTS_TOTAL);
						paint.setColor(Color.HSVToColor(alpha, hsv));
						lightCanvas.drawPoint(i, j, paint);
					}
				}
			}
		}
	}

	/**
	 * Draw the Ember regardless of time.
	 * @param canvas
	 * @return
	 */
	public void draw(Canvas canvas) {
		paint.setARGB(random.nextInt(256), 255, 255, 255);
		int fromX = current.x - LIGHT_SIZE_OFFSET;
		int fromY = current.y - LIGHT_SIZE_OFFSET;
		to.set(fromX, fromY, fromX + LIGHT_SIZE, fromY + LIGHT_SIZE);
		canvas.drawBitmap(lightBitmap, from, to, paint);
	}
	
	/**
	 * Set the location of the center of the ember;
	 * @param p
	 */
	public void setPosition(Ember.Point p) {
		current.x = p.x;
		current.y = p.y;
	}

	/**
	 * Class Point For storing X and Y coordinates.
	 * 
	 */
	static public class Point {
		public int x;
		public int y;

		/**
		 * Constructor
		 */
		public Point() {
		}

		/**
		 * Constructor
		 */
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
