/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ public abstract class PartialCompositeDirContext extends AtomicContext
/*     */   implements DirContext
/*     */ {
/*     */   protected PartialCompositeDirContext()
/*     */   {
/*  51 */     this._contextType = 1;
/*     */   }
/*     */ 
/*     */   protected abstract Attributes p_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void p_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext p_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration p_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration p_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration p_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext p_getSchema(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext p_getSchemaClassDefinition(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   public Attributes getAttributes(String paramString)
/*     */     throws NamingException
/*     */   {
/* 117 */     return getAttributes(paramString, null);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException
/*     */   {
/* 122 */     return getAttributes(paramName, null);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 127 */     return getAttributes(new CompositeName(paramString), paramArrayOfString);
/*     */   }
/* 132 */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException { PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 133 */     Hashtable localHashtable = p_getEnvironment();
/* 134 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 136 */     Name localName = paramName;
/*     */     Attributes localAttributes;
/*     */     try { localAttributes = localPartialCompositeDirContext.p_getAttributes(localName, paramArrayOfString, localContinuation);
/* 140 */       while (localContinuation.isContinue()) {
/* 141 */         localName = localContinuation.getRemainingName();
/* 142 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 143 */         localAttributes = localPartialCompositeDirContext.p_getAttributes(localName, paramArrayOfString, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 146 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 147 */       localAttributes = localDirContext.getAttributes(localCannotProceedException.getRemainingName(), paramArrayOfString);
/*     */     }
/* 149 */     return localAttributes; }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 154 */     modifyAttributes(new CompositeName(paramString), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 159 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 160 */     Hashtable localHashtable = p_getEnvironment();
/* 161 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/* 162 */     Name localName = paramName;
/*     */     try
/*     */     {
/* 165 */       localPartialCompositeDirContext.p_modifyAttributes(localName, paramInt, paramAttributes, localContinuation);
/* 166 */       while (localContinuation.isContinue()) {
/* 167 */         localName = localContinuation.getRemainingName();
/* 168 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 169 */         localPartialCompositeDirContext.p_modifyAttributes(localName, paramInt, paramAttributes, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 172 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 173 */       localDirContext.modifyAttributes(localCannotProceedException.getRemainingName(), paramInt, paramAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 179 */     modifyAttributes(new CompositeName(paramString), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 184 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 185 */     Hashtable localHashtable = p_getEnvironment();
/* 186 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/* 187 */     Name localName = paramName;
/*     */     try
/*     */     {
/* 190 */       localPartialCompositeDirContext.p_modifyAttributes(localName, paramArrayOfModificationItem, localContinuation);
/* 191 */       while (localContinuation.isContinue()) {
/* 192 */         localName = localContinuation.getRemainingName();
/* 193 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 194 */         localPartialCompositeDirContext.p_modifyAttributes(localName, paramArrayOfModificationItem, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 197 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 198 */       localDirContext.modifyAttributes(localCannotProceedException.getRemainingName(), paramArrayOfModificationItem);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 204 */     bind(new CompositeName(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 209 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 210 */     Hashtable localHashtable = p_getEnvironment();
/* 211 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/* 212 */     Name localName = paramName;
/*     */     try
/*     */     {
/* 215 */       localPartialCompositeDirContext.p_bind(localName, paramObject, paramAttributes, localContinuation);
/* 216 */       while (localContinuation.isContinue()) {
/* 217 */         localName = localContinuation.getRemainingName();
/* 218 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 219 */         localPartialCompositeDirContext.p_bind(localName, paramObject, paramAttributes, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 222 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 223 */       localDirContext.bind(localCannotProceedException.getRemainingName(), paramObject, paramAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 229 */     rebind(new CompositeName(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 234 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 235 */     Hashtable localHashtable = p_getEnvironment();
/* 236 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/* 237 */     Name localName = paramName;
/*     */     try
/*     */     {
/* 240 */       localPartialCompositeDirContext.p_rebind(localName, paramObject, paramAttributes, localContinuation);
/* 241 */       while (localContinuation.isContinue()) {
/* 242 */         localName = localContinuation.getRemainingName();
/* 243 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 244 */         localPartialCompositeDirContext.p_rebind(localName, paramObject, paramAttributes, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 247 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 248 */       localDirContext.rebind(localCannotProceedException.getRemainingName(), paramObject, paramAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 254 */     return createSubcontext(new CompositeName(paramString), paramAttributes);
/*     */   }
/* 259 */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException { PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 260 */     Hashtable localHashtable = p_getEnvironment();
/* 261 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 263 */     Name localName = paramName;
/*     */     DirContext localDirContext1;
/*     */     try { localDirContext1 = localPartialCompositeDirContext.p_createSubcontext(localName, paramAttributes, localContinuation);
/* 267 */       while (localContinuation.isContinue()) {
/* 268 */         localName = localContinuation.getRemainingName();
/* 269 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 270 */         localDirContext1 = localPartialCompositeDirContext.p_createSubcontext(localName, paramAttributes, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 273 */       DirContext localDirContext2 = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 274 */       localDirContext1 = localDirContext2.createSubcontext(localCannotProceedException.getRemainingName(), paramAttributes);
/*     */     }
/* 276 */     return localDirContext1;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 283 */     return search(paramString, paramAttributes, null);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 290 */     return search(paramName, paramAttributes, null);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 299 */     return search(new CompositeName(paramString), paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 310 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 311 */     Hashtable localHashtable = p_getEnvironment();
/* 312 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 314 */     Name localName = paramName;
/*     */     NamingEnumeration localNamingEnumeration;
/*     */     try
/*     */     {
/* 317 */       localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramAttributes, paramArrayOfString, localContinuation);
/*     */ 
/* 319 */       while (localContinuation.isContinue()) {
/* 320 */         localName = localContinuation.getRemainingName();
/* 321 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 322 */         localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramAttributes, paramArrayOfString, localContinuation);
/*     */       }
/*     */     }
/*     */     catch (CannotProceedException localCannotProceedException) {
/* 326 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 327 */       localNamingEnumeration = localDirContext.search(localCannotProceedException.getRemainingName(), paramAttributes, paramArrayOfString);
/*     */     }
/*     */ 
/* 330 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 339 */     return search(new CompositeName(paramString1), paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, SearchControls paramSearchControls) throws NamingException
/*     */   {
/* 349 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 350 */     Hashtable localHashtable = p_getEnvironment();
/* 351 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 353 */     Name localName = paramName;
/*     */     NamingEnumeration localNamingEnumeration;
/*     */     try
/*     */     {
/* 356 */       localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramString, paramSearchControls, localContinuation);
/* 357 */       while (localContinuation.isContinue()) {
/* 358 */         localName = localContinuation.getRemainingName();
/* 359 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 360 */         localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramString, paramSearchControls, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 363 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 364 */       localNamingEnumeration = localDirContext.search(localCannotProceedException.getRemainingName(), paramString, paramSearchControls);
/*     */     }
/* 366 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 376 */     return search(new CompositeName(paramString1), paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 387 */     PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 388 */     Hashtable localHashtable = p_getEnvironment();
/* 389 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 391 */     Name localName = paramName;
/*     */     NamingEnumeration localNamingEnumeration;
/*     */     try
/*     */     {
/* 394 */       localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramString, paramArrayOfObject, paramSearchControls, localContinuation);
/* 395 */       while (localContinuation.isContinue()) {
/* 396 */         localName = localContinuation.getRemainingName();
/* 397 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 398 */         localNamingEnumeration = localPartialCompositeDirContext.p_search(localName, paramString, paramArrayOfObject, paramSearchControls, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 401 */       DirContext localDirContext = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 402 */       localNamingEnumeration = localDirContext.search(localCannotProceedException.getRemainingName(), paramString, paramArrayOfObject, paramSearchControls);
/*     */     }
/*     */ 
/* 405 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException {
/* 409 */     return getSchema(new CompositeName(paramString)); } 
/* 413 */   public DirContext getSchema(Name paramName) throws NamingException { PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 414 */     Hashtable localHashtable = p_getEnvironment();
/* 415 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 417 */     Name localName = paramName;
/*     */     DirContext localDirContext1;
/*     */     try { localDirContext1 = localPartialCompositeDirContext.p_getSchema(localName, localContinuation);
/* 421 */       while (localContinuation.isContinue()) {
/* 422 */         localName = localContinuation.getRemainingName();
/* 423 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 424 */         localDirContext1 = localPartialCompositeDirContext.p_getSchema(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 427 */       DirContext localDirContext2 = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 428 */       localDirContext1 = localDirContext2.getSchema(localCannotProceedException.getRemainingName());
/*     */     }
/* 430 */     return localDirContext1; }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString)
/*     */     throws NamingException
/*     */   {
/* 435 */     return getSchemaClassDefinition(new CompositeName(paramString));
/*     */   }
/* 440 */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException { PartialCompositeDirContext localPartialCompositeDirContext = this;
/* 441 */     Hashtable localHashtable = p_getEnvironment();
/* 442 */     Continuation localContinuation = new Continuation(paramName, localHashtable);
/*     */ 
/* 444 */     Name localName = paramName;
/*     */     DirContext localDirContext1;
/*     */     try { localDirContext1 = localPartialCompositeDirContext.p_getSchemaClassDefinition(localName, localContinuation);
/* 448 */       while (localContinuation.isContinue()) {
/* 449 */         localName = localContinuation.getRemainingName();
/* 450 */         localPartialCompositeDirContext = getPCDirContext(localContinuation);
/* 451 */         localDirContext1 = localPartialCompositeDirContext.p_getSchemaClassDefinition(localName, localContinuation);
/*     */       }
/*     */     } catch (CannotProceedException localCannotProceedException) {
/* 454 */       DirContext localDirContext2 = DirectoryManager.getContinuationDirContext(localCannotProceedException);
/* 455 */       localDirContext1 = localDirContext2.getSchemaClassDefinition(localCannotProceedException.getRemainingName());
/*     */     }
/* 457 */     return localDirContext1;
/*     */   }
/*     */ 
/*     */   protected static PartialCompositeDirContext getPCDirContext(Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 469 */     PartialCompositeContext localPartialCompositeContext = PartialCompositeContext.getPCContext(paramContinuation);
/*     */ 
/* 472 */     if (!(localPartialCompositeContext instanceof PartialCompositeDirContext)) {
/* 473 */       throw paramContinuation.fillInException(new NotContextException("Resolved object is not a DirContext."));
/*     */     }
/*     */ 
/* 478 */     return (PartialCompositeDirContext)localPartialCompositeContext;
/*     */   }
/*     */ 
/*     */   protected StringHeadTail c_parseComponent(String paramString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 493 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 495 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected Object a_lookup(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 500 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 502 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected Object a_lookupLink(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 507 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 509 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_list(Continuation paramContinuation) throws NamingException
/*     */   {
/* 514 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 516 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_listBindings(Continuation paramContinuation) throws NamingException
/*     */   {
/* 521 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 523 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected void a_bind(String paramString, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 528 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 530 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected void a_rebind(String paramString, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 535 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 537 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected void a_unbind(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 542 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 544 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected void a_destroySubcontext(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 549 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 551 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected Context a_createSubcontext(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 556 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 558 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected void a_rename(String paramString, Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 563 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 565 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ 
/*     */   protected NameParser a_getNameParser(Continuation paramContinuation) throws NamingException
/*     */   {
/* 570 */     OperationNotSupportedException localOperationNotSupportedException = new OperationNotSupportedException();
/*     */ 
/* 572 */     throw paramContinuation.fillInException(localOperationNotSupportedException);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
 * JD-Core Version:    0.6.2
 */