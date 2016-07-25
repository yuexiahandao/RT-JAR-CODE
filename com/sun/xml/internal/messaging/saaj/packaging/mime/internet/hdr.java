/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;
/*     */ 
/*     */ class hdr
/*     */   implements Header
/*     */ {
/*     */   String name;
/*     */   String line;
/*     */ 
/*     */   hdr(String l)
/*     */   {
/* 350 */     int i = l.indexOf(':');
/* 351 */     if (i < 0)
/*     */     {
/* 353 */       this.name = l.trim();
/*     */     }
/* 355 */     else this.name = l.substring(0, i).trim();
/*     */ 
/* 357 */     this.line = l;
/*     */   }
/*     */ 
/*     */   hdr(String n, String v)
/*     */   {
/* 364 */     this.name = n;
/* 365 */     this.line = (n + ": " + v);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 372 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 379 */     int i = this.line.indexOf(':');
/* 380 */     if (i < 0) {
/* 381 */       return this.line;
/*     */     }
/*     */ 
/* 384 */     if (this.name.equalsIgnoreCase("Content-Description"))
/*     */     {
/* 387 */       for (int j = i + 1; j < this.line.length(); j++) {
/* 388 */         char c = this.line.charAt(j);
/* 389 */         if ((c != '\t') && (c != '\r') && (c != '\n')) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 394 */     for (int j = i + 1; j < this.line.length(); j++) {
/* 395 */       char c = this.line.charAt(j);
/* 396 */       if ((c != ' ') && (c != '\t') && (c != '\r') && (c != '\n')) {
/*     */         break;
/*     */       }
/*     */     }
/* 400 */     return this.line.substring(j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.hdr
 * JD-Core Version:    0.6.2
 */