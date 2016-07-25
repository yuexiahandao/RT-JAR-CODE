/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptedData;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KDCReqBody
/*     */ {
/*     */   public KDCOptions kdcOptions;
/*     */   public PrincipalName cname;
/*     */   public Realm crealm;
/*     */   public PrincipalName sname;
/*     */   public KerberosTime from;
/*     */   public KerberosTime till;
/*     */   public KerberosTime rtime;
/*     */   public HostAddresses addresses;
/*     */   private int nonce;
/*  83 */   private int[] eType = null;
/*     */   private EncryptedData encAuthorizationData;
/*     */   private Ticket[] additionalTickets;
/*     */ 
/*     */   public KDCReqBody(KDCOptions paramKDCOptions, PrincipalName paramPrincipalName1, Realm paramRealm, PrincipalName paramPrincipalName2, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, int paramInt, int[] paramArrayOfInt, HostAddresses paramHostAddresses, EncryptedData paramEncryptedData, Ticket[] paramArrayOfTicket)
/*     */     throws IOException
/*     */   {
/* 101 */     this.kdcOptions = paramKDCOptions;
/* 102 */     this.cname = paramPrincipalName1;
/* 103 */     this.crealm = paramRealm;
/* 104 */     this.sname = paramPrincipalName2;
/* 105 */     this.from = paramKerberosTime1;
/* 106 */     this.till = paramKerberosTime2;
/* 107 */     this.rtime = paramKerberosTime3;
/* 108 */     this.nonce = paramInt;
/* 109 */     if (paramArrayOfInt != null) {
/* 110 */       this.eType = ((int[])paramArrayOfInt.clone());
/*     */     }
/* 112 */     this.addresses = paramHostAddresses;
/* 113 */     this.encAuthorizationData = paramEncryptedData;
/* 114 */     if (paramArrayOfTicket != null) {
/* 115 */       this.additionalTickets = new Ticket[paramArrayOfTicket.length];
/* 116 */       for (int i = 0; i < paramArrayOfTicket.length; i++) {
/* 117 */         if (paramArrayOfTicket[i] == null) {
/* 118 */           throw new IOException("Cannot create a KDCReqBody");
/*     */         }
/* 120 */         this.additionalTickets[i] = ((Ticket)paramArrayOfTicket[i].clone());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public KDCReqBody(DerValue paramDerValue, int paramInt)
/*     */     throws Asn1Exception, RealmException, KrbException, IOException
/*     */   {
/* 138 */     this.addresses = null;
/* 139 */     this.encAuthorizationData = null;
/* 140 */     this.additionalTickets = null;
/* 141 */     if (paramDerValue.getTag() != 48) {
/* 142 */       throw new Asn1Exception(906);
/*     */     }
/* 144 */     this.kdcOptions = KDCOptions.parse(paramDerValue.getData(), (byte)0, false);
/* 145 */     this.cname = PrincipalName.parse(paramDerValue.getData(), (byte)1, true);
/* 146 */     if ((paramInt != 10) && (this.cname != null)) {
/* 147 */       throw new Asn1Exception(906);
/*     */     }
/* 149 */     this.crealm = Realm.parse(paramDerValue.getData(), (byte)2, false);
/* 150 */     this.sname = PrincipalName.parse(paramDerValue.getData(), (byte)3, true);
/* 151 */     this.from = KerberosTime.parse(paramDerValue.getData(), (byte)4, true);
/* 152 */     this.till = KerberosTime.parse(paramDerValue.getData(), (byte)5, false);
/* 153 */     this.rtime = KerberosTime.parse(paramDerValue.getData(), (byte)6, true);
/* 154 */     DerValue localDerValue1 = paramDerValue.getData().getDerValue();
/* 155 */     if ((localDerValue1.getTag() & 0x1F) == 7)
/* 156 */       this.nonce = localDerValue1.getData().getBigInteger().intValue();
/*     */     else {
/* 158 */       throw new Asn1Exception(906);
/*     */     }
/* 160 */     localDerValue1 = paramDerValue.getData().getDerValue();
/* 161 */     Vector localVector1 = new Vector();
/*     */     DerValue localDerValue2;
/* 162 */     if ((localDerValue1.getTag() & 0x1F) == 8) {
/* 163 */       localDerValue2 = localDerValue1.getData().getDerValue();
/*     */ 
/* 165 */       if (localDerValue2.getTag() == 48) {
/* 166 */         while (localDerValue2.getData().available() > 0) {
/* 167 */           localVector1.addElement(Integer.valueOf(localDerValue2.getData().getBigInteger().intValue()));
/*     */         }
/* 169 */         this.eType = new int[localVector1.size()];
/* 170 */         for (int i = 0; i < localVector1.size(); i++)
/* 171 */           this.eType[i] = ((Integer)localVector1.elementAt(i)).intValue();
/*     */       }
/*     */       else {
/* 174 */         throw new Asn1Exception(906);
/*     */       }
/*     */     } else {
/* 177 */       throw new Asn1Exception(906);
/*     */     }
/* 179 */     if (paramDerValue.getData().available() > 0) {
/* 180 */       this.addresses = HostAddresses.parse(paramDerValue.getData(), (byte)9, true);
/*     */     }
/* 182 */     if (paramDerValue.getData().available() > 0) {
/* 183 */       this.encAuthorizationData = EncryptedData.parse(paramDerValue.getData(), (byte)10, true);
/*     */     }
/* 185 */     if (paramDerValue.getData().available() > 0) {
/* 186 */       Vector localVector2 = new Vector();
/* 187 */       localDerValue1 = paramDerValue.getData().getDerValue();
/* 188 */       if ((localDerValue1.getTag() & 0x1F) == 11) {
/* 189 */         localDerValue2 = localDerValue1.getData().getDerValue();
/* 190 */         if (localDerValue2.getTag() == 48) {
/* 191 */           while (localDerValue2.getData().available() > 0) {
/* 192 */             localVector2.addElement(new Ticket(localDerValue2.getData().getDerValue()));
/*     */           }
/*     */         }
/* 195 */         throw new Asn1Exception(906);
/*     */ 
/* 197 */         if (localVector2.size() > 0) {
/* 198 */           this.additionalTickets = new Ticket[localVector2.size()];
/* 199 */           localVector2.copyInto(this.additionalTickets);
/*     */         }
/*     */       } else {
/* 202 */         throw new Asn1Exception(906);
/*     */       }
/*     */     }
/* 205 */     if (paramDerValue.getData().available() > 0)
/* 206 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode(int paramInt)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 219 */     Vector localVector = new Vector();
/* 220 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)0), this.kdcOptions.asn1Encode()));
/* 221 */     if ((paramInt == 10) && 
/* 222 */       (this.cname != null)) {
/* 223 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)1), this.cname.asn1Encode()));
/*     */     }
/*     */ 
/* 226 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)2), this.crealm.asn1Encode()));
/* 227 */     if (this.sname != null) {
/* 228 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)3), this.sname.asn1Encode()));
/*     */     }
/* 230 */     if (this.from != null) {
/* 231 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)4), this.from.asn1Encode()));
/*     */     }
/* 233 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)5), this.till.asn1Encode()));
/* 234 */     if (this.rtime != null) {
/* 235 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)6), this.rtime.asn1Encode()));
/*     */     }
/* 237 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 238 */     localDerOutputStream1.putInteger(BigInteger.valueOf(this.nonce));
/* 239 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)7), localDerOutputStream1.toByteArray()));
/*     */ 
/* 241 */     localDerOutputStream1 = new DerOutputStream();
/* 242 */     for (int i = 0; i < this.eType.length; i++) {
/* 243 */       localDerOutputStream1.putInteger(BigInteger.valueOf(this.eType[i]));
/*     */     }
/* 245 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 246 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 247 */     localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)8), localDerOutputStream2.toByteArray()));
/* 248 */     if (this.addresses != null) {
/* 249 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)9), this.addresses.asn1Encode()));
/*     */     }
/* 251 */     if (this.encAuthorizationData != null) {
/* 252 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)10), this.encAuthorizationData.asn1Encode()));
/*     */     }
/* 254 */     if ((this.additionalTickets != null) && (this.additionalTickets.length > 0)) {
/* 255 */       localDerOutputStream1 = new DerOutputStream();
/* 256 */       for (int j = 0; j < this.additionalTickets.length; j++) {
/* 257 */         localDerOutputStream1.write(this.additionalTickets[j].asn1Encode());
/*     */       }
/* 259 */       localObject = new DerOutputStream();
/* 260 */       ((DerOutputStream)localObject).write((byte)48, localDerOutputStream1);
/* 261 */       localVector.addElement(new DerValue(DerValue.createTag((byte)-128, true, (byte)11), ((DerOutputStream)localObject).toByteArray()));
/*     */     }
/* 263 */     Object localObject = new DerValue[localVector.size()];
/* 264 */     localVector.copyInto((Object[])localObject);
/* 265 */     localDerOutputStream1 = new DerOutputStream();
/* 266 */     localDerOutputStream1.putSequence((DerValue[])localObject);
/* 267 */     return localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getNonce() {
/* 271 */     return this.nonce;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KDCReqBody
 * JD-Core Version:    0.6.2
 */