/*     */ package com.sun.jndi.rmi.registry;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class BindingEnumeration
/*     */   implements NamingEnumeration
/*     */ {
/*     */   private RegistryContext ctx;
/*     */   private final String[] names;
/*     */   private int nextName;
/*     */ 
/*     */   BindingEnumeration(RegistryContext paramRegistryContext, String[] paramArrayOfString)
/*     */   {
/* 551 */     this.ctx = new RegistryContext(paramRegistryContext);
/* 552 */     this.names = paramArrayOfString;
/* 553 */     this.nextName = 0;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 557 */     this.ctx.close();
/*     */   }
/*     */ 
/*     */   public boolean hasMore() {
/* 561 */     if (this.nextName >= this.names.length) {
/* 562 */       this.ctx.close();
/*     */     }
/* 564 */     return this.nextName < this.names.length;
/*     */   }
/*     */ 
/*     */   public Object next() throws NamingException {
/* 568 */     if (!hasMore()) {
/* 569 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/* 573 */     String str1 = this.names[(this.nextName++)];
/* 574 */     Name localName = new CompositeName().add(str1);
/*     */ 
/* 576 */     Object localObject = this.ctx.lookup(localName);
/* 577 */     String str2 = localName.toString();
/* 578 */     Binding localBinding = new Binding(str2, localObject);
/* 579 */     localBinding.setNameInNamespace(str2);
/* 580 */     return localBinding;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 584 */     return hasMore();
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/*     */     try {
/* 589 */       return next(); } catch (NamingException localNamingException) {
/*     */     }
/* 591 */     throw new NoSuchElementException("javax.naming.NamingException was thrown");
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 597 */     finalize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.rmi.registry.BindingEnumeration
 * JD-Core Version:    0.6.2
 */