/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class XML11Char
/*     */ {
/*  49 */   private static final byte[] XML11CHARS = new byte[65536];
/*     */   public static final int MASK_XML11_VALID = 1;
/*     */   public static final int MASK_XML11_SPACE = 2;
/*     */   public static final int MASK_XML11_NAME_START = 4;
/*     */   public static final int MASK_XML11_NAME = 8;
/*     */   public static final int MASK_XML11_CONTROL = 16;
/*     */   public static final int MASK_XML11_CONTENT = 32;
/*     */   public static final int MASK_XML11_NCNAME_START = 64;
/*     */   public static final int MASK_XML11_NCNAME = 128;
/*     */   public static final int MASK_XML11_CONTENT_INTERNAL = 48;
/*     */ 
/*     */   public static boolean isXML11Space(int c)
/*     */   {
/* 157 */     return (c < 65536) && ((XML11CHARS[c] & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Valid(int c)
/*     */   {
/* 171 */     return ((c < 65536) && ((XML11CHARS[c] & 0x1) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Invalid(int c)
/*     */   {
/* 181 */     return !isXML11Valid(c);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidLiteral(int c)
/*     */   {
/* 193 */     return ((c < 65536) && ((XML11CHARS[c] & 0x1) != 0) && ((XML11CHARS[c] & 0x10) == 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Content(int c)
/*     */   {
/* 204 */     return ((c < 65536) && ((XML11CHARS[c] & 0x20) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11InternalEntityContent(int c)
/*     */   {
/* 215 */     return ((c < 65536) && ((XML11CHARS[c] & 0x30) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NameStart(int c)
/*     */   {
/* 227 */     return ((c < 65536) && ((XML11CHARS[c] & 0x4) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Name(int c)
/*     */   {
/* 239 */     return ((c < 65536) && ((XML11CHARS[c] & 0x8) != 0)) || ((c >= 65536) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NCNameStart(int c)
/*     */   {
/* 251 */     return ((c < 65536) && ((XML11CHARS[c] & 0x40) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NCName(int c)
/*     */   {
/* 263 */     return ((c < 65536) && ((XML11CHARS[c] & 0x80) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NameHighSurrogate(int c)
/*     */   {
/* 276 */     return (55296 <= c) && (c <= 56191);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidName(String name)
/*     */   {
/* 290 */     int length = name.length();
/* 291 */     if (length == 0)
/* 292 */       return false;
/* 293 */     int i = 1;
/* 294 */     char ch = name.charAt(0);
/* 295 */     if (!isXML11NameStart(ch)) {
/* 296 */       if ((length > 1) && (isXML11NameHighSurrogate(ch))) {
/* 297 */         char ch2 = name.charAt(1);
/* 298 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NameStart(XMLChar.supplemental(ch, ch2))))
/*     */         {
/* 300 */           return false;
/*     */         }
/* 302 */         i = 2;
/*     */       }
/*     */       else {
/* 305 */         return false;
/*     */       }
/*     */     }
/* 308 */     while (i < length) {
/* 309 */       ch = name.charAt(i);
/* 310 */       if (!isXML11Name(ch)) {
/* 311 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 312 */           char ch2 = name.charAt(i);
/* 313 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11Name(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 315 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 319 */           return false;
/*     */         }
/*     */       }
/* 322 */       i++;
/*     */     }
/* 324 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidNCName(String ncName)
/*     */   {
/* 340 */     int length = ncName.length();
/* 341 */     if (length == 0)
/* 342 */       return false;
/* 343 */     int i = 1;
/* 344 */     char ch = ncName.charAt(0);
/* 345 */     if (!isXML11NCNameStart(ch)) {
/* 346 */       if ((length > 1) && (isXML11NameHighSurrogate(ch))) {
/* 347 */         char ch2 = ncName.charAt(1);
/* 348 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NCNameStart(XMLChar.supplemental(ch, ch2))))
/*     */         {
/* 350 */           return false;
/*     */         }
/* 352 */         i = 2;
/*     */       }
/*     */       else {
/* 355 */         return false;
/*     */       }
/*     */     }
/* 358 */     while (i < length) {
/* 359 */       ch = ncName.charAt(i);
/* 360 */       if (!isXML11NCName(ch)) {
/* 361 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 362 */           char ch2 = ncName.charAt(i);
/* 363 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NCName(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 365 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 369 */           return false;
/*     */         }
/*     */       }
/* 372 */       i++;
/*     */     }
/* 374 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidNmtoken(String nmtoken)
/*     */   {
/* 388 */     int length = nmtoken.length();
/* 389 */     if (length == 0)
/* 390 */       return false;
/* 391 */     for (int i = 0; i < length; i++) {
/* 392 */       char ch = nmtoken.charAt(i);
/* 393 */       if (!isXML11Name(ch)) {
/* 394 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 395 */           char ch2 = nmtoken.charAt(i);
/* 396 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11Name(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 398 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 402 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 406 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidQName(String str)
/*     */   {
/* 416 */     int colon = str.indexOf(':');
/*     */ 
/* 418 */     if ((colon == 0) || (colon == str.length() - 1)) {
/* 419 */       return false;
/*     */     }
/*     */ 
/* 422 */     if (colon > 0) {
/* 423 */       String prefix = str.substring(0, colon);
/* 424 */       String localPart = str.substring(colon + 1);
/* 425 */       return (isXML11ValidNCName(prefix)) && (isXML11ValidNCName(localPart));
/*     */     }
/*     */ 
/* 428 */     return isXML11ValidNCName(str);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     Arrays.fill(XML11CHARS, 1, 9, (byte)17);
/*  88 */     XML11CHARS[9] = 35;
/*  89 */     XML11CHARS[10] = 3;
/*  90 */     Arrays.fill(XML11CHARS, 11, 13, (byte)17);
/*  91 */     XML11CHARS[13] = 3;
/*  92 */     Arrays.fill(XML11CHARS, 14, 32, (byte)17);
/*  93 */     XML11CHARS[32] = 35;
/*  94 */     Arrays.fill(XML11CHARS, 33, 38, (byte)33);
/*  95 */     XML11CHARS[38] = 1;
/*  96 */     Arrays.fill(XML11CHARS, 39, 45, (byte)33);
/*  97 */     Arrays.fill(XML11CHARS, 45, 47, (byte)-87);
/*  98 */     XML11CHARS[47] = 33;
/*  99 */     Arrays.fill(XML11CHARS, 48, 58, (byte)-87);
/* 100 */     XML11CHARS[58] = 45;
/* 101 */     XML11CHARS[59] = 33;
/* 102 */     XML11CHARS[60] = 1;
/* 103 */     Arrays.fill(XML11CHARS, 61, 65, (byte)33);
/* 104 */     Arrays.fill(XML11CHARS, 65, 91, (byte)-19);
/* 105 */     Arrays.fill(XML11CHARS, 91, 93, (byte)33);
/* 106 */     XML11CHARS[93] = 1;
/* 107 */     XML11CHARS[94] = 33;
/* 108 */     XML11CHARS[95] = -19;
/* 109 */     XML11CHARS[96] = 33;
/* 110 */     Arrays.fill(XML11CHARS, 97, 123, (byte)-19);
/* 111 */     Arrays.fill(XML11CHARS, 123, 127, (byte)33);
/* 112 */     Arrays.fill(XML11CHARS, 127, 133, (byte)17);
/* 113 */     XML11CHARS[''] = 35;
/* 114 */     Arrays.fill(XML11CHARS, 134, 160, (byte)17);
/* 115 */     Arrays.fill(XML11CHARS, 160, 183, (byte)33);
/* 116 */     XML11CHARS['·'] = -87;
/* 117 */     Arrays.fill(XML11CHARS, 184, 192, (byte)33);
/* 118 */     Arrays.fill(XML11CHARS, 192, 215, (byte)-19);
/* 119 */     XML11CHARS['×'] = 33;
/* 120 */     Arrays.fill(XML11CHARS, 216, 247, (byte)-19);
/* 121 */     XML11CHARS['÷'] = 33;
/* 122 */     Arrays.fill(XML11CHARS, 248, 768, (byte)-19);
/* 123 */     Arrays.fill(XML11CHARS, 768, 880, (byte)-87);
/* 124 */     Arrays.fill(XML11CHARS, 880, 894, (byte)-19);
/* 125 */     XML11CHARS[894] = 33;
/* 126 */     Arrays.fill(XML11CHARS, 895, 8192, (byte)-19);
/* 127 */     Arrays.fill(XML11CHARS, 8192, 8204, (byte)33);
/* 128 */     Arrays.fill(XML11CHARS, 8204, 8206, (byte)-19);
/* 129 */     Arrays.fill(XML11CHARS, 8206, 8232, (byte)33);
/* 130 */     XML11CHARS[8232] = 35;
/* 131 */     Arrays.fill(XML11CHARS, 8233, 8255, (byte)33);
/* 132 */     Arrays.fill(XML11CHARS, 8255, 8257, (byte)-87);
/* 133 */     Arrays.fill(XML11CHARS, 8257, 8304, (byte)33);
/* 134 */     Arrays.fill(XML11CHARS, 8304, 8592, (byte)-19);
/* 135 */     Arrays.fill(XML11CHARS, 8592, 11264, (byte)33);
/* 136 */     Arrays.fill(XML11CHARS, 11264, 12272, (byte)-19);
/* 137 */     Arrays.fill(XML11CHARS, 12272, 12289, (byte)33);
/* 138 */     Arrays.fill(XML11CHARS, 12289, 55296, (byte)-19);
/* 139 */     Arrays.fill(XML11CHARS, 57344, 63744, (byte)33);
/* 140 */     Arrays.fill(XML11CHARS, 63744, 64976, (byte)-19);
/* 141 */     Arrays.fill(XML11CHARS, 64976, 65008, (byte)33);
/* 142 */     Arrays.fill(XML11CHARS, 65008, 65534, (byte)-19);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XML11Char
 * JD-Core Version:    0.6.2
 */