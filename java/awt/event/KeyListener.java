package java.awt.event;

import java.util.EventListener;

public abstract interface KeyListener extends EventListener
{
  public abstract void keyTyped(KeyEvent paramKeyEvent);

  public abstract void keyPressed(KeyEvent paramKeyEvent);

  public abstract void keyReleased(KeyEvent paramKeyEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.KeyListener
 * JD-Core Version:    0.6.2
 */