/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncryptedData
/*     */   implements Cloneable
/*     */ {
/*     */   int eType;
/*     */   Integer kvno;
/*     */   byte[] cipher;
/*     */   byte[] plain;
/*     */   public static final int ETYPE_NULL = 0;
/*     */   public static final int ETYPE_DES_CBC_CRC = 1;
/*     */   public static final int ETYPE_DES_CBC_MD4 = 2;
/*     */   public static final int ETYPE_DES_CBC_MD5 = 3;
/*     */   public static final int ETYPE_ARCFOUR_HMAC = 23;
/*     */   public static final int ETYPE_ARCFOUR_HMAC_EXP = 24;
/*     */   public static final int ETYPE_DES3_CBC_HMAC_SHA1_KD = 16;
/*     */   public static final int ETYPE_AES128_CTS_HMAC_SHA1_96 = 17;
/*     */   public static final int ETYPE_AES256_CTS_HMAC_SHA1_96 = 18;
/*     */ 
/*     */   private EncryptedData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  87 */     EncryptedData localEncryptedData = new EncryptedData();
/*  88 */     localEncryptedData.eType = this.eType;
/*  89 */     if (this.kvno != null) {
/*  90 */       localEncryptedData.kvno = new Integer(this.kvno.intValue());
/*     */     }
/*  92 */     if (this.cipher != null) {
/*  93 */       localEncryptedData.cipher = new byte[this.cipher.length];
/*  94 */       System.arraycopy(this.cipher, 0, localEncryptedData.cipher, 0, this.cipher.length);
/*     */     }
/*     */ 
/*  97 */     return localEncryptedData;
/*     */   }
/*     */ 
/*     */   public EncryptedData(int paramInt, Integer paramInteger, byte[] paramArrayOfByte)
/*     */   {
/* 105 */     this.eType = paramInt;
/* 106 */     this.kvno = paramInteger;
/* 107 */     this.cipher = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public EncryptedData(EncryptionKey paramEncryptionKey, byte[] paramArrayOfByte, int paramInt)
/*     */     throws KdcErrException, KrbCryptoException
/*     */   {
/* 130 */     EType localEType = EType.getInstance(paramEncryptionKey.getEType());
/* 131 */     this.cipher = localEType.encrypt(paramArrayOfByte, paramEncryptionKey.getBytes(), paramInt);
/* 132 */     this.eType = paramEncryptionKey.getEType();
/* 133 */     this.kvno = paramEncryptionKey.getKeyVersionNumber();
/*     */   }
/*     */ 
/*     */   public byte[] decrypt(EncryptionKey paramEncryptionKey, int paramInt)
/*     */     throws KdcErrException, KrbApErrException, KrbCryptoException
/*     */   {
/* 168 */     if (this.eType != paramEncryptionKey.getEType()) {
/* 169 */       throw new KrbCryptoException("EncryptedData is encrypted using keytype " + EType.toString(this.eType) + " but decryption key is of type " + EType.toString(paramEncryptionKey.getEType()));
/*     */     }
/*     */ 
/* 176 */     EType localEType = EType.getInstance(this.eType);
/* 177 */     this.plain = localEType.decrypt(this.cipher, paramEncryptionKey.getBytes(), paramInt);
/* 178 */     this.cipher = null;
/* 179 */     return localEType.decryptedData(this.plain);
/*     */   }
/*     */ 
/*     */   private byte[] decryptedData()
/*     */     throws KdcErrException
/*     */   {
/* 210 */     if (this.plain != null) {
/* 211 */       EType localEType = EType.getInstance(this.eType);
/* 212 */       return localEType.decryptedData(this.plain);
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   private EncryptedData(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 230 */     DerValue localDerValue = null;
/* 231 */     if (paramDerValue.getTag() != 48) {
/* 232 */       throw new Asn1Exception(906);
/*     */     }
/* 234 */     localDerValue = paramDerValue.getData().getDerValue();
/* 235 */     if ((localDerValue.getTag() & 0x1F) == 0)
/* 236 */       this.eType = localDerValue.getData().getBigInteger().intValue();
/*     */     else {
/* 238 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 241 */     if ((paramDerValue.getData().peekByte() & 0x1F) == 1) {
/* 242 */       localDerValue = paramDerValue.getData().getDerValue();
/* 243 */       int i = localDerValue.getData().getBigInteger().intValue();
/* 244 */       this.kvno = new Integer(i);
/*     */     } else {
/* 246 */       this.kvno = null;
/*     */     }
/* 248 */     localDerValue = paramDerValue.getData().getDerValue();
/* 249 */     if ((localDerValue.getTag() & 0x1F) == 2)
/* 250 */       this.cipher = localDerValue.getData().getOctetString();
/*     */     else {
/* 252 */       throw new Asn1Exception(906);
/*     */     }
/* 254 */     if (paramDerValue.getData().available() > 0)
/* 255 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 284 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 285 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 286 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.eType));
/* 287 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 289 */     localDerOutputStream2 = new DerOutputStream();
/* 290 */     if (this.kvno != null)
/*     */     {
/* 292 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.kvno.longValue()));
/* 293 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */ 
/* 295 */       localDerOutputStream2 = new DerOutputStream();
/*     */     }
/* 297 */     localDerOutputStream2.putOctetString(this.cipher);
/* 298 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */ 
/* 300 */     localDerOutputStream2 = new DerOutputStream();
/* 301 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 302 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static EncryptedData parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 326 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 328 */       return null;
/* 329 */     }DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 330 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 331 */       throw new Asn1Exception(906);
/*     */     }
/* 333 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 334 */     return new EncryptedData(localDerValue2);
/*     */   }
/*     */ 
/*     */   public byte[] reset(byte[] paramArrayOfByte)
/*     */   {
/* 346 */     byte[] arrayOfByte = null;
/*     */ 
/* 349 */     if ((paramArrayOfByte[1] & 0xFF) < 128) {
/* 350 */       arrayOfByte = new byte[paramArrayOfByte[1] + 2];
/* 351 */       System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte[1] + 2);
/*     */     }
/* 353 */     else if ((paramArrayOfByte[1] & 0xFF) > 128) {
/* 354 */       int i = paramArrayOfByte[1] & 0x7F;
/* 355 */       int j = 0;
/* 356 */       for (int k = 0; k < i; k++) {
/* 357 */         j |= (paramArrayOfByte[(k + 2)] & 0xFF) << 8 * (i - k - 1);
/*     */       }
/* 359 */       arrayOfByte = new byte[j + i + 2];
/* 360 */       System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, j + i + 2);
/*     */     }
/*     */ 
/* 363 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getEType() {
/* 367 */     return this.eType;
/*     */   }
/*     */ 
/*     */   public Integer getKeyVersionNumber() {
/* 371 */     return this.kvno;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 378 */     return this.cipher;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.EncryptedData
 * JD-Core Version:    0.6.2
 */