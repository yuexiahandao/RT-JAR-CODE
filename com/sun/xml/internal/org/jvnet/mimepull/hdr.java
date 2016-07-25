/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ class hdr
/*     */   implements Header
/*     */ {
/*     */   String name;
/*     */   String line;
/*     */ 
/*     */   hdr(String l)
/*     */   {
/* 187 */     int i = l.indexOf(':');
/* 188 */     if (i < 0)
/*     */     {
/* 190 */       this.name = l.trim();
/*     */     }
/* 192 */     else this.name = l.substring(0, i).trim();
/*     */ 
/* 194 */     this.line = l;
/*     */   }
/*     */ 
/*     */   hdr(String n, String v)
/*     */   {
/* 201 */     this.name = n;
/* 202 */     this.line = (n + ": " + v);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 209 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 216 */     int i = this.line.indexOf(':');
/* 217 */     if (i < 0) {
/* 218 */       return this.line;
/*     */     }
/*     */ 
/* 221 */     if (this.name.equalsIgnoreCase("Content-Description"))
/*     */     {
/* 224 */       for (int j = i + 1; j < this.line.length(); j++) {
/* 225 */         char c = this.line.charAt(j);
/* 226 */         if ((c != '\t') && (c != '\r') && (c != '\n')) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 231 */     for (int j = i + 1; j < this.line.length(); j++) {
/* 232 */       char c = this.line.charAt(j);
/* 233 */       if ((c != ' ') && (c != '\t') && (c != '\r') && (c != '\n')) {
/*     */         break;
/*     */       }
/*     */     }
/* 237 */     return this.line.substring(j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.hdr
 * JD-Core Version:    0.6.2
 */