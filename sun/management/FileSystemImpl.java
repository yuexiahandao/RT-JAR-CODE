/*    */ package sun.management;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.LoadLibraryAction;
/*    */ 
/*    */ public class FileSystemImpl extends FileSystem
/*    */ {
/*    */   public boolean supportsFileSecurity(File paramFile)
/*    */     throws IOException
/*    */   {
/* 37 */     return isSecuritySupported0(paramFile.getAbsolutePath());
/*    */   }
/*    */ 
/*    */   public boolean isAccessUserOnly(File paramFile) throws IOException {
/* 41 */     String str = paramFile.getAbsolutePath();
/* 42 */     if (!isSecuritySupported0(str)) {
/* 43 */       throw new UnsupportedOperationException("File system does not support file security");
/*    */     }
/* 45 */     return isAccessUserOnly0(str);
/*    */   }
/*    */ 
/*    */   static native void init0();
/*    */ 
/*    */   static native boolean isSecuritySupported0(String paramString)
/*    */     throws IOException;
/*    */ 
/*    */   static native boolean isAccessUserOnly0(String paramString)
/*    */     throws IOException;
/*    */ 
/*    */   static
/*    */   {
/* 59 */     AccessController.doPrivileged(new LoadLibraryAction("management"));
/*    */ 
/* 61 */     init0();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.FileSystemImpl
 * JD-Core Version:    0.6.2
 */