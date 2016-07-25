/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ 
/*     */ public final class RsaMd5CksumType extends CksumType
/*     */ {
/*     */   public int confounderSize()
/*     */   {
/*  45 */     return 0;
/*     */   }
/*     */ 
/*     */   public int cksumType() {
/*  49 */     return 7;
/*     */   }
/*     */ 
/*     */   public boolean isSafe() {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   public int cksumSize() {
/*  57 */     return 16;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  61 */     return 0;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  65 */     return 0;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt)
/*     */     throws KrbCryptoException
/*     */   {
/*  79 */     byte[] arrayOfByte = null;
/*     */     MessageDigest localMessageDigest;
/*     */     try
/*     */     {
/*  81 */       localMessageDigest = MessageDigest.getInstance("MD5");
/*     */     } catch (Exception localException1) {
/*  83 */       throw new KrbCryptoException("JCE provider may not be installed. " + localException1.getMessage());
/*     */     }
/*     */     try {
/*  86 */       localMessageDigest.update(paramArrayOfByte);
/*  87 */       arrayOfByte = localMessageDigest.digest();
/*     */     } catch (Exception localException2) {
/*  89 */       throw new KrbCryptoException(localException2.getMessage());
/*     */     }
/*  91 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2) throws KrbCryptoException
/*     */   {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2) throws KrbCryptoException
/*     */   {
/* 101 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.RsaMd5CksumType
 * JD-Core Version:    0.6.2
 */