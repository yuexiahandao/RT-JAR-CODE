/*    */ package sun.security.tools;
/*    */ 
/*    */ public class KeyStoreUtil
/*    */ {
/*    */   public static boolean isWindowsKeyStore(String paramString)
/*    */   {
/* 53 */     return (paramString.equalsIgnoreCase("Windows-MY")) || (paramString.equalsIgnoreCase("Windows-ROOT"));
/*    */   }
/*    */ 
/*    */   public static String niceStoreTypeName(String paramString)
/*    */   {
/* 61 */     if (paramString.equalsIgnoreCase("Windows-MY"))
/* 62 */       return "Windows-MY";
/* 63 */     if (paramString.equalsIgnoreCase("Windows-ROOT")) {
/* 64 */       return "Windows-ROOT";
/*    */     }
/* 66 */     return paramString.toUpperCase();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.KeyStoreUtil
 * JD-Core Version:    0.6.2
 */