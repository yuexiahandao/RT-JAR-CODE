package java.awt.peer;

import java.awt.Component;
import java.awt.Window;

public abstract interface KeyboardFocusManagerPeer
{
  public abstract void setCurrentFocusedWindow(Window paramWindow);

  public abstract Window getCurrentFocusedWindow();

  public abstract void setCurrentFocusOwner(Component paramComponent);

  public abstract Component getCurrentFocusOwner();

  public abstract void clearGlobalFocusOwner(Window paramWindow);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.KeyboardFocusManagerPeer
 * JD-Core Version:    0.6.2
 */