/*     */ package com.sun.jndi.toolkit.url;
/*     */ 
/*     */ import java.net.MalformedURLException;
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
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.spi.NamingManager;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public abstract class GenericURLContext
/*     */   implements Context
/*     */ {
/*  51 */   protected Hashtable myEnv = null;
/*     */ 
/*     */   public GenericURLContext(Hashtable paramHashtable)
/*     */   {
/*  55 */     this.myEnv = paramHashtable;
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException {
/*  59 */     this.myEnv = null;
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace() throws NamingException {
/*  63 */     return "";
/*     */   }
/*     */ 
/*     */   protected abstract ResolveResult getRootURLContext(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException;
/*     */ 
/*     */   protected Name getURLSuffix(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 116 */     String str = paramString2.substring(paramString1.length());
/* 117 */     if (str.length() == 0) {
/* 118 */       return new CompositeName();
/*     */     }
/*     */ 
/* 121 */     if (str.charAt(0) == '/') {
/* 122 */       str = str.substring(1);
/*     */     }
/*     */     try
/*     */     {
/* 126 */       return new CompositeName().add(UrlUtil.decode(str));
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 128 */       throw new InvalidNameException(localMalformedURLException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getURLPrefix(String paramString)
/*     */     throws NamingException
/*     */   {
/* 150 */     int i = paramString.indexOf(":");
/*     */ 
/* 152 */     if (i < 0) {
/* 153 */       throw new OperationNotSupportedException("Invalid URL: " + paramString);
/*     */     }
/* 155 */     i++;
/*     */ 
/* 157 */     if (paramString.startsWith("//", i)) {
/* 158 */       i += 2;
/*     */ 
/* 161 */       int j = paramString.indexOf("/", i);
/* 162 */       if (j >= 0)
/* 163 */         i = j;
/*     */       else {
/* 165 */         i = paramString.length();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 170 */     return paramString.substring(0, i);
/*     */   }
/*     */ 
/*     */   protected boolean urlEquals(String paramString1, String paramString2)
/*     */   {
/* 180 */     return paramString1.equals(paramString2);
/*     */   }
/*     */ 
/*     */   protected Context getContinuationContext(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 192 */     Object localObject = lookup(paramName.get(0));
/* 193 */     CannotProceedException localCannotProceedException = new CannotProceedException();
/* 194 */     localCannotProceedException.setResolvedObj(localObject);
/* 195 */     localCannotProceedException.setEnvironment(this.myEnv);
/* 196 */     return NamingManager.getContinuationContext(localCannotProceedException);
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString) throws NamingException {
/* 200 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 201 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 203 */       return localContext.lookup(localResolveResult.getRemainingName());
/*     */     } finally {
/* 205 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/* 210 */     if (paramName.size() == 1) {
/* 211 */       return lookup(paramName.get(0));
/*     */     }
/* 213 */     Context localContext = getContinuationContext(paramName);
/*     */     try {
/* 215 */       return localContext.lookup(paramName.getSuffix(1));
/*     */     } finally {
/* 217 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 223 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 224 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 226 */       localContext.bind(localResolveResult.getRemainingName(), paramObject);
/*     */     } finally {
/* 228 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/* 233 */     if (paramName.size() == 1) {
/* 234 */       bind(paramName.get(0), paramObject);
/*     */     } else {
/* 236 */       Context localContext = getContinuationContext(paramName);
/*     */       try {
/* 238 */         localContext.bind(paramName.getSuffix(1), paramObject);
/*     */       } finally {
/* 240 */         localContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/* 246 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 247 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 249 */       localContext.rebind(localResolveResult.getRemainingName(), paramObject);
/*     */     } finally {
/* 251 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 256 */     if (paramName.size() == 1) {
/* 257 */       rebind(paramName.get(0), paramObject);
/*     */     } else {
/* 259 */       Context localContext = getContinuationContext(paramName);
/*     */       try {
/* 261 */         localContext.rebind(paramName.getSuffix(1), paramObject);
/*     */       } finally {
/* 263 */         localContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException {
/* 269 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 270 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 272 */       localContext.unbind(localResolveResult.getRemainingName());
/*     */     } finally {
/* 274 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/* 279 */     if (paramName.size() == 1) {
/* 280 */       unbind(paramName.get(0));
/*     */     } else {
/* 282 */       Context localContext = getContinuationContext(paramName);
/*     */       try {
/* 284 */         localContext.unbind(paramName.getSuffix(1));
/*     */       } finally {
/* 286 */         localContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 292 */     String str1 = getURLPrefix(paramString1);
/* 293 */     String str2 = getURLPrefix(paramString2);
/* 294 */     if (!urlEquals(str1, str2)) {
/* 295 */       throw new OperationNotSupportedException("Renaming using different URL prefixes not supported : " + paramString1 + " " + paramString2);
/*     */     }
/*     */ 
/* 300 */     ResolveResult localResolveResult = getRootURLContext(paramString1, this.myEnv);
/* 301 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 303 */       localContext.rename(localResolveResult.getRemainingName(), getURLSuffix(str2, paramString2));
/*     */     } finally {
/* 305 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2) throws NamingException {
/* 310 */     if (paramName1.size() == 1) {
/* 311 */       if (paramName2.size() != 1) {
/* 312 */         throw new OperationNotSupportedException("Renaming to a Name with more components not supported: " + paramName2);
/*     */       }
/*     */ 
/* 315 */       rename(paramName1.get(0), paramName2.get(0));
/*     */     }
/*     */     else
/*     */     {
/* 319 */       if (!urlEquals(paramName1.get(0), paramName2.get(0))) {
/* 320 */         throw new OperationNotSupportedException("Renaming using different URLs as first components not supported: " + paramName1 + " " + paramName2);
/*     */       }
/*     */ 
/* 325 */       Context localContext = getContinuationContext(paramName1);
/*     */       try {
/* 327 */         localContext.rename(paramName1.getSuffix(1), paramName2.getSuffix(1));
/*     */       } finally {
/* 329 */         localContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<NameClassPair> list(String paramString) throws NamingException {
/* 335 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 336 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 338 */       return localContext.list(localResolveResult.getRemainingName());
/*     */     } finally {
/* 340 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<NameClassPair> list(Name paramName) throws NamingException {
/* 345 */     if (paramName.size() == 1) {
/* 346 */       return list(paramName.get(0));
/*     */     }
/* 348 */     Context localContext = getContinuationContext(paramName);
/*     */     try {
/* 350 */       return localContext.list(paramName.getSuffix(1));
/*     */     } finally {
/* 352 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Binding> listBindings(String paramString)
/*     */     throws NamingException
/*     */   {
/* 359 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 360 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 362 */       return localContext.listBindings(localResolveResult.getRemainingName());
/*     */     } finally {
/* 364 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Binding> listBindings(Name paramName) throws NamingException {
/* 369 */     if (paramName.size() == 1) {
/* 370 */       return listBindings(paramName.get(0));
/*     */     }
/* 372 */     Context localContext = getContinuationContext(paramName);
/*     */     try {
/* 374 */       return localContext.listBindings(paramName.getSuffix(1));
/*     */     } finally {
/* 376 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException
/*     */   {
/* 382 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 383 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 385 */       localContext.destroySubcontext(localResolveResult.getRemainingName());
/*     */     } finally {
/* 387 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 392 */     if (paramName.size() == 1) {
/* 393 */       destroySubcontext(paramName.get(0));
/*     */     } else {
/* 395 */       Context localContext = getContinuationContext(paramName);
/*     */       try {
/* 397 */         localContext.destroySubcontext(paramName.getSuffix(1));
/*     */       } finally {
/* 399 */         localContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 405 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 406 */     Context localContext1 = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 408 */       return localContext1.createSubcontext(localResolveResult.getRemainingName());
/*     */     } finally {
/* 410 */       localContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 415 */     if (paramName.size() == 1) {
/* 416 */       return createSubcontext(paramName.get(0));
/*     */     }
/* 418 */     Context localContext1 = getContinuationContext(paramName);
/*     */     try {
/* 420 */       return localContext1.createSubcontext(paramName.getSuffix(1));
/*     */     } finally {
/* 422 */       localContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException
/*     */   {
/* 428 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 429 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 431 */       return localContext.lookupLink(localResolveResult.getRemainingName());
/*     */     } finally {
/* 433 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException {
/* 438 */     if (paramName.size() == 1) {
/* 439 */       return lookupLink(paramName.get(0));
/*     */     }
/* 441 */     Context localContext = getContinuationContext(paramName);
/*     */     try {
/* 443 */       return localContext.lookupLink(paramName.getSuffix(1));
/*     */     } finally {
/* 445 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException
/*     */   {
/* 451 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 452 */     Context localContext = (Context)localResolveResult.getResolvedObj();
/*     */     try {
/* 454 */       return localContext.getNameParser(localResolveResult.getRemainingName());
/*     */     } finally {
/* 456 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 461 */     if (paramName.size() == 1) {
/* 462 */       return getNameParser(paramName.get(0));
/*     */     }
/* 464 */     Context localContext = getContinuationContext(paramName);
/*     */     try {
/* 466 */       return localContext.getNameParser(paramName.getSuffix(1));
/*     */     } finally {
/* 468 */       localContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 475 */     if (paramString2.equals(""))
/* 476 */       return paramString1;
/* 477 */     if (paramString1.equals("")) {
/* 478 */       return paramString2;
/*     */     }
/* 480 */     return paramString2 + "/" + paramString1;
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 485 */     Name localName = (Name)paramName2.clone();
/* 486 */     localName.addAll(paramName1);
/* 487 */     return localName;
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString) throws NamingException
/*     */   {
/* 492 */     if (this.myEnv == null) {
/* 493 */       return null;
/*     */     }
/* 495 */     this.myEnv = ((Hashtable)this.myEnv.clone());
/* 496 */     return this.myEnv.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 501 */     this.myEnv = (this.myEnv == null ? new Hashtable(11, 0.75F) : (Hashtable)this.myEnv.clone());
/*     */ 
/* 503 */     return this.myEnv.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 507 */     if (this.myEnv == null) {
/* 508 */       return new Hashtable(5, 0.75F);
/*     */     }
/* 510 */     return (Hashtable)this.myEnv.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.url.GenericURLContext
 * JD-Core Version:    0.6.2
 */