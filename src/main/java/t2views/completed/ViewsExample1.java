package t2views.completed;

import static t1copy.completed.CopyExample2.copy;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ViewsExample1< T extends NativeType< T > & NumericType< T > >
{
	public void example( final Img< T > img )
	{
		show( img, "img" );

		final RandomAccessibleInterval< T > t = Views.translate( img, -20, -20 );
		show( t, "t" );

		final RandomAccessibleInterval< T > ti = Views.invertAxis( t, 0 );
		show( ti, "ti" );

		final RandomAccessibleInterval< T > tir = Views.rotate( ti, 0, 1 );
		show( tir, "tir" );

		final RandomAccessibleInterval< T > tirz = Views.zeroMin( tir );
		show( tirz, "tirz" );

		final RandomAccessible< T > p = Views.extendPeriodic( img );
		show( p, "p" );

		final RandomAccessibleInterval< T > pi = Views.interval( p, Intervals.createMinMax( -10, -20, 60, 80 ) );
		show( pi, "pi" );

		final IterableInterval< T > square = Views.iterable( Views.interval( img, Intervals.createMinSize( 40, 40, 20, 20 ) ) );
		for ( final T pixel : square )
			pixel.setZero();
		show( img, "img with square" );
	}

	public static void main( final String[] args ) throws ImgIOException
	{
		final UnsignedByteType backgroundValue = new UnsignedByteType( 128 );
		final UnsignedByteType axisValue = new UnsignedByteType( 0 );
		final ViewsExample1< UnsignedByteType > v = new ViewsExample1<>( 400, 300, backgroundValue, axisValue );

		final ImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>( new UnsignedByteType() );
		final Img< UnsignedByteType > img = new ImgOpener( v.ij.context() ).openImgs( "images/imglib2-logo-gray-70x80-b.tif", factory ).get( 0 ).getImg();
		v.example( img );
	}

	final ImageJ ij;
	final int displayWidth;
	final int displayHeight;
	final T backgroundValue;
	final T axisValue;

	ViewsExample1( final int displayWidth, final int displayHeight, final T backgroundValue, final T axisValue )
	{
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.backgroundValue = backgroundValue;
		this.axisValue = axisValue;

		ij = new ImageJ();
	}

	void show( final RandomAccessibleInterval< T > img, final String name )
	{
		show( Views.extendValue( img, backgroundValue ), name );
	}

	void show( final RandomAccessible< T > img, final String name )
	{
		assert img.numDimensions() == 2;

		final ImgFactory< T > factory = new ArrayImgFactory<>( backgroundValue );
		final Img< T > outputImg = factory.create( displayWidth, displayHeight );
		final RandomAccessibleInterval< T > output = Views.translate( outputImg, -displayWidth / 2, -displayHeight / 2 );
		copy( img, Views.iterable( output ) );
		Views.iterable( Views.interval( output, Intervals.createMinMax( -10, 0, 10, 0 ) ) ).forEach( t -> t.set( axisValue ) );
		Views.iterable( Views.interval( output, Intervals.createMinMax( 0, -10, 0, 10 ) ) ).forEach( t -> t.set( axisValue ) );
		ImageJFunctions.show( output, name );
//		ij.ui().show( name, outputImg );
	}
}
