package t3gradient;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class GradientExample2
{
	/**
	 * Compute the partial derivatives of source in every dimension.
	 *
	 * @param source
	 *            n dimensional source image, has to provide valid data in the
	 *            interval of the gradient image plus a one pixel border in
	 *            every dimension.
	 * @param target
	 *            n+1 dimensional output image. Dimension n is used to index the
	 *            partial derivative. For example, the partial derivative by Y
	 *            is stored in slice n=1.
	 */
	public static < T extends NumericType< T > > void gradients(
			final RandomAccessible< T > source,
			final RandomAccessibleInterval< T > target )
	{
		// TODO: complete the gradients() method
		// The target image is n+1 dimensional. Dimension n is used to index the
		// partial derivative. For example, the partial derivative by Y is
		// stored in slice n=1.
		//
		// Iterate over the dimensions of the source image and use the previously
		// defined gradient() method to compute gradients in every dimension.
		// Use Views.hyperSlice() to access corresponding slices of the target image.
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final Img< FloatType > input = new ImgOpener().openImg( "images/bee-1.tif", factory, type );
		ImageJFunctions.show( input );

		final int n = input.numDimensions();
		final long[] dim = new long[ n + 1 ];
		for ( int d = 0; d < n; ++d )
			dim[ d ] = input.dimension( d );
		dim[ n ] = n;
		final Img< FloatType > gradients = factory.create( dim, type );

		gradients( Views.extendBorder( input ), gradients );
		ImageJFunctions.show( gradients );
	}

}
