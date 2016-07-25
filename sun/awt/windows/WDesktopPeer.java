/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Desktop.Action;
/*    */ import java.awt.peer.DesktopPeer;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ 
/*    */ public class WDesktopPeer
/*    */   implements DesktopPeer
/*    */ {
/* 44 */   private static String ACTION_OPEN_VERB = "open";
/* 45 */   private static String ACTION_EDIT_VERB = "edit";
/* 46 */   private static String ACTION_PRINT_VERB = "print";
/*    */ 
/*    */   public boolean isSupported(Desktop.Action paramAction)
/*    */   {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   public void open(File paramFile) throws IOException {
/* 54 */     ShellExecute(paramFile, ACTION_OPEN_VERB);
/*    */   }
/*    */ 
/*    */   public void edit(File paramFile) throws IOException {
/* 58 */     ShellExecute(paramFile, ACTION_EDIT_VERB);
/*    */   }
/*    */ 
/*    */   public void print(File paramFile) throws IOException {
/* 62 */     ShellExecute(paramFile, ACTION_PRINT_VERB);
/*    */   }
/*    */ 
/*    */   public void mail(URI paramURI) throws IOException {
/* 66 */     ShellExecute(paramURI, ACTION_OPEN_VERB);
/*    */   }
/*    */ 
/*    */   public void browse(URI paramURI) throws IOException {
/* 70 */     ShellExecute(paramURI, ACTION_OPEN_VERB);
/*    */   }
/*    */ 
/*    */   private void ShellExecute(File paramFile, String paramString) throws IOException {
/* 74 */     String str = ShellExecute(paramFile.getAbsolutePath(), paramString);
/* 75 */     if (str != null)
/* 76 */       throw new IOException("Failed to " + paramString + " " + paramFile + ". Error message: " + str);
/*    */   }
/*    */ 
/*    */   private void ShellExecute(URI paramURI, String paramString) throws IOException
/*    */   {
/* 81 */     String str = ShellExecute(paramURI.toString(), paramString);
/*    */ 
/* 83 */     if (str != null)
/* 84 */       throw new IOException("Failed to " + paramString + " " + paramURI + ". Error message: " + str);
/*    */   }
/*    */ 
/*    */   private static native String ShellExecute(String paramString1, String paramString2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDesktopPeer
 * JD-Core Version:    0.6.2
 */