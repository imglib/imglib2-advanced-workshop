package t6converters.completed;

import io.scif.img.IO;
import io.scif.img.ImgIOException;

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
		UnsignedByteType type = new UnsignedByteType();
		ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		RandomAccessibleInterval< UnsignedByteType > img = IO.openImgs( "images/bee-1.tif", factory, type ).get( 0 ).getImg();

		Converter< UnsignedByteType, FloatType > c = new RealFloatConverter<>();
		RandomAccessibleInterval< FloatType > view = Converters.convert( img, c, new FloatType() );
		ImageJFunctions.show( view );

		RandomAccessibleInterval< FloatType > inverted = Converters.convert(
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
