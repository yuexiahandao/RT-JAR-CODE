package com.sun.org.apache.xerces.internal.impl.xs.identity;

public abstract interface FieldActivator
{
  public abstract void startValueScopeFor(IdentityConstraint paramIdentityConstraint, int paramInt);

  public abstract XPathMatcher activateField(Field paramField, int paramInt);

  public abstract void setMayMatch(Field paramField, Boolean paramBoolean);

  public abstract Boolean mayMatch(Field paramField);

  public abstract void endValueScopeFor(IdentityConstraint paramIdentityConstraint, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
 * JD-Core Version:    0.6.2
 */