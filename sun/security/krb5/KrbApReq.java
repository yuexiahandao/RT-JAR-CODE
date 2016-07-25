/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ import sun.security.krb5.internal.APOptions;
/*     */ import sun.security.krb5.internal.APReq;
/*     */ import sun.security.krb5.internal.Authenticator;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.EncTicketPart;
/*     */ import sun.security.krb5.internal.HostAddress;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.LocalSeqNumber;
/*     */ import sun.security.krb5.internal.SeqNumber;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.krb5.internal.rcache.AuthTime;
/*     */ import sun.security.krb5.internal.rcache.CacheTable;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KrbApReq
/*     */ {
/*     */   private byte[] obuf;
/*     */   private KerberosTime ctime;
/*     */   private int cusec;
/*     */   private Authenticator authenticator;
/*     */   private Credentials creds;
/*     */   private APReq apReqMessg;
/*  55 */   private static CacheTable table = new CacheTable();
/*  56 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public KrbApReq(Credentials paramCredentials, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Checksum paramChecksum)
/*     */     throws Asn1Exception, KrbCryptoException, KrbException, IOException
/*     */   {
/* 104 */     APOptions localAPOptions = paramBoolean1 ? new APOptions(2) : new APOptions();
/*     */ 
/* 107 */     if (DEBUG) {
/* 108 */       System.out.println(">>> KrbApReq: APOptions are " + localAPOptions);
/*     */     }
/* 110 */     EncryptionKey localEncryptionKey = paramBoolean2 ? new EncryptionKey(paramCredentials.getSessionKey()) : null;
/*     */ 
/* 114 */     LocalSeqNumber localLocalSeqNumber = new LocalSeqNumber();
/*     */ 
/* 116 */     init(localAPOptions, paramCredentials, paramChecksum, localEncryptionKey, localLocalSeqNumber, null, 11);
/*     */   }
/*     */ 
/*     */   public KrbApReq(byte[] paramArrayOfByte, EncryptionKey[] paramArrayOfEncryptionKey, InetAddress paramInetAddress)
/*     */     throws KrbException, IOException
/*     */   {
/* 141 */     this.obuf = paramArrayOfByte;
/* 142 */     if (this.apReqMessg == null)
/* 143 */       decode();
/* 144 */     authenticate(paramArrayOfEncryptionKey, paramInetAddress);
/*     */   }
/*     */ 
/*     */   KrbApReq(APOptions paramAPOptions, Ticket paramTicket, EncryptionKey paramEncryptionKey1, Realm paramRealm, PrincipalName paramPrincipalName, Checksum paramChecksum, KerberosTime paramKerberosTime, EncryptionKey paramEncryptionKey2, SeqNumber paramSeqNumber, AuthorizationData paramAuthorizationData)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbCryptoException
/*     */   {
/* 192 */     init(paramAPOptions, paramTicket, paramEncryptionKey1, paramRealm, paramPrincipalName, paramChecksum, paramKerberosTime, paramEncryptionKey2, paramSeqNumber, paramAuthorizationData, 7);
/*     */   }
/*     */ 
/*     */   private void init(APOptions paramAPOptions, Credentials paramCredentials, Checksum paramChecksum, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber, AuthorizationData paramAuthorizationData, int paramInt)
/*     */     throws KrbException, IOException
/*     */   {
/* 207 */     this.ctime = new KerberosTime(true);
/* 208 */     init(paramAPOptions, paramCredentials.ticket, paramCredentials.key, paramCredentials.client.getRealm(), paramCredentials.client, paramChecksum, this.ctime, paramEncryptionKey, paramSeqNumber, paramAuthorizationData, paramInt);
/*     */   }
/*     */ 
/*     */   private void init(APOptions paramAPOptions, Ticket paramTicket, EncryptionKey paramEncryptionKey1, Realm paramRealm, PrincipalName paramPrincipalName, Checksum paramChecksum, KerberosTime paramKerberosTime, EncryptionKey paramEncryptionKey2, SeqNumber paramSeqNumber, AuthorizationData paramAuthorizationData, int paramInt)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbCryptoException
/*     */   {
/* 235 */     createMessage(paramAPOptions, paramTicket, paramEncryptionKey1, paramRealm, paramPrincipalName, paramChecksum, paramKerberosTime, paramEncryptionKey2, paramSeqNumber, paramAuthorizationData, paramInt);
/*     */ 
/* 238 */     this.obuf = this.apReqMessg.asn1Encode();
/*     */   }
/*     */ 
/*     */   void decode() throws KrbException, IOException
/*     */   {
/* 243 */     DerValue localDerValue = new DerValue(this.obuf);
/* 244 */     decode(localDerValue);
/*     */   }
/*     */ 
/*     */   void decode(DerValue paramDerValue) throws KrbException, IOException {
/* 248 */     this.apReqMessg = null;
/*     */     try {
/* 250 */       this.apReqMessg = new APReq(paramDerValue);
/*     */     } catch (Asn1Exception localAsn1Exception) {
/* 252 */       this.apReqMessg = null;
/* 253 */       KRBError localKRBError = new KRBError(paramDerValue);
/* 254 */       String str1 = localKRBError.getErrorString();
/*     */       String str2;
/* 256 */       if (str1.charAt(str1.length() - 1) == 0)
/* 257 */         str2 = str1.substring(0, str1.length() - 1);
/*     */       else
/* 259 */         str2 = str1;
/* 260 */       KrbException localKrbException = new KrbException(localKRBError.getErrorCode(), str2);
/* 261 */       localKrbException.initCause(localAsn1Exception);
/* 262 */       throw localKrbException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void authenticate(EncryptionKey[] paramArrayOfEncryptionKey, InetAddress paramInetAddress) throws KrbException, IOException
/*     */   {
/* 268 */     int i = this.apReqMessg.ticket.encPart.getEType();
/* 269 */     Integer localInteger = this.apReqMessg.ticket.encPart.getKeyVersionNumber();
/* 270 */     EncryptionKey localEncryptionKey = EncryptionKey.findKey(i, localInteger, paramArrayOfEncryptionKey);
/*     */ 
/* 272 */     if (localEncryptionKey == null) {
/* 273 */       throw new KrbException(400, "Cannot find key of appropriate type to decrypt AP REP - " + EType.toString(i));
/*     */     }
/*     */ 
/* 278 */     byte[] arrayOfByte1 = this.apReqMessg.ticket.encPart.decrypt(localEncryptionKey, 2);
/*     */ 
/* 280 */     byte[] arrayOfByte2 = this.apReqMessg.ticket.encPart.reset(arrayOfByte1);
/* 281 */     EncTicketPart localEncTicketPart = new EncTicketPart(arrayOfByte2);
/*     */ 
/* 283 */     checkPermittedEType(localEncTicketPart.key.getEType());
/*     */ 
/* 285 */     byte[] arrayOfByte3 = this.apReqMessg.authenticator.decrypt(localEncTicketPart.key, 11);
/*     */ 
/* 287 */     byte[] arrayOfByte4 = this.apReqMessg.authenticator.reset(arrayOfByte3);
/* 288 */     this.authenticator = new Authenticator(arrayOfByte4);
/* 289 */     this.ctime = this.authenticator.ctime;
/* 290 */     this.cusec = this.authenticator.cusec;
/* 291 */     this.authenticator.ctime.setMicroSeconds(this.authenticator.cusec);
/* 292 */     this.authenticator.cname.setRealm(this.authenticator.crealm);
/* 293 */     this.apReqMessg.ticket.sname.setRealm(this.apReqMessg.ticket.realm);
/* 294 */     localEncTicketPart.cname.setRealm(localEncTicketPart.crealm);
/*     */ 
/* 296 */     if (!this.authenticator.cname.equals(localEncTicketPart.cname)) {
/* 297 */       throw new KrbApErrException(36);
/*     */     }
/* 299 */     KerberosTime localKerberosTime = new KerberosTime(true);
/* 300 */     if (!this.authenticator.ctime.inClockSkew(localKerberosTime)) {
/* 301 */       throw new KrbApErrException(37);
/*     */     }
/*     */ 
/* 304 */     AuthTime localAuthTime = new AuthTime(this.authenticator.ctime.getTime(), this.authenticator.cusec);
/*     */ 
/* 306 */     String str = this.authenticator.cname.toString();
/* 307 */     if (table.get(localAuthTime, this.authenticator.cname.toString()) != null) {
/* 308 */       throw new KrbApErrException(34);
/*     */     }
/* 310 */     table.put(str, localAuthTime, localKerberosTime.getTime());
/*     */ 
/* 313 */     if (paramInetAddress != null)
/*     */     {
/* 315 */       localObject = new HostAddress(paramInetAddress);
/* 316 */       if ((localEncTicketPart.caddr != null) && (!localEncTicketPart.caddr.inList((HostAddress)localObject)))
/*     */       {
/* 318 */         if (DEBUG) {
/* 319 */           System.out.println(">>> KrbApReq: initiator is " + ((HostAddress)localObject).getInetAddress() + ", but caddr is " + Arrays.toString(localEncTicketPart.caddr.getInetAddresses()));
/*     */         }
/*     */ 
/* 325 */         throw new KrbApErrException(38);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 335 */     Object localObject = new KerberosTime(true);
/*     */ 
/* 337 */     if (((localEncTicketPart.starttime != null) && (localEncTicketPart.starttime.greaterThanWRTClockSkew((KerberosTime)localObject))) || (localEncTicketPart.flags.get(7)))
/*     */     {
/* 340 */       throw new KrbApErrException(33);
/*     */     }
/*     */ 
/* 344 */     if ((localEncTicketPart.endtime != null) && (((KerberosTime)localObject).greaterThanWRTClockSkew(localEncTicketPart.endtime)))
/*     */     {
/* 346 */       throw new KrbApErrException(32);
/*     */     }
/*     */ 
/* 349 */     this.creds = new Credentials(this.apReqMessg.ticket, this.authenticator.cname, this.apReqMessg.ticket.sname, localEncTicketPart.key, localEncTicketPart.flags, localEncTicketPart.authtime, localEncTicketPart.starttime, localEncTicketPart.endtime, localEncTicketPart.renewTill, localEncTicketPart.caddr, localEncTicketPart.authorizationData);
/*     */ 
/* 361 */     if (DEBUG)
/* 362 */       System.out.println(">>> KrbApReq: authenticate succeed.");
/*     */   }
/*     */ 
/*     */   public Credentials getCreds()
/*     */   {
/* 371 */     return this.creds;
/*     */   }
/*     */ 
/*     */   KerberosTime getCtime() {
/* 375 */     if (this.ctime != null)
/* 376 */       return this.ctime;
/* 377 */     return this.authenticator.ctime;
/*     */   }
/*     */ 
/*     */   int cusec() {
/* 381 */     return this.cusec;
/*     */   }
/*     */ 
/*     */   APOptions getAPOptions() throws KrbException, IOException {
/* 385 */     if (this.apReqMessg == null)
/* 386 */       decode();
/* 387 */     if (this.apReqMessg != null)
/* 388 */       return this.apReqMessg.apOptions;
/* 389 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getMutualAuthRequired()
/*     */     throws KrbException, IOException
/*     */   {
/* 399 */     if (this.apReqMessg == null)
/* 400 */       decode();
/* 401 */     if (this.apReqMessg != null)
/* 402 */       return this.apReqMessg.apOptions.get(2);
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   boolean useSessionKey() throws KrbException, IOException {
/* 407 */     if (this.apReqMessg == null)
/* 408 */       decode();
/* 409 */     if (this.apReqMessg != null)
/* 410 */       return this.apReqMessg.apOptions.get(1);
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */   public EncryptionKey getSubKey()
/*     */   {
/* 420 */     return this.authenticator.getSubKey();
/*     */   }
/*     */ 
/*     */   public Integer getSeqNumber()
/*     */   {
/* 430 */     return this.authenticator.getSeqNumber();
/*     */   }
/*     */ 
/*     */   public Checksum getChecksum()
/*     */   {
/* 439 */     return this.authenticator.getChecksum();
/*     */   }
/*     */ 
/*     */   public byte[] getMessage()
/*     */   {
/* 446 */     return this.obuf;
/*     */   }
/*     */ 
/*     */   public PrincipalName getClient()
/*     */   {
/* 454 */     return this.creds.getClient();
/*     */   }
/*     */ 
/*     */   private void createMessage(APOptions paramAPOptions, Ticket paramTicket, EncryptionKey paramEncryptionKey1, Realm paramRealm, PrincipalName paramPrincipalName, Checksum paramChecksum, KerberosTime paramKerberosTime, EncryptionKey paramEncryptionKey2, SeqNumber paramSeqNumber, AuthorizationData paramAuthorizationData, int paramInt)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbCryptoException
/*     */   {
/* 471 */     Integer localInteger = null;
/*     */ 
/* 473 */     if (paramSeqNumber != null) {
/* 474 */       localInteger = new Integer(paramSeqNumber.current());
/*     */     }
/* 476 */     this.authenticator = new Authenticator(paramRealm, paramPrincipalName, paramChecksum, paramKerberosTime.getMicroSeconds(), paramKerberosTime, paramEncryptionKey2, localInteger, paramAuthorizationData);
/*     */ 
/* 486 */     byte[] arrayOfByte = this.authenticator.asn1Encode();
/*     */ 
/* 488 */     EncryptedData localEncryptedData = new EncryptedData(paramEncryptionKey1, arrayOfByte, paramInt);
/*     */ 
/* 491 */     this.apReqMessg = new APReq(paramAPOptions, paramTicket, localEncryptedData);
/*     */   }
/*     */ 
/*     */   private static void checkPermittedEType(int paramInt)
/*     */     throws KrbException
/*     */   {
/* 497 */     int[] arrayOfInt = EType.getDefaults("permitted_enctypes");
/* 498 */     if (arrayOfInt == null) {
/* 499 */       throw new KrbException("No supported encryption types listed in permitted_enctypes");
/*     */     }
/*     */ 
/* 502 */     if (!EType.isSupported(paramInt, arrayOfInt))
/* 503 */       throw new KrbException(EType.toString(paramInt) + " encryption type not in permitted_enctypes list");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbApReq
 * JD-Core Version:    0.6.2
 */