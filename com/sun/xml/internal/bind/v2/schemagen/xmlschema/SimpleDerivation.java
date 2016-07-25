package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

public abstract interface SimpleDerivation extends TypedXmlWriter
{
  @XmlElement
  public abstract SimpleRestriction restriction();

  @XmlElement
  public abstract Union union();

  @XmlElement
  public abstract List list();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleDerivation
 * JD-Core Version:    0.6.2
 */