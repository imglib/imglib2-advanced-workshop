package t5align;

import ij.ImageJ;
import io.scif.img.IO;
import io.scif.img.ImgIOException;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class ShowSteepestDescentExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>();
		final Img< FloatType > template = IO.openImgs( "images/template.png", factory, type ).get( 0 ).getImg();

		final Align< FloatType > align = new Align<>( template, factory );

		new ImageJ();
		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( align.descent, "steepest descent images" );
	}
}
