/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class ObjectContainer extends SignatureElementProxy
/*     */ {
/*     */   public ObjectContainer(Document paramDocument)
/*     */   {
/*  48 */     super(paramDocument);
/*     */   }
/*     */ 
/*     */   public ObjectContainer(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  61 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/*  71 */     if (paramString != null)
/*  72 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/*  82 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public void setMimeType(String paramString)
/*     */   {
/*  92 */     if (paramString != null)
/*  93 */       this._constructionElement.setAttributeNS(null, "MimeType", paramString);
/*     */   }
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 104 */     return this._constructionElement.getAttributeNS(null, "MimeType");
/*     */   }
/*     */ 
/*     */   public void setEncoding(String paramString)
/*     */   {
/* 114 */     if (paramString != null)
/* 115 */       this._constructionElement.setAttributeNS(null, "Encoding", paramString);
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 126 */     return this._constructionElement.getAttributeNS(null, "Encoding");
/*     */   }
/*     */ 
/*     */   public Node appendChild(Node paramNode)
/*     */   {
/* 137 */     Node localNode = null;
/*     */ 
/* 139 */     localNode = this._constructionElement.appendChild(paramNode);
/*     */ 
/* 141 */     return localNode;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 146 */     return "Object";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.ObjectContainer
 * JD-Core Version:    0.6.2
 */