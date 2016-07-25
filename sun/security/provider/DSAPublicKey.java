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
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgIdDSA;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X509Key;
/*     */ 
/*     */ public class DSAPublicKey extends X509Key
/*     */   implements java.security.interfaces.DSAPublicKey, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2994193307391104133L;
/*     */   private BigInteger y;
/*     */ 
/*     */   public DSAPublicKey()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DSAPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */     throws InvalidKeyException
/*     */   {
/*  87 */     this.y = paramBigInteger1;
/*  88 */     this.algid = new AlgIdDSA(paramBigInteger2, paramBigInteger3, paramBigInteger4);
/*     */     try
/*     */     {
/*  91 */       this.key = new DerValue((byte)2, paramBigInteger1.toByteArray()).toByteArray();
/*     */ 
/*  93 */       encode();
/*     */     } catch (IOException localIOException) {
/*  95 */       throw new InvalidKeyException("could not DER encode y: " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DSAPublicKey(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/* 104 */     decode(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public DSAParams getParams()
/*     */   {
/*     */     try
/*     */     {
/* 113 */       if ((this.algid instanceof DSAParams)) {
/* 114 */         return (DSAParams)this.algid;
/*     */       }
/*     */ 
/* 117 */       AlgorithmParameters localAlgorithmParameters = this.algid.getParameters();
/* 118 */       if (localAlgorithmParameters == null) {
/* 119 */         return null;
/*     */       }
/* 121 */       return (DSAParameterSpec)localAlgorithmParameters.getParameterSpec(DSAParameterSpec.class);
/*     */     }
/*     */     catch (InvalidParameterSpecException localInvalidParameterSpecException) {
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public BigInteger getY()
/*     */   {
/* 135 */     return this.y;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 139 */     return "Sun DSA Public Key\n    Parameters:" + this.algid + "\n  y:\n" + Debug.toHexString(this.y) + "\n";
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits() throws InvalidKeyException
/*     */   {
/*     */     try {
/* 145 */       DerInputStream localDerInputStream = new DerInputStream(this.key);
/* 146 */       this.y = localDerInputStream.getBigInteger();
/*     */     } catch (IOException localIOException) {
/* 148 */       throw new InvalidKeyException("Invalid key: y value\n" + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSAPublicKey
 * JD-Core Version:    0.6.2
 */