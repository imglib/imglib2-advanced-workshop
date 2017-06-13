package t8customtypes.completed;

import net.imglib2.type.Type;
import net.imglib2.type.operators.Add;

public class StringType implements Type< StringType >, Comparable< StringType >, Add< StringType >
{

	/**
	 * The string we wrap.
	 */
	private String str;

	public StringType()
	{}

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
		return new StringType( str );
	}

	@Override
	public void set( final StringType c )
	{
		str = c.str;
	}

	public void set( final String str )
	{
		this.str = str;
	}

	@Override
	public String toString()
	{
		// To facilitate display.
		return str;
	}
	
	/*
	 * COMPARE METHODS.
	 */

	@Override
	public int compareTo( final StringType o )
	{
		// This is how we define comparing for string types. It can be anything you want.
		return str.compareToIgnoreCase( o.str );
	}

	@Override
	public boolean valueEquals( StringType t )
	{
		return str.equals( t.str );
	}
	
	/*
	 * ADD METHODS.
	 */

	@Override
	public void add( StringType t )
	{
		// Since string are immutable, we have to replace this type's string.
		this.str +=  t;
	}

}
