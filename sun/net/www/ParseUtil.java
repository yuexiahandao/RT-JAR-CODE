/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.BitSet;
/*     */ import sun.nio.cs.ThreadLocalCoders;
/*     */ 
/*     */ public class ParseUtil
/*     */ {
/*     */   static BitSet encodedInPath;
/* 507 */   private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/* 594 */   private static final long L_DIGIT = lowMask('0', '9');
/*     */   private static final long H_DIGIT = 0L;
/* 599 */   private static final long L_HEX = L_DIGIT;
/* 600 */   private static final long H_HEX = highMask('A', 'F') | highMask('a', 'f');
/*     */   private static final long L_UPALPHA = 0L;
/* 606 */   private static final long H_UPALPHA = highMask('A', 'Z');
/*     */   private static final long L_LOWALPHA = 0L;
/* 612 */   private static final long H_LOWALPHA = highMask('a', 'z');
/*     */   private static final long L_ALPHA = 0L;
/* 616 */   private static final long H_ALPHA = H_LOWALPHA | H_UPALPHA;
/*     */ 
/* 619 */   private static final long L_ALPHANUM = L_DIGIT | 0L;
/* 620 */   private static final long H_ALPHANUM = 0L | H_ALPHA;
/*     */ 
/* 624 */   private static final long L_MARK = lowMask("-_.!~*'()");
/* 625 */   private static final long H_MARK = highMask("-_.!~*'()");
/*     */ 
/* 628 */   private static final long L_UNRESERVED = L_ALPHANUM | L_MARK;
/* 629 */   private static final long H_UNRESERVED = H_ALPHANUM | H_MARK;
/*     */ 
/* 634 */   private static final long L_RESERVED = lowMask(";/?:@&=+$,[]");
/* 635 */   private static final long H_RESERVED = highMask(";/?:@&=+$,[]");
/*     */   private static final long L_ESCAPED = 1L;
/*     */   private static final long H_ESCAPED = 0L;
/* 643 */   private static final long L_DASH = lowMask("-");
/* 644 */   private static final long H_DASH = highMask("-");
/*     */ 
/* 647 */   private static final long L_URIC = L_RESERVED | L_UNRESERVED | 1L;
/* 648 */   private static final long H_URIC = H_RESERVED | H_UNRESERVED | 0L;
/*     */ 
/* 652 */   private static final long L_PCHAR = L_UNRESERVED | 1L | lowMask(":@&=+$,");
/*     */ 
/* 654 */   private static final long H_PCHAR = H_UNRESERVED | 0L | highMask(":@&=+$,");
/*     */ 
/* 658 */   private static final long L_PATH = L_PCHAR | lowMask(";/");
/* 659 */   private static final long H_PATH = H_PCHAR | highMask(";/");
/*     */ 
/* 663 */   private static final long L_USERINFO = L_UNRESERVED | 1L | lowMask(";:&=+$,");
/*     */ 
/* 665 */   private static final long H_USERINFO = H_UNRESERVED | 0L | highMask(";:&=+$,");
/*     */ 
/* 670 */   private static final long L_REG_NAME = L_UNRESERVED | 1L | lowMask("$,;:@&=+");
/*     */ 
/* 672 */   private static final long H_REG_NAME = H_UNRESERVED | 0L | highMask("$,;:@&=+");
/*     */ 
/* 676 */   private static final long L_SERVER = L_USERINFO | L_ALPHANUM | L_DASH | lowMask(".:@[]");
/*     */ 
/* 678 */   private static final long H_SERVER = H_USERINFO | H_ALPHANUM | H_DASH | highMask(".:@[]");
/*     */ 
/*     */   public static String encodePath(String paramString)
/*     */   {
/*  97 */     return encodePath(paramString, true);
/*     */   }
/*     */ 
/*     */   public static String encodePath(String paramString, boolean paramBoolean)
/*     */   {
/* 105 */     Object localObject = new char[paramString.length() * 2 + 16];
/* 106 */     int i = 0;
/* 107 */     char[] arrayOfChar1 = paramString.toCharArray();
/*     */ 
/* 109 */     int j = paramString.length();
/* 110 */     for (int k = 0; k < j; k++) {
/* 111 */       int m = arrayOfChar1[k];
/* 112 */       if (((!paramBoolean) && (m == 47)) || ((paramBoolean) && (m == File.separatorChar))) {
/* 113 */         localObject[(i++)] = 47;
/*     */       }
/* 115 */       else if (m <= 127) {
/* 116 */         if (((m >= 97) && (m <= 122)) || ((m >= 65) && (m <= 90)) || ((m >= 48) && (m <= 57)))
/*     */         {
/* 119 */           localObject[(i++)] = m;
/*     */         }
/* 121 */         else if (encodedInPath.get(m))
/* 122 */           i = escape((char[])localObject, m, i);
/*     */         else
/* 124 */           localObject[(i++)] = m;
/* 125 */       } else if (m > 2047) {
/* 126 */         i = escape((char[])localObject, (char)(0xE0 | m >> 12 & 0xF), i);
/* 127 */         i = escape((char[])localObject, (char)(0x80 | m >> 6 & 0x3F), i);
/* 128 */         i = escape((char[])localObject, (char)(0x80 | m >> 0 & 0x3F), i);
/*     */       } else {
/* 130 */         i = escape((char[])localObject, (char)(0xC0 | m >> 6 & 0x1F), i);
/* 131 */         i = escape((char[])localObject, (char)(0x80 | m >> 0 & 0x3F), i);
/*     */       }
/*     */ 
/* 136 */       if (i + 9 > localObject.length) {
/* 137 */         int n = localObject.length * 2 + 16;
/* 138 */         if (n < 0) {
/* 139 */           n = 2147483647;
/*     */         }
/* 141 */         char[] arrayOfChar2 = new char[n];
/* 142 */         System.arraycopy(localObject, 0, arrayOfChar2, 0, i);
/* 143 */         localObject = arrayOfChar2;
/*     */       }
/*     */     }
/* 146 */     return new String((char[])localObject, 0, i);
/*     */   }
/*     */ 
/*     */   private static int escape(char[] paramArrayOfChar, char paramChar, int paramInt)
/*     */   {
/* 154 */     paramArrayOfChar[(paramInt++)] = '%';
/* 155 */     paramArrayOfChar[(paramInt++)] = Character.forDigit(paramChar >> '\004' & 0xF, 16);
/* 156 */     paramArrayOfChar[(paramInt++)] = Character.forDigit(paramChar & 0xF, 16);
/* 157 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static byte unescape(String paramString, int paramInt)
/*     */   {
/* 164 */     return (byte)Integer.parseInt(paramString.substring(paramInt + 1, paramInt + 3), 16);
/*     */   }
/*     */ 
/*     */   public static String decode(String paramString)
/*     */   {
/* 174 */     int i = paramString.length();
/* 175 */     if ((i == 0) || (paramString.indexOf('%') < 0)) {
/* 176 */       return paramString;
/*     */     }
/* 178 */     StringBuilder localStringBuilder = new StringBuilder(i);
/* 179 */     ByteBuffer localByteBuffer = ByteBuffer.allocate(i);
/* 180 */     CharBuffer localCharBuffer = CharBuffer.allocate(i);
/* 181 */     CharsetDecoder localCharsetDecoder = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */ 
/* 185 */     char c = paramString.charAt(0);
/* 186 */     for (int j = 0; j < i; ) {
/* 187 */       assert (c == paramString.charAt(j));
/* 188 */       if (c != '%') {
/* 189 */         localStringBuilder.append(c);
/* 190 */         j++; if (j >= i)
/*     */           break;
/* 192 */         c = paramString.charAt(j);
/*     */       }
/*     */       else {
/* 195 */         localByteBuffer.clear();
/* 196 */         int k = j;
/*     */         while (true) {
/* 198 */           assert (i - j >= 2);
/*     */           try {
/* 200 */             localByteBuffer.put(unescape(paramString, j));
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 202 */             throw new IllegalArgumentException();
/*     */           }
/* 204 */           j += 3;
/* 205 */           if (j < i)
/*     */           {
/* 207 */             c = paramString.charAt(j);
/* 208 */             if (c != '%')
/* 209 */               break; 
/*     */           }
/*     */         }
/* 211 */         localByteBuffer.flip();
/* 212 */         localCharBuffer.clear();
/* 213 */         localCharsetDecoder.reset();
/* 214 */         CoderResult localCoderResult = localCharsetDecoder.decode(localByteBuffer, localCharBuffer, true);
/* 215 */         if (localCoderResult.isError())
/* 216 */           throw new IllegalArgumentException("Error decoding percent encoded characters");
/* 217 */         localCoderResult = localCharsetDecoder.flush(localCharBuffer);
/* 218 */         if (localCoderResult.isError())
/* 219 */           throw new IllegalArgumentException("Error decoding percent encoded characters");
/* 220 */         localStringBuilder.append(localCharBuffer.flip().toString());
/*     */       }
/*     */     }
/* 223 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String canonizeString(String paramString)
/*     */   {
/* 230 */     int i = 0;
/* 231 */     int j = paramString.length();
/*     */ 
/* 234 */     while ((i = paramString.indexOf("/../")) >= 0) {
/* 235 */       if ((j = paramString.lastIndexOf('/', i - 1)) >= 0)
/* 236 */         paramString = paramString.substring(0, j) + paramString.substring(i + 3);
/*     */       else {
/* 238 */         paramString = paramString.substring(i + 3);
/*     */       }
/*     */     }
/*     */ 
/* 242 */     while ((i = paramString.indexOf("/./")) >= 0) {
/* 243 */       paramString = paramString.substring(0, i) + paramString.substring(i + 2);
/*     */     }
/*     */ 
/* 246 */     while (paramString.endsWith("/..")) {
/* 247 */       i = paramString.indexOf("/..");
/* 248 */       if ((j = paramString.lastIndexOf('/', i - 1)) >= 0)
/* 249 */         paramString = paramString.substring(0, j + 1);
/*     */       else {
/* 251 */         paramString = paramString.substring(0, i);
/*     */       }
/*     */     }
/*     */ 
/* 255 */     if (paramString.endsWith("/.")) {
/* 256 */       paramString = paramString.substring(0, paramString.length() - 1);
/*     */     }
/* 258 */     return paramString;
/*     */   }
/*     */ 
/*     */   public static URL fileToEncodedURL(File paramFile)
/*     */     throws MalformedURLException
/*     */   {
/* 264 */     String str = paramFile.getAbsolutePath();
/* 265 */     str = encodePath(str);
/* 266 */     if (!str.startsWith("/")) {
/* 267 */       str = "/" + str;
/*     */     }
/* 269 */     if ((!str.endsWith("/")) && (paramFile.isDirectory())) {
/* 270 */       str = str + "/";
/*     */     }
/* 272 */     return new URL("file", "", str);
/*     */   }
/*     */ 
/*     */   public static URI toURI(URL paramURL) {
/* 276 */     String str1 = paramURL.getProtocol();
/* 277 */     String str2 = paramURL.getAuthority();
/* 278 */     String str3 = paramURL.getPath();
/* 279 */     String str4 = paramURL.getQuery();
/* 280 */     String str5 = paramURL.getRef();
/* 281 */     if ((str3 != null) && (!str3.startsWith("/"))) {
/* 282 */       str3 = "/" + str3;
/*     */     }
/*     */ 
/* 288 */     if ((str2 != null) && (str2.endsWith(":-1")))
/* 289 */       str2 = str2.substring(0, str2.length() - 3);
/*     */     URI localURI;
/*     */     try
/*     */     {
/* 293 */       localURI = createURI(str1, str2, str3, str4, str5);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 295 */       localURI = null;
/*     */     }
/* 297 */     return localURI;
/*     */   }
/*     */ 
/*     */   private static URI createURI(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */     throws URISyntaxException
/*     */   {
/* 316 */     String str = toString(paramString1, null, paramString2, null, null, -1, paramString3, paramString4, paramString5);
/*     */ 
/* 319 */     checkPath(str, paramString1, paramString3);
/* 320 */     return new URI(str);
/*     */   }
/*     */ 
/*     */   private static String toString(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt, String paramString6, String paramString7, String paramString8)
/*     */   {
/* 333 */     StringBuffer localStringBuffer = new StringBuffer();
/* 334 */     if (paramString1 != null) {
/* 335 */       localStringBuffer.append(paramString1);
/* 336 */       localStringBuffer.append(':');
/*     */     }
/* 338 */     appendSchemeSpecificPart(localStringBuffer, paramString2, paramString3, paramString4, paramString5, paramInt, paramString6, paramString7);
/*     */ 
/* 341 */     appendFragment(localStringBuffer, paramString8);
/* 342 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static void appendSchemeSpecificPart(StringBuffer paramStringBuffer, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, String paramString5, String paramString6)
/*     */   {
/* 354 */     if (paramString1 != null)
/*     */     {
/* 358 */       if (paramString1.startsWith("//[")) {
/* 359 */         int i = paramString1.indexOf("]");
/* 360 */         if ((i != -1) && (paramString1.indexOf(":") != -1))
/*     */         {
/*     */           String str2;
/*     */           String str1;
/* 362 */           if (i == paramString1.length()) {
/* 363 */             str2 = paramString1;
/* 364 */             str1 = "";
/*     */           } else {
/* 366 */             str2 = paramString1.substring(0, i + 1);
/* 367 */             str1 = paramString1.substring(i + 1);
/*     */           }
/* 369 */           paramStringBuffer.append(str2);
/* 370 */           paramStringBuffer.append(quote(str1, L_URIC, H_URIC));
/*     */         }
/*     */       } else {
/* 373 */         paramStringBuffer.append(quote(paramString1, L_URIC, H_URIC));
/*     */       }
/*     */     } else {
/* 376 */       appendAuthority(paramStringBuffer, paramString2, paramString3, paramString4, paramInt);
/* 377 */       if (paramString5 != null)
/* 378 */         paramStringBuffer.append(quote(paramString5, L_PATH, H_PATH));
/* 379 */       if (paramString6 != null) {
/* 380 */         paramStringBuffer.append('?');
/* 381 */         paramStringBuffer.append(quote(paramString6, L_URIC, H_URIC));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void appendAuthority(StringBuffer paramStringBuffer, String paramString1, String paramString2, String paramString3, int paramInt)
/*     */   {
/*     */     int i;
/* 392 */     if (paramString3 != null) {
/* 393 */       paramStringBuffer.append("//");
/* 394 */       if (paramString2 != null) {
/* 395 */         paramStringBuffer.append(quote(paramString2, L_USERINFO, H_USERINFO));
/* 396 */         paramStringBuffer.append('@');
/*     */       }
/* 398 */       i = (paramString3.indexOf(':') >= 0) && (!paramString3.startsWith("[")) && (!paramString3.endsWith("]")) ? 1 : 0;
/*     */ 
/* 401 */       if (i != 0) paramStringBuffer.append('[');
/* 402 */       paramStringBuffer.append(paramString3);
/* 403 */       if (i != 0) paramStringBuffer.append(']');
/* 404 */       if (paramInt != -1) {
/* 405 */         paramStringBuffer.append(':');
/* 406 */         paramStringBuffer.append(paramInt);
/*     */       }
/* 408 */     } else if (paramString1 != null) {
/* 409 */       paramStringBuffer.append("//");
/* 410 */       if (paramString1.startsWith("[")) {
/* 411 */         i = paramString1.indexOf("]");
/* 412 */         if ((i != -1) && (paramString1.indexOf(":") != -1))
/*     */         {
/*     */           String str2;
/*     */           String str1;
/* 414 */           if (i == paramString1.length()) {
/* 415 */             str2 = paramString1;
/* 416 */             str1 = "";
/*     */           } else {
/* 418 */             str2 = paramString1.substring(0, i + 1);
/* 419 */             str1 = paramString1.substring(i + 1);
/*     */           }
/* 421 */           paramStringBuffer.append(str2);
/* 422 */           paramStringBuffer.append(quote(str1, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 427 */         paramStringBuffer.append(quote(paramString1, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void appendFragment(StringBuffer paramStringBuffer, String paramString)
/*     */   {
/* 435 */     if (paramString != null) {
/* 436 */       paramStringBuffer.append('#');
/* 437 */       paramStringBuffer.append(quote(paramString, L_URIC, H_URIC));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String quote(String paramString, long paramLong1, long paramLong2)
/*     */   {
/* 445 */     int i = paramString.length();
/* 446 */     StringBuffer localStringBuffer = null;
/* 447 */     int j = (paramLong1 & 1L) != 0L ? 1 : 0;
/* 448 */     for (int k = 0; k < paramString.length(); k++) {
/* 449 */       char c = paramString.charAt(k);
/* 450 */       if (c < '') {
/* 451 */         if ((!match(c, paramLong1, paramLong2)) && (!isEscaped(paramString, k))) {
/* 452 */           if (localStringBuffer == null) {
/* 453 */             localStringBuffer = new StringBuffer();
/* 454 */             localStringBuffer.append(paramString.substring(0, k));
/*     */           }
/* 456 */           appendEscape(localStringBuffer, (byte)c);
/*     */         }
/* 458 */         else if (localStringBuffer != null) {
/* 459 */           localStringBuffer.append(c);
/*     */         }
/* 461 */       } else if ((j != 0) && ((Character.isSpaceChar(c)) || (Character.isISOControl(c))))
/*     */       {
/* 464 */         if (localStringBuffer == null) {
/* 465 */           localStringBuffer = new StringBuffer();
/* 466 */           localStringBuffer.append(paramString.substring(0, k));
/*     */         }
/* 468 */         appendEncoded(localStringBuffer, c);
/*     */       }
/* 470 */       else if (localStringBuffer != null) {
/* 471 */         localStringBuffer.append(c);
/*     */       }
/*     */     }
/* 474 */     return localStringBuffer == null ? paramString : localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isEscaped(String paramString, int paramInt)
/*     */   {
/* 482 */     if ((paramString == null) || (paramString.length() <= paramInt + 2)) {
/* 483 */       return false;
/*     */     }
/* 485 */     return (paramString.charAt(paramInt) == '%') && (match(paramString.charAt(paramInt + 1), L_HEX, H_HEX)) && (match(paramString.charAt(paramInt + 2), L_HEX, H_HEX));
/*     */   }
/*     */ 
/*     */   private static void appendEncoded(StringBuffer paramStringBuffer, char paramChar)
/*     */   {
/* 491 */     ByteBuffer localByteBuffer = null;
/*     */     try {
/* 493 */       localByteBuffer = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap("" + paramChar));
/*     */     }
/*     */     catch (CharacterCodingException localCharacterCodingException) {
/* 496 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 498 */     while (localByteBuffer.hasRemaining()) {
/* 499 */       int i = localByteBuffer.get() & 0xFF;
/* 500 */       if (i >= 128)
/* 501 */         appendEscape(paramStringBuffer, (byte)i);
/*     */       else
/* 503 */         paramStringBuffer.append((char)i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void appendEscape(StringBuffer paramStringBuffer, byte paramByte)
/*     */   {
/* 513 */     paramStringBuffer.append('%');
/* 514 */     paramStringBuffer.append(hexDigits[(paramByte >> 4 & 0xF)]);
/* 515 */     paramStringBuffer.append(hexDigits[(paramByte >> 0 & 0xF)]);
/*     */   }
/*     */ 
/*     */   private static boolean match(char paramChar, long paramLong1, long paramLong2)
/*     */   {
/* 520 */     if (paramChar < '@')
/* 521 */       return (1L << paramChar & paramLong1) != 0L;
/* 522 */     if (paramChar < '')
/* 523 */       return (1L << paramChar - '@' & paramLong2) != 0L;
/* 524 */     return false;
/*     */   }
/*     */ 
/*     */   private static void checkPath(String paramString1, String paramString2, String paramString3)
/*     */     throws URISyntaxException
/*     */   {
/* 532 */     if ((paramString2 != null) && 
/* 533 */       (paramString3 != null) && (paramString3.length() > 0) && (paramString3.charAt(0) != '/'))
/*     */     {
/* 535 */       throw new URISyntaxException(paramString1, "Relative path in absolute URI");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static long lowMask(char paramChar1, char paramChar2)
/*     */   {
/* 546 */     long l = 0L;
/* 547 */     int i = Math.max(Math.min(paramChar1, 63), 0);
/* 548 */     int j = Math.max(Math.min(paramChar2, 63), 0);
/* 549 */     for (int k = i; k <= j; k++)
/* 550 */       l |= 1L << k;
/* 551 */     return l;
/*     */   }
/*     */ 
/*     */   private static long lowMask(String paramString)
/*     */   {
/* 556 */     int i = paramString.length();
/* 557 */     long l = 0L;
/* 558 */     for (int j = 0; j < i; j++) {
/* 559 */       int k = paramString.charAt(j);
/* 560 */       if (k < 64)
/* 561 */         l |= 1L << k;
/*     */     }
/* 563 */     return l;
/*     */   }
/*     */ 
/*     */   private static long highMask(char paramChar1, char paramChar2)
/*     */   {
/* 569 */     long l = 0L;
/* 570 */     int i = Math.max(Math.min(paramChar1, 127), 64) - 64;
/* 571 */     int j = Math.max(Math.min(paramChar2, 127), 64) - 64;
/* 572 */     for (int k = i; k <= j; k++)
/* 573 */       l |= 1L << k;
/* 574 */     return l;
/*     */   }
/*     */ 
/*     */   private static long highMask(String paramString)
/*     */   {
/* 579 */     int i = paramString.length();
/* 580 */     long l = 0L;
/* 581 */     for (int j = 0; j < i; j++) {
/* 582 */       int k = paramString.charAt(j);
/* 583 */       if ((k >= 64) && (k < 128))
/* 584 */         l |= 1L << k - 64;
/*     */     }
/* 586 */     return l;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  52 */     encodedInPath = new BitSet(256);
/*     */ 
/*  59 */     encodedInPath.set(61);
/*  60 */     encodedInPath.set(59);
/*  61 */     encodedInPath.set(63);
/*  62 */     encodedInPath.set(47);
/*     */ 
/*  66 */     encodedInPath.set(35);
/*  67 */     encodedInPath.set(32);
/*  68 */     encodedInPath.set(60);
/*  69 */     encodedInPath.set(62);
/*  70 */     encodedInPath.set(37);
/*  71 */     encodedInPath.set(34);
/*  72 */     encodedInPath.set(123);
/*  73 */     encodedInPath.set(125);
/*  74 */     encodedInPath.set(124);
/*  75 */     encodedInPath.set(92);
/*  76 */     encodedInPath.set(94);
/*  77 */     encodedInPath.set(91);
/*  78 */     encodedInPath.set(93);
/*  79 */     encodedInPath.set(96);
/*     */ 
/*  82 */     for (int i = 0; i < 32; i++)
/*  83 */       encodedInPath.set(i);
/*  84 */     encodedInPath.set(127);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.ParseUtil
 * JD-Core Version:    0.6.2
 */