package com.sun.xml.internal.bind.v2.model.core;

import java.util.Set;

public abstract interface RegistryInfo<T, C>
{
  public abstract Set<TypeInfo<T, C>> getReferences();

  public abstract C getClazz();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.RegistryInfo
 * JD-Core Version:    0.6.2
 */