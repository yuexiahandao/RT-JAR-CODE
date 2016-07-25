/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.SignatureException;
/*     */ import java.security.SignatureSpi;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.DSAPrivateKey;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.util.Arrays;
/*     */ import sun.security.jca.JCAUtil;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ abstract class DSA extends SignatureSpi
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private DSAParams params;
/*     */   private BigInteger presetP;
/*     */   private BigInteger presetQ;
/*     */   private BigInteger presetG;
/*     */   private BigInteger presetY;
/*     */   private BigInteger presetX;
/*     */   private int[] Kseed;
/*     */   private byte[] KseedAsByteArray;
/*     */   private int[] previousKseed;
/*     */   private SecureRandom signingRandom;
/*     */   private static final int round1_kt = 1518500249;
/*     */   private static final int round2_kt = 1859775393;
/*     */   private static final int round3_kt = -1894007588;
/*     */   private static final int round4_kt = -899497514;
/*     */ 
/*     */   abstract byte[] getDigest()
/*     */     throws SignatureException;
/*     */ 
/*     */   abstract void resetDigest();
/*     */ 
/*     */   protected void engineInitSign(PrivateKey paramPrivateKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 216 */     if (!(paramPrivateKey instanceof DSAPrivateKey)) {
/* 217 */       throw new InvalidKeyException("not a DSA private key: " + paramPrivateKey);
/*     */     }
/*     */ 
/* 220 */     DSAPrivateKey localDSAPrivateKey = (DSAPrivateKey)paramPrivateKey;
/*     */ 
/* 222 */     this.presetX = localDSAPrivateKey.getX();
/* 223 */     this.presetY = null;
/* 224 */     initialize(localDSAPrivateKey.getParams());
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(PublicKey paramPublicKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 237 */     if (!(paramPublicKey instanceof DSAPublicKey)) {
/* 238 */       throw new InvalidKeyException("not a DSA public key: " + paramPublicKey);
/*     */     }
/*     */ 
/* 241 */     DSAPublicKey localDSAPublicKey = (DSAPublicKey)paramPublicKey;
/*     */ 
/* 243 */     this.presetY = localDSAPublicKey.getY();
/* 244 */     this.presetX = null;
/* 245 */     initialize(localDSAPublicKey.getParams());
/*     */   }
/*     */ 
/*     */   private void initialize(DSAParams paramDSAParams) throws InvalidKeyException {
/* 249 */     resetDigest();
/* 250 */     setParams(paramDSAParams);
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign()
/*     */     throws SignatureException
/*     */   {
/* 268 */     BigInteger localBigInteger1 = generateK(this.presetQ);
/* 269 */     BigInteger localBigInteger2 = generateR(this.presetP, this.presetQ, this.presetG, localBigInteger1);
/* 270 */     BigInteger localBigInteger3 = generateS(this.presetX, this.presetQ, localBigInteger2, localBigInteger1);
/*     */     try
/*     */     {
/* 273 */       DerOutputStream localDerOutputStream = new DerOutputStream(100);
/* 274 */       localDerOutputStream.putInteger(localBigInteger2);
/* 275 */       localDerOutputStream.putInteger(localBigInteger3);
/* 276 */       DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/* 279 */       return localDerValue.toByteArray();
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 282 */     throw new SignatureException("error encoding signature");
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws SignatureException
/*     */   {
/* 300 */     return engineVerify(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SignatureException
/*     */   {
/* 322 */     BigInteger localBigInteger1 = null;
/* 323 */     BigInteger localBigInteger2 = null;
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 326 */       DerInputStream localDerInputStream = new DerInputStream(paramArrayOfByte, paramInt1, paramInt2);
/* 327 */       localObject = localDerInputStream.getSequence(2);
/*     */ 
/* 329 */       localBigInteger1 = localObject[0].getBigInteger();
/* 330 */       localBigInteger2 = localObject[1].getBigInteger();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 333 */       throw new SignatureException("invalid encoding for signature");
/*     */     }
/*     */ 
/* 339 */     if (localBigInteger1.signum() < 0) {
/* 340 */       localBigInteger1 = new BigInteger(1, localBigInteger1.toByteArray());
/*     */     }
/* 342 */     if (localBigInteger2.signum() < 0) {
/* 343 */       localBigInteger2 = new BigInteger(1, localBigInteger2.toByteArray());
/*     */     }
/*     */ 
/* 346 */     if ((localBigInteger1.compareTo(this.presetQ) == -1) && (localBigInteger2.compareTo(this.presetQ) == -1)) {
/* 347 */       BigInteger localBigInteger3 = generateW(this.presetP, this.presetQ, this.presetG, localBigInteger2);
/* 348 */       localObject = generateV(this.presetY, this.presetP, this.presetQ, this.presetG, localBigInteger3, localBigInteger1);
/* 349 */       return ((BigInteger)localObject).equals(localBigInteger1);
/*     */     }
/* 351 */     throw new SignatureException("invalid signature: out of range values");
/*     */   }
/*     */ 
/*     */   private BigInteger generateR(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */   {
/* 357 */     BigInteger localBigInteger = paramBigInteger3.modPow(paramBigInteger4, paramBigInteger1);
/* 358 */     return localBigInteger.remainder(paramBigInteger2);
/*     */   }
/*     */ 
/*     */   private BigInteger generateS(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */     throws SignatureException
/*     */   {
/* 364 */     byte[] arrayOfByte = getDigest();
/* 365 */     BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte);
/* 366 */     BigInteger localBigInteger2 = paramBigInteger4.modInverse(paramBigInteger2);
/*     */ 
/* 368 */     BigInteger localBigInteger3 = paramBigInteger1.multiply(paramBigInteger3);
/* 369 */     localBigInteger3 = localBigInteger1.add(localBigInteger3);
/* 370 */     localBigInteger3 = localBigInteger2.multiply(localBigInteger3);
/* 371 */     return localBigInteger3.remainder(paramBigInteger2);
/*     */   }
/*     */ 
/*     */   private BigInteger generateW(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */   {
/* 376 */     return paramBigInteger4.modInverse(paramBigInteger2);
/*     */   }
/*     */ 
/*     */   private BigInteger generateV(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
/*     */     throws SignatureException
/*     */   {
/* 383 */     byte[] arrayOfByte = getDigest();
/* 384 */     BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte);
/*     */ 
/* 386 */     localBigInteger1 = localBigInteger1.multiply(paramBigInteger5);
/* 387 */     BigInteger localBigInteger2 = localBigInteger1.remainder(paramBigInteger3);
/*     */ 
/* 389 */     BigInteger localBigInteger3 = paramBigInteger6.multiply(paramBigInteger5).remainder(paramBigInteger3);
/*     */ 
/* 391 */     BigInteger localBigInteger4 = paramBigInteger4.modPow(localBigInteger2, paramBigInteger2);
/* 392 */     BigInteger localBigInteger5 = paramBigInteger1.modPow(localBigInteger3, paramBigInteger2);
/* 393 */     BigInteger localBigInteger6 = localBigInteger4.multiply(localBigInteger5);
/* 394 */     BigInteger localBigInteger7 = localBigInteger6.remainder(paramBigInteger2);
/* 395 */     return localBigInteger7.remainder(paramBigInteger3);
/*     */   }
/*     */ 
/*     */   private BigInteger generateK(BigInteger paramBigInteger)
/*     */   {
/* 404 */     BigInteger localBigInteger = null;
/*     */ 
/* 408 */     if ((this.Kseed != null) && (!Arrays.equals(this.Kseed, this.previousKseed))) {
/* 409 */       localBigInteger = generateK(this.Kseed, paramBigInteger);
/* 410 */       if ((localBigInteger.signum() > 0) && (localBigInteger.compareTo(paramBigInteger) < 0)) {
/* 411 */         this.previousKseed = new int[this.Kseed.length];
/* 412 */         System.arraycopy(this.Kseed, 0, this.previousKseed, 0, this.Kseed.length);
/* 413 */         return localBigInteger;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 420 */     SecureRandom localSecureRandom = getSigningRandom();
/*     */     while (true)
/*     */     {
/* 423 */       int[] arrayOfInt = new int[5];
/*     */ 
/* 425 */       for (int i = 0; i < 5; i++)
/* 426 */         arrayOfInt[i] = localSecureRandom.nextInt();
/* 427 */       localBigInteger = generateK(arrayOfInt, paramBigInteger);
/* 428 */       if ((localBigInteger.signum() > 0) && (localBigInteger.compareTo(paramBigInteger) < 0)) {
/* 429 */         this.previousKseed = new int[arrayOfInt.length];
/* 430 */         System.arraycopy(arrayOfInt, 0, this.previousKseed, 0, arrayOfInt.length);
/* 431 */         return localBigInteger;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private SecureRandom getSigningRandom()
/*     */   {
/* 439 */     if (this.signingRandom == null) {
/* 440 */       if (this.appRandom != null)
/* 441 */         this.signingRandom = this.appRandom;
/*     */       else {
/* 443 */         this.signingRandom = JCAUtil.getSecureRandom();
/*     */       }
/*     */     }
/* 446 */     return this.signingRandom;
/*     */   }
/*     */ 
/*     */   private BigInteger generateK(int[] paramArrayOfInt, BigInteger paramBigInteger)
/*     */   {
/* 461 */     int[] arrayOfInt1 = { -271733879, -1732584194, 271733878, -1009589776, 1732584193 };
/*     */ 
/* 464 */     int[] arrayOfInt2 = SHA_7(paramArrayOfInt, arrayOfInt1);
/* 465 */     byte[] arrayOfByte = new byte[arrayOfInt2.length * 4];
/* 466 */     for (int i = 0; i < arrayOfInt2.length; i++) {
/* 467 */       int j = arrayOfInt2[i];
/* 468 */       for (int k = 0; k < 4; k++) {
/* 469 */         arrayOfByte[(i * 4 + k)] = ((byte)(j >>> 24 - k * 8));
/*     */       }
/*     */     }
/* 472 */     BigInteger localBigInteger = new BigInteger(1, arrayOfByte).mod(paramBigInteger);
/* 473 */     return localBigInteger;
/*     */   }
/*     */ 
/*     */   static int[] SHA_7(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 486 */     int[] arrayOfInt1 = new int[80];
/* 487 */     System.arraycopy(paramArrayOfInt1, 0, arrayOfInt1, 0, paramArrayOfInt1.length);
/* 488 */     int i = 0;
/*     */ 
/* 490 */     for (int j = 16; j <= 79; j++) {
/* 491 */       i = arrayOfInt1[(j - 3)] ^ arrayOfInt1[(j - 8)] ^ arrayOfInt1[(j - 14)] ^ arrayOfInt1[(j - 16)];
/* 492 */       arrayOfInt1[j] = (i << 1 | i >>> 31);
/*     */     }
/*     */ 
/* 495 */     j = paramArrayOfInt2[0]; int k = paramArrayOfInt2[1]; int m = paramArrayOfInt2[2]; int n = paramArrayOfInt2[3]; int i1 = paramArrayOfInt2[4];
/* 496 */     for (int i2 = 0; i2 < 20; i2++) {
/* 497 */       i = (j << 5 | j >>> 27) + (k & m | (k ^ 0xFFFFFFFF) & n) + i1 + arrayOfInt1[i2] + 1518500249;
/*     */ 
/* 499 */       i1 = n;
/* 500 */       n = m;
/* 501 */       m = k << 30 | k >>> 2;
/* 502 */       k = j;
/* 503 */       j = i;
/*     */     }
/*     */ 
/* 507 */     for (i2 = 20; i2 < 40; i2++) {
/* 508 */       i = (j << 5 | j >>> 27) + (k ^ m ^ n) + i1 + arrayOfInt1[i2] + 1859775393;
/*     */ 
/* 510 */       i1 = n;
/* 511 */       n = m;
/* 512 */       m = k << 30 | k >>> 2;
/* 513 */       k = j;
/* 514 */       j = i;
/*     */     }
/*     */ 
/* 518 */     for (i2 = 40; i2 < 60; i2++) {
/* 519 */       i = (j << 5 | j >>> 27) + (k & m | k & n | m & n) + i1 + arrayOfInt1[i2] + -1894007588;
/*     */ 
/* 521 */       i1 = n;
/* 522 */       n = m;
/* 523 */       m = k << 30 | k >>> 2;
/* 524 */       k = j;
/* 525 */       j = i;
/*     */     }
/*     */ 
/* 529 */     for (i2 = 60; i2 < 80; i2++) {
/* 530 */       i = (j << 5 | j >>> 27) + (k ^ m ^ n) + i1 + arrayOfInt1[i2] + -899497514;
/*     */ 
/* 532 */       i1 = n;
/* 533 */       n = m;
/* 534 */       m = k << 30 | k >>> 2;
/* 535 */       k = j;
/* 536 */       j = i;
/*     */     }
/* 538 */     int[] arrayOfInt2 = new int[5];
/* 539 */     paramArrayOfInt2[0] += j;
/* 540 */     paramArrayOfInt2[1] += k;
/* 541 */     paramArrayOfInt2[2] += m;
/* 542 */     paramArrayOfInt2[3] += n;
/* 543 */     paramArrayOfInt2[4] += i1;
/* 544 */     return arrayOfInt2;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void engineSetParameter(String paramString, Object paramObject)
/*     */   {
/* 561 */     if (paramString.equals("KSEED")) {
/* 562 */       if ((paramObject instanceof byte[])) {
/* 563 */         this.Kseed = byteArray2IntArray((byte[])paramObject);
/* 564 */         this.KseedAsByteArray = ((byte[])paramObject);
/*     */       } else {
/* 566 */         debug("unrecognized param: " + paramString);
/* 567 */         throw new InvalidParameterException("Kseed not a byte array");
/*     */       }
/*     */     }
/* 570 */     else throw new InvalidParameterException("invalid parameter");
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected Object engineGetParameter(String paramString)
/*     */   {
/* 594 */     if (paramString.equals("KSEED")) {
/* 595 */       return this.KseedAsByteArray;
/*     */     }
/* 597 */     return null;
/*     */   }
/*     */ 
/*     */   private void setParams(DSAParams paramDSAParams)
/*     */     throws InvalidKeyException
/*     */   {
/* 605 */     if (paramDSAParams == null) {
/* 606 */       throw new InvalidKeyException("DSA public key lacks parameters");
/*     */     }
/* 608 */     this.params = paramDSAParams;
/* 609 */     this.presetP = paramDSAParams.getP();
/* 610 */     this.presetQ = paramDSAParams.getQ();
/* 611 */     this.presetG = paramDSAParams.getG();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 618 */     String str = "DSA Signature";
/* 619 */     if ((this.presetP != null) && (this.presetQ != null) && (this.presetG != null)) {
/* 620 */       str = str + "\n\tp: " + Debug.toHexString(this.presetP);
/* 621 */       str = str + "\n\tq: " + Debug.toHexString(this.presetQ);
/* 622 */       str = str + "\n\tg: " + Debug.toHexString(this.presetG);
/*     */     } else {
/* 624 */       str = str + "\n\t P, Q or G not initialized.";
/*     */     }
/* 626 */     if (this.presetY != null) {
/* 627 */       str = str + "\n\ty: " + Debug.toHexString(this.presetY);
/*     */     }
/* 629 */     if ((this.presetY == null) && (this.presetX == null)) {
/* 630 */       str = str + "\n\tUNINIIALIZED";
/*     */     }
/* 632 */     return str;
/*     */   }
/*     */ 
/*     */   private int[] byteArray2IntArray(byte[] paramArrayOfByte)
/*     */   {
/* 640 */     int i = 0;
/*     */ 
/* 642 */     int j = paramArrayOfByte.length % 4;
/*     */     byte[] arrayOfByte;
/* 646 */     switch (j) { case 3:
/* 647 */       arrayOfByte = new byte[paramArrayOfByte.length + 1]; break;
/*     */     case 2:
/* 648 */       arrayOfByte = new byte[paramArrayOfByte.length + 2]; break;
/*     */     case 1:
/* 649 */       arrayOfByte = new byte[paramArrayOfByte.length + 3]; break;
/*     */     default:
/* 650 */       arrayOfByte = new byte[paramArrayOfByte.length + 0];
/*     */     }
/* 652 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 655 */     int[] arrayOfInt = new int[arrayOfByte.length / 4];
/* 656 */     for (int k = 0; k < arrayOfByte.length; k += 4) {
/* 657 */       arrayOfInt[i] = (arrayOfByte[(k + 3)] & 0xFF);
/* 658 */       arrayOfInt[i] |= arrayOfByte[(k + 2)] << 8 & 0xFF00;
/* 659 */       arrayOfInt[i] |= arrayOfByte[(k + 1)] << 16 & 0xFF0000;
/* 660 */       arrayOfInt[i] |= arrayOfByte[(k + 0)] << 24 & 0xFF000000;
/* 661 */       i++;
/*     */     }
/*     */ 
/* 664 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static void debug(Exception paramException)
/*     */   {
/*     */   }
/*     */ 
/*     */   private static void debug(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static final class RawDSA extends DSA
/*     */   {
/*     */     private static final int SHA1_LEN = 20;
/*     */     private final byte[] digestBuffer;
/*     */     private int ofs;
/*     */ 
/*     */     public RawDSA()
/*     */     {
/* 172 */       this.digestBuffer = new byte[20];
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte paramByte) {
/* 176 */       if (this.ofs == 20) {
/* 177 */         this.ofs = 21;
/* 178 */         return;
/*     */       }
/* 180 */       this.digestBuffer[(this.ofs++)] = paramByte;
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 184 */       if (this.ofs + paramInt2 > 20) {
/* 185 */         this.ofs = 21;
/* 186 */         return;
/*     */       }
/* 188 */       System.arraycopy(paramArrayOfByte, paramInt1, this.digestBuffer, this.ofs, paramInt2);
/* 189 */       this.ofs += paramInt2;
/*     */     }
/*     */ 
/*     */     byte[] getDigest() throws SignatureException {
/* 193 */       if (this.ofs != 20) {
/* 194 */         throw new SignatureException("Data for RawDSA must be exactly 20 bytes long");
/*     */       }
/*     */ 
/* 197 */       this.ofs = 0;
/* 198 */       return this.digestBuffer;
/*     */     }
/*     */ 
/*     */     void resetDigest() {
/* 202 */       this.ofs = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA1withDSA extends DSA
/*     */   {
/*     */     private final MessageDigest dataSHA;
/*     */ 
/*     */     public SHA1withDSA()
/*     */       throws NoSuchAlgorithmException
/*     */     {
/* 123 */       this.dataSHA = MessageDigest.getInstance("SHA-1");
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte paramByte)
/*     */     {
/* 130 */       this.dataSHA.update(paramByte);
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/* 137 */       this.dataSHA.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(ByteBuffer paramByteBuffer) {
/* 141 */       this.dataSHA.update(paramByteBuffer);
/*     */     }
/*     */ 
/*     */     byte[] getDigest() {
/* 145 */       return this.dataSHA.digest();
/*     */     }
/*     */ 
/*     */     void resetDigest() {
/* 149 */       this.dataSHA.reset();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DSA
 * JD-Core Version:    0.6.2
 */