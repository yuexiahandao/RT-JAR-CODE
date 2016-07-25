/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyRep;
/*     */ import java.security.KeyRep.Type;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.X509Key;
/*     */ 
/*     */ public final class RSAPublicKeyImpl extends X509Key
/*     */   implements RSAPublicKey
/*     */ {
/*     */   private static final long serialVersionUID = 2644735423591199609L;
/*     */   private BigInteger n;
/*     */   private BigInteger e;
/*     */ 
/*     */   public RSAPublicKeyImpl(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */     throws InvalidKeyException
/*     */   {
/*  61 */     this.n = paramBigInteger1;
/*  62 */     this.e = paramBigInteger2;
/*  63 */     RSAKeyFactory.checkRSAProviderKeyLengths(paramBigInteger1.bitLength(), paramBigInteger2);
/*     */ 
/*  65 */     this.algid = RSAPrivateCrtKeyImpl.rsaId;
/*     */     try {
/*  67 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/*  68 */       localDerOutputStream.putInteger(paramBigInteger1);
/*  69 */       localDerOutputStream.putInteger(paramBigInteger2);
/*  70 */       DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/*  72 */       this.key = localDerValue.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  75 */       throw new InvalidKeyException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public RSAPublicKeyImpl(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  83 */     decode(paramArrayOfByte);
/*  84 */     RSAKeyFactory.checkRSAProviderKeyLengths(this.n.bitLength(), this.e);
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/*  89 */     return "RSA";
/*     */   }
/*     */ 
/*     */   public BigInteger getModulus()
/*     */   {
/*  94 */     return this.n;
/*     */   }
/*     */ 
/*     */   public BigInteger getPublicExponent()
/*     */   {
/*  99 */     return this.e;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       DerInputStream localDerInputStream1 = new DerInputStream(this.key);
/* 108 */       DerValue localDerValue = localDerInputStream1.getDerValue();
/* 109 */       if (localDerValue.tag != 48) {
/* 110 */         throw new IOException("Not a SEQUENCE");
/*     */       }
/* 112 */       DerInputStream localDerInputStream2 = localDerValue.data;
/* 113 */       this.n = RSAPrivateCrtKeyImpl.getBigInteger(localDerInputStream2);
/* 114 */       this.e = RSAPrivateCrtKeyImpl.getBigInteger(localDerInputStream2);
/* 115 */       if (localDerValue.data.available() != 0)
/* 116 */         throw new IOException("Extra data available");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 119 */       throw new InvalidKeyException("Invalid RSA public key", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 125 */     return "Sun RSA public key, " + this.n.bitLength() + " bits\n  modulus: " + this.n + "\n  public exponent: " + this.e;
/*     */   }
/*     */ 
/*     */   protected Object writeReplace() throws ObjectStreamException
/*     */   {
/* 130 */     return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAPublicKeyImpl
 * JD-Core Version:    0.6.2
 */