/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.ctx.Continuation;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.LimitExceededException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.PartialResultException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.ldap.LdapName;
/*     */ 
/*     */ class LdapNamingEnumeration
/*     */   implements NamingEnumeration, ReferralEnumeration
/*     */ {
/*     */   protected Name listArg;
/*  44 */   private boolean cleaned = false;
/*     */   private LdapResult res;
/*     */   private LdapClient enumClnt;
/*     */   private Continuation cont;
/*  48 */   private Vector entries = null;
/*  49 */   private int limit = 0;
/*  50 */   private int posn = 0;
/*     */   protected LdapCtx homeCtx;
/*  52 */   private LdapReferralException refEx = null;
/*  53 */   private NamingException errEx = null;
/*     */ 
/*  55 */   private static final String defaultClassName = DirContext.class.getName();
/*     */ 
/* 172 */   private boolean more = true;
/* 173 */   private boolean hasMoreCalled = false;
/*     */ 
/*     */   LdapNamingEnumeration(LdapCtx paramLdapCtx, LdapResult paramLdapResult, Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*  71 */     if ((paramLdapResult.status != 0) && (paramLdapResult.status != 4) && (paramLdapResult.status != 3) && (paramLdapResult.status != 11) && (paramLdapResult.status != 10) && (paramLdapResult.status != 9))
/*     */     {
/*  79 */       NamingException localNamingException = new NamingException(LdapClient.getErrorMessage(paramLdapResult.status, paramLdapResult.errorMessage));
/*     */ 
/*  83 */       throw paramContinuation.fillInException(localNamingException);
/*     */     }
/*     */ 
/*  88 */     this.res = paramLdapResult;
/*  89 */     this.entries = paramLdapResult.entries;
/*  90 */     this.limit = (this.entries == null ? 0 : this.entries.size());
/*  91 */     this.listArg = paramName;
/*  92 */     this.cont = paramContinuation;
/*     */ 
/*  94 */     if (paramLdapResult.refEx != null) {
/*  95 */       this.refEx = paramLdapResult.refEx;
/*     */     }
/*     */ 
/*  99 */     this.homeCtx = paramLdapCtx;
/* 100 */     paramLdapCtx.incEnumCount();
/* 101 */     this.enumClnt = paramLdapCtx.clnt;
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/*     */     try {
/* 106 */       return next();
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 109 */       cleanup();
/* 110 */     }return null;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*     */     try {
/* 116 */       return hasMore();
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 119 */       cleanup();
/* 120 */     }return false;
/*     */   }
/*     */ 
/*     */   private void getNextBatch()
/*     */     throws NamingException
/*     */   {
/* 129 */     this.res = this.homeCtx.getSearchReply(this.enumClnt, this.res);
/* 130 */     if (this.res == null) {
/* 131 */       this.limit = (this.posn = 0);
/* 132 */       return;
/*     */     }
/*     */ 
/* 135 */     this.entries = this.res.entries;
/* 136 */     this.limit = (this.entries == null ? 0 : this.entries.size());
/* 137 */     this.posn = 0;
/*     */ 
/* 141 */     if ((this.res.status != 0) || ((this.res.status == 0) && (this.res.referrals != null)))
/*     */     {
/*     */       try
/*     */       {
/* 147 */         this.homeCtx.processReturnCode(this.res, this.listArg);
/*     */       }
/*     */       catch (LimitExceededException localLimitExceededException) {
/* 150 */         setNamingException(localLimitExceededException);
/*     */       }
/*     */       catch (PartialResultException localPartialResultException) {
/* 153 */         setNamingException(localPartialResultException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 158 */     if (this.res.refEx != null) {
/* 159 */       if (this.refEx == null)
/* 160 */         this.refEx = this.res.refEx;
/*     */       else {
/* 162 */         this.refEx = this.refEx.appendUnprocessedReferrals(this.res.refEx);
/*     */       }
/* 164 */       this.res.refEx = null;
/*     */     }
/*     */ 
/* 167 */     if (this.res.resControls != null)
/* 168 */       this.homeCtx.respCtls = this.res.resControls;
/*     */   }
/*     */ 
/*     */   public boolean hasMore()
/*     */     throws NamingException
/*     */   {
/* 180 */     if (this.hasMoreCalled) {
/* 181 */       return this.more;
/*     */     }
/*     */ 
/* 184 */     this.hasMoreCalled = true;
/*     */ 
/* 186 */     if (!this.more) {
/* 187 */       return false;
/*     */     }
/* 189 */     return this.more = hasMoreImpl();
/*     */   }
/*     */ 
/*     */   public Object next()
/*     */     throws NamingException
/*     */   {
/* 198 */     if (!this.hasMoreCalled) {
/* 199 */       hasMore();
/*     */     }
/* 201 */     this.hasMoreCalled = false;
/* 202 */     return nextImpl();
/*     */   }
/*     */ 
/*     */   private boolean hasMoreImpl()
/*     */     throws NamingException
/*     */   {
/* 215 */     if (this.posn == this.limit) {
/* 216 */       getNextBatch();
/*     */     }
/*     */ 
/* 220 */     if (this.posn < this.limit) {
/* 221 */       return true;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 226 */       return hasMoreReferrals();
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException) {
/* 229 */       cleanup();
/* 230 */       throw localLdapReferralException;
/*     */     }
/*     */     catch (LimitExceededException localLimitExceededException) {
/* 233 */       cleanup();
/* 234 */       throw localLimitExceededException;
/*     */     }
/*     */     catch (PartialResultException localPartialResultException1) {
/* 237 */       cleanup();
/* 238 */       throw localPartialResultException1;
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 241 */       cleanup();
/* 242 */       PartialResultException localPartialResultException2 = new PartialResultException();
/* 243 */       localPartialResultException2.setRootCause(localNamingException);
/* 244 */       throw localPartialResultException2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object nextImpl()
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 254 */       return nextAux();
/*     */     } catch (NamingException localNamingException) {
/* 256 */       cleanup();
/* 257 */       throw this.cont.fillInException(localNamingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object nextAux() throws NamingException {
/* 262 */     if (this.posn == this.limit) {
/* 263 */       getNextBatch();
/*     */     }
/*     */ 
/* 266 */     if (this.posn >= this.limit) {
/* 267 */       cleanup();
/* 268 */       throw new NoSuchElementException("invalid enumeration handle");
/*     */     }
/*     */ 
/* 271 */     LdapEntry localLdapEntry = (LdapEntry)this.entries.elementAt(this.posn++);
/*     */ 
/* 274 */     return createItem(localLdapEntry.DN, localLdapEntry.attributes, localLdapEntry.respCtls);
/*     */   }
/*     */ 
/*     */   protected String getAtom(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 282 */       LdapName localLdapName = new LdapName(paramString);
/* 283 */       return localLdapName.get(localLdapName.size() - 1); } catch (NamingException localNamingException) {
/*     */     }
/* 285 */     return paramString;
/*     */   }
/*     */ 
/*     */   protected NameClassPair createItem(String paramString, Attributes paramAttributes, Vector paramVector)
/*     */     throws NamingException
/*     */   {
/* 293 */     String str = null;
/*     */     Attribute localAttribute;
/* 296 */     if ((localAttribute = paramAttributes.get(Obj.JAVA_ATTRIBUTES[2])) != null)
/* 297 */       str = (String)localAttribute.get();
/*     */     else {
/* 299 */       str = defaultClassName;
/*     */     }
/* 301 */     CompositeName localCompositeName = new CompositeName();
/* 302 */     localCompositeName.add(getAtom(paramString));
/*     */     Object localObject;
/* 305 */     if (paramVector != null) {
/* 306 */       localObject = new NameClassPairWithControls(localCompositeName.toString(), str, this.homeCtx.convertControls(paramVector));
/*     */     }
/*     */     else
/*     */     {
/* 310 */       localObject = new NameClassPair(localCompositeName.toString(), str);
/*     */     }
/* 312 */     ((NameClassPair)localObject).setNameInNamespace(paramString);
/* 313 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void appendUnprocessedReferrals(LdapReferralException paramLdapReferralException)
/*     */   {
/* 322 */     if (this.refEx != null)
/* 323 */       this.refEx = this.refEx.appendUnprocessedReferrals(paramLdapReferralException);
/*     */     else
/* 325 */       this.refEx = paramLdapReferralException.appendUnprocessedReferrals(this.refEx);
/*     */   }
/*     */ 
/*     */   void setNamingException(NamingException paramNamingException)
/*     */   {
/* 330 */     this.errEx = paramNamingException;
/*     */   }
/*     */ 
/*     */   protected LdapNamingEnumeration getReferredResults(LdapReferralContext paramLdapReferralContext)
/*     */     throws NamingException
/*     */   {
/* 336 */     return (LdapNamingEnumeration)paramLdapReferralContext.list(this.listArg);
/*     */   }
/*     */ 
/*     */   protected boolean hasMoreReferrals()
/*     */     throws NamingException
/*     */   {
/* 346 */     if ((this.refEx != null) && ((this.refEx.hasMoreReferrals()) || (this.refEx.hasMoreReferralExceptions())))
/*     */     {
/* 350 */       if (this.homeCtx.handleReferrals == 2) {
/* 351 */         throw ((NamingException)this.refEx.fillInStackTrace());
/*     */       }
/*     */ 
/*     */       while (true)
/*     */       {
/* 357 */         LdapReferralContext localLdapReferralContext = (LdapReferralContext)this.refEx.getReferralContext(this.homeCtx.envprops, this.homeCtx.reqCtls);
/*     */         try
/*     */         {
/* 363 */           update(getReferredResults(localLdapReferralContext));
/*     */         }
/*     */         catch (LdapReferralException localLdapReferralException)
/*     */         {
/* 369 */           if (this.errEx == null) {
/* 370 */             this.errEx = localLdapReferralException.getNamingException();
/*     */           }
/* 372 */           this.refEx = localLdapReferralException;
/*     */ 
/* 377 */           localLdapReferralContext.close(); } finally { localLdapReferralContext.close(); }
/*     */ 
/*     */       }
/* 380 */       return hasMoreImpl();
/*     */     }
/*     */ 
/* 383 */     cleanup();
/*     */ 
/* 385 */     if (this.errEx != null) {
/* 386 */       throw this.errEx;
/*     */     }
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */   protected void update(LdapNamingEnumeration paramLdapNamingEnumeration)
/*     */   {
/* 398 */     this.homeCtx.decEnumCount();
/*     */ 
/* 401 */     this.homeCtx = paramLdapNamingEnumeration.homeCtx;
/* 402 */     this.enumClnt = paramLdapNamingEnumeration.enumClnt;
/*     */ 
/* 407 */     paramLdapNamingEnumeration.homeCtx = null;
/*     */ 
/* 410 */     this.posn = paramLdapNamingEnumeration.posn;
/* 411 */     this.limit = paramLdapNamingEnumeration.limit;
/* 412 */     this.res = paramLdapNamingEnumeration.res;
/* 413 */     this.entries = paramLdapNamingEnumeration.entries;
/* 414 */     this.refEx = paramLdapNamingEnumeration.refEx;
/* 415 */     this.listArg = paramLdapNamingEnumeration.listArg;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 419 */     cleanup();
/*     */   }
/*     */ 
/*     */   protected void cleanup() {
/* 423 */     if (this.cleaned) return;
/*     */ 
/* 425 */     if (this.enumClnt != null) {
/* 426 */       this.enumClnt.clearSearchReply(this.res, this.homeCtx.reqCtls);
/*     */     }
/*     */ 
/* 429 */     this.enumClnt = null;
/* 430 */     this.cleaned = true;
/* 431 */     if (this.homeCtx != null) {
/* 432 */       this.homeCtx.decEnumCount();
/* 433 */       this.homeCtx = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() {
/* 438 */     cleanup();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapNamingEnumeration
 * JD-Core Version:    0.6.2
 */