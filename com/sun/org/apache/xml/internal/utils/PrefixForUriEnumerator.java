/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class PrefixForUriEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   private Enumeration allPrefixes;
/*     */   private String uri;
/* 394 */   private String lookahead = null;
/*     */   private NamespaceSupport2 nsup;
/*     */ 
/*     */   PrefixForUriEnumerator(NamespaceSupport2 nsup, String uri, Enumeration allPrefixes)
/*     */   {
/* 401 */     this.nsup = nsup;
/* 402 */     this.uri = uri;
/* 403 */     this.allPrefixes = allPrefixes;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/* 408 */     if (this.lookahead != null) {
/* 409 */       return true;
/*     */     }
/* 411 */     while (this.allPrefixes.hasMoreElements())
/*     */     {
/* 413 */       String prefix = (String)this.allPrefixes.nextElement();
/* 414 */       if (this.uri.equals(this.nsup.getURI(prefix)))
/*     */       {
/* 416 */         this.lookahead = prefix;
/* 417 */         return true;
/*     */       }
/*     */     }
/* 420 */     return false;
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/* 425 */     if (hasMoreElements())
/*     */     {
/* 427 */       String tmp = this.lookahead;
/* 428 */       this.lookahead = null;
/* 429 */       return tmp;
/*     */     }
/*     */ 
/* 432 */     throw new NoSuchElementException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.PrefixForUriEnumerator
 * JD-Core Version:    0.6.2
 */