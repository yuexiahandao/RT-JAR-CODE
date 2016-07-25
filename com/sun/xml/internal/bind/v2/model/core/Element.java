package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

public abstract interface Element<T, C> extends TypeInfo<T, C>
{
  public abstract QName getElementName();

  public abstract Element<T, C> getSubstitutionHead();

  public abstract ClassInfo<T, C> getScope();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.Element
 * JD-Core Version:    0.6.2
 */