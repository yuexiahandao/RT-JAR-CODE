/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import sun.security.pkcs.PKCS8Key;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgIdDSA;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public final class DSAPrivateKey extends PKCS8Key
/*     */   implements java.security.interfaces.DSAPrivateKey, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3244453684193605938L;
/*     */   private BigInteger x;
/*     */ 
/*     */   public DSAPrivateKey()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DSAPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */     throws InvalidKeyException
/*     */   {
/*  77 */     this.x = paramBigInteger1;
/*  78 */     this.algid = new AlgIdDSA(paramBigInteger2, paramBigInteger3, paramBigInteger4);
/*     */     try
/*     */     {
/*  81 */       this.key = new DerValue((byte)2, paramBigInteger1.toByteArray()).toByteArray();
/*     */ 
/*  83 */       encode();
/*     */     } catch (IOException localIOException) {
/*  85 */       InvalidKeyException localInvalidKeyException = new InvalidKeyException("could not DER encode x: " + localIOException.getMessage());
/*     */ 
/*  87 */       localInvalidKeyException.initCause(localIOException);
/*  88 */       throw localInvalidKeyException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public DSAPrivateKey(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  96 */     clearOldKey();
/*  97 */     decode(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public DSAParams getParams()
/*     */   {
/*     */     try
/*     */     {
/* 106 */       if ((this.algid instanceof DSAParams)) {
/* 107 */         return (DSAParams)this.algid;
/*     */       }
/*     */ 
/* 110 */       AlgorithmParameters localAlgorithmParameters = this.algid.getParameters();
/* 111 */       if (localAlgorithmParameters == null) {
/* 112 */         return null;
/*     */       }
/* 114 */       return (DSAParameterSpec)localAlgorithmParameters.getParameterSpec(DSAParameterSpec.class);
/*     */     }
/*     */     catch (InvalidParameterSpecException localInvalidParameterSpecException) {
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public BigInteger getX()
/*     */   {
/* 128 */     return this.x;
/*     */   }
/*     */ 
/*     */   private void clearOldKey()
/*     */   {
/*     */     int i;
/* 133 */     if (this.encodedKey != null) {
/* 134 */       for (i = 0; i < this.encodedKey.length; i++) {
/* 135 */         this.encodedKey[i] = 0;
/*     */       }
/*     */     }
/* 138 */     if (this.key != null)
/* 139 */       for (i = 0; i < this.key.length; i++)
/* 140 */         this.key[i] = 0;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits() throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 147 */       DerInputStream localDerInputStream = new DerInputStream(this.key);
/* 148 */       this.x = localDerInputStream.getBigInteger();
/*     */     } catch (IOException localIOException) {
/* 150 */       InvalidKeyException localInvalidKeyException = new InvalidKeyException(localIOException.getMessage());
/* 151 */       localInvalidKeyException.initCause(localIOException);
/* 152 */       throw localInvalidKeyException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAPrivateKey
 * JD-Core Version:    0.6.2
 */