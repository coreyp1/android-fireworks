package goodfellowe.pennycuff.fireworks;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements SensorEventListener {
	SkyView skyView = null;
	SensorManager sensorManager;
	boolean useGravity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        skyView = (SkyView) findViewById(R.id.skyView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        useGravity = true;
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
        getMenuInflater().inflate(R.menu.main, menu);
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
