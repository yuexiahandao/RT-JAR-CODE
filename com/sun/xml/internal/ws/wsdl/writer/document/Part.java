package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("part")
public abstract interface Part extends TypedXmlWriter, OpenAtts
{
  @XmlAttribute
  public abstract Part element(QName paramQName);

  @XmlAttribute
  public abstract Part type(QName paramQName);

  @XmlAttribute
  public abstract Part name(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.Part
 * JD-Core Version:    0.6.2
 */