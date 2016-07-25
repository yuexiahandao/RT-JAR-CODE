/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.crypto.Cipher;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ 
/*     */ public abstract class EType
/*     */ {
/*  50 */   private static final boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*  66 */   private static final boolean ALLOW_WEAK_CRYPTO = bool;
/*     */ 
/* 187 */   private static final int[] BUILTIN_ETYPES = { 18, 17, 16, 23, 1, 3 };
/*     */ 
/* 196 */   private static final int[] BUILTIN_ETYPES_NOAES256 = { 17, 16, 23, 1, 3 };
/*     */ 
/*     */   public static EType getInstance(int paramInt)
/*     */     throws KdcErrException
/*     */   {
/*  71 */     Object localObject = null;
/*  72 */     String str1 = null;
/*  73 */     switch (paramInt) {
/*     */     case 0:
/*  75 */       localObject = new NullEType();
/*  76 */       str1 = "sun.security.krb5.internal.crypto.NullEType";
/*  77 */       break;
/*     */     case 1:
/*  79 */       localObject = new DesCbcCrcEType();
/*  80 */       str1 = "sun.security.krb5.internal.crypto.DesCbcCrcEType";
/*  81 */       break;
/*     */     case 3:
/*  83 */       localObject = new DesCbcMd5EType();
/*  84 */       str1 = "sun.security.krb5.internal.crypto.DesCbcMd5EType";
/*  85 */       break;
/*     */     case 16:
/*  88 */       localObject = new Des3CbcHmacSha1KdEType();
/*  89 */       str1 = "sun.security.krb5.internal.crypto.Des3CbcHmacSha1KdEType";
/*     */ 
/*  91 */       break;
/*     */     case 17:
/*  94 */       localObject = new Aes128CtsHmacSha1EType();
/*  95 */       str1 = "sun.security.krb5.internal.crypto.Aes128CtsHmacSha1EType";
/*     */ 
/*  97 */       break;
/*     */     case 18:
/* 100 */       localObject = new Aes256CtsHmacSha1EType();
/* 101 */       str1 = "sun.security.krb5.internal.crypto.Aes256CtsHmacSha1EType";
/*     */ 
/* 103 */       break;
/*     */     case 23:
/* 106 */       localObject = new ArcFourHmacEType();
/* 107 */       str1 = "sun.security.krb5.internal.crypto.ArcFourHmacEType";
/* 108 */       break;
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     default:
/* 111 */       String str2 = "encryption type = " + toString(paramInt) + " (" + paramInt + ")";
/*     */ 
/* 113 */       throw new KdcErrException(14, str2);
/*     */     }
/* 115 */     if (DEBUG) {
/* 116 */       System.out.println(">>> EType: " + str1);
/*     */     }
/* 118 */     return localObject;
/*     */   }
/*     */ 
/*     */   public abstract int eType();
/*     */ 
/*     */   public abstract int minimumPadSize();
/*     */ 
/*     */   public abstract int confounderSize();
/*     */ 
/*     */   public abstract int checksumType();
/*     */ 
/*     */   public abstract int checksumSize();
/*     */ 
/*     */   public abstract int blockSize();
/*     */ 
/*     */   public abstract int keyType();
/*     */ 
/*     */   public abstract int keySize();
/*     */ 
/*     */   public abstract byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws KrbCryptoException;
/*     */ 
/*     */   public abstract byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt)
/*     */     throws KrbCryptoException;
/*     */ 
/*     */   public abstract byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */     throws KrbApErrException, KrbCryptoException;
/*     */ 
/*     */   public abstract byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt)
/*     */     throws KrbApErrException, KrbCryptoException;
/*     */ 
/*     */   public int dataSize(byte[] paramArrayOfByte)
/*     */   {
/* 156 */     return paramArrayOfByte.length - startOfData();
/*     */   }
/*     */ 
/*     */   public int padSize(byte[] paramArrayOfByte) {
/* 160 */     return paramArrayOfByte.length - confounderSize() - checksumSize() - dataSize(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public int startOfChecksum()
/*     */   {
/* 165 */     return confounderSize();
/*     */   }
/*     */ 
/*     */   public int startOfData() {
/* 169 */     return confounderSize() + checksumSize();
/*     */   }
/*     */ 
/*     */   public int startOfPad(byte[] paramArrayOfByte) {
/* 173 */     return confounderSize() + checksumSize() + dataSize(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public byte[] decryptedData(byte[] paramArrayOfByte) {
/* 177 */     int i = dataSize(paramArrayOfByte);
/* 178 */     byte[] arrayOfByte = new byte[i];
/* 179 */     System.arraycopy(paramArrayOfByte, startOfData(), arrayOfByte, 0, i);
/* 180 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static int[] getBuiltInDefaults()
/*     */   {
/* 207 */     int i = 0;
/*     */     try {
/* 209 */       i = Cipher.getMaxAllowedKeyLength("AES");
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */     int[] arrayOfInt;
/* 214 */     if (i < 256)
/* 215 */       arrayOfInt = BUILTIN_ETYPES_NOAES256;
/*     */     else {
/* 217 */       arrayOfInt = BUILTIN_ETYPES;
/*     */     }
/* 219 */     if (!ALLOW_WEAK_CRYPTO)
/*     */     {
/* 221 */       return Arrays.copyOfRange(arrayOfInt, 0, arrayOfInt.length - 2);
/*     */     }
/* 223 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static int[] getDefaults(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 233 */       return Config.getInstance().defaultEtype(paramString);
/*     */     } catch (KrbException localKrbException) {
/* 235 */       if (DEBUG) {
/* 236 */         System.out.println("Exception while getting " + paramString + localKrbException.getMessage());
/*     */ 
/* 238 */         System.out.println("Using default builtin etypes");
/*     */       }
/*     */     }
/* 240 */     return getBuiltInDefaults();
/*     */   }
/*     */ 
/*     */   public static int[] getDefaults(String paramString, EncryptionKey[] paramArrayOfEncryptionKey)
/*     */     throws KrbException
/*     */   {
/* 254 */     int[] arrayOfInt = getDefaults(paramString);
/* 255 */     if (arrayOfInt == null) {
/* 256 */       throw new KrbException("No supported encryption types listed in " + paramString);
/*     */     }
/*     */ 
/* 260 */     ArrayList localArrayList = new ArrayList(arrayOfInt.length);
/* 261 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 262 */       if (EncryptionKey.findKey(arrayOfInt[i], paramArrayOfEncryptionKey) != null) {
/* 263 */         localArrayList.add(Integer.valueOf(arrayOfInt[i]));
/*     */       }
/*     */     }
/* 266 */     i = localArrayList.size();
/* 267 */     if (i <= 0) {
/* 268 */       StringBuffer localStringBuffer = new StringBuffer();
/* 269 */       for (int k = 0; k < paramArrayOfEncryptionKey.length; k++) {
/* 270 */         localStringBuffer.append(toString(paramArrayOfEncryptionKey[k].getEType()));
/* 271 */         localStringBuffer.append(" ");
/*     */       }
/* 273 */       throw new KrbException("Do not have keys of types listed in " + paramString + " available; only have keys of following type: " + localStringBuffer.toString());
/*     */     }
/*     */ 
/* 278 */     arrayOfInt = new int[i];
/* 279 */     for (int j = 0; j < i; j++) {
/* 280 */       arrayOfInt[j] = ((Integer)localArrayList.get(j)).intValue();
/*     */     }
/* 282 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported(int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 287 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 288 */       if (paramInt == paramArrayOfInt[i]) {
/* 289 */         return true;
/*     */       }
/*     */     }
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported(int paramInt) {
/* 296 */     int[] arrayOfInt = getBuiltInDefaults();
/* 297 */     return isSupported(paramInt, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static String toString(int paramInt) {
/* 301 */     switch (paramInt) {
/*     */     case 0:
/* 303 */       return "NULL";
/*     */     case 1:
/* 305 */       return "DES CBC mode with CRC-32";
/*     */     case 2:
/* 307 */       return "DES CBC mode with MD4";
/*     */     case 3:
/* 309 */       return "DES CBC mode with MD5";
/*     */     case 4:
/* 311 */       return "reserved";
/*     */     case 5:
/* 313 */       return "DES3 CBC mode with MD5";
/*     */     case 6:
/* 315 */       return "reserved";
/*     */     case 7:
/* 317 */       return "DES3 CBC mode with SHA1";
/*     */     case 9:
/* 319 */       return "DSA with SHA1- Cms0ID";
/*     */     case 10:
/* 321 */       return "MD5 with RSA encryption - Cms0ID";
/*     */     case 11:
/* 323 */       return "SHA1 with RSA encryption - Cms0ID";
/*     */     case 12:
/* 325 */       return "RC2 CBC mode with Env0ID";
/*     */     case 13:
/* 327 */       return "RSA encryption with Env0ID";
/*     */     case 14:
/* 329 */       return "RSAES-0AEP-ENV-0ID";
/*     */     case 15:
/* 331 */       return "DES-EDE3-CBC-ENV-0ID";
/*     */     case 16:
/* 333 */       return "DES3 CBC mode with SHA1-KD";
/*     */     case 17:
/* 335 */       return "AES128 CTS mode with HMAC SHA1-96";
/*     */     case 18:
/* 337 */       return "AES256 CTS mode with HMAC SHA1-96";
/*     */     case 23:
/* 339 */       return "RC4 with HMAC";
/*     */     case 24:
/* 341 */       return "RC4 with HMAC EXP";
/*     */     case 8:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/* 344 */     case 22: } return "Unknown (" + paramInt + ")";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  54 */     boolean bool = true;
/*     */     try {
/*  56 */       Config localConfig = Config.getInstance();
/*  57 */       String str = localConfig.getDefault("allow_weak_crypto", "libdefaults");
/*  58 */       if ((str != null) && (str.equals("false"))) bool = false; 
/*     */     }
/*  60 */     catch (Exception localException) { if (DEBUG)
/*  61 */         System.out.println("Exception in getting allow_weak_crypto, using default value " + localException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.EType
 * JD-Core Version:    0.6.2
 */