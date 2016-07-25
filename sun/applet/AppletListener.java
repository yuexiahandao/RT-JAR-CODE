package sun.applet;

import java.util.EventListener;

public abstract interface AppletListener extends EventListener
{
  public abstract void appletStateChanged(AppletEvent paramAppletEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletListener
 * JD-Core Version:    0.6.2
 */