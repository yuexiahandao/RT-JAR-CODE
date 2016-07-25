/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PAEncTSEnc
/*     */ {
/*     */   public KerberosTime pATimeStamp;
/*     */   public Integer pAUSec;
/*     */ 
/*     */   public PAEncTSEnc(KerberosTime paramKerberosTime, Integer paramInteger)
/*     */   {
/*  63 */     this.pATimeStamp = paramKerberosTime;
/*  64 */     this.pAUSec = paramInteger;
/*     */   }
/*     */ 
/*     */   public PAEncTSEnc() {
/*  68 */     KerberosTime localKerberosTime = new KerberosTime(true);
/*  69 */     this.pATimeStamp = localKerberosTime;
/*  70 */     this.pAUSec = new Integer(localKerberosTime.getMicroSeconds());
/*     */   }
/*     */ 
/*     */   public PAEncTSEnc(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  81 */     if (paramDerValue.getTag() != 48) {
/*  82 */       throw new Asn1Exception(906);
/*     */     }
/*  84 */     this.pATimeStamp = KerberosTime.parse(paramDerValue.getData(), (byte)0, false);
/*  85 */     if (paramDerValue.getData().available() > 0) {
/*  86 */       DerValue localDerValue = paramDerValue.getData().getDerValue();
/*  87 */       if ((localDerValue.getTag() & 0x1F) == 1)
/*  88 */         this.pAUSec = new Integer(localDerValue.getData().getBigInteger().intValue());
/*     */       else
/*  90 */         throw new Asn1Exception(906);
/*     */     }
/*  92 */     if (paramDerValue.getData().available() > 0)
/*  93 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 104 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 105 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 106 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), this.pATimeStamp.asn1Encode());
/* 107 */     if (this.pAUSec != null) {
/* 108 */       localDerOutputStream2 = new DerOutputStream();
/* 109 */       localDerOutputStream2.putInteger(BigInteger.valueOf(this.pAUSec.intValue()));
/* 110 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */     }
/* 112 */     localDerOutputStream2 = new DerOutputStream();
/* 113 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 114 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.PAEncTSEnc
 * JD-Core Version:    0.6.2
 */