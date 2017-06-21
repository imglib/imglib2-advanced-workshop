package t8customtypes.completed;

import java.util.Arrays;
import java.util.List;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.list.ListCursor;
import net.imglib2.img.list.ListImg;
import net.imglib2.img.list.ListImgFactory;

public class CustomTypeMIPExample
{

	public static void main( final String[] args )
	{
		final Img< StringType > img = getStringImage();

		System.out.println( "Input image:\n\n" + toString( img, 12 ) );

		final Img< StringType > mip = performMIP( img );
		System.out.println( "\nResult of MIP:\n\n" + toString( mip, 12 ) );

		final Img< StringType > sip = performSIP( img );
		System.out.println( "\nResult of SIP:\n\n" + toString( sip, 12 ) );
	}

	/**
	 * Performs summation-intensity projection along the Y axis for the
	 * specified 2D image.
	 *
	 * @param img
	 *            the 2D input.
	 * @return a new 1D image.
	 */
	private static Img< StringType > performSIP( final Img< StringType > img )
	{
		final ListImg< StringType > sip = new ListImgFactory< StringType >()
				.create( new long[] { img.dimension( 0 ), 1l }, new StringType() );

		final ListCursor< StringType > outputCursor = sip.cursor();
		final RandomAccess< StringType > inputRA = img.randomAccess();
		while ( outputCursor.hasNext() )
		{
			outputCursor.fwd();
			final StringType target = outputCursor.get();

			// Initialize with empty value.
			target.set( "" );

			inputRA.setPosition( outputCursor.getIntPosition( 0 ), 0 );
			for ( int y = 0; y < img.dimension( 1 ); y++ )
			{
				inputRA.setPosition( y, 1 );
				final StringType st = inputRA.get();
				target.add( st );
			}
		}

		return sip;
	}

	/**
	 * Performs maximum-intensity projection along the Y axis for the specified
	 * 2D image.
	 *
	 * @param img
	 *            the 2D input.
	 * @return a new 1D image.
	 */
	private static Img< StringType > performMIP( final Img< StringType > img )
	{
		final ListImg< StringType > mip = new ListImgFactory< StringType >()
				.create( new long[] { img.dimension( 0 ), 1l }, new StringType() );

		final ListCursor< StringType > outputCursor = mip.cursor();
		final RandomAccess< StringType > inputRA = img.randomAccess();
		while ( outputCursor.hasNext() )
		{
			outputCursor.fwd();
			final StringType target = outputCursor.get();
			// Initialize with very low value.
			target.set( "" );

			inputRA.setPosition( outputCursor.getIntPosition( 0 ), 0 );
			for ( int y = 0; y < img.dimension( 1 ); y++ )
			{
				inputRA.setPosition( y, 1 );
				final StringType st = inputRA.get();
				if ( st.compareTo( target ) > 0 )
					target.set( st );
			}
		}

		return mip;
	}

	/**
	 * Create a 7x7 string based image.
	 *
	 * @return an image.
	 */
	private static Img< StringType > getStringImage()
	{
		final String[] wordArray = new String[] {
				"Bling ",   "Bromance ",    "Chillax ",   "Crunk ",       "D'oh ",       "Droolworthy ",  "1 ",
				"Don't ",   "Guyliner ",    "Hater ",     "Illiterati ",  "Infomania ",  "Jeggings ",     "? ",
				"Blast ",   "Mankini ",     "Taratata ",  "Cake ",        "Muggle ",     "Noob ",         "< ",
				"Aaron ",   "Po-po ",       "Purple ",    "Freud ",       "Really ",     "Leetspeak ",    "= ",
				"Crown ",   "Twitterati ",  "Think ",     "ImgLib2 ",     "Killer ",     "Rocks ",        "+ ",
				"Dizzy ",   "You ",         "Florian ",   "Elicit ",      "Horrible ",   "Jan ",          ") ",
				"Albert ",  "Pavel ",       "Johannes ",  "Bob ",         "Mathias ",    "Rastafari ",    "# " };
		final List< String > words = Arrays.asList( wordArray );

		// ListImg is one of the only Accessible that allows storing non-proxy types.
		final ListImg< StringType > img = new ListImgFactory< StringType >()
				.create( new long[] { 7l, 7l }, new StringType() );
		final ListCursor< StringType > cursor = img.cursor();
		for ( final String str : words )
		{
			cursor.fwd();
			cursor.get().set( str );
		}

		return img;
	}

	/**
	 * Outputs an image to a string. Only valid for 2D images.
	 *
	 * @param img
	 *            the image to print.
	 * @param columnSize
	 *            the size of each column.
	 * @return a string representation of the image.
	 */
	private static final < T > String toString( final Img< T > img, final int columnSize )
	{
		final StringBuilder str = new StringBuilder();
		final long maxX = img.dimension( 0 );
		final long maxY = img.dimension( 1 );

		final RandomAccess< T > ra = img.randomAccess();
		for ( long y = 0; y < maxY; y++ )
		{
			ra.setPosition( y, 1 );
			for ( long x = 0; x < maxX; x++ )
			{
				ra.setPosition( x, 0 );
				final String s = ra.get().toString();
				str.append( s );
				for ( int i = 0; i < columnSize - s.length(); i++ )
				{
					str.append( ' ' );
				}
			}
			str.append( '\n' );
		}

		return str.toString();
	}

}
