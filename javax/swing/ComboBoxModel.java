package javax.swing;

public abstract interface ComboBoxModel<E> extends ListModel<E>
{
  public abstract void setSelectedItem(Object paramObject);

  public abstract Object getSelectedItem();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ComboBoxModel
 * JD-Core Version:    0.6.2
 */