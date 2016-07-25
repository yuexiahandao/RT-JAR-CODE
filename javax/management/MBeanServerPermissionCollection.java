/*     */ package javax.management;
/*     */ 
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Set;
/*     */ 
/*     */ class MBeanServerPermissionCollection extends PermissionCollection
/*     */ {
/*     */   private MBeanServerPermission collectionPermission;
/*     */   private static final long serialVersionUID = -5661980843569388590L;
/*     */ 
/*     */   public synchronized void add(Permission paramPermission)
/*     */   {
/* 342 */     if (!(paramPermission instanceof MBeanServerPermission)) {
/* 343 */       localObject = "Permission not an MBeanServerPermission: " + paramPermission;
/*     */ 
/* 345 */       throw new IllegalArgumentException((String)localObject);
/*     */     }
/* 347 */     if (isReadOnly())
/* 348 */       throw new SecurityException("Read-only permission collection");
/* 349 */     Object localObject = (MBeanServerPermission)paramPermission;
/* 350 */     if (this.collectionPermission == null) {
/* 351 */       this.collectionPermission = ((MBeanServerPermission)localObject);
/* 352 */     } else if (!this.collectionPermission.implies(paramPermission)) {
/* 353 */       int i = this.collectionPermission.mask | ((MBeanServerPermission)localObject).mask;
/* 354 */       this.collectionPermission = new MBeanServerPermission(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean implies(Permission paramPermission) {
/* 359 */     return (this.collectionPermission != null) && (this.collectionPermission.implies(paramPermission));
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<Permission> elements()
/*     */   {
/*     */     Set localSet;
/* 365 */     if (this.collectionPermission == null)
/* 366 */       localSet = Collections.emptySet();
/*     */     else
/* 368 */       localSet = Collections.singleton(this.collectionPermission);
/* 369 */     return Collections.enumeration(localSet);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerPermissionCollection
 * JD-Core Version:    0.6.2
 */