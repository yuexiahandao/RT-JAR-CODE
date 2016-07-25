package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import java.lang.reflect.Type;
import javax.xml.bind.JAXBElement;

public abstract interface RuntimeElementInfo extends ElementInfo<Type, Class>, RuntimeElement
{
  public abstract RuntimeClassInfo getScope();

  public abstract RuntimeElementPropertyInfo getProperty();

  public abstract Class<? extends JAXBElement> getType();

  public abstract RuntimeNonElement getContentType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo
 * JD-Core Version:    0.6.2
 */