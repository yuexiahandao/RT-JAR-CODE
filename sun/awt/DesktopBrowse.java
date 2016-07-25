/*    */ package sun.awt;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public abstract class DesktopBrowse
/*    */ {
/*    */   private static volatile DesktopBrowse mInstance;
/*    */ 
/*    */   public static void setInstance(DesktopBrowse paramDesktopBrowse)
/*    */   {
/* 34 */     if (mInstance != null) {
/* 35 */       throw new IllegalStateException("DesktopBrowse instance has already been set.");
/*    */     }
/* 37 */     mInstance = paramDesktopBrowse;
/*    */   }
/*    */ 
/*    */   public static DesktopBrowse getInstance() {
/* 41 */     return mInstance;
/*    */   }
/*    */ 
/*    */   public abstract void browse(URL paramURL);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.DesktopBrowse
 * JD-Core Version:    0.6.2
 */