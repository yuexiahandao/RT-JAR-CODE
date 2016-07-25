package com.sun.xml.internal.ws.wsdl.writer.document.soap12;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("operation")
public abstract interface SOAPOperation extends TypedXmlWriter
{
  @XmlAttribute
  public abstract SOAPOperation soapAction(String paramString);

  @XmlAttribute
  public abstract SOAPOperation style(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPOperation
 * JD-Core Version:    0.6.2
 */