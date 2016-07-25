package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.LeafInfo;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;
import javax.xml.namespace.QName;

public abstract interface RuntimeLeafInfo extends LeafInfo<Type, Class>, RuntimeNonElement
{
  public abstract <V> Transducer<V> getTransducer();

  public abstract Class getClazz();

  public abstract QName[] getTypeNames();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo
 * JD-Core Version:    0.6.2
 */