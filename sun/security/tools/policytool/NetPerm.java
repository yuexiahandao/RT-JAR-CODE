/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class NetPerm extends Perm
/*      */ {
/*      */   public NetPerm()
/*      */   {
/* 3920 */     super("NetPermission", "java.net.NetPermission", new String[] { "setDefaultAuthenticator", "requestPasswordAuthentication", "specifyStreamHandler", "setProxySelector", "getProxySelector", "setCookieHandler", "getCookieHandler", "setResponseCache", "getResponseCache" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.NetPerm
 * JD-Core Version:    0.6.2
 */