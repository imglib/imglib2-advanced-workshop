package t5align;

import static t1copy.completed.CopyExample2.copy;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class ShowIterationErrorExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > image = new ImgOpener( ij.context() ).openImgs( "images/image-2.png", factory ).get( 0 ).getImg();
		final Img< FloatType > template = new ImgOpener( ij.context() ).openImgs( "images/template.png", factory ).get( 0 ).getImg();

		final int numIterations = 10;

		final int n = template.numDimensions();
		final long[] dim = new long[ n + 1 ];
		for ( int d = 0; d < n; ++d )
			dim[ d ] = template.dimension( d );
		dim[ n ] = numIterations;
		final Img< FloatType > errors = factory.create( dim );

		final Align< FloatType > align = new Align<>( template, factory );
		for ( int i = 0; i < numIterations; ++i )
		{
			align.alignStep( image );
			copy( align.error, Views.iterable( Views.hyperSlice( errors, n, i ) ) );
		}

		ImageJFunctions.show( errors, "iterations" );
	}
}
