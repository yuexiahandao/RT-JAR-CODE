/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ 
/*     */ public class DefaultNode
/*     */   implements Node
/*     */ {
/*     */   public String getNodeName()
/*     */   {
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNodeValue() throws DOMException
/*     */   {
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/*  60 */     return -1;
/*     */   }
/*     */ 
/*     */   public Node getParentNode()
/*     */   {
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList getChildNodes()
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   public Node getFirstChild()
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public Node getLastChild()
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   public Node getPreviousSibling()
/*     */   {
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getAttributes()
/*     */   {
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public Document getOwnerDocument()
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasChildNodes()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep)
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public void normalize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isSupported(String feature, String version)
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBaseURI() {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAttributes()
/*     */   {
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String nodeValue) throws DOMException
/*     */   {
/* 149 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Node insertBefore(Node newChild, Node refChild) throws DOMException
/*     */   {
/* 154 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Node replaceChild(Node newChild, Node oldChild) throws DOMException
/*     */   {
/* 159 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Node removeChild(Node oldChild) throws DOMException
/*     */   {
/* 164 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Node appendChild(Node newChild) throws DOMException
/*     */   {
/* 169 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix) throws DOMException
/*     */   {
/* 174 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public short compareDocumentPosition(Node other) {
/* 178 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String getTextContent() throws DOMException {
/* 182 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */   public void setTextContent(String textContent) throws DOMException {
/* 185 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */   public boolean isSameNode(Node other) {
/* 188 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String lookupPrefix(String namespaceURI) {
/* 192 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */   public boolean isDefaultNamespace(String namespaceURI) {
/* 195 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String lookupNamespaceURI(String prefix) {
/* 199 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public boolean isEqualNode(Node arg) {
/* 203 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Object getFeature(String feature, String version)
/*     */   {
/* 208 */     return null;
/*     */   }
/*     */   public Object setUserData(String key, Object data, UserDataHandler handler) {
/* 211 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */   public Object getUserData(String key) {
/* 214 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode
 * JD-Core Version:    0.6.2
 */