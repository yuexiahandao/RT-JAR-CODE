/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ 
/*     */ public class HmacSha1Aes256CksumType extends CksumType
/*     */ {
/*     */   public int confounderSize()
/*     */   {
/*  47 */     return 16;
/*     */   }
/*     */ 
/*     */   public int cksumType() {
/*  51 */     return 16;
/*     */   }
/*     */ 
/*     */   public boolean isSafe() {
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public int cksumSize() {
/*  59 */     return 12;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  63 */     return 3;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  67 */     return 32;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt) {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*     */     throws KrbCryptoException
/*     */   {
/*     */     try
/*     */     {
/*  85 */       return Aes256.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/*  87 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/*  88 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/*  89 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2)
/*     */     throws KrbCryptoException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       byte[] arrayOfByte = Aes256.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
/*     */ 
/* 107 */       return isChecksumEqual(paramArrayOfByte3, arrayOfByte);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 109 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 110 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 111 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.HmacSha1Aes256CksumType
 * JD-Core Version:    0.6.2
 */