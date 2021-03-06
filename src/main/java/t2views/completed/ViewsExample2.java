package t2views.completed;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

public class ViewsExample2
{
	public static void main( final String[] args )
	{
		new ImageJ();
		IJ.run( "T1 Head (2.4M, 16-bits)" );
		final ImagePlus imp = IJ.getImage();
		final Img< UnsignedShortType > img = ImageJFunctions.wrapShort( imp );

		final RandomAccessibleInterval< UnsignedShortType > view = Views.hyperSlice( img, 1, 100 );

		// also try these:
//		final RandomAccessibleInterval< UnsignedShortType > view = Views.hyperSlice( img, 0, 100 );
//		final RandomAccessibleInterval< UnsignedShortType > view = Views.rotate( img, 0, 2 );
//		final RandomAccessibleInterval< UnsignedShortType > view = Views.interval(
//				Views.extendMirrorSingle( img ),
//				Intervals.createMinMax( -300, -300, -100, 300, 300, 100 ) );

		final ImagePlus impView = ImageJFunctions.show( view, "view" );
		impView.setDisplayRange( imp.getDisplayRangeMin(), imp.getDisplayRangeMax() );
		impView.updateAndDraw();
	}
}
