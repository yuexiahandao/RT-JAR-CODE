package sun.java2d.cmm;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public abstract interface ColorTransform
{
  public static final int Any = -1;
  public static final int In = 1;
  public static final int Out = 2;
  public static final int Gamut = 3;
  public static final int Simulation = 4;

  public abstract int getNumInComponents();

  public abstract int getNumOutComponents();

  public abstract void colorConvert(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2);

  public abstract void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4);

  public abstract void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster);

  public abstract short[] colorConvert(short[] paramArrayOfShort1, short[] paramArrayOfShort2);

  public abstract byte[] colorConvert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.ColorTransform
 * JD-Core Version:    0.6.2
 */