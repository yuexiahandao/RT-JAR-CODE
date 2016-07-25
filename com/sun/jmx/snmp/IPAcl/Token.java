/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ class Token
/*     */ {
/*     */   public int kind;
/*     */   public int beginLine;
/*     */   public int beginColumn;
/*     */   public int endLine;
/*     */   public int endColumn;
/*     */   public String image;
/*     */   public Token next;
/*     */   public Token specialToken;
/*     */ 
/*     */   public final String toString()
/*     */   {
/*  84 */     return this.image;
/*     */   }
/*     */ 
/*     */   public static final Token newToken(int paramInt)
/*     */   {
/* 101 */     switch (paramInt) {
/*     */     }
/* 103 */     return new Token();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.Token
 * JD-Core Version:    0.6.2
 */