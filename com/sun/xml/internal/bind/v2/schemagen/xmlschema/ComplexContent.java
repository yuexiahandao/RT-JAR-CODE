package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("complexContent")
public abstract interface ComplexContent extends Annotated, TypedXmlWriter
{
  @XmlElement
  public abstract ComplexExtension extension();

  @XmlElement
  public abstract ComplexRestriction restriction();

  @XmlAttribute
  public abstract ComplexContent mixed(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexContent
 * JD-Core Version:    0.6.2
 */