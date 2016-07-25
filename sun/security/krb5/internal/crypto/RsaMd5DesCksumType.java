/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.MessageDigest;
/*     */ import javax.crypto.spec.DESKeySpec;
/*     */ import sun.security.krb5.Confounder;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ 
/*     */ public final class RsaMd5DesCksumType extends CksumType
/*     */ {
/*     */   public int confounderSize()
/*     */   {
/*  50 */     return 8;
/*     */   }
/*     */ 
/*     */   public int cksumType() {
/*  54 */     return 8;
/*     */   }
/*     */ 
/*     */   public boolean isSafe() {
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   public int cksumSize() {
/*  62 */     return 24;
/*     */   }
/*     */ 
/*     */   public int keyType() {
/*  66 */     return 1;
/*     */   }
/*     */ 
/*     */   public int keySize() {
/*  70 */     return 8;
/*     */   }
/*     */ 
/*     */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*     */     throws KrbCryptoException
/*     */   {
/*  85 */     byte[] arrayOfByte1 = new byte[paramInt1 + confounderSize()];
/*  86 */     byte[] arrayOfByte2 = Confounder.bytes(confounderSize());
/*  87 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, confounderSize());
/*  88 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, confounderSize(), paramInt1);
/*     */ 
/*  91 */     byte[] arrayOfByte3 = calculateChecksum(arrayOfByte1, arrayOfByte1.length);
/*  92 */     byte[] arrayOfByte4 = new byte[cksumSize()];
/*  93 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 0, confounderSize());
/*  94 */     System.arraycopy(arrayOfByte3, 0, arrayOfByte4, confounderSize(), cksumSize() - confounderSize());
/*     */ 
/*  98 */     byte[] arrayOfByte5 = new byte[keySize()];
/*  99 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte5, 0, paramArrayOfByte2.length);
/* 100 */     for (int i = 0; i < arrayOfByte5.length; i++)
/* 101 */       arrayOfByte5[i] = ((byte)(arrayOfByte5[i] ^ 0xF0));
/*     */     try
/*     */     {
/* 104 */       if (DESKeySpec.isWeak(arrayOfByte5, 0))
/* 105 */         arrayOfByte5[7] = ((byte)(arrayOfByte5[7] ^ 0xF0));
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/*     */     }
/* 110 */     byte[] arrayOfByte6 = new byte[arrayOfByte5.length];
/*     */ 
/* 113 */     byte[] arrayOfByte7 = new byte[arrayOfByte4.length];
/* 114 */     Des.cbc_encrypt(arrayOfByte4, arrayOfByte7, arrayOfByte5, arrayOfByte6, true);
/* 115 */     return arrayOfByte7;
/*     */   }
/*     */ 
/*     */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2)
/*     */     throws KrbCryptoException
/*     */   {
/* 131 */     byte[] arrayOfByte1 = decryptKeyedChecksum(paramArrayOfByte3, paramArrayOfByte2);
/*     */ 
/* 134 */     byte[] arrayOfByte2 = new byte[paramInt1 + confounderSize()];
/* 135 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, confounderSize());
/* 136 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte2, confounderSize(), paramInt1);
/*     */ 
/* 138 */     byte[] arrayOfByte3 = calculateChecksum(arrayOfByte2, arrayOfByte2.length);
/*     */ 
/* 140 */     byte[] arrayOfByte4 = new byte[cksumSize() - confounderSize()];
/* 141 */     System.arraycopy(arrayOfByte1, confounderSize(), arrayOfByte4, 0, cksumSize() - confounderSize());
/*     */ 
/* 144 */     return isChecksumEqual(arrayOfByte4, arrayOfByte3);
/*     */   }
/*     */ 
/*     */   private byte[] decryptKeyedChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws KrbCryptoException
/*     */   {
/* 157 */     byte[] arrayOfByte1 = new byte[keySize()];
/* 158 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 0, paramArrayOfByte2.length);
/* 159 */     for (int i = 0; i < arrayOfByte1.length; i++)
/* 160 */       arrayOfByte1[i] = ((byte)(arrayOfByte1[i] ^ 0xF0));
/*     */     try
/*     */     {
/* 163 */       if (DESKeySpec.isWeak(arrayOfByte1, 0))
/* 164 */         arrayOfByte1[7] = ((byte)(arrayOfByte1[7] ^ 0xF0));
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/*     */     }
/* 169 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
/*     */ 
/* 171 */     byte[] arrayOfByte3 = new byte[paramArrayOfByte1.length];
/* 172 */     Des.cbc_encrypt(paramArrayOfByte1, arrayOfByte3, arrayOfByte1, arrayOfByte2, false);
/* 173 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt)
/*     */     throws KrbCryptoException
/*     */   {
/* 186 */     byte[] arrayOfByte = null;
/*     */     MessageDigest localMessageDigest;
/*     */     try
/*     */     {
/* 188 */       localMessageDigest = MessageDigest.getInstance("MD5");
/*     */     } catch (Exception localException1) {
/* 190 */       throw new KrbCryptoException("JCE provider may not be installed. " + localException1.getMessage());
/*     */     }
/*     */     try {
/* 193 */       localMessageDigest.update(paramArrayOfByte);
/* 194 */       arrayOfByte = localMessageDigest.digest();
/*     */     } catch (Exception localException2) {
/* 196 */       throw new KrbCryptoException(localException2.getMessage());
/*     */     }
/* 198 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.RsaMd5DesCksumType
 * JD-Core Version:    0.6.2
 */