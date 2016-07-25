/*     */ package com.sun.jndi.rmi.registry;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class NameClassPairEnumeration
/*     */   implements NamingEnumeration
/*     */ {
/*     */   private final String[] names;
/*     */   private int nextName;
/*     */ 
/*     */   NameClassPairEnumeration(String[] paramArrayOfString)
/*     */   {
/* 495 */     this.names = paramArrayOfString;
/* 496 */     this.nextName = 0;
/*     */   }
/*     */ 
/*     */   public boolean hasMore() {
/* 500 */     return this.nextName < this.names.length;
/*     */   }
/*     */ 
/*     */   public Object next() throws NamingException {
/* 504 */     if (!hasMore()) {
/* 505 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/* 509 */     String str = this.names[(this.nextName++)];
/* 510 */     Name localName = new CompositeName().add(str);
/* 511 */     NameClassPair localNameClassPair = new NameClassPair(localName.toString(), "java.lang.Object");
/*     */ 
/* 513 */     localNameClassPair.setNameInNamespace(str);
/* 514 */     return localNameClassPair;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 518 */     return hasMore();
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/*     */     try {
/* 523 */       return next(); } catch (NamingException localNamingException) {
/*     */     }
/* 525 */     throw new NoSuchElementException("javax.naming.NamingException was thrown");
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 531 */     this.nextName = this.names.length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.rmi.registry.NameClassPairEnumeration
 * JD-Core Version:    0.6.2
 */