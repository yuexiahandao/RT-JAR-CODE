/*     */ package java.security;
/*     */ 
/*     */ import java.util.Random;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ import sun.security.jca.ProviderList;
/*     */ import sun.security.jca.Providers;
/*     */ 
/*     */ public class SecureRandom extends Random
/*     */ {
/* 100 */   private Provider provider = null;
/*     */ 
/* 108 */   private SecureRandomSpi secureRandomSpi = null;
/*     */   private String algorithm;
/* 119 */   private static volatile SecureRandom seedGenerator = null;
/*     */   static final long serialVersionUID = 4940670005562187L;
/*     */   private byte[] state;
/* 563 */   private MessageDigest digest = null;
/*     */   private byte[] randomBytes;
/*     */   private int randomBytesUsed;
/*     */   private long counter;
/*     */ 
/*     */   public SecureRandom()
/*     */   {
/* 154 */     super(0L);
/* 155 */     getDefaultPRNG(false, null);
/*     */   }
/*     */ 
/*     */   public SecureRandom(byte[] paramArrayOfByte)
/*     */   {
/* 182 */     super(0L);
/* 183 */     getDefaultPRNG(true, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private void getDefaultPRNG(boolean paramBoolean, byte[] paramArrayOfByte) {
/* 187 */     String str = getPrngAlgorithm();
/* 188 */     if (str == null)
/*     */     {
/* 190 */       str = "SHA1PRNG";
/* 191 */       this.secureRandomSpi = new sun.security.provider.SecureRandom();
/* 192 */       this.provider = Providers.getSunProvider();
/* 193 */       if (paramBoolean)
/* 194 */         this.secureRandomSpi.engineSetSeed(paramArrayOfByte);
/*     */     }
/*     */     else {
/*     */       try {
/* 198 */         SecureRandom localSecureRandom = getInstance(str);
/* 199 */         this.secureRandomSpi = localSecureRandom.getSecureRandomSpi();
/* 200 */         this.provider = localSecureRandom.getProvider();
/* 201 */         if (paramBoolean)
/* 202 */           this.secureRandomSpi.engineSetSeed(paramArrayOfByte);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/* 206 */         throw new RuntimeException(localNoSuchAlgorithmException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     if (getClass() == SecureRandom.class)
/* 215 */       this.algorithm = str;
/*     */   }
/*     */ 
/*     */   protected SecureRandom(SecureRandomSpi paramSecureRandomSpi, Provider paramProvider)
/*     */   {
/* 227 */     this(paramSecureRandomSpi, paramProvider, null);
/*     */   }
/*     */ 
/*     */   private SecureRandom(SecureRandomSpi paramSecureRandomSpi, Provider paramProvider, String paramString)
/*     */   {
/* 232 */     super(0L);
/* 233 */     this.secureRandomSpi = paramSecureRandomSpi;
/* 234 */     this.provider = paramProvider;
/* 235 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public static SecureRandom getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 276 */     GetInstance.Instance localInstance = GetInstance.getInstance("SecureRandom", SecureRandomSpi.class, paramString);
/*     */ 
/* 278 */     return new SecureRandom((SecureRandomSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static SecureRandom getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 327 */     GetInstance.Instance localInstance = GetInstance.getInstance("SecureRandom", SecureRandomSpi.class, paramString1, paramString2);
/*     */ 
/* 329 */     return new SecureRandom((SecureRandomSpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static SecureRandom getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 371 */     GetInstance.Instance localInstance = GetInstance.getInstance("SecureRandom", SecureRandomSpi.class, paramString, paramProvider);
/*     */ 
/* 373 */     return new SecureRandom((SecureRandomSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   SecureRandomSpi getSecureRandomSpi()
/*     */   {
/* 381 */     return this.secureRandomSpi;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 390 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 402 */     return this.algorithm != null ? this.algorithm : "unknown";
/*     */   }
/*     */ 
/*     */   public synchronized void setSeed(byte[] paramArrayOfByte)
/*     */   {
/* 415 */     this.secureRandomSpi.engineSetSeed(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void setSeed(long paramLong)
/*     */   {
/* 438 */     if (paramLong != 0L)
/* 439 */       this.secureRandomSpi.engineSetSeed(longToByteArray(paramLong));
/*     */   }
/*     */ 
/*     */   public synchronized void nextBytes(byte[] paramArrayOfByte)
/*     */   {
/* 455 */     this.secureRandomSpi.engineNextBytes(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected final int next(int paramInt)
/*     */   {
/* 473 */     int i = (paramInt + 7) / 8;
/* 474 */     byte[] arrayOfByte = new byte[i];
/* 475 */     int j = 0;
/*     */ 
/* 477 */     nextBytes(arrayOfByte);
/* 478 */     for (int k = 0; k < i; k++) {
/* 479 */       j = (j << 8) + (arrayOfByte[k] & 0xFF);
/*     */     }
/* 481 */     return j >>> i * 8 - paramInt;
/*     */   }
/*     */ 
/*     */   public static byte[] getSeed(int paramInt)
/*     */   {
/* 502 */     if (seedGenerator == null)
/* 503 */       seedGenerator = new SecureRandom();
/* 504 */     return seedGenerator.generateSeed(paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] generateSeed(int paramInt)
/*     */   {
/* 517 */     return this.secureRandomSpi.engineGenerateSeed(paramInt);
/*     */   }
/*     */ 
/*     */   private static byte[] longToByteArray(long paramLong)
/*     */   {
/* 525 */     byte[] arrayOfByte = new byte[8];
/*     */ 
/* 527 */     for (int i = 0; i < 8; i++) {
/* 528 */       arrayOfByte[i] = ((byte)(int)paramLong);
/* 529 */       paramLong >>= 8;
/*     */     }
/*     */ 
/* 532 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static String getPrngAlgorithm()
/*     */   {
/* 542 */     for (Provider localProvider : Providers.getProviderList().providers()) {
/* 543 */       for (Provider.Service localService : localProvider.getServices()) {
/* 544 */         if (localService.getType().equals("SecureRandom")) {
/* 545 */           return localService.getAlgorithm();
/*     */         }
/*     */       }
/*     */     }
/* 549 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.SecureRandom
 * JD-Core Version:    0.6.2
 */