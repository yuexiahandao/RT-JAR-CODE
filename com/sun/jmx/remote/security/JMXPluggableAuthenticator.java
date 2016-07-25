/*     */ package com.sun.jmx.remote.security;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.remote.JMXAuthenticator;
/*     */ import javax.security.auth.AuthPermission;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.auth.login.AppConfigurationEntry;
/*     */ import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
/*     */ import javax.security.auth.login.Configuration;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import javax.security.auth.login.LoginException;
/*     */ 
/*     */ public final class JMXPluggableAuthenticator
/*     */   implements JMXAuthenticator
/*     */ {
/*     */   private LoginContext loginContext;
/*     */   private String username;
/*     */   private String password;
/*     */   private static final String LOGIN_CONFIG_PROP = "jmx.remote.x.login.config";
/*     */   private static final String LOGIN_CONFIG_NAME = "JMXPluggableAuthenticator";
/*     */   private static final String PASSWORD_FILE_PROP = "jmx.remote.x.password.file";
/* 253 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXPluggableAuthenticator");
/*     */ 
/*     */   public JMXPluggableAuthenticator(Map<?, ?> paramMap)
/*     */   {
/*  92 */     String str1 = null;
/*  93 */     String str2 = null;
/*     */ 
/*  95 */     if (paramMap != null) {
/*  96 */       str1 = (String)paramMap.get("jmx.remote.x.login.config");
/*  97 */       str2 = (String)paramMap.get("jmx.remote.x.password.file");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 102 */       if (str1 != null)
/*     */       {
/* 104 */         this.loginContext = new LoginContext(str1, new JMXCallbackHandler(null));
/*     */       }
/*     */       else
/*     */       {
/* 109 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 110 */         if (localSecurityManager != null) {
/* 111 */           localSecurityManager.checkPermission(new AuthPermission("createLoginContext.JMXPluggableAuthenticator"));
/*     */         }
/*     */ 
/* 116 */         final String str3 = str2;
/*     */         try {
/* 118 */           this.loginContext = ((LoginContext)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public LoginContext run() throws LoginException {
/* 121 */               return new LoginContext("JMXPluggableAuthenticator", null, new JMXPluggableAuthenticator.JMXCallbackHandler(JMXPluggableAuthenticator.this, null), new JMXPluggableAuthenticator.FileLoginConfig(str3));
/*     */             }
/*     */ 
/*     */           }));
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException)
/*     */         {
/* 129 */           throw ((LoginException)localPrivilegedActionException.getException());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (LoginException localLoginException) {
/* 134 */       authenticationFailure("authenticate", localLoginException);
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 137 */       authenticationFailure("authenticate", localSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Subject authenticate(Object paramObject)
/*     */   {
/* 160 */     if (!(paramObject instanceof String[]))
/*     */     {
/* 162 */       if (paramObject == null) {
/* 163 */         authenticationFailure("authenticate", "Credentials required");
/*     */       }
/* 165 */       localObject1 = "Credentials should be String[] instead of " + paramObject.getClass().getName();
/*     */ 
/* 168 */       authenticationFailure("authenticate", (String)localObject1);
/*     */     }
/*     */ 
/* 172 */     Object localObject1 = (String[])paramObject;
/*     */     Object localObject2;
/* 173 */     if (localObject1.length != 2) {
/* 174 */       localObject2 = "Credentials should have 2 elements not " + localObject1.length;
/*     */ 
/* 177 */       authenticationFailure("authenticate", (String)localObject2);
/*     */     }
/*     */ 
/* 182 */     this.username = localObject1[0];
/* 183 */     this.password = localObject1[1];
/* 184 */     if ((this.username == null) || (this.password == null))
/*     */     {
/* 186 */       authenticationFailure("authenticate", "Username or password is null");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 191 */       this.loginContext.login();
/* 192 */       localObject2 = this.loginContext.getSubject();
/* 193 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 195 */           this.val$subject.setReadOnly();
/* 196 */           return null;
/*     */         }
/*     */       });
/* 200 */       return localObject2;
/*     */     }
/*     */     catch (LoginException localLoginException) {
/* 203 */       authenticationFailure("authenticate", localLoginException);
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   private static void authenticationFailure(String paramString1, String paramString2) throws SecurityException
/*     */   {
/* 210 */     String str = "Authentication failed! " + paramString2;
/* 211 */     SecurityException localSecurityException = new SecurityException(str);
/* 212 */     logException(paramString1, str, localSecurityException);
/* 213 */     throw localSecurityException;
/*     */   }
/*     */ 
/*     */   private static void authenticationFailure(String paramString, Exception paramException)
/*     */     throws SecurityException
/*     */   {
/*     */     String str;
/*     */     Object localObject;
/* 221 */     if ((paramException instanceof SecurityException)) {
/* 222 */       str = paramException.getMessage();
/* 223 */       localObject = (SecurityException)paramException;
/*     */     } else {
/* 225 */       str = "Authentication failed! " + paramException.getMessage();
/* 226 */       SecurityException localSecurityException = new SecurityException(str);
/* 227 */       EnvHelp.initCause(localSecurityException, paramException);
/* 228 */       localObject = localSecurityException;
/*     */     }
/* 230 */     logException(paramString, str, (Exception)localObject);
/* 231 */     throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   private static void logException(String paramString1, String paramString2, Exception paramException)
/*     */   {
/* 237 */     if (logger.traceOn()) {
/* 238 */       logger.trace(paramString1, paramString2);
/*     */     }
/* 240 */     if (logger.debugOn())
/* 241 */       logger.debug(paramString1, paramException);
/*     */   }
/*     */ 
/*     */   private static class FileLoginConfig extends Configuration
/*     */   {
/*     */     private AppConfigurationEntry[] entries;
/* 301 */     private static final String FILE_LOGIN_MODULE = FileLoginModule.class.getName();
/*     */     private static final String PASSWORD_FILE_OPTION = "passwordFile";
/*     */ 
/*     */     public FileLoginConfig(String paramString)
/*     */     {
/*     */       Object localObject;
/* 316 */       if (paramString != null) {
/* 317 */         localObject = new HashMap(1);
/* 318 */         ((Map)localObject).put("passwordFile", paramString);
/*     */       } else {
/* 320 */         localObject = Collections.emptyMap();
/*     */       }
/*     */ 
/* 323 */       this.entries = new AppConfigurationEntry[] { new AppConfigurationEntry(FILE_LOGIN_MODULE, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, (Map)localObject) };
/*     */     }
/*     */ 
/*     */     public AppConfigurationEntry[] getAppConfigurationEntry(String paramString)
/*     */     {
/* 335 */       return paramString.equals("JMXPluggableAuthenticator") ? this.entries : null;
/*     */     }
/*     */ 
/*     */     public void refresh()
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class JMXCallbackHandler
/*     */     implements CallbackHandler
/*     */   {
/*     */     private JMXCallbackHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void handle(Callback[] paramArrayOfCallback)
/*     */       throws IOException, UnsupportedCallbackException
/*     */     {
/* 270 */       for (int i = 0; i < paramArrayOfCallback.length; i++)
/* 271 */         if ((paramArrayOfCallback[i] instanceof NameCallback)) {
/* 272 */           ((NameCallback)paramArrayOfCallback[i]).setName(JMXPluggableAuthenticator.this.username);
/*     */         }
/* 274 */         else if ((paramArrayOfCallback[i] instanceof PasswordCallback)) {
/* 275 */           ((PasswordCallback)paramArrayOfCallback[i]).setPassword(JMXPluggableAuthenticator.this.password.toCharArray());
/*     */         }
/*     */         else
/*     */         {
/* 279 */           throw new UnsupportedCallbackException(paramArrayOfCallback[i], "Unrecognized Callback");
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.JMXPluggableAuthenticator
 * JD-Core Version:    0.6.2
 */