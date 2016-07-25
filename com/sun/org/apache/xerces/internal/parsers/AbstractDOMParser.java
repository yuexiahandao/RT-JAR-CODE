/*      */ package com.sun.org.apache.xerces.internal.parsers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.AttrImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DocumentTypeImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.ElementDefinitionImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.ElementImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.EntityImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.EntityReferenceImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.NodeImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.NotationImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.PSVIElementNSImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.TextImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*      */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import java.util.Locale;
/*      */ import java.util.Stack;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.CDATASection;
/*      */ import org.w3c.dom.Comment;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.EntityReference;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.ProcessingInstruction;
/*      */ import org.w3c.dom.Text;
/*      */ import org.w3c.dom.ls.LSParserFilter;
/*      */ 
/*      */ public class AbstractDOMParser extends AbstractXMLDocumentParser
/*      */ {
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String CREATE_ENTITY_REF_NODES = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
/*      */   protected static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
/*      */   protected static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
/*      */   protected static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
/*      */   protected static final String DEFER_NODE_EXPANSION = "http://apache.org/xml/features/dom/defer-node-expansion";
/*  124 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/dom/create-entity-ref-nodes", "http://apache.org/xml/features/include-comments", "http://apache.org/xml/features/create-cdata-nodes", "http://apache.org/xml/features/dom/include-ignorable-whitespace", "http://apache.org/xml/features/dom/defer-node-expansion" };
/*      */   protected static final String DOCUMENT_CLASS_NAME = "http://apache.org/xml/properties/dom/document-class-name";
/*      */   protected static final String CURRENT_ELEMENT_NODE = "http://apache.org/xml/properties/dom/current-element-node";
/*  146 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/dom/document-class-name", "http://apache.org/xml/properties/dom/current-element-node" };
/*      */   protected static final String DEFAULT_DOCUMENT_CLASS_NAME = "com.sun.org.apache.xerces.internal.dom.DocumentImpl";
/*      */   protected static final String CORE_DOCUMENT_CLASS_NAME = "com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl";
/*      */   protected static final String PSVI_DOCUMENT_CLASS_NAME = "com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl";
/*      */   private static final boolean DEBUG_EVENTS = false;
/*      */   private static final boolean DEBUG_BASEURI = false;
/*  185 */   protected DOMErrorHandlerWrapper fErrorHandler = null;
/*      */   protected boolean fInDTD;
/*      */   protected boolean fCreateEntityRefNodes;
/*      */   protected boolean fIncludeIgnorableWhitespace;
/*      */   protected boolean fIncludeComments;
/*      */   protected boolean fCreateCDATANodes;
/*      */   protected Document fDocument;
/*      */   protected CoreDocumentImpl fDocumentImpl;
/*      */   protected boolean fStorePSVI;
/*      */   protected String fDocumentClassName;
/*      */   protected DocumentType fDocumentType;
/*      */   protected Node fCurrentNode;
/*      */   protected CDATASection fCurrentCDATASection;
/*      */   protected EntityImpl fCurrentEntityDecl;
/*      */   protected int fDeferredEntityDecl;
/*  228 */   protected final StringBuilder fStringBuilder = new StringBuilder(50);
/*      */   protected StringBuilder fInternalSubset;
/*      */   protected boolean fDeferNodeExpansion;
/*      */   protected boolean fNamespaceAware;
/*      */   protected DeferredDocumentImpl fDeferredDocumentImpl;
/*      */   protected int fDocumentIndex;
/*      */   protected int fDocumentTypeIndex;
/*      */   protected int fCurrentNodeIndex;
/*      */   protected int fCurrentCDATASectionIndex;
/*      */   protected boolean fInDTDExternalSubset;
/*      */   protected Node fRoot;
/*      */   protected boolean fInCDATASection;
/*  257 */   protected boolean fFirstChunk = false;
/*      */ 
/*  262 */   protected boolean fFilterReject = false;
/*      */ 
/*  267 */   protected final Stack fBaseURIStack = new Stack();
/*      */ 
/*  270 */   protected int fRejectedElementDepth = 0;
/*      */ 
/*  273 */   protected Stack fSkippedElemStack = null;
/*      */ 
/*  276 */   protected boolean fInEntityRef = false;
/*      */ 
/*  279 */   private final QName fAttrQName = new QName();
/*      */   private XMLLocator fLocator;
/*  286 */   protected LSParserFilter fDOMFilter = null;
/*      */ 
/*      */   protected AbstractDOMParser(XMLParserConfiguration config)
/*      */   {
/*  295 */     super(config);
/*      */ 
/*  299 */     this.fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
/*      */ 
/*  302 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", true);
/*  303 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
/*  304 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
/*  305 */     this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
/*  306 */     this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", true);
/*      */ 
/*  309 */     this.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/*      */ 
/*  312 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "com.sun.org.apache.xerces.internal.dom.DocumentImpl");
/*      */   }
/*      */ 
/*      */   protected String getDocumentClassName()
/*      */   {
/*  321 */     return this.fDocumentClassName;
/*      */   }
/*      */ 
/*      */   protected void setDocumentClassName(String documentClassName)
/*      */   {
/*  341 */     if (documentClassName == null) {
/*  342 */       documentClassName = "com.sun.org.apache.xerces.internal.dom.DocumentImpl";
/*      */     }
/*      */ 
/*  345 */     if ((!documentClassName.equals("com.sun.org.apache.xerces.internal.dom.DocumentImpl")) && (!documentClassName.equals("com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl")))
/*      */     {
/*      */       try
/*      */       {
/*  349 */         Class _class = ObjectFactory.findProviderClass(documentClassName, true);
/*      */ 
/*  351 */         if (!Document.class.isAssignableFrom(_class)) {
/*  352 */           throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidDocumentClassName", new Object[] { documentClassName }));
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (ClassNotFoundException e)
/*      */       {
/*  359 */         throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "MissingDocumentClassName", new Object[] { documentClassName }));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  367 */     this.fDocumentClassName = documentClassName;
/*  368 */     if (!documentClassName.equals("com.sun.org.apache.xerces.internal.dom.DocumentImpl"))
/*  369 */       this.fDeferNodeExpansion = false;
/*      */   }
/*      */ 
/*      */   public Document getDocument()
/*      */   {
/*  380 */     return this.fDocument;
/*      */   }
/*      */ 
/*      */   public final void dropDocumentReferences()
/*      */   {
/*  387 */     this.fDocument = null;
/*  388 */     this.fDocumentImpl = null;
/*  389 */     this.fDeferredDocumentImpl = null;
/*  390 */     this.fDocumentType = null;
/*  391 */     this.fCurrentNode = null;
/*  392 */     this.fCurrentCDATASection = null;
/*  393 */     this.fCurrentEntityDecl = null;
/*  394 */     this.fRoot = null;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws XNIException
/*      */   {
/*  407 */     super.reset();
/*      */ 
/*  411 */     this.fCreateEntityRefNodes = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes");
/*      */ 
/*  414 */     this.fIncludeIgnorableWhitespace = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace");
/*      */ 
/*  417 */     this.fDeferNodeExpansion = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
/*      */ 
/*  420 */     this.fNamespaceAware = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
/*      */ 
/*  422 */     this.fIncludeComments = this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments");
/*      */ 
/*  424 */     this.fCreateCDATANodes = this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
/*      */ 
/*  427 */     setDocumentClassName((String)this.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name"));
/*      */ 
/*  431 */     this.fDocument = null;
/*  432 */     this.fDocumentImpl = null;
/*  433 */     this.fStorePSVI = false;
/*  434 */     this.fDocumentType = null;
/*  435 */     this.fDocumentTypeIndex = -1;
/*  436 */     this.fDeferredDocumentImpl = null;
/*  437 */     this.fCurrentNode = null;
/*      */ 
/*  440 */     this.fStringBuilder.setLength(0);
/*      */ 
/*  443 */     this.fRoot = null;
/*  444 */     this.fInDTD = false;
/*  445 */     this.fInDTDExternalSubset = false;
/*  446 */     this.fInCDATASection = false;
/*  447 */     this.fFirstChunk = false;
/*  448 */     this.fCurrentCDATASection = null;
/*  449 */     this.fCurrentCDATASectionIndex = -1;
/*      */ 
/*  451 */     this.fBaseURIStack.removeAllElements();
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */   {
/*  463 */     this.fConfiguration.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  502 */     if (!this.fDeferNodeExpansion) {
/*  503 */       if (this.fFilterReject) {
/*  504 */         return;
/*      */       }
/*  506 */       setCharacterData(true);
/*  507 */       EntityReference er = this.fDocument.createEntityReference(name);
/*  508 */       if (this.fDocumentImpl != null)
/*      */       {
/*  513 */         EntityReferenceImpl erImpl = (EntityReferenceImpl)er;
/*      */ 
/*  516 */         erImpl.setBaseURI(identifier.getExpandedSystemId());
/*  517 */         if (this.fDocumentType != null)
/*      */         {
/*  519 */           NamedNodeMap entities = this.fDocumentType.getEntities();
/*  520 */           this.fCurrentEntityDecl = ((EntityImpl)entities.getNamedItem(name));
/*  521 */           if (this.fCurrentEntityDecl != null) {
/*  522 */             this.fCurrentEntityDecl.setInputEncoding(encoding);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  528 */         erImpl.needsSyncChildren(false);
/*      */       }
/*  530 */       this.fInEntityRef = true;
/*  531 */       this.fCurrentNode.appendChild(er);
/*  532 */       this.fCurrentNode = er;
/*      */     }
/*      */     else
/*      */     {
/*  536 */       int er = this.fDeferredDocumentImpl.createDeferredEntityReference(name, identifier.getExpandedSystemId());
/*      */ 
/*  538 */       if (this.fDocumentTypeIndex != -1)
/*      */       {
/*  540 */         int node = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/*  541 */         while (node != -1) {
/*  542 */           short nodeType = this.fDeferredDocumentImpl.getNodeType(node, false);
/*  543 */           if (nodeType == 6) {
/*  544 */             String nodeName = this.fDeferredDocumentImpl.getNodeName(node, false);
/*      */ 
/*  546 */             if (nodeName.equals(name)) {
/*  547 */               this.fDeferredEntityDecl = node;
/*  548 */               this.fDeferredDocumentImpl.setInputEncoding(node, encoding);
/*  549 */               break;
/*      */             }
/*      */           }
/*  552 */           node = this.fDeferredDocumentImpl.getRealPrevSibling(node, false);
/*      */         }
/*      */       }
/*  555 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, er);
/*  556 */       this.fCurrentNodeIndex = er;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  579 */     if (this.fInDTD) {
/*  580 */       return;
/*      */     }
/*  582 */     if (!this.fDeferNodeExpansion) {
/*  583 */       if ((this.fCurrentEntityDecl != null) && (!this.fFilterReject)) {
/*  584 */         this.fCurrentEntityDecl.setXmlEncoding(encoding);
/*  585 */         if (version != null) {
/*  586 */           this.fCurrentEntityDecl.setXmlVersion(version);
/*      */         }
/*      */       }
/*      */     }
/*  590 */     else if (this.fDeferredEntityDecl != -1)
/*  591 */       this.fDeferredDocumentImpl.setEntityInfo(this.fDeferredEntityDecl, version, encoding);
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  605 */     if (this.fInDTD) {
/*  606 */       if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/*  607 */         this.fInternalSubset.append("<!--");
/*  608 */         if (text.length > 0) {
/*  609 */           this.fInternalSubset.append(text.ch, text.offset, text.length);
/*      */         }
/*  611 */         this.fInternalSubset.append("-->");
/*      */       }
/*  613 */       return;
/*      */     }
/*  615 */     if ((!this.fIncludeComments) || (this.fFilterReject)) {
/*  616 */       return;
/*      */     }
/*  618 */     if (!this.fDeferNodeExpansion) {
/*  619 */       Comment comment = this.fDocument.createComment(text.toString());
/*      */ 
/*  621 */       setCharacterData(false);
/*  622 */       this.fCurrentNode.appendChild(comment);
/*  623 */       if ((this.fDOMFilter != null) && (!this.fInEntityRef) && ((this.fDOMFilter.getWhatToShow() & 0x80) != 0))
/*      */       {
/*  625 */         short code = this.fDOMFilter.acceptNode(comment);
/*  626 */         switch (code) {
/*      */         case 4:
/*  628 */           throw Abort.INSTANCE;
/*      */         case 2:
/*      */         case 3:
/*  639 */           this.fCurrentNode.removeChild(comment);
/*      */ 
/*  641 */           this.fFirstChunk = true;
/*  642 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  653 */       int comment = this.fDeferredDocumentImpl.createDeferredComment(text.toString());
/*      */ 
/*  655 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, comment);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  680 */     if (this.fInDTD) {
/*  681 */       if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/*  682 */         this.fInternalSubset.append("<?");
/*  683 */         this.fInternalSubset.append(target);
/*  684 */         if (data.length > 0) {
/*  685 */           this.fInternalSubset.append(' ').append(data.ch, data.offset, data.length);
/*      */         }
/*  687 */         this.fInternalSubset.append("?>");
/*      */       }
/*  689 */       return;
/*      */     }
/*      */ 
/*  695 */     if (!this.fDeferNodeExpansion) {
/*  696 */       if (this.fFilterReject) {
/*  697 */         return;
/*      */       }
/*  699 */       ProcessingInstruction pi = this.fDocument.createProcessingInstruction(target, data.toString());
/*      */ 
/*  703 */       setCharacterData(false);
/*  704 */       this.fCurrentNode.appendChild(pi);
/*  705 */       if ((this.fDOMFilter != null) && (!this.fInEntityRef) && ((this.fDOMFilter.getWhatToShow() & 0x40) != 0))
/*      */       {
/*  707 */         short code = this.fDOMFilter.acceptNode(pi);
/*  708 */         switch (code) {
/*      */         case 4:
/*  710 */           throw Abort.INSTANCE;
/*      */         case 2:
/*      */         case 3:
/*  716 */           this.fCurrentNode.removeChild(pi);
/*      */ 
/*  720 */           this.fFirstChunk = true;
/*  721 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  729 */       int pi = this.fDeferredDocumentImpl.createDeferredProcessingInstruction(target, data.toString());
/*      */ 
/*  731 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, pi);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  762 */     this.fLocator = locator;
/*  763 */     if (!this.fDeferNodeExpansion) {
/*  764 */       if (this.fDocumentClassName.equals("com.sun.org.apache.xerces.internal.dom.DocumentImpl")) {
/*  765 */         this.fDocument = new DocumentImpl();
/*  766 */         this.fDocumentImpl = ((CoreDocumentImpl)this.fDocument);
/*      */ 
/*  770 */         this.fDocumentImpl.setStrictErrorChecking(false);
/*      */ 
/*  772 */         this.fDocumentImpl.setInputEncoding(encoding);
/*      */ 
/*  774 */         this.fDocumentImpl.setDocumentURI(locator.getExpandedSystemId());
/*      */       }
/*  776 */       else if (this.fDocumentClassName.equals("com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl")) {
/*  777 */         this.fDocument = new PSVIDocumentImpl();
/*  778 */         this.fDocumentImpl = ((CoreDocumentImpl)this.fDocument);
/*  779 */         this.fStorePSVI = true;
/*      */ 
/*  783 */         this.fDocumentImpl.setStrictErrorChecking(false);
/*      */ 
/*  785 */         this.fDocumentImpl.setInputEncoding(encoding);
/*      */ 
/*  787 */         this.fDocumentImpl.setDocumentURI(locator.getExpandedSystemId());
/*      */       }
/*      */       else
/*      */       {
/*      */         try {
/*  792 */           Class documentClass = ObjectFactory.findProviderClass(this.fDocumentClassName, true);
/*  793 */           this.fDocument = ((Document)documentClass.newInstance());
/*      */ 
/*  796 */           Class defaultDocClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl", true);
/*      */ 
/*  798 */           if (defaultDocClass.isAssignableFrom(documentClass)) {
/*  799 */             this.fDocumentImpl = ((CoreDocumentImpl)this.fDocument);
/*      */ 
/*  801 */             Class psviDocClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl", true);
/*  802 */             if (psviDocClass.isAssignableFrom(documentClass)) {
/*  803 */               this.fStorePSVI = true;
/*      */             }
/*      */ 
/*  809 */             this.fDocumentImpl.setStrictErrorChecking(false);
/*      */ 
/*  811 */             this.fDocumentImpl.setInputEncoding(encoding);
/*      */ 
/*  813 */             if (locator != null)
/*  814 */               this.fDocumentImpl.setDocumentURI(locator.getExpandedSystemId());
/*      */           }
/*      */         }
/*      */         catch (ClassNotFoundException e)
/*      */         {
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*  822 */           throw new RuntimeException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "CannotCreateDocumentClass", new Object[] { this.fDocumentClassName }));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  829 */       this.fCurrentNode = this.fDocument;
/*      */     }
/*      */     else {
/*  832 */       this.fDeferredDocumentImpl = new DeferredDocumentImpl(this.fNamespaceAware);
/*  833 */       this.fDocument = this.fDeferredDocumentImpl;
/*  834 */       this.fDocumentIndex = this.fDeferredDocumentImpl.createDeferredDocument();
/*      */ 
/*  839 */       this.fDeferredDocumentImpl.setInputEncoding(encoding);
/*      */ 
/*  841 */       this.fDeferredDocumentImpl.setDocumentURI(locator.getExpandedSystemId());
/*  842 */       this.fCurrentNodeIndex = this.fDocumentIndex;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  864 */     if (!this.fDeferNodeExpansion)
/*      */     {
/*  867 */       if (this.fDocumentImpl != null) {
/*  868 */         if (version != null)
/*  869 */           this.fDocumentImpl.setXmlVersion(version);
/*  870 */         this.fDocumentImpl.setXmlEncoding(encoding);
/*  871 */         this.fDocumentImpl.setXmlStandalone("yes".equals(standalone));
/*      */       }
/*      */     }
/*      */     else {
/*  875 */       if (version != null)
/*  876 */         this.fDeferredDocumentImpl.setXmlVersion(version);
/*  877 */       this.fDeferredDocumentImpl.setXmlEncoding(encoding);
/*  878 */       this.fDeferredDocumentImpl.setXmlStandalone("yes".equals(standalone));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  898 */     if (!this.fDeferNodeExpansion) {
/*  899 */       if (this.fDocumentImpl != null) {
/*  900 */         this.fDocumentType = this.fDocumentImpl.createDocumentType(rootElement, publicId, systemId);
/*      */ 
/*  902 */         this.fCurrentNode.appendChild(this.fDocumentType);
/*      */       }
/*      */     }
/*      */     else {
/*  906 */       this.fDocumentTypeIndex = this.fDeferredDocumentImpl.createDeferredDocumentType(rootElement, publicId, systemId);
/*      */ 
/*  908 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, this.fDocumentTypeIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  929 */     if (!this.fDeferNodeExpansion) {
/*  930 */       if (this.fFilterReject) {
/*  931 */         this.fRejectedElementDepth += 1;
/*  932 */         return;
/*      */       }
/*  934 */       Element el = createElementNode(element);
/*  935 */       int attrCount = attributes.getLength();
/*  936 */       boolean seenSchemaDefault = false;
/*  937 */       for (int i = 0; i < attrCount; i++) {
/*  938 */         attributes.getName(i, this.fAttrQName);
/*  939 */         Attr attr = createAttrNode(this.fAttrQName);
/*      */ 
/*  941 */         String attrValue = attributes.getValue(i);
/*      */ 
/*  943 */         AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/*  944 */         if ((this.fStorePSVI) && (attrPSVI != null)) {
/*  945 */           ((PSVIAttrNSImpl)attr).setPSVI(attrPSVI);
/*      */         }
/*      */ 
/*  948 */         attr.setValue(attrValue);
/*  949 */         boolean specified = attributes.isSpecified(i);
/*      */ 
/*  953 */         if ((!specified) && ((seenSchemaDefault) || ((this.fAttrQName.uri != null) && (this.fAttrQName.uri != NamespaceContext.XMLNS_URI) && (this.fAttrQName.prefix == null))))
/*      */         {
/*  955 */           el.setAttributeNodeNS(attr);
/*  956 */           seenSchemaDefault = true;
/*      */         }
/*      */         else {
/*  959 */           el.setAttributeNode(attr);
/*      */         }
/*      */ 
/*  965 */         if (this.fDocumentImpl != null) {
/*  966 */           AttrImpl attrImpl = (AttrImpl)attr;
/*  967 */           Object type = null;
/*  968 */           boolean id = false;
/*      */ 
/*  975 */           if ((attrPSVI != null) && (this.fNamespaceAware))
/*      */           {
/*  977 */             type = attrPSVI.getMemberTypeDefinition();
/*  978 */             if (type == null) {
/*  979 */               type = attrPSVI.getTypeDefinition();
/*  980 */               if (type != null) {
/*  981 */                 id = ((XSSimpleType)type).isIDType();
/*  982 */                 attrImpl.setType(type);
/*      */               }
/*      */             }
/*      */             else {
/*  986 */               id = ((XSSimpleType)type).isIDType();
/*  987 */               attrImpl.setType(type);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  992 */             boolean isDeclared = Boolean.TRUE.equals(attributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
/*      */ 
/*  996 */             if (isDeclared) {
/*  997 */               type = attributes.getType(i);
/*  998 */               id = "ID".equals(type);
/*      */             }
/* 1000 */             attrImpl.setType(type);
/*      */           }
/*      */ 
/* 1003 */           if (id) {
/* 1004 */             ((ElementImpl)el).setIdAttributeNode(attr, true);
/*      */           }
/*      */ 
/* 1007 */           attrImpl.setSpecified(specified);
/*      */         }
/*      */       }
/*      */ 
/* 1011 */       setCharacterData(false);
/*      */ 
/* 1013 */       if (augs != null) {
/* 1014 */         ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 1015 */         if ((elementPSVI != null) && (this.fNamespaceAware)) {
/* 1016 */           XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
/* 1017 */           if (type == null) {
/* 1018 */             type = elementPSVI.getTypeDefinition();
/*      */           }
/* 1020 */           ((ElementNSImpl)el).setType(type);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1026 */       if ((this.fDOMFilter != null) && (!this.fInEntityRef)) {
/* 1027 */         if (this.fRoot == null)
/*      */         {
/* 1029 */           this.fRoot = el;
/*      */         } else {
/* 1031 */           short code = this.fDOMFilter.startElement(el);
/* 1032 */           switch (code)
/*      */           {
/*      */           case 4:
/* 1035 */             throw Abort.INSTANCE;
/*      */           case 2:
/* 1039 */             this.fFilterReject = true;
/* 1040 */             this.fRejectedElementDepth = 0;
/* 1041 */             return;
/*      */           case 3:
/* 1050 */             this.fFirstChunk = true;
/* 1051 */             this.fSkippedElemStack.push(Boolean.TRUE);
/* 1052 */             return;
/*      */           }
/*      */ 
/* 1056 */           if (!this.fSkippedElemStack.isEmpty()) {
/* 1057 */             this.fSkippedElemStack.push(Boolean.FALSE);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1063 */       this.fCurrentNode.appendChild(el);
/* 1064 */       this.fCurrentNode = el;
/*      */     }
/*      */     else {
/* 1067 */       int el = this.fDeferredDocumentImpl.createDeferredElement(this.fNamespaceAware ? element.uri : null, element.rawname);
/*      */ 
/* 1069 */       Object type = null;
/* 1070 */       int attrCount = attributes.getLength();
/*      */ 
/* 1073 */       for (int i = attrCount - 1; i >= 0; i--)
/*      */       {
/* 1076 */         AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/* 1077 */         boolean id = false;
/*      */ 
/* 1084 */         if ((attrPSVI != null) && (this.fNamespaceAware))
/*      */         {
/* 1086 */           type = attrPSVI.getMemberTypeDefinition();
/* 1087 */           if (type == null) {
/* 1088 */             type = attrPSVI.getTypeDefinition();
/* 1089 */             if (type != null)
/* 1090 */               id = ((XSSimpleType)type).isIDType();
/*      */           }
/*      */           else
/*      */           {
/* 1094 */             id = ((XSSimpleType)type).isIDType();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1099 */           boolean isDeclared = Boolean.TRUE.equals(attributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
/*      */ 
/* 1103 */           if (isDeclared) {
/* 1104 */             type = attributes.getType(i);
/* 1105 */             id = "ID".equals(type);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1110 */         this.fDeferredDocumentImpl.setDeferredAttribute(el, attributes.getQName(i), attributes.getURI(i), attributes.getValue(i), attributes.isSpecified(i), id, type);
/*      */       }
/*      */ 
/* 1120 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, el);
/* 1121 */       this.fCurrentNodeIndex = el;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1138 */     startElement(element, attributes, augs);
/* 1139 */     endElement(element, augs);
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1157 */     if (!this.fDeferNodeExpansion)
/*      */     {
/* 1159 */       if (this.fFilterReject) {
/* 1160 */         return;
/*      */       }
/* 1162 */       if ((this.fInCDATASection) && (this.fCreateCDATANodes)) {
/* 1163 */         if (this.fCurrentCDATASection == null) {
/* 1164 */           this.fCurrentCDATASection = this.fDocument.createCDATASection(text.toString());
/*      */ 
/* 1166 */           this.fCurrentNode.appendChild(this.fCurrentCDATASection);
/* 1167 */           this.fCurrentNode = this.fCurrentCDATASection;
/*      */         }
/*      */         else {
/* 1170 */           this.fCurrentCDATASection.appendData(text.toString());
/*      */         }
/*      */       }
/* 1173 */       else if (!this.fInDTD)
/*      */       {
/* 1176 */         if (text.length == 0) {
/* 1177 */           return;
/*      */         }
/*      */ 
/* 1180 */         Node child = this.fCurrentNode.getLastChild();
/* 1181 */         if ((child != null) && (child.getNodeType() == 3))
/*      */         {
/* 1183 */           if (this.fFirstChunk) {
/* 1184 */             if (this.fDocumentImpl != null) {
/* 1185 */               this.fStringBuilder.append(((TextImpl)child).removeData());
/*      */             } else {
/* 1187 */               this.fStringBuilder.append(((Text)child).getData());
/* 1188 */               ((Text)child).setNodeValue(null);
/*      */             }
/* 1190 */             this.fFirstChunk = false;
/*      */           }
/* 1192 */           if (text.length > 0)
/* 1193 */             this.fStringBuilder.append(text.ch, text.offset, text.length);
/*      */         }
/*      */         else
/*      */         {
/* 1197 */           this.fFirstChunk = true;
/* 1198 */           Text textNode = this.fDocument.createTextNode(text.toString());
/* 1199 */           this.fCurrentNode.appendChild(textNode);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1207 */     else if ((this.fInCDATASection) && (this.fCreateCDATANodes)) {
/* 1208 */       if (this.fCurrentCDATASectionIndex == -1) {
/* 1209 */         int cs = this.fDeferredDocumentImpl.createDeferredCDATASection(text.toString());
/*      */ 
/* 1212 */         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, cs);
/* 1213 */         this.fCurrentCDATASectionIndex = cs;
/* 1214 */         this.fCurrentNodeIndex = cs;
/*      */       }
/*      */       else {
/* 1217 */         int txt = this.fDeferredDocumentImpl.createDeferredTextNode(text.toString(), false);
/*      */ 
/* 1219 */         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, txt);
/*      */       }
/* 1221 */     } else if (!this.fInDTD)
/*      */     {
/* 1224 */       if (text.length == 0) {
/* 1225 */         return;
/*      */       }
/*      */ 
/* 1228 */       String value = text.toString();
/* 1229 */       int txt = this.fDeferredDocumentImpl.createDeferredTextNode(value, false);
/*      */ 
/* 1231 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, txt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1252 */     if ((!this.fIncludeIgnorableWhitespace) || (this.fFilterReject)) {
/* 1253 */       return;
/*      */     }
/* 1255 */     if (!this.fDeferNodeExpansion) {
/* 1256 */       Node child = this.fCurrentNode.getLastChild();
/* 1257 */       if ((child != null) && (child.getNodeType() == 3)) {
/* 1258 */         Text textNode = (Text)child;
/* 1259 */         textNode.appendData(text.toString());
/*      */       }
/*      */       else {
/* 1262 */         Text textNode = this.fDocument.createTextNode(text.toString());
/* 1263 */         if (this.fDocumentImpl != null) {
/* 1264 */           TextImpl textNodeImpl = (TextImpl)textNode;
/* 1265 */           textNodeImpl.setIgnorableWhitespace(true);
/*      */         }
/* 1267 */         this.fCurrentNode.appendChild(textNode);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1273 */       int txt = this.fDeferredDocumentImpl.createDeferredTextNode(text.toString(), true);
/*      */ 
/* 1275 */       this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, txt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1292 */     if (!this.fDeferNodeExpansion)
/*      */     {
/* 1295 */       if ((augs != null) && (this.fDocumentImpl != null) && ((this.fNamespaceAware) || (this.fStorePSVI))) {
/* 1296 */         ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 1297 */         if (elementPSVI != null)
/*      */         {
/* 1301 */           if (this.fNamespaceAware) {
/* 1302 */             XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
/* 1303 */             if (type == null) {
/* 1304 */               type = elementPSVI.getTypeDefinition();
/*      */             }
/* 1306 */             ((ElementNSImpl)this.fCurrentNode).setType(type);
/*      */           }
/* 1308 */           if (this.fStorePSVI) {
/* 1309 */             ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(elementPSVI);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1314 */       if (this.fDOMFilter != null) {
/* 1315 */         if (this.fFilterReject) {
/* 1316 */           if (this.fRejectedElementDepth-- == 0) {
/* 1317 */             this.fFilterReject = false;
/*      */           }
/* 1319 */           return;
/*      */         }
/* 1321 */         if ((!this.fSkippedElemStack.isEmpty()) && 
/* 1322 */           (this.fSkippedElemStack.pop() == Boolean.TRUE)) {
/* 1323 */           return;
/*      */         }
/*      */ 
/* 1326 */         setCharacterData(false);
/* 1327 */         if ((this.fCurrentNode != this.fRoot) && (!this.fInEntityRef) && ((this.fDOMFilter.getWhatToShow() & 0x1) != 0)) {
/* 1328 */           short code = this.fDOMFilter.acceptNode(this.fCurrentNode);
/* 1329 */           switch (code) {
/*      */           case 4:
/* 1331 */             throw Abort.INSTANCE;
/*      */           case 2:
/* 1334 */             Node parent = this.fCurrentNode.getParentNode();
/* 1335 */             parent.removeChild(this.fCurrentNode);
/* 1336 */             this.fCurrentNode = parent;
/* 1337 */             return;
/*      */           case 3:
/* 1345 */             this.fFirstChunk = true;
/*      */ 
/* 1348 */             Node parent = this.fCurrentNode.getParentNode();
/* 1349 */             NodeList ls = this.fCurrentNode.getChildNodes();
/* 1350 */             int length = ls.getLength();
/*      */ 
/* 1352 */             for (int i = 0; i < length; i++) {
/* 1353 */               parent.appendChild(ls.item(0));
/*      */             }
/* 1355 */             parent.removeChild(this.fCurrentNode);
/* 1356 */             this.fCurrentNode = parent;
/*      */ 
/* 1358 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1364 */         this.fCurrentNode = this.fCurrentNode.getParentNode();
/*      */       }
/*      */       else
/*      */       {
/* 1368 */         setCharacterData(false);
/* 1369 */         this.fCurrentNode = this.fCurrentNode.getParentNode();
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1374 */       if (augs != null) {
/* 1375 */         ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 1376 */         if (elementPSVI != null)
/*      */         {
/* 1380 */           XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
/* 1381 */           if (type == null) {
/* 1382 */             type = elementPSVI.getTypeDefinition();
/*      */           }
/* 1384 */           this.fDeferredDocumentImpl.setTypeInfo(this.fCurrentNodeIndex, type);
/*      */         }
/*      */       }
/* 1387 */       this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1403 */     this.fInCDATASection = true;
/* 1404 */     if (!this.fDeferNodeExpansion) {
/* 1405 */       if (this.fFilterReject) {
/* 1406 */         return;
/*      */       }
/* 1408 */       if (this.fCreateCDATANodes)
/* 1409 */         setCharacterData(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1422 */     this.fInCDATASection = false;
/* 1423 */     if (!this.fDeferNodeExpansion)
/*      */     {
/* 1425 */       if (this.fFilterReject) {
/* 1426 */         return;
/*      */       }
/*      */ 
/* 1429 */       if (this.fCurrentCDATASection != null)
/*      */       {
/* 1431 */         if ((this.fDOMFilter != null) && (!this.fInEntityRef) && ((this.fDOMFilter.getWhatToShow() & 0x8) != 0))
/*      */         {
/* 1433 */           short code = this.fDOMFilter.acceptNode(this.fCurrentCDATASection);
/* 1434 */           switch (code) {
/*      */           case 4:
/* 1436 */             throw Abort.INSTANCE;
/*      */           case 2:
/*      */           case 3:
/* 1442 */             Node parent = this.fCurrentNode.getParentNode();
/* 1443 */             parent.removeChild(this.fCurrentCDATASection);
/* 1444 */             this.fCurrentNode = parent;
/* 1445 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1454 */         this.fCurrentNode = this.fCurrentNode.getParentNode();
/* 1455 */         this.fCurrentCDATASection = null;
/*      */       }
/*      */ 
/*      */     }
/* 1459 */     else if (this.fCurrentCDATASectionIndex != -1) {
/* 1460 */       this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
/*      */ 
/* 1462 */       this.fCurrentCDATASectionIndex = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1476 */     if (!this.fDeferNodeExpansion)
/*      */     {
/* 1480 */       if (this.fDocumentImpl != null) {
/* 1481 */         if ((this.fLocator != null) && 
/* 1482 */           (this.fLocator.getEncoding() != null)) {
/* 1483 */           this.fDocumentImpl.setInputEncoding(this.fLocator.getEncoding());
/*      */         }
/* 1485 */         this.fDocumentImpl.setStrictErrorChecking(true);
/*      */       }
/* 1487 */       this.fCurrentNode = null;
/*      */     }
/*      */     else
/*      */     {
/* 1491 */       if ((this.fLocator != null) && 
/* 1492 */         (this.fLocator.getEncoding() != null)) {
/* 1493 */         this.fDeferredDocumentImpl.setInputEncoding(this.fLocator.getEncoding());
/*      */       }
/* 1495 */       this.fCurrentNodeIndex = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1516 */     if (!this.fDeferNodeExpansion)
/*      */     {
/* 1518 */       if (this.fFilterReject) {
/* 1519 */         return;
/*      */       }
/* 1521 */       setCharacterData(true);
/*      */ 
/* 1523 */       if (this.fDocumentType != null)
/*      */       {
/* 1525 */         NamedNodeMap entities = this.fDocumentType.getEntities();
/* 1526 */         this.fCurrentEntityDecl = ((EntityImpl)entities.getNamedItem(name));
/* 1527 */         if (this.fCurrentEntityDecl != null) {
/* 1528 */           if ((this.fCurrentEntityDecl != null) && (this.fCurrentEntityDecl.getFirstChild() == null)) {
/* 1529 */             this.fCurrentEntityDecl.setReadOnly(false, true);
/* 1530 */             Node child = this.fCurrentNode.getFirstChild();
/* 1531 */             while (child != null) {
/* 1532 */               Node copy = child.cloneNode(true);
/* 1533 */               this.fCurrentEntityDecl.appendChild(copy);
/* 1534 */               child = child.getNextSibling();
/*      */             }
/* 1536 */             this.fCurrentEntityDecl.setReadOnly(true, true);
/*      */           }
/*      */ 
/* 1540 */           this.fCurrentEntityDecl = null;
/*      */         }
/*      */       }
/*      */ 
/* 1544 */       this.fInEntityRef = false;
/* 1545 */       boolean removeEntityRef = false;
/* 1546 */       if (this.fCreateEntityRefNodes) {
/* 1547 */         if (this.fDocumentImpl != null)
/*      */         {
/* 1549 */           ((NodeImpl)this.fCurrentNode).setReadOnly(true, true);
/*      */         }
/*      */ 
/* 1552 */         if ((this.fDOMFilter != null) && ((this.fDOMFilter.getWhatToShow() & 0x10) != 0))
/*      */         {
/* 1554 */           short code = this.fDOMFilter.acceptNode(this.fCurrentNode);
/* 1555 */           switch (code) {
/*      */           case 4:
/* 1557 */             throw Abort.INSTANCE;
/*      */           case 2:
/* 1560 */             Node parent = this.fCurrentNode.getParentNode();
/* 1561 */             parent.removeChild(this.fCurrentNode);
/* 1562 */             this.fCurrentNode = parent;
/* 1563 */             return;
/*      */           case 3:
/* 1568 */             this.fFirstChunk = true;
/* 1569 */             removeEntityRef = true;
/* 1570 */             break;
/*      */           default:
/* 1574 */             this.fCurrentNode = this.fCurrentNode.getParentNode();
/*      */           }
/*      */         }
/*      */         else {
/* 1578 */           this.fCurrentNode = this.fCurrentNode.getParentNode();
/*      */         }
/*      */       }
/*      */ 
/* 1582 */       if ((!this.fCreateEntityRefNodes) || (removeEntityRef))
/*      */       {
/* 1585 */         NodeList children = this.fCurrentNode.getChildNodes();
/* 1586 */         Node parent = this.fCurrentNode.getParentNode();
/* 1587 */         int length = children.getLength();
/* 1588 */         if (length > 0)
/*      */         {
/* 1591 */           Node node = this.fCurrentNode.getPreviousSibling();
/*      */ 
/* 1593 */           Node child = children.item(0);
/* 1594 */           if ((node != null) && (node.getNodeType() == 3) && (child.getNodeType() == 3))
/*      */           {
/* 1596 */             ((Text)node).appendData(child.getNodeValue());
/* 1597 */             this.fCurrentNode.removeChild(child);
/*      */           }
/*      */           else {
/* 1600 */             node = parent.insertBefore(child, this.fCurrentNode);
/* 1601 */             handleBaseURI(node);
/*      */           }
/*      */ 
/* 1604 */           for (int i = 1; i < length; i++) {
/* 1605 */             node = parent.insertBefore(children.item(0), this.fCurrentNode);
/* 1606 */             handleBaseURI(node);
/*      */           }
/*      */         }
/* 1609 */         parent.removeChild(this.fCurrentNode);
/* 1610 */         this.fCurrentNode = parent;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1615 */       if (this.fDocumentTypeIndex != -1)
/*      */       {
/* 1617 */         int node = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/* 1618 */         while (node != -1) {
/* 1619 */           short nodeType = this.fDeferredDocumentImpl.getNodeType(node, false);
/* 1620 */           if (nodeType == 6) {
/* 1621 */             String nodeName = this.fDeferredDocumentImpl.getNodeName(node, false);
/*      */ 
/* 1623 */             if (nodeName.equals(name)) {
/* 1624 */               this.fDeferredEntityDecl = node;
/* 1625 */               break;
/*      */             }
/*      */           }
/* 1628 */           node = this.fDeferredDocumentImpl.getRealPrevSibling(node, false);
/*      */         }
/*      */       }
/*      */ 
/* 1632 */       if ((this.fDeferredEntityDecl != -1) && (this.fDeferredDocumentImpl.getLastChild(this.fDeferredEntityDecl, false) == -1))
/*      */       {
/* 1635 */         int prevIndex = -1;
/* 1636 */         int childIndex = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false);
/* 1637 */         while (childIndex != -1) {
/* 1638 */           int cloneIndex = this.fDeferredDocumentImpl.cloneNode(childIndex, true);
/* 1639 */           this.fDeferredDocumentImpl.insertBefore(this.fDeferredEntityDecl, cloneIndex, prevIndex);
/* 1640 */           prevIndex = cloneIndex;
/* 1641 */           childIndex = this.fDeferredDocumentImpl.getRealPrevSibling(childIndex, false);
/*      */         }
/*      */       }
/* 1644 */       if (this.fCreateEntityRefNodes) {
/* 1645 */         this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
/*      */       }
/*      */       else
/*      */       {
/* 1653 */         int childIndex = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false);
/* 1654 */         int parentIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
/*      */ 
/* 1658 */         int prevIndex = this.fCurrentNodeIndex;
/* 1659 */         int lastChild = childIndex;
/* 1660 */         int sibling = -1;
/* 1661 */         while (childIndex != -1) {
/* 1662 */           handleBaseURI(childIndex);
/* 1663 */           sibling = this.fDeferredDocumentImpl.getRealPrevSibling(childIndex, false);
/* 1664 */           this.fDeferredDocumentImpl.insertBefore(parentIndex, childIndex, prevIndex);
/* 1665 */           prevIndex = childIndex;
/* 1666 */           childIndex = sibling;
/*      */         }
/* 1668 */         if (lastChild != -1) {
/* 1669 */           this.fDeferredDocumentImpl.setAsLastChild(parentIndex, lastChild);
/*      */         } else {
/* 1671 */           sibling = this.fDeferredDocumentImpl.getRealPrevSibling(prevIndex, false);
/* 1672 */           this.fDeferredDocumentImpl.setAsLastChild(parentIndex, sibling);
/*      */         }
/* 1674 */         this.fCurrentNodeIndex = parentIndex;
/*      */       }
/* 1676 */       this.fDeferredEntityDecl = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void handleBaseURI(Node node)
/*      */   {
/* 1691 */     if (this.fDocumentImpl != null)
/*      */     {
/* 1695 */       String baseURI = null;
/* 1696 */       short nodeType = node.getNodeType();
/*      */ 
/* 1698 */       if (nodeType == 1)
/*      */       {
/* 1701 */         if (this.fNamespaceAware)
/*      */         {
/* 1702 */           if (((Element)node).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base") == null);
/*      */         }
/* 1705 */         else if (((Element)node).getAttributeNode("xml:base") != null) {
/* 1706 */           return;
/*      */         }
/*      */ 
/* 1709 */         baseURI = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI();
/* 1710 */         if ((baseURI != null) && (!baseURI.equals(this.fDocumentImpl.getDocumentURI()))) {
/* 1711 */           if (this.fNamespaceAware)
/* 1712 */             ((Element)node).setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", baseURI);
/*      */           else {
/* 1714 */             ((Element)node).setAttribute("xml:base", baseURI);
/*      */           }
/*      */         }
/*      */       }
/* 1718 */       else if (nodeType == 7)
/*      */       {
/* 1720 */         baseURI = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI();
/* 1721 */         if ((baseURI != null) && (this.fErrorHandler != null)) {
/* 1722 */           DOMErrorImpl error = new DOMErrorImpl();
/* 1723 */           error.fType = "pi-base-uri-not-preserved";
/* 1724 */           error.fRelatedData = baseURI;
/* 1725 */           error.fSeverity = 1;
/* 1726 */           this.fErrorHandler.getErrorHandler().handleError(error);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void handleBaseURI(int node)
/*      */   {
/* 1741 */     short nodeType = this.fDeferredDocumentImpl.getNodeType(node, false);
/*      */ 
/* 1743 */     if (nodeType == 1) {
/* 1744 */       String baseURI = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
/* 1745 */       if (baseURI == null) {
/* 1746 */         baseURI = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
/*      */       }
/* 1748 */       if ((baseURI != null) && (!baseURI.equals(this.fDeferredDocumentImpl.getDocumentURI()))) {
/* 1749 */         this.fDeferredDocumentImpl.setDeferredAttribute(node, "xml:base", "http://www.w3.org/XML/1998/namespace", baseURI, true);
/*      */       }
/*      */ 
/*      */     }
/* 1756 */     else if (nodeType == 7)
/*      */     {
/* 1760 */       String baseURI = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
/*      */ 
/* 1762 */       if (baseURI == null)
/*      */       {
/* 1764 */         baseURI = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
/*      */       }
/*      */ 
/* 1767 */       if ((baseURI != null) && (this.fErrorHandler != null)) {
/* 1768 */         DOMErrorImpl error = new DOMErrorImpl();
/* 1769 */         error.fType = "pi-base-uri-not-preserved";
/* 1770 */         error.fRelatedData = baseURI;
/* 1771 */         error.fSeverity = 1;
/* 1772 */         this.fErrorHandler.getErrorHandler().handleError(error);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDTD(XMLLocator locator, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1805 */     this.fInDTD = true;
/* 1806 */     if (locator != null) {
/* 1807 */       this.fBaseURIStack.push(locator.getBaseSystemId());
/*      */     }
/* 1809 */     if ((this.fDeferNodeExpansion) || (this.fDocumentImpl != null))
/* 1810 */       this.fInternalSubset = new StringBuilder(1024);
/*      */   }
/*      */ 
/*      */   public void endDTD(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1827 */     this.fInDTD = false;
/* 1828 */     if (!this.fBaseURIStack.isEmpty()) {
/* 1829 */       this.fBaseURIStack.pop();
/*      */     }
/* 1831 */     String internalSubset = (this.fInternalSubset != null) && (this.fInternalSubset.length() > 0) ? this.fInternalSubset.toString() : null;
/*      */ 
/* 1833 */     if (this.fDeferNodeExpansion) {
/* 1834 */       if (internalSubset != null) {
/* 1835 */         this.fDeferredDocumentImpl.setInternalSubset(this.fDocumentTypeIndex, internalSubset);
/*      */       }
/*      */     }
/* 1838 */     else if ((this.fDocumentImpl != null) && 
/* 1839 */       (internalSubset != null))
/* 1840 */       ((DocumentTypeImpl)this.fDocumentType).setInternalSubset(internalSubset);
/*      */   }
/*      */ 
/*      */   public void startConditional(short type, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endConditional(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1890 */     this.fBaseURIStack.push(identifier.getBaseSystemId());
/* 1891 */     this.fInDTDExternalSubset = true;
/*      */   }
/*      */ 
/*      */   public void endExternalSubset(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1903 */     this.fInDTDExternalSubset = false;
/* 1904 */     this.fBaseURIStack.pop();
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1934 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 1935 */       this.fInternalSubset.append("<!ENTITY ");
/* 1936 */       if (name.startsWith("%")) {
/* 1937 */         this.fInternalSubset.append("% ");
/* 1938 */         this.fInternalSubset.append(name.substring(1));
/*      */       }
/*      */       else {
/* 1941 */         this.fInternalSubset.append(name);
/*      */       }
/* 1943 */       this.fInternalSubset.append(' ');
/* 1944 */       String value = nonNormalizedText.toString();
/* 1945 */       boolean singleQuote = value.indexOf('\'') == -1;
/* 1946 */       this.fInternalSubset.append(singleQuote ? '\'' : '"');
/* 1947 */       this.fInternalSubset.append(value);
/* 1948 */       this.fInternalSubset.append(singleQuote ? '\'' : '"');
/* 1949 */       this.fInternalSubset.append(">\n");
/*      */     }
/*      */ 
/* 1958 */     if (name.startsWith("%"))
/* 1959 */       return;
/* 1960 */     if (this.fDocumentType != null) {
/* 1961 */       NamedNodeMap entities = this.fDocumentType.getEntities();
/* 1962 */       EntityImpl entity = (EntityImpl)entities.getNamedItem(name);
/* 1963 */       if (entity == null) {
/* 1964 */         entity = (EntityImpl)this.fDocumentImpl.createEntity(name);
/* 1965 */         entity.setBaseURI((String)this.fBaseURIStack.peek());
/* 1966 */         entities.setNamedItem(entity);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1971 */     if (this.fDocumentTypeIndex != -1) {
/* 1972 */       boolean found = false;
/* 1973 */       int node = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/* 1974 */       while (node != -1) {
/* 1975 */         short nodeType = this.fDeferredDocumentImpl.getNodeType(node, false);
/* 1976 */         if (nodeType == 6) {
/* 1977 */           String nodeName = this.fDeferredDocumentImpl.getNodeName(node, false);
/* 1978 */           if (nodeName.equals(name)) {
/* 1979 */             found = true;
/* 1980 */             break;
/*      */           }
/*      */         }
/* 1983 */         node = this.fDeferredDocumentImpl.getRealPrevSibling(node, false);
/*      */       }
/* 1985 */       if (!found) {
/* 1986 */         int entityIndex = this.fDeferredDocumentImpl.createDeferredEntity(name, null, null, null, (String)this.fBaseURIStack.peek());
/*      */ 
/* 1988 */         this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, entityIndex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2019 */     String publicId = identifier.getPublicId();
/* 2020 */     String literalSystemId = identifier.getLiteralSystemId();
/* 2021 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 2022 */       this.fInternalSubset.append("<!ENTITY ");
/* 2023 */       if (name.startsWith("%")) {
/* 2024 */         this.fInternalSubset.append("% ");
/* 2025 */         this.fInternalSubset.append(name.substring(1));
/*      */       }
/*      */       else {
/* 2028 */         this.fInternalSubset.append(name);
/*      */       }
/* 2030 */       this.fInternalSubset.append(' ');
/* 2031 */       if (publicId != null) {
/* 2032 */         this.fInternalSubset.append("PUBLIC '");
/* 2033 */         this.fInternalSubset.append(publicId);
/* 2034 */         this.fInternalSubset.append("' '");
/*      */       }
/*      */       else {
/* 2037 */         this.fInternalSubset.append("SYSTEM '");
/*      */       }
/* 2039 */       this.fInternalSubset.append(literalSystemId);
/* 2040 */       this.fInternalSubset.append("'>\n");
/*      */     }
/*      */ 
/* 2049 */     if (name.startsWith("%"))
/* 2050 */       return;
/* 2051 */     if (this.fDocumentType != null) {
/* 2052 */       NamedNodeMap entities = this.fDocumentType.getEntities();
/* 2053 */       EntityImpl entity = (EntityImpl)entities.getNamedItem(name);
/* 2054 */       if (entity == null) {
/* 2055 */         entity = (EntityImpl)this.fDocumentImpl.createEntity(name);
/* 2056 */         entity.setPublicId(publicId);
/* 2057 */         entity.setSystemId(literalSystemId);
/* 2058 */         entity.setBaseURI(identifier.getBaseSystemId());
/* 2059 */         entities.setNamedItem(entity);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2064 */     if (this.fDocumentTypeIndex != -1) {
/* 2065 */       boolean found = false;
/* 2066 */       int nodeIndex = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/* 2067 */       while (nodeIndex != -1) {
/* 2068 */         short nodeType = this.fDeferredDocumentImpl.getNodeType(nodeIndex, false);
/* 2069 */         if (nodeType == 6) {
/* 2070 */           String nodeName = this.fDeferredDocumentImpl.getNodeName(nodeIndex, false);
/* 2071 */           if (nodeName.equals(name)) {
/* 2072 */             found = true;
/* 2073 */             break;
/*      */           }
/*      */         }
/* 2076 */         nodeIndex = this.fDeferredDocumentImpl.getRealPrevSibling(nodeIndex, false);
/*      */       }
/* 2078 */       if (!found) {
/* 2079 */         int entityIndex = this.fDeferredDocumentImpl.createDeferredEntity(name, publicId, literalSystemId, null, identifier.getBaseSystemId());
/*      */ 
/* 2081 */         this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, entityIndex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2114 */     if ((augs != null) && (this.fInternalSubset != null) && (!this.fInDTDExternalSubset) && (Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED"))))
/*      */     {
/* 2117 */       this.fInternalSubset.append(name).append(";\n");
/*      */     }
/* 2119 */     this.fBaseURIStack.push(identifier.getExpandedSystemId());
/*      */   }
/*      */ 
/*      */   public void endParameterEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2138 */     this.fBaseURIStack.pop();
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2165 */     String publicId = identifier.getPublicId();
/* 2166 */     String literalSystemId = identifier.getLiteralSystemId();
/* 2167 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 2168 */       this.fInternalSubset.append("<!ENTITY ");
/* 2169 */       this.fInternalSubset.append(name);
/* 2170 */       this.fInternalSubset.append(' ');
/* 2171 */       if (publicId != null) {
/* 2172 */         this.fInternalSubset.append("PUBLIC '");
/* 2173 */         this.fInternalSubset.append(publicId);
/* 2174 */         if (literalSystemId != null) {
/* 2175 */           this.fInternalSubset.append("' '");
/* 2176 */           this.fInternalSubset.append(literalSystemId);
/*      */         }
/*      */       }
/*      */       else {
/* 2180 */         this.fInternalSubset.append("SYSTEM '");
/* 2181 */         this.fInternalSubset.append(literalSystemId);
/*      */       }
/* 2183 */       this.fInternalSubset.append("' NDATA ");
/* 2184 */       this.fInternalSubset.append(notation);
/* 2185 */       this.fInternalSubset.append(">\n");
/*      */     }
/*      */ 
/* 2193 */     if (this.fDocumentType != null) {
/* 2194 */       NamedNodeMap entities = this.fDocumentType.getEntities();
/* 2195 */       EntityImpl entity = (EntityImpl)entities.getNamedItem(name);
/* 2196 */       if (entity == null) {
/* 2197 */         entity = (EntityImpl)this.fDocumentImpl.createEntity(name);
/* 2198 */         entity.setPublicId(publicId);
/* 2199 */         entity.setSystemId(literalSystemId);
/* 2200 */         entity.setNotationName(notation);
/* 2201 */         entity.setBaseURI(identifier.getBaseSystemId());
/* 2202 */         entities.setNamedItem(entity);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2207 */     if (this.fDocumentTypeIndex != -1) {
/* 2208 */       boolean found = false;
/* 2209 */       int nodeIndex = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/* 2210 */       while (nodeIndex != -1) {
/* 2211 */         short nodeType = this.fDeferredDocumentImpl.getNodeType(nodeIndex, false);
/* 2212 */         if (nodeType == 6) {
/* 2213 */           String nodeName = this.fDeferredDocumentImpl.getNodeName(nodeIndex, false);
/* 2214 */           if (nodeName.equals(name)) {
/* 2215 */             found = true;
/* 2216 */             break;
/*      */           }
/*      */         }
/* 2219 */         nodeIndex = this.fDeferredDocumentImpl.getRealPrevSibling(nodeIndex, false);
/*      */       }
/* 2221 */       if (!found) {
/* 2222 */         int entityIndex = this.fDeferredDocumentImpl.createDeferredEntity(name, publicId, literalSystemId, notation, identifier.getBaseSystemId());
/*      */ 
/* 2224 */         this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, entityIndex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2245 */     String publicId = identifier.getPublicId();
/* 2246 */     String literalSystemId = identifier.getLiteralSystemId();
/* 2247 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 2248 */       this.fInternalSubset.append("<!NOTATION ");
/* 2249 */       this.fInternalSubset.append(name);
/* 2250 */       if (publicId != null) {
/* 2251 */         this.fInternalSubset.append(" PUBLIC '");
/* 2252 */         this.fInternalSubset.append(publicId);
/* 2253 */         if (literalSystemId != null) {
/* 2254 */           this.fInternalSubset.append("' '");
/* 2255 */           this.fInternalSubset.append(literalSystemId);
/*      */         }
/*      */       }
/*      */       else {
/* 2259 */         this.fInternalSubset.append(" SYSTEM '");
/* 2260 */         this.fInternalSubset.append(literalSystemId);
/*      */       }
/* 2262 */       this.fInternalSubset.append("'>\n");
/*      */     }
/*      */ 
/* 2270 */     if ((this.fDocumentImpl != null) && (this.fDocumentType != null)) {
/* 2271 */       NamedNodeMap notations = this.fDocumentType.getNotations();
/* 2272 */       if (notations.getNamedItem(name) == null) {
/* 2273 */         NotationImpl notation = (NotationImpl)this.fDocumentImpl.createNotation(name);
/* 2274 */         notation.setPublicId(publicId);
/* 2275 */         notation.setSystemId(literalSystemId);
/* 2276 */         notation.setBaseURI(identifier.getBaseSystemId());
/* 2277 */         notations.setNamedItem(notation);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2282 */     if (this.fDocumentTypeIndex != -1) {
/* 2283 */       boolean found = false;
/* 2284 */       int nodeIndex = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
/* 2285 */       while (nodeIndex != -1) {
/* 2286 */         short nodeType = this.fDeferredDocumentImpl.getNodeType(nodeIndex, false);
/* 2287 */         if (nodeType == 12) {
/* 2288 */           String nodeName = this.fDeferredDocumentImpl.getNodeName(nodeIndex, false);
/* 2289 */           if (nodeName.equals(name)) {
/* 2290 */             found = true;
/* 2291 */             break;
/*      */           }
/*      */         }
/* 2294 */         nodeIndex = this.fDeferredDocumentImpl.getPrevSibling(nodeIndex, false);
/*      */       }
/* 2296 */       if (!found) {
/* 2297 */         int notationIndex = this.fDeferredDocumentImpl.createDeferredNotation(name, publicId, literalSystemId, identifier.getBaseSystemId());
/*      */ 
/* 2299 */         this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, notationIndex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignoredCharacters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2332 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 2333 */       this.fInternalSubset.append("<!ELEMENT ");
/* 2334 */       this.fInternalSubset.append(name);
/* 2335 */       this.fInternalSubset.append(' ');
/* 2336 */       this.fInternalSubset.append(contentModel);
/* 2337 */       this.fInternalSubset.append(">\n");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 2373 */     if ((this.fInternalSubset != null) && (!this.fInDTDExternalSubset)) {
/* 2374 */       this.fInternalSubset.append("<!ATTLIST ");
/* 2375 */       this.fInternalSubset.append(elementName);
/* 2376 */       this.fInternalSubset.append(' ');
/* 2377 */       this.fInternalSubset.append(attributeName);
/* 2378 */       this.fInternalSubset.append(' ');
/* 2379 */       if (type.equals("ENUMERATION")) {
/* 2380 */         this.fInternalSubset.append('(');
/* 2381 */         for (int i = 0; i < enumeration.length; i++) {
/* 2382 */           if (i > 0) {
/* 2383 */             this.fInternalSubset.append('|');
/*      */           }
/* 2385 */           this.fInternalSubset.append(enumeration[i]);
/*      */         }
/* 2387 */         this.fInternalSubset.append(')');
/*      */       }
/*      */       else {
/* 2390 */         this.fInternalSubset.append(type);
/*      */       }
/* 2392 */       if (defaultType != null) {
/* 2393 */         this.fInternalSubset.append(' ');
/* 2394 */         this.fInternalSubset.append(defaultType);
/*      */       }
/* 2396 */       if (defaultValue != null) {
/* 2397 */         this.fInternalSubset.append(" '");
/* 2398 */         for (int i = 0; i < defaultValue.length; i++) {
/* 2399 */           char c = defaultValue.ch[(defaultValue.offset + i)];
/* 2400 */           if (c == '\'') {
/* 2401 */             this.fInternalSubset.append("&apos;");
/*      */           }
/*      */           else {
/* 2404 */             this.fInternalSubset.append(c);
/*      */           }
/*      */         }
/* 2407 */         this.fInternalSubset.append('\'');
/*      */       }
/* 2409 */       this.fInternalSubset.append(">\n");
/*      */     }
/*      */ 
/* 2415 */     if (this.fDeferredDocumentImpl != null)
/*      */     {
/* 2418 */       if (defaultValue != null)
/*      */       {
/* 2421 */         int elementDefIndex = this.fDeferredDocumentImpl.lookupElementDefinition(elementName);
/*      */ 
/* 2424 */         if (elementDefIndex == -1) {
/* 2425 */           elementDefIndex = this.fDeferredDocumentImpl.createDeferredElementDefinition(elementName);
/* 2426 */           this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, elementDefIndex);
/*      */         }
/*      */ 
/* 2429 */         boolean nsEnabled = this.fNamespaceAware;
/* 2430 */         String namespaceURI = null;
/* 2431 */         if (nsEnabled)
/*      */         {
/* 2436 */           if ((attributeName.startsWith("xmlns:")) || (attributeName.equals("xmlns")))
/*      */           {
/* 2438 */             namespaceURI = NamespaceContext.XMLNS_URI;
/*      */           }
/* 2440 */           else if (attributeName.startsWith("xml:")) {
/* 2441 */             namespaceURI = NamespaceContext.XML_URI;
/*      */           }
/*      */         }
/* 2444 */         int attrIndex = this.fDeferredDocumentImpl.createDeferredAttribute(attributeName, namespaceURI, defaultValue.toString(), false);
/*      */ 
/* 2446 */         if ("ID".equals(type)) {
/* 2447 */           this.fDeferredDocumentImpl.setIdAttribute(attrIndex);
/*      */         }
/*      */ 
/* 2450 */         this.fDeferredDocumentImpl.appendChild(elementDefIndex, attrIndex);
/*      */       }
/*      */ 
/*      */     }
/* 2456 */     else if (this.fDocumentImpl != null)
/*      */     {
/* 2459 */       if (defaultValue != null)
/*      */       {
/* 2462 */         NamedNodeMap elements = ((DocumentTypeImpl)this.fDocumentType).getElements();
/* 2463 */         ElementDefinitionImpl elementDef = (ElementDefinitionImpl)elements.getNamedItem(elementName);
/* 2464 */         if (elementDef == null) {
/* 2465 */           elementDef = this.fDocumentImpl.createElementDefinition(elementName);
/* 2466 */           ((DocumentTypeImpl)this.fDocumentType).getElements().setNamedItem(elementDef);
/*      */         }
/*      */ 
/* 2472 */         boolean nsEnabled = this.fNamespaceAware;
/*      */         AttrImpl attr;
/*      */         AttrImpl attr;
/* 2474 */         if (nsEnabled) {
/* 2475 */           String namespaceURI = null;
/*      */ 
/* 2480 */           if ((attributeName.startsWith("xmlns:")) || (attributeName.equals("xmlns")))
/*      */           {
/* 2482 */             namespaceURI = NamespaceContext.XMLNS_URI;
/*      */           }
/* 2484 */           else if (attributeName.startsWith("xml:")) {
/* 2485 */             namespaceURI = NamespaceContext.XML_URI;
/*      */           }
/* 2487 */           attr = (AttrImpl)this.fDocumentImpl.createAttributeNS(namespaceURI, attributeName);
/*      */         }
/*      */         else
/*      */         {
/* 2491 */           attr = (AttrImpl)this.fDocumentImpl.createAttribute(attributeName);
/*      */         }
/* 2493 */         attr.setValue(defaultValue.toString());
/* 2494 */         attr.setSpecified(false);
/* 2495 */         attr.setIdAttribute("ID".equals(type));
/*      */ 
/* 2498 */         if (nsEnabled) {
/* 2499 */           elementDef.getAttributes().setNamedItemNS(attr);
/*      */         }
/*      */         else
/* 2502 */           elementDef.getAttributes().setNamedItem(attr);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startAttlist(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endAttlist(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected Element createElementNode(QName element)
/*      */   {
/* 2540 */     Element el = null;
/*      */ 
/* 2542 */     if (this.fNamespaceAware)
/*      */     {
/* 2545 */       if (this.fDocumentImpl != null) {
/* 2546 */         el = this.fDocumentImpl.createElementNS(element.uri, element.rawname, element.localpart);
/*      */       }
/*      */       else
/*      */       {
/* 2550 */         el = this.fDocument.createElementNS(element.uri, element.rawname);
/*      */       }
/*      */     }
/*      */     else {
/* 2554 */       el = this.fDocument.createElement(element.rawname);
/*      */     }
/*      */ 
/* 2557 */     return el;
/*      */   }
/*      */ 
/*      */   protected Attr createAttrNode(QName attrQName)
/*      */   {
/* 2563 */     Attr attr = null;
/*      */ 
/* 2565 */     if (this.fNamespaceAware) {
/* 2566 */       if (this.fDocumentImpl != null)
/*      */       {
/* 2569 */         attr = this.fDocumentImpl.createAttributeNS(attrQName.uri, attrQName.rawname, attrQName.localpart);
/*      */       }
/*      */       else
/*      */       {
/* 2574 */         attr = this.fDocument.createAttributeNS(attrQName.uri, attrQName.rawname);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2579 */       attr = this.fDocument.createAttribute(attrQName.rawname);
/*      */     }
/*      */ 
/* 2582 */     return attr;
/*      */   }
/*      */ 
/*      */   protected void setCharacterData(boolean sawChars)
/*      */   {
/* 2601 */     this.fFirstChunk = sawChars;
/*      */ 
/* 2607 */     Node child = this.fCurrentNode.getLastChild();
/* 2608 */     if (child != null) {
/* 2609 */       if (this.fStringBuilder.length() > 0)
/*      */       {
/* 2611 */         if (child.getNodeType() == 3) {
/* 2612 */           if (this.fDocumentImpl != null) {
/* 2613 */             ((TextImpl)child).replaceData(this.fStringBuilder.toString());
/*      */           }
/*      */           else {
/* 2616 */             ((Text)child).setData(this.fStringBuilder.toString());
/*      */           }
/*      */         }
/*      */ 
/* 2620 */         this.fStringBuilder.setLength(0);
/*      */       }
/*      */ 
/* 2623 */       if ((this.fDOMFilter != null) && (!this.fInEntityRef) && 
/* 2624 */         (child.getNodeType() == 3) && ((this.fDOMFilter.getWhatToShow() & 0x4) != 0))
/*      */       {
/* 2626 */         short code = this.fDOMFilter.acceptNode(child);
/* 2627 */         switch (code) {
/*      */         case 4:
/* 2629 */           throw Abort.INSTANCE;
/*      */         case 2:
/*      */         case 3:
/* 2635 */           this.fCurrentNode.removeChild(child);
/* 2636 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void abort()
/*      */   {
/* 2653 */     throw Abort.INSTANCE;
/*      */   }
/*      */ 
/*      */   static final class Abort extends RuntimeException
/*      */   {
/*      */     private static final long serialVersionUID = 1687848994976808490L;
/*  168 */     static final Abort INSTANCE = new Abort();
/*      */ 
/*      */     public Throwable fillInStackTrace() {
/*  171 */       return this;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser
 * JD-Core Version:    0.6.2
 */