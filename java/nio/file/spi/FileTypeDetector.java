/*    */ package java.nio.file.spi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ public abstract class FileTypeDetector
/*    */ {
/*    */   private static Void checkPermission()
/*    */   {
/* 53 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 54 */     if (localSecurityManager != null)
/* 55 */       localSecurityManager.checkPermission(new RuntimePermission("fileTypeDetector"));
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   private FileTypeDetector(Void paramVoid)
/*    */   {
/*    */   }
/*    */ 
/*    */   protected FileTypeDetector()
/*    */   {
/* 68 */     this(checkPermission());
/*    */   }
/*    */ 
/*    */   public abstract String probeContentType(Path paramPath)
/*    */     throws IOException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.spi.FileTypeDetector
 * JD-Core Version:    0.6.2
 */