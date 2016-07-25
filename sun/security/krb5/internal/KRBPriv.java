/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptedData;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KRBPriv
/*     */ {
/*     */   public int pvno;
/*     */   public int msgType;
/*     */   public EncryptedData encPart;
/*     */ 
/*     */   public KRBPriv(EncryptedData paramEncryptedData)
/*     */   {
/*  64 */     this.pvno = 5;
/*  65 */     this.msgType = 21;
/*  66 */     this.encPart = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public KRBPriv(byte[] paramArrayOfByte) throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  71 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public KRBPriv(DerValue paramDerValue) throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  76 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  91 */     if (((paramDerValue.getTag() & 0x1F) != 21) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/*  94 */       throw new Asn1Exception(906);
/*  95 */     }DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/*  96 */     if (localDerValue1.getTag() != 48)
/*  97 */       throw new Asn1Exception(906);
/*  98 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/*  99 */     if ((localDerValue2.getTag() & 0x1F) == 0) {
/* 100 */       this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 101 */       if (this.pvno != 5)
/* 102 */         throw new KrbApErrException(39);
/*     */     }
/*     */     else
/*     */     {
/* 106 */       throw new Asn1Exception(906);
/* 107 */     }localDerValue2 = localDerValue1.getData().getDerValue();
/* 108 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 109 */       this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 110 */       if (this.msgType != 21)
/* 111 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else {
/* 114 */       throw new Asn1Exception(906);
/* 115 */     }this.encPart = EncryptedData.parse(localDerValue1.getData(), (byte)3, false);
/* 116 */     if (localDerValue1.getData().available() > 0)
/* 117 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 128 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 129 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.pvno));
/* 130 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 131 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1);
/* 132 */     localDerOutputStream1 = new DerOutputStream();
/* 133 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.msgType));
/* 134 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1);
/* 135 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)3), this.encPart.asn1Encode());
/* 136 */     localDerOutputStream1 = new DerOutputStream();
/* 137 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 138 */     localDerOutputStream2 = new DerOutputStream();
/* 139 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)21), localDerOutputStream1);
/* 140 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KRBPriv
 * JD-Core Version:    0.6.2
 */