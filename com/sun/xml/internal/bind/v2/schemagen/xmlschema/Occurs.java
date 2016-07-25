package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

public abstract interface Occurs extends TypedXmlWriter
{
  @XmlAttribute
  public abstract Occurs minOccurs(int paramInt);

  @XmlAttribute
  public abstract Occurs maxOccurs(String paramString);

  @XmlAttribute
  public abstract Occurs maxOccurs(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs
 * JD-Core Version:    0.6.2
 */