package t8customtypes;

import net.imglib2.type.Type;

/*
 * Your mission: 
 * 
 * - Complete this type class so that it can be used in a String image. You need 
 * to implement the Type interface; placeholders are already here.
 * 
 * - Try to guess what interfaces to implement to make an image based on this type
 * able to be used in a 
 *     - Maximum Intensity Projection (except that here we don't have an intensity
 *     but strings.
 *     - A Summing Intensity Projection.
 *     
 * - Then implement these interfaces here.
 * 
 * 
 * NOTE: 
 * This type is not relying on proxy objects like for instance the numeric types of ImgLib2.
 * Indeed, for these numeric types on real-world images it would be impractical to return  
 * an instance of the type for each pixel. The overhead would be gigantic. So FloatTye, 
 * UnsignedIntType, etc. are all based on proxy objects that allows for efficient image 
 * manipulation.
 * Here we don't have that: our type is not based on a proxy and there is one StringType object
 * per "pixel" in the image. So the number of string pixels we can have in an image with this
 * dummy solution has some practical limits.
 */

/**
 * An ImgLib2 type for String objects.
 */
public class StringType implements Type< StringType >
{

	@Override
	public boolean valueEquals( StringType t )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StringType copy()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringType createVariable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set( StringType t )
	{
		// TODO Auto-generated method stub
	}

	/**
	 * An extra method to modify the content of this type from a string.
	 * 
	 * @param t
	 *            the string to wrap in this type.
	 */
	public void set( String t )
	{
		// TODO Auto-generated method stub
	}

}
