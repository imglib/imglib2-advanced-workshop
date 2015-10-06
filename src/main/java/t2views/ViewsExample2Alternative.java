package t2views;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

public class ViewsExample2Alternative
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImgFactory< UnsignedByteType > factory = new ArrayImgFactory< UnsignedByteType >();
		final Img< UnsignedByteType > img = new ImgOpener().openImg( "images/t1-head.tif", factory, new UnsignedByteType() );
		ImageJFunctions.show( img );

		// TODO:
		// 1.) Get a XZ slice at Y=100 using Views.hyperslice
		// 2.) Get a rotated view of the input stack
		RandomAccessibleInterval< UnsignedByteType > view = img;

		ImageJFunctions.show( view );
	}
}
