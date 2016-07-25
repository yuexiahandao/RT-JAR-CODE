/*     */ package java.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class URLDecoder
/*     */ {
/*  81 */   static String dfltEncName = URLEncoder.dfltEncName;
/*     */ 
/*     */   @Deprecated
/*     */   public static String decode(String paramString)
/*     */   {
/*  97 */     String str = null;
/*     */     try
/*     */     {
/* 100 */       str = decode(paramString, dfltEncName);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/*     */     }
/* 105 */     return str;
/*     */   }
/*     */ 
/*     */   public static String decode(String paramString1, String paramString2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 135 */     int i = 0;
/* 136 */     int j = paramString1.length();
/* 137 */     StringBuffer localStringBuffer = new StringBuffer(j > 500 ? j / 2 : j);
/* 138 */     int k = 0;
/*     */ 
/* 140 */     if (paramString2.length() == 0) {
/* 141 */       throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
/*     */     }
/*     */ 
/* 145 */     byte[] arrayOfByte = null;
/* 146 */     while (k < j) {
/* 147 */       char c = paramString1.charAt(k);
/* 148 */       switch (c) {
/*     */       case '+':
/* 150 */         localStringBuffer.append(' ');
/* 151 */         k++;
/* 152 */         i = 1;
/* 153 */         break;
/*     */       case '%':
/*     */         try
/*     */         {
/* 168 */           if (arrayOfByte == null)
/* 169 */             arrayOfByte = new byte[(j - k) / 3];
/* 170 */           int m = 0;
/*     */ 
/* 172 */           while ((k + 2 < j) && (c == '%'))
/*     */           {
/* 174 */             int n = Integer.parseInt(paramString1.substring(k + 1, k + 3), 16);
/* 175 */             if (n < 0)
/* 176 */               throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
/* 177 */             arrayOfByte[(m++)] = ((byte)n);
/* 178 */             k += 3;
/* 179 */             if (k < j) {
/* 180 */               c = paramString1.charAt(k);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 186 */           if ((k < j) && (c == '%')) {
/* 187 */             throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
/*     */           }
/*     */ 
/* 190 */           localStringBuffer.append(new String(arrayOfByte, 0, m, paramString2));
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 192 */           throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + localNumberFormatException.getMessage());
/*     */         }
/*     */ 
/* 196 */         i = 1;
/* 197 */         break;
/*     */       default:
/* 199 */         localStringBuffer.append(c);
/* 200 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 205 */     return i != 0 ? localStringBuffer.toString() : paramString1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URLDecoder
 * JD-Core Version:    0.6.2
 */