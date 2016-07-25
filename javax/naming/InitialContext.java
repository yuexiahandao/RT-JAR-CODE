/*     */ package javax.naming;
/*     */ 
/*     */ import com.sun.naming.internal.ResourceManager;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ public class InitialContext
/*     */   implements Context
/*     */ {
/* 141 */   protected Hashtable<Object, Object> myProps = null;
/*     */ 
/* 150 */   protected Context defaultInitCtx = null;
/*     */ 
/* 157 */   protected boolean gotDefault = false;
/*     */ 
/*     */   protected InitialContext(boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 177 */     if (!paramBoolean)
/* 178 */       init(null);
/*     */   }
/*     */ 
/*     */   public InitialContext()
/*     */     throws NamingException
/*     */   {
/* 192 */     init(null);
/*     */   }
/*     */ 
/*     */   public InitialContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 213 */     if (paramHashtable != null) {
/* 214 */       paramHashtable = (Hashtable)paramHashtable.clone();
/*     */     }
/* 216 */     init(paramHashtable);
/*     */   }
/*     */ 
/*     */   protected void init(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 238 */     this.myProps = ResourceManager.getInitialEnvironment(paramHashtable);
/*     */ 
/* 240 */     if (this.myProps.get("java.naming.factory.initial") != null)
/*     */     {
/* 242 */       getDefaultInitCtx();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static <T> T doLookup(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 270 */     return new InitialContext().lookup(paramName);
/*     */   }
/*     */ 
/*     */   public static <T> T doLookup(String paramString)
/*     */     throws NamingException
/*     */   {
/* 284 */     return new InitialContext().lookup(paramString);
/*     */   }
/*     */ 
/*     */   private static String getURLScheme(String paramString) {
/* 288 */     int i = paramString.indexOf(':');
/* 289 */     int j = paramString.indexOf('/');
/*     */ 
/* 291 */     if ((i > 0) && ((j == -1) || (i < j)))
/* 292 */       return paramString.substring(0, i);
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   protected Context getDefaultInitCtx()
/*     */     throws NamingException
/*     */   {
/* 306 */     if (!this.gotDefault) {
/* 307 */       this.defaultInitCtx = NamingManager.getInitialContext(this.myProps);
/* 308 */       this.gotDefault = true;
/*     */     }
/* 310 */     if (this.defaultInitCtx == null) {
/* 311 */       throw new NoInitialContextException();
/*     */     }
/* 313 */     return this.defaultInitCtx;
/*     */   }
/*     */ 
/*     */   protected Context getURLOrDefaultInitCtx(String paramString)
/*     */     throws NamingException
/*     */   {
/* 334 */     if (NamingManager.hasInitialContextFactoryBuilder()) {
/* 335 */       return getDefaultInitCtx();
/*     */     }
/* 337 */     String str = getURLScheme(paramString);
/* 338 */     if (str != null) {
/* 339 */       Context localContext = NamingManager.getURLContext(str, this.myProps);
/* 340 */       if (localContext != null) {
/* 341 */         return localContext;
/*     */       }
/*     */     }
/* 344 */     return getDefaultInitCtx();
/*     */   }
/*     */ 
/*     */   protected Context getURLOrDefaultInitCtx(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 391 */     if (NamingManager.hasInitialContextFactoryBuilder()) {
/* 392 */       return getDefaultInitCtx();
/*     */     }
/* 394 */     if (paramName.size() > 0) {
/* 395 */       String str1 = paramName.get(0);
/* 396 */       String str2 = getURLScheme(str1);
/* 397 */       if (str2 != null) {
/* 398 */         Context localContext = NamingManager.getURLContext(str2, this.myProps);
/* 399 */         if (localContext != null) {
/* 400 */           return localContext;
/*     */         }
/*     */       }
/*     */     }
/* 404 */     return getDefaultInitCtx();
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString)
/*     */     throws NamingException
/*     */   {
/* 411 */     return getURLOrDefaultInitCtx(paramString).lookup(paramString);
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/* 415 */     return getURLOrDefaultInitCtx(paramName).lookup(paramName);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException {
/* 419 */     getURLOrDefaultInitCtx(paramString).bind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/* 423 */     getURLOrDefaultInitCtx(paramName).bind(paramName, paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/* 427 */     getURLOrDefaultInitCtx(paramString).rebind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 431 */     getURLOrDefaultInitCtx(paramName).rebind(paramName, paramObject);
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException {
/* 435 */     getURLOrDefaultInitCtx(paramString).unbind(paramString);
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/* 439 */     getURLOrDefaultInitCtx(paramName).unbind(paramName);
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 443 */     getURLOrDefaultInitCtx(paramString1).rename(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 449 */     getURLOrDefaultInitCtx(paramName1).rename(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<NameClassPair> list(String paramString)
/*     */     throws NamingException
/*     */   {
/* 455 */     return getURLOrDefaultInitCtx(paramString).list(paramString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<NameClassPair> list(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 461 */     return getURLOrDefaultInitCtx(paramName).list(paramName);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Binding> listBindings(String paramString) throws NamingException
/*     */   {
/* 466 */     return getURLOrDefaultInitCtx(paramString).listBindings(paramString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<Binding> listBindings(Name paramName) throws NamingException
/*     */   {
/* 471 */     return getURLOrDefaultInitCtx(paramName).listBindings(paramName);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException {
/* 475 */     getURLOrDefaultInitCtx(paramString).destroySubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 479 */     getURLOrDefaultInitCtx(paramName).destroySubcontext(paramName);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 483 */     return getURLOrDefaultInitCtx(paramString).createSubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 487 */     return getURLOrDefaultInitCtx(paramName).createSubcontext(paramName);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException {
/* 491 */     return getURLOrDefaultInitCtx(paramString).lookupLink(paramString);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException {
/* 495 */     return getURLOrDefaultInitCtx(paramName).lookupLink(paramName);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException {
/* 499 */     return getURLOrDefaultInitCtx(paramString).getNameParser(paramString);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 503 */     return getURLOrDefaultInitCtx(paramName).getNameParser(paramName);
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 515 */     return paramString1;
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 528 */     return (Name)paramName1.clone();
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 533 */     this.myProps.put(paramString, paramObject);
/* 534 */     return getDefaultInitCtx().addToEnvironment(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString) throws NamingException
/*     */   {
/* 539 */     this.myProps.remove(paramString);
/* 540 */     return getDefaultInitCtx().removeFromEnvironment(paramString);
/*     */   }
/*     */ 
/*     */   public Hashtable<?, ?> getEnvironment() throws NamingException {
/* 544 */     return getDefaultInitCtx().getEnvironment();
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException {
/* 548 */     this.myProps = null;
/* 549 */     if (this.defaultInitCtx != null) {
/* 550 */       this.defaultInitCtx.close();
/* 551 */       this.defaultInitCtx = null;
/*     */     }
/* 553 */     this.gotDefault = false;
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace() throws NamingException {
/* 557 */     return getDefaultInitCtx().getNameInNamespace();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.InitialContext
 * JD-Core Version:    0.6.2
 */