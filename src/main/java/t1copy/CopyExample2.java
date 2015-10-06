package t1copy;

import ij.ImagePlus;
import io.scif.img.IO;
import io.scif.img.ImgIOException;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.real.FloatType;

public class CopyExample2
{
	// TODO: Generalize the copy() method.
	// 1.) Use a generic parameter to make it applicable to pixel types other than FloatType.
	// 2.) Replace Img by less specific interfaces that specify what you
	//     minimally expect of the source and target parameters.
	public static void copy( final Img< FloatType > source, final Img< FloatType > target )
	{
		final RandomAccess< FloatType > in = source.randomAccess();
		final Cursor< FloatType > out = target.localizingCursor();
		while ( out.hasNext() )
		{
			out.fwd();
			in.setPosition( out );
			out.get().set( in.get() );
		}
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		// load input image
		final String filename = "images/bee-1.tif";
		final FloatType type = new FloatType();
		final ImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final Img< FloatType > input = IO.openImgs( filename, factory, type ).get( 0 ).getImg();

		// create output image to hold a copy of the input image
		final Dimensions dim = input;
		final Img< FloatType > output = factory.create( dim, type );

		// copy input to output
		copy( input, output );

		// show output
		final ImagePlus imp = ImageJFunctions.show( output );
		imp.getProcessor().resetMinAndMax();
		imp.updateAndRepaintWindow();
	}
}
