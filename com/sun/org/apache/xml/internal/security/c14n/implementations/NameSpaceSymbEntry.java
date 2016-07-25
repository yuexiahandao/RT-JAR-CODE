/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ class NameSpaceSymbEntry
/*     */   implements Cloneable
/*     */ {
/* 308 */   int level = 0;
/*     */   String prefix;
/*     */   String uri;
/* 313 */   String lastrendered = null;
/*     */ 
/* 315 */   boolean rendered = false;
/*     */   Attr n;
/*     */ 
/*     */   NameSpaceSymbEntry(String paramString1, Attr paramAttr, boolean paramBoolean, String paramString2)
/*     */   {
/* 294 */     this.uri = paramString1;
/* 295 */     this.rendered = paramBoolean;
/* 296 */     this.n = paramAttr;
/* 297 */     this.prefix = paramString2;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     try {
/* 302 */       return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 304 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.NameSpaceSymbEntry
 * JD-Core Version:    0.6.2
 */