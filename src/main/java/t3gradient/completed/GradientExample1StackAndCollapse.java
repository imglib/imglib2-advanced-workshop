package t3gradient.completed;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class GradientExample1StackAndCollapse
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
		final int n = target.numDimensions();
		final long[] left = new long[ n ];
		left[ dimension ] = -1;
		final long[] right = new long[ n ];
		right[ dimension ] = 1;

		Views.iterable(
				Views.collapse(
						Views.stack(
//								StackAccessMode.MOVE_ALL_SLICE_ACCESSES,
								target,
								Views.interval( Views.translate( source, left ), target ),
								Views.interval( Views.translate( source, right ), target ) ) ) )
				.forEach(
						t -> {
							t.get( 0 ).set( t.get( 1 ) );
							t.get( 0 ).sub( t.get( 2 ) );
							t.get( 0 ).mul( 0.5 );
						} );
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > input = new ImgOpener( ij.context() ).openImgs( "images/bee-1.tif", factory ).get( 0 ).getImg();
		ImageJFunctions.show( input );

		final Img< FloatType > dX = factory.create( input );
		gradient( Views.extendBorder( input ), dX, 0 );
		ImageJFunctions.show( dX, "gradient X" );

		final Img< FloatType > dY = factory.create( input );
		gradient( Views.extendBorder( input ), dY, 1 );
		ImageJFunctions.show( dY, "gradient Y" );
	}

}
