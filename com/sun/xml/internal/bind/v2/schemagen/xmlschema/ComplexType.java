package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("complexType")
public abstract interface ComplexType extends Annotated, ComplexTypeModel, TypedXmlWriter
{
  @XmlAttribute("final")
  public abstract ComplexType _final(String paramString);

  @XmlAttribute("final")
  public abstract ComplexType _final(String[] paramArrayOfString);

  @XmlAttribute
  public abstract ComplexType block(String paramString);

  @XmlAttribute
  public abstract ComplexType block(String[] paramArrayOfString);

  @XmlAttribute("abstract")
  public abstract ComplexType _abstract(boolean paramBoolean);

  @XmlAttribute
  public abstract ComplexType name(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType
 * JD-Core Version:    0.6.2
 */