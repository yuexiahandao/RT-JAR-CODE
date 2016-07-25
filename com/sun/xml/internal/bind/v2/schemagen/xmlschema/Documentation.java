package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("documentation")
public abstract interface Documentation extends TypedXmlWriter
{
  @XmlAttribute
  public abstract Documentation source(String paramString);

  @XmlAttribute(ns="http://www.w3.org/XML/1998/namespace")
  public abstract Documentation lang(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.Documentation
 * JD-Core Version:    0.6.2
 */