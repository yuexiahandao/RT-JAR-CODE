/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Provider;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSCaller;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public class Krb5AcceptCredential
/*     */   implements Krb5CredElement
/*     */ {
/*     */   private static final long serialVersionUID = 7714332137352567952L;
/*     */   private Krb5NameElement name;
/*     */   private Krb5Util.ServiceCreds screds;
/*     */ 
/*     */   private Krb5AcceptCredential(Krb5NameElement paramKrb5NameElement, Krb5Util.ServiceCreds paramServiceCreds)
/*     */   {
/*  60 */     this.name = paramKrb5NameElement;
/*  61 */     this.screds = paramServiceCreds;
/*     */   }
/*     */ 
/*     */   static Krb5AcceptCredential getInstance(GSSCaller paramGSSCaller, Krb5NameElement paramKrb5NameElement)
/*     */     throws GSSException
/*     */   {
/*  67 */     final String str1 = paramKrb5NameElement == null ? null : paramKrb5NameElement.getKrb5PrincipalName().getName();
/*     */ 
/*  69 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */ 
/*  71 */     Krb5Util.ServiceCreds localServiceCreds = null;
/*     */     try {
/*  73 */       localServiceCreds = (Krb5Util.ServiceCreds)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Krb5Util.ServiceCreds run() throws Exception {
/*  76 */           return Krb5Util.getServiceCreds(this.val$caller == GSSCaller.CALLER_UNKNOWN ? GSSCaller.CALLER_ACCEPT : this.val$caller, str1, localAccessControlContext);
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/*  81 */       GSSException localGSSException = new GSSException(13, -1, "Attempt to obtain new ACCEPT credentials failed!");
/*     */ 
/*  84 */       localGSSException.initCause(localPrivilegedActionException.getException());
/*  85 */       throw localGSSException;
/*     */     }
/*     */ 
/*  88 */     if (localServiceCreds == null) {
/*  89 */       throw new GSSException(13, -1, "Failed to find any Kerberos credentails");
/*     */     }
/*     */ 
/*  92 */     if (paramKrb5NameElement == null) {
/*  93 */       String str2 = localServiceCreds.getName();
/*  94 */       paramKrb5NameElement = Krb5NameElement.getInstance(str2, Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
/*     */     }
/*     */ 
/*  98 */     return new Krb5AcceptCredential(paramKrb5NameElement, localServiceCreds);
/*     */   }
/*     */ 
/*     */   public final GSSNameSpi getName()
/*     */     throws GSSException
/*     */   {
/* 109 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getInitLifetime()
/*     */     throws GSSException
/*     */   {
/* 119 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getAcceptLifetime()
/*     */     throws GSSException
/*     */   {
/* 129 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public boolean isInitiatorCredential() throws GSSException {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAcceptorCredential() throws GSSException {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   public final Oid getMechanism()
/*     */   {
/* 148 */     return Krb5MechFactory.GSS_KRB5_MECH_OID;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider() {
/* 152 */     return Krb5MechFactory.PROVIDER;
/*     */   }
/*     */ 
/*     */   EncryptionKey[] getKrb5EncryptionKeys() {
/* 156 */     return this.screds.getEKeys();
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/* 164 */       destroy();
/*     */     } catch (DestroyFailedException localDestroyFailedException) {
/* 166 */       GSSException localGSSException = new GSSException(11, -1, "Could not destroy credentials - " + localDestroyFailedException.getMessage());
/*     */ 
/* 169 */       localGSSException.initCause(localDestroyFailedException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */     throws DestroyFailedException
/*     */   {
/* 178 */     this.screds.destroy();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5AcceptCredential
 * JD-Core Version:    0.6.2
 */