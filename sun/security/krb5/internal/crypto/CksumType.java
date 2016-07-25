/*     */ package sun.security.krb5.internal.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.KrbCryptoException;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ 
/*     */ public abstract class CksumType
/*     */ {
/*  43 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public static CksumType getInstance(int paramInt) throws KdcErrException
/*     */   {
/*  47 */     Object localObject = null;
/*  48 */     String str = null;
/*  49 */     switch (paramInt) {
/*     */     case 1:
/*  51 */       localObject = new Crc32CksumType();
/*  52 */       str = "sun.security.krb5.internal.crypto.Crc32CksumType";
/*  53 */       break;
/*     */     case 4:
/*  55 */       localObject = new DesMacCksumType();
/*  56 */       str = "sun.security.krb5.internal.crypto.DesMacCksumType";
/*  57 */       break;
/*     */     case 5:
/*  59 */       localObject = new DesMacKCksumType();
/*  60 */       str = "sun.security.krb5.internal.crypto.DesMacKCksumType";
/*     */ 
/*  62 */       break;
/*     */     case 7:
/*  64 */       localObject = new RsaMd5CksumType();
/*  65 */       str = "sun.security.krb5.internal.crypto.RsaMd5CksumType";
/*  66 */       break;
/*     */     case 8:
/*  68 */       localObject = new RsaMd5DesCksumType();
/*  69 */       str = "sun.security.krb5.internal.crypto.RsaMd5DesCksumType";
/*     */ 
/*  71 */       break;
/*     */     case 12:
/*  74 */       localObject = new HmacSha1Des3KdCksumType();
/*  75 */       str = "sun.security.krb5.internal.crypto.HmacSha1Des3KdCksumType";
/*     */ 
/*  77 */       break;
/*     */     case 15:
/*  80 */       localObject = new HmacSha1Aes128CksumType();
/*  81 */       str = "sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType";
/*     */ 
/*  83 */       break;
/*     */     case 16:
/*  85 */       localObject = new HmacSha1Aes256CksumType();
/*  86 */       str = "sun.security.krb5.internal.crypto.HmacSha1Aes256CksumType";
/*     */ 
/*  88 */       break;
/*     */     case -138:
/*  91 */       localObject = new HmacMd5ArcFourCksumType();
/*  92 */       str = "sun.security.krb5.internal.crypto.HmacMd5ArcFourCksumType";
/*     */ 
/*  94 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 6:
/*     */     default:
/* 112 */       throw new KdcErrException(15);
/*     */     }
/* 114 */     if (DEBUG) {
/* 115 */       System.out.println(">>> CksumType: " + str);
/*     */     }
/* 117 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static CksumType getInstance()
/*     */     throws KdcErrException
/*     */   {
/* 126 */     int i = 7;
/*     */     try {
/* 128 */       Config localConfig = Config.getInstance();
/* 129 */       if ((i = localConfig.getType(localConfig.getDefault("ap_req_checksum_type", "libdefaults"))) == -1)
/*     */       {
/* 131 */         if ((i = localConfig.getType(localConfig.getDefault("checksum_type", "libdefaults"))) == -1)
/*     */         {
/* 133 */           i = 7;
/*     */         }
/*     */       }
/*     */     } catch (KrbException localKrbException) {
/*     */     }
/* 138 */     return getInstance(i);
/*     */   }
/*     */ 
/*     */   public abstract int confounderSize();
/*     */ 
/*     */   public abstract int cksumType();
/*     */ 
/*     */   public abstract boolean isSafe();
/*     */ 
/*     */   public abstract int cksumSize();
/*     */ 
/*     */   public abstract int keyType();
/*     */ 
/*     */   public abstract int keySize();
/*     */ 
/*     */   public abstract byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt)
/*     */     throws KrbCryptoException;
/*     */ 
/*     */   public abstract byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*     */     throws KrbCryptoException;
/*     */ 
/*     */   public abstract boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2) throws KrbCryptoException;
/*     */ 
/*     */   public static boolean isChecksumEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 163 */     if (paramArrayOfByte1 == paramArrayOfByte2)
/* 164 */       return true;
/* 165 */     if (((paramArrayOfByte1 == null) && (paramArrayOfByte2 != null)) || ((paramArrayOfByte1 != null) && (paramArrayOfByte2 == null)))
/*     */     {
/* 167 */       return false;
/* 168 */     }if (paramArrayOfByte1.length != paramArrayOfByte2.length)
/* 169 */       return false;
/* 170 */     for (int i = 0; i < paramArrayOfByte1.length; i++)
/* 171 */       if (paramArrayOfByte1[i] != paramArrayOfByte2[i])
/* 172 */         return false;
/* 173 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.CksumType
 * JD-Core Version:    0.6.2
 */