/*     */ package sun.security.krb5.internal.crypto.dk;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.DESKeySpec;
/*     */ import javax.crypto.spec.DESedeKeySpec;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ public class Des3DkCrypto extends DkCrypto
/*     */ {
/*  43 */   private static final byte[] ZERO_IV = { 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*     */   protected int getKeySeedLength()
/*     */   {
/*  49 */     return 168;
/*     */   }
/*     */ 
/*     */   public byte[] stringToKey(char[] paramArrayOfChar) throws GeneralSecurityException {
/*  53 */     byte[] arrayOfByte1 = null;
/*     */     try {
/*  55 */       arrayOfByte1 = charToUtf8(paramArrayOfChar);
/*  56 */       return stringToKey(arrayOfByte1, null);
/*     */     } finally {
/*  58 */       if (arrayOfByte1 != null)
/*  59 */         Arrays.fill(arrayOfByte1, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] stringToKey(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/*  68 */     if ((paramArrayOfByte2 != null) && (paramArrayOfByte2.length > 0)) {
/*  69 */       throw new RuntimeException("Invalid parameter to stringToKey");
/*     */     }
/*     */ 
/*  72 */     byte[] arrayOfByte = randomToKey(nfold(paramArrayOfByte1, getKeySeedLength()));
/*  73 */     return dk(arrayOfByte, KERBEROS_CONSTANT);
/*     */   }
/*     */ 
/*     */   public byte[] parityFix(byte[] paramArrayOfByte)
/*     */     throws GeneralSecurityException
/*     */   {
/*  79 */     setParityBit(paramArrayOfByte);
/*  80 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   protected byte[] randomToKey(byte[] paramArrayOfByte)
/*     */   {
/*  92 */     if (paramArrayOfByte.length != 21) {
/*  93 */       throw new IllegalArgumentException("input must be 168 bits");
/*     */     }
/*     */ 
/*  96 */     byte[] arrayOfByte1 = keyCorrection(des3Expand(paramArrayOfByte, 0, 7));
/*  97 */     byte[] arrayOfByte2 = keyCorrection(des3Expand(paramArrayOfByte, 7, 14));
/*  98 */     byte[] arrayOfByte3 = keyCorrection(des3Expand(paramArrayOfByte, 14, 21));
/*     */ 
/* 100 */     byte[] arrayOfByte4 = new byte[24];
/* 101 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte4, 0, 8);
/* 102 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 8, 8);
/* 103 */     System.arraycopy(arrayOfByte3, 0, arrayOfByte4, 16, 8);
/*     */ 
/* 105 */     return arrayOfByte4;
/*     */   }
/*     */ 
/*     */   private static byte[] keyCorrection(byte[] paramArrayOfByte)
/*     */   {
/*     */     try {
/* 111 */       if (DESKeySpec.isWeak(paramArrayOfByte, 0))
/* 112 */         paramArrayOfByte[7] = ((byte)(paramArrayOfByte[7] ^ 0xF0));
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/*     */     }
/* 117 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   private static byte[] des3Expand(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 142 */     if (paramInt2 - paramInt1 != 7) {
/* 143 */       throw new IllegalArgumentException("Invalid length of DES Key Value:" + paramInt1 + "," + paramInt2);
/*     */     }
/*     */ 
/* 146 */     byte[] arrayOfByte = new byte[8];
/* 147 */     int i = 0;
/* 148 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, 7);
/* 149 */     int j = 0;
/*     */ 
/* 152 */     for (int k = paramInt1; k < paramInt2; k++) {
/* 153 */       int m = (byte)(paramArrayOfByte[k] & 0x1);
/*     */ 
/* 158 */       j = (byte)(j + 1);
/* 159 */       if (m != 0) {
/* 160 */         i = (byte)(i | m << j);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 167 */     arrayOfByte[7] = i;
/* 168 */     setParityBit(arrayOfByte);
/* 169 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static void setParityBit(byte[] paramArrayOfByte)
/*     */   {
/* 177 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 178 */       int j = paramArrayOfByte[i] & 0xFE;
/* 179 */       j |= Integer.bitCount(j) & 0x1 ^ 0x1;
/* 180 */       paramArrayOfByte[i] = ((byte)j);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Cipher getCipher(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws GeneralSecurityException
/*     */   {
/* 187 */     SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("desede");
/*     */ 
/* 190 */     DESedeKeySpec localDESedeKeySpec = new DESedeKeySpec(paramArrayOfByte1, 0);
/*     */ 
/* 193 */     SecretKey localSecretKey = localSecretKeyFactory.generateSecret(localDESedeKeySpec);
/*     */ 
/* 196 */     if (paramArrayOfByte2 == null) {
/* 197 */       paramArrayOfByte2 = ZERO_IV;
/*     */     }
/*     */ 
/* 202 */     Cipher localCipher = Cipher.getInstance("DESede/CBC/NoPadding");
/* 203 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2, 0, paramArrayOfByte2.length);
/*     */ 
/* 206 */     localCipher.init(paramInt, localSecretKey, localIvParameterSpec);
/*     */ 
/* 208 */     return localCipher;
/*     */   }
/*     */ 
/*     */   public int getChecksumLength() {
/* 212 */     return 20;
/*     */   }
/*     */ 
/*     */   protected byte[] getHmac(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 218 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "HmacSHA1");
/* 219 */     Mac localMac = Mac.getInstance("HmacSHA1");
/* 220 */     localMac.init(localSecretKeySpec);
/* 221 */     return localMac.doFinal(paramArrayOfByte2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.dk.Des3DkCrypto
 * JD-Core Version:    0.6.2
 */