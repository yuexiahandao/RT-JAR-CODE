/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.security.DigestException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.MGF1ParameterSpec;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.spec.OAEPParameterSpec;
/*     */ import javax.crypto.spec.PSource;
/*     */ import javax.crypto.spec.PSource.PSpecified;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public final class RSAPadding
/*     */ {
/*     */   public static final int PAD_BLOCKTYPE_1 = 1;
/*     */   public static final int PAD_BLOCKTYPE_2 = 2;
/*     */   public static final int PAD_NONE = 3;
/*     */   public static final int PAD_OAEP_MGF1 = 4;
/*     */   private final int type;
/*     */   private final int paddedSize;
/*     */   private SecureRandom random;
/*     */   private final int maxDataSize;
/*     */   private MessageDigest md;
/*     */   private MessageDigest mgfMd;
/*     */   private byte[] lHash;
/* 208 */   private static final Map<String, byte[]> emptyHashes = Collections.synchronizedMap(new HashMap());
/*     */ 
/*     */   public static RSAPadding getInstance(int paramInt1, int paramInt2)
/*     */     throws InvalidKeyException, InvalidAlgorithmParameterException
/*     */   {
/* 123 */     return new RSAPadding(paramInt1, paramInt2, null, null);
/*     */   }
/*     */ 
/*     */   public static RSAPadding getInstance(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
/*     */     throws InvalidKeyException, InvalidAlgorithmParameterException
/*     */   {
/* 133 */     return new RSAPadding(paramInt1, paramInt2, paramSecureRandom, null);
/*     */   }
/*     */ 
/*     */   public static RSAPadding getInstance(int paramInt1, int paramInt2, SecureRandom paramSecureRandom, OAEPParameterSpec paramOAEPParameterSpec)
/*     */     throws InvalidKeyException, InvalidAlgorithmParameterException
/*     */   {
/* 143 */     return new RSAPadding(paramInt1, paramInt2, paramSecureRandom, paramOAEPParameterSpec);
/*     */   }
/*     */ 
/*     */   private RSAPadding(int paramInt1, int paramInt2, SecureRandom paramSecureRandom, OAEPParameterSpec paramOAEPParameterSpec)
/*     */     throws InvalidKeyException, InvalidAlgorithmParameterException
/*     */   {
/* 150 */     this.type = paramInt1;
/* 151 */     this.paddedSize = paramInt2;
/* 152 */     this.random = paramSecureRandom;
/* 153 */     if (paramInt2 < 64)
/*     */     {
/* 155 */       throw new InvalidKeyException("Padded size must be at least 64");
/*     */     }
/* 157 */     switch (paramInt1) {
/*     */     case 1:
/*     */     case 2:
/* 160 */       this.maxDataSize = (paramInt2 - 11);
/* 161 */       break;
/*     */     case 3:
/* 163 */       this.maxDataSize = paramInt2;
/* 164 */       break;
/*     */     case 4:
/* 166 */       String str1 = "SHA-1";
/* 167 */       String str2 = "SHA-1";
/* 168 */       byte[] arrayOfByte = null;
/*     */       try {
/* 170 */         if (paramOAEPParameterSpec != null) {
/* 171 */           str1 = paramOAEPParameterSpec.getDigestAlgorithm();
/* 172 */           String str3 = paramOAEPParameterSpec.getMGFAlgorithm();
/* 173 */           if (!str3.equalsIgnoreCase("MGF1")) {
/* 174 */             throw new InvalidAlgorithmParameterException("Unsupported MGF algo: " + str3);
/*     */           }
/*     */ 
/* 177 */           str2 = ((MGF1ParameterSpec)paramOAEPParameterSpec.getMGFParameters()).getDigestAlgorithm();
/*     */ 
/* 179 */           PSource localPSource = paramOAEPParameterSpec.getPSource();
/* 180 */           String str4 = localPSource.getAlgorithm();
/* 181 */           if (!str4.equalsIgnoreCase("PSpecified")) {
/* 182 */             throw new InvalidAlgorithmParameterException("Unsupported pSource algo: " + str4);
/*     */           }
/*     */ 
/* 185 */           arrayOfByte = ((PSource.PSpecified)localPSource).getValue();
/*     */         }
/* 187 */         this.md = MessageDigest.getInstance(str1);
/* 188 */         this.mgfMd = MessageDigest.getInstance(str2);
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 190 */         throw new InvalidKeyException("Digest " + str1 + " not available", localNoSuchAlgorithmException);
/*     */       }
/*     */ 
/* 193 */       this.lHash = getInitialHash(this.md, arrayOfByte);
/* 194 */       int i = this.lHash.length;
/* 195 */       this.maxDataSize = (paramInt2 - 2 - 2 * i);
/* 196 */       if (this.maxDataSize <= 0) {
/* 197 */         throw new InvalidKeyException("Key is too short for encryption using OAEPPadding with " + str1 + " and MGF1" + str2);
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 203 */       throw new InvalidKeyException("Invalid padding: " + paramInt1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static byte[] getInitialHash(MessageDigest paramMessageDigest, byte[] paramArrayOfByte)
/*     */   {
/*     */     byte[] arrayOfByte;
/* 221 */     if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
/* 222 */       String str = paramMessageDigest.getAlgorithm();
/* 223 */       arrayOfByte = (byte[])emptyHashes.get(str);
/* 224 */       if (arrayOfByte == null) {
/* 225 */         arrayOfByte = paramMessageDigest.digest();
/* 226 */         emptyHashes.put(str, arrayOfByte);
/*     */       }
/*     */     } else {
/* 229 */       arrayOfByte = paramMessageDigest.digest(paramArrayOfByte);
/*     */     }
/* 231 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getMaxDataSize()
/*     */   {
/* 239 */     return this.maxDataSize;
/*     */   }
/*     */ 
/*     */   public byte[] pad(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws BadPaddingException
/*     */   {
/* 247 */     return pad(RSACore.convert(paramArrayOfByte, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public byte[] pad(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 254 */     if (paramArrayOfByte.length > this.maxDataSize) {
/* 255 */       throw new BadPaddingException("Data must be shorter than " + (this.maxDataSize + 1) + " bytes");
/*     */     }
/*     */ 
/* 258 */     switch (this.type) {
/*     */     case 3:
/* 260 */       return paramArrayOfByte;
/*     */     case 1:
/*     */     case 2:
/* 263 */       return padV15(paramArrayOfByte);
/*     */     case 4:
/* 265 */       return padOAEP(paramArrayOfByte);
/*     */     }
/* 267 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public byte[] unpad(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws BadPaddingException
/*     */   {
/* 276 */     return unpad(RSACore.convert(paramArrayOfByte, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public byte[] unpad(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 283 */     if (paramArrayOfByte.length != this.paddedSize) {
/* 284 */       throw new BadPaddingException("Decryption error");
/*     */     }
/* 286 */     switch (this.type) {
/*     */     case 3:
/* 288 */       return paramArrayOfByte;
/*     */     case 1:
/*     */     case 2:
/* 291 */       return unpadV15(paramArrayOfByte);
/*     */     case 4:
/* 293 */       return unpadOAEP(paramArrayOfByte);
/*     */     }
/* 295 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   private byte[] padV15(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 303 */     byte[] arrayOfByte1 = new byte[this.paddedSize];
/* 304 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, this.paddedSize - paramArrayOfByte.length, paramArrayOfByte.length);
/*     */ 
/* 306 */     int i = this.paddedSize - 3 - paramArrayOfByte.length;
/* 307 */     int j = 0;
/* 308 */     arrayOfByte1[(j++)] = 0;
/* 309 */     arrayOfByte1[(j++)] = ((byte)this.type);
/* 310 */     if (this.type == 1)
/*     */     {
/* 312 */       while (i-- > 0) {
/* 313 */         arrayOfByte1[(j++)] = -1;
/*     */       }
/*     */     }
/*     */ 
/* 317 */     if (this.random == null) {
/* 318 */       this.random = JCAUtil.getSecureRandom();
/*     */     }
/*     */ 
/* 322 */     byte[] arrayOfByte2 = new byte[64];
/* 323 */     int k = -1;
/* 324 */     while (i-- > 0) {
/*     */       int m;
/*     */       do {
/* 327 */         if (k < 0) {
/* 328 */           this.random.nextBytes(arrayOfByte2);
/* 329 */           k = arrayOfByte2.length - 1;
/*     */         }
/* 331 */         m = arrayOfByte2[(k--)] & 0xFF;
/* 332 */       }while (m == 0);
/* 333 */       arrayOfByte1[(j++)] = ((byte)m);
/*     */     }
/*     */ 
/* 336 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   private byte[] unpadV15(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 345 */     int i = 0;
/* 346 */     int j = 0;
/*     */ 
/* 348 */     if (paramArrayOfByte[(i++)] != 0) {
/* 349 */       j = 1;
/*     */     }
/* 351 */     if (paramArrayOfByte[(i++)] != this.type) {
/* 352 */       j = 1;
/*     */     }
/* 354 */     int k = 0;
/* 355 */     while (i < paramArrayOfByte.length) {
/* 356 */       m = paramArrayOfByte[(i++)] & 0xFF;
/* 357 */       if ((m == 0) && (k == 0)) {
/* 358 */         k = i;
/*     */       }
/* 360 */       if ((i == paramArrayOfByte.length) && (k == 0)) {
/* 361 */         j = 1;
/*     */       }
/* 363 */       if ((this.type == 1) && (m != 255) && (k == 0))
/*     */       {
/* 365 */         j = 1;
/*     */       }
/*     */     }
/* 368 */     int m = paramArrayOfByte.length - k;
/* 369 */     if (m > this.maxDataSize) {
/* 370 */       j = 1;
/*     */     }
/*     */ 
/* 374 */     byte[] arrayOfByte1 = new byte[k];
/* 375 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, k);
/*     */ 
/* 377 */     byte[] arrayOfByte2 = new byte[m];
/* 378 */     System.arraycopy(paramArrayOfByte, k, arrayOfByte2, 0, m);
/*     */ 
/* 380 */     BadPaddingException localBadPaddingException = new BadPaddingException("Decryption error");
/*     */ 
/* 382 */     if (j != 0) {
/* 383 */       throw localBadPaddingException;
/*     */     }
/* 385 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private byte[] padOAEP(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 394 */     if (this.random == null) {
/* 395 */       this.random = JCAUtil.getSecureRandom();
/*     */     }
/* 397 */     int i = this.lHash.length;
/*     */ 
/* 401 */     byte[] arrayOfByte1 = new byte[i];
/* 402 */     this.random.nextBytes(arrayOfByte1);
/*     */ 
/* 405 */     byte[] arrayOfByte2 = new byte[this.paddedSize];
/*     */ 
/* 408 */     int j = 1;
/* 409 */     int k = i;
/*     */ 
/* 412 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, j, k);
/*     */ 
/* 416 */     int m = i + 1;
/* 417 */     int n = arrayOfByte2.length - m;
/*     */ 
/* 420 */     int i1 = this.paddedSize - paramArrayOfByte.length;
/*     */ 
/* 427 */     System.arraycopy(this.lHash, 0, arrayOfByte2, m, i);
/* 428 */     arrayOfByte2[(i1 - 1)] = 1;
/* 429 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte2, i1, paramArrayOfByte.length);
/*     */ 
/* 432 */     mgf1(arrayOfByte2, j, k, arrayOfByte2, m, n);
/*     */ 
/* 435 */     mgf1(arrayOfByte2, m, n, arrayOfByte2, j, k);
/*     */ 
/* 437 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private byte[] unpadOAEP(byte[] paramArrayOfByte)
/*     */     throws BadPaddingException
/*     */   {
/* 444 */     byte[] arrayOfByte1 = paramArrayOfByte;
/* 445 */     int i = 0;
/* 446 */     int j = this.lHash.length;
/*     */ 
/* 448 */     if (arrayOfByte1[0] != 0) {
/* 449 */       i = 1;
/*     */     }
/*     */ 
/* 452 */     int k = 1;
/* 453 */     int m = j;
/*     */ 
/* 455 */     int n = j + 1;
/* 456 */     int i1 = arrayOfByte1.length - n;
/*     */ 
/* 458 */     mgf1(arrayOfByte1, n, i1, arrayOfByte1, k, m);
/* 459 */     mgf1(arrayOfByte1, k, m, arrayOfByte1, n, i1);
/*     */ 
/* 462 */     for (int i2 = 0; i2 < j; i2++) {
/* 463 */       if (this.lHash[i2] != arrayOfByte1[(n + i2)]) {
/* 464 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 468 */     i2 = n + j;
/* 469 */     int i3 = -1;
/*     */ 
/* 471 */     for (int i4 = i2; i4 < arrayOfByte1.length; i4++) {
/* 472 */       int i5 = arrayOfByte1[i4];
/* 473 */       if ((i3 == -1) && 
/* 474 */         (i5 != 0))
/*     */       {
/* 476 */         if (i5 == 1)
/* 477 */           i3 = i4;
/*     */         else {
/* 479 */           i = 1;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 485 */     if (i3 == -1) {
/* 486 */       i = 1;
/* 487 */       i3 = arrayOfByte1.length - 1;
/*     */     }
/*     */ 
/* 490 */     i4 = i3 + 1;
/*     */ 
/* 493 */     byte[] arrayOfByte2 = new byte[i4 - i2];
/* 494 */     System.arraycopy(arrayOfByte1, i2, arrayOfByte2, 0, arrayOfByte2.length);
/*     */ 
/* 496 */     byte[] arrayOfByte3 = new byte[arrayOfByte1.length - i4];
/* 497 */     System.arraycopy(arrayOfByte1, i4, arrayOfByte3, 0, arrayOfByte3.length);
/*     */ 
/* 499 */     BadPaddingException localBadPaddingException = new BadPaddingException("Decryption error");
/*     */ 
/* 501 */     if (i != 0) {
/* 502 */       throw localBadPaddingException;
/*     */     }
/* 504 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   private void mgf1(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
/*     */     throws BadPaddingException
/*     */   {
/* 518 */     byte[] arrayOfByte1 = new byte[4];
/* 519 */     byte[] arrayOfByte2 = new byte[this.mgfMd.getDigestLength()];
/* 520 */     while (paramInt4 > 0) {
/* 521 */       this.mgfMd.update(paramArrayOfByte1, paramInt1, paramInt2);
/* 522 */       this.mgfMd.update(arrayOfByte1);
/*     */       try {
/* 524 */         this.mgfMd.digest(arrayOfByte2, 0, arrayOfByte2.length);
/*     */       }
/*     */       catch (DigestException localDigestException) {
/* 527 */         throw new BadPaddingException(localDigestException.toString());
/*     */       }
/* 529 */       for (int i = 0; (i < arrayOfByte2.length) && (paramInt4 > 0); paramInt4--)
/*     */       {
/*     */         int tmp95_92 = (paramInt3++);
/*     */         byte[] tmp95_88 = paramArrayOfByte2; tmp95_88[tmp95_92] = ((byte)(tmp95_88[tmp95_92] ^ arrayOfByte2[(i++)]));
/*     */       }
/* 532 */       if (paramInt4 > 0)
/*     */       {
/* 534 */         for (i = arrayOfByte1.length - 1; ; i--)
/*     */         {
/*     */           int tmp130_128 = i;
/*     */           byte[] tmp130_126 = arrayOfByte1; if (((tmp130_126[tmp130_128] = (byte)(tmp130_126[tmp130_128] + 1)) != 0) || (i <= 0))
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSAPadding
 * JD-Core Version:    0.6.2
 */