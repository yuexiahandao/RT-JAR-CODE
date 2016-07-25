/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.crypto.CksumType;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class Checksum
/*     */ {
/*     */   private int cksumType;
/*     */   private byte[] checksum;
/*     */   public static final int CKSUMTYPE_NULL = 0;
/*     */   public static final int CKSUMTYPE_CRC32 = 1;
/*     */   public static final int CKSUMTYPE_RSA_MD4 = 2;
/*     */   public static final int CKSUMTYPE_RSA_MD4_DES = 3;
/*     */   public static final int CKSUMTYPE_DES_MAC = 4;
/*     */   public static final int CKSUMTYPE_DES_MAC_K = 5;
/*     */   public static final int CKSUMTYPE_RSA_MD4_DES_K = 6;
/*     */   public static final int CKSUMTYPE_RSA_MD5 = 7;
/*     */   public static final int CKSUMTYPE_RSA_MD5_DES = 8;
/*     */   public static final int CKSUMTYPE_HMAC_SHA1_DES3_KD = 12;
/*     */   public static final int CKSUMTYPE_HMAC_SHA1_96_AES128 = 15;
/*     */   public static final int CKSUMTYPE_HMAC_SHA1_96_AES256 = 16;
/*     */   public static final int CKSUMTYPE_HMAC_MD5_ARCFOUR = -138;
/*     */   static int CKSUMTYPE_DEFAULT;
/*     */   static int SAFECKSUMTYPE_DEFAULT;
/*  75 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public Checksum(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 133 */     this.cksumType = paramInt;
/* 134 */     this.checksum = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public Checksum(int paramInt, byte[] paramArrayOfByte)
/*     */     throws KdcErrException, KrbCryptoException
/*     */   {
/* 146 */     this.cksumType = paramInt;
/* 147 */     CksumType localCksumType = CksumType.getInstance(this.cksumType);
/* 148 */     if (!localCksumType.isSafe())
/* 149 */       this.checksum = localCksumType.calculateChecksum(paramArrayOfByte, paramArrayOfByte.length);
/*     */     else
/* 151 */       throw new KdcErrException(50);
/*     */   }
/*     */ 
/*     */   public Checksum(int paramInt1, byte[] paramArrayOfByte, EncryptionKey paramEncryptionKey, int paramInt2)
/*     */     throws KdcErrException, KrbApErrException, KrbCryptoException
/*     */   {
/* 165 */     this.cksumType = paramInt1;
/* 166 */     CksumType localCksumType = CksumType.getInstance(this.cksumType);
/* 167 */     if (!localCksumType.isSafe())
/* 168 */       throw new KrbApErrException(50);
/* 169 */     this.checksum = localCksumType.calculateKeyedChecksum(paramArrayOfByte, paramArrayOfByte.length, paramEncryptionKey.getBytes(), paramInt2);
/*     */   }
/*     */ 
/*     */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte, EncryptionKey paramEncryptionKey, int paramInt)
/*     */     throws KdcErrException, KrbApErrException, KrbCryptoException
/*     */   {
/* 182 */     CksumType localCksumType = CksumType.getInstance(this.cksumType);
/* 183 */     if (!localCksumType.isSafe())
/* 184 */       throw new KrbApErrException(50);
/* 185 */     return localCksumType.verifyKeyedChecksum(paramArrayOfByte, paramArrayOfByte.length, paramEncryptionKey.getBytes(), this.checksum, paramInt);
/*     */   }
/*     */ 
/*     */   boolean isEqual(Checksum paramChecksum)
/*     */     throws KdcErrException
/*     */   {
/* 199 */     if (this.cksumType != paramChecksum.cksumType)
/* 200 */       return false;
/* 201 */     CksumType localCksumType = CksumType.getInstance(this.cksumType);
/* 202 */     return CksumType.isChecksumEqual(this.checksum, paramChecksum.checksum);
/*     */   }
/*     */ 
/*     */   private Checksum(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 215 */     if (paramDerValue.getTag() != 48) {
/* 216 */       throw new Asn1Exception(906);
/*     */     }
/* 218 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/* 219 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/* 220 */       this.cksumType = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else
/* 223 */       throw new Asn1Exception(906);
/* 224 */     localDerValue = paramDerValue.getData().getDerValue();
/* 225 */     if ((localDerValue.getTag() & 0x1F) == 1) {
/* 226 */       this.checksum = localDerValue.getData().getOctetString();
/*     */     }
/*     */     else
/* 229 */       throw new Asn1Exception(906);
/* 230 */     if (paramDerValue.getData().available() > 0)
/* 231 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 257 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 258 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 259 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.cksumType));
/* 260 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/* 262 */     localDerOutputStream2 = new DerOutputStream();
/* 263 */     localDerOutputStream2.putOctetString(this.checksum);
/* 264 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */ 
/* 266 */     localDerOutputStream2 = new DerOutputStream();
/* 267 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 268 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static Checksum parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 292 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 294 */       return null;
/*     */     }
/* 296 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 297 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 298 */       throw new Asn1Exception(906);
/*     */     }
/* 300 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 301 */     return new Checksum(localDerValue2);
/*     */   }
/*     */ 
/*     */   public final byte[] getBytes()
/*     */   {
/* 309 */     return this.checksum;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 313 */     return this.cksumType;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 317 */     if (this == paramObject) {
/* 318 */       return true;
/*     */     }
/* 320 */     if (!(paramObject instanceof Checksum)) {
/* 321 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 325 */       return isEqual((Checksum)paramObject); } catch (KdcErrException localKdcErrException) {
/*     */     }
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 332 */     int i = 17;
/* 333 */     i = 37 * i + this.cksumType;
/* 334 */     if (this.checksum != null) {
/* 335 */       i = 37 * i + Arrays.hashCode(this.checksum);
/*     */     }
/* 337 */     return i;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  77 */     String str = null;
/*  78 */     Config localConfig = null;
/*     */     try {
/*  80 */       localConfig = Config.getInstance();
/*  81 */       str = localConfig.getDefault("default_checksum", "libdefaults");
/*  82 */       if (str != null)
/*     */       {
/*  84 */         CKSUMTYPE_DEFAULT = localConfig.getType(str);
/*     */       }
/*     */       else
/*     */       {
/*  92 */         CKSUMTYPE_DEFAULT = 7;
/*     */       }
/*     */     } catch (Exception localException1) {
/*  95 */       if (DEBUG) {
/*  96 */         System.out.println("Exception in getting default checksum value from the configuration Setting default checksum to be RSA-MD5");
/*     */ 
/*  99 */         localException1.printStackTrace();
/*     */       }
/* 101 */       CKSUMTYPE_DEFAULT = 7;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 106 */       str = localConfig.getDefault("safe_checksum_type", "libdefaults");
/* 107 */       if (str != null)
/*     */       {
/* 109 */         SAFECKSUMTYPE_DEFAULT = localConfig.getType(str);
/*     */       }
/* 111 */       else SAFECKSUMTYPE_DEFAULT = 8; 
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/* 114 */       if (DEBUG) {
/* 115 */         System.out.println("Exception in getting safe default checksum value from the configuration Setting  safe default checksum to be RSA-MD5");
/*     */ 
/* 119 */         localException2.printStackTrace();
/*     */       }
/* 121 */       SAFECKSUMTYPE_DEFAULT = 8;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.Checksum
 * JD-Core Version:    0.6.2
 */