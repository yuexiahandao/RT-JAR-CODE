/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.ProtectionDomain;
/*    */ 
/*    */ final class SecurityUtilities
/*    */ {
/*    */   public static String getSystemProperty(String paramString)
/*    */   {
/* 59 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public String run()
/*    */       {
/* 64 */         return System.getProperty(this.val$name);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public static ProtectionDomain getProtectionDomain(Class<?> paramClass)
/*    */   {
/* 71 */     return (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public ProtectionDomain run()
/*    */       {
/* 76 */         return this.val$clazz.getProtectionDomain();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public static ProtectionDomain getScriptProtectionDomain()
/*    */   {
/* 86 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.SecurityUtilities
 * JD-Core Version:    0.6.2
 */