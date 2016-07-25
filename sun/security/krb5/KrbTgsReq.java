/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.UnknownHostException;
/*     */ import sun.security.krb5.internal.APOptions;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCOptions;
/*     */ import sun.security.krb5.internal.KDCReqBody;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.PAData;
/*     */ import sun.security.krb5.internal.TGSReq;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.krb5.internal.crypto.Nonce;
/*     */ 
/*     */ public class KrbTgsReq
/*     */ {
/*     */   private PrincipalName princName;
/*     */   private PrincipalName servName;
/*     */   private TGSReq tgsReqMessg;
/*     */   private KerberosTime ctime;
/*  49 */   private Ticket secondTicket = null;
/*  50 */   private boolean useSubkey = false;
/*     */   EncryptionKey tgsReqKey;
/*  53 */   private static final boolean DEBUG = Krb5.DEBUG;
/*     */   private byte[] obuf;
/*     */   private byte[] ibuf;
/*     */ 
/*     */   public KrbTgsReq(Credentials paramCredentials, PrincipalName paramPrincipalName)
/*     */     throws KrbException, IOException
/*     */   {
/*  62 */     this(new KDCOptions(), paramCredentials, paramPrincipalName, null, null, null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */   KrbTgsReq(KDCOptions paramKDCOptions, Credentials paramCredentials, PrincipalName paramPrincipalName, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, int[] paramArrayOfInt, HostAddresses paramHostAddresses, AuthorizationData paramAuthorizationData, Ticket[] paramArrayOfTicket, EncryptionKey paramEncryptionKey)
/*     */     throws KrbException, IOException
/*     */   {
/*  89 */     this.princName = paramCredentials.client;
/*  90 */     this.servName = paramPrincipalName;
/*  91 */     this.ctime = new KerberosTime(true);
/*     */ 
/*  96 */     if ((paramKDCOptions.get(1)) && (!paramCredentials.flags.get(1)))
/*     */     {
/*  98 */       throw new KrbException(101);
/*     */     }
/* 100 */     if ((paramKDCOptions.get(2)) && 
/* 101 */       (!paramCredentials.flags.get(1))) {
/* 102 */       throw new KrbException(101);
/*     */     }
/* 104 */     if ((paramKDCOptions.get(3)) && (!paramCredentials.flags.get(3)))
/*     */     {
/* 106 */       throw new KrbException(101);
/*     */     }
/* 108 */     if ((paramKDCOptions.get(4)) && 
/* 109 */       (!paramCredentials.flags.get(3))) {
/* 110 */       throw new KrbException(101);
/*     */     }
/* 112 */     if ((paramKDCOptions.get(5)) && (!paramCredentials.flags.get(5)))
/*     */     {
/* 114 */       throw new KrbException(101);
/*     */     }
/* 116 */     if ((paramKDCOptions.get(8)) && (!paramCredentials.flags.get(8)))
/*     */     {
/* 118 */       throw new KrbException(101);
/*     */     }
/*     */ 
/* 121 */     if (paramKDCOptions.get(6)) {
/* 122 */       if (!paramCredentials.flags.get(6))
/* 123 */         throw new KrbException(101);
/*     */     }
/* 125 */     else if (paramKerberosTime1 != null) paramKerberosTime1 = null;
/*     */ 
/* 127 */     if (paramKDCOptions.get(8)) {
/* 128 */       if (!paramCredentials.flags.get(8))
/* 129 */         throw new KrbException(101);
/*     */     }
/* 131 */     else if (paramKerberosTime3 != null) paramKerberosTime3 = null;
/*     */ 
/* 133 */     if (paramKDCOptions.get(28)) {
/* 134 */       if (paramArrayOfTicket == null) {
/* 135 */         throw new KrbException(101);
/*     */       }
/*     */ 
/* 139 */       this.secondTicket = paramArrayOfTicket[0];
/*     */     }
/* 141 */     else if (paramArrayOfTicket != null) {
/* 142 */       paramArrayOfTicket = null;
/*     */     }
/*     */ 
/* 145 */     this.tgsReqMessg = createRequest(paramKDCOptions, paramCredentials.ticket, paramCredentials.key, this.ctime, this.princName, this.princName.getRealm(), this.servName, paramKerberosTime1, paramKerberosTime2, paramKerberosTime3, paramArrayOfInt, paramHostAddresses, paramAuthorizationData, paramArrayOfTicket, paramEncryptionKey);
/*     */ 
/* 161 */     this.obuf = this.tgsReqMessg.asn1Encode();
/*     */ 
/* 171 */     if (paramCredentials.flags.get(2))
/* 172 */       paramKDCOptions.set(2, true);
/*     */   }
/*     */ 
/*     */   public void send()
/*     */     throws IOException, KrbException
/*     */   {
/* 183 */     String str = null;
/* 184 */     if (this.servName != null)
/* 185 */       str = this.servName.getRealmString();
/* 186 */     KdcComm localKdcComm = new KdcComm(str);
/* 187 */     this.ibuf = localKdcComm.send(this.obuf);
/*     */   }
/*     */ 
/*     */   public KrbTgsRep getReply() throws KrbException, IOException
/*     */   {
/* 192 */     return new KrbTgsRep(this.ibuf, this);
/*     */   }
/*     */ 
/*     */   public Credentials sendAndGetCreds()
/*     */     throws IOException, KrbException
/*     */   {
/* 200 */     KrbTgsRep localKrbTgsRep = null;
/* 201 */     Object localObject = null;
/* 202 */     send();
/* 203 */     localKrbTgsRep = getReply();
/* 204 */     return localKrbTgsRep.getCreds();
/*     */   }
/*     */ 
/*     */   KerberosTime getCtime() {
/* 208 */     return this.ctime;
/*     */   }
/*     */ 
/*     */   private TGSReq createRequest(KDCOptions paramKDCOptions, Ticket paramTicket, EncryptionKey paramEncryptionKey1, KerberosTime paramKerberosTime1, PrincipalName paramPrincipalName1, Realm paramRealm, PrincipalName paramPrincipalName2, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, int[] paramArrayOfInt, HostAddresses paramHostAddresses, AuthorizationData paramAuthorizationData, Ticket[] paramArrayOfTicket, EncryptionKey paramEncryptionKey2)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbApErrException, UnknownHostException, KrbCryptoException
/*     */   {
/* 229 */     KerberosTime localKerberosTime = null;
/* 230 */     if (paramKerberosTime3 == null)
/* 231 */       localKerberosTime = new KerberosTime(0L);
/*     */     else {
/* 233 */       localKerberosTime = paramKerberosTime3;
/*     */     }
/*     */ 
/* 245 */     this.tgsReqKey = paramEncryptionKey1;
/*     */ 
/* 247 */     int[] arrayOfInt = null;
/* 248 */     if (paramArrayOfInt == null) {
/* 249 */       arrayOfInt = EType.getDefaults("default_tgs_enctypes");
/* 250 */       if (arrayOfInt == null)
/* 251 */         throw new KrbCryptoException("No supported encryption types listed in default_tgs_enctypes");
/*     */     }
/*     */     else
/*     */     {
/* 255 */       arrayOfInt = paramArrayOfInt;
/*     */     }
/*     */ 
/* 258 */     EncryptionKey localEncryptionKey = null;
/* 259 */     EncryptedData localEncryptedData = null;
/* 260 */     if (paramAuthorizationData != null) {
/* 261 */       localObject = paramAuthorizationData.asn1Encode();
/* 262 */       if (paramEncryptionKey2 != null) {
/* 263 */         localEncryptionKey = paramEncryptionKey2;
/* 264 */         this.tgsReqKey = paramEncryptionKey2;
/* 265 */         this.useSubkey = true;
/* 266 */         localEncryptedData = new EncryptedData(localEncryptionKey, (byte[])localObject, 5);
/*     */       }
/*     */       else {
/* 269 */         localEncryptedData = new EncryptedData(paramEncryptionKey1, (byte[])localObject, 4);
/*     */       }
/*     */     }
/*     */ 
/* 273 */     Object localObject = new KDCReqBody(paramKDCOptions, paramPrincipalName1, paramPrincipalName2.getRealm(), paramPrincipalName2, paramKerberosTime2, localKerberosTime, paramKerberosTime4, Nonce.value(), arrayOfInt, paramHostAddresses, localEncryptedData, paramArrayOfTicket);
/*     */ 
/* 288 */     byte[] arrayOfByte1 = ((KDCReqBody)localObject).asn1Encode(12);
/*     */     Checksum localChecksum;
/* 292 */     switch (Checksum.CKSUMTYPE_DEFAULT) {
/*     */     case -138:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 12:
/*     */     case 15:
/*     */     case 16:
/* 302 */       localChecksum = new Checksum(Checksum.CKSUMTYPE_DEFAULT, arrayOfByte1, paramEncryptionKey1, 6);
/*     */ 
/* 304 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 7:
/*     */     default:
/* 309 */       localChecksum = new Checksum(Checksum.CKSUMTYPE_DEFAULT, arrayOfByte1);
/*     */     }
/*     */ 
/* 314 */     byte[] arrayOfByte2 = new KrbApReq(new APOptions(), paramTicket, paramEncryptionKey1, paramRealm, paramPrincipalName1, localChecksum, paramKerberosTime1, localEncryptionKey, null, null).getMessage();
/*     */ 
/* 326 */     PAData[] arrayOfPAData = new PAData[1];
/* 327 */     arrayOfPAData[0] = new PAData(1, arrayOfByte2);
/*     */ 
/* 329 */     return new TGSReq(arrayOfPAData, (KDCReqBody)localObject);
/*     */   }
/*     */ 
/*     */   TGSReq getMessage() {
/* 333 */     return this.tgsReqMessg;
/*     */   }
/*     */ 
/*     */   Ticket getSecondTicket() {
/* 337 */     return this.secondTicket;
/*     */   }
/*     */ 
/*     */   private static void debug(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   boolean usedSubkey() {
/* 345 */     return this.useSubkey;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbTgsReq
 * JD-Core Version:    0.6.2
 */