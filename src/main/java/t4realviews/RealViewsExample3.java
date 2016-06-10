package t4realviews;

import io.scif.img.IO;
import io.scif.img.ImgIOException;

import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class RealViewsExample3
{
	/**
	 * Compute the pixel-wise difference between an affine-transformed source
	 * image and a target image.
	 *
	 * @param source
	 *            The source image.
	 * @param transform
	 *            A coordinate transformation to apply to the source image.
	 * @param target
	 *            The target image.
	 * @param difference
	 *            Output image. The pixel-wise difference between the
	 *            transformed source image and the target image is stored here.
	 */
	public static < T extends NumericType< T > > void computeDifference(
			final RandomAccessible< T > source,
			final AffineTransform transform,
			final RandomAccessible< T > target,
			final RandomAccessibleInterval< T > difference )
	{
		// TODO:
		// 1.) interpolate the source image (use Views.interpolate())
		// 2.) apply the transform to the interpolated source (use RealViews.affine())
		// 3.) Crop an interval out of the transformed source and the target.
		//     The cropped interval is identical to the interval of the output
		//     difference image. Use Views.interval().
		// 4.) Make the cropped views and the output difference image iterable.
		//	   Use Views.flatIterable() to enforce the same iteration order on
		//     all the images.
		// 5.) Simultaneously iterate the cropped views and the output image
		//     and compute the pixel-wise differences.
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>();
		final Img< FloatType > template = IO.openImgs( "images/template.png", factory, type ).get( 0 ).getImg();
		final Img< FloatType > image = IO.openImgs( "images/image-1.png", factory, type ).get( 0 ).getImg();
		final Img< FloatType > difference = factory.create( template, type );

		// TODO:
		// If you want, you can try to find the transform parameters that bring
		// the image and template into perfect alignment.
		AffineTransform transform = new AffineTransform( 2 );
		transform.set(
				2, 0, 5,
				0, 2, 5 );
		computeDifference( Views.extendBorder( image ), transform, template, difference );

		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( image, "image" );
		ImageJFunctions.show( difference, "difference" );
	}

}
