package t4realviews.completed;

import io.scif.img.ImgIOException;

import io.scif.img.ImgOpener;
import net.imagej.ImageJ;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.RealViews;
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
		final RealRandomAccessible< T > interpolated = Views.interpolate( source, new NLinearInterpolatorFactory<>() );
		final RandomAccessible< T > warped = RealViews.affine( interpolated, transform );

		final Cursor< T > cw = Views.flatIterable( Views.interval( warped, difference ) ).cursor();
		final Cursor< T > ct = Views.flatIterable( Views.interval( target, difference ) ).cursor();
		for ( final T t : Views.flatIterable( difference ) )
		{
			t.set( cw.next() );
			t.sub( ct.next() );
		} 
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > imgFactory = new ArrayImgFactory<>( new FloatType() );
		final ImgOpener opener = new ImgOpener( ij.context() );
		final Img< FloatType > template = opener.openImgs( "images/template.png", imgFactory ).get( 0 ).getImg();
		final Img< FloatType > image = opener.openImgs( "images/image-1.png", imgFactory ).get( 0 ).getImg();
		final Img< FloatType > difference = imgFactory.create( template );

		// TODO:
		// If you want, you can try to find the transform parameters that brings
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
