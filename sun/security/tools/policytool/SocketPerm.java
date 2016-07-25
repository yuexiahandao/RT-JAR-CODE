/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class SocketPerm extends Perm
/*      */ {
/*      */   public SocketPerm()
/*      */   {
/* 4082 */     super("SocketPermission", "java.net.SocketPermission", new String[0], new String[] { "accept", "connect", "listen", "resolve" });
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.SocketPerm
 * JD-Core Version:    0.6.2
 */