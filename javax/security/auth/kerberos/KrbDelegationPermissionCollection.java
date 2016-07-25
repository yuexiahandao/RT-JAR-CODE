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
/*     */ final class KrbDelegationPermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient List<Permission> perms;
/*     */   private static final long serialVersionUID = -3383936936589966948L;
/* 349 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Vector.class) };
/*     */ 
/*     */   public KrbDelegationPermissionCollection()
/*     */   {
/* 273 */     this.perms = new ArrayList();
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 288 */     if (!(paramPermission instanceof DelegationPermission)) {
/* 289 */       return false;
/*     */     }
/* 291 */     synchronized (this) {
/* 292 */       for (Permission localPermission : this.perms) {
/* 293 */         if (localPermission.implies(paramPermission))
/* 294 */           return true;
/*     */       }
/*     */     }
/* 297 */     return false;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 315 */     if (!(paramPermission instanceof DelegationPermission)) {
/* 316 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 318 */     if (isReadOnly()) {
/* 319 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/* 321 */     synchronized (this) {
/* 322 */       this.perms.add(0, paramPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 335 */     synchronized (this) {
/* 336 */       return Collections.enumeration(this.perms);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 364 */     Vector localVector = new Vector(this.perms.size());
/*     */ 
/* 366 */     synchronized (this) {
/* 367 */       localVector.addAll(this.perms);
/*     */     }
/*     */ 
/* 370 */     ??? = paramObjectOutputStream.putFields();
/* 371 */     ((ObjectOutputStream.PutField)???).put("permissions", localVector);
/* 372 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 383 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 386 */     Vector localVector = (Vector)localGetField.get("permissions", null);
/*     */ 
/* 388 */     this.perms = new ArrayList(localVector.size());
/* 389 */     this.perms.addAll(localVector);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KrbDelegationPermissionCollection
 * JD-Core Version:    0.6.2
 */