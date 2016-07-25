package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
import java.lang.reflect.Type;

public abstract interface RuntimeMapPropertyInfo extends RuntimePropertyInfo, MapPropertyInfo<Type, Class>
{
  public abstract RuntimeNonElement getKeyType();

  public abstract RuntimeNonElement getValueType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.runtime.RuntimeMapPropertyInfo
 * JD-Core Version:    0.6.2
 */