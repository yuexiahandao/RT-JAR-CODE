/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import javax.security.auth.Destroyable;
/*     */ import javax.security.auth.RefreshFailedException;
/*     */ import javax.security.auth.Refreshable;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public class KerberosTicket
/*     */   implements Destroyable, Refreshable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7395334370157380539L;
/*     */   private static final int FORWARDABLE_TICKET_FLAG = 1;
/*     */   private static final int FORWARDED_TICKET_FLAG = 2;
/*     */   private static final int PROXIABLE_TICKET_FLAG = 3;
/*     */   private static final int PROXY_TICKET_FLAG = 4;
/*     */   private static final int POSTDATED_TICKET_FLAG = 6;
/*     */   private static final int RENEWABLE_TICKET_FLAG = 8;
/*     */   private static final int INITIAL_TICKET_FLAG = 9;
/*     */   private static final int NUM_FLAGS = 32;
/*     */   private byte[] asn1Encoding;
/*     */   private KeyImpl sessionKey;
/*     */   private boolean[] flags;
/*     */   private Date authTime;
/*     */   private Date startTime;
/*     */   private Date endTime;
/*     */   private Date renewTill;
/*     */   private KerberosPrincipal client;
/*     */   private KerberosPrincipal server;
/*     */   private InetAddress[] clientAddresses;
/* 196 */   private transient boolean destroyed = false;
/*     */ 
/*     */   public KerberosTicket(byte[] paramArrayOfByte1, KerberosPrincipal paramKerberosPrincipal1, KerberosPrincipal paramKerberosPrincipal2, byte[] paramArrayOfByte2, int paramInt, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */   {
/* 241 */     init(paramArrayOfByte1, paramKerberosPrincipal1, paramKerberosPrincipal2, paramArrayOfByte2, paramInt, paramArrayOfBoolean, paramDate1, paramDate2, paramDate3, paramDate4, paramArrayOfInetAddress);
/*     */   }
/*     */ 
/*     */   private void init(byte[] paramArrayOfByte1, KerberosPrincipal paramKerberosPrincipal1, KerberosPrincipal paramKerberosPrincipal2, byte[] paramArrayOfByte2, int paramInt, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */   {
/* 256 */     if (paramArrayOfByte2 == null) {
/* 257 */       throw new IllegalArgumentException("Session key for ticket cannot be null");
/*     */     }
/* 259 */     init(paramArrayOfByte1, paramKerberosPrincipal1, paramKerberosPrincipal2, new KeyImpl(paramArrayOfByte2, paramInt), paramArrayOfBoolean, paramDate1, paramDate2, paramDate3, paramDate4, paramArrayOfInetAddress);
/*     */   }
/*     */ 
/*     */   private void init(byte[] paramArrayOfByte, KerberosPrincipal paramKerberosPrincipal1, KerberosPrincipal paramKerberosPrincipal2, KeyImpl paramKeyImpl, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */   {
/* 274 */     if (paramArrayOfByte == null) {
/* 275 */       throw new IllegalArgumentException("ASN.1 encoding of ticket cannot be null");
/*     */     }
/* 277 */     this.asn1Encoding = ((byte[])paramArrayOfByte.clone());
/*     */ 
/* 279 */     if (paramKerberosPrincipal1 == null) {
/* 280 */       throw new IllegalArgumentException("Client name in ticket cannot be null");
/*     */     }
/* 282 */     this.client = paramKerberosPrincipal1;
/*     */ 
/* 284 */     if (paramKerberosPrincipal2 == null) {
/* 285 */       throw new IllegalArgumentException("Server name in ticket cannot be null");
/*     */     }
/* 287 */     this.server = paramKerberosPrincipal2;
/*     */ 
/* 290 */     this.sessionKey = paramKeyImpl;
/*     */ 
/* 292 */     if (paramArrayOfBoolean != null) {
/* 293 */       if (paramArrayOfBoolean.length >= 32) {
/* 294 */         this.flags = ((boolean[])paramArrayOfBoolean.clone());
/*     */       } else {
/* 296 */         this.flags = new boolean[32];
/*     */ 
/* 298 */         for (int i = 0; i < paramArrayOfBoolean.length; i++)
/* 299 */           this.flags[i] = paramArrayOfBoolean[i];
/*     */       }
/*     */     }
/* 302 */     else this.flags = new boolean[32];
/*     */ 
/* 304 */     if (this.flags[8] != 0) {
/* 305 */       if (paramDate4 == null) {
/* 306 */         throw new IllegalArgumentException("The renewable period end time cannot be null for renewable tickets.");
/*     */       }
/*     */ 
/* 309 */       this.renewTill = new Date(paramDate4.getTime());
/*     */     }
/*     */ 
/* 312 */     if (paramDate1 != null) {
/* 313 */       this.authTime = new Date(paramDate1.getTime());
/*     */     }
/* 315 */     if (paramDate2 != null)
/* 316 */       this.startTime = new Date(paramDate2.getTime());
/*     */     else {
/* 318 */       this.startTime = this.authTime;
/*     */     }
/*     */ 
/* 321 */     if (paramDate3 == null) {
/* 322 */       throw new IllegalArgumentException("End time for ticket validity cannot be null");
/*     */     }
/* 324 */     this.endTime = new Date(paramDate3.getTime());
/*     */ 
/* 326 */     if (paramArrayOfInetAddress != null)
/* 327 */       this.clientAddresses = ((InetAddress[])paramArrayOfInetAddress.clone());
/*     */   }
/*     */ 
/*     */   public final KerberosPrincipal getClient()
/*     */   {
/* 336 */     return this.client;
/*     */   }
/*     */ 
/*     */   public final KerberosPrincipal getServer()
/*     */   {
/* 345 */     return this.server;
/*     */   }
/*     */ 
/*     */   public final SecretKey getSessionKey()
/*     */   {
/* 354 */     if (this.destroyed)
/* 355 */       throw new IllegalStateException("This ticket is no longer valid");
/* 356 */     return this.sessionKey;
/*     */   }
/*     */ 
/*     */   public final int getSessionKeyType()
/*     */   {
/* 369 */     if (this.destroyed)
/* 370 */       throw new IllegalStateException("This ticket is no longer valid");
/* 371 */     return this.sessionKey.getKeyType();
/*     */   }
/*     */ 
/*     */   public final boolean isForwardable()
/*     */   {
/* 380 */     return this.flags[1];
/*     */   }
/*     */ 
/*     */   public final boolean isForwarded()
/*     */   {
/* 392 */     return this.flags[2];
/*     */   }
/*     */ 
/*     */   public final boolean isProxiable()
/*     */   {
/* 401 */     return this.flags[3];
/*     */   }
/*     */ 
/*     */   public final boolean isProxy()
/*     */   {
/* 410 */     return this.flags[4];
/*     */   }
/*     */ 
/*     */   public final boolean isPostdated()
/*     */   {
/* 420 */     return this.flags[6];
/*     */   }
/*     */ 
/*     */   public final boolean isRenewable()
/*     */   {
/* 431 */     return this.flags[8];
/*     */   }
/*     */ 
/*     */   public final boolean isInitial()
/*     */   {
/* 442 */     return this.flags[9];
/*     */   }
/*     */ 
/*     */   public final boolean[] getFlags()
/*     */   {
/* 453 */     return this.flags == null ? null : (boolean[])this.flags.clone();
/*     */   }
/*     */ 
/*     */   public final Date getAuthTime()
/*     */   {
/* 463 */     return this.authTime == null ? null : (Date)this.authTime.clone();
/*     */   }
/*     */ 
/*     */   public final Date getStartTime()
/*     */   {
/* 473 */     return this.startTime == null ? null : (Date)this.startTime.clone();
/*     */   }
/*     */ 
/*     */   public final Date getEndTime()
/*     */   {
/* 482 */     return (Date)this.endTime.clone();
/*     */   }
/*     */ 
/*     */   public final Date getRenewTill()
/*     */   {
/* 492 */     return this.renewTill == null ? null : (Date)this.renewTill.clone();
/*     */   }
/*     */ 
/*     */   public final InetAddress[] getClientAddresses()
/*     */   {
/* 502 */     return this.clientAddresses == null ? null : (InetAddress[])this.clientAddresses.clone();
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded()
/*     */   {
/* 511 */     if (this.destroyed)
/* 512 */       throw new IllegalStateException("This ticket is no longer valid");
/* 513 */     return (byte[])this.asn1Encoding.clone();
/*     */   }
/*     */ 
/*     */   public boolean isCurrent()
/*     */   {
/* 518 */     return System.currentTimeMillis() <= getEndTime().getTime();
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */     throws RefreshFailedException
/*     */   {
/* 542 */     if (this.destroyed) {
/* 543 */       throw new RefreshFailedException("A destroyed ticket cannot be renewd.");
/*     */     }
/*     */ 
/* 546 */     if (!isRenewable()) {
/* 547 */       throw new RefreshFailedException("This ticket is not renewable");
/*     */     }
/* 549 */     if (System.currentTimeMillis() > getRenewTill().getTime()) {
/* 550 */       throw new RefreshFailedException("This ticket is past its last renewal time.");
/*     */     }
/* 552 */     Object localObject1 = null;
/* 553 */     Credentials localCredentials = null;
/*     */     try
/*     */     {
/* 556 */       localCredentials = new Credentials(this.asn1Encoding, this.client.toString(), this.server.toString(), this.sessionKey.getEncoded(), this.sessionKey.getKeyType(), this.flags, this.authTime, this.startTime, this.endTime, this.renewTill, this.clientAddresses);
/*     */ 
/* 567 */       localCredentials = localCredentials.renew();
/*     */     } catch (KrbException localKrbException) {
/* 569 */       localObject1 = localKrbException;
/*     */     } catch (IOException localIOException) {
/* 571 */       localObject1 = localIOException;
/*     */     }
/*     */ 
/* 574 */     if (localObject1 != null) {
/* 575 */       RefreshFailedException localRefreshFailedException = new RefreshFailedException("Failed to renew Kerberos Ticket for client " + this.client + " and server " + this.server + " - " + localObject1.getMessage());
/*     */ 
/* 580 */       localRefreshFailedException.initCause(localObject1);
/* 581 */       throw localRefreshFailedException;
/*     */     }
/*     */ 
/* 587 */     synchronized (this) {
/*     */       try {
/* 589 */         destroy();
/*     */       }
/*     */       catch (DestroyFailedException localDestroyFailedException) {
/*     */       }
/* 593 */       init(localCredentials.getEncoded(), new KerberosPrincipal(localCredentials.getClient().getName()), new KerberosPrincipal(localCredentials.getServer().getName(), 2), localCredentials.getSessionKey().getBytes(), localCredentials.getSessionKey().getEType(), localCredentials.getFlags(), localCredentials.getAuthTime(), localCredentials.getStartTime(), localCredentials.getEndTime(), localCredentials.getRenewTill(), localCredentials.getClientAddresses());
/*     */ 
/* 605 */       this.destroyed = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */     throws DestroyFailedException
/*     */   {
/* 614 */     if (!this.destroyed) {
/* 615 */       Arrays.fill(this.asn1Encoding, (byte)0);
/* 616 */       this.client = null;
/* 617 */       this.server = null;
/* 618 */       this.sessionKey.destroy();
/* 619 */       this.flags = null;
/* 620 */       this.authTime = null;
/* 621 */       this.startTime = null;
/* 622 */       this.endTime = null;
/* 623 */       this.renewTill = null;
/* 624 */       this.clientAddresses = null;
/* 625 */       this.destroyed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDestroyed()
/*     */   {
/* 633 */     return this.destroyed;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 637 */     if (this.destroyed)
/* 638 */       throw new IllegalStateException("This ticket is no longer valid");
/* 639 */     StringBuffer localStringBuffer = new StringBuffer();
/* 640 */     if (this.clientAddresses != null) {
/* 641 */       for (int i = 0; i < this.clientAddresses.length; i++) {
/* 642 */         localStringBuffer.append("clientAddresses[" + i + "] = " + this.clientAddresses[i].toString());
/*     */       }
/*     */     }
/*     */ 
/* 646 */     return "Ticket (hex) = \n" + new HexDumpEncoder().encodeBuffer(this.asn1Encoding) + "\n" + "Client Principal = " + this.client.toString() + "\n" + "Server Principal = " + this.server.toString() + "\n" + "Session Key = " + this.sessionKey.toString() + "\n" + "Forwardable Ticket " + this.flags[1] + "\n" + "Forwarded Ticket " + this.flags[2] + "\n" + "Proxiable Ticket " + this.flags[3] + "\n" + "Proxy Ticket " + this.flags[4] + "\n" + "Postdated Ticket " + this.flags[6] + "\n" + "Renewable Ticket " + this.flags[8] + "\n" + "Initial Ticket " + this.flags[8] + "\n" + "Auth Time = " + String.valueOf(this.authTime) + "\n" + "Start Time = " + String.valueOf(this.startTime) + "\n" + "End Time = " + this.endTime.toString() + "\n" + "Renew Till = " + String.valueOf(this.renewTill) + "\n" + "Client Addresses " + (this.clientAddresses == null ? " Null " : new StringBuilder().append(localStringBuffer.toString()).append("\n").toString());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 674 */     int i = 17;
/* 675 */     if (isDestroyed()) {
/* 676 */       return i;
/*     */     }
/* 678 */     i = i * 37 + Arrays.hashCode(getEncoded());
/* 679 */     i = i * 37 + this.endTime.hashCode();
/* 680 */     i = i * 37 + this.client.hashCode();
/* 681 */     i = i * 37 + this.server.hashCode();
/* 682 */     i = i * 37 + this.sessionKey.hashCode();
/*     */ 
/* 685 */     if (this.authTime != null) {
/* 686 */       i = i * 37 + this.authTime.hashCode();
/*     */     }
/*     */ 
/* 690 */     if (this.startTime != null) {
/* 691 */       i = i * 37 + this.startTime.hashCode();
/*     */     }
/*     */ 
/* 695 */     if (this.renewTill != null) {
/* 696 */       i = i * 37 + this.renewTill.hashCode();
/*     */     }
/*     */ 
/* 700 */     i = i * 37 + Arrays.hashCode(this.clientAddresses);
/* 701 */     return i * 37 + Arrays.hashCode(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 718 */     if (paramObject == this) {
/* 719 */       return true;
/*     */     }
/* 721 */     if (!(paramObject instanceof KerberosTicket)) {
/* 722 */       return false;
/*     */     }
/*     */ 
/* 725 */     KerberosTicket localKerberosTicket = (KerberosTicket)paramObject;
/* 726 */     if ((isDestroyed()) || (localKerberosTicket.isDestroyed())) {
/* 727 */       return false;
/*     */     }
/*     */ 
/* 730 */     if ((!Arrays.equals(getEncoded(), localKerberosTicket.getEncoded())) || (!this.endTime.equals(localKerberosTicket.getEndTime())) || (!this.server.equals(localKerberosTicket.getServer())) || (!this.client.equals(localKerberosTicket.getClient())) || (!this.sessionKey.equals(localKerberosTicket.getSessionKey())) || (!Arrays.equals(this.clientAddresses, localKerberosTicket.getClientAddresses())) || (!Arrays.equals(this.flags, localKerberosTicket.getFlags())))
/*     */     {
/* 737 */       return false;
/*     */     }
/*     */ 
/* 741 */     if (this.authTime == null) {
/* 742 */       if (localKerberosTicket.getAuthTime() != null)
/* 743 */         return false;
/*     */     }
/* 745 */     else if (!this.authTime.equals(localKerberosTicket.getAuthTime())) {
/* 746 */       return false;
/*     */     }
/*     */ 
/* 750 */     if (this.startTime == null) {
/* 751 */       if (localKerberosTicket.getStartTime() != null)
/* 752 */         return false;
/*     */     }
/* 754 */     else if (!this.startTime.equals(localKerberosTicket.getStartTime())) {
/* 755 */       return false;
/*     */     }
/*     */ 
/* 758 */     if (this.renewTill == null) {
/* 759 */       if (localKerberosTicket.getRenewTill() != null)
/* 760 */         return false;
/*     */     }
/* 762 */     else if (!this.renewTill.equals(localKerberosTicket.getRenewTill())) {
/* 763 */       return false;
/*     */     }
/*     */ 
/* 766 */     return true;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 771 */     paramObjectInputStream.defaultReadObject();
/* 772 */     if (this.sessionKey == null)
/* 773 */       throw new InvalidObjectException("Session key cannot be null");
/*     */     try
/*     */     {
/* 776 */       init(this.asn1Encoding, this.client, this.server, this.sessionKey, this.flags, this.authTime, this.startTime, this.endTime, this.renewTill, this.clientAddresses);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 780 */       throw ((InvalidObjectException)new InvalidObjectException(localIllegalArgumentException.getMessage()).initCause(localIllegalArgumentException));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KerberosTicket
 * JD-Core Version:    0.6.2
 */