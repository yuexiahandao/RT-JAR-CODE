/*      */ package sun.security.jgss.krb5;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.CipherInputStream;
/*      */ import javax.crypto.CipherOutputStream;
/*      */ import javax.crypto.spec.IvParameterSpec;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ import org.ietf.jgss.GSSException;
/*      */ import sun.security.krb5.EncryptionKey;
/*      */ import sun.security.krb5.internal.crypto.Aes128;
/*      */ import sun.security.krb5.internal.crypto.Aes256;
/*      */ import sun.security.krb5.internal.crypto.ArcFourHmac;
/*      */ import sun.security.krb5.internal.crypto.Des3;
/*      */ 
/*      */ class CipherHelper
/*      */ {
/*      */   private static final int KG_USAGE_SEAL = 22;
/*      */   private static final int KG_USAGE_SIGN = 23;
/*      */   private static final int KG_USAGE_SEQ = 24;
/*      */   private static final int DES_CHECKSUM_SIZE = 8;
/*      */   private static final int DES_IV_SIZE = 8;
/*      */   private static final int AES_IV_SIZE = 16;
/*      */   private static final int HMAC_CHECKSUM_SIZE = 8;
/*      */   private static final int KG_USAGE_SIGN_MS = 15;
/*   67 */   private static final boolean DEBUG = Krb5Util.DEBUG;
/*      */ 
/*   73 */   private static final byte[] ZERO_IV = new byte[8];
/*   74 */   private static final byte[] ZERO_IV_AES = new byte[16];
/*      */   private int etype;
/*      */   private int sgnAlg;
/*      */   private int sealAlg;
/*      */   private byte[] keybytes;
/*   82 */   private int proto = 0;
/*      */ 
/*      */   CipherHelper(EncryptionKey paramEncryptionKey) throws GSSException {
/*   85 */     this.etype = paramEncryptionKey.getEType();
/*   86 */     this.keybytes = paramEncryptionKey.getBytes();
/*      */ 
/*   88 */     switch (this.etype) {
/*      */     case 1:
/*      */     case 3:
/*   91 */       this.sgnAlg = 0;
/*   92 */       this.sealAlg = 0;
/*   93 */       break;
/*      */     case 16:
/*   96 */       this.sgnAlg = 1024;
/*   97 */       this.sealAlg = 512;
/*   98 */       break;
/*      */     case 23:
/*  101 */       this.sgnAlg = 4352;
/*  102 */       this.sealAlg = 4096;
/*  103 */       break;
/*      */     case 17:
/*      */     case 18:
/*  107 */       this.sgnAlg = -1;
/*  108 */       this.sealAlg = -1;
/*  109 */       this.proto = 1;
/*  110 */       break;
/*      */     default:
/*  113 */       throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
/*      */     }
/*      */   }
/*      */ 
/*      */   int getSgnAlg()
/*      */   {
/*  119 */     return this.sgnAlg;
/*      */   }
/*      */ 
/*      */   int getSealAlg() {
/*  123 */     return this.sealAlg;
/*      */   }
/*      */ 
/*      */   int getProto() {
/*  127 */     return this.proto;
/*      */   }
/*      */ 
/*      */   int getEType() {
/*  131 */     return this.etype;
/*      */   }
/*      */ 
/*      */   boolean isArcFour() {
/*  135 */     boolean bool = false;
/*  136 */     if (this.etype == 23) {
/*  137 */       bool = true;
/*      */     }
/*  139 */     return bool;
/*      */   }
/*      */ 
/*      */   byte[] calculateChecksum(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws GSSException
/*      */   {
/*  145 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/*      */       try
/*      */       {
/*  152 */         MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/*      */ 
/*  158 */         localMessageDigest.update(paramArrayOfByte1);
/*      */ 
/*  161 */         localMessageDigest.update(paramArrayOfByte3, paramInt2, paramInt3);
/*      */ 
/*  163 */         if (paramArrayOfByte2 != null)
/*      */         {
/*  167 */           localMessageDigest.update(paramArrayOfByte2);
/*      */         }
/*      */ 
/*  171 */         paramArrayOfByte3 = localMessageDigest.digest();
/*  172 */         paramInt2 = 0;
/*  173 */         paramInt3 = paramArrayOfByte3.length;
/*      */ 
/*  176 */         paramArrayOfByte1 = null;
/*  177 */         paramArrayOfByte2 = null;
/*      */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  179 */         GSSException localGSSException1 = new GSSException(11, -1, "Could not get MD5 Message Digest - " + localNoSuchAlgorithmException.getMessage());
/*      */ 
/*  181 */         localGSSException1.initCause(localNoSuchAlgorithmException);
/*  182 */         throw localGSSException1;
/*      */       }
/*      */ 
/*      */     case 512:
/*  187 */       return getDesCbcChecksum(this.keybytes, paramArrayOfByte1, paramArrayOfByte3, paramInt2, paramInt3);
/*      */     case 1024:
/*      */       byte[] arrayOfByte1;
/*      */       int j;
/*      */       int i;
/*  192 */       if ((paramArrayOfByte1 == null) && (paramArrayOfByte2 == null)) {
/*  193 */         arrayOfByte1 = paramArrayOfByte3;
/*  194 */         j = paramInt3;
/*  195 */         i = paramInt2;
/*      */       } else {
/*  197 */         j = (paramArrayOfByte1 != null ? paramArrayOfByte1.length : 0) + paramInt3 + (paramArrayOfByte2 != null ? paramArrayOfByte2.length : 0);
/*      */ 
/*  200 */         arrayOfByte1 = new byte[j];
/*  201 */         int k = 0;
/*  202 */         if (paramArrayOfByte1 != null) {
/*  203 */           System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/*  204 */           k = paramArrayOfByte1.length;
/*      */         }
/*  206 */         System.arraycopy(paramArrayOfByte3, paramInt2, arrayOfByte1, k, paramInt3);
/*  207 */         k += paramInt3;
/*  208 */         if (paramArrayOfByte2 != null) {
/*  209 */           System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, k, paramArrayOfByte2.length);
/*      */         }
/*      */ 
/*  212 */         i = 0;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  230 */         return Des3.calculateChecksum(this.keybytes, 23, arrayOfByte1, i, j);
/*      */       }
/*      */       catch (GeneralSecurityException localGeneralSecurityException1)
/*      */       {
/*  236 */         GSSException localGSSException2 = new GSSException(11, -1, "Could not use HMAC-SHA1-DES3-KD signing algorithm - " + localGeneralSecurityException1.getMessage());
/*      */ 
/*  239 */         localGSSException2.initCause(localGeneralSecurityException1);
/*  240 */         throw localGSSException2;
/*      */       }
/*      */     case 4352:
/*      */       byte[] arrayOfByte3;
/*      */       int n;
/*      */       int m;
/*      */       int i1;
/*  246 */       if ((paramArrayOfByte1 == null) && (paramArrayOfByte2 == null)) {
/*  247 */         arrayOfByte3 = paramArrayOfByte3;
/*  248 */         n = paramInt3;
/*  249 */         m = paramInt2;
/*      */       } else {
/*  251 */         n = (paramArrayOfByte1 != null ? paramArrayOfByte1.length : 0) + paramInt3 + (paramArrayOfByte2 != null ? paramArrayOfByte2.length : 0);
/*      */ 
/*  254 */         arrayOfByte3 = new byte[n];
/*  255 */         i1 = 0;
/*      */ 
/*  257 */         if (paramArrayOfByte1 != null) {
/*  258 */           System.arraycopy(paramArrayOfByte1, 0, arrayOfByte3, 0, paramArrayOfByte1.length);
/*  259 */           i1 = paramArrayOfByte1.length;
/*      */         }
/*  261 */         System.arraycopy(paramArrayOfByte3, paramInt2, arrayOfByte3, i1, paramInt3);
/*  262 */         i1 += paramInt3;
/*  263 */         if (paramArrayOfByte2 != null) {
/*  264 */           System.arraycopy(paramArrayOfByte2, 0, arrayOfByte3, i1, paramArrayOfByte2.length);
/*      */         }
/*      */ 
/*  267 */         m = 0;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  289 */         i1 = 23;
/*  290 */         if (paramInt4 == 257) {
/*  291 */           i1 = 15;
/*      */         }
/*  293 */         localObject = ArcFourHmac.calculateChecksum(this.keybytes, i1, arrayOfByte3, m, n);
/*      */ 
/*  299 */         byte[] arrayOfByte4 = new byte[getChecksumLength()];
/*  300 */         System.arraycopy(localObject, 0, arrayOfByte4, 0, arrayOfByte4.length);
/*      */ 
/*  303 */         return arrayOfByte4;
/*      */       } catch (GeneralSecurityException localGeneralSecurityException2) {
/*  305 */         Object localObject = new GSSException(11, -1, "Could not use HMAC_MD5_ARCFOUR signing algorithm - " + localGeneralSecurityException2.getMessage());
/*      */ 
/*  308 */         ((GSSException)localObject).initCause(localGeneralSecurityException2);
/*  309 */         throw ((Throwable)localObject);
/*      */       }
/*      */     }
/*      */ 
/*  313 */     throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
/*      */   }
/*      */ 
/*      */   byte[] calculateChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*  323 */     int i = (paramArrayOfByte1 != null ? paramArrayOfByte1.length : 0) + paramInt2;
/*      */ 
/*  326 */     byte[] arrayOfByte1 = new byte[i];
/*      */ 
/*  329 */     System.arraycopy(paramArrayOfByte2, paramInt1, arrayOfByte1, 0, paramInt2);
/*      */ 
/*  332 */     if (paramArrayOfByte1 != null)
/*  333 */       System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, paramInt2, paramArrayOfByte1.length);
/*      */     GSSException localGSSException;
/*  338 */     switch (this.etype) {
/*      */     case 17:
/*      */       try {
/*  341 */         return Aes128.calculateChecksum(this.keybytes, paramInt3, arrayOfByte1, 0, i);
/*      */       }
/*      */       catch (GeneralSecurityException localGeneralSecurityException1)
/*      */       {
/*  347 */         localGSSException = new GSSException(11, -1, "Could not use AES128 signing algorithm - " + localGeneralSecurityException1.getMessage());
/*      */ 
/*  350 */         localGSSException.initCause(localGeneralSecurityException1);
/*  351 */         throw localGSSException;
/*      */       }
/*      */     case 18:
/*      */       try
/*      */       {
/*  356 */         return Aes256.calculateChecksum(this.keybytes, paramInt3, arrayOfByte1, 0, i);
/*      */       }
/*      */       catch (GeneralSecurityException localGeneralSecurityException2)
/*      */       {
/*  362 */         localGSSException = new GSSException(11, -1, "Could not use AES256 signing algorithm - " + localGeneralSecurityException2.getMessage());
/*      */ 
/*  365 */         localGSSException.initCause(localGeneralSecurityException2);
/*  366 */         throw localGSSException;
/*      */       }
/*      */     }
/*      */ 
/*  370 */     throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
/*      */   }
/*      */ 
/*      */   byte[] encryptSeq(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
/*      */     throws GSSException
/*      */   {
/*  378 */     switch (this.sgnAlg) {
/*      */     case 0:
/*      */     case 512:
/*      */       try {
/*  382 */         Cipher localCipher = getInitializedDes(true, this.keybytes, paramArrayOfByte1);
/*  383 */         return localCipher.doFinal(paramArrayOfByte2, paramInt1, paramInt2);
/*      */       }
/*      */       catch (GeneralSecurityException localGeneralSecurityException) {
/*  386 */         GSSException localGSSException1 = new GSSException(11, -1, "Could not encrypt sequence number using DES - " + localGeneralSecurityException.getMessage());
/*      */ 
/*  389 */         localGSSException1.initCause(localGeneralSecurityException);
/*  390 */         throw localGSSException1;
/*      */       }
/*      */     case 1024:
/*      */       byte[] arrayOfByte1;
/*  395 */       if (paramArrayOfByte1.length == 8) {
/*  396 */         arrayOfByte1 = paramArrayOfByte1;
/*      */       } else {
/*  398 */         arrayOfByte1 = new byte[8];
/*  399 */         System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, 8);
/*      */       }
/*      */       try {
/*  402 */         return Des3.encryptRaw(this.keybytes, 24, arrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2);
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/*  406 */         GSSException localGSSException2 = new GSSException(11, -1, "Could not encrypt sequence number using DES3-KD - " + localException1.getMessage());
/*      */ 
/*  409 */         localGSSException2.initCause(localException1);
/*  410 */         throw localGSSException2;
/*      */       }
/*      */     case 4352:
/*      */       byte[] arrayOfByte2;
/*  416 */       if (paramArrayOfByte1.length == 8) {
/*  417 */         arrayOfByte2 = paramArrayOfByte1;
/*      */       } else {
/*  419 */         arrayOfByte2 = new byte[8];
/*  420 */         System.arraycopy(paramArrayOfByte1, 0, arrayOfByte2, 0, 8);
/*      */       }
/*      */       try
/*      */       {
/*  424 */         return ArcFourHmac.encryptSeq(this.keybytes, 24, arrayOfByte2, paramArrayOfByte2, paramInt1, paramInt2);
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*  428 */         GSSException localGSSException3 = new GSSException(11, -1, "Could not encrypt sequence number using RC4-HMAC - " + localException2.getMessage());
/*      */ 
/*  431 */         localGSSException3.initCause(localException2);
/*  432 */         throw localGSSException3;
/*      */       }
/*      */     }
/*      */ 
/*  436 */     throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
/*      */   }
/*      */ 
/*      */   byte[] decryptSeq(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
/*      */     throws GSSException
/*      */   {
/*  444 */     switch (this.sgnAlg) {
/*      */     case 0:
/*      */     case 512:
/*      */       try {
/*  448 */         Cipher localCipher = getInitializedDes(false, this.keybytes, paramArrayOfByte1);
/*  449 */         return localCipher.doFinal(paramArrayOfByte2, paramInt1, paramInt2);
/*      */       } catch (GeneralSecurityException localGeneralSecurityException) {
/*  451 */         GSSException localGSSException1 = new GSSException(11, -1, "Could not decrypt sequence number using DES - " + localGeneralSecurityException.getMessage());
/*      */ 
/*  454 */         localGSSException1.initCause(localGeneralSecurityException);
/*  455 */         throw localGSSException1;
/*      */       }
/*      */     case 1024:
/*      */       byte[] arrayOfByte1;
/*  460 */       if (paramArrayOfByte1.length == 8) {
/*  461 */         arrayOfByte1 = paramArrayOfByte1;
/*      */       } else {
/*  463 */         arrayOfByte1 = new byte[8];
/*  464 */         System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, 8);
/*      */       }
/*      */       try
/*      */       {
/*  468 */         return Des3.decryptRaw(this.keybytes, 24, arrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2);
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/*  472 */         GSSException localGSSException2 = new GSSException(11, -1, "Could not decrypt sequence number using DES3-KD - " + localException1.getMessage());
/*      */ 
/*  475 */         localGSSException2.initCause(localException1);
/*  476 */         throw localGSSException2;
/*      */       }
/*      */     case 4352:
/*      */       byte[] arrayOfByte2;
/*  482 */       if (paramArrayOfByte1.length == 8) {
/*  483 */         arrayOfByte2 = paramArrayOfByte1;
/*      */       } else {
/*  485 */         arrayOfByte2 = new byte[8];
/*  486 */         System.arraycopy(paramArrayOfByte1, 0, arrayOfByte2, 0, 8);
/*      */       }
/*      */       try
/*      */       {
/*  490 */         return ArcFourHmac.decryptSeq(this.keybytes, 24, arrayOfByte2, paramArrayOfByte2, paramInt1, paramInt2);
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*  494 */         GSSException localGSSException3 = new GSSException(11, -1, "Could not decrypt sequence number using RC4-HMAC - " + localException2.getMessage());
/*      */ 
/*  497 */         localGSSException3.initCause(localException2);
/*  498 */         throw localGSSException3;
/*      */       }
/*      */     }
/*      */ 
/*  502 */     throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
/*      */   }
/*      */ 
/*      */   int getChecksumLength()
/*      */     throws GSSException
/*      */   {
/*  508 */     switch (this.etype) {
/*      */     case 1:
/*      */     case 3:
/*  511 */       return 8;
/*      */     case 16:
/*  514 */       return Des3.getChecksumLength();
/*      */     case 17:
/*  517 */       return Aes128.getChecksumLength();
/*      */     case 18:
/*  519 */       return Aes256.getChecksumLength();
/*      */     case 23:
/*  523 */       return 8;
/*      */     }
/*      */ 
/*  526 */     throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
/*      */   }
/*      */ 
/*      */   void decryptData(WrapToken paramWrapToken, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*  539 */     switch (this.sealAlg) {
/*      */     case 0:
/*  541 */       desCbcDecrypt(paramWrapToken, getDesEncryptionKey(this.keybytes), paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
/*      */ 
/*  543 */       break;
/*      */     case 512:
/*  546 */       des3KdDecrypt(paramWrapToken, paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
/*  547 */       break;
/*      */     case 4096:
/*  550 */       arcFourDecrypt(paramWrapToken, paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
/*  551 */       break;
/*      */     default:
/*  554 */       throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
/*      */     }
/*      */   }
/*      */ 
/*      */   void decryptData(WrapToken_v2 paramWrapToken_v2, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
/*      */     throws GSSException
/*      */   {
/*  569 */     switch (this.etype) {
/*      */     case 17:
/*  571 */       aes128Decrypt(paramWrapToken_v2, paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3, paramInt4);
/*      */ 
/*  573 */       break;
/*      */     case 18:
/*  575 */       aes256Decrypt(paramWrapToken_v2, paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3, paramInt4);
/*      */ 
/*  577 */       break;
/*      */     default:
/*  579 */       throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
/*      */     }
/*      */   }
/*      */ 
/*      */   void decryptData(WrapToken paramWrapToken, InputStream paramInputStream, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*      */     throws GSSException, IOException
/*      */   {
/*  588 */     switch (this.sealAlg) {
/*      */     case 0:
/*  590 */       desCbcDecrypt(paramWrapToken, getDesEncryptionKey(this.keybytes), paramInputStream, paramInt1, paramArrayOfByte, paramInt2);
/*      */ 
/*  592 */       break;
/*      */     case 512:
/*  597 */       byte[] arrayOfByte1 = new byte[paramInt1];
/*      */       try {
/*  599 */         Krb5Token.readFully(paramInputStream, arrayOfByte1, 0, paramInt1);
/*      */       } catch (IOException localIOException1) {
/*  601 */         GSSException localGSSException1 = new GSSException(10, -1, "Cannot read complete token");
/*      */ 
/*  604 */         localGSSException1.initCause(localIOException1);
/*  605 */         throw localGSSException1;
/*      */       }
/*      */ 
/*  608 */       des3KdDecrypt(paramWrapToken, arrayOfByte1, 0, paramInt1, paramArrayOfByte, paramInt2);
/*  609 */       break;
/*      */     case 4096:
/*  614 */       byte[] arrayOfByte2 = new byte[paramInt1];
/*      */       try {
/*  616 */         Krb5Token.readFully(paramInputStream, arrayOfByte2, 0, paramInt1);
/*      */       } catch (IOException localIOException2) {
/*  618 */         GSSException localGSSException2 = new GSSException(10, -1, "Cannot read complete token");
/*      */ 
/*  621 */         localGSSException2.initCause(localIOException2);
/*  622 */         throw localGSSException2;
/*      */       }
/*      */ 
/*  625 */       arcFourDecrypt(paramWrapToken, arrayOfByte2, 0, paramInt1, paramArrayOfByte, paramInt2);
/*  626 */       break;
/*      */     default:
/*  629 */       throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
/*      */     }
/*      */   }
/*      */ 
/*      */   void decryptData(WrapToken_v2 paramWrapToken_v2, InputStream paramInputStream, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*      */     throws GSSException, IOException
/*      */   {
/*  639 */     byte[] arrayOfByte = new byte[paramInt1];
/*      */     try {
/*  641 */       Krb5Token.readFully(paramInputStream, arrayOfByte, 0, paramInt1);
/*      */     } catch (IOException localIOException) {
/*  643 */       GSSException localGSSException = new GSSException(10, -1, "Cannot read complete token");
/*      */ 
/*  646 */       localGSSException.initCause(localIOException);
/*  647 */       throw localGSSException;
/*      */     }
/*  649 */     switch (this.etype) {
/*      */     case 17:
/*  651 */       aes128Decrypt(paramWrapToken_v2, arrayOfByte, 0, paramInt1, paramArrayOfByte, paramInt2, paramInt3);
/*      */ 
/*  653 */       break;
/*      */     case 18:
/*  655 */       aes256Decrypt(paramWrapToken_v2, arrayOfByte, 0, paramInt1, paramArrayOfByte, paramInt2, paramInt3);
/*      */ 
/*  657 */       break;
/*      */     default:
/*  659 */       throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
/*      */     }
/*      */   }
/*      */ 
/*      */   void encryptData(WrapToken paramWrapToken, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3, OutputStream paramOutputStream)
/*      */     throws GSSException, IOException
/*      */   {
/*  668 */     switch (this.sealAlg)
/*      */     {
/*      */     case 0:
/*  671 */       Cipher localCipher = getInitializedDes(true, getDesEncryptionKey(this.keybytes), ZERO_IV);
/*      */ 
/*  673 */       CipherOutputStream localCipherOutputStream = new CipherOutputStream(paramOutputStream, localCipher);
/*      */ 
/*  675 */       localCipherOutputStream.write(paramArrayOfByte1);
/*      */ 
/*  677 */       localCipherOutputStream.write(paramArrayOfByte2, paramInt1, paramInt2);
/*      */ 
/*  679 */       localCipherOutputStream.write(paramArrayOfByte3);
/*  680 */       break;
/*      */     case 512:
/*  683 */       byte[] arrayOfByte1 = des3KdEncrypt(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*      */ 
/*  687 */       paramOutputStream.write(arrayOfByte1);
/*  688 */       break;
/*      */     case 4096:
/*  691 */       byte[] arrayOfByte2 = arcFourEncrypt(paramWrapToken, paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*      */ 
/*  695 */       paramOutputStream.write(arrayOfByte2);
/*  696 */       break;
/*      */     default:
/*  699 */       throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
/*      */     }
/*      */   }
/*      */ 
/*      */   byte[] encryptData(WrapToken_v2 paramWrapToken_v2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*  717 */     switch (this.etype) {
/*      */     case 17:
/*  719 */       return aes128Encrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramInt1, paramInt2, paramInt3);
/*      */     case 18:
/*  722 */       return aes256Encrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramInt1, paramInt2, paramInt3);
/*      */     }
/*      */ 
/*  725 */     throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
/*      */   }
/*      */ 
/*      */   void encryptData(WrapToken paramWrapToken, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*      */     Object localObject;
/*  734 */     switch (this.sealAlg) {
/*      */     case 0:
/*  736 */       int i = paramInt3;
/*      */ 
/*  738 */       Cipher localCipher = getInitializedDes(true, getDesEncryptionKey(this.keybytes), ZERO_IV);
/*      */       try
/*      */       {
/*  742 */         i += localCipher.update(paramArrayOfByte1, 0, paramArrayOfByte1.length, paramArrayOfByte4, i);
/*      */ 
/*  745 */         i += localCipher.update(paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte4, i);
/*      */ 
/*  748 */         localCipher.update(paramArrayOfByte3, 0, paramArrayOfByte3.length, paramArrayOfByte4, i);
/*      */ 
/*  750 */         localCipher.doFinal();
/*      */       } catch (GeneralSecurityException localGeneralSecurityException) {
/*  752 */         localObject = new GSSException(11, -1, "Could not use DES Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/*  754 */         ((GSSException)localObject).initCause(localGeneralSecurityException);
/*  755 */         throw ((Throwable)localObject);
/*      */       }
/*      */ 
/*      */     case 512:
/*  760 */       byte[] arrayOfByte = des3KdEncrypt(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*      */ 
/*  762 */       System.arraycopy(arrayOfByte, 0, paramArrayOfByte4, paramInt3, arrayOfByte.length);
/*  763 */       break;
/*      */     case 4096:
/*  766 */       localObject = arcFourEncrypt(paramWrapToken, paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*      */ 
/*  768 */       System.arraycopy(localObject, 0, paramArrayOfByte4, paramInt3, localObject.length);
/*  769 */       break;
/*      */     default:
/*  772 */       throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
/*      */     }
/*      */   }
/*      */ 
/*      */   int encryptData(WrapToken_v2 paramWrapToken_v2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, byte[] paramArrayOfByte4, int paramInt3, int paramInt4)
/*      */     throws GSSException
/*      */   {
/*  790 */     byte[] arrayOfByte = null;
/*  791 */     switch (this.etype) {
/*      */     case 17:
/*  793 */       arrayOfByte = aes128Encrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramInt1, paramInt2, paramInt4);
/*      */ 
/*  795 */       break;
/*      */     case 18:
/*  797 */       arrayOfByte = aes256Encrypt(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramInt1, paramInt2, paramInt4);
/*      */ 
/*  799 */       break;
/*      */     default:
/*  801 */       throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
/*      */     }
/*      */ 
/*  804 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte4, paramInt3, arrayOfByte.length);
/*  805 */     return arrayOfByte.length;
/*      */   }
/*      */ 
/*      */   private byte[] getDesCbcChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2)
/*      */     throws GSSException
/*      */   {
/*  829 */     Cipher localCipher = getInitializedDes(true, paramArrayOfByte1, ZERO_IV);
/*      */ 
/*  831 */     int i = localCipher.getBlockSize();
/*      */ 
/*  839 */     byte[] arrayOfByte1 = new byte[i];
/*      */ 
/*  841 */     int j = paramInt2 / i;
/*  842 */     int k = paramInt2 % i;
/*  843 */     if (k == 0)
/*      */     {
/*  845 */       j--;
/*  846 */       System.arraycopy(paramArrayOfByte3, paramInt1 + j * i, arrayOfByte1, 0, i);
/*      */     }
/*      */     else {
/*  849 */       System.arraycopy(paramArrayOfByte3, paramInt1 + j * i, arrayOfByte1, 0, k);
/*      */     }
/*      */ 
/*      */     Object localObject;
/*      */     try
/*      */     {
/*  855 */       byte[] arrayOfByte2 = new byte[Math.max(i, paramArrayOfByte2 == null ? i : paramArrayOfByte2.length)];
/*      */ 
/*  858 */       if (paramArrayOfByte2 != null)
/*      */       {
/*  860 */         localCipher.update(paramArrayOfByte2, 0, paramArrayOfByte2.length, arrayOfByte2, 0);
/*      */       }
/*      */ 
/*  864 */       for (int m = 0; m < j; m++) {
/*  865 */         localCipher.update(paramArrayOfByte3, paramInt1, i, arrayOfByte2, 0);
/*      */ 
/*  867 */         paramInt1 += i;
/*      */       }
/*      */ 
/*  871 */       localObject = new byte[i];
/*  872 */       localCipher.update(arrayOfByte1, 0, i, (byte[])localObject, 0);
/*  873 */       localCipher.doFinal();
/*      */ 
/*  875 */       return localObject;
/*      */     } catch (GeneralSecurityException localGeneralSecurityException) {
/*  877 */       localObject = new GSSException(11, -1, "Could not use DES Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/*  879 */       ((GSSException)localObject).initCause(localGeneralSecurityException);
/*  880 */     }throw ((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   private final Cipher getInitializedDes(boolean paramBoolean, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */     throws GSSException
/*      */   {
/*      */     Object localObject;
/*      */     try
/*      */     {
/*  898 */       IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte2);
/*  899 */       localObject = new SecretKeySpec(paramArrayOfByte1, "DES");
/*      */ 
/*  901 */       Cipher localCipher = Cipher.getInstance("DES/CBC/NoPadding");
/*  902 */       localCipher.init(paramBoolean ? 1 : 2, (Key)localObject, localIvParameterSpec);
/*      */ 
/*  905 */       return localCipher;
/*      */     } catch (GeneralSecurityException localGeneralSecurityException) {
/*  907 */       localObject = new GSSException(11, -1, localGeneralSecurityException.getMessage());
/*      */ 
/*  909 */       ((GSSException)localObject).initCause(localGeneralSecurityException);
/*  910 */     }throw ((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   private void desCbcDecrypt(WrapToken paramWrapToken, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*      */     try
/*      */     {
/*  936 */       int i = 0;
/*      */ 
/*  938 */       localObject = getInitializedDes(false, paramArrayOfByte1, ZERO_IV);
/*      */ 
/*  944 */       i = ((Cipher)localObject).update(paramArrayOfByte2, paramInt1, 8, paramWrapToken.confounder);
/*      */ 
/*  950 */       paramInt1 += 8;
/*  951 */       paramInt2 -= 8;
/*      */ 
/*  960 */       int j = ((Cipher)localObject).getBlockSize();
/*  961 */       int k = paramInt2 / j - 1;
/*      */ 
/*  964 */       for (int m = 0; m < k; m++) {
/*  965 */         i = ((Cipher)localObject).update(paramArrayOfByte2, paramInt1, j, paramArrayOfByte3, paramInt3);
/*      */ 
/*  971 */         paramInt1 += j;
/*  972 */         paramInt3 += j;
/*      */       }
/*      */ 
/*  976 */       byte[] arrayOfByte = new byte[j];
/*  977 */       ((Cipher)localObject).update(paramArrayOfByte2, paramInt1, j, arrayOfByte);
/*      */ 
/*  979 */       ((Cipher)localObject).doFinal();
/*      */ 
/*  986 */       int n = arrayOfByte[(j - 1)];
/*  987 */       if ((n < 1) || (n > 8)) {
/*  988 */         throw new GSSException(10, -1, "Invalid padding on Wrap Token");
/*      */       }
/*  990 */       paramWrapToken.padding = WrapToken.pads[n];
/*  991 */       j -= n;
/*      */ 
/*  994 */       System.arraycopy(arrayOfByte, 0, paramArrayOfByte3, paramInt3, j);
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException)
/*      */     {
/*  998 */       Object localObject = new GSSException(11, -1, "Could not use DES cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1000 */       ((GSSException)localObject).initCause(localGeneralSecurityException);
/* 1001 */       throw ((Throwable)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void desCbcDecrypt(WrapToken paramWrapToken, byte[] paramArrayOfByte1, InputStream paramInputStream, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*      */     throws GSSException, IOException
/*      */   {
/* 1025 */     int i = 0;
/*      */ 
/* 1027 */     Cipher localCipher = getInitializedDes(false, paramArrayOfByte1, ZERO_IV);
/*      */ 
/* 1029 */     WrapTokenInputStream localWrapTokenInputStream = new WrapTokenInputStream(paramInputStream, paramInt1);
/*      */ 
/* 1031 */     CipherInputStream localCipherInputStream = new CipherInputStream(localWrapTokenInputStream, localCipher);
/*      */ 
/* 1037 */     i = localCipherInputStream.read(paramWrapToken.confounder);
/*      */ 
/* 1039 */     paramInt1 -= i;
/*      */ 
/* 1053 */     int j = localCipher.getBlockSize();
/* 1054 */     int k = paramInt1 / j - 1;
/*      */ 
/* 1057 */     for (int m = 0; m < k; m++)
/*      */     {
/* 1059 */       i = localCipherInputStream.read(paramArrayOfByte2, paramInt2, j);
/*      */ 
/* 1066 */       paramInt2 += j;
/*      */     }
/*      */ 
/* 1070 */     byte[] arrayOfByte = new byte[j];
/*      */ 
/* 1072 */     i = localCipherInputStream.read(arrayOfByte);
/*      */     try
/*      */     {
/* 1082 */       localCipher.doFinal();
/*      */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 1084 */       GSSException localGSSException = new GSSException(11, -1, "Could not use DES cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1086 */       localGSSException.initCause(localGeneralSecurityException);
/* 1087 */       throw localGSSException;
/*      */     }
/*      */ 
/* 1095 */     int n = arrayOfByte[(j - 1)];
/* 1096 */     if ((n < 1) || (n > 8)) {
/* 1097 */       throw new GSSException(10, -1, "Invalid padding on Wrap Token");
/*      */     }
/* 1099 */     paramWrapToken.padding = WrapToken.pads[n];
/* 1100 */     j -= n;
/*      */ 
/* 1103 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt2, j);
/*      */   }
/*      */ 
/*      */   private static byte[] getDesEncryptionKey(byte[] paramArrayOfByte)
/*      */     throws GSSException
/*      */   {
/* 1119 */     if (paramArrayOfByte.length > 8) {
/* 1120 */       throw new GSSException(11, -100, "Invalid DES Key!");
/*      */     }
/*      */ 
/* 1123 */     byte[] arrayOfByte = new byte[paramArrayOfByte.length];
/* 1124 */     for (int i = 0; i < paramArrayOfByte.length; i++)
/* 1125 */       arrayOfByte[i] = ((byte)(paramArrayOfByte[i] ^ 0xF0));
/* 1126 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private void des3KdDecrypt(WrapToken paramWrapToken, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/* 1135 */       arrayOfByte = Des3.decryptRaw(this.keybytes, 22, ZERO_IV, paramArrayOfByte1, paramInt1, paramInt2);
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 1138 */       GSSException localGSSException = new GSSException(11, -1, "Could not use DES3-KD Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1140 */       localGSSException.initCause(localGeneralSecurityException);
/* 1141 */       throw localGSSException;
/*      */     }
/*      */ 
/* 1156 */     int i = arrayOfByte[(arrayOfByte.length - 1)];
/* 1157 */     if ((i < 1) || (i > 8)) {
/* 1158 */       throw new GSSException(10, -1, "Invalid padding on Wrap Token");
/*      */     }
/*      */ 
/* 1161 */     paramWrapToken.padding = WrapToken.pads[i];
/* 1162 */     int j = arrayOfByte.length - 8 - i;
/*      */ 
/* 1164 */     System.arraycopy(arrayOfByte, 8, paramArrayOfByte2, paramInt3, j);
/*      */ 
/* 1168 */     System.arraycopy(arrayOfByte, 0, paramWrapToken.confounder, 0, 8);
/*      */   }
/*      */ 
/*      */   private byte[] des3KdEncrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3)
/*      */     throws GSSException
/*      */   {
/* 1177 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length + paramInt2 + paramArrayOfByte3.length];
/* 1178 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/* 1179 */     System.arraycopy(paramArrayOfByte2, paramInt1, arrayOfByte1, paramArrayOfByte1.length, paramInt2);
/* 1180 */     System.arraycopy(paramArrayOfByte3, 0, arrayOfByte1, paramArrayOfByte1.length + paramInt2, paramArrayOfByte3.length);
/*      */     try
/*      */     {
/* 1187 */       return Des3.encryptRaw(this.keybytes, 22, ZERO_IV, arrayOfByte1, 0, arrayOfByte1.length);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1194 */       GSSException localGSSException = new GSSException(11, -1, "Could not use DES3-KD Cipher - " + localException.getMessage());
/*      */ 
/* 1196 */       localGSSException.initCause(localException);
/* 1197 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void arcFourDecrypt(WrapToken paramWrapToken, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/* 1208 */     byte[] arrayOfByte1 = decryptSeq(paramWrapToken.getChecksum(), paramWrapToken.getEncSeqNumber(), 0, 8);
/*      */     byte[] arrayOfByte2;
/*      */     try
/*      */     {
/* 1213 */       arrayOfByte2 = ArcFourHmac.decryptRaw(this.keybytes, 22, ZERO_IV, paramArrayOfByte1, paramInt1, paramInt2, arrayOfByte1);
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 1216 */       GSSException localGSSException = new GSSException(11, -1, "Could not use ArcFour Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1218 */       localGSSException.initCause(localGeneralSecurityException);
/* 1219 */       throw localGSSException;
/*      */     }
/*      */ 
/* 1234 */     int i = arrayOfByte2[(arrayOfByte2.length - 1)];
/* 1235 */     if (i < 1) {
/* 1236 */       throw new GSSException(10, -1, "Invalid padding on Wrap Token");
/*      */     }
/*      */ 
/* 1239 */     paramWrapToken.padding = WrapToken.pads[i];
/* 1240 */     int j = arrayOfByte2.length - 8 - i;
/*      */ 
/* 1242 */     System.arraycopy(arrayOfByte2, 8, paramArrayOfByte2, paramInt3, j);
/*      */ 
/* 1249 */     System.arraycopy(arrayOfByte2, 0, paramWrapToken.confounder, 0, 8);
/*      */   }
/*      */ 
/*      */   private byte[] arcFourEncrypt(WrapToken paramWrapToken, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3)
/*      */     throws GSSException
/*      */   {
/* 1258 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length + paramInt2 + paramArrayOfByte3.length];
/* 1259 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/* 1260 */     System.arraycopy(paramArrayOfByte2, paramInt1, arrayOfByte1, paramArrayOfByte1.length, paramInt2);
/* 1261 */     System.arraycopy(paramArrayOfByte3, 0, arrayOfByte1, paramArrayOfByte1.length + paramInt2, paramArrayOfByte3.length);
/*      */ 
/* 1267 */     byte[] arrayOfByte2 = new byte[4];
/* 1268 */     WrapToken.writeBigEndian(paramWrapToken.getSequenceNumber(), arrayOfByte2);
/*      */     try
/*      */     {
/* 1274 */       return ArcFourHmac.encryptRaw(this.keybytes, 22, arrayOfByte2, arrayOfByte1, 0, arrayOfByte1.length);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1281 */       GSSException localGSSException = new GSSException(11, -1, "Could not use ArcFour Cipher - " + localException.getMessage());
/*      */ 
/* 1283 */       localGSSException.initCause(localException);
/* 1284 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private byte[] aes128Encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/* 1298 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length + paramInt2 + paramArrayOfByte2.length];
/* 1299 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/* 1300 */     System.arraycopy(paramArrayOfByte3, paramInt1, arrayOfByte1, paramArrayOfByte1.length, paramInt2);
/* 1301 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, paramArrayOfByte1.length + paramInt2, paramArrayOfByte2.length);
/*      */     try
/*      */     {
/* 1306 */       return Aes128.encryptRaw(this.keybytes, paramInt3, ZERO_IV_AES, arrayOfByte1, 0, arrayOfByte1.length);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1314 */       GSSException localGSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + localException.getMessage());
/*      */ 
/* 1316 */       localGSSException.initCause(localException);
/* 1317 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void aes128Decrypt(WrapToken_v2 paramWrapToken_v2, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
/*      */     throws GSSException
/*      */   {
/* 1325 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/* 1328 */       arrayOfByte = Aes128.decryptRaw(this.keybytes, paramInt4, ZERO_IV_AES, paramArrayOfByte1, paramInt1, paramInt2);
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 1331 */       GSSException localGSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1333 */       localGSSException.initCause(localGeneralSecurityException);
/* 1334 */       throw localGSSException;
/*      */     }
/*      */ 
/* 1347 */     int i = arrayOfByte.length - 16 - 16;
/*      */ 
/* 1349 */     System.arraycopy(arrayOfByte, 16, paramArrayOfByte2, paramInt3, i);
/*      */   }
/*      */ 
/*      */   private byte[] aes256Encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws GSSException
/*      */   {
/* 1367 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte1.length + paramInt2 + paramArrayOfByte2.length];
/* 1368 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, paramArrayOfByte1.length);
/* 1369 */     System.arraycopy(paramArrayOfByte3, paramInt1, arrayOfByte1, paramArrayOfByte1.length, paramInt2);
/* 1370 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, paramArrayOfByte1.length + paramInt2, paramArrayOfByte2.length);
/*      */     try
/*      */     {
/* 1376 */       return Aes256.encryptRaw(this.keybytes, paramInt3, ZERO_IV_AES, arrayOfByte1, 0, arrayOfByte1.length);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1383 */       GSSException localGSSException = new GSSException(11, -1, "Could not use AES256 Cipher - " + localException.getMessage());
/*      */ 
/* 1385 */       localGSSException.initCause(localException);
/* 1386 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void aes256Decrypt(WrapToken_v2 paramWrapToken_v2, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
/*      */     throws GSSException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/* 1396 */       arrayOfByte = Aes256.decryptRaw(this.keybytes, paramInt4, ZERO_IV_AES, paramArrayOfByte1, paramInt1, paramInt2);
/*      */     }
/*      */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 1399 */       GSSException localGSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + localGeneralSecurityException.getMessage());
/*      */ 
/* 1401 */       localGSSException.initCause(localGeneralSecurityException);
/* 1402 */       throw localGSSException;
/*      */     }
/*      */ 
/* 1415 */     int i = arrayOfByte.length - 16 - 16;
/*      */ 
/* 1417 */     System.arraycopy(arrayOfByte, 16, paramArrayOfByte2, paramInt3, i);
/*      */   }
/*      */ 
/*      */   class WrapTokenInputStream extends InputStream
/*      */   {
/*      */     private InputStream is;
/*      */     private int length;
/*      */     private int remaining;
/*      */     private int temp;
/*      */ 
/*      */     public WrapTokenInputStream(InputStream paramInt, int arg3)
/*      */     {
/* 1442 */       this.is = paramInt;
/*      */       int i;
/* 1443 */       this.length = i;
/* 1444 */       this.remaining = i;
/*      */     }
/*      */ 
/*      */     public final int read() throws IOException {
/* 1448 */       if (this.remaining == 0) {
/* 1449 */         return -1;
/*      */       }
/* 1451 */       this.temp = this.is.read();
/* 1452 */       if (this.temp != -1)
/* 1453 */         this.remaining -= this.temp;
/* 1454 */       return this.temp;
/*      */     }
/*      */ 
/*      */     public final int read(byte[] paramArrayOfByte) throws IOException
/*      */     {
/* 1459 */       if (this.remaining == 0) {
/* 1460 */         return -1;
/*      */       }
/* 1462 */       this.temp = Math.min(this.remaining, paramArrayOfByte.length);
/* 1463 */       this.temp = this.is.read(paramArrayOfByte, 0, this.temp);
/* 1464 */       if (this.temp != -1)
/* 1465 */         this.remaining -= this.temp;
/* 1466 */       return this.temp;
/*      */     }
/*      */ 
/*      */     public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */       throws IOException
/*      */     {
/* 1473 */       if (this.remaining == 0) {
/* 1474 */         return -1;
/*      */       }
/* 1476 */       this.temp = Math.min(this.remaining, paramInt2);
/* 1477 */       this.temp = this.is.read(paramArrayOfByte, paramInt1, this.temp);
/* 1478 */       if (this.temp != -1)
/* 1479 */         this.remaining -= this.temp;
/* 1480 */       return this.temp;
/*      */     }
/*      */ 
/*      */     public final long skip(long paramLong) throws IOException
/*      */     {
/* 1485 */       if (this.remaining == 0) {
/* 1486 */         return 0L;
/*      */       }
/* 1488 */       this.temp = ((int)Math.min(this.remaining, paramLong));
/* 1489 */       this.temp = ((int)this.is.skip(this.temp));
/* 1490 */       this.remaining -= this.temp;
/* 1491 */       return this.temp;
/*      */     }
/*      */ 
/*      */     public final int available() throws IOException
/*      */     {
/* 1496 */       return Math.min(this.remaining, this.is.available());
/*      */     }
/*      */ 
/*      */     public final void close() throws IOException {
/* 1500 */       this.remaining = 0;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.CipherHelper
 * JD-Core Version:    0.6.2
 */