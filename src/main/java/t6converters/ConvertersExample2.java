package t6converters;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

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
		ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory< UnsignedByteType >();
		RandomAccessibleInterval< UnsignedByteType > img = new ImgOpener().openImg( "images/bee-1.tif", factory, type );

		Converter< UnsignedByteType, FloatType > c = new RealFloatConverter< UnsignedByteType >();
		RandomAccessibleInterval< FloatType > view = Converters.convert( img, c, new FloatType() );
		ImageJFunctions.show( view );

		RandomAccessibleInterval< FloatType > inverted = Converters.convert(
				view,
				new Converter< FloatType, FloatType >() {
					// TODO:
					// 1.) Implement the convert method such that the output
					//     is set to the inverted input.
					@Override
					public void convert( FloatType input, FloatType output )
					{
					}
				},
				new FloatType() );
		ImageJFunctions.show( inverted );
	}
}
