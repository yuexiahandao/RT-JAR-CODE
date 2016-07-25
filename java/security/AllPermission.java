/*     */ package java.security;
/*     */ 
/*     */ public final class AllPermission extends Permission
/*     */ {
/*     */   private static final long serialVersionUID = -2916474571451318075L;
/*     */ 
/*     */   public AllPermission()
/*     */   {
/*  68 */     super("<all permissions>");
/*     */   }
/*     */ 
/*     */   public AllPermission(String paramString1, String paramString2)
/*     */   {
/*  82 */     this();
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 105 */     return paramObject instanceof AllPermission;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 115 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 125 */     return "<all actions>";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 138 */     return new AllPermissionCollection();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AllPermission
 * JD-Core Version:    0.6.2
 */