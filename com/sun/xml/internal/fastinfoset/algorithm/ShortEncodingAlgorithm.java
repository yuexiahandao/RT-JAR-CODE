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
/*     */ public class ShortEncodingAlgorithm extends IntegerEncodingAlgorithm
/*     */ {
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  50 */     if (octetLength % 2 != 0) {
/*  51 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfShort", new Object[] { Integer.valueOf(2) }));
/*     */     }
/*     */ 
/*  55 */     return octetLength / 2;
/*     */   }
/*     */ 
/*     */   public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
/*  59 */     return primitiveLength * 2;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
/*  63 */     short[] data = new short[getPrimtiveLengthFromOctetLength(length)];
/*  64 */     decodeFromBytesToShortArray(data, 0, b, start, length);
/*     */ 
/*  66 */     return data;
/*     */   }
/*     */ 
/*     */   public final Object decodeFromInputStream(InputStream s) throws IOException {
/*  70 */     return decodeFromInputStreamToShortArray(s);
/*     */   }
/*     */ 
/*     */   public void encodeToOutputStream(Object data, OutputStream s) throws IOException
/*     */   {
/*  75 */     if (!(data instanceof short[])) {
/*  76 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
/*     */     }
/*     */ 
/*  79 */     short[] idata = (short[])data;
/*     */ 
/*  81 */     encodeToOutputStreamFromShortArray(idata, s);
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length)
/*     */   {
/*  86 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  87 */     final List shortList = new ArrayList();
/*     */ 
/*  89 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  92 */         String iStringValue = cb.subSequence(start, end).toString();
/*  93 */         shortList.add(Short.valueOf(iStringValue));
/*     */       }
/*     */     });
/*  98 */     return generateArrayFromList(shortList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/* 102 */     if (!(data instanceof short[])) {
/* 103 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
/*     */     }
/*     */ 
/* 106 */     short[] idata = (short[])data;
/*     */ 
/* 108 */     convertToCharactersFromShortArray(idata, s);
/*     */   }
/*     */ 
/*     */   public final void decodeFromBytesToShortArray(short[] sdata, int istart, byte[] b, int start, int length)
/*     */   {
/* 113 */     int size = length / 2;
/* 114 */     for (int i = 0; i < size; i++)
/* 115 */       sdata[(istart++)] = ((short)((b[(start++)] & 0xFF) << 8 | b[(start++)] & 0xFF));
/*     */   }
/*     */ 
/*     */   public final short[] decodeFromInputStreamToShortArray(InputStream s)
/*     */     throws IOException
/*     */   {
/* 121 */     List shortList = new ArrayList();
/* 122 */     byte[] b = new byte[2];
/*     */     while (true)
/*     */     {
/* 125 */       int n = s.read(b);
/* 126 */       if (n != 2) {
/* 127 */         if (n == -1)
/*     */         {
/*     */           break;
/*     */         }
/* 131 */         while (n != 2) {
/* 132 */           int m = s.read(b, n, 2 - n);
/* 133 */           if (m == -1) {
/* 134 */             throw new EOFException();
/*     */           }
/* 136 */           n += m;
/*     */         }
/*     */       }
/*     */ 
/* 140 */       int i = (b[0] & 0xFF) << 8 | b[1] & 0xFF;
/*     */ 
/* 142 */       shortList.add(Short.valueOf((short)i));
/*     */     }
/*     */ 
/* 145 */     return generateArrayFromList(shortList);
/*     */   }
/*     */ 
/*     */   public final void encodeToOutputStreamFromShortArray(short[] idata, OutputStream s) throws IOException
/*     */   {
/* 150 */     for (int i = 0; i < idata.length; i++) {
/* 151 */       int bits = idata[i];
/* 152 */       s.write(bits >>> 8 & 0xFF);
/* 153 */       s.write(bits & 0xFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
/* 158 */     encodeToBytesFromShortArray((short[])array, astart, alength, b, start);
/*     */   }
/*     */ 
/*     */   public final void encodeToBytesFromShortArray(short[] sdata, int istart, int ilength, byte[] b, int start) {
/* 162 */     int iend = istart + ilength;
/* 163 */     for (int i = istart; i < iend; i++) {
/* 164 */       short bits = sdata[i];
/* 165 */       b[(start++)] = ((byte)(bits >>> 8 & 0xFF));
/* 166 */       b[(start++)] = ((byte)(bits & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void convertToCharactersFromShortArray(short[] sdata, StringBuffer s)
/*     */   {
/* 172 */     int end = sdata.length - 1;
/* 173 */     for (int i = 0; i <= end; i++) {
/* 174 */       s.append(Short.toString(sdata[i]));
/* 175 */       if (i != end)
/* 176 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public final short[] generateArrayFromList(List array)
/*     */   {
/* 183 */     short[] sdata = new short[array.size()];
/* 184 */     for (int i = 0; i < sdata.length; i++) {
/* 185 */       sdata[i] = ((Short)array.get(i)).shortValue();
/*     */     }
/*     */ 
/* 188 */     return sdata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.ShortEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */