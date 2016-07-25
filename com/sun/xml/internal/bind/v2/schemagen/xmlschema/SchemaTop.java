package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

public abstract interface SchemaTop extends Redefinable, TypedXmlWriter
{
  @XmlElement
  public abstract TopLevelAttribute attribute();

  @XmlElement
  public abstract TopLevelElement element();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.SchemaTop
 * JD-Core Version:    0.6.2
 */