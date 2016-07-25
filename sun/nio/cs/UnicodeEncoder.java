/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public abstract class UnicodeEncoder extends CharsetEncoder
/*     */ {
/*     */   protected static final char BYTE_ORDER_MARK = '﻿';
/*     */   protected static final char REVERSED_MARK = '￾';
/*     */   protected static final int BIG = 0;
/*     */   protected static final int LITTLE = 1;
/*     */   private int byteOrder;
/*     */   private boolean usesMark;
/*     */   private boolean needsMark;
/*  68 */   private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/*     */   protected UnicodeEncoder(Charset paramCharset, int paramInt, boolean paramBoolean)
/*     */   {
/*  47 */     super(paramCharset, 2.0F, paramBoolean ? 4.0F : 2.0F, new byte[] { -3, paramInt == 0 ? new byte[] { -1, -3 } : -1 });
/*     */ 
/*  54 */     this.usesMark = (this.needsMark = paramBoolean);
/*  55 */     this.byteOrder = paramInt;
/*     */   }
/*     */ 
/*     */   private void put(char paramChar, ByteBuffer paramByteBuffer) {
/*  59 */     if (this.byteOrder == 0) {
/*  60 */       paramByteBuffer.put((byte)(paramChar >> '\b'));
/*  61 */       paramByteBuffer.put((byte)(paramChar & 0xFF));
/*     */     } else {
/*  63 */       paramByteBuffer.put((byte)(paramChar & 0xFF));
/*  64 */       paramByteBuffer.put((byte)(paramChar >> '\b'));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */   {
/*  71 */     int i = paramCharBuffer.position();
/*     */ 
/*  73 */     if ((this.needsMark) && (paramCharBuffer.hasRemaining())) {
/*  74 */       if (paramByteBuffer.remaining() < 2)
/*  75 */         return CoderResult.OVERFLOW;
/*  76 */       put(65279, paramByteBuffer);
/*  77 */       this.needsMark = false;
/*     */     }
/*     */     try {
/*  80 */       while (paramCharBuffer.hasRemaining()) {
/*  81 */         char c = paramCharBuffer.get();
/*  82 */         if (!Character.isSurrogate(c)) {
/*  83 */           if (paramByteBuffer.remaining() < 2)
/*  84 */             return CoderResult.OVERFLOW;
/*  85 */           i++;
/*  86 */           put(c, paramByteBuffer);
/*     */         }
/*     */         else {
/*  89 */           int j = this.sgp.parse(c, paramCharBuffer);
/*     */           CoderResult localCoderResult3;
/*  90 */           if (j < 0)
/*  91 */             return this.sgp.error();
/*  92 */           if (paramByteBuffer.remaining() < 4)
/*  93 */             return CoderResult.OVERFLOW;
/*  94 */           i += 2;
/*  95 */           put(Character.highSurrogate(j), paramByteBuffer);
/*  96 */           put(Character.lowSurrogate(j), paramByteBuffer);
/*     */         }
/*     */       }
/*  98 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 100 */       paramCharBuffer.position(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void implReset() {
/* 105 */     this.needsMark = this.usesMark;
/*     */   }
/*     */ 
/*     */   public boolean canEncode(char paramChar) {
/* 109 */     return !Character.isSurrogate(paramChar);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UnicodeEncoder
 * JD-Core Version:    0.6.2
 */