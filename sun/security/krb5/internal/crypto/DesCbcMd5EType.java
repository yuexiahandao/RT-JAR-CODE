/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import sun.security.krb5.KrbCryptoException;
/*    */ 
/*    */ public final class DesCbcMd5EType extends DesCbcEType
/*    */ {
/*    */   public int eType()
/*    */   {
/* 47 */     return 3;
/*    */   }
/*    */ 
/*    */   public int minimumPadSize() {
/* 51 */     return 0;
/*    */   }
/*    */ 
/*    */   public int confounderSize() {
/* 55 */     return 8;
/*    */   }
/*    */ 
/*    */   public int checksumType() {
/* 59 */     return 7;
/*    */   }
/*    */ 
/*    */   public int checksumSize() {
/* 63 */     return 16;
/*    */   }
/*    */ 
/*    */   protected byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt)
/*    */     throws KrbCryptoException
/*    */   {
/* 76 */     MessageDigest localMessageDigest = null;
/*    */     try {
/* 78 */       localMessageDigest = MessageDigest.getInstance("MD5");
/*    */     } catch (Exception localException1) {
/* 80 */       throw new KrbCryptoException("JCE provider may not be installed. " + localException1.getMessage());
/*    */     }
/*    */     try {
/* 83 */       localMessageDigest.update(paramArrayOfByte);
/* 84 */       return localMessageDigest.digest();
/*    */     } catch (Exception localException2) {
/* 86 */       throw new KrbCryptoException(localException2.getMessage());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.DesCbcMd5EType
 * JD-Core Version:    0.6.2
 */