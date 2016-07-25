package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

public abstract interface ContentModelContainer extends TypedXmlWriter
{
  @XmlElement
  public abstract LocalElement element();

  @XmlElement
  public abstract Any any();

  @XmlElement
  public abstract ExplicitGroup all();

  @XmlElement
  public abstract ExplicitGroup sequence();

  @XmlElement
  public abstract ExplicitGroup choice();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer
 * JD-Core Version:    0.6.2
 */