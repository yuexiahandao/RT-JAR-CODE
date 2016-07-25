/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class FilePerm extends Perm
/*      */ {
/*      */   public FilePerm()
/*      */   {
/* 3813 */     super("FilePermission", "java.io.FilePermission", new String[] { "<<ALL FILES>>" }, new String[] { "read", "write", "delete", "execute" });
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.FilePerm
 * JD-Core Version:    0.6.2
 */