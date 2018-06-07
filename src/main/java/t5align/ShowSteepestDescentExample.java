package t5align;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class ShowSteepestDescentExample
{
	public static void main( final String[] args ) throws ImgIOException
	{
		final ImageJ ij = new ImageJ();

		final ArrayImgFactory< FloatType > factory = new ArrayImgFactory<>( new FloatType() );
		final Img< FloatType > template = new ImgOpener( ij.context() ).openImgs( "images/template.png", factory ).get( 0 ).getImg();

		final Align< FloatType > align = new Align<>( template, factory );

		ImageJFunctions.show( template, "template" );
		ImageJFunctions.show( align.descent, "steepest descent images" );
	}
}
