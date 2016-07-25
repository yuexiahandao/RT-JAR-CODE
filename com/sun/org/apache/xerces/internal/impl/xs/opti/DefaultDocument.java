/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.DOMConfiguration;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class DefaultDocument extends NodeImpl
/*     */   implements Document
/*     */ {
/*  50 */   private String fDocumentURI = null;
/*     */ 
/*     */   public DocumentType getDoctype()
/*     */   {
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   public DOMImplementation getImplementation()
/*     */   {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   public Element getDocumentElement()
/*     */   {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagName(String tagname)
/*     */   {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*     */   {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public Element getElementById(String elementId)
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public Node importNode(Node importedNode, boolean deep) throws DOMException
/*     */   {
/*  91 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Element createElement(String tagName) throws DOMException
/*     */   {
/*  96 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public DocumentFragment createDocumentFragment()
/*     */   {
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public Text createTextNode(String data)
/*     */   {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public Comment createComment(String data) {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public CDATASection createCDATASection(String data) throws DOMException
/*     */   {
/* 115 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException
/*     */   {
/* 120 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Attr createAttribute(String name) throws DOMException
/*     */   {
/* 125 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public EntityReference createEntityReference(String name) throws DOMException
/*     */   {
/* 130 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 135 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 140 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String getInputEncoding()
/*     */   {
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public String getXmlEncoding()
/*     */   {
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getXmlStandalone()
/*     */   {
/* 176 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void setXmlStandalone(boolean standalone)
/*     */   {
/* 185 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String getXmlVersion()
/*     */   {
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */   public void setXmlVersion(String version)
/*     */     throws DOMException
/*     */   {
/* 210 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public boolean getStrictErrorChecking()
/*     */   {
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public void setStrictErrorChecking(boolean strictErrorChecking)
/*     */   {
/* 235 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String getDocumentURI()
/*     */   {
/* 246 */     return this.fDocumentURI;
/*     */   }
/*     */ 
/*     */   public void setDocumentURI(String documentURI)
/*     */   {
/* 257 */     this.fDocumentURI = documentURI;
/*     */   }
/*     */ 
/*     */   public Node adoptNode(Node source) throws DOMException
/*     */   {
/* 262 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void normalizeDocument()
/*     */   {
/* 267 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public DOMConfiguration getDomConfig()
/*     */   {
/* 276 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Node renameNode(Node n, String namespaceURI, String name) throws DOMException
/*     */   {
/* 281 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultDocument
 * JD-Core Version:    0.6.2
 */