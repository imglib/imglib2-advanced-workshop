package t1copy.completed;

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
	public static < T extends Type< T >> void copy( final RandomAccessible< T > source, final IterableInterval< T > target )
	{
		final RandomAccess< T > in = source.randomAccess();
		final Cursor< T > out = target.localizingCursor();
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
		final ImgFactory< FloatType > factory = new ArrayImgFactory<>();
		final Img< FloatType > input = IO.openImgs( filename, factory, type ).get( 0 ).getImg();

		// create output image to hold a copy of the input image
		final Dimensions dim = input;
		final Img< FloatType > output = factory.create( dim, type );

		// copy input to output
		copy( input, output );

		// show output
		ImageJFunctions.show( output );
	}
}
