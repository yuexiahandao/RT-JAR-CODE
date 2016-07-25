/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptedData;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class APReq
/*     */ {
/*     */   public int pvno;
/*     */   public int msgType;
/*     */   public APOptions apOptions;
/*     */   public Ticket ticket;
/*     */   public EncryptedData authenticator;
/*     */ 
/*     */   public APReq(APOptions paramAPOptions, Ticket paramTicket, EncryptedData paramEncryptedData)
/*     */   {
/*  69 */     this.pvno = 5;
/*  70 */     this.msgType = 14;
/*  71 */     this.apOptions = paramAPOptions;
/*  72 */     this.ticket = paramTicket;
/*  73 */     this.authenticator = paramEncryptedData;
/*     */   }
/*     */ 
/*     */   public APReq(byte[] paramArrayOfByte) throws Asn1Exception, IOException, KrbApErrException, RealmException {
/*  77 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public APReq(DerValue paramDerValue) throws Asn1Exception, IOException, KrbApErrException, RealmException {
/*  81 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException, KrbApErrException, RealmException
/*     */   {
/*  95 */     if (((paramDerValue.getTag() & 0x1F) != 14) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/*  98 */       throw new Asn1Exception(906);
/*     */     }
/* 100 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 101 */     if (localDerValue1.getTag() != 48) {
/* 102 */       throw new Asn1Exception(906);
/*     */     }
/* 104 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 105 */     if ((localDerValue2.getTag() & 0x1F) != 0) {
/* 106 */       throw new Asn1Exception(906);
/*     */     }
/* 108 */     this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 109 */     if (this.pvno != 5) {
/* 110 */       throw new KrbApErrException(39);
/*     */     }
/* 112 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 113 */     if ((localDerValue2.getTag() & 0x1F) != 1) {
/* 114 */       throw new Asn1Exception(906);
/*     */     }
/* 116 */     this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 117 */     if (this.msgType != 14) {
/* 118 */       throw new KrbApErrException(40);
/*     */     }
/* 120 */     this.apOptions = APOptions.parse(localDerValue1.getData(), (byte)2, false);
/* 121 */     this.ticket = Ticket.parse(localDerValue1.getData(), (byte)3, false);
/* 122 */     this.authenticator = EncryptedData.parse(localDerValue1.getData(), (byte)4, false);
/* 123 */     if (localDerValue1.getData().available() > 0)
/* 124 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 135 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 136 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 137 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.pvno));
/* 138 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 139 */     localDerOutputStream2 = new DerOutputStream();
/* 140 */     localDerOutputStream2.putInteger(BigInteger.valueOf(this.msgType));
/* 141 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 142 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), this.apOptions.asn1Encode());
/* 143 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), this.ticket.asn1Encode());
/* 144 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)4), this.authenticator.asn1Encode());
/* 145 */     localDerOutputStream2 = new DerOutputStream();
/* 146 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 147 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 148 */     localDerOutputStream3.write(DerValue.createTag((byte)64, true, (byte)14), localDerOutputStream2);
/* 149 */     return localDerOutputStream3.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.APReq
 * JD-Core Version:    0.6.2
 */