package org.xml.sax.ext;

import org.xml.sax.SAXException;

public abstract interface LexicalHandler
{
  public abstract void startDTD(String paramString1, String paramString2, String paramString3)
    throws SAXException;

  public abstract void endDTD()
    throws SAXException;

  public abstract void startEntity(String paramString)
    throws SAXException;

  public abstract void endEntity(String paramString)
    throws SAXException;

  public abstract void startCDATA()
    throws SAXException;

  public abstract void endCDATA()
    throws SAXException;

  public abstract void comment(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ext.LexicalHandler
 * JD-Core Version:    0.6.2
 */