/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class PrivCredPerm extends Perm
/*      */ {
/*      */   public PrivCredPerm()
/*      */   {
/* 3939 */     super("PrivateCredentialPermission", "javax.security.auth.PrivateCredentialPermission", new String[0], new String[] { "read" });
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PrivCredPerm
 * JD-Core Version:    0.6.2
 */