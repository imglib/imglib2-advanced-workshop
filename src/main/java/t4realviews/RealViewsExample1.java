package t4realviews;

import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.display.RealARGBConverter;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.LanczosInterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.ui.InteractiveRealViewer2D;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class RealViewsExample1
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final Img< FloatType > img = new ImgOpener().openImg( "images/bee-1.tif", new ArrayImgFactory< FloatType >(), new FloatType() );
		System.out.println( Util.printInterval( img ));

		// TODO: try the different InterpolatorFactories
		InterpolatorFactory< FloatType, RandomAccessible< FloatType > > factory;
		factory = new NearestNeighborInterpolatorFactory< FloatType >();
//		factory = new NLinearInterpolatorFactory< FloatType >();
//		factory = new LanczosInterpolatorFactory< FloatType >();

		final RandomAccessible< FloatType > input = Views.extendValue( img, new FloatType( 128 ) );
		final RealRandomAccessible< FloatType > interpolated = Views.interpolate( input, factory );

		new InteractiveRealViewer2D< FloatType >( 639, 373, interpolated, new RealARGBConverter< FloatType >( 0, 255 ) );
	}
}
