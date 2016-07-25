package com.sun.org.apache.xalan.internal.xsltc.compiler;

public abstract interface NodeTest
{
  public static final int TEXT = 3;
  public static final int COMMENT = 8;
  public static final int PI = 7;
  public static final int ROOT = 9;
  public static final int ELEMENT = 1;
  public static final int ATTRIBUTE = 2;
  public static final int GTYPE = 14;
  public static final int ANODE = -1;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NodeTest
 * JD-Core Version:    0.6.2
 */