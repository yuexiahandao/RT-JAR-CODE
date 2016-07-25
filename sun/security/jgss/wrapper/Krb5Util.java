/*    */ package sun.security.jgss.wrapper;
/*    */ 
/*    */ import javax.security.auth.kerberos.ServicePermission;
/*    */ import org.ietf.jgss.GSSException;
/*    */ 
/*    */ class Krb5Util
/*    */ {
/*    */   static String getTGSName(GSSNameElement paramGSSNameElement)
/*    */     throws GSSException
/*    */   {
/* 41 */     String str1 = paramGSSNameElement.getKrbName();
/* 42 */     int i = str1.indexOf("@");
/* 43 */     String str2 = str1.substring(i + 1);
/* 44 */     StringBuffer localStringBuffer = new StringBuffer("krbtgt/");
/* 45 */     localStringBuffer.append(str2).append('@').append(str2);
/* 46 */     return localStringBuffer.toString();
/*    */   }
/*    */ 
/*    */   static void checkServicePermission(String paramString1, String paramString2)
/*    */   {
/* 52 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 53 */     if (localSecurityManager != null) {
/* 54 */       SunNativeProvider.debug("Checking ServicePermission(" + paramString1 + ", " + paramString2 + ")");
/*    */ 
/* 56 */       ServicePermission localServicePermission = new ServicePermission(paramString1, paramString2);
/*    */ 
/* 58 */       localSecurityManager.checkPermission(localServicePermission);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.wrapper.Krb5Util
 * JD-Core Version:    0.6.2
 */