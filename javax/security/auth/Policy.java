/*     */ package javax.security.auth;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Security;
/*     */ import java.util.Objects;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class Policy
/*     */ {
/*     */   private static Policy policy;
/* 168 */   private final AccessControlContext acc = AccessController.getContext();
/*     */   private static boolean isCustomPolicy;
/*     */ 
/*     */   public static Policy getPolicy()
/*     */   {
/* 199 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 200 */     if (localSecurityManager != null) localSecurityManager.checkPermission(new AuthPermission("getPolicy"));
/* 201 */     return getPolicyNoCheck();
/*     */   }
/*     */ 
/*     */   static Policy getPolicyNoCheck()
/*     */   {
/* 211 */     if (policy == null)
/*     */     {
/* 213 */       synchronized (Policy.class)
/*     */       {
/* 215 */         if (policy == null) {
/* 216 */           String str1 = null;
/* 217 */           str1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public String run() {
/* 220 */               return Security.getProperty("auth.policy.provider");
/*     */             }
/*     */           });
/* 224 */           if (str1 == null) {
/* 225 */             str1 = "com.sun.security.auth.PolicyFile";
/*     */           }
/*     */           try
/*     */           {
/* 229 */             final String str2 = str1;
/* 230 */             Policy localPolicy = (Policy)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */             {
/*     */               public Policy run()
/*     */                 throws ClassNotFoundException, InstantiationException, IllegalAccessException
/*     */               {
/* 235 */                 Class localClass = Class.forName(this.val$finalClass, false, Thread.currentThread().getContextClassLoader()).asSubclass(Policy.class);
/*     */ 
/* 239 */                 return (Policy)localClass.newInstance();
/*     */               }
/*     */             });
/* 242 */             AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */             {
/*     */               public Void run() {
/* 245 */                 Policy.setPolicy(this.val$untrustedImpl);
/* 246 */                 Policy.access$002(!str2.equals("com.sun.security.auth.PolicyFile"));
/*     */ 
/* 248 */                 return null;
/*     */               }
/*     */             }
/*     */             , (AccessControlContext)Objects.requireNonNull(localPolicy.acc));
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/* 253 */             throw new SecurityException(ResourcesMgr.getString("unable.to.instantiate.Subject.based.policy"));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 260 */     return policy;
/*     */   }
/*     */ 
/*     */   public static void setPolicy(Policy paramPolicy)
/*     */   {
/* 280 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 281 */     if (localSecurityManager != null) localSecurityManager.checkPermission(new AuthPermission("setPolicy"));
/* 282 */     policy = paramPolicy;
/*     */ 
/* 284 */     isCustomPolicy = paramPolicy != null;
/*     */   }
/*     */ 
/*     */   static boolean isCustomPolicySet(Debug paramDebug)
/*     */   {
/* 298 */     if (policy != null) {
/* 299 */       if ((paramDebug != null) && (isCustomPolicy)) {
/* 300 */         paramDebug.println("Providing backwards compatibility for javax.security.auth.policy implementation: " + policy.toString());
/*     */       }
/*     */ 
/* 304 */       return isCustomPolicy;
/*     */     }
/*     */ 
/* 307 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 310 */         return Security.getProperty("auth.policy.provider");
/*     */       }
/*     */     });
/* 313 */     if ((str != null) && (!str.equals("com.sun.security.auth.PolicyFile")))
/*     */     {
/* 315 */       if (paramDebug != null) {
/* 316 */         paramDebug.println("Providing backwards compatibility for javax.security.auth.policy implementation: " + str);
/*     */       }
/*     */ 
/* 320 */       return true;
/*     */     }
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract PermissionCollection getPermissions(Subject paramSubject, CodeSource paramCodeSource);
/*     */ 
/*     */   public abstract void refresh();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.Policy
 * JD-Core Version:    0.6.2
 */