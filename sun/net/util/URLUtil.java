/*    */ package sun.net.util;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public class URLUtil
/*    */ {
/*    */   public static String urlNoFragString(URL paramURL)
/*    */   {
/* 45 */     StringBuilder localStringBuilder = new StringBuilder();
/*    */ 
/* 47 */     String str1 = paramURL.getProtocol();
/* 48 */     if (str1 != null)
/*    */     {
/* 50 */       str1 = str1.toLowerCase();
/* 51 */       localStringBuilder.append(str1);
/* 52 */       localStringBuilder.append("://");
/*    */     }
/*    */ 
/* 55 */     String str2 = paramURL.getHost();
/* 56 */     if (str2 != null)
/*    */     {
/* 58 */       str2 = str2.toLowerCase();
/* 59 */       localStringBuilder.append(str2);
/*    */ 
/* 61 */       int i = paramURL.getPort();
/* 62 */       if (i == -1)
/*    */       {
/* 65 */         i = paramURL.getDefaultPort();
/*    */       }
/* 67 */       if (i != -1) {
/* 68 */         localStringBuilder.append(":").append(i);
/*    */       }
/*    */     }
/*    */ 
/* 72 */     String str3 = paramURL.getFile();
/* 73 */     if (str3 != null) {
/* 74 */       localStringBuilder.append(str3);
/*    */     }
/*    */ 
/* 77 */     return localStringBuilder.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.util.URLUtil
 * JD-Core Version:    0.6.2
 */