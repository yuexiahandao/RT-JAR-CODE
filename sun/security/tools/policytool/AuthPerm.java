/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class AuthPerm extends Perm
/*      */ {
/*      */   public AuthPerm()
/*      */   {
/* 3753 */     super("AuthPermission", "javax.security.auth.AuthPermission", new String[] { "doAs", "doAsPrivileged", "getSubject", "getSubjectFromDomainCombiner", "setReadOnly", "modifyPrincipals", "modifyPublicCredentials", "modifyPrivateCredentials", "refreshCredential", "destroyCredential", "createLoginContext.<" + PolicyTool.rb.getString("name") + ">", "getLoginConfiguration", "setLoginConfiguration", "createLoginConfiguration.<" + PolicyTool.rb.getString("configuration.type") + ">", "refreshLoginConfiguration" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.AuthPerm
 * JD-Core Version:    0.6.2
 */