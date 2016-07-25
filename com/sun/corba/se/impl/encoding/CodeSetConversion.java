/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.MalformedInputException;
/*     */ import java.nio.charset.UnmappableCharacterException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ 
/*     */ public class CodeSetConversion
/*     */ {
/*     */   private static CodeSetConversion implementation;
/*     */   private static final int FALLBACK_CODESET = 0;
/* 706 */   private CodeSetCache cache = new CodeSetCache();
/*     */ 
/*     */   public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry paramEntry)
/*     */   {
/* 508 */     int i = !paramEntry.isFixedWidth() ? 1 : paramEntry.getMaxBytesPerChar();
/*     */ 
/* 512 */     return new JavaCTBConverter(paramEntry, i);
/*     */   }
/*     */ 
/*     */   public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry paramEntry, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 535 */     if (paramEntry == OSFCodeSetRegistry.UCS_2) {
/* 536 */       return new UTF16CTBConverter(paramBoolean1);
/*     */     }
/*     */ 
/* 539 */     if (paramEntry == OSFCodeSetRegistry.UTF_16) {
/* 540 */       if (paramBoolean2) {
/* 541 */         return new UTF16CTBConverter();
/*     */       }
/* 543 */       return new UTF16CTBConverter(paramBoolean1);
/*     */     }
/*     */ 
/* 556 */     int i = !paramEntry.isFixedWidth() ? 1 : paramEntry.getMaxBytesPerChar();
/*     */ 
/* 560 */     return new JavaCTBConverter(paramEntry, i);
/*     */   }
/*     */ 
/*     */   public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry paramEntry)
/*     */   {
/* 567 */     return new JavaBTCConverter(paramEntry);
/*     */   }
/*     */ 
/*     */   public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry paramEntry, boolean paramBoolean)
/*     */   {
/* 576 */     if ((paramEntry == OSFCodeSetRegistry.UTF_16) || (paramEntry == OSFCodeSetRegistry.UCS_2))
/*     */     {
/* 579 */       return new UTF16BTCConverter(paramBoolean);
/*     */     }
/* 581 */     return new JavaBTCConverter(paramEntry);
/*     */   }
/*     */ 
/*     */   private int selectEncoding(CodeSetComponentInfo.CodeSetComponent paramCodeSetComponent1, CodeSetComponentInfo.CodeSetComponent paramCodeSetComponent2)
/*     */   {
/* 599 */     int i = paramCodeSetComponent2.nativeCodeSet;
/*     */ 
/* 601 */     if (i == 0) {
/* 602 */       if (paramCodeSetComponent2.conversionCodeSets.length > 0)
/* 603 */         i = paramCodeSetComponent2.conversionCodeSets[0];
/*     */       else {
/* 605 */         return 0;
/*     */       }
/*     */     }
/* 608 */     if (paramCodeSetComponent1.nativeCodeSet == i)
/*     */     {
/* 610 */       return i;
/*     */     }
/*     */ 
/* 615 */     for (int j = 0; j < paramCodeSetComponent1.conversionCodeSets.length; j++) {
/* 616 */       if (i == paramCodeSetComponent1.conversionCodeSets[j])
/*     */       {
/* 619 */         return i;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 625 */     for (j = 0; j < paramCodeSetComponent2.conversionCodeSets.length; j++) {
/* 626 */       if (paramCodeSetComponent1.nativeCodeSet == paramCodeSetComponent2.conversionCodeSets[j])
/*     */       {
/* 629 */         return paramCodeSetComponent1.nativeCodeSet;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 636 */     for (j = 0; j < paramCodeSetComponent2.conversionCodeSets.length; j++) {
/* 637 */       for (int k = 0; k < paramCodeSetComponent1.conversionCodeSets.length; k++) {
/* 638 */         if (paramCodeSetComponent2.conversionCodeSets[j] == paramCodeSetComponent1.conversionCodeSets[k]) {
/* 639 */           return paramCodeSetComponent2.conversionCodeSets[j];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 652 */     return 0;
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo.CodeSetContext negotiate(CodeSetComponentInfo paramCodeSetComponentInfo1, CodeSetComponentInfo paramCodeSetComponentInfo2)
/*     */   {
/* 661 */     int i = selectEncoding(paramCodeSetComponentInfo1.getCharComponent(), paramCodeSetComponentInfo2.getCharComponent());
/*     */ 
/* 665 */     if (i == 0) {
/* 666 */       i = OSFCodeSetRegistry.UTF_8.getNumber();
/*     */     }
/*     */ 
/* 669 */     int j = selectEncoding(paramCodeSetComponentInfo1.getWCharComponent(), paramCodeSetComponentInfo2.getWCharComponent());
/*     */ 
/* 673 */     if (j == 0) {
/* 674 */       j = OSFCodeSetRegistry.UTF_16.getNumber();
/*     */     }
/*     */ 
/* 677 */     return new CodeSetComponentInfo.CodeSetContext(i, j);
/*     */   }
/*     */ 
/*     */   public static final CodeSetConversion impl()
/*     */   {
/* 694 */     return CodeSetConversionHolder.csc;
/*     */   }
/*     */ 
/*     */   public static abstract class BTCConverter
/*     */   {
/*     */     public abstract boolean isFixedWidthEncoding();
/*     */ 
/*     */     public abstract int getFixedCharWidth();
/*     */ 
/*     */     public abstract int getNumChars();
/*     */ 
/*     */     public abstract char[] getChars(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */   }
/*     */ 
/*     */   public static abstract class CTBConverter
/*     */   {
/*     */     public abstract void convert(char paramChar);
/*     */ 
/*     */     public abstract void convert(String paramString);
/*     */ 
/*     */     public abstract int getNumBytes();
/*     */ 
/*     */     public abstract float getMaxBytesPerChar();
/*     */ 
/*     */     public abstract boolean isFixedWidthEncoding();
/*     */ 
/*     */     public abstract int getAlignment();
/*     */ 
/*     */     public abstract byte[] getBytes();
/*     */   }
/*     */ 
/*     */   private static class CodeSetConversionHolder
/*     */   {
/* 687 */     static final CodeSetConversion csc = new CodeSetConversion(null);
/*     */   }
/*     */ 
/*     */   private class JavaBTCConverter extends CodeSetConversion.BTCConverter
/*     */   {
/* 313 */     private ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.encoding");
/*     */ 
/* 316 */     private OMGSystemException omgWrapper = OMGSystemException.get("rpc.encoding");
/*     */     protected CharsetDecoder btc;
/*     */     private char[] buffer;
/*     */     private int resultingNumChars;
/*     */     private OSFCodeSetRegistry.Entry codeset;
/*     */ 
/*     */     public JavaBTCConverter(OSFCodeSetRegistry.Entry arg2)
/*     */     {
/*     */       Object localObject;
/* 327 */       this.btc = getConverter(localObject.getName());
/*     */ 
/* 329 */       this.codeset = localObject;
/*     */     }
/*     */ 
/*     */     public final boolean isFixedWidthEncoding() {
/* 333 */       return this.codeset.isFixedWidth();
/*     */     }
/*     */ 
/*     */     public final int getFixedCharWidth()
/*     */     {
/* 340 */       return this.codeset.getMaxBytesPerChar();
/*     */     }
/*     */ 
/*     */     public final int getNumChars() {
/* 344 */       return this.resultingNumChars;
/*     */     }
/*     */ 
/*     */     public char[] getChars(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/*     */       try
/*     */       {
/* 361 */         ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2);
/* 362 */         CharBuffer localCharBuffer = this.btc.decode(localByteBuffer);
/*     */ 
/* 366 */         this.resultingNumChars = localCharBuffer.limit();
/*     */ 
/* 374 */         if (localCharBuffer.limit() == localCharBuffer.capacity()) {
/* 375 */           this.buffer = localCharBuffer.array();
/*     */         } else {
/* 377 */           this.buffer = new char[localCharBuffer.limit()];
/* 378 */           localCharBuffer.get(this.buffer, 0, localCharBuffer.limit()).position(0);
/*     */         }
/*     */ 
/* 381 */         return this.buffer;
/*     */       }
/*     */       catch (IllegalStateException localIllegalStateException)
/*     */       {
/* 385 */         throw this.wrapper.btcConverterFailure(localIllegalStateException);
/*     */       }
/*     */       catch (MalformedInputException localMalformedInputException) {
/* 388 */         throw this.wrapper.badUnicodePair(localMalformedInputException);
/*     */       }
/*     */       catch (UnmappableCharacterException localUnmappableCharacterException)
/*     */       {
/* 392 */         throw this.omgWrapper.charNotInCodeset(localUnmappableCharacterException);
/*     */       }
/*     */       catch (CharacterCodingException localCharacterCodingException) {
/* 395 */         throw this.wrapper.btcConverterFailure(localCharacterCodingException);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CharsetDecoder getConverter(String paramString)
/*     */     {
/* 406 */       CharsetDecoder localCharsetDecoder = null;
/*     */       try {
/* 408 */         localCharsetDecoder = CodeSetConversion.this.cache.getByteToCharConverter(paramString);
/*     */ 
/* 410 */         if (localCharsetDecoder == null) {
/* 411 */           Charset localCharset = Charset.forName(paramString);
/* 412 */           localCharsetDecoder = localCharset.newDecoder();
/* 413 */           CodeSetConversion.this.cache.setConverter(paramString, localCharsetDecoder);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IllegalCharsetNameException localIllegalCharsetNameException)
/*     */       {
/* 419 */         throw this.wrapper.invalidBtcConverterName(localIllegalCharsetNameException, paramString);
/*     */       }
/*     */ 
/* 422 */       return localCharsetDecoder;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class JavaCTBConverter extends CodeSetConversion.CTBConverter
/*     */   {
/* 137 */     private ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.encoding");
/*     */ 
/* 140 */     private OMGSystemException omgWrapper = OMGSystemException.get("rpc.encoding");
/*     */     private CharsetEncoder ctb;
/*     */     private int alignment;
/* 154 */     private char[] chars = null;
/*     */ 
/* 157 */     private int numBytes = 0;
/*     */ 
/* 161 */     private int numChars = 0;
/*     */     private ByteBuffer buffer;
/*     */     private OSFCodeSetRegistry.Entry codeset;
/*     */ 
/*     */     public JavaCTBConverter(OSFCodeSetRegistry.Entry paramInt, int arg3)
/*     */     {
/*     */       try
/*     */       {
/* 175 */         this.ctb = CodeSetConversion.this.cache.getCharToByteConverter(paramInt.getName());
/* 176 */         if (this.ctb == null) {
/* 177 */           Charset localCharset = Charset.forName(paramInt.getName());
/* 178 */           this.ctb = localCharset.newEncoder();
/* 179 */           CodeSetConversion.this.cache.setConverter(paramInt.getName(), this.ctb);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IllegalCharsetNameException localIllegalCharsetNameException)
/*     */       {
/* 185 */         throw this.wrapper.invalidCtbConverterName(localIllegalCharsetNameException, paramInt.getName());
/*     */       }
/*     */       catch (UnsupportedCharsetException localUnsupportedCharsetException)
/*     */       {
/* 190 */         throw this.wrapper.invalidCtbConverterName(localUnsupportedCharsetException, paramInt.getName());
/*     */       }
/*     */ 
/* 193 */       this.codeset = paramInt;
/*     */       int i;
/* 194 */       this.alignment = i;
/*     */     }
/*     */ 
/*     */     public final float getMaxBytesPerChar() {
/* 198 */       return this.ctb.maxBytesPerChar();
/*     */     }
/*     */ 
/*     */     public void convert(char paramChar) {
/* 202 */       if (this.chars == null) {
/* 203 */         this.chars = new char[1];
/*     */       }
/*     */ 
/* 206 */       this.chars[0] = paramChar;
/* 207 */       this.numChars = 1;
/*     */ 
/* 209 */       convertCharArray();
/*     */     }
/*     */ 
/*     */     public void convert(String paramString)
/*     */     {
/* 217 */       if ((this.chars == null) || (this.chars.length < paramString.length())) {
/* 218 */         this.chars = new char[paramString.length()];
/*     */       }
/* 220 */       this.numChars = paramString.length();
/*     */ 
/* 222 */       paramString.getChars(0, this.numChars, this.chars, 0);
/*     */ 
/* 224 */       convertCharArray();
/*     */     }
/*     */ 
/*     */     public final int getNumBytes() {
/* 228 */       return this.numBytes;
/*     */     }
/*     */ 
/*     */     public final int getAlignment() {
/* 232 */       return this.alignment;
/*     */     }
/*     */ 
/*     */     public final boolean isFixedWidthEncoding() {
/* 236 */       return this.codeset.isFixedWidth();
/*     */     }
/*     */ 
/*     */     public byte[] getBytes()
/*     */     {
/* 243 */       return this.buffer.array();
/*     */     }
/*     */ 
/*     */     private void convertCharArray()
/*     */     {
/*     */       try
/*     */       {
/* 262 */         this.buffer = this.ctb.encode(CharBuffer.wrap(this.chars, 0, this.numChars));
/*     */ 
/* 266 */         this.numBytes = this.buffer.limit();
/*     */       }
/*     */       catch (IllegalStateException localIllegalStateException)
/*     */       {
/* 270 */         throw this.wrapper.ctbConverterFailure(localIllegalStateException);
/*     */       }
/*     */       catch (MalformedInputException localMalformedInputException) {
/* 273 */         throw this.wrapper.badUnicodePair(localMalformedInputException);
/*     */       }
/*     */       catch (UnmappableCharacterException localUnmappableCharacterException)
/*     */       {
/* 277 */         throw this.omgWrapper.charNotInCodeset(localUnmappableCharacterException);
/*     */       }
/*     */       catch (CharacterCodingException localCharacterCodingException) {
/* 280 */         throw this.wrapper.ctbConverterFailure(localCharacterCodingException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class UTF16BTCConverter extends CodeSetConversion.JavaBTCConverter
/*     */   {
/*     */     private boolean defaultToLittleEndian;
/* 437 */     private boolean converterUsesBOM = true;
/*     */     private static final char UTF16_BE_MARKER = '﻿';
/*     */     private static final char UTF16_LE_MARKER = '￾';
/*     */ 
/*     */     public UTF16BTCConverter(boolean arg2)
/*     */     {
/* 445 */       super(OSFCodeSetRegistry.UTF_16);
/*     */       boolean bool;
/* 447 */       this.defaultToLittleEndian = bool;
/*     */     }
/*     */ 
/*     */     public char[] getChars(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/* 452 */       if (hasUTF16ByteOrderMarker(paramArrayOfByte, paramInt1, paramInt2)) {
/* 453 */         if (!this.converterUsesBOM) {
/* 454 */           switchToConverter(OSFCodeSetRegistry.UTF_16);
/*     */         }
/* 456 */         this.converterUsesBOM = true;
/*     */ 
/* 458 */         return super.getChars(paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/* 460 */       if (this.converterUsesBOM) {
/* 461 */         if (this.defaultToLittleEndian)
/* 462 */           switchToConverter(OSFCodeSetRegistry.UTF_16LE);
/*     */         else {
/* 464 */           switchToConverter(OSFCodeSetRegistry.UTF_16BE);
/*     */         }
/* 466 */         this.converterUsesBOM = false;
/*     */       }
/*     */ 
/* 469 */       return super.getChars(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     private boolean hasUTF16ByteOrderMarker(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/* 479 */       if (paramInt2 >= 4)
/*     */       {
/* 481 */         int i = paramArrayOfByte[paramInt1] & 0xFF;
/* 482 */         int j = paramArrayOfByte[(paramInt1 + 1)] & 0xFF;
/*     */ 
/* 484 */         int k = (char)(i << 8 | j << 0);
/*     */ 
/* 486 */         return (k == 65279) || (k == 65534);
/*     */       }
/* 488 */       return false;
/*     */     }
/*     */ 
/*     */     private void switchToConverter(OSFCodeSetRegistry.Entry paramEntry)
/*     */     {
/* 500 */       this.btc = super.getConverter(paramEntry.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class UTF16CTBConverter extends CodeSetConversion.JavaCTBConverter
/*     */   {
/*     */     public UTF16CTBConverter()
/*     */     {
/* 293 */       super(OSFCodeSetRegistry.UTF_16, 2);
/*     */     }
/*     */ 
/*     */     public UTF16CTBConverter(boolean arg2)
/*     */     {
/* 299 */       super(i != 0 ? OSFCodeSetRegistry.UTF_16LE : OSFCodeSetRegistry.UTF_16BE, 2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CodeSetConversion
 * JD-Core Version:    0.6.2
 */