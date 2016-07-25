package com.sun.xml.internal.bind.v2.model.core;

import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

public abstract interface TypeInfoSet<T, C, F, M>
{
  public abstract Navigator<T, C, F, M> getNavigator();

  public abstract NonElement<T, C> getTypeInfo(T paramT);

  public abstract NonElement<T, C> getAnyTypeInfo();

  public abstract NonElement<T, C> getClassInfo(C paramC);

  public abstract Map<? extends T, ? extends ArrayInfo<T, C>> arrays();

  public abstract Map<C, ? extends ClassInfo<T, C>> beans();

  public abstract Map<T, ? extends BuiltinLeafInfo<T, C>> builtins();

  public abstract Map<C, ? extends EnumLeafInfo<T, C>> enums();

  public abstract ElementInfo<T, C> getElementInfo(C paramC, QName paramQName);

  public abstract NonElement<T, C> getTypeInfo(Ref<T, C> paramRef);

  public abstract Map<QName, ? extends ElementInfo<T, C>> getElementMappings(C paramC);

  public abstract Iterable<? extends ElementInfo<T, C>> getAllElements();

  public abstract Map<String, String> getXmlNs(String paramString);

  public abstract Map<String, String> getSchemaLocations();

  public abstract XmlNsForm getElementFormDefault(String paramString);

  public abstract XmlNsForm getAttributeFormDefault(String paramString);

  public abstract void dump(Result paramResult)
    throws JAXBException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
 * JD-Core Version:    0.6.2
 */