package t5align.completed;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import t5align.Align;

public class Align_Plugin_Example2 implements PlugIn
{
	public static < T extends NumericType< T > > void showTransformed(
			Img< T > img,
			AffineTransform transform,
			Interval interval )
	{
		ImageJFunctions.show(
				Views.interval(
						RealViews.constantAffine(
								Views.interpolate(
										Views.extendBorder( img ),
										new NLinearInterpolatorFactory< T >() ),
								transform ),
						interval ),
				"aligned image" );
	}

	@Override
	public void run( final String arg0 )
	{
		final GenericDialogPlus dialog = new GenericDialogPlus( "Inverse Compositional Image Alignment" );
		dialog.addImageChoice( "template", null );
		dialog.addImageChoice( "image to align", null );
		dialog.addNumericField( "max iterations", 50, 0 );
		dialog.addNumericField( "min parameter change", 0.001, 4 );
		dialog.showDialog();
		if ( dialog.wasCanceled() )
			return;

		ImagePlus impTemplate = dialog.getNextImage();
		ImagePlus impImage = dialog.getNextImage();
		int maxIterations = ( int ) dialog.getNextNumber();
		double minParameterChange = dialog.getNextNumber();

		if( impTemplate == null || impImage == null )
		{
			IJ.showMessage( "Please select two images." );
			return;
		}

		RandomAccessibleInterval< FloatType > template = ImageJFunctions.convertFloat( impTemplate );
		RandomAccessibleInterval< FloatType > image = ImageJFunctions.convertFloat( impImage );
		if ( image == null || template == null )
		{
			IJ.showMessage( "Image type not supported." );
			return;
		}

		Align< FloatType > align = new Align< FloatType >( template, new ArrayImgFactory< FloatType >() );
		AffineTransform transform = align.align( image, maxIterations, minParameterChange );

		switch ( impTemplate.getType() )
		{
		case ImagePlus.GRAY8:
			showTransformed( ImageJFunctions.wrapByte( impImage ), transform, template );
			break;
		case ImagePlus.GRAY16:
			showTransformed( ImageJFunctions.wrapShort( impImage ), transform, template );
			break;
		case ImagePlus.GRAY32:
			showTransformed( ImageJFunctions.wrapFloat( impImage ), transform, template );
			break;
		case ImagePlus.COLOR_RGB:
			showTransformed( ImageJFunctions.wrapRGBA( impImage ), transform, template );
			break;
		default:
			IJ.showMessage( "Image type not supported." );
		}
	}
}
