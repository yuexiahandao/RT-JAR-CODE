/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.URI;
/*     */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Notation;
/*     */ 
/*     */ public class NotationImpl extends NodeImpl
/*     */   implements Notation
/*     */ {
/*     */   static final long serialVersionUID = -764632195890658402L;
/*     */   protected String name;
/*     */   protected String publicId;
/*     */   protected String systemId;
/*     */   protected String baseURI;
/*     */ 
/*     */   public NotationImpl(CoreDocumentImpl ownerDoc, String name)
/*     */   {
/*  83 */     super(ownerDoc);
/*  84 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/*  96 */     return 12;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/* 103 */     if (needsSyncData()) {
/* 104 */       synchronizeData();
/*     */     }
/* 106 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 119 */     if (needsSyncData()) {
/* 120 */       synchronizeData();
/*     */     }
/* 122 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 132 */     if (needsSyncData()) {
/* 133 */       synchronizeData();
/*     */     }
/* 135 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String id)
/*     */   {
/* 149 */     if (isReadOnly()) {
/* 150 */       throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */     }
/*     */ 
/* 154 */     if (needsSyncData()) {
/* 155 */       synchronizeData();
/*     */     }
/* 157 */     this.publicId = id;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String id)
/*     */   {
/* 167 */     if (isReadOnly()) {
/* 168 */       throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */     }
/*     */ 
/* 172 */     if (needsSyncData()) {
/* 173 */       synchronizeData();
/*     */     }
/* 175 */     this.systemId = id;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 189 */     if (needsSyncData()) {
/* 190 */       synchronizeData();
/*     */     }
/* 192 */     if ((this.baseURI != null) && (this.baseURI.length() != 0)) {
/*     */       try {
/* 194 */         return new URI(this.baseURI).toString();
/*     */       }
/*     */       catch (URI.MalformedURIException e)
/*     */       {
/* 198 */         return null;
/*     */       }
/*     */     }
/* 201 */     return this.baseURI;
/*     */   }
/*     */ 
/*     */   public void setBaseURI(String uri)
/*     */   {
/* 206 */     if (needsSyncData()) {
/* 207 */       synchronizeData();
/*     */     }
/* 209 */     this.baseURI = uri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.NotationImpl
 * JD-Core Version:    0.6.2
 */