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
/*     */ public class FloatEncodingAlgorithm extends IEEE754FloatingPointEncodingAlgorithm
/*     */ {
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  45 */     if (octetLength % 4 != 0) {
/*  46 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfFloat", new Object[] { Integer.valueOf(4) }));
/*     */     }
/*     */ 
/*  50 */     return octetLength / 4;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/*  54 */     return primitiveLength * 4;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
/*  58 */     float[] data = new float[getPrimtiveLengthFromOctetLength(length)];
/*  59 */     decodeFromBytesToFloatArray(data, 0, b, start, length);
/*     */ 
/*  61 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  65 */     return decodeFromInputStreamToFloatArray(s);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  70 */     if (!(data instanceof float[])) {
/*  71 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
/*     */     }
/*     */ 
/*  74 */     float[] fdata = (float[])data;
/*     */ 
/*  76 */     encodeToOutputStreamFromFloatArray(fdata, s);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length) {
/*  80 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  81 */     final List floatList = new ArrayList();
/*     */ 
/*  83 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  86 */         String fStringValue = cb.subSequence(start, end).toString();
/*  87 */         floatList.add(Float.valueOf(fStringValue));
/*     */       }
/*     */     });
/*  92 */     return generateArrayFromList(floatList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/*  96 */     if (!(data instanceof float[])) {
/*  97 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
/*     */     }
/*     */ 
/* 100 */     float[] fdata = (float[])data;
/*     */ 
/* 102 */     convertToCharactersFromFloatArray(fdata, s);
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToFloatArray(float[] data, int fstart, byte[] b, int start, int length)
/*     */   {
/* 107 */     int size = length / 4;
/* 108 */     for (int i = 0; i < size; i++) {
/* 109 */       int bits = (b[(start++)] & 0xFF) << 24 | (b[(start++)] & 0xFF) << 16 | (b[(start++)] & 0xFF) << 8 | b[(start++)] & 0xFF;
/*     */ 
/* 113 */       data[(fstart++)] = Float.intBitsToFloat(bits);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final float[] decodeFromInputStreamToFloatArray(InputStream s) throws IOException {
/* 118 */     List floatList = new ArrayList();
/* 119 */     byte[] b = new byte[4];
/*     */     while (true)
/*     */     {
/* 122 */       int n = s.read(b);
/* 123 */       if (n != 4) {
/* 124 */         if (n == -1)
/*     */         {
/*     */           break;
/*     */         }
/* 128 */         while (n != 4) {
/* 129 */           int m = s.read(b, n, 4 - n);
/* 130 */           if (m == -1) {
/* 131 */             throw new EOFException();
/*     */           }
/* 133 */           n += m;
/*     */         }
/*     */       }
/*     */ 
/* 137 */       int bits = (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | b[3] & 0xFF;
/*     */ 
/* 141 */       floatList.add(Float.valueOf(Float.intBitsToFloat(bits)));
/*     */     }
/*     */ 
/* 144 */     return generateArrayFromList(floatList);
/*     */   }
/*     */ 
/*     */   public final void encodeToOutputStreamFromFloatArray(float[] fdata, OutputStream s) throws IOException
/*     */   {
/* 149 */     for (int i = 0; i < fdata.length; i++) {
/* 150 */       int bits = Float.floatToIntBits(fdata[i]);
/* 151 */       s.write(bits >>> 24 & 0xFF);
/* 152 */       s.write(bits >>> 16 & 0xFF);
/* 153 */       s.write(bits >>> 8 & 0xFF);
/* 154 */       s.write(bits & 0xFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 159 */     encodeToBytesFromFloatArray((float[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public final void encodeToBytesFromFloatArray(float[] fdata, int fstart, int flength, byte[] b, int start) {
/* 163 */     int fend = fstart + flength;
/* 164 */     for (int i = fstart; i < fend; i++) {
/* 165 */       int bits = Float.floatToIntBits(fdata[i]);
/* 166 */       b[(start++)] = ((byte)(bits >>> 24 & 0xFF));
/* 167 */       b[(start++)] = ((byte)(bits >>> 16 & 0xFF));
/* 168 */       b[(start++)] = ((byte)(bits >>> 8 & 0xFF));
/* 169 */       b[(start++)] = ((byte)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void convertToCharactersFromFloatArray(float[] fdata, StringBuffer s)
/*     */   {
/* 175 */     int end = fdata.length - 1;
/* 176 */     for (int i = 0; i <= end; i++) {
/* 177 */       s.append(Float.toString(fdata[i]));
/* 178 */       if (i != end)
/* 179 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public final float[] generateArrayFromList(List array)
/*     */   {
/* 186 */     float[] fdata = new float[array.size()];
/* 187 */     for (int i = 0; i < fdata.length; i++) {
/* 188 */       fdata[i] = ((Float)array.get(i)).floatValue();
/*     */     }
/*     */ 
/* 191 */     return fdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */