/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public abstract class SingleByteEncoder extends CharsetEncoder
/*     */ {
/*     */   private final short[] index1;
/*     */   private final String index2;
/*     */   private final int mask1;
/*     */   private final int mask2;
/*     */   private final int shift;
/*  52 */   private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/*     */   protected SingleByteEncoder(Charset paramCharset, short[] paramArrayOfShort, String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  58 */     super(paramCharset, 1.0F, 1.0F);
/*  59 */     this.index1 = paramArrayOfShort;
/*  60 */     this.index2 = paramString;
/*  61 */     this.mask1 = paramInt1;
/*  62 */     this.mask2 = paramInt2;
/*  63 */     this.shift = paramInt3;
/*     */   }
/*     */ 
/*     */   public boolean canEncode(char paramChar) {
/*  67 */     int i = this.index2.charAt(this.index1[((paramChar & this.mask1) >> this.shift)] + (paramChar & this.mask2));
/*     */ 
/*  69 */     return (i != 0) || (paramChar == 0);
/*     */   }
/*     */ 
/*     */   private CoderResult encodeArrayLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/*  73 */     char[] arrayOfChar = paramCharBuffer.array();
/*  74 */     int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/*  75 */     int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*  76 */     assert (i <= j);
/*  77 */     i = i <= j ? i : j;
/*  78 */     byte[] arrayOfByte = paramByteBuffer.array();
/*  79 */     int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/*  80 */     int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*  81 */     assert (k <= m);
/*  82 */     k = k <= m ? k : m;
/*     */     try
/*     */     {
/*  85 */       while (i < j) {
/*  86 */         int n = arrayOfChar[i];
/*     */         CoderResult localCoderResult2;
/*  87 */         if (Character.isSurrogate(n)) {
/*  88 */           if (this.sgp.parse(n, arrayOfChar, i, j) < 0)
/*  89 */             return this.sgp.error();
/*  90 */           return this.sgp.unmappableResult();
/*     */         }
/*  92 */         if (n >= 65534)
/*  93 */           return CoderResult.unmappableForLength(1);
/*  94 */         if (m - k < 1) {
/*  95 */           return CoderResult.OVERFLOW;
/*     */         }
/*  97 */         int i1 = this.index2.charAt(this.index1[((n & this.mask1) >> this.shift)] + (n & this.mask2));
/*     */ 
/* 102 */         if ((i1 == 0) && (n != 0)) {
/* 103 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 105 */         i++;
/* 106 */         arrayOfByte[(k++)] = ((byte)i1);
/*     */       }
/* 108 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 110 */       paramCharBuffer.position(i - paramCharBuffer.arrayOffset());
/* 111 */       paramByteBuffer.position(k - paramByteBuffer.arrayOffset());
/*     */     }
/*     */   }
/*     */ 
/*     */   private CoderResult encodeBufferLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/* 116 */     int i = paramCharBuffer.position();
/*     */     try {
/* 118 */       while (paramCharBuffer.hasRemaining()) {
/* 119 */         int j = paramCharBuffer.get();
/*     */         CoderResult localCoderResult2;
/* 120 */         if (Character.isSurrogate(j)) {
/* 121 */           if (this.sgp.parse(j, paramCharBuffer) < 0)
/* 122 */             return this.sgp.error();
/* 123 */           return this.sgp.unmappableResult();
/*     */         }
/* 125 */         if (j >= 65534)
/* 126 */           return CoderResult.unmappableForLength(1);
/* 127 */         if (!paramByteBuffer.hasRemaining()) {
/* 128 */           return CoderResult.OVERFLOW;
/*     */         }
/* 130 */         int k = this.index2.charAt(this.index1[((j & this.mask1) >> this.shift)] + (j & this.mask2));
/*     */ 
/* 135 */         if ((k == 0) && (j != 0)) {
/* 136 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 138 */         i++;
/* 139 */         paramByteBuffer.put((byte)k);
/*     */       }
/* 141 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 143 */       paramCharBuffer.position(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/* 148 */     if ((paramCharBuffer.hasArray()) && (paramByteBuffer.hasArray())) {
/* 149 */       return encodeArrayLoop(paramCharBuffer, paramByteBuffer);
/*     */     }
/* 151 */     return encodeBufferLoop(paramCharBuffer, paramByteBuffer);
/*     */   }
/*     */ 
/*     */   public byte encode(char paramChar) {
/* 155 */     return (byte)this.index2.charAt(this.index1[((paramChar & this.mask1) >> this.shift)] + (paramChar & this.mask2));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.SingleByteEncoder
 * JD-Core Version:    0.6.2
 */