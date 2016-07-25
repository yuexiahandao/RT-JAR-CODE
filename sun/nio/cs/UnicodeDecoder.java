/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ abstract class UnicodeDecoder extends CharsetDecoder
/*     */ {
/*     */   protected static final char BYTE_ORDER_MARK = '﻿';
/*     */   protected static final char REVERSED_MARK = '￾';
/*     */   protected static final int NONE = 0;
/*     */   protected static final int BIG = 1;
/*     */   protected static final int LITTLE = 2;
/*     */   private final int expectedByteOrder;
/*     */   private int currentByteOrder;
/*  48 */   private int defaultByteOrder = 1;
/*     */ 
/*     */   public UnicodeDecoder(Charset paramCharset, int paramInt) {
/*  51 */     super(paramCharset, 0.5F, 1.0F);
/*  52 */     this.expectedByteOrder = (this.currentByteOrder = paramInt);
/*     */   }
/*     */ 
/*     */   public UnicodeDecoder(Charset paramCharset, int paramInt1, int paramInt2) {
/*  56 */     this(paramCharset, paramInt1);
/*  57 */     this.defaultByteOrder = paramInt2;
/*     */   }
/*     */ 
/*     */   private char decode(int paramInt1, int paramInt2) {
/*  61 */     if (this.currentByteOrder == 1) {
/*  62 */       return (char)(paramInt1 << 8 | paramInt2);
/*     */     }
/*  64 */     return (char)(paramInt2 << 8 | paramInt1);
/*     */   }
/*     */ 
/*     */   protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/*  68 */     int i = paramByteBuffer.position();
/*     */     try
/*     */     {
/*  71 */       while (paramByteBuffer.remaining() > 1) {
/*  72 */         int j = paramByteBuffer.get() & 0xFF;
/*  73 */         int k = paramByteBuffer.get() & 0xFF;
/*     */         int m;
/*  76 */         if (this.currentByteOrder == 0) {
/*  77 */           m = (char)(j << 8 | k);
/*  78 */           if (m == 65279) {
/*  79 */             this.currentByteOrder = 1;
/*  80 */             i += 2;
/*     */           }
/*  82 */           else if (m == 65534) {
/*  83 */             this.currentByteOrder = 2;
/*  84 */             i += 2;
/*     */           }
/*     */           else {
/*  87 */             this.currentByteOrder = this.defaultByteOrder;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  92 */           m = decode(j, k);
/*     */           CoderResult localCoderResult2;
/*  94 */           if (m == 65534)
/*     */           {
/*  96 */             return CoderResult.malformedForLength(2);
/*     */           }
/*     */           CoderResult localCoderResult3;
/* 100 */           if (Character.isSurrogate(m)) {
/* 101 */             if (Character.isHighSurrogate(m)) {
/* 102 */               if (paramByteBuffer.remaining() < 2)
/* 103 */                 return CoderResult.UNDERFLOW;
/* 104 */               char c = decode(paramByteBuffer.get() & 0xFF, paramByteBuffer.get() & 0xFF);
/*     */               CoderResult localCoderResult4;
/* 105 */               if (!Character.isLowSurrogate(c))
/* 106 */                 return CoderResult.malformedForLength(4);
/* 107 */               if (paramCharBuffer.remaining() < 2)
/* 108 */                 return CoderResult.OVERFLOW;
/* 109 */               i += 4;
/* 110 */               paramCharBuffer.put(m);
/* 111 */               paramCharBuffer.put(c);
/*     */             }
/*     */             else
/*     */             {
/* 115 */               return CoderResult.malformedForLength(2);
/*     */             }
/*     */           } else {
/* 118 */             if (!paramCharBuffer.hasRemaining())
/* 119 */               return CoderResult.OVERFLOW;
/* 120 */             i += 2;
/* 121 */             paramCharBuffer.put(m);
/*     */           }
/*     */         }
/*     */       }
/* 124 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     finally {
/* 127 */       paramByteBuffer.position(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void implReset() {
/* 132 */     this.currentByteOrder = this.expectedByteOrder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UnicodeDecoder
 * JD-Core Version:    0.6.2
 */