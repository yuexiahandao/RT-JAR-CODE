package javax.swing.table;

import javax.swing.event.TableModelListener;

public abstract interface TableModel
{
  public abstract int getRowCount();

  public abstract int getColumnCount();

  public abstract String getColumnName(int paramInt);

  public abstract Class<?> getColumnClass(int paramInt);

  public abstract boolean isCellEditable(int paramInt1, int paramInt2);

  public abstract Object getValueAt(int paramInt1, int paramInt2);

  public abstract void setValueAt(Object paramObject, int paramInt1, int paramInt2);

  public abstract void addTableModelListener(TableModelListener paramTableModelListener);

  public abstract void removeTableModelListener(TableModelListener paramTableModelListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.TableModel
 * JD-Core Version:    0.6.2
 */