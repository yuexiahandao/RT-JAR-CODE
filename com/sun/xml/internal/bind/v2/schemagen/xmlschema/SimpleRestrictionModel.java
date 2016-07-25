package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

public abstract interface SimpleRestrictionModel extends SimpleTypeHost, TypedXmlWriter
{
  @XmlAttribute
  public abstract SimpleRestrictionModel base(QName paramQName);

  @XmlElement
  public abstract NoFixedFacet enumeration();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleRestrictionModel
 * JD-Core Version:    0.6.2
 */