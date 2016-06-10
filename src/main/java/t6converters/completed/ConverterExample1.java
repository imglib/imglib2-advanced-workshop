package t6converters.completed;

import io.scif.img.IO;
import io.scif.img.ImgIOException;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.ChannelARGBConverter;
import net.imglib2.converter.ChannelARGBConverter.Channel;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealFloatConverter;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

public class ConverterExample1
{
	public static void main(final String[] args) throws ImgIOException
	{
		UnsignedByteType type = new UnsignedByteType();
		ArrayImgFactory< UnsignedByteType > factory = new ArrayImgFactory<>();
		RandomAccessibleInterval< UnsignedByteType > img = IO.openImgs( "images/bee-1.tif", factory, type ).get( 0 ).getImg();
		ImageJFunctions.show( img );

		Converter< UnsignedByteType, ARGBType > c1 = new ChannelARGBConverter( Channel.G );
		RandomAccessibleInterval< ARGBType > view1 = Converters.convert( img, c1, new ARGBType() );
		ImageJFunctions.show( view1 );

		Converter< UnsignedByteType, FloatType > c2 = new RealFloatConverter<>();
		RandomAccessibleInterval< FloatType > view2 = Converters.convert( img, c2, new FloatType() );
		ImageJFunctions.show( view2 );
	}
}
