package com.sun.xml.internal.bind.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract interface InfosetScanner<XmlNode>
{
  public abstract void scan(XmlNode paramXmlNode)
    throws SAXException;

  public abstract void setContentHandler(ContentHandler paramContentHandler);

  public abstract ContentHandler getContentHandler();

  public abstract XmlNode getCurrentElement();

  public abstract LocatorEx getLocator();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.unmarshaller.InfosetScanner
 * JD-Core Version:    0.6.2
 */