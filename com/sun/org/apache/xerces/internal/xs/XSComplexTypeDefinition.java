package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSComplexTypeDefinition extends XSTypeDefinition
{
  public static final short CONTENTTYPE_EMPTY = 0;
  public static final short CONTENTTYPE_SIMPLE = 1;
  public static final short CONTENTTYPE_ELEMENT = 2;
  public static final short CONTENTTYPE_MIXED = 3;

  public abstract short getDerivationMethod();

  public abstract boolean getAbstract();

  public abstract XSObjectList getAttributeUses();

  public abstract XSWildcard getAttributeWildcard();

  public abstract short getContentType();

  public abstract XSSimpleTypeDefinition getSimpleType();

  public abstract XSParticle getParticle();

  public abstract boolean isProhibitedSubstitution(short paramShort);

  public abstract short getProhibitedSubstitutions();

  public abstract XSObjectList getAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
 * JD-Core Version:    0.6.2
 */