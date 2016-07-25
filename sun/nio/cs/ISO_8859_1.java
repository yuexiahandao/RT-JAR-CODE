/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ class ISO_8859_1 extends Charset
/*     */   implements HistoricallyNamedCharset
/*     */ {
/*     */   public ISO_8859_1()
/*     */   {
/*  42 */     super("ISO-8859-1", StandardCharsets.aliases_ISO_8859_1);
/*     */   }
/*     */ 
/*     */   public String historicalName() {
/*  46 */     return "ISO8859_1";
/*     */   }
/*     */ 
/*     */   public boolean contains(Charset paramCharset) {
/*  50 */     return ((paramCharset instanceof US_ASCII)) || ((paramCharset instanceof ISO_8859_1));
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder()
/*     */   {
/*  55 */     return new Decoder(this, null);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  59 */     return new Encoder(this, null);
/*     */   }
/*     */ 
/*     */   private static class Decoder extends CharsetDecoder implements ArrayDecoder
/*     */   {
/*     */     private Decoder(Charset paramCharset) {
/*  65 */       super(1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     private CoderResult decodeArrayLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/*  71 */       byte[] arrayOfByte = paramByteBuffer.array();
/*  72 */       int i = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/*  73 */       int j = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*  74 */       assert (i <= j);
/*  75 */       i = i <= j ? i : j;
/*  76 */       char[] arrayOfChar = paramCharBuffer.array();
/*  77 */       int k = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/*  78 */       int m = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*  79 */       assert (k <= m);
/*  80 */       k = k <= m ? k : m;
/*     */       try
/*     */       {
/*  83 */         while (i < j) {
/*  84 */           int n = arrayOfByte[i];
/*  85 */           if (k >= m)
/*  86 */             return CoderResult.OVERFLOW;
/*  87 */           arrayOfChar[(k++)] = ((char)(n & 0xFF));
/*  88 */           i++;
/*     */         }
/*  90 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/*  92 */         paramByteBuffer.position(i - paramByteBuffer.arrayOffset());
/*  93 */         paramCharBuffer.position(k - paramCharBuffer.arrayOffset());
/*     */       }
/*     */     }
/*     */ 
/*     */     private CoderResult decodeBufferLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 100 */       int i = paramByteBuffer.position();
/*     */       try {
/* 102 */         while (paramByteBuffer.hasRemaining()) {
/* 103 */           int j = paramByteBuffer.get();
/* 104 */           if (!paramCharBuffer.hasRemaining())
/* 105 */             return CoderResult.OVERFLOW;
/* 106 */           paramCharBuffer.put((char)(j & 0xFF));
/* 107 */           i++;
/*     */         }
/* 109 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 111 */         paramByteBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 118 */       if ((paramByteBuffer.hasArray()) && (paramCharBuffer.hasArray())) {
/* 119 */         return decodeArrayLoop(paramByteBuffer, paramCharBuffer);
/*     */       }
/* 121 */       return decodeBufferLoop(paramByteBuffer, paramCharBuffer);
/*     */     }
/*     */ 
/*     */     public int decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar) {
/* 125 */       if (paramInt2 > paramArrayOfChar.length)
/* 126 */         paramInt2 = paramArrayOfChar.length;
/* 127 */       int i = 0;
/* 128 */       while (i < paramInt2)
/* 129 */         paramArrayOfChar[(i++)] = ((char)(paramArrayOfByte[(paramInt1++)] & 0xFF));
/* 130 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Encoder extends CharsetEncoder
/*     */     implements ArrayEncoder
/*     */   {
/* 148 */     private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/* 217 */     private byte repl = 63;
/*     */ 
/*     */     private Encoder(Charset paramCharset)
/*     */     {
/* 137 */       super(1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     public boolean canEncode(char paramChar) {
/* 141 */       return paramChar <= 'ÿ';
/*     */     }
/*     */ 
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte) {
/* 145 */       return true;
/*     */     }
/*     */ 
/*     */     private CoderResult encodeArrayLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 153 */       char[] arrayOfChar = paramCharBuffer.array();
/* 154 */       int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/* 155 */       int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/* 156 */       assert (i <= j);
/* 157 */       i = i <= j ? i : j;
/* 158 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 159 */       int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/* 160 */       int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/* 161 */       assert (k <= m);
/* 162 */       k = k <= m ? k : m;
/*     */       try {
/* 164 */         while (i < j) {
/* 165 */           char c = arrayOfChar[i];
/*     */           CoderResult localCoderResult2;
/* 166 */           if (c <= 'ÿ') {
/* 167 */             if (k >= m)
/* 168 */               return CoderResult.OVERFLOW;
/* 169 */             arrayOfByte[(k++)] = ((byte)c);
/* 170 */             i++;
/*     */           }
/*     */           else {
/* 173 */             if (this.sgp.parse(c, arrayOfChar, i, j) < 0)
/* 174 */               return this.sgp.error();
/* 175 */             return this.sgp.unmappableResult();
/*     */           }
/*     */         }
/* 177 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 179 */         paramCharBuffer.position(i - paramCharBuffer.arrayOffset());
/* 180 */         paramByteBuffer.position(k - paramByteBuffer.arrayOffset());
/*     */       }
/*     */     }
/*     */ 
/*     */     private CoderResult encodeBufferLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 187 */       int i = paramCharBuffer.position();
/*     */       try {
/* 189 */         while (paramCharBuffer.hasRemaining()) {
/* 190 */           char c = paramCharBuffer.get();
/*     */           CoderResult localCoderResult2;
/* 191 */           if (c <= 'ÿ') {
/* 192 */             if (!paramByteBuffer.hasRemaining())
/* 193 */               return CoderResult.OVERFLOW;
/* 194 */             paramByteBuffer.put((byte)c);
/* 195 */             i++;
/*     */           }
/*     */           else {
/* 198 */             if (this.sgp.parse(c, paramCharBuffer) < 0)
/* 199 */               return this.sgp.error();
/* 200 */             return this.sgp.unmappableResult();
/*     */           }
/*     */         }
/* 202 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 204 */         paramCharBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 211 */       if ((paramCharBuffer.hasArray()) && (paramByteBuffer.hasArray())) {
/* 212 */         return encodeArrayLoop(paramCharBuffer, paramByteBuffer);
/*     */       }
/* 214 */       return encodeBufferLoop(paramCharBuffer, paramByteBuffer);
/*     */     }
/*     */ 
/*     */     protected void implReplaceWith(byte[] paramArrayOfByte)
/*     */     {
/* 219 */       this.repl = paramArrayOfByte[0];
/*     */     }
/*     */ 
/*     */     public int encode(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
/* 223 */       int i = 0;
/* 224 */       int j = paramInt1 + Math.min(paramInt2, paramArrayOfByte.length);
/* 225 */       while (paramInt1 < j) {
/* 226 */         char c = paramArrayOfChar[(paramInt1++)];
/* 227 */         if (c <= 'ÿ') {
/* 228 */           paramArrayOfByte[(i++)] = ((byte)c);
/*     */         }
/*     */         else {
/* 231 */           if ((Character.isHighSurrogate(c)) && (paramInt1 < j) && (Character.isLowSurrogate(paramArrayOfChar[paramInt1])))
/*     */           {
/* 233 */             if (paramInt2 > paramArrayOfByte.length) {
/* 234 */               j++;
/* 235 */               paramInt2--;
/*     */             }
/* 237 */             paramInt1++;
/*     */           }
/* 239 */           paramArrayOfByte[(i++)] = this.repl;
/*     */         }
/*     */       }
/* 241 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.ISO_8859_1
 * JD-Core Version:    0.6.2
 */