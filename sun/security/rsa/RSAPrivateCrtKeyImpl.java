/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.interfaces.RSAPrivateCrtKey;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import sun.security.pkcs.PKCS8Key;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public final class RSAPrivateCrtKeyImpl extends PKCS8Key
/*     */   implements RSAPrivateCrtKey
/*     */ {
/*     */   private static final long serialVersionUID = -1326088454257084918L;
/*     */   private BigInteger n;
/*     */   private BigInteger e;
/*     */   private BigInteger d;
/*     */   private BigInteger p;
/*     */   private BigInteger q;
/*     */   private BigInteger pe;
/*     */   private BigInteger qe;
/*     */   private BigInteger coeff;
/*  66 */   static final AlgorithmId rsaId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);
/*     */ 
/*     */   public static RSAPrivateKey newKey(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  75 */     RSAPrivateCrtKeyImpl localRSAPrivateCrtKeyImpl = new RSAPrivateCrtKeyImpl(paramArrayOfByte);
/*  76 */     if (localRSAPrivateCrtKeyImpl.getPublicExponent().signum() == 0)
/*     */     {
/*  78 */       return new RSAPrivateKeyImpl(localRSAPrivateCrtKeyImpl.getModulus(), localRSAPrivateCrtKeyImpl.getPrivateExponent());
/*     */     }
/*     */ 
/*  83 */     return localRSAPrivateCrtKeyImpl;
/*     */   }
/*     */ 
/*     */   RSAPrivateCrtKeyImpl(byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  91 */     decode(paramArrayOfByte);
/*  92 */     RSAKeyFactory.checkRSAProviderKeyLengths(this.n.bitLength(), this.e);
/*     */   }
/*     */ 
/*     */   RSAPrivateCrtKeyImpl(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
/*     */     throws InvalidKeyException
/*     */   {
/* 102 */     this.n = paramBigInteger1;
/* 103 */     this.e = paramBigInteger2;
/* 104 */     this.d = paramBigInteger3;
/* 105 */     this.p = paramBigInteger4;
/* 106 */     this.q = paramBigInteger5;
/* 107 */     this.pe = paramBigInteger6;
/* 108 */     this.qe = paramBigInteger7;
/* 109 */     this.coeff = paramBigInteger8;
/* 110 */     RSAKeyFactory.checkRSAProviderKeyLengths(paramBigInteger1.bitLength(), paramBigInteger2);
/*     */ 
/* 113 */     this.algid = rsaId;
/*     */     try {
/* 115 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 116 */       localDerOutputStream.putInteger(0);
/* 117 */       localDerOutputStream.putInteger(paramBigInteger1);
/* 118 */       localDerOutputStream.putInteger(paramBigInteger2);
/* 119 */       localDerOutputStream.putInteger(paramBigInteger3);
/* 120 */       localDerOutputStream.putInteger(paramBigInteger4);
/* 121 */       localDerOutputStream.putInteger(paramBigInteger5);
/* 122 */       localDerOutputStream.putInteger(paramBigInteger6);
/* 123 */       localDerOutputStream.putInteger(paramBigInteger7);
/* 124 */       localDerOutputStream.putInteger(paramBigInteger8);
/* 125 */       DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/* 127 */       this.key = localDerValue.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 130 */       throw new InvalidKeyException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 136 */     return "RSA";
/*     */   }
/*     */ 
/*     */   public BigInteger getModulus()
/*     */   {
/* 141 */     return this.n;
/*     */   }
/*     */ 
/*     */   public BigInteger getPublicExponent()
/*     */   {
/* 146 */     return this.e;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrivateExponent()
/*     */   {
/* 151 */     return this.d;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeP()
/*     */   {
/* 156 */     return this.p;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeQ()
/*     */   {
/* 161 */     return this.q;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentP()
/*     */   {
/* 166 */     return this.pe;
/*     */   }
/*     */ 
/*     */   public BigInteger getPrimeExponentQ()
/*     */   {
/* 171 */     return this.qe;
/*     */   }
/*     */ 
/*     */   public BigInteger getCrtCoefficient()
/*     */   {
/* 176 */     return this.coeff;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 184 */       DerInputStream localDerInputStream1 = new DerInputStream(this.key);
/* 185 */       DerValue localDerValue = localDerInputStream1.getDerValue();
/* 186 */       if (localDerValue.tag != 48) {
/* 187 */         throw new IOException("Not a SEQUENCE");
/*     */       }
/* 189 */       DerInputStream localDerInputStream2 = localDerValue.data;
/* 190 */       int i = localDerInputStream2.getInteger();
/* 191 */       if (i != 0) {
/* 192 */         throw new IOException("Version must be 0");
/*     */       }
/* 194 */       this.n = getBigInteger(localDerInputStream2);
/* 195 */       this.e = getBigInteger(localDerInputStream2);
/* 196 */       this.d = getBigInteger(localDerInputStream2);
/* 197 */       this.p = getBigInteger(localDerInputStream2);
/* 198 */       this.q = getBigInteger(localDerInputStream2);
/* 199 */       this.pe = getBigInteger(localDerInputStream2);
/* 200 */       this.qe = getBigInteger(localDerInputStream2);
/* 201 */       this.coeff = getBigInteger(localDerInputStream2);
/* 202 */       if (localDerValue.data.available() != 0)
/* 203 */         throw new IOException("Extra data available");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 206 */       throw new InvalidKeyException("Invalid RSA private key", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static BigInteger getBigInteger(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 214 */     BigInteger localBigInteger = paramDerInputStream.getBigInteger();
/*     */ 
/* 223 */     if (localBigInteger.signum() < 0) {
/* 224 */       localBigInteger = new BigInteger(1, localBigInteger.toByteArray());
/*     */     }
/* 226 */     return localBigInteger;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAPrivateCrtKeyImpl
 * JD-Core Version:    0.6.2
 */