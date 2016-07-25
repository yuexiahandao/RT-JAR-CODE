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
/*     */ public class DoubleEncodingAlgorithm extends IEEE754FloatingPointEncodingAlgorithm
/*     */ {
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  45 */     if (octetLength % 8 != 0) {
/*  46 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthIsNotMultipleOfDouble", new Object[] { Integer.valueOf(8) }));
/*     */     }
/*     */ 
/*  50 */     return octetLength / 8;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/*  54 */     return primitiveLength * 8;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
/*  58 */     double[] data = new double[getPrimtiveLengthFromOctetLength(length)];
/*  59 */     decodeFromBytesToDoubleArray(data, 0, b, start, length);
/*     */ 
/*  61 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  65 */     return decodeFromInputStreamToDoubleArray(s);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  70 */     if (!(data instanceof double[])) {
/*  71 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotDouble"));
/*     */     }
/*     */ 
/*  74 */     double[] fdata = (double[])data;
/*     */ 
/*  76 */     encodeToOutputStreamFromDoubleArray(fdata, s);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length) {
/*  80 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  81 */     final List doubleList = new ArrayList();
/*     */ 
/*  83 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  86 */         String fStringValue = cb.subSequence(start, end).toString();
/*  87 */         doubleList.add(Double.valueOf(fStringValue));
/*     */       }
/*     */     });
/*  92 */     return generateArrayFromList(doubleList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/*  96 */     if (!(data instanceof double[])) {
/*  97 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotDouble"));
/*     */     }
/*     */ 
/* 100 */     double[] fdata = (double[])data;
/*     */ 
/* 102 */     convertToCharactersFromDoubleArray(fdata, s);
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToDoubleArray(double[] data, int fstart, byte[] b, int start, int length)
/*     */   {
/* 107 */     int size = length / 8;
/* 108 */     for (int i = 0; i < size; i++) {
/* 109 */       long bits = (b[(start++)] & 0xFF) << 56 | (b[(start++)] & 0xFF) << 48 | (b[(start++)] & 0xFF) << 40 | (b[(start++)] & 0xFF) << 32 | (b[(start++)] & 0xFF) << 24 | (b[(start++)] & 0xFF) << 16 | (b[(start++)] & 0xFF) << 8 | b[(start++)] & 0xFF;
/*     */ 
/* 118 */       data[(fstart++)] = Double.longBitsToDouble(bits);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final double[] decodeFromInputStreamToDoubleArray(InputStream s) throws IOException {
/* 123 */     List doubleList = new ArrayList();
/* 124 */     byte[] b = new byte[8];
/*     */     while (true)
/*     */     {
/* 127 */       int n = s.read(b);
/* 128 */       if (n != 8) {
/* 129 */         if (n == -1)
/*     */         {
/*     */           break;
/*     */         }
/* 133 */         while (n != 8) {
/* 134 */           int m = s.read(b, n, 8 - n);
/* 135 */           if (m == -1) {
/* 136 */             throw new EOFException();
/*     */           }
/* 138 */           n += m;
/*     */         }
/*     */       }
/*     */ 
/* 142 */       long bits = (b[0] & 0xFF) << 56 | (b[1] & 0xFF) << 48 | (b[2] & 0xFF) << 40 | (b[3] & 0xFF) << 32 | (b[4] & 0xFF) << 24 | (b[5] & 0xFF) << 16 | (b[6] & 0xFF) << 8 | b[7] & 0xFF;
/*     */ 
/* 152 */       doubleList.add(Double.valueOf(Double.longBitsToDouble(bits)));
/*     */     }
/*     */ 
/* 155 */     return generateArrayFromList(doubleList);
/*     */   }
/*     */ 
/*     */   public final void encodeToOutputStreamFromDoubleArray(double[] fdata, OutputStream s) throws IOException
/*     */   {
/* 160 */     for (int i = 0; i < fdata.length; i++) {
/* 161 */       long bits = Double.doubleToLongBits(fdata[i]);
/* 162 */       s.write((int)(bits >>> 56 & 0xFF));
/* 163 */       s.write((int)(bits >>> 48 & 0xFF));
/* 164 */       s.write((int)(bits >>> 40 & 0xFF));
/* 165 */       s.write((int)(bits >>> 32 & 0xFF));
/* 166 */       s.write((int)(bits >>> 24 & 0xFF));
/* 167 */       s.write((int)(bits >>> 16 & 0xFF));
/* 168 */       s.write((int)(bits >>> 8 & 0xFF));
/* 169 */       s.write((int)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 174 */     encodeToBytesFromDoubleArray((double[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public final void encodeToBytesFromDoubleArray(double[] fdata, int fstart, int flength, byte[] b, int start) {
/* 178 */     int fend = fstart + flength;
/* 179 */     for (int i = fstart; i < fend; i++) {
/* 180 */       long bits = Double.doubleToLongBits(fdata[i]);
/* 181 */       b[(start++)] = ((byte)(int)(bits >>> 56 & 0xFF));
/* 182 */       b[(start++)] = ((byte)(int)(bits >>> 48 & 0xFF));
/* 183 */       b[(start++)] = ((byte)(int)(bits >>> 40 & 0xFF));
/* 184 */       b[(start++)] = ((byte)(int)(bits >>> 32 & 0xFF));
/* 185 */       b[(start++)] = ((byte)(int)(bits >>> 24 & 0xFF));
/* 186 */       b[(start++)] = ((byte)(int)(bits >>> 16 & 0xFF));
/* 187 */       b[(start++)] = ((byte)(int)(bits >>> 8 & 0xFF));
/* 188 */       b[(start++)] = ((byte)(int)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void convertToCharactersFromDoubleArray(double[] fdata, StringBuffer s)
/*     */   {
/* 194 */     int end = fdata.length - 1;
/* 195 */     for (int i = 0; i <= end; i++) {
/* 196 */       s.append(Double.toString(fdata[i]));
/* 197 */       if (i != end)
/* 198 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public final double[] generateArrayFromList(List array)
/*     */   {
/* 205 */     double[] fdata = new double[array.size()];
/* 206 */     for (int i = 0; i < fdata.length; i++) {
/* 207 */       fdata[i] = ((Double)array.get(i)).doubleValue();
/*     */     }
/*     */ 
/* 210 */     return fdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.DoubleEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */