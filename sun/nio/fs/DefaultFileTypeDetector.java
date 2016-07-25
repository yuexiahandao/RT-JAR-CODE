/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.nio.file.spi.FileTypeDetector;
/*    */ 
/*    */ public class DefaultFileTypeDetector
/*    */ {
/*    */   public static FileTypeDetector create()
/*    */   {
/* 34 */     return new RegistryFileTypeDetector();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.DefaultFileTypeDetector
 * JD-Core Version:    0.6.2
 */