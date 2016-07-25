/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncAPRepPart
/*     */ {
/*     */   public KerberosTime ctime;
/*     */   public int cusec;
/*     */   EncryptionKey subKey;
/*     */   Integer seqNumber;
/*     */ 
/*     */   public EncAPRepPart(KerberosTime paramKerberosTime, int paramInt, EncryptionKey paramEncryptionKey, Integer paramInteger)
/*     */   {
/*  69 */     this.ctime = paramKerberosTime;
/*  70 */     this.cusec = paramInt;
/*  71 */     this.subKey = paramEncryptionKey;
/*  72 */     this.seqNumber = paramInteger;
/*     */   }
/*     */ 
/*     */   public EncAPRepPart(byte[] paramArrayOfByte) throws Asn1Exception, IOException
/*     */   {
/*  77 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public EncAPRepPart(DerValue paramDerValue) throws Asn1Exception, IOException
/*     */   {
/*  82 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  93 */     if (((paramDerValue.getTag() & 0x1F) != 27) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/*  96 */       throw new Asn1Exception(906);
/*     */     }
/*  98 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/*  99 */     if (localDerValue1.getTag() != 48) {
/* 100 */       throw new Asn1Exception(906);
/*     */     }
/* 102 */     this.ctime = KerberosTime.parse(localDerValue1.getData(), (byte)0, true);
/* 103 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 104 */     if ((localDerValue2.getTag() & 0x1F) == 1)
/* 105 */       this.cusec = localDerValue2.getData().getBigInteger().intValue();
/*     */     else {
/* 107 */       throw new Asn1Exception(906);
/*     */     }
/* 109 */     if (localDerValue1.getData().available() > 0) {
/* 110 */       this.subKey = EncryptionKey.parse(localDerValue1.getData(), (byte)2, true);
/*     */     } else {
/* 112 */       this.subKey = null;
/* 113 */       this.seqNumber = null;
/*     */     }
/* 115 */     if (localDerValue1.getData().available() > 0) {
/* 116 */       localDerValue2 = localDerValue1.getData().getDerValue();
/* 117 */       if ((localDerValue2.getTag() & 0x1F) != 3) {
/* 118 */         throw new Asn1Exception(906);
/*     */       }
/* 120 */       this.seqNumber = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */     } else {
/* 122 */       this.seqNumber = null;
/*     */     }
/* 124 */     if (localDerValue1.getData().available() > 0)
/* 125 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 136 */     Vector localVector = new Vector();
/* 137 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 138 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)0), this.ctime.asn1Encode()));
/*     */ 
/* 140 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.cusec));
/* 141 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1.toByteArray()));
/*     */ 
/* 143 */     if (this.subKey != null) {
/* 144 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)2), this.subKey.asn1Encode()));
/*     */     }
/*     */ 
/* 147 */     if (this.seqNumber != null) {
/* 148 */       localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 150 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
/* 151 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream1.toByteArray()));
/*     */     }
/*     */ 
/* 154 */     DerValue[] arrayOfDerValue = new DerValue[localVector.size()];
/* 155 */     localVector.copyInto(arrayOfDerValue);
/* 156 */     localDerOutputStream1 = new DerOutputStream();
/* 157 */     localDerOutputStream1.putSequence(arrayOfDerValue);
/* 158 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 159 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)27), localDerOutputStream1);
/*     */ 
/* 161 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public final EncryptionKey getSubKey() {
/* 165 */     return this.subKey;
/*     */   }
/*     */ 
/*     */   public final Integer getSeqNumber() {
/* 169 */     return this.seqNumber;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncAPRepPart
 * JD-Core Version:    0.6.2
 */