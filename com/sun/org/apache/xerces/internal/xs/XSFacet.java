package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSFacet extends XSObject
{
  public abstract short getFacetKind();

  public abstract String getLexicalFacetValue();

  public abstract boolean getFixed();

  public abstract XSAnnotation getAnnotation();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSFacet
 * JD-Core Version:    0.6.2
 */