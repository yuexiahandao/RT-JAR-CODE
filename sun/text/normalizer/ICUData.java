/*    */ package sun.text.normalizer;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.MissingResourceException;
/*    */ 
/*    */ public final class ICUData
/*    */ {
/*    */   private static InputStream getStream(Class paramClass, final String paramString, boolean paramBoolean)
/*    */   {
/* 52 */     InputStream localInputStream = null;
/*    */ 
/* 54 */     if (System.getSecurityManager() != null)
/* 55 */       localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
/*    */         public Object run() {
/* 57 */           return this.val$root.getResourceAsStream(paramString);
/*    */         }
/*    */       });
/*    */     else {
/* 61 */       localInputStream = paramClass.getResourceAsStream(paramString);
/*    */     }
/*    */ 
/* 64 */     if ((localInputStream == null) && (paramBoolean)) {
/* 65 */       throw new MissingResourceException("could not locate data", paramClass.getPackage().getName(), paramString);
/*    */     }
/* 67 */     return localInputStream;
/*    */   }
/*    */ 
/*    */   public static InputStream getStream(String paramString)
/*    */   {
/* 74 */     return getStream(ICUData.class, paramString, false);
/*    */   }
/*    */ 
/*    */   public static InputStream getRequiredStream(String paramString)
/*    */   {
/* 81 */     return getStream(ICUData.class, paramString, true);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.ICUData
 * JD-Core Version:    0.6.2
 */