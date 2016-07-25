package javax.swing.plaf;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JList;

public abstract class ListUI extends ComponentUI
{
  public abstract int locationToIndex(JList paramJList, Point paramPoint);

  public abstract Point indexToLocation(JList paramJList, int paramInt);

  public abstract Rectangle getCellBounds(JList paramJList, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.ListUI
 * JD-Core Version:    0.6.2
 */