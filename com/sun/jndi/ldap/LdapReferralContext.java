/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.dir.SearchFilter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.naming.ldap.ExtendedRequest;
/*     */ import javax.naming.ldap.ExtendedResponse;
/*     */ import javax.naming.ldap.LdapContext;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ final class LdapReferralContext
/*     */   implements DirContext, LdapContext
/*     */ {
/*  44 */   private DirContext refCtx = null;
/*  45 */   private Name urlName = null;
/*  46 */   private String urlAttrs = null;
/*  47 */   private String urlScope = null;
/*  48 */   private String urlFilter = null;
/*     */ 
/*  50 */   private LdapReferralException refEx = null;
/*  51 */   private boolean skipThisReferral = false;
/*  52 */   private int hopCount = 1;
/*  53 */   private NamingException previousEx = null;
/*     */ 
/*     */   LdapReferralContext(LdapReferralException paramLdapReferralException, Hashtable paramHashtable, Control[] paramArrayOfControl1, Control[] paramArrayOfControl2, String paramString, boolean paramBoolean, int paramInt)
/*     */     throws NamingException
/*     */   {
/*  62 */     this.refEx = paramLdapReferralException;
/*     */ 
/*  64 */     if ((this.skipThisReferral = paramBoolean)) {
/*  65 */       return;
/*     */     }
/*     */ 
/*  71 */     if (paramHashtable != null) {
/*  72 */       paramHashtable = (Hashtable)paramHashtable.clone();
/*     */ 
/*  75 */       if (paramArrayOfControl1 == null)
/*  76 */         paramHashtable.remove("java.naming.ldap.control.connect");
/*     */     }
/*  78 */     else if (paramArrayOfControl1 != null) {
/*  79 */       paramHashtable = new Hashtable(5);
/*     */     }
/*  81 */     if (paramArrayOfControl1 != null) {
/*  82 */       Control[] arrayOfControl = new Control[paramArrayOfControl1.length];
/*  83 */       System.arraycopy(paramArrayOfControl1, 0, arrayOfControl, 0, paramArrayOfControl1.length);
/*     */ 
/*  85 */       paramHashtable.put("java.naming.ldap.control.connect", arrayOfControl);
/*     */     }String str;
/*     */     Object localObject;
/*     */     while (true) {
/*     */       try { str = this.refEx.getNextReferral();
/*  91 */         if (str == null) {
/*  92 */           throw ((NamingException)this.previousEx.fillInStackTrace());
/*     */         }
/*     */       }
/*     */       catch (LdapReferralException localLdapReferralException)
/*     */       {
/*  97 */         if (paramInt == 2) {
/*  98 */           throw localLdapReferralException;
/*     */         }
/* 100 */         this.refEx = localLdapReferralException;
/* 101 */       }continue;
/*     */ 
/* 106 */       Reference localReference = new Reference("javax.naming.directory.DirContext", new StringRefAddr("URL", str));
/*     */       try
/*     */       {
/* 111 */         localObject = NamingManager.getObjectInstance(localReference, null, null, paramHashtable);
/*     */       }
/*     */       catch (NamingException localNamingException1)
/*     */       {
/* 115 */         if (paramInt == 2) {
/* 116 */           throw localNamingException1;
/*     */         }
/*     */ 
/* 120 */         this.previousEx = localNamingException1;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 126 */         NamingException localNamingException2 = new NamingException("problem generating object using object factory");
/*     */ 
/* 129 */         localNamingException2.setRootCause(localException);
/* 130 */         throw localNamingException2;
/*     */       }
/*     */     }
/* 132 */     if ((localObject instanceof LdapContext)) {
/* 133 */       this.refCtx = ((LdapContext)localObject);
/* 134 */       if (((this.refCtx instanceof LdapContext)) && (paramArrayOfControl2 != null)) {
/* 135 */         ((LdapContext)this.refCtx).setRequestControls(paramArrayOfControl2);
/*     */       }
/* 137 */       initDefaults(str, paramString);
/*     */     }
/*     */     else
/*     */     {
/* 141 */       NotContextException localNotContextException = new NotContextException("Cannot create context for: " + str);
/*     */ 
/* 143 */       localNotContextException.setRemainingName(new CompositeName().add(paramString));
/* 144 */       throw localNotContextException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initDefaults(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/*     */     String str;
/*     */     try
/*     */     {
/* 154 */       LdapURL localLdapURL = new LdapURL(paramString1);
/* 155 */       str = localLdapURL.getDN();
/* 156 */       this.urlAttrs = localLdapURL.getAttributes();
/* 157 */       this.urlScope = localLdapURL.getScope();
/* 158 */       this.urlFilter = localLdapURL.getFilter();
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 162 */       str = paramString1;
/* 163 */       this.urlAttrs = (this.urlScope = this.urlFilter = null);
/*     */     }
/*     */ 
/* 167 */     if (str == null) {
/* 168 */       str = paramString2;
/*     */     }
/*     */     else {
/* 171 */       str = "";
/*     */     }
/*     */ 
/* 174 */     if (str == null)
/* 175 */       this.urlName = null;
/*     */     else
/* 177 */       this.urlName = (str.equals("") ? new CompositeName() : new CompositeName().add(str));
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws NamingException
/*     */   {
/* 184 */     if (this.refCtx != null) {
/* 185 */       this.refCtx.close();
/* 186 */       this.refCtx = null;
/*     */     }
/* 188 */     this.refEx = null;
/*     */   }
/*     */ 
/*     */   void setHopCount(int paramInt) {
/* 192 */     this.hopCount = paramInt;
/* 193 */     if ((this.refCtx != null) && ((this.refCtx instanceof LdapCtx)))
/* 194 */       ((LdapCtx)this.refCtx).setHopCount(paramInt);
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString) throws NamingException
/*     */   {
/* 199 */     return lookup(toName(paramString));
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/* 203 */     if (this.skipThisReferral) {
/* 204 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 208 */     return this.refCtx.lookup(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException {
/* 212 */     bind(toName(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/* 216 */     if (this.skipThisReferral) {
/* 217 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 221 */     this.refCtx.bind(overrideName(paramName), paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/* 225 */     rebind(toName(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 229 */     if (this.skipThisReferral) {
/* 230 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 234 */     this.refCtx.rebind(overrideName(paramName), paramObject);
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException {
/* 238 */     unbind(toName(paramString));
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/* 242 */     if (this.skipThisReferral) {
/* 243 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 247 */     this.refCtx.unbind(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 251 */     rename(toName(paramString1), toName(paramString2));
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2) throws NamingException {
/* 255 */     if (this.skipThisReferral) {
/* 256 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 260 */     this.refCtx.rename(overrideName(paramName1), toName(this.refEx.getNewRdn()));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(String paramString) throws NamingException {
/* 264 */     return list(toName(paramString));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(Name paramName) throws NamingException {
/* 268 */     if (this.skipThisReferral) {
/* 269 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */     try
/*     */     {
/* 273 */       NamingEnumeration localNamingEnumeration = null;
/*     */ 
/* 275 */       if ((this.urlScope != null) && (this.urlScope.equals("base"))) {
/* 276 */         SearchControls localSearchControls = new SearchControls();
/* 277 */         localSearchControls.setReturningObjFlag(true);
/* 278 */         localSearchControls.setSearchScope(0);
/*     */ 
/* 280 */         localNamingEnumeration = this.refCtx.search(overrideName(paramName), "(objectclass=*)", localSearchControls);
/*     */       }
/*     */       else {
/* 283 */         localNamingEnumeration = this.refCtx.list(overrideName(paramName));
/*     */       }
/*     */ 
/* 286 */       this.refEx.setNameResolved(true);
/*     */ 
/* 291 */       ((ReferralEnumeration)localNamingEnumeration).appendUnprocessedReferrals(this.refEx);
/*     */ 
/* 293 */       return localNamingEnumeration;
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException)
/*     */     {
/* 301 */       localLdapReferralException.appendUnprocessedReferrals(this.refEx);
/* 302 */       throw ((NamingException)localLdapReferralException.fillInStackTrace());
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 307 */       if ((this.refEx != null) && (!this.refEx.hasMoreReferrals())) {
/* 308 */         this.refEx.setNamingException(localNamingException);
/*     */       }
/* 310 */       if ((this.refEx != null) && ((this.refEx.hasMoreReferrals()) || (this.refEx.hasMoreReferralExceptions())))
/*     */       {
/* 313 */         throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */       }
/*     */ 
/* 316 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(String paramString) throws NamingException
/*     */   {
/* 322 */     return listBindings(toName(paramString));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(Name paramName) throws NamingException {
/* 326 */     if (this.skipThisReferral) {
/* 327 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 332 */       NamingEnumeration localNamingEnumeration = null;
/*     */ 
/* 334 */       if ((this.urlScope != null) && (this.urlScope.equals("base"))) {
/* 335 */         SearchControls localSearchControls = new SearchControls();
/* 336 */         localSearchControls.setReturningObjFlag(true);
/* 337 */         localSearchControls.setSearchScope(0);
/*     */ 
/* 339 */         localNamingEnumeration = this.refCtx.search(overrideName(paramName), "(objectclass=*)", localSearchControls);
/*     */       }
/*     */       else {
/* 342 */         localNamingEnumeration = this.refCtx.listBindings(overrideName(paramName));
/*     */       }
/*     */ 
/* 345 */       this.refEx.setNameResolved(true);
/*     */ 
/* 350 */       ((ReferralEnumeration)localNamingEnumeration).appendUnprocessedReferrals(this.refEx);
/*     */ 
/* 352 */       return localNamingEnumeration;
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException)
/*     */     {
/* 360 */       localLdapReferralException.appendUnprocessedReferrals(this.refEx);
/* 361 */       throw ((NamingException)localLdapReferralException.fillInStackTrace());
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 366 */       if ((this.refEx != null) && (!this.refEx.hasMoreReferrals())) {
/* 367 */         this.refEx.setNamingException(localNamingException);
/*     */       }
/* 369 */       if ((this.refEx != null) && ((this.refEx.hasMoreReferrals()) || (this.refEx.hasMoreReferralExceptions())))
/*     */       {
/* 372 */         throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */       }
/*     */ 
/* 375 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException
/*     */   {
/* 381 */     destroySubcontext(toName(paramString));
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 385 */     if (this.skipThisReferral) {
/* 386 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 390 */     this.refCtx.destroySubcontext(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 394 */     return createSubcontext(toName(paramString));
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 398 */     if (this.skipThisReferral) {
/* 399 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 403 */     return this.refCtx.createSubcontext(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException {
/* 407 */     return lookupLink(toName(paramString));
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException {
/* 411 */     if (this.skipThisReferral) {
/* 412 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 416 */     return this.refCtx.lookupLink(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException {
/* 420 */     return getNameParser(toName(paramString));
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 424 */     if (this.skipThisReferral) {
/* 425 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 429 */     return this.refCtx.getNameParser(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2) throws NamingException
/*     */   {
/* 434 */     return composeName(toName(paramString1), toName(paramString2)).toString();
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2) throws NamingException {
/* 438 */     if (this.skipThisReferral) {
/* 439 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 442 */     return this.refCtx.composeName(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 447 */     if (this.skipThisReferral) {
/* 448 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 452 */     return this.refCtx.addToEnvironment(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString) throws NamingException
/*     */   {
/* 457 */     if (this.skipThisReferral) {
/* 458 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 462 */     return this.refCtx.removeFromEnvironment(paramString);
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 466 */     if (this.skipThisReferral) {
/* 467 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 471 */     return this.refCtx.getEnvironment();
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString) throws NamingException {
/* 475 */     return getAttributes(toName(paramString));
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException {
/* 479 */     if (this.skipThisReferral) {
/* 480 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 484 */     return this.refCtx.getAttributes(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 489 */     return getAttributes(toName(paramString), paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 494 */     if (this.skipThisReferral) {
/* 495 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 499 */     return this.refCtx.getAttributes(overrideName(paramName), paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 504 */     modifyAttributes(toName(paramString), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 509 */     if (this.skipThisReferral) {
/* 510 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 514 */     this.refCtx.modifyAttributes(overrideName(paramName), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 519 */     modifyAttributes(toName(paramString), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 524 */     if (this.skipThisReferral) {
/* 525 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 529 */     this.refCtx.modifyAttributes(overrideName(paramName), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 534 */     bind(toName(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 539 */     if (this.skipThisReferral) {
/* 540 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 544 */     this.refCtx.bind(overrideName(paramName), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 549 */     rebind(toName(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 554 */     if (this.skipThisReferral) {
/* 555 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 559 */     this.refCtx.rebind(overrideName(paramName), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 564 */     return createSubcontext(toName(paramString), paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 569 */     if (this.skipThisReferral) {
/* 570 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 574 */     return this.refCtx.createSubcontext(overrideName(paramName), paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException {
/* 578 */     return getSchema(toName(paramString));
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException {
/* 582 */     if (this.skipThisReferral) {
/* 583 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 587 */     return this.refCtx.getSchema(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString) throws NamingException
/*     */   {
/* 592 */     return getSchemaClassDefinition(toName(paramString));
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException
/*     */   {
/* 597 */     if (this.skipThisReferral) {
/* 598 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 602 */     return this.refCtx.getSchemaClassDefinition(overrideName(paramName));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 608 */     return search(toName(paramString), SearchFilter.format(paramAttributes), new SearchControls());
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 615 */     return search(paramName, SearchFilter.format(paramAttributes), new SearchControls());
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 623 */     SearchControls localSearchControls = new SearchControls();
/* 624 */     localSearchControls.setReturningAttributes(paramArrayOfString);
/*     */ 
/* 626 */     return search(toName(paramString), SearchFilter.format(paramAttributes), localSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 634 */     SearchControls localSearchControls = new SearchControls();
/* 635 */     localSearchControls.setReturningAttributes(paramArrayOfString);
/*     */ 
/* 637 */     return search(paramName, SearchFilter.format(paramAttributes), localSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 644 */     return search(toName(paramString1), paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 651 */     if (this.skipThisReferral) {
/* 652 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 657 */       NamingEnumeration localNamingEnumeration = this.refCtx.search(overrideName(paramName), overrideFilter(paramString), overrideAttributesAndScope(paramSearchControls));
/*     */ 
/* 660 */       this.refEx.setNameResolved(true);
/*     */ 
/* 665 */       ((ReferralEnumeration)localNamingEnumeration).appendUnprocessedReferrals(this.refEx);
/*     */ 
/* 667 */       return localNamingEnumeration;
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException)
/*     */     {
/* 677 */       localLdapReferralException.appendUnprocessedReferrals(this.refEx);
/* 678 */       throw ((NamingException)localLdapReferralException.fillInStackTrace());
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 683 */       if ((this.refEx != null) && (!this.refEx.hasMoreReferrals())) {
/* 684 */         this.refEx.setNamingException(localNamingException);
/*     */       }
/* 686 */       if ((this.refEx != null) && ((this.refEx.hasMoreReferrals()) || (this.refEx.hasMoreReferralExceptions())))
/*     */       {
/* 689 */         throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */       }
/*     */ 
/* 692 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 702 */     return search(toName(paramString1), paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 710 */     if (this.skipThisReferral)
/* 711 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     try
/*     */     {
/*     */       NamingEnumeration localNamingEnumeration;
/* 718 */       if (this.urlFilter != null) {
/* 719 */         localNamingEnumeration = this.refCtx.search(overrideName(paramName), this.urlFilter, overrideAttributesAndScope(paramSearchControls));
/*     */       }
/*     */       else {
/* 722 */         localNamingEnumeration = this.refCtx.search(overrideName(paramName), paramString, paramArrayOfObject, overrideAttributesAndScope(paramSearchControls));
/*     */       }
/*     */ 
/* 726 */       this.refEx.setNameResolved(true);
/*     */ 
/* 731 */       ((ReferralEnumeration)localNamingEnumeration).appendUnprocessedReferrals(this.refEx);
/*     */ 
/* 733 */       return localNamingEnumeration;
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException)
/*     */     {
/* 741 */       localLdapReferralException.appendUnprocessedReferrals(this.refEx);
/* 742 */       throw ((NamingException)localLdapReferralException.fillInStackTrace());
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 747 */       if ((this.refEx != null) && (!this.refEx.hasMoreReferrals())) {
/* 748 */         this.refEx.setNamingException(localNamingException);
/*     */       }
/* 750 */       if ((this.refEx != null) && ((this.refEx.hasMoreReferrals()) || (this.refEx.hasMoreReferralExceptions())))
/*     */       {
/* 753 */         throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */       }
/*     */ 
/* 756 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace() throws NamingException
/*     */   {
/* 762 */     if (this.skipThisReferral) {
/* 763 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 766 */     return (this.urlName != null) && (!this.urlName.isEmpty()) ? this.urlName.get(0) : "";
/*     */   }
/*     */ 
/*     */   public ExtendedResponse extendedOperation(ExtendedRequest paramExtendedRequest)
/*     */     throws NamingException
/*     */   {
/* 774 */     if (this.skipThisReferral) {
/* 775 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 779 */     if (!(this.refCtx instanceof LdapContext)) {
/* 780 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 784 */     return ((LdapContext)this.refCtx).extendedOperation(paramExtendedRequest);
/*     */   }
/*     */ 
/*     */   public LdapContext newInstance(Control[] paramArrayOfControl)
/*     */     throws NamingException
/*     */   {
/* 790 */     if (this.skipThisReferral) {
/* 791 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 795 */     if (!(this.refCtx instanceof LdapContext)) {
/* 796 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 800 */     return ((LdapContext)this.refCtx).newInstance(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public void reconnect(Control[] paramArrayOfControl) throws NamingException {
/* 804 */     if (this.skipThisReferral) {
/* 805 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 809 */     if (!(this.refCtx instanceof LdapContext)) {
/* 810 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 814 */     ((LdapContext)this.refCtx).reconnect(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public Control[] getConnectControls() throws NamingException {
/* 818 */     if (this.skipThisReferral) {
/* 819 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 823 */     if (!(this.refCtx instanceof LdapContext)) {
/* 824 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 828 */     return ((LdapContext)this.refCtx).getConnectControls();
/*     */   }
/*     */ 
/*     */   public void setRequestControls(Control[] paramArrayOfControl)
/*     */     throws NamingException
/*     */   {
/* 834 */     if (this.skipThisReferral) {
/* 835 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 839 */     if (!(this.refCtx instanceof LdapContext)) {
/* 840 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 844 */     ((LdapContext)this.refCtx).setRequestControls(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public Control[] getRequestControls() throws NamingException {
/* 848 */     if (this.skipThisReferral) {
/* 849 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 853 */     if (!(this.refCtx instanceof LdapContext)) {
/* 854 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 857 */     return ((LdapContext)this.refCtx).getRequestControls();
/*     */   }
/*     */ 
/*     */   public Control[] getResponseControls() throws NamingException {
/* 861 */     if (this.skipThisReferral) {
/* 862 */       throw ((NamingException)this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
/*     */     }
/*     */ 
/* 866 */     if (!(this.refCtx instanceof LdapContext)) {
/* 867 */       throw new NotContextException("Referral context not an instance of LdapContext");
/*     */     }
/*     */ 
/* 870 */     return ((LdapContext)this.refCtx).getResponseControls();
/*     */   }
/*     */ 
/*     */   private Name toName(String paramString) throws InvalidNameException
/*     */   {
/* 875 */     return paramString.equals("") ? new CompositeName() : new CompositeName().add(paramString);
/*     */   }
/*     */ 
/*     */   private Name overrideName(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/* 884 */     return this.urlName == null ? paramName : this.urlName;
/*     */   }
/*     */ 
/*     */   private SearchControls overrideAttributesAndScope(SearchControls paramSearchControls)
/*     */   {
/* 894 */     if ((this.urlScope != null) || (this.urlAttrs != null)) {
/* 895 */       SearchControls localSearchControls = new SearchControls(paramSearchControls.getSearchScope(), paramSearchControls.getCountLimit(), paramSearchControls.getTimeLimit(), paramSearchControls.getReturningAttributes(), paramSearchControls.getReturningObjFlag(), paramSearchControls.getDerefLinkFlag());
/*     */ 
/* 902 */       if (this.urlScope != null) {
/* 903 */         if (this.urlScope.equals("base"))
/* 904 */           localSearchControls.setSearchScope(0);
/* 905 */         else if (this.urlScope.equals("one"))
/* 906 */           localSearchControls.setSearchScope(1);
/* 907 */         else if (this.urlScope.equals("sub")) {
/* 908 */           localSearchControls.setSearchScope(2);
/*     */         }
/*     */       }
/*     */ 
/* 912 */       if (this.urlAttrs != null) {
/* 913 */         StringTokenizer localStringTokenizer = new StringTokenizer(this.urlAttrs, ",");
/* 914 */         int i = localStringTokenizer.countTokens();
/* 915 */         String[] arrayOfString = new String[i];
/* 916 */         for (int j = 0; j < i; j++) {
/* 917 */           arrayOfString[j] = localStringTokenizer.nextToken();
/*     */         }
/* 919 */         localSearchControls.setReturningAttributes(arrayOfString);
/*     */       }
/*     */ 
/* 922 */       return localSearchControls;
/*     */     }
/*     */ 
/* 925 */     return paramSearchControls;
/*     */   }
/*     */ 
/*     */   private String overrideFilter(String paramString)
/*     */   {
/* 934 */     return this.urlFilter == null ? paramString : this.urlFilter;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapReferralContext
 * JD-Core Version:    0.6.2
 */