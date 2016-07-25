/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class ServicePerm extends Perm
/*      */ {
/*      */   public ServicePerm()
/*      */   {
/* 4068 */     super("ServicePermission", "javax.security.auth.kerberos.ServicePermission", new String[0], new String[] { "initiate", "accept" });
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ServicePerm
 * JD-Core Version:    0.6.2
 */