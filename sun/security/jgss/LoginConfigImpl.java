/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import javax.security.auth.login.AppConfigurationEntry;
/*     */ import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
/*     */ import javax.security.auth.login.Configuration;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class LoginConfigImpl extends Configuration
/*     */ {
/*     */   private final Configuration config;
/*     */   private final GSSCaller caller;
/*     */   private final String mechName;
/*  44 */   private static final Debug debug = Debug.getInstance("gssloginconfig", "\t[GSS LoginConfigImpl]");
/*     */ 
/*     */   public LoginConfigImpl(GSSCaller paramGSSCaller, Oid paramOid)
/*     */   {
/*  55 */     this.caller = paramGSSCaller;
/*     */ 
/*  57 */     if (paramOid.equals(GSSUtil.GSS_KRB5_MECH_OID))
/*  58 */       this.mechName = "krb5";
/*     */     else {
/*  60 */       throw new IllegalArgumentException(paramOid.toString() + " not supported");
/*     */     }
/*  62 */     this.config = ((Configuration)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Configuration run() {
/*  65 */         return Configuration.getConfiguration();
/*     */       }
/*     */     }));
/*     */   }
/*     */ 
/*     */   public AppConfigurationEntry[] getAppConfigurationEntry(String paramString)
/*     */   {
/*  77 */     AppConfigurationEntry[] arrayOfAppConfigurationEntry = null;
/*     */ 
/*  80 */     if ("OTHER".equalsIgnoreCase(paramString)) {
/*  81 */       return null;
/*     */     }
/*     */ 
/*  84 */     String[] arrayOfString1 = null;
/*     */ 
/*  90 */     if ("krb5".equals(this.mechName)) {
/*  91 */       if (this.caller == GSSCaller.CALLER_INITIATE) {
/*  92 */         arrayOfString1 = new String[] { "com.sun.security.jgss.krb5.initiate", "com.sun.security.jgss.initiate" };
/*     */       }
/*  96 */       else if (this.caller == GSSCaller.CALLER_ACCEPT) {
/*  97 */         arrayOfString1 = new String[] { "com.sun.security.jgss.krb5.accept", "com.sun.security.jgss.accept" };
/*     */       }
/* 101 */       else if (this.caller == GSSCaller.CALLER_SSL_CLIENT) {
/* 102 */         arrayOfString1 = new String[] { "com.sun.security.jgss.krb5.initiate", "com.sun.net.ssl.client" };
/*     */       }
/* 106 */       else if (this.caller == GSSCaller.CALLER_SSL_SERVER) {
/* 107 */         arrayOfString1 = new String[] { "com.sun.security.jgss.krb5.accept", "com.sun.net.ssl.server" };
/*     */       }
/* 111 */       else if ((this.caller instanceof HttpCaller)) {
/* 112 */         arrayOfString1 = new String[] { "com.sun.security.jgss.krb5.initiate" };
/*     */       }
/* 115 */       else if (this.caller == GSSCaller.CALLER_UNKNOWN)
/* 116 */         throw new AssertionError("caller not defined");
/*     */     }
/*     */     else {
/* 119 */       throw new IllegalArgumentException(this.mechName + " not supported");
/*     */     }
/*     */ 
/* 144 */     for (String str : arrayOfString1) {
/* 145 */       arrayOfAppConfigurationEntry = this.config.getAppConfigurationEntry(str);
/* 146 */       if (debug != null) {
/* 147 */         debug.println("Trying " + str + (arrayOfAppConfigurationEntry == null ? ": does not exist." : ": Found!"));
/*     */       }
/*     */ 
/* 150 */       if (arrayOfAppConfigurationEntry != null)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 155 */     if (arrayOfAppConfigurationEntry == null) {
/* 156 */       if (debug != null) {
/* 157 */         debug.println("Cannot read JGSS entry, use default values instead.");
/*     */       }
/* 159 */       arrayOfAppConfigurationEntry = getDefaultConfigurationEntry();
/*     */     }
/* 161 */     return arrayOfAppConfigurationEntry;
/*     */   }
/*     */ 
/*     */   private AppConfigurationEntry[] getDefaultConfigurationEntry()
/*     */   {
/* 169 */     HashMap localHashMap = new HashMap(2);
/*     */ 
/* 171 */     if ((this.mechName == null) || (this.mechName.equals("krb5"))) {
/* 172 */       if (isServerSide(this.caller))
/*     */       {
/* 175 */         localHashMap.put("useKeyTab", "true");
/* 176 */         localHashMap.put("storeKey", "true");
/* 177 */         localHashMap.put("doNotPrompt", "true");
/* 178 */         localHashMap.put("isInitiator", "false");
/*     */       } else {
/* 180 */         localHashMap.put("useTicketCache", "true");
/* 181 */         localHashMap.put("doNotPrompt", "false");
/*     */       }
/* 183 */       return new AppConfigurationEntry[] { new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, localHashMap) };
/*     */     }
/*     */ 
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   private static boolean isServerSide(GSSCaller paramGSSCaller) {
/* 194 */     return (GSSCaller.CALLER_ACCEPT == paramGSSCaller) || (GSSCaller.CALLER_SSL_SERVER == paramGSSCaller);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.LoginConfigImpl
 * JD-Core Version:    0.6.2
 */