/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ import sun.security.krb5.internal.KrbApErrException;
/*    */ 
/*    */ public class DesCbcCrcEType extends DesCbcEType
/*    */ {
/*    */   public int eType()
/*    */   {
/* 44 */     return 1;
/*    */   }
/*    */ 
/*    */   public int minimumPadSize() {
/* 48 */     return 4;
/*    */   }
/*    */ 
/*    */   public int confounderSize() {
/* 52 */     return 8;
/*    */   }
/*    */ 
/*    */   public int checksumType() {
/* 56 */     return 1;
/*    */   }
/*    */ 
/*    */   public int checksumSize() {
/* 60 */     return 4;
/*    */   }
/*    */ 
/*    */   public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*    */     throws KrbCryptoException
/*    */   {
/* 73 */     return encrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte2, paramInt);
/*    */   }
/*    */ 
/*    */   public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*    */     throws KrbApErrException, KrbCryptoException
/*    */   {
/* 85 */     return decrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte2, paramInt);
/*    */   }
/*    */ 
/*    */   protected byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt) {
/* 89 */     return crc32.byte2crc32sum_bytes(paramArrayOfByte, paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.DesCbcCrcEType
 * JD-Core Version:    0.6.2
 */