/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ public class XMLNotationDecl
/*     */ {
/*     */   public String name;
/*     */   public String publicId;
/*     */   public String systemId;
/*     */   public String baseSystemId;
/*     */ 
/*     */   public void setValues(String name, String publicId, String systemId, String baseSystemId)
/*     */   {
/*  96 */     this.name = name;
/*  97 */     this.publicId = publicId;
/*  98 */     this.systemId = systemId;
/*  99 */     this.baseSystemId = baseSystemId;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 106 */     this.name = null;
/* 107 */     this.publicId = null;
/* 108 */     this.systemId = null;
/* 109 */     this.baseSystemId = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLNotationDecl
 * JD-Core Version:    0.6.2
 */