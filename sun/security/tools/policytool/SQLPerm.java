/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class SQLPerm extends Perm
/*      */ {
/*      */   public SQLPerm()
/*      */   {
/* 4098 */     super("SQLPermission", "java.sql.SQLPermission", new String[] { "setLog", "callAbort", "setSyncFactory", "setNetworkTimeout" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.SQLPerm
 * JD-Core Version:    0.6.2
 */