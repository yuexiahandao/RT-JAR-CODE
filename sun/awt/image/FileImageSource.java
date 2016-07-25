/*    */ package sun.awt.image;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ 
/*    */ public class FileImageSource extends InputStreamImageSource
/*    */ {
/*    */   String imagefile;
/*    */ 
/*    */   public FileImageSource(String paramString)
/*    */   {
/* 37 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 38 */     if (localSecurityManager != null) {
/* 39 */       localSecurityManager.checkRead(paramString);
/*    */     }
/* 41 */     this.imagefile = paramString;
/*    */   }
/*    */ 
/*    */   final boolean checkSecurity(Object paramObject, boolean paramBoolean)
/*    */   {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   protected ImageDecoder getDecoder() {
/* 51 */     if (this.imagefile == null) {
/* 52 */       return null;
/*    */     }
/*    */     BufferedInputStream localBufferedInputStream;
/*    */     try
/*    */     {
/* 57 */       localBufferedInputStream = new BufferedInputStream(new FileInputStream(this.imagefile));
/*    */     } catch (FileNotFoundException localFileNotFoundException) {
/* 59 */       return null;
/*    */     }
/*    */ 
/* 77 */     return getDecoder(localBufferedInputStream);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.FileImageSource
 * JD-Core Version:    0.6.2
 */