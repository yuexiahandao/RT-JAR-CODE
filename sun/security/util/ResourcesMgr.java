/*    */ package sun.security.util;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class ResourcesMgr
/*    */ {
/*    */   private static ResourceBundle bundle;
/*    */   private static ResourceBundle altBundle;
/*    */ 
/*    */   public static String getString(String paramString)
/*    */   {
/* 40 */     if (bundle == null)
/*    */     {
/* 43 */       bundle = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */         public ResourceBundle run() {
/* 46 */           return ResourceBundle.getBundle("sun.security.util.Resources");
/*    */         }
/*    */ 
/*    */       });
/*    */     }
/*    */ 
/* 52 */     return bundle.getString(paramString);
/*    */   }
/*    */ 
/*    */   public static String getString(String paramString1, String paramString2)
/*    */   {
/* 57 */     if (altBundle == null)
/*    */     {
/* 60 */       altBundle = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */         public ResourceBundle run() {
/* 63 */           return ResourceBundle.getBundle(this.val$altBundleName);
/*    */         }
/*    */       });
/*    */     }
/*    */ 
/* 68 */     return altBundle.getString(paramString1);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.ResourcesMgr
 * JD-Core Version:    0.6.2
 */