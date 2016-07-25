package javax.swing.event;

import java.util.EventListener;

public abstract interface TreeModelListener extends EventListener
{
  public abstract void treeNodesChanged(TreeModelEvent paramTreeModelEvent);

  public abstract void treeNodesInserted(TreeModelEvent paramTreeModelEvent);

  public abstract void treeNodesRemoved(TreeModelEvent paramTreeModelEvent);

  public abstract void treeStructureChanged(TreeModelEvent paramTreeModelEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.TreeModelListener
 * JD-Core Version:    0.6.2
 */