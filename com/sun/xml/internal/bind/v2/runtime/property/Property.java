package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

public abstract interface Property<BeanT> extends StructureLoaderBuilder
{
  public abstract void reset(BeanT paramBeanT)
    throws AccessorException;

  public abstract void serializeBody(BeanT paramBeanT, XMLSerializer paramXMLSerializer, Object paramObject)
    throws SAXException, AccessorException, IOException, XMLStreamException;

  public abstract void serializeURIs(BeanT paramBeanT, XMLSerializer paramXMLSerializer)
    throws SAXException, AccessorException;

  public abstract boolean hasSerializeURIAction();

  public abstract String getIdValue(BeanT paramBeanT)
    throws AccessorException, SAXException;

  public abstract PropertyKind getKind();

  public abstract Accessor getElementPropertyAccessor(String paramString1, String paramString2);

  public abstract void wrapUp();

  public abstract RuntimePropertyInfo getInfo();

  public abstract boolean isHiddenByOverride();

  public abstract void setHiddenByOverride(boolean paramBoolean);

  public abstract String getFieldName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.Property
 * JD-Core Version:    0.6.2
 */