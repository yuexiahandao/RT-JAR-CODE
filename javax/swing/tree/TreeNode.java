package javax.swing.tree;

import java.util.Enumeration;

public abstract interface TreeNode
{
  public abstract TreeNode getChildAt(int paramInt);

  public abstract int getChildCount();

  public abstract TreeNode getParent();

  public abstract int getIndex(TreeNode paramTreeNode);

  public abstract boolean getAllowsChildren();

  public abstract boolean isLeaf();

  public abstract Enumeration children();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.TreeNode
 * JD-Core Version:    0.6.2
 */