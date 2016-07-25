/*     */ package sun.security.krb5.internal.crypto.dk;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.Confounder;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.internal.crypto.KeyUsage;
/*     */ 
/*     */ public abstract class DkCrypto
/*     */ {
/*     */   protected static final boolean debug = false;
/*  61 */   static final byte[] KERBEROS_CONSTANT = { 107, 101, 114, 98, 101, 114, 111, 115 };
/*     */ 
/*     */   protected abstract int getKeySeedLength();
/*     */ 
/*     */   protected abstract byte[] randomToKey(byte[] paramArrayOfByte);
/*     */ 
/*     */   protected abstract Cipher getCipher(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws GeneralSecurityException;
/*     */ 
/*     */   public abstract int getChecksumLength();
/*     */ 
/*     */   protected abstract byte[] getHmac(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException;
/*     */ 
/*     */   public byte[] encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/*  98 */     if (!KeyUsage.isValid(paramInt1)) {
/*  99 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 103 */     byte[] arrayOfByte1 = null;
/* 104 */     byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/* 109 */       byte[] arrayOfByte3 = new byte[5];
/* 110 */       arrayOfByte3[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 111 */       arrayOfByte3[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 112 */       arrayOfByte3[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 113 */       arrayOfByte3[3] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 115 */       arrayOfByte3[4] = -86;
/*     */ 
/* 117 */       arrayOfByte1 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 131 */       Cipher localCipher = getCipher(arrayOfByte1, paramArrayOfByte2, 1);
/* 132 */       int i = localCipher.getBlockSize();
/* 133 */       byte[] arrayOfByte4 = Confounder.bytes(i);
/*     */ 
/* 135 */       int j = roundup(arrayOfByte4.length + paramInt3, i);
/*     */ 
/* 144 */       byte[] arrayOfByte5 = new byte[j];
/* 145 */       System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 0, arrayOfByte4.length);
/*     */ 
/* 147 */       System.arraycopy(paramArrayOfByte4, paramInt2, arrayOfByte5, arrayOfByte4.length, paramInt3);
/*     */ 
/* 151 */       Arrays.fill(arrayOfByte5, arrayOfByte4.length + paramInt3, j, (byte)0);
/*     */ 
/* 154 */       int k = localCipher.getOutputSize(j);
/* 155 */       int m = k + getChecksumLength();
/*     */ 
/* 157 */       byte[] arrayOfByte6 = new byte[m];
/*     */ 
/* 159 */       localCipher.doFinal(arrayOfByte5, 0, j, arrayOfByte6, 0);
/*     */ 
/* 164 */       if ((paramArrayOfByte3 != null) && (paramArrayOfByte3.length == i)) {
/* 165 */         System.arraycopy(arrayOfByte6, k - i, paramArrayOfByte3, 0, i);
/*     */       }
/*     */ 
/* 173 */       arrayOfByte3[4] = 85;
/* 174 */       arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 182 */       byte[] arrayOfByte7 = getHmac(arrayOfByte2, arrayOfByte5);
/*     */ 
/* 191 */       System.arraycopy(arrayOfByte7, 0, arrayOfByte6, k, getChecksumLength());
/*     */ 
/* 193 */       return arrayOfByte6;
/*     */     } finally {
/* 195 */       if (arrayOfByte1 != null) {
/* 196 */         Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */       }
/* 198 */       if (arrayOfByte2 != null)
/* 199 */         Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] encryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException, KrbCryptoException
/*     */   {
/* 224 */     Cipher localCipher = getCipher(paramArrayOfByte1, paramArrayOfByte2, 1);
/* 225 */     int i = localCipher.getBlockSize();
/*     */ 
/* 227 */     if (paramInt3 % i != 0) {
/* 228 */       throw new GeneralSecurityException("length of data to be encrypted (" + paramInt3 + ") is not a multiple of the blocksize (" + i + ")");
/*     */     }
/*     */ 
/* 233 */     int j = localCipher.getOutputSize(paramInt3);
/* 234 */     byte[] arrayOfByte = new byte[j];
/*     */ 
/* 236 */     localCipher.doFinal(paramArrayOfByte3, 0, paramInt3, arrayOfByte, 0);
/* 237 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] decryptRaw(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 259 */     Cipher localCipher = getCipher(paramArrayOfByte1, paramArrayOfByte2, 2);
/*     */ 
/* 261 */     int i = localCipher.getBlockSize();
/*     */ 
/* 263 */     if (paramInt3 % i != 0) {
/* 264 */       throw new GeneralSecurityException("length of data to be decrypted (" + paramInt3 + ") is not a multiple of the blocksize (" + i + ")");
/*     */     }
/*     */ 
/* 269 */     byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte3, paramInt2, paramInt3);
/*     */ 
/* 276 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 286 */     if (!KeyUsage.isValid(paramInt1)) {
/* 287 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 291 */     byte[] arrayOfByte1 = null;
/* 292 */     byte[] arrayOfByte2 = null;
/*     */     try
/*     */     {
/* 296 */       byte[] arrayOfByte3 = new byte[5];
/* 297 */       arrayOfByte3[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 298 */       arrayOfByte3[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 299 */       arrayOfByte3[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 300 */       arrayOfByte3[3] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 302 */       arrayOfByte3[4] = -86;
/*     */ 
/* 304 */       arrayOfByte1 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 317 */       Cipher localCipher = getCipher(arrayOfByte1, paramArrayOfByte2, 2);
/* 318 */       int i = localCipher.getBlockSize();
/*     */ 
/* 321 */       int j = getChecksumLength();
/* 322 */       int k = paramInt3 - j;
/* 323 */       byte[] arrayOfByte4 = localCipher.doFinal(paramArrayOfByte3, paramInt2, k);
/*     */ 
/* 333 */       arrayOfByte3[4] = 85;
/* 334 */       arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte3);
/*     */ 
/* 342 */       byte[] arrayOfByte5 = getHmac(arrayOfByte2, arrayOfByte4);
/*     */ 
/* 351 */       int m = 0;
/* 352 */       if (arrayOfByte5.length >= j) {
/* 353 */         for (int n = 0; n < j; n++) {
/* 354 */           if (arrayOfByte5[n] != paramArrayOfByte3[(k + n)]) {
/* 355 */             m = 1;
/* 356 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 361 */       if (m != 0) {
/* 362 */         throw new GeneralSecurityException("Checksum failed");
/*     */       }
/*     */ 
/* 367 */       if ((paramArrayOfByte2 != null) && (paramArrayOfByte2.length == i)) {
/* 368 */         System.arraycopy(paramArrayOfByte3, paramInt2 + k - i, paramArrayOfByte2, 0, i);
/*     */       }
/*     */ 
/* 377 */       byte[] arrayOfByte6 = new byte[arrayOfByte4.length - i];
/* 378 */       System.arraycopy(arrayOfByte4, i, arrayOfByte6, 0, arrayOfByte6.length);
/*     */ 
/* 380 */       return arrayOfByte6;
/*     */     } finally {
/* 382 */       if (arrayOfByte1 != null) {
/* 383 */         Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */       }
/* 385 */       if (arrayOfByte2 != null)
/* 386 */         Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   int roundup(int paramInt1, int paramInt2)
/*     */   {
/* 393 */     return (paramInt1 + paramInt2 - 1) / paramInt2 * paramInt2;
/*     */   }
/*     */ 
/*     */   public byte[] calculateChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */     throws GeneralSecurityException
/*     */   {
/* 399 */     if (!KeyUsage.isValid(paramInt1)) {
/* 400 */       throw new GeneralSecurityException("Invalid key usage number: " + paramInt1);
/*     */     }
/*     */ 
/* 405 */     byte[] arrayOfByte1 = new byte[5];
/* 406 */     arrayOfByte1[0] = ((byte)(paramInt1 >> 24 & 0xFF));
/* 407 */     arrayOfByte1[1] = ((byte)(paramInt1 >> 16 & 0xFF));
/* 408 */     arrayOfByte1[2] = ((byte)(paramInt1 >> 8 & 0xFF));
/* 409 */     arrayOfByte1[3] = ((byte)(paramInt1 & 0xFF));
/*     */ 
/* 411 */     arrayOfByte1[4] = -103;
/*     */ 
/* 413 */     byte[] arrayOfByte2 = dk(paramArrayOfByte1, arrayOfByte1);
/*     */     try
/*     */     {
/* 425 */       byte[] arrayOfByte3 = getHmac(arrayOfByte2, paramArrayOfByte2);
/*     */       byte[] arrayOfByte4;
/* 429 */       if (arrayOfByte3.length == getChecksumLength())
/* 430 */         return arrayOfByte3;
/* 431 */       if (arrayOfByte3.length > getChecksumLength()) {
/* 432 */         arrayOfByte4 = new byte[getChecksumLength()];
/* 433 */         System.arraycopy(arrayOfByte3, 0, arrayOfByte4, 0, arrayOfByte4.length);
/* 434 */         return arrayOfByte4;
/*     */       }
/* 436 */       throw new GeneralSecurityException("checksum size too short: " + arrayOfByte3.length + "; expecting : " + getChecksumLength());
/*     */     }
/*     */     finally
/*     */     {
/* 440 */       Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   byte[] dk(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 447 */     return randomToKey(dr(paramArrayOfByte1, paramArrayOfByte2));
/*     */   }
/*     */ 
/*     */   private byte[] dr(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws GeneralSecurityException
/*     */   {
/* 484 */     Cipher localCipher = getCipher(paramArrayOfByte1, null, 1);
/* 485 */     int i = localCipher.getBlockSize();
/*     */ 
/* 487 */     if (paramArrayOfByte2.length != i) {
/* 488 */       paramArrayOfByte2 = nfold(paramArrayOfByte2, i * 8);
/*     */     }
/* 490 */     Object localObject = paramArrayOfByte2;
/*     */ 
/* 492 */     int j = getKeySeedLength() >> 3;
/* 493 */     byte[] arrayOfByte1 = new byte[j];
/* 494 */     int k = 0;
/*     */ 
/* 497 */     int m = 0;
/* 498 */     while (m < j)
/*     */     {
/* 504 */       byte[] arrayOfByte2 = localCipher.doFinal((byte[])localObject);
/*     */ 
/* 510 */       int n = j - m <= arrayOfByte2.length ? j - m : arrayOfByte2.length;
/*     */ 
/* 515 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte1, m, n);
/* 516 */       m += n;
/* 517 */       localObject = arrayOfByte2;
/*     */     }
/* 519 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   static byte[] nfold(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 544 */     int i = paramArrayOfByte.length;
/* 545 */     paramInt >>= 3;
/*     */ 
/* 549 */     int j = paramInt;
/* 550 */     int k = i;
/*     */ 
/* 552 */     while (k != 0) {
/* 553 */       int m = k;
/* 554 */       k = j % k;
/* 555 */       j = m;
/*     */     }
/* 557 */     int n = paramInt * i / j;
/*     */ 
/* 566 */     byte[] arrayOfByte = new byte[paramInt];
/* 567 */     Arrays.fill(arrayOfByte, (byte)0);
/*     */ 
/* 569 */     int i1 = 0;
/*     */ 
/* 574 */     for (int i3 = n - 1; i3 >= 0; i3--)
/*     */     {
/* 576 */       int i2 = ((i << 3) - 1 + ((i << 3) + 13) * (i3 / i) + (i - i3 % i << 3)) % (i << 3);
/*     */ 
/* 586 */       int i4 = ((paramArrayOfByte[((i - 1 - (i2 >>> 3)) % i)] & 0xFF) << 8 | paramArrayOfByte[((i - (i2 >>> 3)) % i)] & 0xFF) >>> (i2 & 0x7) + 1 & 0xFF;
/*     */ 
/* 597 */       i1 += i4;
/*     */ 
/* 601 */       int i5 = arrayOfByte[(i3 % paramInt)] & 0xFF;
/* 602 */       i1 += i5;
/* 603 */       arrayOfByte[(i3 % paramInt)] = ((byte)(i1 & 0xFF));
/*     */ 
/* 614 */       i1 >>>= 8;
/*     */     }
/*     */ 
/* 622 */     if (i1 != 0) {
/* 623 */       for (i3 = paramInt - 1; i3 >= 0; i3--)
/*     */       {
/* 625 */         i1 += (arrayOfByte[i3] & 0xFF);
/* 626 */         arrayOfByte[i3] = ((byte)(i1 & 0xFF));
/*     */ 
/* 629 */         i1 >>>= 8;
/*     */       }
/*     */     }
/*     */ 
/* 633 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static String bytesToString(byte[] paramArrayOfByte)
/*     */   {
/* 639 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 641 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 642 */       if ((paramArrayOfByte[i] & 0xFF) < 16) {
/* 643 */         localStringBuffer.append("0" + Integer.toHexString(paramArrayOfByte[i] & 0xFF));
/*     */       }
/*     */       else {
/* 646 */         localStringBuffer.append(Integer.toHexString(paramArrayOfByte[i] & 0xFF));
/*     */       }
/*     */     }
/*     */ 
/* 650 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static byte[] binaryStringToBytes(String paramString) {
/* 654 */     char[] arrayOfChar = paramString.toCharArray();
/* 655 */     byte[] arrayOfByte = new byte[arrayOfChar.length / 2];
/* 656 */     for (int i = 0; i < arrayOfByte.length; i++) {
/* 657 */       int j = Byte.parseByte(new String(arrayOfChar, i * 2, 1), 16);
/* 658 */       int k = Byte.parseByte(new String(arrayOfChar, i * 2 + 1, 1), 16);
/* 659 */       arrayOfByte[i] = ((byte)(j << 4 | k));
/*     */     }
/* 661 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static void traceOutput(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*     */     try {
/* 667 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(paramInt2);
/* 668 */       new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), localByteArrayOutputStream);
/*     */ 
/* 671 */       System.err.println(paramString + ":" + localByteArrayOutputStream.toString());
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static byte[] charToUtf8(char[] paramArrayOfChar) {
/* 679 */     Charset localCharset = Charset.forName("UTF-8");
/*     */ 
/* 681 */     CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar);
/* 682 */     ByteBuffer localByteBuffer = localCharset.encode(localCharBuffer);
/* 683 */     int i = localByteBuffer.limit();
/* 684 */     byte[] arrayOfByte = new byte[i];
/* 685 */     localByteBuffer.get(arrayOfByte, 0, i);
/* 686 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static byte[] charToUtf16(char[] paramArrayOfChar) {
/* 690 */     Charset localCharset = Charset.forName("UTF-16LE");
/*     */ 
/* 692 */     CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar);
/* 693 */     ByteBuffer localByteBuffer = localCharset.encode(localCharBuffer);
/* 694 */     int i = localByteBuffer.limit();
/* 695 */     byte[] arrayOfByte = new byte[i];
/* 696 */     localByteBuffer.get(arrayOfByte, 0, i);
/* 697 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.dk.DkCrypto
 * JD-Core Version:    0.6.2
 */