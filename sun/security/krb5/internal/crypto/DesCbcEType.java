/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import sun.security.krb5.Confounder;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ 
/*     */ abstract class DesCbcEType extends EType
/*     */ {
/*     */   protected abstract byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt)
/*     */     throws KrbCryptoException;
/*     */ 
/*     */   public int blockSize()
/*     */   {
/*  43 */     return 8;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  47 */     return 1;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  51 */     return 8;
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws KrbCryptoException
/*     */   {
/*  65 */     byte[] arrayOfByte = new byte[keySize()];
/*  66 */     return encrypt(paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt)
/*     */     throws KrbCryptoException
/*     */   {
/*  90 */     if (paramArrayOfByte2.length > 8) {
/*  91 */       throw new KrbCryptoException("Invalid DES Key!");
/*     */     }
/*  93 */     int i = paramArrayOfByte1.length + confounderSize() + checksumSize();
/*     */     byte[] arrayOfByte1;
/*     */     int j;
/* 103 */     if (i % blockSize() == 0) {
/* 104 */       arrayOfByte1 = new byte[i + blockSize()];
/* 105 */       j = 8;
/*     */     }
/*     */     else {
/* 108 */       arrayOfByte1 = new byte[i + blockSize() - i % blockSize()];
/* 109 */       j = (byte)(blockSize() - i % blockSize());
/*     */     }
/* 111 */     for (int k = i; k < arrayOfByte1.length; k++) {
/* 112 */       arrayOfByte1[k] = j;
/*     */     }
/* 114 */     byte[] arrayOfByte2 = Confounder.bytes(confounderSize());
/* 115 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, confounderSize());
/* 116 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, startOfData(), paramArrayOfByte1.length);
/* 117 */     byte[] arrayOfByte3 = calculateChecksum(arrayOfByte1, arrayOfByte1.length);
/* 118 */     System.arraycopy(arrayOfByte3, 0, arrayOfByte1, startOfChecksum(), checksumSize());
/*     */ 
/* 120 */     byte[] arrayOfByte4 = new byte[arrayOfByte1.length];
/* 121 */     Des.cbc_encrypt(arrayOfByte1, arrayOfByte4, paramArrayOfByte2, paramArrayOfByte3, true);
/* 122 */     return arrayOfByte4;
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws KrbApErrException, KrbCryptoException
/*     */   {
/* 134 */     byte[] arrayOfByte = new byte[keySize()];
/* 135 */     return decrypt(paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt)
/*     */     throws KrbApErrException, KrbCryptoException
/*     */   {
/* 158 */     if (paramArrayOfByte2.length > 8) {
/* 159 */       throw new KrbCryptoException("Invalid DES Key!");
/*     */     }
/* 161 */     byte[] arrayOfByte = new byte[paramArrayOfByte1.length];
/* 162 */     Des.cbc_encrypt(paramArrayOfByte1, arrayOfByte, paramArrayOfByte2, paramArrayOfByte3, false);
/* 163 */     if (!isChecksumValid(arrayOfByte))
/* 164 */       throw new KrbApErrException(31);
/* 165 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private void copyChecksumField(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
/* 169 */     for (int i = 0; i < checksumSize(); i++)
/* 170 */       paramArrayOfByte1[(startOfChecksum() + i)] = paramArrayOfByte2[i];
/*     */   }
/*     */ 
/*     */   private byte[] checksumField(byte[] paramArrayOfByte) {
/* 174 */     byte[] arrayOfByte = new byte[checksumSize()];
/* 175 */     for (int i = 0; i < checksumSize(); i++)
/* 176 */       arrayOfByte[i] = paramArrayOfByte[(startOfChecksum() + i)];
/* 177 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private void resetChecksumField(byte[] paramArrayOfByte) {
/* 181 */     for (int i = startOfChecksum(); i < startOfChecksum() + checksumSize(); 
/* 182 */       i++)
/* 183 */       paramArrayOfByte[i] = 0;
/*     */   }
/*     */ 
/*     */   private byte[] generateChecksum(byte[] paramArrayOfByte)
/*     */     throws KrbCryptoException
/*     */   {
/* 196 */     byte[] arrayOfByte1 = checksumField(paramArrayOfByte);
/* 197 */     resetChecksumField(paramArrayOfByte);
/* 198 */     byte[] arrayOfByte2 = calculateChecksum(paramArrayOfByte, paramArrayOfByte.length);
/* 199 */     copyChecksumField(paramArrayOfByte, arrayOfByte1);
/* 200 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private boolean isChecksumEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
/* 204 */     if (paramArrayOfByte1 == paramArrayOfByte2)
/* 205 */       return true;
/* 206 */     if (((paramArrayOfByte1 == null) && (paramArrayOfByte2 != null)) || ((paramArrayOfByte1 != null) && (paramArrayOfByte2 == null)))
/*     */     {
/* 208 */       return false;
/* 209 */     }if (paramArrayOfByte1.length != paramArrayOfByte2.length)
/* 210 */       return false;
/* 211 */     for (int i = 0; i < paramArrayOfByte1.length; i++)
/* 212 */       if (paramArrayOfByte1[i] != paramArrayOfByte2[i])
/* 213 */         return false;
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isChecksumValid(byte[] paramArrayOfByte) throws KrbCryptoException {
/* 218 */     byte[] arrayOfByte1 = checksumField(paramArrayOfByte);
/* 219 */     byte[] arrayOfByte2 = generateChecksum(paramArrayOfByte);
/* 220 */     return isChecksumEqual(arrayOfByte1, arrayOfByte2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.DesCbcEType
 * JD-Core Version:    0.6.2
 */