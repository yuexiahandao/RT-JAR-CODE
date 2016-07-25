/*    */ package javax.security.auth.kerberos;
/*    */ 
/*    */ import sun.misc.JavaxSecurityAuthKerberosAccess;
/*    */ import sun.security.krb5.EncryptionKey;
/*    */ import sun.security.krb5.PrincipalName;
/*    */ 
/*    */ class JavaxSecurityAuthKerberosAccessImpl
/*    */   implements JavaxSecurityAuthKerberosAccess
/*    */ {
/*    */   public EncryptionKey[] keyTabGetEncryptionKeys(KeyTab paramKeyTab, PrincipalName paramPrincipalName)
/*    */   {
/* 36 */     return paramKeyTab.getEncryptionKeys(paramPrincipalName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.JavaxSecurityAuthKerberosAccessImpl
 * JD-Core Version:    0.6.2
 */