/*     */ package sun.security.jgss;
/*     */ 
/*     */ import com.sun.security.auth.callback.TextCallbackHandler;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Security;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.kerberos.KerberosKey;
/*     */ import javax.security.auth.kerberos.KerberosPrincipal;
/*     */ import javax.security.auth.kerberos.KerberosTicket;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.net.www.protocol.http.spnego.NegotiateCallbackHandler;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.jgss.krb5.Krb5NameElement;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.jgss.spnego.SpNegoCredElement;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public class GSSUtil
/*     */ {
/*  58 */   public static final Oid GSS_KRB5_MECH_OID = createOid("1.2.840.113554.1.2.2");
/*     */ 
/*  60 */   public static final Oid GSS_KRB5_MECH_OID2 = createOid("1.3.5.1.5.2");
/*     */ 
/*  63 */   public static final Oid GSS_SPNEGO_MECH_OID = createOid("1.3.6.1.5.5.2");
/*     */ 
/*  66 */   public static final Oid NT_GSS_KRB5_PRINCIPAL = createOid("1.2.840.113554.1.2.2.1");
/*     */   private static final String DEFAULT_HANDLER = "auth.login.defaultCallbackHandler";
/*  74 */   static final boolean DEBUG = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.jgss.debug"))).booleanValue();
/*     */ 
/*     */   static void debug(String paramString)
/*     */   {
/*  80 */     if (DEBUG) {
/*  81 */       assert (paramString != null);
/*  82 */       System.out.println(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Oid createOid(String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return new Oid(paramString);
/*     */     } catch (GSSException localGSSException) {
/*  93 */       debug("Ignored invalid OID: " + paramString);
/*  94 */     }return null;
/*     */   }
/*     */ 
/*     */   public static boolean isSpNegoMech(Oid paramOid)
/*     */   {
/*  99 */     return GSS_SPNEGO_MECH_OID.equals(paramOid);
/*     */   }
/*     */ 
/*     */   public static boolean isKerberosMech(Oid paramOid) {
/* 103 */     return (GSS_KRB5_MECH_OID.equals(paramOid)) || (GSS_KRB5_MECH_OID2.equals(paramOid));
/*     */   }
/*     */ 
/*     */   public static String getMechStr(Oid paramOid)
/*     */   {
/* 109 */     if (isSpNegoMech(paramOid))
/* 110 */       return "SPNEGO";
/* 111 */     if (isKerberosMech(paramOid)) {
/* 112 */       return "Kerberos V5";
/*     */     }
/* 114 */     return paramOid.toString();
/*     */   }
/*     */ 
/*     */   public static Subject getSubject(GSSName paramGSSName, GSSCredential paramGSSCredential)
/*     */   {
/* 126 */     HashSet localHashSet1 = null;
/* 127 */     HashSet localHashSet2 = new HashSet();
/*     */ 
/* 129 */     Set localSet = null;
/*     */ 
/* 131 */     HashSet localHashSet3 = new HashSet();
/*     */ 
/* 134 */     if ((paramGSSName instanceof GSSNameImpl)) {
/*     */       try {
/* 136 */         GSSNameSpi localGSSNameSpi = ((GSSNameImpl)paramGSSName).getElement(GSS_KRB5_MECH_OID);
/*     */ 
/* 138 */         String str = localGSSNameSpi.toString();
/* 139 */         if ((localGSSNameSpi instanceof Krb5NameElement)) {
/* 140 */           str = ((Krb5NameElement)localGSSNameSpi).getKrb5PrincipalName().getName();
/*     */         }
/*     */ 
/* 143 */         KerberosPrincipal localKerberosPrincipal = new KerberosPrincipal(str);
/* 144 */         localHashSet3.add(localKerberosPrincipal);
/*     */       } catch (GSSException localGSSException) {
/* 146 */         debug("Skipped name " + paramGSSName + " due to " + localGSSException);
/*     */       }
/*     */     }
/*     */ 
/* 150 */     if ((paramGSSCredential instanceof GSSCredentialImpl)) {
/* 151 */       localSet = ((GSSCredentialImpl)paramGSSCredential).getElements();
/* 152 */       localHashSet1 = new HashSet(localSet.size());
/* 153 */       populateCredentials(localHashSet1, localSet);
/*     */     } else {
/* 155 */       localHashSet1 = new HashSet();
/*     */     }
/* 157 */     debug("Created Subject with the following");
/* 158 */     debug("principals=" + localHashSet3);
/* 159 */     debug("public creds=" + localHashSet2);
/* 160 */     debug("private creds=" + localHashSet1);
/*     */ 
/* 162 */     return new Subject(false, localHashSet3, localHashSet2, localHashSet1);
/*     */   }
/*     */ 
/*     */   private static void populateCredentials(Set<Object> paramSet, Set<?> paramSet1)
/*     */   {
/* 181 */     Iterator localIterator = paramSet1.iterator();
/* 182 */     while (localIterator.hasNext())
/*     */     {
/* 184 */       Object localObject1 = localIterator.next();
/*     */ 
/* 187 */       if ((localObject1 instanceof SpNegoCredElement))
/* 188 */         localObject1 = ((SpNegoCredElement)localObject1).getInternalCred();
/*     */       Object localObject2;
/* 191 */       if ((localObject1 instanceof KerberosTicket)) {
/* 192 */         if (!localObject1.getClass().getName().equals("javax.security.auth.kerberos.KerberosTicket"))
/*     */         {
/* 194 */           localObject2 = (KerberosTicket)localObject1;
/* 195 */           localObject1 = new KerberosTicket(((KerberosTicket)localObject2).getEncoded(), ((KerberosTicket)localObject2).getClient(), ((KerberosTicket)localObject2).getServer(), ((KerberosTicket)localObject2).getSessionKey().getEncoded(), ((KerberosTicket)localObject2).getSessionKeyType(), ((KerberosTicket)localObject2).getFlags(), ((KerberosTicket)localObject2).getAuthTime(), ((KerberosTicket)localObject2).getStartTime(), ((KerberosTicket)localObject2).getEndTime(), ((KerberosTicket)localObject2).getRenewTill(), ((KerberosTicket)localObject2).getClientAddresses());
/*     */         }
/*     */ 
/* 207 */         paramSet.add(localObject1);
/* 208 */       } else if ((localObject1 instanceof KerberosKey)) {
/* 209 */         if (!localObject1.getClass().getName().equals("javax.security.auth.kerberos.KerberosKey"))
/*     */         {
/* 211 */           localObject2 = (KerberosKey)localObject1;
/* 212 */           localObject1 = new KerberosKey(((KerberosKey)localObject2).getPrincipal(), ((KerberosKey)localObject2).getEncoded(), ((KerberosKey)localObject2).getKeyType(), ((KerberosKey)localObject2).getVersionNumber());
/*     */         }
/*     */ 
/* 217 */         paramSet.add(localObject1);
/*     */       }
/*     */       else {
/* 220 */         debug("Skipped cred element: " + localObject1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Subject login(GSSCaller paramGSSCaller, Oid paramOid)
/*     */     throws LoginException
/*     */   {
/* 235 */     Object localObject1 = null;
/* 236 */     if ((paramGSSCaller instanceof HttpCaller)) {
/* 237 */       localObject1 = new NegotiateCallbackHandler(((HttpCaller)paramGSSCaller).info());
/*     */     }
/*     */     else {
/* 240 */       localObject2 = Security.getProperty("auth.login.defaultCallbackHandler");
/*     */ 
/* 243 */       if ((localObject2 != null) && (((String)localObject2).length() != 0))
/* 244 */         localObject1 = null;
/*     */       else {
/* 246 */         localObject1 = new TextCallbackHandler();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 253 */     Object localObject2 = new LoginContext("", null, (CallbackHandler)localObject1, new LoginConfigImpl(paramGSSCaller, paramOid));
/*     */ 
/* 255 */     ((LoginContext)localObject2).login();
/* 256 */     return ((LoginContext)localObject2).getSubject();
/*     */   }
/*     */ 
/*     */   public static boolean useSubjectCredsOnly(GSSCaller paramGSSCaller)
/*     */   {
/* 273 */     if ((paramGSSCaller instanceof HttpCaller)) {
/* 274 */       return false;
/*     */     }
/*     */ 
/* 280 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("javax.security.auth.useSubjectCredsOnly", "true"));
/*     */ 
/* 287 */     return !str.equalsIgnoreCase("false");
/*     */   }
/*     */ 
/*     */   public static boolean useMSInterop()
/*     */   {
/* 302 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.security.spnego.msinterop", "true"));
/*     */ 
/* 309 */     return !str.equalsIgnoreCase("false");
/*     */   }
/*     */ 
/*     */   public static Vector searchSubject(final GSSNameSpi paramGSSNameSpi, final Oid paramOid, final boolean paramBoolean, final Class paramClass)
/*     */   {
/* 323 */     debug("Search Subject for " + getMechStr(paramOid) + (paramBoolean ? " INIT" : " ACCEPT") + " cred (" + (paramGSSNameSpi == null ? "<<DEF>>" : paramGSSNameSpi.toString()) + ", " + paramClass.getName() + ")");
/*     */ 
/* 327 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */     try {
/* 329 */       return (Vector)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Vector run() throws Exception
/*     */         {
/* 333 */           Subject localSubject = Subject.getSubject(this.val$acc);
/* 334 */           Vector localVector = null;
/* 335 */           if (localSubject != null) {
/* 336 */             localVector = new Vector();
/* 337 */             Iterator localIterator = localSubject.getPrivateCredentials(GSSCredentialImpl.class).iterator();
/*     */ 
/* 340 */             while (localIterator.hasNext()) {
/* 341 */               GSSCredentialImpl localGSSCredentialImpl = (GSSCredentialImpl)localIterator.next();
/* 342 */               GSSUtil.debug("...Found cred" + localGSSCredentialImpl);
/*     */               try {
/* 344 */                 GSSCredentialSpi localGSSCredentialSpi = localGSSCredentialImpl.getElement(paramOid, paramBoolean);
/*     */ 
/* 346 */                 GSSUtil.debug("......Found element: " + localGSSCredentialSpi);
/* 347 */                 if ((localGSSCredentialSpi.getClass().equals(paramClass)) && ((paramGSSNameSpi == null) || (paramGSSNameSpi.equals(localGSSCredentialSpi.getName()))))
/*     */                 {
/* 350 */                   localVector.add(localGSSCredentialSpi);
/*     */                 }
/* 352 */                 else GSSUtil.debug("......Discard element"); 
/*     */               }
/*     */               catch (GSSException localGSSException)
/*     */               {
/* 355 */                 GSSUtil.debug("...Discard cred (" + localGSSException + ")");
/*     */               }
/*     */             }
/*     */           } else { GSSUtil.debug("No Subject"); }
/* 359 */           return localVector;
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 364 */       debug("Unexpected exception when searching Subject:");
/* 365 */       if (DEBUG) localPrivilegedActionException.printStackTrace(); 
/*     */     }
/* 366 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.GSSUtil
 * JD-Core Version:    0.6.2
 */