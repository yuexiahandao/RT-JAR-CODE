/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class MBeanTrustPerm extends Perm
/*      */ {
/*      */   public MBeanTrustPerm()
/*      */   {
/* 3909 */     super("MBeanTrustPermission", "javax.management.MBeanTrustPermission", new String[] { "register" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.MBeanTrustPerm
 * JD-Core Version:    0.6.2
 */