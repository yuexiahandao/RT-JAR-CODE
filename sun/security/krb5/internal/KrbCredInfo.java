/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KrbCredInfo
/*     */ {
/*     */   public EncryptionKey key;
/*     */   public Realm prealm;
/*     */   public PrincipalName pname;
/*     */   public TicketFlags flags;
/*     */   public KerberosTime authtime;
/*     */   public KerberosTime starttime;
/*     */   public KerberosTime endtime;
/*     */   public KerberosTime renewTill;
/*     */   public Realm srealm;
/*     */   public PrincipalName sname;
/*     */   public HostAddresses caddr;
/*     */ 
/*     */   private KrbCredInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public KrbCredInfo(EncryptionKey paramEncryptionKey, Realm paramRealm1, PrincipalName paramPrincipalName1, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, Realm paramRealm2, PrincipalName paramPrincipalName2, HostAddresses paramHostAddresses)
/*     */   {
/*  93 */     this.key = paramEncryptionKey;
/*  94 */     this.prealm = paramRealm1;
/*  95 */     this.pname = paramPrincipalName1;
/*  96 */     this.flags = paramTicketFlags;
/*  97 */     this.authtime = paramKerberosTime1;
/*  98 */     this.starttime = paramKerberosTime2;
/*  99 */     this.endtime = paramKerberosTime3;
/* 100 */     this.renewTill = paramKerberosTime4;
/* 101 */     this.srealm = paramRealm2;
/* 102 */     this.sname = paramPrincipalName2;
/* 103 */     this.caddr = paramHostAddresses;
/*     */   }
/*     */ 
/*     */   public KrbCredInfo(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException, RealmException
/*     */   {
/* 115 */     if (paramDerValue.getTag() != 48) {
/* 116 */       throw new Asn1Exception(906);
/*     */     }
/* 118 */     this.prealm = null;
/* 119 */     this.pname = null;
/* 120 */     this.flags = null;
/* 121 */     this.authtime = null;
/* 122 */     this.starttime = null;
/* 123 */     this.endtime = null;
/* 124 */     this.renewTill = null;
/* 125 */     this.srealm = null;
/* 126 */     this.sname = null;
/* 127 */     this.caddr = null;
/* 128 */     this.key = EncryptionKey.parse(paramDerValue.getData(), (byte)0, false);
/* 129 */     if (paramDerValue.getData().available() > 0)
/* 130 */       this.prealm = Realm.parse(paramDerValue.getData(), (byte)1, true);
/* 131 */     if (paramDerValue.getData().available() > 0)
/* 132 */       this.pname = PrincipalName.parse(paramDerValue.getData(), (byte)2, true);
/* 133 */     if (paramDerValue.getData().available() > 0)
/* 134 */       this.flags = TicketFlags.parse(paramDerValue.getData(), (byte)3, true);
/* 135 */     if (paramDerValue.getData().available() > 0)
/* 136 */       this.authtime = KerberosTime.parse(paramDerValue.getData(), (byte)4, true);
/* 137 */     if (paramDerValue.getData().available() > 0)
/* 138 */       this.starttime = KerberosTime.parse(paramDerValue.getData(), (byte)5, true);
/* 139 */     if (paramDerValue.getData().available() > 0)
/* 140 */       this.endtime = KerberosTime.parse(paramDerValue.getData(), (byte)6, true);
/* 141 */     if (paramDerValue.getData().available() > 0)
/* 142 */       this.renewTill = KerberosTime.parse(paramDerValue.getData(), (byte)7, true);
/* 143 */     if (paramDerValue.getData().available() > 0)
/* 144 */       this.srealm = Realm.parse(paramDerValue.getData(), (byte)8, true);
/* 145 */     if (paramDerValue.getData().available() > 0)
/* 146 */       this.sname = PrincipalName.parse(paramDerValue.getData(), (byte)9, true);
/* 147 */     if (paramDerValue.getData().available() > 0)
/* 148 */       this.caddr = HostAddresses.parse(paramDerValue.getData(), (byte)10, true);
/* 149 */     if (paramDerValue.getData().available() > 0)
/* 150 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 160 */     Vector localVector = new Vector();
/* 161 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)0), this.key.asn1Encode()));
/* 162 */     if (this.prealm != null)
/* 163 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)1), this.prealm.asn1Encode()));
/* 164 */     if (this.pname != null)
/* 165 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)2), this.pname.asn1Encode()));
/* 166 */     if (this.flags != null)
/* 167 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)3), this.flags.asn1Encode()));
/* 168 */     if (this.authtime != null)
/* 169 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)4), this.authtime.asn1Encode()));
/* 170 */     if (this.starttime != null)
/* 171 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)5), this.starttime.asn1Encode()));
/* 172 */     if (this.endtime != null)
/* 173 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)6), this.endtime.asn1Encode()));
/* 174 */     if (this.renewTill != null)
/* 175 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)7), this.renewTill.asn1Encode()));
/* 176 */     if (this.srealm != null)
/* 177 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)8), this.srealm.asn1Encode()));
/* 178 */     if (this.sname != null)
/* 179 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)9), this.sname.asn1Encode()));
/* 180 */     if (this.caddr != null)
/* 181 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)10), this.caddr.asn1Encode()));
/* 182 */     DerValue[] arrayOfDerValue = new DerValue[localVector.size()];
/* 183 */     localVector.copyInto(arrayOfDerValue);
/* 184 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 185 */     localDerOutputStream.putSequence(arrayOfDerValue);
/* 186 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 190 */     KrbCredInfo localKrbCredInfo = new KrbCredInfo();
/* 191 */     localKrbCredInfo.key = ((EncryptionKey)this.key.clone());
/*     */ 
/* 193 */     if (this.prealm != null)
/* 194 */       localKrbCredInfo.prealm = ((Realm)this.prealm.clone());
/* 195 */     if (this.pname != null)
/* 196 */       localKrbCredInfo.pname = ((PrincipalName)this.pname.clone());
/* 197 */     if (this.flags != null)
/* 198 */       localKrbCredInfo.flags = ((TicketFlags)this.flags.clone());
/* 199 */     if (this.authtime != null)
/* 200 */       localKrbCredInfo.authtime = ((KerberosTime)this.authtime.clone());
/* 201 */     if (this.starttime != null)
/* 202 */       localKrbCredInfo.starttime = ((KerberosTime)this.starttime.clone());
/* 203 */     if (this.endtime != null)
/* 204 */       localKrbCredInfo.endtime = ((KerberosTime)this.endtime.clone());
/* 205 */     if (this.renewTill != null)
/* 206 */       localKrbCredInfo.renewTill = ((KerberosTime)this.renewTill.clone());
/* 207 */     if (this.srealm != null)
/* 208 */       localKrbCredInfo.srealm = ((Realm)this.srealm.clone());
/* 209 */     if (this.sname != null)
/* 210 */       localKrbCredInfo.sname = ((PrincipalName)this.sname.clone());
/* 211 */     if (this.caddr != null)
/* 212 */       localKrbCredInfo.caddr = ((HostAddresses)this.caddr.clone());
/* 213 */     return localKrbCredInfo;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KrbCredInfo
 * JD-Core Version:    0.6.2
 */