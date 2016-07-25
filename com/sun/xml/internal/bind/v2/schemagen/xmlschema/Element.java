package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

public abstract interface Element extends Annotated, ComplexTypeHost, FixedOrDefault, SimpleTypeHost, TypedXmlWriter
{
  @XmlAttribute
  public abstract Element type(QName paramQName);

  @XmlAttribute
  public abstract Element block(String paramString);

  @XmlAttribute
  public abstract Element block(String[] paramArrayOfString);

  @XmlAttribute
  public abstract Element nillable(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.xmlschema.Element
 * JD-Core Version:    0.6.2
 */