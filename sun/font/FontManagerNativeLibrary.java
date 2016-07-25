/*    */ package sun.font;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public class FontManagerNativeLibrary
/*    */ {
/*    */   public static void load()
/*    */   {
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 32 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run()
/*    */       {
/* 36 */         System.loadLibrary("awt");
/* 37 */         if ((FontUtilities.isOpenJDK) && (System.getProperty("os.name").startsWith("Windows")))
/*    */         {
/* 59 */           System.loadLibrary("freetype");
/*    */         }
/* 61 */         System.loadLibrary("fontmanager");
/*    */ 
/* 63 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontManagerNativeLibrary
 * JD-Core Version:    0.6.2
 */