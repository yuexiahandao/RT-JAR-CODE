package javax.swing.event;

import java.util.EventListener;

public abstract interface MenuDragMouseListener extends EventListener
{
  public abstract void menuDragMouseEntered(MenuDragMouseEvent paramMenuDragMouseEvent);

  public abstract void menuDragMouseExited(MenuDragMouseEvent paramMenuDragMouseEvent);

  public abstract void menuDragMouseDragged(MenuDragMouseEvent paramMenuDragMouseEvent);

  public abstract void menuDragMouseReleased(MenuDragMouseEvent paramMenuDragMouseEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.MenuDragMouseListener
 * JD-Core Version:    0.6.2
 */