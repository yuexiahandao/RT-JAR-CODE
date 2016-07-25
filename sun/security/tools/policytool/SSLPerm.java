/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class SSLPerm extends Perm
/*      */ {
/*      */   public SSLPerm()
/*      */   {
/* 4112 */     super("SSLPermission", "javax.net.ssl.SSLPermission", new String[] { "setHostnameVerifier", "getSSLSessionContext" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.SSLPerm
 * JD-Core Version:    0.6.2
 */