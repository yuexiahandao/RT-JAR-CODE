package java.awt.event;

import java.util.EventListener;

public abstract interface WindowFocusListener extends EventListener
{
  public abstract void windowGainedFocus(WindowEvent paramWindowEvent);

  public abstract void windowLostFocus(WindowEvent paramWindowEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.WindowFocusListener
 * JD-Core Version:    0.6.2
 */