/*     */ package java.util;
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
/*     */ 
/*     */ final class PropertyPermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map perms;
/*     */   private boolean all_allowed;
/*     */   private static final long serialVersionUID = 7015263904581634791L;
/* 619 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Hashtable.class), new ObjectStreamField("all_allowed", Boolean.TYPE) };
/*     */ 
/*     */   public PropertyPermissionCollection()
/*     */   {
/* 461 */     this.perms = new HashMap(32);
/* 462 */     this.all_allowed = false;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 480 */     if (!(paramPermission instanceof PropertyPermission)) {
/* 481 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 483 */     if (isReadOnly()) {
/* 484 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/*     */ 
/* 487 */     PropertyPermission localPropertyPermission1 = (PropertyPermission)paramPermission;
/* 488 */     String str1 = localPropertyPermission1.getName();
/*     */ 
/* 490 */     synchronized (this) {
/* 491 */       PropertyPermission localPropertyPermission2 = (PropertyPermission)this.perms.get(str1);
/*     */ 
/* 493 */       if (localPropertyPermission2 != null) {
/* 494 */         int i = localPropertyPermission2.getMask();
/* 495 */         int j = localPropertyPermission1.getMask();
/* 496 */         if (i != j) {
/* 497 */           int k = i | j;
/* 498 */           String str2 = PropertyPermission.getActions(k);
/* 499 */           this.perms.put(str1, new PropertyPermission(str1, str2));
/*     */         }
/*     */       } else {
/* 502 */         this.perms.put(str1, paramPermission);
/*     */       }
/*     */     }
/*     */ 
/* 506 */     if ((!this.all_allowed) && 
/* 507 */       (str1.equals("*")))
/* 508 */       this.all_allowed = true;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 524 */     if (!(paramPermission instanceof PropertyPermission)) {
/* 525 */       return false;
/*     */     }
/* 527 */     PropertyPermission localPropertyPermission1 = (PropertyPermission)paramPermission;
/*     */ 
/* 530 */     int i = localPropertyPermission1.getMask();
/* 531 */     int j = 0;
/*     */     PropertyPermission localPropertyPermission2;
/* 534 */     if (this.all_allowed) {
/* 535 */       synchronized (this) {
/* 536 */         localPropertyPermission2 = (PropertyPermission)this.perms.get("*");
/*     */       }
/* 538 */       if (localPropertyPermission2 != null) {
/* 539 */         j |= localPropertyPermission2.getMask();
/* 540 */         if ((j & i) == i) {
/* 541 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 549 */     ??? = localPropertyPermission1.getName();
/*     */ 
/* 552 */     synchronized (this) {
/* 553 */       localPropertyPermission2 = (PropertyPermission)this.perms.get(???);
/*     */     }
/*     */ 
/* 556 */     if (localPropertyPermission2 != null)
/*     */     {
/* 558 */       j |= localPropertyPermission2.getMask();
/* 559 */       if ((j & i) == i) {
/* 560 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 566 */     int m = ((String)???).length() - 1;
/*     */     int k;
/* 568 */     while ((k = ((String)???).lastIndexOf(".", m)) != -1)
/*     */     {
/* 570 */       ??? = ((String)???).substring(0, k + 1) + "*";
/*     */ 
/* 572 */       synchronized (this) {
/* 573 */         localPropertyPermission2 = (PropertyPermission)this.perms.get(???);
/*     */       }
/*     */ 
/* 576 */       if (localPropertyPermission2 != null) {
/* 577 */         j |= localPropertyPermission2.getMask();
/* 578 */         if ((j & i) == i)
/* 579 */           return true;
/*     */       }
/* 581 */       m = k - 1;
/*     */     }
/*     */ 
/* 586 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration elements()
/*     */   {
/* 598 */     synchronized (this) {
/* 599 */       return Collections.enumeration(this.perms.values());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 636 */     Hashtable localHashtable = new Hashtable(this.perms.size() * 2);
/* 637 */     synchronized (this) {
/* 638 */       localHashtable.putAll(this.perms);
/*     */     }
/*     */ 
/* 642 */     ??? = paramObjectOutputStream.putFields();
/* 643 */     ((ObjectOutputStream.PutField)???).put("all_allowed", this.all_allowed);
/* 644 */     ((ObjectOutputStream.PutField)???).put("permissions", localHashtable);
/* 645 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 657 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 660 */     this.all_allowed = localGetField.get("all_allowed", false);
/*     */ 
/* 663 */     Hashtable localHashtable = (Hashtable)localGetField.get("permissions", null);
/* 664 */     this.perms = new HashMap(localHashtable.size() * 2);
/* 665 */     this.perms.putAll(localHashtable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.PropertyPermissionCollection
 * JD-Core Version:    0.6.2
 */