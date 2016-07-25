/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ 
/*     */ public class HmacMd5ArcFourCksumType extends CksumType
/*     */ {
/*     */   public int confounderSize()
/*     */   {
/*  47 */     return 8;
/*     */   }
/*     */ 
/*     */   public int cksumType() {
/*  51 */     return -138;
/*     */   }
/*     */ 
/*     */   public boolean isSafe() {
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public int cksumSize() {
/*  59 */     return 16;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  63 */     return 4;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  67 */     return 16;
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
/*  85 */       return ArcFourHmac.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
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
/* 105 */       byte[] arrayOfByte = ArcFourHmac.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
/*     */ 
/* 108 */       return isChecksumEqual(paramArrayOfByte3, arrayOfByte);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 110 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 111 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 112 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.HmacMd5ArcFourCksumType
 * JD-Core Version:    0.6.2
 */