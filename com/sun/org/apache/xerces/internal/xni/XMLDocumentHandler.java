package com.sun.org.apache.xerces.internal.xni;

import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

public abstract interface XMLDocumentHandler
{
  public abstract void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startCDATA(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endCDATA(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endDocument(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void setDocumentSource(XMLDocumentSource paramXMLDocumentSource);

  public abstract XMLDocumentSource getDocumentSource();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
 * JD-Core Version:    0.6.2
 */