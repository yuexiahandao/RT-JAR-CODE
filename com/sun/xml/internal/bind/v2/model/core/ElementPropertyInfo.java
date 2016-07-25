package com.sun.xml.internal.bind.v2.model.core;

import java.util.List;
import javax.xml.namespace.QName;

public abstract interface ElementPropertyInfo<T, C> extends PropertyInfo<T, C>
{
  public abstract List<? extends TypeRef<T, C>> getTypes();

  public abstract QName getXmlName();

  public abstract boolean isCollectionRequired();

  public abstract boolean isCollectionNillable();

  public abstract boolean isValueList();

  public abstract boolean isRequired();

  public abstract Adapter<T, C> getAdapter();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
 * JD-Core Version:    0.6.2
 */