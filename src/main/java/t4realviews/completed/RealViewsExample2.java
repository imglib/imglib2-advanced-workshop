package t4realviews.completed;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class RealViewsExample2
{
	public static void main( String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > imgFactory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > img = new ImgOpener( ij.context() ).openImgs( "images/bee-1.tif", imgFactory ).get( 0 ).getImg();

		RandomAccessible< FloatType > input = Views.extendValue( img, new FloatType( 128 ) );
		RealRandomAccessible< FloatType > interpolated = Views.interpolate( input, new NLinearInterpolatorFactory<>() );

		AffineTransform2D affine = new AffineTransform2D();
		affine.scale( 2.3 );
		affine.translate( -400.0, -200.0 );

		RealRandomAccessible< FloatType > realview = RealViews.affineReal( interpolated, affine );

		RandomAccessibleInterval< FloatType > view = Views.interval( Views.raster( realview ), img );

		ImageJFunctions.show( view );

		// alternatively using RealViews.affine():
		RandomAccessible< FloatType > view2 = RealViews.affine( interpolated, affine );
		ImageJFunctions.show( Views.interval( view2, img ) );
	} 
}
