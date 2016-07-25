/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactorySpi;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.spec.DSAPrivateKeySpec;
/*     */ import java.security.spec.DSAPublicKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class DSAKeyFactory extends KeyFactorySpi
/*     */ {
/*  78 */   static final boolean SERIAL_INTEROP = "true".equalsIgnoreCase(str);
/*     */   private static final String SERIAL_PROP = "sun.security.key.serial.interop";
/*     */ 
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/*  95 */       if ((paramKeySpec instanceof DSAPublicKeySpec)) {
/*  96 */         DSAPublicKeySpec localDSAPublicKeySpec = (DSAPublicKeySpec)paramKeySpec;
/*  97 */         if (SERIAL_INTEROP) {
/*  98 */           return new DSAPublicKey(localDSAPublicKeySpec.getY(), localDSAPublicKeySpec.getP(), localDSAPublicKeySpec.getQ(), localDSAPublicKeySpec.getG());
/*     */         }
/*     */ 
/* 103 */         return new DSAPublicKeyImpl(localDSAPublicKeySpec.getY(), localDSAPublicKeySpec.getP(), localDSAPublicKeySpec.getQ(), localDSAPublicKeySpec.getG());
/*     */       }
/*     */ 
/* 108 */       if ((paramKeySpec instanceof X509EncodedKeySpec)) {
/* 109 */         if (SERIAL_INTEROP) {
/* 110 */           return new DSAPublicKey(((X509EncodedKeySpec)paramKeySpec).getEncoded());
/*     */         }
/*     */ 
/* 113 */         return new DSAPublicKeyImpl(((X509EncodedKeySpec)paramKeySpec).getEncoded());
/*     */       }
/*     */ 
/* 117 */       throw new InvalidKeySpecException("Inappropriate key specification");
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 121 */       throw new InvalidKeySpecException("Inappropriate key specification: " + localInvalidKeyException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       if ((paramKeySpec instanceof DSAPrivateKeySpec)) {
/* 141 */         DSAPrivateKeySpec localDSAPrivateKeySpec = (DSAPrivateKeySpec)paramKeySpec;
/* 142 */         return new DSAPrivateKey(localDSAPrivateKeySpec.getX(), localDSAPrivateKeySpec.getP(), localDSAPrivateKeySpec.getQ(), localDSAPrivateKeySpec.getG());
/*     */       }
/*     */ 
/* 147 */       if ((paramKeySpec instanceof PKCS8EncodedKeySpec)) {
/* 148 */         return new DSAPrivateKey(((PKCS8EncodedKeySpec)paramKeySpec).getEncoded());
/*     */       }
/*     */ 
/* 152 */       throw new InvalidKeySpecException("Inappropriate key specification");
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 156 */       throw new InvalidKeySpecException("Inappropriate key specification: " + localInvalidKeyException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
/*     */     throws InvalidKeySpecException
/*     */   {
/*     */     try
/*     */     {
/*     */       Class localClass1;
/*     */       Class localClass2;
/*     */       Object localObject;
/*     */       DSAParams localDSAParams;
/* 185 */       if ((paramKey instanceof java.security.interfaces.DSAPublicKey))
/*     */       {
/* 188 */         localClass1 = Class.forName("java.security.spec.DSAPublicKeySpec");
/*     */ 
/* 190 */         localClass2 = Class.forName("java.security.spec.X509EncodedKeySpec");
/*     */ 
/* 193 */         if (localClass1.isAssignableFrom(paramClass)) {
/* 194 */           localObject = (java.security.interfaces.DSAPublicKey)paramKey;
/*     */ 
/* 196 */           localDSAParams = ((java.security.interfaces.DSAPublicKey)localObject).getParams();
/* 197 */           return new DSAPublicKeySpec(((java.security.interfaces.DSAPublicKey)localObject).getY(), localDSAParams.getP(), localDSAParams.getQ(), localDSAParams.getG());
/*     */         }
/*     */ 
/* 202 */         if (localClass2.isAssignableFrom(paramClass)) {
/* 203 */           return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */         }
/*     */ 
/* 206 */         throw new InvalidKeySpecException("Inappropriate key specification");
/*     */       }
/*     */ 
/* 210 */       if ((paramKey instanceof java.security.interfaces.DSAPrivateKey))
/*     */       {
/* 213 */         localClass1 = Class.forName("java.security.spec.DSAPrivateKeySpec");
/*     */ 
/* 215 */         localClass2 = Class.forName("java.security.spec.PKCS8EncodedKeySpec");
/*     */ 
/* 218 */         if (localClass1.isAssignableFrom(paramClass)) {
/* 219 */           localObject = (java.security.interfaces.DSAPrivateKey)paramKey;
/*     */ 
/* 221 */           localDSAParams = ((java.security.interfaces.DSAPrivateKey)localObject).getParams();
/* 222 */           return new DSAPrivateKeySpec(((java.security.interfaces.DSAPrivateKey)localObject).getX(), localDSAParams.getP(), localDSAParams.getQ(), localDSAParams.getG());
/*     */         }
/*     */ 
/* 227 */         if (localClass2.isAssignableFrom(paramClass)) {
/* 228 */           return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/*     */         }
/*     */ 
/* 231 */         throw new InvalidKeySpecException("Inappropriate key specification");
/*     */       }
/*     */ 
/* 236 */       throw new InvalidKeySpecException("Inappropriate key type");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 240 */       throw new InvalidKeySpecException("Unsupported key specification: " + localClassNotFoundException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Key engineTranslateKey(Key paramKey)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/*     */       Object localObject;
/* 260 */       if ((paramKey instanceof java.security.interfaces.DSAPublicKey))
/*     */       {
/* 262 */         if ((paramKey instanceof DSAPublicKey)) {
/* 263 */           return paramKey;
/*     */         }
/*     */ 
/* 266 */         localObject = (DSAPublicKeySpec)engineGetKeySpec(paramKey, DSAPublicKeySpec.class);
/*     */ 
/* 269 */         return engineGeneratePublic((KeySpec)localObject);
/*     */       }
/* 271 */       if ((paramKey instanceof java.security.interfaces.DSAPrivateKey))
/*     */       {
/* 273 */         if ((paramKey instanceof DSAPrivateKey)) {
/* 274 */           return paramKey;
/*     */         }
/*     */ 
/* 277 */         localObject = (DSAPrivateKeySpec)engineGetKeySpec(paramKey, DSAPrivateKeySpec.class);
/*     */ 
/* 280 */         return engineGeneratePrivate((KeySpec)localObject);
/*     */       }
/*     */ 
/* 283 */       throw new InvalidKeyException("Wrong algorithm type");
/*     */     }
/*     */     catch (InvalidKeySpecException localInvalidKeySpecException)
/*     */     {
/* 287 */       throw new InvalidKeyException("Cannot translate key: " + localInvalidKeySpecException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  76 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.security.key.serial.interop", null));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAKeyFactory
 * JD-Core Version:    0.6.2
 */