/*     */ package com.sun.xml.internal.fastinfoset.algorithm;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class HexadecimalEncodingAlgorithm extends BuiltInEncodingAlgorithm
/*     */ {
/*  37 */   private static final char[] NIBBLE_TO_HEXADECIMAL_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*  41 */   private static final int[] HEXADECIMAL_TO_NIBBLE_TABLE = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15 };
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  68 */     byte[] data = new byte[length];
/*  69 */     System.arraycopy(b, start, data, 0, length);
/*  70 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  74 */     throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  79 */     if (!(data instanceof byte[])) {
/*  80 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotByteArray"));
/*     */     }
/*     */ 
/*  83 */     s.write((byte[])data);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length) {
/*  87 */     if (length == 0) {
/*  88 */       return new byte[0];
/*     */     }
/*     */ 
/*  91 */     StringBuffer encodedValue = removeWhitespace(ch, start, length);
/*  92 */     int encodedLength = encodedValue.length();
/*  93 */     if (encodedLength == 0) {
/*  94 */       return new byte[0];
/*     */     }
/*     */ 
/*  97 */     int valueLength = encodedValue.length() / 2;
/*  98 */     byte[] value = new byte[valueLength];
/*     */ 
/* 100 */     int encodedIdx = 0;
/* 101 */     for (int i = 0; i < valueLength; i++) {
/* 102 */       int nibble1 = HEXADECIMAL_TO_NIBBLE_TABLE[(encodedValue.charAt(encodedIdx++) - '0')];
/* 103 */       int nibble2 = HEXADECIMAL_TO_NIBBLE_TABLE[(encodedValue.charAt(encodedIdx++) - '0')];
/* 104 */       value[i] = ((byte)(nibble1 << 4 | nibble2));
/*     */     }
/*     */ 
/* 107 */     return value;
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/* 111 */     if (data == null) {
/* 112 */       return;
/*     */     }
/* 114 */     byte[] value = (byte[])data;
/* 115 */     if (value.length == 0) {
/* 116 */       return;
/*     */     }
/*     */ 
/* 119 */     s.ensureCapacity(value.length * 2);
/* 120 */     for (int i = 0; i < value.length; i++) {
/* 121 */       s.append(NIBBLE_TO_HEXADECIMAL_TABLE[(value[i] >>> 4 & 0xF)]);
/* 122 */       s.append(NIBBLE_TO_HEXADECIMAL_TABLE[(value[i] & 0xF)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/* 129 */     return octetLength * 2;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/* 133 */     return primitiveLength / 2;
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 137 */     System.arraycopy((byte[])array, astart, b, start, alength);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */