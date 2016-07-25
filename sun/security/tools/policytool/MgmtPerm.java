/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class MgmtPerm extends Perm
/*      */ {
/*      */   public MgmtPerm()
/*      */   {
/* 3854 */     super("ManagementPermission", "java.lang.management.ManagementPermission", new String[] { "control", "monitor" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.MgmtPerm
 * JD-Core Version:    0.6.2
 */