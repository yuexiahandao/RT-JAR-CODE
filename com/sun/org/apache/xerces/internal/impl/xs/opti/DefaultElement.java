/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ 
/*     */ public class DefaultElement extends NodeImpl
/*     */   implements Element
/*     */ {
/*     */   public DefaultElement()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DefaultElement(String prefix, String localpart, String rawname, String uri, short nodeType)
/*     */   {
/*  46 */     super(prefix, localpart, rawname, uri, nodeType);
/*     */   }
/*     */ 
/*     */   public String getTagName()
/*     */   {
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String name)
/*     */   {
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   public Attr getAttributeNode(String name)
/*     */   {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagName(String name)
/*     */   {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public String getAttributeNS(String namespaceURI, String localName)
/*     */   {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public Attr getAttributeNodeNS(String namespaceURI, String localName)
/*     */   {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAttribute(String name)
/*     */   {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasAttributeNS(String namespaceURI, String localName)
/*     */   {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   public TypeInfo getSchemaTypeInfo() {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, String value)
/*     */     throws DOMException
/*     */   {
/* 106 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void removeAttribute(String name) throws DOMException
/*     */   {
/* 111 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Attr removeAttributeNode(Attr oldAttr) throws DOMException
/*     */   {
/* 116 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Attr setAttributeNode(Attr newAttr) throws DOMException
/*     */   {
/* 121 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException
/*     */   {
/* 126 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void removeAttributeNS(String namespaceURI, String localName) throws DOMException
/*     */   {
/* 131 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Attr setAttributeNodeNS(Attr newAttr) throws DOMException
/*     */   {
/* 136 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void setIdAttributeNode(Attr at, boolean makeId) throws DOMException {
/* 140 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */   public void setIdAttribute(String name, boolean makeId) throws DOMException {
/* 143 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) throws DOMException
/*     */   {
/* 148 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement
 * JD-Core Version:    0.6.2
 */