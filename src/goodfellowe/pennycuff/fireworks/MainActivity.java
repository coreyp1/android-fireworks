package goodfellowe.pennycuff.fireworks;

import java.util.HashMap;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements SensorEventListener {
	SkyView skyView = null;
	SensorManager sensorManager;
	boolean useGravity;
	
	public static final int SOUND_EXPLOSION = 1;
	public static final int SOUND_ROCKET = 2;
		
	static SoundPool soundPool;
    static HashMap<Integer, Integer> soundPoolMap;
    
    private void initSounds() {
    	soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
    	soundPoolMap = new HashMap<Integer, Integer>();
    	soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(this, R.raw.explosion, 1));
    	soundPoolMap.put(SOUND_ROCKET, soundPool.load(this, R.raw.rocket, 1));

    }
    public void playSound(int sound) {
    	AudioManager mgr = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
    	int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
    	soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        final MediaPlayer starsMp = MediaPlayer.create(this,  R.raw.stars_and_stripes_forever);
        final MediaPlayer spangledMp = MediaPlayer.create(this,  R.raw.the_star_spangled_banner);
        final MediaPlayer thundererMp = MediaPlayer.create(this,  R.raw.thunderer);
        final MediaPlayer washingtonMp = MediaPlayer.create(this,  R.raw.washington_post);
        
        initSounds();
        
        Button stripesPlayerButton = (Button) this.findViewById(R.id.stripes);
        stripesPlayerButton.setVisibility(0);
        //stripesPlayerButton.setBackgroundColor(Color.TRANSPARENT);        
        stripesPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (starsMp.isPlaying() == true) {
        			starsMp.pause();
        			starsMp.seekTo(0);
        		}
        		else if (starsMp.isPlaying() == false)
        		{
        			
        			starsMp.start();
        		}
        	}
        });
        Button spangledPlayerButton = (Button) this.findViewById(R.id.spangled);
        spangledPlayerButton.setVisibility(0);
        spangledPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (spangledMp.isPlaying() == true) {
        			spangledMp.pause();
        			spangledMp.seekTo(0);
        		}
        		else if (spangledMp.isPlaying() == false)
        		{
        			
        			spangledMp.start();
        		}
        	}
        });
        Button washingtonPlayerButton = (Button) this.findViewById(R.id.washington);
        washingtonPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (washingtonMp.isPlaying() == true) {
        			washingtonMp.pause();
        			washingtonMp.seekTo(0);
        		}
        		else if (washingtonMp.isPlaying() == false)
        		{
        			
        			washingtonMp.start();
        		}
        	}
        });
        Button thundererPlayerButton = (Button) this.findViewById(R.id.thunderer);
        thundererPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (thundererMp.isPlaying() == true) {
        			thundererMp.pause();
        			thundererMp.seekTo(0);
        		}
        		else if (thundererMp.isPlaying() == false)
        		{
        			
        			thundererMp.start();
        		}
        	}
        });
        
        skyView = (SkyView) findViewById(R.id.skyView);
        skyView.setActivity(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        useGravity = true;

        // Allow volume keys to set sound volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, 
        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add("Painting");
        		//item.setIcon(R.drawable.ic_launcher);
        		item = menu.add("Photos");
        		//item.setIcon(R.drawable.ic_action_search);
    	//getMenuInflater().inflate(R.menu.main, menu);
        		SubMenu subScience = menu.addSubMenu(R.string.hello_world);
        		subScience.setIcon(R.drawable.ic_launcher);
        		MenuInflater inflater = new MenuInflater(this);
        		inflater.inflate(R.menu.main, subScience);
        return true;
    }

    public void onSensorChanged(SensorEvent event) {
    	if (useGravity && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && skyView != null) {
    		synchronized(this) {
                //Log.d("FIREWORKS", "x: " + (int)event.values[0] + ", y: " + (int)event.values[1] + ", z: " + (int)event.values[2]);
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
    
    // Must be included for Interface
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    
}
