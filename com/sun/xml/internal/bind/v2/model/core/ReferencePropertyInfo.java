package com.sun.xml.internal.bind.v2.model.core;

import java.util.Collection;
import java.util.Set;
import javax.xml.namespace.QName;

public abstract interface ReferencePropertyInfo<T, C> extends PropertyInfo<T, C>
{
  public abstract Set<? extends Element<T, C>> getElements();

  public abstract Collection<? extends TypeInfo<T, C>> ref();

  public abstract QName getXmlName();

  public abstract boolean isCollectionNillable();

  public abstract boolean isCollectionRequired();

  public abstract boolean isMixed();

  public abstract WildcardMode getWildcard();

  public abstract C getDOMHandler();

  public abstract boolean isRequired();

  public abstract Adapter<T, C> getAdapter();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
 * JD-Core Version:    0.6.2
 */