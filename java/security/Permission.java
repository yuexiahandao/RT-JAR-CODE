/*     */ package java.security;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Permission
/*     */   implements Guard, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5636570222231596674L;
/*     */   private String name;
/*     */ 
/*     */   public Permission(String paramString)
/*     */   {
/*  79 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public void checkGuard(Object paramObject)
/*     */     throws SecurityException
/*     */   {
/* 101 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 102 */     if (localSecurityManager != null) localSecurityManager.checkPermission(this);
/*     */   }
/*     */ 
/*     */   public abstract boolean implies(Permission paramPermission);
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 170 */     return this.name;
/*     */   }
/*     */ 
/*     */   public abstract String getActions();
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 223 */     String str = getActions();
/* 224 */     if ((str == null) || (str.length() == 0)) {
/* 225 */       return "(\"" + getClass().getName() + "\" \"" + this.name + "\")";
/*     */     }
/* 227 */     return "(\"" + getClass().getName() + "\" \"" + this.name + "\" \"" + str + "\")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Permission
 * JD-Core Version:    0.6.2
 */