package t5align;

import ij.ImageJ;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class ShowSteepestDescentExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final FloatType type = new FloatType();
		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory< FloatType >();
		final ImgOpener opener = new ImgOpener();
		final ImgPlus< FloatType > template = opener.openImg( "images/template.png", factory, type );

		final Align< FloatType > align = new Align< FloatType >( template, factory );

		new ImageJ();
		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( align.descent, "steepest descent images" );
	}
}
