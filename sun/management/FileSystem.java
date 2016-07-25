/*    */ package sun.management;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class FileSystem
/*    */ {
/* 38 */   private static final Object lock = new Object();
/*    */   private static FileSystem fs;
/*    */ 
/*    */   public static FileSystem open()
/*    */   {
/* 47 */     synchronized (lock) {
/* 48 */       if (fs == null) {
/* 49 */         fs = new FileSystemImpl();
/*    */       }
/* 51 */       return fs;
/*    */     }
/*    */   }
/*    */ 
/*    */   public abstract boolean supportsFileSecurity(File paramFile)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract boolean isAccessUserOnly(File paramFile)
/*    */     throws IOException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.FileSystem
 * JD-Core Version:    0.6.2
 */