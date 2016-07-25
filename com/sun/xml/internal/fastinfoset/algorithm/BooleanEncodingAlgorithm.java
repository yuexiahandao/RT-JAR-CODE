/*     */ package com.sun.xml.internal.fastinfoset.algorithm;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class BooleanEncodingAlgorithm extends BuiltInEncodingAlgorithm
/*     */ {
/*  54 */   private static final int[] BIT_TABLE = { 128, 64, 32, 16, 8, 4, 2, 1 };
/*     */ 
/*     */   public int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  66 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/*  70 */     if (primitiveLength < 5) {
/*  71 */       return 1;
/*     */     }
/*  73 */     int div = primitiveLength / 8;
/*  74 */     return div == 0 ? 2 : 1 + div;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException
/*     */   {
/*  79 */     int blength = getPrimtiveLengthFromOctetLength(length, b[start]);
/*  80 */     boolean[] data = new boolean[blength];
/*     */ 
/*  82 */     decodeFromBytesToBooleanArray(data, 0, blength, b, start, length);
/*  83 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  87 */     List booleanList = new ArrayList();
/*     */ 
/*  89 */     int value = s.read();
/*  90 */     if (value == -1) {
/*  91 */       throw new EOFException();
/*     */     }
/*  93 */     int unusedBits = value >> 4 & 0xFF;
/*     */ 
/*  95 */     int bitPosition = 4;
/*  96 */     int bitPositionEnd = 8;
/*  97 */     int valueNext = 0;
/*     */     do {
/*  99 */       valueNext = s.read();
/* 100 */       if (valueNext == -1) {
/* 101 */         bitPositionEnd -= unusedBits;
/*     */       }
/*     */ 
/* 104 */       while (bitPosition < bitPositionEnd) {
/* 105 */         booleanList.add(Boolean.valueOf((value & BIT_TABLE[(bitPosition++)]) > 0));
/*     */       }
/*     */ 
/* 109 */       value = valueNext;
/* 110 */     }while (value != -1);
/*     */ 
/* 112 */     return generateArrayFromList(booleanList);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
/* 116 */     if (!(data instanceof boolean[])) {
/* 117 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
/*     */     }
/*     */ 
/* 120 */     boolean[] array = (boolean[])data;
/* 121 */     int alength = array.length;
/*     */ 
/* 123 */     int mod = (alength + 4) % 8;
/* 124 */     int unusedBits = mod == 0 ? 0 : 8 - mod;
/*     */ 
/* 126 */     int bitPosition = 4;
/* 127 */     int value = unusedBits << 4;
/* 128 */     int astart = 0;
/* 129 */     while (astart < alength) {
/* 130 */       if (array[(astart++)] != 0) {
/* 131 */         value |= BIT_TABLE[bitPosition];
/*     */       }
/*     */ 
/* 134 */       bitPosition++; if (bitPosition == 8) {
/* 135 */         s.write(value);
/* 136 */         bitPosition = value = 0;
/*     */       }
/*     */     }
/*     */ 
/* 140 */     if (bitPosition != 8)
/* 141 */       s.write(value);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length)
/*     */   {
/* 146 */     if (length == 0) {
/* 147 */       return new boolean[0];
/*     */     }
/*     */ 
/* 150 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/* 151 */     final List booleanList = new ArrayList();
/*     */ 
/* 153 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/* 156 */         if (cb.charAt(start) == 't')
/* 157 */           booleanList.add(Boolean.TRUE);
/*     */         else
/* 159 */           booleanList.add(Boolean.FALSE);
/*     */       }
/*     */     });
/* 165 */     return generateArrayFromList(booleanList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/* 169 */     if (data == null) {
/* 170 */       return;
/*     */     }
/*     */ 
/* 173 */     boolean[] value = (boolean[])data;
/* 174 */     if (value.length == 0) {
/* 175 */       return;
/*     */     }
/*     */ 
/* 179 */     s.ensureCapacity(value.length * 5);
/*     */ 
/* 181 */     int end = value.length - 1;
/* 182 */     for (int i = 0; i <= end; i++) {
/* 183 */       if (value[i] != 0)
/* 184 */         s.append("true");
/*     */       else {
/* 186 */         s.append("false");
/*     */       }
/* 188 */       if (i != end)
/* 189 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPrimtiveLengthFromOctetLength(int octetLength, int firstOctet) throws EncodingAlgorithmException
/*     */   {
/* 195 */     int unusedBits = firstOctet >> 4 & 0xFF;
/* 196 */     if (octetLength == 1) {
/* 197 */       if (unusedBits > 3) {
/* 198 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits4"));
/*     */       }
/* 200 */       return 4 - unusedBits;
/*     */     }
/* 202 */     if (unusedBits > 7) {
/* 203 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits8"));
/*     */     }
/* 205 */     return octetLength * 8 - 4 - unusedBits;
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToBooleanArray(boolean[] bdata, int bstart, int blength, byte[] b, int start, int length)
/*     */   {
/* 210 */     int value = b[(start++)] & 0xFF;
/* 211 */     int bitPosition = 4;
/* 212 */     int bend = bstart + blength;
/* 213 */     while (bstart < bend) {
/* 214 */       if (bitPosition == 8) {
/* 215 */         value = b[(start++)] & 0xFF;
/* 216 */         bitPosition = 0;
/*     */       }
/*     */ 
/* 219 */       bdata[(bstart++)] = ((value & BIT_TABLE[(bitPosition++)]) > 0 ? 1 : false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 224 */     if (!(array instanceof boolean[])) {
/* 225 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
/*     */     }
/*     */ 
/* 228 */     encodeToBytesFromBooleanArray((boolean[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public void encodeToBytesFromBooleanArray(boolean[] array, int astart, int alength, byte[] b, int start) {
/* 232 */     int mod = (alength + 4) % 8;
/* 233 */     int unusedBits = mod == 0 ? 0 : 8 - mod;
/*     */ 
/* 235 */     int bitPosition = 4;
/* 236 */     int value = unusedBits << 4;
/* 237 */     int aend = astart + alength;
/* 238 */     while (astart < aend) {
/* 239 */       if (array[(astart++)] != 0) {
/* 240 */         value |= BIT_TABLE[bitPosition];
/*     */       }
/*     */ 
/* 243 */       bitPosition++; if (bitPosition == 8) {
/* 244 */         b[(start++)] = ((byte)value);
/* 245 */         bitPosition = value = 0;
/*     */       }
/*     */     }
/*     */ 
/* 249 */     if (bitPosition > 0)
/* 250 */       b[start] = ((byte)value);
/*     */   }
/*     */ 
/*     */   private final boolean[] generateArrayFromList(List array)
/*     */   {
/* 263 */     boolean[] bdata = new boolean[array.size()];
/* 264 */     for (int i = 0; i < bdata.length; i++) {
/* 265 */       bdata[i] = ((Boolean)array.get(i)).booleanValue();
/*     */     }
/*     */ 
/* 268 */     return bdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */