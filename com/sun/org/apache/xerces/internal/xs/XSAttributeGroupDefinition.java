package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSAttributeGroupDefinition extends XSObject
{
  public abstract XSObjectList getAttributeUses();

  public abstract XSWildcard getAttributeWildcard();

  public abstract XSAnnotation getAnnotation();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition
 * JD-Core Version:    0.6.2
 */