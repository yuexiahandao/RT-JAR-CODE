/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.CredentialsUtil;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCOptions;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ import sun.security.krb5.internal.ccache.CredentialsCache;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ 
/*     */ public class Credentials
/*     */ {
/*     */   Ticket ticket;
/*     */   PrincipalName client;
/*     */   PrincipalName server;
/*     */   EncryptionKey key;
/*     */   TicketFlags flags;
/*     */   KerberosTime authTime;
/*     */   KerberosTime startTime;
/*     */   KerberosTime endTime;
/*     */   KerberosTime renewTill;
/*     */   HostAddresses cAddr;
/*     */   EncryptionKey serviceKey;
/*     */   AuthorizationData authzData;
/*  61 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */   private static CredentialsCache cache;
/*  63 */   static boolean alreadyLoaded = false;
/*  64 */   private static boolean alreadyTried = false;
/*     */ 
/*     */   private static native Credentials acquireDefaultNativeCreds(int[] paramArrayOfInt);
/*     */ 
/*     */   public Credentials(Ticket paramTicket, PrincipalName paramPrincipalName1, PrincipalName paramPrincipalName2, EncryptionKey paramEncryptionKey, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, HostAddresses paramHostAddresses, AuthorizationData paramAuthorizationData)
/*     */   {
/*  80 */     this(paramTicket, paramPrincipalName1, paramPrincipalName2, paramEncryptionKey, paramTicketFlags, paramKerberosTime1, paramKerberosTime2, paramKerberosTime3, paramKerberosTime4, paramHostAddresses);
/*     */ 
/*  82 */     this.authzData = paramAuthorizationData;
/*     */   }
/*     */ 
/*     */   public Credentials(Ticket paramTicket, PrincipalName paramPrincipalName1, PrincipalName paramPrincipalName2, EncryptionKey paramEncryptionKey, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, HostAddresses paramHostAddresses)
/*     */   {
/*  95 */     this.ticket = paramTicket;
/*  96 */     this.client = paramPrincipalName1;
/*  97 */     this.server = paramPrincipalName2;
/*  98 */     this.key = paramEncryptionKey;
/*  99 */     this.flags = paramTicketFlags;
/* 100 */     this.authTime = paramKerberosTime1;
/* 101 */     this.startTime = paramKerberosTime2;
/* 102 */     this.endTime = paramKerberosTime3;
/* 103 */     this.renewTill = paramKerberosTime4;
/* 104 */     this.cAddr = paramHostAddresses;
/*     */   }
/*     */ 
/*     */   public Credentials(byte[] paramArrayOfByte1, String paramString1, String paramString2, byte[] paramArrayOfByte2, int paramInt, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */     throws KrbException, IOException
/*     */   {
/* 118 */     this(new Ticket(paramArrayOfByte1), new PrincipalName(paramString1, 1), new PrincipalName(paramString2, 2), new EncryptionKey(paramInt, paramArrayOfByte2), paramArrayOfBoolean == null ? null : new TicketFlags(paramArrayOfBoolean), paramDate1 == null ? null : new KerberosTime(paramDate1), paramDate2 == null ? null : new KerberosTime(paramDate2), paramDate3 == null ? null : new KerberosTime(paramDate3), paramDate4 == null ? null : new KerberosTime(paramDate4), null);
/*     */   }
/*     */ 
/*     */   public final PrincipalName getClient()
/*     */   {
/* 142 */     return this.client;
/*     */   }
/*     */ 
/*     */   public final PrincipalName getServer() {
/* 146 */     return this.server;
/*     */   }
/*     */ 
/*     */   public final EncryptionKey getSessionKey() {
/* 150 */     return this.key;
/*     */   }
/*     */ 
/*     */   public final Date getAuthTime() {
/* 154 */     if (this.authTime != null) {
/* 155 */       return this.authTime.toDate();
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   public final Date getStartTime()
/*     */   {
/* 162 */     if (this.startTime != null)
/*     */     {
/* 164 */       return this.startTime.toDate();
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   public final Date getEndTime() {
/* 170 */     if (this.endTime != null)
/*     */     {
/* 172 */       return this.endTime.toDate();
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public final Date getRenewTill() {
/* 178 */     if (this.renewTill != null)
/*     */     {
/* 180 */       return this.renewTill.toDate();
/*     */     }
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   public final boolean[] getFlags() {
/* 186 */     if (this.flags == null)
/* 187 */       return null;
/* 188 */     return this.flags.toBooleanArray();
/*     */   }
/*     */ 
/*     */   public final InetAddress[] getClientAddresses()
/*     */   {
/* 193 */     if (this.cAddr == null) {
/* 194 */       return null;
/*     */     }
/* 196 */     return this.cAddr.getInetAddresses();
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded() {
/* 200 */     byte[] arrayOfByte = null;
/*     */     try {
/* 202 */       arrayOfByte = this.ticket.asn1Encode();
/*     */     } catch (Asn1Exception localAsn1Exception) {
/* 204 */       if (DEBUG)
/* 205 */         System.out.println(localAsn1Exception);
/*     */     } catch (IOException localIOException) {
/* 207 */       if (DEBUG)
/* 208 */         System.out.println(localIOException);
/*     */     }
/* 210 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public boolean isForwardable() {
/* 214 */     return this.flags.get(1);
/*     */   }
/*     */ 
/*     */   public boolean isRenewable() {
/* 218 */     return this.flags.get(8);
/*     */   }
/*     */ 
/*     */   public Ticket getTicket() {
/* 222 */     return this.ticket;
/*     */   }
/*     */ 
/*     */   public TicketFlags getTicketFlags() {
/* 226 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public AuthorizationData getAuthzData() {
/* 230 */     return this.authzData;
/*     */   }
/*     */ 
/*     */   public boolean checkDelegate()
/*     */   {
/* 238 */     return this.flags.get(13);
/*     */   }
/*     */ 
/*     */   public void resetDelegate()
/*     */   {
/* 250 */     this.flags.set(13, false);
/*     */   }
/*     */ 
/*     */   public Credentials renew() throws KrbException, IOException {
/* 254 */     KDCOptions localKDCOptions = new KDCOptions();
/* 255 */     localKDCOptions.set(30, true);
/*     */ 
/* 259 */     localKDCOptions.set(8, true);
/*     */ 
/* 261 */     return new KrbTgsReq(localKDCOptions, this, this.server, null, null, null, null, this.cAddr, null, null, null).sendAndGetCreds();
/*     */   }
/*     */ 
/*     */   public static Credentials acquireTGTFromCache(PrincipalName paramPrincipalName, String paramString)
/*     */     throws KrbException, IOException
/*     */   {
/* 289 */     if (paramString == null)
/*     */     {
/* 291 */       localObject1 = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
/*     */ 
/* 293 */       if ((((String)localObject1).toUpperCase(Locale.ENGLISH).startsWith("WINDOWS")) || (((String)localObject1).toUpperCase(Locale.ENGLISH).contains("OS X")))
/*     */       {
/* 295 */         localObject2 = acquireDefaultCreds();
/* 296 */         if (localObject2 == null) {
/* 297 */           if (DEBUG) {
/* 298 */             System.out.println(">>> Found no TGT's in LSA");
/*     */           }
/* 300 */           return null;
/*     */         }
/* 302 */         if (paramPrincipalName != null) {
/* 303 */           if (((Credentials)localObject2).getClient().equals(paramPrincipalName)) {
/* 304 */             if (DEBUG) {
/* 305 */               System.out.println(">>> Obtained TGT from LSA: " + localObject2);
/*     */             }
/*     */ 
/* 308 */             return localObject2;
/*     */           }
/* 310 */           if (DEBUG) {
/* 311 */             System.out.println(">>> LSA contains TGT for " + ((Credentials)localObject2).getClient() + " not " + paramPrincipalName);
/*     */           }
/*     */ 
/* 316 */           return null;
/*     */         }
/*     */ 
/* 319 */         if (DEBUG) {
/* 320 */           System.out.println(">>> Obtained TGT from LSA: " + localObject2);
/*     */         }
/*     */ 
/* 323 */         return localObject2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 332 */     Object localObject1 = CredentialsCache.getInstance(paramPrincipalName, paramString);
/*     */ 
/* 335 */     if (localObject1 == null) {
/* 336 */       return null;
/*     */     }
/*     */ 
/* 339 */     Object localObject2 = ((CredentialsCache)localObject1).getDefaultCreds();
/*     */ 
/* 342 */     if (localObject2 == null) {
/* 343 */       return null;
/*     */     }
/*     */ 
/* 346 */     if (EType.isSupported(((sun.security.krb5.internal.ccache.Credentials)localObject2).getEType())) {
/* 347 */       return ((sun.security.krb5.internal.ccache.Credentials)localObject2).setKrbCreds();
/*     */     }
/* 349 */     if (DEBUG) {
/* 350 */       System.out.println(">>> unsupported key type found the default TGT: " + ((sun.security.krb5.internal.ccache.Credentials)localObject2).getEType());
/*     */     }
/*     */ 
/* 354 */     return null;
/*     */   }
/*     */ 
/*     */   public static synchronized Credentials acquireDefaultCreds()
/*     */   {
/* 381 */     Credentials localCredentials = null;
/*     */ 
/* 383 */     if (cache == null) {
/* 384 */       cache = CredentialsCache.getInstance();
/*     */     }
/* 386 */     if (cache != null) {
/* 387 */       sun.security.krb5.internal.ccache.Credentials localCredentials1 = cache.getDefaultCreds();
/*     */ 
/* 389 */       if (localCredentials1 != null) {
/* 390 */         if (DEBUG) {
/* 391 */           System.out.println(">>> KrbCreds found the default ticket granting ticket in credential cache.");
/*     */         }
/*     */ 
/* 394 */         if (EType.isSupported(localCredentials1.getEType())) {
/* 395 */           localCredentials = localCredentials1.setKrbCreds();
/*     */         }
/* 397 */         else if (DEBUG) {
/* 398 */           System.out.println(">>> unsupported key type found the default TGT: " + localCredentials1.getEType());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 405 */     if (localCredentials == null)
/*     */     {
/* 409 */       if (!alreadyTried) {
/*     */         try
/*     */         {
/* 412 */           ensureLoaded();
/*     */         } catch (Exception localException) {
/* 414 */           if (DEBUG) {
/* 415 */             System.out.println("Can not load credentials cache");
/* 416 */             localException.printStackTrace();
/*     */           }
/* 418 */           alreadyTried = true;
/*     */         }
/*     */       }
/* 421 */       if (alreadyLoaded)
/*     */       {
/* 423 */         if (DEBUG) {
/* 424 */           System.out.println(">> Acquire default native Credentials");
/*     */         }
/* 426 */         localCredentials = acquireDefaultNativeCreds(EType.getDefaults("default_tkt_enctypes"));
/*     */       }
/*     */     }
/*     */ 
/* 430 */     return localCredentials;
/*     */   }
/*     */ 
/*     */   public static Credentials acquireServiceCreds(String paramString, Credentials paramCredentials)
/*     */     throws KrbException, IOException
/*     */   {
/* 454 */     return CredentialsUtil.acquireServiceCreds(paramString, paramCredentials);
/*     */   }
/*     */ 
/*     */   public CredentialsCache getCache() {
/* 458 */     return cache;
/*     */   }
/*     */ 
/*     */   public EncryptionKey getServiceKey() {
/* 462 */     return this.serviceKey;
/*     */   }
/*     */ 
/*     */   public static void printDebug(Credentials paramCredentials)
/*     */   {
/* 469 */     System.out.println(">>> DEBUG: ----Credentials----");
/* 470 */     System.out.println("\tclient: " + paramCredentials.client.toString());
/* 471 */     System.out.println("\tserver: " + paramCredentials.server.toString());
/* 472 */     System.out.println("\tticket: realm: " + paramCredentials.ticket.realm.toString());
/* 473 */     System.out.println("\t        sname: " + paramCredentials.ticket.sname.toString());
/* 474 */     if (paramCredentials.startTime != null) {
/* 475 */       System.out.println("\tstartTime: " + paramCredentials.startTime.getTime());
/*     */     }
/* 477 */     System.out.println("\tendTime: " + paramCredentials.endTime.getTime());
/* 478 */     System.out.println("        ----Credentials end----");
/*     */   }
/*     */ 
/*     */   static void ensureLoaded()
/*     */   {
/* 483 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 486 */         if (System.getProperty("os.name").contains("OS X"))
/* 487 */           System.loadLibrary("osxkrb5");
/*     */         else {
/* 489 */           System.loadLibrary("w2k_lsa_auth");
/*     */         }
/* 491 */         return null;
/*     */       }
/*     */     });
/* 494 */     alreadyLoaded = true;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 498 */     StringBuffer localStringBuffer = new StringBuffer("Credentials:");
/* 499 */     localStringBuffer.append("\nclient=").append(this.client);
/* 500 */     localStringBuffer.append("\nserver=").append(this.server);
/* 501 */     if (this.authTime != null) {
/* 502 */       localStringBuffer.append("\nauthTime=").append(this.authTime);
/*     */     }
/* 504 */     if (this.startTime != null) {
/* 505 */       localStringBuffer.append("\nstartTime=").append(this.startTime);
/*     */     }
/* 507 */     localStringBuffer.append("\nendTime=").append(this.endTime);
/* 508 */     localStringBuffer.append("\nrenewTill=").append(this.renewTill);
/* 509 */     localStringBuffer.append("\nflags: ").append(this.flags);
/* 510 */     localStringBuffer.append("\nEType (int): ").append(this.key.getEType());
/* 511 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.Credentials
 * JD-Core Version:    0.6.2
 */