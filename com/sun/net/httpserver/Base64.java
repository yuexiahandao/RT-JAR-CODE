/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ class Base64
/*     */ {
/* 170 */   private static final char[] intToBase64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/* 185 */   private static final char[] intToAltBase64 = { '!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?' };
/*     */ 
/* 284 */   private static final byte[] base64ToInt = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/* 298 */   private static final byte[] altBase64ToInt = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };
/*     */ 
/*     */   static String byteArrayToBase64(byte[] paramArrayOfByte)
/*     */   {
/* 112 */     return byteArrayToBase64(paramArrayOfByte, false);
/*     */   }
/*     */ 
/*     */   static String byteArrayToAltBase64(byte[] paramArrayOfByte)
/*     */   {
/* 122 */     return byteArrayToBase64(paramArrayOfByte, true);
/*     */   }
/*     */ 
/*     */   private static String byteArrayToBase64(byte[] paramArrayOfByte, boolean paramBoolean) {
/* 126 */     int i = paramArrayOfByte.length;
/* 127 */     int j = i / 3;
/* 128 */     int k = i - 3 * j;
/* 129 */     int m = 4 * ((i + 2) / 3);
/* 130 */     StringBuffer localStringBuffer = new StringBuffer(m);
/* 131 */     char[] arrayOfChar = paramBoolean ? intToAltBase64 : intToBase64;
/*     */ 
/* 134 */     int n = 0;
/*     */     int i2;
/* 135 */     for (int i1 = 0; i1 < j; i1++) {
/* 136 */       i2 = paramArrayOfByte[(n++)] & 0xFF;
/* 137 */       int i3 = paramArrayOfByte[(n++)] & 0xFF;
/* 138 */       int i4 = paramArrayOfByte[(n++)] & 0xFF;
/* 139 */       localStringBuffer.append(arrayOfChar[(i2 >> 2)]);
/* 140 */       localStringBuffer.append(arrayOfChar[(i2 << 4 & 0x3F | i3 >> 4)]);
/* 141 */       localStringBuffer.append(arrayOfChar[(i3 << 2 & 0x3F | i4 >> 6)]);
/* 142 */       localStringBuffer.append(arrayOfChar[(i4 & 0x3F)]);
/*     */     }
/*     */ 
/* 146 */     if (k != 0) {
/* 147 */       i1 = paramArrayOfByte[(n++)] & 0xFF;
/* 148 */       localStringBuffer.append(arrayOfChar[(i1 >> 2)]);
/* 149 */       if (k == 1) {
/* 150 */         localStringBuffer.append(arrayOfChar[(i1 << 4 & 0x3F)]);
/* 151 */         localStringBuffer.append("==");
/*     */       }
/*     */       else {
/* 154 */         i2 = paramArrayOfByte[(n++)] & 0xFF;
/* 155 */         localStringBuffer.append(arrayOfChar[(i1 << 4 & 0x3F | i2 >> 4)]);
/* 156 */         localStringBuffer.append(arrayOfChar[(i2 << 2 & 0x3F)]);
/* 157 */         localStringBuffer.append('=');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 162 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static byte[] base64ToByteArray(String paramString)
/*     */   {
/* 201 */     return base64ToByteArray(paramString, false);
/*     */   }
/*     */ 
/*     */   static byte[] altBase64ToByteArray(String paramString)
/*     */   {
/* 213 */     return base64ToByteArray(paramString, true);
/*     */   }
/*     */ 
/*     */   private static byte[] base64ToByteArray(String paramString, boolean paramBoolean) {
/* 217 */     byte[] arrayOfByte1 = paramBoolean ? altBase64ToInt : base64ToInt;
/* 218 */     int i = paramString.length();
/* 219 */     int j = i / 4;
/* 220 */     if (4 * j != i) {
/* 221 */       throw new IllegalArgumentException("String length must be a multiple of four.");
/*     */     }
/* 223 */     int k = 0;
/* 224 */     int m = j;
/* 225 */     if (i != 0) {
/* 226 */       if (paramString.charAt(i - 1) == '=') {
/* 227 */         k++;
/* 228 */         m--;
/*     */       }
/* 230 */       if (paramString.charAt(i - 2) == '=')
/* 231 */         k++;
/*     */     }
/* 233 */     byte[] arrayOfByte2 = new byte[3 * j - k];
/*     */ 
/* 236 */     int n = 0; int i1 = 0;
/*     */     int i3;
/*     */     int i4;
/* 237 */     for (int i2 = 0; i2 < m; i2++) {
/* 238 */       i3 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 239 */       i4 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 240 */       int i5 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 241 */       int i6 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 242 */       arrayOfByte2[(i1++)] = ((byte)(i3 << 2 | i4 >> 4));
/* 243 */       arrayOfByte2[(i1++)] = ((byte)(i4 << 4 | i5 >> 2));
/* 244 */       arrayOfByte2[(i1++)] = ((byte)(i5 << 6 | i6));
/*     */     }
/*     */ 
/* 248 */     if (k != 0) {
/* 249 */       i2 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 250 */       i3 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 251 */       arrayOfByte2[(i1++)] = ((byte)(i2 << 2 | i3 >> 4));
/*     */ 
/* 253 */       if (k == 1) {
/* 254 */         i4 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 255 */         arrayOfByte2[(i1++)] = ((byte)(i3 << 4 | i4 >> 2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 260 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private static int base64toInt(char paramChar, byte[] paramArrayOfByte)
/*     */   {
/* 271 */     int i = paramArrayOfByte[paramChar];
/* 272 */     if (i < 0)
/* 273 */       throw new IllegalArgumentException("Illegal character " + paramChar);
/* 274 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.Base64
 * JD-Core Version:    0.6.2
 */