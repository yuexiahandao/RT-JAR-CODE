/*     */ package sun.security.util;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.interfaces.DSAKey;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.ECKey;
/*     */ import java.security.interfaces.RSAKey;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.KeySpec;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.interfaces.DHKey;
/*     */ import javax.crypto.interfaces.DHPublicKey;
/*     */ import javax.crypto.spec.DHParameterSpec;
/*     */ import javax.crypto.spec.DHPublicKeySpec;
/*     */ 
/*     */ public final class KeyUtil
/*     */ {
/*     */   public static final int getKeySize(Key paramKey)
/*     */   {
/*  57 */     int i = -1;
/*     */ 
/*  59 */     if ((paramKey instanceof Length)) {
/*     */       try {
/*  61 */         Length localLength = (Length)paramKey;
/*  62 */         i = localLength.length();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException)
/*     */       {
/*     */       }
/*  67 */       if (i >= 0)
/*  68 */         return i;
/*     */     }
/*     */     Object localObject;
/*  73 */     if ((paramKey instanceof SecretKey)) {
/*  74 */       localObject = (SecretKey)paramKey;
/*  75 */       String str = ((SecretKey)localObject).getFormat();
/*  76 */       if (("RAW".equals(str)) && (((SecretKey)localObject).getEncoded() != null)) {
/*  77 */         i = ((SecretKey)localObject).getEncoded().length * 8;
/*     */       }
/*     */     }
/*  80 */     else if ((paramKey instanceof RSAKey)) {
/*  81 */       localObject = (RSAKey)paramKey;
/*  82 */       i = ((RSAKey)localObject).getModulus().bitLength();
/*  83 */     } else if ((paramKey instanceof ECKey)) {
/*  84 */       localObject = (ECKey)paramKey;
/*  85 */       i = ((ECKey)localObject).getParams().getOrder().bitLength();
/*  86 */     } else if ((paramKey instanceof DSAKey)) {
/*  87 */       localObject = (DSAKey)paramKey;
/*  88 */       i = ((DSAKey)localObject).getParams().getP().bitLength();
/*  89 */     } else if ((paramKey instanceof DHKey)) {
/*  90 */       localObject = (DHKey)paramKey;
/*  91 */       i = ((DHKey)localObject).getParams().getP().bitLength();
/*     */     }
/*     */ 
/*  95 */     return i;
/*     */   }
/*     */ 
/*     */   public static final void validate(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 111 */     if (paramKey == null) {
/* 112 */       throw new NullPointerException("The key to be validated cannot be null");
/*     */     }
/*     */ 
/* 116 */     if ((paramKey instanceof DHPublicKey))
/* 117 */       validateDHPublicKey((DHPublicKey)paramKey);
/*     */   }
/*     */ 
/*     */   public static final void validate(KeySpec paramKeySpec)
/*     */     throws InvalidKeyException
/*     */   {
/* 135 */     if (paramKeySpec == null) {
/* 136 */       throw new NullPointerException("The key spec to be validated cannot be null");
/*     */     }
/*     */ 
/* 140 */     if ((paramKeySpec instanceof DHPublicKeySpec))
/* 141 */       validateDHPublicKey((DHPublicKeySpec)paramKeySpec);
/*     */   }
/*     */ 
/*     */   public static final boolean isOracleJCEProvider(String paramString)
/*     */   {
/* 156 */     return (paramString != null) && ((paramString.equals("SunJCE")) || (paramString.startsWith("SunPKCS11")));
/*     */   }
/*     */ 
/*     */   public static byte[] checkTlsPreMasterSecretKey(int paramInt1, int paramInt2, SecureRandom paramSecureRandom, byte[] paramArrayOfByte, boolean paramBoolean)
/*     */   {
/* 201 */     if (paramSecureRandom == null) {
/* 202 */       paramSecureRandom = new SecureRandom();
/*     */     }
/* 204 */     byte[] arrayOfByte = new byte[48];
/* 205 */     paramSecureRandom.nextBytes(arrayOfByte);
/*     */ 
/* 207 */     if ((!paramBoolean) && (paramArrayOfByte != null))
/*     */     {
/* 209 */       if (paramArrayOfByte.length != 48)
/*     */       {
/* 211 */         return arrayOfByte;
/*     */       }
/*     */ 
/* 214 */       int i = (paramArrayOfByte[0] & 0xFF) << 8 | paramArrayOfByte[1] & 0xFF;
/*     */ 
/* 216 */       if ((paramInt1 != i) && (
/* 217 */         (paramInt1 > 769) || (paramInt2 != i)))
/*     */       {
/* 219 */         paramArrayOfByte = arrayOfByte;
/*     */       }
/*     */ 
/* 226 */       return paramArrayOfByte;
/*     */     }
/*     */ 
/* 230 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static void validateDHPublicKey(DHPublicKey paramDHPublicKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 245 */     DHParameterSpec localDHParameterSpec = paramDHPublicKey.getParams();
/*     */ 
/* 247 */     BigInteger localBigInteger1 = localDHParameterSpec.getP();
/* 248 */     BigInteger localBigInteger2 = localDHParameterSpec.getG();
/* 249 */     BigInteger localBigInteger3 = paramDHPublicKey.getY();
/*     */ 
/* 251 */     validateDHPublicKey(localBigInteger1, localBigInteger2, localBigInteger3);
/*     */   }
/*     */ 
/*     */   private static void validateDHPublicKey(DHPublicKeySpec paramDHPublicKeySpec) throws InvalidKeyException
/*     */   {
/* 256 */     validateDHPublicKey(paramDHPublicKeySpec.getP(), paramDHPublicKeySpec.getG(), paramDHPublicKeySpec.getY());
/*     */   }
/*     */ 
/*     */   private static void validateDHPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*     */     throws InvalidKeyException
/*     */   {
/* 264 */     BigInteger localBigInteger1 = BigInteger.ONE;
/* 265 */     BigInteger localBigInteger2 = paramBigInteger1.subtract(BigInteger.ONE);
/* 266 */     if (paramBigInteger3.compareTo(localBigInteger1) <= 0) {
/* 267 */       throw new InvalidKeyException("Diffie-Hellman public key is too small");
/*     */     }
/*     */ 
/* 270 */     if (paramBigInteger3.compareTo(localBigInteger2) >= 0) {
/* 271 */       throw new InvalidKeyException("Diffie-Hellman public key is too large");
/*     */     }
/*     */ 
/* 281 */     BigInteger localBigInteger3 = paramBigInteger1.remainder(paramBigInteger3);
/* 282 */     if (localBigInteger3.equals(BigInteger.ZERO))
/* 283 */       throw new InvalidKeyException("Invalid Diffie-Hellman parameters");
/*     */   }
/*     */ 
/*     */   public static byte[] trimZeroes(byte[] paramArrayOfByte)
/*     */   {
/* 293 */     int i = 0;
/* 294 */     while ((i < paramArrayOfByte.length - 1) && (paramArrayOfByte[i] == 0)) {
/* 295 */       i++;
/*     */     }
/* 297 */     if (i == 0) {
/* 298 */       return paramArrayOfByte;
/*     */     }
/* 300 */     byte[] arrayOfByte = new byte[paramArrayOfByte.length - i];
/* 301 */     System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, arrayOfByte.length);
/* 302 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.KeyUtil
 * JD-Core Version:    0.6.2
 */