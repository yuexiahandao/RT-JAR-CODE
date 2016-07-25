package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("portType")
public abstract interface PortType extends TypedXmlWriter, Documented
{
  @XmlAttribute
  public abstract PortType name(String paramString);

  @XmlElement
  public abstract Operation operation();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.PortType
 * JD-Core Version:    0.6.2
 */