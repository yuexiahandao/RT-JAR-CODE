package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import java.lang.reflect.Type;

public abstract interface RuntimeTypeRef extends TypeRef<Type, Class>, RuntimeNonElementRef
{
  public abstract RuntimeNonElement getTarget();

  public abstract RuntimePropertyInfo getSource();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef
 * JD-Core Version:    0.6.2
 */