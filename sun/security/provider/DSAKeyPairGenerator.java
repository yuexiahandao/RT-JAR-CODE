/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.ProviderException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public class DSAKeyPairGenerator extends KeyPairGenerator
/*     */   implements java.security.interfaces.DSAKeyPairGenerator
/*     */ {
/*     */   private int modlen;
/*     */   private boolean forceNewParameters;
/*     */   private DSAParameterSpec params;
/*     */   private SecureRandom random;
/*     */ 
/*     */   public DSAKeyPairGenerator()
/*     */   {
/*  64 */     super("DSA");
/*  65 */     initialize(1024, null);
/*     */   }
/*     */ 
/*     */   private static void checkStrength(int paramInt) {
/*  69 */     if ((paramInt < 512) || (paramInt > 1024) || (paramInt % 64 != 0))
/*  70 */       throw new InvalidParameterException("Modulus size must range from 512 to 1024 and be a multiple of 64");
/*     */   }
/*     */ 
/*     */   public void initialize(int paramInt, SecureRandom paramSecureRandom)
/*     */   {
/*  77 */     checkStrength(paramInt);
/*  78 */     this.random = paramSecureRandom;
/*  79 */     this.modlen = paramInt;
/*  80 */     this.params = null;
/*  81 */     this.forceNewParameters = false;
/*     */   }
/*     */ 
/*     */   public void initialize(int paramInt, boolean paramBoolean, SecureRandom paramSecureRandom)
/*     */   {
/*  89 */     checkStrength(paramInt);
/*  90 */     if (paramBoolean) {
/*  91 */       this.params = null;
/*     */     } else {
/*  93 */       this.params = ParameterCache.getCachedDSAParameterSpec(paramInt);
/*  94 */       if (this.params == null) {
/*  95 */         throw new InvalidParameterException("No precomputed parameters for requested modulus size available");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 100 */     this.modlen = paramInt;
/* 101 */     this.random = paramSecureRandom;
/* 102 */     this.forceNewParameters = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void initialize(DSAParams paramDSAParams, SecureRandom paramSecureRandom)
/*     */   {
/* 111 */     if (paramDSAParams == null) {
/* 112 */       throw new InvalidParameterException("Params must not be null");
/*     */     }
/* 114 */     DSAParameterSpec localDSAParameterSpec = new DSAParameterSpec(paramDSAParams.getP(), paramDSAParams.getQ(), paramDSAParams.getG());
/*     */ 
/* 116 */     initialize0(localDSAParameterSpec, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 131 */     if (!(paramAlgorithmParameterSpec instanceof DSAParameterSpec)) {
/* 132 */       throw new InvalidAlgorithmParameterException("Inappropriate parameter");
/*     */     }
/*     */ 
/* 135 */     initialize0((DSAParameterSpec)paramAlgorithmParameterSpec, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   private void initialize0(DSAParameterSpec paramDSAParameterSpec, SecureRandom paramSecureRandom) {
/* 139 */     int i = paramDSAParameterSpec.getP().bitLength();
/* 140 */     checkStrength(i);
/* 141 */     this.modlen = i;
/* 142 */     this.params = paramDSAParameterSpec;
/* 143 */     this.random = paramSecureRandom;
/* 144 */     this.forceNewParameters = false;
/*     */   }
/*     */ 
/*     */   public KeyPair generateKeyPair()
/*     */   {
/* 152 */     if (this.random == null)
/* 153 */       this.random = JCAUtil.getSecureRandom();
/*     */     DSAParameterSpec localDSAParameterSpec;
/*     */     try
/*     */     {
/* 157 */       if (this.forceNewParameters)
/*     */       {
/* 159 */         localDSAParameterSpec = ParameterCache.getNewDSAParameterSpec(this.modlen, this.random);
/*     */       } else {
/* 161 */         if (this.params == null) {
/* 162 */           this.params = ParameterCache.getDSAParameterSpec(this.modlen, this.random);
/*     */         }
/*     */ 
/* 165 */         localDSAParameterSpec = this.params;
/*     */       }
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 168 */       throw new ProviderException(localGeneralSecurityException);
/*     */     }
/* 170 */     return generateKeyPair(localDSAParameterSpec.getP(), localDSAParameterSpec.getQ(), localDSAParameterSpec.getG(), this.random);
/*     */   }
/*     */ 
/*     */   public KeyPair generateKeyPair(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, SecureRandom paramSecureRandom)
/*     */   {
/* 176 */     BigInteger localBigInteger1 = generateX(paramSecureRandom, paramBigInteger2);
/* 177 */     BigInteger localBigInteger2 = generateY(localBigInteger1, paramBigInteger1, paramBigInteger3);
/*     */     try
/*     */     {
/*     */       Object localObject;
/* 184 */       if (DSAKeyFactory.SERIAL_INTEROP)
/* 185 */         localObject = new DSAPublicKey(localBigInteger2, paramBigInteger1, paramBigInteger2, paramBigInteger3);
/*     */       else {
/* 187 */         localObject = new DSAPublicKeyImpl(localBigInteger2, paramBigInteger1, paramBigInteger2, paramBigInteger3);
/*     */       }
/* 189 */       DSAPrivateKey localDSAPrivateKey = new DSAPrivateKey(localBigInteger1, paramBigInteger1, paramBigInteger2, paramBigInteger3);
/*     */ 
/* 191 */       return new KeyPair((PublicKey)localObject, localDSAPrivateKey);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 194 */       throw new ProviderException(localInvalidKeyException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private BigInteger generateX(SecureRandom paramSecureRandom, BigInteger paramBigInteger)
/*     */   {
/* 205 */     BigInteger localBigInteger = null;
/*     */     while (true) {
/* 207 */       int[] arrayOfInt = new int[5];
/* 208 */       for (int i = 0; i < 5; i++) {
/* 209 */         arrayOfInt[i] = paramSecureRandom.nextInt();
/*     */       }
/* 211 */       localBigInteger = generateX(arrayOfInt, paramBigInteger);
/* 212 */       if ((localBigInteger.signum() > 0) && (localBigInteger.compareTo(paramBigInteger) < 0)) {
/*     */         break;
/*     */       }
/*     */     }
/* 216 */     return localBigInteger;
/*     */   }
/*     */ 
/*     */   BigInteger generateX(int[] paramArrayOfInt, BigInteger paramBigInteger)
/*     */   {
/* 229 */     int[] arrayOfInt1 = { 1732584193, -271733879, -1732584194, 271733878, -1009589776 };
/*     */ 
/* 233 */     int[] arrayOfInt2 = DSA.SHA_7(paramArrayOfInt, arrayOfInt1);
/* 234 */     byte[] arrayOfByte = new byte[arrayOfInt2.length * 4];
/* 235 */     for (int i = 0; i < arrayOfInt2.length; i++) {
/* 236 */       int j = arrayOfInt2[i];
/* 237 */       for (int k = 0; k < 4; k++) {
/* 238 */         arrayOfByte[(i * 4 + k)] = ((byte)(j >>> 24 - k * 8));
/*     */       }
/*     */     }
/* 241 */     BigInteger localBigInteger = new BigInteger(1, arrayOfByte).mod(paramBigInteger);
/* 242 */     return localBigInteger;
/*     */   }
/*     */ 
/*     */   BigInteger generateY(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */   {
/* 253 */     BigInteger localBigInteger = paramBigInteger3.modPow(paramBigInteger1, paramBigInteger2);
/* 254 */     return localBigInteger;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAKeyPairGenerator
 * JD-Core Version:    0.6.2
 */