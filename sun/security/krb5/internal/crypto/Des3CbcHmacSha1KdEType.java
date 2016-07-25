/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ 
/*     */ public final class Des3CbcHmacSha1KdEType extends EType
/*     */ {
/*     */   public int eType()
/*     */   {
/*  37 */     return 16;
/*     */   }
/*     */ 
/*     */   public int minimumPadSize() {
/*  41 */     return 0;
/*     */   }
/*     */ 
/*     */   public int confounderSize() {
/*  45 */     return blockSize();
/*     */   }
/*     */ 
/*     */   public int checksumType() {
/*  49 */     return 12;
/*     */   }
/*     */ 
/*     */   public int checksumSize() {
/*  53 */     return Des3.getChecksumLength();
/*     */   }
/*     */ 
/*     */   public int blockSize() {
/*  57 */     return 8;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  61 */     return 2;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  65 */     return 24;
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) throws KrbCryptoException
/*     */   {
/*  70 */     byte[] arrayOfByte = new byte[blockSize()];
/*  71 */     return encrypt(paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt) throws KrbCryptoException
/*     */   {
/*     */     try {
/*  77 */       return Des3.encrypt(paramArrayOfByte2, paramInt, paramArrayOfByte3, paramArrayOfByte1, 0, paramArrayOfByte1.length);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/*  79 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/*  80 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/*  81 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) throws KrbApErrException, KrbCryptoException
/*     */   {
/*  87 */     byte[] arrayOfByte = new byte[blockSize()];
/*  88 */     return decrypt(paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt) throws KrbApErrException, KrbCryptoException
/*     */   {
/*     */     try {
/*  94 */       return Des3.decrypt(paramArrayOfByte2, paramInt, paramArrayOfByte3, paramArrayOfByte1, 0, paramArrayOfByte1.length);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/*  96 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/*  97 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/*  98 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] decryptedData(byte[] paramArrayOfByte)
/*     */   {
/* 106 */     return paramArrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.Des3CbcHmacSha1KdEType
 * JD-Core Version:    0.6.2
 */