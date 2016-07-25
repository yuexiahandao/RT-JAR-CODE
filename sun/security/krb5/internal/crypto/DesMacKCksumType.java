/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import java.security.InvalidKeyException;
/*    */ import javax.crypto.spec.DESKeySpec;
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ 
/*    */ public class DesMacKCksumType extends CksumType
/*    */ {
/*    */   public int confounderSize()
/*    */   {
/* 44 */     return 0;
/*    */   }
/*    */ 
/*    */   public int cksumType() {
/* 48 */     return 5;
/*    */   }
/*    */ 
/*    */   public boolean isSafe() {
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   public int cksumSize() {
/* 56 */     return 16;
/*    */   }
/*    */ 
/*    */   public int keyType() {
/* 60 */     return 1;
/*    */   }
/*    */ 
/*    */   public int keySize() {
/* 64 */     return 8;
/*    */   }
/*    */ 
/*    */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt) {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*    */     throws KrbCryptoException
/*    */   {
/*    */     try
/*    */     {
/* 84 */       if (DESKeySpec.isWeak(paramArrayOfByte2, 0))
/* 85 */         paramArrayOfByte2[7] = ((byte)(paramArrayOfByte2[7] ^ 0xF0));
/*    */     }
/*    */     catch (InvalidKeyException localInvalidKeyException)
/*    */     {
/*    */     }
/* 90 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte2.length];
/* 91 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 0, paramArrayOfByte2.length);
/* 92 */     byte[] arrayOfByte2 = Des.des_cksum(arrayOfByte1, paramArrayOfByte1, paramArrayOfByte2);
/* 93 */     return arrayOfByte2;
/*    */   }
/*    */ 
/*    */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2) throws KrbCryptoException
/*    */   {
/* 98 */     byte[] arrayOfByte = calculateKeyedChecksum(paramArrayOfByte1, paramArrayOfByte1.length, paramArrayOfByte2, paramInt2);
/* 99 */     return isChecksumEqual(paramArrayOfByte3, arrayOfByte);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.DesMacKCksumType
 * JD-Core Version:    0.6.2
 */