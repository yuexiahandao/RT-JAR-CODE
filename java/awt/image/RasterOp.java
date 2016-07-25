package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract interface RasterOp
{
  public abstract WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster);

  public abstract Rectangle2D getBounds2D(Raster paramRaster);

  public abstract WritableRaster createCompatibleDestRaster(Raster paramRaster);

  public abstract Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2);

  public abstract RenderingHints getRenderingHints();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.RasterOp
 * JD-Core Version:    0.6.2
 */