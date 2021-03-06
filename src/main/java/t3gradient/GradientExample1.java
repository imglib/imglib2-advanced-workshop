package t3gradient;

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
		// TODO: complete the gradient() method
		// 1.) Define a view that takes an interval out of source that
		//     corresponds to the target interval shifted by 1 in dimension.
		//     Hint: Use Intervals.translate() and Views.interval().
		// 2.) Make the view flatIterable() and obtain a cursor().
		// 3.) Likewise get a cursor from a view shifted by -1 in dimension.
		// 4.) Iterate both views and target simultaneously to compute the
		//     gradient at every pixel.
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

		// TODO:
		// Compute and show the gradient of input in Y direction
	}

}
