/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public class SingleByte
/*     */ {
/*     */   private static final CoderResult withResult(CoderResult paramCoderResult, Buffer paramBuffer1, int paramInt1, Buffer paramBuffer2, int paramInt2)
/*     */   {
/*  44 */     paramBuffer1.position(paramInt1 - paramBuffer1.arrayOffset());
/*  45 */     paramBuffer2.position(paramInt2 - paramBuffer2.arrayOffset());
/*  46 */     return paramCoderResult;
/*     */   }
/*     */ 
/*     */   public static void initC2B(char[] paramArrayOfChar1, char[] paramArrayOfChar2, char[] paramArrayOfChar3, char[] paramArrayOfChar4)
/*     */   {
/* 263 */     for (int i = 0; i < paramArrayOfChar4.length; i++)
/* 264 */       paramArrayOfChar4[i] = 65533;
/* 265 */     for (i = 0; i < paramArrayOfChar3.length; i++)
/* 266 */       paramArrayOfChar3[i] = 65533;
/* 267 */     i = 0;
/*     */     int k;
/*     */     int m;
/* 268 */     for (int j = 0; j < paramArrayOfChar1.length; j++) {
/* 269 */       k = paramArrayOfChar1[j];
/* 270 */       if (k != 65533)
/*     */       {
/* 272 */         m = k >> 8;
/* 273 */         if (paramArrayOfChar4[m] == 65533) {
/* 274 */           paramArrayOfChar4[m] = ((char)i);
/* 275 */           i += 256;
/*     */         }
/* 277 */         m = paramArrayOfChar4[m] + (k & 0xFF);
/* 278 */         paramArrayOfChar3[m] = ((char)(j >= 128 ? j - 128 : j + 128));
/*     */       }
/*     */     }
/* 280 */     if (paramArrayOfChar2 != null)
/*     */     {
/* 282 */       j = 0;
/* 283 */       while (j < paramArrayOfChar2.length) {
/* 284 */         k = paramArrayOfChar2[(j++)];
/* 285 */         m = paramArrayOfChar2[(j++)];
/* 286 */         int n = m >> 8;
/* 287 */         if (paramArrayOfChar4[n] == 65533) {
/* 288 */           paramArrayOfChar4[n] = ((char)i);
/* 289 */           i += 256;
/*     */         }
/* 291 */         n = paramArrayOfChar4[n] + (m & 0xFF);
/* 292 */         paramArrayOfChar3[n] = k;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Decoder extends CharsetDecoder
/*     */     implements ArrayDecoder
/*     */   {
/*     */     private final char[] b2c;
/* 114 */     private char repl = 65533;
/*     */ 
/*     */     public Decoder(Charset paramCharset, char[] paramArrayOfChar)
/*     */     {
/*  54 */       super(1.0F, 1.0F);
/*  55 */       this.b2c = paramArrayOfChar;
/*     */     }
/*     */ 
/*     */     private CoderResult decodeArrayLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/*  59 */       byte[] arrayOfByte = paramByteBuffer.array();
/*  60 */       int i = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/*  61 */       int j = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*     */ 
/*  63 */       char[] arrayOfChar = paramCharBuffer.array();
/*  64 */       int k = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/*  65 */       int m = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*     */ 
/*  67 */       CoderResult localCoderResult = CoderResult.UNDERFLOW;
/*  68 */       if (m - k < j - i) {
/*  69 */         j = i + (m - k);
/*  70 */         localCoderResult = CoderResult.OVERFLOW;
/*     */       }
/*     */ 
/*  73 */       while (i < j) {
/*  74 */         int n = decode(arrayOfByte[i]);
/*  75 */         if (n == 65533) {
/*  76 */           return SingleByte.withResult(CoderResult.unmappableForLength(1), paramByteBuffer, i, paramCharBuffer, k);
/*     */         }
/*     */ 
/*  79 */         arrayOfChar[(k++)] = n;
/*  80 */         i++;
/*     */       }
/*  82 */       return SingleByte.withResult(localCoderResult, paramByteBuffer, i, paramCharBuffer, k);
/*     */     }
/*     */ 
/*     */     private CoderResult decodeBufferLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/*  86 */       int i = paramByteBuffer.position();
/*     */       try {
/*  88 */         while (paramByteBuffer.hasRemaining()) {
/*  89 */           int j = decode(paramByteBuffer.get());
/*     */           CoderResult localCoderResult2;
/*  90 */           if (j == 65533)
/*  91 */             return CoderResult.unmappableForLength(1);
/*  92 */           if (!paramCharBuffer.hasRemaining())
/*  93 */             return CoderResult.OVERFLOW;
/*  94 */           paramCharBuffer.put(j);
/*  95 */           i++;
/*     */         }
/*  97 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/*  99 */         paramByteBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/* 104 */       if ((paramByteBuffer.hasArray()) && (paramCharBuffer.hasArray())) {
/* 105 */         return decodeArrayLoop(paramByteBuffer, paramCharBuffer);
/*     */       }
/* 107 */       return decodeBufferLoop(paramByteBuffer, paramCharBuffer);
/*     */     }
/*     */ 
/*     */     private final char decode(int paramInt) {
/* 111 */       return this.b2c[(paramInt + 128)];
/*     */     }
/*     */ 
/*     */     protected void implReplaceWith(String paramString)
/*     */     {
/* 116 */       this.repl = paramString.charAt(0);
/*     */     }
/*     */ 
/*     */     public int decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar) {
/* 120 */       if (paramInt2 > paramArrayOfChar.length)
/* 121 */         paramInt2 = paramArrayOfChar.length;
/* 122 */       int i = 0;
/* 123 */       while (i < paramInt2) {
/* 124 */         paramArrayOfChar[i] = decode(paramArrayOfByte[(paramInt1++)]);
/* 125 */         if (paramArrayOfChar[i] == 65533) {
/* 126 */           paramArrayOfChar[i] = this.repl;
/*     */         }
/* 128 */         i++;
/*     */       }
/* 130 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Encoder extends CharsetEncoder
/*     */     implements ArrayEncoder
/*     */   {
/*     */     private Surrogate.Parser sgp;
/*     */     private final char[] c2b;
/*     */     private final char[] c2bIndex;
/* 231 */     private byte repl = 63;
/*     */ 
/*     */     public Encoder(Charset paramCharset, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
/*     */     {
/* 141 */       super(1.0F, 1.0F);
/* 142 */       this.c2b = paramArrayOfChar1;
/* 143 */       this.c2bIndex = paramArrayOfChar2;
/*     */     }
/*     */ 
/*     */     public boolean canEncode(char paramChar) {
/* 147 */       return encode(paramChar) != 65533;
/*     */     }
/*     */ 
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte) {
/* 151 */       return ((paramArrayOfByte.length == 1) && (paramArrayOfByte[0] == 63)) || (super.isLegalReplacement(paramArrayOfByte));
/*     */     }
/*     */ 
/*     */     private CoderResult encodeArrayLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 156 */       char[] arrayOfChar = paramCharBuffer.array();
/* 157 */       int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/* 158 */       int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*     */ 
/* 160 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 161 */       int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/* 162 */       int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*     */ 
/* 164 */       CoderResult localCoderResult = CoderResult.UNDERFLOW;
/* 165 */       if (m - k < j - i) {
/* 166 */         j = i + (m - k);
/* 167 */         localCoderResult = CoderResult.OVERFLOW;
/*     */       }
/*     */ 
/* 170 */       while (i < j) {
/* 171 */         char c = arrayOfChar[i];
/* 172 */         int n = encode(c);
/* 173 */         if (n == 65533) {
/* 174 */           if (Character.isSurrogate(c)) {
/* 175 */             if (this.sgp == null)
/* 176 */               this.sgp = new Surrogate.Parser();
/* 177 */             if (this.sgp.parse(c, arrayOfChar, i, j) < 0)
/* 178 */               return SingleByte.withResult(this.sgp.error(), paramCharBuffer, i, paramByteBuffer, k);
/* 179 */             return SingleByte.withResult(this.sgp.unmappableResult(), paramCharBuffer, i, paramByteBuffer, k);
/*     */           }
/* 181 */           return SingleByte.withResult(CoderResult.unmappableForLength(1), paramCharBuffer, i, paramByteBuffer, k);
/*     */         }
/*     */ 
/* 184 */         arrayOfByte[(k++)] = ((byte)n);
/* 185 */         i++;
/*     */       }
/* 187 */       return SingleByte.withResult(localCoderResult, paramCharBuffer, i, paramByteBuffer, k);
/*     */     }
/*     */ 
/*     */     private CoderResult encodeBufferLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/* 191 */       int i = paramCharBuffer.position();
/*     */       try {
/* 193 */         while (paramCharBuffer.hasRemaining()) {
/* 194 */           char c = paramCharBuffer.get();
/* 195 */           int j = encode(c);
/*     */           CoderResult localCoderResult2;
/* 196 */           if (j == 65533) {
/* 197 */             if (Character.isSurrogate(c)) {
/* 198 */               if (this.sgp == null)
/* 199 */                 this.sgp = new Surrogate.Parser();
/* 200 */               if (this.sgp.parse(c, paramCharBuffer) < 0)
/* 201 */                 return this.sgp.error();
/* 202 */               return this.sgp.unmappableResult();
/*     */             }
/* 204 */             return CoderResult.unmappableForLength(1);
/*     */           }
/* 206 */           if (!paramByteBuffer.hasRemaining())
/* 207 */             return CoderResult.OVERFLOW;
/* 208 */           paramByteBuffer.put((byte)j);
/* 209 */           i++;
/*     */         }
/* 211 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/* 213 */         paramCharBuffer.position(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/* 218 */       if ((paramCharBuffer.hasArray()) && (paramByteBuffer.hasArray())) {
/* 219 */         return encodeArrayLoop(paramCharBuffer, paramByteBuffer);
/*     */       }
/* 221 */       return encodeBufferLoop(paramCharBuffer, paramByteBuffer);
/*     */     }
/*     */ 
/*     */     private final int encode(char paramChar) {
/* 225 */       int i = this.c2bIndex[(paramChar >> '\b')];
/* 226 */       if (i == 65533)
/* 227 */         return 65533;
/* 228 */       return this.c2b[(i + (paramChar & 0xFF))];
/*     */     }
/*     */ 
/*     */     protected void implReplaceWith(byte[] paramArrayOfByte)
/*     */     {
/* 233 */       this.repl = paramArrayOfByte[0];
/*     */     }
/*     */ 
/*     */     public int encode(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
/* 237 */       int i = 0;
/* 238 */       int j = paramInt1 + Math.min(paramInt2, paramArrayOfByte.length);
/* 239 */       while (paramInt1 < j) {
/* 240 */         char c = paramArrayOfChar[(paramInt1++)];
/* 241 */         int k = encode(c);
/* 242 */         if (k != 65533) {
/* 243 */           paramArrayOfByte[(i++)] = ((byte)k);
/*     */         }
/*     */         else {
/* 246 */           if ((Character.isHighSurrogate(c)) && (paramInt1 < j) && (Character.isLowSurrogate(paramArrayOfChar[paramInt1])))
/*     */           {
/* 248 */             if (paramInt2 > paramArrayOfByte.length) {
/* 249 */               j++;
/* 250 */               paramInt2--;
/*     */             }
/* 252 */             paramInt1++;
/*     */           }
/* 254 */           paramArrayOfByte[(i++)] = this.repl;
/*     */         }
/*     */       }
/* 256 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.SingleByte
 * JD-Core Version:    0.6.2
 */