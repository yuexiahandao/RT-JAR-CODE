/*     */ package com.sun.security.auth.module;
/*     */ 
/*     */ import com.sun.security.auth.UnixNumericGroupPrincipal;
/*     */ import com.sun.security.auth.UnixNumericUserPrincipal;
/*     */ import com.sun.security.auth.UnixPrincipal;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.security.auth.spi.LoginModule;
/*     */ 
/*     */ public class JndiLoginModule
/*     */   implements LoginModule
/*     */ {
/* 156 */   static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */ 
/* 160 */   public final String USER_PROVIDER = "user.provider.url";
/* 161 */   public final String GROUP_PROVIDER = "group.provider.url";
/*     */ 
/* 164 */   private boolean debug = false;
/* 165 */   private boolean strongDebug = false;
/*     */   private String userProvider;
/*     */   private String groupProvider;
/* 168 */   private boolean useFirstPass = false;
/* 169 */   private boolean tryFirstPass = false;
/* 170 */   private boolean storePass = false;
/* 171 */   private boolean clearPass = false;
/*     */ 
/* 174 */   private boolean succeeded = false;
/* 175 */   private boolean commitSucceeded = false;
/*     */   private String username;
/*     */   private char[] password;
/*     */   DirContext ctx;
/*     */   private UnixPrincipal userPrincipal;
/*     */   private UnixNumericUserPrincipal UIDPrincipal;
/*     */   private UnixNumericGroupPrincipal GIDPrincipal;
/* 186 */   private LinkedList<UnixNumericGroupPrincipal> supplementaryGroups = new LinkedList();
/*     */   private Subject subject;
/*     */   private CallbackHandler callbackHandler;
/*     */   private Map sharedState;
/*     */   private Map<String, ?> options;
/*     */   private static final String CRYPT = "{crypt}";
/*     */   private static final String USER_PWD = "userPassword";
/*     */   private static final String USER_UID = "uidNumber";
/*     */   private static final String USER_GID = "gidNumber";
/*     */   private static final String GROUP_ID = "gidNumber";
/*     */   private static final String NAME = "javax.security.auth.login.name";
/*     */   private static final String PWD = "javax.security.auth.login.password";
/*     */ 
/*     */   public void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2)
/*     */   {
/* 224 */     this.subject = paramSubject;
/* 225 */     this.callbackHandler = paramCallbackHandler;
/* 226 */     this.sharedState = paramMap1;
/* 227 */     this.options = paramMap2;
/*     */ 
/* 230 */     this.debug = "true".equalsIgnoreCase((String)paramMap2.get("debug"));
/* 231 */     this.strongDebug = "true".equalsIgnoreCase((String)paramMap2.get("strongDebug"));
/*     */ 
/* 233 */     this.userProvider = ((String)paramMap2.get("user.provider.url"));
/* 234 */     this.groupProvider = ((String)paramMap2.get("group.provider.url"));
/* 235 */     this.tryFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("tryFirstPass"));
/*     */ 
/* 237 */     this.useFirstPass = "true".equalsIgnoreCase((String)paramMap2.get("useFirstPass"));
/*     */ 
/* 239 */     this.storePass = "true".equalsIgnoreCase((String)paramMap2.get("storePass"));
/*     */ 
/* 241 */     this.clearPass = "true".equalsIgnoreCase((String)paramMap2.get("clearPass"));
/*     */   }
/*     */ 
/*     */   public boolean login()
/*     */     throws LoginException
/*     */   {
/* 261 */     if (this.userProvider == null) {
/* 262 */       throw new LoginException("Error: Unable to locate JNDI user provider");
/*     */     }
/*     */ 
/* 265 */     if (this.groupProvider == null) {
/* 266 */       throw new LoginException("Error: Unable to locate JNDI group provider");
/*     */     }
/*     */ 
/* 270 */     if (this.debug) {
/* 271 */       System.out.println("\t\t[JndiLoginModule] user provider: " + this.userProvider);
/*     */ 
/* 273 */       System.out.println("\t\t[JndiLoginModule] group provider: " + this.groupProvider);
/*     */     }
/*     */ 
/* 278 */     if (this.tryFirstPass)
/*     */     {
/*     */       try
/*     */       {
/* 283 */         attemptAuthentication(true);
/*     */ 
/* 286 */         this.succeeded = true;
/* 287 */         if (this.debug) {
/* 288 */           System.out.println("\t\t[JndiLoginModule] tryFirstPass succeeded");
/*     */         }
/*     */ 
/* 291 */         return true;
/*     */       }
/*     */       catch (LoginException localLoginException1) {
/* 294 */         cleanState();
/* 295 */         if (this.debug) {
/* 296 */           System.out.println("\t\t[JndiLoginModule] tryFirstPass failed with:" + localLoginException1.toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/* 302 */     else if (this.useFirstPass)
/*     */     {
/*     */       try
/*     */       {
/* 307 */         attemptAuthentication(true);
/*     */ 
/* 310 */         this.succeeded = true;
/* 311 */         if (this.debug) {
/* 312 */           System.out.println("\t\t[JndiLoginModule] useFirstPass succeeded");
/*     */         }
/*     */ 
/* 315 */         return true;
/*     */       }
/*     */       catch (LoginException localLoginException2) {
/* 318 */         cleanState();
/* 319 */         if (this.debug) {
/* 320 */           System.out.println("\t\t[JndiLoginModule] useFirstPass failed");
/*     */         }
/*     */ 
/* 323 */         throw localLoginException2;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 329 */       attemptAuthentication(false);
/*     */ 
/* 332 */       this.succeeded = true;
/* 333 */       if (this.debug) {
/* 334 */         System.out.println("\t\t[JndiLoginModule] regular authentication succeeded");
/*     */       }
/*     */ 
/* 337 */       return true;
/*     */     } catch (LoginException localLoginException3) {
/* 339 */       cleanState();
/* 340 */       if (this.debug) {
/* 341 */         System.out.println("\t\t[JndiLoginModule] regular authentication failed");
/*     */       }
/*     */ 
/* 344 */       throw localLoginException3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean commit()
/*     */     throws LoginException
/*     */   {
/* 374 */     if (!this.succeeded) {
/* 375 */       return false;
/*     */     }
/* 377 */     if (this.subject.isReadOnly()) {
/* 378 */       cleanState();
/* 379 */       throw new LoginException("Subject is Readonly");
/*     */     }
/*     */ 
/* 382 */     if (!this.subject.getPrincipals().contains(this.userPrincipal))
/* 383 */       this.subject.getPrincipals().add(this.userPrincipal);
/* 384 */     if (!this.subject.getPrincipals().contains(this.UIDPrincipal))
/* 385 */       this.subject.getPrincipals().add(this.UIDPrincipal);
/* 386 */     if (!this.subject.getPrincipals().contains(this.GIDPrincipal))
/* 387 */       this.subject.getPrincipals().add(this.GIDPrincipal);
/* 388 */     for (int i = 0; i < this.supplementaryGroups.size(); i++) {
/* 389 */       if (!this.subject.getPrincipals().contains(this.supplementaryGroups.get(i)))
/*     */       {
/* 391 */         this.subject.getPrincipals().add(this.supplementaryGroups.get(i));
/*     */       }
/*     */     }
/* 394 */     if (this.debug) {
/* 395 */       System.out.println("\t\t[JndiLoginModule]: added UnixPrincipal,");
/*     */ 
/* 397 */       System.out.println("\t\t\t\tUnixNumericUserPrincipal,");
/* 398 */       System.out.println("\t\t\t\tUnixNumericGroupPrincipal(s),");
/* 399 */       System.out.println("\t\t\t to Subject");
/*     */     }
/*     */ 
/* 403 */     cleanState();
/* 404 */     this.commitSucceeded = true;
/* 405 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean abort()
/*     */     throws LoginException
/*     */   {
/* 427 */     if (this.debug) {
/* 428 */       System.out.println("\t\t[JndiLoginModule]: aborted authentication failed");
/*     */     }
/*     */ 
/* 431 */     if (!this.succeeded)
/* 432 */       return false;
/* 433 */     if ((this.succeeded == true) && (!this.commitSucceeded))
/*     */     {
/* 436 */       this.succeeded = false;
/* 437 */       cleanState();
/*     */ 
/* 439 */       this.userPrincipal = null;
/* 440 */       this.UIDPrincipal = null;
/* 441 */       this.GIDPrincipal = null;
/* 442 */       this.supplementaryGroups = new LinkedList();
/*     */     }
/*     */     else
/*     */     {
/* 446 */       logout();
/*     */     }
/* 448 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean logout()
/*     */     throws LoginException
/*     */   {
/* 465 */     if (this.subject.isReadOnly()) {
/* 466 */       cleanState();
/* 467 */       throw new LoginException("Subject is Readonly");
/*     */     }
/* 469 */     this.subject.getPrincipals().remove(this.userPrincipal);
/* 470 */     this.subject.getPrincipals().remove(this.UIDPrincipal);
/* 471 */     this.subject.getPrincipals().remove(this.GIDPrincipal);
/* 472 */     for (int i = 0; i < this.supplementaryGroups.size(); i++) {
/* 473 */       this.subject.getPrincipals().remove(this.supplementaryGroups.get(i));
/*     */     }
/*     */ 
/* 478 */     cleanState();
/* 479 */     this.succeeded = false;
/* 480 */     this.commitSucceeded = false;
/*     */ 
/* 482 */     this.userPrincipal = null;
/* 483 */     this.UIDPrincipal = null;
/* 484 */     this.GIDPrincipal = null;
/* 485 */     this.supplementaryGroups = new LinkedList();
/*     */ 
/* 487 */     if (this.debug) {
/* 488 */       System.out.println("\t\t[JndiLoginModule]: logged out Subject");
/*     */     }
/*     */ 
/* 491 */     return true;
/*     */   }
/*     */ 
/*     */   private void attemptAuthentication(boolean paramBoolean)
/*     */     throws LoginException
/*     */   {
/* 505 */     String str1 = null;
/*     */ 
/* 508 */     getUsernamePassword(paramBoolean);
/*     */     try
/*     */     {
/* 513 */       InitialContext localInitialContext = new InitialContext();
/* 514 */       this.ctx = ((DirContext)localInitialContext.lookup(this.userProvider));
/*     */ 
/* 526 */       SearchControls localSearchControls = new SearchControls();
/* 527 */       NamingEnumeration localNamingEnumeration = this.ctx.search("", "(uid=" + this.username + ")", localSearchControls);
/*     */ 
/* 530 */       if (localNamingEnumeration.hasMore()) {
/* 531 */         SearchResult localSearchResult = (SearchResult)localNamingEnumeration.next();
/* 532 */         Attributes localAttributes = localSearchResult.getAttributes();
/*     */ 
/* 551 */         Attribute localAttribute1 = localAttributes.get("userPassword");
/* 552 */         String str2 = new String((byte[])localAttribute1.get(), "UTF8");
/* 553 */         str1 = str2.substring("{crypt}".length());
/*     */ 
/* 556 */         if (verifyPassword(str1, new String(this.password)) == true)
/*     */         {
/* 560 */           if (this.debug) {
/* 561 */             System.out.println("\t\t[JndiLoginModule] attemptAuthentication() succeeded");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 566 */           if (this.debug) {
/* 567 */             System.out.println("\t\t[JndiLoginModule] attemptAuthentication() failed");
/*     */           }
/* 569 */           throw new FailedLoginException("Login incorrect");
/*     */         }
/*     */ 
/* 574 */         if ((this.storePass) && (!this.sharedState.containsKey("javax.security.auth.login.name")) && (!this.sharedState.containsKey("javax.security.auth.login.password")))
/*     */         {
/* 577 */           this.sharedState.put("javax.security.auth.login.name", this.username);
/* 578 */           this.sharedState.put("javax.security.auth.login.password", this.password);
/*     */         }
/*     */ 
/* 582 */         this.userPrincipal = new UnixPrincipal(this.username);
/*     */ 
/* 585 */         Attribute localAttribute2 = localAttributes.get("uidNumber");
/* 586 */         String str3 = (String)localAttribute2.get();
/* 587 */         this.UIDPrincipal = new UnixNumericUserPrincipal(str3);
/* 588 */         if ((this.debug) && (str3 != null)) {
/* 589 */           System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has UID: " + str3);
/*     */         }
/*     */ 
/* 595 */         Attribute localAttribute3 = localAttributes.get("gidNumber");
/* 596 */         String str4 = (String)localAttribute3.get();
/* 597 */         this.GIDPrincipal = new UnixNumericGroupPrincipal(str4, true);
/*     */ 
/* 599 */         if ((this.debug) && (str4 != null)) {
/* 600 */           System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has GID: " + str4);
/*     */         }
/*     */ 
/* 606 */         this.ctx = ((DirContext)localInitialContext.lookup(this.groupProvider));
/* 607 */         localNamingEnumeration = this.ctx.search("", new BasicAttributes("memberUid", this.username));
/*     */ 
/* 609 */         while (localNamingEnumeration.hasMore()) {
/* 610 */           localSearchResult = (SearchResult)localNamingEnumeration.next();
/* 611 */           localAttributes = localSearchResult.getAttributes();
/*     */ 
/* 613 */           localAttribute3 = localAttributes.get("gidNumber");
/* 614 */           String str5 = (String)localAttribute3.get();
/* 615 */           if (!str4.equals(str5)) {
/* 616 */             UnixNumericGroupPrincipal localUnixNumericGroupPrincipal = new UnixNumericGroupPrincipal(str5, false);
/*     */ 
/* 618 */             this.supplementaryGroups.add(localUnixNumericGroupPrincipal);
/* 619 */             if ((this.debug) && (str5 != null)) {
/* 620 */               System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has Supplementary Group: " + str5);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 630 */         if (this.debug) {
/* 631 */           System.out.println("\t\t[JndiLoginModule]: User not found");
/*     */         }
/* 633 */         throw new FailedLoginException("User not found");
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 637 */       if (this.debug) {
/* 638 */         System.out.println("\t\t[JndiLoginModule]:  User not found");
/* 639 */         localNamingException.printStackTrace();
/*     */       }
/* 641 */       throw new FailedLoginException("User not found");
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 644 */       if (this.debug) {
/* 645 */         System.out.println("\t\t[JndiLoginModule]:  password incorrectly encoded");
/*     */ 
/* 647 */         localUnsupportedEncodingException.printStackTrace();
/*     */       }
/* 649 */       throw new LoginException("Login failure due to incorrect password encoding in the password database");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void getUsernamePassword(boolean paramBoolean)
/*     */     throws LoginException
/*     */   {
/* 673 */     if (paramBoolean)
/*     */     {
/* 675 */       this.username = ((String)this.sharedState.get("javax.security.auth.login.name"));
/* 676 */       this.password = ((char[])this.sharedState.get("javax.security.auth.login.password"));
/* 677 */       return;
/*     */     }
/*     */ 
/* 681 */     if (this.callbackHandler == null) {
/* 682 */       throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");
/*     */     }
/*     */ 
/* 685 */     String str = this.userProvider.substring(0, this.userProvider.indexOf(":"));
/*     */ 
/* 687 */     Callback[] arrayOfCallback = new Callback[2];
/* 688 */     arrayOfCallback[0] = new NameCallback(str + " " + rb.getString("username."));
/*     */ 
/* 690 */     arrayOfCallback[1] = new PasswordCallback(str + " " + rb.getString("password."), false);
/*     */     try
/*     */     {
/* 695 */       this.callbackHandler.handle(arrayOfCallback);
/* 696 */       this.username = ((NameCallback)arrayOfCallback[0]).getName();
/* 697 */       char[] arrayOfChar = ((PasswordCallback)arrayOfCallback[1]).getPassword();
/* 698 */       this.password = new char[arrayOfChar.length];
/* 699 */       System.arraycopy(arrayOfChar, 0, this.password, 0, arrayOfChar.length);
/*     */ 
/* 701 */       ((PasswordCallback)arrayOfCallback[1]).clearPassword();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 704 */       throw new LoginException(localIOException.toString());
/*     */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 706 */       throw new LoginException("Error: " + localUnsupportedCallbackException.getCallback().toString() + " not available to garner authentication information " + "from the user");
/*     */     }
/*     */ 
/* 712 */     if (this.strongDebug) {
/* 713 */       System.out.println("\t\t[JndiLoginModule] user entered username: " + this.username);
/*     */ 
/* 716 */       System.out.print("\t\t[JndiLoginModule] user entered password: ");
/*     */ 
/* 718 */       for (int i = 0; i < this.password.length; i++)
/* 719 */         System.out.print(this.password[i]);
/* 720 */       System.out.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean verifyPassword(String paramString1, String paramString2)
/*     */   {
/* 729 */     if (paramString1 == null) {
/* 730 */       return false;
/*     */     }
/* 732 */     Crypt localCrypt = new Crypt();
/*     */     try {
/* 734 */       byte[] arrayOfByte1 = paramString1.getBytes("UTF8");
/* 735 */       byte[] arrayOfByte2 = localCrypt.crypt(paramString2.getBytes("UTF8"), arrayOfByte1);
/*     */ 
/* 737 */       if (arrayOfByte2.length != arrayOfByte1.length)
/* 738 */         return false;
/* 739 */       for (int i = 0; i < arrayOfByte2.length; i++)
/* 740 */         if (arrayOfByte1[i] != arrayOfByte2[i])
/* 741 */           return false;
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/* 745 */       return false;
/*     */     }
/* 747 */     return true;
/*     */   }
/*     */ 
/*     */   private void cleanState()
/*     */   {
/* 754 */     this.username = null;
/* 755 */     if (this.password != null) {
/* 756 */       for (int i = 0; i < this.password.length; i++)
/* 757 */         this.password[i] = ' ';
/* 758 */       this.password = null;
/*     */     }
/* 760 */     this.ctx = null;
/*     */ 
/* 762 */     if (this.clearPass) {
/* 763 */       this.sharedState.remove("javax.security.auth.login.name");
/* 764 */       this.sharedState.remove("javax.security.auth.login.password");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.JndiLoginModule
 * JD-Core Version:    0.6.2
 */