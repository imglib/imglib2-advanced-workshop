package t5align;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.real.FloatType;

public class Align_Plugin_Example1 implements PlugIn
{
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

		// TODO:
		// 1.) use ImageJFunctions.convertFloat() to get the selected ImagePlus
		//     as ImgLib2 images
		RandomAccessibleInterval< FloatType > template = null;
		RandomAccessibleInterval< FloatType > image = null;
		if ( image == null || template == null )
		{
			IJ.showMessage( "Image type not supported." );
			return;
		}

		Align< FloatType > align = new Align< FloatType >( template, new ArrayImgFactory< FloatType >() );
		AffineTransform transform = align.align( image, maxIterations, minParameterChange );

		// TODO:
		// 2.) Construct and show the transformed source image.
		//     You will need to specify an interval for the transformed image --
		//     just use the interval of the template image.
	}
}
