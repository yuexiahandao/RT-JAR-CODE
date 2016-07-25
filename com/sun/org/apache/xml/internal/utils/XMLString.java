package com.sun.org.apache.xml.internal.utils;

import java.util.Locale;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public abstract interface XMLString
{
  public abstract void dispatchCharactersEvents(ContentHandler paramContentHandler)
    throws SAXException;

  public abstract void dispatchAsComment(LexicalHandler paramLexicalHandler)
    throws SAXException;

  public abstract XMLString fixWhiteSpace(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

  public abstract int length();

  public abstract char charAt(int paramInt);

  public abstract void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3);

  public abstract boolean equals(XMLString paramXMLString);

  public abstract boolean equals(String paramString);

  public abstract boolean equals(Object paramObject);

  public abstract boolean equalsIgnoreCase(String paramString);

  public abstract int compareTo(XMLString paramXMLString);

  public abstract int compareToIgnoreCase(XMLString paramXMLString);

  public abstract boolean startsWith(String paramString, int paramInt);

  public abstract boolean startsWith(XMLString paramXMLString, int paramInt);

  public abstract boolean startsWith(String paramString);

  public abstract boolean startsWith(XMLString paramXMLString);

  public abstract boolean endsWith(String paramString);

  public abstract int hashCode();

  public abstract int indexOf(int paramInt);

  public abstract int indexOf(int paramInt1, int paramInt2);

  public abstract int lastIndexOf(int paramInt);

  public abstract int lastIndexOf(int paramInt1, int paramInt2);

  public abstract int indexOf(String paramString);

  public abstract int indexOf(XMLString paramXMLString);

  public abstract int indexOf(String paramString, int paramInt);

  public abstract int lastIndexOf(String paramString);

  public abstract int lastIndexOf(String paramString, int paramInt);

  public abstract XMLString substring(int paramInt);

  public abstract XMLString substring(int paramInt1, int paramInt2);

  public abstract XMLString concat(String paramString);

  public abstract XMLString toLowerCase(Locale paramLocale);

  public abstract XMLString toLowerCase();

  public abstract XMLString toUpperCase(Locale paramLocale);

  public abstract XMLString toUpperCase();

  public abstract XMLString trim();

  public abstract String toString();

  public abstract boolean hasString();

  public abstract double toDouble();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLString
 * JD-Core Version:    0.6.2
 */