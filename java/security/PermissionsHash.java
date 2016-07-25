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
/*     */ final class PermissionsHash extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<Permission, Permission> permsMap;
/*     */   private static final long serialVersionUID = -8491988220802933440L;
/* 554 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("perms", Hashtable.class) };
/*     */ 
/*     */   PermissionsHash()
/*     */   {
/* 489 */     this.permsMap = new HashMap(11);
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 499 */     synchronized (this) {
/* 500 */       this.permsMap.put(paramPermission, paramPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 517 */     synchronized (this) {
/* 518 */       Permission localPermission1 = (Permission)this.permsMap.get(paramPermission);
/*     */ 
/* 521 */       if (localPermission1 == null) {
/* 522 */         for (Permission localPermission2 : this.permsMap.values()) {
/* 523 */           if (localPermission2.implies(paramPermission))
/* 524 */             return true;
/*     */         }
/* 526 */         return false;
/*     */       }
/* 528 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 541 */     synchronized (this) {
/* 542 */       return Collections.enumeration(this.permsMap.values());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 569 */     Hashtable localHashtable = new Hashtable(this.permsMap.size() * 2);
/*     */ 
/* 571 */     synchronized (this) {
/* 572 */       localHashtable.putAll(this.permsMap);
/*     */     }
/*     */ 
/* 576 */     ??? = paramObjectOutputStream.putFields();
/* 577 */     ((ObjectOutputStream.PutField)???).put("perms", localHashtable);
/* 578 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 590 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 593 */     Hashtable localHashtable = (Hashtable)localGetField.get("perms", null);
/*     */ 
/* 595 */     this.permsMap = new HashMap(localHashtable.size() * 2);
/* 596 */     this.permsMap.putAll(localHashtable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.PermissionsHash
 * JD-Core Version:    0.6.2
 */