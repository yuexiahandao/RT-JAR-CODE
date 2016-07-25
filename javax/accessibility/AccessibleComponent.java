package javax.accessibility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;

public abstract interface AccessibleComponent
{
  public abstract Color getBackground();

  public abstract void setBackground(Color paramColor);

  public abstract Color getForeground();

  public abstract void setForeground(Color paramColor);

  public abstract Cursor getCursor();

  public abstract void setCursor(Cursor paramCursor);

  public abstract Font getFont();

  public abstract void setFont(Font paramFont);

  public abstract FontMetrics getFontMetrics(Font paramFont);

  public abstract boolean isEnabled();

  public abstract void setEnabled(boolean paramBoolean);

  public abstract boolean isVisible();

  public abstract void setVisible(boolean paramBoolean);

  public abstract boolean isShowing();

  public abstract boolean contains(Point paramPoint);

  public abstract Point getLocationOnScreen();

  public abstract Point getLocation();

  public abstract void setLocation(Point paramPoint);

  public abstract Rectangle getBounds();

  public abstract void setBounds(Rectangle paramRectangle);

  public abstract Dimension getSize();

  public abstract void setSize(Dimension paramDimension);

  public abstract Accessible getAccessibleAt(Point paramPoint);

  public abstract boolean isFocusTraversable();

  public abstract void requestFocus();

  public abstract void addFocusListener(FocusListener paramFocusListener);

  public abstract void removeFocusListener(FocusListener paramFocusListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleComponent
 * JD-Core Version:    0.6.2
 */