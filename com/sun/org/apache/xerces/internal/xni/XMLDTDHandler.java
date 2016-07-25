package com.sun.org.apache.xerces.internal.xni;

import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;

public abstract interface XMLDTDHandler
{
  public static final short CONDITIONAL_INCLUDE = 0;
  public static final short CONDITIONAL_IGNORE = 1;

  public abstract void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startAttlist(String paramString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endAttlist(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void startConditional(short paramShort, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endConditional(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void endDTD(Augmentations paramAugmentations)
    throws XNIException;

  public abstract void setDTDSource(XMLDTDSource paramXMLDTDSource);

  public abstract XMLDTDSource getDTDSource();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
 * JD-Core Version:    0.6.2
 */