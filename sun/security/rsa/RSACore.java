/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.interfaces.RSAKey;
/*     */ import java.security.interfaces.RSAPrivateCrtKey;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public final class RSACore
/*     */ {
/*     */   private static final boolean ENABLE_BLINDING = true;
/*  60 */   private static final Map<BigInteger, BlindingParameters> blindingCache = new WeakHashMap();
/*     */ 
/*     */   public static int getByteLength(BigInteger paramBigInteger)
/*     */   {
/*  72 */     int i = paramBigInteger.bitLength();
/*  73 */     return i + 7 >> 3;
/*     */   }
/*     */ 
/*     */   public static int getByteLength(RSAKey paramRSAKey)
/*     */   {
/*  81 */     return getByteLength(paramRSAKey.getModulus());
/*     */   }
/*     */ 
/*     */   public static byte[] convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  86 */     if ((paramInt1 == 0) && (paramInt2 == paramArrayOfByte.length)) {
/*  87 */       return paramArrayOfByte;
/*     */     }
/*  89 */     byte[] arrayOfByte = new byte[paramInt2];
/*  90 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/*  91 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] rsa(byte[] paramArrayOfByte, RSAPublicKey paramRSAPublicKey)
/*     */     throws BadPaddingException
/*     */   {
/* 100 */     return crypt(paramArrayOfByte, paramRSAPublicKey.getModulus(), paramRSAPublicKey.getPublicExponent());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static byte[] rsa(byte[] paramArrayOfByte, RSAPrivateKey paramRSAPrivateKey)
/*     */     throws BadPaddingException
/*     */   {
/* 111 */     return rsa(paramArrayOfByte, paramRSAPrivateKey, true);
/*     */   }
/*     */ 
/*     */   public static byte[] rsa(byte[] paramArrayOfByte, RSAPrivateKey paramRSAPrivateKey, boolean paramBoolean)
/*     */     throws BadPaddingException
/*     */   {
/* 121 */     if ((paramRSAPrivateKey instanceof RSAPrivateCrtKey)) {
/* 122 */       return crtCrypt(paramArrayOfByte, (RSAPrivateCrtKey)paramRSAPrivateKey, paramBoolean);
/*     */     }
/* 124 */     return priCrypt(paramArrayOfByte, paramRSAPrivateKey.getModulus(), paramRSAPrivateKey.getPrivateExponent());
/*     */   }
/*     */ 
/*     */   private static byte[] crypt(byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */     throws BadPaddingException
/*     */   {
/* 133 */     BigInteger localBigInteger1 = parseMsg(paramArrayOfByte, paramBigInteger1);
/* 134 */     BigInteger localBigInteger2 = localBigInteger1.modPow(paramBigInteger2, paramBigInteger1);
/* 135 */     return toByteArray(localBigInteger2, getByteLength(paramBigInteger1));
/*     */   }
/*     */ 
/*     */   private static byte[] priCrypt(byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */     throws BadPaddingException
/*     */   {
/* 144 */     BigInteger localBigInteger1 = parseMsg(paramArrayOfByte, paramBigInteger1);
/* 145 */     BlindingRandomPair localBlindingRandomPair = null;
/*     */ 
/* 148 */     localBlindingRandomPair = getBlindingRandomPair(null, paramBigInteger2, paramBigInteger1);
/* 149 */     localBigInteger1 = localBigInteger1.multiply(localBlindingRandomPair.u).mod(paramBigInteger1);
/* 150 */     BigInteger localBigInteger2 = localBigInteger1.modPow(paramBigInteger2, paramBigInteger1);
/* 151 */     localBigInteger2 = localBigInteger2.multiply(localBlindingRandomPair.v).mod(paramBigInteger1);
/*     */ 
/* 156 */     return toByteArray(localBigInteger2, getByteLength(paramBigInteger1));
/*     */   }
/*     */ 
/*     */   private static byte[] crtCrypt(byte[] paramArrayOfByte, RSAPrivateCrtKey paramRSAPrivateCrtKey, boolean paramBoolean)
/*     */     throws BadPaddingException
/*     */   {
/* 165 */     BigInteger localBigInteger1 = paramRSAPrivateCrtKey.getModulus();
/* 166 */     BigInteger localBigInteger2 = parseMsg(paramArrayOfByte, localBigInteger1);
/* 167 */     BigInteger localBigInteger3 = localBigInteger2;
/* 168 */     BigInteger localBigInteger4 = paramRSAPrivateCrtKey.getPrimeP();
/* 169 */     BigInteger localBigInteger5 = paramRSAPrivateCrtKey.getPrimeQ();
/* 170 */     BigInteger localBigInteger6 = paramRSAPrivateCrtKey.getPrimeExponentP();
/* 171 */     BigInteger localBigInteger7 = paramRSAPrivateCrtKey.getPrimeExponentQ();
/* 172 */     BigInteger localBigInteger8 = paramRSAPrivateCrtKey.getCrtCoefficient();
/* 173 */     BigInteger localBigInteger9 = paramRSAPrivateCrtKey.getPublicExponent();
/* 174 */     BigInteger localBigInteger10 = paramRSAPrivateCrtKey.getPrivateExponent();
/*     */ 
/* 178 */     BlindingRandomPair localBlindingRandomPair = getBlindingRandomPair(localBigInteger9, localBigInteger10, localBigInteger1);
/* 179 */     localBigInteger3 = localBigInteger3.multiply(localBlindingRandomPair.u).mod(localBigInteger1);
/*     */ 
/* 183 */     BigInteger localBigInteger11 = localBigInteger3.modPow(localBigInteger6, localBigInteger4);
/*     */ 
/* 185 */     BigInteger localBigInteger12 = localBigInteger3.modPow(localBigInteger7, localBigInteger5);
/*     */ 
/* 188 */     BigInteger localBigInteger13 = localBigInteger11.subtract(localBigInteger12);
/* 189 */     if (localBigInteger13.signum() < 0) {
/* 190 */       localBigInteger13 = localBigInteger13.add(localBigInteger4);
/*     */     }
/* 192 */     BigInteger localBigInteger14 = localBigInteger13.multiply(localBigInteger8).mod(localBigInteger4);
/*     */ 
/* 195 */     BigInteger localBigInteger15 = localBigInteger14.multiply(localBigInteger5).add(localBigInteger12);
/*     */ 
/* 198 */     localBigInteger15 = localBigInteger15.multiply(localBlindingRandomPair.v).mod(localBigInteger1);
/*     */ 
/* 200 */     if ((paramBoolean) && (!localBigInteger2.equals(localBigInteger15.modPow(localBigInteger9, localBigInteger1)))) {
/* 201 */       throw new BadPaddingException("RSA private key operation failed");
/*     */     }
/*     */ 
/* 204 */     return toByteArray(localBigInteger15, getByteLength(localBigInteger1));
/*     */   }
/*     */ 
/*     */   private static BigInteger parseMsg(byte[] paramArrayOfByte, BigInteger paramBigInteger)
/*     */     throws BadPaddingException
/*     */   {
/* 212 */     BigInteger localBigInteger = new BigInteger(1, paramArrayOfByte);
/* 213 */     if (localBigInteger.compareTo(paramBigInteger) >= 0) {
/* 214 */       throw new BadPaddingException("Message is larger than modulus");
/*     */     }
/* 216 */     return localBigInteger;
/*     */   }
/*     */ 
/*     */   private static byte[] toByteArray(BigInteger paramBigInteger, int paramInt)
/*     */   {
/* 225 */     byte[] arrayOfByte1 = paramBigInteger.toByteArray();
/* 226 */     int i = arrayOfByte1.length;
/* 227 */     if (i == paramInt) {
/* 228 */       return arrayOfByte1;
/*     */     }
/*     */ 
/* 231 */     if ((i == paramInt + 1) && (arrayOfByte1[0] == 0)) {
/* 232 */       arrayOfByte2 = new byte[paramInt];
/* 233 */       System.arraycopy(arrayOfByte1, 1, arrayOfByte2, 0, paramInt);
/* 234 */       return arrayOfByte2;
/*     */     }
/*     */ 
/* 237 */     assert (i < paramInt);
/* 238 */     byte[] arrayOfByte2 = new byte[paramInt];
/* 239 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, paramInt - i, i);
/* 240 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private static BlindingRandomPair getBlindingRandomPair(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */   {
/* 431 */     BlindingParameters localBlindingParameters = null;
/* 432 */     synchronized (blindingCache) {
/* 433 */       localBlindingParameters = (BlindingParameters)blindingCache.get(paramBigInteger3);
/*     */     }
/*     */ 
/* 436 */     if (localBlindingParameters == null) {
/* 437 */       localBlindingParameters = new BlindingParameters(paramBigInteger1, paramBigInteger2, paramBigInteger3);
/* 438 */       synchronized (blindingCache) {
/* 439 */         if (blindingCache.get(paramBigInteger3) == null) {
/* 440 */           blindingCache.put(paramBigInteger3, localBlindingParameters);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 445 */     ??? = localBlindingParameters.getBlindingRandomPair(paramBigInteger1, paramBigInteger2, paramBigInteger3);
/* 446 */     if (??? == null)
/*     */     {
/* 448 */       localBlindingParameters = new BlindingParameters(paramBigInteger1, paramBigInteger2, paramBigInteger3);
/* 449 */       synchronized (blindingCache) {
/* 450 */         if (blindingCache.get(paramBigInteger3) != null) {
/* 451 */           blindingCache.put(paramBigInteger3, localBlindingParameters);
/*     */         }
/*     */       }
/* 454 */       ??? = localBlindingParameters.getBlindingRandomPair(paramBigInteger1, paramBigInteger2, paramBigInteger3);
/*     */     }
/*     */ 
/* 457 */     return ???;
/*     */   }
/*     */ 
/*     */   private static final class BlindingParameters
/*     */   {
/* 336 */     private static final BigInteger BIG_TWO = BigInteger.valueOf(2L);
/*     */     private final BigInteger e;
/*     */     private final BigInteger d;
/*     */     private BigInteger u;
/*     */     private BigInteger v;
/*     */ 
/*     */     BlindingParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */     {
/* 354 */       this.u = null;
/* 355 */       this.v = null;
/* 356 */       this.e = paramBigInteger1;
/* 357 */       this.d = paramBigInteger2;
/*     */ 
/* 359 */       int i = paramBigInteger3.bitLength();
/* 360 */       SecureRandom localSecureRandom = JCAUtil.getSecureRandom();
/* 361 */       this.u = new BigInteger(i, localSecureRandom).mod(paramBigInteger3);
/*     */ 
/* 370 */       if (this.u.equals(BigInteger.ZERO)) {
/* 371 */         this.u = BigInteger.ONE;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 378 */         this.v = this.u.modInverse(paramBigInteger3);
/*     */       }
/*     */       catch (ArithmeticException localArithmeticException) {
/* 381 */         this.u = BigInteger.ONE;
/* 382 */         this.v = BigInteger.ONE;
/*     */       }
/*     */ 
/* 385 */       if (paramBigInteger1 != null) {
/* 386 */         this.u = this.u.modPow(paramBigInteger1, paramBigInteger3);
/*     */       }
/*     */       else
/*     */       {
/* 390 */         this.v = this.v.modPow(paramBigInteger2, paramBigInteger3);
/*     */       }
/*     */     }
/*     */ 
/*     */     RSACore.BlindingRandomPair getBlindingRandomPair(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */     {
/* 400 */       if (((this.e != null) && (this.e.equals(paramBigInteger1))) || ((this.d != null) && (this.d.equals(paramBigInteger2))))
/*     */       {
/* 403 */         RSACore.BlindingRandomPair localBlindingRandomPair = null;
/* 404 */         synchronized (this) {
/* 405 */           if ((!this.u.equals(BigInteger.ZERO)) && (!this.v.equals(BigInteger.ZERO)))
/*     */           {
/* 408 */             localBlindingRandomPair = new RSACore.BlindingRandomPair(this.u, this.v);
/* 409 */             if ((this.u.compareTo(BigInteger.ONE) <= 0) || (this.v.compareTo(BigInteger.ONE) <= 0))
/*     */             {
/* 413 */               this.u = BigInteger.ZERO;
/* 414 */               this.v = BigInteger.ZERO;
/*     */             } else {
/* 416 */               this.u = this.u.modPow(BIG_TWO, paramBigInteger3);
/* 417 */               this.v = this.v.modPow(BIG_TWO, paramBigInteger3);
/*     */             }
/*     */           }
/*     */         }
/* 421 */         return localBlindingRandomPair;
/*     */       }
/*     */ 
/* 424 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class BlindingRandomPair
/*     */   {
/*     */     final BigInteger u;
/*     */     final BigInteger v;
/*     */ 
/*     */     BlindingRandomPair(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */     {
/* 319 */       this.u = paramBigInteger1;
/* 320 */       this.v = paramBigInteger2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSACore
 * JD-Core Version:    0.6.2
 */