package t5align;

import static t1copy.completed.CopyExample2.copy;
import net.imglib2.img.Img;
import net.imglib2.img.ImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class ShowIterationErrorExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final ImgOpener opener = new ImgOpener();
		final ImgPlus< FloatType > image = opener.openImg( "images/image-2.png", factory, type );
		final ImgPlus< FloatType > template = opener.openImg( "images/template.png", factory, type );

		final int numIterations = 10;
		
		final int n = template.numDimensions();
		final long[] dim = new long[ n + 1 ];
		for ( int d = 0; d < n; ++d )
			dim[ d ] = template.dimension( d );
		dim[ n ] = numIterations;
		final Img< FloatType > errors = factory.create( dim, type );

		Align< FloatType > align = new Align< FloatType >( template, factory );
		for ( int i = 0; i < numIterations; ++i )
		{
			align.alignStep( image );
			copy( align.error, Views.iterable( Views.hyperSlice( errors, n, i ) ) );
		}

		ImageJFunctions.show( errors, "iterations" );
	}
}
