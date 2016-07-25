package java.awt.peer;

import java.awt.Dialog;

public abstract interface WindowPeer extends ContainerPeer
{
  public abstract void toFront();

  public abstract void toBack();

  public abstract void updateAlwaysOnTopState();

  public abstract void updateFocusableWindowState();

  public abstract void setModalBlocked(Dialog paramDialog, boolean paramBoolean);

  public abstract void updateMinimumSize();

  public abstract void updateIconImages();

  public abstract void setOpacity(float paramFloat);

  public abstract void setOpaque(boolean paramBoolean);

  public abstract void updateWindow();

  public abstract void repositionSecurityWarning();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.WindowPeer
 * JD-Core Version:    0.6.2
 */