/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.nio.file.spi.FileSystemProvider;
/*    */ 
/*    */ public class DefaultFileSystemProvider
/*    */ {
/*    */   public static FileSystemProvider create()
/*    */   {
/* 36 */     return new WindowsFileSystemProvider();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.DefaultFileSystemProvider
 * JD-Core Version:    0.6.2
 */