package java.awt;

import java.awt.image.ColorModel;
import java.awt.image.Raster;

public abstract interface PaintContext
{
  public abstract void dispose();

  public abstract ColorModel getColorModel();

  public abstract Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PaintContext
 * JD-Core Version:    0.6.2
 */