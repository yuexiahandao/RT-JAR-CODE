/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.ReferralException;
/*     */ import javax.naming.ldap.Control;
/*     */ 
/*     */ public final class LdapReferralException extends javax.naming.ldap.LdapReferralException
/*     */ {
/*     */   private int handleReferrals;
/*     */   private Hashtable envprops;
/*     */   private String nextName;
/*     */   private Control[] reqCtls;
/*  78 */   private Vector referrals = null;
/*  79 */   private int referralIndex = 0;
/*  80 */   private int referralCount = 0;
/*  81 */   private boolean foundEntry = false;
/*  82 */   private boolean skipThisReferral = false;
/*  83 */   private int hopCount = 1;
/*  84 */   private NamingException errorEx = null;
/*  85 */   private String newRdn = null;
/*  86 */   private boolean debug = false;
/*  87 */   LdapReferralException nextReferralEx = null;
/*     */ 
/*     */   LdapReferralException(Name paramName1, Object paramObject, Name paramName2, String paramString1, Hashtable paramHashtable, String paramString2, int paramInt, Control[] paramArrayOfControl)
/*     */   {
/* 106 */     super(paramString1);
/*     */ 
/* 108 */     if (this.debug) {
/* 109 */       System.out.println("LdapReferralException constructor");
/*     */     }
/* 111 */     setResolvedName(paramName1);
/* 112 */     setResolvedObj(paramObject);
/* 113 */     setRemainingName(paramName2);
/* 114 */     this.envprops = paramHashtable;
/* 115 */     this.nextName = paramString2;
/* 116 */     this.handleReferrals = paramInt;
/*     */ 
/* 119 */     this.reqCtls = (paramInt == 1 ? paramArrayOfControl : null);
/*     */   }
/*     */ 
/*     */   public Context getReferralContext()
/*     */     throws NamingException
/*     */   {
/* 128 */     return getReferralContext(this.envprops, null);
/*     */   }
/*     */ 
/*     */   public Context getReferralContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 137 */     return getReferralContext(paramHashtable, null);
/*     */   }
/*     */ 
/*     */   public Context getReferralContext(Hashtable<?, ?> paramHashtable, Control[] paramArrayOfControl)
/*     */     throws NamingException
/*     */   {
/* 147 */     if (this.debug) {
/* 148 */       System.out.println("LdapReferralException.getReferralContext");
/*     */     }
/* 150 */     LdapReferralContext localLdapReferralContext = new LdapReferralContext(this, paramHashtable, paramArrayOfControl, this.reqCtls, this.nextName, this.skipThisReferral, this.handleReferrals);
/*     */ 
/* 154 */     localLdapReferralContext.setHopCount(this.hopCount + 1);
/*     */ 
/* 156 */     if (this.skipThisReferral) {
/* 157 */       this.skipThisReferral = false;
/*     */     }
/* 159 */     return localLdapReferralContext;
/*     */   }
/*     */ 
/*     */   public Object getReferralInfo()
/*     */   {
/* 166 */     if (this.debug) {
/* 167 */       System.out.println("LdapReferralException.getReferralInfo");
/* 168 */       System.out.println("  referralIndex=" + this.referralIndex);
/*     */     }
/*     */ 
/* 171 */     if (hasMoreReferrals()) {
/* 172 */       return this.referrals.elementAt(this.referralIndex);
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public void retryReferral()
/*     */   {
/* 182 */     if (this.debug) {
/* 183 */       System.out.println("LdapReferralException.retryReferral");
/*     */     }
/* 185 */     if (this.referralIndex > 0)
/* 186 */       this.referralIndex -= 1;
/*     */   }
/*     */ 
/*     */   public boolean skipReferral()
/*     */   {
/* 194 */     if (this.debug) {
/* 195 */       System.out.println("LdapReferralException.skipReferral");
/*     */     }
/* 197 */     this.skipThisReferral = true;
/*     */     try
/*     */     {
/* 201 */       getNextReferral();
/*     */     }
/*     */     catch (ReferralException localReferralException)
/*     */     {
/*     */     }
/* 206 */     return (hasMoreReferrals()) || (hasMoreReferralExceptions());
/*     */   }
/*     */ 
/*     */   void setReferralInfo(Vector paramVector, boolean paramBoolean)
/*     */   {
/* 216 */     if (this.debug) {
/* 217 */       System.out.println("LdapReferralException.setReferralInfo");
/*     */     }
/* 219 */     this.referrals = paramVector;
/* 220 */     if (paramVector != null) {
/* 221 */       this.referralCount = paramVector.size();
/*     */     }
/*     */ 
/* 224 */     if (this.debug)
/* 225 */       for (int i = 0; i < this.referralCount; i++)
/* 226 */         System.out.println("  [" + i + "] " + paramVector.elementAt(i));
/*     */   }
/*     */ 
/*     */   String getNextReferral()
/*     */     throws ReferralException
/*     */   {
/* 237 */     if (this.debug) {
/* 238 */       System.out.println("LdapReferralException.getNextReferral");
/*     */     }
/* 240 */     if (hasMoreReferrals())
/* 241 */       return (String)this.referrals.elementAt(this.referralIndex++);
/* 242 */     if (hasMoreReferralExceptions()) {
/* 243 */       throw this.nextReferralEx;
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   LdapReferralException appendUnprocessedReferrals(LdapReferralException paramLdapReferralException)
/*     */   {
/* 257 */     if (this.debug) {
/* 258 */       System.out.println("LdapReferralException.appendUnprocessedReferrals");
/*     */ 
/* 260 */       dump();
/* 261 */       if (paramLdapReferralException != null) {
/* 262 */         paramLdapReferralException.dump();
/*     */       }
/*     */     }
/*     */ 
/* 266 */     LdapReferralException localLdapReferralException1 = this;
/*     */ 
/* 268 */     if (!localLdapReferralException1.hasMoreReferrals()) {
/* 269 */       localLdapReferralException1 = this.nextReferralEx;
/*     */ 
/* 271 */       if ((this.errorEx != null) && (localLdapReferralException1 != null)) {
/* 272 */         localLdapReferralException1.setNamingException(this.errorEx);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 277 */     if (this == paramLdapReferralException) {
/* 278 */       return localLdapReferralException1;
/*     */     }
/*     */ 
/* 281 */     if ((paramLdapReferralException != null) && (!paramLdapReferralException.hasMoreReferrals())) {
/* 282 */       paramLdapReferralException = paramLdapReferralException.nextReferralEx;
/*     */     }
/*     */ 
/* 285 */     if (paramLdapReferralException == null) {
/* 286 */       return localLdapReferralException1;
/*     */     }
/*     */ 
/* 290 */     LdapReferralException localLdapReferralException2 = localLdapReferralException1;
/* 291 */     while (localLdapReferralException2.nextReferralEx != null) {
/* 292 */       localLdapReferralException2 = localLdapReferralException2.nextReferralEx;
/*     */     }
/* 294 */     localLdapReferralException2.nextReferralEx = paramLdapReferralException;
/*     */ 
/* 296 */     return localLdapReferralException1;
/*     */   }
/*     */ 
/*     */   boolean hasMoreReferrals()
/*     */   {
/* 305 */     if (this.debug) {
/* 306 */       System.out.println("LdapReferralException.hasMoreReferrals");
/*     */     }
/* 308 */     return (!this.foundEntry) && (this.referralIndex < this.referralCount);
/*     */   }
/*     */ 
/*     */   boolean hasMoreReferralExceptions()
/*     */   {
/* 315 */     if (this.debug) {
/* 316 */       System.out.println("LdapReferralException.hasMoreReferralExceptions");
/*     */     }
/*     */ 
/* 319 */     return this.nextReferralEx != null;
/*     */   }
/*     */ 
/*     */   void setHopCount(int paramInt)
/*     */   {
/* 327 */     if (this.debug) {
/* 328 */       System.out.println("LdapReferralException.setHopCount");
/*     */     }
/* 330 */     this.hopCount = paramInt;
/*     */   }
/*     */ 
/*     */   void setNameResolved(boolean paramBoolean)
/*     */   {
/* 337 */     if (this.debug) {
/* 338 */       System.out.println("LdapReferralException.setNameResolved");
/*     */     }
/* 340 */     this.foundEntry = paramBoolean;
/*     */   }
/*     */ 
/*     */   void setNamingException(NamingException paramNamingException)
/*     */   {
/* 348 */     if (this.debug) {
/* 349 */       System.out.println("LdapReferralException.setNamingException");
/*     */     }
/* 351 */     if (this.errorEx == null) {
/* 352 */       paramNamingException.setRootCause(this);
/* 353 */       this.errorEx = paramNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   String getNewRdn()
/*     */   {
/* 361 */     if (this.debug) {
/* 362 */       System.out.println("LdapReferralException.getNewRdn");
/*     */     }
/* 364 */     return this.newRdn;
/*     */   }
/*     */ 
/*     */   void setNewRdn(String paramString)
/*     */   {
/* 372 */     if (this.debug) {
/* 373 */       System.out.println("LdapReferralException.setNewRdn");
/*     */     }
/* 375 */     this.newRdn = paramString;
/*     */   }
/*     */ 
/*     */   NamingException getNamingException()
/*     */   {
/* 382 */     if (this.debug) {
/* 383 */       System.out.println("LdapReferralException.getNamingException");
/*     */     }
/* 385 */     return this.errorEx;
/*     */   }
/*     */ 
/*     */   void dump()
/*     */   {
/* 394 */     System.out.println();
/* 395 */     System.out.println("LdapReferralException.dump");
/* 396 */     LdapReferralException localLdapReferralException = this;
/* 397 */     while (localLdapReferralException != null) {
/* 398 */       localLdapReferralException.dumpState();
/* 399 */       localLdapReferralException = localLdapReferralException.nextReferralEx;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void dumpState()
/*     */   {
/* 407 */     System.out.println("LdapReferralException.dumpState");
/* 408 */     System.out.println("  hashCode=" + hashCode());
/* 409 */     System.out.println("  foundEntry=" + this.foundEntry);
/* 410 */     System.out.println("  skipThisReferral=" + this.skipThisReferral);
/* 411 */     System.out.println("  referralIndex=" + this.referralIndex);
/*     */ 
/* 413 */     if (this.referrals != null) {
/* 414 */       System.out.println("  referrals:");
/* 415 */       for (int i = 0; i < this.referralCount; i++)
/* 416 */         System.out.println("    [" + i + "] " + this.referrals.elementAt(i));
/*     */     }
/*     */     else {
/* 419 */       System.out.println("  referrals=null");
/*     */     }
/*     */ 
/* 422 */     System.out.println("  errorEx=" + this.errorEx);
/*     */ 
/* 424 */     if (this.nextReferralEx == null)
/* 425 */       System.out.println("  nextRefEx=null");
/*     */     else {
/* 427 */       System.out.println("  nextRefEx=" + this.nextReferralEx.hashCode());
/*     */     }
/* 429 */     System.out.println();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapReferralException
 * JD-Core Version:    0.6.2
 */