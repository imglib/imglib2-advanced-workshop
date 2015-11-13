package t2views;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ViewsExample2
{
	public static void main( final String[] args )
	{
		new ImageJ();
		IJ.run("T1 Head (2.4M, 16-bits)");
		ImagePlus imp = IJ.getImage();
		Img< UnsignedShortType > img = ImageJFunctions.wrapShort( imp );

		// TODO:
		// 1.) Get a XZ slice at Y=100 using Views.hyperslice
		// 2.) Get a rotated view of the input stack
		RandomAccessibleInterval< UnsignedShortType > view = img;

		ImagePlus impView = ImageJFunctions.show( view, "view" );
		impView.setDisplayRange( imp.getDisplayRangeMin(), imp.getDisplayRangeMax() );
		impView.updateAndDraw();
	}
}
