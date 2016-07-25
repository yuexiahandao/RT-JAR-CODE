/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public class US_ASCII extends Charset
/*     */   implements HistoricallyNamedCharset
/*     */ {
/*     */   public US_ASCII()
/*     */   {
/*  42 */     super("US-ASCII", StandardCharsets.aliases_US_ASCII);
/*     */   }
/*     */ 
/*     */   public String historicalName() {
/*  46 */     return "ASCII";
/*     */   }
/*     */ 
/*     */   public boolean contains(Charset paramCharset) {
/*  50 */     return paramCharset instanceof US_ASCII;
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder() {
/*  54 */     return new Decoder(this, null);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  58 */     return new Encoder(this, null);
/*     */   }
/*     */ 
/*     */   private static class Decoder extends CharsetDecoder
/*     */     implements ArrayDecoder
/*     */   {
/* 132 */     private char repl = 65533;
/*     */ 
/*     */     private Decoder(Charset paramCharset)
/*     */     {
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
/*     */           CoderResult localCoderResult2;
/*  85 */           if (n >= 0) {
/*  86 */             if (k >= m)
/*  87 */               return CoderResult.OVERFLOW;
/*  88 */             arrayOfChar[(k++)] = ((char)n);
/*  89 */             i++;
/*     */           }
/*     */           else {
/*  92 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */         }
/*  94 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/*  96 */         paramByteBuffer.position(i - paramByteBuffer.arrayOffset());
/*  97 */         paramCharBuffer.position(k - paramCharBuffer.arrayOffset());
/*     */       }
/*     */     }
/*     */ 
/*     */     private CoderResult decodeBufferLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 104 */       int i = paramByteBuffer.position();
/*     */       try {
/* 106 */         while (paramByteBuffer.hasRemaining()) {
/* 107 */           int j = paramByteBuffer.get();
/*     */           CoderResult localCoderResult2;
/* 108 */           if (j >= 0) {
/* 109 */             if (!paramCharBuffer.hasRemaining())
/* 110 */               return CoderResult.OVERFLOW;
/* 111 */             paramCharBuffer.put((char)j);
/* 112 */             i++;
/*     */           }
/*     */           else {
/* 115 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */         }
/* 117 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 119 */         paramByteBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 126 */       if ((paramByteBuffer.hasArray()) && (paramCharBuffer.hasArray())) {
/* 127 */         return decodeArrayLoop(paramByteBuffer, paramCharBuffer);
/*     */       }
/* 129 */       return decodeBufferLoop(paramByteBuffer, paramCharBuffer);
/*     */     }
/*     */ 
/*     */     protected void implReplaceWith(String paramString)
/*     */     {
/* 134 */       this.repl = paramString.charAt(0);
/*     */     }
/*     */ 
/*     */     public int decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar) {
/* 138 */       int i = 0;
/* 139 */       paramInt2 = Math.min(paramInt2, paramArrayOfChar.length);
/* 140 */       while (i < paramInt2) {
/* 141 */         int j = paramArrayOfByte[(paramInt1++)];
/* 142 */         if (j >= 0)
/* 143 */           paramArrayOfChar[(i++)] = ((char)j);
/*     */         else
/* 145 */           paramArrayOfChar[(i++)] = this.repl;
/*     */       }
/* 147 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Encoder extends CharsetEncoder
/*     */     implements ArrayEncoder
/*     */   {
/* 167 */     private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/* 236 */     private byte repl = 63;
/*     */ 
/*     */     private Encoder(Charset paramCharset)
/*     */     {
/* 155 */       super(1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     public boolean canEncode(char paramChar) {
/* 159 */       return paramChar < '';
/*     */     }
/*     */ 
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte) {
/* 163 */       return ((paramArrayOfByte.length == 1) && (paramArrayOfByte[0] >= 0)) || (super.isLegalReplacement(paramArrayOfByte));
/*     */     }
/*     */ 
/*     */     private CoderResult encodeArrayLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 171 */       char[] arrayOfChar = paramCharBuffer.array();
/* 172 */       int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/* 173 */       int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/* 174 */       assert (i <= j);
/* 175 */       i = i <= j ? i : j;
/* 176 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 177 */       int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/* 178 */       int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/* 179 */       assert (k <= m);
/* 180 */       k = k <= m ? k : m;
/*     */       try
/*     */       {
/* 183 */         while (i < j) {
/* 184 */           char c = arrayOfChar[i];
/*     */           CoderResult localCoderResult2;
/* 185 */           if (c < '') {
/* 186 */             if (k >= m)
/* 187 */               return CoderResult.OVERFLOW;
/* 188 */             arrayOfByte[k] = ((byte)c);
/* 189 */             i++; k++;
/*     */           }
/*     */           else {
/* 192 */             if (this.sgp.parse(c, arrayOfChar, i, j) < 0)
/* 193 */               return this.sgp.error();
/* 194 */             return this.sgp.unmappableResult();
/*     */           }
/*     */         }
/* 196 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 198 */         paramCharBuffer.position(i - paramCharBuffer.arrayOffset());
/* 199 */         paramByteBuffer.position(k - paramByteBuffer.arrayOffset());
/*     */       }
/*     */     }
/*     */ 
/*     */     private CoderResult encodeBufferLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 206 */       int i = paramCharBuffer.position();
/*     */       try {
/* 208 */         while (paramCharBuffer.hasRemaining()) {
/* 209 */           char c = paramCharBuffer.get();
/*     */           CoderResult localCoderResult2;
/* 210 */           if (c < '') {
/* 211 */             if (!paramByteBuffer.hasRemaining())
/* 212 */               return CoderResult.OVERFLOW;
/* 213 */             paramByteBuffer.put((byte)c);
/* 214 */             i++;
/*     */           }
/*     */           else {
/* 217 */             if (this.sgp.parse(c, paramCharBuffer) < 0)
/* 218 */               return this.sgp.error();
/* 219 */             return this.sgp.unmappableResult();
/*     */           }
/*     */         }
/* 221 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 223 */         paramCharBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 230 */       if ((paramCharBuffer.hasArray()) && (paramByteBuffer.hasArray())) {
/* 231 */         return encodeArrayLoop(paramCharBuffer, paramByteBuffer);
/*     */       }
/* 233 */       return encodeBufferLoop(paramCharBuffer, paramByteBuffer);
/*     */     }
/*     */ 
/*     */     protected void implReplaceWith(byte[] paramArrayOfByte)
/*     */     {
/* 238 */       this.repl = paramArrayOfByte[0];
/*     */     }
/*     */ 
/*     */     public int encode(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
/* 242 */       int i = 0;
/* 243 */       int j = paramInt1 + Math.min(paramInt2, paramArrayOfByte.length);
/* 244 */       while (paramInt1 < j) {
/* 245 */         char c = paramArrayOfChar[(paramInt1++)];
/* 246 */         if (c < '') {
/* 247 */           paramArrayOfByte[(i++)] = ((byte)c);
/*     */         }
/*     */         else {
/* 250 */           if ((Character.isHighSurrogate(c)) && (paramInt1 < j) && (Character.isLowSurrogate(paramArrayOfChar[paramInt1])))
/*     */           {
/* 252 */             if (paramInt2 > paramArrayOfByte.length) {
/* 253 */               j++;
/* 254 */               paramInt2--;
/*     */             }
/* 256 */             paramInt1++;
/*     */           }
/* 258 */           paramArrayOfByte[(i++)] = this.repl;
/*     */         }
/*     */       }
/* 260 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.US_ASCII
 * JD-Core Version:    0.6.2
 */