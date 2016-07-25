package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType;

@XmlElement(value="http://www.w3.org/2006/05/addressing/wsdl", ns="UsingAddressing")
public abstract interface UsingAddressing extends TypedXmlWriter, StartWithExtensionsType
{
  @XmlAttribute(value="required", ns="http://schemas.xmlsoap.org/wsdl/")
  public abstract void required(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.UsingAddressing
 * JD-Core Version:    0.6.2
 */