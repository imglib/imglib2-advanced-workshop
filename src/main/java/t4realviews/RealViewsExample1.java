package t4realviews;

import io.scif.img.IO;
import io.scif.img.ImgIOException;

import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.converter.RealARGBConverter;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.ui.viewer.InteractiveRealViewer2D;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class RealViewsExample1
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final Img< FloatType > img = IO.openImgs( "images/bee-1.tif", new ArrayImgFactory<>(), new FloatType() ).get( 0 ).getImg();
		System.out.println( Util.printInterval( img ) );

		// TODO: try the different InterpolatorFactories
		InterpolatorFactory< FloatType, RandomAccessible< FloatType > > factory;
		factory = new NearestNeighborInterpolatorFactory<>();
//		factory = new NLinearInterpolatorFactory<>();
//		factory = new LanczosInterpolatorFactory<>();

		final RandomAccessible< FloatType > input = Views.extendValue( img, new FloatType( 128 ) );
		final RealRandomAccessible< FloatType > interpolated = Views.interpolate( input, factory );

		new InteractiveRealViewer2D<>( 639, 373, interpolated, new AffineTransform2D(), new RealARGBConverter<>( 0, 255 ) );
	}
}
