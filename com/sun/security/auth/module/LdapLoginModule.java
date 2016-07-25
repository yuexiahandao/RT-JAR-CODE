/*      */ package com.sun.security.auth.module;
/*      */ 
/*      */ import com.sun.security.auth.LdapPrincipal;
/*      */ import com.sun.security.auth.UserPrincipal;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.naming.Context;
/*      */ import javax.naming.InvalidNameException;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.SearchControls;
/*      */ import javax.naming.directory.SearchResult;
/*      */ import javax.naming.ldap.InitialLdapContext;
/*      */ import javax.naming.ldap.LdapContext;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.security.auth.callback.Callback;
/*      */ import javax.security.auth.callback.CallbackHandler;
/*      */ import javax.security.auth.callback.NameCallback;
/*      */ import javax.security.auth.callback.PasswordCallback;
/*      */ import javax.security.auth.callback.UnsupportedCallbackException;
/*      */ import javax.security.auth.login.FailedLoginException;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import javax.security.auth.spi.LoginModule;
/*      */ 
/*      */ public class LdapLoginModule
/*      */   implements LoginModule
/*      */ {
/*  313 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public ResourceBundle run() {
/*  316 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*      */     }
/*      */   });
/*      */   private static final String USERNAME_KEY = "javax.security.auth.login.name";
/*      */   private static final String PASSWORD_KEY = "javax.security.auth.login.password";
/*      */   private static final String USER_PROVIDER = "userProvider";
/*      */   private static final String USER_FILTER = "userFilter";
/*      */   private static final String AUTHC_IDENTITY = "authIdentity";
/*      */   private static final String AUTHZ_IDENTITY = "authzIdentity";
/*      */   private static final String USERNAME_TOKEN = "{USERNAME}";
/*  335 */   private static final Pattern USERNAME_PATTERN = Pattern.compile("\\{USERNAME\\}");
/*      */   private String userProvider;
/*      */   private String userFilter;
/*      */   private String authcIdentity;
/*      */   private String authzIdentity;
/*  343 */   private String authzIdentityAttr = null;
/*  344 */   private boolean useSSL = true;
/*  345 */   private boolean authFirst = false;
/*  346 */   private boolean authOnly = false;
/*  347 */   private boolean useFirstPass = false;
/*  348 */   private boolean tryFirstPass = false;
/*  349 */   private boolean storePass = false;
/*  350 */   private boolean clearPass = false;
/*  351 */   private boolean debug = false;
/*      */ 
/*  354 */   private boolean succeeded = false;
/*  355 */   private boolean commitSucceeded = false;
/*      */   private String username;
/*      */   private char[] password;
/*      */   private LdapPrincipal ldapPrincipal;
/*      */   private UserPrincipal userPrincipal;
/*      */   private UserPrincipal authzPrincipal;
/*      */   private Subject subject;
/*      */   private CallbackHandler callbackHandler;
/*      */   private Map sharedState;
/*      */   private Map<String, ?> options;
/*      */   private LdapContext ctx;
/*  372 */   private Matcher identityMatcher = null;
/*  373 */   private Matcher filterMatcher = null;
/*      */   private Hashtable ldapEnvironment;
/*  375 */   private SearchControls constraints = null;
/*      */ 
/*      */   public void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2)
/*      */   {
/*  391 */     this.subject = paramSubject;
/*  392 */     this.callbackHandler = paramCallbackHandler;
/*  393 */     this.sharedState = paramMap1;
/*  394 */     this.options = paramMap2;
/*      */ 
/*  396 */     this.ldapEnvironment = new Hashtable(9);
/*  397 */     this.ldapEnvironment.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
/*      */ 
/*  401 */     for (String str : paramMap2.keySet()) {
/*  402 */       if (str.indexOf(".") > -1) {
/*  403 */         this.ldapEnvironment.put(str, paramMap2.get(str));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  409 */     this.userProvider = ((String)paramMap2.get("userProvider"));
/*  410 */     if (this.userProvider != null) {
/*  411 */       this.ldapEnvironment.put("java.naming.provider.url", this.userProvider);
/*      */     }
/*      */ 
/*  414 */     this.authcIdentity = ((String)paramMap2.get("authIdentity"));
/*  415 */     if ((this.authcIdentity != null) && (this.authcIdentity.indexOf("{USERNAME}") != -1))
/*      */     {
/*  417 */       this.identityMatcher = USERNAME_PATTERN.matcher(this.authcIdentity);
/*      */     }
/*      */ 
/*  420 */     this.userFilter = ((String)paramMap2.get("userFilter"));
/*  421 */     if (this.userFilter != null) {
/*  422 */       if (this.userFilter.indexOf("{USERNAME}") != -1) {
/*  423 */         this.filterMatcher = USERNAME_PATTERN.matcher(this.userFilter);
/*      */       }
/*  425 */       this.constraints = new SearchControls();
/*  426 */       this.constraints.setSearchScope(2);
/*  427 */       this.constraints.setReturningAttributes(new String[0]);
/*  428 */       this.constraints.setReturningObjFlag(true);
/*      */     }
/*      */ 
/*  431 */     this.authzIdentity = ((String)paramMap2.get("authzIdentity"));
/*  432 */     if ((this.authzIdentity != null) && (this.authzIdentity.startsWith("{")) && (this.authzIdentity.endsWith("}")))
/*      */     {
/*  434 */       if (this.constraints != null) {
/*  435 */         this.authzIdentityAttr = this.authzIdentity.substring(1, this.authzIdentity.length() - 1);
/*      */ 
/*  437 */         this.constraints.setReturningAttributes(new String[] { this.authzIdentityAttr });
/*      */       }
/*      */ 
/*  440 */       this.authzIdentity = null;
/*      */     }
/*      */ 
/*  444 */     if (this.authcIdentity != null) {
/*  445 */       if (this.userFilter != null)
/*  446 */         this.authFirst = true;
/*      */       else {
/*  448 */         this.authOnly = true;
/*      */       }
/*      */     }
/*      */ 
/*  452 */     if ("false".equalsIgnoreCase((String)paramMap2.get("useSSL"))) {
/*  453 */       this.useSSL = false;
/*  454 */       this.ldapEnvironment.remove("java.naming.security.protocol");
/*      */     } else {
/*  456 */       this.ldapEnvironment.put("java.naming.security.protocol", "ssl");
/*      */     }
/*      */ 
/*  459 */     this.tryFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("tryFirstPass"));
/*      */ 
/*  462 */     this.useFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("useFirstPass"));
/*      */ 
/*  465 */     this.storePass = "true".equalsIgnoreCase((String)paramMap2.get("storePass"));
/*      */ 
/*  467 */     this.clearPass = "true".equalsIgnoreCase((String)paramMap2.get("clearPass"));
/*      */ 
/*  469 */     this.debug = "true".equalsIgnoreCase((String)paramMap2.get("debug"));
/*      */ 
/*  471 */     if (this.debug)
/*  472 */       if (this.authFirst) {
/*  473 */         System.out.println("\t\t[LdapLoginModule] authentication-first mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
/*      */       }
/*  476 */       else if (this.authOnly) {
/*  477 */         System.out.println("\t\t[LdapLoginModule] authentication-only mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
/*      */       }
/*      */       else
/*      */       {
/*  481 */         System.out.println("\t\t[LdapLoginModule] search-first mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean login()
/*      */     throws LoginException
/*      */   {
/*  502 */     if (this.userProvider == null) {
/*  503 */       throw new LoginException("Unable to locate the LDAP directory service");
/*      */     }
/*      */ 
/*  507 */     if (this.debug) {
/*  508 */       System.out.println("\t\t[LdapLoginModule] user provider: " + this.userProvider);
/*      */     }
/*      */ 
/*  513 */     if (this.tryFirstPass)
/*      */     {
/*      */       try
/*      */       {
/*  518 */         attemptAuthentication(true);
/*      */ 
/*  521 */         this.succeeded = true;
/*  522 */         if (this.debug) {
/*  523 */           System.out.println("\t\t[LdapLoginModule] tryFirstPass succeeded");
/*      */         }
/*      */ 
/*  526 */         return true;
/*      */       }
/*      */       catch (LoginException localLoginException1)
/*      */       {
/*  530 */         cleanState();
/*  531 */         if (this.debug) {
/*  532 */           System.out.println("\t\t[LdapLoginModule] tryFirstPass failed: " + localLoginException1.toString());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*  537 */     else if (this.useFirstPass)
/*      */     {
/*      */       try
/*      */       {
/*  542 */         attemptAuthentication(true);
/*      */ 
/*  545 */         this.succeeded = true;
/*  546 */         if (this.debug) {
/*  547 */           System.out.println("\t\t[LdapLoginModule] useFirstPass succeeded");
/*      */         }
/*      */ 
/*  550 */         return true;
/*      */       }
/*      */       catch (LoginException localLoginException2)
/*      */       {
/*  554 */         cleanState();
/*  555 */         if (this.debug) {
/*  556 */           System.out.println("\t\t[LdapLoginModule] useFirstPass failed");
/*      */         }
/*      */ 
/*  559 */         throw localLoginException2;
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  565 */       attemptAuthentication(false);
/*      */ 
/*  568 */       this.succeeded = true;
/*  569 */       if (this.debug) {
/*  570 */         System.out.println("\t\t[LdapLoginModule] authentication succeeded");
/*      */       }
/*      */ 
/*  573 */       return true;
/*      */     }
/*      */     catch (LoginException localLoginException3) {
/*  576 */       cleanState();
/*  577 */       if (this.debug) {
/*  578 */         System.out.println("\t\t[LdapLoginModule] authentication failed");
/*      */       }
/*      */ 
/*  581 */       throw localLoginException3;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean commit()
/*      */     throws LoginException
/*      */   {
/*  608 */     if (!this.succeeded) {
/*  609 */       return false;
/*      */     }
/*  611 */     if (this.subject.isReadOnly()) {
/*  612 */       cleanState();
/*  613 */       throw new LoginException("Subject is read-only");
/*      */     }
/*      */ 
/*  616 */     Set localSet = this.subject.getPrincipals();
/*  617 */     if (!localSet.contains(this.ldapPrincipal)) {
/*  618 */       localSet.add(this.ldapPrincipal);
/*      */     }
/*  620 */     if (this.debug) {
/*  621 */       System.out.println("\t\t[LdapLoginModule] added LdapPrincipal \"" + this.ldapPrincipal + "\" to Subject");
/*      */     }
/*      */ 
/*  627 */     if (!localSet.contains(this.userPrincipal)) {
/*  628 */       localSet.add(this.userPrincipal);
/*      */     }
/*  630 */     if (this.debug) {
/*  631 */       System.out.println("\t\t[LdapLoginModule] added UserPrincipal \"" + this.userPrincipal + "\" to Subject");
/*      */     }
/*      */ 
/*  637 */     if ((this.authzPrincipal != null) && (!localSet.contains(this.authzPrincipal)))
/*      */     {
/*  639 */       localSet.add(this.authzPrincipal);
/*      */ 
/*  641 */       if (this.debug) {
/*  642 */         System.out.println("\t\t[LdapLoginModule] added UserPrincipal \"" + this.authzPrincipal + "\" to Subject");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  650 */     cleanState();
/*  651 */     this.commitSucceeded = true;
/*  652 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean abort()
/*      */     throws LoginException
/*      */   {
/*  672 */     if (this.debug) {
/*  673 */       System.out.println("\t\t[LdapLoginModule] aborted authentication");
/*      */     }
/*      */ 
/*  676 */     if (!this.succeeded)
/*  677 */       return false;
/*  678 */     if ((this.succeeded == true) && (!this.commitSucceeded))
/*      */     {
/*  681 */       this.succeeded = false;
/*  682 */       cleanState();
/*      */ 
/*  684 */       this.ldapPrincipal = null;
/*  685 */       this.userPrincipal = null;
/*  686 */       this.authzPrincipal = null;
/*      */     }
/*      */     else
/*      */     {
/*  690 */       logout();
/*      */     }
/*  692 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean logout()
/*      */     throws LoginException
/*      */   {
/*  706 */     if (this.subject.isReadOnly()) {
/*  707 */       cleanState();
/*  708 */       throw new LoginException("Subject is read-only");
/*      */     }
/*  710 */     Set localSet = this.subject.getPrincipals();
/*  711 */     localSet.remove(this.ldapPrincipal);
/*  712 */     localSet.remove(this.userPrincipal);
/*  713 */     if (this.authzIdentity != null) {
/*  714 */       localSet.remove(this.authzPrincipal);
/*      */     }
/*      */ 
/*  718 */     cleanState();
/*  719 */     this.succeeded = false;
/*  720 */     this.commitSucceeded = false;
/*      */ 
/*  722 */     this.ldapPrincipal = null;
/*  723 */     this.userPrincipal = null;
/*  724 */     this.authzPrincipal = null;
/*      */ 
/*  726 */     if (this.debug) {
/*  727 */       System.out.println("\t\t[LdapLoginModule] logged out Subject");
/*      */     }
/*  729 */     return true;
/*      */   }
/*      */ 
/*      */   private void attemptAuthentication(boolean paramBoolean)
/*      */     throws LoginException
/*      */   {
/*  743 */     getUsernamePassword(paramBoolean);
/*      */ 
/*  745 */     if ((this.password == null) || (this.password.length == 0)) {
/*  746 */       throw new FailedLoginException("No password was supplied");
/*      */     }
/*      */ 
/*  750 */     Object localObject = "";
/*      */ 
/*  752 */     if ((this.authFirst) || (this.authOnly))
/*      */     {
/*  754 */       String str = replaceUsernameToken(this.identityMatcher, this.authcIdentity);
/*      */ 
/*  757 */       this.ldapEnvironment.put("java.naming.security.credentials", this.password);
/*  758 */       this.ldapEnvironment.put("java.naming.security.principal", str);
/*      */ 
/*  760 */       if (this.debug) {
/*  761 */         System.out.println("\t\t[LdapLoginModule] attempting to authenticate user: " + this.username);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  767 */         this.ctx = new InitialLdapContext(this.ldapEnvironment, null);
/*      */       }
/*      */       catch (NamingException localNamingException3) {
/*  770 */         throw ((LoginException)new FailedLoginException("Cannot bind to LDAP server").initCause(localNamingException3));
/*      */       }
/*      */ 
/*  778 */       if (this.userFilter != null)
/*  779 */         localObject = findUserDN(this.ctx);
/*      */       else {
/*  781 */         localObject = str;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/*  788 */         this.ctx = new InitialLdapContext(this.ldapEnvironment, null);
/*      */       }
/*      */       catch (NamingException localNamingException1) {
/*  791 */         throw ((LoginException)new FailedLoginException("Cannot connect to LDAP server").initCause(localNamingException1));
/*      */       }
/*      */ 
/*  797 */       localObject = findUserDN(this.ctx);
/*      */       try
/*      */       {
/*  802 */         this.ctx.addToEnvironment("java.naming.security.authentication", "simple");
/*  803 */         this.ctx.addToEnvironment("java.naming.security.principal", localObject);
/*  804 */         this.ctx.addToEnvironment("java.naming.security.credentials", this.password);
/*      */ 
/*  806 */         if (this.debug) {
/*  807 */           System.out.println("\t\t[LdapLoginModule] attempting to authenticate user: " + this.username);
/*      */         }
/*      */ 
/*  811 */         this.ctx.reconnect(null);
/*      */       }
/*      */       catch (NamingException localNamingException2)
/*      */       {
/*  816 */         throw ((LoginException)new FailedLoginException("Cannot bind to LDAP server").initCause(localNamingException2));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  823 */     if ((this.storePass) && (!this.sharedState.containsKey("javax.security.auth.login.name")) && (!this.sharedState.containsKey("javax.security.auth.login.password")))
/*      */     {
/*  826 */       this.sharedState.put("javax.security.auth.login.name", this.username);
/*  827 */       this.sharedState.put("javax.security.auth.login.password", this.password);
/*      */     }
/*      */ 
/*  831 */     this.userPrincipal = new UserPrincipal(this.username);
/*  832 */     if (this.authzIdentity != null) {
/*  833 */       this.authzPrincipal = new UserPrincipal(this.authzIdentity);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  838 */       this.ldapPrincipal = new LdapPrincipal((String)localObject);
/*      */     }
/*      */     catch (InvalidNameException localInvalidNameException) {
/*  841 */       if (this.debug) {
/*  842 */         System.out.println("\t\t[LdapLoginModule] cannot create LdapPrincipal: bad DN");
/*      */       }
/*      */ 
/*  845 */       throw ((LoginException)new FailedLoginException("Cannot create LdapPrincipal").initCause(localInvalidNameException));
/*      */     }
/*      */   }
/*      */ 
/*      */   private String findUserDN(LdapContext paramLdapContext)
/*      */     throws LoginException
/*      */   {
/*  863 */     String str = "";
/*      */ 
/*  866 */     if (this.userFilter != null) {
/*  867 */       if (this.debug)
/*  868 */         System.out.println("\t\t[LdapLoginModule] searching for entry belonging to user: " + this.username);
/*      */     }
/*      */     else
/*      */     {
/*  872 */       if (this.debug) {
/*  873 */         System.out.println("\t\t[LdapLoginModule] cannot search for entry belonging to user: " + this.username);
/*      */       }
/*      */ 
/*  876 */       throw new FailedLoginException("Cannot find user's LDAP entry");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  881 */       NamingEnumeration localNamingEnumeration = paramLdapContext.search("", replaceUsernameToken(this.filterMatcher, this.userFilter), this.constraints);
/*      */ 
/*  886 */       if (localNamingEnumeration.hasMore()) {
/*  887 */         SearchResult localSearchResult = (SearchResult)localNamingEnumeration.next();
/*      */ 
/*  892 */         str = ((Context)localSearchResult.getObject()).getNameInNamespace();
/*      */ 
/*  894 */         if (this.debug) {
/*  895 */           System.out.println("\t\t[LdapLoginModule] found entry: " + str);
/*      */         }
/*      */ 
/*  900 */         if (this.authzIdentityAttr != null) {
/*  901 */           Attribute localAttribute = localSearchResult.getAttributes().get(this.authzIdentityAttr);
/*      */ 
/*  903 */           if (localAttribute != null) {
/*  904 */             Object localObject = localAttribute.get();
/*  905 */             if ((localObject instanceof String)) {
/*  906 */               this.authzIdentity = ((String)localObject);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  911 */         localNamingEnumeration.close();
/*      */       }
/*  915 */       else if (this.debug) {
/*  916 */         System.out.println("\t\t[LdapLoginModule] user's entry not found");
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/*      */     }
/*      */ 
/*  925 */     if (str.equals("")) {
/*  926 */       throw new FailedLoginException("Cannot find user's LDAP entry");
/*      */     }
/*      */ 
/*  929 */     return str;
/*      */   }
/*      */ 
/*      */   private String replaceUsernameToken(Matcher paramMatcher, String paramString)
/*      */   {
/*  940 */     return paramMatcher != null ? paramMatcher.replaceAll(this.username) : paramString;
/*      */   }
/*      */ 
/*      */   private void getUsernamePassword(boolean paramBoolean)
/*      */     throws LoginException
/*      */   {
/*  959 */     if (paramBoolean)
/*      */     {
/*  961 */       this.username = ((String)this.sharedState.get("javax.security.auth.login.name"));
/*  962 */       this.password = ((char[])this.sharedState.get("javax.security.auth.login.password"));
/*  963 */       return;
/*      */     }
/*      */ 
/*  967 */     if (this.callbackHandler == null) {
/*  968 */       throw new LoginException("No CallbackHandler available to acquire authentication information from the user");
/*      */     }
/*      */ 
/*  971 */     Callback[] arrayOfCallback = new Callback[2];
/*  972 */     arrayOfCallback[0] = new NameCallback(rb.getString("username."));
/*  973 */     arrayOfCallback[1] = new PasswordCallback(rb.getString("password."), false);
/*      */     try
/*      */     {
/*  976 */       this.callbackHandler.handle(arrayOfCallback);
/*  977 */       this.username = ((NameCallback)arrayOfCallback[0]).getName();
/*  978 */       char[] arrayOfChar = ((PasswordCallback)arrayOfCallback[1]).getPassword();
/*  979 */       this.password = new char[arrayOfChar.length];
/*  980 */       System.arraycopy(arrayOfChar, 0, this.password, 0, arrayOfChar.length);
/*      */ 
/*  982 */       ((PasswordCallback)arrayOfCallback[1]).clearPassword();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  985 */       throw new LoginException(localIOException.toString());
/*      */     }
/*      */     catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/*  988 */       throw new LoginException("Error: " + localUnsupportedCallbackException.getCallback().toString() + " not available to acquire authentication information" + " from the user");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void cleanState()
/*      */   {
/*  998 */     this.username = null;
/*  999 */     if (this.password != null) {
/* 1000 */       Arrays.fill(this.password, ' ');
/* 1001 */       this.password = null;
/*      */     }
/*      */     try {
/* 1004 */       if (this.ctx != null)
/* 1005 */         this.ctx.close();
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/*      */     }
/* 1010 */     this.ctx = null;
/*      */ 
/* 1012 */     if (this.clearPass) {
/* 1013 */       this.sharedState.remove("javax.security.auth.login.name");
/* 1014 */       this.sharedState.remove("javax.security.auth.login.password");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.LdapLoginModule
 * JD-Core Version:    0.6.2
 */