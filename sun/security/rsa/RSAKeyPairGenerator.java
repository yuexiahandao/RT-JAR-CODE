/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGeneratorSpi;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.RSAKeyGenParameterSpec;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public final class RSAKeyPairGenerator extends KeyPairGeneratorSpi
/*     */ {
/*     */   private BigInteger publicExponent;
/*     */   private int keySize;
/*     */   private SecureRandom random;
/*     */ 
/*     */   public RSAKeyPairGenerator()
/*     */   {
/*  58 */     initialize(1024, null);
/*     */   }
/*     */ 
/*     */   public void initialize(int paramInt, SecureRandom paramSecureRandom)
/*     */   {
/*     */     try
/*     */     {
/*  67 */       RSAKeyFactory.checkKeyLengths(paramInt, RSAKeyGenParameterSpec.F4, 512, 65536);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/*  70 */       throw new InvalidParameterException(localInvalidKeyException.getMessage());
/*     */     }
/*     */ 
/*  73 */     this.keySize = paramInt;
/*  74 */     this.random = paramSecureRandom;
/*  75 */     this.publicExponent = RSAKeyGenParameterSpec.F4;
/*     */   }
/*     */ 
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  82 */     if (!(paramAlgorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
/*  83 */       throw new InvalidAlgorithmParameterException("Params must be instance of RSAKeyGenParameterSpec");
/*     */     }
/*     */ 
/*  87 */     RSAKeyGenParameterSpec localRSAKeyGenParameterSpec = (RSAKeyGenParameterSpec)paramAlgorithmParameterSpec;
/*  88 */     int i = localRSAKeyGenParameterSpec.getKeysize();
/*  89 */     BigInteger localBigInteger = localRSAKeyGenParameterSpec.getPublicExponent();
/*     */ 
/*  91 */     if (localBigInteger == null) {
/*  92 */       localBigInteger = RSAKeyGenParameterSpec.F4;
/*     */     } else {
/*  94 */       if (localBigInteger.compareTo(RSAKeyGenParameterSpec.F0) < 0) {
/*  95 */         throw new InvalidAlgorithmParameterException("Public exponent must be 3 or larger");
/*     */       }
/*     */ 
/*  98 */       if (localBigInteger.bitLength() > i) {
/*  99 */         throw new InvalidAlgorithmParameterException("Public exponent must be smaller than key size");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 106 */       RSAKeyFactory.checkKeyLengths(i, localBigInteger, 512, 65536);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 109 */       throw new InvalidAlgorithmParameterException("Invalid key sizes", localInvalidKeyException);
/*     */     }
/*     */ 
/* 113 */     this.keySize = i;
/* 114 */     this.publicExponent = localBigInteger;
/* 115 */     this.random = paramSecureRandom;
/*     */   }
/*     */ 
/*     */   public KeyPair generateKeyPair()
/*     */   {
/* 121 */     int i = this.keySize + 1 >> 1;
/* 122 */     int j = this.keySize - i;
/* 123 */     if (this.random == null) {
/* 124 */       this.random = JCAUtil.getSecureRandom(); } BigInteger localBigInteger1 = this.publicExponent;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/*     */     BigInteger localBigInteger2;
/*     */     BigInteger localBigInteger3;
/*     */     BigInteger localBigInteger4;
/*     */     do { localObject1 = BigInteger.probablePrime(i, this.random);
/*     */       do
/*     */       {
/* 132 */         localObject2 = BigInteger.probablePrime(j, this.random);
/*     */ 
/* 134 */         if (((BigInteger)localObject1).compareTo((BigInteger)localObject2) < 0) {
/* 135 */           localObject3 = localObject1;
/* 136 */           localObject1 = localObject2;
/* 137 */           localObject2 = localObject3;
/*     */         }
/*     */ 
/* 140 */         localBigInteger2 = ((BigInteger)localObject1).multiply((BigInteger)localObject2);
/*     */       }
/*     */ 
/* 143 */       while (localBigInteger2.bitLength() < this.keySize);
/*     */ 
/* 147 */       localObject3 = ((BigInteger)localObject1).subtract(BigInteger.ONE);
/* 148 */       localBigInteger3 = ((BigInteger)localObject2).subtract(BigInteger.ONE);
/* 149 */       localBigInteger4 = ((BigInteger)localObject3).multiply(localBigInteger3);
/*     */     }
/*     */ 
/* 152 */     while (!localBigInteger1.gcd(localBigInteger4).equals(BigInteger.ONE));
/*     */ 
/* 157 */     BigInteger localBigInteger5 = localBigInteger1.modInverse(localBigInteger4);
/*     */ 
/* 160 */     BigInteger localBigInteger6 = localBigInteger5.mod((BigInteger)localObject3);
/*     */ 
/* 162 */     BigInteger localBigInteger7 = localBigInteger5.mod(localBigInteger3);
/*     */ 
/* 165 */     BigInteger localBigInteger8 = ((BigInteger)localObject2).modInverse((BigInteger)localObject1);
/*     */     try
/*     */     {
/* 168 */       RSAPublicKeyImpl localRSAPublicKeyImpl = new RSAPublicKeyImpl(localBigInteger2, localBigInteger1);
/* 169 */       RSAPrivateCrtKeyImpl localRSAPrivateCrtKeyImpl = new RSAPrivateCrtKeyImpl(localBigInteger2, localBigInteger1, localBigInteger5, (BigInteger)localObject1, (BigInteger)localObject2, localBigInteger6, localBigInteger7, localBigInteger8);
/*     */ 
/* 171 */       return new KeyPair(localRSAPublicKeyImpl, localRSAPrivateCrtKeyImpl);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 175 */       throw new RuntimeException(localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAKeyPairGenerator
 * JD-Core Version:    0.6.2
 */