package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSMultiValueFacet extends XSObject
{
  public abstract short getFacetKind();

  public abstract StringList getLexicalFacetValues();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet
 * JD-Core Version:    0.6.2
 */