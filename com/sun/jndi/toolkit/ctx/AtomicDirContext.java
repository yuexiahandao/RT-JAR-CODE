/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ 
/*     */ public abstract class AtomicDirContext extends ComponentDirContext
/*     */ {
/*     */   protected AtomicDirContext()
/*     */   {
/*  48 */     this._contextType = 3;
/*     */   }
/*     */ 
/*     */   protected abstract Attributes a_getAttributes(String paramString, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_modifyAttributes(String paramString, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_bind(String paramString, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_rebind(String paramString, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext a_createSubcontext(String paramString, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration a_search(Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration a_search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration a_search(String paramString1, String paramString2, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext a_getSchema(Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext a_getSchemaClassDefinition(Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected Attributes a_getAttributes_nns(String paramString, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 126 */     a_processJunction_nns(paramString, paramContinuation);
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   protected void a_modifyAttributes_nns(String paramString, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 134 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_modifyAttributes_nns(String paramString, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 141 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_bind_nns(String paramString, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 148 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_rebind_nns(String paramString, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 155 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected DirContext a_createSubcontext_nns(String paramString, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 162 */     a_processJunction_nns(paramString, paramContinuation);
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_search_nns(Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 170 */     a_processJunction_nns(paramContinuation);
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_search_nns(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 180 */     a_processJunction_nns(paramString1, paramContinuation);
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_search_nns(String paramString1, String paramString2, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 189 */     a_processJunction_nns(paramString1, paramContinuation);
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext a_getSchema_nns(Continuation paramContinuation) throws NamingException {
/* 194 */     a_processJunction_nns(paramContinuation);
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext a_getSchemaDefinition_nns(Continuation paramContinuation) throws NamingException
/*     */   {
/* 200 */     a_processJunction_nns(paramContinuation);
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   protected Attributes c_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 210 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 211 */       return a_getAttributes(paramName.toString(), paramArrayOfString, paramContinuation);
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 218 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 219 */       a_modifyAttributes(paramName.toString(), paramInt, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 225 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 226 */       a_modifyAttributes(paramName.toString(), paramArrayOfModificationItem, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 232 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 233 */       a_bind(paramName.toString(), paramObject, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 239 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 240 */       a_rebind(paramName.toString(), paramObject, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected DirContext c_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 247 */     if (resolve_to_penultimate_context(paramName, paramContinuation)) {
/* 248 */       return a_createSubcontext(paramName.toString(), paramAttributes, paramContinuation);
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 258 */     if (resolve_to_context(paramName, paramContinuation))
/* 259 */       return a_search(paramAttributes, paramArrayOfString, paramContinuation);
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 267 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 268 */       return a_search(paramName.toString(), paramString, paramSearchControls, paramContinuation);
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 277 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 278 */       return a_search(paramName.toString(), paramString, paramArrayOfObject, paramSearchControls, paramContinuation);
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchema(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 284 */     if (resolve_to_context(paramName, paramContinuation))
/* 285 */       return a_getSchema(paramContinuation);
/* 286 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchemaClassDefinition(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 291 */     if (resolve_to_context(paramName, paramContinuation))
/* 292 */       return a_getSchemaClassDefinition(paramContinuation);
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   protected Attributes c_getAttributes_nns(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 301 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 302 */       return a_getAttributes_nns(paramName.toString(), paramArrayOfString, paramContinuation);
/* 303 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes_nns(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 309 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 310 */       a_modifyAttributes_nns(paramName.toString(), paramInt, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes_nns(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 316 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 317 */       a_modifyAttributes_nns(paramName.toString(), paramArrayOfModificationItem, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_bind_nns(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 323 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 324 */       a_bind_nns(paramName.toString(), paramObject, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind_nns(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 330 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 331 */       a_rebind_nns(paramName.toString(), paramObject, paramAttributes, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected DirContext c_createSubcontext_nns(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 338 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 339 */       return a_createSubcontext_nns(paramName.toString(), paramAttributes, paramContinuation);
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 348 */     resolve_to_nns_and_continue(paramName, paramContinuation);
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 356 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 357 */       return a_search_nns(paramName.toString(), paramString, paramSearchControls, paramContinuation);
/* 358 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 367 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation)) {
/* 368 */       return a_search_nns(paramName.toString(), paramString, paramArrayOfObject, paramSearchControls, paramContinuation);
/*     */     }
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchema_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 375 */     resolve_to_nns_and_continue(paramName, paramContinuation);
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchemaClassDefinition_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 381 */     resolve_to_nns_and_continue(paramName, paramContinuation);
/* 382 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.AtomicDirContext
 * JD-Core Version:    0.6.2
 */