package t5align;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.img.Img;
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
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > image = new ImgOpener( ij.context() ).openImgs( "images/image-2.png", factory ).get( 0 ).getImg();
		final Img< FloatType > template = new ImgOpener( ij.context() ).openImgs( "images/template.png", factory ).get( 0 ).getImg();

		final Align< FloatType > align = new Align<>( template, factory );
		final AffineTransform transform = align.align( image, 500, 0.01 );

		ij.ui().showUI();
		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( image, "image" );
		ImageJFunctions.show(
				Views.interval(
						RealViews.affine(
								Views.interpolate(
										Views.extendValue( image, new FloatType(0) ),
										new NLinearInterpolatorFactory<>() ),
						transform ),
				template ),
				"aligned image" );
	}
}
