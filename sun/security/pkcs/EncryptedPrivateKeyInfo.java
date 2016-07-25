/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public class EncryptedPrivateKeyInfo
/*     */ {
/*     */   private AlgorithmId algid;
/*     */   private byte[] encryptedData;
/*     */   private byte[] encoded;
/*     */ 
/*     */   public EncryptedPrivateKeyInfo(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  65 */     if (paramArrayOfByte == null) {
/*  66 */       throw new IllegalArgumentException("encoding must not be null");
/*     */     }
/*     */ 
/*  69 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/*     */ 
/*  71 */     DerValue[] arrayOfDerValue = new DerValue[2];
/*     */ 
/*  73 */     arrayOfDerValue[0] = localDerValue.data.getDerValue();
/*  74 */     arrayOfDerValue[1] = localDerValue.data.getDerValue();
/*     */ 
/*  76 */     if (localDerValue.data.available() != 0) {
/*  77 */       throw new IOException("overrun, bytes = " + localDerValue.data.available());
/*     */     }
/*     */ 
/*  80 */     this.algid = AlgorithmId.parse(arrayOfDerValue[0]);
/*  81 */     if (arrayOfDerValue[0].data.available() != 0) {
/*  82 */       throw new IOException("encryptionAlgorithm field overrun");
/*     */     }
/*     */ 
/*  85 */     this.encryptedData = arrayOfDerValue[1].getOctetString();
/*  86 */     if (arrayOfDerValue[1].data.available() != 0) {
/*  87 */       throw new IOException("encryptedData field overrun");
/*     */     }
/*  89 */     this.encoded = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public EncryptedPrivateKeyInfo(AlgorithmId paramAlgorithmId, byte[] paramArrayOfByte)
/*     */   {
/*  97 */     this.algid = paramAlgorithmId;
/*  98 */     this.encryptedData = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public AlgorithmId getAlgorithm()
/*     */   {
/* 105 */     return this.algid;
/*     */   }
/*     */ 
/*     */   public byte[] getEncryptedData()
/*     */   {
/* 112 */     return (byte[])this.encryptedData.clone();
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws IOException
/*     */   {
/* 121 */     if (this.encoded != null) return (byte[])this.encoded.clone();
/*     */ 
/* 123 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 124 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 127 */     this.algid.encode(localDerOutputStream2);
/*     */ 
/* 130 */     localDerOutputStream2.putOctetString(this.encryptedData);
/*     */ 
/* 133 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 134 */     this.encoded = localDerOutputStream1.toByteArray();
/*     */ 
/* 136 */     return (byte[])this.encoded.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 140 */     if (this == paramObject)
/* 141 */       return true;
/* 142 */     if (!(paramObject instanceof EncryptedPrivateKeyInfo))
/* 143 */       return false;
/*     */     try {
/* 145 */       byte[] arrayOfByte1 = getEncoded();
/* 146 */       byte[] arrayOfByte2 = ((EncryptedPrivateKeyInfo)paramObject).getEncoded();
/*     */ 
/* 149 */       if (arrayOfByte1.length != arrayOfByte2.length)
/* 150 */         return false;
/* 151 */       for (int i = 0; i < arrayOfByte1.length; i++)
/* 152 */         if (arrayOfByte1[i] != arrayOfByte2[i])
/* 153 */           return false;
/* 154 */       return true; } catch (IOException localIOException) {
/*     */     }
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 166 */     int i = 0;
/*     */ 
/* 168 */     for (int j = 0; j < this.encryptedData.length; j++)
/* 169 */       i += this.encryptedData[j] * j;
/* 170 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.EncryptedPrivateKeyInfo
 * JD-Core Version:    0.6.2
 */