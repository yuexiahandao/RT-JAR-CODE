/*     */ package java.security;
/*     */ 
/*     */ public abstract class PolicySpi
/*     */ {
/*     */   protected abstract boolean engineImplies(ProtectionDomain paramProtectionDomain, Permission paramPermission);
/*     */ 
/*     */   protected void engineRefresh()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected PermissionCollection engineGetPermissions(CodeSource paramCodeSource)
/*     */   {
/*  92 */     return Policy.UNSUPPORTED_EMPTY_COLLECTION;
/*     */   }
/*     */ 
/*     */   protected PermissionCollection engineGetPermissions(ProtectionDomain paramProtectionDomain)
/*     */   {
/* 116 */     return Policy.UNSUPPORTED_EMPTY_COLLECTION;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.PolicySpi
 * JD-Core Version:    0.6.2
 */