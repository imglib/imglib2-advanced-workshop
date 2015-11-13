package t6converters;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.ChannelARGBConverter;
import net.imglib2.converter.ChannelARGBConverter.Channel;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class ConverterExample1
{
	public static void main(final String[] args) throws ImgIOException
	{
		UnsignedByteType type = new UnsignedByteType();
		ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory< UnsignedByteType >();
		RandomAccessibleInterval< UnsignedByteType > img = new ImgOpener().openImg( "images/bee-1.tif", factory, type );
		ImageJFunctions.show( img );

		Converter< UnsignedByteType, ARGBType > c1 = new ChannelARGBConverter( Channel.G );
		RandomAccessibleInterval< ARGBType > view1 = Converters.convert( img, c1, new ARGBType() );
		ImageJFunctions.show( view1 );

		// TODO:
		// 1.) Use a RealFloatConverter to Converters.convert() img to FloatType
		//     and display the result
	}
}
