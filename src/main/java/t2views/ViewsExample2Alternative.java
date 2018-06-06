package t2views;

import io.scif.img.ImgIOException;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import ij.IJ;
import ij.ImagePlus;

public class ViewsExample2Alternative
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImagePlus imp = IJ.openImage( "images/t1-head.tif" );
		final Img< UnsignedByteType > img = ImageJFunctions.wrapByte( imp );
		ImageJFunctions.show( img, "t1-head" );

		// TODO:
		// 1.) Get a XZ slice at Y=100 using Views.hyperslice
		// 2.) Get a rotated view of the input stack
		final RandomAccessibleInterval< UnsignedByteType > view = img;

		ImageJFunctions.show( view, "view" );
	}
}
