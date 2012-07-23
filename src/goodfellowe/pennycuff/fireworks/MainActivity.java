/**
 * Fireworks
 * by Corey Pennycuff and Rob Goodfellowe
 */
package goodfellowe.pennycuff.fireworks;

import java.util.HashMap;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity implements SensorEventListener {
	// Constants
	public static final int SOUND_EXPLOSION = 1;
	public static final int SOUND_ROCKET = 2;
	final public static int MODE_ONTOUCH = 0;
	final public static int MODE_CONTINUOUS = 1;
	final public static int MP_STARS = 0;
	final public static int MP_SPANGLED = 1;
	final public static int MP_WASHINGTON = 2;
	final public static int MP_THUNDERER = 3;

	// Sound-related variables
	private MediaPlayer[] player;
    public SoundThread soundThread;
    
    // Settings update page
    private View settingsView;
    
    // State variables
	boolean useGravity;
    
	// Element Variables
	SkyView skyView = null;
	SensorManager sensorManager;
	
    /**
     * Constructor
     *
     */
    public class SoundThread extends Thread {
    	private SoundPool soundPool;
        private HashMap<Integer, Integer> soundPoolMap;
        private int sound;
    	private boolean threadIsRunning;
    	MainActivity activity;

    	public SoundThread(MainActivity activity) {
    		this.activity = activity;
    		sound = 0;
    		threadIsRunning = false;
    	}
    	
		// Changes running state
		public void setRunning(boolean running) {
			threadIsRunning = running;
		}

		@Override
		public void run() {
			soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			soundPoolMap = new HashMap<Integer, Integer>();
			soundPoolMap.put(SOUND_EXPLOSION,
					soundPool.load(activity, R.raw.explosion, 1));
			soundPoolMap.put(SOUND_ROCKET,
					soundPool.load(activity, R.raw.rocket, 1));

			while (threadIsRunning) {
				if (sound != 0) {
					AudioManager mgr = (AudioManager) getBaseContext()
							.getSystemService(Context.AUDIO_SERVICE);
					int streamVolume = mgr
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					soundPool.play(soundPoolMap.get(sound), streamVolume,
							streamVolume, 1, 0, 1);
					sound = 0;
				}
			}
			soundPool.release();
		}
		
		public void play(int sound) {
			this.sound = sound;
		}
    }
    
    /**
     * Called when the activity is starting.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Sound must be initialized before the layout
     	soundThread = new SoundThread(this);
    	soundThread.setRunning(true);
    	soundThread.start();
        
    	// Set the layout
        setContentView(R.layout.main);
        
        // Load the media
        player = new MediaPlayer[4];
		player[MP_STARS] = MediaPlayer.create(this,	R.raw.stars_and_stripes_forever);
		player[MP_SPANGLED] = MediaPlayer.create(this, R.raw.the_star_spangled_banner);
		player[MP_THUNDERER] = MediaPlayer.create(this, R.raw.thunderer);
		player[MP_WASHINGTON] = MediaPlayer.create(this, R.raw.washington_post);
        
		// Set the onClick listeners for the MediaPlayer buttons
        Button stripesPlayerButton = (Button) this.findViewById(R.id.stripes);
        stripesPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		toggleMpState(player[MP_STARS]);
        	}
        });
        Button spangledPlayerButton = (Button) this.findViewById(R.id.spangled);
        spangledPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		toggleMpState(player[MP_WASHINGTON]);
        	}
        });
        Button washingtonPlayerButton = (Button) this.findViewById(R.id.washington);
        washingtonPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		toggleMpState(player[MP_WASHINGTON]);
        	}
        });
        Button thundererPlayerButton = (Button) this.findViewById(R.id.thunderer);
        thundererPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		toggleMpState(player[MP_THUNDERER]);
        	}
        });
        
        // Set variables for elements to be monitored
        skyView = (SkyView) findViewById(R.id.skyView);
        skyView.setActivity(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        useGravity = true;

        // Allow volume keys to set sound volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
    /**
     * Toggle the MediaPlayer's Playing State
     */
    private void toggleMpState(MediaPlayer mp) {
   		if (mp.isPlaying() == true) {
   			// Stop this particular media player
   			mp.pause();
   			mp.seekTo(0);
		}
		else if (mp.isPlaying() == false)
		{
			// Stop all other media players to avoid overlap
			for (MediaPlayer p : player) {
				if (p.isPlaying()) {
					p.pause();
					p.seekTo(0);
				}
			}
			// Start the desired media player
			mp.start();
		}
    }

    /**
     * Called when the application is restarted or unpaused.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, 
        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    /**
     * Called when the application is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    
    /**
     * Final cleanup before Activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        soundThread.setRunning(false);
        // Stop the music
        for (MediaPlayer mp : player) {
        	mp.stop();
        	mp.release();
        }
        super.onDestroy();
    }

    /**
     * Menu is being created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/**
	 * Called when the user selects an option from the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String string;
		WebView webView;
		// Switch the menu id of the user-selected option
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// The Settings menu has been requested
			break;
		case R.id.menu_credits:
			// The Credits page has been requested
			break;
		}
		return true;
	}

   /**
     * Responds to Sensor events, specifically TYPE_ACCELEROMETER.
     */
    public void onSensorChanged(SensorEvent event) {
    	if (useGravity && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && skyView != null) {
    		synchronized(this) {
    			// X, Y, and Z have a value in the range of -10, 10
    			double x = event.values[0];
    			double y = event.values[1];
    			double z = event.values[2];
    			double scaleup = 2;
    			double zScalingFactor = (10 - Math.abs(z)) / 10;
    			skyView.updateGravity(-x / 10 * zScalingFactor * scaleup, -y / 10 * zScalingFactor * scaleup);
    		}
    	}
    }
    
    /**
     *  Must be included for SensorEventListener Interface
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	// It is fine for this to be empty.
    }
}
