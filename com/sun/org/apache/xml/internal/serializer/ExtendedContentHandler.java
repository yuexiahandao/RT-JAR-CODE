package com.sun.org.apache.xml.internal.serializer;

import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

abstract interface ExtendedContentHandler extends ContentHandler
{
  public static final int NO_BAD_CHARS = 1;
  public static final int HTML_ATTREMPTY = 2;
  public static final int HTML_ATTRURL = 4;

  public abstract void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean)
    throws SAXException;

  public abstract void addAttributes(Attributes paramAttributes)
    throws SAXException;

  public abstract void addAttribute(String paramString1, String paramString2);

  public abstract void characters(String paramString)
    throws SAXException;

  public abstract void characters(Node paramNode)
    throws SAXException;

  public abstract void endElement(String paramString)
    throws SAXException;

  public abstract void startElement(String paramString1, String paramString2, String paramString3)
    throws SAXException;

  public abstract void startElement(String paramString)
    throws SAXException;

  public abstract void namespaceAfterStartElement(String paramString1, String paramString2)
    throws SAXException;

  public abstract boolean startPrefixMapping(String paramString1, String paramString2, boolean paramBoolean)
    throws SAXException;

  public abstract void entityReference(String paramString)
    throws SAXException;

  public abstract NamespaceMappings getNamespaceMappings();

  public abstract String getPrefix(String paramString);

  public abstract String getNamespaceURI(String paramString, boolean paramBoolean);

  public abstract String getNamespaceURIFromPrefix(String paramString);

  public abstract void setSourceLocator(SourceLocator paramSourceLocator);

  public abstract void addUniqueAttribute(String paramString1, String paramString2, int paramInt)
    throws SAXException;

  public abstract void addXSLAttribute(String paramString1, String paramString2, String paramString3);

  public abstract void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
 * JD-Core Version:    0.6.2
 */