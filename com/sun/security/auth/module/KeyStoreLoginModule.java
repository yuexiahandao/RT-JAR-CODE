/*     */ package com.sun.security.auth.module;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AuthProvider;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import javax.security.auth.Destroyable;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.ConfirmationCallback;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.TextOutputCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.security.auth.spi.LoginModule;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import javax.security.auth.x500.X500PrivateCredential;
/*     */ import sun.security.util.Password;
/*     */ 
/*     */ public class KeyStoreLoginModule
/*     */   implements LoginModule
/*     */ {
/* 133 */   static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */   private static final int UNINITIALIZED = 0;
/*     */   private static final int INITIALIZED = 1;
/*     */   private static final int AUTHENTICATED = 2;
/*     */   private static final int LOGGED_IN = 3;
/*     */   private static final int PROTECTED_PATH = 0;
/*     */   private static final int TOKEN = 1;
/*     */   private static final int NORMAL = 2;
/*     */   private static final String NONE = "NONE";
/*     */   private static final String P11KEYSTORE = "PKCS11";
/* 150 */   private static final TextOutputCallback bannerCallback = new TextOutputCallback(0, rb.getString("Please.enter.keystore.information"));
/*     */ 
/* 154 */   private final ConfirmationCallback confirmationCallback = new ConfirmationCallback(0, 2, 3);
/*     */   private Subject subject;
/*     */   private CallbackHandler callbackHandler;
/*     */   private Map sharedState;
/*     */   private Map<String, ?> options;
/*     */   private char[] keyStorePassword;
/*     */   private char[] privateKeyPassword;
/*     */   private KeyStore keyStore;
/*     */   private String keyStoreURL;
/*     */   private String keyStoreType;
/*     */   private String keyStoreProvider;
/*     */   private String keyStoreAlias;
/*     */   private String keyStorePasswordURL;
/*     */   private String privateKeyPasswordURL;
/*     */   private boolean debug;
/*     */   private X500Principal principal;
/*     */   private Certificate[] fromKeyStore;
/* 178 */   private CertPath certP = null;
/*     */   private X500PrivateCredential privateCredential;
/* 180 */   private int status = 0;
/* 181 */   private boolean nullStream = false;
/* 182 */   private boolean token = false;
/* 183 */   private boolean protectedPath = false;
/*     */ 
/*     */   public void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2)
/*     */   {
/* 211 */     this.subject = paramSubject;
/* 212 */     this.callbackHandler = paramCallbackHandler;
/* 213 */     this.sharedState = paramMap1;
/* 214 */     this.options = paramMap2;
/*     */ 
/* 216 */     processOptions();
/* 217 */     this.status = 1;
/*     */   }
/*     */ 
/*     */   private void processOptions() {
/* 221 */     this.keyStoreURL = ((String)this.options.get("keyStoreURL"));
/* 222 */     if (this.keyStoreURL == null) {
/* 223 */       this.keyStoreURL = ("file:" + System.getProperty("user.home").replace(File.separatorChar, '/') + '/' + ".keystore");
/*     */     }
/* 228 */     else if ("NONE".equals(this.keyStoreURL)) {
/* 229 */       this.nullStream = true;
/*     */     }
/* 231 */     this.keyStoreType = ((String)this.options.get("keyStoreType"));
/* 232 */     if (this.keyStoreType == null) {
/* 233 */       this.keyStoreType = KeyStore.getDefaultType();
/*     */     }
/* 235 */     if ("PKCS11".equalsIgnoreCase(this.keyStoreType)) {
/* 236 */       this.token = true;
/*     */     }
/*     */ 
/* 239 */     this.keyStoreProvider = ((String)this.options.get("keyStoreProvider"));
/*     */ 
/* 241 */     this.keyStoreAlias = ((String)this.options.get("keyStoreAlias"));
/*     */ 
/* 243 */     this.keyStorePasswordURL = ((String)this.options.get("keyStorePasswordURL"));
/*     */ 
/* 245 */     this.privateKeyPasswordURL = ((String)this.options.get("privateKeyPasswordURL"));
/*     */ 
/* 247 */     this.protectedPath = "true".equalsIgnoreCase((String)this.options.get("protected"));
/*     */ 
/* 250 */     this.debug = "true".equalsIgnoreCase((String)this.options.get("debug"));
/* 251 */     if (this.debug) {
/* 252 */       debugPrint(null);
/* 253 */       debugPrint("keyStoreURL=" + this.keyStoreURL);
/* 254 */       debugPrint("keyStoreType=" + this.keyStoreType);
/* 255 */       debugPrint("keyStoreProvider=" + this.keyStoreProvider);
/* 256 */       debugPrint("keyStoreAlias=" + this.keyStoreAlias);
/* 257 */       debugPrint("keyStorePasswordURL=" + this.keyStorePasswordURL);
/* 258 */       debugPrint("privateKeyPasswordURL=" + this.privateKeyPasswordURL);
/* 259 */       debugPrint("protectedPath=" + this.protectedPath);
/* 260 */       debugPrint(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean login()
/*     */     throws LoginException
/*     */   {
/* 279 */     switch (this.status) {
/*     */     case 0:
/*     */     default:
/* 282 */       throw new LoginException("The login module is not initialized");
/*     */     case 1:
/*     */     case 2:
/* 286 */       if ((this.token) && (!this.nullStream)) {
/* 287 */         throw new LoginException("if keyStoreType is PKCS11 then keyStoreURL must be NONE");
/*     */       }
/*     */ 
/* 292 */       if ((this.token) && (this.privateKeyPasswordURL != null)) {
/* 293 */         throw new LoginException("if keyStoreType is PKCS11 then privateKeyPasswordURL must not be specified");
/*     */       }
/*     */ 
/* 298 */       if ((this.protectedPath) && ((this.keyStorePasswordURL != null) || (this.privateKeyPasswordURL != null)))
/*     */       {
/* 301 */         throw new LoginException("if protected is true then keyStorePasswordURL and privateKeyPasswordURL must not be specified");
/*     */       }
/*     */ 
/* 308 */       if (this.protectedPath)
/* 309 */         getAliasAndPasswords(0);
/* 310 */       else if (this.token)
/* 311 */         getAliasAndPasswords(1);
/*     */       else {
/* 313 */         getAliasAndPasswords(2);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 320 */         getKeyStoreInfo();
/*     */       } finally {
/* 322 */         if ((this.privateKeyPassword != null) && (this.privateKeyPassword != this.keyStorePassword))
/*     */         {
/* 324 */           Arrays.fill(this.privateKeyPassword, '\000');
/* 325 */           this.privateKeyPassword = null;
/*     */         }
/* 327 */         if (this.keyStorePassword != null) {
/* 328 */           Arrays.fill(this.keyStorePassword, '\000');
/* 329 */           this.keyStorePassword = null;
/*     */         }
/*     */       }
/* 332 */       this.status = 2;
/* 333 */       return true;
/*     */     case 3:
/* 335 */     }return true;
/*     */   }
/*     */ 
/*     */   private void getAliasAndPasswords(int paramInt)
/*     */     throws LoginException
/*     */   {
/* 341 */     if (this.callbackHandler == null)
/*     */     {
/* 345 */       switch (paramInt) {
/*     */       case 0:
/* 347 */         checkAlias();
/* 348 */         break;
/*     */       case 1:
/* 350 */         checkAlias();
/* 351 */         checkStorePass();
/* 352 */         break;
/*     */       case 2:
/* 354 */         checkAlias();
/* 355 */         checkStorePass();
/* 356 */         checkKeyPass();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       NameCallback localNameCallback;
/* 365 */       if ((this.keyStoreAlias == null) || (this.keyStoreAlias.length() == 0)) {
/* 366 */         localNameCallback = new NameCallback(rb.getString("Keystore.alias."));
/*     */       }
/*     */       else {
/* 369 */         localNameCallback = new NameCallback(rb.getString("Keystore.alias."), this.keyStoreAlias);
/*     */       }
/*     */ 
/* 374 */       PasswordCallback localPasswordCallback1 = null;
/* 375 */       PasswordCallback localPasswordCallback2 = null;
/*     */ 
/* 377 */       switch (paramInt) {
/*     */       case 0:
/* 379 */         break;
/*     */       case 2:
/* 381 */         localPasswordCallback2 = new PasswordCallback(rb.getString("Private.key.password.optional."), false);
/*     */       case 1:
/* 385 */         localPasswordCallback1 = new PasswordCallback(rb.getString("Keystore.password."), false);
/*     */       }
/*     */ 
/* 389 */       prompt(localNameCallback, localPasswordCallback1, localPasswordCallback2);
/*     */     }
/*     */ 
/* 392 */     if (this.debug)
/* 393 */       debugPrint("alias=" + this.keyStoreAlias);
/*     */   }
/*     */ 
/*     */   private void checkAlias() throws LoginException
/*     */   {
/* 398 */     if (this.keyStoreAlias == null)
/* 399 */       throw new LoginException("Need to specify an alias option to use KeyStoreLoginModule non-interactively.");
/*     */   }
/*     */ 
/*     */   private void checkStorePass()
/*     */     throws LoginException
/*     */   {
/* 406 */     if (this.keyStorePasswordURL == null) {
/* 407 */       throw new LoginException("Need to specify keyStorePasswordURL option to use KeyStoreLoginModule non-interactively.");
/*     */     }
/*     */ 
/* 411 */     InputStream localInputStream = null;
/*     */     try {
/* 413 */       localInputStream = new URL(this.keyStorePasswordURL).openStream();
/* 414 */       this.keyStorePassword = Password.readPassword(localInputStream);
/*     */     } catch (IOException localIOException2) {
/* 416 */       LoginException localLoginException1 = new LoginException("Problem accessing keystore password \"" + this.keyStorePasswordURL + "\"");
/*     */ 
/* 419 */       localLoginException1.initCause(localIOException2);
/* 420 */       throw localLoginException1;
/*     */     } finally {
/* 422 */       if (localInputStream != null)
/*     */         try {
/* 424 */           localInputStream.close();
/*     */         } catch (IOException localIOException3) {
/* 426 */           LoginException localLoginException2 = new LoginException("Problem closing the keystore password stream");
/*     */ 
/* 428 */           localLoginException2.initCause(localIOException3);
/* 429 */           throw localLoginException2;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkKeyPass() throws LoginException
/*     */   {
/* 436 */     if (this.privateKeyPasswordURL == null) {
/* 437 */       this.privateKeyPassword = this.keyStorePassword;
/*     */     } else {
/* 439 */       InputStream localInputStream = null;
/*     */       try {
/* 441 */         localInputStream = new URL(this.privateKeyPasswordURL).openStream();
/* 442 */         this.privateKeyPassword = Password.readPassword(localInputStream);
/*     */       } catch (IOException localIOException2) {
/* 444 */         LoginException localLoginException1 = new LoginException("Problem accessing private key password \"" + this.privateKeyPasswordURL + "\"");
/*     */ 
/* 447 */         localLoginException1.initCause(localIOException2);
/* 448 */         throw localLoginException1;
/*     */       } finally {
/* 450 */         if (localInputStream != null)
/*     */           try {
/* 452 */             localInputStream.close();
/*     */           } catch (IOException localIOException3) {
/* 454 */             LoginException localLoginException2 = new LoginException("Problem closing the private key password stream");
/*     */ 
/* 456 */             localLoginException2.initCause(localIOException3);
/* 457 */             throw localLoginException2;
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void prompt(NameCallback paramNameCallback, PasswordCallback paramPasswordCallback1, PasswordCallback paramPasswordCallback2)
/*     */     throws LoginException
/*     */   {
/*     */     LoginException localLoginException;
/* 469 */     if (paramPasswordCallback1 == null)
/*     */     {
/*     */       try
/*     */       {
/* 474 */         this.callbackHandler.handle(new Callback[] { bannerCallback, paramNameCallback, this.confirmationCallback });
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/* 479 */         localLoginException = new LoginException("Problem retrieving keystore alias");
/*     */ 
/* 481 */         localLoginException.initCause(localIOException1);
/* 482 */         throw localLoginException;
/*     */       } catch (UnsupportedCallbackException localUnsupportedCallbackException1) {
/* 484 */         throw new LoginException("Error: " + localUnsupportedCallbackException1.getCallback().toString() + " is not available to retrieve authentication " + " information from the user");
/*     */       }
/*     */ 
/* 490 */       int i = this.confirmationCallback.getSelectedIndex();
/*     */ 
/* 492 */       if (i == 2) {
/* 493 */         throw new LoginException("Login cancelled");
/*     */       }
/*     */ 
/* 496 */       saveAlias(paramNameCallback);
/*     */     }
/* 498 */     else if (paramPasswordCallback2 == null)
/*     */     {
/*     */       try
/*     */       {
/* 503 */         this.callbackHandler.handle(new Callback[] { bannerCallback, paramNameCallback, paramPasswordCallback1, this.confirmationCallback });
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/* 509 */         localLoginException = new LoginException("Problem retrieving keystore alias and password");
/*     */ 
/* 511 */         localLoginException.initCause(localIOException2);
/* 512 */         throw localLoginException;
/*     */       } catch (UnsupportedCallbackException localUnsupportedCallbackException2) {
/* 514 */         throw new LoginException("Error: " + localUnsupportedCallbackException2.getCallback().toString() + " is not available to retrieve authentication " + " information from the user");
/*     */       }
/*     */ 
/* 520 */       int j = this.confirmationCallback.getSelectedIndex();
/*     */ 
/* 522 */       if (j == 2) {
/* 523 */         throw new LoginException("Login cancelled");
/*     */       }
/*     */ 
/* 526 */       saveAlias(paramNameCallback);
/* 527 */       saveStorePass(paramPasswordCallback1);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 534 */         this.callbackHandler.handle(new Callback[] { bannerCallback, paramNameCallback, paramPasswordCallback1, paramPasswordCallback2, this.confirmationCallback });
/*     */       }
/*     */       catch (IOException localIOException3)
/*     */       {
/* 541 */         localLoginException = new LoginException("Problem retrieving keystore alias and passwords");
/*     */ 
/* 543 */         localLoginException.initCause(localIOException3);
/* 544 */         throw localLoginException;
/*     */       } catch (UnsupportedCallbackException localUnsupportedCallbackException3) {
/* 546 */         throw new LoginException("Error: " + localUnsupportedCallbackException3.getCallback().toString() + " is not available to retrieve authentication " + " information from the user");
/*     */       }
/*     */ 
/* 552 */       int k = this.confirmationCallback.getSelectedIndex();
/*     */ 
/* 554 */       if (k == 2) {
/* 555 */         throw new LoginException("Login cancelled");
/*     */       }
/*     */ 
/* 558 */       saveAlias(paramNameCallback);
/* 559 */       saveStorePass(paramPasswordCallback1);
/* 560 */       saveKeyPass(paramPasswordCallback2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void saveAlias(NameCallback paramNameCallback) {
/* 565 */     this.keyStoreAlias = paramNameCallback.getName();
/*     */   }
/*     */ 
/*     */   private void saveStorePass(PasswordCallback paramPasswordCallback) {
/* 569 */     this.keyStorePassword = paramPasswordCallback.getPassword();
/* 570 */     if (this.keyStorePassword == null)
/*     */     {
/* 572 */       this.keyStorePassword = new char[0];
/*     */     }
/* 574 */     paramPasswordCallback.clearPassword();
/*     */   }
/*     */ 
/*     */   private void saveKeyPass(PasswordCallback paramPasswordCallback) {
/* 578 */     this.privateKeyPassword = paramPasswordCallback.getPassword();
/* 579 */     if ((this.privateKeyPassword == null) || (this.privateKeyPassword.length == 0))
/*     */     {
/* 584 */       this.privateKeyPassword = this.keyStorePassword;
/*     */     }
/* 586 */     paramPasswordCallback.clearPassword();
/*     */   }
/*     */ 
/*     */   private void getKeyStoreInfo()
/*     */     throws LoginException
/*     */   {
/*     */     try
/*     */     {
/* 594 */       if (this.keyStoreProvider == null)
/* 595 */         this.keyStore = KeyStore.getInstance(this.keyStoreType);
/*     */       else
/* 597 */         this.keyStore = KeyStore.getInstance(this.keyStoreType, this.keyStoreProvider);
/*     */     }
/*     */     catch (KeyStoreException localKeyStoreException1)
/*     */     {
/* 601 */       localLoginException1 = new LoginException("The specified keystore type was not available");
/*     */ 
/* 603 */       localLoginException1.initCause(localKeyStoreException1);
/* 604 */       throw localLoginException1;
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/* 606 */       LoginException localLoginException1 = new LoginException("The specified keystore provider was not available");
/*     */ 
/* 608 */       localLoginException1.initCause(localNoSuchProviderException);
/* 609 */       throw localLoginException1;
/*     */     }
/*     */ 
/* 613 */     InputStream localInputStream = null;
/*     */     try {
/* 615 */       if (this.nullStream)
/*     */       {
/* 617 */         this.keyStore.load(null, this.keyStorePassword);
/*     */       } else {
/* 619 */         localInputStream = new URL(this.keyStoreURL).openStream();
/* 620 */         this.keyStore.load(localInputStream, this.keyStorePassword);
/*     */       }
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 623 */       localLoginException2 = new LoginException("Incorrect keyStoreURL option");
/*     */ 
/* 625 */       localLoginException2.initCause(localMalformedURLException);
/* 626 */       throw localLoginException2;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 628 */       localLoginException2 = new LoginException("Error initializing keystore");
/*     */ 
/* 630 */       localLoginException2.initCause(localGeneralSecurityException);
/* 631 */       throw localLoginException2;
/*     */     } catch (IOException localIOException2) {
/* 633 */       LoginException localLoginException2 = new LoginException("Error initializing keystore");
/*     */ 
/* 635 */       localLoginException2.initCause(localIOException2);
/* 636 */       throw localLoginException2;
/*     */     } finally {
/* 638 */       if (localInputStream != null) {
/*     */         try {
/* 640 */           localInputStream.close();
/*     */         } catch (IOException localIOException3) {
/* 642 */           LoginException localLoginException3 = new LoginException("Error initializing keystore");
/*     */ 
/* 644 */           localLoginException3.initCause(localIOException3);
/* 645 */           throw localLoginException3;
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 652 */       this.fromKeyStore = this.keyStore.getCertificateChain(this.keyStoreAlias);
/*     */ 
/* 654 */       if ((this.fromKeyStore == null) || (this.fromKeyStore.length == 0) || (!(this.fromKeyStore[0] instanceof X509Certificate)))
/*     */       {
/* 658 */         throw new FailedLoginException("Unable to find X.509 certificate chain in keystore");
/*     */       }
/*     */ 
/* 661 */       LinkedList localLinkedList = new LinkedList();
/* 662 */       for (int i = 0; i < this.fromKeyStore.length; i++) {
/* 663 */         localLinkedList.add(this.fromKeyStore[i]);
/*     */       }
/* 665 */       localObject1 = CertificateFactory.getInstance("X.509");
/*     */ 
/* 667 */       this.certP = ((CertificateFactory)localObject1).generateCertPath(localLinkedList);
/*     */     }
/*     */     catch (KeyStoreException localKeyStoreException2)
/*     */     {
/* 671 */       localObject1 = new LoginException("Error using keystore");
/* 672 */       ((LoginException)localObject1).initCause(localKeyStoreException2);
/* 673 */       throw ((Throwable)localObject1);
/*     */     } catch (CertificateException localCertificateException) {
/* 675 */       localObject1 = new LoginException("Error: X.509 Certificate type unavailable");
/*     */ 
/* 677 */       ((LoginException)localObject1).initCause(localCertificateException);
/* 678 */       throw ((Throwable)localObject1);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 683 */       X509Certificate localX509Certificate = (X509Certificate)this.fromKeyStore[0];
/* 684 */       this.principal = new X500Principal(localX509Certificate.getSubjectDN().getName());
/*     */ 
/* 688 */       localObject1 = this.keyStore.getKey(this.keyStoreAlias, this.privateKeyPassword);
/* 689 */       if ((localObject1 == null) || (!(localObject1 instanceof PrivateKey)))
/*     */       {
/* 692 */         throw new FailedLoginException("Unable to recover key from keystore");
/*     */       }
/*     */ 
/* 696 */       this.privateCredential = new X500PrivateCredential(localX509Certificate, (PrivateKey)localObject1, this.keyStoreAlias);
/*     */     }
/*     */     catch (KeyStoreException localKeyStoreException3) {
/* 699 */       localObject1 = new LoginException("Error using keystore");
/* 700 */       ((LoginException)localObject1).initCause(localKeyStoreException3);
/* 701 */       throw ((Throwable)localObject1);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 703 */       localObject1 = new LoginException("Error using keystore");
/* 704 */       ((LoginException)localObject1).initCause(localNoSuchAlgorithmException);
/* 705 */       throw ((Throwable)localObject1);
/*     */     } catch (UnrecoverableKeyException localUnrecoverableKeyException) {
/* 707 */       localObject1 = new FailedLoginException("Unable to recover key from keystore");
/*     */ 
/* 709 */       ((FailedLoginException)localObject1).initCause(localUnrecoverableKeyException);
/* 710 */       throw ((Throwable)localObject1);
/*     */     }
/* 712 */     if (this.debug)
/* 713 */       debugPrint("principal=" + this.principal + "\n certificate=" + this.privateCredential.getCertificate() + "\n alias =" + this.privateCredential.getAlias());
/*     */   }
/*     */ 
/*     */   public boolean commit()
/*     */     throws LoginException
/*     */   {
/* 750 */     switch (this.status) {
/*     */     case 0:
/*     */     default:
/* 753 */       throw new LoginException("The login module is not initialized");
/*     */     case 1:
/* 755 */       logoutInternal();
/* 756 */       throw new LoginException("Authentication failed");
/*     */     case 2:
/* 758 */       if (commitInternal()) {
/* 759 */         return true;
/*     */       }
/* 761 */       logoutInternal();
/* 762 */       throw new LoginException("Unable to retrieve certificates");
/*     */     case 3:
/*     */     }
/* 765 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean commitInternal()
/*     */     throws LoginException
/*     */   {
/* 773 */     if (this.subject.isReadOnly()) {
/* 774 */       throw new LoginException("Subject is set readonly");
/*     */     }
/* 776 */     this.subject.getPrincipals().add(this.principal);
/* 777 */     this.subject.getPublicCredentials().add(this.certP);
/* 778 */     this.subject.getPrivateCredentials().add(this.privateCredential);
/* 779 */     this.status = 3;
/* 780 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean abort()
/*     */     throws LoginException
/*     */   {
/* 808 */     switch (this.status) {
/*     */     case 0:
/*     */     default:
/* 811 */       return false;
/*     */     case 1:
/* 813 */       return false;
/*     */     case 2:
/* 815 */       logoutInternal();
/* 816 */       return true;
/*     */     case 3:
/* 818 */     }logoutInternal();
/* 819 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean logout()
/*     */     throws LoginException
/*     */   {
/* 841 */     if (this.debug)
/* 842 */       debugPrint("Entering logout " + this.status);
/* 843 */     switch (this.status) {
/*     */     case 0:
/* 845 */       throw new LoginException("The login module is not initialized");
/*     */     case 1:
/*     */     case 2:
/*     */     default:
/* 853 */       return false;
/*     */     case 3:
/* 855 */     }logoutInternal();
/* 856 */     return true;
/*     */   }
/*     */ 
/*     */   private void logoutInternal() throws LoginException
/*     */   {
/* 861 */     if (this.debug) {
/* 862 */       debugPrint("Entering logoutInternal");
/*     */     }
/*     */ 
/* 867 */     Object localObject1 = null;
/* 868 */     Provider localProvider = this.keyStore.getProvider();
/*     */     Object localObject2;
/* 869 */     if ((localProvider instanceof AuthProvider)) {
/* 870 */       localObject2 = (AuthProvider)localProvider;
/*     */       try {
/* 872 */         ((AuthProvider)localObject2).logout();
/* 873 */         if (this.debug)
/* 874 */           debugPrint("logged out of KeyStore AuthProvider");
/*     */       }
/*     */       catch (LoginException localLoginException1)
/*     */       {
/* 878 */         localObject1 = localLoginException1;
/*     */       }
/*     */     }
/*     */ 
/* 882 */     if (this.subject.isReadOnly())
/*     */     {
/* 885 */       this.principal = null;
/* 886 */       this.certP = null;
/* 887 */       this.status = 1;
/*     */ 
/* 889 */       localObject2 = this.subject.getPrivateCredentials().iterator();
/* 890 */       while (((Iterator)localObject2).hasNext()) {
/* 891 */         Object localObject3 = ((Iterator)localObject2).next();
/* 892 */         if (this.privateCredential.equals(localObject3)) {
/* 893 */           this.privateCredential = null;
/*     */           try {
/* 895 */             ((Destroyable)localObject3).destroy();
/* 896 */             if (this.debug)
/* 897 */               debugPrint("Destroyed private credential, " + localObject3.getClass().getName());
/*     */           }
/*     */           catch (DestroyFailedException localDestroyFailedException)
/*     */           {
/* 901 */             LoginException localLoginException2 = new LoginException("Unable to destroy private credential, " + localObject3.getClass().getName());
/*     */ 
/* 904 */             localLoginException2.initCause(localDestroyFailedException);
/* 905 */             throw localLoginException2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 913 */       throw new LoginException("Unable to remove Principal (X500Principal ) and public credential (certificatepath) from read-only Subject");
/*     */     }
/*     */ 
/* 919 */     if (this.principal != null) {
/* 920 */       this.subject.getPrincipals().remove(this.principal);
/* 921 */       this.principal = null;
/*     */     }
/* 923 */     if (this.certP != null) {
/* 924 */       this.subject.getPublicCredentials().remove(this.certP);
/* 925 */       this.certP = null;
/*     */     }
/* 927 */     if (this.privateCredential != null) {
/* 928 */       this.subject.getPrivateCredentials().remove(this.privateCredential);
/* 929 */       this.privateCredential = null;
/*     */     }
/*     */ 
/* 933 */     if (localObject1 != null) {
/* 934 */       throw localObject1;
/*     */     }
/* 936 */     this.status = 1;
/*     */   }
/*     */ 
/*     */   private void debugPrint(String paramString)
/*     */   {
/* 941 */     if (paramString == null)
/* 942 */       System.err.println();
/*     */     else
/* 944 */       System.err.println("Debug KeyStoreLoginModule: " + paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.KeyStoreLoginModule
 * JD-Core Version:    0.6.2
 */