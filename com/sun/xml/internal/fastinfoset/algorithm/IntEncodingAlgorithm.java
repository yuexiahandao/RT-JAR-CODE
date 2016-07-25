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
/*     */ public class IntEncodingAlgorithm extends IntegerEncodingAlgorithm
/*     */ {
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  45 */     if (octetLength % 4 != 0) {
/*  46 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfInt", new Object[] { Integer.valueOf(4) }));
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
/*  58 */     int[] data = new int[getPrimtiveLengthFromOctetLength(length)];
/*  59 */     decodeFromBytesToIntArray(data, 0, b, start, length);
/*     */ 
/*  61 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  65 */     return decodeFromInputStreamToIntArray(s);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  70 */     if (!(data instanceof int[])) {
/*  71 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
/*     */     }
/*     */ 
/*  74 */     int[] idata = (int[])data;
/*     */ 
/*  76 */     encodeToOutputStreamFromIntArray(idata, s);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length)
/*     */   {
/*  81 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  82 */     final List integerList = new ArrayList();
/*     */ 
/*  84 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  87 */         String iStringValue = cb.subSequence(start, end).toString();
/*  88 */         integerList.add(Integer.valueOf(iStringValue));
/*     */       }
/*     */     });
/*  93 */     return generateArrayFromList(integerList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/*  97 */     if (!(data instanceof int[])) {
/*  98 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
/*     */     }
/*     */ 
/* 101 */     int[] idata = (int[])data;
/*     */ 
/* 103 */     convertToCharactersFromIntArray(idata, s);
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToIntArray(int[] idata, int istart, byte[] b, int start, int length)
/*     */   {
/* 108 */     int size = length / 4;
/* 109 */     for (int i = 0; i < size; i++)
/* 110 */       idata[(istart++)] = ((b[(start++)] & 0xFF) << 24 | (b[(start++)] & 0xFF) << 16 | (b[(start++)] & 0xFF) << 8 | b[(start++)] & 0xFF);
/*     */   }
/*     */ 
/*     */   public final int[] decodeFromInputStreamToIntArray(InputStream s)
/*     */     throws IOException
/*     */   {
/* 118 */     List integerList = new ArrayList();
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
/* 137 */       int i = (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | b[3] & 0xFF;
/*     */ 
/* 141 */       integerList.add(Integer.valueOf(i));
/*     */     }
/*     */ 
/* 144 */     return generateArrayFromList(integerList);
/*     */   }
/*     */ 
/*     */   public final void encodeToOutputStreamFromIntArray(int[] idata, OutputStream s) throws IOException
/*     */   {
/* 149 */     for (int i = 0; i < idata.length; i++) {
/* 150 */       int bits = idata[i];
/* 151 */       s.write(bits >>> 24 & 0xFF);
/* 152 */       s.write(bits >>> 16 & 0xFF);
/* 153 */       s.write(bits >>> 8 & 0xFF);
/* 154 */       s.write(bits & 0xFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 159 */     encodeToBytesFromIntArray((int[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public final void encodeToBytesFromIntArray(int[] idata, int istart, int ilength, byte[] b, int start) {
/* 163 */     int iend = istart + ilength;
/* 164 */     for (int i = istart; i < iend; i++) {
/* 165 */       int bits = idata[i];
/* 166 */       b[(start++)] = ((byte)(bits >>> 24 & 0xFF));
/* 167 */       b[(start++)] = ((byte)(bits >>> 16 & 0xFF));
/* 168 */       b[(start++)] = ((byte)(bits >>> 8 & 0xFF));
/* 169 */       b[(start++)] = ((byte)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void convertToCharactersFromIntArray(int[] idata, StringBuffer s)
/*     */   {
/* 175 */     int end = idata.length - 1;
/* 176 */     for (int i = 0; i <= end; i++) {
/* 177 */       s.append(Integer.toString(idata[i]));
/* 178 */       if (i != end)
/* 179 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int[] generateArrayFromList(List array)
/*     */   {
/* 186 */     int[] idata = new int[array.size()];
/* 187 */     for (int i = 0; i < idata.length; i++) {
/* 188 */       idata[i] = ((Integer)array.get(i)).intValue();
/*     */     }
/*     */ 
/* 191 */     return idata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.IntEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */