/*     */ package sun.security.ec;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyFactorySpi;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.ECKey;
/*     */ import java.security.interfaces.ECPrivateKey;
/*     */ import java.security.interfaces.ECPublicKey;
/*     */ import java.security.spec.ECPrivateKeySpec;
/*     */ import java.security.spec.ECPublicKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ 
/*     */ public final class ECKeyFactory extends KeyFactorySpi
/*     */ {
/*     */   public static final KeyFactory INSTANCE;
/*  78 */   public static final Provider ecInternalProvider = local1;
/*     */ 
/*     */   public static ECKey toECKey(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/*  98 */     if ((paramKey instanceof ECKey)) {
/*  99 */       ECKey localECKey = (ECKey)paramKey;
/* 100 */       checkKey(localECKey);
/* 101 */       return localECKey;
/*     */     }
/* 103 */     return (ECKey)INSTANCE.translateKey(paramKey);
/*     */   }
/*     */ 
/*     */   private static void checkKey(ECKey paramECKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 112 */     if ((paramECKey instanceof ECPublicKey))
/*     */     {
/* 113 */       if (!(paramECKey instanceof ECPublicKeyImpl));
/*     */     }
/* 116 */     else if ((paramECKey instanceof ECPrivateKey))
/*     */     {
/* 117 */       if (!(paramECKey instanceof ECPrivateKeyImpl));
/*     */     }
/*     */     else {
/* 121 */       throw new InvalidKeyException("Neither a public nor a private key");
/*     */     }
/*     */ 
/* 124 */     String str = ((Key)paramECKey).getAlgorithm();
/* 125 */     if (!str.equals("EC"))
/* 126 */       throw new InvalidKeyException("Not an EC key: " + str);
/*     */   }
/*     */ 
/*     */   protected Key engineTranslateKey(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 138 */     if (paramKey == null) {
/* 139 */       throw new InvalidKeyException("Key must not be null");
/*     */     }
/* 141 */     String str = paramKey.getAlgorithm();
/* 142 */     if (!str.equals("EC")) {
/* 143 */       throw new InvalidKeyException("Not an EC key: " + str);
/*     */     }
/* 145 */     if ((paramKey instanceof PublicKey))
/* 146 */       return implTranslatePublicKey((PublicKey)paramKey);
/* 147 */     if ((paramKey instanceof PrivateKey)) {
/* 148 */       return implTranslatePrivateKey((PrivateKey)paramKey);
/*     */     }
/* 150 */     throw new InvalidKeyException("Neither a public nor a private key");
/*     */   }
/*     */ 
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       return implGeneratePublic(paramKeySpec);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 160 */       throw localInvalidKeySpecException;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 162 */       throw new InvalidKeySpecException(localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 170 */       return implGeneratePrivate(paramKeySpec);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 172 */       throw localInvalidKeySpecException;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 174 */       throw new InvalidKeySpecException(localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private PublicKey implTranslatePublicKey(PublicKey paramPublicKey)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     Object localObject;
/* 181 */     if ((paramPublicKey instanceof ECPublicKey)) {
/* 182 */       if ((paramPublicKey instanceof ECPublicKeyImpl)) {
/* 183 */         return paramPublicKey;
/*     */       }
/* 185 */       localObject = (ECPublicKey)paramPublicKey;
/* 186 */       return new ECPublicKeyImpl(((ECPublicKey)localObject).getW(), ((ECPublicKey)localObject).getParams());
/*     */     }
/*     */ 
/* 190 */     if ("X.509".equals(paramPublicKey.getFormat())) {
/* 191 */       localObject = paramPublicKey.getEncoded();
/* 192 */       return new ECPublicKeyImpl((byte[])localObject);
/*     */     }
/* 194 */     throw new InvalidKeyException("Public keys must be instance of ECPublicKey or have X.509 encoding");
/*     */   }
/*     */ 
/*     */   private PrivateKey implTranslatePrivateKey(PrivateKey paramPrivateKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 202 */     if ((paramPrivateKey instanceof ECPrivateKey)) {
/* 203 */       if ((paramPrivateKey instanceof ECPrivateKeyImpl)) {
/* 204 */         return paramPrivateKey;
/*     */       }
/* 206 */       ECPrivateKey localECPrivateKey = (ECPrivateKey)paramPrivateKey;
/* 207 */       return new ECPrivateKeyImpl(localECPrivateKey.getS(), localECPrivateKey.getParams());
/*     */     }
/*     */ 
/* 211 */     if ("PKCS#8".equals(paramPrivateKey.getFormat())) {
/* 212 */       return new ECPrivateKeyImpl(paramPrivateKey.getEncoded());
/*     */     }
/* 214 */     throw new InvalidKeyException("Private keys must be instance of ECPrivateKey or have PKCS#8 encoding");
/*     */   }
/*     */ 
/*     */   private PublicKey implGeneratePublic(KeySpec paramKeySpec)
/*     */     throws GeneralSecurityException
/*     */   {
/*     */     Object localObject;
/* 222 */     if ((paramKeySpec instanceof X509EncodedKeySpec)) {
/* 223 */       localObject = (X509EncodedKeySpec)paramKeySpec;
/* 224 */       return new ECPublicKeyImpl(((X509EncodedKeySpec)localObject).getEncoded());
/* 225 */     }if ((paramKeySpec instanceof ECPublicKeySpec)) {
/* 226 */       localObject = (ECPublicKeySpec)paramKeySpec;
/* 227 */       return new ECPublicKeyImpl(((ECPublicKeySpec)localObject).getW(), ((ECPublicKeySpec)localObject).getParams());
/*     */     }
/*     */ 
/* 232 */     throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
/*     */   }
/*     */ 
/*     */   private PrivateKey implGeneratePrivate(KeySpec paramKeySpec)
/*     */     throws GeneralSecurityException
/*     */   {
/*     */     Object localObject;
/* 240 */     if ((paramKeySpec instanceof PKCS8EncodedKeySpec)) {
/* 241 */       localObject = (PKCS8EncodedKeySpec)paramKeySpec;
/* 242 */       return new ECPrivateKeyImpl(((PKCS8EncodedKeySpec)localObject).getEncoded());
/* 243 */     }if ((paramKeySpec instanceof ECPrivateKeySpec)) {
/* 244 */       localObject = (ECPrivateKeySpec)paramKeySpec;
/* 245 */       return new ECPrivateKeyImpl(((ECPrivateKeySpec)localObject).getS(), ((ECPrivateKeySpec)localObject).getParams());
/*     */     }
/* 247 */     throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
/*     */   }
/*     */ 
/*     */   protected <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 258 */       paramKey = engineTranslateKey(paramKey);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 260 */       throw new InvalidKeySpecException(localInvalidKeyException);
/*     */     }
/*     */     Object localObject;
/* 262 */     if ((paramKey instanceof ECPublicKey)) {
/* 263 */       localObject = (ECPublicKey)paramKey;
/* 264 */       if (ECPublicKeySpec.class.isAssignableFrom(paramClass)) {
/* 265 */         return new ECPublicKeySpec(((ECPublicKey)localObject).getW(), ((ECPublicKey)localObject).getParams());
/*     */       }
/*     */ 
/* 269 */       if (X509EncodedKeySpec.class.isAssignableFrom(paramClass)) {
/* 270 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/* 272 */       throw new InvalidKeySpecException("KeySpec must be ECPublicKeySpec or X509EncodedKeySpec for EC public keys");
/*     */     }
/*     */ 
/* 276 */     if ((paramKey instanceof ECPrivateKey)) {
/* 277 */       if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass))
/* 278 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/* 279 */       if (ECPrivateKeySpec.class.isAssignableFrom(paramClass)) {
/* 280 */         localObject = (ECPrivateKey)paramKey;
/* 281 */         return new ECPrivateKeySpec(((ECPrivateKey)localObject).getS(), ((ECPrivateKey)localObject).getParams());
/*     */       }
/*     */ 
/* 286 */       throw new InvalidKeySpecException("KeySpec must be ECPrivateKeySpec or PKCS8EncodedKeySpec for EC private keys");
/*     */     }
/*     */ 
/* 292 */     throw new InvalidKeySpecException("Neither public nor private key");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  64 */     Provider local1 = new Provider("SunEC-Internal", 1.0D, null)
/*     */     {
/*     */     };
/*  65 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*  67 */         this.val$p.put("KeyFactory.EC", "sun.security.ec.ECKeyFactory");
/*  68 */         this.val$p.put("AlgorithmParameters.EC", "sun.security.ec.ECParameters");
/*  69 */         this.val$p.put("Alg.Alias.AlgorithmParameters.1.2.840.10045.2.1", "EC");
/*  70 */         return null;
/*     */       }
/*     */     });
/*     */     try {
/*  74 */       INSTANCE = KeyFactory.getInstance("EC", local1);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  76 */       throw new RuntimeException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.ec.ECKeyFactory
 * JD-Core Version:    0.6.2
 */