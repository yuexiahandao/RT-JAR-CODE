/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParameterGenerator;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.crypto.spec.DHParameterSpec;
/*     */ 
/*     */ public final class ParameterCache
/*     */ {
/*     */   private static final Map<Integer, DSAParameterSpec> dsaCache;
/* 128 */   private static final Map<Integer, DHParameterSpec> dhCache = Collections.synchronizedMap(new HashMap());
/*     */ 
/*     */   public static DSAParameterSpec getCachedDSAParameterSpec(int paramInt)
/*     */   {
/*  62 */     return (DSAParameterSpec)dsaCache.get(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static DHParameterSpec getCachedDHParameterSpec(int paramInt)
/*     */   {
/*  70 */     return (DHParameterSpec)dhCache.get(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static DSAParameterSpec getDSAParameterSpec(int paramInt, SecureRandom paramSecureRandom)
/*     */     throws NoSuchAlgorithmException, InvalidParameterSpecException
/*     */   {
/*  80 */     DSAParameterSpec localDSAParameterSpec = getCachedDSAParameterSpec(paramInt);
/*  81 */     if (localDSAParameterSpec != null) {
/*  82 */       return localDSAParameterSpec;
/*     */     }
/*  84 */     localDSAParameterSpec = getNewDSAParameterSpec(paramInt, paramSecureRandom);
/*  85 */     dsaCache.put(Integer.valueOf(paramInt), localDSAParameterSpec);
/*  86 */     return localDSAParameterSpec;
/*     */   }
/*     */ 
/*     */   public static DHParameterSpec getDHParameterSpec(int paramInt, SecureRandom paramSecureRandom)
/*     */     throws NoSuchAlgorithmException, InvalidParameterSpecException
/*     */   {
/*  96 */     DHParameterSpec localDHParameterSpec = getCachedDHParameterSpec(paramInt);
/*  97 */     if (localDHParameterSpec != null) {
/*  98 */       return localDHParameterSpec;
/*     */     }
/* 100 */     AlgorithmParameterGenerator localAlgorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("DH");
/*     */ 
/* 102 */     localAlgorithmParameterGenerator.init(paramInt, paramSecureRandom);
/* 103 */     AlgorithmParameters localAlgorithmParameters = localAlgorithmParameterGenerator.generateParameters();
/* 104 */     localDHParameterSpec = (DHParameterSpec)localAlgorithmParameters.getParameterSpec(DHParameterSpec.class);
/* 105 */     dhCache.put(Integer.valueOf(paramInt), localDHParameterSpec);
/* 106 */     return localDHParameterSpec;
/*     */   }
/*     */ 
/*     */   public static DSAParameterSpec getNewDSAParameterSpec(int paramInt, SecureRandom paramSecureRandom)
/*     */     throws NoSuchAlgorithmException, InvalidParameterSpecException
/*     */   {
/* 118 */     AlgorithmParameterGenerator localAlgorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("DSA");
/*     */ 
/* 120 */     localAlgorithmParameterGenerator.init(paramInt, paramSecureRandom);
/* 121 */     AlgorithmParameters localAlgorithmParameters = localAlgorithmParameterGenerator.generateParameters();
/* 122 */     DSAParameterSpec localDSAParameterSpec = (DSAParameterSpec)localAlgorithmParameters.getParameterSpec(DSAParameterSpec.class);
/* 123 */     return localDSAParameterSpec;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 130 */     dsaCache = Collections.synchronizedMap(new HashMap());
/*     */ 
/* 149 */     BigInteger localBigInteger1 = new BigInteger("fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17", 16);
/*     */ 
/* 154 */     BigInteger localBigInteger2 = new BigInteger("962eddcc369cba8ebb260ee6b6a126d9346e38c5", 16);
/*     */ 
/* 157 */     BigInteger localBigInteger3 = new BigInteger("678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4", 16);
/*     */ 
/* 167 */     BigInteger localBigInteger4 = new BigInteger("e9e642599d355f37c97ffd3567120b8e25c9cd43e927b3a9670fbec5d890141922d2c3b3ad2480093799869d1e846aab49fab0ad26d2ce6a22219d470bce7d777d4a21fbe9c270b57f607002f3cef8393694cf45ee3688c11a8c56ab127a3daf", 16);
/*     */ 
/* 174 */     BigInteger localBigInteger5 = new BigInteger("9cdbd84c9f1ac2f38d0f80f42ab952e7338bf511", 16);
/*     */ 
/* 178 */     BigInteger localBigInteger6 = new BigInteger("30470ad5a005fb14ce2d9dcd87e38bc7d1b1c5facbaecbe95f190aa7a31d23c4dbbcbe06174544401a5b2c020965d8c2bd2171d3668445771f74ba084d2029d83c1c158547f3a9f1a2715be23d51ae4d3e5a1f6a7064f316933a346d3f529252", 16);
/*     */ 
/* 191 */     BigInteger localBigInteger7 = new BigInteger("fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b76b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c7", 16);
/*     */ 
/* 200 */     BigInteger localBigInteger8 = new BigInteger("9760508f15230bccb292b982a2eb840bf0581cf5", 16);
/*     */ 
/* 204 */     BigInteger localBigInteger9 = new BigInteger("f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d0782675159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e13c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243bcca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a", 16);
/*     */ 
/* 213 */     dsaCache.put(Integer.valueOf(512), new DSAParameterSpec(localBigInteger1, localBigInteger2, localBigInteger3));
/*     */ 
/* 215 */     dsaCache.put(Integer.valueOf(768), new DSAParameterSpec(localBigInteger4, localBigInteger5, localBigInteger6));
/*     */ 
/* 217 */     dsaCache.put(Integer.valueOf(1024), new DSAParameterSpec(localBigInteger7, localBigInteger8, localBigInteger9));
/*     */ 
/* 221 */     dhCache.put(Integer.valueOf(512), new DHParameterSpec(localBigInteger1, localBigInteger3));
/* 222 */     dhCache.put(Integer.valueOf(768), new DHParameterSpec(localBigInteger4, localBigInteger6));
/* 223 */     dhCache.put(Integer.valueOf(1024), new DHParameterSpec(localBigInteger7, localBigInteger9));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.ParameterCache
 * JD-Core Version:    0.6.2
 */