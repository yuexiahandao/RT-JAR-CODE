package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("port")
public abstract interface Port extends TypedXmlWriter, Documented
{
  @XmlAttribute
  public abstract Port name(String paramString);

  @XmlAttribute
  public abstract Port arrayType(String paramString);

  @XmlAttribute
  public abstract Port binding(QName paramQName);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.Port
 * JD-Core Version:    0.6.2
 */