/*     */ package sun.security.acl;
/*     */ 
/*     */ import java.security.acl.Acl;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ final class AclEnumerator
/*     */   implements Enumeration<AclEntry>
/*     */ {
/*     */   Acl acl;
/*     */   Enumeration<AclEntry> u1;
/*     */   Enumeration<AclEntry> u2;
/*     */   Enumeration<AclEntry> g1;
/*     */   Enumeration<AclEntry> g2;
/*     */ 
/*     */   AclEnumerator(Acl paramAcl, Hashtable<?, AclEntry> paramHashtable1, Hashtable<?, AclEntry> paramHashtable2, Hashtable<?, AclEntry> paramHashtable3, Hashtable<?, AclEntry> paramHashtable4)
/*     */   {
/* 379 */     this.acl = paramAcl;
/* 380 */     this.u1 = paramHashtable1.elements();
/* 381 */     this.u2 = paramHashtable3.elements();
/* 382 */     this.g1 = paramHashtable2.elements();
/* 383 */     this.g2 = paramHashtable4.elements();
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 387 */     return (this.u1.hasMoreElements()) || (this.u2.hasMoreElements()) || (this.g1.hasMoreElements()) || (this.g2.hasMoreElements());
/*     */   }
/*     */ 
/*     */   public AclEntry nextElement()
/*     */   {
/* 396 */     synchronized (this.acl) {
/* 397 */       if (this.u1.hasMoreElements())
/* 398 */         return (AclEntry)this.u1.nextElement();
/* 399 */       if (this.u2.hasMoreElements())
/* 400 */         return (AclEntry)this.u2.nextElement();
/* 401 */       if (this.g1.hasMoreElements())
/* 402 */         return (AclEntry)this.g1.nextElement();
/* 403 */       if (this.g2.hasMoreElements())
/* 404 */         return (AclEntry)this.g2.nextElement();
/*     */     }
/* 406 */     throw new NoSuchElementException("Acl Enumerator");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.AclEnumerator
 * JD-Core Version:    0.6.2
 */