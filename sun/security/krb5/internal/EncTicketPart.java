/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EncTicketPart
/*     */ {
/*     */   public TicketFlags flags;
/*     */   public EncryptionKey key;
/*     */   public Realm crealm;
/*     */   public PrincipalName cname;
/*     */   public TransitedEncoding transited;
/*     */   public KerberosTime authtime;
/*     */   public KerberosTime starttime;
/*     */   public KerberosTime endtime;
/*     */   public KerberosTime renewTill;
/*     */   public HostAddresses caddr;
/*     */   public AuthorizationData authorizationData;
/*     */ 
/*     */   public EncTicketPart(TicketFlags paramTicketFlags, EncryptionKey paramEncryptionKey, Realm paramRealm, PrincipalName paramPrincipalName, TransitedEncoding paramTransitedEncoding, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, HostAddresses paramHostAddresses, AuthorizationData paramAuthorizationData)
/*     */   {
/*  90 */     this.flags = paramTicketFlags;
/*  91 */     this.key = paramEncryptionKey;
/*  92 */     this.crealm = paramRealm;
/*  93 */     this.cname = paramPrincipalName;
/*  94 */     this.transited = paramTransitedEncoding;
/*  95 */     this.authtime = paramKerberosTime1;
/*  96 */     this.starttime = paramKerberosTime2;
/*  97 */     this.endtime = paramKerberosTime3;
/*  98 */     this.renewTill = paramKerberosTime4;
/*  99 */     this.caddr = paramHostAddresses;
/* 100 */     this.authorizationData = paramAuthorizationData;
/*     */   }
/*     */ 
/*     */   public EncTicketPart(byte[] paramArrayOfByte) throws Asn1Exception, KrbException, IOException
/*     */   {
/* 105 */     init(new DerValue(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   public EncTicketPart(DerValue paramDerValue) throws Asn1Exception, KrbException, IOException
/*     */   {
/* 110 */     init(paramDerValue);
/*     */   }
/*     */ 
/*     */   private static String getHexBytes(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 123 */     StringBuffer localStringBuffer = new StringBuffer();
/* 124 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/* 126 */       int j = paramArrayOfByte[i] >> 4 & 0xF;
/* 127 */       int k = paramArrayOfByte[i] & 0xF;
/*     */ 
/* 129 */       localStringBuffer.append(Integer.toHexString(j));
/* 130 */       localStringBuffer.append(Integer.toHexString(k));
/* 131 */       localStringBuffer.append(' ');
/*     */     }
/* 133 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void init(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException, RealmException
/*     */   {
/* 140 */     this.renewTill = null;
/* 141 */     this.caddr = null;
/* 142 */     this.authorizationData = null;
/* 143 */     if (((paramDerValue.getTag() & 0x1F) != 3) || (paramDerValue.isApplication() != true) || (paramDerValue.isConstructed() != true))
/*     */     {
/* 146 */       throw new Asn1Exception(906);
/*     */     }
/* 148 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/* 149 */     if (localDerValue.getTag() != 48) {
/* 150 */       throw new Asn1Exception(906);
/*     */     }
/* 152 */     this.flags = TicketFlags.parse(localDerValue.getData(), (byte)0, false);
/* 153 */     this.key = EncryptionKey.parse(localDerValue.getData(), (byte)1, false);
/* 154 */     this.crealm = Realm.parse(localDerValue.getData(), (byte)2, false);
/* 155 */     this.cname = PrincipalName.parse(localDerValue.getData(), (byte)3, false);
/* 156 */     this.transited = TransitedEncoding.parse(localDerValue.getData(), (byte)4, false);
/* 157 */     this.authtime = KerberosTime.parse(localDerValue.getData(), (byte)5, false);
/* 158 */     this.starttime = KerberosTime.parse(localDerValue.getData(), (byte)6, true);
/* 159 */     this.endtime = KerberosTime.parse(localDerValue.getData(), (byte)7, false);
/* 160 */     if (localDerValue.getData().available() > 0) {
/* 161 */       this.renewTill = KerberosTime.parse(localDerValue.getData(), (byte)8, true);
/*     */     }
/* 163 */     if (localDerValue.getData().available() > 0) {
/* 164 */       this.caddr = HostAddresses.parse(localDerValue.getData(), (byte)9, true);
/*     */     }
/* 166 */     if (localDerValue.getData().available() > 0) {
/* 167 */       this.authorizationData = AuthorizationData.parse(localDerValue.getData(), (byte)10, true);
/*     */     }
/* 169 */     if (localDerValue.getData().available() > 0)
/* 170 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 182 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 183 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 184 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), this.flags.asn1Encode());
/*     */ 
/* 186 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), this.key.asn1Encode());
/*     */ 
/* 188 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), this.crealm.asn1Encode());
/*     */ 
/* 190 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), this.cname.asn1Encode());
/*     */ 
/* 192 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)4), this.transited.asn1Encode());
/*     */ 
/* 194 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)5), this.authtime.asn1Encode());
/*     */ 
/* 196 */     if (this.starttime != null) {
/* 197 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)6), this.starttime.asn1Encode());
/*     */     }
/*     */ 
/* 200 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)7), this.endtime.asn1Encode());
/*     */ 
/* 203 */     if (this.renewTill != null) {
/* 204 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)8), this.renewTill.asn1Encode());
/*     */     }
/*     */ 
/* 208 */     if (this.caddr != null) {
/* 209 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)9), this.caddr.asn1Encode());
/*     */     }
/*     */ 
/* 213 */     if (this.authorizationData != null) {
/* 214 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)10), this.authorizationData.asn1Encode());
/*     */     }
/*     */ 
/* 217 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 218 */     localDerOutputStream1 = new DerOutputStream();
/* 219 */     localDerOutputStream1.write(DerValue.createTag((byte)64, true, (byte)3), localDerOutputStream2);
/*     */ 
/* 221 */     return localDerOutputStream1.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncTicketPart
 * JD-Core Version:    0.6.2
 */