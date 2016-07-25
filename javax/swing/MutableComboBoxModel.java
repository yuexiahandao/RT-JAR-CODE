package javax.swing;

public abstract interface MutableComboBoxModel<E> extends ComboBoxModel<E>
{
  public abstract void addElement(E paramE);

  public abstract void removeElement(Object paramObject);

  public abstract void insertElementAt(E paramE, int paramInt);

  public abstract void removeElementAt(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.MutableComboBoxModel
 * JD-Core Version:    0.6.2
 */