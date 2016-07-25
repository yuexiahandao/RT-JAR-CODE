package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSAttributeDeclaration extends XSObject
{
  public abstract XSSimpleTypeDefinition getTypeDefinition();

  public abstract short getScope();

  public abstract XSComplexTypeDefinition getEnclosingCTDefinition();

  public abstract short getConstraintType();

  public abstract String getConstraintValue();

  public abstract Object getActualVC()
    throws XSException;

  public abstract short getActualVCType()
    throws XSException;

  public abstract ShortList getItemValueTypes()
    throws XSException;

  public abstract XSAnnotation getAnnotation();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
 * JD-Core Version:    0.6.2
 */