/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class RSAPrivateCrtKeySpec extends RSAPrivateKeySpec
/*     */ {
/*     */   private final BigInteger publicExponent;
/*     */   private final BigInteger primeP;
/*     */   private final BigInteger primeQ;
/*     */   private final BigInteger primeExponentP;
/*     */   private final BigInteger primeExponentQ;
/*     */   private final BigInteger crtCoefficient;
/*     */ 
/*     */   public RSAPrivateCrtKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
/*     */   {
/*  81 */     super(paramBigInteger1, paramBigInteger3);
/*  82 */     this.publicExponent = paramBigInteger2;
/*  83 */     this.primeP = paramBigInteger4;
/*  84 */     this.primeQ = paramBigInteger5;
/*  85 */     this.primeExponentP = paramBigInteger6;
/*  86 */     this.primeExponentQ = paramBigInteger7;
/*  87 */     this.crtCoefficient = paramBigInteger8;
/*     */   }
/*     */ 
/*     */   public BigInteger getPublicExponent()
/*     */   {
/*  96 */     return this.publicExponent;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeP()
/*     */   {
/* 105 */     return this.primeP;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeQ()
/*     */   {
/* 114 */     return this.primeQ;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentP()
/*     */   {
/* 123 */     return this.primeExponentP;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentQ()
/*     */   {
/* 132 */     return this.primeExponentQ;
/*     */   }
/*     */ 
/*     */   public BigInteger getCrtCoefficient()
/*     */   {
/* 141 */     return this.crtCoefficient;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.RSAPrivateCrtKeySpec
 * JD-Core Version:    0.6.2
 */