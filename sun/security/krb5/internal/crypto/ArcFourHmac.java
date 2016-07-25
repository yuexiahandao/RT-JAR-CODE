/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ import sun.security.krb5.internal.crypto.dk.ArcFourCrypto;
/*    */ 
/*    */ public class ArcFourHmac
/*    */ {
/* 40 */   private static final ArcFourCrypto CRYPTO = new ArcFourCrypto(128);
/*    */ 
/*    */   public static byte[] stringToKey(char[] paramArrayOfChar)
/*    */     throws GeneralSecurityException
/*    */   {
/* 47 */     return CRYPTO.stringToKey(paramArrayOfChar);
/*    */   }
/*    */ 
/*    */   public static int getChecksumLength()
/*    */   {
/* 52 */     return CRYPTO.getChecksumLength();
/*    */   }
/*    */ 
/*    */   public static byte[] calculateChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3) throws GeneralSecurityException
/*    */   {
/* 57 */     return CRYPTO.calculateChecksum(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] encryptSeq(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 64 */     return CRYPTO.encryptSeq(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] decryptSeq(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 71 */     return CRYPTO.decryptSeq(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 77 */     return CRYPTO.encrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, null, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] encryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException, KrbCryptoException
/*    */   {
/* 85 */     return CRYPTO.encryptRaw(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*    */     throws GeneralSecurityException
/*    */   {
/* 91 */     return CRYPTO.decrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public static byte[] decryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3, byte[] paramArrayOfByte4)
/*    */     throws GeneralSecurityException
/*    */   {
/* 98 */     return CRYPTO.decryptRaw(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3, paramArrayOfByte4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.ArcFourHmac
 * JD-Core Version:    0.6.2
 */