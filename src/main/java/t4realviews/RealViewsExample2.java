package t4realviews;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class RealViewsExample2
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final Img< FloatType > img = new ImgOpener().openImg( "images/bee-1.tif", new ArrayImgFactory< FloatType >(), new FloatType() );

		RandomAccessible< FloatType > input = Views.extendValue( img, new FloatType( 128 ) );
		RealRandomAccessible< FloatType > interpolated = Views.interpolate( input, new NLinearInterpolatorFactory<FloatType>() );

		// TODO
		// 1.) Use RealViews.affineReal() to create a transformed view of
		//     "interpolated" with the following affine transform:
		AffineTransform2D affine = new AffineTransform2D();
		affine.scale( 2.3 );
		affine.translate( -400.0, -200.0 );
		RealRandomAccessible< FloatType > realview = null;

		// 2.) Create and display a rasterized and cropped view using
		//     Views.raster() and Views.interval().
		RandomAccessibleInterval< FloatType > view = null;

		ImageJFunctions.show( view );

		// TODO
		// 3.) Use RealViews.affine() instead to get an affine-transformed view that is
		//     already rasterized. Crop and display.
	}
}
