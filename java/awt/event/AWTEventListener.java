package java.awt.event;

import java.awt.AWTEvent;
import java.util.EventListener;

public abstract interface AWTEventListener extends EventListener
{
  public abstract void eventDispatched(AWTEvent paramAWTEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.AWTEventListener
 * JD-Core Version:    0.6.2
 */