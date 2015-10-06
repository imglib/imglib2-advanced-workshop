package t2views.completed;

import io.scif.img.IO;
import io.scif.img.ImgIOException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

public class ViewsExample2Alternative
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImgFactory< UnsignedByteType > factory = new ArrayImgFactory< UnsignedByteType >();
		final Img< UnsignedByteType > img = IO.openImgs( "images/t1-head.tif", factory, new UnsignedByteType() ).get( 0 ).getImg();
		ImageJFunctions.show( img );

		final RandomAccessibleInterval< UnsignedByteType > view = Views.hyperSlice( img, 1, 100 );

		// also try these:
//		final RandomAccessibleInterval< UnsignedByteType > view = Views.hyperSlice( img, 0, 100 );
//		final RandomAccessibleInterval< UnsignedByteType > view = Views.rotate( img, 0, 2 );
//		final RandomAccessibleInterval< UnsignedByteType > view = Views.interval(
//				Views.extendMirrorSingle( img ),
//				Intervals.createMinMax( -300, -300, -100, 300, 300, 100 ) );

		ImageJFunctions.show( view );
	}
}
