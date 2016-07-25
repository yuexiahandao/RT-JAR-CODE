/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.ParseException;
/*     */ import sun.net.idn.Punycode;
/*     */ import sun.net.idn.StringPrep;
/*     */ import sun.text.normalizer.UCharacterIterator;
/*     */ 
/*     */ public final class IDN
/*     */ {
/*     */   public static final int ALLOW_UNASSIGNED = 1;
/*     */   public static final int USE_STD3_ASCII_RULES = 2;
/*     */   private static final String ACE_PREFIX = "xn--";
/*     */   private static final int ACE_PREFIX_LENGTH;
/*     */   private static final int MAX_LABEL_LENGTH = 63;
/*     */   private static StringPrep namePrep;
/*     */ 
/*     */   public static String toASCII(String paramString, int paramInt)
/*     */   {
/* 113 */     int i = 0; int j = 0;
/* 114 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 116 */     while (i < paramString.length()) {
/* 117 */       j = searchDots(paramString, i);
/* 118 */       localStringBuffer.append(toASCIIInternal(paramString.substring(i, j), paramInt));
/* 119 */       i = j + 1;
/* 120 */       if (i < paramString.length()) localStringBuffer.append('.');
/*     */     }
/*     */ 
/* 123 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static String toASCII(String paramString)
/*     */   {
/* 144 */     return toASCII(paramString, 0);
/*     */   }
/*     */ 
/*     */   public static String toUnicode(String paramString, int paramInt)
/*     */   {
/* 167 */     int i = 0; int j = 0;
/* 168 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 170 */     while (i < paramString.length()) {
/* 171 */       j = searchDots(paramString, i);
/* 172 */       localStringBuffer.append(toUnicodeInternal(paramString.substring(i, j), paramInt));
/* 173 */       i = j + 1;
/* 174 */       if (i < paramString.length()) localStringBuffer.append('.');
/*     */     }
/*     */ 
/* 177 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static String toUnicode(String paramString)
/*     */   {
/* 196 */     return toUnicode(paramString, 0);
/*     */   }
/*     */ 
/*     */   private static String toASCIIInternal(String paramString, int paramInt)
/*     */   {
/* 250 */     boolean bool = isAllASCII(paramString);
/*     */     StringBuffer localStringBuffer;
/* 255 */     if (!bool) {
/* 256 */       UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(paramString);
/*     */       try {
/* 258 */         localStringBuffer = namePrep.prepare(localUCharacterIterator, paramInt);
/*     */       } catch (ParseException localParseException1) {
/* 260 */         throw new IllegalArgumentException(localParseException1);
/*     */       }
/*     */     } else {
/* 263 */       localStringBuffer = new StringBuffer(paramString);
/*     */     }
/*     */ 
/* 270 */     int i = (paramInt & 0x2) != 0 ? 1 : 0;
/* 271 */     if (i != 0) {
/* 272 */       for (int j = 0; j < localStringBuffer.length(); j++) {
/* 273 */         int k = localStringBuffer.charAt(j);
/* 274 */         if (isNonLDHAsciiCodePoint(k)) {
/* 275 */           throw new IllegalArgumentException("Contains non-LDH ASCII characters");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 280 */       if ((localStringBuffer.charAt(0) == '-') || (localStringBuffer.charAt(localStringBuffer.length() - 1) == '-'))
/*     */       {
/* 283 */         throw new IllegalArgumentException("Has leading or trailing hyphen");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 288 */     if (!bool)
/*     */     {
/* 291 */       if (!isAllASCII(localStringBuffer.toString()))
/*     */       {
/* 294 */         if (!startsWithACEPrefix(localStringBuffer))
/*     */         {
/*     */           try
/*     */           {
/* 299 */             localStringBuffer = Punycode.encode(localStringBuffer, null);
/*     */           } catch (ParseException localParseException2) {
/* 301 */             throw new IllegalArgumentException(localParseException2);
/*     */           }
/*     */ 
/* 304 */           localStringBuffer = toASCIILower(localStringBuffer);
/*     */ 
/* 308 */           localStringBuffer.insert(0, "xn--");
/*     */         } else {
/* 310 */           throw new IllegalArgumentException("The input starts with the ACE Prefix");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 318 */     if (localStringBuffer.length() > 63) {
/* 319 */       throw new IllegalArgumentException("The label in the input is too long");
/*     */     }
/*     */ 
/* 322 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String toUnicodeInternal(String paramString, int paramInt)
/*     */   {
/* 329 */     Object localObject = null;
/*     */ 
/* 334 */     boolean bool = isAllASCII(paramString);
/*     */     StringBuffer localStringBuffer1;
/* 336 */     if (!bool)
/*     */     {
/*     */       try
/*     */       {
/* 340 */         UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(paramString);
/* 341 */         localStringBuffer1 = namePrep.prepare(localUCharacterIterator, paramInt);
/*     */       }
/*     */       catch (Exception localException1) {
/* 344 */         return paramString;
/*     */       }
/*     */     }
/* 347 */     else localStringBuffer1 = new StringBuffer(paramString);
/*     */ 
/* 352 */     if (startsWithACEPrefix(localStringBuffer1))
/*     */     {
/* 356 */       String str1 = localStringBuffer1.substring(ACE_PREFIX_LENGTH, localStringBuffer1.length());
/*     */       try
/*     */       {
/* 361 */         StringBuffer localStringBuffer2 = Punycode.decode(new StringBuffer(str1), null);
/*     */ 
/* 365 */         String str2 = toASCII(localStringBuffer2.toString(), paramInt);
/*     */ 
/* 369 */         if (str2.equalsIgnoreCase(localStringBuffer1.toString()))
/*     */         {
/* 372 */           return localStringBuffer2.toString();
/*     */         }
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 380 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static boolean isNonLDHAsciiCodePoint(int paramInt)
/*     */   {
/* 394 */     return ((0 <= paramInt) && (paramInt <= 44)) || ((46 <= paramInt) && (paramInt <= 47)) || ((58 <= paramInt) && (paramInt <= 64)) || ((91 <= paramInt) && (paramInt <= 96)) || ((123 <= paramInt) && (paramInt <= 127));
/*     */   }
/*     */ 
/*     */   private static int searchDots(String paramString, int paramInt)
/*     */   {
/* 409 */     for (int i = paramInt; i < paramString.length(); i++) {
/* 410 */       int j = paramString.charAt(i);
/* 411 */       if ((j == 46) || (j == 12290) || (j == 65294) || (j == 65377))
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 416 */     return i;
/*     */   }
/*     */ 
/*     */   private static boolean isAllASCII(String paramString)
/*     */   {
/* 424 */     boolean bool = true;
/* 425 */     for (int i = 0; i < paramString.length(); i++) {
/* 426 */       int j = paramString.charAt(i);
/* 427 */       if (j > 127) {
/* 428 */         bool = false;
/* 429 */         break;
/*     */       }
/*     */     }
/* 432 */     return bool;
/*     */   }
/*     */ 
/*     */   private static boolean startsWithACEPrefix(StringBuffer paramStringBuffer)
/*     */   {
/* 439 */     boolean bool = true;
/*     */ 
/* 441 */     if (paramStringBuffer.length() < ACE_PREFIX_LENGTH) {
/* 442 */       return false;
/*     */     }
/* 444 */     for (int i = 0; i < ACE_PREFIX_LENGTH; i++) {
/* 445 */       if (toASCIILower(paramStringBuffer.charAt(i)) != "xn--".charAt(i)) {
/* 446 */         bool = false;
/*     */       }
/*     */     }
/* 449 */     return bool;
/*     */   }
/*     */ 
/*     */   private static char toASCIILower(char paramChar) {
/* 453 */     if (('A' <= paramChar) && (paramChar <= 'Z')) {
/* 454 */       return (char)(paramChar + 'a' - 65);
/*     */     }
/* 456 */     return paramChar;
/*     */   }
/*     */ 
/*     */   private static StringBuffer toASCIILower(StringBuffer paramStringBuffer) {
/* 460 */     StringBuffer localStringBuffer = new StringBuffer();
/* 461 */     for (int i = 0; i < paramStringBuffer.length(); i++) {
/* 462 */       localStringBuffer.append(toASCIILower(paramStringBuffer.charAt(i)));
/*     */     }
/* 464 */     return localStringBuffer;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 204 */     ACE_PREFIX_LENGTH = "xn--".length();
/*     */ 
/* 209 */     namePrep = null;
/*     */ 
/* 212 */     InputStream localInputStream = null;
/*     */     try
/*     */     {
/* 216 */       if (System.getSecurityManager() != null)
/* 217 */         localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public InputStream run() {
/* 219 */             return StringPrep.class.getResourceAsStream("uidna.spp");
/*     */           }
/*     */         });
/*     */       else {
/* 223 */         localInputStream = StringPrep.class.getResourceAsStream("uidna.spp");
/*     */       }
/*     */ 
/* 226 */       namePrep = new StringPrep(localInputStream);
/* 227 */       localInputStream.close();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 230 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.IDN
 * JD-Core Version:    0.6.2
 */