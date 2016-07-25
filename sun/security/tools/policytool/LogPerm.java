/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class LogPerm extends Perm
/*      */ {
/*      */   public LogPerm()
/*      */   {
/* 3843 */     super("LoggingPermission", "java.util.logging.LoggingPermission", new String[] { "control" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.LogPerm
 * JD-Core Version:    0.6.2
 */