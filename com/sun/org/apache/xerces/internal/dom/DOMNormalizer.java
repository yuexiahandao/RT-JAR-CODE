/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*      */ import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDDescription;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*      */ import com.sun.org.apache.xerces.internal.parsers.XMLGrammarPreparser;
/*      */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import java.io.IOException;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Comment;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Entity;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.ProcessingInstruction;
/*      */ import org.w3c.dom.Text;
/*      */ 
/*      */ public class DOMNormalizer
/*      */   implements XMLDocumentHandler
/*      */ {
/*      */   protected static final boolean DEBUG_ND = false;
/*      */   protected static final boolean DEBUG = false;
/*      */   protected static final boolean DEBUG_EVENTS = false;
/*      */   protected static final String PREFIX = "NS";
/*  117 */   protected DOMConfigurationImpl fConfiguration = null;
/*  118 */   protected CoreDocumentImpl fDocument = null;
/*  119 */   protected final XMLAttributesProxy fAttrProxy = new XMLAttributesProxy();
/*  120 */   protected final QName fQName = new QName();
/*      */   protected RevalidationHandler fValidationHandler;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected DOMErrorHandler fErrorHandler;
/*  134 */   private final DOMErrorImpl fError = new DOMErrorImpl();
/*      */ 
/*  137 */   protected boolean fNamespaceValidation = false;
/*      */ 
/*  140 */   protected boolean fPSVI = false;
/*      */ 
/*  143 */   protected final NamespaceContext fNamespaceContext = new NamespaceSupport();
/*      */ 
/*  146 */   protected final NamespaceContext fLocalNSBinder = new NamespaceSupport();
/*      */ 
/*  149 */   protected final ArrayList fAttributeList = new ArrayList(5);
/*      */ 
/*  152 */   protected final DOMLocatorImpl fLocator = new DOMLocatorImpl();
/*      */ 
/*  155 */   protected Node fCurrentNode = null;
/*  156 */   private QName fAttrQName = new QName();
/*      */ 
/*  159 */   final XMLString fNormalizedValue = new XMLString(new char[16], 0, 0);
/*      */ 
/*  164 */   public static final RuntimeException abort = new RuntimeException();
/*      */   private XMLDTDValidator fDTDValidator;
/*  170 */   private boolean allWhitespace = false;
/*      */ 
/*      */   protected void normalizeDocument(CoreDocumentImpl document, DOMConfigurationImpl config)
/*      */   {
/*  185 */     this.fDocument = document;
/*  186 */     this.fConfiguration = config;
/*      */ 
/*  190 */     this.fSymbolTable = ((SymbolTable)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*      */ 
/*  192 */     this.fNamespaceContext.reset();
/*  193 */     this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*      */ 
/*  195 */     if ((this.fConfiguration.features & 0x40) != 0) {
/*  196 */       String schemaLang = (String)this.fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
/*      */ 
/*  198 */       if ((schemaLang != null) && (schemaLang.equals(Constants.NS_XMLSCHEMA))) {
/*  199 */         this.fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator("http://www.w3.org/2001/XMLSchema");
/*      */ 
/*  201 */         this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
/*  202 */         this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
/*      */ 
/*  204 */         this.fNamespaceValidation = true;
/*      */ 
/*  207 */         this.fPSVI = ((this.fConfiguration.features & 0x80) != 0);
/*      */       }
/*      */ 
/*  210 */       this.fConfiguration.setFeature("http://xml.org/sax/features/validation", true);
/*      */ 
/*  213 */       this.fDocument.clearIdentifiers();
/*      */ 
/*  215 */       if (this.fValidationHandler != null)
/*      */       {
/*  217 */         ((XMLComponent)this.fValidationHandler).reset(this.fConfiguration);
/*      */       }
/*      */     }
/*      */ 
/*  221 */     this.fErrorHandler = ((DOMErrorHandler)this.fConfiguration.getParameter("error-handler"));
/*  222 */     if (this.fValidationHandler != null) {
/*  223 */       this.fValidationHandler.setDocumentHandler(this);
/*  224 */       this.fValidationHandler.startDocument(new SimpleLocator(this.fDocument.fDocumentURI, this.fDocument.fDocumentURI, -1, -1), this.fDocument.encoding, this.fNamespaceContext, null);
/*      */     }
/*      */     try
/*      */     {
/*      */       Node next;
/*  231 */       for (Node kid = this.fDocument.getFirstChild(); kid != null; kid = next) {
/*  232 */         next = kid.getNextSibling();
/*  233 */         kid = normalizeNode(kid);
/*  234 */         if (kid != null) {
/*  235 */           next = kid;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  240 */       if (this.fValidationHandler != null) {
/*  241 */         this.fValidationHandler.endDocument(null);
/*  242 */         CoreDOMImplementationImpl.singleton.releaseValidator("http://www.w3.org/2001/XMLSchema", this.fValidationHandler);
/*      */ 
/*  244 */         this.fValidationHandler = null;
/*      */       }
/*      */     }
/*      */     catch (RuntimeException e) {
/*  248 */       if (e == abort)
/*  249 */         return;
/*  250 */       throw e;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Node normalizeNode(Node node)
/*      */   {
/*  273 */     int type = node.getNodeType();
/*      */ 
/*  275 */     this.fLocator.fRelatedNode = node;
/*      */ 
/*  277 */     switch (type)
/*      */     {
/*      */     case 10:
/*  282 */       DocumentTypeImpl docType = (DocumentTypeImpl)node;
/*  283 */       this.fDTDValidator = ((XMLDTDValidator)CoreDOMImplementationImpl.singleton.getValidator("http://www.w3.org/TR/REC-xml"));
/*  284 */       this.fDTDValidator.setDocumentHandler(this);
/*  285 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", createGrammarPool(docType));
/*  286 */       this.fDTDValidator.reset(this.fConfiguration);
/*  287 */       this.fDTDValidator.startDocument(new SimpleLocator(this.fDocument.fDocumentURI, this.fDocument.fDocumentURI, -1, -1), this.fDocument.encoding, this.fNamespaceContext, null);
/*      */ 
/*  290 */       this.fDTDValidator.doctypeDecl(docType.getName(), docType.getPublicId(), docType.getSystemId(), null);
/*      */ 
/*  292 */       break;
/*      */     case 1:
/*  302 */       if ((this.fDocument.errorChecking) && 
/*  303 */         ((this.fConfiguration.features & 0x100) != 0) && (this.fDocument.isXMLVersionChanged()))
/*      */       {
/*      */         boolean wellformed;
/*      */         boolean wellformed;
/*  305 */         if (this.fNamespaceValidation) {
/*  306 */           wellformed = CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), this.fDocument.isXML11Version());
/*      */         }
/*      */         else {
/*  309 */           wellformed = CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
/*      */         }
/*  311 */         if (!wellformed) {
/*  312 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", node.getNodeName() });
/*      */ 
/*  316 */           reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "wf-invalid-character-in-node-name");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  322 */       this.fNamespaceContext.pushContext();
/*  323 */       this.fLocalNSBinder.reset();
/*      */ 
/*  325 */       ElementImpl elem = (ElementImpl)node;
/*  326 */       if (elem.needsSyncChildren()) {
/*  327 */         elem.synchronizeChildren();
/*      */       }
/*  329 */       AttributeMap attributes = elem.hasAttributes() ? (AttributeMap)elem.getAttributes() : null;
/*      */ 
/*  332 */       if ((this.fConfiguration.features & 0x1) != 0)
/*      */       {
/*  336 */         namespaceFixUp(elem, attributes);
/*      */ 
/*  338 */         if (((this.fConfiguration.features & 0x200) == 0) && (attributes != null)) {
/*  339 */           for (int i = 0; i < attributes.getLength(); i++) {
/*  340 */             Attr att = (Attr)attributes.getItem(i);
/*  341 */             if ((XMLSymbols.PREFIX_XMLNS.equals(att.getPrefix())) || (XMLSymbols.PREFIX_XMLNS.equals(att.getName())))
/*      */             {
/*  343 */               elem.removeAttributeNode(att);
/*  344 */               i--;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*  350 */       else if (attributes != null) {
/*  351 */         for (int i = 0; i < attributes.getLength(); i++) {
/*  352 */           Attr attr = (Attr)attributes.item(i);
/*      */ 
/*  354 */           attr.normalize();
/*  355 */           if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0)) {
/*  356 */             isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributes, (AttrImpl)attr, attr.getValue(), this.fDocument.isXML11Version());
/*  357 */             if (this.fDocument.isXMLVersionChanged()) {
/*  358 */               boolean wellformed = CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
/*  359 */               if (!wellformed) {
/*  360 */                 String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Attr", node.getNodeName() });
/*      */ 
/*  364 */                 reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "wf-invalid-character-in-node-name");
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  374 */       if (this.fValidationHandler != null)
/*      */       {
/*  378 */         this.fAttrProxy.setAttributes(attributes, this.fDocument, elem);
/*  379 */         updateQName(elem, this.fQName);
/*      */ 
/*  382 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  383 */         this.fCurrentNode = node;
/*      */ 
/*  385 */         this.fValidationHandler.startElement(this.fQName, this.fAttrProxy, null);
/*      */       }
/*      */ 
/*  388 */       if (this.fDTDValidator != null)
/*      */       {
/*  392 */         this.fAttrProxy.setAttributes(attributes, this.fDocument, elem);
/*  393 */         updateQName(elem, this.fQName);
/*      */ 
/*  396 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  397 */         this.fCurrentNode = node;
/*      */ 
/*  399 */         this.fDTDValidator.startElement(this.fQName, this.fAttrProxy, null);
/*      */       }
/*      */       Node next;
/*  404 */       for (Node kid = elem.getFirstChild(); kid != null; kid = next) {
/*  405 */         next = kid.getNextSibling();
/*  406 */         kid = normalizeNode(kid);
/*  407 */         if (kid != null) {
/*  408 */           next = kid;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  422 */       if (this.fValidationHandler != null) {
/*  423 */         updateQName(elem, this.fQName);
/*      */ 
/*  427 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  428 */         this.fCurrentNode = node;
/*  429 */         this.fValidationHandler.endElement(this.fQName, null);
/*      */       }
/*      */ 
/*  432 */       if (this.fDTDValidator != null) {
/*  433 */         updateQName(elem, this.fQName);
/*      */ 
/*  437 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  438 */         this.fCurrentNode = node;
/*  439 */         this.fDTDValidator.endElement(this.fQName, null);
/*      */       }
/*      */ 
/*  443 */       this.fNamespaceContext.popContext();
/*      */ 
/*  445 */       break;
/*      */     case 8:
/*  453 */       if ((this.fConfiguration.features & 0x20) == 0) {
/*  454 */         Node prevSibling = node.getPreviousSibling();
/*  455 */         Node parent = node.getParentNode();
/*      */ 
/*  457 */         parent.removeChild(node);
/*  458 */         if ((prevSibling != null) && (prevSibling.getNodeType() == 3)) {
/*  459 */           Node nextSibling = prevSibling.getNextSibling();
/*  460 */           if ((nextSibling != null) && (nextSibling.getNodeType() == 3)) {
/*  461 */             ((TextImpl)nextSibling).insertData(0, prevSibling.getNodeValue());
/*  462 */             parent.removeChild(prevSibling);
/*  463 */             return nextSibling;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*  468 */       else if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0)) {
/*  469 */         String commentdata = ((Comment)node).getData();
/*      */ 
/*  472 */         isCommentWF(this.fErrorHandler, this.fError, this.fLocator, commentdata, this.fDocument.isXML11Version());
/*  473 */       }break;
/*      */     case 5:
/*  482 */       if ((this.fConfiguration.features & 0x4) == 0) {
/*  483 */         Node prevSibling = node.getPreviousSibling();
/*  484 */         Node parent = node.getParentNode();
/*  485 */         ((EntityReferenceImpl)node).setReadOnly(false, true);
/*  486 */         expandEntityRef(parent, node);
/*  487 */         parent.removeChild(node);
/*  488 */         Node next = prevSibling != null ? prevSibling.getNextSibling() : parent.getFirstChild();
/*      */ 
/*  492 */         if ((prevSibling != null) && (next != null) && (prevSibling.getNodeType() == 3) && (next.getNodeType() == 3))
/*      */         {
/*  494 */           return prevSibling;
/*      */         }
/*  496 */         return next;
/*      */       }
/*  498 */       if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0) && (this.fDocument.isXMLVersionChanged()))
/*      */       {
/*  500 */         CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version()); } break;
/*      */     case 4:
/*  513 */       if ((this.fConfiguration.features & 0x8) == 0)
/*      */       {
/*  515 */         Node prevSibling = node.getPreviousSibling();
/*  516 */         if ((prevSibling != null) && (prevSibling.getNodeType() == 3)) {
/*  517 */           ((Text)prevSibling).appendData(node.getNodeValue());
/*  518 */           node.getParentNode().removeChild(node);
/*  519 */           return prevSibling;
/*      */         }
/*      */ 
/*  522 */         Text text = this.fDocument.createTextNode(node.getNodeValue());
/*  523 */         Node parent = node.getParentNode();
/*  524 */         node = parent.replaceChild(text, node);
/*  525 */         return text;
/*      */       }
/*      */ 
/*  531 */       if (this.fValidationHandler != null)
/*      */       {
/*  534 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  535 */         this.fCurrentNode = node;
/*  536 */         this.fValidationHandler.startCDATA(null);
/*  537 */         this.fValidationHandler.characterData(node.getNodeValue(), null);
/*  538 */         this.fValidationHandler.endCDATA(null);
/*      */       }
/*      */ 
/*  541 */       if (this.fDTDValidator != null)
/*      */       {
/*  544 */         this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  545 */         this.fCurrentNode = node;
/*  546 */         this.fDTDValidator.startCDATA(null);
/*  547 */         this.fDTDValidator.characterData(node.getNodeValue(), null);
/*  548 */         this.fDTDValidator.endCDATA(null);
/*      */       }
/*  550 */       String value = node.getNodeValue();
/*      */ 
/*  552 */       if ((this.fConfiguration.features & 0x10) != 0)
/*      */       {
/*  554 */         Node parent = node.getParentNode();
/*  555 */         if (this.fDocument.errorChecking)
/*  556 */           isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
/*      */         int index;
/*  558 */         while ((index = value.indexOf("]]>")) >= 0) {
/*  559 */           node.setNodeValue(value.substring(0, index + 2));
/*  560 */           value = value.substring(index + 2);
/*      */ 
/*  562 */           Node firstSplitNode = node;
/*  563 */           Node newChild = this.fDocument.createCDATASection(value);
/*  564 */           parent.insertBefore(newChild, node.getNextSibling());
/*  565 */           node = newChild;
/*      */ 
/*  567 */           this.fLocator.fRelatedNode = firstSplitNode;
/*  568 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "cdata-sections-splitted", null);
/*      */ 
/*  572 */           reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)1, "cdata-sections-splitted");
/*      */         }
/*      */ 
/*      */       }
/*  577 */       else if (this.fDocument.errorChecking)
/*      */       {
/*  579 */         isCDataWF(this.fErrorHandler, this.fError, this.fLocator, value, this.fDocument.isXML11Version()); } break;
/*      */     case 3:
/*  593 */       Node next = node.getNextSibling();
/*      */ 
/*  595 */       if ((next != null) && (next.getNodeType() == 3)) {
/*  596 */         ((Text)node).appendData(next.getNodeValue());
/*  597 */         node.getParentNode().removeChild(next);
/*      */ 
/*  601 */         return node;
/*  602 */       }if (node.getNodeValue().length() == 0)
/*      */       {
/*  604 */         node.getParentNode().removeChild(node);
/*      */       }
/*      */       else
/*      */       {
/*  612 */         short nextType = next != null ? next.getNodeType() : -1;
/*  613 */         if ((nextType == -1) || ((((this.fConfiguration.features & 0x4) != 0) || (nextType != 6)) && (((this.fConfiguration.features & 0x20) != 0) || (nextType != 8)) && (((this.fConfiguration.features & 0x8) != 0) || (nextType != 4))))
/*      */         {
/*  619 */           if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0)) {
/*  620 */             isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
/*      */           }
/*  622 */           if (this.fValidationHandler != null) {
/*  623 */             this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  624 */             this.fCurrentNode = node;
/*  625 */             this.fValidationHandler.characterData(node.getNodeValue(), null);
/*      */           }
/*      */ 
/*  631 */           if (this.fDTDValidator != null) {
/*  632 */             this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
/*  633 */             this.fCurrentNode = node;
/*  634 */             this.fDTDValidator.characterData(node.getNodeValue(), null);
/*      */ 
/*  639 */             if (this.allWhitespace) {
/*  640 */               this.allWhitespace = false;
/*  641 */               ((TextImpl)node).setIgnorableWhitespace(true);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  652 */       break;
/*      */     case 7:
/*  657 */       if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0)) {
/*  658 */         ProcessingInstruction pinode = (ProcessingInstruction)node;
/*      */ 
/*  660 */         String target = pinode.getTarget();
/*      */         boolean wellformed;
/*      */         boolean wellformed;
/*  662 */         if (this.fDocument.isXML11Version()) {
/*  663 */           wellformed = XML11Char.isXML11ValidName(target);
/*      */         }
/*      */         else {
/*  666 */           wellformed = XMLChar.isValidName(target);
/*      */         }
/*      */ 
/*  669 */         if (!wellformed) {
/*  670 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", node.getNodeName() });
/*      */ 
/*  674 */           reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "wf-invalid-character-in-node-name");
/*      */         }
/*      */ 
/*  681 */         isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, pinode.getData(), this.fDocument.isXML11Version());
/*      */       }break;
/*      */     case 2:
/*      */     case 6:
/*      */     case 9:
/*  686 */     }return null;
/*      */   }
/*      */ 
/*      */   private XMLGrammarPool createGrammarPool(DocumentTypeImpl docType)
/*      */   {
/*  691 */     XMLGrammarPoolImpl pool = new XMLGrammarPoolImpl();
/*      */ 
/*  693 */     XMLGrammarPreparser preParser = new XMLGrammarPreparser(this.fSymbolTable);
/*  694 */     preParser.registerPreparser("http://www.w3.org/TR/REC-xml", null);
/*  695 */     preParser.setFeature("http://apache.org/xml/features/namespaces", true);
/*  696 */     preParser.setFeature("http://apache.org/xml/features/validation", true);
/*  697 */     preParser.setProperty("http://apache.org/xml/properties/internal/grammar-pool", pool);
/*      */ 
/*  699 */     String internalSubset = docType.getInternalSubset();
/*  700 */     XMLInputSource is = new XMLInputSource(docType.getPublicId(), docType.getSystemId(), null);
/*      */ 
/*  702 */     if (internalSubset != null)
/*  703 */       is.setCharacterStream(new StringReader(internalSubset));
/*      */     try {
/*  705 */       DTDGrammar g = (DTDGrammar)preParser.preparseGrammar("http://www.w3.org/TR/REC-xml", is);
/*  706 */       ((XMLDTDDescription)g.getGrammarDescription()).setRootName(docType.getName());
/*  707 */       is.setCharacterStream(null);
/*  708 */       g = (DTDGrammar)preParser.preparseGrammar("http://www.w3.org/TR/REC-xml", is);
/*  709 */       ((XMLDTDDescription)g.getGrammarDescription()).setRootName(docType.getName());
/*      */     }
/*      */     catch (XNIException e) {
/*      */     }
/*      */     catch (IOException e) {
/*      */     }
/*  715 */     return pool;
/*      */   }
/*      */ 
/*      */   protected final void expandEntityRef(Node parent, Node reference)
/*      */   {
/*      */     Node next;
/*  722 */     for (Node kid = reference.getFirstChild(); kid != null; kid = next) {
/*  723 */       next = kid.getNextSibling();
/*  724 */       parent.insertBefore(kid, reference);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void namespaceFixUp(ElementImpl element, AttributeMap attributes)
/*      */   {
/*  750 */     if (attributes != null)
/*      */     {
/*  753 */       for (int k = 0; k < attributes.getLength(); k++) {
/*  754 */         Attr attr = (Attr)attributes.getItem(k);
/*      */ 
/*  758 */         if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0) && (this.fDocument.isXMLVersionChanged()))
/*      */         {
/*  761 */           this.fDocument.checkQName(attr.getPrefix(), attr.getLocalName());
/*      */         }
/*      */ 
/*  764 */         String uri = attr.getNamespaceURI();
/*  765 */         if ((uri != null) && (uri.equals(NamespaceContext.XMLNS_URI)))
/*      */         {
/*  769 */           if ((this.fConfiguration.features & 0x200) != 0)
/*      */           {
/*  773 */             String value = attr.getNodeValue();
/*  774 */             if (value == null) {
/*  775 */               value = XMLSymbols.EMPTY_STRING;
/*      */             }
/*      */ 
/*  779 */             if ((this.fDocument.errorChecking) && (value.equals(NamespaceContext.XMLNS_URI)))
/*      */             {
/*  782 */               this.fLocator.fRelatedNode = attr;
/*  783 */               String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
/*  784 */               reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "CantBindXMLNS");
/*      */             }
/*      */             else
/*      */             {
/*  788 */               String prefix = attr.getPrefix();
/*  789 */               prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  791 */               String localpart = this.fSymbolTable.addSymbol(attr.getLocalName());
/*  792 */               if (prefix == XMLSymbols.PREFIX_XMLNS)
/*      */               {
/*  794 */                 value = this.fSymbolTable.addSymbol(value);
/*  795 */                 if (value.length() != 0) {
/*  796 */                   this.fNamespaceContext.declarePrefix(localpart, value);
/*      */                 }
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*  806 */                 value = this.fSymbolTable.addSymbol(value);
/*  807 */                 this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, value);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  833 */     String uri = element.getNamespaceURI();
/*  834 */     String prefix = element.getPrefix();
/*      */ 
/*  837 */     if ((this.fConfiguration.features & 0x200) == 0)
/*      */     {
/*  839 */       uri = null;
/*  840 */     } else if (uri != null) {
/*  841 */       uri = this.fSymbolTable.addSymbol(uri);
/*  842 */       prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  844 */       if (this.fNamespaceContext.getURI(prefix) != uri)
/*      */       {
/*  851 */         addNamespaceDecl(prefix, uri, element);
/*  852 */         this.fLocalNSBinder.declarePrefix(prefix, uri);
/*  853 */         this.fNamespaceContext.declarePrefix(prefix, uri);
/*      */       }
/*      */     }
/*  856 */     else if (element.getLocalName() == null)
/*      */     {
/*  859 */       if (this.fNamespaceValidation) {
/*  860 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { element.getNodeName() });
/*      */ 
/*  863 */         reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)3, "NullLocalElementName");
/*      */       }
/*      */       else {
/*  866 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { element.getNodeName() });
/*      */ 
/*  869 */         reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "NullLocalElementName");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  874 */       uri = this.fNamespaceContext.getURI(XMLSymbols.EMPTY_STRING);
/*  875 */       if ((uri != null) && (uri.length() > 0))
/*      */       {
/*  878 */         addNamespaceDecl(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING, element);
/*  879 */         this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*  880 */         this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  889 */     if (attributes != null)
/*      */     {
/*  892 */       attributes.cloneMap(this.fAttributeList);
/*  893 */       for (int i = 0; i < this.fAttributeList.size(); i++) {
/*  894 */         Attr attr = (Attr)this.fAttributeList.get(i);
/*  895 */         this.fLocator.fRelatedNode = attr;
/*      */ 
/*  901 */         attr.normalize();
/*  902 */         String value = attr.getValue();
/*  903 */         String name = attr.getNodeName();
/*  904 */         uri = attr.getNamespaceURI();
/*      */ 
/*  907 */         if (value == null) {
/*  908 */           value = XMLSymbols.EMPTY_STRING;
/*      */         }
/*      */ 
/*  911 */         if (uri != null) {
/*  912 */           prefix = attr.getPrefix();
/*  913 */           prefix = (prefix == null) || (prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
/*      */ 
/*  915 */           this.fSymbolTable.addSymbol(attr.getLocalName());
/*      */ 
/*  922 */           if ((uri == null) || (!uri.equals(NamespaceContext.XMLNS_URI)))
/*      */           {
/*  928 */             if ((this.fDocument.errorChecking) && ((this.fConfiguration.features & 0x100) != 0)) {
/*  929 */               isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributes, (AttrImpl)attr, attr.getValue(), this.fDocument.isXML11Version());
/*  930 */               if (this.fDocument.isXMLVersionChanged()) {
/*  931 */                 boolean wellformed = CoreDocumentImpl.isXMLName(attr.getNodeName(), this.fDocument.isXML11Version());
/*  932 */                 if (!wellformed) {
/*  933 */                   String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Attribute", attr.getNodeName() });
/*      */ 
/*  937 */                   reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "wf-invalid-character-in-node-name");
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  955 */             ((AttrImpl)attr).setIdAttribute(false);
/*      */ 
/*  958 */             uri = this.fSymbolTable.addSymbol(uri);
/*      */ 
/*  961 */             String declaredURI = this.fNamespaceContext.getURI(prefix);
/*      */ 
/*  963 */             if ((prefix == XMLSymbols.EMPTY_STRING) || (declaredURI != uri))
/*      */             {
/*  971 */               name = attr.getNodeName();
/*      */ 
/*  974 */               String declaredPrefix = this.fNamespaceContext.getPrefix(uri);
/*  975 */               if ((declaredPrefix != null) && (declaredPrefix != XMLSymbols.EMPTY_STRING))
/*      */               {
/*  978 */                 prefix = declaredPrefix;
/*      */               } else {
/*  980 */                 if ((prefix == XMLSymbols.EMPTY_STRING) || (this.fLocalNSBinder.getURI(prefix) != null))
/*      */                 {
/*  988 */                   int counter = 1;
/*  989 */                   prefix = this.fSymbolTable.addSymbol("NS" + counter++);
/*  990 */                   while (this.fLocalNSBinder.getURI(prefix) != null) {
/*  991 */                     prefix = this.fSymbolTable.addSymbol("NS" + counter++);
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*  996 */                 addNamespaceDecl(prefix, uri, element);
/*  997 */                 value = this.fSymbolTable.addSymbol(value);
/*  998 */                 this.fLocalNSBinder.declarePrefix(prefix, value);
/*  999 */                 this.fNamespaceContext.declarePrefix(prefix, uri);
/*      */               }
/*      */ 
/* 1003 */               attr.setPrefix(prefix);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1011 */           ((AttrImpl)attr).setIdAttribute(false);
/*      */ 
/* 1013 */           if (attr.getLocalName() == null)
/*      */           {
/* 1015 */             if (this.fNamespaceValidation) {
/* 1016 */               String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { attr.getNodeName() });
/*      */ 
/* 1019 */               reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)3, "NullLocalAttrName");
/*      */             }
/*      */             else {
/* 1022 */               String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { attr.getNodeName() });
/*      */ 
/* 1025 */               reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short)2, "NullLocalAttrName");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void addNamespaceDecl(String prefix, String uri, ElementImpl element)
/*      */   {
/* 1056 */     if (prefix == XMLSymbols.EMPTY_STRING)
/*      */     {
/* 1060 */       element.setAttributeNS(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS, uri);
/*      */     }
/*      */     else
/*      */     {
/* 1065 */       element.setAttributeNS(NamespaceContext.XMLNS_URI, "xmlns:" + prefix, uri);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void isCDataWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version)
/*      */   {
/* 1083 */     if ((datavalue == null) || (datavalue.length() == 0)) {
/* 1084 */       return;
/*      */     }
/*      */ 
/* 1087 */     char[] dataarray = datavalue.toCharArray();
/* 1088 */     int datalength = dataarray.length;
/*      */ 
/* 1091 */     if (isXML11Version)
/*      */     {
/* 1093 */       int i = 0;
/* 1094 */       while (i < datalength) {
/* 1095 */         char c = dataarray[(i++)];
/* 1096 */         if (XML11Char.isXML11Invalid(c))
/*      */         {
/* 1098 */           if ((XMLChar.isHighSurrogate(c)) && (i < datalength)) {
/* 1099 */             char c2 = dataarray[(i++)];
/* 1100 */             if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2))));
/*      */           }
/*      */           else
/*      */           {
/* 1105 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[] { Integer.toString(c, 16) });
/*      */ 
/* 1109 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/*      */ 
/*      */         }
/* 1117 */         else if (c == ']') {
/* 1118 */           int count = i;
/* 1119 */           if ((count < datalength) && (dataarray[count] == ']')) {
/*      */             do count++; while ((count < datalength) && (dataarray[count] == ']'));
/*      */ 
/* 1123 */             if ((count < datalength) && (dataarray[count] == '>'))
/*      */             {
/* 1125 */               String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
/* 1126 */               reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1135 */       int i = 0;
/* 1136 */       while (i < datalength) {
/* 1137 */         char c = dataarray[(i++)];
/* 1138 */         if (XMLChar.isInvalid(c))
/*      */         {
/* 1140 */           if ((XMLChar.isHighSurrogate(c)) && (i < datalength))
/*      */           {
/* 1141 */             char c2 = dataarray[(i++)];
/* 1142 */             if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2))));
/*      */           }
/*      */           else
/*      */           {
/* 1151 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[] { Integer.toString(c, 16) });
/*      */ 
/* 1155 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/* 1157 */         } else if (c == ']') {
/* 1158 */           int count = i;
/* 1159 */           if ((count < datalength) && (dataarray[count] == ']')) {
/*      */             do count++; while ((count < datalength) && (dataarray[count] == ']'));
/*      */ 
/* 1163 */             if ((count < datalength) && (dataarray[count] == '>')) {
/* 1164 */               String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
/* 1165 */               reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void isXMLCharWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version)
/*      */   {
/* 1183 */     if ((datavalue == null) || (datavalue.length() == 0)) {
/* 1184 */       return;
/*      */     }
/*      */ 
/* 1187 */     char[] dataarray = datavalue.toCharArray();
/* 1188 */     int datalength = dataarray.length;
/*      */ 
/* 1191 */     if (isXML11Version)
/*      */     {
/* 1193 */       int i = 0;
/* 1194 */       while (i < datalength) {
/* 1195 */         if (XML11Char.isXML11Invalid(dataarray[(i++)]))
/*      */         {
/* 1197 */           char ch = dataarray[(i - 1)];
/* 1198 */           if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
/* 1199 */             char ch2 = dataarray[(i++)];
/* 1200 */             if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2))));
/*      */           }
/*      */           else
/*      */           {
/* 1205 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[] { Integer.toString(dataarray[(i - 1)], 16) });
/*      */ 
/* 1208 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1215 */       int i = 0;
/* 1216 */       while (i < datalength)
/* 1217 */         if (XMLChar.isInvalid(dataarray[(i++)]))
/*      */         {
/* 1219 */           char ch = dataarray[(i - 1)];
/* 1220 */           if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
/* 1221 */             char ch2 = dataarray[(i++)];
/* 1222 */             if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2))));
/*      */           }
/*      */           else
/*      */           {
/* 1227 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[] { Integer.toString(dataarray[(i - 1)], 16) });
/*      */ 
/* 1230 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void isCommentWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version)
/*      */   {
/* 1246 */     if ((datavalue == null) || (datavalue.length() == 0)) {
/* 1247 */       return;
/*      */     }
/*      */ 
/* 1250 */     char[] dataarray = datavalue.toCharArray();
/* 1251 */     int datalength = dataarray.length;
/*      */ 
/* 1254 */     if (isXML11Version)
/*      */     {
/* 1256 */       int i = 0;
/* 1257 */       while (i < datalength) {
/* 1258 */         char c = dataarray[(i++)];
/* 1259 */         if (XML11Char.isXML11Invalid(c))
/*      */         {
/* 1261 */           if ((XMLChar.isHighSurrogate(c)) && (i < datalength)) {
/* 1262 */             char c2 = dataarray[(i++)];
/* 1263 */             if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2))));
/*      */           }
/*      */           else
/*      */           {
/* 1268 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[] { Integer.toString(dataarray[(i - 1)], 16) });
/*      */ 
/* 1271 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/* 1273 */         } else if ((c == '-') && (i < datalength) && (dataarray[i] == '-')) {
/* 1274 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
/*      */ 
/* 1277 */           reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1283 */       int i = 0;
/* 1284 */       while (i < datalength) {
/* 1285 */         char c = dataarray[(i++)];
/* 1286 */         if (XMLChar.isInvalid(c))
/*      */         {
/* 1288 */           if ((XMLChar.isHighSurrogate(c)) && (i < datalength)) {
/* 1289 */             char c2 = dataarray[(i++)];
/* 1290 */             if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2))));
/*      */           }
/*      */           else
/*      */           {
/* 1295 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[] { Integer.toString(dataarray[(i - 1)], 16) });
/*      */ 
/* 1297 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */           }
/* 1299 */         } else if ((c == '-') && (i < datalength) && (dataarray[i] == '-')) {
/* 1300 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
/*      */ 
/* 1303 */           reportDOMError(errorHandler, error, locator, msg, (short)2, "wf-invalid-character");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void isAttrValueWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, NamedNodeMap attributes, Attr a, String value, boolean xml11Version)
/*      */   {
/* 1318 */     if (((a instanceof AttrImpl)) && (((AttrImpl)a).hasStringValue())) {
/* 1319 */       isXMLCharWF(errorHandler, error, locator, value, xml11Version);
/*      */     } else {
/* 1321 */       NodeList children = a.getChildNodes();
/*      */ 
/* 1323 */       for (int j = 0; j < children.getLength(); j++) {
/* 1324 */         Node child = children.item(j);
/*      */ 
/* 1326 */         if (child.getNodeType() == 5) {
/* 1327 */           Document owner = a.getOwnerDocument();
/* 1328 */           Entity ent = null;
/*      */ 
/* 1331 */           if (owner != null) {
/* 1332 */             DocumentType docType = owner.getDoctype();
/* 1333 */             if (docType != null) {
/* 1334 */               NamedNodeMap entities = docType.getEntities();
/* 1335 */               ent = (Entity)entities.getNamedItemNS("*", child.getNodeName());
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1341 */           if (ent == null) {
/* 1342 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UndeclaredEntRefInAttrValue", new Object[] { a.getNodeName() });
/*      */ 
/* 1345 */             reportDOMError(errorHandler, error, locator, msg, (short)2, "UndeclaredEntRefInAttrValue");
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1351 */           isXMLCharWF(errorHandler, error, locator, child.getNodeValue(), xml11Version);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void reportDOMError(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String message, short severity, String type)
/*      */   {
/* 1366 */     if (errorHandler != null) {
/* 1367 */       error.reset();
/* 1368 */       error.fMessage = message;
/* 1369 */       error.fSeverity = severity;
/* 1370 */       error.fLocator = locator;
/* 1371 */       error.fType = type;
/* 1372 */       error.fRelatedData = locator.fRelatedNode;
/*      */ 
/* 1374 */       if (!errorHandler.handleError(error))
/* 1375 */         throw abort;
/*      */     }
/* 1377 */     if (severity == 3)
/* 1378 */       throw abort;
/*      */   }
/*      */ 
/*      */   protected final void updateQName(Node node, QName qname)
/*      */   {
/* 1383 */     String prefix = node.getPrefix();
/* 1384 */     String namespace = node.getNamespaceURI();
/* 1385 */     String localName = node.getLocalName();
/*      */ 
/* 1388 */     qname.prefix = ((prefix != null) && (prefix.length() != 0) ? this.fSymbolTable.addSymbol(prefix) : null);
/* 1389 */     qname.localpart = (localName != null ? this.fSymbolTable.addSymbol(localName) : null);
/* 1390 */     qname.rawname = this.fSymbolTable.addSymbol(node.getNodeName());
/* 1391 */     qname.uri = (namespace != null ? this.fSymbolTable.addSymbol(namespace) : null);
/*      */   }
/*      */ 
/*      */   final String normalizeAttributeValue(String value, Attr attr)
/*      */   {
/* 1415 */     if (!attr.getSpecified())
/*      */     {
/* 1418 */       return value;
/*      */     }
/* 1420 */     int end = value.length();
/*      */ 
/* 1422 */     if (this.fNormalizedValue.ch.length < end) {
/* 1423 */       this.fNormalizedValue.ch = new char[end];
/*      */     }
/* 1425 */     this.fNormalizedValue.length = 0;
/* 1426 */     boolean normalized = false;
/* 1427 */     for (int i = 0; i < end; i++) {
/* 1428 */       char c = value.charAt(i);
/* 1429 */       if ((c == '\t') || (c == '\n')) {
/* 1430 */         this.fNormalizedValue.ch[(this.fNormalizedValue.length++)] = ' ';
/* 1431 */         normalized = true;
/*      */       }
/* 1433 */       else if (c == '\r') {
/* 1434 */         normalized = true;
/* 1435 */         this.fNormalizedValue.ch[(this.fNormalizedValue.length++)] = ' ';
/* 1436 */         int next = i + 1;
/* 1437 */         if ((next < end) && (value.charAt(next) == '\n')) i = next; 
/*      */       }
/*      */       else
/*      */       {
/* 1440 */         this.fNormalizedValue.ch[(this.fNormalizedValue.length++)] = c;
/*      */       }
/*      */     }
/* 1443 */     if (normalized) {
/* 1444 */       value = this.fNormalizedValue.toString();
/* 1445 */       attr.setValue(value);
/*      */     }
/* 1447 */     return value;
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1799 */     Element currentElement = (Element)this.fCurrentNode;
/* 1800 */     int attrCount = attributes.getLength();
/*      */ 
/* 1806 */     for (int i = 0; i < attrCount; i++) {
/* 1807 */       attributes.getName(i, this.fAttrQName);
/* 1808 */       Attr attr = null;
/*      */ 
/* 1810 */       attr = currentElement.getAttributeNodeNS(this.fAttrQName.uri, this.fAttrQName.localpart);
/* 1811 */       AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/*      */ 
/* 1814 */       if (attrPSVI != null)
/*      */       {
/* 1817 */         XSTypeDefinition decl = attrPSVI.getMemberTypeDefinition();
/* 1818 */         boolean id = false;
/* 1819 */         if (decl != null) {
/* 1820 */           id = ((XSSimpleType)decl).isIDType();
/*      */         } else {
/* 1822 */           decl = attrPSVI.getTypeDefinition();
/* 1823 */           if (decl != null) {
/* 1824 */             id = ((XSSimpleType)decl).isIDType();
/*      */           }
/*      */         }
/* 1827 */         if (id) {
/* 1828 */           ((ElementImpl)currentElement).setIdAttributeNode(attr, true);
/*      */         }
/*      */ 
/* 1831 */         if (this.fPSVI) {
/* 1832 */           ((PSVIAttrNSImpl)attr).setPSVI(attrPSVI);
/*      */         }
/* 1834 */         if ((this.fConfiguration.features & 0x2) != 0)
/*      */         {
/* 1840 */           boolean specified = attr.getSpecified();
/* 1841 */           attr.setValue(attrPSVI.getSchemaNormalizedValue());
/* 1842 */           if (!specified)
/* 1843 */             ((AttrImpl)attr).setSpecified(specified);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1867 */     startElement(element, attributes, augs);
/* 1868 */     endElement(element, augs);
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1957 */     this.allWhitespace = true;
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1974 */     if (augs != null) {
/* 1975 */       ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 1976 */       if (elementPSVI != null) {
/* 1977 */         ElementImpl elementNode = (ElementImpl)this.fCurrentNode;
/* 1978 */         if (this.fPSVI) {
/* 1979 */           ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(elementPSVI);
/*      */         }
/*      */ 
/* 1982 */         String normalizedValue = elementPSVI.getSchemaNormalizedValue();
/* 1983 */         if ((this.fConfiguration.features & 0x2) != 0) {
/* 1984 */           if (normalizedValue != null) {
/* 1985 */             elementNode.setTextContent(normalizedValue);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1991 */           String text = elementNode.getTextContent();
/* 1992 */           if (text.length() == 0)
/*      */           {
/* 1994 */             if (normalizedValue != null)
/* 1995 */               elementNode.setTextContent(normalizedValue);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDocumentSource(XMLDocumentSource source)
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLDocumentSource getDocumentSource()
/*      */   {
/* 2044 */     return null;
/*      */   }
/*      */ 
/*      */   protected final class XMLAttributesProxy
/*      */     implements XMLAttributes
/*      */   {
/*      */     protected AttributeMap fAttributes;
/*      */     protected CoreDocumentImpl fDocument;
/*      */     protected ElementImpl fElement;
/* 1456 */     protected final Vector fAugmentations = new Vector(5);
/*      */ 
/*      */     protected XMLAttributesProxy() {
/*      */     }
/* 1460 */     public void setAttributes(AttributeMap attributes, CoreDocumentImpl doc, ElementImpl elem) { this.fDocument = doc;
/* 1461 */       this.fAttributes = attributes;
/* 1462 */       this.fElement = elem;
/* 1463 */       if (attributes != null) {
/* 1464 */         int length = attributes.getLength();
/*      */ 
/* 1466 */         this.fAugmentations.setSize(length);
/*      */ 
/* 1470 */         for (int i = 0; i < length; i++)
/* 1471 */           this.fAugmentations.setElementAt(new AugmentationsImpl(), i);
/*      */       }
/*      */       else {
/* 1474 */         this.fAugmentations.setSize(0);
/*      */       }
/*      */     }
/*      */ 
/*      */     public int addAttribute(QName qname, String attrType, String attrValue)
/*      */     {
/* 1484 */       int index = this.fElement.getXercesAttribute(qname.uri, qname.localpart);
/*      */ 
/* 1486 */       if (index < 0)
/*      */       {
/* 1489 */         AttrImpl attr = (AttrImpl)((CoreDocumentImpl)this.fElement.getOwnerDocument()).createAttributeNS(qname.uri, qname.rawname, qname.localpart);
/*      */ 
/* 1495 */         attr.setNodeValue(attrValue);
/* 1496 */         index = this.fElement.setXercesAttributeNode(attr);
/* 1497 */         this.fAugmentations.insertElementAt(new AugmentationsImpl(), index);
/* 1498 */         attr.setSpecified(false);
/*      */       }
/*      */ 
/* 1508 */       return index;
/*      */     }
/*      */ 
/*      */     public void removeAllAttributes()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeAttributeAt(int attrIndex)
/*      */     {
/*      */     }
/*      */ 
/*      */     public int getLength()
/*      */     {
/* 1523 */       return this.fAttributes != null ? this.fAttributes.getLength() : 0;
/*      */     }
/*      */ 
/*      */     public int getIndex(String qName)
/*      */     {
/* 1529 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getIndex(String uri, String localPart)
/*      */     {
/* 1534 */       return -1;
/*      */     }
/*      */ 
/*      */     public void setName(int attrIndex, QName attrName)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void getName(int attrIndex, QName attrName) {
/* 1542 */       if (this.fAttributes != null)
/* 1543 */         DOMNormalizer.this.updateQName((Node)this.fAttributes.getItem(attrIndex), attrName);
/*      */     }
/*      */ 
/*      */     public String getPrefix(int index)
/*      */     {
/* 1549 */       return null;
/*      */     }
/*      */ 
/*      */     public String getURI(int index)
/*      */     {
/* 1555 */       return null;
/*      */     }
/*      */ 
/*      */     public String getLocalName(int index)
/*      */     {
/* 1561 */       return null;
/*      */     }
/*      */ 
/*      */     public String getQName(int index)
/*      */     {
/* 1567 */       return null;
/*      */     }
/*      */ 
/*      */     public QName getQualifiedName(int index)
/*      */     {
/* 1572 */       return null;
/*      */     }
/*      */ 
/*      */     public void setType(int attrIndex, String attrType)
/*      */     {
/*      */     }
/*      */ 
/*      */     public String getType(int index)
/*      */     {
/* 1581 */       return "CDATA";
/*      */     }
/*      */ 
/*      */     public String getType(String qName)
/*      */     {
/* 1586 */       return "CDATA";
/*      */     }
/*      */ 
/*      */     public String getType(String uri, String localName)
/*      */     {
/* 1591 */       return "CDATA";
/*      */     }
/*      */ 
/*      */     public void setValue(int attrIndex, String attrValue)
/*      */     {
/* 1600 */       if (this.fAttributes != null) {
/* 1601 */         AttrImpl attr = (AttrImpl)this.fAttributes.getItem(attrIndex);
/* 1602 */         boolean specified = attr.getSpecified();
/* 1603 */         attr.setValue(attrValue);
/* 1604 */         attr.setSpecified(specified);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setValue(int attrIndex, String attrValue, XMLString value)
/*      */     {
/* 1610 */       setValue(attrIndex, value.toString());
/*      */     }
/*      */ 
/*      */     public String getValue(int index) {
/* 1614 */       return this.fAttributes != null ? this.fAttributes.item(index).getNodeValue() : "";
/*      */     }
/*      */ 
/*      */     public String getValue(String qName)
/*      */     {
/* 1621 */       return null;
/*      */     }
/*      */ 
/*      */     public String getValue(String uri, String localName)
/*      */     {
/* 1626 */       if (this.fAttributes != null) {
/* 1627 */         Node node = this.fAttributes.getNamedItemNS(uri, localName);
/* 1628 */         return node != null ? node.getNodeValue() : null;
/*      */       }
/* 1630 */       return null;
/*      */     }
/*      */ 
/*      */     public void setNonNormalizedValue(int attrIndex, String attrValue)
/*      */     {
/*      */     }
/*      */ 
/*      */     public String getNonNormalizedValue(int attrIndex)
/*      */     {
/* 1642 */       return null;
/*      */     }
/*      */ 
/*      */     public void setSpecified(int attrIndex, boolean specified)
/*      */     {
/* 1647 */       AttrImpl attr = (AttrImpl)this.fAttributes.getItem(attrIndex);
/* 1648 */       attr.setSpecified(specified);
/*      */     }
/*      */ 
/*      */     public boolean isSpecified(int attrIndex) {
/* 1652 */       return ((Attr)this.fAttributes.getItem(attrIndex)).getSpecified();
/*      */     }
/*      */ 
/*      */     public Augmentations getAugmentations(int attributeIndex) {
/* 1656 */       return (Augmentations)this.fAugmentations.elementAt(attributeIndex);
/*      */     }
/*      */ 
/*      */     public Augmentations getAugmentations(String uri, String localPart)
/*      */     {
/* 1661 */       return null;
/*      */     }
/*      */ 
/*      */     public Augmentations getAugmentations(String qName)
/*      */     {
/* 1666 */       return null;
/*      */     }
/*      */ 
/*      */     public void setAugmentations(int attrIndex, Augmentations augs)
/*      */     {
/* 1676 */       this.fAugmentations.setElementAt(augs, attrIndex);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMNormalizer
 * JD-Core Version:    0.6.2
 */