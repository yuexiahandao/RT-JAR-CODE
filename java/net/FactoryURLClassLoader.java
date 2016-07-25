/*     */ package java.net;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ 
/*     */ final class FactoryURLClassLoader extends URLClassLoader
/*     */ {
/*     */   FactoryURLClassLoader(URL[] paramArrayOfURL, ClassLoader paramClassLoader, AccessControlContext paramAccessControlContext)
/*     */   {
/* 770 */     super(paramArrayOfURL, paramClassLoader, paramAccessControlContext);
/*     */   }
/*     */ 
/*     */   FactoryURLClassLoader(URL[] paramArrayOfURL, AccessControlContext paramAccessControlContext) {
/* 774 */     super(paramArrayOfURL, paramAccessControlContext);
/*     */   }
/*     */ 
/*     */   public final Class loadClass(String paramString, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/* 782 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 783 */     if (localSecurityManager != null) {
/* 784 */       int i = paramString.lastIndexOf('.');
/* 785 */       if (i != -1) {
/* 786 */         localSecurityManager.checkPackageAccess(paramString.substring(0, i));
/*     */       }
/*     */     }
/* 789 */     return super.loadClass(paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 765 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.FactoryURLClassLoader
 * JD-Core Version:    0.6.2
 */