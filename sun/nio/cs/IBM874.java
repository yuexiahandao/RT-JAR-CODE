/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ 
/*     */ public class IBM874 extends Charset
/*     */   implements HistoricallyNamedCharset
/*     */ {
/*     */   private static final String b2cTable = "";
/* 105 */   private static final char[] b2c = "".toCharArray();
/* 106 */   private static final char[] c2b = new char[1024];
/* 107 */   private static final char[] c2bIndex = new char[256];
/*     */ 
/*     */   public IBM874()
/*     */   {
/*  39 */     super("x-IBM874", StandardCharsets.aliases_IBM874);
/*     */   }
/*     */ 
/*     */   public String historicalName() {
/*  43 */     return "Cp874";
/*     */   }
/*     */ 
/*     */   public boolean contains(Charset paramCharset) {
/*  47 */     return paramCharset instanceof IBM874;
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder() {
/*  51 */     return new SingleByte.Decoder(this, b2c);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  55 */     return new SingleByte.Encoder(this, c2b, c2bIndex);
/*     */   }
/*     */ 
/*     */   public String getDecoderSingleByteMappings() {
/*  59 */     return "";
/*     */   }
/*     */ 
/*     */   public char[] getEncoderIndex2() {
/*  63 */     return c2b;
/*     */   }
/*     */ 
/*     */   public char[] getEncoderIndex1() {
/*  67 */     return c2bIndex;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 110 */     char[] arrayOfChar1 = b2c;
/* 111 */     char[] arrayOfChar2 = null;
/*     */ 
/* 113 */     arrayOfChar1 = "".toCharArray();
/* 114 */     arrayOfChar1[32] = 65533;
/* 115 */     arrayOfChar1[91] = 65533;
/* 116 */     arrayOfChar1[92] = 65533;
/* 117 */     arrayOfChar1[93] = 65533;
/* 118 */     arrayOfChar1[94] = 65533;
/*     */ 
/* 120 */     SingleByte.initC2B(arrayOfChar1, arrayOfChar2, c2b, c2bIndex);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.IBM874
 * JD-Core Version:    0.6.2
 */