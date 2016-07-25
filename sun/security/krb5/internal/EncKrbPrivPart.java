/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncKrbPrivPart
/*     */ {
/*  60 */   public byte[] userData = null;
/*     */   public KerberosTime timestamp;
/*     */   public Integer usec;
/*     */   public Integer seqNumber;
/*     */   public HostAddress sAddress;
/*     */   public HostAddress rAddress;
/*     */ 
/*     */   public EncKrbPrivPart(byte[] paramArrayOfByte, KerberosTime paramKerberosTime, Integer paramInteger1, Integer paramInteger2, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */   {
/*  74 */     if (paramArrayOfByte != null) {
/*  75 */       this.userData = ((byte[])paramArrayOfByte.clone());
/*     */     }
/*  77 */     this.timestamp = paramKerberosTime;
/*  78 */     this.usec = paramInteger1;
/*  79 */     this.seqNumber = paramInteger2;
/*  80 */     this.sAddress = paramHostAddress1;
/*  81 */     this.rAddress = paramHostAddress2;
/*     */   }
/*     */ 
/*     */   public EncKrbPrivPart(byte[] paramArrayOfByte) throws Asn1Exception, IOException {
/*  85 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public EncKrbPrivPart(DerValue paramDerValue) throws Asn1Exception, IOException {
/*  89 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 100 */     if (((paramDerValue.getTag() & 0x1F) != 28) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 103 */       throw new Asn1Exception(906);
/*     */     }
/* 105 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 106 */     if (localDerValue1.getTag() != 48) {
/* 107 */       throw new Asn1Exception(906);
/*     */     }
/* 109 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 110 */     if ((localDerValue2.getTag() & 0x1F) == 0)
/* 111 */       this.userData = localDerValue2.getData().getOctetString();
/*     */     else {
/* 113 */       throw new Asn1Exception(906);
/*     */     }
/* 115 */     this.timestamp = KerberosTime.parse(localDerValue1.getData(), (byte)1, true);
/* 116 */     if ((localDerValue1.getData().peekByte() & 0x1F) == 2) {
/* 117 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 118 */       this.usec = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     } else {
/* 120 */       this.usec = null;
/*     */     }
/* 122 */     if ((localDerValue1.getData().peekByte() & 0x1F) == 3) {
/* 123 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 124 */       this.seqNumber = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     } else {
/* 126 */       this.seqNumber = null;
/*     */     }
/* 128 */     this.sAddress = HostAddress.parse(localDerValue1.getData(), (byte)4, false);
/* 129 */     if (localDerValue1.getData().available() > 0) {
/* 130 */       this.rAddress = HostAddress.parse(localDerValue1.getData(), (byte)5, true);
/*     */     }
/* 132 */     if (localDerValue1.getData().available() > 0)
/* 133 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 144 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 145 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 147 */     localDerOutputStream1.putOctetString(this.userData);
/* 148 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1);
/* 149 */     if (this.timestamp != null) {
/* 150 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)1), this.timestamp.asn1Encode());
/*     */     }
/* 152 */     if (this.usec != null) {
/* 153 */       localDerOutputStream1 = new DerOutputStream();
/* 154 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.usec.intValue()));
/* 155 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream1);
/*     */     }
/* 157 */     if (this.seqNumber != null) {
/* 158 */       localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 160 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
/* 161 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream1);
/*     */     }
/* 163 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)4), this.sAddress.asn1Encode());
/* 164 */     if (this.rAddress != null) {
/* 165 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)5), this.rAddress.asn1Encode());
/*     */     }
/* 167 */     localDerOutputStream1 = new DerOutputStream();
/* 168 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 169 */     localDerOutputStream2 = new DerOutputStream();
/* 170 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)28), localDerOutputStream1);
/* 171 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncKrbPrivPart
 * JD-Core Version:    0.6.2
 */