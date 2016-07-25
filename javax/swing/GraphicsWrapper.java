package javax.swing;

import java.awt.Graphics;
import java.awt.Rectangle;

abstract interface GraphicsWrapper
{
  public abstract Graphics subGraphics();

  public abstract boolean isClipIntersecting(Rectangle paramRectangle);

  public abstract int getClipX();

  public abstract int getClipY();

  public abstract int getClipWidth();

  public abstract int getClipHeight();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.GraphicsWrapper
 * JD-Core Version:    0.6.2
 */