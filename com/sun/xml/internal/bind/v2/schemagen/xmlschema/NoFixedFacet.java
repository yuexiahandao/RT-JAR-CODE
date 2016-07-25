package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

public abstract interface NoFixedFacet extends Annotated, TypedXmlWriter
{
  @XmlAttribute
  public abstract NoFixedFacet value(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet
 * JD-Core Version:    0.6.2
 */