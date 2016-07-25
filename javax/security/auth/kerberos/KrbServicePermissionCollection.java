/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class KrbServicePermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient List<Permission> perms;
/*     */   private static final long serialVersionUID = -4118834211490102011L;
/* 557 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Vector.class) };
/*     */ 
/*     */   public KrbServicePermissionCollection()
/*     */   {
/* 464 */     this.perms = new ArrayList();
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 478 */     if (!(paramPermission instanceof ServicePermission)) {
/* 479 */       return false;
/*     */     }
/* 481 */     ServicePermission localServicePermission1 = (ServicePermission)paramPermission;
/* 482 */     int i = localServicePermission1.getMask();
/* 483 */     int j = 0;
/* 484 */     int k = i;
/*     */ 
/* 486 */     synchronized (this) {
/* 487 */       int m = this.perms.size();
/*     */ 
/* 493 */       for (int n = 0; n < m; n++) {
/* 494 */         ServicePermission localServicePermission2 = (ServicePermission)this.perms.get(n);
/*     */ 
/* 497 */         if (((k & localServicePermission2.getMask()) != 0) && (localServicePermission2.impliesIgnoreMask(localServicePermission1))) {
/* 498 */           j |= localServicePermission2.getMask();
/* 499 */           if ((j & i) == i)
/* 500 */             return true;
/* 501 */           k = i ^ j;
/*     */         }
/*     */       }
/*     */     }
/* 505 */     return false;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 522 */     if (!(paramPermission instanceof ServicePermission)) {
/* 523 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 525 */     if (isReadOnly()) {
/* 526 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/* 528 */     synchronized (this) {
/* 529 */       this.perms.add(0, paramPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 542 */     synchronized (this) {
/* 543 */       return Collections.enumeration(this.perms);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 572 */     Vector localVector = new Vector(this.perms.size());
/*     */ 
/* 574 */     synchronized (this) {
/* 575 */       localVector.addAll(this.perms);
/*     */     }
/*     */ 
/* 578 */     ??? = paramObjectOutputStream.putFields();
/* 579 */     ((ObjectOutputStream.PutField)???).put("permissions", localVector);
/* 580 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 591 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 594 */     Vector localVector = (Vector)localGetField.get("permissions", null);
/*     */ 
/* 596 */     this.perms = new ArrayList(localVector.size());
/* 597 */     this.perms.addAll(localVector);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KrbServicePermissionCollection
 * JD-Core Version:    0.6.2
 */