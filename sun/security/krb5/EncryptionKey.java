/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.spec.DESKeySpec;
/*     */ import javax.crypto.spec.DESedeKeySpec;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.PAData.SaltAndParams;
/*     */ import sun.security.krb5.internal.ccache.CCacheOutputStream;
/*     */ import sun.security.krb5.internal.crypto.Aes128;
/*     */ import sun.security.krb5.internal.crypto.Aes256;
/*     */ import sun.security.krb5.internal.crypto.ArcFourHmac;
/*     */ import sun.security.krb5.internal.crypto.Des;
/*     */ import sun.security.krb5.internal.crypto.Des3;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.krb5.internal.ktab.KeyTab;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncryptionKey
/*     */   implements Cloneable
/*     */ {
/*  70 */   public static final EncryptionKey NULL_KEY = new EncryptionKey(new byte[0], 0, null);
/*     */   private int keyType;
/*     */   private byte[] keyValue;
/*     */   private Integer kvno;
/*  77 */   private static final boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public synchronized int getEType() {
/*  80 */     return this.keyType;
/*     */   }
/*     */ 
/*     */   public final Integer getKeyVersionNumber() {
/*  84 */     return this.kvno;
/*     */   }
/*     */ 
/*     */   public final byte[] getBytes()
/*     */   {
/*  93 */     return this.keyValue;
/*     */   }
/*     */ 
/*     */   public synchronized Object clone() {
/*  97 */     return new EncryptionKey(this.keyValue, this.keyType, this.kvno);
/*     */   }
/*     */ 
/*     */   public static EncryptionKey[] acquireSecretKeys(PrincipalName paramPrincipalName, String paramString)
/*     */   {
/* 143 */     if (paramPrincipalName == null) {
/* 144 */       throw new IllegalArgumentException("Cannot have null pricipal name to look in keytab.");
/*     */     }
/*     */ 
/* 149 */     KeyTab localKeyTab = KeyTab.getInstance(paramString);
/* 150 */     return localKeyTab.readServiceKeys(paramPrincipalName);
/*     */   }
/*     */ 
/*     */   public static EncryptionKey acquireSecretKey(PrincipalName paramPrincipalName, char[] paramArrayOfChar, int paramInt, PAData.SaltAndParams paramSaltAndParams)
/*     */     throws KrbException
/*     */   {
/*     */     String str;
/*     */     byte[] arrayOfByte;
/* 167 */     if (paramSaltAndParams != null) {
/* 168 */       str = paramSaltAndParams.salt != null ? paramSaltAndParams.salt : paramPrincipalName.getSalt();
/* 169 */       arrayOfByte = paramSaltAndParams.params;
/*     */     } else {
/* 171 */       str = paramPrincipalName.getSalt();
/* 172 */       arrayOfByte = null;
/*     */     }
/* 174 */     return acquireSecretKey(paramArrayOfChar, str, paramInt, arrayOfByte);
/*     */   }
/*     */ 
/*     */   public static EncryptionKey acquireSecretKey(char[] paramArrayOfChar, String paramString, int paramInt, byte[] paramArrayOfByte)
/*     */     throws KrbException
/*     */   {
/* 189 */     return new EncryptionKey(stringToKey(paramArrayOfChar, paramString, paramArrayOfByte, paramInt), paramInt, null);
/*     */   }
/*     */ 
/*     */   public static EncryptionKey[] acquireSecretKeys(char[] paramArrayOfChar, String paramString)
/*     */     throws KrbException
/*     */   {
/* 210 */     int[] arrayOfInt = EType.getDefaults("default_tkt_enctypes");
/* 211 */     if (arrayOfInt == null) {
/* 212 */       arrayOfInt = EType.getBuiltInDefaults();
/*     */     }
/*     */ 
/* 215 */     EncryptionKey[] arrayOfEncryptionKey = new EncryptionKey[arrayOfInt.length];
/* 216 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 217 */       if (EType.isSupported(arrayOfInt[i])) {
/* 218 */         arrayOfEncryptionKey[i] = new EncryptionKey(stringToKey(paramArrayOfChar, paramString, null, arrayOfInt[i]), arrayOfInt[i], null);
/*     */       }
/* 222 */       else if (DEBUG) {
/* 223 */         System.out.println("Encryption Type " + EType.toString(arrayOfInt[i]) + " is not supported/enabled");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 229 */     return arrayOfEncryptionKey;
/*     */   }
/*     */ 
/*     */   public EncryptionKey(byte[] paramArrayOfByte, int paramInt, Integer paramInteger)
/*     */   {
/* 237 */     if (paramArrayOfByte != null) {
/* 238 */       this.keyValue = new byte[paramArrayOfByte.length];
/* 239 */       System.arraycopy(paramArrayOfByte, 0, this.keyValue, 0, paramArrayOfByte.length);
/*     */     } else {
/* 241 */       throw new IllegalArgumentException("EncryptionKey: Key bytes cannot be null!");
/*     */     }
/*     */ 
/* 244 */     this.keyType = paramInt;
/* 245 */     this.kvno = paramInteger;
/*     */   }
/*     */ 
/*     */   public EncryptionKey(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/* 258 */     this(paramArrayOfByte, paramInt, null);
/*     */   }
/*     */ 
/*     */   private static byte[] stringToKey(char[] paramArrayOfChar, String paramString, byte[] paramArrayOfByte, int paramInt)
/*     */     throws KrbCryptoException
/*     */   {
/* 264 */     char[] arrayOfChar1 = paramString.toCharArray();
/* 265 */     char[] arrayOfChar2 = new char[paramArrayOfChar.length + arrayOfChar1.length];
/* 266 */     System.arraycopy(paramArrayOfChar, 0, arrayOfChar2, 0, paramArrayOfChar.length);
/* 267 */     System.arraycopy(arrayOfChar1, 0, arrayOfChar2, paramArrayOfChar.length, arrayOfChar1.length);
/* 268 */     Arrays.fill(arrayOfChar1, '0');
/*     */     try
/*     */     {
/*     */       byte[] arrayOfByte;
/* 271 */       switch (paramInt) {
/*     */       case 1:
/*     */       case 3:
/* 274 */         return Des.string_to_key_bytes(arrayOfChar2);
/*     */       case 16:
/* 277 */         return Des3.stringToKey(arrayOfChar2);
/*     */       case 23:
/* 280 */         return ArcFourHmac.stringToKey(paramArrayOfChar);
/*     */       case 17:
/* 283 */         return Aes128.stringToKey(paramArrayOfChar, paramString, paramArrayOfByte);
/*     */       case 18:
/* 286 */         return Aes256.stringToKey(paramArrayOfChar, paramString, paramArrayOfByte);
/*     */       }
/*     */ 
/* 289 */       throw new IllegalArgumentException("encryption type " + EType.toString(paramInt) + " not supported");
/*     */     }
/*     */     catch (GeneralSecurityException localGeneralSecurityException)
/*     */     {
/* 294 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 295 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 296 */       throw localKrbCryptoException;
/*     */     } finally {
/* 298 */       Arrays.fill(arrayOfChar2, '0');
/*     */     }
/*     */   }
/*     */ 
/*     */   public EncryptionKey(char[] paramArrayOfChar, String paramString1, String paramString2)
/*     */     throws KrbCryptoException
/*     */   {
/* 307 */     if ((paramString2 == null) || (paramString2.equalsIgnoreCase("DES"))) {
/* 308 */       this.keyType = 3;
/* 309 */     } else if (paramString2.equalsIgnoreCase("DESede")) {
/* 310 */       this.keyType = 16;
/* 311 */     } else if (paramString2.equalsIgnoreCase("AES128")) {
/* 312 */       this.keyType = 17;
/* 313 */     } else if (paramString2.equalsIgnoreCase("ArcFourHmac")) {
/* 314 */       this.keyType = 23;
/* 315 */     } else if (paramString2.equalsIgnoreCase("AES256")) {
/* 316 */       this.keyType = 18;
/*     */ 
/* 318 */       if (!EType.isSupported(this.keyType))
/* 319 */         throw new IllegalArgumentException("Algorithm " + paramString2 + " not enabled");
/*     */     }
/*     */     else
/*     */     {
/* 323 */       throw new IllegalArgumentException("Algorithm " + paramString2 + " not supported");
/*     */     }
/*     */ 
/* 327 */     this.keyValue = stringToKey(paramArrayOfChar, paramString1, null, this.keyType);
/* 328 */     this.kvno = null;
/*     */   }
/*     */ 
/*     */   EncryptionKey(EncryptionKey paramEncryptionKey)
/*     */     throws KrbCryptoException
/*     */   {
/* 337 */     this.keyValue = Confounder.bytes(paramEncryptionKey.keyValue.length);
/* 338 */     for (int i = 0; i < this.keyValue.length; i++)
/*     */     {
/*     */       int tmp32_31 = i;
/*     */       byte[] tmp32_28 = this.keyValue; tmp32_28[tmp32_31] = ((byte)(tmp32_28[tmp32_31] ^ paramEncryptionKey.keyValue[i]));
/*     */     }
/* 341 */     this.keyType = paramEncryptionKey.keyType;
/*     */     try
/*     */     {
/* 346 */       if ((this.keyType == 3) || (this.keyType == 1))
/*     */       {
/* 349 */         if (!DESKeySpec.isParityAdjusted(this.keyValue, 0)) {
/* 350 */           this.keyValue = Des.set_parity(this.keyValue);
/*     */         }
/*     */ 
/* 353 */         if (DESKeySpec.isWeak(this.keyValue, 0)) {
/* 354 */           this.keyValue[7] = ((byte)(this.keyValue[7] ^ 0xF0));
/*     */         }
/*     */       }
/*     */ 
/* 358 */       if (this.keyType == 16)
/*     */       {
/* 360 */         if (!DESedeKeySpec.isParityAdjusted(this.keyValue, 0)) {
/* 361 */           this.keyValue = Des3.parityFix(this.keyValue);
/*     */         }
/*     */ 
/* 364 */         byte[] arrayOfByte = new byte[8];
/* 365 */         for (int j = 0; j < this.keyValue.length; j += 8) {
/* 366 */           System.arraycopy(this.keyValue, j, arrayOfByte, 0, 8);
/* 367 */           if (DESKeySpec.isWeak(arrayOfByte, 0))
/* 368 */             this.keyValue[(j + 7)] = ((byte)(this.keyValue[(j + 7)] ^ 0xF0));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 373 */       KrbCryptoException localKrbCryptoException = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 374 */       localKrbCryptoException.initCause(localGeneralSecurityException);
/* 375 */       throw localKrbCryptoException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public EncryptionKey(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 392 */     if (paramDerValue.getTag() != 48) {
/* 393 */       throw new Asn1Exception(906);
/*     */     }
/* 395 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/* 396 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/* 397 */       this.keyType = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else
/* 400 */       throw new Asn1Exception(906);
/* 401 */     localDerValue = paramDerValue.getData().getDerValue();
/* 402 */     if ((localDerValue.getTag() & 0x1F) == 1) {
/* 403 */       this.keyValue = localDerValue.getData().getOctetString();
/*     */     }
/*     */     else
/* 406 */       throw new Asn1Exception(906);
/* 407 */     if (localDerValue.getData().available() > 0)
/* 408 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public synchronized byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 435 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 436 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 437 */     localDerOutputStream2.putInteger(this.keyType);
/* 438 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 440 */     localDerOutputStream2 = new DerOutputStream();
/* 441 */     localDerOutputStream2.putOctetString(this.keyValue);
/* 442 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */ 
/* 444 */     localDerOutputStream2 = new DerOutputStream();
/* 445 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 446 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public synchronized void destroy() {
/* 450 */     if (this.keyValue != null)
/* 451 */       for (int i = 0; i < this.keyValue.length; i++)
/* 452 */         this.keyValue[i] = 0;
/*     */   }
/*     */ 
/*     */   public static EncryptionKey parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 475 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 477 */       return null;
/*     */     }
/* 479 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 480 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 481 */       throw new Asn1Exception(906);
/*     */     }
/* 483 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 484 */     return new EncryptionKey(localDerValue2);
/*     */   }
/*     */ 
/*     */   public synchronized void writeKey(CCacheOutputStream paramCCacheOutputStream)
/*     */     throws IOException
/*     */   {
/* 499 */     paramCCacheOutputStream.write16(this.keyType);
/*     */ 
/* 501 */     paramCCacheOutputStream.write16(this.keyType);
/* 502 */     paramCCacheOutputStream.write32(this.keyValue.length);
/* 503 */     for (int i = 0; i < this.keyValue.length; i++)
/* 504 */       paramCCacheOutputStream.write8(this.keyValue[i]);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 509 */     return new String("EncryptionKey: keyType=" + this.keyType + " kvno=" + this.kvno + " keyValue (hex dump)=" + ((this.keyValue == null) || (this.keyValue.length == 0) ? " Empty Key" : new StringBuilder().append('\n').append(Krb5.hexDumper.encodeBuffer(this.keyValue)).append('\n').toString()));
/*     */   }
/*     */ 
/*     */   public static EncryptionKey findKey(int paramInt, EncryptionKey[] paramArrayOfEncryptionKey)
/*     */     throws KrbException
/*     */   {
/* 523 */     return findKey(paramInt, null, paramArrayOfEncryptionKey);
/*     */   }
/*     */ 
/*     */   private static boolean versionMatches(Integer paramInteger1, Integer paramInteger2)
/*     */   {
/* 537 */     if ((paramInteger1 == null) || (paramInteger1.intValue() == 0) || (paramInteger2 == null) || (paramInteger2.intValue() == 0)) {
/* 538 */       return true;
/*     */     }
/* 540 */     return paramInteger1.equals(paramInteger2);
/*     */   }
/*     */ 
/*     */   public static EncryptionKey findKey(int paramInt, Integer paramInteger, EncryptionKey[] paramArrayOfEncryptionKey)
/*     */     throws KrbException
/*     */   {
/* 551 */     if (!EType.isSupported(paramInt)) {
/* 552 */       throw new KrbException("Encryption type " + EType.toString(paramInt) + " is not supported/enabled");
/*     */     }
/*     */ 
/* 557 */     int j = 0;
/*     */     int i;
/*     */     Integer localInteger;
/* 558 */     for (int k = 0; k < paramArrayOfEncryptionKey.length; k++) {
/* 559 */       i = paramArrayOfEncryptionKey[k].getEType();
/* 560 */       if (EType.isSupported(i)) {
/* 561 */         localInteger = paramArrayOfEncryptionKey[k].getKeyVersionNumber();
/* 562 */         if (paramInt == i) {
/* 563 */           j = 1;
/* 564 */           if (versionMatches(paramInteger, localInteger)) {
/* 565 */             return paramArrayOfEncryptionKey[k];
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 573 */     if ((paramInt == 1) || (paramInt == 3))
/*     */     {
/* 575 */       for (k = 0; k < paramArrayOfEncryptionKey.length; k++) {
/* 576 */         i = paramArrayOfEncryptionKey[k].getEType();
/* 577 */         if ((i == 1) || (i == 3))
/*     */         {
/* 579 */           localInteger = paramArrayOfEncryptionKey[k].getKeyVersionNumber();
/* 580 */           j = 1;
/* 581 */           if (versionMatches(paramInteger, localInteger)) {
/* 582 */             return new EncryptionKey(paramInt, paramArrayOfEncryptionKey[k].getBytes());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 587 */     if (j != 0) {
/* 588 */       throw new KrbException(44);
/*     */     }
/* 590 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.EncryptionKey
 * JD-Core Version:    0.6.2
 */