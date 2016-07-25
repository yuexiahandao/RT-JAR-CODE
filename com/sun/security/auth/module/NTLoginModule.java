/*     */ package com.sun.security.auth.module;
/*     */ 
/*     */ import com.sun.security.auth.NTDomainPrincipal;
/*     */ import com.sun.security.auth.NTNumericCredential;
/*     */ import com.sun.security.auth.NTSidDomainPrincipal;
/*     */ import com.sun.security.auth.NTSidGroupPrincipal;
/*     */ import com.sun.security.auth.NTSidPrimaryGroupPrincipal;
/*     */ import com.sun.security.auth.NTSidUserPrincipal;
/*     */ import com.sun.security.auth.NTUserPrincipal;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.security.auth.spi.LoginModule;
/*     */ 
/*     */ public class NTLoginModule
/*     */   implements LoginModule
/*     */ {
/*     */   private NTSystem ntSystem;
/*     */   private Subject subject;
/*     */   private CallbackHandler callbackHandler;
/*     */   private Map<String, ?> sharedState;
/*     */   private Map<String, ?> options;
/*  71 */   private boolean debug = false;
/*  72 */   private boolean debugNative = false;
/*     */ 
/*  75 */   private boolean succeeded = false;
/*  76 */   private boolean commitSucceeded = false;
/*     */   private NTUserPrincipal userPrincipal;
/*     */   private NTSidUserPrincipal userSID;
/*     */   private NTDomainPrincipal userDomain;
/*     */   private NTSidDomainPrincipal domainSID;
/*     */   private NTSidPrimaryGroupPrincipal primaryGroup;
/*     */   private NTSidGroupPrincipal[] groups;
/*     */   private NTNumericCredential iToken;
/*     */ 
/*     */   public void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2)
/*     */   {
/* 110 */     this.subject = paramSubject;
/* 111 */     this.callbackHandler = paramCallbackHandler;
/* 112 */     this.sharedState = paramMap1;
/* 113 */     this.options = paramMap2;
/*     */ 
/* 116 */     this.debug = "true".equalsIgnoreCase((String)paramMap2.get("debug"));
/* 117 */     this.debugNative = "true".equalsIgnoreCase((String)paramMap2.get("debugNative"));
/*     */ 
/* 119 */     if (this.debugNative == true)
/* 120 */       this.debug = true;
/*     */   }
/*     */ 
/*     */   public boolean login()
/*     */     throws LoginException
/*     */   {
/* 139 */     this.succeeded = false;
/*     */ 
/* 141 */     this.ntSystem = new NTSystem(this.debugNative);
/* 142 */     if (this.ntSystem == null) {
/* 143 */       if (this.debug) {
/* 144 */         System.out.println("\t\t[NTLoginModule] Failed in NT login");
/*     */       }
/*     */ 
/* 147 */       throw new FailedLoginException("Failed in attempt to import the underlying NT system identity information");
/*     */     }
/*     */ 
/* 152 */     if (this.ntSystem.getName() == null) {
/* 153 */       throw new FailedLoginException("Failed in attempt to import the underlying NT system identity information");
/*     */     }
/*     */ 
/* 157 */     this.userPrincipal = new NTUserPrincipal(this.ntSystem.getName());
/* 158 */     if (this.debug) {
/* 159 */       System.out.println("\t\t[NTLoginModule] succeeded importing info: ");
/*     */ 
/* 161 */       System.out.println("\t\t\tuser name = " + this.userPrincipal.getName());
/*     */     }
/*     */ 
/* 165 */     if (this.ntSystem.getUserSID() != null) {
/* 166 */       this.userSID = new NTSidUserPrincipal(this.ntSystem.getUserSID());
/* 167 */       if (this.debug) {
/* 168 */         System.out.println("\t\t\tuser SID = " + this.userSID.getName());
/*     */       }
/*     */     }
/*     */ 
/* 172 */     if (this.ntSystem.getDomain() != null) {
/* 173 */       this.userDomain = new NTDomainPrincipal(this.ntSystem.getDomain());
/* 174 */       if (this.debug) {
/* 175 */         System.out.println("\t\t\tuser domain = " + this.userDomain.getName());
/*     */       }
/*     */     }
/*     */ 
/* 179 */     if (this.ntSystem.getDomainSID() != null) {
/* 180 */       this.domainSID = new NTSidDomainPrincipal(this.ntSystem.getDomainSID());
/*     */ 
/* 182 */       if (this.debug) {
/* 183 */         System.out.println("\t\t\tuser domain SID = " + this.domainSID.getName());
/*     */       }
/*     */     }
/*     */ 
/* 187 */     if (this.ntSystem.getPrimaryGroupID() != null) {
/* 188 */       this.primaryGroup = new NTSidPrimaryGroupPrincipal(this.ntSystem.getPrimaryGroupID());
/*     */ 
/* 190 */       if (this.debug) {
/* 191 */         System.out.println("\t\t\tuser primary group = " + this.primaryGroup.getName());
/*     */       }
/*     */     }
/*     */ 
/* 195 */     if ((this.ntSystem.getGroupIDs() != null) && (this.ntSystem.getGroupIDs().length > 0))
/*     */     {
/* 198 */       String[] arrayOfString = this.ntSystem.getGroupIDs();
/* 199 */       this.groups = new NTSidGroupPrincipal[arrayOfString.length];
/* 200 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 201 */         this.groups[i] = new NTSidGroupPrincipal(arrayOfString[i]);
/* 202 */         if (this.debug) {
/* 203 */           System.out.println("\t\t\tuser group = " + this.groups[i].getName());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 208 */     if (this.ntSystem.getImpersonationToken() != 0L) {
/* 209 */       this.iToken = new NTNumericCredential(this.ntSystem.getImpersonationToken());
/* 210 */       if (this.debug) {
/* 211 */         System.out.println("\t\t\timpersonation token = " + this.ntSystem.getImpersonationToken());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     this.succeeded = true;
/* 217 */     return this.succeeded;
/*     */   }
/*     */ 
/*     */   public boolean commit()
/*     */     throws LoginException
/*     */   {
/* 243 */     if (!this.succeeded) {
/* 244 */       if (this.debug) {
/* 245 */         System.out.println("\t\t[NTLoginModule]: did not add any Principals to Subject because own authentication failed.");
/*     */       }
/*     */ 
/* 249 */       return false;
/*     */     }
/* 251 */     if (this.subject.isReadOnly()) {
/* 252 */       throw new LoginException("Subject is ReadOnly");
/*     */     }
/* 254 */     Set localSet1 = this.subject.getPrincipals();
/*     */ 
/* 257 */     if (!localSet1.contains(this.userPrincipal)) {
/* 258 */       localSet1.add(this.userPrincipal);
/*     */     }
/* 260 */     if ((this.userSID != null) && (!localSet1.contains(this.userSID))) {
/* 261 */       localSet1.add(this.userSID);
/*     */     }
/*     */ 
/* 264 */     if ((this.userDomain != null) && (!localSet1.contains(this.userDomain))) {
/* 265 */       localSet1.add(this.userDomain);
/*     */     }
/* 267 */     if ((this.domainSID != null) && (!localSet1.contains(this.domainSID))) {
/* 268 */       localSet1.add(this.domainSID);
/*     */     }
/*     */ 
/* 271 */     if ((this.primaryGroup != null) && (!localSet1.contains(this.primaryGroup))) {
/* 272 */       localSet1.add(this.primaryGroup);
/*     */     }
/* 274 */     for (int i = 0; (this.groups != null) && (i < this.groups.length); i++) {
/* 275 */       if (!localSet1.contains(this.groups[i])) {
/* 276 */         localSet1.add(this.groups[i]);
/*     */       }
/*     */     }
/*     */ 
/* 280 */     Set localSet2 = this.subject.getPublicCredentials();
/* 281 */     if ((this.iToken != null) && (!localSet2.contains(this.iToken))) {
/* 282 */       localSet2.add(this.iToken);
/*     */     }
/* 284 */     this.commitSucceeded = true;
/* 285 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean abort()
/*     */     throws LoginException
/*     */   {
/* 308 */     if (this.debug) {
/* 309 */       System.out.println("\t\t[NTLoginModule]: aborted authentication attempt");
/*     */     }
/*     */ 
/* 313 */     if (!this.succeeded)
/* 314 */       return false;
/* 315 */     if ((this.succeeded == true) && (!this.commitSucceeded)) {
/* 316 */       this.ntSystem = null;
/* 317 */       this.userPrincipal = null;
/* 318 */       this.userSID = null;
/* 319 */       this.userDomain = null;
/* 320 */       this.domainSID = null;
/* 321 */       this.primaryGroup = null;
/* 322 */       this.groups = null;
/* 323 */       this.iToken = null;
/* 324 */       this.succeeded = false;
/*     */     }
/*     */     else
/*     */     {
/* 328 */       logout();
/*     */     }
/* 330 */     return this.succeeded;
/*     */   }
/*     */ 
/*     */   public boolean logout()
/*     */     throws LoginException
/*     */   {
/* 351 */     if (this.subject.isReadOnly()) {
/* 352 */       throw new LoginException("Subject is ReadOnly");
/*     */     }
/* 354 */     Set localSet1 = this.subject.getPrincipals();
/* 355 */     if (localSet1.contains(this.userPrincipal)) {
/* 356 */       localSet1.remove(this.userPrincipal);
/*     */     }
/* 358 */     if (localSet1.contains(this.userSID)) {
/* 359 */       localSet1.remove(this.userSID);
/*     */     }
/* 361 */     if (localSet1.contains(this.userDomain)) {
/* 362 */       localSet1.remove(this.userDomain);
/*     */     }
/* 364 */     if (localSet1.contains(this.domainSID)) {
/* 365 */       localSet1.remove(this.domainSID);
/*     */     }
/* 367 */     if (localSet1.contains(this.primaryGroup)) {
/* 368 */       localSet1.remove(this.primaryGroup);
/*     */     }
/* 370 */     for (int i = 0; (this.groups != null) && (i < this.groups.length); i++) {
/* 371 */       if (localSet1.contains(this.groups[i])) {
/* 372 */         localSet1.remove(this.groups[i]);
/*     */       }
/*     */     }
/*     */ 
/* 376 */     Set localSet2 = this.subject.getPublicCredentials();
/* 377 */     if (localSet2.contains(this.iToken)) {
/* 378 */       localSet2.remove(this.iToken);
/*     */     }
/*     */ 
/* 381 */     this.succeeded = false;
/* 382 */     this.commitSucceeded = false;
/* 383 */     this.userPrincipal = null;
/* 384 */     this.userDomain = null;
/* 385 */     this.userSID = null;
/* 386 */     this.domainSID = null;
/* 387 */     this.groups = null;
/* 388 */     this.primaryGroup = null;
/* 389 */     this.iToken = null;
/* 390 */     this.ntSystem = null;
/*     */ 
/* 392 */     if (this.debug) {
/* 393 */       System.out.println("\t\t[NTLoginModule] completed logout processing");
/*     */     }
/*     */ 
/* 396 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.NTLoginModule
 * JD-Core Version:    0.6.2
 */