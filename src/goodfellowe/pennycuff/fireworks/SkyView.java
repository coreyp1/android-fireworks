/**
 * 
 */
package goodfellowe.pennycuff.fireworks;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Corey Pennycuff and Rob Goodfellowe
 * 
 */
public class SkyView extends SurfaceView implements SurfaceHolder.Callback {
	private FireworksThread fireworksThread; // controls the game loop
	private int screenWidth; // width of the screen
	private int screenHeight; // height of the screen
	
	private long lastDim; // Track the time that the screen was last dimmed;

	// Paint variables
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
		// register SurfaceHolder.Callback listener
		getHolder().addCallback(this); 

		dimmingPaint = new Paint(); // Paint for drawing the target
		dimmingPaint.setColor(Color.parseColor("#05000000"));
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

	// draws the game to the given Canvas
	public void dimCanvas(Canvas canvas, long currentTime) {
		// clear the background
		if (currentTime - lastDim > 100) {
			canvas.drawARGB(10, 0, 0, 0);
			lastDim += 100;
		}

	}

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
			Canvas canvas = null; // Double Buffer of SurfaceView
			// Draw to a temporary bitmap and canvas
			Bitmap tempBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
					Bitmap.Config.ARGB_8888);
			Canvas tempCanvas = new Canvas(tempBitmap);

			// Initialize the time variable
			long previousFrameTime = System.currentTimeMillis();
			
			Paint color = new Paint();
			color.setARGB(255, 255, 255, 255);
			
			/*
			int lastx = 0;
			int lasty = 0;
			int x, y;
			int yaccel = -3;
			int yvelocity = 50;
			int direction = 1;
			*/
			final int STAR_COUNT = 40;
			int[] starX = new int[STAR_COUNT], starY = new int[STAR_COUNT];
			for (int i = 0; i < STAR_COUNT; i++) {
				starX[i] = random.nextInt(screenWidth);
				starY[i] = random.nextInt(screenHeight);
			}
			int starUpdate;

			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(3);
			paint.setARGB(255, 255, 0, 0);
			paint.setAntiAlias(true);
			
			//y = 0;
			
			Rocket rocket = new Rocket();
			
			//Explosion explosion = new Explosion(screenWidth / 2, screenHeight / 2, screenHeight);
			
			while (threadIsRunning) {
				// Do all drawing to the tempCanvas
				long currentTime = System.currentTimeMillis();
				//double elapsedTimeMS = currentTime - previousFrameTime;
				dimCanvas(tempCanvas, currentTime);

				// Draw "Stars"
				if (random.nextInt(2) == 0) {
					starUpdate = random.nextInt(STAR_COUNT);
					starX[starUpdate] = random.nextInt(screenWidth);
					starY[starUpdate] = random.nextInt(screenHeight);
				}
				for (int i = 0; i < STAR_COUNT; i++) {
					tempCanvas.drawPoint(starX[i], starY[i], color);
				}
				
				/*
				x = (lastx + (6 * direction));
				if (x > screenWidth || x < 0) {
					if (x < 0) {
						x = 0;
					}
					else {
						x = screenWidth;
					}
					direction = -direction;
				}
				y = lasty + yvelocity;
				yvelocity += yaccel;
				if (y < 0) {
					y = 0;
					yvelocity = 50;
				}
				
				//tempCanvas.drawLine(lastx, screenHeight - lasty, x, screenHeight - y, paint);
				lastx = x;
				lasty = y;
				*/
				
				// draw the particle
				if (!rocket.isAlive()) {
					int y1 = (screenHeight / 4) + random.nextInt(screenHeight / 2);
					int y2 = y1 + random.nextInt(screenHeight / 4) + 1;
					int x1 = (screenWidth / 4) + random.nextInt(screenWidth / 2);
					while (!rocket.defineCriticalPoints(random.nextInt(100), y2, x1, y1, 950 + random.nextInt(1000), screenWidth, screenHeight));
					rocket.makeAlive(previousFrameTime);
				}
				rocket.draw(tempCanvas, currentTime);
				
				try {
					canvas = surfaceHolder.lockCanvas(null);
					// Lock the surfaceHolder for updating with bitmap
					synchronized (surfaceHolder) {
						if (canvas != null) {
							canvas.drawBitmap(tempBitmap, 0, 0, null);
						}
					}
				}
				finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				previousFrameTime = currentTime; // update previous time
			}
		}
	}
}
