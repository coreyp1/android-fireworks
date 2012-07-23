/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * SkyView Class
 * Draws and animates fireworks
 */
public class SkyView extends SurfaceView implements SurfaceHolder.Callback {
	// Constants
	final private long ROCKET_ADD_INTERVAL = 1000;

	// Thread variables
	private FireworksThread fireworksThread; // Controls the display loop
	private long lastDim; // Track the time that the screen was last dimmed
	private Paint dimmingPaint; // Paint used to clear the drawing area
	private long lastRocketAdd; // Track the last attempt to add a rocket
	private double gravityX = 0; // Gravity of the X-axis
	private double gravityY = -2; // Gravity of the Y-axis

	// State variables
	private int screenWidth; // Width of the screen
	private int screenHeight; // Height of the screen
	private int fireworksGenerationMode; // Tracks how fireworks are generated
	private MainActivity activity; // Link to the parent activity
	private Random random = new Random(); // Used for all random values
	private int frequency = 20; // Average frequency of adding rockets
	
	// Rockets and Explosions
	private Set<Rocket> rockets = new HashSet<Rocket>(); // Stores all rockets
	private Set<Rocket> rocketsToBeRemoved = new HashSet<Rocket>(); // Dead rockets
	private GestureDetector gestureDetector; // Listens for double taps

	/**
	 * Constructor
	 * @param context
	 */
	public SkyView(Context context) {
		super(context);
		setup();
	}

	/**
	 * Constructor
	 * @param context
	 * @param attrs
	 */
	public SkyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	/**
	 * Constructor
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

		// Set the paint for dimming the drawing Canvas
		dimmingPaint = new Paint();
		dimmingPaint.setColor(Color.parseColor("#05000000"));

		// Set the state
		fireworksGenerationMode = MainActivity.MODE_CONTINUOUS;

		// Initialize the GestureDetector
		gestureDetector = new GestureDetector(this.getContext(),
				gestureListener);
	}
	
	/**
	 * Helper method to set a reference to the parent activity.
	 * @param activity
	 */
	public void setActivity(MainActivity activity) {
		this.activity = activity;
	}

	/**
	 * Called by surfaceChanged when the size of the SurfaceView changes, such
	 * as when it's first added to the View hierarchy.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		screenWidth = w; // store the width
		screenHeight = h; // store the height
	}

	/**
	 * Called when surface changes size.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Intentionally empty
	}

	/**
	 * Called when surface is first created.
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Start the fireworks loop thread
		fireworksThread = new FireworksThread(holder);
		fireworksThread.setRunning(true);
		fireworksThread.start();
	}

	/**
	 * Called when the surface is destroyed.
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Ensure that thread terminates properly
		boolean retry = true;
		fireworksThread.setRunning(false);

		while (retry) {
			try {
				fireworksThread.join();
				retry = false;
			}
			catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Update the Gravity.
	 */
	public void updateGravity(double newGravityX, double newGravityY) {
		gravityX = newGravityX;
		gravityY = newGravityY;
	}

	/**
	 * Dims the Canvas.
	 * Returns true if canvas was dimmed, false otherwise.
	 * @param canvas
	 * @param currentTime
	 */
	public boolean dimCanvas(Canvas canvas, long currentTime) {
		// Only dim the canvas 10 times per second
		if (currentTime - lastDim > 100) {
			canvas.drawARGB(10, 0, 0, 0);
			lastDim += 100;
			return true;
		}
		return false;
	}

	/**
	 * Respond to onTouchEvents
	 */
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// Call the GestureDetector's onTouchEvent method
		return gestureDetector.onTouchEvent(e);
	}

	/**
	 * Listens for touch events sent to the GestureDetector
	 */
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

		/**
		 * Necessary for the SimpleOnGestureListener to respond to Taps.
		 */
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	};

	/**
	 * Thread subclass to control the fireworks loop.
	 */
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

			// Paint variable used in drawing stars
			Paint color = new Paint();
			color.setARGB(255, 255, 255, 255);

			// Create Stars array
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
				if (dimCanvas(tempCanvas, currentTime)) {
					// If the Canvas was dimmed, then re-draw the stars
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
				}

				// If we are in MODE_CONTINUOUS, add a rocket, if appropriate
				if (fireworksGenerationMode == MainActivity.MODE_CONTINUOUS) {
					// Only add a rocket ever so often
					if (lastRocketAdd + ROCKET_ADD_INTERVAL < currentTime) {
						lastRocketAdd = currentTime;
						if (random.nextInt(100) <= frequency) {
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
								; // Empty loop to generate appropriate values;
							rocket.makeAlive(currentTime);
							// Add to the collection
							synchronized (rockets) {
								rockets.add(rocket);
							}
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
						rocket = null;
					}
					rocketsToBeRemoved.clear();
				}

				// Transfer the bitmap drawings to the Canvas
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
				previousFrameTime = currentTime; // Update previous time
			}
		}
	}
}
