package java.awt.event;

import java.util.EventListener;

public abstract interface MouseListener extends EventListener
{
  public abstract void mouseClicked(MouseEvent paramMouseEvent);

  public abstract void mousePressed(MouseEvent paramMouseEvent);

  public abstract void mouseReleased(MouseEvent paramMouseEvent);

  public abstract void mouseEntered(MouseEvent paramMouseEvent);

  public abstract void mouseExited(MouseEvent paramMouseEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.MouseListener
 * JD-Core Version:    0.6.2
 */