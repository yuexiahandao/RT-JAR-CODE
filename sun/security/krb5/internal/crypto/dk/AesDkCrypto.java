/*     */ package sun.security.krb5.internal.crypto.dk;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import sun.security.krb5.Confounder;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.internal.crypto.KeyUsage;
/*     */ 
/*     */ public class AesDkCrypto extends DkCrypto
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private static final int BLOCK_SIZE = 16;
/*     */   private static final int DEFAULT_ITERATION_COUNT = 4096;
/*  89 */   private static final byte[] ZERO_IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */   private static final int hashSize = 12;
/*     */   private final int keyLength;
/*     */ 
/*     */   public AesDkCrypto(int paramInt)
/*     */   {
/*  95 */     this.keyLength = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getKeySeedLength() {
/*  99 */     return this.keyLength;
/*     */   }
/*     */ 
/*     */   public byte[] stringToKey(char[] paramArrayOfChar, String paramString, byte[] paramArrayOfByte)
/*     */     throws GeneralSecurityException
/*     */   {
/* 105 */     byte[] arrayOfByte1 = null;
/*     */     try {
/* 107 */       arrayOfByte1 = paramString.getBytes("UTF-8");
/* 108 */       return stringToKey(paramArrayOfChar, arrayOfByte1, paramArrayOfByte);
/*     */     } catch (Exception localException) {
/* 110 */       return null;
/*     */     } finally {
/* 112 */       if (arrayOfByte1 != null)
/* 113 */         Arrays.fill(arrayOfByte1, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] stringToKey(char[] paramArrayOfChar, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 121 */     int i = 4096;
/* 122 */     if (paramArrayOfByte2 != null) {
/* 123 */       if (paramArrayOfByte2.length != 4) {
/* 124 */         throw new RuntimeException("Invalid parameter to stringToKey");
/*     */       }
/* 126 */       i = readBigEndian(paramArrayOfByte2, 0, 4);
/*     */     }
/*     */ 
/* 129 */     byte[] arrayOfByte1 = randomToKey(PBKDF2(paramArrayOfChar, paramArrayOfByte1, i, getKeySeedLength()));
/*     */ 
/* 131 */     byte[] arrayOfByte2 = dk(arrayOfByte1, KERBEROS_CONSTANT);
/* 132 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   protected byte[] randomToKey(byte[] paramArrayOfByte)
/*     */   {
/* 137 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   protected Cipher getCipher(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws GeneralSecurityException
/*     */   {
/* 144 */     if (paramArrayOfByte2 == null) {
/* 145 */       paramArrayOfByte2 = ZERO_IV;
/*     */     }
/* 147 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "AES");
/* 148 */     Cipher localCipher = Cipher.getInstance("AES/CBC/NoPadding");
/* 149 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2, 0, paramArrayOfByte2.length);
/* 150 */     localCipher.init(paramInt, localSecretKeySpec, localIvParameterSpec);
/* 151 */     return localCipher;
/*     */   }
/*     */ 
/*     */   public int getChecksumLength()
/*     */   {
/* 156 */     return 12;
/*     */   }
/*     */ 
/*     */   protected byte[] getHmac(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 165 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "HMAC");
/* 166 */     Mac localMac = Mac.getInstance("HmacSHA1");
/* 167 */     localMac.init(localSecretKeySpec);
/*     */ 
/* 170 */     byte[] arrayOfByte1 = localMac.doFinal(paramArrayOfByte2);
/*     */ 
/* 173 */     byte[] arrayOfByte2 = new byte[12];
/* 174 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, 12);
/* 175 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 184 */     if (!KeyUsage.isValid(paramInt1)) {
/* 185 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 190 */     byte[] arrayOfByte1 = new byte[5];
/* 191 */     arrayOfByte1[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 192 */     arrayOfByte1[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 193 */     arrayOfByte1[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 194 */     arrayOfByte1[3] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 196 */     arrayOfByte1[4] = -103;
/*     */ 
/* 198 */     byte[] arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte1);
/*     */     try
/*     */     {
/* 210 */       byte[] arrayOfByte3 = getHmac(arrayOfByte2, paramArrayOfByte2);
/*     */       byte[] arrayOfByte4;
/* 214 */       if (arrayOfByte3.length == getChecksumLength())
/* 215 */         return arrayOfByte3;
/* 216 */       if (arrayOfByte3.length > getChecksumLength()) {
/* 217 */         arrayOfByte4 = new byte[getChecksumLength()];
/* 218 */         System.arraycopy(arrayOfByte3, 0, arrayOfByte4, 0, arrayOfByte4.length);
/* 219 */         return arrayOfByte4;
/*     */       }
/* 221 */       throw new GeneralSecurityException("checksum size too short: " + arrayOfByte3.length + "; expecting : " + getChecksumLength());
/*     */     }
/*     */     finally
/*     */     {
/* 225 */       Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 236 */     if (!KeyUsage.isValid(paramInt1)) {
/* 237 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 240 */     byte[] arrayOfByte = encryptCTS(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, paramInt2, paramInt3, true);
/*     */ 
/* 242 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] encryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 252 */     if (!KeyUsage.isValid(paramInt1)) {
/* 253 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 256 */     byte[] arrayOfByte = encryptCTS(paramArrayOfByte1, paramInt1, paramArrayOfByte2, null, paramArrayOfByte3, paramInt2, paramInt3, false);
/*     */ 
/* 258 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 268 */     if (!KeyUsage.isValid(paramInt1)) {
/* 269 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 272 */     byte[] arrayOfByte = decryptCTS(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3, true);
/*     */ 
/* 274 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] decryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 287 */     if (!KeyUsage.isValid(paramInt1)) {
/* 288 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 291 */     byte[] arrayOfByte = decryptCTS(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramArrayOfByte3, paramInt2, paramInt3, false);
/*     */ 
/* 293 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private byte[] encryptCTS(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 304 */     byte[] arrayOfByte1 = null;
/* 305 */     byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/* 318 */       byte[] arrayOfByte3 = new byte[5];
/* 319 */       arrayOfByte3[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 320 */       arrayOfByte3[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 321 */       arrayOfByte3[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 322 */       arrayOfByte3[3] = ((byte)(paramInt1 & 0xFF));
/* 323 */       arrayOfByte3[4] = -86;
/* 324 */       arrayOfByte1 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 326 */       byte[] arrayOfByte4 = null;
/* 327 */       if (paramBoolean) {
/* 328 */         arrayOfByte5 = Confounder.bytes(16);
/* 329 */         arrayOfByte4 = new byte[arrayOfByte5.length + paramInt3];
/* 330 */         System.arraycopy(arrayOfByte5, 0, arrayOfByte4, 0, arrayOfByte5.length);
/*     */ 
/* 332 */         System.arraycopy(paramArrayOfByte4, paramInt2, arrayOfByte4, arrayOfByte5.length, paramInt3);
/*     */       }
/*     */       else {
/* 335 */         arrayOfByte4 = new byte[paramInt3];
/* 336 */         System.arraycopy(paramArrayOfByte4, paramInt2, arrayOfByte4, 0, paramInt3);
/*     */       }
/*     */ 
/* 340 */       byte[] arrayOfByte5 = new byte[arrayOfByte4.length + 12];
/*     */ 
/* 343 */       Cipher localCipher = Cipher.getInstance("AES/CTS/NoPadding");
/* 344 */       SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, "AES");
/* 345 */       IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2, 0, paramArrayOfByte2.length);
/* 346 */       localCipher.init(1, localSecretKeySpec, localIvParameterSpec);
/* 347 */       localCipher.doFinal(arrayOfByte4, 0, arrayOfByte4.length, arrayOfByte5);
/*     */ 
/* 350 */       arrayOfByte3[4] = 85;
/* 351 */       arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 359 */       byte[] arrayOfByte6 = getHmac(arrayOfByte2, arrayOfByte4);
/*     */ 
/* 362 */       System.arraycopy(arrayOfByte6, 0, arrayOfByte5, arrayOfByte4.length, arrayOfByte6.length);
/*     */ 
/* 364 */       return arrayOfByte5;
/*     */     } finally {
/* 366 */       if (arrayOfByte1 != null) {
/* 367 */         Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */       }
/* 369 */       if (arrayOfByte2 != null)
/* 370 */         Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] decryptCTS(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */     throws GeneralSecurityException
/*     */   {
/* 382 */     byte[] arrayOfByte1 = null;
/* 383 */     byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/* 387 */       byte[] arrayOfByte3 = new byte[5];
/* 388 */       arrayOfByte3[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 389 */       arrayOfByte3[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 390 */       arrayOfByte3[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 391 */       arrayOfByte3[3] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 393 */       arrayOfByte3[4] = -86;
/* 394 */       arrayOfByte1 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 410 */       Cipher localCipher = Cipher.getInstance("AES/CTS/NoPadding");
/* 411 */       SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, "AES");
/* 412 */       IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2, 0, paramArrayOfByte2.length);
/* 413 */       localCipher.init(2, localSecretKeySpec, localIvParameterSpec);
/* 414 */       byte[] arrayOfByte4 = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3 - 12);
/*     */ 
/* 422 */       arrayOfByte3[4] = 85;
/* 423 */       arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 431 */       byte[] arrayOfByte5 = getHmac(arrayOfByte2, arrayOfByte4);
/* 432 */       int i = paramInt2 + paramInt3 - 12;
/*     */ 
/* 438 */       int j = 0;
/* 439 */       if (arrayOfByte5.length >= 12) {
/* 440 */         for (int k = 0; k < 12; k++) {
/* 441 */           if (arrayOfByte5[k] != paramArrayOfByte3[(i + k)]) {
/* 442 */             j = 1;
/*     */ 
/* 446 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 450 */       if (j != 0)
/* 451 */         throw new GeneralSecurityException("Checksum failed");
/*     */       byte[] arrayOfByte6;
/* 454 */       if (paramBoolean)
/*     */       {
/* 457 */         arrayOfByte6 = new byte[arrayOfByte4.length - 16];
/* 458 */         System.arraycopy(arrayOfByte4, 16, arrayOfByte6, 0, arrayOfByte6.length);
/*     */ 
/* 460 */         return arrayOfByte6;
/*     */       }
/* 462 */       return arrayOfByte4;
/*     */     }
/*     */     finally {
/* 465 */       if (arrayOfByte1 != null) {
/* 466 */         Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */       }
/* 468 */       if (arrayOfByte2 != null)
/* 469 */         Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static byte[] PBKDF2(char[] paramArrayOfChar, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 480 */     PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar, paramArrayOfByte, paramInt1, paramInt2);
/* 481 */     SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
/*     */ 
/* 483 */     SecretKey localSecretKey = localSecretKeyFactory.generateSecret(localPBEKeySpec);
/* 484 */     byte[] arrayOfByte = localSecretKey.getEncoded();
/*     */ 
/* 486 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static final int readBigEndian(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 490 */     int i = 0;
/* 491 */     int j = (paramInt2 - 1) * 8;
/* 492 */     while (paramInt2 > 0) {
/* 493 */       i += ((paramArrayOfByte[paramInt1] & 0xFF) << j);
/* 494 */       j -= 8;
/* 495 */       paramInt1++;
/* 496 */       paramInt2--;
/*     */     }
/* 498 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.dk.AesDkCrypto
 * JD-Core Version:    0.6.2
 */