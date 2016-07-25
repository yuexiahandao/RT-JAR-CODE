package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public abstract interface RuntimeClassInfo extends ClassInfo<Type, Class>, RuntimeNonElement
{
  public abstract RuntimeClassInfo getBaseClass();

  public abstract List<? extends RuntimePropertyInfo> getProperties();

  public abstract RuntimePropertyInfo getProperty(String paramString);

  public abstract Method getFactoryMethod();

  public abstract <BeanT> Accessor<BeanT, Map<QName, String>> getAttributeWildcard();

  public abstract <BeanT> Accessor<BeanT, Locator> getLocatorField();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo
 * JD-Core Version:    0.6.2
 */