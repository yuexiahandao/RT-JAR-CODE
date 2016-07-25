/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class RSAMultiPrimePrivateCrtKeySpec extends RSAPrivateKeySpec
/*     */ {
/*     */   private final BigInteger publicExponent;
/*     */   private final BigInteger primeP;
/*     */   private final BigInteger primeQ;
/*     */   private final BigInteger primeExponentP;
/*     */   private final BigInteger primeExponentQ;
/*     */   private final BigInteger crtCoefficient;
/*     */   private final RSAOtherPrimeInfo[] otherPrimeInfo;
/*     */ 
/*     */   public RSAMultiPrimePrivateCrtKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8, RSAOtherPrimeInfo[] paramArrayOfRSAOtherPrimeInfo)
/*     */   {
/*  98 */     super(paramBigInteger1, paramBigInteger3);
/*  99 */     if (paramBigInteger1 == null) {
/* 100 */       throw new NullPointerException("the modulus parameter must be non-null");
/*     */     }
/*     */ 
/* 103 */     if (paramBigInteger2 == null) {
/* 104 */       throw new NullPointerException("the publicExponent parameter must be non-null");
/*     */     }
/*     */ 
/* 107 */     if (paramBigInteger3 == null) {
/* 108 */       throw new NullPointerException("the privateExponent parameter must be non-null");
/*     */     }
/*     */ 
/* 111 */     if (paramBigInteger4 == null) {
/* 112 */       throw new NullPointerException("the primeP parameter must be non-null");
/*     */     }
/*     */ 
/* 115 */     if (paramBigInteger5 == null) {
/* 116 */       throw new NullPointerException("the primeQ parameter must be non-null");
/*     */     }
/*     */ 
/* 119 */     if (paramBigInteger6 == null) {
/* 120 */       throw new NullPointerException("the primeExponentP parameter must be non-null");
/*     */     }
/*     */ 
/* 123 */     if (paramBigInteger7 == null) {
/* 124 */       throw new NullPointerException("the primeExponentQ parameter must be non-null");
/*     */     }
/*     */ 
/* 127 */     if (paramBigInteger8 == null) {
/* 128 */       throw new NullPointerException("the crtCoefficient parameter must be non-null");
/*     */     }
/*     */ 
/* 131 */     this.publicExponent = paramBigInteger2;
/* 132 */     this.primeP = paramBigInteger4;
/* 133 */     this.primeQ = paramBigInteger5;
/* 134 */     this.primeExponentP = paramBigInteger6;
/* 135 */     this.primeExponentQ = paramBigInteger7;
/* 136 */     this.crtCoefficient = paramBigInteger8;
/* 137 */     if (paramArrayOfRSAOtherPrimeInfo == null) {
/* 138 */       this.otherPrimeInfo = null; } else {
/* 139 */       if (paramArrayOfRSAOtherPrimeInfo.length == 0) {
/* 140 */         throw new IllegalArgumentException("the otherPrimeInfo parameter must not be empty");
/*     */       }
/*     */ 
/* 143 */       this.otherPrimeInfo = ((RSAOtherPrimeInfo[])paramArrayOfRSAOtherPrimeInfo.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public BigInteger getPublicExponent()
/*     */   {
/* 153 */     return this.publicExponent;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeP()
/*     */   {
/* 162 */     return this.primeP;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeQ()
/*     */   {
/* 171 */     return this.primeQ;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentP()
/*     */   {
/* 180 */     return this.primeExponentP;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentQ()
/*     */   {
/* 189 */     return this.primeExponentQ;
/*     */   }
/*     */ 
/*     */   public BigInteger getCrtCoefficient()
/*     */   {
/* 198 */     return this.crtCoefficient;
/*     */   }
/*     */ 
/*     */   public RSAOtherPrimeInfo[] getOtherPrimeInfo()
/*     */   {
/* 209 */     if (this.otherPrimeInfo == null) return null;
/* 210 */     return (RSAOtherPrimeInfo[])this.otherPrimeInfo.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.RSAMultiPrimePrivateCrtKeySpec
 * JD-Core Version:    0.6.2
 */