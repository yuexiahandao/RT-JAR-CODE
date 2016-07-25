/*     */ package javax.naming.spi;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class ContinuationContext
/*     */   implements Context, Resolver
/*     */ {
/*     */   protected CannotProceedException cpe;
/*     */   protected Hashtable env;
/*  42 */   protected Context contCtx = null;
/*     */ 
/*     */   protected ContinuationContext(CannotProceedException paramCannotProceedException, Hashtable paramHashtable)
/*     */   {
/*  46 */     this.cpe = paramCannotProceedException;
/*  47 */     this.env = paramHashtable;
/*     */   }
/*     */ 
/*     */   protected Context getTargetContext() throws NamingException {
/*  51 */     if (this.contCtx == null) {
/*  52 */       if (this.cpe.getResolvedObj() == null) {
/*  53 */         throw ((NamingException)this.cpe.fillInStackTrace());
/*     */       }
/*  55 */       this.contCtx = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
/*     */ 
/*  59 */       if (this.contCtx == null)
/*  60 */         throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/*  62 */     return this.contCtx;
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/*  66 */     Context localContext = getTargetContext();
/*  67 */     return localContext.lookup(paramName);
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString) throws NamingException {
/*  71 */     Context localContext = getTargetContext();
/*  72 */     return localContext.lookup(paramString);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/*  76 */     Context localContext = getTargetContext();
/*  77 */     localContext.bind(paramName, paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException {
/*  81 */     Context localContext = getTargetContext();
/*  82 */     localContext.bind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/*  86 */     Context localContext = getTargetContext();
/*  87 */     localContext.rebind(paramName, paramObject);
/*     */   }
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/*  90 */     Context localContext = getTargetContext();
/*  91 */     localContext.rebind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/*  95 */     Context localContext = getTargetContext();
/*  96 */     localContext.unbind(paramName);
/*     */   }
/*     */   public void unbind(String paramString) throws NamingException {
/*  99 */     Context localContext = getTargetContext();
/* 100 */     localContext.unbind(paramString);
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2) throws NamingException {
/* 104 */     Context localContext = getTargetContext();
/* 105 */     localContext.rename(paramName1, paramName2);
/*     */   }
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 108 */     Context localContext = getTargetContext();
/* 109 */     localContext.rename(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(Name paramName) throws NamingException {
/* 113 */     Context localContext = getTargetContext();
/* 114 */     return localContext.list(paramName);
/*     */   }
/*     */   public NamingEnumeration list(String paramString) throws NamingException {
/* 117 */     Context localContext = getTargetContext();
/* 118 */     return localContext.list(paramString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 125 */     Context localContext = getTargetContext();
/* 126 */     return localContext.listBindings(paramName);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(String paramString) throws NamingException {
/* 130 */     Context localContext = getTargetContext();
/* 131 */     return localContext.listBindings(paramString);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 135 */     Context localContext = getTargetContext();
/* 136 */     localContext.destroySubcontext(paramName);
/*     */   }
/*     */   public void destroySubcontext(String paramString) throws NamingException {
/* 139 */     Context localContext = getTargetContext();
/* 140 */     localContext.destroySubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 144 */     Context localContext = getTargetContext();
/* 145 */     return localContext.createSubcontext(paramName);
/*     */   }
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 148 */     Context localContext = getTargetContext();
/* 149 */     return localContext.createSubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException {
/* 153 */     Context localContext = getTargetContext();
/* 154 */     return localContext.lookupLink(paramName);
/*     */   }
/*     */   public Object lookupLink(String paramString) throws NamingException {
/* 157 */     Context localContext = getTargetContext();
/* 158 */     return localContext.lookupLink(paramString);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 162 */     Context localContext = getTargetContext();
/* 163 */     return localContext.getNameParser(paramName);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException {
/* 167 */     Context localContext = getTargetContext();
/* 168 */     return localContext.getNameParser(paramString);
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 174 */     Context localContext = getTargetContext();
/* 175 */     return localContext.composeName(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2) throws NamingException
/*     */   {
/* 180 */     Context localContext = getTargetContext();
/* 181 */     return localContext.composeName(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 186 */     Context localContext = getTargetContext();
/* 187 */     return localContext.addToEnvironment(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString) throws NamingException
/*     */   {
/* 192 */     Context localContext = getTargetContext();
/* 193 */     return localContext.removeFromEnvironment(paramString);
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 197 */     Context localContext = getTargetContext();
/* 198 */     return localContext.getEnvironment();
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace() throws NamingException {
/* 202 */     Context localContext = getTargetContext();
/* 203 */     return localContext.getNameInNamespace();
/*     */   }
/*     */ 
/*     */   public ResolveResult resolveToClass(Name paramName, Class<? extends Context> paramClass)
/*     */     throws NamingException
/*     */   {
/* 210 */     if (this.cpe.getResolvedObj() == null) {
/* 211 */       throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/* 213 */     Resolver localResolver = NamingManager.getResolver(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
/*     */ 
/* 217 */     if (localResolver == null)
/* 218 */       throw ((NamingException)this.cpe.fillInStackTrace());
/* 219 */     return localResolver.resolveToClass(paramName, paramClass);
/*     */   }
/*     */ 
/*     */   public ResolveResult resolveToClass(String paramString, Class<? extends Context> paramClass)
/*     */     throws NamingException
/*     */   {
/* 226 */     if (this.cpe.getResolvedObj() == null) {
/* 227 */       throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/* 229 */     Resolver localResolver = NamingManager.getResolver(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
/*     */ 
/* 233 */     if (localResolver == null)
/* 234 */       throw ((NamingException)this.cpe.fillInStackTrace());
/* 235 */     return localResolver.resolveToClass(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException {
/* 239 */     this.cpe = null;
/* 240 */     this.env = null;
/* 241 */     if (this.contCtx != null) {
/* 242 */       this.contCtx.close();
/* 243 */       this.contCtx = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.ContinuationContext
 * JD-Core Version:    0.6.2
 */