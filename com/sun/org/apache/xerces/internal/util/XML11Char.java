/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class XML11Char
/*     */ {
/*  53 */   private static final byte[] XML11CHARS = new byte[65536];
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
/* 161 */     return (c < 65536) && ((XML11CHARS[c] & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Valid(int c)
/*     */   {
/* 175 */     return ((c < 65536) && ((XML11CHARS[c] & 0x1) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Invalid(int c)
/*     */   {
/* 185 */     return !isXML11Valid(c);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidLiteral(int c)
/*     */   {
/* 197 */     return ((c < 65536) && ((XML11CHARS[c] & 0x1) != 0) && ((XML11CHARS[c] & 0x10) == 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Content(int c)
/*     */   {
/* 208 */     return ((c < 65536) && ((XML11CHARS[c] & 0x20) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11InternalEntityContent(int c)
/*     */   {
/* 219 */     return ((c < 65536) && ((XML11CHARS[c] & 0x30) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NameStart(int c)
/*     */   {
/* 231 */     return ((c < 65536) && ((XML11CHARS[c] & 0x4) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11Name(int c)
/*     */   {
/* 243 */     return ((c < 65536) && ((XML11CHARS[c] & 0x8) != 0)) || ((c >= 65536) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NCNameStart(int c)
/*     */   {
/* 255 */     return ((c < 65536) && ((XML11CHARS[c] & 0x40) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NCName(int c)
/*     */   {
/* 267 */     return ((c < 65536) && ((XML11CHARS[c] & 0x80) != 0)) || ((65536 <= c) && (c < 983040));
/*     */   }
/*     */ 
/*     */   public static boolean isXML11NameHighSurrogate(int c)
/*     */   {
/* 280 */     return (55296 <= c) && (c <= 56191);
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidName(String name)
/*     */   {
/* 294 */     int length = name.length();
/* 295 */     if (length == 0) {
/* 296 */       return false;
/*     */     }
/* 298 */     int i = 1;
/* 299 */     char ch = name.charAt(0);
/* 300 */     if (!isXML11NameStart(ch)) {
/* 301 */       if ((length > 1) && (isXML11NameHighSurrogate(ch))) {
/* 302 */         char ch2 = name.charAt(1);
/* 303 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NameStart(XMLChar.supplemental(ch, ch2))))
/*     */         {
/* 305 */           return false;
/*     */         }
/* 307 */         i = 2;
/*     */       }
/*     */       else {
/* 310 */         return false;
/*     */       }
/*     */     }
/* 313 */     while (i < length) {
/* 314 */       ch = name.charAt(i);
/* 315 */       if (!isXML11Name(ch)) {
/* 316 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 317 */           char ch2 = name.charAt(i);
/* 318 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11Name(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 320 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 324 */           return false;
/*     */         }
/*     */       }
/* 327 */       i++;
/*     */     }
/* 329 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidNCName(String ncName)
/*     */   {
/* 344 */     int length = ncName.length();
/* 345 */     if (length == 0) {
/* 346 */       return false;
/*     */     }
/* 348 */     int i = 1;
/* 349 */     char ch = ncName.charAt(0);
/* 350 */     if (!isXML11NCNameStart(ch)) {
/* 351 */       if ((length > 1) && (isXML11NameHighSurrogate(ch))) {
/* 352 */         char ch2 = ncName.charAt(1);
/* 353 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NCNameStart(XMLChar.supplemental(ch, ch2))))
/*     */         {
/* 355 */           return false;
/*     */         }
/* 357 */         i = 2;
/*     */       }
/*     */       else {
/* 360 */         return false;
/*     */       }
/*     */     }
/* 363 */     while (i < length) {
/* 364 */       ch = ncName.charAt(i);
/* 365 */       if (!isXML11NCName(ch)) {
/* 366 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 367 */           char ch2 = ncName.charAt(i);
/* 368 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11NCName(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 370 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 374 */           return false;
/*     */         }
/*     */       }
/* 377 */       i++;
/*     */     }
/* 379 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isXML11ValidNmtoken(String nmtoken)
/*     */   {
/* 393 */     int length = nmtoken.length();
/* 394 */     if (length == 0) {
/* 395 */       return false;
/*     */     }
/* 397 */     for (int i = 0; i < length; i++) {
/* 398 */       char ch = nmtoken.charAt(i);
/* 399 */       if (!isXML11Name(ch)) {
/* 400 */         i++; if ((i < length) && (isXML11NameHighSurrogate(ch))) {
/* 401 */           char ch2 = nmtoken.charAt(i);
/* 402 */           if ((!XMLChar.isLowSurrogate(ch2)) || (!isXML11Name(XMLChar.supplemental(ch, ch2))))
/*     */           {
/* 404 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 408 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  91 */     Arrays.fill(XML11CHARS, 1, 9, (byte)17);
/*  92 */     XML11CHARS[9] = 35;
/*  93 */     XML11CHARS[10] = 3;
/*  94 */     Arrays.fill(XML11CHARS, 11, 13, (byte)17);
/*  95 */     XML11CHARS[13] = 3;
/*  96 */     Arrays.fill(XML11CHARS, 14, 32, (byte)17);
/*  97 */     XML11CHARS[32] = 35;
/*  98 */     Arrays.fill(XML11CHARS, 33, 38, (byte)33);
/*  99 */     XML11CHARS[38] = 1;
/* 100 */     Arrays.fill(XML11CHARS, 39, 45, (byte)33);
/* 101 */     Arrays.fill(XML11CHARS, 45, 47, (byte)-87);
/* 102 */     XML11CHARS[47] = 33;
/* 103 */     Arrays.fill(XML11CHARS, 48, 58, (byte)-87);
/* 104 */     XML11CHARS[58] = 45;
/* 105 */     XML11CHARS[59] = 33;
/* 106 */     XML11CHARS[60] = 1;
/* 107 */     Arrays.fill(XML11CHARS, 61, 65, (byte)33);
/* 108 */     Arrays.fill(XML11CHARS, 65, 91, (byte)-19);
/* 109 */     Arrays.fill(XML11CHARS, 91, 93, (byte)33);
/* 110 */     XML11CHARS[93] = 1;
/* 111 */     XML11CHARS[94] = 33;
/* 112 */     XML11CHARS[95] = -19;
/* 113 */     XML11CHARS[96] = 33;
/* 114 */     Arrays.fill(XML11CHARS, 97, 123, (byte)-19);
/* 115 */     Arrays.fill(XML11CHARS, 123, 127, (byte)33);
/* 116 */     Arrays.fill(XML11CHARS, 127, 133, (byte)17);
/* 117 */     XML11CHARS[''] = 35;
/* 118 */     Arrays.fill(XML11CHARS, 134, 160, (byte)17);
/* 119 */     Arrays.fill(XML11CHARS, 160, 183, (byte)33);
/* 120 */     XML11CHARS['·'] = -87;
/* 121 */     Arrays.fill(XML11CHARS, 184, 192, (byte)33);
/* 122 */     Arrays.fill(XML11CHARS, 192, 215, (byte)-19);
/* 123 */     XML11CHARS['×'] = 33;
/* 124 */     Arrays.fill(XML11CHARS, 216, 247, (byte)-19);
/* 125 */     XML11CHARS['÷'] = 33;
/* 126 */     Arrays.fill(XML11CHARS, 248, 768, (byte)-19);
/* 127 */     Arrays.fill(XML11CHARS, 768, 880, (byte)-87);
/* 128 */     Arrays.fill(XML11CHARS, 880, 894, (byte)-19);
/* 129 */     XML11CHARS[894] = 33;
/* 130 */     Arrays.fill(XML11CHARS, 895, 8192, (byte)-19);
/* 131 */     Arrays.fill(XML11CHARS, 8192, 8204, (byte)33);
/* 132 */     Arrays.fill(XML11CHARS, 8204, 8206, (byte)-19);
/* 133 */     Arrays.fill(XML11CHARS, 8206, 8232, (byte)33);
/* 134 */     XML11CHARS[8232] = 35;
/* 135 */     Arrays.fill(XML11CHARS, 8233, 8255, (byte)33);
/* 136 */     Arrays.fill(XML11CHARS, 8255, 8257, (byte)-87);
/* 137 */     Arrays.fill(XML11CHARS, 8257, 8304, (byte)33);
/* 138 */     Arrays.fill(XML11CHARS, 8304, 8592, (byte)-19);
/* 139 */     Arrays.fill(XML11CHARS, 8592, 11264, (byte)33);
/* 140 */     Arrays.fill(XML11CHARS, 11264, 12272, (byte)-19);
/* 141 */     Arrays.fill(XML11CHARS, 12272, 12289, (byte)33);
/* 142 */     Arrays.fill(XML11CHARS, 12289, 55296, (byte)-19);
/* 143 */     Arrays.fill(XML11CHARS, 57344, 63744, (byte)33);
/* 144 */     Arrays.fill(XML11CHARS, 63744, 64976, (byte)-19);
/* 145 */     Arrays.fill(XML11CHARS, 64976, 65008, (byte)33);
/* 146 */     Arrays.fill(XML11CHARS, 65008, 65534, (byte)-19);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XML11Char
 * JD-Core Version:    0.6.2
 */