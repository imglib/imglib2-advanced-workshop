package algorithm.morphology.completed;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import io.scif.img.IO;
import net.imglib2.Point;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.algorithm.gradient.HessianMatrix;
import net.imglib2.algorithm.gradient.PartialDerivative;
import net.imglib2.algorithm.linalg.eigen.TensorEigenValues;
import net.imglib2.algorithm.localextrema.LocalExtrema;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class WatershedSeedsExample
{

	public static < T extends RealType< T > > List< Point > getLocalMinima( final RandomAccessibleInterval< T > source, final T maximumValue )
	{
		final T extension = maximumValue.createVariable();
		extension.set( maximumValue );
		final IntervalView< T > expanded = Views.expandValue( source, extension, LongStream.generate( () -> 1 ).limit( source.numDimensions() ).toArray() );
		return LocalExtrema.findLocalExtrema(
				expanded,
				new LocalExtrema.MinimumCheck<>( maximumValue ),
				Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() - 1 ) );
	}

	public static void main( final String[] args ) throws IncompatibleTypeException
	{
		// use ImageJ1 this time (for overlays)
		new ImageJ();

		// load example image
		final UnsignedByteType type = new UnsignedByteType();
		final ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		final RandomAccessibleInterval< UnsignedByteType > img = IO.openImgs( "images/blobs.tif", factory, type ).get( 0 ).getImg();
		final long w = img.dimension( 0 );
		final long h = img.dimension( 1 );
		final RandomAccessibleInterval< DoubleType > imgDouble = Converters.convert( img, new RealDoubleConverter<>(), new DoubleType() );

		// smooth image with gauss
		final ArrayImg< DoubleType, DoubleArray > smooth = ArrayImgs.doubles( w, h );
		Gauss3.gauss( 5.0, Views.extendBorder( img ), smooth );

		// calculate gradients
		final ArrayImg< DoubleType, DoubleArray > gradients = ArrayImgs.doubles( w, h, 2 );
		final RandomAccessible< DoubleType > input = Views.extendBorder( smooth );
		for ( int d = 0; d < input.numDimensions(); ++d )
			PartialDerivative.gradientCentralDifference( input, Views.hyperSlice( gradients, 2, d ), d );

		// calculate Hessian matrix and eigenvalues
		final ArrayImg< DoubleType, DoubleArray > hessian = ArrayImgs.doubles( w, h, 3 );
		final ArrayImg< DoubleType, DoubleArray > eigenvals = ArrayImgs.doubles( w, h, 2 );
		HessianMatrix.calculateMatrix( Views.extendBorder( gradients ), hessian );
		TensorEigenValues.calculateEigenValuesSymmetric( hessian, eigenvals );

		// get local minima of first eigenvalue of Hessian matrix
		final List< Point > minima = getLocalMinima( Views.hyperSlice( eigenvals, 2, 0 ), new DoubleType( Double.POSITIVE_INFINITY ) );

		// show all intermediate results of image processing pipeline with
		// overlay
		final Overlay overlay = new Overlay();
		minima.forEach( minimum -> {
			overlay.add( new PointRoi( minimum.getIntPosition( 0 ) + 0.5, minimum.getIntPosition( 1 ) + 0.5 ) );
		} );

		final RandomAccessibleInterval< DoubleType > images = Views.concatenate(
				2,
				Views.addDimension( imgDouble, 0, 0 ),
				Views.addDimension( smooth, 0, 0 ),
				gradients,
				hessian,
				eigenvals );

		final ImagePlus imp = ImageJFunctions.show( images );

		imp.setOverlay( overlay );



	}

}
