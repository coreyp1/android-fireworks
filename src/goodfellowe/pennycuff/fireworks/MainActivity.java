/**
Corey Pennycuff and Rob Goodfellowe
PROG 3: 7.11 Fireworks Show
Utilize principles learned from Cannonball App
and create an animated fireworks show
 */
package goodfellowe.pennycuff.fireworks;

import java.util.HashMap;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

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
	private SharedPreferences savedPreferences; // user's preferences
	private boolean useGravity;
	private boolean playSounds;

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

		// Soundpool loads and plays sound effects for the rocket and the
		// explosion
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
					if (playSounds) {
						AudioManager mgr = (AudioManager) getBaseContext()
								.getSystemService(Context.AUDIO_SERVICE);
						int streamVolume = mgr
								.getStreamVolume(AudioManager.STREAM_MUSIC);
						soundPool.play(soundPoolMap.get(sound), streamVolume,
								streamVolume, 1, 0, 1);
					}
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

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set the layout
		setContentView(R.layout.main);

		// Load the media
		player = new MediaPlayer[4];
		player[MP_STARS] = MediaPlayer.create(this,
				R.raw.stars_and_stripes_forever);
		player[MP_SPANGLED] = MediaPlayer.create(this,
				R.raw.the_star_spangled_banner);
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
				toggleMpState(player[MP_SPANGLED]);
			}
		});
		Button washingtonPlayerButton = (Button) this
				.findViewById(R.id.washington);
		washingtonPlayerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleMpState(player[MP_WASHINGTON]);
			}
		});
		Button thundererPlayerButton = (Button) this
				.findViewById(R.id.thunderer);
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

		// get the SharedPreferences that contains the user's saved preferences
		savedPreferences = getSharedPreferences("FireworksPreferences",
				MODE_PRIVATE);

		// Load user settings
		useGravity = savedPreferences.getBoolean("useGravity", false);
		playSounds = savedPreferences.getBoolean("playSounds", true);
		skyView.frequency = savedPreferences.getInt("frequency", 30);
		skyView.fireworksGenerationMode = savedPreferences.getInt(
				"fireworksGenerationMode", MODE_ONTOUCH);

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
		} else if (mp.isPlaying() == false) {
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
		// Switch the menu id of the user-selected option
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// The Settings menu has been requested
			LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the Settings View
			settingsView = layoutInflater.inflate(R.layout.settings,
					null);
			// Set values of the elements
			((CheckBox) settingsView.findViewById(R.id.automaticFireworks)).setChecked(skyView.fireworksGenerationMode == MODE_CONTINUOUS);
			((CheckBox) settingsView.findViewById(R.id.playSounds)).setChecked(playSounds);
			((CheckBox) settingsView.findViewById(R.id.detectGravity)).setChecked(useGravity);
			((SeekBar) settingsView.findViewById(R.id.adjustFrequencySeekBar)).setProgress(skyView.frequency);
			
			// Create a new AlertDialog Builder
			new AlertDialog.Builder(this)
			// Title bar string
					.setTitle(R.string.menuSettings)
					// Add the Settings View
					.setView(settingsView)
					// Set the Save Button
					.setPositiveButton(R.string.menuSettingsSave,
							settingsSaveListener)
					// Set the Cancel Button
					.setNegativeButton(R.string.menuSettingsCancel, null)
					// Show the dialog
					.show();
			return true;

		case R.id.menu_credits:
			// The Credits page has been requested
			// Use WebView so that we can use HTML formatting
			String string = getString(R.string.menuCreditsText);
			WebView webView = new WebView(getBaseContext());
			webView.loadData(string, "text/html", "utf-8");
			webView.setBackgroundColor(Color.WHITE);
			webView.getSettings().setDefaultTextEncodingName("utf-8");

			// Create a new AlertDialog Builder
			new AlertDialog.Builder(this)
					// Title bar string
					.setTitle(R.string.menuCredits)
					.setView(webView) // Display text
					// Set positive button text
					.setPositiveButton(R.string.creditsOK, null)
					.create() // Convert from Builder to Dialog
					.show(); // Display the Dialog
			return true;
		}
		return true;
	}

	/**
	 * Responds to Settings Save button click.
	 */
	public DialogInterface.OnClickListener settingsSaveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, int var) {
			// Get the SharedPreferences Editor
			SharedPreferences.Editor editor = savedPreferences.edit();

			// Set generation mode to either MODE_CONTINUOUS or MODE_ONTOUCH
			CheckBox automaticFireworksCB = (CheckBox) settingsView
					.findViewById(R.id.automaticFireworks);
			if (automaticFireworksCB.isChecked()) {
				skyView.fireworksGenerationMode = MODE_CONTINUOUS;
			} else {
				skyView.fireworksGenerationMode = MODE_ONTOUCH;
			}
			editor.putInt("fireworksGenerationMode",
					skyView.fireworksGenerationMode);

			// Set the mode of whether or not to use gravity detection
			CheckBox detectGravityCB = (CheckBox) settingsView
					.findViewById(R.id.detectGravity);
			useGravity = detectGravityCB.isChecked();
			editor.putBoolean("useGravity", useGravity);

			// Set the average frequency that Fireworks occur in MODE_CONTINUOUS
			SeekBar adjustFrequencySB = (SeekBar) settingsView
					.findViewById(R.id.adjustFrequencySeekBar);
			skyView.frequency = adjustFrequencySB.getProgress();
			editor.putInt("frequency", skyView.frequency);
			
			// Set whether or not to play sounds when fireworks explode
			CheckBox playSoundsCB = (CheckBox) settingsView
					.findViewById(R.id.playSounds);
			playSounds = playSoundsCB.isChecked();
			editor.putBoolean("playSounds", playSounds);

			// Commit the SharedPreferences settings
			editor.commit();
		}
	};

	/**
	 * Responds to Sensor events, specifically TYPE_ACCELEROMETER.
	 */
	public void onSensorChanged(SensorEvent event) {
		if (useGravity && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER
				&& skyView != null) {
			synchronized (this) {
				// X, Y, and Z have a value in the range of -10, 10
				double x = event.values[0];
				double y = event.values[1];
				double z = event.values[2];
				double scaleup = 2;
				double zScalingFactor = (10 - Math.abs(z)) / 10;
				skyView.updateGravity(-x / 10 * zScalingFactor * scaleup, -y
						/ 10 * zScalingFactor * scaleup);
			}
		}
	}

	/**
	 * Must be included for SensorEventListener Interface
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// It is fine for this to be empty.
	}
}
