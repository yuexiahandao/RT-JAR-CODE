package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("simpleContent")
public abstract interface SimpleContent extends Annotated, TypedXmlWriter
{
  @XmlElement
  public abstract SimpleExtension extension();

  @XmlElement
  public abstract SimpleRestriction restriction();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleContent
 * JD-Core Version:    0.6.2
 */