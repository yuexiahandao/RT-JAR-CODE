/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KRBSafeBody
/*     */ {
/*  61 */   public byte[] userData = null;
/*     */   public KerberosTime timestamp;
/*     */   public Integer usec;
/*     */   public Integer seqNumber;
/*     */   public HostAddress sAddress;
/*     */   public HostAddress rAddress;
/*     */ 
/*     */   public KRBSafeBody(byte[] paramArrayOfByte, KerberosTime paramKerberosTime, Integer paramInteger1, Integer paramInteger2, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */   {
/*  76 */     if (paramArrayOfByte != null) {
/*  77 */       this.userData = ((byte[])paramArrayOfByte.clone());
/*     */     }
/*  79 */     this.timestamp = paramKerberosTime;
/*  80 */     this.usec = paramInteger1;
/*  81 */     this.seqNumber = paramInteger2;
/*  82 */     this.sAddress = paramHostAddress1;
/*  83 */     this.rAddress = paramHostAddress2;
/*     */   }
/*     */ 
/*     */   public KRBSafeBody(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  95 */     if (paramDerValue.getTag() != 48) {
/*  96 */       throw new Asn1Exception(906);
/*     */     }
/*  98 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/*  99 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/* 100 */       this.userData = localDerValue.getData().getOctetString();
/*     */     }
/*     */     else
/* 103 */       throw new Asn1Exception(906);
/* 104 */     this.timestamp = KerberosTime.parse(paramDerValue.getData(), (byte)1, true);
/* 105 */     if ((paramDerValue.getData().peekByte() & 0x1F) == 2) {
/* 106 */       localDerValue = paramDerValue.getData().getDerValue();
/* 107 */       this.usec = new Integer(localDerValue.getData().getBigInteger().intValue());
/*     */     }
/* 109 */     if ((paramDerValue.getData().peekByte() & 0x1F) == 3) {
/* 110 */       localDerValue = paramDerValue.getData().getDerValue();
/* 111 */       this.seqNumber = new Integer(localDerValue.getData().getBigInteger().intValue());
/*     */     }
/* 113 */     this.sAddress = HostAddress.parse(paramDerValue.getData(), (byte)4, false);
/* 114 */     if (paramDerValue.getData().available() > 0)
/* 115 */       this.rAddress = HostAddress.parse(paramDerValue.getData(), (byte)5, true);
/* 116 */     if (paramDerValue.getData().available() > 0)
/* 117 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 127 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 128 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 129 */     localDerOutputStream2.putOctetString(this.userData);
/* 130 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 131 */     if (this.timestamp != null)
/* 132 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), this.timestamp.asn1Encode());
/* 133 */     if (this.usec != null) {
/* 134 */       localDerOutputStream2 = new DerOutputStream();
/* 135 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.usec.intValue()));
/* 136 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */     }
/* 138 */     if (this.seqNumber != null) {
/* 139 */       localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 141 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
/* 142 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream2);
/*     */     }
/* 144 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)4), this.sAddress.asn1Encode());
/* 145 */     if (this.rAddress != null)
/* 146 */       localDerOutputStream2 = new DerOutputStream();
/* 147 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 148 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static KRBSafeBody parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 164 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 165 */       return null;
/* 166 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 167 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 168 */       throw new Asn1Exception(906);
/*     */     }
/* 170 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 171 */     return new KRBSafeBody(localDerValue2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KRBSafeBody
 * JD-Core Version:    0.6.2
 */