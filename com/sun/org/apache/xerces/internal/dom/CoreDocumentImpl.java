/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.URI;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.CDATASection;
/*      */ import org.w3c.dom.Comment;
/*      */ import org.w3c.dom.DOMConfiguration;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Entity;
/*      */ import org.w3c.dom.EntityReference;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.Notation;
/*      */ import org.w3c.dom.ProcessingInstruction;
/*      */ import org.w3c.dom.Text;
/*      */ import org.w3c.dom.UserDataHandler;
/*      */ import org.w3c.dom.events.Event;
/*      */ import org.w3c.dom.events.EventListener;
/*      */ import org.w3c.dom.ls.DOMImplementationLS;
/*      */ import org.w3c.dom.ls.LSSerializer;
/*      */ 
/*      */ public class CoreDocumentImpl extends ParentNode
/*      */   implements Document
/*      */ {
/*      */   static final long serialVersionUID = 0L;
/*      */   protected DocumentTypeImpl docType;
/*      */   protected ElementImpl docElement;
/*      */   transient NodeListCache fFreeNLCache;
/*      */   protected String encoding;
/*      */   protected String actualEncoding;
/*      */   protected String version;
/*      */   protected boolean standalone;
/*      */   protected String fDocumentURI;
/*      */   protected Hashtable userData;
/*      */   protected Hashtable identifiers;
/*  142 */   transient DOMNormalizer domNormalizer = null;
/*  143 */   transient DOMConfigurationImpl fConfiguration = null;
/*      */ 
/*  146 */   transient Object fXPathEvaluator = null;
/*      */ 
/*  218 */   private static final int[] kidOK = new int[13];
/*      */ 
/*  185 */   protected int changes = 0;
/*      */   protected boolean allowGrammarAccess;
/*  193 */   protected boolean errorChecking = true;
/*      */ 
/*  195 */   protected boolean ancestorChecking = true;
/*      */ 
/*  199 */   protected boolean xmlVersionChanged = false;
/*      */ 
/*  205 */   private int documentNumber = 0;
/*      */ 
/*  209 */   private int nodeCounter = 0;
/*      */   private Hashtable nodeTable;
/*  211 */   private boolean xml11Version = false;
/*      */ 
/*      */   public CoreDocumentImpl()
/*      */   {
/*  255 */     this(false);
/*      */   }
/*      */ 
/*      */   public CoreDocumentImpl(boolean grammarAccess)
/*      */   {
/*  260 */     super(null);
/*  261 */     this.ownerDocument = this;
/*  262 */     this.allowGrammarAccess = grammarAccess;
/*  263 */     String systemProp = SecuritySupport.getSystemProperty("http://java.sun.com/xml/dom/properties/ancestor-check");
/*  264 */     if ((systemProp != null) && 
/*  265 */       (systemProp.equalsIgnoreCase("false")))
/*  266 */       this.ancestorChecking = false;
/*      */   }
/*      */ 
/*      */   public CoreDocumentImpl(DocumentType doctype)
/*      */   {
/*  276 */     this(doctype, false);
/*      */   }
/*      */ 
/*      */   public CoreDocumentImpl(DocumentType doctype, boolean grammarAccess)
/*      */   {
/*  281 */     this(grammarAccess);
/*  282 */     if (doctype != null) {
/*      */       DocumentTypeImpl doctypeImpl;
/*      */       try {
/*  285 */         doctypeImpl = (DocumentTypeImpl)doctype;
/*      */       } catch (ClassCastException e) {
/*  287 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*  288 */         throw new DOMException((short)4, msg);
/*      */       }
/*  290 */       doctypeImpl.ownerDocument = this;
/*  291 */       appendChild(doctype);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Document getOwnerDocument()
/*      */   {
/*  302 */     return null;
/*      */   }
/*      */ 
/*      */   public short getNodeType()
/*      */   {
/*  307 */     return 9;
/*      */   }
/*      */ 
/*      */   public String getNodeName()
/*      */   {
/*  312 */     return "#document";
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  326 */     CoreDocumentImpl newdoc = new CoreDocumentImpl();
/*  327 */     callUserDataHandlers(this, newdoc, (short)1);
/*  328 */     cloneNode(newdoc, deep);
/*      */ 
/*  330 */     return newdoc;
/*      */   }
/*      */ 
/*      */   protected void cloneNode(CoreDocumentImpl newdoc, boolean deep)
/*      */   {
/*  341 */     if (needsSyncChildren()) {
/*  342 */       synchronizeChildren();
/*      */     }
/*      */ 
/*  345 */     if (deep) {
/*  346 */       Hashtable reversedIdentifiers = null;
/*      */ 
/*  348 */       if (this.identifiers != null)
/*      */       {
/*  350 */         reversedIdentifiers = new Hashtable();
/*  351 */         Enumeration elementIds = this.identifiers.keys();
/*  352 */         while (elementIds.hasMoreElements()) {
/*  353 */           Object elementId = elementIds.nextElement();
/*  354 */           reversedIdentifiers.put(this.identifiers.get(elementId), elementId);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  360 */       for (ChildNode kid = this.firstChild; kid != null; 
/*  361 */         kid = kid.nextSibling) {
/*  362 */         newdoc.appendChild(newdoc.importNode(kid, true, true, reversedIdentifiers));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  368 */     newdoc.allowGrammarAccess = this.allowGrammarAccess;
/*  369 */     newdoc.errorChecking = this.errorChecking;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  391 */     int type = newChild.getNodeType();
/*  392 */     if ((this.errorChecking) && (
/*  393 */       ((type == 1) && (this.docElement != null)) || ((type == 10) && (this.docType != null))))
/*      */     {
/*  395 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/*  396 */       throw new DOMException((short)3, msg);
/*      */     }
/*      */ 
/*  400 */     if ((newChild.getOwnerDocument() == null) && ((newChild instanceof DocumentTypeImpl)))
/*      */     {
/*  402 */       ((DocumentTypeImpl)newChild).ownerDocument = this;
/*      */     }
/*  404 */     super.insertBefore(newChild, refChild);
/*      */ 
/*  407 */     if (type == 1) {
/*  408 */       this.docElement = ((ElementImpl)newChild);
/*      */     }
/*  410 */     else if (type == 10) {
/*  411 */       this.docType = ((DocumentTypeImpl)newChild);
/*      */     }
/*      */ 
/*  414 */     return newChild;
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  427 */     super.removeChild(oldChild);
/*      */ 
/*  430 */     int type = oldChild.getNodeType();
/*  431 */     if (type == 1) {
/*  432 */       this.docElement = null;
/*      */     }
/*  434 */     else if (type == 10) {
/*  435 */       this.docType = null;
/*      */     }
/*      */ 
/*  438 */     return oldChild;
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  453 */     if ((newChild.getOwnerDocument() == null) && ((newChild instanceof DocumentTypeImpl)))
/*      */     {
/*  455 */       ((DocumentTypeImpl)newChild).ownerDocument = this;
/*      */     }
/*      */ 
/*  458 */     if ((this.errorChecking) && (((this.docType != null) && (oldChild.getNodeType() != 10) && (newChild.getNodeType() == 10)) || ((this.docElement != null) && (oldChild.getNodeType() != 1) && (newChild.getNodeType() == 1))))
/*      */     {
/*  465 */       throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */     }
/*      */ 
/*  469 */     super.replaceChild(newChild, oldChild);
/*      */ 
/*  471 */     int type = oldChild.getNodeType();
/*  472 */     if (type == 1) {
/*  473 */       this.docElement = ((ElementImpl)newChild);
/*      */     }
/*  475 */     else if (type == 10) {
/*  476 */       this.docType = ((DocumentTypeImpl)newChild);
/*      */     }
/*  478 */     return oldChild;
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/*  486 */     return null;
/*      */   }
/*      */ 
/*      */   public void setTextContent(String textContent)
/*      */     throws DOMException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Object getFeature(String feature, String version)
/*      */   {
/*  503 */     boolean anyVersion = (version == null) || (version.length() == 0);
/*      */ 
/*  510 */     if ((feature.equalsIgnoreCase("+XPath")) && ((anyVersion) || (version.equals("3.0"))))
/*      */     {
/*  515 */       if (this.fXPathEvaluator != null) {
/*  516 */         return this.fXPathEvaluator;
/*      */       }
/*      */       try
/*      */       {
/*  520 */         Class xpathClass = ObjectFactory.findProviderClass("com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl", true);
/*      */ 
/*  522 */         Constructor xpathClassConstr = xpathClass.getConstructor(new Class[] { Document.class });
/*      */ 
/*  527 */         Class[] interfaces = xpathClass.getInterfaces();
/*  528 */         for (int i = 0; i < interfaces.length; i++) {
/*  529 */           if (interfaces[i].getName().equals("org.w3c.dom.xpath.XPathEvaluator"))
/*      */           {
/*  531 */             this.fXPathEvaluator = xpathClassConstr.newInstance(new Object[] { this });
/*  532 */             return this.fXPathEvaluator;
/*      */           }
/*      */         }
/*  535 */         return null;
/*      */       } catch (Exception e) {
/*  537 */         return null;
/*      */       }
/*      */     }
/*  540 */     return super.getFeature(feature, version);
/*      */   }
/*      */ 
/*      */   public Attr createAttribute(String name)
/*      */     throws DOMException
/*      */   {
/*  562 */     if ((this.errorChecking) && (!isXMLName(name, this.xml11Version))) {
/*  563 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*      */ 
/*  568 */       throw new DOMException((short)5, msg);
/*      */     }
/*  570 */     return new AttrImpl(this, name);
/*      */   }
/*      */ 
/*      */   public CDATASection createCDATASection(String data)
/*      */     throws DOMException
/*      */   {
/*  585 */     return new CDATASectionImpl(this, data);
/*      */   }
/*      */ 
/*      */   public Comment createComment(String data)
/*      */   {
/*  594 */     return new CommentImpl(this, data);
/*      */   }
/*      */ 
/*      */   public DocumentFragment createDocumentFragment()
/*      */   {
/*  602 */     return new DocumentFragmentImpl(this);
/*      */   }
/*      */ 
/*      */   public Element createElement(String tagName)
/*      */     throws DOMException
/*      */   {
/*  620 */     if ((this.errorChecking) && (!isXMLName(tagName, this.xml11Version))) {
/*  621 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*  622 */       throw new DOMException((short)5, msg);
/*      */     }
/*  624 */     return new ElementImpl(this, tagName);
/*      */   }
/*      */ 
/*      */   public EntityReference createEntityReference(String name)
/*      */     throws DOMException
/*      */   {
/*  641 */     if ((this.errorChecking) && (!isXMLName(name, this.xml11Version))) {
/*  642 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*  643 */       throw new DOMException((short)5, msg);
/*      */     }
/*  645 */     return new EntityReferenceImpl(this, name);
/*      */   }
/*      */ 
/*      */   public ProcessingInstruction createProcessingInstruction(String target, String data)
/*      */     throws DOMException
/*      */   {
/*  666 */     if ((this.errorChecking) && (!isXMLName(target, this.xml11Version))) {
/*  667 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*  668 */       throw new DOMException((short)5, msg);
/*      */     }
/*  670 */     return new ProcessingInstructionImpl(this, target, data);
/*      */   }
/*      */ 
/*      */   public Text createTextNode(String data)
/*      */   {
/*  681 */     return new TextImpl(this, data);
/*      */   }
/*      */ 
/*      */   public DocumentType getDoctype()
/*      */   {
/*  692 */     if (needsSyncChildren()) {
/*  693 */       synchronizeChildren();
/*      */     }
/*  695 */     return this.docType;
/*      */   }
/*      */ 
/*      */   public Element getDocumentElement()
/*      */   {
/*  709 */     if (needsSyncChildren()) {
/*  710 */       synchronizeChildren();
/*      */     }
/*  712 */     return this.docElement;
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagName(String tagname)
/*      */   {
/*  725 */     return new DeepNodeListImpl(this, tagname);
/*      */   }
/*      */ 
/*      */   public DOMImplementation getImplementation()
/*      */   {
/*  737 */     return CoreDOMImplementationImpl.getDOMImplementation();
/*      */   }
/*      */ 
/*      */   public void setErrorChecking(boolean check)
/*      */   {
/*  766 */     this.errorChecking = check;
/*      */   }
/*      */ 
/*      */   public void setStrictErrorChecking(boolean check)
/*      */   {
/*  773 */     this.errorChecking = check;
/*      */   }
/*      */ 
/*      */   public boolean getErrorChecking()
/*      */   {
/*  780 */     return this.errorChecking;
/*      */   }
/*      */ 
/*      */   public boolean getStrictErrorChecking()
/*      */   {
/*  787 */     return this.errorChecking;
/*      */   }
/*      */ 
/*      */   public String getInputEncoding()
/*      */   {
/*  801 */     return this.actualEncoding;
/*      */   }
/*      */ 
/*      */   public void setInputEncoding(String value)
/*      */   {
/*  814 */     this.actualEncoding = value;
/*      */   }
/*      */ 
/*      */   public void setXmlEncoding(String value)
/*      */   {
/*  825 */     this.encoding = value;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setEncoding(String value)
/*      */   {
/*  834 */     setXmlEncoding(value);
/*      */   }
/*      */ 
/*      */   public String getXmlEncoding()
/*      */   {
/*  842 */     return this.encoding;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public String getEncoding()
/*      */   {
/*  851 */     return getXmlEncoding();
/*      */   }
/*      */ 
/*      */   public void setXmlVersion(String value)
/*      */   {
/*  860 */     if ((value.equals("1.0")) || (value.equals("1.1")))
/*      */     {
/*  863 */       if (!getXmlVersion().equals(value)) {
/*  864 */         this.xmlVersionChanged = true;
/*      */ 
/*  866 */         isNormalized(false);
/*  867 */         this.version = value;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  874 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/*  875 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */ 
/*  878 */     if (getXmlVersion().equals("1.1")) {
/*  879 */       this.xml11Version = true;
/*      */     }
/*      */     else
/*  882 */       this.xml11Version = false;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setVersion(String value)
/*      */   {
/*  892 */     setXmlVersion(value);
/*      */   }
/*      */ 
/*      */   public String getXmlVersion()
/*      */   {
/*  901 */     return this.version == null ? "1.0" : this.version;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public String getVersion()
/*      */   {
/*  910 */     return getXmlVersion();
/*      */   }
/*      */ 
/*      */   public void setXmlStandalone(boolean value)
/*      */     throws DOMException
/*      */   {
/*  925 */     this.standalone = value;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setStandalone(boolean value)
/*      */   {
/*  934 */     setXmlStandalone(value);
/*      */   }
/*      */ 
/*      */   public boolean getXmlStandalone()
/*      */   {
/*  943 */     return this.standalone;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public boolean getStandalone()
/*      */   {
/*  952 */     return getXmlStandalone();
/*      */   }
/*      */ 
/*      */   public String getDocumentURI()
/*      */   {
/*  964 */     return this.fDocumentURI;
/*      */   }
/*      */ 
/*      */   public Node renameNode(Node n, String namespaceURI, String name)
/*      */     throws DOMException
/*      */   {
/*  975 */     if ((this.errorChecking) && (n.getOwnerDocument() != this) && (n != this)) {
/*  976 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*      */ 
/*  978 */       throw new DOMException((short)4, msg);
/*      */     }
/*  980 */     switch (n.getNodeType()) {
/*      */     case 1:
/*  982 */       ElementImpl el = (ElementImpl)n;
/*  983 */       if ((el instanceof ElementNSImpl)) {
/*  984 */         ((ElementNSImpl)el).rename(namespaceURI, name);
/*      */ 
/*  987 */         callUserDataHandlers(el, null, (short)4);
/*      */       }
/*  990 */       else if (namespaceURI == null) {
/*  991 */         if (this.errorChecking) {
/*  992 */           int colon1 = name.indexOf(':');
/*  993 */           if (colon1 != -1) {
/*  994 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*      */ 
/*  999 */             throw new DOMException((short)14, msg);
/*      */           }
/* 1001 */           if (!isXMLName(name, this.xml11Version)) {
/* 1002 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*      */ 
/* 1005 */             throw new DOMException((short)5, msg);
/*      */           }
/*      */         }
/*      */ 
/* 1009 */         el.rename(name);
/*      */ 
/* 1012 */         callUserDataHandlers(el, null, (short)4);
/*      */       }
/*      */       else
/*      */       {
/* 1017 */         ElementNSImpl nel = new ElementNSImpl(this, namespaceURI, name);
/*      */ 
/* 1021 */         copyEventListeners(el, nel);
/*      */ 
/* 1024 */         Hashtable data = removeUserDataTable(el);
/*      */ 
/* 1027 */         Node parent = el.getParentNode();
/* 1028 */         Node nextSib = el.getNextSibling();
/* 1029 */         if (parent != null) {
/* 1030 */           parent.removeChild(el);
/*      */         }
/*      */ 
/* 1033 */         Node child = el.getFirstChild();
/* 1034 */         while (child != null) {
/* 1035 */           el.removeChild(child);
/* 1036 */           nel.appendChild(child);
/* 1037 */           child = el.getFirstChild();
/*      */         }
/*      */ 
/* 1040 */         nel.moveSpecifiedAttributes(el);
/*      */ 
/* 1043 */         setUserDataTable(nel, data);
/*      */ 
/* 1046 */         callUserDataHandlers(el, nel, (short)4);
/*      */ 
/* 1050 */         if (parent != null) {
/* 1051 */           parent.insertBefore(nel, nextSib);
/*      */         }
/* 1053 */         el = nel;
/*      */       }
/*      */ 
/* 1057 */       renamedElement((Element)n, el);
/* 1058 */       return el;
/*      */     case 2:
/* 1061 */       AttrImpl at = (AttrImpl)n;
/*      */ 
/* 1064 */       Element el = at.getOwnerElement();
/* 1065 */       if (el != null) {
/* 1066 */         el.removeAttributeNode(at);
/*      */       }
/* 1068 */       if ((n instanceof AttrNSImpl)) {
/* 1069 */         ((AttrNSImpl)at).rename(namespaceURI, name);
/*      */ 
/* 1071 */         if (el != null) {
/* 1072 */           el.setAttributeNodeNS(at);
/*      */         }
/*      */ 
/* 1076 */         callUserDataHandlers(at, null, (short)4);
/*      */       }
/* 1079 */       else if (namespaceURI == null) {
/* 1080 */         at.rename(name);
/*      */ 
/* 1082 */         if (el != null) {
/* 1083 */           el.setAttributeNode(at);
/*      */         }
/*      */ 
/* 1087 */         callUserDataHandlers(at, null, (short)4);
/*      */       }
/*      */       else
/*      */       {
/* 1091 */         AttrNSImpl nat = new AttrNSImpl(this, namespaceURI, name);
/*      */ 
/* 1094 */         copyEventListeners(at, nat);
/*      */ 
/* 1097 */         Hashtable data = removeUserDataTable(at);
/*      */ 
/* 1100 */         Node child = at.getFirstChild();
/* 1101 */         while (child != null) {
/* 1102 */           at.removeChild(child);
/* 1103 */           nat.appendChild(child);
/* 1104 */           child = at.getFirstChild();
/*      */         }
/*      */ 
/* 1108 */         setUserDataTable(nat, data);
/*      */ 
/* 1111 */         callUserDataHandlers(at, nat, (short)4);
/*      */ 
/* 1114 */         if (el != null) {
/* 1115 */           el.setAttributeNode(nat);
/*      */         }
/* 1117 */         at = nat;
/*      */       }
/*      */ 
/* 1121 */       renamedAttrNode((Attr)n, at);
/*      */ 
/* 1123 */       return at;
/*      */     }
/*      */ 
/* 1126 */     String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 1127 */     throw new DOMException((short)9, msg);
/*      */   }
/*      */ 
/*      */   public void normalizeDocument()
/*      */   {
/* 1140 */     if ((isNormalized()) && (!isNormalizeDocRequired())) {
/* 1141 */       return;
/*      */     }
/* 1143 */     if (needsSyncChildren()) {
/* 1144 */       synchronizeChildren();
/*      */     }
/*      */ 
/* 1147 */     if (this.domNormalizer == null) {
/* 1148 */       this.domNormalizer = new DOMNormalizer();
/*      */     }
/*      */ 
/* 1151 */     if (this.fConfiguration == null) {
/* 1152 */       this.fConfiguration = new DOMConfigurationImpl();
/*      */     }
/*      */     else {
/* 1155 */       this.fConfiguration.reset();
/*      */     }
/*      */ 
/* 1158 */     this.domNormalizer.normalizeDocument(this, this.fConfiguration);
/* 1159 */     isNormalized(true);
/*      */ 
/* 1162 */     this.xmlVersionChanged = false;
/*      */   }
/*      */ 
/*      */   public DOMConfiguration getDomConfig()
/*      */   {
/* 1174 */     if (this.fConfiguration == null) {
/* 1175 */       this.fConfiguration = new DOMConfigurationImpl();
/*      */     }
/* 1177 */     return this.fConfiguration;
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */   {
/* 1190 */     if ((this.fDocumentURI != null) && (this.fDocumentURI.length() != 0)) {
/*      */       try {
/* 1192 */         return new URI(this.fDocumentURI).toString();
/*      */       }
/*      */       catch (URI.MalformedURIException e)
/*      */       {
/* 1196 */         return null;
/*      */       }
/*      */     }
/* 1199 */     return this.fDocumentURI;
/*      */   }
/*      */ 
/*      */   public void setDocumentURI(String documentURI)
/*      */   {
/* 1206 */     this.fDocumentURI = documentURI;
/*      */   }
/*      */ 
/*      */   public boolean getAsync()
/*      */   {
/* 1228 */     return false;
/*      */   }
/*      */ 
/*      */   public void setAsync(boolean async)
/*      */   {
/* 1246 */     if (async) {
/* 1247 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 1248 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void abort()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean load(String uri)
/*      */   {
/* 1298 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean loadXML(String source)
/*      */   {
/* 1310 */     return false;
/*      */   }
/*      */ 
/*      */   public String saveXML(Node node)
/*      */     throws DOMException
/*      */   {
/* 1333 */     if ((this.errorChecking) && (node != null) && (this != node.getOwnerDocument()))
/*      */     {
/* 1335 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/* 1336 */       throw new DOMException((short)4, msg);
/*      */     }
/* 1338 */     DOMImplementationLS domImplLS = (DOMImplementationLS)DOMImplementationImpl.getDOMImplementation();
/* 1339 */     LSSerializer xmlWriter = domImplLS.createLSSerializer();
/* 1340 */     if (node == null) {
/* 1341 */       node = this;
/*      */     }
/* 1343 */     return xmlWriter.writeToString(node);
/*      */   }
/*      */ 
/*      */   void setMutationEvents(boolean set)
/*      */   {
/*      */   }
/*      */ 
/*      */   boolean getMutationEvents()
/*      */   {
/* 1359 */     return false;
/*      */   }
/*      */ 
/*      */   public DocumentType createDocumentType(String qualifiedName, String publicID, String systemID)
/*      */     throws DOMException
/*      */   {
/* 1382 */     return new DocumentTypeImpl(this, qualifiedName, publicID, systemID);
/*      */   }
/*      */ 
/*      */   public Entity createEntity(String name)
/*      */     throws DOMException
/*      */   {
/* 1402 */     if ((this.errorChecking) && (!isXMLName(name, this.xml11Version))) {
/* 1403 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/* 1404 */       throw new DOMException((short)5, msg);
/*      */     }
/* 1406 */     return new EntityImpl(this, name);
/*      */   }
/*      */ 
/*      */   public Notation createNotation(String name)
/*      */     throws DOMException
/*      */   {
/* 1425 */     if ((this.errorChecking) && (!isXMLName(name, this.xml11Version))) {
/* 1426 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/* 1427 */       throw new DOMException((short)5, msg);
/*      */     }
/* 1429 */     return new NotationImpl(this, name);
/*      */   }
/*      */ 
/*      */   public ElementDefinitionImpl createElementDefinition(String name)
/*      */     throws DOMException
/*      */   {
/* 1440 */     if ((this.errorChecking) && (!isXMLName(name, this.xml11Version))) {
/* 1441 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/* 1442 */       throw new DOMException((short)5, msg);
/*      */     }
/* 1444 */     return new ElementDefinitionImpl(this, name);
/*      */   }
/*      */ 
/*      */   protected int getNodeNumber()
/*      */   {
/* 1454 */     if (this.documentNumber == 0)
/*      */     {
/* 1456 */       CoreDOMImplementationImpl cd = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
/* 1457 */       this.documentNumber = cd.assignDocumentNumber();
/*      */     }
/* 1459 */     return this.documentNumber;
/*      */   }
/*      */ 
/*      */   protected int getNodeNumber(Node node)
/*      */   {
/*      */     int num;
/* 1474 */     if (this.nodeTable == null) {
/* 1475 */       this.nodeTable = new Hashtable();
/* 1476 */       int num = --this.nodeCounter;
/* 1477 */       this.nodeTable.put(node, new Integer(num));
/*      */     }
/*      */     else {
/* 1480 */       Integer n = (Integer)this.nodeTable.get(node);
/* 1481 */       if (n == null) {
/* 1482 */         int num = --this.nodeCounter;
/* 1483 */         this.nodeTable.put(node, new Integer(num));
/*      */       }
/*      */       else {
/* 1486 */         num = n.intValue();
/*      */       }
/*      */     }
/* 1488 */     return num;
/*      */   }
/*      */ 
/*      */   public Node importNode(Node source, boolean deep)
/*      */     throws DOMException
/*      */   {
/* 1502 */     return importNode(source, deep, false, null);
/*      */   }
/*      */ 
/*      */   private Node importNode(Node source, boolean deep, boolean cloningDoc, Hashtable reversedIdentifiers)
/*      */     throws DOMException
/*      */   {
/* 1520 */     Node newnode = null;
/* 1521 */     Hashtable userData = null;
/*      */ 
/* 1534 */     if ((source instanceof NodeImpl))
/* 1535 */       userData = ((NodeImpl)source).getUserDataRecord();
/* 1536 */     int type = source.getNodeType();
/* 1537 */     switch (type)
/*      */     {
/*      */     case 1:
/* 1540 */       boolean domLevel20 = source.getOwnerDocument().getImplementation().hasFeature("XML", "2.0");
/*      */       Element newElement;
/*      */       Element newElement;
/* 1542 */       if ((!domLevel20) || (source.getLocalName() == null))
/* 1543 */         newElement = createElement(source.getNodeName());
/*      */       else {
/* 1545 */         newElement = createElementNS(source.getNamespaceURI(), source.getNodeName());
/*      */       }
/*      */ 
/* 1549 */       NamedNodeMap sourceAttrs = source.getAttributes();
/* 1550 */       if (sourceAttrs != null) {
/* 1551 */         int length = sourceAttrs.getLength();
/* 1552 */         for (int index = 0; index < length; index++) {
/* 1553 */           Attr attr = (Attr)sourceAttrs.item(index);
/*      */ 
/* 1559 */           if ((attr.getSpecified()) || (cloningDoc)) {
/* 1560 */             Attr newAttr = (Attr)importNode(attr, true, cloningDoc, reversedIdentifiers);
/*      */ 
/* 1565 */             if ((!domLevel20) || (attr.getLocalName() == null))
/*      */             {
/* 1567 */               newElement.setAttributeNode(newAttr);
/*      */             }
/* 1569 */             else newElement.setAttributeNodeNS(newAttr);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1575 */       if (reversedIdentifiers != null)
/*      */       {
/* 1577 */         Object elementId = reversedIdentifiers.get(source);
/* 1578 */         if (elementId != null) {
/* 1579 */           if (this.identifiers == null) {
/* 1580 */             this.identifiers = new Hashtable();
/*      */           }
/* 1582 */           this.identifiers.put(elementId, newElement);
/*      */         }
/*      */       }
/*      */ 
/* 1586 */       newnode = newElement;
/* 1587 */       break;
/*      */     case 2:
/* 1592 */       if (source.getOwnerDocument().getImplementation().hasFeature("XML", "2.0")) {
/* 1593 */         if (source.getLocalName() == null)
/* 1594 */           newnode = createAttribute(source.getNodeName());
/*      */         else {
/* 1596 */           newnode = createAttributeNS(source.getNamespaceURI(), source.getNodeName());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1601 */         newnode = createAttribute(source.getNodeName());
/*      */       }
/*      */ 
/* 1605 */       if ((source instanceof AttrImpl)) {
/* 1606 */         AttrImpl attr = (AttrImpl)source;
/* 1607 */         if (attr.hasStringValue()) {
/* 1608 */           AttrImpl newattr = (AttrImpl)newnode;
/* 1609 */           newattr.setValue(attr.getValue());
/* 1610 */           deep = false;
/*      */         }
/*      */         else {
/* 1613 */           deep = true;
/*      */         }
/*      */ 
/*      */       }
/* 1622 */       else if (source.getFirstChild() == null) {
/* 1623 */         newnode.setNodeValue(source.getNodeValue());
/* 1624 */         deep = false;
/*      */       } else {
/* 1626 */         deep = true;
/*      */       }
/*      */ 
/* 1629 */       break;
/*      */     case 3:
/* 1633 */       newnode = createTextNode(source.getNodeValue());
/* 1634 */       break;
/*      */     case 4:
/* 1638 */       newnode = createCDATASection(source.getNodeValue());
/* 1639 */       break;
/*      */     case 5:
/* 1643 */       newnode = createEntityReference(source.getNodeName());
/*      */ 
/* 1646 */       deep = false;
/* 1647 */       break;
/*      */     case 6:
/* 1651 */       Entity srcentity = (Entity)source;
/* 1652 */       EntityImpl newentity = (EntityImpl)createEntity(source.getNodeName());
/*      */ 
/* 1654 */       newentity.setPublicId(srcentity.getPublicId());
/* 1655 */       newentity.setSystemId(srcentity.getSystemId());
/* 1656 */       newentity.setNotationName(srcentity.getNotationName());
/*      */ 
/* 1659 */       newentity.isReadOnly(false);
/* 1660 */       newnode = newentity;
/* 1661 */       break;
/*      */     case 7:
/* 1665 */       newnode = createProcessingInstruction(source.getNodeName(), source.getNodeValue());
/*      */ 
/* 1667 */       break;
/*      */     case 8:
/* 1671 */       newnode = createComment(source.getNodeValue());
/* 1672 */       break;
/*      */     case 10:
/* 1678 */       if (!cloningDoc) {
/* 1679 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 1680 */         throw new DOMException((short)9, msg);
/*      */       }
/* 1682 */       DocumentType srcdoctype = (DocumentType)source;
/* 1683 */       DocumentTypeImpl newdoctype = (DocumentTypeImpl)createDocumentType(srcdoctype.getNodeName(), srcdoctype.getPublicId(), srcdoctype.getSystemId());
/*      */ 
/* 1688 */       NamedNodeMap smap = srcdoctype.getEntities();
/* 1689 */       NamedNodeMap tmap = newdoctype.getEntities();
/* 1690 */       if (smap != null) {
/* 1691 */         for (int i = 0; i < smap.getLength(); i++) {
/* 1692 */           tmap.setNamedItem(importNode(smap.item(i), true, true, reversedIdentifiers));
/*      */         }
/*      */       }
/*      */ 
/* 1696 */       smap = srcdoctype.getNotations();
/* 1697 */       tmap = newdoctype.getNotations();
/* 1698 */       if (smap != null) {
/* 1699 */         for (int i = 0; i < smap.getLength(); i++) {
/* 1700 */           tmap.setNamedItem(importNode(smap.item(i), true, true, reversedIdentifiers));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1710 */       newnode = newdoctype;
/* 1711 */       break;
/*      */     case 11:
/* 1715 */       newnode = createDocumentFragment();
/*      */ 
/* 1717 */       break;
/*      */     case 12:
/* 1721 */       Notation srcnotation = (Notation)source;
/* 1722 */       NotationImpl newnotation = (NotationImpl)createNotation(source.getNodeName());
/*      */ 
/* 1724 */       newnotation.setPublicId(srcnotation.getPublicId());
/* 1725 */       newnotation.setSystemId(srcnotation.getSystemId());
/*      */ 
/* 1727 */       newnode = newnotation;
/*      */ 
/* 1729 */       break;
/*      */     case 9:
/*      */     default:
/* 1733 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 1734 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */ 
/* 1738 */     if (userData != null) {
/* 1739 */       callUserDataHandlers(source, newnode, (short)2, userData);
/*      */     }
/*      */ 
/* 1742 */     if (deep) {
/* 1743 */       for (Node srckid = source.getFirstChild(); 
/* 1744 */         srckid != null; 
/* 1745 */         srckid = srckid.getNextSibling()) {
/* 1746 */         newnode.appendChild(importNode(srckid, true, cloningDoc, reversedIdentifiers));
/*      */       }
/*      */     }
/*      */ 
/* 1750 */     if (newnode.getNodeType() == 6) {
/* 1751 */       ((NodeImpl)newnode).setReadOnly(true, true);
/*      */     }
/* 1753 */     return newnode;
/*      */   }
/*      */ 
/*      */   public Node adoptNode(Node source)
/*      */   {
/* 1766 */     Hashtable userData = null;
/*      */     NodeImpl node;
/*      */     try
/*      */     {
/* 1768 */       node = (NodeImpl)source;
/*      */     }
/*      */     catch (ClassCastException e) {
/* 1771 */       return null;
/*      */     }
/*      */ 
/* 1776 */     if (source == null)
/* 1777 */       return null;
/* 1778 */     if ((source != null) && (source.getOwnerDocument() != null))
/*      */     {
/* 1780 */       DOMImplementation thisImpl = getImplementation();
/* 1781 */       DOMImplementation otherImpl = source.getOwnerDocument().getImplementation();
/*      */ 
/* 1784 */       if (thisImpl != otherImpl)
/*      */       {
/* 1787 */         if (((thisImpl instanceof DOMImplementationImpl)) && ((otherImpl instanceof DeferredDOMImplementationImpl)))
/*      */         {
/* 1790 */           undeferChildren(node);
/* 1791 */         } else if ((!(thisImpl instanceof DeferredDOMImplementationImpl)) || (!(otherImpl instanceof DOMImplementationImpl)))
/*      */         {
/* 1796 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     Node child;
/* 1801 */     switch (node.getNodeType()) {
/*      */     case 2:
/* 1803 */       AttrImpl attr = (AttrImpl)node;
/*      */ 
/* 1805 */       if (attr.getOwnerElement() != null)
/*      */       {
/* 1807 */         attr.getOwnerElement().removeAttributeNode(attr);
/*      */       }
/*      */ 
/* 1810 */       attr.isSpecified(true);
/* 1811 */       userData = node.getUserDataRecord();
/*      */ 
/* 1814 */       attr.setOwnerDocument(this);
/* 1815 */       if (userData != null)
/* 1816 */         setUserDataTable(node, userData); break;
/*      */     case 6:
/*      */     case 12:
/* 1823 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 1824 */       throw new DOMException((short)7, msg);
/*      */     case 9:
/*      */     case 10:
/* 1831 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/* 1832 */       throw new DOMException((short)9, msg);
/*      */     case 5:
/* 1835 */       userData = node.getUserDataRecord();
/*      */ 
/* 1837 */       Node parent = node.getParentNode();
/* 1838 */       if (parent != null) {
/* 1839 */         parent.removeChild(source);
/*      */       }
/*      */ 
/* 1843 */       while ((child = node.getFirstChild()) != null) {
/* 1844 */         node.removeChild(child);
/*      */       }
/*      */ 
/* 1847 */       node.setOwnerDocument(this);
/* 1848 */       if (userData != null) {
/* 1849 */         setUserDataTable(node, userData);
/*      */       }
/* 1851 */       if (this.docType != null)
/*      */       {
/* 1854 */         NamedNodeMap entities = this.docType.getEntities();
/* 1855 */         Node entityNode = entities.getNamedItem(node.getNodeName());
/* 1856 */         if (entityNode != null)
/*      */         {
/* 1859 */           child = entityNode.getFirstChild(); }  } break;
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 11:
/*      */     default:
/* 1860 */       while (child != null) {
/* 1861 */         Node childClone = child.cloneNode(true);
/* 1862 */         node.appendChild(childClone);
/*      */ 
/* 1860 */         child = child.getNextSibling(); continue;
/*      */ 
/* 1867 */         userData = node.getUserDataRecord();
/*      */ 
/* 1869 */         Node parent = node.getParentNode();
/* 1870 */         if (parent != null) {
/* 1871 */           parent.removeChild(source);
/*      */         }
/*      */ 
/* 1874 */         node.setOwnerDocument(this);
/* 1875 */         if (userData != null) {
/* 1876 */           setUserDataTable(node, userData);
/*      */         }
/* 1878 */         ((ElementImpl)node).reconcileDefaultAttributes();
/* 1879 */         break;
/*      */ 
/* 1882 */         userData = node.getUserDataRecord();
/*      */ 
/* 1884 */         Node parent = node.getParentNode();
/* 1885 */         if (parent != null) {
/* 1886 */           parent.removeChild(source);
/*      */         }
/*      */ 
/* 1889 */         node.setOwnerDocument(this);
/* 1890 */         if (userData != null) {
/* 1891 */           setUserDataTable(node, userData);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1897 */     if (userData != null) {
/* 1898 */       callUserDataHandlers(source, null, (short)5, userData);
/*      */     }
/* 1900 */     return node;
/*      */   }
/*      */ 
/*      */   protected void undeferChildren(Node node)
/*      */   {
/* 1910 */     Node top = node;
/*      */ 
/* 1912 */     while (null != node)
/*      */     {
/* 1914 */       if (((NodeImpl)node).needsSyncData()) {
/* 1915 */         ((NodeImpl)node).synchronizeData();
/*      */       }
/*      */ 
/* 1918 */       NamedNodeMap attributes = node.getAttributes();
/* 1919 */       if (attributes != null) {
/* 1920 */         int length = attributes.getLength();
/* 1921 */         for (int i = 0; i < length; i++) {
/* 1922 */           undeferChildren(attributes.item(i));
/*      */         }
/*      */       }
/*      */ 
/* 1926 */       Node nextNode = null;
/* 1927 */       nextNode = node.getFirstChild();
/*      */ 
/* 1929 */       while (null == nextNode)
/*      */       {
/* 1931 */         if (!top.equals(node))
/*      */         {
/* 1934 */           nextNode = node.getNextSibling();
/*      */ 
/* 1936 */           if (null == nextNode) {
/* 1937 */             node = node.getParentNode();
/*      */ 
/* 1939 */             if ((null == node) || (top.equals(node))) {
/* 1940 */               nextNode = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1946 */       node = nextNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Element getElementById(String elementId)
/*      */   {
/* 1964 */     return getIdentifier(elementId);
/*      */   }
/*      */ 
/*      */   protected final void clearIdentifiers()
/*      */   {
/* 1971 */     if (this.identifiers != null)
/* 1972 */       this.identifiers.clear();
/*      */   }
/*      */ 
/*      */   public void putIdentifier(String idName, Element element)
/*      */   {
/* 1987 */     if (element == null) {
/* 1988 */       removeIdentifier(idName);
/* 1989 */       return;
/*      */     }
/*      */ 
/* 1992 */     if (needsSyncData()) {
/* 1993 */       synchronizeData();
/*      */     }
/*      */ 
/* 1996 */     if (this.identifiers == null) {
/* 1997 */       this.identifiers = new Hashtable();
/*      */     }
/*      */ 
/* 2000 */     this.identifiers.put(idName, element);
/*      */   }
/*      */ 
/*      */   public Element getIdentifier(String idName)
/*      */   {
/* 2013 */     if (needsSyncData()) {
/* 2014 */       synchronizeData();
/*      */     }
/*      */ 
/* 2017 */     if (this.identifiers == null) {
/* 2018 */       return null;
/*      */     }
/* 2020 */     Element elem = (Element)this.identifiers.get(idName);
/* 2021 */     if (elem != null)
/*      */     {
/* 2023 */       Node parent = elem.getParentNode();
/* 2024 */       while (parent != null) {
/* 2025 */         if (parent == this) {
/* 2026 */           return elem;
/*      */         }
/* 2028 */         parent = parent.getParentNode();
/*      */       }
/*      */     }
/* 2031 */     return null;
/*      */   }
/*      */ 
/*      */   public void removeIdentifier(String idName)
/*      */   {
/* 2043 */     if (needsSyncData()) {
/* 2044 */       synchronizeData();
/*      */     }
/*      */ 
/* 2047 */     if (this.identifiers == null) {
/* 2048 */       return;
/*      */     }
/*      */ 
/* 2051 */     this.identifiers.remove(idName);
/*      */   }
/*      */ 
/*      */   public Enumeration getIdentifiers()
/*      */   {
/* 2058 */     if (needsSyncData()) {
/* 2059 */       synchronizeData();
/*      */     }
/*      */ 
/* 2062 */     if (this.identifiers == null) {
/* 2063 */       this.identifiers = new Hashtable();
/*      */     }
/*      */ 
/* 2066 */     return this.identifiers.keys();
/*      */   }
/*      */ 
/*      */   public Element createElementNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/* 2099 */     return new ElementNSImpl(this, namespaceURI, qualifiedName);
/*      */   }
/*      */ 
/*      */   public Element createElementNS(String namespaceURI, String qualifiedName, String localpart)
/*      */     throws DOMException
/*      */   {
/* 2119 */     return new ElementNSImpl(this, namespaceURI, qualifiedName, localpart);
/*      */   }
/*      */ 
/*      */   public Attr createAttributeNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/* 2142 */     return new AttrNSImpl(this, namespaceURI, qualifiedName);
/*      */   }
/*      */ 
/*      */   public Attr createAttributeNS(String namespaceURI, String qualifiedName, String localpart)
/*      */     throws DOMException
/*      */   {
/* 2163 */     return new AttrNSImpl(this, namespaceURI, qualifiedName, localpart);
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*      */   {
/* 2184 */     return new DeepNodeListImpl(this, namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/* 2193 */     CoreDocumentImpl newdoc = (CoreDocumentImpl)super.clone();
/* 2194 */     newdoc.docType = null;
/* 2195 */     newdoc.docElement = null;
/* 2196 */     return newdoc;
/*      */   }
/*      */ 
/*      */   public static final boolean isXMLName(String s, boolean xml11Version)
/*      */   {
/* 2211 */     if (s == null) {
/* 2212 */       return false;
/*      */     }
/* 2214 */     if (!xml11Version) {
/* 2215 */       return XMLChar.isValidName(s);
/*      */     }
/* 2217 */     return XML11Char.isXML11ValidName(s);
/*      */   }
/*      */ 
/*      */   public static final boolean isValidQName(String prefix, String local, boolean xml11Version)
/*      */   {
/* 2231 */     if (local == null) return false;
/* 2232 */     boolean validNCName = false;
/*      */ 
/* 2234 */     if (!xml11Version) {
/* 2235 */       validNCName = ((prefix == null) || (XMLChar.isValidNCName(prefix))) && (XMLChar.isValidNCName(local));
/*      */     }
/*      */     else
/*      */     {
/* 2239 */       validNCName = ((prefix == null) || (XML11Char.isXML11ValidNCName(prefix))) && (XML11Char.isXML11ValidNCName(local));
/*      */     }
/*      */ 
/* 2243 */     return validNCName;
/*      */   }
/*      */ 
/*      */   protected boolean isKidOK(Node parent, Node child)
/*      */   {
/* 2254 */     if ((this.allowGrammarAccess) && (parent.getNodeType() == 10))
/*      */     {
/* 2256 */       return child.getNodeType() == 1;
/*      */     }
/* 2258 */     return 0 != (kidOK[parent.getNodeType()] & 1 << child.getNodeType());
/*      */   }
/*      */ 
/*      */   protected void changed()
/*      */   {
/* 2265 */     this.changes += 1;
/*      */   }
/*      */ 
/*      */   protected int changes()
/*      */   {
/* 2272 */     return this.changes;
/*      */   }
/*      */ 
/*      */   NodeListCache getNodeListCache(ParentNode owner)
/*      */   {
/* 2281 */     if (this.fFreeNLCache == null) {
/* 2282 */       return new NodeListCache(owner);
/*      */     }
/* 2284 */     NodeListCache c = this.fFreeNLCache;
/* 2285 */     this.fFreeNLCache = this.fFreeNLCache.next;
/* 2286 */     c.fChild = null;
/* 2287 */     c.fChildIndex = -1;
/* 2288 */     c.fLength = -1;
/*      */ 
/* 2290 */     if (c.fOwner != null) {
/* 2291 */       c.fOwner.fNodeListCache = null;
/*      */     }
/* 2293 */     c.fOwner = owner;
/*      */ 
/* 2295 */     return c;
/*      */   }
/*      */ 
/*      */   void freeNodeListCache(NodeListCache c)
/*      */   {
/* 2303 */     c.next = this.fFreeNLCache;
/* 2304 */     this.fFreeNLCache = c;
/*      */   }
/*      */ 
/*      */   public Object setUserData(Node n, String key, Object data, UserDataHandler handler)
/*      */   {
/* 2327 */     if (data == null) {
/* 2328 */       if (this.userData != null) {
/* 2329 */         Hashtable t = (Hashtable)this.userData.get(n);
/* 2330 */         if (t != null) {
/* 2331 */           Object o = t.remove(key);
/* 2332 */           if (o != null) {
/* 2333 */             ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 2334 */             return r.fData;
/*      */           }
/*      */         }
/*      */       }
/* 2338 */       return null;
/*      */     }
/*      */     Hashtable t;
/* 2342 */     if (this.userData == null) {
/* 2343 */       this.userData = new Hashtable();
/* 2344 */       Hashtable t = new Hashtable();
/* 2345 */       this.userData.put(n, t);
/*      */     }
/*      */     else {
/* 2348 */       t = (Hashtable)this.userData.get(n);
/* 2349 */       if (t == null) {
/* 2350 */         t = new Hashtable();
/* 2351 */         this.userData.put(n, t);
/*      */       }
/*      */     }
/* 2354 */     Object o = t.put(key, new ParentNode.UserDataRecord(this, data, handler));
/* 2355 */     if (o != null) {
/* 2356 */       ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 2357 */       return r.fData;
/*      */     }
/* 2359 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getUserData(Node n, String key)
/*      */   {
/* 2375 */     if (this.userData == null) {
/* 2376 */       return null;
/*      */     }
/* 2378 */     Hashtable t = (Hashtable)this.userData.get(n);
/* 2379 */     if (t == null) {
/* 2380 */       return null;
/*      */     }
/* 2382 */     Object o = t.get(key);
/* 2383 */     if (o != null) {
/* 2384 */       ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 2385 */       return r.fData;
/*      */     }
/* 2387 */     return null;
/*      */   }
/*      */ 
/*      */   protected Hashtable getUserDataRecord(Node n) {
/* 2391 */     if (this.userData == null) {
/* 2392 */       return null;
/*      */     }
/* 2394 */     Hashtable t = (Hashtable)this.userData.get(n);
/* 2395 */     if (t == null) {
/* 2396 */       return null;
/*      */     }
/* 2398 */     return t;
/*      */   }
/*      */ 
/*      */   Hashtable removeUserDataTable(Node n)
/*      */   {
/* 2407 */     if (this.userData == null) {
/* 2408 */       return null;
/*      */     }
/* 2410 */     return (Hashtable)this.userData.get(n);
/*      */   }
/*      */ 
/*      */   void setUserDataTable(Node n, Hashtable data)
/*      */   {
/* 2419 */     if (this.userData == null)
/* 2420 */       this.userData = new Hashtable();
/* 2421 */     if (data != null)
/* 2422 */       this.userData.put(n, data);
/*      */   }
/*      */ 
/*      */   void callUserDataHandlers(Node n, Node c, short operation)
/*      */   {
/* 2433 */     if (this.userData == null) {
/* 2434 */       return;
/*      */     }
/*      */ 
/* 2437 */     if ((n instanceof NodeImpl)) {
/* 2438 */       Hashtable t = ((NodeImpl)n).getUserDataRecord();
/* 2439 */       if ((t == null) || (t.isEmpty())) {
/* 2440 */         return;
/*      */       }
/* 2442 */       callUserDataHandlers(n, c, operation, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   void callUserDataHandlers(Node n, Node c, short operation, Hashtable userData)
/*      */   {
/* 2454 */     if ((userData == null) || (userData.isEmpty())) {
/* 2455 */       return;
/*      */     }
/* 2457 */     Enumeration keys = userData.keys();
/* 2458 */     while (keys.hasMoreElements()) {
/* 2459 */       String key = (String)keys.nextElement();
/* 2460 */       ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)userData.get(key);
/* 2461 */       if (r.fHandler != null)
/* 2462 */         r.fHandler.handle(operation, key, r.fData, n, c);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void checkNamespaceWF(String qname, int colon1, int colon2)
/*      */   {
/* 2506 */     if (!this.errorChecking) {
/* 2507 */       return;
/*      */     }
/*      */ 
/* 2512 */     if ((colon1 == 0) || (colon1 == qname.length() - 1) || (colon2 != colon1)) {
/* 2513 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*      */ 
/* 2518 */       throw new DOMException((short)14, msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void checkDOMNSErr(String prefix, String namespace) {
/* 2523 */     if (this.errorChecking) {
/* 2524 */       if (namespace == null) {
/* 2525 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*      */ 
/* 2530 */         throw new DOMException((short)14, msg);
/*      */       }
/* 2532 */       if ((prefix.equals("xml")) && (!namespace.equals(NamespaceContext.XML_URI)))
/*      */       {
/* 2534 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*      */ 
/* 2539 */         throw new DOMException((short)14, msg);
/*      */       }
/* 2541 */       if (((prefix.equals("xmlns")) && (!namespace.equals(NamespaceContext.XMLNS_URI))) || ((!prefix.equals("xmlns")) && (namespace.equals(NamespaceContext.XMLNS_URI))))
/*      */       {
/* 2546 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*      */ 
/* 2551 */         throw new DOMException((short)14, msg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void checkQName(String prefix, String local)
/*      */   {
/* 2564 */     if (!this.errorChecking) {
/* 2565 */       return;
/*      */     }
/*      */ 
/* 2569 */     boolean validNCName = false;
/* 2570 */     if (!this.xml11Version) {
/* 2571 */       validNCName = ((prefix == null) || (XMLChar.isValidNCName(prefix))) && (XMLChar.isValidNCName(local));
/*      */     }
/*      */     else
/*      */     {
/* 2575 */       validNCName = ((prefix == null) || (XML11Char.isXML11ValidNCName(prefix))) && (XML11Char.isXML11ValidNCName(local));
/*      */     }
/*      */ 
/* 2579 */     if (!validNCName)
/*      */     {
/* 2581 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/*      */ 
/* 2586 */       throw new DOMException((short)5, msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isXML11Version()
/*      */   {
/* 2595 */     return this.xml11Version;
/*      */   }
/*      */ 
/*      */   boolean isNormalizeDocRequired()
/*      */   {
/* 2601 */     return true;
/*      */   }
/*      */ 
/*      */   boolean isXMLVersionChanged()
/*      */   {
/* 2607 */     return this.xmlVersionChanged;
/*      */   }
/*      */ 
/*      */   protected void setUserData(NodeImpl n, Object data)
/*      */   {
/* 2617 */     setUserData(n, "XERCES1DOMUSERDATA", data, null);
/*      */   }
/*      */ 
/*      */   protected Object getUserData(NodeImpl n)
/*      */   {
/* 2625 */     return getUserData(n, "XERCES1DOMUSERDATA");
/*      */   }
/*      */ 
/*      */   protected void addEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void removeEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void copyEventListeners(NodeImpl src, NodeImpl tgt)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected boolean dispatchEvent(NodeImpl node, Event event)
/*      */   {
/* 2649 */     return false;
/*      */   }
/*      */ 
/*      */   void replacedText(NodeImpl node)
/*      */   {
/*      */   }
/*      */ 
/*      */   void deletedText(NodeImpl node, int offset, int count)
/*      */   {
/*      */   }
/*      */ 
/*      */   void insertedText(NodeImpl node, int offset, int count)
/*      */   {
/*      */   }
/*      */ 
/*      */   void modifyingCharacterData(NodeImpl node, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void modifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void insertingNode(NodeImpl node, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void removedNode(NodeImpl node, boolean replace)
/*      */   {
/*      */   }
/*      */ 
/*      */   void replacingNode(NodeImpl node)
/*      */   {
/*      */   }
/*      */ 
/*      */   void replacedNode(NodeImpl node)
/*      */   {
/*      */   }
/*      */ 
/*      */   void replacingData(NodeImpl node)
/*      */   {
/*      */   }
/*      */ 
/*      */   void replacedCharacterData(NodeImpl node, String oldvalue, String value)
/*      */   {
/*      */   }
/*      */ 
/*      */   void modifiedAttrValue(AttrImpl attr, String oldvalue)
/*      */   {
/*      */   }
/*      */ 
/*      */   void setAttrNode(AttrImpl attr, AttrImpl previous)
/*      */   {
/*      */   }
/*      */ 
/*      */   void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name)
/*      */   {
/*      */   }
/*      */ 
/*      */   void renamedAttrNode(Attr oldAt, Attr newAt)
/*      */   {
/*      */   }
/*      */ 
/*      */   void renamedElement(Element oldEl, Element newEl)
/*      */   {
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  220 */     kidOK[9] = 1410;
/*      */     short tmp41_40 = (kidOK[5] = kidOK[1] = 442); kidOK[6] = tmp41_40; kidOK[11] = tmp41_40;
/*      */ 
/*  233 */     kidOK[2] = 40;
/*      */     int tmp88_87 = (kidOK[8] = kidOK[3] = kidOK[4] = kidOK[12] = 0); kidOK[7] = tmp88_87; kidOK[10] = tmp88_87;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
 * JD-Core Version:    0.6.2
 */