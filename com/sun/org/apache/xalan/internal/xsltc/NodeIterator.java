package com.sun.org.apache.xalan.internal.xsltc;

public abstract interface NodeIterator extends Cloneable
{
  public static final int END = -1;

  public abstract int next();

  public abstract NodeIterator reset();

  public abstract int getLast();

  public abstract int getPosition();

  public abstract void setMark();

  public abstract void gotoMark();

  public abstract NodeIterator setStartNode(int paramInt);

  public abstract boolean isReverse();

  public abstract NodeIterator cloneIterator();

  public abstract void setRestartable(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.NodeIterator
 * JD-Core Version:    0.6.2
 */