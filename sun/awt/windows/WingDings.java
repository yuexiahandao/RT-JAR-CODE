/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ public class WingDings extends Charset
/*     */ {
/*     */   public WingDings()
/*     */   {
/*  34 */     super("WingDings", null);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  38 */     return new Encoder(this);
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder()
/*     */   {
/*  45 */     throw new Error("Decoder isn't implemented for WingDings Charset");
/*     */   }
/*     */ 
/*     */   public boolean contains(Charset paramCharset) {
/*  49 */     return paramCharset instanceof WingDings;
/*     */   }
/*     */ 
/*     */   private static class Encoder extends CharsetEncoder
/*     */   {
/*  96 */     private static byte[] table = { 0, 35, 34, 0, 0, 0, 41, 62, 81, 42, 0, 0, 65, 63, 0, 0, 0, 0, 0, -4, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 86, 0, 88, 89, 0, 0, 0, 0, 0, 0, 0, 0, -75, 0, 0, 0, 0, 0, -74, 0, 0, 0, -83, -81, -84, 0, 0, 0, 0, 0, 0, 0, 0, 124, 123, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, -90, 0, 0, 0, 113, 114, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, -40, 0, 0, -60, -58, 0, 0, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*     */     public Encoder(Charset paramCharset)
/*     */     {
/*  54 */       super(1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     public boolean canEncode(char paramChar) {
/*  58 */       if ((paramChar >= '✁') && (paramChar <= '➾')) {
/*  59 */         if (table[(paramChar - '✀')] != 0) {
/*  60 */           return true;
/*     */         }
/*  62 */         return false;
/*     */       }
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/*  68 */       char[] arrayOfChar = paramCharBuffer.array();
/*  69 */       int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/*  70 */       int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*  71 */       assert (i <= j);
/*  72 */       i = i <= j ? i : j;
/*  73 */       byte[] arrayOfByte = paramByteBuffer.array();
/*  74 */       int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/*  75 */       int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*  76 */       assert (k <= m);
/*  77 */       k = k <= m ? k : m;
/*     */       try
/*     */       {
/*  80 */         while (i < j) {
/*  81 */           int n = arrayOfChar[i];
/*     */           CoderResult localCoderResult2;
/*  82 */           if (m - k < 1)
/*  83 */             return CoderResult.OVERFLOW;
/*  84 */           if (!canEncode(n))
/*  85 */             return CoderResult.unmappableForLength(1);
/*  86 */           i++;
/*  87 */           arrayOfByte[(k++)] = table[(n - 9984)];
/*     */         }
/*  89 */         return CoderResult.UNDERFLOW;
/*     */       } finally {
/*  91 */         paramCharBuffer.position(i - paramCharBuffer.arrayOffset());
/*  92 */         paramByteBuffer.position(k - paramByteBuffer.arrayOffset());
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte)
/*     */     {
/* 160 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WingDings
 * JD-Core Version:    0.6.2
 */