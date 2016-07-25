/*     */ package java.util.zip;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import sun.nio.cs.ArrayDecoder;
/*     */ import sun.nio.cs.ArrayEncoder;
/*     */ 
/*     */ final class ZipCoder
/*     */ {
/*     */   private Charset cs;
/*     */   private CharsetDecoder dec;
/*     */   private CharsetEncoder enc;
/*     */   private boolean isUTF8;
/*     */   private ZipCoder utf8;
/*     */ 
/*     */   String toString(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  47 */     CharsetDecoder localCharsetDecoder = decoder().reset();
/*  48 */     int i = (int)(paramInt * localCharsetDecoder.maxCharsPerByte());
/*  49 */     char[] arrayOfChar = new char[i];
/*  50 */     if (i == 0) {
/*  51 */       return new String(arrayOfChar);
/*     */     }
/*     */ 
/*  55 */     if ((this.isUTF8) && ((localCharsetDecoder instanceof ArrayDecoder))) {
/*  56 */       int j = ((ArrayDecoder)localCharsetDecoder).decode(paramArrayOfByte, 0, paramInt, arrayOfChar);
/*  57 */       if (j == -1)
/*  58 */         throw new IllegalArgumentException("MALFORMED");
/*  59 */       return new String(arrayOfChar, 0, j);
/*     */     }
/*  61 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, 0, paramInt);
/*  62 */     CharBuffer localCharBuffer = CharBuffer.wrap(arrayOfChar);
/*  63 */     CoderResult localCoderResult = localCharsetDecoder.decode(localByteBuffer, localCharBuffer, true);
/*  64 */     if (!localCoderResult.isUnderflow())
/*  65 */       throw new IllegalArgumentException(localCoderResult.toString());
/*  66 */     localCoderResult = localCharsetDecoder.flush(localCharBuffer);
/*  67 */     if (!localCoderResult.isUnderflow())
/*  68 */       throw new IllegalArgumentException(localCoderResult.toString());
/*  69 */     return new String(arrayOfChar, 0, localCharBuffer.position());
/*     */   }
/*     */ 
/*     */   String toString(byte[] paramArrayOfByte) {
/*  73 */     return toString(paramArrayOfByte, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   byte[] getBytes(String paramString) {
/*  77 */     CharsetEncoder localCharsetEncoder = encoder().reset();
/*  78 */     char[] arrayOfChar = paramString.toCharArray();
/*  79 */     int i = (int)(arrayOfChar.length * localCharsetEncoder.maxBytesPerChar());
/*  80 */     byte[] arrayOfByte = new byte[i];
/*  81 */     if (i == 0) {
/*  82 */       return arrayOfByte;
/*     */     }
/*     */ 
/*  85 */     if ((this.isUTF8) && ((localCharsetEncoder instanceof ArrayEncoder))) {
/*  86 */       int j = ((ArrayEncoder)localCharsetEncoder).encode(arrayOfChar, 0, arrayOfChar.length, arrayOfByte);
/*  87 */       if (j == -1)
/*  88 */         throw new IllegalArgumentException("MALFORMED");
/*  89 */       return Arrays.copyOf(arrayOfByte, j);
/*     */     }
/*  91 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
/*  92 */     CharBuffer localCharBuffer = CharBuffer.wrap(arrayOfChar);
/*  93 */     CoderResult localCoderResult = localCharsetEncoder.encode(localCharBuffer, localByteBuffer, true);
/*  94 */     if (!localCoderResult.isUnderflow())
/*  95 */       throw new IllegalArgumentException(localCoderResult.toString());
/*  96 */     localCoderResult = localCharsetEncoder.flush(localByteBuffer);
/*  97 */     if (!localCoderResult.isUnderflow())
/*  98 */       throw new IllegalArgumentException(localCoderResult.toString());
/*  99 */     if (localByteBuffer.position() == arrayOfByte.length) {
/* 100 */       return arrayOfByte;
/*     */     }
/* 102 */     return Arrays.copyOf(arrayOfByte, localByteBuffer.position());
/*     */   }
/*     */ 
/*     */   byte[] getBytesUTF8(String paramString)
/*     */   {
/* 107 */     if (this.isUTF8)
/* 108 */       return getBytes(paramString);
/* 109 */     if (this.utf8 == null)
/* 110 */       this.utf8 = new ZipCoder(StandardCharsets.UTF_8);
/* 111 */     return this.utf8.getBytes(paramString);
/*     */   }
/*     */ 
/*     */   String toStringUTF8(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 116 */     if (this.isUTF8)
/* 117 */       return toString(paramArrayOfByte, paramInt);
/* 118 */     if (this.utf8 == null)
/* 119 */       this.utf8 = new ZipCoder(StandardCharsets.UTF_8);
/* 120 */     return this.utf8.toString(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   boolean isUTF8() {
/* 124 */     return this.isUTF8;
/*     */   }
/*     */ 
/*     */   private ZipCoder(Charset paramCharset)
/*     */   {
/* 134 */     this.cs = paramCharset;
/* 135 */     this.isUTF8 = paramCharset.name().equals(StandardCharsets.UTF_8.name());
/*     */   }
/*     */ 
/*     */   static ZipCoder get(Charset paramCharset) {
/* 139 */     return new ZipCoder(paramCharset);
/*     */   }
/*     */ 
/*     */   private CharsetDecoder decoder() {
/* 143 */     if (this.dec == null) {
/* 144 */       this.dec = this.cs.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     }
/*     */ 
/* 148 */     return this.dec;
/*     */   }
/*     */ 
/*     */   private CharsetEncoder encoder() {
/* 152 */     if (this.enc == null) {
/* 153 */       this.enc = this.cs.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     }
/*     */ 
/* 157 */     return this.enc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZipCoder
 * JD-Core Version:    0.6.2
 */