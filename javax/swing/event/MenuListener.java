package javax.swing.event;

import java.util.EventListener;

public abstract interface MenuListener extends EventListener
{
  public abstract void menuSelected(MenuEvent paramMenuEvent);

  public abstract void menuDeselected(MenuEvent paramMenuEvent);

  public abstract void menuCanceled(MenuEvent paramMenuEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.MenuListener
 * JD-Core Version:    0.6.2
 */