package javax.swing.event;

import java.util.EventListener;

public abstract interface AncestorListener extends EventListener
{
  public abstract void ancestorAdded(AncestorEvent paramAncestorEvent);

  public abstract void ancestorRemoved(AncestorEvent paramAncestorEvent);

  public abstract void ancestorMoved(AncestorEvent paramAncestorEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.AncestorListener
 * JD-Core Version:    0.6.2
 */