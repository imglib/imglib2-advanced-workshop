package t5align;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class AlignExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final ImgOpener opener = new ImgOpener();
		final ImgPlus< FloatType > image = opener.openImg( "images/image-3.png", factory, type );
		final ImgPlus< FloatType > template = opener.openImg( "images/template.png", factory, type );

		Align< FloatType > align = new Align< FloatType >( template, factory );
		AffineTransform transform = align.align( image, 500, 0.01 );

		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( image, "image" );
		ImageJFunctions.show(
				Views.interval(
						RealViews.constantAffine(
								Views.interpolate(
										Views.extendValue( image, new FloatType(0) ),
										new NLinearInterpolatorFactory< FloatType >() ),
						transform ),
				template ),
				"aligned image" );
	}
}
