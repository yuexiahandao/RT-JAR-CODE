/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.kerberos.KerberosKey;
/*     */ import javax.security.auth.kerberos.KerberosPrincipal;
/*     */ import javax.security.auth.kerberos.KerberosTicket;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import sun.misc.JavaxSecurityAuthKerberosAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.jgss.GSSCaller;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public class Krb5Util
/*     */ {
/*  55 */   static final boolean DEBUG = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.debug"))).booleanValue();
/*     */ 
/*     */   public static KerberosTicket getTicketFromSubjectAndTgs(GSSCaller paramGSSCaller, String paramString1, String paramString2, String paramString3, AccessControlContext paramAccessControlContext)
/*     */     throws LoginException, KrbException, IOException
/*     */   {
/*  84 */     Subject localSubject1 = Subject.getSubject(paramAccessControlContext);
/*  85 */     KerberosTicket localKerberosTicket1 = (KerberosTicket)SubjectComber.find(localSubject1, paramString2, paramString1, KerberosTicket.class);
/*     */ 
/*  88 */     if (localKerberosTicket1 != null) {
/*  89 */       return localKerberosTicket1;
/*     */     }
/*     */ 
/*  92 */     Subject localSubject2 = null;
/*  93 */     if (!GSSUtil.useSubjectCredsOnly(paramGSSCaller)) {
/*     */       try
/*     */       {
/*  96 */         localSubject2 = GSSUtil.login(paramGSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
/*  97 */         localKerberosTicket1 = (KerberosTicket)SubjectComber.find(localSubject2, paramString2, paramString1, KerberosTicket.class);
/*     */ 
/*  99 */         if (localKerberosTicket1 != null) {
/* 100 */           return localKerberosTicket1;
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (LoginException localLoginException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 112 */     KerberosTicket localKerberosTicket2 = (KerberosTicket)SubjectComber.find(localSubject1, paramString3, paramString1, KerberosTicket.class);
/*     */     int i;
/* 116 */     if ((localKerberosTicket2 == null) && (localSubject2 != null))
/*     */     {
/* 118 */       localKerberosTicket2 = (KerberosTicket)SubjectComber.find(localSubject2, paramString3, paramString1, KerberosTicket.class);
/*     */ 
/* 120 */       i = 0;
/*     */     } else {
/* 122 */       i = 1;
/*     */     }
/*     */ 
/* 126 */     if (localKerberosTicket2 != null) {
/* 127 */       Credentials localCredentials1 = ticketToCreds(localKerberosTicket2);
/* 128 */       Credentials localCredentials2 = Credentials.acquireServiceCreds(paramString2, localCredentials1);
/*     */ 
/* 130 */       if (localCredentials2 != null) {
/* 131 */         localKerberosTicket1 = credsToTicket(localCredentials2);
/*     */ 
/* 134 */         if ((i != 0) && (localSubject1 != null) && (!localSubject1.isReadOnly())) {
/* 135 */           localSubject1.getPrivateCredentials().add(localKerberosTicket1);
/*     */         }
/*     */       }
/*     */     }
/* 139 */     return localKerberosTicket1;
/*     */   }
/*     */ 
/*     */   static KerberosTicket getTicket(GSSCaller paramGSSCaller, String paramString1, String paramString2, AccessControlContext paramAccessControlContext)
/*     */     throws LoginException
/*     */   {
/* 154 */     Subject localSubject1 = Subject.getSubject(paramAccessControlContext);
/* 155 */     KerberosTicket localKerberosTicket = (KerberosTicket)SubjectComber.find(localSubject1, paramString2, paramString1, KerberosTicket.class);
/*     */ 
/* 160 */     if ((localKerberosTicket == null) && (!GSSUtil.useSubjectCredsOnly(paramGSSCaller))) {
/* 161 */       Subject localSubject2 = GSSUtil.login(paramGSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
/* 162 */       localKerberosTicket = (KerberosTicket)SubjectComber.find(localSubject2, paramString2, paramString1, KerberosTicket.class);
/*     */     }
/*     */ 
/* 165 */     return localKerberosTicket;
/*     */   }
/*     */ 
/*     */   public static Subject getSubject(GSSCaller paramGSSCaller, AccessControlContext paramAccessControlContext)
/*     */     throws LoginException
/*     */   {
/* 183 */     Subject localSubject = Subject.getSubject(paramAccessControlContext);
/*     */ 
/* 186 */     if ((localSubject == null) && (!GSSUtil.useSubjectCredsOnly(paramGSSCaller))) {
/* 187 */       localSubject = GSSUtil.login(paramGSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
/*     */     }
/* 189 */     return localSubject;
/*     */   }
/*     */ 
/*     */   public static ServiceCreds getServiceCreds(GSSCaller paramGSSCaller, String paramString, AccessControlContext paramAccessControlContext)
/*     */     throws LoginException
/*     */   {
/* 328 */     Subject localSubject1 = Subject.getSubject(paramAccessControlContext);
/* 329 */     ServiceCreds localServiceCreds = null;
/* 330 */     if (localSubject1 != null) {
/* 331 */       localServiceCreds = ServiceCreds.getInstance(localSubject1, paramString);
/*     */     }
/* 333 */     if ((localServiceCreds == null) && (!GSSUtil.useSubjectCredsOnly(paramGSSCaller))) {
/* 334 */       Subject localSubject2 = GSSUtil.login(paramGSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
/* 335 */       localServiceCreds = ServiceCreds.getInstance(localSubject2, paramString);
/*     */     }
/* 337 */     return localServiceCreds;
/*     */   }
/*     */ 
/*     */   public static KerberosTicket credsToTicket(Credentials paramCredentials) {
/* 341 */     EncryptionKey localEncryptionKey = paramCredentials.getSessionKey();
/* 342 */     return new KerberosTicket(paramCredentials.getEncoded(), new KerberosPrincipal(paramCredentials.getClient().getName()), new KerberosPrincipal(paramCredentials.getServer().getName(), 2), localEncryptionKey.getBytes(), localEncryptionKey.getEType(), paramCredentials.getFlags(), paramCredentials.getAuthTime(), paramCredentials.getStartTime(), paramCredentials.getEndTime(), paramCredentials.getRenewTill(), paramCredentials.getClientAddresses());
/*     */   }
/*     */ 
/*     */   public static Credentials ticketToCreds(KerberosTicket paramKerberosTicket)
/*     */     throws KrbException, IOException
/*     */   {
/* 359 */     return new Credentials(paramKerberosTicket.getEncoded(), paramKerberosTicket.getClient().getName(), paramKerberosTicket.getServer().getName(), paramKerberosTicket.getSessionKey().getEncoded(), paramKerberosTicket.getSessionKeyType(), paramKerberosTicket.getFlags(), paramKerberosTicket.getAuthTime(), paramKerberosTicket.getStartTime(), paramKerberosTicket.getEndTime(), paramKerberosTicket.getRenewTill(), paramKerberosTicket.getClientAddresses());
/*     */   }
/*     */ 
/*     */   public static EncryptionKey[] keysFromJavaxKeyTab(KeyTab paramKeyTab, PrincipalName paramPrincipalName)
/*     */   {
/* 381 */     return SharedSecrets.getJavaxSecurityAuthKerberosAccess().keyTabGetEncryptionKeys(paramKeyTab, paramPrincipalName);
/*     */   }
/*     */ 
/*     */   public static class KeysFromKeyTab extends KerberosKey
/*     */   {
/*     */     public KeysFromKeyTab(KerberosKey paramKerberosKey)
/*     */     {
/* 197 */       super(paramKerberosKey.getEncoded(), paramKerberosKey.getKeyType(), paramKerberosKey.getVersionNumber());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ServiceCreds
/*     */   {
/*     */     private KerberosPrincipal kp;
/*     */     private List<KeyTab> ktabs;
/*     */     private List<KerberosKey> kk;
/*     */     private Subject subj;
/*     */ 
/*     */     private static ServiceCreds getInstance(Subject paramSubject, String paramString)
/*     */     {
/* 225 */       ServiceCreds localServiceCreds = new ServiceCreds();
/* 226 */       localServiceCreds.subj = paramSubject;
/*     */ 
/* 228 */       for (Object localObject = paramSubject.getPrincipals(KerberosPrincipal.class).iterator(); ((Iterator)localObject).hasNext(); ) { KerberosPrincipal localKerberosPrincipal = (KerberosPrincipal)((Iterator)localObject).next();
/* 229 */         if ((paramString == null) || (localKerberosPrincipal.getName().equals(paramString)))
/*     */         {
/* 231 */           localServiceCreds.kp = localKerberosPrincipal;
/* 232 */           paramString = localKerberosPrincipal.getName();
/* 233 */           break;
/*     */         }
/*     */       }
/* 236 */       if (localServiceCreds.kp == null)
/*     */       {
/* 239 */         localObject = SubjectComber.findMany(paramSubject, null, null, KerberosKey.class);
/*     */ 
/* 241 */         if (!((List)localObject).isEmpty()) {
/* 242 */           localServiceCreds.kp = ((KerberosKey)((List)localObject).get(0)).getPrincipal();
/* 243 */           paramString = localServiceCreds.kp.getName();
/* 244 */           if (Krb5Util.DEBUG)
/* 245 */             System.out.println(">>> ServiceCreds: no kp? find one from kk: " + paramString);
/*     */         }
/*     */         else
/*     */         {
/* 249 */           return null;
/*     */         }
/*     */       }
/* 252 */       localServiceCreds.ktabs = SubjectComber.findMany(paramSubject, null, null, KeyTab.class);
/*     */ 
/* 254 */       localServiceCreds.kk = SubjectComber.findMany(paramSubject, paramString, null, KerberosKey.class);
/*     */ 
/* 256 */       if ((localServiceCreds.ktabs.isEmpty()) && (localServiceCreds.kk.isEmpty())) {
/* 257 */         return null;
/*     */       }
/* 259 */       return localServiceCreds;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 263 */       return this.kp.getName();
/*     */     }
/*     */ 
/*     */     public KerberosKey[] getKKeys() {
/* 267 */       if (this.ktabs.isEmpty()) {
/* 268 */         return (KerberosKey[])this.kk.toArray(new KerberosKey[this.kk.size()]);
/*     */       }
/* 270 */       ArrayList localArrayList = new ArrayList();
/* 271 */       for (Object localObject1 = this.ktabs.iterator(); ((Iterator)localObject1).hasNext(); ) { KeyTab localKeyTab = (KeyTab)((Iterator)localObject1).next();
/* 272 */         for (Object localObject4 : localKeyTab.getKeys(this.kp)) {
/* 273 */           localArrayList.add(localObject4);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 278 */       if (!this.subj.isReadOnly()) {
/* 279 */         localObject1 = this.subj.getPrivateCredentials();
/* 280 */         synchronized (localObject1) {
/* 281 */           ??? = ((Set)localObject1).iterator();
/* 282 */           while (((Iterator)???).hasNext()) {
/* 283 */             Object localObject3 = ((Iterator)???).next();
/* 284 */             if ((localObject3 instanceof Krb5Util.KeysFromKeyTab)) {
/* 285 */               KerberosKey localKerberosKey = (KerberosKey)localObject3;
/* 286 */               if (Objects.equals(localKerberosKey.getPrincipal(), this.kp)) {
/* 287 */                 ((Iterator)???).remove();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 292 */         for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { ??? = (KerberosKey)((Iterator)???).next();
/* 293 */           this.subj.getPrivateCredentials().add(new Krb5Util.KeysFromKeyTab((KerberosKey)???));
/*     */         }
/*     */       }
/* 296 */       return (KerberosKey[])localArrayList.toArray(new KerberosKey[localArrayList.size()]);
/*     */     }
/*     */ 
/*     */     public EncryptionKey[] getEKeys()
/*     */     {
/* 301 */       KerberosKey[] arrayOfKerberosKey = getKKeys();
/* 302 */       EncryptionKey[] arrayOfEncryptionKey = new EncryptionKey[arrayOfKerberosKey.length];
/* 303 */       for (int i = 0; i < arrayOfEncryptionKey.length; i++) {
/* 304 */         arrayOfEncryptionKey[i] = new EncryptionKey(arrayOfKerberosKey[i].getEncoded(), arrayOfKerberosKey[i].getKeyType(), new Integer(arrayOfKerberosKey[i].getVersionNumber()));
/*     */       }
/*     */ 
/* 308 */       return arrayOfEncryptionKey;
/*     */     }
/*     */ 
/*     */     public void destroy() {
/* 312 */       this.kp = null;
/* 313 */       this.ktabs = null;
/* 314 */       this.kk = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5Util
 * JD-Core Version:    0.6.2
 */