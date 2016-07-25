package java.awt.image;

import java.util.Hashtable;

public abstract interface ImageConsumer
{
  public static final int RANDOMPIXELORDER = 1;
  public static final int TOPDOWNLEFTRIGHT = 2;
  public static final int COMPLETESCANLINES = 4;
  public static final int SINGLEPASS = 8;
  public static final int SINGLEFRAME = 16;
  public static final int IMAGEERROR = 1;
  public static final int SINGLEFRAMEDONE = 2;
  public static final int STATICIMAGEDONE = 3;
  public static final int IMAGEABORTED = 4;

  public abstract void setDimensions(int paramInt1, int paramInt2);

  public abstract void setProperties(Hashtable<?, ?> paramHashtable);

  public abstract void setColorModel(ColorModel paramColorModel);

  public abstract void setHints(int paramInt);

  public abstract void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6);

  public abstract void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6);

  public abstract void imageComplete(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ImageConsumer
 * JD-Core Version:    0.6.2
 */