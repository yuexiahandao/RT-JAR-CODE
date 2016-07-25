/*     */ package sun.security.ec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.interfaces.ECPrivateKey;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.InvalidParameterSpecException;
/*     */ import sun.security.pkcs.PKCS8Key;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public final class ECPrivateKeyImpl extends PKCS8Key
/*     */   implements ECPrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 88695385615075129L;
/*     */   private BigInteger s;
/*     */   private ECParameterSpec params;
/*     */ 
/*     */   public ECPrivateKeyImpl(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  74 */     decode(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public ECPrivateKeyImpl(BigInteger paramBigInteger, ECParameterSpec paramECParameterSpec)
/*     */     throws InvalidKeyException
/*     */   {
/*  83 */     this.s = paramBigInteger;
/*  84 */     this.params = paramECParameterSpec;
/*     */ 
/*  86 */     this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(paramECParameterSpec));
/*     */     try
/*     */     {
/*  89 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/*  90 */       localDerOutputStream.putInteger(1);
/*  91 */       byte[] arrayOfByte = ECParameters.trimZeroes(paramBigInteger.toByteArray());
/*  92 */       localDerOutputStream.putOctetString(arrayOfByte);
/*  93 */       DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/*  95 */       this.key = localDerValue.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  98 */       throw new InvalidKeyException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 104 */     return "EC";
/*     */   }
/*     */ 
/*     */   public BigInteger getS()
/*     */   {
/* 109 */     return this.s;
/*     */   }
/*     */ 
/*     */   public ECParameterSpec getParams()
/*     */   {
/* 114 */     return this.params;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 122 */       DerInputStream localDerInputStream1 = new DerInputStream(this.key);
/* 123 */       DerValue localDerValue = localDerInputStream1.getDerValue();
/* 124 */       if (localDerValue.tag != 48) {
/* 125 */         throw new IOException("Not a SEQUENCE");
/*     */       }
/* 127 */       DerInputStream localDerInputStream2 = localDerValue.data;
/* 128 */       int i = localDerInputStream2.getInteger();
/* 129 */       if (i != 1) {
/* 130 */         throw new IOException("Version must be 1");
/*     */       }
/* 132 */       byte[] arrayOfByte = localDerInputStream2.getOctetString();
/* 133 */       this.s = new BigInteger(1, arrayOfByte);
/* 134 */       while (localDerInputStream2.available() != 0) {
/* 135 */         localObject = localDerInputStream2.getDerValue();
/* 136 */         if (!((DerValue)localObject).isContextSpecific((byte)0))
/*     */         {
/* 138 */           if (!((DerValue)localObject).isContextSpecific((byte)1))
/*     */           {
/* 141 */             throw new InvalidKeyException("Unexpected value: " + localObject);
/*     */           }
/*     */         }
/*     */       }
/* 144 */       Object localObject = this.algid.getParameters();
/* 145 */       if (localObject == null) {
/* 146 */         throw new InvalidKeyException("EC domain parameters must be encoded in the algorithm identifier");
/*     */       }
/*     */ 
/* 149 */       this.params = ((ECParameterSpec)((AlgorithmParameters)localObject).getParameterSpec(ECParameterSpec.class));
/*     */     } catch (IOException localIOException) {
/* 151 */       throw new InvalidKeyException("Invalid EC private key", localIOException);
/*     */     } catch (InvalidParameterSpecException localInvalidParameterSpecException) {
/* 153 */       throw new InvalidKeyException("Invalid EC private key", localInvalidParameterSpecException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.ec.ECPrivateKeyImpl
 * JD-Core Version:    0.6.2
 */