/*     */ package sun.awt;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ 
/*     */ public class AWTCharset extends Charset
/*     */ {
/*     */   protected Charset awtCs;
/*     */   protected Charset javaCs;
/*     */ 
/*     */   public AWTCharset(String paramString, Charset paramCharset)
/*     */   {
/*  41 */     super(paramString, null);
/*  42 */     this.javaCs = paramCharset;
/*  43 */     this.awtCs = this;
/*     */   }
/*     */ 
/*     */   public boolean contains(Charset paramCharset) {
/*  47 */     if (this.javaCs == null) return false;
/*  48 */     return this.javaCs.contains(paramCharset);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  52 */     if (this.javaCs == null)
/*  53 */       throw new Error("Encoder is not supported by this Charset");
/*  54 */     return new Encoder(this.javaCs.newEncoder());
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder() {
/*  58 */     if (this.javaCs == null)
/*  59 */       throw new Error("Decoder is not supported by this Charset");
/*  60 */     return new Decoder(this.javaCs.newDecoder());
/*     */   }
/*     */ 
/*     */   public class Decoder extends CharsetDecoder
/*     */   {
/*     */     protected CharsetDecoder dec;
/*     */     private String nr;
/* 121 */     ByteBuffer fbb = ByteBuffer.allocate(0);
/*     */ 
/*     */     protected Decoder()
/*     */     {
/* 109 */       this(AWTCharset.this.javaCs.newDecoder());
/*     */     }
/*     */ 
/*     */     protected Decoder(CharsetDecoder arg2) {
/* 113 */       super(localObject.averageCharsPerByte(), localObject.maxCharsPerByte());
/*     */ 
/* 116 */       this.dec = localObject;
/*     */     }
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
/* 119 */       return this.dec.decode(paramByteBuffer, paramCharBuffer, true);
/*     */     }
/*     */ 
/*     */     protected CoderResult implFlush(CharBuffer paramCharBuffer) {
/* 123 */       this.dec.decode(this.fbb, paramCharBuffer, true);
/* 124 */       return this.dec.flush(paramCharBuffer);
/*     */     }
/*     */     protected void implReset() {
/* 127 */       this.dec.reset();
/*     */     }
/*     */     protected void implReplaceWith(String paramString) {
/* 130 */       if (this.dec != null)
/* 131 */         this.dec.replaceWith(paramString); 
/*     */     }
/*     */ 
/* 134 */     protected void implOnMalformedInput(CodingErrorAction paramCodingErrorAction) { this.dec.onMalformedInput(paramCodingErrorAction); }
/*     */ 
/*     */     protected void implOnUnmappableCharacter(CodingErrorAction paramCodingErrorAction) {
/* 137 */       this.dec.onUnmappableCharacter(paramCodingErrorAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class Encoder extends CharsetEncoder
/*     */   {
/*     */     protected CharsetEncoder enc;
/*     */ 
/*     */     protected Encoder()
/*     */     {
/*  66 */       this(AWTCharset.this.javaCs.newEncoder());
/*     */     }
/*     */     protected Encoder(CharsetEncoder arg2) {
/*  69 */       super(localObject.averageBytesPerChar(), localObject.maxBytesPerChar());
/*     */ 
/*  72 */       this.enc = localObject;
/*     */     }
/*     */     public boolean canEncode(char paramChar) {
/*  75 */       return this.enc.canEncode(paramChar);
/*     */     }
/*     */     public boolean canEncode(CharSequence paramCharSequence) {
/*  78 */       return this.enc.canEncode(paramCharSequence);
/*     */     }
/*     */     protected CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer) {
/*  81 */       return this.enc.encode(paramCharBuffer, paramByteBuffer, true);
/*     */     }
/*     */     protected CoderResult implFlush(ByteBuffer paramByteBuffer) {
/*  84 */       return this.enc.flush(paramByteBuffer);
/*     */     }
/*     */     protected void implReset() {
/*  87 */       this.enc.reset();
/*     */     }
/*     */     protected void implReplaceWith(byte[] paramArrayOfByte) {
/*  90 */       if (this.enc != null)
/*  91 */         this.enc.replaceWith(paramArrayOfByte); 
/*     */     }
/*     */ 
/*  94 */     protected void implOnMalformedInput(CodingErrorAction paramCodingErrorAction) { this.enc.onMalformedInput(paramCodingErrorAction); }
/*     */ 
/*     */     protected void implOnUnmappableCharacter(CodingErrorAction paramCodingErrorAction) {
/*  97 */       this.enc.onUnmappableCharacter(paramCodingErrorAction);
/*     */     }
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte) {
/* 100 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.AWTCharset
 * JD-Core Version:    0.6.2
 */