package java.awt.peer;

import java.awt.Menu;

public abstract interface MenuBarPeer extends MenuComponentPeer
{
  public abstract void addMenu(Menu paramMenu);

  public abstract void delMenu(int paramInt);

  public abstract void addHelpMenu(Menu paramMenu);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.MenuBarPeer
 * JD-Core Version:    0.6.2
 */