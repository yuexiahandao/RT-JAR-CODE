package javax.swing.event;

import java.util.EventListener;

public abstract interface MenuKeyListener extends EventListener
{
  public abstract void menuKeyTyped(MenuKeyEvent paramMenuKeyEvent);

  public abstract void menuKeyPressed(MenuKeyEvent paramMenuKeyEvent);

  public abstract void menuKeyReleased(MenuKeyEvent paramMenuKeyEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.MenuKeyListener
 * JD-Core Version:    0.6.2
 */