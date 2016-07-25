/*     */ package com.sun.xml.internal.messaging.saaj.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class Base64
/*     */ {
/*     */   private static final int BASELENGTH = 255;
/*     */   private static final int LOOKUPLENGTH = 63;
/*     */   private static final int TWENTYFOURBITGROUP = 24;
/*     */   private static final int EIGHTBIT = 8;
/*     */   private static final int SIXTEENBIT = 16;
/*     */   private static final int SIXBIT = 6;
/*     */   private static final int FOURBYTE = 4;
/*     */   private static final byte PAD = 61;
/*  55 */   private static byte[] base64Alphabet = new byte['ÿ'];
/*  56 */   private static byte[] lookUpBase64Alphabet = new byte[63];
/*     */ 
/* 226 */   static final int[] base64 = { 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 62, 64, 64, 64, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 64, 64, 64, 64, 64, 64, 64, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 64, 64, 64, 64, 64, 64, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64 };
/*     */ 
/*     */   static boolean isBase64(byte octect)
/*     */   {
/*  91 */     return (octect == 61) || (base64Alphabet[octect] != -1);
/*     */   }
/*     */ 
/*     */   static boolean isArrayByteBase64(byte[] arrayOctect)
/*     */   {
/*  96 */     int length = arrayOctect.length;
/*  97 */     if (length == 0)
/*  98 */       return false;
/*  99 */     for (int i = 0; i < length; i++) {
/* 100 */       if (!isBase64(arrayOctect[i]))
/* 101 */         return false;
/*     */     }
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   public static byte[] encode(byte[] binaryData)
/*     */   {
/* 113 */     int lengthDataBits = binaryData.length * 8;
/* 114 */     int fewerThan24bits = lengthDataBits % 24;
/* 115 */     int numberTriplets = lengthDataBits / 24;
/* 116 */     byte[] encodedData = null;
/*     */ 
/* 119 */     if (fewerThan24bits != 0)
/* 120 */       encodedData = new byte[(numberTriplets + 1) * 4];
/*     */     else {
/* 122 */       encodedData = new byte[numberTriplets * 4];
/*     */     }
/* 124 */     byte k = 0; byte l = 0; byte b1 = 0; byte b2 = 0; byte b3 = 0;
/*     */ 
/* 126 */     int encodedIndex = 0;
/* 127 */     int dataIndex = 0;
/* 128 */     int i = 0;
/* 129 */     for (i = 0; i < numberTriplets; i++)
/*     */     {
/* 131 */       dataIndex = i * 3;
/* 132 */       b1 = binaryData[dataIndex];
/* 133 */       b2 = binaryData[(dataIndex + 1)];
/* 134 */       b3 = binaryData[(dataIndex + 2)];
/*     */ 
/* 136 */       l = (byte)(b2 & 0xF);
/* 137 */       k = (byte)(b1 & 0x3);
/*     */ 
/* 139 */       encodedIndex = i * 4;
/* 140 */       encodedData[encodedIndex] = lookUpBase64Alphabet[(b1 >> 2)];
/* 141 */       encodedData[(encodedIndex + 1)] = lookUpBase64Alphabet[(b2 >> 4 | k << 4)];
/*     */ 
/* 143 */       encodedData[(encodedIndex + 2)] = lookUpBase64Alphabet[(l << 2 | b3 >> 6)];
/*     */ 
/* 145 */       encodedData[(encodedIndex + 3)] = lookUpBase64Alphabet[(b3 & 0x3F)];
/*     */     }
/*     */ 
/* 149 */     dataIndex = i * 3;
/* 150 */     encodedIndex = i * 4;
/* 151 */     if (fewerThan24bits == 8) {
/* 152 */       b1 = binaryData[dataIndex];
/* 153 */       k = (byte)(b1 & 0x3);
/* 154 */       encodedData[encodedIndex] = lookUpBase64Alphabet[(b1 >> 2)];
/* 155 */       encodedData[(encodedIndex + 1)] = lookUpBase64Alphabet[(k << 4)];
/* 156 */       encodedData[(encodedIndex + 2)] = 61;
/* 157 */       encodedData[(encodedIndex + 3)] = 61;
/* 158 */     } else if (fewerThan24bits == 16)
/*     */     {
/* 160 */       b1 = binaryData[dataIndex];
/* 161 */       b2 = binaryData[(dataIndex + 1)];
/* 162 */       l = (byte)(b2 & 0xF);
/* 163 */       k = (byte)(b1 & 0x3);
/* 164 */       encodedData[encodedIndex] = lookUpBase64Alphabet[(b1 >> 2)];
/* 165 */       encodedData[(encodedIndex + 1)] = lookUpBase64Alphabet[(b2 >> 4 | k << 4)];
/*     */ 
/* 167 */       encodedData[(encodedIndex + 2)] = lookUpBase64Alphabet[(l << 2)];
/* 168 */       encodedData[(encodedIndex + 3)] = 61;
/*     */     }
/* 170 */     return encodedData;
/*     */   }
/*     */ 
/*     */   public byte[] decode(byte[] base64Data)
/*     */   {
/* 181 */     int numberQuadruple = base64Data.length / 4;
/* 182 */     byte[] decodedData = null;
/* 183 */     byte b1 = 0; byte b2 = 0; byte b3 = 0; byte b4 = 0; byte marker0 = 0; byte marker1 = 0;
/*     */ 
/* 188 */     int encodedIndex = 0;
/* 189 */     int dataIndex = 0;
/* 190 */     decodedData = new byte[numberQuadruple * 3 + 1];
/*     */ 
/* 192 */     for (int i = 0; i < numberQuadruple; i++) {
/* 193 */       dataIndex = i * 4;
/* 194 */       marker0 = base64Data[(dataIndex + 2)];
/* 195 */       marker1 = base64Data[(dataIndex + 3)];
/*     */ 
/* 197 */       b1 = base64Alphabet[base64Data[dataIndex]];
/* 198 */       b2 = base64Alphabet[base64Data[(dataIndex + 1)]];
/*     */ 
/* 200 */       if ((marker0 != 61) && (marker1 != 61)) {
/* 201 */         b3 = base64Alphabet[marker0];
/* 202 */         b4 = base64Alphabet[marker1];
/*     */ 
/* 204 */         decodedData[encodedIndex] = ((byte)(b1 << 2 | b2 >> 4));
/* 205 */         decodedData[(encodedIndex + 1)] = ((byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF));
/*     */ 
/* 207 */         decodedData[(encodedIndex + 2)] = ((byte)(b3 << 6 | b4));
/* 208 */       } else if (marker0 == 61) {
/* 209 */         decodedData[encodedIndex] = ((byte)(b1 << 2 | b2 >> 4));
/* 210 */         decodedData[(encodedIndex + 1)] = ((byte)((b2 & 0xF) << 4));
/* 211 */         decodedData[(encodedIndex + 2)] = 0;
/* 212 */       } else if (marker1 == 61) {
/* 213 */         b3 = base64Alphabet[marker0];
/*     */ 
/* 215 */         decodedData[encodedIndex] = ((byte)(b1 << 2 | b2 >> 4));
/* 216 */         decodedData[(encodedIndex + 1)] = ((byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF));
/*     */ 
/* 218 */         decodedData[(encodedIndex + 2)] = ((byte)(b3 << 6));
/*     */       }
/* 220 */       encodedIndex += 3;
/*     */     }
/* 222 */     return decodedData;
/*     */   }
/*     */ 
/*     */   public static String base64Decode(String orig)
/*     */   {
/* 246 */     char[] chars = orig.toCharArray();
/* 247 */     StringBuffer sb = new StringBuffer();
/* 248 */     int i = 0;
/*     */ 
/* 250 */     int shift = 0;
/* 251 */     int acc = 0;
/*     */ 
/* 253 */     for (i = 0; i < chars.length; i++) {
/* 254 */       int v = base64[(chars[i] & 0xFF)];
/*     */ 
/* 256 */       if (v >= 64) {
/* 257 */         if (chars[i] != '=')
/* 258 */           System.out.println("Wrong char in base64: " + chars[i]);
/*     */       } else {
/* 260 */         acc = acc << 6 | v;
/* 261 */         shift += 6;
/* 262 */         if (shift >= 8) {
/* 263 */           shift -= 8;
/* 264 */           sb.append((char)(acc >> shift & 0xFF));
/*     */         }
/*     */       }
/*     */     }
/* 268 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  60 */     for (int i = 0; i < 255; i++) {
/*  61 */       base64Alphabet[i] = -1;
/*     */     }
/*  63 */     for (int i = 90; i >= 65; i--) {
/*  64 */       base64Alphabet[i] = ((byte)(i - 65));
/*     */     }
/*  66 */     for (int i = 122; i >= 97; i--) {
/*  67 */       base64Alphabet[i] = ((byte)(i - 97 + 26));
/*     */     }
/*     */ 
/*  70 */     for (int i = 57; i >= 48; i--) {
/*  71 */       base64Alphabet[i] = ((byte)(i - 48 + 52));
/*     */     }
/*     */ 
/*  74 */     base64Alphabet[43] = 62;
/*  75 */     base64Alphabet[47] = 63;
/*     */ 
/*  77 */     for (int i = 0; i <= 25; i++) {
/*  78 */       lookUpBase64Alphabet[i] = ((byte)(65 + i));
/*     */     }
/*  80 */     int i = 26; for (int j = 0; i <= 51; j++) {
/*  81 */       lookUpBase64Alphabet[i] = ((byte)(97 + j));
/*     */ 
/*  80 */       i++;
/*     */     }
/*     */ 
/*  83 */     int i = 52; for (int j = 0; i <= 61; j++) {
/*  84 */       lookUpBase64Alphabet[i] = ((byte)(48 + j));
/*     */ 
/*  83 */       i++;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.Base64
 * JD-Core Version:    0.6.2
 */