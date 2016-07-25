/*      */ package com.sun.org.apache.xerces.internal.parsers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.Status;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*      */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
/*      */ import java.io.IOException;
/*      */ import java.util.Locale;
/*      */ import org.xml.sax.AttributeList;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.DocumentHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Parser;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.ext.Attributes2;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.EntityResolver2;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ import org.xml.sax.ext.Locator2;
/*      */ import org.xml.sax.helpers.LocatorImpl;
/*      */ 
/*      */ public abstract class AbstractSAXParser extends AbstractXMLDocumentParser
/*      */   implements PSVIProvider, Parser, XMLReader
/*      */ {
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
/*      */   protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
/*      */   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
/*  113 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/namespace-prefixes", "http://xml.org/sax/features/string-interning" };
/*      */   protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
/*      */   protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
/*      */   protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
/*      */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*  138 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/properties/declaration-handler", "http://xml.org/sax/properties/dom-node" };
/*      */   protected boolean fNamespaces;
/*  154 */   protected boolean fNamespacePrefixes = false;
/*      */ 
/*  157 */   protected boolean fLexicalHandlerParameterEntities = true;
/*      */   protected boolean fStandalone;
/*  163 */   protected boolean fResolveDTDURIs = true;
/*      */ 
/*  166 */   protected boolean fUseEntityResolver2 = true;
/*      */ 
/*  172 */   protected boolean fXMLNSURIs = false;
/*      */   protected ContentHandler fContentHandler;
/*      */   protected DocumentHandler fDocumentHandler;
/*      */   protected NamespaceContext fNamespaceContext;
/*      */   protected DTDHandler fDTDHandler;
/*      */   protected DeclHandler fDeclHandler;
/*      */   protected LexicalHandler fLexicalHandler;
/*  194 */   protected QName fQName = new QName();
/*      */ 
/*  203 */   protected boolean fParseInProgress = false;
/*      */   protected String fVersion;
/*  209 */   private final AttributesProxy fAttributesProxy = new AttributesProxy();
/*  210 */   private Augmentations fAugmentations = null;
/*      */   private static final int BUFFER_SIZE = 20;
/*  216 */   private char[] fCharBuffer = new char[20];
/*      */ 
/*  221 */   protected SymbolHash fDeclaredAttrs = null;
/*      */ 
/*      */   protected AbstractSAXParser(XMLParserConfiguration config)
/*      */   {
/*  229 */     super(config);
/*      */ 
/*  231 */     config.addRecognizedFeatures(RECOGNIZED_FEATURES);
/*  232 */     config.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/*      */     try
/*      */     {
/*  235 */       config.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", false);
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  276 */     this.fNamespaceContext = namespaceContext;
/*      */     try
/*      */     {
/*  280 */       if (this.fDocumentHandler != null) {
/*  281 */         if (locator != null) {
/*  282 */           this.fDocumentHandler.setDocumentLocator(new LocatorProxy(locator));
/*      */         }
/*  284 */         this.fDocumentHandler.startDocument();
/*      */       }
/*      */ 
/*  288 */       if (this.fContentHandler != null) {
/*  289 */         if (locator != null) {
/*  290 */           this.fContentHandler.setDocumentLocator(new LocatorProxy(locator));
/*      */         }
/*  292 */         this.fContentHandler.startDocument();
/*      */       }
/*      */     }
/*      */     catch (SAXException e) {
/*  296 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  318 */     this.fVersion = version;
/*  319 */     this.fStandalone = "yes".equals(standalone);
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  337 */     this.fInDTD = true;
/*      */     try
/*      */     {
/*  341 */       if (this.fLexicalHandler != null)
/*  342 */         this.fLexicalHandler.startDTD(rootElement, publicId, systemId);
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  346 */       throw new XNIException(e);
/*      */     }
/*      */ 
/*  350 */     if (this.fDeclHandler != null)
/*  351 */       this.fDeclaredAttrs = new SymbolHash();
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  386 */       if ((augs != null) && (Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED"))))
/*      */       {
/*  388 */         if (this.fContentHandler != null) {
/*  389 */           this.fContentHandler.skippedEntity(name);
/*      */         }
/*      */ 
/*      */       }
/*  394 */       else if (this.fLexicalHandler != null) {
/*  395 */         this.fLexicalHandler.startEntity(name);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  400 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  428 */       if ((augs == null) || (!Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED"))))
/*      */       {
/*  430 */         if (this.fLexicalHandler != null)
/*  431 */           this.fLexicalHandler.endEntity(name);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  436 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  457 */       if (this.fDocumentHandler != null)
/*      */       {
/*  460 */         this.fAttributesProxy.setAttributes(attributes);
/*  461 */         this.fDocumentHandler.startElement(element.rawname, this.fAttributesProxy);
/*      */       }
/*      */ 
/*  465 */       if (this.fContentHandler != null)
/*      */       {
/*  467 */         if (this.fNamespaces)
/*      */         {
/*  469 */           startNamespaceMapping();
/*      */ 
/*  477 */           int len = attributes.getLength();
/*  478 */           if (!this.fNamespacePrefixes) {
/*  479 */             for (int i = len - 1; i >= 0; i--) {
/*  480 */               attributes.getName(i, this.fQName);
/*  481 */               if ((this.fQName.prefix == XMLSymbols.PREFIX_XMLNS) || (this.fQName.rawname == XMLSymbols.PREFIX_XMLNS))
/*      */               {
/*  484 */                 attributes.removeAttributeAt(i);
/*      */               }
/*      */             }
/*      */           }
/*  488 */           else if (!this.fXMLNSURIs) {
/*  489 */             for (int i = len - 1; i >= 0; i--) {
/*  490 */               attributes.getName(i, this.fQName);
/*  491 */               if ((this.fQName.prefix == XMLSymbols.PREFIX_XMLNS) || (this.fQName.rawname == XMLSymbols.PREFIX_XMLNS))
/*      */               {
/*  495 */                 this.fQName.prefix = "";
/*  496 */                 this.fQName.uri = "";
/*  497 */                 this.fQName.localpart = "";
/*  498 */                 attributes.setName(i, this.fQName);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  504 */         this.fAugmentations = augs;
/*      */ 
/*  506 */         String uri = element.uri != null ? element.uri : "";
/*  507 */         String localpart = this.fNamespaces ? element.localpart : "";
/*  508 */         this.fAttributesProxy.setAttributes(attributes);
/*  509 */         this.fContentHandler.startElement(uri, localpart, element.rawname, this.fAttributesProxy);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  514 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  531 */     if (text.length == 0) {
/*  532 */       return;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  538 */       if (this.fDocumentHandler != null)
/*      */       {
/*  541 */         this.fDocumentHandler.characters(text.ch, text.offset, text.length);
/*      */       }
/*      */ 
/*  545 */       if (this.fContentHandler != null)
/*  546 */         this.fContentHandler.characters(text.ch, text.offset, text.length);
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  550 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  572 */       if (this.fDocumentHandler != null) {
/*  573 */         this.fDocumentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
/*      */       }
/*      */ 
/*  577 */       if (this.fContentHandler != null)
/*  578 */         this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  582 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  600 */       if (this.fDocumentHandler != null) {
/*  601 */         this.fDocumentHandler.endElement(element.rawname);
/*      */       }
/*      */ 
/*  605 */       if (this.fContentHandler != null) {
/*  606 */         this.fAugmentations = augs;
/*  607 */         String uri = element.uri != null ? element.uri : "";
/*  608 */         String localpart = this.fNamespaces ? element.localpart : "";
/*  609 */         this.fContentHandler.endElement(uri, localpart, element.rawname);
/*      */ 
/*  611 */         if (this.fNamespaces)
/*  612 */           endNamespaceMapping();
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  617 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  632 */       if (this.fLexicalHandler != null)
/*  633 */         this.fLexicalHandler.startCDATA();
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  637 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  652 */       if (this.fLexicalHandler != null)
/*  653 */         this.fLexicalHandler.endCDATA();
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  657 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  674 */       if (this.fLexicalHandler != null)
/*  675 */         this.fLexicalHandler.comment(text.ch, 0, text.length);
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  679 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  712 */       if (this.fDocumentHandler != null) {
/*  713 */         this.fDocumentHandler.processingInstruction(target, data.toString());
/*      */       }
/*      */ 
/*  718 */       if (this.fContentHandler != null)
/*  719 */         this.fContentHandler.processingInstruction(target, data.toString());
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  723 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  739 */       if (this.fDocumentHandler != null) {
/*  740 */         this.fDocumentHandler.endDocument();
/*      */       }
/*      */ 
/*  744 */       if (this.fContentHandler != null)
/*  745 */         this.fContentHandler.endDocument();
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  749 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  768 */     startParameterEntity("[dtd]", null, null, augs);
/*      */   }
/*      */ 
/*      */   public void endExternalSubset(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  780 */     endParameterEntity("[dtd]", augs);
/*      */   }
/*      */ 
/*      */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  815 */       if ((augs != null) && (Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED"))))
/*      */       {
/*  817 */         if (this.fContentHandler != null) {
/*  818 */           this.fContentHandler.skippedEntity(name);
/*      */         }
/*      */ 
/*      */       }
/*  823 */       else if ((this.fLexicalHandler != null) && (this.fLexicalHandlerParameterEntities)) {
/*  824 */         this.fLexicalHandler.startEntity(name);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  829 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endParameterEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  858 */       if ((augs == null) || (!Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED"))))
/*      */       {
/*  860 */         if ((this.fLexicalHandler != null) && (this.fLexicalHandlerParameterEntities))
/*  861 */           this.fLexicalHandler.endEntity(name);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  866 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  887 */       if (this.fDeclHandler != null)
/*  888 */         this.fDeclHandler.elementDecl(name, contentModel);
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  892 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  930 */       if (this.fDeclHandler != null)
/*      */       {
/*  932 */         String elemAttr = elementName + "<" + attributeName;
/*  933 */         if (this.fDeclaredAttrs.get(elemAttr) != null)
/*      */         {
/*  935 */           return;
/*      */         }
/*  937 */         this.fDeclaredAttrs.put(elemAttr, Boolean.TRUE);
/*  938 */         if ((type.equals("NOTATION")) || (type.equals("ENUMERATION")))
/*      */         {
/*  941 */           StringBuffer str = new StringBuffer();
/*  942 */           if (type.equals("NOTATION")) {
/*  943 */             str.append(type);
/*  944 */             str.append(" (");
/*      */           }
/*      */           else {
/*  947 */             str.append("(");
/*      */           }
/*  949 */           for (int i = 0; i < enumeration.length; i++) {
/*  950 */             str.append(enumeration[i]);
/*  951 */             if (i < enumeration.length - 1) {
/*  952 */               str.append('|');
/*      */             }
/*      */           }
/*  955 */           str.append(')');
/*  956 */           type = str.toString();
/*      */         }
/*  958 */         String value = defaultValue == null ? null : defaultValue.toString();
/*  959 */         this.fDeclHandler.attributeDecl(elementName, attributeName, type, defaultType, value);
/*      */       }
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  964 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/*  992 */       if (this.fDeclHandler != null)
/*  993 */         this.fDeclHandler.internalEntityDecl(name, text.toString());
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/*  997 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/* 1019 */       if (this.fDeclHandler != null) {
/* 1020 */         String publicId = identifier.getPublicId();
/* 1021 */         String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
/*      */ 
/* 1023 */         this.fDeclHandler.externalEntityDecl(name, publicId, systemId);
/*      */       }
/*      */     }
/*      */     catch (SAXException e) {
/* 1027 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/* 1050 */       if (this.fDTDHandler != null) {
/* 1051 */         String publicId = identifier.getPublicId();
/* 1052 */         String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
/*      */ 
/* 1054 */         this.fDTDHandler.unparsedEntityDecl(name, publicId, systemId, notation);
/*      */       }
/*      */     }
/*      */     catch (SAXException e) {
/* 1058 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */     try
/*      */     {
/* 1078 */       if (this.fDTDHandler != null) {
/* 1079 */         String publicId = identifier.getPublicId();
/* 1080 */         String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
/*      */ 
/* 1082 */         this.fDTDHandler.notationDecl(name, publicId, systemId);
/*      */       }
/*      */     }
/*      */     catch (SAXException e) {
/* 1086 */       throw new XNIException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDTD(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1100 */     this.fInDTD = false;
/*      */     try
/*      */     {
/* 1104 */       if (this.fLexicalHandler != null)
/* 1105 */         this.fLexicalHandler.endDTD();
/*      */     }
/*      */     catch (SAXException e)
/*      */     {
/* 1109 */       throw new XNIException(e);
/*      */     }
/* 1111 */     if (this.fDeclaredAttrs != null)
/*      */     {
/* 1113 */       this.fDeclaredAttrs.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parse(String systemId)
/*      */     throws SAXException, IOException
/*      */   {
/* 1138 */     XMLInputSource source = new XMLInputSource(null, systemId, null);
/*      */     try {
/* 1140 */       parse(source);
/*      */     }
/*      */     catch (XMLParseException e)
/*      */     {
/* 1145 */       Exception ex = e.getException();
/* 1146 */       if (ex == null)
/*      */       {
/* 1149 */         LocatorImpl locatorImpl = new LocatorImpl() {
/*      */           public String getXMLVersion() {
/* 1151 */             return AbstractSAXParser.this.fVersion;
/*      */           }
/*      */ 
/*      */           public String getEncoding()
/*      */           {
/* 1159 */             return null;
/*      */           }
/*      */         };
/* 1162 */         locatorImpl.setPublicId(e.getPublicId());
/* 1163 */         locatorImpl.setSystemId(e.getExpandedSystemId());
/* 1164 */         locatorImpl.setLineNumber(e.getLineNumber());
/* 1165 */         locatorImpl.setColumnNumber(e.getColumnNumber());
/* 1166 */         throw new SAXParseException(e.getMessage(), locatorImpl);
/*      */       }
/* 1168 */       if ((ex instanceof SAXException))
/*      */       {
/* 1170 */         throw ((SAXException)ex);
/*      */       }
/* 1172 */       if ((ex instanceof IOException)) {
/* 1173 */         throw ((IOException)ex);
/*      */       }
/* 1175 */       throw new SAXException(ex);
/*      */     }
/*      */     catch (XNIException e) {
/* 1178 */       Exception ex = e.getException();
/* 1179 */       if (ex == null) {
/* 1180 */         throw new SAXException(e.getMessage());
/*      */       }
/* 1182 */       if ((ex instanceof SAXException)) {
/* 1183 */         throw ((SAXException)ex);
/*      */       }
/* 1185 */       if ((ex instanceof IOException)) {
/* 1186 */         throw ((IOException)ex);
/*      */       }
/* 1188 */       throw new SAXException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parse(InputSource inputSource)
/*      */     throws SAXException, IOException
/*      */   {
/*      */     try
/*      */     {
/* 1206 */       XMLInputSource xmlInputSource = new XMLInputSource(inputSource.getPublicId(), inputSource.getSystemId(), null);
/*      */ 
/* 1210 */       xmlInputSource.setByteStream(inputSource.getByteStream());
/* 1211 */       xmlInputSource.setCharacterStream(inputSource.getCharacterStream());
/* 1212 */       xmlInputSource.setEncoding(inputSource.getEncoding());
/* 1213 */       parse(xmlInputSource);
/*      */     }
/*      */     catch (XMLParseException e)
/*      */     {
/* 1218 */       Exception ex = e.getException();
/* 1219 */       if (ex == null)
/*      */       {
/* 1222 */         LocatorImpl locatorImpl = new LocatorImpl() {
/*      */           public String getXMLVersion() {
/* 1224 */             return AbstractSAXParser.this.fVersion;
/*      */           }
/*      */ 
/*      */           public String getEncoding()
/*      */           {
/* 1232 */             return null;
/*      */           }
/*      */         };
/* 1235 */         locatorImpl.setPublicId(e.getPublicId());
/* 1236 */         locatorImpl.setSystemId(e.getExpandedSystemId());
/* 1237 */         locatorImpl.setLineNumber(e.getLineNumber());
/* 1238 */         locatorImpl.setColumnNumber(e.getColumnNumber());
/* 1239 */         throw new SAXParseException(e.getMessage(), locatorImpl);
/*      */       }
/* 1241 */       if ((ex instanceof SAXException))
/*      */       {
/* 1243 */         throw ((SAXException)ex);
/*      */       }
/* 1245 */       if ((ex instanceof IOException)) {
/* 1246 */         throw ((IOException)ex);
/*      */       }
/* 1248 */       throw new SAXException(ex);
/*      */     }
/*      */     catch (XNIException e) {
/* 1251 */       Exception ex = e.getException();
/* 1252 */       if (ex == null) {
/* 1253 */         throw new SAXException(e.getMessage());
/*      */       }
/* 1255 */       if ((ex instanceof SAXException)) {
/* 1256 */         throw ((SAXException)ex);
/*      */       }
/* 1258 */       if ((ex instanceof IOException)) {
/* 1259 */         throw ((IOException)ex);
/*      */       }
/* 1261 */       throw new SAXException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(EntityResolver resolver)
/*      */   {
/*      */     try
/*      */     {
/* 1276 */       XMLEntityResolver xer = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/* 1277 */       if ((this.fUseEntityResolver2) && ((resolver instanceof EntityResolver2))) {
/* 1278 */         if ((xer instanceof EntityResolver2Wrapper)) {
/* 1279 */           EntityResolver2Wrapper er2w = (EntityResolver2Wrapper)xer;
/* 1280 */           er2w.setEntityResolver((EntityResolver2)resolver);
/*      */         }
/*      */         else {
/* 1283 */           this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)resolver));
/*      */         }
/*      */ 
/*      */       }
/* 1288 */       else if ((xer instanceof EntityResolverWrapper)) {
/* 1289 */         EntityResolverWrapper erw = (EntityResolverWrapper)xer;
/* 1290 */         erw.setEntityResolver(resolver);
/*      */       }
/*      */       else {
/* 1293 */         this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(resolver));
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/* 1313 */     EntityResolver entityResolver = null;
/*      */     try {
/* 1315 */       XMLEntityResolver xmlEntityResolver = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/*      */ 
/* 1317 */       if (xmlEntityResolver != null) {
/* 1318 */         if ((xmlEntityResolver instanceof EntityResolverWrapper)) {
/* 1319 */           entityResolver = ((EntityResolverWrapper)xmlEntityResolver).getEntityResolver();
/*      */         }
/* 1322 */         else if ((xmlEntityResolver instanceof EntityResolver2Wrapper)) {
/* 1323 */           entityResolver = ((EntityResolver2Wrapper)xmlEntityResolver).getEntityResolver();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */ 
/* 1331 */     return entityResolver;
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(ErrorHandler errorHandler)
/*      */   {
/*      */     try
/*      */     {
/* 1354 */       XMLErrorHandler xeh = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
/* 1355 */       if ((xeh instanceof ErrorHandlerWrapper)) {
/* 1356 */         ErrorHandlerWrapper ehw = (ErrorHandlerWrapper)xeh;
/* 1357 */         ehw.setErrorHandler(errorHandler);
/*      */       }
/*      */       else {
/* 1360 */         this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(errorHandler));
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/* 1379 */     ErrorHandler errorHandler = null;
/*      */     try {
/* 1381 */       XMLErrorHandler xmlErrorHandler = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
/*      */ 
/* 1383 */       if ((xmlErrorHandler != null) && ((xmlErrorHandler instanceof ErrorHandlerWrapper)))
/*      */       {
/* 1385 */         errorHandler = ((ErrorHandlerWrapper)xmlErrorHandler).getErrorHandler();
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/* 1391 */     return errorHandler;
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws SAXException
/*      */   {
/* 1408 */     this.fConfiguration.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(DTDHandler dtdHandler)
/*      */   {
/* 1428 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(DocumentHandler documentHandler)
/*      */   {
/* 1450 */     this.fDocumentHandler = documentHandler;
/*      */   }
/*      */ 
/*      */   public void setContentHandler(ContentHandler contentHandler)
/*      */   {
/* 1473 */     this.fContentHandler = contentHandler;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/* 1485 */     return this.fContentHandler;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/* 1496 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*      */     try
/*      */     {
/* 1521 */       if (featureId.startsWith("http://xml.org/sax/features/")) {
/* 1522 */         int suffixLength = featureId.length() - "http://xml.org/sax/features/".length();
/*      */ 
/* 1525 */         if ((suffixLength == "namespaces".length()) && (featureId.endsWith("namespaces")))
/*      */         {
/* 1527 */           this.fConfiguration.setFeature(featureId, state);
/* 1528 */           this.fNamespaces = state;
/* 1529 */           return;
/*      */         }
/*      */ 
/* 1538 */         if ((suffixLength == "namespace-prefixes".length()) && (featureId.endsWith("namespace-prefixes")))
/*      */         {
/* 1540 */           this.fConfiguration.setFeature(featureId, state);
/* 1541 */           this.fNamespacePrefixes = state;
/* 1542 */           return;
/*      */         }
/*      */ 
/* 1549 */         if ((suffixLength == "string-interning".length()) && (featureId.endsWith("string-interning")))
/*      */         {
/* 1551 */           if (!state) {
/* 1552 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "false-not-supported", new Object[] { featureId }));
/*      */           }
/*      */ 
/* 1556 */           return;
/*      */         }
/*      */ 
/* 1563 */         if ((suffixLength == "lexical-handler/parameter-entities".length()) && (featureId.endsWith("lexical-handler/parameter-entities")))
/*      */         {
/* 1565 */           this.fLexicalHandlerParameterEntities = state;
/* 1566 */           return;
/*      */         }
/*      */ 
/* 1573 */         if ((suffixLength == "resolve-dtd-uris".length()) && (featureId.endsWith("resolve-dtd-uris")))
/*      */         {
/* 1575 */           this.fResolveDTDURIs = state;
/* 1576 */           return;
/*      */         }
/*      */ 
/* 1583 */         if ((suffixLength == "unicode-normalization-checking".length()) && (featureId.endsWith("unicode-normalization-checking")))
/*      */         {
/* 1587 */           if (state) {
/* 1588 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "true-not-supported", new Object[] { featureId }));
/*      */           }
/*      */ 
/* 1592 */           return;
/*      */         }
/*      */ 
/* 1599 */         if ((suffixLength == "xmlns-uris".length()) && (featureId.endsWith("xmlns-uris")))
/*      */         {
/* 1601 */           this.fXMLNSURIs = state;
/* 1602 */           return;
/*      */         }
/*      */ 
/* 1609 */         if ((suffixLength == "use-entity-resolver2".length()) && (featureId.endsWith("use-entity-resolver2")))
/*      */         {
/* 1611 */           if (state != this.fUseEntityResolver2) {
/* 1612 */             this.fUseEntityResolver2 = state;
/*      */ 
/* 1614 */             setEntityResolver(getEntityResolver());
/*      */           }
/* 1616 */           return;
/*      */         }
/*      */ 
/* 1633 */         if (((suffixLength == "is-standalone".length()) && (featureId.endsWith("is-standalone"))) || ((suffixLength == "use-attributes2".length()) && (featureId.endsWith("use-attributes2"))) || ((suffixLength == "use-locator2".length()) && (featureId.endsWith("use-locator2"))) || ((suffixLength == "xml-1.1".length()) && (featureId.endsWith("xml-1.1"))))
/*      */         {
/* 1641 */           throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-read-only", new Object[] { featureId }));
/*      */         }
/*      */ 
/*      */       }
/* 1651 */       else if ((featureId.equals("http://javax.xml.XMLConstants/feature/secure-processing")) && 
/* 1652 */         (state) && 
/* 1653 */         (this.fConfiguration.getProperty("http://apache.org/xml/properties/security-manager") == null)) {
/* 1654 */         this.fConfiguration.setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager());
/*      */       }
/*      */ 
/* 1663 */       this.fConfiguration.setFeature(featureId, state);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/* 1666 */       String identifier = e.getIdentifier();
/* 1667 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 1668 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[] { identifier }));
/*      */       }
/*      */ 
/* 1673 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[] { identifier })); }  } 
/*      */   public boolean getFeature(String featureId) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: ldc 24
/*      */     //   3: invokevirtual 778	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*      */     //   6: ifeq +264 -> 270
/*      */     //   9: aload_1
/*      */     //   10: invokevirtual 775	java/lang/String:length	()I
/*      */     //   13: ldc 24
/*      */     //   15: invokevirtual 775	java/lang/String:length	()I
/*      */     //   18: isub
/*      */     //   19: istore_2
/*      */     //   20: iload_2
/*      */     //   21: ldc 37
/*      */     //   23: invokevirtual 775	java/lang/String:length	()I
/*      */     //   26: if_icmpne +25 -> 51
/*      */     //   29: aload_1
/*      */     //   30: ldc 37
/*      */     //   32: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   35: ifeq +16 -> 51
/*      */     //   38: aload_0
/*      */     //   39: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   42: aload_1
/*      */     //   43: invokeinterface 811 2 0
/*      */     //   48: istore_3
/*      */     //   49: iload_3
/*      */     //   50: ireturn
/*      */     //   51: iload_2
/*      */     //   52: ldc 46
/*      */     //   54: invokevirtual 775	java/lang/String:length	()I
/*      */     //   57: if_icmpne +14 -> 71
/*      */     //   60: aload_1
/*      */     //   61: ldc 46
/*      */     //   63: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   66: ifeq +5 -> 71
/*      */     //   69: iconst_1
/*      */     //   70: ireturn
/*      */     //   71: iload_2
/*      */     //   72: ldc 34
/*      */     //   74: invokevirtual 775	java/lang/String:length	()I
/*      */     //   77: if_icmpne +17 -> 94
/*      */     //   80: aload_1
/*      */     //   81: ldc 34
/*      */     //   83: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   86: ifeq +8 -> 94
/*      */     //   89: aload_0
/*      */     //   90: getfield 695	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fStandalone	Z
/*      */     //   93: ireturn
/*      */     //   94: iload_2
/*      */     //   95: ldc 52
/*      */     //   97: invokevirtual 775	java/lang/String:length	()I
/*      */     //   100: if_icmpne +20 -> 120
/*      */     //   103: aload_1
/*      */     //   104: ldc 52
/*      */     //   106: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   109: ifeq +11 -> 120
/*      */     //   112: aload_0
/*      */     //   113: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   116: instanceof 408
/*      */     //   119: ireturn
/*      */     //   120: iload_2
/*      */     //   121: ldc 36
/*      */     //   123: invokevirtual 775	java/lang/String:length	()I
/*      */     //   126: if_icmpne +17 -> 143
/*      */     //   129: aload_1
/*      */     //   130: ldc 36
/*      */     //   132: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   135: ifeq +8 -> 143
/*      */     //   138: aload_0
/*      */     //   139: getfield 690	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fLexicalHandlerParameterEntities	Z
/*      */     //   142: ireturn
/*      */     //   143: iload_2
/*      */     //   144: ldc 45
/*      */     //   146: invokevirtual 775	java/lang/String:length	()I
/*      */     //   149: if_icmpne +17 -> 166
/*      */     //   152: aload_1
/*      */     //   153: ldc 45
/*      */     //   155: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   158: ifeq +8 -> 166
/*      */     //   161: aload_0
/*      */     //   162: getfield 694	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fResolveDTDURIs	Z
/*      */     //   165: ireturn
/*      */     //   166: iload_2
/*      */     //   167: ldc 53
/*      */     //   169: invokevirtual 775	java/lang/String:length	()I
/*      */     //   172: if_icmpne +17 -> 189
/*      */     //   175: aload_1
/*      */     //   176: ldc 53
/*      */     //   178: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   181: ifeq +8 -> 189
/*      */     //   184: aload_0
/*      */     //   185: getfield 697	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fXMLNSURIs	Z
/*      */     //   188: ireturn
/*      */     //   189: iload_2
/*      */     //   190: ldc 48
/*      */     //   192: invokevirtual 775	java/lang/String:length	()I
/*      */     //   195: if_icmpne +14 -> 209
/*      */     //   198: aload_1
/*      */     //   199: ldc 48
/*      */     //   201: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   204: ifeq +5 -> 209
/*      */     //   207: iconst_0
/*      */     //   208: ireturn
/*      */     //   209: iload_2
/*      */     //   210: ldc 50
/*      */     //   212: invokevirtual 775	java/lang/String:length	()I
/*      */     //   215: if_icmpne +17 -> 232
/*      */     //   218: aload_1
/*      */     //   219: ldc 50
/*      */     //   221: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   224: ifeq +8 -> 232
/*      */     //   227: aload_0
/*      */     //   228: getfield 696	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fUseEntityResolver2	Z
/*      */     //   231: ireturn
/*      */     //   232: iload_2
/*      */     //   233: ldc 49
/*      */     //   235: invokevirtual 775	java/lang/String:length	()I
/*      */     //   238: if_icmpne +12 -> 250
/*      */     //   241: aload_1
/*      */     //   242: ldc 49
/*      */     //   244: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   247: ifne +21 -> 268
/*      */     //   250: iload_2
/*      */     //   251: ldc 51
/*      */     //   253: invokevirtual 775	java/lang/String:length	()I
/*      */     //   256: if_icmpne +14 -> 270
/*      */     //   259: aload_1
/*      */     //   260: ldc 51
/*      */     //   262: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   265: ifeq +5 -> 270
/*      */     //   268: iconst_1
/*      */     //   269: ireturn
/*      */     //   270: aload_0
/*      */     //   271: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   274: aload_1
/*      */     //   275: invokeinterface 811 2 0
/*      */     //   280: ireturn
/*      */     //   281: astore_2
/*      */     //   282: aload_2
/*      */     //   283: invokevirtual 763	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*      */     //   286: astore_3
/*      */     //   287: aload_2
/*      */     //   288: invokevirtual 762	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*      */     //   291: getstatic 714	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*      */     //   294: if_acmpne +33 -> 327
/*      */     //   297: new 448	org/xml/sax/SAXNotRecognizedException
/*      */     //   300: dup
/*      */     //   301: aload_0
/*      */     //   302: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   305: invokeinterface 815 1 0
/*      */     //   310: ldc 17
/*      */     //   312: iconst_1
/*      */     //   313: anewarray 437	java/lang/Object
/*      */     //   316: dup
/*      */     //   317: iconst_0
/*      */     //   318: aload_3
/*      */     //   319: aastore
/*      */     //   320: invokestatic 751	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   323: invokespecial 791	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*      */     //   326: athrow
/*      */     //   327: new 449	org/xml/sax/SAXNotSupportedException
/*      */     //   330: dup
/*      */     //   331: aload_0
/*      */     //   332: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   335: invokeinterface 815 1 0
/*      */     //   340: ldc 18
/*      */     //   342: iconst_1
/*      */     //   343: anewarray 437	java/lang/Object
/*      */     //   346: dup
/*      */     //   347: iconst_0
/*      */     //   348: aload_3
/*      */     //   349: aastore
/*      */     //   350: invokestatic 751	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   353: invokespecial 792	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*      */     //   356: athrow
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	50	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   51	70	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   71	93	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   94	119	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   120	142	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   143	165	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   166	188	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   189	208	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   209	231	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   232	269	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   270	280	281	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/*      */   public void setProperty(String propertyId, Object value) throws SAXNotRecognizedException, SAXNotSupportedException { try { if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 1862 */         int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*      */ 
/* 1870 */         if ((suffixLength == "lexical-handler".length()) && (propertyId.endsWith("lexical-handler")))
/*      */         {
/*      */           try {
/* 1873 */             setLexicalHandler((LexicalHandler)value);
/*      */           }
/*      */           catch (ClassCastException e) {
/* 1876 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[] { propertyId, "org.xml.sax.ext.LexicalHandler" }));
/*      */           }
/*      */ 
/* 1880 */           return;
/*      */         }
/*      */ 
/* 1888 */         if ((suffixLength == "declaration-handler".length()) && (propertyId.endsWith("declaration-handler")))
/*      */         {
/*      */           try {
/* 1891 */             setDeclHandler((DeclHandler)value);
/*      */           }
/*      */           catch (ClassCastException e) {
/* 1894 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[] { propertyId, "org.xml.sax.ext.DeclHandler" }));
/*      */           }
/*      */ 
/* 1898 */           return;
/*      */         }
/*      */ 
/* 1914 */         if (((suffixLength == "dom-node".length()) && (propertyId.endsWith("dom-node"))) || ((suffixLength == "document-xml-version".length()) && (propertyId.endsWith("document-xml-version"))))
/*      */         {
/* 1918 */           throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-read-only", new Object[] { propertyId }));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1943 */       this.fConfiguration.setProperty(propertyId, value);
/*      */     } catch (XMLConfigurationException e)
/*      */     {
/* 1946 */       String identifier = e.getIdentifier();
/* 1947 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 1948 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[] { identifier }));
/*      */       }
/*      */ 
/* 1953 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[] { identifier })); }  } 
/*      */   public Object getProperty(String propertyId) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: ldc 29
/*      */     //   3: invokevirtual 778	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*      */     //   6: ifeq +124 -> 130
/*      */     //   9: aload_1
/*      */     //   10: invokevirtual 775	java/lang/String:length	()I
/*      */     //   13: ldc 29
/*      */     //   15: invokevirtual 775	java/lang/String:length	()I
/*      */     //   18: isub
/*      */     //   19: istore_2
/*      */     //   20: iload_2
/*      */     //   21: ldc 13
/*      */     //   23: invokevirtual 775	java/lang/String:length	()I
/*      */     //   26: if_icmpne +17 -> 43
/*      */     //   29: aload_1
/*      */     //   30: ldc 13
/*      */     //   32: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   35: ifeq +8 -> 43
/*      */     //   38: aload_0
/*      */     //   39: getfield 705	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fVersion	Ljava/lang/String;
/*      */     //   42: areturn
/*      */     //   43: iload_2
/*      */     //   44: ldc 35
/*      */     //   46: invokevirtual 775	java/lang/String:length	()I
/*      */     //   49: if_icmpne +17 -> 66
/*      */     //   52: aload_1
/*      */     //   53: ldc 35
/*      */     //   55: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   58: ifeq +8 -> 66
/*      */     //   61: aload_0
/*      */     //   62: invokevirtual 731	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:getLexicalHandler	()Lorg/xml/sax/ext/LexicalHandler;
/*      */     //   65: areturn
/*      */     //   66: iload_2
/*      */     //   67: ldc 12
/*      */     //   69: invokevirtual 775	java/lang/String:length	()I
/*      */     //   72: if_icmpne +17 -> 89
/*      */     //   75: aload_1
/*      */     //   76: ldc 12
/*      */     //   78: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   81: ifeq +8 -> 89
/*      */     //   84: aload_0
/*      */     //   85: invokevirtual 729	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:getDeclHandler	()Lorg/xml/sax/ext/DeclHandler;
/*      */     //   88: areturn
/*      */     //   89: iload_2
/*      */     //   90: ldc 14
/*      */     //   92: invokevirtual 775	java/lang/String:length	()I
/*      */     //   95: if_icmpne +35 -> 130
/*      */     //   98: aload_1
/*      */     //   99: ldc 14
/*      */     //   101: invokevirtual 777	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   104: ifeq +26 -> 130
/*      */     //   107: new 449	org/xml/sax/SAXNotSupportedException
/*      */     //   110: dup
/*      */     //   111: aload_0
/*      */     //   112: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   115: invokeinterface 815 1 0
/*      */     //   120: ldc 15
/*      */     //   122: aconst_null
/*      */     //   123: invokestatic 751	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   126: invokespecial 792	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*      */     //   129: athrow
/*      */     //   130: aload_0
/*      */     //   131: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   134: aload_1
/*      */     //   135: invokeinterface 817 2 0
/*      */     //   140: areturn
/*      */     //   141: astore_2
/*      */     //   142: aload_2
/*      */     //   143: invokevirtual 763	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*      */     //   146: astore_3
/*      */     //   147: aload_2
/*      */     //   148: invokevirtual 762	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*      */     //   151: getstatic 714	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*      */     //   154: if_acmpne +33 -> 187
/*      */     //   157: new 448	org/xml/sax/SAXNotRecognizedException
/*      */     //   160: dup
/*      */     //   161: aload_0
/*      */     //   162: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   165: invokeinterface 815 1 0
/*      */     //   170: ldc 42
/*      */     //   172: iconst_1
/*      */     //   173: anewarray 437	java/lang/Object
/*      */     //   176: dup
/*      */     //   177: iconst_0
/*      */     //   178: aload_3
/*      */     //   179: aastore
/*      */     //   180: invokestatic 751	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   183: invokespecial 791	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*      */     //   186: athrow
/*      */     //   187: new 449	org/xml/sax/SAXNotSupportedException
/*      */     //   190: dup
/*      */     //   191: aload_0
/*      */     //   192: getfield 704	com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*      */     //   195: invokeinterface 815 1 0
/*      */     //   200: ldc 43
/*      */     //   202: iconst_1
/*      */     //   203: anewarray 437	java/lang/Object
/*      */     //   206: dup
/*      */     //   207: iconst_0
/*      */     //   208: aload_3
/*      */     //   209: aastore
/*      */     //   210: invokestatic 751	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   213: invokespecial 792	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*      */     //   216: athrow
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	42	141	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   43	65	141	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   66	88	141	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*      */     //   89	140	141	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/* 2097 */   protected void setDeclHandler(DeclHandler handler) throws SAXNotRecognizedException, SAXNotSupportedException { if (this.fParseInProgress) {
/* 2098 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[] { "http://xml.org/sax/properties/declaration-handler" }));
/*      */     }
/*      */ 
/* 2103 */     this.fDeclHandler = handler;
/*      */   }
/*      */ 
/*      */   protected DeclHandler getDeclHandler()
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/* 2114 */     return this.fDeclHandler;
/*      */   }
/*      */ 
/*      */   protected void setLexicalHandler(LexicalHandler handler)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/* 2133 */     if (this.fParseInProgress) {
/* 2134 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[] { "http://xml.org/sax/properties/lexical-handler" }));
/*      */     }
/*      */ 
/* 2139 */     this.fLexicalHandler = handler;
/*      */   }
/*      */ 
/*      */   protected LexicalHandler getLexicalHandler()
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/* 2150 */     return this.fLexicalHandler;
/*      */   }
/*      */ 
/*      */   protected final void startNamespaceMapping()
/*      */     throws SAXException
/*      */   {
/* 2157 */     int count = this.fNamespaceContext.getDeclaredPrefixCount();
/* 2158 */     if (count > 0) {
/* 2159 */       String prefix = null;
/* 2160 */       String uri = null;
/* 2161 */       for (int i = 0; i < count; i++) {
/* 2162 */         prefix = this.fNamespaceContext.getDeclaredPrefixAt(i);
/* 2163 */         uri = this.fNamespaceContext.getURI(prefix);
/* 2164 */         this.fContentHandler.startPrefixMapping(prefix, uri == null ? "" : uri);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void endNamespaceMapping()
/*      */     throws SAXException
/*      */   {
/* 2174 */     int count = this.fNamespaceContext.getDeclaredPrefixCount();
/* 2175 */     if (count > 0)
/* 2176 */       for (int i = 0; i < count; i++)
/* 2177 */         this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(i));
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws XNIException
/*      */   {
/* 2192 */     super.reset();
/*      */ 
/* 2195 */     this.fInDTD = false;
/* 2196 */     this.fVersion = "1.0";
/* 2197 */     this.fStandalone = false;
/*      */ 
/* 2200 */     this.fNamespaces = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
/* 2201 */     this.fNamespacePrefixes = this.fConfiguration.getFeature("http://xml.org/sax/features/namespace-prefixes");
/* 2202 */     this.fAugmentations = null;
/* 2203 */     this.fDeclaredAttrs = null;
/*      */   }
/*      */ 
/*      */   public ElementPSVI getElementPSVI()
/*      */   {
/* 2402 */     return this.fAugmentations != null ? (ElementPSVI)this.fAugmentations.getItem("ELEMENT_PSVI") : null;
/*      */   }
/*      */ 
/*      */   public AttributePSVI getAttributePSVI(int index)
/*      */   {
/* 2408 */     return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(index).getItem("ATTRIBUTE_PSVI");
/*      */   }
/*      */ 
/*      */   public AttributePSVI getAttributePSVIByName(String uri, String localname)
/*      */   {
/* 2414 */     return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(uri, localname).getItem("ATTRIBUTE_PSVI");
/*      */   }
/*      */ 
/*      */   protected static final class AttributesProxy
/*      */     implements AttributeList, Attributes2
/*      */   {
/*      */     protected XMLAttributes fAttributes;
/*      */ 
/*      */     public void setAttributes(XMLAttributes attributes)
/*      */     {
/* 2280 */       this.fAttributes = attributes;
/*      */     }
/*      */ 
/*      */     public int getLength() {
/* 2284 */       return this.fAttributes.getLength();
/*      */     }
/*      */ 
/*      */     public String getName(int i) {
/* 2288 */       return this.fAttributes.getQName(i);
/*      */     }
/*      */ 
/*      */     public String getQName(int index) {
/* 2292 */       return this.fAttributes.getQName(index);
/*      */     }
/*      */ 
/*      */     public String getURI(int index)
/*      */     {
/* 2299 */       String uri = this.fAttributes.getURI(index);
/* 2300 */       return uri != null ? uri : "";
/*      */     }
/*      */ 
/*      */     public String getLocalName(int index) {
/* 2304 */       return this.fAttributes.getLocalName(index);
/*      */     }
/*      */ 
/*      */     public String getType(int i) {
/* 2308 */       return this.fAttributes.getType(i);
/*      */     }
/*      */ 
/*      */     public String getType(String name) {
/* 2312 */       return this.fAttributes.getType(name);
/*      */     }
/*      */ 
/*      */     public String getType(String uri, String localName) {
/* 2316 */       return uri.equals("") ? this.fAttributes.getType(null, localName) : this.fAttributes.getType(uri, localName);
/*      */     }
/*      */ 
/*      */     public String getValue(int i)
/*      */     {
/* 2321 */       return this.fAttributes.getValue(i);
/*      */     }
/*      */ 
/*      */     public String getValue(String name) {
/* 2325 */       return this.fAttributes.getValue(name);
/*      */     }
/*      */ 
/*      */     public String getValue(String uri, String localName) {
/* 2329 */       return uri.equals("") ? this.fAttributes.getValue(null, localName) : this.fAttributes.getValue(uri, localName);
/*      */     }
/*      */ 
/*      */     public int getIndex(String qName)
/*      */     {
/* 2334 */       return this.fAttributes.getIndex(qName);
/*      */     }
/*      */ 
/*      */     public int getIndex(String uri, String localPart) {
/* 2338 */       return uri.equals("") ? this.fAttributes.getIndex(null, localPart) : this.fAttributes.getIndex(uri, localPart);
/*      */     }
/*      */ 
/*      */     public boolean isDeclared(int index)
/*      */     {
/* 2345 */       if ((index < 0) || (index >= this.fAttributes.getLength())) {
/* 2346 */         throw new ArrayIndexOutOfBoundsException(index);
/*      */       }
/* 2348 */       return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem("ATTRIBUTE_DECLARED"));
/*      */     }
/*      */ 
/*      */     public boolean isDeclared(String qName)
/*      */     {
/* 2354 */       int index = getIndex(qName);
/* 2355 */       if (index == -1) {
/* 2356 */         throw new IllegalArgumentException(qName);
/*      */       }
/* 2358 */       return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem("ATTRIBUTE_DECLARED"));
/*      */     }
/*      */ 
/*      */     public boolean isDeclared(String uri, String localName)
/*      */     {
/* 2364 */       int index = getIndex(uri, localName);
/* 2365 */       if (index == -1) {
/* 2366 */         throw new IllegalArgumentException(localName);
/*      */       }
/* 2368 */       return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem("ATTRIBUTE_DECLARED"));
/*      */     }
/*      */ 
/*      */     public boolean isSpecified(int index)
/*      */     {
/* 2374 */       if ((index < 0) || (index >= this.fAttributes.getLength())) {
/* 2375 */         throw new ArrayIndexOutOfBoundsException(index);
/*      */       }
/* 2377 */       return this.fAttributes.isSpecified(index);
/*      */     }
/*      */ 
/*      */     public boolean isSpecified(String qName) {
/* 2381 */       int index = getIndex(qName);
/* 2382 */       if (index == -1) {
/* 2383 */         throw new IllegalArgumentException(qName);
/*      */       }
/* 2385 */       return this.fAttributes.isSpecified(index);
/*      */     }
/*      */ 
/*      */     public boolean isSpecified(String uri, String localName) {
/* 2389 */       int index = getIndex(uri, localName);
/* 2390 */       if (index == -1) {
/* 2391 */         throw new IllegalArgumentException(localName);
/*      */       }
/* 2393 */       return this.fAttributes.isSpecified(index);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class LocatorProxy
/*      */     implements Locator2
/*      */   {
/*      */     protected XMLLocator fLocator;
/*      */ 
/*      */     public LocatorProxy(XMLLocator locator)
/*      */     {
/* 2227 */       this.fLocator = locator;
/*      */     }
/*      */ 
/*      */     public String getPublicId()
/*      */     {
/* 2236 */       return this.fLocator.getPublicId();
/*      */     }
/*      */ 
/*      */     public String getSystemId()
/*      */     {
/* 2241 */       return this.fLocator.getExpandedSystemId();
/*      */     }
/*      */ 
/*      */     public int getLineNumber() {
/* 2245 */       return this.fLocator.getLineNumber();
/*      */     }
/*      */ 
/*      */     public int getColumnNumber()
/*      */     {
/* 2250 */       return this.fLocator.getColumnNumber();
/*      */     }
/*      */ 
/*      */     public String getXMLVersion()
/*      */     {
/* 2255 */       return this.fLocator.getXMLVersion();
/*      */     }
/*      */ 
/*      */     public String getEncoding() {
/* 2259 */       return this.fLocator.getEncoding();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser
 * JD-Core Version:    0.6.2
 */