/*     */ package com.sun.org.apache.xerces.internal.parsers;
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
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ 
/*     */ public abstract class AbstractXMLDocumentParser extends XMLParser
/*     */   implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler
/*     */ {
/*     */   protected boolean fInDTD;
/*     */   protected XMLDocumentSource fDocumentSource;
/*     */   protected XMLDTDSource fDTDSource;
/*     */   protected XMLDTDContentModelSource fDTDContentModelSource;
/*     */ 
/*     */   protected AbstractXMLDocumentParser(XMLParserConfiguration config)
/*     */   {
/*  81 */     super(config);
/*     */ 
/*  84 */     config.setDocumentHandler(this);
/*  85 */     config.setDTDHandler(this);
/*  86 */     config.setDTDContentModelHandler(this);
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
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
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 182 */     startElement(element, attributes, augs);
/* 183 */     endElement(element, augs);
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
/*     */   public void setDocumentSource(XMLDocumentSource source)
/*     */   {
/* 350 */     this.fDocumentSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource()
/*     */   {
/* 355 */     return this.fDocumentSource;
/*     */   }
/*     */ 
/*     */   public void startDTD(XMLLocator locator, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 376 */     this.fInDTD = true;
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
/*     */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endParameterEntity(String name, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignoredCharacters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startAttlist(String elementName, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endAttlist(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startConditional(short type, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endConditional(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 631 */     this.fInDTD = false;
/*     */   }
/*     */ 
/*     */   public void setDTDSource(XMLDTDSource source)
/*     */   {
/* 636 */     this.fDTDSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDTDSource getDTDSource()
/*     */   {
/* 641 */     return this.fDTDSource;
/*     */   }
/*     */ 
/*     */   public void startContentModel(String elementName, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void any(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void empty(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startGroup(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void pcdata(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void element(String elementName, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void separator(short separator, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void occurrence(short occurrence, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endGroup(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endContentModel(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDTDContentModelSource(XMLDTDContentModelSource source)
/*     */   {
/* 792 */     this.fDTDContentModelSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDTDContentModelSource getDTDContentModelSource()
/*     */   {
/* 797 */     return this.fDTDContentModelSource;
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */     throws XNIException
/*     */   {
/* 808 */     super.reset();
/* 809 */     this.fInDTD = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
 * JD-Core Version:    0.6.2
 */