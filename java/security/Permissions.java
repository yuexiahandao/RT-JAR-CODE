/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class Permissions extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<Class<?>, PermissionCollection> permsMap;
/*  93 */   private transient boolean hasUnresolved = false;
/*     */   PermissionCollection allPermission;
/*     */   private static final long serialVersionUID = 4858622370623524688L;
/* 347 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("perms", Hashtable.class), new ObjectStreamField("allPermission", PermissionCollection.class) };
/*     */ 
/*     */   public Permissions()
/*     */   {
/* 103 */     this.permsMap = new HashMap(11);
/* 104 */     this.allPermission = null;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 126 */     if (isReadOnly())
/* 127 */       throw new SecurityException("attempt to add a Permission to a readonly Permissions object");
/*     */     PermissionCollection localPermissionCollection;
/* 132 */     synchronized (this) {
/* 133 */       localPermissionCollection = getPermissionCollection(paramPermission, true);
/* 134 */       localPermissionCollection.add(paramPermission);
/*     */     }
/*     */ 
/* 138 */     if ((paramPermission instanceof AllPermission)) {
/* 139 */       this.allPermission = localPermissionCollection;
/*     */     }
/* 141 */     if ((paramPermission instanceof UnresolvedPermission))
/* 142 */       this.hasUnresolved = true;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 176 */     if (this.allPermission != null) {
/* 177 */       return true;
/*     */     }
/* 179 */     synchronized (this) {
/* 180 */       PermissionCollection localPermissionCollection = getPermissionCollection(paramPermission, false);
/*     */ 
/* 182 */       if (localPermissionCollection != null) {
/* 183 */         return localPermissionCollection.implies(paramPermission);
/*     */       }
/*     */ 
/* 186 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 203 */     synchronized (this) {
/* 204 */       return new PermissionsEnumerator(this.permsMap.values().iterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   private PermissionCollection getPermissionCollection(Permission paramPermission, boolean paramBoolean)
/*     */   {
/* 241 */     Class localClass = paramPermission.getClass();
/*     */ 
/* 243 */     Object localObject = (PermissionCollection)this.permsMap.get(localClass);
/*     */ 
/* 245 */     if ((!this.hasUnresolved) && (!paramBoolean))
/* 246 */       return localObject;
/* 247 */     if (localObject == null)
/*     */     {
/* 250 */       localObject = this.hasUnresolved ? getUnresolvedPermissions(paramPermission) : null;
/*     */ 
/* 253 */       if ((localObject == null) && (paramBoolean))
/*     */       {
/* 255 */         localObject = paramPermission.newPermissionCollection();
/*     */ 
/* 259 */         if (localObject == null) {
/* 260 */           localObject = new PermissionsHash();
/*     */         }
/*     */       }
/* 263 */       if (localObject != null) {
/* 264 */         this.permsMap.put(localClass, localObject);
/*     */       }
/*     */     }
/* 267 */     return localObject;
/*     */   }
/*     */ 
/*     */   private PermissionCollection getUnresolvedPermissions(Permission paramPermission)
/*     */   {
/* 283 */     UnresolvedPermissionCollection localUnresolvedPermissionCollection = (UnresolvedPermissionCollection)this.permsMap.get(UnresolvedPermission.class);
/*     */ 
/* 287 */     if (localUnresolvedPermissionCollection == null) {
/* 288 */       return null;
/*     */     }
/* 290 */     List localList = localUnresolvedPermissionCollection.getUnresolvedPermissions(paramPermission);
/*     */ 
/* 294 */     if (localList == null) {
/* 295 */       return null;
/*     */     }
/* 297 */     Certificate[] arrayOfCertificate = null;
/*     */ 
/* 299 */     Object[] arrayOfObject = paramPermission.getClass().getSigners();
/*     */ 
/* 301 */     int i = 0;
/* 302 */     if (arrayOfObject != null) {
/* 303 */       for (int j = 0; j < arrayOfObject.length; j++) {
/* 304 */         if ((arrayOfObject[j] instanceof Certificate)) {
/* 305 */           i++;
/*     */         }
/*     */       }
/* 308 */       arrayOfCertificate = new Certificate[i];
/* 309 */       i = 0;
/* 310 */       for (j = 0; j < arrayOfObject.length; j++) {
/* 311 */         if ((arrayOfObject[j] instanceof Certificate)) {
/* 312 */           arrayOfCertificate[(i++)] = ((Certificate)arrayOfObject[j]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 317 */     Object localObject1 = null;
/* 318 */     synchronized (localList) {
/* 319 */       int k = localList.size();
/* 320 */       for (int m = 0; m < k; m++) {
/* 321 */         UnresolvedPermission localUnresolvedPermission = (UnresolvedPermission)localList.get(m);
/* 322 */         Permission localPermission = localUnresolvedPermission.resolve(paramPermission, arrayOfCertificate);
/* 323 */         if (localPermission != null) {
/* 324 */           if (localObject1 == null) {
/* 325 */             localObject1 = paramPermission.newPermissionCollection();
/* 326 */             if (localObject1 == null)
/* 327 */               localObject1 = new PermissionsHash();
/*     */           }
/* 329 */           ((PermissionCollection)localObject1).add(localPermission);
/*     */         }
/*     */       }
/*     */     }
/* 333 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 364 */     Hashtable localHashtable = new Hashtable(this.permsMap.size() * 2);
/*     */ 
/* 366 */     synchronized (this) {
/* 367 */       localHashtable.putAll(this.permsMap);
/*     */     }
/*     */ 
/* 371 */     ??? = paramObjectOutputStream.putFields();
/*     */ 
/* 373 */     ((ObjectOutputStream.PutField)???).put("allPermission", this.allPermission);
/* 374 */     ((ObjectOutputStream.PutField)???).put("perms", localHashtable);
/* 375 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 387 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 390 */     this.allPermission = ((PermissionCollection)localGetField.get("allPermission", null));
/*     */ 
/* 393 */     Hashtable localHashtable = (Hashtable)localGetField.get("perms", null);
/*     */ 
/* 395 */     this.permsMap = new HashMap(localHashtable.size() * 2);
/* 396 */     this.permsMap.putAll(localHashtable);
/*     */ 
/* 399 */     UnresolvedPermissionCollection localUnresolvedPermissionCollection = (UnresolvedPermissionCollection)this.permsMap.get(UnresolvedPermission.class);
/*     */ 
/* 401 */     this.hasUnresolved = ((localUnresolvedPermissionCollection != null) && (localUnresolvedPermissionCollection.elements().hasMoreElements()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Permissions
 * JD-Core Version:    0.6.2
 */