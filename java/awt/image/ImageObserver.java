package java.awt.image;

import java.awt.Image;

public abstract interface ImageObserver
{
  public static final int WIDTH = 1;
  public static final int HEIGHT = 2;
  public static final int PROPERTIES = 4;
  public static final int SOMEBITS = 8;
  public static final int FRAMEBITS = 16;
  public static final int ALLBITS = 32;
  public static final int ERROR = 64;
  public static final int ABORT = 128;

  public abstract boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ImageObserver
 * JD-Core Version:    0.6.2
 */