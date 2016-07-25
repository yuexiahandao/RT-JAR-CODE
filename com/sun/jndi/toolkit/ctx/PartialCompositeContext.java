/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.NamingManager;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ import javax.naming.spi.Resolver;
/*     */ 
/*     */ public abstract class PartialCompositeContext
/*     */   implements Context, Resolver
/*     */ {
/*     */   protected static final int _PARTIAL = 1;
/*     */   protected static final int _COMPONENT = 2;
/*     */   protected static final int _ATOMIC = 3;
/*  60 */   protected int _contextType = 1;
/*     */ 
/*  62 */   static final CompositeName _EMPTY_NAME = new CompositeName();
/*     */   static CompositeName _NNS_NAME;
/*     */ 
/*     */   protected abstract ResolveResult p_resolveToClass(Name paramName, Class paramClass, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Object p_lookup(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Object p_lookupLink(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration p_list(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration p_listBindings(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_bind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_rebind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_unbind(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_destroySubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Context p_createSubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_rename(Name paramName1, Name paramName2, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NameParser p_getNameParser(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected Hashtable p_getEnvironment()
/*     */     throws NamingException
/*     */   {
/* 119 */     return getEnvironment();
/*     */   }
/*     */ 
/*     */   public ResolveResult resolveToClass(String paramString, Class<? extends Context> paramClass)
/*     */     throws NamingException
/*     */   {
/* 132 */     return resolveToClass(new CompositeName(paramString), paramClass);
/*     */   }
/*     */   public ResolveResult resolveToClass(Name paramName, Class<? extends Context> paramClass) throws NamingException {
/* 139 */     PartialCompositeContext localPartialCompositeContext = this;
/* 140 */     Hashtable localHashtable = p_getEnvironment();
/* 141 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 143 */     Name localName = paramName;
/*     */     ResolveResult localResolveResult;
/*     */     try {
/* 146 */       localResolveResult = localPartialCompositeContext.p_resolveToClass(localName, paramClass, localContinuation);
/* 147 */       while (localContinuation.isContinue()) {
/* 148 */         localName = localContinuation.getRemainingName();
/* 149 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 150 */         localResolveResult = localPartialCompositeContext.p_resolveToClass(localName, paramClass, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 153 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 154 */       if (!(localContext instanceof Resolver)) {
/* 155 */         throw localCannotProceedException;
/*     */       }
/* 157 */       localResolveResult = ((Resolver)localContext).resolveToClass(localCannotProceedException.getRemainingName(), paramClass);
/*     */     }
/*     */ 
/* 160 */     return localResolveResult;
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString)
/*     */     throws NamingException
/*     */   {
/* 166 */     return lookup(new CompositeName(paramString)); } 
/* 170 */   public Object lookup(Name paramName) throws NamingException { PartialCompositeContext localPartialCompositeContext = this;
/* 171 */     Hashtable localHashtable = p_getEnvironment();
/* 172 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 174 */     Name localName = paramName;
/*     */     Object localObject;
/*     */     try { localObject = localPartialCompositeContext.p_lookup(localName, localContinuation);
/* 178 */       while (localContinuation.isContinue()) {
/* 179 */         localName = localContinuation.getRemainingName();
/* 180 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 181 */         localObject = localPartialCompositeContext.p_lookup(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 184 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 185 */       localObject = localContext.lookup(localCannotProceedException.getRemainingName());
/*     */     }
/* 187 */     return localObject; }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 191 */     bind(new CompositeName(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/* 195 */     PartialCompositeContext localPartialCompositeContext = this;
/* 196 */     Name localName = paramName;
/* 197 */     Hashtable localHashtable = p_getEnvironment();
/* 198 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     try
/*     */     {
/* 201 */       localPartialCompositeContext.p_bind(localName, paramObject, localContinuation);
/* 202 */       while (localContinuation.isContinue()) {
/* 203 */         localName = localContinuation.getRemainingName();
/* 204 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 205 */         localPartialCompositeContext.p_bind(localName, paramObject, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 208 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 209 */       localContext.bind(localCannotProceedException.getRemainingName(), paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/* 214 */     rebind(new CompositeName(paramString), paramObject);
/*     */   }
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 217 */     PartialCompositeContext localPartialCompositeContext = this;
/* 218 */     Name localName = paramName;
/* 219 */     Hashtable localHashtable = p_getEnvironment();
/* 220 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     try
/*     */     {
/* 223 */       localPartialCompositeContext.p_rebind(localName, paramObject, localContinuation);
/* 224 */       while (localContinuation.isContinue()) {
/* 225 */         localName = localContinuation.getRemainingName();
/* 226 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 227 */         localPartialCompositeContext.p_rebind(localName, paramObject, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 230 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 231 */       localContext.rebind(localCannotProceedException.getRemainingName(), paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException {
/* 236 */     unbind(new CompositeName(paramString));
/*     */   }
/*     */   public void unbind(Name paramName) throws NamingException {
/* 239 */     PartialCompositeContext localPartialCompositeContext = this;
/* 240 */     Name localName = paramName;
/* 241 */     Hashtable localHashtable = p_getEnvironment();
/* 242 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     try
/*     */     {
/* 245 */       localPartialCompositeContext.p_unbind(localName, localContinuation);
/* 246 */       while (localContinuation.isContinue()) {
/* 247 */         localName = localContinuation.getRemainingName();
/* 248 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 249 */         localPartialCompositeContext.p_unbind(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 252 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 253 */       localContext.unbind(localCannotProceedException.getRemainingName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 258 */     rename(new CompositeName(paramString1), new CompositeName(paramString2));
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 263 */     PartialCompositeContext localPartialCompositeContext = this;
/* 264 */     Name localName = paramName1;
/* 265 */     Hashtable localHashtable = p_getEnvironment();
/* 266 */     Continuation localContinuation = new Continuation(paramName1, localHashtable);
/*     */     try
/*     */     {
/* 269 */       localPartialCompositeContext.p_rename(localName, paramName2, localContinuation);
/* 270 */       while (localContinuation.isContinue()) {
/* 271 */         localName = localContinuation.getRemainingName();
/* 272 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 273 */         localPartialCompositeContext.p_rename(localName, paramName2, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 276 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 277 */       if (localCannotProceedException.getRemainingNewName() != null)
/*     */       {
/* 279 */         paramName2 = localCannotProceedException.getRemainingNewName();
/*     */       }
/* 281 */       localContext.rename(localCannotProceedException.getRemainingName(), paramName2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<NameClassPair> list(String paramString)
/*     */     throws NamingException
/*     */   {
/* 288 */     return list(new CompositeName(paramString));
/*     */   }
/*     */   public NamingEnumeration<NameClassPair> list(Name paramName) throws NamingException {
/* 294 */     PartialCompositeContext localPartialCompositeContext = this;
/* 295 */     Name localName = paramName;
/*     */ 
/* 297 */     Hashtable localHashtable = p_getEnvironment();
/* 298 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     NamingEnumeration localNamingEnumeration;
/*     */     try { localNamingEnumeration = localPartialCompositeContext.p_list(localName, localContinuation);
/* 302 */       while (localContinuation.isContinue()) {
/* 303 */         localName = localContinuation.getRemainingName();
/* 304 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 305 */         localNamingEnumeration = localPartialCompositeContext.p_list(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 308 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 309 */       localNamingEnumeration = localContext.list(localCannotProceedException.getRemainingName());
/*     */     }
/* 311 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Binding> listBindings(String paramString)
/*     */     throws NamingException
/*     */   {
/* 317 */     return listBindings(new CompositeName(paramString));
/*     */   }
/*     */   public NamingEnumeration<Binding> listBindings(Name paramName) throws NamingException {
/* 323 */     PartialCompositeContext localPartialCompositeContext = this;
/* 324 */     Name localName = paramName;
/*     */ 
/* 326 */     Hashtable localHashtable = p_getEnvironment();
/* 327 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     NamingEnumeration localNamingEnumeration;
/*     */     try { localNamingEnumeration = localPartialCompositeContext.p_listBindings(localName, localContinuation);
/* 331 */       while (localContinuation.isContinue()) {
/* 332 */         localName = localContinuation.getRemainingName();
/* 333 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 334 */         localNamingEnumeration = localPartialCompositeContext.p_listBindings(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 337 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 338 */       localNamingEnumeration = localContext.listBindings(localCannotProceedException.getRemainingName());
/*     */     }
/* 340 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException {
/* 344 */     destroySubcontext(new CompositeName(paramString));
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 348 */     PartialCompositeContext localPartialCompositeContext = this;
/* 349 */     Name localName = paramName;
/* 350 */     Hashtable localHashtable = p_getEnvironment();
/* 351 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     try
/*     */     {
/* 354 */       localPartialCompositeContext.p_destroySubcontext(localName, localContinuation);
/* 355 */       while (localContinuation.isContinue()) {
/* 356 */         localName = localContinuation.getRemainingName();
/* 357 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 358 */         localPartialCompositeContext.p_destroySubcontext(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 361 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 362 */       localContext.destroySubcontext(localCannotProceedException.getRemainingName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 367 */     return createSubcontext(new CompositeName(paramString)); } 
/* 371 */   public Context createSubcontext(Name paramName) throws NamingException { PartialCompositeContext localPartialCompositeContext = this;
/* 372 */     Name localName = paramName;
/*     */ 
/* 374 */     Hashtable localHashtable = p_getEnvironment();
/* 375 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     Context localContext1;
/*     */     try { localContext1 = localPartialCompositeContext.p_createSubcontext(localName, localContinuation);
/* 379 */       while (localContinuation.isContinue()) {
/* 380 */         localName = localContinuation.getRemainingName();
/* 381 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 382 */         localContext1 = localPartialCompositeContext.p_createSubcontext(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 385 */       Context localContext2 = NamingManager.getContinuationContext(localCannotProceedException);
/* 386 */       localContext1 = localContext2.createSubcontext(localCannotProceedException.getRemainingName());
/*     */     }
/* 388 */     return localContext1; }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException
/*     */   {
/* 392 */     return lookupLink(new CompositeName(paramString)); } 
/* 396 */   public Object lookupLink(Name paramName) throws NamingException { PartialCompositeContext localPartialCompositeContext = this;
/* 397 */     Hashtable localHashtable = p_getEnvironment();
/* 398 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 400 */     Name localName = paramName;
/*     */     Object localObject;
/*     */     try { localObject = localPartialCompositeContext.p_lookupLink(localName, localContinuation);
/* 404 */       while (localContinuation.isContinue()) {
/* 405 */         localName = localContinuation.getRemainingName();
/* 406 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 407 */         localObject = localPartialCompositeContext.p_lookupLink(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 410 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 411 */       localObject = localContext.lookupLink(localCannotProceedException.getRemainingName());
/*     */     }
/* 413 */     return localObject; }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException
/*     */   {
/* 417 */     return getNameParser(new CompositeName(paramString)); } 
/* 421 */   public NameParser getNameParser(Name paramName) throws NamingException { PartialCompositeContext localPartialCompositeContext = this;
/* 422 */     Name localName = paramName;
/*     */ 
/* 424 */     Hashtable localHashtable = p_getEnvironment();
/* 425 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */     NameParser localNameParser;
/*     */     try { localNameParser = localPartialCompositeContext.p_getNameParser(localName, localContinuation);
/* 429 */       while (localContinuation.isContinue()) {
/* 430 */         localName = localContinuation.getRemainingName();
/* 431 */         localPartialCompositeContext = getPCContext(localContinuation);
/* 432 */         localNameParser = localPartialCompositeContext.p_getNameParser(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 435 */       Context localContext = NamingManager.getContinuationContext(localCannotProceedException);
/* 436 */       localNameParser = localContext.getNameParser(localCannotProceedException.getRemainingName());
/*     */     }
/* 438 */     return localNameParser; }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 443 */     Name localName = composeName(new CompositeName(paramString1), new CompositeName(paramString2));
/*     */ 
/* 445 */     return localName.toString();
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 465 */     Name localName = (Name)paramName2.clone();
/* 466 */     if (paramName1 == null) {
/* 467 */       return localName;
/*     */     }
/* 469 */     localName.addAll(paramName1);
/*     */ 
/* 471 */     String str = (String)p_getEnvironment().get("java.naming.provider.compose.elideEmpty");
/*     */ 
/* 473 */     if ((str == null) || (!str.equalsIgnoreCase("true"))) {
/* 474 */       return localName;
/*     */     }
/*     */ 
/* 477 */     int i = paramName2.size();
/*     */ 
/* 479 */     if ((!allEmpty(paramName2)) && (!allEmpty(paramName1))) {
/* 480 */       if (localName.get(i - 1).equals(""))
/* 481 */         localName.remove(i - 1);
/* 482 */       else if (localName.get(i).equals("")) {
/* 483 */         localName.remove(i);
/*     */       }
/*     */     }
/* 486 */     return localName;
/*     */   }
/*     */ 
/*     */   protected static boolean allEmpty(Name paramName)
/*     */   {
/* 496 */     Enumeration localEnumeration = paramName.getAll();
/* 497 */     while (localEnumeration.hasMoreElements()) {
/* 498 */       if (!((String)localEnumeration.nextElement()).isEmpty()) {
/* 499 */         return false;
/*     */       }
/*     */     }
/* 502 */     return true;
/*     */   }
/*     */ 
/*     */   protected static PartialCompositeContext getPCContext(Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 512 */     Object localObject1 = paramContinuation.getResolvedObj();
/* 513 */     Object localObject2 = null;
/*     */ 
/* 515 */     if ((localObject1 instanceof PartialCompositeContext))
/*     */     {
/* 518 */       return (PartialCompositeContext)localObject1;
/*     */     }
/* 520 */     throw paramContinuation.fillInException(new CannotProceedException());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  67 */       _NNS_NAME = new CompositeName("/");
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.PartialCompositeContext
 * JD-Core Version:    0.6.2
 */