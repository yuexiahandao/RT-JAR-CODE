/*     */ package com.sun.org.apache.xerces.internal.impl.dv.util;
/*     */ 
/*     */ public final class Base64
/*     */ {
/*     */   private static final int BASELENGTH = 128;
/*     */   private static final int LOOKUPLENGTH = 64;
/*     */   private static final int TWENTYFOURBITGROUP = 24;
/*     */   private static final int EIGHTBIT = 8;
/*     */   private static final int SIXTEENBIT = 16;
/*     */   private static final int SIXBIT = 6;
/*     */   private static final int FOURBYTE = 4;
/*     */   private static final int SIGN = -128;
/*     */   private static final char PAD = '=';
/*     */   private static final boolean fDebug = false;
/*  52 */   private static final byte[] base64Alphabet = new byte[''];
/*  53 */   private static final char[] lookUpBase64Alphabet = new char[64];
/*     */ 
/*     */   protected static boolean isWhiteSpace(char octect)
/*     */   {
/*  88 */     return (octect == ' ') || (octect == '\r') || (octect == '\n') || (octect == '\t');
/*     */   }
/*     */ 
/*     */   protected static boolean isPad(char octect) {
/*  92 */     return octect == '=';
/*     */   }
/*     */ 
/*     */   protected static boolean isData(char octect) {
/*  96 */     return (octect < '') && (base64Alphabet[octect] != -1);
/*     */   }
/*     */ 
/*     */   protected static boolean isBase64(char octect) {
/* 100 */     return (isWhiteSpace(octect)) || (isPad(octect)) || (isData(octect));
/*     */   }
/*     */ 
/*     */   public static String encode(byte[] binaryData)
/*     */   {
/* 111 */     if (binaryData == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     int lengthDataBits = binaryData.length * 8;
/* 115 */     if (lengthDataBits == 0) {
/* 116 */       return "";
/*     */     }
/*     */ 
/* 119 */     int fewerThan24bits = lengthDataBits % 24;
/* 120 */     int numberTriplets = lengthDataBits / 24;
/* 121 */     int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
/* 122 */     char[] encodedData = null;
/*     */ 
/* 124 */     encodedData = new char[numberQuartet * 4];
/*     */ 
/* 126 */     byte k = 0; byte l = 0; byte b1 = 0; byte b2 = 0; byte b3 = 0;
/*     */ 
/* 128 */     int encodedIndex = 0;
/* 129 */     int dataIndex = 0;
/*     */ 
/* 134 */     for (int i = 0; i < numberTriplets; i++) {
/* 135 */       b1 = binaryData[(dataIndex++)];
/* 136 */       b2 = binaryData[(dataIndex++)];
/* 137 */       b3 = binaryData[(dataIndex++)];
/*     */ 
/* 143 */       l = (byte)(b2 & 0xF);
/* 144 */       k = (byte)(b1 & 0x3);
/*     */ 
/* 146 */       byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/*     */ 
/* 148 */       byte val2 = (b2 & 0xFFFFFF80) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 0xF0);
/* 149 */       byte val3 = (b3 & 0xFFFFFF80) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 0xFC);
/*     */ 
/* 157 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[val1];
/* 158 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(val2 | k << 4)];
/* 159 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(l << 2 | val3)];
/* 160 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(b3 & 0x3F)];
/*     */     }
/*     */ 
/* 164 */     if (fewerThan24bits == 8) {
/* 165 */       b1 = binaryData[dataIndex];
/* 166 */       k = (byte)(b1 & 0x3);
/*     */ 
/* 171 */       byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/* 172 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[val1];
/* 173 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(k << 4)];
/* 174 */       encodedData[(encodedIndex++)] = '=';
/* 175 */       encodedData[(encodedIndex++)] = '=';
/* 176 */     } else if (fewerThan24bits == 16) {
/* 177 */       b1 = binaryData[dataIndex];
/* 178 */       b2 = binaryData[(dataIndex + 1)];
/* 179 */       l = (byte)(b2 & 0xF);
/* 180 */       k = (byte)(b1 & 0x3);
/*     */ 
/* 182 */       byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/* 183 */       byte val2 = (b2 & 0xFFFFFF80) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 0xF0);
/*     */ 
/* 185 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[val1];
/* 186 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(val2 | k << 4)];
/* 187 */       encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(l << 2)];
/* 188 */       encodedData[(encodedIndex++)] = '=';
/*     */     }
/*     */ 
/* 191 */     return new String(encodedData);
/*     */   }
/*     */ 
/*     */   public static byte[] decode(String encoded)
/*     */   {
/* 202 */     if (encoded == null) {
/* 203 */       return null;
/*     */     }
/* 205 */     char[] base64Data = encoded.toCharArray();
/*     */ 
/* 207 */     int len = removeWhiteSpace(base64Data);
/*     */ 
/* 209 */     if (len % 4 != 0) {
/* 210 */       return null;
/*     */     }
/*     */ 
/* 213 */     int numberQuadruple = len / 4;
/*     */ 
/* 215 */     if (numberQuadruple == 0) {
/* 216 */       return new byte[0];
/*     */     }
/* 218 */     byte[] decodedData = null;
/* 219 */     byte b1 = 0; byte b2 = 0; byte b3 = 0; byte b4 = 0;
/* 220 */     char d1 = '\000'; char d2 = '\000'; char d3 = '\000'; char d4 = '\000';
/*     */ 
/* 222 */     int i = 0;
/* 223 */     int encodedIndex = 0;
/* 224 */     int dataIndex = 0;
/* 225 */     decodedData = new byte[numberQuadruple * 3];
/*     */ 
/* 227 */     for (; i < numberQuadruple - 1; i++)
/*     */     {
/* 229 */       if ((!isData(d1 = base64Data[(dataIndex++)])) || (!isData(d2 = base64Data[(dataIndex++)])) || (!isData(d3 = base64Data[(dataIndex++)])) || (!isData(d4 = base64Data[(dataIndex++)])))
/*     */       {
/* 233 */         return null;
/*     */       }
/* 235 */       b1 = base64Alphabet[d1];
/* 236 */       b2 = base64Alphabet[d2];
/* 237 */       b3 = base64Alphabet[d3];
/* 238 */       b4 = base64Alphabet[d4];
/*     */ 
/* 240 */       decodedData[(encodedIndex++)] = ((byte)(b1 << 2 | b2 >> 4));
/* 241 */       decodedData[(encodedIndex++)] = ((byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF));
/* 242 */       decodedData[(encodedIndex++)] = ((byte)(b3 << 6 | b4));
/*     */     }
/*     */ 
/* 245 */     if ((!isData(d1 = base64Data[(dataIndex++)])) || (!isData(d2 = base64Data[(dataIndex++)])))
/*     */     {
/* 247 */       return null;
/*     */     }
/*     */ 
/* 250 */     b1 = base64Alphabet[d1];
/* 251 */     b2 = base64Alphabet[d2];
/*     */ 
/* 253 */     d3 = base64Data[(dataIndex++)];
/* 254 */     d4 = base64Data[(dataIndex++)];
/* 255 */     if ((!isData(d3)) || (!isData(d4)))
/*     */     {
/* 257 */       if ((isPad(d3)) && (isPad(d4))) {
/* 258 */         if ((b2 & 0xF) != 0)
/* 259 */           return null;
/* 260 */         byte[] tmp = new byte[i * 3 + 1];
/* 261 */         System.arraycopy(decodedData, 0, tmp, 0, i * 3);
/* 262 */         tmp[encodedIndex] = ((byte)(b1 << 2 | b2 >> 4));
/* 263 */         return tmp;
/* 264 */       }if ((!isPad(d3)) && (isPad(d4))) {
/* 265 */         b3 = base64Alphabet[d3];
/* 266 */         if ((b3 & 0x3) != 0)
/* 267 */           return null;
/* 268 */         byte[] tmp = new byte[i * 3 + 2];
/* 269 */         System.arraycopy(decodedData, 0, tmp, 0, i * 3);
/* 270 */         tmp[(encodedIndex++)] = ((byte)(b1 << 2 | b2 >> 4));
/* 271 */         tmp[encodedIndex] = ((byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF));
/* 272 */         return tmp;
/*     */       }
/* 274 */       return null;
/*     */     }
/*     */ 
/* 277 */     b3 = base64Alphabet[d3];
/* 278 */     b4 = base64Alphabet[d4];
/* 279 */     decodedData[(encodedIndex++)] = ((byte)(b1 << 2 | b2 >> 4));
/* 280 */     decodedData[(encodedIndex++)] = ((byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF));
/* 281 */     decodedData[(encodedIndex++)] = ((byte)(b3 << 6 | b4));
/*     */ 
/* 285 */     return decodedData;
/*     */   }
/*     */ 
/*     */   protected static int removeWhiteSpace(char[] data)
/*     */   {
/* 295 */     if (data == null) {
/* 296 */       return 0;
/*     */     }
/*     */ 
/* 299 */     int newSize = 0;
/* 300 */     int len = data.length;
/* 301 */     for (int i = 0; i < len; i++) {
/* 302 */       if (!isWhiteSpace(data[i]))
/* 303 */         data[(newSize++)] = data[i];
/*     */     }
/* 305 */     return newSize;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  57 */     for (int i = 0; i < 128; i++) {
/*  58 */       base64Alphabet[i] = -1;
/*     */     }
/*  60 */     for (int i = 90; i >= 65; i--) {
/*  61 */       base64Alphabet[i] = ((byte)(i - 65));
/*     */     }
/*  63 */     for (int i = 122; i >= 97; i--) {
/*  64 */       base64Alphabet[i] = ((byte)(i - 97 + 26));
/*     */     }
/*     */ 
/*  67 */     for (int i = 57; i >= 48; i--) {
/*  68 */       base64Alphabet[i] = ((byte)(i - 48 + 52));
/*     */     }
/*     */ 
/*  71 */     base64Alphabet[43] = 62;
/*  72 */     base64Alphabet[47] = 63;
/*     */ 
/*  74 */     for (int i = 0; i <= 25; i++) {
/*  75 */       lookUpBase64Alphabet[i] = ((char)(65 + i));
/*     */     }
/*  77 */     int i = 26; for (int j = 0; i <= 51; j++) {
/*  78 */       lookUpBase64Alphabet[i] = ((char)(97 + j));
/*     */ 
/*  77 */       i++;
/*     */     }
/*     */ 
/*  80 */     int i = 52; for (int j = 0; i <= 61; j++) {
/*  81 */       lookUpBase64Alphabet[i] = ((char)(48 + j));
/*     */ 
/*  80 */       i++;
/*     */     }
/*  82 */     lookUpBase64Alphabet[62] = '+';
/*  83 */     lookUpBase64Alphabet[63] = '/';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.util.Base64
 * JD-Core Version:    0.6.2
 */