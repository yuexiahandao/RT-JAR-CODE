/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Provider;
/*     */ import java.util.Date;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import javax.security.auth.kerberos.KerberosPrincipal;
/*     */ import javax.security.auth.kerberos.KerberosTicket;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSCaller;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ public class Krb5InitCredential extends KerberosTicket
/*     */   implements Krb5CredElement
/*     */ {
/*     */   private static final long serialVersionUID = 7723415700837898232L;
/*     */   private Krb5NameElement name;
/*     */   private Credentials krb5Credentials;
/*     */ 
/*     */   private Krb5InitCredential(Krb5NameElement paramKrb5NameElement, byte[] paramArrayOfByte1, KerberosPrincipal paramKerberosPrincipal1, KerberosPrincipal paramKerberosPrincipal2, byte[] paramArrayOfByte2, int paramInt, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */     throws GSSException
/*     */   {
/*  72 */     super(paramArrayOfByte1, paramKerberosPrincipal1, paramKerberosPrincipal2, paramArrayOfByte2, paramInt, paramArrayOfBoolean, paramDate1, paramDate2, paramDate3, paramDate4, paramArrayOfInetAddress);
/*     */ 
/*  84 */     this.name = paramKrb5NameElement;
/*     */     try
/*     */     {
/*  88 */       this.krb5Credentials = new Credentials(paramArrayOfByte1, paramKerberosPrincipal1.getName(), paramKerberosPrincipal2.getName(), paramArrayOfByte2, paramInt, paramArrayOfBoolean, paramDate1, paramDate2, paramDate3, paramDate4, paramArrayOfInetAddress);
/*     */     }
/*     */     catch (KrbException localKrbException)
/*     */     {
/* 100 */       throw new GSSException(13, -1, localKrbException.getMessage());
/*     */     }
/*     */     catch (IOException localIOException) {
/* 103 */       throw new GSSException(13, -1, localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Krb5InitCredential(Krb5NameElement paramKrb5NameElement, Credentials paramCredentials, byte[] paramArrayOfByte1, KerberosPrincipal paramKerberosPrincipal1, KerberosPrincipal paramKerberosPrincipal2, byte[] paramArrayOfByte2, int paramInt, boolean[] paramArrayOfBoolean, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4, InetAddress[] paramArrayOfInetAddress)
/*     */     throws GSSException
/*     */   {
/* 123 */     super(paramArrayOfByte1, paramKerberosPrincipal1, paramKerberosPrincipal2, paramArrayOfByte2, paramInt, paramArrayOfBoolean, paramDate1, paramDate2, paramDate3, paramDate4, paramArrayOfInetAddress);
/*     */ 
/* 135 */     this.name = paramKrb5NameElement;
/*     */ 
/* 138 */     this.krb5Credentials = paramCredentials;
/*     */   }
/*     */ 
/*     */   static Krb5InitCredential getInstance(GSSCaller paramGSSCaller, Krb5NameElement paramKrb5NameElement, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 145 */     KerberosTicket localKerberosTicket = getTgt(paramGSSCaller, paramKrb5NameElement, paramInt);
/* 146 */     if (localKerberosTicket == null) {
/* 147 */       throw new GSSException(13, -1, "Failed to find any Kerberos tgt");
/*     */     }
/*     */ 
/* 150 */     if (paramKrb5NameElement == null) {
/* 151 */       String str = localKerberosTicket.getClient().getName();
/* 152 */       paramKrb5NameElement = Krb5NameElement.getInstance(str, Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
/*     */     }
/*     */ 
/* 156 */     return new Krb5InitCredential(paramKrb5NameElement, localKerberosTicket.getEncoded(), localKerberosTicket.getClient(), localKerberosTicket.getServer(), localKerberosTicket.getSessionKey().getEncoded(), localKerberosTicket.getSessionKeyType(), localKerberosTicket.getFlags(), localKerberosTicket.getAuthTime(), localKerberosTicket.getStartTime(), localKerberosTicket.getEndTime(), localKerberosTicket.getRenewTill(), localKerberosTicket.getClientAddresses());
/*     */   }
/*     */ 
/*     */   static Krb5InitCredential getInstance(Krb5NameElement paramKrb5NameElement, Credentials paramCredentials)
/*     */     throws GSSException
/*     */   {
/* 174 */     EncryptionKey localEncryptionKey = paramCredentials.getSessionKey();
/*     */ 
/* 181 */     PrincipalName localPrincipalName1 = paramCredentials.getClient();
/* 182 */     PrincipalName localPrincipalName2 = paramCredentials.getServer();
/*     */ 
/* 184 */     KerberosPrincipal localKerberosPrincipal1 = null;
/* 185 */     KerberosPrincipal localKerberosPrincipal2 = null;
/*     */ 
/* 187 */     Krb5NameElement localKrb5NameElement = null;
/*     */ 
/* 189 */     if (localPrincipalName1 != null) {
/* 190 */       String str = localPrincipalName1.getName();
/* 191 */       localKrb5NameElement = Krb5NameElement.getInstance(str, Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
/*     */ 
/* 193 */       localKerberosPrincipal1 = new KerberosPrincipal(str);
/*     */     }
/*     */ 
/* 198 */     if (localPrincipalName2 != null) {
/* 199 */       localKerberosPrincipal2 = new KerberosPrincipal(localPrincipalName2.getName(), 2);
/*     */     }
/*     */ 
/* 204 */     return new Krb5InitCredential(localKrb5NameElement, paramCredentials, paramCredentials.getEncoded(), localKerberosPrincipal1, localKerberosPrincipal2, localEncryptionKey.getBytes(), localEncryptionKey.getEType(), paramCredentials.getFlags(), paramCredentials.getAuthTime(), paramCredentials.getStartTime(), paramCredentials.getEndTime(), paramCredentials.getRenewTill(), paramCredentials.getClientAddresses());
/*     */   }
/*     */ 
/*     */   public final GSSNameSpi getName()
/*     */     throws GSSException
/*     */   {
/* 227 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getInitLifetime()
/*     */     throws GSSException
/*     */   {
/* 237 */     int i = 0;
/* 238 */     i = (int)(getEndTime().getTime() - new Date().getTime());
/*     */ 
/* 241 */     return i / 1000;
/*     */   }
/*     */ 
/*     */   public int getAcceptLifetime()
/*     */     throws GSSException
/*     */   {
/* 251 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isInitiatorCredential() throws GSSException {
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isAcceptorCredential() throws GSSException {
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   public final Oid getMechanism()
/*     */   {
/* 270 */     return Krb5MechFactory.GSS_KRB5_MECH_OID;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider() {
/* 274 */     return Krb5MechFactory.PROVIDER;
/*     */   }
/*     */ 
/*     */   Credentials getKrb5Credentials()
/*     */   {
/* 283 */     return this.krb5Credentials;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/* 296 */       destroy();
/*     */     } catch (DestroyFailedException localDestroyFailedException) {
/* 298 */       GSSException localGSSException = new GSSException(11, -1, "Could not destroy credentials - " + localDestroyFailedException.getMessage());
/*     */ 
/* 301 */       localGSSException.initCause(localDestroyFailedException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static KerberosTicket getTgt(GSSCaller paramGSSCaller, Krb5NameElement paramKrb5NameElement, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 312 */     String str1 = null;
/* 313 */     final String str3 = null;
/*     */     final String str2;
/*     */     Object localObject;
/* 319 */     if (paramKrb5NameElement != null) {
/* 320 */       str2 = paramKrb5NameElement.getKrb5PrincipalName().getName();
/* 321 */       str1 = paramKrb5NameElement.getKrb5PrincipalName().getRealmAsString();
/*     */     } else {
/* 323 */       str2 = null;
/*     */       try {
/* 325 */         Config localConfig = Config.getInstance();
/* 326 */         str1 = localConfig.getDefaultRealm();
/*     */       } catch (KrbException localKrbException) {
/* 328 */         localObject = new GSSException(13, -1, "Attempt to obtain INITIATE credentials failed! (" + localKrbException.getMessage() + ")");
/*     */ 
/* 332 */         ((GSSException)localObject).initCause(localKrbException);
/* 333 */         throw ((Throwable)localObject);
/*     */       }
/*     */     }
/*     */ 
/* 337 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */     try
/*     */     {
/* 340 */       localObject = paramGSSCaller == GSSCaller.CALLER_UNKNOWN ? GSSCaller.CALLER_INITIATE : paramGSSCaller;
/*     */ 
/* 343 */       return (KerberosTicket)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public KerberosTicket run() throws Exception {
/* 346 */           return Krb5Util.getTicket(this.val$realCaller, str2, str3, localAccessControlContext);
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 351 */       GSSException localGSSException = new GSSException(13, -1, "Attempt to obtain new INITIATE credentials failed! (" + localPrivilegedActionException.getMessage() + ")");
/*     */ 
/* 355 */       localGSSException.initCause(localPrivilegedActionException.getException());
/* 356 */       throw localGSSException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5InitCredential
 * JD-Core Version:    0.6.2
 */