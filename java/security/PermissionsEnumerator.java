/*     */ package java.security;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ final class PermissionsEnumerator
/*     */   implements Enumeration<Permission>
/*     */ {
/*     */   private Iterator<PermissionCollection> perms;
/*     */   private Enumeration<Permission> permset;
/*     */ 
/*     */   PermissionsEnumerator(Iterator<PermissionCollection> paramIterator)
/*     */   {
/* 413 */     this.perms = paramIterator;
/* 414 */     this.permset = getNextEnumWithMore();
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/* 422 */     if (this.permset == null) {
/* 423 */       return false;
/*     */     }
/*     */ 
/* 427 */     if (this.permset.hasMoreElements()) {
/* 428 */       return true;
/*     */     }
/*     */ 
/* 431 */     this.permset = getNextEnumWithMore();
/*     */ 
/* 434 */     return this.permset != null;
/*     */   }
/*     */ 
/*     */   public Permission nextElement()
/*     */   {
/* 443 */     if (hasMoreElements()) {
/* 444 */       return (Permission)this.permset.nextElement();
/*     */     }
/* 446 */     throw new NoSuchElementException("PermissionsEnumerator");
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> getNextEnumWithMore()
/*     */   {
/* 452 */     while (this.perms.hasNext()) {
/* 453 */       PermissionCollection localPermissionCollection = (PermissionCollection)this.perms.next();
/* 454 */       Enumeration localEnumeration = localPermissionCollection.elements();
/* 455 */       if (localEnumeration.hasMoreElements())
/* 456 */         return localEnumeration;
/*     */     }
/* 458 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.PermissionsEnumerator
 * JD-Core Version:    0.6.2
 */