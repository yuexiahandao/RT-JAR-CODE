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
/*     */ public abstract class ComponentDirContext extends PartialCompositeDirContext
/*     */ {
/*     */   protected ComponentDirContext()
/*     */   {
/*  48 */     this._contextType = 2;
/*     */   }
/*     */ 
/*     */   protected abstract Attributes c_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext c_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration c_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration c_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration c_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext c_getSchema(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract DirContext c_getSchemaClassDefinition(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected Attributes c_getAttributes_nns(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 132 */     c_processJunction_nns(paramName, paramContinuation);
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes_nns(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 141 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_modifyAttributes_nns(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 148 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_bind_nns(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 156 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind_nns(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 164 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected DirContext c_createSubcontext_nns(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 171 */     c_processJunction_nns(paramName, paramContinuation);
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 180 */     c_processJunction_nns(paramName, paramContinuation);
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 189 */     c_processJunction_nns(paramName, paramContinuation);
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_search_nns(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 199 */     c_processJunction_nns(paramName, paramContinuation);
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchema_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 205 */     c_processJunction_nns(paramName, paramContinuation);
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   protected DirContext c_getSchemaClassDefinition_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 211 */     c_processJunction_nns(paramName, paramContinuation);
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   protected Attributes p_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 223 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 224 */     Attributes localAttributes = null;
/* 225 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 227 */       localAttributes = c_getAttributes_nns(localHeadTail.getHead(), paramArrayOfString, paramContinuation);
/* 228 */       break;
/*     */     case 2:
/* 231 */       localAttributes = c_getAttributes(localHeadTail.getHead(), paramArrayOfString, paramContinuation);
/* 232 */       break;
/*     */     }
/*     */ 
/* 239 */     return localAttributes;
/*     */   }
/*     */ 
/*     */   protected void p_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 246 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 247 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 249 */       c_modifyAttributes_nns(localHeadTail.getHead(), paramInt, paramAttributes, paramContinuation);
/* 250 */       break;
/*     */     case 2:
/* 253 */       c_modifyAttributes(localHeadTail.getHead(), paramInt, paramAttributes, paramContinuation);
/* 254 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 266 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 267 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 269 */       c_modifyAttributes_nns(localHeadTail.getHead(), paramArrayOfModificationItem, paramContinuation);
/* 270 */       break;
/*     */     case 2:
/* 273 */       c_modifyAttributes(localHeadTail.getHead(), paramArrayOfModificationItem, paramContinuation);
/* 274 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 288 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 289 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 291 */       c_bind_nns(localHeadTail.getHead(), paramObject, paramAttributes, paramContinuation);
/* 292 */       break;
/*     */     case 2:
/* 295 */       c_bind(localHeadTail.getHead(), paramObject, paramAttributes, paramContinuation);
/* 296 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 308 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 309 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 311 */       c_rebind_nns(localHeadTail.getHead(), paramObject, paramAttributes, paramContinuation);
/* 312 */       break;
/*     */     case 2:
/* 315 */       c_rebind(localHeadTail.getHead(), paramObject, paramAttributes, paramContinuation);
/* 316 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DirContext p_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 329 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 330 */     DirContext localDirContext = null;
/* 331 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 333 */       localDirContext = c_createSubcontext_nns(localHeadTail.getHead(), paramAttributes, paramContinuation);
/* 334 */       break;
/*     */     case 2:
/* 337 */       localDirContext = c_createSubcontext(localHeadTail.getHead(), paramAttributes, paramContinuation);
/* 338 */       break;
/*     */     }
/*     */ 
/* 345 */     return localDirContext;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration p_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 353 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 354 */     NamingEnumeration localNamingEnumeration = null;
/* 355 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 357 */       localNamingEnumeration = c_search_nns(localHeadTail.getHead(), paramAttributes, paramArrayOfString, paramContinuation);
/*     */ 
/* 359 */       break;
/*     */     case 2:
/* 362 */       localNamingEnumeration = c_search(localHeadTail.getHead(), paramAttributes, paramArrayOfString, paramContinuation);
/*     */ 
/* 364 */       break;
/*     */     }
/*     */ 
/* 371 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration p_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 378 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 379 */     NamingEnumeration localNamingEnumeration = null;
/* 380 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 382 */       localNamingEnumeration = c_search_nns(localHeadTail.getHead(), paramString, paramSearchControls, paramContinuation);
/* 383 */       break;
/*     */     case 2:
/* 386 */       localNamingEnumeration = c_search(localHeadTail.getHead(), paramString, paramSearchControls, paramContinuation);
/* 387 */       break;
/*     */     }
/*     */ 
/* 394 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration p_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 403 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 404 */     NamingEnumeration localNamingEnumeration = null;
/* 405 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 407 */       localNamingEnumeration = c_search_nns(localHeadTail.getHead(), paramString, paramArrayOfObject, paramSearchControls, paramContinuation);
/*     */ 
/* 409 */       break;
/*     */     case 2:
/* 412 */       localNamingEnumeration = c_search(localHeadTail.getHead(), paramString, paramArrayOfObject, paramSearchControls, paramContinuation);
/* 413 */       break;
/*     */     }
/*     */ 
/* 420 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   protected DirContext p_getSchema(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 425 */     DirContext localDirContext = null;
/* 426 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 427 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 429 */       localDirContext = c_getSchema_nns(localHeadTail.getHead(), paramContinuation);
/* 430 */       break;
/*     */     case 2:
/* 433 */       localDirContext = c_getSchema(localHeadTail.getHead(), paramContinuation);
/* 434 */       break;
/*     */     }
/*     */ 
/* 441 */     return localDirContext;
/*     */   }
/*     */ 
/*     */   protected DirContext p_getSchemaClassDefinition(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 446 */     DirContext localDirContext = null;
/* 447 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 448 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 450 */       localDirContext = c_getSchemaClassDefinition_nns(localHeadTail.getHead(), paramContinuation);
/* 451 */       break;
/*     */     case 2:
/* 454 */       localDirContext = c_getSchemaClassDefinition(localHeadTail.getHead(), paramContinuation);
/* 455 */       break;
/*     */     }
/*     */ 
/* 462 */     return localDirContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.ComponentDirContext
 * JD-Core Version:    0.6.2
 */