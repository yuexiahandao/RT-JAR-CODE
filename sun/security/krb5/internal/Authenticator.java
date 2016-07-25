/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.Checksum;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class Authenticator
/*     */ {
/*     */   public int authenticator_vno;
/*     */   public Realm crealm;
/*     */   public PrincipalName cname;
/*     */   Checksum cksum;
/*     */   public int cusec;
/*     */   public KerberosTime ctime;
/*     */   EncryptionKey subKey;
/*     */   Integer seqNumber;
/*     */   public AuthorizationData authorizationData;
/*     */ 
/*     */   public Authenticator(Realm paramRealm, PrincipalName paramPrincipalName, Checksum paramChecksum, int paramInt, KerberosTime paramKerberosTime, EncryptionKey paramEncryptionKey, Integer paramInteger, AuthorizationData paramAuthorizationData)
/*     */   {
/*  82 */     this.authenticator_vno = 5;
/*  83 */     this.crealm = paramRealm;
/*  84 */     this.cname = paramPrincipalName;
/*  85 */     this.cksum = paramChecksum;
/*  86 */     this.cusec = paramInt;
/*  87 */     this.ctime = paramKerberosTime;
/*  88 */     this.subKey = paramEncryptionKey;
/*  89 */     this.seqNumber = paramInteger;
/*  90 */     this.authorizationData = paramAuthorizationData;
/*     */   }
/*     */ 
/*     */   public Authenticator(byte[] paramArrayOfByte) throws Asn1Exception, IOException, KrbApErrException, RealmException
/*     */   {
/*  95 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public Authenticator(DerValue paramDerValue) throws Asn1Exception, IOException, KrbApErrException, RealmException
/*     */   {
/* 100 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException, KrbApErrException, RealmException
/*     */   {
/* 117 */     if (((paramDerValue.getTag() & 0x1F) != 2) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 120 */       throw new Asn1Exception(906);
/*     */     }
/* 122 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 123 */     if (localDerValue1.getTag() != 48) {
/* 124 */       throw new Asn1Exception(906);
/*     */     }
/* 126 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 127 */     if ((localDerValue2.getTag() & 0x1F) != 0) {
/* 128 */       throw new Asn1Exception(906);
/*     */     }
/* 130 */     this.authenticator_vno = localDerValue2.getData().getBigInteger().intValue();
/* 131 */     if (this.authenticator_vno != 5) {
/* 132 */       throw new KrbApErrException(39);
/*     */     }
/* 134 */     this.crealm = Realm.parse(localDerValue1.getData(), (byte)1, false);
/* 135 */     this.cname = PrincipalName.parse(localDerValue1.getData(), (byte)2, false);
/* 136 */     this.cksum = Checksum.parse(localDerValue1.getData(), (byte)3, true);
/* 137 */     localDerValue2 = localDerValue1.getData().getDerValue();
/* 138 */     if ((localDerValue2.getTag() & 0x1F) == 4)
/* 139 */       this.cusec = localDerValue2.getData().getBigInteger().intValue();
/*     */     else {
/* 141 */       throw new Asn1Exception(906);
/*     */     }
/* 143 */     this.ctime = KerberosTime.parse(localDerValue1.getData(), (byte)5, false);
/* 144 */     if (localDerValue1.getData().available() > 0) {
/* 145 */       this.subKey = EncryptionKey.parse(localDerValue1.getData(), (byte)6, true);
/*     */     } else {
/* 147 */       this.subKey = null;
/* 148 */       this.seqNumber = null;
/* 149 */       this.authorizationData = null;
/*     */     }
/* 151 */     if (localDerValue1.getData().available() > 0) {
/* 152 */       if ((localDerValue1.getData().peekByte() & 0x1F) == 7) {
/* 153 */         localDerValue2 = localDerValue1.getData().getDerValue();
/* 154 */         if ((localDerValue2.getTag() & 0x1F) == 7)
/* 155 */           this.seqNumber = new Integer(localDerValue2.getData().getBigInteger().intValue());
/*     */       }
/*     */     }
/*     */     else {
/* 159 */       this.seqNumber = null;
/* 160 */       this.authorizationData = null;
/*     */     }
/* 162 */     if (localDerValue1.getData().available() > 0)
/* 163 */       this.authorizationData = AuthorizationData.parse(localDerValue1.getData(), (byte)8, true);
/*     */     else {
/* 165 */       this.authorizationData = null;
/*     */     }
/* 167 */     if (localDerValue1.getData().available() > 0)
/* 168 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 179 */     Vector localVector = new Vector();
/* 180 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 181 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.authenticator_vno));
/* 182 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream1.toByteArray()));
/* 183 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)1), this.crealm.asn1Encode()));
/* 184 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)2), this.cname.asn1Encode()));
/* 185 */     if (this.cksum != null) {
/* 186 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)3), this.cksum.asn1Encode()));
/*     */     }
/* 188 */     localDerOutputStream1 = new DerOutputStream();
/* 189 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.cusec));
/* 190 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)4), localDerOutputStream1.toByteArray()));
/* 191 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)5), this.ctime.asn1Encode()));
/* 192 */     if (this.subKey != null) {
/* 193 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)6), this.subKey.asn1Encode()));
/*     */     }
/* 195 */     if (this.seqNumber != null) {
/* 196 */       localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 198 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
/* 199 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)7), localDerOutputStream1.toByteArray()));
/*     */     }
/* 201 */     if (this.authorizationData != null) {
/* 202 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)8), this.authorizationData.asn1Encode()));
/*     */     }
/* 204 */     DerValue[] arrayOfDerValue = new DerValue[localVector.size()];
/* 205 */     localVector.copyInto(arrayOfDerValue);
/* 206 */     localDerOutputStream1 = new DerOutputStream();
/* 207 */     localDerOutputStream1.putSequence(arrayOfDerValue);
/* 208 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 209 */     localDerOutputStream2.write(DerValue.createTag((byte)64, true, (byte)2), localDerOutputStream1);
/* 210 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public final Checksum getChecksum() {
/* 214 */     return this.cksum;
/*     */   }
/*     */ 
/*     */   public final Integer getSeqNumber() {
/* 218 */     return this.seqNumber;
/*     */   }
/*     */ 
/*     */   public final EncryptionKey getSubKey() {
/* 222 */     return this.subKey;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.Authenticator
 * JD-Core Version:    0.6.2
 */