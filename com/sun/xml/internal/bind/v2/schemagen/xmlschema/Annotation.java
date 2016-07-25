package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("annotation")
public abstract interface Annotation extends TypedXmlWriter
{
  @XmlElement
  public abstract Appinfo appinfo();

  @XmlElement
  public abstract Documentation documentation();

  @XmlAttribute
  public abstract Annotation id(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotation
 * JD-Core Version:    0.6.2
 */