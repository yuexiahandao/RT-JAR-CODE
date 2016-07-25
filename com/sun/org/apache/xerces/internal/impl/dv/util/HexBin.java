/*     */ package com.sun.org.apache.xerces.internal.impl.dv.util;
/*     */ 
/*     */ public final class HexBin
/*     */ {
/*     */   private static final int BASELENGTH = 128;
/*     */   private static final int LOOKUPLENGTH = 16;
/*  35 */   private static final byte[] hexNumberTable = new byte[''];
/*  36 */   private static final char[] lookUpHexAlphabet = new char[16];
/*     */ 
/*     */   public static String encode(byte[] binaryData)
/*     */   {
/*  68 */     if (binaryData == null)
/*  69 */       return null;
/*  70 */     int lengthData = binaryData.length;
/*  71 */     int lengthEncode = lengthData * 2;
/*  72 */     char[] encodedData = new char[lengthEncode];
/*     */ 
/*  74 */     for (int i = 0; i < lengthData; i++) {
/*  75 */       int temp = binaryData[i];
/*  76 */       if (temp < 0)
/*  77 */         temp += 256;
/*  78 */       encodedData[(i * 2)] = lookUpHexAlphabet[(temp >> 4)];
/*  79 */       encodedData[(i * 2 + 1)] = lookUpHexAlphabet[(temp & 0xF)];
/*     */     }
/*  81 */     return new String(encodedData);
/*     */   }
/*     */ 
/*     */   public static byte[] decode(String encoded)
/*     */   {
/*  91 */     if (encoded == null)
/*  92 */       return null;
/*  93 */     int lengthData = encoded.length();
/*  94 */     if (lengthData % 2 != 0) {
/*  95 */       return null;
/*     */     }
/*  97 */     char[] binaryData = encoded.toCharArray();
/*  98 */     int lengthDecode = lengthData / 2;
/*  99 */     byte[] decodedData = new byte[lengthDecode];
/*     */ 
/* 102 */     for (int i = 0; i < lengthDecode; i++) {
/* 103 */       char tempChar = binaryData[(i * 2)];
/* 104 */       byte temp1 = tempChar < '' ? hexNumberTable[tempChar] : -1;
/* 105 */       if (temp1 == -1)
/* 106 */         return null;
/* 107 */       tempChar = binaryData[(i * 2 + 1)];
/* 108 */       byte temp2 = tempChar < '' ? hexNumberTable[tempChar] : -1;
/* 109 */       if (temp2 == -1)
/* 110 */         return null;
/* 111 */       decodedData[i] = ((byte)(temp1 << 4 | temp2));
/*     */     }
/* 113 */     return decodedData;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  40 */     for (int i = 0; i < 128; i++) {
/*  41 */       hexNumberTable[i] = -1;
/*     */     }
/*  43 */     for (int i = 57; i >= 48; i--) {
/*  44 */       hexNumberTable[i] = ((byte)(i - 48));
/*     */     }
/*  46 */     for (int i = 70; i >= 65; i--) {
/*  47 */       hexNumberTable[i] = ((byte)(i - 65 + 10));
/*     */     }
/*  49 */     for (int i = 102; i >= 97; i--) {
/*  50 */       hexNumberTable[i] = ((byte)(i - 97 + 10));
/*     */     }
/*     */ 
/*  53 */     for (int i = 0; i < 10; i++) {
/*  54 */       lookUpHexAlphabet[i] = ((char)(48 + i));
/*     */     }
/*  56 */     for (int i = 10; i <= 15; i++)
/*  57 */       lookUpHexAlphabet[i] = ((char)(65 + i - 10));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.util.HexBin
 * JD-Core Version:    0.6.2
 */