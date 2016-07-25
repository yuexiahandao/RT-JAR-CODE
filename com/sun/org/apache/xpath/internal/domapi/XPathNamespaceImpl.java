/*     */ package com.sun.org.apache.xpath.internal.domapi;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ import org.w3c.dom.xpath.XPathNamespace;
/*     */ 
/*     */ class XPathNamespaceImpl
/*     */   implements XPathNamespace
/*     */ {
/*     */   private final Node m_attributeNode;
/*     */   private String textContent;
/*     */ 
/*     */   XPathNamespaceImpl(Node node)
/*     */   {
/*  85 */     this.m_attributeNode = node;
/*     */   }
/*     */ 
/*     */   public Element getOwnerElement()
/*     */   {
/*  92 */     return ((Attr)this.m_attributeNode).getOwnerElement();
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/*  99 */     return "#namespace";
/*     */   }
/*     */ 
/*     */   public String getNodeValue()
/*     */     throws DOMException
/*     */   {
/* 106 */     return this.m_attributeNode.getNodeValue();
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String arg0)
/*     */     throws DOMException
/*     */   {
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/* 119 */     return 13;
/*     */   }
/*     */ 
/*     */   public Node getParentNode()
/*     */   {
/* 126 */     return this.m_attributeNode.getParentNode();
/*     */   }
/*     */ 
/*     */   public NodeList getChildNodes()
/*     */   {
/* 133 */     return this.m_attributeNode.getChildNodes();
/*     */   }
/*     */ 
/*     */   public Node getFirstChild()
/*     */   {
/* 140 */     return this.m_attributeNode.getFirstChild();
/*     */   }
/*     */ 
/*     */   public Node getLastChild()
/*     */   {
/* 147 */     return this.m_attributeNode.getLastChild();
/*     */   }
/*     */ 
/*     */   public Node getPreviousSibling()
/*     */   {
/* 154 */     return this.m_attributeNode.getPreviousSibling();
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/* 161 */     return this.m_attributeNode.getNextSibling();
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getAttributes()
/*     */   {
/* 168 */     return this.m_attributeNode.getAttributes();
/*     */   }
/*     */ 
/*     */   public Document getOwnerDocument()
/*     */   {
/* 175 */     return this.m_attributeNode.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public Node insertBefore(Node arg0, Node arg1)
/*     */     throws DOMException
/*     */   {
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   public Node replaceChild(Node arg0, Node arg1)
/*     */     throws DOMException
/*     */   {
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */   public Node removeChild(Node arg0)
/*     */     throws DOMException
/*     */   {
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   public Node appendChild(Node arg0)
/*     */     throws DOMException
/*     */   {
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasChildNodes()
/*     */   {
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean arg0)
/*     */   {
/* 217 */     throw new DOMException((short)9, null);
/*     */   }
/*     */ 
/*     */   public void normalize()
/*     */   {
/* 224 */     this.m_attributeNode.normalize();
/*     */   }
/*     */ 
/*     */   public boolean isSupported(String arg0, String arg1)
/*     */   {
/* 231 */     return this.m_attributeNode.isSupported(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 241 */     return this.m_attributeNode.getNodeValue();
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 248 */     return this.m_attributeNode.getPrefix();
/*     */   }
/*     */ 
/*     */   public void setPrefix(String arg0)
/*     */     throws DOMException
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 263 */     return this.m_attributeNode.getPrefix();
/*     */   }
/*     */ 
/*     */   public boolean hasAttributes()
/*     */   {
/* 270 */     return this.m_attributeNode.hasAttributes();
/*     */   }
/*     */ 
/*     */   public String getBaseURI() {
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */   public short compareDocumentPosition(Node other) throws DOMException {
/* 278 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getTextContent()
/*     */     throws DOMException
/*     */   {
/* 284 */     return this.textContent;
/*     */   }
/*     */ 
/*     */   public void setTextContent(String textContent) throws DOMException {
/* 288 */     this.textContent = textContent;
/*     */   }
/*     */ 
/*     */   public boolean isSameNode(Node other) {
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   public String lookupPrefix(String namespaceURI) {
/* 296 */     return "";
/*     */   }
/*     */ 
/*     */   public boolean isDefaultNamespace(String namespaceURI) {
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   public String lookupNamespaceURI(String prefix) {
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isEqualNode(Node arg) {
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getFeature(String feature, String version) {
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*     */   {
/* 318 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getUserData(String key) {
/* 322 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.domapi.XPathNamespaceImpl
 * JD-Core Version:    0.6.2
 */