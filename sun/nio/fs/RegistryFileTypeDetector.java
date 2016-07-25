/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public class RegistryFileTypeDetector extends AbstractFileTypeDetector
/*    */ {
/*    */   public String implProbeContentType(Path paramPath)
/*    */     throws IOException
/*    */   {
/* 46 */     if (!(paramPath instanceof Path)) {
/* 47 */       return null;
/*    */     }
/*    */ 
/* 50 */     Path localPath = paramPath.getFileName();
/* 51 */     if (localPath == null)
/* 52 */       return null;
/* 53 */     String str1 = localPath.toString();
/* 54 */     int i = str1.lastIndexOf('.');
/* 55 */     if ((i < 0) || (i == str1.length() - 1)) {
/* 56 */       return null;
/*    */     }
/*    */ 
/* 59 */     String str2 = str1.substring(i);
/* 60 */     NativeBuffer localNativeBuffer1 = WindowsNativeDispatcher.asNativeBuffer(str2);
/* 61 */     NativeBuffer localNativeBuffer2 = WindowsNativeDispatcher.asNativeBuffer("Content Type");
/*    */     try {
/* 63 */       return queryStringValue(localNativeBuffer1.address(), localNativeBuffer2.address());
/*    */     } finally {
/* 65 */       localNativeBuffer2.release();
/* 66 */       localNativeBuffer1.release();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static native String queryStringValue(long paramLong1, long paramLong2);
/*    */ 
/*    */   static {
/* 73 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Void run()
/*    */       {
/* 77 */         System.loadLibrary("net");
/* 78 */         System.loadLibrary("nio");
/* 79 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.RegistryFileTypeDetector
 * JD-Core Version:    0.6.2
 */