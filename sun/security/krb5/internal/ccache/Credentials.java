/*     */ package sun.security.krb5.internal.ccache;
/*     */ 
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.EncKDCRepPart;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCRep;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ 
/*     */ public class Credentials
/*     */ {
/*     */   PrincipalName cname;
/*     */   Realm crealm;
/*     */   PrincipalName sname;
/*     */   Realm srealm;
/*     */   EncryptionKey key;
/*     */   KerberosTime authtime;
/*     */   KerberosTime starttime;
/*     */   KerberosTime endtime;
/*     */   KerberosTime renewTill;
/*     */   HostAddresses caddr;
/*     */   AuthorizationData authorizationData;
/*     */   public boolean isEncInSKey;
/*     */   TicketFlags flags;
/*     */   Ticket ticket;
/*     */   Ticket secondTicket;
/*  53 */   private boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public Credentials(PrincipalName paramPrincipalName1, PrincipalName paramPrincipalName2, EncryptionKey paramEncryptionKey, KerberosTime paramKerberosTime1, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, boolean paramBoolean, TicketFlags paramTicketFlags, HostAddresses paramHostAddresses, AuthorizationData paramAuthorizationData, Ticket paramTicket1, Ticket paramTicket2)
/*     */   {
/*  69 */     this.cname = ((PrincipalName)paramPrincipalName1.clone());
/*  70 */     if (paramPrincipalName1.getRealm() != null) {
/*  71 */       this.crealm = ((Realm)paramPrincipalName1.getRealm().clone());
/*     */     }
/*     */ 
/*  74 */     this.sname = ((PrincipalName)paramPrincipalName2.clone());
/*  75 */     if (paramPrincipalName2.getRealm() != null) {
/*  76 */       this.srealm = ((Realm)paramPrincipalName2.getRealm().clone());
/*     */     }
/*     */ 
/*  79 */     this.key = ((EncryptionKey)paramEncryptionKey.clone());
/*     */ 
/*  81 */     this.authtime = ((KerberosTime)paramKerberosTime1.clone());
/*  82 */     if (paramKerberosTime2 != null) {
/*  83 */       this.starttime = ((KerberosTime)paramKerberosTime2.clone());
/*     */     }
/*  85 */     this.endtime = ((KerberosTime)paramKerberosTime3.clone());
/*  86 */     if (paramKerberosTime4 != null) {
/*  87 */       this.renewTill = ((KerberosTime)paramKerberosTime4.clone());
/*     */     }
/*  89 */     if (paramHostAddresses != null) {
/*  90 */       this.caddr = ((HostAddresses)paramHostAddresses.clone());
/*     */     }
/*  92 */     if (paramAuthorizationData != null) {
/*  93 */       this.authorizationData = ((AuthorizationData)paramAuthorizationData.clone());
/*     */     }
/*     */ 
/*  96 */     this.isEncInSKey = paramBoolean;
/*  97 */     this.flags = ((TicketFlags)paramTicketFlags.clone());
/*  98 */     this.ticket = ((Ticket)paramTicket1.clone());
/*  99 */     if (paramTicket2 != null)
/* 100 */       this.secondTicket = ((Ticket)paramTicket2.clone());
/*     */   }
/*     */ 
/*     */   public Credentials(KDCRep paramKDCRep, Ticket paramTicket, AuthorizationData paramAuthorizationData, boolean paramBoolean)
/*     */   {
/* 109 */     if (paramKDCRep.encKDCRepPart == null)
/*     */     {
/* 111 */       return;
/*     */     }
/* 113 */     this.crealm = ((Realm)paramKDCRep.crealm.clone());
/* 114 */     this.cname = ((PrincipalName)paramKDCRep.cname.clone());
/* 115 */     this.ticket = ((Ticket)paramKDCRep.ticket.clone());
/* 116 */     this.key = ((EncryptionKey)paramKDCRep.encKDCRepPart.key.clone());
/* 117 */     this.flags = ((TicketFlags)paramKDCRep.encKDCRepPart.flags.clone());
/* 118 */     this.authtime = ((KerberosTime)paramKDCRep.encKDCRepPart.authtime.clone());
/* 119 */     if (paramKDCRep.encKDCRepPart.starttime != null) {
/* 120 */       this.starttime = ((KerberosTime)paramKDCRep.encKDCRepPart.starttime.clone());
/*     */     }
/* 122 */     this.endtime = ((KerberosTime)paramKDCRep.encKDCRepPart.endtime.clone());
/* 123 */     if (paramKDCRep.encKDCRepPart.renewTill != null) {
/* 124 */       this.renewTill = ((KerberosTime)paramKDCRep.encKDCRepPart.renewTill.clone());
/*     */     }
/* 126 */     this.srealm = ((Realm)paramKDCRep.encKDCRepPart.srealm.clone());
/* 127 */     this.sname = ((PrincipalName)paramKDCRep.encKDCRepPart.sname.clone());
/* 128 */     this.caddr = ((HostAddresses)paramKDCRep.encKDCRepPart.caddr.clone());
/* 129 */     this.secondTicket = ((Ticket)paramTicket.clone());
/* 130 */     this.authorizationData = ((AuthorizationData)paramAuthorizationData.clone());
/*     */ 
/* 132 */     this.isEncInSKey = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Credentials(KDCRep paramKDCRep) {
/* 136 */     this(paramKDCRep, null);
/*     */   }
/*     */ 
/*     */   public Credentials(KDCRep paramKDCRep, Ticket paramTicket) {
/* 140 */     this.sname = ((PrincipalName)paramKDCRep.encKDCRepPart.sname.clone());
/* 141 */     this.srealm = ((Realm)paramKDCRep.encKDCRepPart.srealm.clone());
/*     */     try {
/* 143 */       this.sname.setRealm(this.srealm);
/*     */     } catch (RealmException localRealmException1) {
/*     */     }
/* 146 */     this.cname = ((PrincipalName)paramKDCRep.cname.clone());
/* 147 */     this.crealm = ((Realm)paramKDCRep.crealm.clone());
/*     */     try {
/* 149 */       this.cname.setRealm(this.crealm);
/*     */     } catch (RealmException localRealmException2) {
/*     */     }
/* 152 */     this.key = ((EncryptionKey)paramKDCRep.encKDCRepPart.key.clone());
/* 153 */     this.authtime = ((KerberosTime)paramKDCRep.encKDCRepPart.authtime.clone());
/* 154 */     if (paramKDCRep.encKDCRepPart.starttime != null)
/* 155 */       this.starttime = ((KerberosTime)paramKDCRep.encKDCRepPart.starttime.clone());
/*     */     else {
/* 157 */       this.starttime = null;
/*     */     }
/* 159 */     this.endtime = ((KerberosTime)paramKDCRep.encKDCRepPart.endtime.clone());
/* 160 */     if (paramKDCRep.encKDCRepPart.renewTill != null)
/* 161 */       this.renewTill = ((KerberosTime)paramKDCRep.encKDCRepPart.renewTill.clone());
/*     */     else {
/* 163 */       this.renewTill = null;
/*     */     }
/*     */ 
/* 169 */     this.flags = paramKDCRep.encKDCRepPart.flags;
/* 170 */     if (paramKDCRep.encKDCRepPart.caddr != null)
/* 171 */       this.caddr = ((HostAddresses)paramKDCRep.encKDCRepPart.caddr.clone());
/*     */     else {
/* 173 */       this.caddr = null;
/*     */     }
/* 175 */     this.ticket = ((Ticket)paramKDCRep.ticket.clone());
/* 176 */     if (paramTicket != null) {
/* 177 */       this.secondTicket = ((Ticket)paramTicket.clone());
/* 178 */       this.isEncInSKey = true;
/*     */     } else {
/* 180 */       this.secondTicket = null;
/* 181 */       this.isEncInSKey = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 189 */     boolean bool = true;
/* 190 */     if (this.endtime.getTime() < System.currentTimeMillis())
/* 191 */       bool = false;
/* 192 */     else if (this.starttime != null) {
/* 193 */       if (this.starttime.getTime() > System.currentTimeMillis()) {
/* 194 */         bool = false;
/*     */       }
/*     */     }
/* 197 */     else if (this.authtime.getTime() > System.currentTimeMillis()) {
/* 198 */       bool = false;
/*     */     }
/*     */ 
/* 201 */     return bool;
/*     */   }
/*     */ 
/*     */   public PrincipalName getServicePrincipal() throws RealmException {
/* 205 */     if (this.sname.getRealm() == null) {
/* 206 */       this.sname.setRealm(this.srealm);
/*     */     }
/* 208 */     return this.sname;
/*     */   }
/*     */ 
/*     */   public sun.security.krb5.Credentials setKrbCreds() {
/* 212 */     return new sun.security.krb5.Credentials(this.ticket, this.cname, this.sname, this.key, this.flags, this.authtime, this.starttime, this.endtime, this.renewTill, this.caddr);
/*     */   }
/*     */ 
/*     */   public KerberosTime getAuthTime()
/*     */   {
/* 217 */     return this.authtime;
/*     */   }
/*     */ 
/*     */   public KerberosTime getEndTime() {
/* 221 */     return this.endtime;
/*     */   }
/*     */ 
/*     */   public TicketFlags getTicketFlags() {
/* 225 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public int getEType() {
/* 229 */     return this.key.getEType();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ccache.Credentials
 * JD-Core Version:    0.6.2
 */