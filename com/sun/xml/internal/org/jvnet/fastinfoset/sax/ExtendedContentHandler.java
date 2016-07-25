package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract interface ExtendedContentHandler extends ContentHandler
{
  public abstract void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.ExtendedContentHandler
 * JD-Core Version:    0.6.2
 */