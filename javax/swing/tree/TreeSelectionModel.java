package javax.swing.tree;

import java.beans.PropertyChangeListener;
import javax.swing.event.TreeSelectionListener;

public abstract interface TreeSelectionModel
{
  public static final int SINGLE_TREE_SELECTION = 1;
  public static final int CONTIGUOUS_TREE_SELECTION = 2;
  public static final int DISCONTIGUOUS_TREE_SELECTION = 4;

  public abstract void setSelectionMode(int paramInt);

  public abstract int getSelectionMode();

  public abstract void setSelectionPath(TreePath paramTreePath);

  public abstract void setSelectionPaths(TreePath[] paramArrayOfTreePath);

  public abstract void addSelectionPath(TreePath paramTreePath);

  public abstract void addSelectionPaths(TreePath[] paramArrayOfTreePath);

  public abstract void removeSelectionPath(TreePath paramTreePath);

  public abstract void removeSelectionPaths(TreePath[] paramArrayOfTreePath);

  public abstract TreePath getSelectionPath();

  public abstract TreePath[] getSelectionPaths();

  public abstract int getSelectionCount();

  public abstract boolean isPathSelected(TreePath paramTreePath);

  public abstract boolean isSelectionEmpty();

  public abstract void clearSelection();

  public abstract void setRowMapper(RowMapper paramRowMapper);

  public abstract RowMapper getRowMapper();

  public abstract int[] getSelectionRows();

  public abstract int getMinSelectionRow();

  public abstract int getMaxSelectionRow();

  public abstract boolean isRowSelected(int paramInt);

  public abstract void resetRowSelection();

  public abstract int getLeadSelectionRow();

  public abstract TreePath getLeadSelectionPath();

  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);

  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);

  public abstract void addTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener);

  public abstract void removeTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.TreeSelectionModel
 * JD-Core Version:    0.6.2
 */