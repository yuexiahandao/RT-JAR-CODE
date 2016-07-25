package com.sun.org.apache.xml.internal.dtm;

public abstract interface DTMAxisIterator extends Cloneable
{
  public static final int END = -1;

  public abstract int next();

  public abstract DTMAxisIterator reset();

  public abstract int getLast();

  public abstract int getPosition();

  public abstract void setMark();

  public abstract void gotoMark();

  public abstract DTMAxisIterator setStartNode(int paramInt);

  public abstract int getStartNode();

  public abstract boolean isReverse();

  public abstract DTMAxisIterator cloneIterator();

  public abstract void setRestartable(boolean paramBoolean);

  public abstract int getNodeByPosition(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
 * JD-Core Version:    0.6.2
 */