/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public abstract class SingleByteDecoder extends CharsetDecoder
/*     */ {
/*     */   private final String byteToCharTable;
/*     */ 
/*     */   protected SingleByteDecoder(Charset paramCharset, String paramString)
/*     */   {
/*  48 */     super(paramCharset, 1.0F, 1.0F);
/*  49 */     this.byteToCharTable = paramString;
/*     */   }
/*     */ 
/*     */   private CoderResult decodeArrayLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/*  53 */     byte[] arrayOfByte = paramByteBuffer.array();
/*  54 */     int i = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/*  55 */     int j = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*  56 */     assert (i <= j);
/*  57 */     i = i <= j ? i : j;
/*  58 */     char[] arrayOfChar = paramCharBuffer.array();
/*  59 */     int k = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/*  60 */     int m = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*  61 */     assert (k <= m);
/*  62 */     k = k <= m ? k : m;
/*     */     try
/*     */     {
/*  65 */       while (i < j) {
/*  66 */         int n = arrayOfByte[i];
/*     */ 
/*  68 */         int i1 = decode(n);
/*     */         CoderResult localCoderResult2;
/*  69 */         if (i1 == 65533)
/*  70 */           return CoderResult.unmappableForLength(1);
/*  71 */         if (m - k < 1)
/*  72 */           return CoderResult.OVERFLOW;
/*  73 */         arrayOfChar[(k++)] = i1;
/*  74 */         i++;
/*     */       }
/*  76 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/*  78 */       paramByteBuffer.position(i - paramByteBuffer.arrayOffset());
/*  79 */       paramCharBuffer.position(k - paramCharBuffer.arrayOffset());
/*     */     }
/*     */   }
/*     */ 
/*     */   private CoderResult decodeBufferLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/*  84 */     int i = paramByteBuffer.position();
/*     */     try {
/*  86 */       while (paramByteBuffer.hasRemaining()) {
/*  87 */         int j = paramByteBuffer.get();
/*     */ 
/*  89 */         int k = decode(j);
/*     */         CoderResult localCoderResult2;
/*  90 */         if (k == 65533)
/*  91 */           return CoderResult.unmappableForLength(1);
/*  92 */         if (!paramCharBuffer.hasRemaining())
/*  93 */           return CoderResult.OVERFLOW;
/*  94 */         i++;
/*  95 */         paramCharBuffer.put(k);
/*     */       }
/*  97 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/*  99 */       paramByteBuffer.position(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/* 104 */     if ((paramByteBuffer.hasArray()) && (paramCharBuffer.hasArray())) {
/* 105 */       return decodeArrayLoop(paramByteBuffer, paramCharBuffer);
/*     */     }
/* 107 */     return decodeBufferLoop(paramByteBuffer, paramCharBuffer);
/*     */   }
/*     */ 
/*     */   public char decode(int paramInt) {
/* 111 */     int i = paramInt + 128;
/* 112 */     if ((i >= this.byteToCharTable.length()) || (i < 0))
/* 113 */       return 65533;
/* 114 */     return this.byteToCharTable.charAt(i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.SingleByteDecoder
 * JD-Core Version:    0.6.2
 */