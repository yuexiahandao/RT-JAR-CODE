/*     */ package com.sun.jndi.toolkit.url;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public abstract class GenericURLDirContext extends GenericURLContext
/*     */   implements DirContext
/*     */ {
/*     */   protected GenericURLDirContext(Hashtable paramHashtable)
/*     */   {
/*  54 */     super(paramHashtable);
/*     */   }
/*     */ 
/*     */   protected DirContext getContinuationDirContext(Name paramName)
/*     */     throws NamingException
/*     */   {
/*  67 */     Object localObject = lookup(paramName.get(0));
/*  68 */     CannotProceedException localCannotProceedException = new CannotProceedException();
/*  69 */     localCannotProceedException.setResolvedObj(localObject);
/*  70 */     localCannotProceedException.setEnvironment(this.myEnv);
/*  71 */     return DirectoryManager.getContinuationDirContext(localCannotProceedException);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString) throws NamingException
/*     */   {
/*  76 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/*  77 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/*  79 */       return localDirContext.getAttributes(localResolveResult.getRemainingName());
/*     */     } finally {
/*  81 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException {
/*  86 */     if (paramName.size() == 1) {
/*  87 */       return getAttributes(paramName.get(0));
/*     */     }
/*  89 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/*  91 */       return localDirContext.getAttributes(paramName.getSuffix(1));
/*     */     } finally {
/*  93 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 100 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 101 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 103 */       return localDirContext.getAttributes(localResolveResult.getRemainingName(), paramArrayOfString);
/*     */     } finally {
/* 105 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 111 */     if (paramName.size() == 1) {
/* 112 */       return getAttributes(paramName.get(0), paramArrayOfString);
/*     */     }
/* 114 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/* 116 */       return localDirContext.getAttributes(paramName.getSuffix(1), paramArrayOfString);
/*     */     } finally {
/* 118 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 125 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 126 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 128 */       localDirContext.modifyAttributes(localResolveResult.getRemainingName(), paramInt, paramAttributes);
/*     */     } finally {
/* 130 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 136 */     if (paramName.size() == 1) {
/* 137 */       modifyAttributes(paramName.get(0), paramInt, paramAttributes);
/*     */     } else {
/* 139 */       DirContext localDirContext = getContinuationDirContext(paramName);
/*     */       try {
/* 141 */         localDirContext.modifyAttributes(paramName.getSuffix(1), paramInt, paramAttributes);
/*     */       } finally {
/* 143 */         localDirContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 150 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 151 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 153 */       localDirContext.modifyAttributes(localResolveResult.getRemainingName(), paramArrayOfModificationItem);
/*     */     } finally {
/* 155 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 161 */     if (paramName.size() == 1) {
/* 162 */       modifyAttributes(paramName.get(0), paramArrayOfModificationItem);
/*     */     } else {
/* 164 */       DirContext localDirContext = getContinuationDirContext(paramName);
/*     */       try {
/* 166 */         localDirContext.modifyAttributes(paramName.getSuffix(1), paramArrayOfModificationItem);
/*     */       } finally {
/* 168 */         localDirContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 175 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 176 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 178 */       localDirContext.bind(localResolveResult.getRemainingName(), paramObject, paramAttributes);
/*     */     } finally {
/* 180 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 186 */     if (paramName.size() == 1) {
/* 187 */       bind(paramName.get(0), paramObject, paramAttributes);
/*     */     } else {
/* 189 */       DirContext localDirContext = getContinuationDirContext(paramName);
/*     */       try {
/* 191 */         localDirContext.bind(paramName.getSuffix(1), paramObject, paramAttributes);
/*     */       } finally {
/* 193 */         localDirContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 200 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 201 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 203 */       localDirContext.rebind(localResolveResult.getRemainingName(), paramObject, paramAttributes);
/*     */     } finally {
/* 205 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 211 */     if (paramName.size() == 1) {
/* 212 */       rebind(paramName.get(0), paramObject, paramAttributes);
/*     */     } else {
/* 214 */       DirContext localDirContext = getContinuationDirContext(paramName);
/*     */       try {
/* 216 */         localDirContext.rebind(paramName.getSuffix(1), paramObject, paramAttributes);
/*     */       } finally {
/* 218 */         localDirContext.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 225 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 226 */     DirContext localDirContext1 = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 228 */       return localDirContext1.createSubcontext(localResolveResult.getRemainingName(), paramAttributes);
/*     */     } finally {
/* 230 */       localDirContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 236 */     if (paramName.size() == 1) {
/* 237 */       return createSubcontext(paramName.get(0), paramAttributes);
/*     */     }
/* 239 */     DirContext localDirContext1 = getContinuationDirContext(paramName);
/*     */     try {
/* 241 */       return localDirContext1.createSubcontext(paramName.getSuffix(1), paramAttributes);
/*     */     } finally {
/* 243 */       localDirContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException
/*     */   {
/* 249 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 250 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/* 251 */     return localDirContext.getSchema(localResolveResult.getRemainingName());
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException {
/* 255 */     if (paramName.size() == 1) {
/* 256 */       return getSchema(paramName.get(0));
/*     */     }
/* 258 */     DirContext localDirContext1 = getContinuationDirContext(paramName);
/*     */     try {
/* 260 */       return localDirContext1.getSchema(paramName.getSuffix(1));
/*     */     } finally {
/* 262 */       localDirContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString)
/*     */     throws NamingException
/*     */   {
/* 269 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 270 */     DirContext localDirContext1 = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 272 */       return localDirContext1.getSchemaClassDefinition(localResolveResult.getRemainingName());
/*     */     } finally {
/* 274 */       localDirContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException
/*     */   {
/* 280 */     if (paramName.size() == 1) {
/* 281 */       return getSchemaClassDefinition(paramName.get(0));
/*     */     }
/* 283 */     DirContext localDirContext1 = getContinuationDirContext(paramName);
/*     */     try {
/* 285 */       return localDirContext1.getSchemaClassDefinition(paramName.getSuffix(1));
/*     */     } finally {
/* 287 */       localDirContext1.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 295 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 296 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 298 */       return localDirContext.search(localResolveResult.getRemainingName(), paramAttributes);
/*     */     } finally {
/* 300 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 307 */     if (paramName.size() == 1) {
/* 308 */       return search(paramName.get(0), paramAttributes);
/*     */     }
/* 310 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/* 312 */       return localDirContext.search(paramName.getSuffix(1), paramAttributes);
/*     */     } finally {
/* 314 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 323 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 324 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 326 */       return localDirContext.search(localResolveResult.getRemainingName(), paramAttributes, paramArrayOfString);
/*     */     }
/*     */     finally {
/* 329 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 337 */     if (paramName.size() == 1) {
/* 338 */       return search(paramName.get(0), paramAttributes, paramArrayOfString);
/*     */     }
/*     */ 
/* 341 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/* 343 */       return localDirContext.search(paramName.getSuffix(1), paramAttributes, paramArrayOfString);
/*     */     }
/*     */     finally {
/* 346 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 355 */     ResolveResult localResolveResult = getRootURLContext(paramString1, this.myEnv);
/* 356 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 358 */       return localDirContext.search(localResolveResult.getRemainingName(), paramString2, paramSearchControls);
/*     */     } finally {
/* 360 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 368 */     if (paramName.size() == 1) {
/* 369 */       return search(paramName.get(0), paramString, paramSearchControls);
/*     */     }
/* 371 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/* 373 */       return localDirContext.search(paramName.getSuffix(1), paramString, paramSearchControls);
/*     */     } finally {
/* 375 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 385 */     ResolveResult localResolveResult = getRootURLContext(paramString1, this.myEnv);
/* 386 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 388 */       return localDirContext.search(localResolveResult.getRemainingName(), paramString2, paramArrayOfObject, paramSearchControls);
/*     */     }
/*     */     finally {
/* 391 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 400 */     if (paramName.size() == 1) {
/* 401 */       return search(paramName.get(0), paramString, paramArrayOfObject, paramSearchControls);
/*     */     }
/* 403 */     DirContext localDirContext = getContinuationDirContext(paramName);
/*     */     try {
/* 405 */       return localDirContext.search(paramName.getSuffix(1), paramString, paramArrayOfObject, paramSearchControls);
/*     */     } finally {
/* 407 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.url.GenericURLDirContext
 * JD-Core Version:    0.6.2
 */