/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactorySpi;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.RSAKey;
/*     */ import java.security.interfaces.RSAPrivateCrtKey;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.RSAPrivateCrtKeySpec;
/*     */ import java.security.spec.RSAPrivateKeySpec;
/*     */ import java.security.spec.RSAPublicKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public final class RSAKeyFactory extends KeyFactorySpi
/*     */ {
/*  63 */   private static final Class<?> rsaPublicKeySpecClass = RSAPublicKeySpec.class;
/*     */ 
/*  65 */   private static final Class<?> rsaPrivateKeySpecClass = RSAPrivateKeySpec.class;
/*     */ 
/*  67 */   private static final Class<?> rsaPrivateCrtKeySpecClass = RSAPrivateCrtKeySpec.class;
/*     */ 
/*  70 */   private static final Class<?> x509KeySpecClass = X509EncodedKeySpec.class;
/*  71 */   private static final Class<?> pkcs8KeySpecClass = PKCS8EncodedKeySpec.class;
/*     */   public static final int MIN_MODLEN = 512;
/*     */   public static final int MAX_MODLEN = 16384;
/*     */   public static final int MAX_MODLEN_RESTRICT_EXP = 3072;
/*     */   public static final int MAX_RESTRICTED_EXPLEN = 64;
/*  86 */   private static final boolean restrictExpLen = "true".equalsIgnoreCase((String)AccessController.doPrivileged(new GetPropertyAction("sun.security.rsa.restrictRSAExponent", "true")));
/*     */ 
/*  92 */   private static final RSAKeyFactory INSTANCE = new RSAKeyFactory();
/*     */ 
/*     */   public static RSAKey toRSAKey(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 106 */     if (((paramKey instanceof RSAPrivateKeyImpl)) || ((paramKey instanceof RSAPrivateCrtKeyImpl)) || ((paramKey instanceof RSAPublicKeyImpl)))
/*     */     {
/* 109 */       return (RSAKey)paramKey;
/*     */     }
/* 111 */     return (RSAKey)INSTANCE.engineTranslateKey(paramKey);
/*     */   }
/*     */ 
/*     */   static void checkRSAProviderKeyLengths(int paramInt, BigInteger paramBigInteger)
/*     */     throws InvalidKeyException
/*     */   {
/* 125 */     checkKeyLengths(paramInt + 7 & 0xFFFFFFF8, paramBigInteger, 512, 2147483647);
/*     */   }
/*     */ 
/*     */   public static void checkKeyLengths(int paramInt1, BigInteger paramBigInteger, int paramInt2, int paramInt3)
/*     */     throws InvalidKeyException
/*     */   {
/* 146 */     if ((paramInt2 > 0) && (paramInt1 < paramInt2)) {
/* 147 */       throw new InvalidKeyException("RSA keys must be at least " + paramInt2 + " bits long");
/*     */     }
/*     */ 
/* 154 */     int i = Math.min(paramInt3, 16384);
/*     */ 
/* 158 */     if (paramInt1 > i) {
/* 159 */       throw new InvalidKeyException("RSA keys must be no longer than " + i + " bits");
/*     */     }
/*     */ 
/* 164 */     if ((restrictExpLen) && (paramBigInteger != null) && (paramInt1 > 3072) && (paramBigInteger.bitLength() > 64))
/*     */     {
/* 167 */       throw new InvalidKeyException("RSA exponents can be no longer than 64 bits  if modulus is greater than 3072 bits");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Key engineTranslateKey(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 181 */     if (paramKey == null) {
/* 182 */       throw new InvalidKeyException("Key must not be null");
/*     */     }
/* 184 */     String str = paramKey.getAlgorithm();
/* 185 */     if (!str.equals("RSA")) {
/* 186 */       throw new InvalidKeyException("Not an RSA key: " + str);
/*     */     }
/* 188 */     if ((paramKey instanceof PublicKey))
/* 189 */       return translatePublicKey((PublicKey)paramKey);
/* 190 */     if ((paramKey instanceof PrivateKey)) {
/* 191 */       return translatePrivateKey((PrivateKey)paramKey);
/*     */     }
/* 193 */     throw new InvalidKeyException("Neither a public nor a private key");
/*     */   }
/*     */ 
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 201 */       return generatePublic(paramKeySpec);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 203 */       throw localInvalidKeySpecException;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 205 */       throw new InvalidKeySpecException(localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 213 */       return generatePrivate(paramKeySpec);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 215 */       throw localInvalidKeySpecException;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 217 */       throw new InvalidKeySpecException(localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private PublicKey translatePublicKey(PublicKey paramPublicKey)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     Object localObject;
/* 224 */     if ((paramPublicKey instanceof RSAPublicKey)) {
/* 225 */       if ((paramPublicKey instanceof RSAPublicKeyImpl)) {
/* 226 */         return paramPublicKey;
/*     */       }
/* 228 */       localObject = (RSAPublicKey)paramPublicKey;
/*     */       try {
/* 230 */         return new RSAPublicKeyImpl(((RSAPublicKey)localObject).getModulus(), ((RSAPublicKey)localObject).getPublicExponent());
/*     */       }
/*     */       catch (RuntimeException localRuntimeException)
/*     */       {
/* 236 */         throw new InvalidKeyException("Invalid key", localRuntimeException);
/*     */       }
/*     */     }
/* 238 */     if ("X.509".equals(paramPublicKey.getFormat())) {
/* 239 */       localObject = paramPublicKey.getEncoded();
/* 240 */       return new RSAPublicKeyImpl((byte[])localObject);
/*     */     }
/* 242 */     throw new InvalidKeyException("Public keys must be instance of RSAPublicKey or have X.509 encoding");
/*     */   }
/*     */ 
/*     */   private PrivateKey translatePrivateKey(PrivateKey paramPrivateKey)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     Object localObject;
/* 250 */     if ((paramPrivateKey instanceof RSAPrivateCrtKey)) {
/* 251 */       if ((paramPrivateKey instanceof RSAPrivateCrtKeyImpl)) {
/* 252 */         return paramPrivateKey;
/*     */       }
/* 254 */       localObject = (RSAPrivateCrtKey)paramPrivateKey;
/*     */       try {
/* 256 */         return new RSAPrivateCrtKeyImpl(((RSAPrivateCrtKey)localObject).getModulus(), ((RSAPrivateCrtKey)localObject).getPublicExponent(), ((RSAPrivateCrtKey)localObject).getPrivateExponent(), ((RSAPrivateCrtKey)localObject).getPrimeP(), ((RSAPrivateCrtKey)localObject).getPrimeQ(), ((RSAPrivateCrtKey)localObject).getPrimeExponentP(), ((RSAPrivateCrtKey)localObject).getPrimeExponentQ(), ((RSAPrivateCrtKey)localObject).getCrtCoefficient());
/*     */       }
/*     */       catch (RuntimeException localRuntimeException1)
/*     */       {
/* 268 */         throw new InvalidKeyException("Invalid key", localRuntimeException1);
/*     */       }
/*     */     }
/* 270 */     if ((paramPrivateKey instanceof RSAPrivateKey)) {
/* 271 */       if ((paramPrivateKey instanceof RSAPrivateKeyImpl)) {
/* 272 */         return paramPrivateKey;
/*     */       }
/* 274 */       localObject = (RSAPrivateKey)paramPrivateKey;
/*     */       try {
/* 276 */         return new RSAPrivateKeyImpl(((RSAPrivateKey)localObject).getModulus(), ((RSAPrivateKey)localObject).getPrivateExponent());
/*     */       }
/*     */       catch (RuntimeException localRuntimeException2)
/*     */       {
/* 282 */         throw new InvalidKeyException("Invalid key", localRuntimeException2);
/*     */       }
/*     */     }
/* 284 */     if ("PKCS#8".equals(paramPrivateKey.getFormat())) {
/* 285 */       localObject = paramPrivateKey.getEncoded();
/* 286 */       return RSAPrivateCrtKeyImpl.newKey((byte[])localObject);
/*     */     }
/* 288 */     throw new InvalidKeyException("Private keys must be instance of RSAPrivate(Crt)Key or have PKCS#8 encoding");
/*     */   }
/*     */ 
/*     */   private PublicKey generatePublic(KeySpec paramKeySpec)
/*     */     throws GeneralSecurityException
/*     */   {
/*     */     Object localObject;
/* 296 */     if ((paramKeySpec instanceof X509EncodedKeySpec)) {
/* 297 */       localObject = (X509EncodedKeySpec)paramKeySpec;
/* 298 */       return new RSAPublicKeyImpl(((X509EncodedKeySpec)localObject).getEncoded());
/* 299 */     }if ((paramKeySpec instanceof RSAPublicKeySpec)) {
/* 300 */       localObject = (RSAPublicKeySpec)paramKeySpec;
/* 301 */       return new RSAPublicKeyImpl(((RSAPublicKeySpec)localObject).getModulus(), ((RSAPublicKeySpec)localObject).getPublicExponent());
/*     */     }
/*     */ 
/* 306 */     throw new InvalidKeySpecException("Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys");
/*     */   }
/*     */ 
/*     */   private PrivateKey generatePrivate(KeySpec paramKeySpec)
/*     */     throws GeneralSecurityException
/*     */   {
/*     */     Object localObject;
/* 314 */     if ((paramKeySpec instanceof PKCS8EncodedKeySpec)) {
/* 315 */       localObject = (PKCS8EncodedKeySpec)paramKeySpec;
/* 316 */       return RSAPrivateCrtKeyImpl.newKey(((PKCS8EncodedKeySpec)localObject).getEncoded());
/* 317 */     }if ((paramKeySpec instanceof RSAPrivateCrtKeySpec)) {
/* 318 */       localObject = (RSAPrivateCrtKeySpec)paramKeySpec;
/* 319 */       return new RSAPrivateCrtKeyImpl(((RSAPrivateCrtKeySpec)localObject).getModulus(), ((RSAPrivateCrtKeySpec)localObject).getPublicExponent(), ((RSAPrivateCrtKeySpec)localObject).getPrivateExponent(), ((RSAPrivateCrtKeySpec)localObject).getPrimeP(), ((RSAPrivateCrtKeySpec)localObject).getPrimeQ(), ((RSAPrivateCrtKeySpec)localObject).getPrimeExponentP(), ((RSAPrivateCrtKeySpec)localObject).getPrimeExponentQ(), ((RSAPrivateCrtKeySpec)localObject).getCrtCoefficient());
/*     */     }
/*     */ 
/* 329 */     if ((paramKeySpec instanceof RSAPrivateKeySpec)) {
/* 330 */       localObject = (RSAPrivateKeySpec)paramKeySpec;
/* 331 */       return new RSAPrivateKeyImpl(((RSAPrivateKeySpec)localObject).getModulus(), ((RSAPrivateKeySpec)localObject).getPrivateExponent());
/*     */     }
/*     */ 
/* 336 */     throw new InvalidKeySpecException("Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys");
/*     */   }
/*     */ 
/*     */   protected <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 347 */       paramKey = engineTranslateKey(paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 349 */       throw new InvalidKeySpecException(localInvalidKeyException);
/*     */     }
/*     */     Object localObject;
/* 351 */     if ((paramKey instanceof RSAPublicKey)) {
/* 352 */       localObject = (RSAPublicKey)paramKey;
/* 353 */       if (rsaPublicKeySpecClass.isAssignableFrom(paramClass)) {
/* 354 */         return new RSAPublicKeySpec(((RSAPublicKey)localObject).getModulus(), ((RSAPublicKey)localObject).getPublicExponent());
/*     */       }
/*     */ 
/* 358 */       if (x509KeySpecClass.isAssignableFrom(paramClass)) {
/* 359 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/* 361 */       throw new InvalidKeySpecException("KeySpec must be RSAPublicKeySpec or X509EncodedKeySpec for RSA public keys");
/*     */     }
/*     */ 
/* 365 */     if ((paramKey instanceof RSAPrivateKey)) {
/* 366 */       if (pkcs8KeySpecClass.isAssignableFrom(paramClass))
/* 367 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/* 368 */       if (rsaPrivateCrtKeySpecClass.isAssignableFrom(paramClass)) {
/* 369 */         if ((paramKey instanceof RSAPrivateCrtKey)) {
/* 370 */           localObject = (RSAPrivateCrtKey)paramKey;
/* 371 */           return new RSAPrivateCrtKeySpec(((RSAPrivateCrtKey)localObject).getModulus(), ((RSAPrivateCrtKey)localObject).getPublicExponent(), ((RSAPrivateCrtKey)localObject).getPrivateExponent(), ((RSAPrivateCrtKey)localObject).getPrimeP(), ((RSAPrivateCrtKey)localObject).getPrimeQ(), ((RSAPrivateCrtKey)localObject).getPrimeExponentP(), ((RSAPrivateCrtKey)localObject).getPrimeExponentQ(), ((RSAPrivateCrtKey)localObject).getCrtCoefficient());
/*     */         }
/*     */ 
/* 382 */         throw new InvalidKeySpecException("RSAPrivateCrtKeySpec can only be used with CRT keys");
/*     */       }
/*     */ 
/* 385 */       if (rsaPrivateKeySpecClass.isAssignableFrom(paramClass)) {
/* 386 */         localObject = (RSAPrivateKey)paramKey;
/* 387 */         return new RSAPrivateKeySpec(((RSAPrivateKey)localObject).getModulus(), ((RSAPrivateKey)localObject).getPrivateExponent());
/*     */       }
/*     */ 
/* 392 */       throw new InvalidKeySpecException("KeySpec must be RSAPrivate(Crt)KeySpec or PKCS8EncodedKeySpec for RSA private keys");
/*     */     }
/*     */ 
/* 398 */     throw new InvalidKeySpecException("Neither public nor private key");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAKeyFactory
 * JD-Core Version:    0.6.2
 */