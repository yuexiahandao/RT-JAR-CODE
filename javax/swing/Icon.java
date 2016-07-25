package javax.swing;

import java.awt.Component;
import java.awt.Graphics;

public abstract interface Icon
{
  public abstract void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2);

  public abstract int getIconWidth();

  public abstract int getIconHeight();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Icon
 * JD-Core Version:    0.6.2
 */