package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

public abstract interface BuiltinLeafInfo<T, C> extends LeafInfo<T, C>
{
  public abstract QName getTypeName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo
 * JD-Core Version:    0.6.2
 */