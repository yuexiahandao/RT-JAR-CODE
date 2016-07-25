/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ 
/*    */ public class HmacSha1Des3KdCksumType extends CksumType
/*    */ {
/*    */   public int confounderSize()
/*    */   {
/* 41 */     return 8;
/*    */   }
/*    */ 
/*    */   public int cksumType() {
/* 45 */     return 12;
/*    */   }
/*    */ 
/*    */   public boolean isSafe() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   public int cksumSize() {
/* 53 */     return 20;
/*    */   }
/*    */ 
/*    */   public int keyType() {
/* 57 */     return 2;
/*    */   }
/*    */ 
/*    */   public int keySize() {
/* 61 */     return 24;
/*    */   }
/*    */ 
/*    */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt) {
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*    */     throws KrbCryptoException
/*    */   {
/*    */     try
/*    */     {
/* 79 */       return Des3.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
/*    */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 81 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 82 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 83 */       throw localKrbCryptoException;
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2)
/*    */     throws KrbCryptoException
/*    */   {
/*    */     try
/*    */     {
/* 99 */       byte[] arrayOfByte = Des3.calculateChecksum(paramArrayOfByte2, paramInt2, paramArrayOfByte1, 0, paramInt1);
/*    */ 
/* 102 */       return isChecksumEqual(paramArrayOfByte3, arrayOfByte);
/*    */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 104 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 105 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 106 */       throw localKrbCryptoException;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.HmacSha1Des3KdCksumType
 * JD-Core Version:    0.6.2
 */