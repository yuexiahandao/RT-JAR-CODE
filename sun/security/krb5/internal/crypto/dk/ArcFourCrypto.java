/*     */ package sun.security.krb5.internal.crypto.dk;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import sun.security.krb5.Confounder;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.internal.crypto.KeyUsage;
/*     */ import sun.security.provider.MD4;
/*     */ 
/*     */ public class ArcFourCrypto extends DkCrypto
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private static final int confounderSize = 8;
/*  50 */   private static final byte[] ZERO_IV = { 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */   private static final int hashSize = 16;
/*     */   private final int keyLength;
/*     */ 
/*     */   public ArcFourCrypto(int paramInt)
/*     */   {
/*  55 */     this.keyLength = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getKeySeedLength() {
/*  59 */     return this.keyLength;
/*     */   }
/*     */ 
/*     */   protected byte[] randomToKey(byte[] paramArrayOfByte)
/*     */   {
/*  64 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] stringToKey(char[] paramArrayOfChar) throws GeneralSecurityException
/*     */   {
/*  69 */     return stringToKey(paramArrayOfChar, null);
/*     */   }
/*     */ 
/*     */   private byte[] stringToKey(char[] paramArrayOfChar, byte[] paramArrayOfByte)
/*     */     throws GeneralSecurityException
/*     */   {
/*  79 */     if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
/*  80 */       throw new RuntimeException("Invalid parameter to stringToKey");
/*     */     }
/*     */ 
/*  83 */     byte[] arrayOfByte1 = null;
/*  84 */     byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/*  87 */       arrayOfByte1 = charToUtf16(paramArrayOfChar);
/*     */ 
/*  90 */       MessageDigest localMessageDigest = MD4.getInstance();
/*  91 */       localMessageDigest.update(arrayOfByte1);
/*  92 */       arrayOfByte2 = localMessageDigest.digest();
/*     */     } catch (Exception localException) {
/*  94 */       return null;
/*     */     } finally {
/*  96 */       if (arrayOfByte1 != null) {
/*  97 */         Arrays.fill(arrayOfByte1, (byte)0);
/*     */       }
/*     */     }
/*     */ 
/* 101 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   protected Cipher getCipher(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws GeneralSecurityException
/*     */   {
/* 108 */     if (paramArrayOfByte2 == null) {
/* 109 */       paramArrayOfByte2 = ZERO_IV;
/*     */     }
/* 111 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "ARCFOUR");
/* 112 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 113 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2, 0, paramArrayOfByte2.length);
/* 114 */     localCipher.init(paramInt, localSecretKeySpec, localIvParameterSpec);
/* 115 */     return localCipher;
/*     */   }
/*     */ 
/*     */   public int getChecksumLength() {
/* 119 */     return 16;
/*     */   }
/*     */ 
/*     */   protected byte[] getHmac(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 128 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "HmacMD5");
/* 129 */     Mac localMac = Mac.getInstance("HmacMD5");
/* 130 */     localMac.init(localSecretKeySpec);
/*     */ 
/* 133 */     byte[] arrayOfByte = localMac.doFinal(paramArrayOfByte2);
/* 134 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 148 */     if (!KeyUsage.isValid(paramInt1)) {
/* 149 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 153 */     byte[] arrayOfByte1 = null;
/*     */     try
/*     */     {
/* 156 */       byte[] arrayOfByte2 = "signaturekey".getBytes();
/*     */ 
/* 158 */       localObject1 = new byte[arrayOfByte2.length + 1];
/* 159 */       System.arraycopy(arrayOfByte2, 0, localObject1, 0, arrayOfByte2.length);
/* 160 */       arrayOfByte1 = getHmac(paramArrayOfByte1, (byte[])localObject1);
/*     */     } catch (Exception localException) {
/* 162 */       localObject1 = new GeneralSecurityException("Calculate Checkum Failed!");
/*     */ 
/* 164 */       ((GeneralSecurityException)localObject1).initCause(localException);
/* 165 */       throw ((Throwable)localObject1);
/*     */     }
/*     */ 
/* 169 */     byte[] arrayOfByte3 = getSalt(paramInt1);
/*     */ 
/* 172 */     Object localObject1 = null;
/*     */     try {
/* 174 */       localObject1 = MessageDigest.getInstance("MD5");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 176 */       localObject2 = new GeneralSecurityException("Calculate Checkum Failed!");
/*     */ 
/* 178 */       ((GeneralSecurityException)localObject2).initCause(localNoSuchAlgorithmException);
/* 179 */       throw ((Throwable)localObject2);
/*     */     }
/* 181 */     ((MessageDigest)localObject1).update(arrayOfByte3);
/* 182 */     ((MessageDigest)localObject1).update(paramArrayOfByte2, paramInt2, paramInt3);
/* 183 */     byte[] arrayOfByte4 = ((MessageDigest)localObject1).digest();
/*     */ 
/* 186 */     Object localObject2 = getHmac(arrayOfByte1, arrayOfByte4);
/*     */ 
/* 190 */     if (localObject2.length == getChecksumLength())
/* 191 */       return localObject2;
/* 192 */     if (localObject2.length > getChecksumLength()) {
/* 193 */       byte[] arrayOfByte5 = new byte[getChecksumLength()];
/* 194 */       System.arraycopy(localObject2, 0, arrayOfByte5, 0, arrayOfByte5.length);
/* 195 */       return arrayOfByte5;
/*     */     }
/* 197 */     throw new GeneralSecurityException("checksum size too short: " + localObject2.length + "; expecting : " + getChecksumLength());
/*     */   }
/*     */ 
/*     */   public byte[] encryptSeq(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 209 */     if (!KeyUsage.isValid(paramInt1)) {
/* 210 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 214 */     byte[] arrayOfByte1 = new byte[4];
/* 215 */     byte[] arrayOfByte2 = getHmac(paramArrayOfByte1, arrayOfByte1);
/*     */ 
/* 218 */     arrayOfByte2 = getHmac(arrayOfByte2, paramArrayOfByte2);
/*     */ 
/* 220 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 221 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte2, "ARCFOUR");
/* 222 */     localCipher.init(1, localSecretKeySpec);
/* 223 */     byte[] arrayOfByte3 = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3);
/*     */ 
/* 225 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   public byte[] decryptSeq(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 235 */     if (!KeyUsage.isValid(paramInt1)) {
/* 236 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 241 */     byte[] arrayOfByte1 = new byte[4];
/* 242 */     byte[] arrayOfByte2 = getHmac(paramArrayOfByte1, arrayOfByte1);
/*     */ 
/* 245 */     arrayOfByte2 = getHmac(arrayOfByte2, paramArrayOfByte2);
/*     */ 
/* 247 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 248 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte2, "ARCFOUR");
/* 249 */     localCipher.init(2, localSecretKeySpec);
/* 250 */     byte[] arrayOfByte3 = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3);
/*     */ 
/* 252 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 262 */     if (!KeyUsage.isValid(paramInt1)) {
/* 263 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 272 */     byte[] arrayOfByte1 = Confounder.bytes(8);
/*     */ 
/* 275 */     int i = roundup(arrayOfByte1.length + paramInt3, 1);
/* 276 */     byte[] arrayOfByte2 = new byte[i];
/* 277 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
/* 278 */     System.arraycopy(paramArrayOfByte4, paramInt2, arrayOfByte2, arrayOfByte1.length, paramInt3);
/*     */ 
/* 282 */     byte[] arrayOfByte3 = new byte[paramArrayOfByte1.length];
/* 283 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte3, 0, paramArrayOfByte1.length);
/*     */ 
/* 286 */     byte[] arrayOfByte4 = getSalt(paramInt1);
/*     */ 
/* 289 */     byte[] arrayOfByte5 = getHmac(arrayOfByte3, arrayOfByte4);
/*     */ 
/* 292 */     byte[] arrayOfByte6 = getHmac(arrayOfByte5, arrayOfByte2);
/*     */ 
/* 295 */     byte[] arrayOfByte7 = getHmac(arrayOfByte5, arrayOfByte6);
/*     */ 
/* 297 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 298 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte7, "ARCFOUR");
/* 299 */     localCipher.init(1, localSecretKeySpec);
/* 300 */     byte[] arrayOfByte8 = localCipher.doFinal(arrayOfByte2, 0, arrayOfByte2.length);
/*     */ 
/* 303 */     byte[] arrayOfByte9 = new byte[16 + arrayOfByte8.length];
/* 304 */     System.arraycopy(arrayOfByte6, 0, arrayOfByte9, 0, 16);
/* 305 */     System.arraycopy(arrayOfByte8, 0, arrayOfByte9, 16, arrayOfByte8.length);
/*     */ 
/* 307 */     return arrayOfByte9;
/*     */   }
/*     */ 
/*     */   public byte[] encryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 317 */     if (!KeyUsage.isValid(paramInt1)) {
/* 318 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 328 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length];
/* 329 */     for (int i = 0; i <= 15; i++) {
/* 330 */       arrayOfByte1[i] = ((byte)(paramArrayOfByte1[i] ^ 0xF0));
/*     */     }
/* 332 */     byte[] arrayOfByte2 = new byte[4];
/* 333 */     byte[] arrayOfByte3 = getHmac(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 339 */     arrayOfByte3 = getHmac(arrayOfByte3, paramArrayOfByte2);
/*     */ 
/* 341 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 342 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte3, "ARCFOUR");
/* 343 */     localCipher.init(1, localSecretKeySpec);
/* 344 */     byte[] arrayOfByte4 = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3);
/*     */ 
/* 346 */     return arrayOfByte4;
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 357 */     if (!KeyUsage.isValid(paramInt1)) {
/* 358 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 366 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length];
/* 367 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/*     */ 
/* 370 */     byte[] arrayOfByte2 = getSalt(paramInt1);
/*     */ 
/* 373 */     byte[] arrayOfByte3 = getHmac(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 376 */     byte[] arrayOfByte4 = new byte[16];
/* 377 */     System.arraycopy(paramArrayOfByte3, paramInt2, arrayOfByte4, 0, 16);
/* 378 */     byte[] arrayOfByte5 = getHmac(arrayOfByte3, arrayOfByte4);
/*     */ 
/* 381 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 382 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte5, "ARCFOUR");
/* 383 */     localCipher.init(2, localSecretKeySpec);
/* 384 */     byte[] arrayOfByte6 = localCipher.doFinal(paramArrayOfByte3, paramInt2 + 16, paramInt3 - 16);
/*     */ 
/* 388 */     byte[] arrayOfByte7 = getHmac(arrayOfByte3, arrayOfByte6);
/*     */ 
/* 395 */     int i = 0;
/* 396 */     if (arrayOfByte7.length >= 16) {
/* 397 */       for (int j = 0; j < 16; j++) {
/* 398 */         if (arrayOfByte7[j] != paramArrayOfByte3[j]) {
/* 399 */           i = 1;
/*     */ 
/* 403 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 407 */     if (i != 0) {
/* 408 */       throw new GeneralSecurityException("Checksum failed");
/*     */     }
/*     */ 
/* 413 */     byte[] arrayOfByte8 = new byte[arrayOfByte6.length - 8];
/* 414 */     System.arraycopy(arrayOfByte6, 8, arrayOfByte8, 0, arrayOfByte8.length);
/*     */ 
/* 416 */     return arrayOfByte8;
/*     */   }
/*     */ 
/*     */   public byte[] decryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3, byte[] paramArrayOfByte4)
/*     */     throws GeneralSecurityException
/*     */   {
/* 429 */     if (!KeyUsage.isValid(paramInt1)) {
/* 430 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 439 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length];
/* 440 */     for (int i = 0; i <= 15; i++) {
/* 441 */       arrayOfByte1[i] = ((byte)(paramArrayOfByte1[i] ^ 0xF0));
/*     */     }
/* 443 */     byte[] arrayOfByte2 = new byte[4];
/* 444 */     byte[] arrayOfByte3 = getHmac(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 447 */     byte[] arrayOfByte4 = new byte[4];
/* 448 */     System.arraycopy(paramArrayOfByte4, 0, arrayOfByte4, 0, arrayOfByte4.length);
/*     */ 
/* 451 */     arrayOfByte3 = getHmac(arrayOfByte3, arrayOfByte4);
/*     */ 
/* 453 */     Cipher localCipher = Cipher.getInstance("ARCFOUR");
/* 454 */     SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte3, "ARCFOUR");
/* 455 */     localCipher.init(2, localSecretKeySpec);
/* 456 */     byte[] arrayOfByte5 = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3);
/*     */ 
/* 458 */     return arrayOfByte5;
/*     */   }
/*     */ 
/*     */   private byte[] getSalt(int paramInt)
/*     */   {
/* 463 */     int i = arcfour_translate_usage(paramInt);
/* 464 */     byte[] arrayOfByte = new byte[4];
/* 465 */     arrayOfByte[0] = ((byte)(i & 0xFF));
/* 466 */     arrayOfByte[1] = ((byte)(i >> 8 & 0xFF));
/* 467 */     arrayOfByte[2] = ((byte)(i >> 16 & 0xFF));
/* 468 */     arrayOfByte[3] = ((byte)(i >> 24 & 0xFF));
/* 469 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private int arcfour_translate_usage(int paramInt)
/*     */   {
/* 474 */     switch (paramInt) { case 3:
/* 475 */       return 8;
/*     */     case 9:
/* 476 */       return 8;
/*     */     case 23:
/* 477 */       return 13; }
/* 478 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.dk.ArcFourCrypto
 * JD-Core Version:    0.6.2
 */