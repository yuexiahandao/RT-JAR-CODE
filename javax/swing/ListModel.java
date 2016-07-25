package javax.swing;

import javax.swing.event.ListDataListener;

public abstract interface ListModel<E>
{
  public abstract int getSize();

  public abstract E getElementAt(int paramInt);

  public abstract void addListDataListener(ListDataListener paramListDataListener);

  public abstract void removeListDataListener(ListDataListener paramListDataListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ListModel
 * JD-Core Version:    0.6.2
 */