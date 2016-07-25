/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ 
/*     */ final class BasicPermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 739301742472979399L;
/*     */   private transient Map<String, Permission> perms;
/*     */   private boolean all_allowed;
/*     */   private Class permClass;
/* 499 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Hashtable.class), new ObjectStreamField("all_allowed", Boolean.TYPE), new ObjectStreamField("permClass", Class.class) };
/*     */ 
/*     */   public BasicPermissionCollection(Class paramClass)
/*     */   {
/* 344 */     this.perms = new HashMap(11);
/* 345 */     this.all_allowed = false;
/* 346 */     this.permClass = paramClass;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 367 */     if (!(paramPermission instanceof BasicPermission)) {
/* 368 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 370 */     if (isReadOnly()) {
/* 371 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/* 373 */     BasicPermission localBasicPermission = (BasicPermission)paramPermission;
/*     */ 
/* 378 */     if (this.permClass == null)
/*     */     {
/* 380 */       this.permClass = localBasicPermission.getClass();
/*     */     }
/* 382 */     else if (localBasicPermission.getClass() != this.permClass) {
/* 383 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/*     */ 
/* 387 */     synchronized (this) {
/* 388 */       this.perms.put(localBasicPermission.getCanonicalName(), paramPermission);
/*     */     }
/*     */ 
/* 392 */     if ((!this.all_allowed) && 
/* 393 */       (localBasicPermission.getCanonicalName().equals("*")))
/* 394 */       this.all_allowed = true;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 410 */     if (!(paramPermission instanceof BasicPermission)) {
/* 411 */       return false;
/*     */     }
/* 413 */     BasicPermission localBasicPermission = (BasicPermission)paramPermission;
/*     */ 
/* 416 */     if (localBasicPermission.getClass() != this.permClass) {
/* 417 */       return false;
/*     */     }
/*     */ 
/* 420 */     if (this.all_allowed) {
/* 421 */       return true;
/*     */     }
/*     */ 
/* 427 */     String str = localBasicPermission.getCanonicalName();
/*     */     Permission localPermission;
/* 432 */     synchronized (this) {
/* 433 */       localPermission = (Permission)this.perms.get(str);
/*     */     }
/*     */ 
/* 436 */     if (localPermission != null)
/*     */     {
/* 438 */       return localPermission.implies(paramPermission);
/*     */     }
/*     */ 
/* 444 */     int j = str.length() - 1;
/*     */     int i;
/* 446 */     while ((i = str.lastIndexOf(".", j)) != -1)
/*     */     {
/* 448 */       str = str.substring(0, i + 1) + "*";
/*     */ 
/* 451 */       synchronized (this) {
/* 452 */         localPermission = (Permission)this.perms.get(str);
/*     */       }
/*     */ 
/* 455 */       if (localPermission != null) {
/* 456 */         return localPermission.implies(paramPermission);
/*     */       }
/* 458 */       j = i - 1;
/*     */     }
/*     */ 
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 475 */     synchronized (this) {
/* 476 */       return Collections.enumeration(this.perms.values());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 517 */     Hashtable localHashtable = new Hashtable(this.perms.size() * 2);
/*     */ 
/* 520 */     synchronized (this) {
/* 521 */       localHashtable.putAll(this.perms);
/*     */     }
/*     */ 
/* 525 */     ??? = paramObjectOutputStream.putFields();
/* 526 */     ((ObjectOutputStream.PutField)???).put("all_allowed", this.all_allowed);
/* 527 */     ((ObjectOutputStream.PutField)???).put("permissions", localHashtable);
/* 528 */     ((ObjectOutputStream.PutField)???).put("permClass", this.permClass);
/* 529 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 542 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 545 */     Hashtable localHashtable = (Hashtable)localGetField.get("permissions", null);
/*     */ 
/* 547 */     this.perms = new HashMap(localHashtable.size() * 2);
/* 548 */     this.perms.putAll(localHashtable);
/*     */ 
/* 551 */     this.all_allowed = localGetField.get("all_allowed", false);
/*     */ 
/* 554 */     this.permClass = ((Class)localGetField.get("permClass", null));
/*     */ 
/* 556 */     if (this.permClass == null)
/*     */     {
/* 558 */       Enumeration localEnumeration = localHashtable.elements();
/* 559 */       if (localEnumeration.hasMoreElements()) {
/* 560 */         Permission localPermission = (Permission)localEnumeration.nextElement();
/* 561 */         this.permClass = localPermission.getClass();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.BasicPermissionCollection
 * JD-Core Version:    0.6.2
 */