/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class SerialPerm extends Perm
/*      */ {
/*      */   public SerialPerm()
/*      */   {
/* 4056 */     super("SerializablePermission", "java.io.SerializablePermission", new String[] { "enableSubclassImplementation", "enableSubstitution" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.SerialPerm
 * JD-Core Version:    0.6.2
 */