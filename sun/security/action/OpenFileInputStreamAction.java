/*    */ package sun.security.action;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ 
/*    */ public class OpenFileInputStreamAction
/*    */   implements PrivilegedExceptionAction<FileInputStream>
/*    */ {
/*    */   private final File file;
/*    */ 
/*    */   public OpenFileInputStreamAction(File paramFile)
/*    */   {
/* 43 */     this.file = paramFile;
/*    */   }
/*    */ 
/*    */   public OpenFileInputStreamAction(String paramString) {
/* 47 */     this.file = new File(paramString);
/*    */   }
/*    */ 
/*    */   public FileInputStream run() throws Exception {
/* 51 */     return new FileInputStream(this.file);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.action.OpenFileInputStreamAction
 * JD-Core Version:    0.6.2
 */