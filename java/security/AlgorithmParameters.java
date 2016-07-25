/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ 
/*     */ public class AlgorithmParameters
/*     */ {
/*     */   private Provider provider;
/*     */   private AlgorithmParametersSpi paramSpi;
/*     */   private String algorithm;
/*  87 */   private boolean initialized = false;
/*     */ 
/*     */   protected AlgorithmParameters(AlgorithmParametersSpi paramAlgorithmParametersSpi, Provider paramProvider, String paramString)
/*     */   {
/*  99 */     this.paramSpi = paramAlgorithmParametersSpi;
/* 100 */     this.provider = paramProvider;
/* 101 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 110 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static AlgorithmParameters getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/* 146 */       Object[] arrayOfObject = Security.getImpl(paramString, "AlgorithmParameters", (String)null);
/*     */ 
/* 148 */       return new AlgorithmParameters((AlgorithmParametersSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */     }
/*     */     catch (NoSuchProviderException localNoSuchProviderException) {
/*     */     }
/* 152 */     throw new NoSuchAlgorithmException(paramString + " not found");
/*     */   }
/*     */ 
/*     */   public static AlgorithmParameters getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 197 */     if ((paramString2 == null) || (paramString2.length() == 0))
/* 198 */       throw new IllegalArgumentException("missing provider");
/* 199 */     Object[] arrayOfObject = Security.getImpl(paramString1, "AlgorithmParameters", paramString2);
/*     */ 
/* 201 */     return new AlgorithmParameters((AlgorithmParametersSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString1);
/*     */   }
/*     */ 
/*     */   public static AlgorithmParameters getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 242 */     if (paramProvider == null)
/* 243 */       throw new IllegalArgumentException("missing provider");
/* 244 */     Object[] arrayOfObject = Security.getImpl(paramString, "AlgorithmParameters", paramProvider);
/*     */ 
/* 246 */     return new AlgorithmParameters((AlgorithmParametersSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 257 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final void init(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidParameterSpecException
/*     */   {
/* 273 */     if (this.initialized)
/* 274 */       throw new InvalidParameterSpecException("already initialized");
/* 275 */     this.paramSpi.engineInit(paramAlgorithmParameterSpec);
/* 276 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public final void init(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 291 */     if (this.initialized)
/* 292 */       throw new IOException("already initialized");
/* 293 */     this.paramSpi.engineInit(paramArrayOfByte);
/* 294 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public final void init(byte[] paramArrayOfByte, String paramString)
/*     */     throws IOException
/*     */   {
/* 313 */     if (this.initialized)
/* 314 */       throw new IOException("already initialized");
/* 315 */     this.paramSpi.engineInit(paramArrayOfByte, paramString);
/* 316 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public final <T extends AlgorithmParameterSpec> T getParameterSpec(Class<T> paramClass)
/*     */     throws InvalidParameterSpecException
/*     */   {
/* 340 */     if (!this.initialized) {
/* 341 */       throw new InvalidParameterSpecException("not initialized");
/*     */     }
/* 343 */     return this.paramSpi.engineGetParameterSpec(paramClass);
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded()
/*     */     throws IOException
/*     */   {
/* 358 */     if (!this.initialized) {
/* 359 */       throw new IOException("not initialized");
/*     */     }
/* 361 */     return this.paramSpi.engineGetEncoded();
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded(String paramString)
/*     */     throws IOException
/*     */   {
/* 380 */     if (!this.initialized) {
/* 381 */       throw new IOException("not initialized");
/*     */     }
/* 383 */     return this.paramSpi.engineGetEncoded(paramString);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 393 */     if (!this.initialized) {
/* 394 */       return null;
/*     */     }
/* 396 */     return this.paramSpi.engineToString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AlgorithmParameters
 * JD-Core Version:    0.6.2
 */