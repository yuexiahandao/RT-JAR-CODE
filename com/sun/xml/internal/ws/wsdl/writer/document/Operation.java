package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("operation")
public abstract interface Operation extends TypedXmlWriter, Documented
{
  @XmlElement
  public abstract ParamType input();

  @XmlElement
  public abstract ParamType output();

  @XmlElement
  public abstract FaultType fault();

  @XmlAttribute
  public abstract Operation name(String paramString);

  @XmlAttribute
  public abstract Operation parameterOrder(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.Operation
 * JD-Core Version:    0.6.2
 */