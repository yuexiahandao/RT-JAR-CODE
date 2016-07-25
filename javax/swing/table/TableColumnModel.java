package javax.swing.table;

import java.util.Enumeration;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;

public abstract interface TableColumnModel
{
  public abstract void addColumn(TableColumn paramTableColumn);

  public abstract void removeColumn(TableColumn paramTableColumn);

  public abstract void moveColumn(int paramInt1, int paramInt2);

  public abstract void setColumnMargin(int paramInt);

  public abstract int getColumnCount();

  public abstract Enumeration<TableColumn> getColumns();

  public abstract int getColumnIndex(Object paramObject);

  public abstract TableColumn getColumn(int paramInt);

  public abstract int getColumnMargin();

  public abstract int getColumnIndexAtX(int paramInt);

  public abstract int getTotalColumnWidth();

  public abstract void setColumnSelectionAllowed(boolean paramBoolean);

  public abstract boolean getColumnSelectionAllowed();

  public abstract int[] getSelectedColumns();

  public abstract int getSelectedColumnCount();

  public abstract void setSelectionModel(ListSelectionModel paramListSelectionModel);

  public abstract ListSelectionModel getSelectionModel();

  public abstract void addColumnModelListener(TableColumnModelListener paramTableColumnModelListener);

  public abstract void removeColumnModelListener(TableColumnModelListener paramTableColumnModelListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.TableColumnModel
 * JD-Core Version:    0.6.2
 */