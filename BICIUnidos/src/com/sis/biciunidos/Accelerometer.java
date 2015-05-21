package com.sis.biciunidos;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class Accelerometer extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float []gravity;
	private float []acceleration;
	private float accX, accY, accZ;
	private long last_update = 0, last_movement = 0;
	private float prevX = 0, prevY = 0, prevZ = 0, movement = 0;
	
	
	public Accelerometer()
	{
		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
	}
	
	@Override
	public void onSensorChanged(SensorEvent event){
		  synchronized (this) {
		        long current_time = event.timestamp;
		          
		        // In this example, alpha is calculated as t / (t + dT),
		          // where t is the low-pass filter's time-constant and
		          // dT is the event delivery rate.

		            gravity = new float[event.values.length];
		            acceleration = new float[event.values.length];
		        
		          final float alpha = (float) 0.8;

		          // Isolate the force of gravity with the low-pass filter.
		          gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		          gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		          gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		          // Remove the gravity contribution with the high-pass filter.
		          accX = acceleration[0] = event.values[0] - gravity[0];
		          accY = acceleration[1] = event.values[1] - gravity[1];
		          accZ = acceleration[2] = event.values[2] - gravity[2];
		        		         
		        if (prevX == 0 && prevY == 0 && prevZ == 0) {
		            last_update = current_time;
		            last_movement = current_time;
		            prevX = accX;
		            prevY = accY;
		            prevZ = accZ;
		        }
		 
		        long time_difference = current_time - last_update;
		        if (time_difference > 0) {
		            movement = Math.abs((accX + accY + accZ) - (prevX - prevY - prevZ)) / time_difference;
		            
		            last_movement = current_time;
		            prevX = accX;
		            prevY = accY;
		            prevZ = accZ;
		            last_update = current_time;
		        }
		         
		         
		        Toast.makeText(getApplicationContext(), "Velocidad de: "+String.valueOf(movement), Toast.LENGTH_SHORT).show();
		    } 
		  
		}
	
	@Override
	  protected void onStop() 
	  {
		  super.onStop();
	      mSensorManager.unregisterListener(this);
	  }	 
	
	@Override
	protected void onResume() {
	    super.onResume();
	    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);        
	    if (sensors.size() > 0) {
	        mSensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
	    }
	}
	
	public float darAccX()
	{
		return accX;
	}
	
	public float darAccY()
	{
		return accY;
	}
	
	public float darAccZ()
	{
		return accZ;
	}
	
	public float darMov()
	{
	    return movement;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
