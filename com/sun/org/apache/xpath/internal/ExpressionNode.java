package com.sun.org.apache.xpath.internal;

import javax.xml.transform.SourceLocator;

public abstract interface ExpressionNode extends SourceLocator
{
  public abstract void exprSetParent(ExpressionNode paramExpressionNode);

  public abstract ExpressionNode exprGetParent();

  public abstract void exprAddChild(ExpressionNode paramExpressionNode, int paramInt);

  public abstract ExpressionNode exprGetChild(int paramInt);

  public abstract int exprGetNumChildren();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.ExpressionNode
 * JD-Core Version:    0.6.2
 */