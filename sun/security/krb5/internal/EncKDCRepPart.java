/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncKDCRepPart
/*     */ {
/*     */   public EncryptionKey key;
/*     */   public LastReq lastReq;
/*     */   public int nonce;
/*     */   public KerberosTime keyExpiration;
/*     */   public TicketFlags flags;
/*     */   public KerberosTime authtime;
/*     */   public KerberosTime starttime;
/*     */   public KerberosTime endtime;
/*     */   public KerberosTime renewTill;
/*     */   public Realm srealm;
/*     */   public PrincipalName sname;
/*     */   public HostAddresses caddr;
/*     */   public int msgType;
/*     */ 
/*     */   public EncKDCRepPart(EncryptionKey paramEncryptionKey, LastReq paramLastReq, int paramInt1, KerberosTime paramKerberosTime1, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, KerberosTime paramKerberosTime5, Realm paramRealm, PrincipalName paramPrincipalName, HostAddresses paramHostAddresses, int paramInt2)
/*     */   {
/*  96 */     this.key = paramEncryptionKey;
/*  97 */     this.lastReq = paramLastReq;
/*  98 */     this.nonce = paramInt1;
/*  99 */     this.keyExpiration = paramKerberosTime1;
/* 100 */     this.flags = paramTicketFlags;
/* 101 */     this.authtime = paramKerberosTime2;
/* 102 */     this.starttime = paramKerberosTime3;
/* 103 */     this.endtime = paramKerberosTime4;
/* 104 */     this.renewTill = paramKerberosTime5;
/* 105 */     this.srealm = paramRealm;
/* 106 */     this.sname = paramPrincipalName;
/* 107 */     this.caddr = paramHostAddresses;
/* 108 */     this.msgType = paramInt2;
/*     */   }
/*     */ 
/*     */   public EncKDCRepPart()
/*     */   {
/*     */   }
/*     */ 
/*     */   public EncKDCRepPart(byte[] paramArrayOfByte, int paramInt) throws Asn1Exception, IOException, RealmException {
/* 116 */     init(new DerValue(paramArrayOfByte), paramInt);
/*     */   }
/*     */ 
/*     */   public EncKDCRepPart(DerValue paramDerValue, int paramInt) throws Asn1Exception, IOException, RealmException
/*     */   {
/* 121 */     init(paramDerValue, paramInt);
/*     */   }
/*     */ 
/*     */   protected void init(DerValue paramDerValue, int paramInt)
/*     */     throws Asn1Exception, IOException, RealmException
/*     */   {
/* 138 */     this.msgType = (paramDerValue.getTag() & 0x1F);
/* 139 */     if ((this.msgType != 25) && (this.msgType != 26))
/*     */     {
/* 141 */       throw new Asn1Exception(906);
/*     */     }
/* 143 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 144 */     if (localDerValue1.getTag() != 48) {
/* 145 */       throw new Asn1Exception(906);
/*     */     }
/* 147 */     this.key = EncryptionKey.parse(localDerValue1.getData(), (byte)0, false);
/* 148 */     this.lastReq = LastReq.parse(localDerValue1.getData(), (byte)1, false);
/* 149 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 150 */     if ((localDerValue2.getTag() & 0x1F) == 2)
/* 151 */       this.nonce = localDerValue2.getData().getBigInteger().intValue();
/*     */     else {
/* 153 */       throw new Asn1Exception(906);
/*     */     }
/* 155 */     this.keyExpiration = KerberosTime.parse(localDerValue1.getData(), (byte)3, true);
/* 156 */     this.flags = TicketFlags.parse(localDerValue1.getData(), (byte)4, false);
/* 157 */     this.authtime = KerberosTime.parse(localDerValue1.getData(), (byte)5, false);
/* 158 */     this.starttime = KerberosTime.parse(localDerValue1.getData(), (byte)6, true);
/* 159 */     this.endtime = KerberosTime.parse(localDerValue1.getData(), (byte)7, false);
/* 160 */     this.renewTill = KerberosTime.parse(localDerValue1.getData(), (byte)8, true);
/* 161 */     this.srealm = Realm.parse(localDerValue1.getData(), (byte)9, false);
/* 162 */     this.sname = PrincipalName.parse(localDerValue1.getData(), (byte)10, false);
/* 163 */     if (localDerValue1.getData().available() > 0) {
/* 164 */       this.caddr = HostAddresses.parse(localDerValue1.getData(), (byte)11, true);
/*     */     }
/* 166 */     if (localDerValue1.getData().available() > 0)
/* 167 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode(int paramInt)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 180 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 181 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 182 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), this.key.asn1Encode());
/*     */ 
/* 184 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)1), this.lastReq.asn1Encode());
/*     */ 
/* 186 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.nonce));
/* 187 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream1);
/*     */ 
/* 190 */     if (this.keyExpiration != null) {
/* 191 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)3), this.keyExpiration.asn1Encode());
/*     */     }
/*     */ 
/* 194 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)4), this.flags.asn1Encode());
/*     */ 
/* 196 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)5), this.authtime.asn1Encode());
/*     */ 
/* 198 */     if (this.starttime != null) {
/* 199 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)6), this.starttime.asn1Encode());
/*     */     }
/*     */ 
/* 202 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)7), this.endtime.asn1Encode());
/*     */ 
/* 204 */     if (this.renewTill != null) {
/* 205 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)8), this.renewTill.asn1Encode());
/*     */     }
/*     */ 
/* 208 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)9), this.srealm.asn1Encode());
/*     */ 
/* 210 */     localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)10), this.sname.asn1Encode());
/*     */ 
/* 212 */     if (this.caddr != null) {
/* 213 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)11), this.caddr.asn1Encode());
/*     */     }
/*     */ 
/* 219 */     localDerOutputStream1 = new DerOutputStream();
/* 220 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 221 */     localDerOutputStream2 = new DerOutputStream();
/* 222 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)this.msgType), localDerOutputStream1);
/*     */ 
/* 224 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncKDCRepPart
 * JD-Core Version:    0.6.2
 */