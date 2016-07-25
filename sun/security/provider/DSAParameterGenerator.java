/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParameterGeneratorSpi;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ 
/*     */ public class DSAParameterGenerator extends AlgorithmParameterGeneratorSpi
/*     */ {
/*  58 */   private int modLen = 1024;
/*     */   private SecureRandom random;
/*  64 */   private static final BigInteger ZERO = BigInteger.valueOf(0L);
/*  65 */   private static final BigInteger ONE = BigInteger.valueOf(1L);
/*  66 */   private static final BigInteger TWO = BigInteger.valueOf(2L);
/*     */   private SHA sha;
/*     */ 
/*     */   public DSAParameterGenerator()
/*     */   {
/*  72 */     this.sha = new SHA();
/*     */   }
/*     */ 
/*     */   protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
/*     */   {
/*  90 */     if ((paramInt < 512) || (paramInt > 1024) || (paramInt % 64 != 0)) {
/*  91 */       throw new InvalidParameterException("Prime size must range from 512 to 1024 and be a multiple of 64");
/*     */     }
/*     */ 
/*  95 */     this.modLen = paramInt;
/*  96 */     this.random = paramSecureRandom;
/*     */   }
/*     */ 
/*     */   protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 112 */     throw new InvalidAlgorithmParameterException("Invalid parameter");
/*     */   }
/*     */ 
/*     */   protected AlgorithmParameters engineGenerateParameters()
/*     */   {
/* 121 */     AlgorithmParameters localAlgorithmParameters = null;
/*     */     try {
/* 123 */       if (this.random == null) {
/* 124 */         this.random = new SecureRandom();
/*     */       }
/*     */ 
/* 127 */       BigInteger[] arrayOfBigInteger = generatePandQ(this.random, this.modLen);
/* 128 */       BigInteger localBigInteger1 = arrayOfBigInteger[0];
/* 129 */       BigInteger localBigInteger2 = arrayOfBigInteger[1];
/* 130 */       BigInteger localBigInteger3 = generateG(localBigInteger1, localBigInteger2);
/*     */ 
/* 132 */       DSAParameterSpec localDSAParameterSpec = new DSAParameterSpec(localBigInteger1, localBigInteger2, localBigInteger3);
/*     */ 
/* 135 */       localAlgorithmParameters = AlgorithmParameters.getInstance("DSA", "SUN");
/* 136 */       localAlgorithmParameters.init(localDSAParameterSpec);
/*     */     }
/*     */     catch (InvalidParameterSpecException localInvalidParameterSpecException) {
/* 139 */       throw new RuntimeException(localInvalidParameterSpecException.getMessage());
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 142 */       throw new RuntimeException(localNoSuchAlgorithmException.getMessage());
/*     */     }
/*     */     catch (NoSuchProviderException localNoSuchProviderException) {
/* 145 */       throw new RuntimeException(localNoSuchProviderException.getMessage());
/*     */     }
/*     */ 
/* 148 */     return localAlgorithmParameters;
/*     */   }
/*     */ 
/*     */   BigInteger[] generatePandQ(SecureRandom paramSecureRandom, int paramInt)
/*     */   {
/* 165 */     BigInteger[] arrayOfBigInteger = null;
/* 166 */     byte[] arrayOfByte = new byte[20];
/*     */ 
/* 168 */     while (arrayOfBigInteger == null) {
/* 169 */       for (int i = 0; i < 20; i++) {
/* 170 */         arrayOfByte[i] = ((byte)paramSecureRandom.nextInt());
/*     */       }
/* 172 */       arrayOfBigInteger = generatePandQ(arrayOfByte, paramInt);
/*     */     }
/* 174 */     return arrayOfBigInteger;
/*     */   }
/*     */ 
/*     */   BigInteger[] generatePandQ(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 194 */     int i = paramArrayOfByte.length * 8;
/* 195 */     int j = (paramInt - 1) / 160;
/* 196 */     int k = (paramInt - 1) % 160;
/*     */ 
/* 198 */     BigInteger localBigInteger1 = new BigInteger(1, paramArrayOfByte);
/* 199 */     BigInteger localBigInteger2 = TWO.pow(2 * i);
/*     */ 
/* 202 */     byte[] arrayOfByte1 = SHA(paramArrayOfByte);
/* 203 */     byte[] arrayOfByte2 = SHA(toByteArray(localBigInteger1.add(ONE).mod(localBigInteger2)));
/*     */ 
/* 205 */     xor(arrayOfByte1, arrayOfByte2);
/* 206 */     byte[] arrayOfByte3 = arrayOfByte1;
/*     */     int tmp91_90 = 0;
/*     */     byte[] tmp91_88 = arrayOfByte3; tmp91_88[tmp91_90] = ((byte)(tmp91_88[tmp91_90] | 0x80));
/*     */     byte[] tmp103_99 = arrayOfByte3; tmp103_99[19] = ((byte)(tmp103_99[19] | 0x1));
/* 211 */     BigInteger localBigInteger3 = new BigInteger(1, arrayOfByte3);
/*     */ 
/* 214 */     if (!localBigInteger3.isProbablePrime(80)) {
/* 215 */       return null;
/*     */     }
/*     */ 
/* 218 */     BigInteger[] arrayOfBigInteger1 = new BigInteger[j + 1];
/* 219 */     BigInteger localBigInteger4 = TWO;
/*     */ 
/* 222 */     for (int m = 0; m < 4096; m++)
/*     */     {
/* 225 */       for (int n = 0; n <= j; n++) {
/* 226 */         BigInteger localBigInteger6 = BigInteger.valueOf(n);
/* 227 */         localBigInteger8 = localBigInteger1.add(localBigInteger4).add(localBigInteger6).mod(localBigInteger2);
/* 228 */         arrayOfBigInteger1[n] = new BigInteger(1, SHA(toByteArray(localBigInteger8)));
/*     */       }
/*     */ 
/* 232 */       BigInteger localBigInteger5 = arrayOfBigInteger1[0];
/* 233 */       for (int i1 = 1; i1 < j; i1++) {
/* 234 */         localBigInteger5 = localBigInteger5.add(arrayOfBigInteger1[i1].multiply(TWO.pow(i1 * 160)));
/*     */       }
/* 236 */       localBigInteger5 = localBigInteger5.add(arrayOfBigInteger1[j].mod(TWO.pow(k)).multiply(TWO.pow(j * 160)));
/*     */ 
/* 238 */       BigInteger localBigInteger7 = TWO.pow(paramInt - 1);
/* 239 */       BigInteger localBigInteger8 = localBigInteger5.add(localBigInteger7);
/*     */ 
/* 242 */       BigInteger localBigInteger9 = localBigInteger8.mod(localBigInteger3.multiply(TWO));
/* 243 */       BigInteger localBigInteger10 = localBigInteger8.subtract(localBigInteger9.subtract(ONE));
/*     */ 
/* 246 */       if ((localBigInteger10.compareTo(localBigInteger7) > -1) && (localBigInteger10.isProbablePrime(80))) {
/* 247 */         BigInteger[] arrayOfBigInteger2 = { localBigInteger10, localBigInteger3, localBigInteger1, BigInteger.valueOf(m) };
/*     */ 
/* 249 */         return arrayOfBigInteger2;
/*     */       }
/* 251 */       localBigInteger4 = localBigInteger4.add(BigInteger.valueOf(j)).add(ONE);
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   BigInteger generateG(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */   {
/* 266 */     BigInteger localBigInteger1 = ONE;
/* 267 */     BigInteger localBigInteger2 = paramBigInteger1.subtract(ONE).divide(paramBigInteger2);
/* 268 */     BigInteger localBigInteger3 = ONE;
/* 269 */     while (localBigInteger3.compareTo(TWO) < 0) {
/* 270 */       localBigInteger3 = localBigInteger1.modPow(localBigInteger2, paramBigInteger1);
/* 271 */       localBigInteger1 = localBigInteger1.add(ONE);
/*     */     }
/* 273 */     return localBigInteger3;
/*     */   }
/*     */ 
/*     */   private byte[] SHA(byte[] paramArrayOfByte)
/*     */   {
/* 280 */     this.sha.engineReset();
/* 281 */     this.sha.engineUpdate(paramArrayOfByte, 0, paramArrayOfByte.length);
/* 282 */     return this.sha.engineDigest();
/*     */   }
/*     */ 
/*     */   private byte[] toByteArray(BigInteger paramBigInteger)
/*     */   {
/* 290 */     Object localObject = paramBigInteger.toByteArray();
/* 291 */     if (localObject[0] == 0) {
/* 292 */       byte[] arrayOfByte = new byte[localObject.length - 1];
/* 293 */       System.arraycopy(localObject, 1, arrayOfByte, 0, arrayOfByte.length);
/* 294 */       localObject = arrayOfByte;
/*     */     }
/* 296 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void xor(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 303 */     for (int i = 0; i < paramArrayOfByte1.length; i++)
/*     */     {
/*     */       int tmp10_9 = i;
/*     */       byte[] tmp10_8 = paramArrayOfByte1; tmp10_8[tmp10_9] = ((byte)(tmp10_8[tmp10_9] ^ paramArrayOfByte2[i]));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAParameterGenerator
 * JD-Core Version:    0.6.2
 */