package t8customtypes.completed;

import net.imglib2.type.Type;
import net.imglib2.type.operators.Add;
import net.imglib2.type.operators.SetZero;

/**
 * An ImgLib2 type for String objects.
 */
public class StringType implements Type< StringType >, Comparable< StringType >, Add< StringType >, SetZero
{
	/**
	 * The string we wrap.
	 */
	private String str;

	public StringType()
	{
		this( "" );
	}

	public StringType( final StringType t )
	{
		this( t.str );
	}

	public StringType( final String str )
	{
		this.str = str;
	}

	@Override
	public StringType createVariable()
	{
		return new StringType();
	}

	@Override
	public StringType copy()
	{
		return new StringType( this );
	}

	@Override
	public void set( final StringType t )
	{
		str = t.str;
	}

	/**
	 * An extra method to modify the content of this type from a string.
	 *
	 * @param t
	 *            the string to wrap in this type.
	 */
	public void set( final String t )
	{
		str = t;
	}

	@Override
	public String toString()
	{
		// To facilitate display.
		return str;
	}

	@Override
	public boolean valueEquals( final StringType t )
	{
		return compareTo( t ) == 0;
	}

	/*
	 * COMPARE INTERFACE.
	 */

	@Override
	public int compareTo( final StringType o )
	{
		// This is how we define comparing for string types. It can be anything you want.
		return toString().compareToIgnoreCase( o.toString() );
	}

	/*
	 * ADD INTERFACE.
	 */

	@Override
	public void add( final StringType t )
	{
		// Since string are immutable, we have to replace this type's string.
		str = toString() + t.toString();
	}

	/*
	 * SETZERO INTERFACE.
	 */

	@Override
	public void setZero()
	{
		str = "";
	}
}
