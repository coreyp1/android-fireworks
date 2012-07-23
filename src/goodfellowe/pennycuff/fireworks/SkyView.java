/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 * 
 */
public class SkyView extends SurfaceView implements SurfaceHolder.Callback {
	// Constants
	final public int MODE_ONTOUCH = 0;
	final public int MODE_CONTINUOUS = 1;
	final private long ROCKET_ADD_INTERVAL = 3000;

	private FireworksThread fireworksThread; // controls the game loop
	private int screenWidth; // width of the screen
	private int screenHeight; // height of the screen

	private long lastDim; // Track the time that the screen was last dimmed
	private long lastRocketAdd; // Track the last attempt to add a rocket
	private double gravityX = 0;
	private double gravityY = -2;
	private int state;
	private Set<Rocket> rockets = new HashSet<Rocket>();
	private Set<Rocket> rocketsToBeRemoved = new HashSet<Rocket>();
	private GestureDetector gestureDetector; // listens for double taps
	private MainActivity activity;
	
	private Paint dimmingPaint; // Paint used to clear the drawing area
	private Random random = new Random();

	/**
	 * @param context
	 */
	public SkyView(Context context) {
		super(context);
		setup();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SkyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SkyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	/**
	 * Setup the View
	 */
	private void setup() {
		activity = null;
		// Register SurfaceHolder.Callback listener
		getHolder().addCallback(this);

		// Set the paint for dimming
		dimmingPaint = new Paint(); // Paint for drawing the target
		dimmingPaint.setColor(Color.parseColor("#05000000"));

		// Set the state
		state = MODE_CONTINUOUS;

		// Initialize the GestureDetector
		gestureDetector = new GestureDetector(this.getContext(),
				gestureListener);
	}
	public void setActivity(MainActivity activity) {
		this.activity = activity;
	}

	/**
	 * Called by surfaceChanged when the size of the SurfaceView changes, such
	 * as when it's first added to the View hierarchy
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		screenWidth = w; // store the width
		screenHeight = h; // store the height
	}

	// called when surface changes size
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	} // end method surfaceChanged

	// called when surface is first created
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		fireworksThread = new FireworksThread(holder);
		fireworksThread.setRunning(true);
		fireworksThread.start(); // start the game loop thread
	}

	// called when the surface is destroyed
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// ensure that thread terminates properly
		boolean retry = true;
		fireworksThread.setRunning(false);

		while (retry) {
			try {
				fireworksThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	// Update the Gravity
	public void updateGravity(double newGravityX, double newGravityY) {
		gravityX = newGravityX;
		gravityY = newGravityY;
	}

	// draws the game to the given Canvas
	public void dimCanvas(Canvas canvas, long currentTime) {
		// clear the background
		if (currentTime - lastDim > 100) {
			canvas.drawARGB(10, 0, 0, 0);
			lastDim += 100;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// call the GestureDetector's onTouchEvent method
		return gestureDetector.onTouchEvent(e);
	}

	// Listens for touch events sent to the GestureDetector
	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Make a new Rocket
			Rocket rocket = new Rocket(activity);
			int y1 = screenHeight - (int) e.getY();
			int y2 = y1 + random.nextInt(screenHeight / 8) + 1;
			int x1 = (int) e.getX();
			while (!rocket.defineCriticalPoints(random.nextInt(100), y2, x1,
					y1, 950 + random.nextInt(1000), screenWidth, screenHeight))
				; // Repeat until suitable values are generated
			rocket.makeAlive(System.currentTimeMillis());

			// Add to the collection
			synchronized (rockets) {
				rockets.add(rocket);
			}

			// Event was completed
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	};

	// Thread subclass to control the fireworks loop
	private class FireworksThread extends Thread {
		private SurfaceHolder surfaceHolder; // for manipulating canvas
		private boolean threadIsRunning = true; // running by default
	
		// Initializes the surface holder
		public FireworksThread(SurfaceHolder holder) {
			surfaceHolder = holder;
			setName("FireworksThread");
		}

		// Changes running state
		public void setRunning(boolean running) {
			threadIsRunning = running;
		}

		// Controls the particle loop
		@Override
		public void run() {
			Canvas canvas = null; // Used for Double Buffer of SurfaceView

			// Initialize a temporary Bitmap and Canvas.
			// All drawing will be done on the temporary Canvas and then
			// copied to the SurfaceView Canvas. This allows us to draw on the
			// previously shown image without flickering.
			Bitmap tempBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
					Bitmap.Config.ARGB_8888);
			Canvas tempCanvas = new Canvas(tempBitmap);

			// Initialize the time variable
			long previousFrameTime = System.currentTimeMillis();
			lastRocketAdd = previousFrameTime - ROCKET_ADD_INTERVAL;

			Paint color = new Paint();
			color.setARGB(255, 255, 255, 255);

			// Create Stars
			final int STAR_COUNT = 40;
			int[] starX = new int[STAR_COUNT], starY = new int[STAR_COUNT];
			for (int i = 0; i < STAR_COUNT; i++) {
				starX[i] = random.nextInt(screenWidth);
				starY[i] = random.nextInt(screenHeight);
			}
			int starUpdate;

			while (threadIsRunning) {
				long currentTime = System.currentTimeMillis();

				// First, dim the canvas, if appropriate
				dimCanvas(tempCanvas, currentTime);

				// Randomly make a star disappear and reappear somewhere else
				if (random.nextInt(20) == 0) {
					starUpdate = random.nextInt(STAR_COUNT);
					starX[starUpdate] = random.nextInt(screenWidth);
					starY[starUpdate] = random.nextInt(screenHeight);
				}
				// Draw the stars
				for (int i = 0; i < STAR_COUNT; i++) {
					tempCanvas.drawPoint(starX[i], starY[i], color);
				}

				// If we are in MODE_CONTINUOUS, add a rocket, if appropriate
				if (state == MODE_CONTINUOUS) {
					// Only add a rocket ever so often
					if (lastRocketAdd + ROCKET_ADD_INTERVAL < currentTime) {
						lastRocketAdd = currentTime;
						Rocket rocket = new Rocket(activity);
						int y1 = (screenHeight / 4)
								+ random.nextInt(screenHeight / 2);
						int y2 = y1 + random.nextInt(screenHeight / 4) + 1;
						int x1 = (screenWidth / 4)
								+ random.nextInt(screenWidth / 2);
						while (!rocket.defineCriticalPoints(
								random.nextInt(100), y2, x1, y1,
								950 + random.nextInt(1000), screenWidth,
								screenHeight))
							;
						rocket.makeAlive(currentTime);
						// Add to the collection
						synchronized (rockets) {
							rockets.add(rocket);
						}
					}
				}

				// Protect against simultaneous access to rockets
				synchronized (rockets) {
					// Draw all current rockets
					for (Rocket rocket : rockets) {
						if (!rocket.isAlive()) {
							// The rocket cannot be removed here, queue it's
							// removal
							rocketsToBeRemoved.add(rocket);
						} else {
							rocket.draw(tempCanvas, currentTime, gravityX,
									gravityY);
						}
					}
					// Cleanup. Remove dead rockets
					for (Rocket rocket : rocketsToBeRemoved) {
						rockets.remove(rocket);
					}
					rocketsToBeRemoved.clear();
				}

				try {
					canvas = surfaceHolder.lockCanvas(null);
					// Lock the surfaceHolder for updating with bitmap
					synchronized (surfaceHolder) {
						if (canvas != null) {
							canvas.drawBitmap(tempBitmap, 0, 0, null);
						}
					}
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				previousFrameTime = currentTime; // update previous time
			}
		}
	}
}
