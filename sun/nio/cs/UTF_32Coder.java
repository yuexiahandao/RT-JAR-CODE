/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ class UTF_32Coder
/*     */ {
/*     */   protected static final int BOM_BIG = 65279;
/*     */   protected static final int BOM_LITTLE = -131072;
/*     */   protected static final int NONE = 0;
/*     */   protected static final int BIG = 1;
/*     */   protected static final int LITTLE = 2;
/*     */ 
/*     */   protected static class Decoder extends CharsetDecoder
/*     */   {
/*     */     private int currentBO;
/*     */     private int expectedBO;
/*     */ 
/*     */     protected Decoder(Charset paramCharset, int paramInt)
/*     */     {
/*  47 */       super(0.25F, 1.0F);
/*  48 */       this.expectedBO = paramInt;
/*  49 */       this.currentBO = 0;
/*     */     }
/*     */ 
/*     */     private int getCP(ByteBuffer paramByteBuffer) {
/*  53 */       return this.currentBO == 1 ? (paramByteBuffer.get() & 0xFF) << 24 | (paramByteBuffer.get() & 0xFF) << 16 | (paramByteBuffer.get() & 0xFF) << 8 | paramByteBuffer.get() & 0xFF : paramByteBuffer.get() & 0xFF | (paramByteBuffer.get() & 0xFF) << 8 | (paramByteBuffer.get() & 0xFF) << 16 | (paramByteBuffer.get() & 0xFF) << 24;
/*     */     }
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/*  65 */       if (paramByteBuffer.remaining() < 4)
/*  66 */         return CoderResult.UNDERFLOW;
/*  67 */       int i = paramByteBuffer.position();
/*     */       try
/*     */       {
/*     */         int j;
/*  70 */         if (this.currentBO == 0) {
/*  71 */           j = (paramByteBuffer.get() & 0xFF) << 24 | (paramByteBuffer.get() & 0xFF) << 16 | (paramByteBuffer.get() & 0xFF) << 8 | paramByteBuffer.get() & 0xFF;
/*     */ 
/*  75 */           if ((j == 65279) && (this.expectedBO != 2)) {
/*  76 */             this.currentBO = 1;
/*  77 */             i += 4;
/*  78 */           } else if ((j == -131072) && (this.expectedBO != 1)) {
/*  79 */             this.currentBO = 2;
/*  80 */             i += 4;
/*     */           } else {
/*  82 */             if (this.expectedBO == 0)
/*  83 */               this.currentBO = 1;
/*     */             else
/*  85 */               this.currentBO = this.expectedBO;
/*  86 */             paramByteBuffer.position(i);
/*     */           }
/*     */         }
/*     */         CoderResult localCoderResult;
/*  89 */         while (paramByteBuffer.remaining() >= 4) {
/*  90 */           j = getCP(paramByteBuffer);
/*  91 */           if (Character.isBmpCodePoint(j)) {
/*  92 */             if (!paramCharBuffer.hasRemaining())
/*  93 */               return CoderResult.OVERFLOW;
/*  94 */             i += 4;
/*  95 */             paramCharBuffer.put((char)j);
/*  96 */           } else if (Character.isValidCodePoint(j)) {
/*  97 */             if (paramCharBuffer.remaining() < 2)
/*  98 */               return CoderResult.OVERFLOW;
/*  99 */             i += 4;
/* 100 */             paramCharBuffer.put(Character.highSurrogate(j));
/* 101 */             paramCharBuffer.put(Character.lowSurrogate(j));
/*     */           } else {
/* 103 */             return CoderResult.malformedForLength(4);
/*     */           }
/*     */         }
/* 106 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 108 */         paramByteBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/* 112 */     protected void implReset() { this.currentBO = 0; } 
/*     */   }
/*     */ 
/*     */   protected static class Encoder extends CharsetEncoder {
/* 117 */     private boolean doBOM = false;
/* 118 */     private boolean doneBOM = true;
/*     */     private int byteOrder;
/*     */ 
/* 122 */     protected void put(int paramInt, ByteBuffer paramByteBuffer) { if (this.byteOrder == 1) {
/* 123 */         paramByteBuffer.put((byte)(paramInt >> 24));
/* 124 */         paramByteBuffer.put((byte)(paramInt >> 16));
/* 125 */         paramByteBuffer.put((byte)(paramInt >> 8));
/* 126 */         paramByteBuffer.put((byte)paramInt);
/*     */       } else {
/* 128 */         paramByteBuffer.put((byte)paramInt);
/* 129 */         paramByteBuffer.put((byte)(paramInt >> 8));
/* 130 */         paramByteBuffer.put((byte)(paramInt >> 16));
/* 131 */         paramByteBuffer.put((byte)(paramInt >> 24));
/*     */       } }
/*     */ 
/*     */     protected Encoder(Charset paramCharset, int paramInt, boolean paramBoolean)
/*     */     {
/* 136 */       super(4.0F, paramBoolean ? 8.0F : 4.0F, new byte[] { -3, -1, 0, paramInt == 1 ? new byte[] { 0, 0, -1, -3 } : 0 });
/*     */ 
/* 140 */       this.byteOrder = paramInt;
/* 141 */       this.doBOM = paramBoolean;
/* 142 */       this.doneBOM = (!paramBoolean);
/*     */     }
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/* 146 */       int i = paramCharBuffer.position();
/* 147 */       if ((!this.doneBOM) && (paramCharBuffer.hasRemaining())) {
/* 148 */         if (paramByteBuffer.remaining() < 4)
/* 149 */           return CoderResult.OVERFLOW;
/* 150 */         put(65279, paramByteBuffer);
/* 151 */         this.doneBOM = true;
/*     */       }
/*     */       try {
/* 154 */         while (paramCharBuffer.hasRemaining()) {
/* 155 */           char c1 = paramCharBuffer.get();
/*     */           CoderResult localCoderResult2;
/* 156 */           if (!Character.isSurrogate(c1)) {
/* 157 */             if (paramByteBuffer.remaining() < 4)
/* 158 */               return CoderResult.OVERFLOW;
/* 159 */             i++;
/* 160 */             put(c1, paramByteBuffer);
/* 161 */           } else if (Character.isHighSurrogate(c1)) {
/* 162 */             if (!paramCharBuffer.hasRemaining())
/* 163 */               return CoderResult.UNDERFLOW;
/* 164 */             char c2 = paramCharBuffer.get();
/*     */             CoderResult localCoderResult4;
/* 165 */             if (Character.isLowSurrogate(c2)) {
/* 166 */               if (paramByteBuffer.remaining() < 4)
/* 167 */                 return CoderResult.OVERFLOW;
/* 168 */               i += 2;
/* 169 */               put(Character.toCodePoint(c1, c2), paramByteBuffer);
/*     */             } else {
/* 171 */               return CoderResult.malformedForLength(1);
/*     */             }
/*     */           }
/*     */           else {
/* 175 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */         }
/* 178 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 180 */         paramCharBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void implReset() {
/* 185 */       this.doneBOM = (!this.doBOM);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_32Coder
 * JD-Core Version:    0.6.2
 */