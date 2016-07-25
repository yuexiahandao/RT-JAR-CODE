/*     */ package com.sun.xml.internal.fastinfoset.algorithm;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class UUIDEncodingAlgorithm extends LongEncodingAlgorithm
/*     */ {
/*     */   private long _msb;
/*     */   private long _lsb;
/*     */ 
/*     */   public final int getPrimtiveLengthFromOctetLength(int octetLength)
/*     */     throws EncodingAlgorithmException
/*     */   {
/*  39 */     if (octetLength % 16 != 0) {
/*  40 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfUUID", new Object[] { Integer.valueOf(16) }));
/*     */     }
/*     */ 
/*  44 */     return octetLength / 8;
/*     */   }
/*     */ 
/*     */   public final Object convertFromCharacters(char[] ch, int start, int length) {
/*  48 */     final CharBuffer cb = CharBuffer.wrap(ch, start, length);
/*  49 */     final List longList = new ArrayList();
/*     */ 
/*  51 */     matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener()
/*     */     {
/*     */       public void word(int start, int end) {
/*  54 */         String uuidValue = cb.subSequence(start, end).toString();
/*  55 */         UUIDEncodingAlgorithm.this.fromUUIDString(uuidValue);
/*  56 */         longList.add(Long.valueOf(UUIDEncodingAlgorithm.this._msb));
/*  57 */         longList.add(Long.valueOf(UUIDEncodingAlgorithm.this._lsb));
/*     */       }
/*     */     });
/*  62 */     return generateArrayFromList(longList);
/*     */   }
/*     */ 
/*     */   public final void convertToCharacters(Object data, StringBuffer s) {
/*  66 */     if (!(data instanceof long[])) {
/*  67 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
/*     */     }
/*     */ 
/*  70 */     long[] ldata = (long[])data;
/*     */ 
/*  72 */     int end = ldata.length - 2;
/*  73 */     for (int i = 0; i <= end; i += 2) {
/*  74 */       s.append(toUUIDString(ldata[i], ldata[(i + 1)]));
/*  75 */       if (i != end)
/*  76 */         s.append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   final void fromUUIDString(String name)
/*     */   {
/*  86 */     String[] components = name.split("-");
/*  87 */     if (components.length != 5) {
/*  88 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.invalidUUID", new Object[] { name }));
/*     */     }
/*     */ 
/*  91 */     for (int i = 0; i < 5; i++) {
/*  92 */       components[i] = ("0x" + components[i]);
/*     */     }
/*  94 */     this._msb = Long.parseLong(components[0], 16);
/*  95 */     this._msb <<= 16;
/*  96 */     this._msb |= Long.parseLong(components[1], 16);
/*  97 */     this._msb <<= 16;
/*  98 */     this._msb |= Long.parseLong(components[2], 16);
/*     */ 
/* 100 */     this._lsb = Long.parseLong(components[3], 16);
/* 101 */     this._lsb <<= 48;
/* 102 */     this._lsb |= Long.parseLong(components[4], 16);
/*     */   }
/*     */ 
/*     */   final String toUUIDString(long msb, long lsb) {
/* 106 */     return digits(msb >> 32, 8) + "-" + digits(msb >> 16, 4) + "-" + digits(msb, 4) + "-" + digits(lsb >> 48, 4) + "-" + digits(lsb, 12);
/*     */   }
/*     */ 
/*     */   final String digits(long val, int digits)
/*     */   {
/* 114 */     long hi = 1L << digits * 4;
/* 115 */     return Long.toHexString(hi | val & hi - 1L).substring(1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.UUIDEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */