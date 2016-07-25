package javax.swing;

import java.awt.Component;

public abstract interface ListCellRenderer<E>
{
  public abstract Component getListCellRendererComponent(JList<? extends E> paramJList, E paramE, int paramInt, boolean paramBoolean1, boolean paramBoolean2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ListCellRenderer
 * JD-Core Version:    0.6.2
 */