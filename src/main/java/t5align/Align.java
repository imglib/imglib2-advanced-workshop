package t5align;

import static t3gradient.completed.GradientExample2.gradients;
import static t4realviews.completed.RealViewsExample3.computeDifference;

import Jama.Matrix;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.iterator.LocalizingIntervalIterator;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.LinAlgHelpers;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class Align< T extends RealType<T> >
{
	final RandomAccessibleInterval< T > template;

	final WarpFunction warpFunction;

	/**
	 * Number of dimensions in the image
	 */
	final int n;

	/**
	 * Number of parameters of the warp function
	 */
	final int numParameters;

	final AffineTransform currentTransform;

	/**
	 * Image of <em>n+1</em> dimensions to store the steepest descent images of
	 * the template image at the identity warp. Dimension <em>n</em> is used to
	 * index the parameters of the warp function. For example, the partial
	 * derivative of the template image intensity by parameter 2 of the warp
	 * function at pixel <em>(x,y)</em> is stored at position <em>(x,y,1)</em>.
	 */
	final Img< T > descent;

	/**
	 * Inverse of the Hessian matrix.
	 */
	double[][] Hinv;

	/**
	 * The error image for the last iteration shows the difference between the
	 * template and the warped (with {@link #currentTransform}) image.
	 */
	final Img< T > error;

	public Align( final RandomAccessibleInterval< T > template, final ImgFactory< T > factory )
	{
		this.template = template;
		final T type = Util.getTypeFromInterval( template );

		n = template.numDimensions();
		warpFunction = new AffineWarp( n );
		numParameters = warpFunction.numParameters();
		currentTransform = new AffineTransform( n );

		final long[] dim = new long[ n + 1 ];
		for ( int d = 0; d < n; ++d )
			dim[ d ] = template.dimension( d );
		dim[ n ] = n;
		final Img< T > gradients = factory.create( dim, type );
		gradients( Views.extendBorder( template ), gradients );

		dim[ n ] = numParameters;
		descent = factory.create( dim, type );
		computeSteepestDescents( gradients, warpFunction, descent );

		Hinv = computeInverseHessian( descent );

		error = factory.create( template, type );
	}

	/**
	 * Compute the steepest descent images of the template at the identity warp.
	 * Each steepest descent image comprises the partial derivatives of template
	 * intensities with respect to one parameter of the warp function.
	 *
	 * The result is stored in the <em>n+1</em> dimensional {@code target}
	 * image. Dimension <em>n</em> is used to index the partial derivative. For
	 * example, the partial derivative by the second parameter of the warp
	 * function is stored in slice <em>n=1</em>.
	 *
	 * @param gradients
	 *            n+1 dimensional image of partial derivatives of the template.
	 *            Dimension n is used to index the partial derivative. For
	 *            example, the partial derivative by Y is stored in slice n=1.
	 * @param warpFunction
	 *            The warp function to be applied to the template. The partial
	 *            derivatives of template intensities with respect to the
	 *            parameters of this warp function are computed.
	 * @param target
	 *            Image of <em>n+1</em> dimensions to store the steepest descent
	 *            Dimension <em>n</em> is used to index the parameters of the
	 *            warp function. For example, the partial derivative of the
	 *            template image intensity by parameter 2 of the warp function
	 *            at pixel <em>(x,y)</em> is stored at position <em>(x,y,1)</em>.
	 */
	public static < T extends NumericType< T > > void computeSteepestDescents( final RandomAccessibleInterval< T > gradients, final WarpFunction warpFunction, final RandomAccessibleInterval< T > target )
	{
		final int n = gradients.numDimensions() - 1;
		final int numParameters = warpFunction.numParameters();
		final T tmp = Util.getTypeFromInterval( gradients ).createVariable();
		for ( int p = 0; p < numParameters; ++p )
		{
			for ( int d = 0; d < n; ++d )
			{
				final Cursor< T > gd = Views.flatIterable( Views.hyperSlice( gradients, n, d ) ).localizingCursor();
				for ( final T t : Views.flatIterable( Views.hyperSlice( target, n, p ) ) )
				{
					tmp.set( gd.next() );
					tmp.mul( warpFunction.partial( gd, d, p ) );
					t.add( tmp );
				}
			}
		}
	}

	/**
	 * Compute the inverse Hessian matrix from the the steepest descent images.
	 */
	public static < T extends RealType< T > > double[][] computeInverseHessian( final RandomAccessibleInterval< T > descent )
	{
		final int n = descent.numDimensions() - 1;
		final int numParameters = ( int ) descent.dimension( n );
		final long[] dim = new long[ n + 1 ];
		descent.dimensions( dim );
		dim[ n ] = 1;
		final LocalizingIntervalIterator pos = new LocalizingIntervalIterator( dim );
		final RandomAccess< T > r = descent.randomAccess();
		final double[] deriv = new double[ numParameters ];
		final double[][] H = new double[ numParameters ][ numParameters ];
		while( pos.hasNext() )
		{
			pos.fwd();
			r.setPosition( pos );
			for ( int p = 0; p < numParameters; ++p )
			{
				deriv[ p ] = r.get().getRealDouble();
				r.fwd( n );
			}
			for ( int i = 0; i < numParameters; ++i )
				for ( int j = 0; j < numParameters; ++j )
					H[ i ][ j ] += deriv[ i ] * deriv[ j ];
		}
		return new Matrix( H ).inverse().getArray();
	}

	/**
	 * Computed and return the affine transform that aligns image to template.
	 */
	public AffineTransform align( final RandomAccessibleInterval< T > image, final int maxIterations, final double minParameterChange )
	{
		currentTransform.set( new AffineTransform( n ) );
		int i = 0;
		while( i < maxIterations )
		{
			++i;
			if( alignStep( image ) < minParameterChange )
				break;
		}
		System.out.println( "computed " + i + " iterations." );
		return currentTransform;
	}

	double alignStep( final RandomAccessibleInterval< T > image )
	{
		// compute error image = warped image - template
		computeDifference( Views.extendBorder( image ), currentTransform, template, error );

		// compute transform parameter update
		final double[] gradient = new double[ numParameters ];
		for ( int p = 0; p < numParameters; ++p )
		{
			final Cursor< T > err = Views.flatIterable( error ).cursor();
			for ( final T t : Views.flatIterable( Views.hyperSlice( descent, n, p ) ) )
				gradient[ p ] += t.getRealDouble() * err.next().getRealDouble();
		}
		final double[] dp = new double[ numParameters ];
		LinAlgHelpers.mult( Hinv, gradient, dp );

		// udpate transform
		currentTransform.preConcatenate( warpFunction.getAffine( dp ) );

		// return norm of parameter update vector
		return LinAlgHelpers.length( dp );
	}
}
