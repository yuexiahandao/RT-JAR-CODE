/*     */ package java.net;
/*     */ 
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.security.AccessController;
/*     */ import java.util.BitSet;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class URLEncoder
/*     */ {
/*     */   static BitSet dontNeedEncoding;
/*     */   static final int caseDiff = 32;
/* 144 */   static String dfltEncName = (String)AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
/*     */ 
/*     */   @Deprecated
/*     */   public static String encode(String paramString)
/*     */   {
/* 168 */     String str = null;
/*     */     try
/*     */     {
/* 171 */       str = encode(paramString, dfltEncName);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/*     */     }
/* 176 */     return str;
/*     */   }
/*     */ 
/*     */   public static String encode(String paramString1, String paramString2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 204 */     int i = 0;
/* 205 */     StringBuffer localStringBuffer = new StringBuffer(paramString1.length());
/*     */ 
/* 207 */     CharArrayWriter localCharArrayWriter = new CharArrayWriter();
/*     */ 
/* 209 */     if (paramString2 == null)
/* 210 */       throw new NullPointerException("charsetName");
/*     */     Charset localCharset;
/*     */     try {
/* 213 */       localCharset = Charset.forName(paramString2);
/*     */     } catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 215 */       throw new UnsupportedEncodingException(paramString2);
/*     */     } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/* 217 */       throw new UnsupportedEncodingException(paramString2);
/*     */     }
/*     */ 
/* 220 */     for (int j = 0; j < paramString1.length(); ) {
/* 221 */       int k = paramString1.charAt(j);
/*     */ 
/* 223 */       if (dontNeedEncoding.get(k)) {
/* 224 */         if (k == 32) {
/* 225 */           k = 43;
/* 226 */           i = 1;
/*     */         }
/*     */ 
/* 229 */         localStringBuffer.append((char)k);
/* 230 */         j++;
/*     */       }
/*     */       else {
/*     */         do {
/* 234 */           localCharArrayWriter.write(k);
/*     */ 
/* 243 */           if ((k >= 55296) && (k <= 56319))
/*     */           {
/* 248 */             if (j + 1 < paramString1.length()) {
/* 249 */               int m = paramString1.charAt(j + 1);
/*     */ 
/* 254 */               if ((m >= 56320) && (m <= 57343))
/*     */               {
/* 260 */                 localCharArrayWriter.write(m);
/* 261 */                 j++;
/*     */               }
/*     */             }
/*     */           }
/* 265 */           j++;
/* 266 */         }while ((j < paramString1.length()) && (!dontNeedEncoding.get(k = paramString1.charAt(j))));
/*     */ 
/* 268 */         localCharArrayWriter.flush();
/* 269 */         String str = new String(localCharArrayWriter.toCharArray());
/* 270 */         byte[] arrayOfByte = str.getBytes(localCharset);
/* 271 */         for (int n = 0; n < arrayOfByte.length; n++) {
/* 272 */           localStringBuffer.append('%');
/* 273 */           char c = Character.forDigit(arrayOfByte[n] >> 4 & 0xF, 16);
/*     */ 
/* 276 */           if (Character.isLetter(c)) {
/* 277 */             c = (char)(c - ' ');
/*     */           }
/* 279 */           localStringBuffer.append(c);
/* 280 */           c = Character.forDigit(arrayOfByte[n] & 0xF, 16);
/* 281 */           if (Character.isLetter(c)) {
/* 282 */             c = (char)(c - ' ');
/*     */           }
/* 284 */           localStringBuffer.append(c);
/*     */         }
/* 286 */         localCharArrayWriter.reset();
/* 287 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 291 */     return i != 0 ? localStringBuffer.toString() : paramString1;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 126 */     dontNeedEncoding = new BitSet(256);
/*     */ 
/* 128 */     for (int i = 97; i <= 122; i++) {
/* 129 */       dontNeedEncoding.set(i);
/*     */     }
/* 131 */     for (i = 65; i <= 90; i++) {
/* 132 */       dontNeedEncoding.set(i);
/*     */     }
/* 134 */     for (i = 48; i <= 57; i++) {
/* 135 */       dontNeedEncoding.set(i);
/*     */     }
/* 137 */     dontNeedEncoding.set(32);
/*     */ 
/* 139 */     dontNeedEncoding.set(45);
/* 140 */     dontNeedEncoding.set(95);
/* 141 */     dontNeedEncoding.set(46);
/* 142 */     dontNeedEncoding.set(42);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URLEncoder
 * JD-Core Version:    0.6.2
 */