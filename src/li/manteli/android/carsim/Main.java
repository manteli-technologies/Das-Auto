package li.manteli.android.carsim;

import java.io.IOException;
import java.util.ArrayList;

import li.manteli.android.carsim.MovableObject.DIRECTION;
import android.app.Activity;
import android.content.*;
import android.hardware.*;
import android.media.MediaPlayer;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

/***
 * The main UI for this application.
 * 
 * @author Matti Nelimarkka
 * */
public class Main extends Activity implements SensorEventListener {
	
	// GENERAL
	/**
	 * Context of this application.
	 * */
	private Context context = null;
	
	/**
	 * Core application logic.
	 * */
	private MovableSystem system = new MovableSystem();
	
	// SENSORS
	
	/**
	 * {@link SensorManager} for this application.
	 * 
	 * I.e. accessory for all sensor related content.
	 * */
	private SensorManager sensorManager = null;
	
	/**
	 * Accelometer {@link Sensor}.
	 * */
	private Sensor accelometer = null;

	// MEDIA
	
	/***
	 * The current media file played.
	 * */
	private MediaPlayer currentMedia = null;
	
	// UI
	
	/**
	 * The main surface of ths application.
	 * */
	private SurfaceView mainArea = null;
	
	// UI creators and other UI stuff
	
    /**
     * Creation of this application UI and application itself.
     * 
     * Sets sensors and UI actions, enables camera see trough and sets the first displayed object on display.
     * 
     **/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.main );
        
        this.context = this.getApplicationContext();
        
        this.sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        this.accelometer = sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        sensorManager.registerListener(this, this.accelometer, SensorManager.SENSOR_DELAY_NORMAL );
        
        this.mainArea = (SurfaceView) findViewById(R.id.cameraSurface);
        
        // add change of objects displayed by a long click.
        this.mainArea.setOnLongClickListener( new View.OnLongClickListener() {
			
        	// when clicked long time, display new object
			public boolean onLongClick(View v) {
				Main.this.nextMovable();
				return true;
			}
			
		} );
        
        // create new see trugh display via camera's preview function
		SurfaceHolder cameraPreview = this.mainArea.getHolder();
		cameraPreview.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraPreview.addCallback( new SurfaceHolder.Callback() {

			private Camera c;
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				
			}

			public void surfaceCreated(SurfaceHolder holder) {
				// start using camera for this application
				c = Camera.open();
				
				try {
					c.setPreviewDisplay( holder );
					c.startPreview();
				} catch (IOException e) {
					// error
				}
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				// release camera resource for other application
				c.release();
			}
			
		} );
		
        // set initial car
        nextMovable();
				
    }
    
    /**
     * Done when application is resumed from background.
     * 
     * Stars listening sensors again.
     * */
	protected void onResume() {
		super.onResume();
		
		// register sensors
		sensorManager.registerListener(this, this.accelometer, SensorManager.SENSOR_DELAY_NORMAL );
	}

	/**
	 * Done when application goes background.
	 * 
	 * Stops playing of media files and removes sensor listeners.
	 * */
    protected void onPause() {
    	super.onPause();
    	
    	// remove sensor
    	this.sensorManager.unregisterListener(this, this.accelometer);
    	
    	// remove media
    	if( this.currentMedia != null ) {
    		this.currentMedia.release();
    	}
    }
    
    /**
     * Creates a new menu (when menu is pressed).
     * */
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	// close application
    	MenuItem m = menu.add( R.string.exit );
    	m.setOnMenuItemClickListener( new OnMenuItemClickListener() {
			
			public boolean onMenuItemClick(MenuItem item) {
				finish();
				return true;
			}
		} );
    	return true;
    }
    
    // APP LOGIC
    
    /**
     * Selects next movable object.
     * 
     * Selects the next movable object from the system and puts its image to display.
     * */
    private void nextMovable() {
    	
    	// check the currently selected method
    	system.nextMovable();
    	
    	// background
    	this.mainArea.setBackgroundResource( system.getCurrent().getImage() );    	
    }
    
    /**
     * Sets a new direction to current movable.
     * 
     *  Sets the new direction and starts playing the audio resource.
     **/
    public void switchMode(DIRECTION direction) {
    	system.getCurrent().setDirection(direction);
    	
    	if( this.currentMedia != null ) {
    		// remove the file
    		this.currentMedia.release();
    	}
    	
    	this.currentMedia = system.getCurrent().getSound(this.context);
    	this.currentMedia.start();
    }

    // SENSORS
    
	long timeStamp = System.currentTimeMillis();
	float velocity = 0;
	float previousValue;
    
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Not needed
	}

	/**
	 * Handles the change in sensors valued.
	 * 
	 * In case of accelometer, changes the mode if new and previous values have different sings.
	 * */
	public void onSensorChanged(SensorEvent event) {
		if( event.sensor.getType() == this.accelometer.getType() ) {
			
		
			// time difference
			long newTime = System.currentTimeMillis();
			long deltaT = newTime - timeStamp;
			
			float[] values = event.values;
			
			// +4.5 based on empirical testing -> don't try to understand
			// TODO: fixed when API level 9 supports gravitation
			values[1] = (float) (values[1] + 4.5);
			
			// v = a * t
			// velocity += values[1] * deltaT;
			
			if( values[1] > 0 && previousValue < 0 ) {
				switchMode(DIRECTION.FORWARD);
			}
			if( values[1] < 0 && previousValue > 0 ) {
				switchMode(DIRECTION.BACKWARD);
			}
			
			previousValue = values[1];
			
			timeStamp = newTime;
		}
		
	}
    
}