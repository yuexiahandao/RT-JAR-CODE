package java.awt.image;

import java.awt.Rectangle;
import java.util.Vector;

public abstract interface RenderedImage
{
  public abstract Vector<RenderedImage> getSources();

  public abstract Object getProperty(String paramString);

  public abstract String[] getPropertyNames();

  public abstract ColorModel getColorModel();

  public abstract SampleModel getSampleModel();

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract int getMinX();

  public abstract int getMinY();

  public abstract int getNumXTiles();

  public abstract int getNumYTiles();

  public abstract int getMinTileX();

  public abstract int getMinTileY();

  public abstract int getTileWidth();

  public abstract int getTileHeight();

  public abstract int getTileGridXOffset();

  public abstract int getTileGridYOffset();

  public abstract Raster getTile(int paramInt1, int paramInt2);

  public abstract Raster getData();

  public abstract Raster getData(Rectangle paramRectangle);

  public abstract WritableRaster copyData(WritableRaster paramWritableRaster);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.RenderedImage
 * JD-Core Version:    0.6.2
 */