package javax.swing.tree;

import javax.swing.event.TreeModelListener;

public abstract interface TreeModel
{
  public abstract Object getRoot();

  public abstract Object getChild(Object paramObject, int paramInt);

  public abstract int getChildCount(Object paramObject);

  public abstract boolean isLeaf(Object paramObject);

  public abstract void valueForPathChanged(TreePath paramTreePath, Object paramObject);

  public abstract int getIndexOfChild(Object paramObject1, Object paramObject2);

  public abstract void addTreeModelListener(TreeModelListener paramTreeModelListener);

  public abstract void removeTreeModelListener(TreeModelListener paramTreeModelListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.TreeModel
 * JD-Core Version:    0.6.2
 */