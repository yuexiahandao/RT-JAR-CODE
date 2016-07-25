package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSModelGroup extends XSTerm
{
  public static final short COMPOSITOR_SEQUENCE = 1;
  public static final short COMPOSITOR_CHOICE = 2;
  public static final short COMPOSITOR_ALL = 3;

  public abstract short getCompositor();

  public abstract XSObjectList getParticles();

  public abstract XSAnnotation getAnnotation();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSModelGroup
 * JD-Core Version:    0.6.2
 */