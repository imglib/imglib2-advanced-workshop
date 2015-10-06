package t2views;

import static t1copy.completed.CopyExample2.copy;
import io.scif.img.IO;
import io.scif.img.ImgIOException;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ViewsExample1< T extends NativeType< T > & NumericType< T >>
{
	public void example( final Img< T > img )
	{
		ImageJFunctions.show( img );

		// TODO:
		// 1.) Try show( img, "img" ).
		//     It displays img in a 400x300 canvas with the origin in the center
		//     of the canvas.

		// TODO: Experiment with the static methods of class Views:
		// 1.) Show() the following translated view of img:
		final RandomAccessibleInterval<T> t = Views.translate(img, -20, -20);

		// 2.) Invert the X axis of the view 1.)

		// 3.) Rotate the view 2.) 90 degree clockwise

		// 4.) Shift view 3.) to the origin

		// 5.) Show() the following periodically extended view of img:
		final RandomAccessible<T> p = Views.extendPeriodic( img );

		// 6.) Crop an interval of view 5.)

		// 7.) Use a view to zero-fill a 20x20 rectangle of img:
		//     use Intervals.createMinSize() to define the interval.
		//     use Views.iterable() to make the cropped rectangle iterable.
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final UnsignedByteType backgroundValue = new UnsignedByteType( 128 );
		final UnsignedByteType axisValue = new UnsignedByteType( 0 );
		final ViewsExample1< UnsignedByteType > v = new ViewsExample1< UnsignedByteType >( 400, 300, backgroundValue, axisValue );

		final ImgFactory< UnsignedByteType > factory = new ArrayImgFactory< UnsignedByteType >();
		final Img< UnsignedByteType > img = IO.openImgs( "images/imglib2-logo-gray-70x80-b.tif", factory, new UnsignedByteType() ).get( 0 ).getImg();
		v.example( img );
	}

	final int displayWidth;
	final int displayHeight;
	final T backgroundValue;
	final T axisValue;

	ViewsExample1( final int displayWidth, final int displayHeight, final T backgroundValue, final T axisValue )
	{
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.backgroundValue = backgroundValue;
		this.axisValue = axisValue;
	}

	void show( final RandomAccessibleInterval< T > img, final String name )
	{
		show( Views.extendValue( img, backgroundValue ), name );
	}

	void show( final RandomAccessible< T > img, final String name )
	{
		assert img.numDimensions() == 2;

		final ImgFactory< T > factory = new ArrayImgFactory< T >();
		final Img< T > outputImg = factory.create( new long[] { displayWidth, displayHeight }, backgroundValue );
		final RandomAccessibleInterval< T > output = Views.translate( outputImg, -displayWidth / 2, -displayHeight / 2 );
		copy( img, Views.iterable( output ) );
		for ( final T t : Views.iterable( Views.interval( output, Intervals.createMinMax( -10, 0, 10, 0 ) ) ) )
			t.set( axisValue );
		for ( final T t : Views.iterable( Views.interval( output, Intervals.createMinMax( 0, -10, 0, 10 ) ) ) )
			t.set( axisValue );
		ImageJFunctions.show( output, name );
	}
}
