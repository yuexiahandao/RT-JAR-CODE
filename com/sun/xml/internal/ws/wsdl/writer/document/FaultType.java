package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

public abstract interface FaultType extends TypedXmlWriter, Documented
{
  @XmlAttribute
  public abstract FaultType message(QName paramQName);

  @XmlAttribute
  public abstract FaultType name(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.FaultType
 * JD-Core Version:    0.6.2
 */