package com.sun.xml.internal.bind;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.xml.bind.JAXBException;

public abstract interface AccessorFactory
{
  public abstract Accessor createFieldAccessor(Class paramClass, Field paramField, boolean paramBoolean)
    throws JAXBException;

  public abstract Accessor createPropertyAccessor(Class paramClass, Method paramMethod1, Method paramMethod2)
    throws JAXBException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.AccessorFactory
 * JD-Core Version:    0.6.2
 */