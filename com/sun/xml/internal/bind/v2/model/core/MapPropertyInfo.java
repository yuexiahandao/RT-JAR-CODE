package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

public abstract interface MapPropertyInfo<T, C> extends PropertyInfo<T, C>
{
  public abstract QName getXmlName();

  public abstract boolean isCollectionNillable();

  public abstract NonElement<T, C> getKeyType();

  public abstract NonElement<T, C> getValueType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
 * JD-Core Version:    0.6.2
 */