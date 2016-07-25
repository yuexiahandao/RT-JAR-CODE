/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.LinkRef;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ 
/*     */ public abstract class AtomicContext extends ComponentContext
/*     */ {
/*  46 */   private static int debug = 0;
/*     */ 
/*     */   protected AtomicContext() {
/*  49 */     this._contextType = 3;
/*     */   }
/*     */ 
/*     */   protected abstract Object a_lookup(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Object a_lookupLink(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration a_list(Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration a_listBindings(Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_bind(String paramString, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_rebind(String paramString, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_unbind(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_destroySubcontext(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Context a_createSubcontext(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void a_rename(String paramString, Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NameParser a_getNameParser(Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract StringHeadTail c_parseComponent(String paramString, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected Object a_resolveIntermediate_nns(String paramString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 119 */       final Object localObject = a_lookup(paramString, paramContinuation);
/*     */ 
/* 125 */       if ((localObject != null) && (getClass().isInstance(localObject)))
/*     */       {
/* 129 */         paramContinuation.setContinueNNS(localObject, paramString, this);
/* 130 */         return null;
/*     */       }
/* 132 */       if ((localObject != null) && (!(localObject instanceof Context)))
/*     */       {
/* 135 */         RefAddr local1 = new RefAddr("nns") { private static final long serialVersionUID = -3399518522645918499L;
/*     */ 
/* 137 */           public Object getContent() { return localObject; }
/*     */ 
/*     */         };
/* 142 */         Reference localReference = new Reference("java.lang.Object", local1);
/*     */ 
/* 145 */         CompositeName localCompositeName = new CompositeName();
/* 146 */         localCompositeName.add(paramString);
/* 147 */         localCompositeName.add("");
/*     */ 
/* 154 */         paramContinuation.setContinue(localReference, localCompositeName, this);
/* 155 */         return null;
/*     */       }
/*     */ 
/* 158 */       return localObject;
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 162 */       localNamingException.appendRemainingComponent("");
/* 163 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object a_lookup_nns(String paramString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 186 */     a_processJunction_nns(paramString, paramContinuation);
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object a_lookupLink_nns(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 192 */     a_processJunction_nns(paramString, paramContinuation);
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_list_nns(Continuation paramContinuation) throws NamingException
/*     */   {
/* 198 */     a_processJunction_nns(paramContinuation);
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration a_listBindings_nns(Continuation paramContinuation) throws NamingException {
/* 203 */     a_processJunction_nns(paramContinuation);
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   protected void a_bind_nns(String paramString, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 209 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_rebind_nns(String paramString, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 214 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_unbind_nns(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 219 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Context a_createSubcontext_nns(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 224 */     a_processJunction_nns(paramString, paramContinuation);
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   protected void a_destroySubcontext_nns(String paramString, Continuation paramContinuation) throws NamingException
/*     */   {
/* 230 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_rename_nns(String paramString, Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 235 */     a_processJunction_nns(paramString, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NameParser a_getNameParser_nns(Continuation paramContinuation) throws NamingException
/*     */   {
/* 240 */     a_processJunction_nns(paramContinuation);
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean isEmpty(String paramString)
/*     */   {
/* 247 */     return (paramString == null) || (paramString.equals(""));
/*     */   }
/*     */ 
/*     */   protected Object c_lookup(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 257 */     Object localObject = null;
/* 258 */     if (resolve_to_penultimate_context(paramName, paramContinuation)) {
/* 259 */       localObject = a_lookup(paramName.toString(), paramContinuation);
/* 260 */       if ((localObject != null) && ((localObject instanceof LinkRef))) {
/* 261 */         paramContinuation.setContinue(localObject, paramName, this);
/* 262 */         localObject = null;
/*     */       }
/*     */     }
/* 265 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected Object c_lookupLink(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 270 */     if (resolve_to_penultimate_context(paramName, paramContinuation)) {
/* 271 */       return a_lookupLink(paramName.toString(), paramContinuation);
/*     */     }
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_list(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 278 */     if (resolve_to_context(paramName, paramContinuation)) {
/* 279 */       return a_list(paramContinuation);
/*     */     }
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_listBindings(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 286 */     if (resolve_to_context(paramName, paramContinuation)) {
/* 287 */       return a_listBindings(paramContinuation);
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_bind(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 294 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 295 */       a_bind(paramName.toString(), paramObject, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 300 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 301 */       a_rebind(paramName.toString(), paramObject, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_unbind(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 306 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 307 */       a_unbind(paramName.toString(), paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_destroySubcontext(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 312 */     if (resolve_to_penultimate_context(paramName, paramContinuation))
/* 313 */       a_destroySubcontext(paramName.toString(), paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Context c_createSubcontext(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 318 */     if (resolve_to_penultimate_context(paramName, paramContinuation)) {
/* 319 */       return a_createSubcontext(paramName.toString(), paramContinuation);
/*     */     }
/* 321 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_rename(Name paramName1, Name paramName2, Continuation paramContinuation) throws NamingException
/*     */   {
/* 326 */     if (resolve_to_penultimate_context(paramName1, paramContinuation))
/* 327 */       a_rename(paramName1.toString(), paramName2, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NameParser c_getNameParser(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 332 */     if (resolve_to_context(paramName, paramContinuation))
/* 333 */       return a_getNameParser(paramContinuation);
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object c_resolveIntermediate_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 348 */     if (this._contextType == 3) {
/* 349 */       Object localObject = null;
/* 350 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation)) {
/* 351 */         localObject = a_resolveIntermediate_nns(paramName.toString(), paramContinuation);
/* 352 */         if ((localObject != null) && ((localObject instanceof LinkRef))) {
/* 353 */           paramContinuation.setContinue(localObject, paramName, this);
/* 354 */           localObject = null;
/*     */         }
/*     */       }
/* 357 */       return localObject;
/*     */     }
/*     */ 
/* 360 */     return super.c_resolveIntermediate_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Object c_lookup_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 368 */     if (this._contextType == 3) {
/* 369 */       Object localObject = null;
/* 370 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation)) {
/* 371 */         localObject = a_lookup_nns(paramName.toString(), paramContinuation);
/* 372 */         if ((localObject != null) && ((localObject instanceof LinkRef))) {
/* 373 */           paramContinuation.setContinue(localObject, paramName, this);
/* 374 */           localObject = null;
/*     */         }
/*     */       }
/* 377 */       return localObject;
/*     */     }
/* 379 */     return super.c_lookup_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Object c_lookupLink_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 385 */     if (this._contextType == 3)
/*     */     {
/* 387 */       resolve_to_nns_and_continue(paramName, paramContinuation);
/* 388 */       return null;
/*     */     }
/*     */ 
/* 391 */     return super.c_lookupLink_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_list_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 397 */     if (this._contextType == 3) {
/* 398 */       resolve_to_nns_and_continue(paramName, paramContinuation);
/* 399 */       return null;
/*     */     }
/*     */ 
/* 402 */     return super.c_list_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_listBindings_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 408 */     if (this._contextType == 3) {
/* 409 */       resolve_to_nns_and_continue(paramName, paramContinuation);
/* 410 */       return null;
/*     */     }
/*     */ 
/* 413 */     return super.c_list_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_bind_nns(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 419 */     if (this._contextType == 3) {
/* 420 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 421 */         a_bind_nns(paramName.toString(), paramObject, paramContinuation);
/*     */     }
/*     */     else
/* 424 */       super.c_bind_nns(paramName, paramObject, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind_nns(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 430 */     if (this._contextType == 3) {
/* 431 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 432 */         a_rebind_nns(paramName.toString(), paramObject, paramContinuation);
/*     */     }
/*     */     else
/* 435 */       super.c_rebind_nns(paramName, paramObject, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_unbind_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 441 */     if (this._contextType == 3) {
/* 442 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 443 */         a_unbind_nns(paramName.toString(), paramContinuation);
/*     */     }
/*     */     else
/* 446 */       super.c_unbind_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Context c_createSubcontext_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 452 */     if (this._contextType == 3) {
/* 453 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation)) {
/* 454 */         return a_createSubcontext_nns(paramName.toString(), paramContinuation);
/*     */       }
/* 456 */       return null;
/*     */     }
/*     */ 
/* 459 */     return super.c_createSubcontext_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_destroySubcontext_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 465 */     if (this._contextType == 3) {
/* 466 */       if (resolve_to_penultimate_context_nns(paramName, paramContinuation))
/* 467 */         a_destroySubcontext_nns(paramName.toString(), paramContinuation);
/*     */     }
/*     */     else
/* 470 */       super.c_destroySubcontext_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rename_nns(Name paramName1, Name paramName2, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 476 */     if (this._contextType == 3) {
/* 477 */       if (resolve_to_penultimate_context_nns(paramName1, paramContinuation))
/* 478 */         a_rename_nns(paramName1.toString(), paramName2, paramContinuation);
/*     */     }
/*     */     else
/* 481 */       super.c_rename_nns(paramName1, paramName2, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NameParser c_getNameParser_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 487 */     if (this._contextType == 3) {
/* 488 */       resolve_to_nns_and_continue(paramName, paramContinuation);
/* 489 */       return null;
/*     */     }
/*     */ 
/* 492 */     return super.c_getNameParser_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void a_processJunction_nns(String paramString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     Object localObject;
/* 514 */     if (paramString.equals("")) {
/* 515 */       localObject = new NameNotFoundException();
/* 516 */       paramContinuation.setErrorNNS(this, paramString);
/* 517 */       throw paramContinuation.fillInException((NamingException)localObject);
/*     */     }
/*     */     try
/*     */     {
/* 521 */       localObject = a_lookup(paramString, paramContinuation);
/* 522 */       if (paramContinuation.isContinue())
/* 523 */         paramContinuation.appendRemainingComponent("");
/*     */       else
/* 525 */         paramContinuation.setContinueNNS(localObject, paramString, this);
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 528 */       localNamingException.appendRemainingComponent("");
/* 529 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void a_processJunction_nns(Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 544 */     RefAddr local2 = new RefAddr("nns") { private static final long serialVersionUID = 3449785852664978312L;
/*     */ 
/* 546 */       public Object getContent() { return AtomicContext.this; }
/*     */ 
/*     */     };
/* 550 */     Reference localReference = new Reference("java.lang.Object", local2);
/*     */ 
/* 556 */     paramContinuation.setContinue(localReference, _NNS_NAME, this);
/*     */   }
/*     */ 
/*     */   protected boolean resolve_to_context(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 568 */     String str1 = paramName.toString();
/*     */ 
/* 571 */     StringHeadTail localStringHeadTail = c_parseComponent(str1, paramContinuation);
/* 572 */     String str2 = localStringHeadTail.getTail();
/* 573 */     String str3 = localStringHeadTail.getHead();
/*     */ 
/* 575 */     if (debug > 0)
/* 576 */       System.out.println("RESOLVE TO CONTEXT(" + str1 + ") = {" + str3 + ", " + str2 + "}");
/*     */     Object localObject;
/* 579 */     if (str3 == null)
/*     */     {
/* 581 */       localObject = new InvalidNameException();
/* 582 */       throw paramContinuation.fillInException((NamingException)localObject);
/*     */     }
/* 584 */     if (!isEmpty(str3))
/*     */     {
/*     */       try
/*     */       {
/* 588 */         localObject = a_lookup(str3, paramContinuation);
/*     */ 
/* 590 */         if (localObject != null)
/* 591 */           paramContinuation.setContinue(localObject, str3, this, str2 == null ? "" : str2);
/* 592 */         else if (paramContinuation.isContinue())
/* 593 */           paramContinuation.appendRemainingComponent(str2);
/*     */       } catch (NamingException localNamingException) {
/* 595 */         localNamingException.appendRemainingComponent(str2);
/* 596 */         throw localNamingException;
/*     */       }
/*     */     } else {
/* 599 */       paramContinuation.setSuccess();
/* 600 */       return true;
/*     */     }
/* 602 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean resolve_to_penultimate_context(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 614 */     String str1 = paramName.toString();
/*     */ 
/* 616 */     if (debug > 0) {
/* 617 */       System.out.println("RESOLVE TO PENULTIMATE" + str1);
/*     */     }
/* 619 */     StringHeadTail localStringHeadTail = c_parseComponent(str1, paramContinuation);
/* 620 */     String str2 = localStringHeadTail.getTail();
/* 621 */     String str3 = localStringHeadTail.getHead();
/*     */     Object localObject;
/* 622 */     if (str3 == null)
/*     */     {
/* 624 */       localObject = new InvalidNameException();
/* 625 */       throw paramContinuation.fillInException((NamingException)localObject);
/*     */     }
/*     */ 
/* 628 */     if (!isEmpty(str2))
/*     */     {
/*     */       try {
/* 631 */         localObject = a_lookup(str3, paramContinuation);
/* 632 */         if (localObject != null)
/* 633 */           paramContinuation.setContinue(localObject, str3, this, str2);
/* 634 */         else if (paramContinuation.isContinue())
/* 635 */           paramContinuation.appendRemainingComponent(str2);
/*     */       } catch (NamingException localNamingException) {
/* 637 */         localNamingException.appendRemainingComponent(str2);
/* 638 */         throw localNamingException;
/*     */       }
/*     */     }
/*     */     else {
/* 642 */       paramContinuation.setSuccess();
/* 643 */       return true;
/*     */     }
/* 645 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean resolve_to_penultimate_context_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 658 */       if (debug > 0)
/* 659 */         System.out.println("RESOLVE TO PENULTIMATE NNS" + paramName.toString());
/* 660 */       boolean bool = resolve_to_penultimate_context(paramName, paramContinuation);
/*     */ 
/* 665 */       if (paramContinuation.isContinue()) {
/* 666 */         paramContinuation.appendRemainingComponent("");
/*     */       }
/* 668 */       return bool;
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 673 */       localNamingException.appendRemainingComponent("");
/* 674 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void resolve_to_nns_and_continue(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 684 */     if (debug > 0) {
/* 685 */       System.out.println("RESOLVE TO NNS AND CONTINUE" + paramName.toString());
/*     */     }
/* 687 */     if (resolve_to_penultimate_context_nns(paramName, paramContinuation)) {
/* 688 */       Object localObject = a_lookup_nns(paramName.toString(), paramContinuation);
/* 689 */       if (localObject != null)
/* 690 */         paramContinuation.setContinue(localObject, paramName, this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.AtomicContext
 * JD-Core Version:    0.6.2
 */