package java.awt.event;

import java.util.EventListener;

public abstract interface FocusListener extends EventListener
{
  public abstract void focusGained(FocusEvent paramFocusEvent);

  public abstract void focusLost(FocusEvent paramFocusEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.FocusListener
 * JD-Core Version:    0.6.2
 */