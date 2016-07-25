/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ 
/*     */ public class DefaultXMLDocumentHandler
/*     */   implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler
/*     */ {
/*     */   private XMLDocumentSource fDocumentSource;
/*     */   private XMLDTDSource fDTDSource;
/*     */   private XMLDTDContentModelSource fCMSource;
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext context, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endGeneralEntity(String name, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDTD(XMLLocator locator, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endParameterEntity(String name, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endExternalSubset(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String contentModel, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startAttlist(String elementName, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endAttlist(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startConditional(short type, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignoredCharacters(XMLString text, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endConditional(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startContentModel(String elementName, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void any(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void empty(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startGroup(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void pcdata(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void element(String elementName, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void separator(short separator, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void occurrence(short occurrence, Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endGroup(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endContentModel(Augmentations augmentations)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source)
/*     */   {
/* 839 */     this.fDocumentSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource()
/*     */   {
/* 844 */     return this.fDocumentSource;
/*     */   }
/*     */ 
/*     */   public void setDTDSource(XMLDTDSource source)
/*     */   {
/* 851 */     this.fDTDSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDTDSource getDTDSource()
/*     */   {
/* 856 */     return this.fDTDSource;
/*     */   }
/*     */ 
/*     */   public void setDTDContentModelSource(XMLDTDContentModelSource source)
/*     */   {
/* 863 */     this.fCMSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDTDContentModelSource getDTDContentModelSource()
/*     */   {
/* 868 */     return this.fCMSource;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler
 * JD-Core Version:    0.6.2
 */