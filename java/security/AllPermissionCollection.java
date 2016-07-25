/*     */ package java.security;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ final class AllPermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4023755556366636806L;
/*     */   private boolean all_allowed;
/*     */ 
/*     */   public AllPermissionCollection()
/*     */   {
/* 175 */     this.all_allowed = false;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 193 */     if (!(paramPermission instanceof AllPermission)) {
/* 194 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 196 */     if (isReadOnly()) {
/* 197 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/* 199 */     this.all_allowed = true;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 213 */     return this.all_allowed;
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 224 */     return new Enumeration() {
/* 225 */       private boolean hasMore = AllPermissionCollection.this.all_allowed;
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 228 */         return this.hasMore;
/*     */       }
/*     */ 
/*     */       public Permission nextElement() {
/* 232 */         this.hasMore = false;
/* 233 */         return SecurityConstants.ALL_PERMISSION;
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AllPermissionCollection
 * JD-Core Version:    0.6.2
 */