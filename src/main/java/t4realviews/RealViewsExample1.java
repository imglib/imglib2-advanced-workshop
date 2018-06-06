package t4realviews;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvSource;

public class RealViewsExample1
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > imgFactory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > img = new ImgOpener( ij.context() ).openImgs( "images/bee-1.tif", imgFactory ).get( 0 ).getImg();
		System.out.println( Util.printInterval( img ) );

		// TODO: try the different InterpolatorFactories
		InterpolatorFactory< FloatType, RandomAccessible< FloatType > > factory;
		factory = new NearestNeighborInterpolatorFactory<>();
//		factory = new NLinearInterpolatorFactory<>();

		final RandomAccessible< FloatType > input = Views.extendValue( img, new FloatType( 128 ) );
		final RealRandomAccessible< FloatType > interpolated = Views.interpolate( input, factory );

		final BdvSource source = BdvFunctions.show( interpolated, img, "interpolated", Bdv.options().is2D() );
		source.setDisplayRange( 0, 255 );
	}
}
