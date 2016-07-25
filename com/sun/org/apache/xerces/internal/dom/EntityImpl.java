/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Entity;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class EntityImpl extends ParentNode
/*     */   implements Entity
/*     */ {
/*     */   static final long serialVersionUID = -3575760943444303423L;
/*     */   protected String name;
/*     */   protected String publicId;
/*     */   protected String systemId;
/*     */   protected String encoding;
/*     */   protected String inputEncoding;
/*     */   protected String version;
/*     */   protected String notationName;
/*     */   protected String baseURI;
/*     */ 
/*     */   public EntityImpl(CoreDocumentImpl ownerDoc, String name)
/*     */   {
/* 109 */     super(ownerDoc);
/* 110 */     this.name = name;
/* 111 */     isReadOnly(true);
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/* 123 */     return 6;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/* 130 */     if (needsSyncData()) {
/* 131 */       synchronizeData();
/*     */     }
/* 133 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String x)
/*     */     throws DOMException
/*     */   {
/* 141 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/* 142 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 143 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */     throws DOMException
/*     */   {
/* 154 */     if ((this.ownerDocument.errorChecking) && (isReadOnly()))
/* 155 */       throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep)
/*     */   {
/* 162 */     EntityImpl newentity = (EntityImpl)super.cloneNode(deep);
/* 163 */     newentity.setReadOnly(true, deep);
/* 164 */     return newentity;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 177 */     if (needsSyncData()) {
/* 178 */       synchronizeData();
/*     */     }
/* 180 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 190 */     if (needsSyncData()) {
/* 191 */       synchronizeData();
/*     */     }
/* 193 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public String getXmlVersion()
/*     */   {
/* 203 */     if (needsSyncData()) {
/* 204 */       synchronizeData();
/*     */     }
/* 206 */     return this.version;
/*     */   }
/*     */ 
/*     */   public String getXmlEncoding()
/*     */   {
/* 217 */     if (needsSyncData()) {
/* 218 */       synchronizeData();
/*     */     }
/*     */ 
/* 221 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public String getNotationName()
/*     */   {
/* 237 */     if (needsSyncData()) {
/* 238 */       synchronizeData();
/*     */     }
/* 240 */     return this.notationName;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String id)
/*     */   {
/* 253 */     if (needsSyncData()) {
/* 254 */       synchronizeData();
/*     */     }
/* 256 */     this.publicId = id;
/*     */   }
/*     */ 
/*     */   public void setXmlEncoding(String value)
/*     */   {
/* 268 */     if (needsSyncData()) {
/* 269 */       synchronizeData();
/*     */     }
/* 271 */     this.encoding = value;
/*     */   }
/*     */ 
/*     */   public String getInputEncoding()
/*     */   {
/* 283 */     if (needsSyncData()) {
/* 284 */       synchronizeData();
/*     */     }
/* 286 */     return this.inputEncoding;
/*     */   }
/*     */ 
/*     */   public void setInputEncoding(String inputEncoding)
/*     */   {
/* 293 */     if (needsSyncData()) {
/* 294 */       synchronizeData();
/*     */     }
/* 296 */     this.inputEncoding = inputEncoding;
/*     */   }
/*     */ 
/*     */   public void setXmlVersion(String value)
/*     */   {
/* 306 */     if (needsSyncData()) {
/* 307 */       synchronizeData();
/*     */     }
/* 309 */     this.version = value;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String id)
/*     */   {
/* 318 */     if (needsSyncData()) {
/* 319 */       synchronizeData();
/*     */     }
/* 321 */     this.systemId = id;
/*     */   }
/*     */ 
/*     */   public void setNotationName(String name)
/*     */   {
/* 332 */     if (needsSyncData()) {
/* 333 */       synchronizeData();
/*     */     }
/* 335 */     this.notationName = name;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 351 */     if (needsSyncData()) {
/* 352 */       synchronizeData();
/*     */     }
/* 354 */     return this.baseURI != null ? this.baseURI : ((CoreDocumentImpl)getOwnerDocument()).getBaseURI();
/*     */   }
/*     */ 
/*     */   public void setBaseURI(String uri)
/*     */   {
/* 359 */     if (needsSyncData()) {
/* 360 */       synchronizeData();
/*     */     }
/* 362 */     this.baseURI = uri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.EntityImpl
 * JD-Core Version:    0.6.2
 */