package java.awt.event;

import java.util.EventListener;

public abstract interface ContainerListener extends EventListener
{
  public abstract void componentAdded(ContainerEvent paramContainerEvent);

  public abstract void componentRemoved(ContainerEvent paramContainerEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.ContainerListener
 * JD-Core Version:    0.6.2
 */