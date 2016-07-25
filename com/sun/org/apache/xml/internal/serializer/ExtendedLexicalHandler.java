package com.sun.org.apache.xml.internal.serializer;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

abstract interface ExtendedLexicalHandler extends LexicalHandler
{
  public abstract void comment(String paramString)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
 * JD-Core Version:    0.6.2
 */