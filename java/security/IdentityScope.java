/*     */ package java.security;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class IdentityScope extends Identity
/*     */ {
/*     */   private static final long serialVersionUID = -2337346281189773310L;
/*     */   private static IdentityScope scope;
/*     */ 
/*     */   private static void initializeSystemScope()
/*     */   {
/*  76 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/*  79 */         return Security.getProperty("system.scope");
/*     */       }
/*     */     });
/*  83 */     if (str == null) {
/*  84 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  89 */       Class.forName(str);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*  93 */       localClassNotFoundException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected IdentityScope()
/*     */   {
/* 103 */     this("restoring...");
/*     */   }
/*     */ 
/*     */   public IdentityScope(String paramString)
/*     */   {
/* 112 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public IdentityScope(String paramString, IdentityScope paramIdentityScope)
/*     */     throws KeyManagementException
/*     */   {
/* 126 */     super(paramString, paramIdentityScope);
/*     */   }
/*     */ 
/*     */   public static IdentityScope getSystemScope()
/*     */   {
/* 138 */     if (scope == null) {
/* 139 */       initializeSystemScope();
/*     */     }
/* 141 */     return scope;
/*     */   }
/*     */ 
/*     */   protected static void setSystemScope(IdentityScope paramIdentityScope)
/*     */   {
/* 163 */     check("setSystemScope");
/* 164 */     scope = paramIdentityScope;
/*     */   }
/*     */ 
/*     */   public abstract int size();
/*     */ 
/*     */   public abstract Identity getIdentity(String paramString);
/*     */ 
/*     */   public Identity getIdentity(Principal paramPrincipal)
/*     */   {
/* 196 */     return getIdentity(paramPrincipal.getName());
/*     */   }
/*     */ 
/*     */   public abstract Identity getIdentity(PublicKey paramPublicKey);
/*     */ 
/*     */   public abstract void addIdentity(Identity paramIdentity)
/*     */     throws KeyManagementException;
/*     */ 
/*     */   public abstract void removeIdentity(Identity paramIdentity)
/*     */     throws KeyManagementException;
/*     */ 
/*     */   public abstract Enumeration<Identity> identities();
/*     */ 
/*     */   public String toString()
/*     */   {
/* 247 */     return super.toString() + "[" + size() + "]";
/*     */   }
/*     */ 
/*     */   private static void check(String paramString) {
/* 251 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 252 */     if (localSecurityManager != null)
/* 253 */       localSecurityManager.checkSecurityAccess(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.IdentityScope
 * JD-Core Version:    0.6.2
 */