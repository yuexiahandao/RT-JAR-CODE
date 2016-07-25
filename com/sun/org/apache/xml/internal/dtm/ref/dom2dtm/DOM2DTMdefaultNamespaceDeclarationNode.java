/*     */ package com.sun.org.apache.xml.internal.dtm.ref.dom2dtm;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ 
/*     */ public class DOM2DTMdefaultNamespaceDeclarationNode
/*     */   implements Attr, TypeInfo
/*     */ {
/*  57 */   final String NOT_SUPPORTED_ERR = "Unsupported operation on pseudonode";
/*     */   Element pseudoparent;
/*     */   String prefix;
/*     */   String uri;
/*     */   String nodename;
/*     */   int handle;
/*     */ 
/*     */   DOM2DTMdefaultNamespaceDeclarationNode(Element pseudoparent, String prefix, String uri, int handle)
/*     */   {
/*  64 */     this.pseudoparent = pseudoparent;
/*  65 */     this.prefix = prefix;
/*  66 */     this.uri = uri;
/*  67 */     this.handle = handle;
/*  68 */     this.nodename = ("xmlns:" + prefix);
/*     */   }
/*  70 */   public String getNodeName() { return this.nodename; } 
/*  71 */   public String getName() { return this.nodename; } 
/*  72 */   public String getNamespaceURI() { return "http://www.w3.org/2000/xmlns/"; } 
/*  73 */   public String getPrefix() { return this.prefix; } 
/*  74 */   public String getLocalName() { return this.prefix; } 
/*  75 */   public String getNodeValue() { return this.uri; } 
/*  76 */   public String getValue() { return this.uri; } 
/*  77 */   public Element getOwnerElement() { return this.pseudoparent; } 
/*     */   public boolean isSupported(String feature, String version) {
/*  79 */     return false; } 
/*  80 */   public boolean hasChildNodes() { return false; } 
/*  81 */   public boolean hasAttributes() { return false; } 
/*  82 */   public Node getParentNode() { return null; } 
/*  83 */   public Node getFirstChild() { return null; } 
/*  84 */   public Node getLastChild() { return null; } 
/*  85 */   public Node getPreviousSibling() { return null; } 
/*  86 */   public Node getNextSibling() { return null; } 
/*  87 */   public boolean getSpecified() { return false; } 
/*     */   public void normalize() {  } 
/*  89 */   public NodeList getChildNodes() { return null; } 
/*  90 */   public NamedNodeMap getAttributes() { return null; } 
/*  91 */   public short getNodeType() { return 2; } 
/*  92 */   public void setNodeValue(String value) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  93 */   public void setValue(String value) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  94 */   public void setPrefix(String value) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  95 */   public Node insertBefore(Node a, Node b) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  96 */   public Node replaceChild(Node a, Node b) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  97 */   public Node appendChild(Node a) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  98 */   public Node removeChild(Node a) { throw new DTMException("Unsupported operation on pseudonode"); } 
/*  99 */   public Document getOwnerDocument() { return this.pseudoparent.getOwnerDocument(); } 
/* 100 */   public Node cloneNode(boolean deep) { throw new DTMException("Unsupported operation on pseudonode"); }
/*     */ 
/*     */ 
/*     */   public int getHandleOfNode()
/*     */   {
/* 109 */     return this.handle;
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeNamespace()
/*     */   {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(String ns, String localName, int derivationMethod)
/*     */   {
/* 128 */     return false;
/*     */   }
/*     */   public TypeInfo getSchemaTypeInfo() {
/* 131 */     return this;
/*     */   }
/* 133 */   public boolean isId() { return false; }
/*     */ 
/*     */ 
/*     */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*     */   {
/* 151 */     return getOwnerDocument().setUserData(key, data, handler);
/*     */   }
/*     */ 
/*     */   public Object getUserData(String key)
/*     */   {
/* 164 */     return getOwnerDocument().getUserData(key);
/*     */   }
/*     */ 
/*     */   public Object getFeature(String feature, String version)
/*     */   {
/* 190 */     return isSupported(feature, version) ? this : null;
/*     */   }
/*     */ 
/*     */   public boolean isEqualNode(Node arg)
/*     */   {
/* 236 */     if (arg == this) {
/* 237 */       return true;
/*     */     }
/* 239 */     if (arg.getNodeType() != getNodeType()) {
/* 240 */       return false;
/*     */     }
/*     */ 
/* 244 */     if (getNodeName() == null) {
/* 245 */       if (arg.getNodeName() != null) {
/* 246 */         return false;
/*     */       }
/*     */     }
/* 249 */     else if (!getNodeName().equals(arg.getNodeName())) {
/* 250 */       return false;
/*     */     }
/*     */ 
/* 253 */     if (getLocalName() == null) {
/* 254 */       if (arg.getLocalName() != null) {
/* 255 */         return false;
/*     */       }
/*     */     }
/* 258 */     else if (!getLocalName().equals(arg.getLocalName())) {
/* 259 */       return false;
/*     */     }
/*     */ 
/* 262 */     if (getNamespaceURI() == null) {
/* 263 */       if (arg.getNamespaceURI() != null) {
/* 264 */         return false;
/*     */       }
/*     */     }
/* 267 */     else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
/* 268 */       return false;
/*     */     }
/*     */ 
/* 271 */     if (getPrefix() == null) {
/* 272 */       if (arg.getPrefix() != null) {
/* 273 */         return false;
/*     */       }
/*     */     }
/* 276 */     else if (!getPrefix().equals(arg.getPrefix())) {
/* 277 */       return false;
/*     */     }
/*     */ 
/* 280 */     if (getNodeValue() == null) {
/* 281 */       if (arg.getNodeValue() != null) {
/* 282 */         return false;
/*     */       }
/*     */     }
/* 285 */     else if (!getNodeValue().equals(arg.getNodeValue())) {
/* 286 */       return false;
/*     */     }
/*     */ 
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   public String lookupNamespaceURI(String specifiedPrefix)
/*     */   {
/* 312 */     short type = getNodeType();
/* 313 */     switch (type)
/*     */     {
/*     */     case 1:
/* 316 */       String namespace = getNamespaceURI();
/* 317 */       String prefix = getPrefix();
/* 318 */       if (namespace != null)
/*     */       {
/* 320 */         if ((specifiedPrefix == null) && (prefix == specifiedPrefix))
/*     */         {
/* 322 */           return namespace;
/* 323 */         }if ((prefix != null) && (prefix.equals(specifiedPrefix)))
/*     */         {
/* 325 */           return namespace;
/*     */         }
/*     */       }
/* 328 */       if (hasAttributes()) {
/* 329 */         NamedNodeMap map = getAttributes();
/* 330 */         int length = map.getLength();
/* 331 */         for (int i = 0; i < length; i++) {
/* 332 */           Node attr = map.item(i);
/* 333 */           String attrPrefix = attr.getPrefix();
/* 334 */           String value = attr.getNodeValue();
/* 335 */           namespace = attr.getNamespaceURI();
/* 336 */           if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
/*     */           {
/* 338 */             if ((specifiedPrefix == null) && (attr.getNodeName().equals("xmlns")))
/*     */             {
/* 341 */               return value;
/* 342 */             }if ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (attr.getLocalName().equals(specifiedPrefix)))
/*     */             {
/* 346 */               return value;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 358 */       return null;
/*     */     case 6:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 372 */       return null;
/*     */     case 2:
/* 374 */       if (getOwnerElement().getNodeType() == 1) {
/* 375 */         return getOwnerElement().lookupNamespaceURI(specifiedPrefix);
/*     */       }
/*     */ 
/* 378 */       return null;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     }
/*     */ 
/* 387 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isDefaultNamespace(String namespaceURI)
/*     */   {
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   public String lookupPrefix(String namespaceURI)
/*     */   {
/* 480 */     if (namespaceURI == null) {
/* 481 */       return null;
/*     */     }
/*     */ 
/* 484 */     short type = getNodeType();
/*     */ 
/* 486 */     switch (type)
/*     */     {
/*     */     case 6:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 503 */       return null;
/*     */     case 2:
/* 505 */       if (getOwnerElement().getNodeType() == 1) {
/* 506 */         return getOwnerElement().lookupPrefix(namespaceURI);
/*     */       }
/*     */ 
/* 509 */       return null;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     }
/*     */ 
/* 518 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSameNode(Node other)
/*     */   {
/* 539 */     return this == other;
/*     */   }
/*     */ 
/*     */   public void setTextContent(String textContent)
/*     */     throws DOMException
/*     */   {
/* 589 */     setNodeValue(textContent);
/*     */   }
/*     */ 
/*     */   public String getTextContent()
/*     */     throws DOMException
/*     */   {
/* 638 */     return getNodeValue();
/*     */   }
/*     */ 
/*     */   public short compareDocumentPosition(Node other)
/*     */     throws DOMException
/*     */   {
/* 650 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 678 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode
 * JD-Core Version:    0.6.2
 */