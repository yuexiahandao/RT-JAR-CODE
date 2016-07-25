/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.Key;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ 
/*     */ public final class Des
/*     */ {
/*  56 */   private static final String CHARSET = (String)AccessController.doPrivileged(new GetPropertyAction("sun.security.krb5.msinterop.des.s2kcharset"));
/*     */ 
/*  60 */   private static final long[] bad_keys = { 72340172838076673L, -72340172838076674L, 2242545357980376863L, -2242545357980376864L, 143554428589179390L, -143554428589179391L, 2296870857142767345L, -2296870857142767346L, 135110050437988849L, -2305315235293957887L, 2305315235293957886L, -135110050437988850L, 80784550989267214L, 2234100979542855169L, -2234100979542855170L, -80784550989267215L };
/*     */ 
/*  71 */   private static final byte[] good_parity = { 1, 1, 2, 2, 4, 4, 7, 7, 8, 8, 11, 11, 13, 13, 14, 14, 16, 16, 19, 19, 21, 21, 22, 22, 25, 25, 26, 26, 28, 28, 31, 31, 32, 32, 35, 35, 37, 37, 38, 38, 41, 41, 42, 42, 44, 44, 47, 47, 49, 49, 50, 50, 52, 52, 55, 55, 56, 56, 59, 59, 61, 61, 62, 62, 64, 64, 67, 67, 69, 69, 70, 70, 73, 73, 74, 74, 76, 76, 79, 79, 81, 81, 82, 82, 84, 84, 87, 87, 88, 88, 91, 91, 93, 93, 94, 94, 97, 97, 98, 98, 100, 100, 103, 103, 104, 104, 107, 107, 109, 109, 110, 110, 112, 112, 115, 115, 117, 117, 118, 118, 121, 121, 122, 122, 124, 124, 127, 127, -128, -128, -125, -125, -123, -123, -122, -122, -119, -119, -118, -118, -116, -116, -113, -113, -111, -111, -110, -110, -108, -108, -105, -105, -104, -104, -101, -101, -99, -99, -98, -98, -95, -95, -94, -94, -92, -92, -89, -89, -88, -88, -85, -85, -83, -83, -82, -82, -80, -80, -77, -77, -75, -75, -74, -74, -71, -71, -70, -70, -68, -68, -65, -65, -63, -63, -62, -62, -60, -60, -57, -57, -56, -56, -53, -53, -51, -51, -50, -50, -48, -48, -45, -45, -43, -43, -42, -42, -39, -39, -38, -38, -36, -36, -33, -33, -32, -32, -29, -29, -27, -27, -26, -26, -23, -23, -22, -22, -20, -20, -17, -17, -15, -15, -14, -14, -12, -12, -9, -9, -8, -8, -5, -5, -3, -3, -2, -2 };
/*     */ 
/*     */   public static final byte[] set_parity(byte[] paramArrayOfByte)
/*     */   {
/* 123 */     for (int i = 0; i < 8; i++) {
/* 124 */       paramArrayOfByte[i] = good_parity[(paramArrayOfByte[i] & 0xFF)];
/*     */     }
/* 126 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public static final long set_parity(long paramLong) {
/* 130 */     return octet2long(set_parity(long2octet(paramLong)));
/*     */   }
/*     */ 
/*     */   public static final boolean bad_key(long paramLong) {
/* 134 */     for (int i = 0; i < bad_keys.length; i++) {
/* 135 */       if (bad_keys[i] == paramLong) {
/* 136 */         return true;
/*     */       }
/*     */     }
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   public static final boolean bad_key(byte[] paramArrayOfByte) {
/* 143 */     return bad_key(octet2long(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public static long octet2long(byte[] paramArrayOfByte) {
/* 147 */     return octet2long(paramArrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   public static long octet2long(byte[] paramArrayOfByte, int paramInt) {
/* 151 */     long l = 0L;
/* 152 */     for (int i = 0; i < 8; i++) {
/* 153 */       if (i + paramInt < paramArrayOfByte.length) {
/* 154 */         l |= (paramArrayOfByte[(i + paramInt)] & 0xFF) << (7 - i) * 8;
/*     */       }
/*     */     }
/* 157 */     return l;
/*     */   }
/*     */ 
/*     */   public static byte[] long2octet(long paramLong) {
/* 161 */     byte[] arrayOfByte = new byte[8];
/* 162 */     for (int i = 0; i < 8; i++) {
/* 163 */       arrayOfByte[i] = ((byte)(int)(paramLong >>> (7 - i) * 8 & 0xFF));
/*     */     }
/* 165 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static void long2octet(long paramLong, byte[] paramArrayOfByte) {
/* 169 */     long2octet(paramLong, paramArrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   public static void long2octet(long paramLong, byte[] paramArrayOfByte, int paramInt) {
/* 173 */     for (int i = 0; i < 8; i++)
/* 174 */       if (i + paramInt < paramArrayOfByte.length)
/* 175 */         paramArrayOfByte[(i + paramInt)] = ((byte)(int)(paramLong >>> (7 - i) * 8 & 0xFF));
/*     */   }
/*     */ 
/*     */   public static void cbc_encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, boolean paramBoolean)
/*     */     throws KrbCryptoException
/*     */   {
/* 197 */     Cipher localCipher = null;
/*     */     try
/*     */     {
/* 200 */       localCipher = Cipher.getInstance("DES/CBC/NoPadding");
/*     */     } catch (GeneralSecurityException localGeneralSecurityException1) {
/* 202 */       localObject1 = new KrbCryptoException("JCE provider may not be installed. " + localGeneralSecurityException1.getMessage());
/*     */ 
/* 204 */       ((KrbCryptoException)localObject1).initCause(localGeneralSecurityException1);
/* 205 */       throw ((Throwable)localObject1);
/*     */     }
/* 207 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte4);
/* 208 */     Object localObject1 = new SecretKeySpec(paramArrayOfByte3, "DES");
/*     */     try {
/* 210 */       SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("DES");
/*     */ 
/* 212 */       localObject2 = localObject1;
/* 213 */       if (paramBoolean)
/* 214 */         localCipher.init(1, (Key)localObject2, localIvParameterSpec);
/*     */       else {
/* 216 */         localCipher.init(2, (Key)localObject2, localIvParameterSpec);
/*     */       }
/* 218 */       byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte1);
/* 219 */       System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, 0, arrayOfByte.length);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException2) {
/* 221 */       Object localObject2 = new KrbCryptoException(localGeneralSecurityException2.getMessage());
/* 222 */       ((KrbCryptoException)localObject2).initCause(localGeneralSecurityException2);
/* 223 */       throw ((Throwable)localObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static long char_to_key(char[] paramArrayOfChar)
/*     */     throws KrbCryptoException
/*     */   {
/* 235 */     long l1 = 0L;
/* 236 */     long l4 = 0L;
/* 237 */     byte[] arrayOfByte1 = null;
/*     */     try
/*     */     {
/* 241 */       if (CHARSET == null)
/* 242 */         arrayOfByte1 = new String(paramArrayOfChar).getBytes();
/*     */       else
/* 244 */         arrayOfByte1 = new String(paramArrayOfChar).getBytes(CHARSET);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 248 */       if (arrayOfByte1 != null) {
/* 249 */         Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */       }
/* 251 */       localObject = new KrbCryptoException("Unable to convert passwd, " + localException);
/*     */ 
/* 253 */       ((KrbCryptoException)localObject).initCause(localException);
/* 254 */       throw ((Throwable)localObject);
/*     */     }
/*     */ 
/* 258 */     byte[] arrayOfByte2 = pad(arrayOfByte1);
/*     */ 
/* 260 */     Object localObject = new byte[8];
/* 261 */     int i = arrayOfByte2.length / 8 + (arrayOfByte2.length % 8 == 0 ? 0 : 1);
/* 262 */     for (int j = 0; j < i; j++) {
/* 263 */       long l2 = octet2long(arrayOfByte2, j * 8) & 0x7F7F7F7F;
/* 264 */       if (j % 2 == 1) {
/* 265 */         long l3 = 0L;
/* 266 */         for (int k = 0; k < 64; k++) {
/* 267 */           l3 |= (l2 & 1L << k) >>> k << 63 - k;
/*     */         }
/* 269 */         l2 = l3 >>> 1;
/*     */       }
/* 271 */       l1 ^= l2 << 1;
/*     */     }
/* 273 */     l1 = set_parity(l1);
/*     */     byte[] arrayOfByte3;
/* 274 */     if (bad_key(l1)) {
/* 275 */       arrayOfByte3 = long2octet(l1);
/*     */       byte[] tmp253_249 = arrayOfByte3; tmp253_249[7] = ((byte)(tmp253_249[7] ^ 0xF0));
/* 277 */       l1 = octet2long(tmp253_249);
/*     */     }
/*     */ 
/* 280 */     localObject = des_cksum(long2octet(l1), arrayOfByte2, long2octet(l1));
/* 281 */     l1 = octet2long(set_parity((byte[])localObject));
/* 282 */     if (bad_key(l1)) {
/* 283 */       tmp253_249 = long2octet(l1);
/*     */       byte[] tmp308_304 = tmp253_249; tmp308_304[7] = ((byte)(tmp308_304[7] ^ 0xF0));
/* 285 */       l1 = octet2long(tmp253_249);
/*     */     }
/*     */ 
/* 289 */     if (arrayOfByte1 != null) {
/* 290 */       Arrays.fill(arrayOfByte1, 0, arrayOfByte1.length, (byte)0);
/*     */     }
/* 292 */     if (arrayOfByte2 != null) {
/* 293 */       Arrays.fill(arrayOfByte2, 0, arrayOfByte2.length, (byte)0);
/*     */     }
/*     */ 
/* 296 */     return l1;
/*     */   }
/*     */ 
/*     */   public static byte[] des_cksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */     throws KrbCryptoException
/*     */   {
/* 310 */     Cipher localCipher = null;
/*     */ 
/* 312 */     byte[] arrayOfByte = new byte[8];
/*     */     try {
/* 314 */       localCipher = Cipher.getInstance("DES/CBC/NoPadding");
/*     */     } catch (Exception localException) {
/* 316 */       localObject1 = new KrbCryptoException("JCE provider may not be installed. " + localException.getMessage());
/*     */ 
/* 318 */       ((KrbCryptoException)localObject1).initCause(localException);
/* 319 */       throw ((Throwable)localObject1);
/*     */     }
/* 321 */     IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte1);
/* 322 */     Object localObject1 = new SecretKeySpec(paramArrayOfByte3, "DES");
/*     */     try {
/* 324 */       SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("DES");
/*     */ 
/* 326 */       localObject2 = localObject1;
/* 327 */       localCipher.init(1, (Key)localObject2, localIvParameterSpec);
/* 328 */       for (int i = 0; i < paramArrayOfByte2.length / 8; i++) {
/* 329 */         arrayOfByte = localCipher.doFinal(paramArrayOfByte2, i * 8, 8);
/* 330 */         localCipher.init(1, (Key)localObject2, new IvParameterSpec(arrayOfByte));
/*     */       }
/*     */     }
/*     */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 334 */       Object localObject2 = new KrbCryptoException(localGeneralSecurityException.getMessage());
/* 335 */       ((KrbCryptoException)localObject2).initCause(localGeneralSecurityException);
/* 336 */       throw ((Throwable)localObject2);
/*     */     }
/* 338 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static byte[] pad(byte[] paramArrayOfByte)
/*     */   {
/* 350 */     int i;
/* 350 */     if (paramArrayOfByte.length < 8) i = paramArrayOfByte.length; else
/* 351 */       i = paramArrayOfByte.length % 8;
/* 352 */     if (i == 0) return paramArrayOfByte;
/*     */ 
/* 354 */     byte[] arrayOfByte = new byte[8 - i + paramArrayOfByte.length];
/* 355 */     for (int j = arrayOfByte.length - 1; j > paramArrayOfByte.length - 1; j--) {
/* 356 */       arrayOfByte[j] = 0;
/*     */     }
/* 358 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
/* 359 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] string_to_key_bytes(char[] paramArrayOfChar)
/*     */     throws KrbCryptoException
/*     */   {
/* 366 */     return long2octet(char_to_key(paramArrayOfChar));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.Des
 * JD-Core Version:    0.6.2
 */