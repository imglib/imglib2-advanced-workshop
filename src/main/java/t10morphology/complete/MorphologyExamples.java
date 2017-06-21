package t10morphology.complete;

import java.util.Arrays;
import java.util.List;

import ij.ImageJ;
import io.scif.img.IO;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.binary.Thresholder;
import net.imglib2.algorithm.morphology.BlackTopHat;
import net.imglib2.algorithm.morphology.Closing;
import net.imglib2.algorithm.morphology.Dilation;
import net.imglib2.algorithm.morphology.Erosion;
import net.imglib2.algorithm.morphology.Opening;
import net.imglib2.algorithm.morphology.StructuringElements;
import net.imglib2.algorithm.morphology.TopHat;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

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
		 * to specify the shape with which you will probe the image. Here they
		 * are abbreviated 'strel' sometimes. 2) A morphological operation to
		 * apply to the image. Both can be accessed via static classes.
		 * 
		 */

		int dimensionality = img.numDimensions(); // 2D
		int radius = 3;
		
		
		/*
		 * 7x7 Square.
		 * 
		 * Note that the structuring element is an imglib2 Shape or a list of
		 * Shape. We will come back to that later.
		 */
		List< Shape > strel = StructuringElements.square( radius, dimensionality );
		// Erosion operation.
		Img< BitType > eroded = Erosion.erode( mask, strel, 1 );
		ImageJFunctions.show( eroded, "eroded" );

		/*
		 * B. Boundaries variant.
		 * 
		 * Each operation exists in several flavor that deal with boundaries.
		 * The basic `erode()` operation we just used return an image of the
		 * same size that of the input.
		 * 
		 * The `erodeFull()` version increases the size of the returned image to
		 * process all operations without handling the boundaries.
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
		 * C. Grayscale morphology.
		 * 
		 * Grayscale types are supported indifferently.
		 */
		
		Img< UnsignedByteType > erodedGrayscale = Erosion.erode( img, strel, 1 );
		ImageJFunctions.show( erodedGrayscale, "Grayscale erosion" );

		/*
		 * D. Supported operations.
		 * 
		 * A subset of all morphological operations is supported.
		 */

		Img< UnsignedByteType > dilated = Dilation.dilate( img, strel, 1 );
		Img< UnsignedByteType > opened = Opening.open( img, strel, 1 );
		Img< UnsignedByteType > closed = Closing.close( img, strel, 1 );
		Img< UnsignedByteType > topHat = TopHat.topHat( img, strel, 1 );
		Img< UnsignedByteType > blackTopHat = BlackTopHat.blackTopHat( img, strel, 1 );

		// Concatenate them together.
		@SuppressWarnings( "unchecked" )
		List< Img< UnsignedByteType > > row1 = Arrays.asList( new Img[] { erodedGrayscale, dilated, topHat } );
		RandomAccessibleInterval< UnsignedByteType > irc1 = Views.concatenate( 1, row1 );
		@SuppressWarnings( "unchecked" )
		List< Img< UnsignedByteType > > row2 = Arrays.asList( new Img[] { opened, closed, blackTopHat } );
		RandomAccessibleInterval< UnsignedByteType > irc2 = Views.concatenate( 1, row2 );
		@SuppressWarnings( "unchecked" )
		List< RandomAccessibleInterval< UnsignedByteType > > montage = Arrays.asList( new RandomAccessibleInterval[] { irc1, irc2 } );
		RandomAccessibleInterval< UnsignedByteType > concatenated = Views.concatenate( 0, montage );
		ImageJFunctions.show( concatenated, "erosion, dilation, opening, closing, tophat, black tophat" );

		/*
		 * E. Structuring elements.
		 * 
		 * Structuring elements can be any class that implements the Shape
		 * interface. The later is not immediately trivial to grasp, you
		 * probably want to check some example implementations.
		 * 
		 * Using the Shape interface means that we can benefit from
		 * interoperability with many frameworks in ImgLib2 that provide these
		 * implementations. For instance the Labeling framework demonstrated in
		 * another module.
		 * 
		 * It also means that the morphological operations we have are defined
		 * for any grayscale source image (and more, see below), but using the
		 * Shape means that the operations are defined for structuring elements
		 * with on/off pixels only.
		 *
		 * The StructuringElement can be used to provide classical structuring
		 * elements, again adapted from the MATLAB morphology package. But any
		 * Shape implementation will work.
		 */
		
		List< Shape > disk = StructuringElements.disk( radius, dimensionality );
		List< Shape > rectangle = StructuringElements.rectangle( new int[] { radius, 0 } );
		List< Shape > diamond = StructuringElements.diamond( radius, dimensionality );
		Shape line = StructuringElements.periodicLine( radius, new int[] { 1, 1 } );

		Img< UnsignedByteType > dilateDisk = Dilation.dilate( img, disk, 1 );
		Img< UnsignedByteType > dilateRectangle = Dilation.dilate( img, rectangle, 1 );
		Img< UnsignedByteType > dilateDiamond = Dilation.dilate( img, diamond, 1 );
		Img< UnsignedByteType > dilateLine = Dilation.dilate( img, line, 1 );

		@SuppressWarnings( "unchecked" )
		List< Img< UnsignedByteType > > rowb1 = Arrays.asList( new Img[] { dilated, dilateDisk, dilateRectangle } );
		RandomAccessibleInterval< UnsignedByteType > ircb1 = Views.concatenate( 1, rowb1 );
		@SuppressWarnings( "unchecked" )
		List< Img< UnsignedByteType > > rowb2 = Arrays.asList( new Img[] { dilateDiamond, dilateLine, img } );
		RandomAccessibleInterval< UnsignedByteType > ircb2 = Views.concatenate( 1, rowb2 );
		@SuppressWarnings( "unchecked" )
		List< RandomAccessibleInterval< UnsignedByteType > > montageb = Arrays.asList( new RandomAccessibleInterval[] { ircb1, ircb2 } );
		RandomAccessibleInterval< UnsignedByteType > concatenatedb = Views.concatenate( 0, montageb );
		ImageJFunctions.show( concatenatedb, "Structuring elements: square, disk, rectangle, diamond, line and source image" );

		/*
		 * F. Structuring elements decomposition.
		 * 
		 * There are clever ways to accelerate the morphological operations. One
		 * of them is structuring-element decomposition. It is based on the fact
		 * that an operation with a large structuring element can be decomposed
		 * in several successive operations on smaller structuring-elements.
		 * 
		 * For instance, a dilation operation on a 7x7 square strel will involve
		 * 49 operations on each pixel of the source image. The same result can
		 * be obtained with a first dilation with a horizontal line of 7 pixels
		 * followed by a second dilation of the result with a vertical line of 7
		 * pixels. In total the same results take only 14 operations, a good
		 * source of speedup.
		 * 
		 * This is why the StructuringElements class and the the morphological
		 * operation classes generates and respectively accept a List< Shape >,
		 * corresponding to the decomposition. StructuringElements methods have
		 * some flags to force or forbid optimization.
		 * 
		 * For instance:
		 */

		boolean optimize = false;
		List< Shape > nonOptimized = StructuringElements.square( radius, dimensionality, optimize );
		System.out.println( "\n\nNon-optimized strel is made of " + nonOptimized.size() + " elements:" );
		for ( Shape shape : nonOptimized )
			System.out.println( " - " + shape );

		optimize = true;
		List< Shape > optimizedStrel = StructuringElements.square( radius, dimensionality, optimize );
		System.out.println( "\n\nOptimized strel is made of " + optimizedStrel.size() + " elements:" );
		for ( Shape shape : optimizedStrel )
			System.out.println( " - " + shape );

		/*
		 * Details of decomposition and benchmarks are available here:
		 * 
		 * http://imagej.net/Imglib2_Morphological_Operations
		 */

		/*
		 * G. Morphological operations on custom types.
		 * 
		 * Morphological operations are defined on types that have very little
		 * requirement. The data does not have to be made of numerical pixels at
		 * all. Mathematically, they are defined on partially ordered sets
		 * (complete lattices, see for instance
		 * https://en.wikipedia.org/wiki/Dilation_(morphology)#
		 * Dilation_on_complete_lattices).
		 * 
		 * In Imglib2, we require a little bit more than that. The data type you
		 * can use with morphological operations needs to be comparable. In
		 * practice, it must extends Type and Comparable: `T extends Type< T > &
		 * Comparable< T >`. With this, it is perfectly possible to dilate an
		 * image of strings by a 3x3 square strel:
		 */
	}

}
