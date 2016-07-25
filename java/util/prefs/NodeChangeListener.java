package java.util.prefs;

import java.util.EventListener;

public abstract interface NodeChangeListener extends EventListener
{
  public abstract void childAdded(NodeChangeEvent paramNodeChangeEvent);

  public abstract void childRemoved(NodeChangeEvent paramNodeChangeEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.NodeChangeListener
 * JD-Core Version:    0.6.2
 */