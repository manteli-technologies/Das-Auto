package li.manteli.android.carsim;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Container for a single movable object in the system.
 * 
 * Contains audio resources and images of the object.
 * 
 * @author Matti Nelimarkka 
 * 
 **/
public class MovableObject {
	
	/**
	 * Potantial directions of the object, i.e. directions how the device can be moved.
	 * 
	 * Potentail directions are forward and backward (along x-axis) and left and right (along y-axis).
	 * 
	 * */
	public enum DIRECTION{
		
		FORWARD(0),
		BACKWARD(2),
		LEFT(3),
		RIGHT(1);
		// NOT implemented
		// UP(4),
		// DOWN(5);
		
		/**
		 * Index of this direction. Used in the internal dynamic of {@link MovableObject}.
		 * */
		private int index;
		
		/**
		 * Default constructor.
		 * 
		 * @param index place of this direction's audio file in sounds array.
		 * */
		DIRECTION(int index) {
			this.index = index;
		}
		
	}
	
	/**
	 * Storage of audio resource file IDs for all directions.
	 * */
	private int[] sounds = new int[ DIRECTION.values().length ];
	
	/**
	 * Image resource file ID
	 * */
	private int image = 0;
	
	/**
	 * Direction of this MovableObject.
	 * 
	 * */
	private DIRECTION direction = DIRECTION.FORWARD;
	
	/**
	 * Setter for direction.
	 * 
	 * @param direction new direction
	 * */
	public void setDirection(DIRECTION direction) {
		this.direction = direction;
	}
	
	/**
	 * Getter for direction.
	 * 
	 * @return direction of this object
	 * */
	public DIRECTION getDirection() {
		return this.direction;
	}
	
	/**
	 * Gets the media resource (audio) for this object.
	 * 
	 * Depends on the given direction using the resources stored in sounds-table.
	 * 
	 * @param c context where media file is applied
	 * @param direction direction that should be selected
	 * 
	 * @return a {@link MediaPlayer} object for this soundfile. The object is set on looping mode.
	 * 
	 * */
	public MediaPlayer getSound(Context c, DIRECTION direction){
		MediaPlayer m = MediaPlayer.create( c, this.sounds[direction.index] );
		m.setLooping(true);
		return m;
	}
	
	/**
	 * Gets the media resource (audio) for this object.
	 * 
	 * Depends on the set direction of this object.
	 * 
	 * @param c context where the media file is applied.
	 * 
	 * @returns a {@link MediaPlayer} object for this direction.
	 * 
	 * @see #getSound(Context, DIRECTION)
	 * 
	 * */
	public MediaPlayer getSound(Context c) {
		return this.getSound( c, this.direction );
	}
	
	/**
	 * Returns the image resource ID for this MovableObject.
	 * */
	public int getImage() {
		return this.image;
	}
	
	/***
	 * Constructor for this class. Parameters should point to valid resources in the R-file.
	 * 
	 * Note, no validation used in the passed arguments. Invalid arguments will cause a crash!
	 * 
	 * @see android.R
	 * 
	 * @param image the default image shown when this object is displayd
	 * @param forward the audio file played when moving forward
	 * @param backward the audio file played when moving backward
	 * @param left the audio file played when moving left
	 * @param right the audio file played when moving right
	 * 
	 * */
	public MovableObject(int image, int forward, int backward, int left, int right) {
		this.image = image;
		this.sounds[DIRECTION.FORWARD.index] = forward;
		this.sounds[DIRECTION.BACKWARD.index] = backward;
		this.sounds[DIRECTION.LEFT.index] = left;
		this.sounds[DIRECTION.RIGHT.index] = right;
	}

}
