/*      */ package com.sun.security.auth.module;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import javax.security.auth.DestroyFailedException;
/*      */ import javax.security.auth.RefreshFailedException;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.security.auth.callback.Callback;
/*      */ import javax.security.auth.callback.CallbackHandler;
/*      */ import javax.security.auth.callback.NameCallback;
/*      */ import javax.security.auth.callback.PasswordCallback;
/*      */ import javax.security.auth.callback.UnsupportedCallbackException;
/*      */ import javax.security.auth.kerberos.KerberosKey;
/*      */ import javax.security.auth.kerberos.KerberosPrincipal;
/*      */ import javax.security.auth.kerberos.KerberosTicket;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import javax.security.auth.spi.LoginModule;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.jgss.krb5.Krb5Util;
/*      */ import sun.security.jgss.krb5.Krb5Util.KeysFromKeyTab;
/*      */ import sun.security.krb5.Config;
/*      */ import sun.security.krb5.Credentials;
/*      */ import sun.security.krb5.EncryptionKey;
/*      */ import sun.security.krb5.KrbAsReqBuilder;
/*      */ import sun.security.krb5.KrbException;
/*      */ import sun.security.krb5.PrincipalName;
/*      */ 
/*      */ public class Krb5LoginModule
/*      */   implements LoginModule
/*      */ {
/*      */   private Subject subject;
/*      */   private CallbackHandler callbackHandler;
/*      */   private Map sharedState;
/*      */   private Map<String, ?> options;
/*  374 */   private boolean debug = false;
/*  375 */   private boolean storeKey = false;
/*  376 */   private boolean doNotPrompt = false;
/*  377 */   private boolean useTicketCache = false;
/*  378 */   private boolean useKeyTab = false;
/*  379 */   private String ticketCacheName = null;
/*  380 */   private String keyTabName = null;
/*  381 */   private String princName = null;
/*      */ 
/*  383 */   private boolean useFirstPass = false;
/*  384 */   private boolean tryFirstPass = false;
/*  385 */   private boolean storePass = false;
/*  386 */   private boolean clearPass = false;
/*  387 */   private boolean refreshKrb5Config = false;
/*  388 */   private boolean renewTGT = false;
/*      */ 
/*  392 */   private boolean isInitiator = true;
/*      */ 
/*  395 */   private boolean succeeded = false;
/*  396 */   private boolean commitSucceeded = false;
/*      */   private String username;
/*  401 */   private EncryptionKey[] encKeys = null;
/*      */ 
/*  403 */   javax.security.auth.kerberos.KeyTab ktab = null;
/*      */ 
/*  405 */   private Credentials cred = null;
/*      */ 
/*  407 */   private PrincipalName principal = null;
/*  408 */   private KerberosPrincipal kerbClientPrinc = null;
/*  409 */   private KerberosTicket kerbTicket = null;
/*  410 */   private KerberosKey[] kerbKeys = null;
/*  411 */   private StringBuffer krb5PrincName = null;
/*  412 */   private char[] password = null;
/*      */   private static final String NAME = "javax.security.auth.login.name";
/*      */   private static final String PWD = "javax.security.auth.login.password";
/*  416 */   static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.util.AuthResources");
/*      */ 
/*      */   public void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2)
/*      */   {
/*  441 */     this.subject = paramSubject;
/*  442 */     this.callbackHandler = paramCallbackHandler;
/*  443 */     this.sharedState = paramMap1;
/*  444 */     this.options = paramMap2;
/*      */ 
/*  448 */     this.debug = "true".equalsIgnoreCase((String)paramMap2.get("debug"));
/*  449 */     this.storeKey = "true".equalsIgnoreCase((String)paramMap2.get("storeKey"));
/*  450 */     this.doNotPrompt = "true".equalsIgnoreCase((String)paramMap2.get("doNotPrompt"));
/*      */ 
/*  452 */     this.useTicketCache = "true".equalsIgnoreCase((String)paramMap2.get("useTicketCache"));
/*      */ 
/*  454 */     this.useKeyTab = "true".equalsIgnoreCase((String)paramMap2.get("useKeyTab"));
/*  455 */     this.ticketCacheName = ((String)paramMap2.get("ticketCache"));
/*  456 */     this.keyTabName = ((String)paramMap2.get("keyTab"));
/*  457 */     if (this.keyTabName != null) {
/*  458 */       this.keyTabName = sun.security.krb5.internal.ktab.KeyTab.normalize(this.keyTabName);
/*      */     }
/*      */ 
/*  461 */     this.princName = ((String)paramMap2.get("principal"));
/*  462 */     this.refreshKrb5Config = "true".equalsIgnoreCase((String)paramMap2.get("refreshKrb5Config"));
/*      */ 
/*  464 */     this.renewTGT = "true".equalsIgnoreCase((String)paramMap2.get("renewTGT"));
/*      */ 
/*  468 */     String str = (String)paramMap2.get("isInitiator");
/*  469 */     if (str != null)
/*      */     {
/*  472 */       this.isInitiator = "true".equalsIgnoreCase(str);
/*      */     }
/*      */ 
/*  475 */     this.tryFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("tryFirstPass"));
/*      */ 
/*  478 */     this.useFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("useFirstPass"));
/*      */ 
/*  481 */     this.storePass = "true".equalsIgnoreCase((String)paramMap2.get("storePass"));
/*      */ 
/*  483 */     this.clearPass = "true".equalsIgnoreCase((String)paramMap2.get("clearPass"));
/*      */ 
/*  485 */     if (this.debug)
/*  486 */       System.out.print("Debug is  " + this.debug + " storeKey " + this.storeKey + " useTicketCache " + this.useTicketCache + " useKeyTab " + this.useKeyTab + " doNotPrompt " + this.doNotPrompt + " ticketCache is " + this.ticketCacheName + " isInitiator " + this.isInitiator + " KeyTab is " + this.keyTabName + " refreshKrb5Config is " + this.refreshKrb5Config + " principal is " + this.princName + " tryFirstPass is " + this.tryFirstPass + " useFirstPass is " + this.useFirstPass + " storePass is " + this.storePass + " clearPass is " + this.clearPass + "\n");
/*      */   }
/*      */ 
/*      */   public boolean login()
/*      */     throws LoginException
/*      */   {
/*  520 */     validateConfiguration();
/*  521 */     if (this.refreshKrb5Config) {
/*      */       try {
/*  523 */         if (this.debug) {
/*  524 */           System.out.println("Refreshing Kerberos configuration");
/*      */         }
/*  526 */         Config.refresh();
/*      */       } catch (KrbException localKrbException) {
/*  528 */         LoginException localLoginException1 = new LoginException(localKrbException.getMessage());
/*  529 */         localLoginException1.initCause(localKrbException);
/*  530 */         throw localLoginException1;
/*      */       }
/*      */     }
/*  533 */     String str = System.getProperty("sun.security.krb5.principal");
/*      */ 
/*  535 */     if (str != null) {
/*  536 */       this.krb5PrincName = new StringBuffer(str);
/*      */     }
/*  538 */     else if (this.princName != null) {
/*  539 */       this.krb5PrincName = new StringBuffer(this.princName);
/*      */     }
/*      */ 
/*  543 */     if (this.tryFirstPass) {
/*      */       try {
/*  545 */         attemptAuthentication(true);
/*  546 */         if (this.debug) {
/*  547 */           System.out.println("\t\t[Krb5LoginModule] authentication succeeded");
/*      */         }
/*  549 */         this.succeeded = true;
/*  550 */         cleanState();
/*  551 */         return true;
/*      */       }
/*      */       catch (LoginException localLoginException2) {
/*  554 */         cleanState();
/*  555 */         if (this.debug) {
/*  556 */           System.out.println("\t\t[Krb5LoginModule] tryFirstPass failed with:" + localLoginException2.getMessage());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*  561 */     else if (this.useFirstPass) {
/*      */       try {
/*  563 */         attemptAuthentication(true);
/*  564 */         this.succeeded = true;
/*  565 */         cleanState();
/*  566 */         return true;
/*      */       }
/*      */       catch (LoginException localLoginException3) {
/*  569 */         if (this.debug) {
/*  570 */           System.out.println("\t\t[Krb5LoginModule] authentication failed \n" + localLoginException3.getMessage());
/*      */         }
/*      */ 
/*  574 */         this.succeeded = false;
/*  575 */         cleanState();
/*  576 */         throw localLoginException3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  584 */       attemptAuthentication(false);
/*  585 */       this.succeeded = true;
/*  586 */       cleanState();
/*  587 */       return true;
/*      */     }
/*      */     catch (LoginException localLoginException4) {
/*  590 */       if (this.debug) {
/*  591 */         System.out.println("\t\t[Krb5LoginModule] authentication failed \n" + localLoginException4.getMessage());
/*      */       }
/*      */ 
/*  595 */       this.succeeded = false;
/*  596 */       cleanState();
/*  597 */       throw localLoginException4;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void attemptAuthentication(boolean paramBoolean)
/*      */     throws LoginException
/*      */   {
/*      */     Object localObject;
/*  614 */     if (this.krb5PrincName != null) {
/*      */       try {
/*  616 */         this.principal = new PrincipalName(this.krb5PrincName.toString(), 1);
/*      */       }
/*      */       catch (KrbException localKrbException1)
/*      */       {
/*  620 */         localObject = new LoginException(localKrbException1.getMessage());
/*  621 */         ((LoginException)localObject).initCause(localKrbException1);
/*  622 */         throw ((Throwable)localObject);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  627 */       if (this.useTicketCache)
/*      */       {
/*  629 */         if (this.debug)
/*  630 */           System.out.println("Acquire TGT from Cache");
/*  631 */         this.cred = Credentials.acquireTGTFromCache(this.principal, this.ticketCacheName);
/*      */ 
/*  634 */         if (this.cred != null)
/*      */         {
/*  636 */           if (!isCurrent(this.cred)) {
/*  637 */             if (this.renewTGT) {
/*  638 */               this.cred = renewCredentials(this.cred);
/*      */             }
/*      */             else {
/*  641 */               this.cred = null;
/*  642 */               if (this.debug) {
/*  643 */                 System.out.println("Credentials are no longer valid");
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  649 */         if (this.cred != null)
/*      */         {
/*  651 */           if (this.principal == null) {
/*  652 */             this.principal = this.cred.getClient();
/*      */           }
/*      */         }
/*  655 */         if (this.debug) {
/*  656 */           System.out.println("Principal is " + this.principal);
/*  657 */           if (this.cred == null) {
/*  658 */             System.out.println("null credentials from Ticket Cache");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  667 */       if (this.cred == null)
/*      */       {
/*  670 */         if (this.principal == null) {
/*  671 */           promptForName(paramBoolean);
/*  672 */           this.principal = new PrincipalName(this.krb5PrincName.toString(), 1);
/*      */         }
/*      */ 
/*  696 */         if (this.useKeyTab) {
/*  697 */           this.ktab = (this.keyTabName == null ? javax.security.auth.kerberos.KeyTab.getInstance() : javax.security.auth.kerberos.KeyTab.getInstance(new File(this.keyTabName)));
/*      */ 
/*  700 */           if ((this.isInitiator) && 
/*  701 */             (Krb5Util.keysFromJavaxKeyTab(this.ktab, this.principal).length == 0))
/*      */           {
/*  703 */             this.ktab = null;
/*  704 */             if (this.debug)
/*  705 */               System.out.println("Key for the principal " + this.principal + " not available in " + (this.keyTabName == null ? "default key tab" : this.keyTabName));
/*      */           }
/*      */         }
/*      */         KrbAsReqBuilder localKrbAsReqBuilder;
/*  718 */         if (this.ktab == null) {
/*  719 */           promptForPass(paramBoolean);
/*  720 */           localKrbAsReqBuilder = new KrbAsReqBuilder(this.principal, this.password);
/*  721 */           if (this.isInitiator)
/*      */           {
/*  725 */             this.cred = localKrbAsReqBuilder.action().getCreds();
/*      */           }
/*  727 */           if (this.storeKey) {
/*  728 */             this.encKeys = localKrbAsReqBuilder.getKeys(this.isInitiator);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  733 */           localKrbAsReqBuilder = new KrbAsReqBuilder(this.principal, this.ktab);
/*  734 */           if (this.isInitiator) {
/*  735 */             this.cred = localKrbAsReqBuilder.action().getCreds();
/*      */           }
/*      */         }
/*  738 */         localKrbAsReqBuilder.destroy();
/*      */ 
/*  740 */         if (this.debug) {
/*  741 */           System.out.println("principal is " + this.principal);
/*  742 */           localObject = new HexDumpEncoder();
/*  743 */           if (this.ktab != null)
/*  744 */             System.out.println("Will use keytab");
/*  745 */           else if (this.storeKey) {
/*  746 */             for (int i = 0; i < this.encKeys.length; i++) {
/*  747 */               System.out.println("EncryptionKey: keyType=" + this.encKeys[i].getEType() + " keyBytes (hex dump)=" + ((HexDumpEncoder)localObject).encodeBuffer(this.encKeys[i].getBytes()));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  756 */         if ((this.isInitiator) && (this.cred == null)) {
/*  757 */           throw new LoginException("TGT Can not be obtained from the KDC ");
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (KrbException localKrbException2)
/*      */     {
/*  763 */       localObject = new LoginException(localKrbException2.getMessage());
/*  764 */       ((LoginException)localObject).initCause(localKrbException2);
/*  765 */       throw ((Throwable)localObject);
/*      */     } catch (IOException localIOException) {
/*  767 */       localObject = new LoginException(localIOException.getMessage());
/*  768 */       ((LoginException)localObject).initCause(localIOException);
/*  769 */       throw ((Throwable)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void promptForName(boolean paramBoolean) throws LoginException
/*      */   {
/*  775 */     this.krb5PrincName = new StringBuffer("");
/*  776 */     if (paramBoolean)
/*      */     {
/*  778 */       this.username = ((String)this.sharedState.get("javax.security.auth.login.name"));
/*  779 */       if (this.debug) {
/*  780 */         System.out.println("username from shared state is " + this.username + "\n");
/*      */       }
/*      */ 
/*  783 */       if (this.username == null) {
/*  784 */         System.out.println("username from shared state is null\n");
/*      */ 
/*  786 */         throw new LoginException("Username can not be obtained from sharedstate ");
/*      */       }
/*      */ 
/*  789 */       if (this.debug) {
/*  790 */         System.out.println("username from shared state is " + this.username + "\n");
/*      */       }
/*      */ 
/*  793 */       if ((this.username != null) && (this.username.length() > 0)) {
/*  794 */         this.krb5PrincName.insert(0, this.username);
/*  795 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  799 */     if (this.doNotPrompt) {
/*  800 */       throw new LoginException("Unable to obtain Princpal Name for authentication ");
/*      */     }
/*      */ 
/*  803 */     if (this.callbackHandler == null) {
/*  804 */       throw new LoginException("No CallbackHandler available to garner authentication information from the user");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  809 */       String str = System.getProperty("user.name");
/*      */ 
/*  811 */       Callback[] arrayOfCallback = new Callback[1];
/*  812 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Kerberos.username.defUsername."));
/*      */ 
/*  815 */       Object[] arrayOfObject = { str };
/*  816 */       arrayOfCallback[0] = new NameCallback(localMessageFormat.format(arrayOfObject));
/*  817 */       this.callbackHandler.handle(arrayOfCallback);
/*  818 */       this.username = ((NameCallback)arrayOfCallback[0]).getName();
/*  819 */       if ((this.username == null) || (this.username.length() == 0))
/*  820 */         this.username = str;
/*  821 */       this.krb5PrincName.insert(0, this.username);
/*      */     }
/*      */     catch (IOException localIOException) {
/*  824 */       throw new LoginException(localIOException.getMessage());
/*      */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/*  826 */       throw new LoginException(localUnsupportedCallbackException.getMessage() + " not available to garner " + " authentication information " + " from the user");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void promptForPass(boolean paramBoolean)
/*      */     throws LoginException
/*      */   {
/*  838 */     if (paramBoolean)
/*      */     {
/*  840 */       this.password = ((char[])this.sharedState.get("javax.security.auth.login.password"));
/*  841 */       if (this.password == null) {
/*  842 */         if (this.debug) {
/*  843 */           System.out.println("Password from shared state is null");
/*      */         }
/*      */ 
/*  846 */         throw new LoginException("Password can not be obtained from sharedstate ");
/*      */       }
/*      */ 
/*  849 */       if (this.debug) {
/*  850 */         System.out.println("password is " + new String(this.password));
/*      */       }
/*      */ 
/*  853 */       return;
/*      */     }
/*  855 */     if (this.doNotPrompt) {
/*  856 */       throw new LoginException("Unable to obtain password from user\n");
/*      */     }
/*      */ 
/*  859 */     if (this.callbackHandler == null) {
/*  860 */       throw new LoginException("No CallbackHandler available to garner authentication information from the user");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  865 */       Callback[] arrayOfCallback = new Callback[1];
/*  866 */       String str = this.krb5PrincName.toString();
/*  867 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("Kerberos.password.for.username."));
/*      */ 
/*  870 */       Object[] arrayOfObject = { str };
/*  871 */       arrayOfCallback[0] = new PasswordCallback(localMessageFormat.format(arrayOfObject), false);
/*      */ 
/*  874 */       this.callbackHandler.handle(arrayOfCallback);
/*  875 */       char[] arrayOfChar = ((PasswordCallback)arrayOfCallback[0]).getPassword();
/*      */ 
/*  877 */       if (arrayOfChar == null) {
/*  878 */         throw new LoginException("No password provided");
/*      */       }
/*  880 */       this.password = new char[arrayOfChar.length];
/*  881 */       System.arraycopy(arrayOfChar, 0, this.password, 0, arrayOfChar.length);
/*      */ 
/*  883 */       ((PasswordCallback)arrayOfCallback[0]).clearPassword();
/*      */ 
/*  887 */       for (int i = 0; i < arrayOfChar.length; i++)
/*  888 */         arrayOfChar[i] = ' ';
/*  889 */       arrayOfChar = null;
/*  890 */       if (this.debug) {
/*  891 */         System.out.println("\t\t[Krb5LoginModule] user entered username: " + this.krb5PrincName);
/*      */ 
/*  894 */         System.out.println();
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  897 */       throw new LoginException(localIOException.getMessage());
/*      */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/*  899 */       throw new LoginException(localUnsupportedCallbackException.getMessage() + " not available to garner " + " authentication information " + "from the user");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void validateConfiguration()
/*      */     throws LoginException
/*      */   {
/*  908 */     if ((this.doNotPrompt) && (!this.useTicketCache) && (!this.useKeyTab) && (!this.tryFirstPass) && (!this.useFirstPass))
/*      */     {
/*  910 */       throw new LoginException("Configuration Error - either doNotPrompt should be  false or at least one of useTicketCache,  useKeyTab, tryFirstPass and useFirstPass should be true");
/*      */     }
/*      */ 
/*  916 */     if ((this.ticketCacheName != null) && (!this.useTicketCache)) {
/*  917 */       throw new LoginException("Configuration Error  - useTicketCache should be set to true to use the ticket cache" + this.ticketCacheName);
/*      */     }
/*      */ 
/*  922 */     if (((this.keyTabName != null ? 1 : 0) & (!this.useKeyTab ? 1 : 0)) != 0) {
/*  923 */       throw new LoginException("Configuration Error - useKeyTab should be set to true to use the keytab" + this.keyTabName);
/*      */     }
/*      */ 
/*  926 */     if ((this.storeKey) && (this.doNotPrompt) && (!this.useKeyTab) && (!this.tryFirstPass) && (!this.useFirstPass))
/*      */     {
/*  928 */       throw new LoginException("Configuration Error - either doNotPrompt should be set to  false or at least one of tryFirstPass, useFirstPass or useKeyTab must be set to true for storeKey option");
/*      */     }
/*      */ 
/*  932 */     if ((this.renewTGT) && (!this.useTicketCache))
/*  933 */       throw new LoginException("Configuration Error - either useTicketCache should be  true or renewTGT should be false");
/*      */   }
/*      */ 
/*      */   private boolean isCurrent(Credentials paramCredentials)
/*      */   {
/*  941 */     Date localDate = paramCredentials.getEndTime();
/*  942 */     if (localDate != null) {
/*  943 */       return System.currentTimeMillis() <= localDate.getTime();
/*      */     }
/*  945 */     return true;
/*      */   }
/*      */ 
/*      */   private Credentials renewCredentials(Credentials paramCredentials)
/*      */   {
/*      */     Credentials localCredentials;
/*      */     try {
/*  952 */       if (!paramCredentials.isRenewable()) {
/*  953 */         throw new RefreshFailedException("This ticket is not renewable");
/*      */       }
/*  955 */       if (System.currentTimeMillis() > this.cred.getRenewTill().getTime()) {
/*  956 */         throw new RefreshFailedException("This ticket is past its last renewal time.");
/*      */       }
/*  958 */       localCredentials = paramCredentials.renew();
/*  959 */       if (this.debug)
/*  960 */         System.out.println("Renewed Kerberos Ticket");
/*      */     } catch (Exception localException) {
/*  962 */       localCredentials = null;
/*  963 */       if (this.debug) {
/*  964 */         System.out.println("Ticket could not be renewed : " + localException.getMessage());
/*      */       }
/*      */     }
/*  967 */     return localCredentials;
/*      */   }
/*      */ 
/*      */   public boolean commit()
/*      */     throws LoginException
/*      */   {
/* 1001 */     if (!this.succeeded) {
/* 1002 */       return false;
/*      */     }
/*      */ 
/* 1005 */     if ((this.isInitiator) && (this.cred == null)) {
/* 1006 */       this.succeeded = false;
/* 1007 */       throw new LoginException("Null Client Credential");
/*      */     }
/*      */ 
/* 1010 */     if (this.subject.isReadOnly()) {
/* 1011 */       cleanKerberosCred();
/* 1012 */       throw new LoginException("Subject is Readonly");
/*      */     }
/*      */ 
/* 1022 */     Set localSet1 = this.subject.getPrivateCredentials();
/* 1023 */     Set localSet2 = this.subject.getPrincipals();
/* 1024 */     this.kerbClientPrinc = new KerberosPrincipal(this.principal.getName());
/*      */ 
/* 1027 */     if (this.isInitiator) {
/* 1028 */       this.kerbTicket = Krb5Util.credsToTicket(this.cred);
/*      */     }
/*      */ 
/* 1031 */     if ((this.storeKey) && (this.encKeys != null)) {
/* 1032 */       if (this.encKeys.length == 0) {
/* 1033 */         this.succeeded = false;
/* 1034 */         throw new LoginException("Null Server Key ");
/*      */       }
/*      */ 
/* 1037 */       this.kerbKeys = new KerberosKey[this.encKeys.length];
/* 1038 */       for (int i = 0; i < this.encKeys.length; i++) {
/* 1039 */         Integer localInteger = this.encKeys[i].getKeyVersionNumber();
/* 1040 */         this.kerbKeys[i] = new KerberosKey(this.kerbClientPrinc, this.encKeys[i].getBytes(), this.encKeys[i].getEType(), localInteger == null ? 0 : localInteger.intValue());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1050 */     if (!localSet2.contains(this.kerbClientPrinc)) {
/* 1051 */       localSet2.add(this.kerbClientPrinc);
/*      */     }
/*      */ 
/* 1055 */     if ((this.kerbTicket != null) && 
/* 1056 */       (!localSet1.contains(this.kerbTicket))) {
/* 1057 */       localSet1.add(this.kerbTicket);
/*      */     }
/*      */ 
/* 1060 */     if (this.storeKey) {
/* 1061 */       if (this.encKeys == null) {
/* 1062 */         if (this.ktab != null) {
/* 1063 */           if (!localSet1.contains(this.ktab)) {
/* 1064 */             localSet1.add(this.ktab);
/*      */ 
/* 1066 */             for (KerberosKey localKerberosKey : this.ktab.getKeys(this.kerbClientPrinc))
/* 1067 */               localSet1.add(new Krb5Util.KeysFromKeyTab(localKerberosKey));
/*      */           }
/*      */         }
/*      */         else {
/* 1071 */           this.succeeded = false;
/* 1072 */           throw new LoginException("No key to store");
/*      */         }
/*      */       }
/* 1075 */       else for (int j = 0; j < this.kerbKeys.length; j++) {
/* 1076 */           if (!localSet1.contains(this.kerbKeys[j])) {
/* 1077 */             localSet1.add(this.kerbKeys[j]);
/*      */           }
/* 1079 */           this.encKeys[j].destroy();
/* 1080 */           this.encKeys[j] = null;
/* 1081 */           if (this.debug) {
/* 1082 */             System.out.println("Added server's key" + this.kerbKeys[j]);
/*      */ 
/* 1084 */             System.out.println("\t\t[Krb5LoginModule] added Krb5Principal  " + this.kerbClientPrinc.toString() + " to Subject");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/* 1093 */     this.commitSucceeded = true;
/* 1094 */     if (this.debug)
/* 1095 */       System.out.println("Commit Succeeded \n");
/* 1096 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean abort()
/*      */     throws LoginException
/*      */   {
/* 1119 */     if (!this.succeeded)
/* 1120 */       return false;
/* 1121 */     if ((this.succeeded == true) && (!this.commitSucceeded))
/*      */     {
/* 1123 */       this.succeeded = false;
/* 1124 */       cleanKerberosCred();
/*      */     }
/*      */     else
/*      */     {
/* 1128 */       logout();
/*      */     }
/* 1130 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean logout()
/*      */     throws LoginException
/*      */   {
/* 1148 */     if (this.debug) {
/* 1149 */       System.out.println("\t\t[Krb5LoginModule]: Entering logout");
/*      */     }
/*      */ 
/* 1153 */     if (this.subject.isReadOnly()) {
/* 1154 */       cleanKerberosCred();
/* 1155 */       throw new LoginException("Subject is Readonly");
/*      */     }
/*      */ 
/* 1158 */     this.subject.getPrincipals().remove(this.kerbClientPrinc);
/*      */ 
/* 1160 */     Iterator localIterator = this.subject.getPrivateCredentials().iterator();
/* 1161 */     while (localIterator.hasNext()) {
/* 1162 */       Object localObject = localIterator.next();
/* 1163 */       if (((localObject instanceof KerberosTicket)) || ((localObject instanceof KerberosKey)) || ((localObject instanceof javax.security.auth.kerberos.KeyTab)))
/*      */       {
/* 1166 */         localIterator.remove();
/*      */       }
/*      */     }
/*      */ 
/* 1170 */     cleanKerberosCred();
/*      */ 
/* 1172 */     this.succeeded = false;
/* 1173 */     this.commitSucceeded = false;
/* 1174 */     if (this.debug) {
/* 1175 */       System.out.println("\t\t[Krb5LoginModule]: logged out Subject");
/*      */     }
/*      */ 
/* 1178 */     return true;
/*      */   }
/*      */ 
/*      */   private void cleanKerberosCred()
/*      */     throws LoginException
/*      */   {
/*      */     try
/*      */     {
/* 1187 */       if (this.kerbTicket != null)
/* 1188 */         this.kerbTicket.destroy();
/* 1189 */       if (this.kerbKeys != null)
/* 1190 */         for (int i = 0; i < this.kerbKeys.length; i++)
/* 1191 */           this.kerbKeys[i].destroy();
/*      */     }
/*      */     catch (DestroyFailedException localDestroyFailedException)
/*      */     {
/* 1195 */       throw new LoginException("Destroy Failed on Kerberos Private Credentials");
/*      */     }
/*      */ 
/* 1198 */     this.kerbTicket = null;
/* 1199 */     this.kerbKeys = null;
/* 1200 */     this.kerbClientPrinc = null;
/*      */   }
/*      */ 
/*      */   private void cleanState()
/*      */   {
/* 1210 */     if (this.succeeded) {
/* 1211 */       if ((this.storePass) && (!this.sharedState.containsKey("javax.security.auth.login.name")) && (!this.sharedState.containsKey("javax.security.auth.login.password")))
/*      */       {
/* 1214 */         this.sharedState.put("javax.security.auth.login.name", this.username);
/* 1215 */         this.sharedState.put("javax.security.auth.login.password", this.password);
/*      */       }
/*      */     }
/*      */     else {
/* 1219 */       this.encKeys = null;
/* 1220 */       this.ktab = null;
/* 1221 */       this.principal = null;
/*      */     }
/* 1223 */     this.username = null;
/* 1224 */     this.password = null;
/* 1225 */     if ((this.krb5PrincName != null) && (this.krb5PrincName.length() != 0))
/* 1226 */       this.krb5PrincName.delete(0, this.krb5PrincName.length());
/* 1227 */     this.krb5PrincName = null;
/* 1228 */     if (this.clearPass) {
/* 1229 */       this.sharedState.remove("javax.security.auth.login.name");
/* 1230 */       this.sharedState.remove("javax.security.auth.login.password");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.Krb5LoginModule
 * JD-Core Version:    0.6.2
 */