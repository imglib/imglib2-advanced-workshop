package t10morphology.complete;

import java.util.List;

import ij.ImageJ;
import io.scif.img.IO;
import net.imglib2.algorithm.binary.Thresholder;
import net.imglib2.algorithm.morphology.Closing;
import net.imglib2.algorithm.morphology.Dilation;
import net.imglib2.algorithm.morphology.Erosion;
import net.imglib2.algorithm.morphology.Opening;
import net.imglib2.algorithm.morphology.StructuringElements;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class MorphologyExamples
{

	public static void main( String[] args )
	{
		/*
		 * ImageLib2 has support for basic gray morphology operations, in the
		 * sense brought by P. Soille and colleagues. The syntax and
		 * capabilities are strongly influenced by the morphology package in
		 * MATLAB, if you now it.
		 */

		// Load and display a grayscale image.
		ImageJ.main( args );
		UnsignedByteType type = new UnsignedByteType();
		ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		Img< UnsignedByteType > img = IO.openImgs( "images/blobs.tif", factory, type ).get( 0 ).getImg();
		ImageJFunctions.show( img, "source" );

		// Make a binary image.
		UnsignedByteType threshold = img.firstElement().createVariable();
		threshold.set( 127 );
		Img< BitType > mask = Thresholder.threshold( img, threshold, true, 1 );
		ImageJFunctions.show( mask, "B&W" );

		/*
		 * A. General syntax.
		 * 
		 * To run a morphological operation, you need: 1) A structuring element
		 * to specify the shape with which you will probe the image. 2) A
		 * morphological operation to apply to the image. Both can be accessed
		 * via static classes.
		 * 
		 */

		/*
		 * 7x7 Rectangle.
		 * 
		 * Note that the structuring element is an imglib2 Shape or a list of
		 * Shape. We will come back to that later.
		 */
		List< Shape > strel = StructuringElements.rectangle( new int[] { 3, 3 } );
		// Erosion operation.
		Img< BitType > eroded = Erosion.erode( mask, strel, 1 );
		ImageJFunctions.show( eroded, "eroded" );

		/*
		 * B. Boundaries variant.
		 * 
		 * Each operation exists in several flavor that deal with boundaries.
		 * The basic `erode()` operation we just used return an image of the
		 * same size that of the input. The `erodeFull()` version increases the
		 * size of the returned image to process all operations without handling
		 * the boundaries.
		 */
		Img< BitType > erodeFull = Erosion.erodeFull( mask, strel, 1 );
		ImageJFunctions.show( erodeFull, "full-eroded" );
		System.out.println( "Size of the source image: " + Util.printInterval( mask ) );
		System.out.println( "Size of the eroded image: " + Util.printInterval( eroded ) );
		System.out.println( "Size of the full-eroded image: " + Util.printInterval( erodeFull ) );
		
		/*
		 * There is also a version for in-place calculations, and a version
		 * where the user provides the target image.
		 */

		/*
		 * C. Supported operations.
		 * 
		 * A subset of all morphological operations is supported.
		 */

		Img< BitType > dilated = Dilation.dilate( mask, strel, 1 );
		Img< BitType > opened = Opening.open( mask, strel, 1 );
		Img< BitType > closed = Closing.close( mask, strel, 1 );

	}

}
