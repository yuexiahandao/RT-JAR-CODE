/*     */ package com.sun.xml.internal.fastinfoset.algorithm;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class BASE64EncodingAlgorithm extends BuiltInEncodingAlgorithm
/*     */ {
/*  38 */   static final char[] encodeBase64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*  46 */   static final int[] decodeBase64 = { 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length)
/*     */     throws EncodingAlgorithmException
/*     */   {
/* 117 */     byte[] data = new byte[length];
/* 118 */     System.arraycopy(b, start, data, 0, length);
/* 119 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/* 123 */     throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/* 128 */     if (!(data instanceof byte[])) {
/* 129 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotByteArray"));
/*     */     }
/*     */ 
/* 132 */     s.write((byte[])data);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length) {
/* 136 */     if (length == 0) {
/* 137 */       return new byte[0];
/*     */     }
/*     */ 
/* 140 */     StringBuffer encodedValue = removeWhitespace(ch, start, length);
/* 141 */     int encodedLength = encodedValue.length();
/* 142 */     if (encodedLength == 0) {
/* 143 */       return new byte[0];
/*     */     }
/*     */ 
/* 146 */     int blockCount = encodedLength / 4;
/* 147 */     int partialBlockLength = 3;
/*     */ 
/* 149 */     if (encodedValue.charAt(encodedLength - 1) == '=') {
/* 150 */       partialBlockLength--;
/* 151 */       if (encodedValue.charAt(encodedLength - 2) == '=') {
/* 152 */         partialBlockLength--;
/*     */       }
/*     */     }
/*     */ 
/* 156 */     int valueLength = (blockCount - 1) * 3 + partialBlockLength;
/* 157 */     byte[] value = new byte[valueLength];
/*     */ 
/* 159 */     int idx = 0;
/* 160 */     int encodedIdx = 0;
/* 161 */     for (int i = 0; i < blockCount; i++) {
/* 162 */       int x1 = decodeBase64[(encodedValue.charAt(encodedIdx++) - '+')];
/* 163 */       int x2 = decodeBase64[(encodedValue.charAt(encodedIdx++) - '+')];
/* 164 */       int x3 = decodeBase64[(encodedValue.charAt(encodedIdx++) - '+')];
/* 165 */       int x4 = decodeBase64[(encodedValue.charAt(encodedIdx++) - '+')];
/*     */ 
/* 167 */       value[(idx++)] = ((byte)(x1 << 2 | x2 >> 4));
/* 168 */       if (idx < valueLength) {
/* 169 */         value[(idx++)] = ((byte)((x2 & 0xF) << 4 | x3 >> 2));
/*     */       }
/* 171 */       if (idx < valueLength) {
/* 172 */         value[(idx++)] = ((byte)((x3 & 0x3) << 6 | x4));
/*     */       }
/*     */     }
/*     */ 
/* 176 */     return value;
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/* 180 */     if (data == null) {
/* 181 */       return;
/*     */     }
/* 183 */     byte[] value = (byte[])data;
/*     */ 
/* 185 */     convertToCharacters(value, 0, value.length, s);
/*     */   }
/*     */ 
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
/* 189 */     return octetLength;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/* 193 */     return primitiveLength;
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 197 */     System.arraycopy((byte[])array, astart, b, start, alength);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(byte[] data, int offset, int length, StringBuffer s) {
/* 201 */     if (data == null) {
/* 202 */       return;
/*     */     }
/* 204 */     byte[] value = data;
/* 205 */     if (length == 0) {
/* 206 */       return;
/*     */     }
/*     */ 
/* 209 */     int partialBlockLength = length % 3;
/* 210 */     int blockCount = partialBlockLength != 0 ? length / 3 + 1 : length / 3;
/*     */ 
/* 214 */     int encodedLength = blockCount * 4;
/* 215 */     int originalBufferSize = s.length();
/* 216 */     s.ensureCapacity(encodedLength + originalBufferSize);
/*     */ 
/* 218 */     int idx = offset;
/* 219 */     int lastIdx = offset + length;
/* 220 */     for (int i = 0; i < blockCount; i++) {
/* 221 */       int b1 = value[(idx++)] & 0xFF;
/* 222 */       int b2 = idx < lastIdx ? value[(idx++)] & 0xFF : 0;
/* 223 */       int b3 = idx < lastIdx ? value[(idx++)] & 0xFF : 0;
/*     */ 
/* 225 */       s.append(encodeBase64[(b1 >> 2)]);
/*     */ 
/* 227 */       s.append(encodeBase64[((b1 & 0x3) << 4 | b2 >> 4)]);
/*     */ 
/* 229 */       s.append(encodeBase64[((b2 & 0xF) << 2 | b3 >> 6)]);
/*     */ 
/* 231 */       s.append(encodeBase64[(b3 & 0x3F)]);
/*     */     }
/*     */ 
/* 234 */     switch (partialBlockLength) {
/*     */     case 1:
/* 236 */       s.setCharAt(originalBufferSize + encodedLength - 1, '=');
/* 237 */       s.setCharAt(originalBufferSize + encodedLength - 2, '=');
/* 238 */       break;
/*     */     case 2:
/* 240 */       s.setCharAt(originalBufferSize + encodedLength - 1, '=');
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.BASE64EncodingAlgorithm
 * JD-Core Version:    0.6.2
 */