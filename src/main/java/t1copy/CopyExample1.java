package t1copy;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.FloatType;

public class CopyExample1
{
	public static void copy( final Img< FloatType > source, final Img< FloatType > target )
	{
		// TODO: Complete this method.
		// Use a Cursor on the target image to fill every pixel with values from source.
		// Use a RandomAccess on the source image to retrieve source pixels.
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		// load input image
		final String filename = "images/bee-1.tif";
		final ImgFactory< FloatType > factory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > input = new ImgOpener( ij.context() ).openImgs( filename, factory ).get( 0 ).getImg();

		// create output image to hold a copy of the input image
		final Dimensions dim = input;
		final Img< FloatType > output = factory.create( dim );

		// copy input to output
		copy( input, output );

		// show output
		ij.ui().show( "output", output );
	}
}
