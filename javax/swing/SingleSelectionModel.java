package javax.swing;

import javax.swing.event.ChangeListener;

public abstract interface SingleSelectionModel
{
  public abstract int getSelectedIndex();

  public abstract void setSelectedIndex(int paramInt);

  public abstract void clearSelection();

  public abstract boolean isSelected();

  public abstract void addChangeListener(ChangeListener paramChangeListener);

  public abstract void removeChangeListener(ChangeListener paramChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SingleSelectionModel
 * JD-Core Version:    0.6.2
 */