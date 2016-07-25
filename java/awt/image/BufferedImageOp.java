package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract interface BufferedImageOp
{
  public abstract BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2);

  public abstract Rectangle2D getBounds2D(BufferedImage paramBufferedImage);

  public abstract BufferedImage createCompatibleDestImage(BufferedImage paramBufferedImage, ColorModel paramColorModel);

  public abstract Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2);

  public abstract RenderingHints getRenderingHints();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.BufferedImageOp
 * JD-Core Version:    0.6.2
 */