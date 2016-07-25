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
/*     */ public class LongEncodingAlgorithm extends IntegerEncodingAlgorithm
/*     */ {
/*     */   public int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  44 */     if (octetLength % 8 != 0) {
/*  45 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfLong", new Object[] { Integer.valueOf(8) }));
/*     */     }
/*     */ 
/*  49 */     return octetLength / 8;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/*  53 */     return primitiveLength * 8;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
/*  57 */     long[] data = new long[getPrimtiveLengthFromOctetLength(length)];
/*  58 */     decodeFromBytesToLongArray(data, 0, b, start, length);
/*     */ 
/*  60 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  64 */     return decodeFromInputStreamToIntArray(s);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  69 */     if (!(data instanceof long[])) {
/*  70 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
/*     */     }
/*     */ 
/*  73 */     long[] ldata = (long[])data;
/*     */ 
/*  75 */     encodeToOutputStreamFromLongArray(ldata, s);
/*     */   }
/*     */ 
/*     */   public Object convertFromCharacters(char[] ch, int start, int length)
/*     */   {
/*  80 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  81 */     final List longList = new ArrayList();
/*     */ 
/*  83 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  86 */         String lStringValue = cb.subSequence(start, end).toString();
/*  87 */         longList.add(Long.valueOf(lStringValue));
/*     */       }
/*     */     });
/*  92 */     return generateArrayFromList(longList);
/*     */   }
/*     */ 
/*     */   public void convertToCharacters(Object data, StringBuffer s) {
/*  96 */     if (!(data instanceof long[])) {
/*  97 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
/*     */     }
/*     */ 
/* 100 */     long[] ldata = (long[])data;
/*     */ 
/* 102 */     convertToCharactersFromLongArray(ldata, s);
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToLongArray(long[] ldata, int istart, byte[] b, int start, int length)
/*     */   {
/* 107 */     int size = length / 8;
/* 108 */     for (int i = 0; i < size; i++)
/* 109 */       ldata[(istart++)] = ((b[(start++)] & 0xFF) << 56 | (b[(start++)] & 0xFF) << 48 | (b[(start++)] & 0xFF) << 40 | (b[(start++)] & 0xFF) << 32 | (b[(start++)] & 0xFF) << 24 | (b[(start++)] & 0xFF) << 16 | (b[(start++)] & 0xFF) << 8 | b[(start++)] & 0xFF);
/*     */   }
/*     */ 
/*     */   public final long[] decodeFromInputStreamToIntArray(InputStream s)
/*     */     throws IOException
/*     */   {
/* 122 */     List longList = new ArrayList();
/* 123 */     byte[] b = new byte[8];
/*     */     while (true)
/*     */     {
/* 126 */       int n = s.read(b);
/* 127 */       if (n != 8) {
/* 128 */         if (n == -1)
/*     */         {
/*     */           break;
/*     */         }
/* 132 */         while (n != 8) {
/* 133 */           int m = s.read(b, n, 8 - n);
/* 134 */           if (m == -1) {
/* 135 */             throw new EOFException();
/*     */           }
/* 137 */           n += m;
/*     */         }
/*     */       }
/*     */ 
/* 141 */       long l = (b[0] << 56) + ((b[1] & 0xFF) << 48) + ((b[2] & 0xFF) << 40) + ((b[3] & 0xFF) << 32) + ((b[4] & 0xFF) << 24) + ((b[5] & 0xFF) << 16) + ((b[6] & 0xFF) << 8) + ((b[7] & 0xFF) << 0);
/*     */ 
/* 151 */       longList.add(Long.valueOf(l));
/*     */     }
/*     */ 
/* 154 */     return generateArrayFromList(longList);
/*     */   }
/*     */ 
/*     */   public final void encodeToOutputStreamFromLongArray(long[] ldata, OutputStream s) throws IOException
/*     */   {
/* 159 */     for (int i = 0; i < ldata.length; i++) {
/* 160 */       long bits = ldata[i];
/* 161 */       s.write((int)(bits >>> 56 & 0xFF));
/* 162 */       s.write((int)(bits >>> 48 & 0xFF));
/* 163 */       s.write((int)(bits >>> 40 & 0xFF));
/* 164 */       s.write((int)(bits >>> 32 & 0xFF));
/* 165 */       s.write((int)(bits >>> 24 & 0xFF));
/* 166 */       s.write((int)(bits >>> 16 & 0xFF));
/* 167 */       s.write((int)(bits >>> 8 & 0xFF));
/* 168 */       s.write((int)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 173 */     encodeToBytesFromLongArray((long[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public final void encodeToBytesFromLongArray(long[] ldata, int lstart, int llength, byte[] b, int start) {
/* 177 */     int lend = lstart + llength;
/* 178 */     for (int i = lstart; i < lend; i++) {
/* 179 */       long bits = ldata[i];
/* 180 */       b[(start++)] = ((byte)(int)(bits >>> 56 & 0xFF));
/* 181 */       b[(start++)] = ((byte)(int)(bits >>> 48 & 0xFF));
/* 182 */       b[(start++)] = ((byte)(int)(bits >>> 40 & 0xFF));
/* 183 */       b[(start++)] = ((byte)(int)(bits >>> 32 & 0xFF));
/* 184 */       b[(start++)] = ((byte)(int)(bits >>> 24 & 0xFF));
/* 185 */       b[(start++)] = ((byte)(int)(bits >>> 16 & 0xFF));
/* 186 */       b[(start++)] = ((byte)(int)(bits >>> 8 & 0xFF));
/* 187 */       b[(start++)] = ((byte)(int)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void convertToCharactersFromLongArray(long[] ldata, StringBuffer s)
/*     */   {
/* 193 */     int end = ldata.length - 1;
/* 194 */     for (int i = 0; i <= end; i++) {
/* 195 */       s.append(Long.toString(ldata[i]));
/* 196 */       if (i != end)
/* 197 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long[] generateArrayFromList(List array)
/*     */   {
/* 204 */     long[] ldata = new long[array.size()];
/* 205 */     for (int i = 0; i < ldata.length; i++) {
/* 206 */       ldata[i] = ((Long)array.get(i)).longValue();
/*     */     }
/*     */ 
/* 209 */     return ldata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.LongEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */