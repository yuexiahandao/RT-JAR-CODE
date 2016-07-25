package javax.swing.tree;

public abstract interface MutableTreeNode extends TreeNode
{
  public abstract void insert(MutableTreeNode paramMutableTreeNode, int paramInt);

  public abstract void remove(int paramInt);

  public abstract void remove(MutableTreeNode paramMutableTreeNode);

  public abstract void setUserObject(Object paramObject);

  public abstract void removeFromParent();

  public abstract void setParent(MutableTreeNode paramMutableTreeNode);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.MutableTreeNode
 * JD-Core Version:    0.6.2
 */