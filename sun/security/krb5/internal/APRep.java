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
/*     */ public class APRep
/*     */ {
/*     */   public int pvno;
/*     */   public int msgType;
/*     */   public EncryptedData encPart;
/*     */ 
/*     */   public APRep(EncryptedData paramEncryptedData)
/*     */   {
/*  63 */     this.pvno = 5;
/*  64 */     this.msgType = 15;
/*  65 */     this.encPart = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public APRep(byte[] paramArrayOfByte) throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  70 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public APRep(DerValue paramDerValue) throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  75 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, KrbApErrException, IOException
/*     */   {
/*  89 */     if (((paramDerValue.getTag() & 0x1F) != 15) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/*  92 */       throw new Asn1Exception(906);
/*     */     }
/*  94 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/*  95 */     if (localDerValue1.getTag() != 48) {
/*  96 */       throw new Asn1Exception(906);
/*     */     }
/*  98 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/*  99 */     if ((localDerValue2.getTag() & 0x1F) != 0) {
/* 100 */       throw new Asn1Exception(906);
/*     */     }
/* 102 */     this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 103 */     if (this.pvno != 5) {
/* 104 */       throw new KrbApErrException(39);
/*     */     }
/* 106 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 107 */     if ((localDerValue2.getTag() & 0x1F) != 1) {
/* 108 */       throw new Asn1Exception(906);
/*     */     }
/* 110 */     this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 111 */     if (this.msgType != 15) {
/* 112 */       throw new KrbApErrException(40);
/*     */     }
/* 114 */     this.encPart = EncryptedData.parse(localDerValue1.getData(), (byte)2, false);
/* 115 */     if (localDerValue1.getData().available() > 0)
/* 116 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 127 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 128 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 129 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.pvno));
/* 130 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 131 */     localDerOutputStream2 = new DerOutputStream();
/* 132 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.msgType));
/* 133 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 134 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), this.encPart.asn1Encode());
/* 135 */     localDerOutputStream2 = new DerOutputStream();
/* 136 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 137 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 138 */     localDerOutputStream3.write(DerValue.createTag((byte)64, true, (byte)15), localDerOutputStream2);
/* 139 */     return localDerOutputStream3.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.APRep
 * JD-Core Version:    0.6.2
 */