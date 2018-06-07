package t5align;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.real.FloatType;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;

public class Align_Plugin_Example2 implements PlugIn
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

		RandomAccessibleInterval< FloatType > template = ImageJFunctions.convertFloat( impTemplate );
		RandomAccessibleInterval< FloatType > image = ImageJFunctions.convertFloat( impImage );
		if ( image == null || template == null )
		{
			IJ.showMessage( "Image type not supported." );
			return;
		}

		Align< FloatType > align = new Align<>( template, new ArrayImgFactory<>( new FloatType() ) );
		AffineTransform transform = align.align( image, maxIterations, minParameterChange );

		// TODO:
		// 1.) Use ImageJFunctions.wrapRGBA(), wrapByte(), etc. to get a Img<T> wrapping impImage
		//     with T according to the type of the ImagePlus.
		// 2.) Show the transformed Img<T>. Maybe put this functionality into a generic method to
		//     avoid having to re-implement each case.
		switch ( impTemplate.getType() )
		{
		case ImagePlus.GRAY8:
			// TODO
			break;
		case ImagePlus.GRAY16:
			// TODO
			break;
		case ImagePlus.GRAY32:
			// TODO
			break;
		case ImagePlus.COLOR_RGB:
			// TODO
			break;
		default:
			IJ.showMessage( "Image type not supported." );
		}
	}

	public static void main( final String[] args )
	{
		new ImageJ();
		IJ.openImage( "images/template.png" ).show();
		IJ.openImage( "images/image-2.png" ).show();
		new Align_Plugin_Example2().run( null );
	}
}
