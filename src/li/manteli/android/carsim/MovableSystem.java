package li.manteli.android.carsim;

import java.util.ArrayList;

/***
 * Logic for handling all {@link MovableObject}s in this system.
 * 
 * @author Matti Nelimarkka
 * 
 * */
public class MovableSystem {
	
	/**
	 * Currently selected {@link MovableObject}.
	 * */
	private MovableObject selected = null;
	
	/**
	 * All {@link MovableObject}s in the system.
	 * */
	private ArrayList<MovableObject> objects = new ArrayList<MovableObject>();
	
	/**
	 * Counter for the currently selected object.
	 * 
	 * Used in selecting the next objects.
	 * */
	private int current = 0;
	
	/**
	 * Default constructor. Includes debug data for this special application.
	 * */
	public MovableSystem() {
        // debug
        int[] debugCars = {R.drawable.truck, R.drawable.car_ambulance, R.drawable.car_sisu };
        for( int i : debugCars ) {
        	MovableObject c = new MovableObject( i , R.raw.forward, R.raw.backward, -1, -1 );
        	this.objects.add( c );
        }
        
        // set initial selected
        this.selected = this.objects.get(current);
	}
	
	/**
	 * Moves the system to next {@link MovableObject}. Can do carousel.
	 * */
	public void nextMovable() {
    	current = ++current % this.objects.size();
    	selected = this.objects.get(current);
	}
	
	/**
	 * Moves the system to the previous {@link MovableObject}. Can do carousel.
	 * */
	public void previousMovable() {
		current = --current % this.objects.size();
		selected = this.objects.get(current);
	}
	
	/**
	 * Returns the currently selected {@link MovableObject}.
	 * */
	public MovableObject getCurrent() {
		return selected;
	}

}
