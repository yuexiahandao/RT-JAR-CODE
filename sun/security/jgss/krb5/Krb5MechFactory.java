/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.Provider;
/*     */ import java.util.Vector;
/*     */ import javax.security.auth.kerberos.ServicePermission;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSCaller;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.SunProvider;
/*     */ import sun.security.jgss.spi.GSSContextSpi;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.jgss.spi.MechanismFactory;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public final class Krb5MechFactory
/*     */   implements MechanismFactory
/*     */ {
/*  46 */   private static final boolean DEBUG = Krb5Util.DEBUG;
/*     */ 
/*  48 */   static final Provider PROVIDER = new SunProvider();
/*     */ 
/*  51 */   static final Oid GSS_KRB5_MECH_OID = createOid("1.2.840.113554.1.2.2");
/*     */ 
/*  54 */   static final Oid NT_GSS_KRB5_PRINCIPAL = createOid("1.2.840.113554.1.2.2.1");
/*     */ 
/*  57 */   private static Oid[] nameTypes = { GSSName.NT_USER_NAME, GSSName.NT_HOSTBASED_SERVICE, GSSName.NT_EXPORT_NAME, NT_GSS_KRB5_PRINCIPAL };
/*     */   private final GSSCaller caller;
/*     */ 
/*     */   private static Krb5CredElement getCredFromSubject(GSSNameSpi paramGSSNameSpi, boolean paramBoolean)
/*     */     throws GSSException
/*     */   {
/*  68 */     Vector localVector = GSSUtil.searchSubject(paramGSSNameSpi, GSS_KRB5_MECH_OID, paramBoolean, paramBoolean ? Krb5InitCredential.class : Krb5AcceptCredential.class);
/*     */ 
/*  74 */     Krb5CredElement localKrb5CredElement = (localVector == null) || (localVector.isEmpty()) ? null : (Krb5CredElement)localVector.firstElement();
/*     */ 
/*  78 */     if (localKrb5CredElement != null) {
/*  79 */       if (paramBoolean)
/*  80 */         checkInitCredPermission((Krb5NameElement)localKrb5CredElement.getName());
/*     */       else {
/*  82 */         checkAcceptCredPermission((Krb5NameElement)localKrb5CredElement.getName(), paramGSSNameSpi);
/*     */       }
/*     */     }
/*     */ 
/*  86 */     return localKrb5CredElement;
/*     */   }
/*     */ 
/*     */   public Krb5MechFactory(GSSCaller paramGSSCaller) {
/*  90 */     this.caller = paramGSSCaller;
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(String paramString, Oid paramOid) throws GSSException
/*     */   {
/*  95 */     return Krb5NameElement.getInstance(paramString, paramOid);
/*     */   }
/*     */ 
/*     */   public GSSNameSpi getNameElement(byte[] paramArrayOfByte, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 103 */     return Krb5NameElement.getInstance(new String(paramArrayOfByte), paramOid);
/*     */   }
/*     */ 
/*     */   public GSSCredentialSpi getCredentialElement(GSSNameSpi paramGSSNameSpi, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws GSSException
/*     */   {
/* 110 */     if ((paramGSSNameSpi != null) && (!(paramGSSNameSpi instanceof Krb5NameElement))) {
/* 111 */       paramGSSNameSpi = Krb5NameElement.getInstance(paramGSSNameSpi.toString(), paramGSSNameSpi.getStringNameType());
/*     */     }
/*     */ 
/* 115 */     Object localObject = getCredFromSubject(paramGSSNameSpi, paramInt3 != 2);
/*     */ 
/* 118 */     if (localObject == null) {
/* 119 */       if ((paramInt3 == 1) || (paramInt3 == 0))
/*     */       {
/* 121 */         localObject = Krb5InitCredential.getInstance(this.caller, (Krb5NameElement)paramGSSNameSpi, paramInt1);
/*     */ 
/* 123 */         checkInitCredPermission((Krb5NameElement)((Krb5CredElement)localObject).getName());
/*     */       }
/* 125 */       else if (paramInt3 == 2) {
/* 126 */         localObject = Krb5AcceptCredential.getInstance(this.caller, (Krb5NameElement)paramGSSNameSpi);
/*     */ 
/* 129 */         checkAcceptCredPermission((Krb5NameElement)((Krb5CredElement)localObject).getName(), paramGSSNameSpi);
/*     */       }
/*     */       else {
/* 132 */         throw new GSSException(11, -1, "Unknown usage mode requested");
/*     */       }
/*     */     }
/* 135 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static void checkInitCredPermission(Krb5NameElement paramKrb5NameElement) {
/* 139 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 140 */     if (localSecurityManager != null) {
/* 141 */       String str1 = paramKrb5NameElement.getKrb5PrincipalName().getRealmAsString();
/* 142 */       String str2 = new String("krbtgt/" + str1 + '@' + str1);
/*     */ 
/* 144 */       ServicePermission localServicePermission = new ServicePermission(str2, "initiate");
/*     */       try
/*     */       {
/* 147 */         localSecurityManager.checkPermission(localServicePermission);
/*     */       } catch (SecurityException localSecurityException) {
/* 149 */         if (DEBUG) {
/* 150 */           System.out.println("Permission to initiatekerberos init credential" + localSecurityException.getMessage());
/*     */         }
/*     */ 
/* 153 */         throw localSecurityException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void checkAcceptCredPermission(Krb5NameElement paramKrb5NameElement, GSSNameSpi paramGSSNameSpi)
/*     */   {
/* 160 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 161 */     if (localSecurityManager != null) {
/* 162 */       ServicePermission localServicePermission = new ServicePermission(paramKrb5NameElement.getKrb5PrincipalName().getName(), "accept");
/*     */       try
/*     */       {
/* 165 */         localSecurityManager.checkPermission(localServicePermission);
/*     */       }
/*     */       catch (SecurityException localSecurityException1)
/*     */       {
/*     */         SecurityException localSecurityException2;
/* 167 */         if (paramGSSNameSpi == null)
/*     */         {
/* 169 */           localSecurityException2 = new SecurityException("No permission to acquire Kerberos accept credential");
/*     */         }
/*     */ 
/* 173 */         throw localSecurityException2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(GSSNameSpi paramGSSNameSpi, GSSCredentialSpi paramGSSCredentialSpi, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 181 */     if ((paramGSSNameSpi != null) && (!(paramGSSNameSpi instanceof Krb5NameElement))) {
/* 182 */       paramGSSNameSpi = Krb5NameElement.getInstance(paramGSSNameSpi.toString(), paramGSSNameSpi.getStringNameType());
/*     */     }
/*     */ 
/* 186 */     if (paramGSSCredentialSpi == null) {
/* 187 */       paramGSSCredentialSpi = getCredentialElement(null, paramInt, 0, 1);
/*     */     }
/*     */ 
/* 190 */     return new Krb5Context(this.caller, (Krb5NameElement)paramGSSNameSpi, (Krb5CredElement)paramGSSCredentialSpi, paramInt);
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(GSSCredentialSpi paramGSSCredentialSpi)
/*     */     throws GSSException
/*     */   {
/* 197 */     if (paramGSSCredentialSpi == null) {
/* 198 */       paramGSSCredentialSpi = getCredentialElement(null, 0, 2147483647, 2);
/*     */     }
/*     */ 
/* 201 */     return new Krb5Context(this.caller, (Krb5CredElement)paramGSSCredentialSpi);
/*     */   }
/*     */ 
/*     */   public GSSContextSpi getMechanismContext(byte[] paramArrayOfByte) throws GSSException
/*     */   {
/* 206 */     return new Krb5Context(this.caller, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final Oid getMechanismOid()
/*     */   {
/* 211 */     return GSS_KRB5_MECH_OID;
/*     */   }
/*     */ 
/*     */   public Provider getProvider() {
/* 215 */     return PROVIDER;
/*     */   }
/*     */ 
/*     */   public Oid[] getNameTypes()
/*     */   {
/* 220 */     return nameTypes;
/*     */   }
/*     */ 
/*     */   private static Oid createOid(String paramString) {
/* 224 */     Oid localOid = null;
/*     */     try {
/* 226 */       localOid = new Oid(paramString);
/*     */     }
/*     */     catch (GSSException localGSSException) {
/*     */     }
/* 230 */     return localOid;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5MechFactory
 * JD-Core Version:    0.6.2
 */