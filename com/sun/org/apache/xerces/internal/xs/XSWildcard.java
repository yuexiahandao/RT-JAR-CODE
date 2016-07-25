package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSWildcard extends XSTerm
{
  public static final short NSCONSTRAINT_ANY = 1;
  public static final short NSCONSTRAINT_NOT = 2;
  public static final short NSCONSTRAINT_LIST = 3;
  public static final short PC_STRICT = 1;
  public static final short PC_SKIP = 2;
  public static final short PC_LAX = 3;

  public abstract short getConstraintType();

  public abstract StringList getNsConstraintList();

  public abstract short getProcessContents();

  public abstract XSAnnotation getAnnotation();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSWildcard
 * JD-Core Version:    0.6.2
 */