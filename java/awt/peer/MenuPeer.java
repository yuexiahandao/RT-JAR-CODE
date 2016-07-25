package java.awt.peer;

import java.awt.MenuItem;

public abstract interface MenuPeer extends MenuItemPeer
{
  public abstract void addSeparator();

  public abstract void addItem(MenuItem paramMenuItem);

  public abstract void delItem(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.MenuPeer
 * JD-Core Version:    0.6.2
 */