package t3gradient.completed;

import net.imglib2.Cursor;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class GradientExample1
{
	/**
	 * Compute the partial derivative of source in a particular dimension.
	 *
	 * @param source
	 *            source image, has to provide valid data in the interval of the
	 *            gradient image plus a one pixel border in dimension.
	 * @param target
	 *            output image, the partial derivative of source in the
	 *            specified dimension.
	 * @param dimension
	 *            along which dimension the partial derivatives are computed
	 */
	public static < T extends NumericType< T > > void gradient(
			final RandomAccessible< T > source,
			final RandomAccessibleInterval< T > target,
			final int dimension )
	{
		final Cursor< T > front = Views.flatIterable(
				Views.interval( source,
						Intervals.translate( target, 1, dimension ) ) ).cursor();
		final Cursor< T > back = Views.flatIterable(
				Views.interval( source,
						Intervals.translate( target, -1, dimension ) ) ).cursor();
		for( final T t : Views.flatIterable( target ) )
		{
			t.set( front.next() );
			t.sub( back.next() );
			t.mul( 0.5 );
		}
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final Img< FloatType > input = new ImgOpener().openImg( "images/bee-1.tif", factory, type );
		ImageJFunctions.show( input );

		final Img< FloatType > dX = factory.create( input, type );
		gradient( Views.extendBorder( input ), dX, 0 );
		ImageJFunctions.show( dX, "gradient X" );

		final Img< FloatType > dY = factory.create( input, type );
		gradient( Views.extendBorder( input ), dY, 1 );
		ImageJFunctions.show( dY, "gradient Y" );
	}

}
