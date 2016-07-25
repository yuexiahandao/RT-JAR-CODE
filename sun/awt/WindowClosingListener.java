package sun.awt;

import java.awt.event.WindowEvent;

public abstract interface WindowClosingListener
{
  public abstract RuntimeException windowClosingNotify(WindowEvent paramWindowEvent);

  public abstract RuntimeException windowClosingDelivered(WindowEvent paramWindowEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.WindowClosingListener
 * JD-Core Version:    0.6.2
 */