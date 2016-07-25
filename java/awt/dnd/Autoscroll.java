package java.awt.dnd;

import java.awt.Insets;
import java.awt.Point;

public abstract interface Autoscroll
{
  public abstract Insets getAutoscrollInsets();

  public abstract void autoscroll(Point paramPoint);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.Autoscroll
 * JD-Core Version:    0.6.2
 */