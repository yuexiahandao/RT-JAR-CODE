package javax.swing.event;

import java.util.EventListener;

public abstract interface ListDataListener extends EventListener
{
  public abstract void intervalAdded(ListDataEvent paramListDataEvent);

  public abstract void intervalRemoved(ListDataEvent paramListDataEvent);

  public abstract void contentsChanged(ListDataEvent paramListDataEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.ListDataListener
 * JD-Core Version:    0.6.2
 */