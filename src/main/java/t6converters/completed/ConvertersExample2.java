package t6converters.completed;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealFloatConverter;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

public class ConvertersExample2
{
	public static void main(final String[] args) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< UnsignedByteType > imgFactory = new ArrayImgFactory<>( new UnsignedByteType() );
		final RandomAccessibleInterval< UnsignedByteType > img = new ImgOpener( ij.context() ).openImgs( "images/bee-1.tif", imgFactory ).get( 0 ).getImg();

		final Converter< UnsignedByteType, FloatType > c = new RealFloatConverter<>();
		final RandomAccessibleInterval< FloatType > view = Converters.convert( img, c, new FloatType() );
		ImageJFunctions.show( view );

		final RandomAccessibleInterval< FloatType > inverted = Converters.convert(
				view,
				new Converter< FloatType, FloatType >() {
					@Override
					public void convert( FloatType input, FloatType output )
					{
						output.set( -input.get() );
					}
				},
				new FloatType() );

		// Alternatively you can implement the Converter as a lambda expression:
//		RandomAccessibleInterval< FloatType > inverted = Converters.convert(
//				view,
//				( input, output ) -> output.set( -input.get() ),
//				new FloatType() );

		ImageJFunctions.show( inverted );
	}
}
