package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;

public abstract interface RestrictedAlphabetContentHandler
{
  public abstract void numericCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException;

  public abstract void dateTimeCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException;

  public abstract void alphabetCharacters(String paramString, char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
 * JD-Core Version:    0.6.2
 */