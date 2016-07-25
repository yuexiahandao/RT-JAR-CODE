/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ import sun.security.krb5.internal.crypto.dk.Des3DkCrypto;
/*    */ 
/*    */ public class Des3
/*    */ {
/* 37 */   private static final Des3DkCrypto CRYPTO = new Des3DkCrypto();
/*    */ 
/*    */   public static byte[] stringToKey(char[] paramArrayOfChar)
/*    */     throws GeneralSecurityException
/*    */   {
/* 44 */     return CRYPTO.stringToKey(paramArrayOfChar);
/*    */   }
/*    */ 
/*    */   public static byte[] parityFix(byte[] paramArrayOfByte) throws GeneralSecurityException
/*    */   {
/* 49 */     return CRYPTO.parityFix(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public static int getChecksumLength()
/*    */   {
/* 54 */     return CRYPTO.getChecksumLength();
/*    */   }
/*    */ 
/*    */   public static byte[] calculateChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3) throws GeneralSecurityException
/*    */   {
/* 59 */     return CRYPTO.calculateChecksum(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 65 */     return CRYPTO.encrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, null, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] encryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 73 */     return CRYPTO.encryptRaw(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException
/*    */   {
/* 79 */     return CRYPTO.decrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] decryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException
/*    */   {
/* 89 */     return CRYPTO.decryptRaw(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.Des3
 * JD-Core Version:    0.6.2
 */