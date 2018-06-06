package t2views;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

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

		// TODO:
		// 1.) Get a XZ slice at Y=100 using Views.hyperslice
		// 2.) Get a rotated view of the input stack
		final RandomAccessibleInterval< UnsignedShortType > view = img;

		final ImagePlus impView = ImageJFunctions.show( view, "view" );
		impView.setDisplayRange( imp.getDisplayRangeMin(), imp.getDisplayRangeMax() );
		impView.updateAndDraw();
	}
}
