package algorithm.morphology;

import io.scif.img.IO;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.distance.DistanceTransform;
import net.imglib2.algorithm.morphology.distance.DistanceTransform.DISTANCE_TYPE;
import net.imglib2.converter.Converters;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;

public class DistanceTransformExample
{

	// packages I would like to mention:
	// DoG
	// FloodFill
	// Gauss3
	// PartialDerivative
	// HessianMatrix and Eigenvalues
	// LocalExtrema
	// DistanceTransform
	// Neighborhood

	public static void main( final String[] args )
	{
		// Load and display a grayscale image.
		final ImageJ ij = new ImageJ();
		ij.launch( args );
		final UnsignedByteType type = new UnsignedByteType();
		final ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		final RandomAccessibleInterval< UnsignedByteType > img = IO.openImgs( "images/blobs.tif", factory, type ).get( 0 ).getImg();
		ij.ui().show( "image", img );

		final RandomAccessibleInterval< DoubleType > squared = Util.square( img, new DoubleType() );

		// gray scale distance transform D for pixel p:
		// D(p) = min_q f(q) + d(p,q)

		// euclidian distance
		final RandomAccessibleInterval< DoubleType > euclidian = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( img ) );
		final double lambda = 10.0;
		DistanceTransform.transform( squared, euclidian, DISTANCE_TYPE.EUCLIDIAN, lambda );
		ij.ui().show( "euclidian " + lambda, Util.sqrt( euclidian, new DoubleType() ) );

		final int byteMaxSquared = 255 * 255;
		final RandomAccessibleInterval< DoubleType > euclidianOnInverse = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( img ) );
		final double lambdaInverse = 1.0;
		DistanceTransform.transform( Converters.convert( squared, ( s, t ) -> {
			t.set( byteMaxSquared - s.get() );
		}, new DoubleType() ), euclidianOnInverse, DISTANCE_TYPE.EUCLIDIAN, lambdaInverse );
		ij.ui().show( "euclidian on inverrse " + lambdaInverse, Util.sqrt( euclidianOnInverse, new DoubleType() ) );

		// l1 distance
		final RandomAccessibleInterval< DoubleType > l1 = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( img ) );
		final double lambdaL1 = 1.0;
		DistanceTransform.transform( img, l1, DISTANCE_TYPE.L1, lambdaL1 );
		ij.ui().show( "l1 " + lambdaL1, l1 );

		final int byteMax = 255;
		final RandomAccessibleInterval< DoubleType > l1OnInverse = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( img ) );
		final double lambdaL1Inverse = 1.0;
		DistanceTransform.transform( Converters.convert( img, ( s, t ) -> {
			t.set( byteMax - s.get() );
		}, new DoubleType() ), l1OnInverse, DISTANCE_TYPE.EUCLIDIAN, lambdaL1Inverse );
		ij.ui().show( "l1 on inverse " + lambdaL1Inverse, Util.sqrt( l1OnInverse, new DoubleType() ) );

		// mimic distance transform on binary images by setting
		// f(q) = infinity, if q on mask
		//        0,        otherwise

		final int threshold = 127;
		// actually found a bug here in the DistanceTransform code as we cannot
		// use Double.POSITIVE_INFINITY (DistanceTransform.transform crashes
		// with ArrayIndexOutOfBounds
		final RandomAccessibleInterval< DoubleType > thresholded = Util.threshold( img, t -> t.getInteger() > 127, new DoubleType( 1e20 ), new DoubleType( 0.0 ) );
		ij.ui().show( "thresholded at " + threshold, thresholded );

		// euclidian
		final RandomAccessibleInterval< DoubleType > binaryDistanceTransform = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( thresholded ) );
		DistanceTransform.transform( thresholded, binaryDistanceTransform, DISTANCE_TYPE.EUCLIDIAN, 1.0 );
		ij.ui().show( "binary distance transform euclidian", Util.sqrt( binaryDistanceTransform, new DoubleType() ) );

		// l1
		final RandomAccessibleInterval< DoubleType > binaryDistanceTransformL1 = ArrayImgs.doubles( Intervals.dimensionsAsLongArray( thresholded ) );
		DistanceTransform.transform( thresholded, binaryDistanceTransformL1, DISTANCE_TYPE.L1, 1.0 );
		ij.ui().show( "binary distance transform l1", binaryDistanceTransformL1 );

	}

}
