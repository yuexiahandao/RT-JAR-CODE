package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

public abstract interface Border
{
  public abstract void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract Insets getBorderInsets(Component paramComponent);

  public abstract boolean isBorderOpaque();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.Border
 * JD-Core Version:    0.6.2
 */