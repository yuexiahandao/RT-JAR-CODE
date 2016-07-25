/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ 
/*     */ public class XMLNamespaceBinder
/*     */   implements XMLComponent, XMLDocumentFilter
/*     */ {
/*     */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/* 128 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces" };
/*     */ 
/* 133 */   private static final Boolean[] FEATURE_DEFAULTS = { null };
/*     */ 
/* 138 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter" };
/*     */ 
/* 144 */   private static final Object[] PROPERTY_DEFAULTS = { null, null };
/*     */   protected boolean fNamespaces;
/*     */   protected SymbolTable fSymbolTable;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLDocumentHandler fDocumentHandler;
/*     */   protected XMLDocumentSource fDocumentSource;
/*     */   protected boolean fOnlyPassPrefixMappingEvents;
/*     */   private NamespaceContext fNamespaceContext;
/* 186 */   private QName fAttributeQName = new QName();
/*     */ 
/*     */   public void setOnlyPassPrefixMappingEvents(boolean onlyPassPrefixMappingEvents)
/*     */   {
/* 212 */     this.fOnlyPassPrefixMappingEvents = onlyPassPrefixMappingEvents;
/*     */   }
/*     */ 
/*     */   public boolean getOnlyPassPrefixMappingEvents()
/*     */   {
/* 221 */     return this.fOnlyPassPrefixMappingEvents;
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XNIException
/*     */   {
/* 246 */     this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
/*     */ 
/* 249 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 250 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedFeatures()
/*     */   {
/* 260 */     return (String[])RECOGNIZED_FEATURES.clone();
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedProperties()
/*     */   {
/* 288 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 301 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 302 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 304 */       if ((suffixLength == "internal/symbol-table".length()) && (propertyId.endsWith("internal/symbol-table")))
/*     */       {
/* 306 */         this.fSymbolTable = ((SymbolTable)value);
/*     */       }
/* 308 */       else if ((suffixLength == "internal/error-reporter".length()) && (propertyId.endsWith("internal/error-reporter")))
/*     */       {
/* 310 */         this.fErrorReporter = ((XMLErrorReporter)value);
/*     */       }
/* 312 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Boolean getFeatureDefault(String featureId)
/*     */   {
/* 327 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/* 328 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/* 329 */         return FEATURE_DEFAULTS[i];
/*     */       }
/*     */     }
/* 332 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getPropertyDefault(String propertyId)
/*     */   {
/* 345 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/* 346 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/* 347 */         return PROPERTY_DEFAULTS[i];
/*     */       }
/*     */     }
/* 350 */     return null;
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*     */   {
/* 359 */     this.fDocumentHandler = documentHandler;
/*     */   }
/*     */ 
/*     */   public XMLDocumentHandler getDocumentHandler()
/*     */   {
/* 364 */     return this.fDocumentHandler;
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source)
/*     */   {
/* 374 */     this.fDocumentSource = source;
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource()
/*     */   {
/* 379 */     return this.fDocumentSource;
/*     */   }
/*     */ 
/*     */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 404 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 405 */       this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 428 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 429 */       this.fDocumentHandler.textDecl(version, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 458 */     this.fNamespaceContext = namespaceContext;
/*     */ 
/* 460 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 461 */       this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 480 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 481 */       this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 500 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 501 */       this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 514 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 515 */       this.fDocumentHandler.comment(text, augs);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 538 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 539 */       this.fDocumentHandler.processingInstruction(target, data, augs);
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 562 */     if (this.fNamespaces) {
/* 563 */       handleStartElement(element, attributes, augs, false);
/*     */     }
/* 565 */     else if (this.fDocumentHandler != null)
/* 566 */       this.fDocumentHandler.startElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 584 */     if (this.fNamespaces) {
/* 585 */       handleStartElement(element, attributes, augs, true);
/* 586 */       handleEndElement(element, augs, true);
/*     */     }
/* 588 */     else if (this.fDocumentHandler != null) {
/* 589 */       this.fDocumentHandler.emptyElement(element, attributes, augs);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 603 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 604 */       this.fDocumentHandler.characters(text, augs);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 622 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 623 */       this.fDocumentHandler.ignorableWhitespace(text, augs);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 637 */     if (this.fNamespaces) {
/* 638 */       handleEndElement(element, augs, false);
/*     */     }
/* 640 */     else if (this.fDocumentHandler != null)
/* 641 */       this.fDocumentHandler.endElement(element, augs);
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 653 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 654 */       this.fDocumentHandler.startCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 665 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 666 */       this.fDocumentHandler.endCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 677 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 678 */       this.fDocumentHandler.endDocument(augs);
/*     */   }
/*     */ 
/*     */   public void endGeneralEntity(String name, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 695 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 696 */       this.fDocumentHandler.endGeneralEntity(name, augs);
/*     */   }
/*     */ 
/*     */   protected void handleStartElement(QName element, XMLAttributes attributes, Augmentations augs, boolean isEmpty)
/*     */     throws XNIException
/*     */   {
/* 710 */     this.fNamespaceContext.pushContext();
/*     */ 
/* 712 */     if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 713 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { element.rawname }, (short)2);
/*     */     }
/*     */ 
/* 720 */     int length = attributes.getLength();
/* 721 */     for (int i = 0; i < length; i++) {
/* 722 */       String localpart = attributes.getLocalName(i);
/* 723 */       String prefix = attributes.getPrefix(i);
/*     */ 
/* 726 */       if ((prefix == XMLSymbols.PREFIX_XMLNS) || ((prefix == XMLSymbols.EMPTY_STRING) && (localpart == XMLSymbols.PREFIX_XMLNS)))
/*     */       {
/* 730 */         String uri = this.fSymbolTable.addSymbol(attributes.getValue(i));
/*     */ 
/* 733 */         if ((prefix == XMLSymbols.PREFIX_XMLNS) && (localpart == XMLSymbols.PREFIX_XMLNS)) {
/* 734 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 741 */         if (uri == NamespaceContext.XMLNS_URI) {
/* 742 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 749 */         if (localpart == XMLSymbols.PREFIX_XML) {
/* 750 */           if (uri != NamespaceContext.XML_URI) {
/* 751 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { attributes.getQName(i) }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/* 759 */         else if (uri == NamespaceContext.XML_URI) {
/* 760 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 767 */         prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 776 */         if (prefixBoundToNullURI(uri, localpart)) {
/* 777 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */         else
/*     */         {
/* 785 */           this.fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 791 */     String prefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 793 */     element.uri = this.fNamespaceContext.getURI(prefix);
/* 794 */     if ((element.prefix == null) && (element.uri != null)) {
/* 795 */       element.prefix = XMLSymbols.EMPTY_STRING;
/*     */     }
/* 797 */     if ((element.prefix != null) && (element.uri == null)) {
/* 798 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { element.prefix, element.rawname }, (short)2);
/*     */     }
/*     */ 
/* 805 */     for (int i = 0; i < length; i++) {
/* 806 */       attributes.getName(i, this.fAttributeQName);
/* 807 */       String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 809 */       String arawname = this.fAttributeQName.rawname;
/* 810 */       if (arawname == XMLSymbols.PREFIX_XMLNS) {
/* 811 */         this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
/* 812 */         attributes.setName(i, this.fAttributeQName);
/*     */       }
/* 814 */       else if (aprefix != XMLSymbols.EMPTY_STRING) {
/* 815 */         this.fAttributeQName.uri = this.fNamespaceContext.getURI(aprefix);
/* 816 */         if (this.fAttributeQName.uri == null) {
/* 817 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { element.rawname, arawname, aprefix }, (short)2);
/*     */         }
/*     */ 
/* 822 */         attributes.setName(i, this.fAttributeQName);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 828 */     int attrCount = attributes.getLength();
/* 829 */     for (int i = 0; i < attrCount - 1; i++) {
/* 830 */       String auri = attributes.getURI(i);
/* 831 */       if ((auri != null) && (auri != NamespaceContext.XMLNS_URI))
/*     */       {
/* 834 */         String alocalpart = attributes.getLocalName(i);
/* 835 */         for (int j = i + 1; j < attrCount; j++) {
/* 836 */           String blocalpart = attributes.getLocalName(j);
/* 837 */           String buri = attributes.getURI(j);
/* 838 */           if ((alocalpart == blocalpart) && (auri == buri)) {
/* 839 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { element.rawname, alocalpart, auri }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 848 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents))
/* 849 */       if (isEmpty) {
/* 850 */         this.fDocumentHandler.emptyElement(element, attributes, augs);
/*     */       }
/*     */       else
/* 853 */         this.fDocumentHandler.startElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty)
/*     */     throws XNIException
/*     */   {
/* 865 */     String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
/* 866 */     element.uri = this.fNamespaceContext.getURI(eprefix);
/* 867 */     if (element.uri != null) {
/* 868 */       element.prefix = eprefix;
/*     */     }
/*     */ 
/* 872 */     if ((this.fDocumentHandler != null) && (!this.fOnlyPassPrefixMappingEvents) && 
/* 873 */       (!isEmpty)) {
/* 874 */       this.fDocumentHandler.endElement(element, augs);
/*     */     }
/*     */ 
/* 879 */     this.fNamespaceContext.popContext();
/*     */   }
/*     */ 
/*     */   protected boolean prefixBoundToNullURI(String uri, String localpart)
/*     */   {
/* 886 */     return (uri == XMLSymbols.EMPTY_STRING) && (localpart != XMLSymbols.PREFIX_XMLNS);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLNamespaceBinder
 * JD-Core Version:    0.6.2
 */