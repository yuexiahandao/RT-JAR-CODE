/*     */ package sun.security.pkcs12;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import sun.security.pkcs.ParsingException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ class MacData
/*     */ {
/*     */   private String digestAlgorithmName;
/*     */   private AlgorithmParameters digestAlgorithmParams;
/*     */   private byte[] digest;
/*     */   private byte[] macSalt;
/*     */   private int iterations;
/*  53 */   private byte[] encoded = null;
/*     */ 
/*     */   MacData(DerInputStream paramDerInputStream)
/*     */     throws IOException, ParsingException
/*     */   {
/*  61 */     DerValue[] arrayOfDerValue1 = paramDerInputStream.getSequence(2);
/*     */ 
/*  64 */     DerInputStream localDerInputStream = new DerInputStream(arrayOfDerValue1[0].toByteArray());
/*  65 */     DerValue[] arrayOfDerValue2 = localDerInputStream.getSequence(2);
/*     */ 
/*  68 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(arrayOfDerValue2[0]);
/*  69 */     this.digestAlgorithmName = localAlgorithmId.getName();
/*  70 */     this.digestAlgorithmParams = localAlgorithmId.getParameters();
/*     */ 
/*  72 */     this.digest = arrayOfDerValue2[1].getOctetString();
/*     */ 
/*  75 */     this.macSalt = arrayOfDerValue1[1].getOctetString();
/*     */ 
/*  78 */     if (arrayOfDerValue1.length > 2)
/*  79 */       this.iterations = arrayOfDerValue1[2].getInteger();
/*     */     else
/*  81 */       this.iterations = 1;
/*     */   }
/*     */ 
/*     */   MacData(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  88 */     if (paramString == null) {
/*  89 */       throw new NullPointerException("the algName parameter must be non-null");
/*     */     }
/*     */ 
/*  92 */     AlgorithmId localAlgorithmId = AlgorithmId.get(paramString);
/*  93 */     this.digestAlgorithmName = localAlgorithmId.getName();
/*  94 */     this.digestAlgorithmParams = localAlgorithmId.getParameters();
/*     */ 
/*  96 */     if (paramArrayOfByte1 == null) {
/*  97 */       throw new NullPointerException("the digest parameter must be non-null");
/*     */     }
/*  99 */     if (paramArrayOfByte1.length == 0) {
/* 100 */       throw new IllegalArgumentException("the digest parameter must not be empty");
/*     */     }
/*     */ 
/* 103 */     this.digest = ((byte[])paramArrayOfByte1.clone());
/*     */ 
/* 106 */     this.macSalt = paramArrayOfByte2;
/* 107 */     this.iterations = paramInt;
/*     */ 
/* 111 */     this.encoded = null;
/*     */   }
/*     */ 
/*     */   MacData(AlgorithmParameters paramAlgorithmParameters, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 118 */     if (paramAlgorithmParameters == null) {
/* 119 */       throw new NullPointerException("the algParams parameter must be non-null");
/*     */     }
/*     */ 
/* 122 */     AlgorithmId localAlgorithmId = AlgorithmId.get(paramAlgorithmParameters);
/* 123 */     this.digestAlgorithmName = localAlgorithmId.getName();
/* 124 */     this.digestAlgorithmParams = localAlgorithmId.getParameters();
/*     */ 
/* 126 */     if (paramArrayOfByte1 == null) {
/* 127 */       throw new NullPointerException("the digest parameter must be non-null");
/*     */     }
/* 129 */     if (paramArrayOfByte1.length == 0) {
/* 130 */       throw new IllegalArgumentException("the digest parameter must not be empty");
/*     */     }
/*     */ 
/* 133 */     this.digest = ((byte[])paramArrayOfByte1.clone());
/*     */ 
/* 136 */     this.macSalt = paramArrayOfByte2;
/* 137 */     this.iterations = paramInt;
/*     */ 
/* 141 */     this.encoded = null;
/*     */   }
/*     */ 
/*     */   String getDigestAlgName()
/*     */   {
/* 146 */     return this.digestAlgorithmName;
/*     */   }
/*     */ 
/*     */   byte[] getSalt() {
/* 150 */     return this.macSalt;
/*     */   }
/*     */ 
/*     */   int getIterations() {
/* 154 */     return this.iterations;
/*     */   }
/*     */ 
/*     */   byte[] getDigest() {
/* 158 */     return this.digest;
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws NoSuchAlgorithmException, IOException
/*     */   {
/* 169 */     if (this.encoded != null) {
/* 170 */       return (byte[])this.encoded.clone();
/*     */     }
/* 172 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 173 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 175 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/*     */ 
/* 177 */     AlgorithmId localAlgorithmId = AlgorithmId.get(this.digestAlgorithmName);
/* 178 */     localAlgorithmId.encode(localDerOutputStream3);
/*     */ 
/* 181 */     localDerOutputStream3.putOctetString(this.digest);
/*     */ 
/* 183 */     localDerOutputStream2.write((byte)48, localDerOutputStream3);
/*     */ 
/* 186 */     localDerOutputStream2.putOctetString(this.macSalt);
/*     */ 
/* 189 */     localDerOutputStream2.putInteger(this.iterations);
/*     */ 
/* 192 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 193 */     this.encoded = localDerOutputStream1.toByteArray();
/*     */ 
/* 195 */     return (byte[])this.encoded.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs12.MacData
 * JD-Core Version:    0.6.2
 */