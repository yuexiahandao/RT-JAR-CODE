package javax.swing;

import java.awt.Dimension;
import java.awt.Rectangle;

public abstract interface Scrollable
{
  public abstract Dimension getPreferredScrollableViewportSize();

  public abstract int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2);

  public abstract int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2);

  public abstract boolean getScrollableTracksViewportWidth();

  public abstract boolean getScrollableTracksViewportHeight();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Scrollable
 * JD-Core Version:    0.6.2
 */