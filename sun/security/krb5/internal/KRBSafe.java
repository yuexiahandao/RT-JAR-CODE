/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.Checksum;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KRBSafe
/*     */ {
/*     */   public int pvno;
/*     */   public int msgType;
/*     */   public KRBSafeBody safeBody;
/*     */   public Checksum cksum;
/*     */ 
/*     */   public KRBSafe(KRBSafeBody paramKRBSafeBody, Checksum paramChecksum)
/*     */   {
/*  66 */     this.pvno = 5;
/*  67 */     this.msgType = 20;
/*  68 */     this.safeBody = paramKRBSafeBody;
/*  69 */     this.cksum = paramChecksum;
/*     */   }
/*     */ 
/*     */   public KRBSafe(byte[] paramArrayOfByte) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  74 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public KRBSafe(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  79 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*     */   {
/*  94 */     if (((paramDerValue.getTag() & 0x1F) != 20) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/*  97 */       throw new Asn1Exception(906);
/*  98 */     }DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/*  99 */     if (localDerValue1.getTag() != 48)
/* 100 */       throw new Asn1Exception(906);
/* 101 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 102 */     if ((localDerValue2.getTag() & 0x1F) == 0) {
/* 103 */       this.pvno = localDerValue2.getData().getBigInteger().intValue();
/* 104 */       if (this.pvno != 5)
/* 105 */         throw new KrbApErrException(39);
/*     */     }
/*     */     else {
/* 108 */       throw new Asn1Exception(906);
/* 109 */     }localDerValue2 = localDerValue1.getData().getDerValue();
/* 110 */     if ((localDerValue2.getTag() & 0x1F) == 1) {
/* 111 */       this.msgType = localDerValue2.getData().getBigInteger().intValue();
/* 112 */       if (this.msgType != 20)
/* 113 */         throw new KrbApErrException(40);
/*     */     }
/*     */     else
/*     */     {
/* 117 */       throw new Asn1Exception(906);
/* 118 */     }this.safeBody = KRBSafeBody.parse(localDerValue1.getData(), (byte)2, false);
/* 119 */     this.cksum = Checksum.parse(localDerValue1.getData(), (byte)3, false);
/* 120 */     if (localDerValue1.getData().available() > 0)
/* 121 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 131 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 132 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 133 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.pvno));
/* 134 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1);
/* 135 */     localDerOutputStream1 = new DerOutputStream();
/* 136 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.msgType));
/* 137 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream1);
/* 138 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)2), this.safeBody.asn1Encode());
/* 139 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)3), this.cksum.asn1Encode());
/* 140 */     localDerOutputStream1 = new DerOutputStream();
/* 141 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 142 */     localDerOutputStream2 = new DerOutputStream();
/* 143 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)20), localDerOutputStream1);
/* 144 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KRBSafe
 * JD-Core Version:    0.6.2
 */