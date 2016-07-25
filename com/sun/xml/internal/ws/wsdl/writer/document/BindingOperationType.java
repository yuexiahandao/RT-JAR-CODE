package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

public abstract interface BindingOperationType extends TypedXmlWriter, StartWithExtensionsType
{
  @XmlAttribute
  public abstract BindingOperationType name(String paramString);

  @XmlElement(value="operation", ns="http://schemas.xmlsoap.org/wsdl/soap/")
  public abstract com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPOperation soapOperation();

  @XmlElement(value="operation", ns="http://schemas.xmlsoap.org/wsdl/soap12/")
  public abstract com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPOperation soap12Operation();

  @XmlElement
  public abstract Fault fault();

  @XmlElement
  public abstract StartWithExtensionsType output();

  @XmlElement
  public abstract StartWithExtensionsType input();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.document.BindingOperationType
 * JD-Core Version:    0.6.2
 */