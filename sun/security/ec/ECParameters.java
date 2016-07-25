/*     */ package sun.security.ec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.AlgorithmParametersSpi;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.ECField;
/*     */ import java.security.spec.ECGenParameterSpec;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.ECPoint;
/*     */ import java.security.spec.EllipticCurve;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public final class ECParameters extends AlgorithmParametersSpi
/*     */ {
/*     */   private ECParameterSpec paramSpec;
/*     */ 
/*     */   public static ECPoint decodePoint(byte[] paramArrayOfByte, EllipticCurve paramEllipticCurve)
/*     */     throws IOException
/*     */   {
/*  87 */     if ((paramArrayOfByte.length == 0) || (paramArrayOfByte[0] != 4)) {
/*  88 */       throw new IOException("Only uncompressed point format supported");
/*     */     }
/*  90 */     int i = paramEllipticCurve.getField().getFieldSize() + 7 >> 3;
/*  91 */     if (paramArrayOfByte.length != i * 2 + 1) {
/*  92 */       throw new IOException("Point does not match field size");
/*     */     }
/*  94 */     byte[] arrayOfByte1 = new byte[i];
/*  95 */     byte[] arrayOfByte2 = new byte[i];
/*  96 */     System.arraycopy(paramArrayOfByte, 1, arrayOfByte1, 0, i);
/*  97 */     System.arraycopy(paramArrayOfByte, i + 1, arrayOfByte2, 0, i);
/*  98 */     return new ECPoint(new BigInteger(1, arrayOfByte1), new BigInteger(1, arrayOfByte2));
/*     */   }
/*     */ 
/*     */   public static byte[] encodePoint(ECPoint paramECPoint, EllipticCurve paramEllipticCurve)
/*     */   {
/* 104 */     int i = paramEllipticCurve.getField().getFieldSize() + 7 >> 3;
/* 105 */     byte[] arrayOfByte1 = trimZeroes(paramECPoint.getAffineX().toByteArray());
/* 106 */     byte[] arrayOfByte2 = trimZeroes(paramECPoint.getAffineY().toByteArray());
/* 107 */     if ((arrayOfByte1.length > i) || (arrayOfByte2.length > i)) {
/* 108 */       throw new RuntimeException("Point coordinates do not match field size");
/*     */     }
/*     */ 
/* 111 */     byte[] arrayOfByte3 = new byte[1 + (i << 1)];
/* 112 */     arrayOfByte3[0] = 4;
/* 113 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte3, i - arrayOfByte1.length + 1, arrayOfByte1.length);
/* 114 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte3.length - arrayOfByte2.length, arrayOfByte2.length);
/* 115 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   static byte[] trimZeroes(byte[] paramArrayOfByte)
/*     */   {
/* 121 */     int i = 0;
/* 122 */     while ((i < paramArrayOfByte.length - 1) && (paramArrayOfByte[i] == 0)) {
/* 123 */       i++;
/*     */     }
/* 125 */     if (i == 0) {
/* 126 */       return paramArrayOfByte;
/*     */     }
/* 128 */     byte[] arrayOfByte = new byte[paramArrayOfByte.length - i];
/* 129 */     System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, arrayOfByte.length);
/* 130 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static NamedCurve getNamedCurve(ECParameterSpec paramECParameterSpec)
/*     */   {
/* 137 */     if (((paramECParameterSpec instanceof NamedCurve)) || (paramECParameterSpec == null)) {
/* 138 */       return (NamedCurve)paramECParameterSpec;
/*     */     }
/*     */ 
/* 148 */     int i = paramECParameterSpec.getCurve().getField().getFieldSize();
/* 149 */     for (ECParameterSpec localECParameterSpec : NamedCurve.knownECParameterSpecs())
/*     */     {
/* 153 */       if ((localECParameterSpec.getCurve().getField().getFieldSize() == i) && 
/* 156 */         (localECParameterSpec.getCurve().equals(paramECParameterSpec.getCurve())) && 
/* 159 */         (localECParameterSpec.getGenerator().equals(paramECParameterSpec.getGenerator())) && 
/* 162 */         (localECParameterSpec.getOrder().equals(paramECParameterSpec.getOrder())) && 
/* 165 */         (localECParameterSpec.getCofactor() == paramECParameterSpec.getCofactor()))
/*     */       {
/* 169 */         return (NamedCurve)localECParameterSpec;
/*     */       }
/*     */     }
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getCurveName(ECParameterSpec paramECParameterSpec)
/*     */   {
/* 177 */     NamedCurve localNamedCurve = getNamedCurve(paramECParameterSpec);
/* 178 */     return localNamedCurve == null ? null : localNamedCurve.getObjectIdentifier().toString();
/*     */   }
/*     */ 
/*     */   public static byte[] encodeParameters(ECParameterSpec paramECParameterSpec)
/*     */   {
/* 183 */     NamedCurve localNamedCurve = getNamedCurve(paramECParameterSpec);
/* 184 */     if (localNamedCurve == null) {
/* 185 */       throw new RuntimeException("Not a known named curve: " + paramECParameterSpec);
/*     */     }
/* 187 */     return localNamedCurve.getEncoded();
/*     */   }
/*     */ 
/*     */   public static ECParameterSpec decodeParameters(byte[] paramArrayOfByte) throws IOException
/*     */   {
/* 192 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/* 193 */     if (localDerValue.tag == 6) {
/* 194 */       ObjectIdentifier localObjectIdentifier = localDerValue.getOID();
/* 195 */       ECParameterSpec localECParameterSpec = NamedCurve.getECParameterSpec(localObjectIdentifier);
/* 196 */       if (localECParameterSpec == null) {
/* 197 */         throw new IOException("Unknown named curve: " + localObjectIdentifier);
/*     */       }
/* 199 */       return localECParameterSpec;
/*     */     }
/*     */ 
/* 202 */     throw new IOException("Only named ECParameters supported");
/*     */   }
/*     */ 
/*     */   static AlgorithmParameters getAlgorithmParameters(ECParameterSpec paramECParameterSpec)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 279 */       AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("EC", ECKeyFactory.ecInternalProvider);
/*     */ 
/* 281 */       localAlgorithmParameters.init(paramECParameterSpec);
/* 282 */       return localAlgorithmParameters;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 284 */       throw new InvalidKeyException("EC parameters error", localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidParameterSpecException
/*     */   {
/* 296 */     if ((paramAlgorithmParameterSpec instanceof ECParameterSpec)) {
/* 297 */       this.paramSpec = getNamedCurve((ECParameterSpec)paramAlgorithmParameterSpec);
/* 298 */       if (this.paramSpec == null) {
/* 299 */         throw new InvalidParameterSpecException("Not a supported named curve: " + paramAlgorithmParameterSpec);
/*     */       }
/*     */     }
/* 302 */     else if ((paramAlgorithmParameterSpec instanceof ECGenParameterSpec)) {
/* 303 */       String str = ((ECGenParameterSpec)paramAlgorithmParameterSpec).getName();
/* 304 */       ECParameterSpec localECParameterSpec = NamedCurve.getECParameterSpec(str);
/* 305 */       if (localECParameterSpec == null) {
/* 306 */         throw new InvalidParameterSpecException("Unknown curve: " + str);
/*     */       }
/* 308 */       this.paramSpec = localECParameterSpec; } else {
/* 309 */       if (paramAlgorithmParameterSpec == null) {
/* 310 */         throw new InvalidParameterSpecException("paramSpec must not be null");
/*     */       }
/*     */ 
/* 313 */       throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void engineInit(byte[] paramArrayOfByte) throws IOException
/*     */   {
/* 319 */     this.paramSpec = decodeParameters(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected void engineInit(byte[] paramArrayOfByte, String paramString) throws IOException {
/* 323 */     engineInit(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> paramClass) throws InvalidParameterSpecException
/*     */   {
/* 328 */     if (paramClass.isAssignableFrom(ECParameterSpec.class))
/* 329 */       return this.paramSpec;
/* 330 */     if (paramClass.isAssignableFrom(ECGenParameterSpec.class)) {
/* 331 */       return new ECGenParameterSpec(getCurveName(this.paramSpec));
/*     */     }
/* 333 */     throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
/*     */   }
/*     */ 
/*     */   protected byte[] engineGetEncoded()
/*     */     throws IOException
/*     */   {
/* 339 */     return encodeParameters(this.paramSpec);
/*     */   }
/*     */ 
/*     */   protected byte[] engineGetEncoded(String paramString) throws IOException {
/* 343 */     return engineGetEncoded();
/*     */   }
/*     */ 
/*     */   protected String engineToString() {
/* 347 */     return this.paramSpec.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.ec.ECParameters
 * JD-Core Version:    0.6.2
 */