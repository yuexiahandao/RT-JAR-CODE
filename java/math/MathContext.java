/*     */ package java.math;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StreamCorruptedException;
/*     */ 
/*     */ public final class MathContext
/*     */   implements Serializable
/*     */ {
/*     */   private static final int DEFAULT_DIGITS = 9;
/*  62 */   private static final RoundingMode DEFAULT_ROUNDINGMODE = RoundingMode.HALF_UP;
/*     */   private static final int MIN_DIGITS = 0;
/*     */   private static final long serialVersionUID = 5579720004786848255L;
/*  78 */   public static final MathContext UNLIMITED = new MathContext(0, RoundingMode.HALF_UP);
/*     */ 
/*  87 */   public static final MathContext DECIMAL32 = new MathContext(7, RoundingMode.HALF_EVEN);
/*     */ 
/*  96 */   public static final MathContext DECIMAL64 = new MathContext(16, RoundingMode.HALF_EVEN);
/*     */ 
/* 105 */   public static final MathContext DECIMAL128 = new MathContext(34, RoundingMode.HALF_EVEN);
/*     */   final int precision;
/*     */   final RoundingMode roundingMode;
/*     */ 
/*     */   public MathContext(int paramInt)
/*     */   {
/* 141 */     this(paramInt, DEFAULT_ROUNDINGMODE);
/*     */   }
/*     */ 
/*     */   public MathContext(int paramInt, RoundingMode paramRoundingMode)
/*     */   {
/* 157 */     if (paramInt < 0)
/* 158 */       throw new IllegalArgumentException("Digits < 0");
/* 159 */     if (paramRoundingMode == null) {
/* 160 */       throw new NullPointerException("null RoundingMode");
/*     */     }
/* 162 */     this.precision = paramInt;
/* 163 */     this.roundingMode = paramRoundingMode;
/*     */   }
/*     */ 
/*     */   public MathContext(String paramString)
/*     */   {
/* 183 */     int i = 0;
/*     */ 
/* 185 */     if (paramString == null)
/* 186 */       throw new NullPointerException("null String"); int j;
/*     */     try {
/* 188 */       if (!paramString.startsWith("precision=")) throw new RuntimeException();
/* 189 */       int k = paramString.indexOf(' ');
/* 190 */       int m = 10;
/* 191 */       j = Integer.parseInt(paramString.substring(10, k));
/*     */ 
/* 193 */       if (!paramString.startsWith("roundingMode=", k + 1))
/* 194 */         throw new RuntimeException();
/* 195 */       m = k + 1 + 13;
/* 196 */       String str = paramString.substring(m, paramString.length());
/* 197 */       this.roundingMode = RoundingMode.valueOf(str);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 199 */       throw new IllegalArgumentException("bad string format");
/*     */     }
/*     */ 
/* 202 */     if (j < 0) {
/* 203 */       throw new IllegalArgumentException("Digits < 0");
/*     */     }
/* 205 */     this.precision = j;
/*     */   }
/*     */ 
/*     */   public int getPrecision()
/*     */   {
/* 216 */     return this.precision;
/*     */   }
/*     */ 
/*     */   public RoundingMode getRoundingMode()
/*     */   {
/* 236 */     return this.roundingMode;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 251 */     if (!(paramObject instanceof MathContext))
/* 252 */       return false;
/* 253 */     MathContext localMathContext = (MathContext)paramObject;
/* 254 */     return (localMathContext.precision == this.precision) && (localMathContext.roundingMode == this.roundingMode);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 264 */     return this.precision + this.roundingMode.hashCode() * 59;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 300 */     return "precision=" + this.precision + " " + "roundingMode=" + this.roundingMode.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 314 */     paramObjectInputStream.defaultReadObject();
/*     */     String str;
/* 316 */     if (this.precision < 0) {
/* 317 */       str = "MathContext: invalid digits in stream";
/* 318 */       throw new StreamCorruptedException(str);
/*     */     }
/* 320 */     if (this.roundingMode == null) {
/* 321 */       str = "MathContext: null roundingMode in stream";
/* 322 */       throw new StreamCorruptedException(str);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.MathContext
 * JD-Core Version:    0.6.2
 */