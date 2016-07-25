/*    */ package com.sun.corba.se.impl.naming.namingutil;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*    */ import java.io.StringWriter;
/*    */ import org.omg.CORBA.DATA_CONVERSION;
/*    */ 
/*    */ class Utility
/*    */ {
/* 42 */   private static NamingSystemException wrapper = NamingSystemException.get("naming");
/*    */ 
/*    */   static String cleanEscapes(String paramString)
/*    */   {
/* 49 */     StringWriter localStringWriter = new StringWriter();
/* 50 */     for (int i = 0; i < paramString.length(); i++) {
/* 51 */       int j = paramString.charAt(i);
/* 52 */       if (j != 37) {
/* 53 */         localStringWriter.write(j);
/*    */       }
/*    */       else {
/* 56 */         i++;
/* 57 */         int k = hexOf(paramString.charAt(i));
/* 58 */         i++;
/* 59 */         int m = hexOf(paramString.charAt(i));
/* 60 */         int n = k * 16 + m;
/*    */ 
/* 62 */         localStringWriter.write((char)n);
/*    */       }
/*    */     }
/* 65 */     return localStringWriter.toString();
/*    */   }
/*    */ 
/*    */   static int hexOf(char paramChar)
/*    */   {
/* 77 */     int i = paramChar - '0';
/* 78 */     if ((i >= 0) && (i <= 9)) {
/* 79 */       return i;
/*    */     }
/* 81 */     i = paramChar - 'a' + 10;
/* 82 */     if ((i >= 10) && (i <= 15)) {
/* 83 */       return i;
/*    */     }
/* 85 */     i = paramChar - 'A' + 10;
/* 86 */     if ((i >= 10) && (i <= 15)) {
/* 87 */       return i;
/*    */     }
/* 89 */     throw new DATA_CONVERSION();
/*    */   }
/*    */ 
/*    */   static void validateGIOPVersion(IIOPEndpointInfo paramIIOPEndpointInfo)
/*    */   {
/* 97 */     if ((paramIIOPEndpointInfo.getMajor() > 1) || (paramIIOPEndpointInfo.getMinor() > 2))
/*    */     {
/* 100 */       throw wrapper.insBadAddress();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.Utility
 * JD-Core Version:    0.6.2
 */