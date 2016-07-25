package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;

public abstract interface EncodingAlgorithmContentHandler
{
  public abstract void octets(String paramString, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
    throws SAXException;

  public abstract void object(String paramString, int paramInt, Object paramObject)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
 * JD-Core Version:    0.6.2
 */