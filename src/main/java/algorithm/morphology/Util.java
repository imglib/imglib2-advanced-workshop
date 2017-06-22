package algorithm.morphology;

import java.util.function.Predicate;

import io.scif.img.IO;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Util
{

	public static < T extends RealType< T > > RandomAccessibleInterval< T > square( final RandomAccessibleInterval< ? extends RealType< ? > > source, final T type )
	{
		return Converters.convert( source, ( s, t ) -> {
			final double val = s.getRealDouble();
			t.setReal( val * val );
		}, type );
	}

	public static < T extends RealType< T > > RandomAccessibleInterval< T > sqrt( final RandomAccessibleInterval< ? extends RealType< ? > > source, final T type )
	{
		return Converters.convert( source, ( s, t ) -> {
			t.setReal( Math.sqrt( s.getRealDouble() ) );
		}, type );
	}

	public static < T extends Type< T >, U > RandomAccessibleInterval< T > threshold( final RandomAccessibleInterval< U > source, final Predicate< U > predicate, final T yes, final T no )
	{
		return Converters.convert( source, ( s, t ) -> {
			t.set( predicate.test( s ) ? yes : no );
		}, yes.createVariable() );
	}

	public static Img< UnsignedByteType > loadData()
	{

		final UnsignedByteType type = new UnsignedByteType();
		final ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		final Img< UnsignedByteType > img = IO.openImgs( "images/blobs.tif", factory, type ).get( 0 ).getImg();
		return img;
	}

}
