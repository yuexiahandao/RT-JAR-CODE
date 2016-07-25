/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.LinkRef;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public abstract class ComponentContext extends PartialCompositeContext
/*     */ {
/*  46 */   private static int debug = 0;
/*     */   protected static final byte USE_CONTINUATION = 1;
/*     */   protected static final byte TERMINAL_COMPONENT = 2;
/*     */   protected static final byte TERMINAL_NNS_COMPONENT = 3;
/*     */ 
/*     */   protected ComponentContext()
/*     */   {
/*  49 */     this._contextType = 2;
/*     */   }
/*     */ 
/*     */   protected abstract Object c_lookup(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Object c_lookupLink(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration c_list(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NamingEnumeration c_listBindings(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_bind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_rebind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_unbind(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_destroySubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract Context c_createSubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract void c_rename(Name paramName1, Name paramName2, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected abstract NameParser c_getNameParser(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException;
/*     */ 
/*     */   protected HeadTail p_parseComponent(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     int i;
/* 110 */     if ((paramName.isEmpty()) || (paramName.get(0).equals("")))
/* 111 */       i = 0;
/*     */     else
/* 113 */       i = 1;
/*     */     Name localName1;
/*     */     Name localName2;
/* 117 */     if ((paramName instanceof CompositeName)) {
/* 118 */       localName1 = paramName.getPrefix(i);
/* 119 */       localName2 = paramName.getSuffix(i);
/*     */     }
/*     */     else {
/* 122 */       localName1 = new CompositeName().add(paramName.toString());
/* 123 */       localName2 = null;
/*     */     }
/*     */ 
/* 126 */     if (debug > 2) {
/* 127 */       System.err.println("ORIG: " + paramName);
/* 128 */       System.err.println("PREFIX: " + paramName);
/* 129 */       System.err.println("SUFFIX: " + null);
/*     */     }
/* 131 */     return new HeadTail(localName1, localName2);
/*     */   }
/*     */ 
/*     */   protected Object c_resolveIntermediate_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 170 */       final Object localObject = c_lookup(paramName, paramContinuation);
/*     */ 
/* 175 */       if ((localObject != null) && (getClass().isInstance(localObject)))
/*     */       {
/* 179 */         paramContinuation.setContinueNNS(localObject, paramName, this);
/* 180 */         return null;
/*     */       }
/* 182 */       if ((localObject != null) && (!(localObject instanceof Context)))
/*     */       {
/* 185 */         RefAddr local1 = new RefAddr("nns") { private static final long serialVersionUID = -8831204798861786362L;
/*     */ 
/* 187 */           public Object getContent() { return localObject; }
/*     */ 
/*     */         };
/* 192 */         Reference localReference = new Reference("java.lang.Object", local1);
/*     */ 
/* 195 */         CompositeName localCompositeName = (CompositeName)paramName.clone();
/* 196 */         localCompositeName.add("");
/*     */ 
/* 203 */         paramContinuation.setContinue(localReference, localCompositeName, this);
/* 204 */         return null;
/*     */       }
/*     */ 
/* 207 */       return localObject;
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/* 211 */       localNamingException.appendRemainingComponent("");
/* 212 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object c_lookup_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 230 */     c_processJunction_nns(paramName, paramContinuation);
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object c_lookupLink_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 236 */     c_processJunction_nns(paramName, paramContinuation);
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_list_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 242 */     c_processJunction_nns(paramName, paramContinuation);
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration c_listBindings_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 248 */     c_processJunction_nns(paramName, paramContinuation);
/* 249 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_bind_nns(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 254 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rebind_nns(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 259 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_unbind_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 264 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected Context c_createSubcontext_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 269 */     c_processJunction_nns(paramName, paramContinuation);
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_destroySubcontext_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 275 */     c_processJunction_nns(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected void c_rename_nns(Name paramName1, Name paramName2, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 281 */     c_processJunction_nns(paramName1, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NameParser c_getNameParser_nns(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 286 */     c_processJunction_nns(paramName, paramContinuation);
/* 287 */     return null;
/*     */   }
/*     */ 
/*     */   protected void c_processJunction_nns(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*     */     Object localObject;
/* 323 */     if (paramName.isEmpty())
/*     */     {
/* 325 */       localObject = new RefAddr("nns") { private static final long serialVersionUID = -1389472957988053402L;
/*     */ 
/* 327 */         public Object getContent() { return ComponentContext.this; }
/*     */ 
/*     */       };
/* 332 */       Reference localReference = new Reference("java.lang.Object", (RefAddr)localObject);
/*     */ 
/* 338 */       paramContinuation.setContinue(localReference, _NNS_NAME, this);
/* 339 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 344 */       localObject = c_lookup(paramName, paramContinuation);
/* 345 */       if (paramContinuation.isContinue())
/* 346 */         paramContinuation.appendRemainingComponent("");
/*     */       else
/* 348 */         paramContinuation.setContinueNNS(localObject, paramName, this);
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 351 */       localNamingException.appendRemainingComponent("");
/* 352 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected HeadTail p_resolveIntermediate(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 375 */     int i = 1;
/* 376 */     paramContinuation.setSuccess();
/* 377 */     HeadTail localHeadTail = p_parseComponent(paramName, paramContinuation);
/* 378 */     Name localName1 = localHeadTail.getTail();
/* 379 */     Name localName2 = localHeadTail.getHead();
/*     */ 
/* 381 */     if ((localName1 == null) || (localName1.isEmpty()))
/*     */     {
/* 383 */       i = 2;
/* 384 */     } else if (!localName1.get(0).equals(""))
/*     */     {
/*     */       try
/*     */       {
/* 399 */         Object localObject1 = c_resolveIntermediate_nns(localName2, paramContinuation);
/*     */ 
/* 401 */         if (localObject1 != null) {
/* 402 */           paramContinuation.setContinue(localObject1, localName2, this, localName1);
/* 403 */         } else if (paramContinuation.isContinue()) {
/* 404 */           checkAndAdjustRemainingName(paramContinuation.getRemainingName());
/* 405 */           paramContinuation.appendRemainingName(localName1);
/*     */         }
/*     */       } catch (NamingException localNamingException1) {
/* 408 */         checkAndAdjustRemainingName(localNamingException1.getRemainingName());
/* 409 */         localNamingException1.appendRemainingName(localName1);
/* 410 */         throw localNamingException1;
/*     */       }
/*     */ 
/*     */     }
/* 417 */     else if (localName1.size() == 1) {
/* 418 */       i = 3;
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject2;
/* 420 */       if ((localName2.isEmpty()) || (isAllEmpty(localName1)))
/*     */       {
/* 422 */         localObject2 = localName1.getSuffix(1);
/*     */         try {
/* 424 */           Object localObject3 = c_lookup_nns(localName2, paramContinuation);
/*     */ 
/* 426 */           if (localObject3 != null)
/* 427 */             paramContinuation.setContinue(localObject3, localName2, this, (Name)localObject2);
/* 428 */           else if (paramContinuation.isContinue()) {
/* 429 */             paramContinuation.appendRemainingName((Name)localObject2);
/*     */           }
/*     */         }
/*     */         catch (NamingException localNamingException3)
/*     */         {
/* 434 */           localNamingException3.appendRemainingName((Name)localObject2);
/* 435 */           throw localNamingException3;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         try {
/* 441 */           localObject2 = c_resolveIntermediate_nns(localName2, paramContinuation);
/*     */ 
/* 443 */           if (localObject2 != null) {
/* 444 */             paramContinuation.setContinue(localObject2, localName2, this, localName1);
/* 445 */           } else if (paramContinuation.isContinue()) {
/* 446 */             checkAndAdjustRemainingName(paramContinuation.getRemainingName());
/* 447 */             paramContinuation.appendRemainingName(localName1);
/*     */           }
/*     */         } catch (NamingException localNamingException2) {
/* 450 */           checkAndAdjustRemainingName(localNamingException2.getRemainingName());
/* 451 */           localNamingException2.appendRemainingName(localName1);
/* 452 */           throw localNamingException2;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 457 */     localHeadTail.setStatus(i);
/* 458 */     return localHeadTail;
/*     */   }
/*     */ 
/*     */   void checkAndAdjustRemainingName(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/*     */     int i;
/* 472 */     if ((paramName != null) && ((i = paramName.size()) > 1) && (paramName.get(i - 1).equals("")))
/*     */     {
/* 474 */       paramName.remove(i - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isAllEmpty(Name paramName)
/*     */   {
/* 480 */     int i = paramName.size();
/* 481 */     for (int j = 0; j < i; j++) {
/* 482 */       if (!paramName.get(j).equals("")) {
/* 483 */         return false;
/*     */       }
/*     */     }
/* 486 */     return true;
/*     */   }
/*     */ 
/*     */   protected ResolveResult p_resolveToClass(Name paramName, Class paramClass, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 502 */     if (paramClass.isInstance(this)) {
/* 503 */       paramContinuation.setSuccess();
/* 504 */       return new ResolveResult(this, paramName);
/*     */     }
/*     */ 
/* 507 */     ResolveResult localResolveResult = null;
/* 508 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 509 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 511 */       Object localObject = p_lookup(paramName, paramContinuation);
/* 512 */       if ((!paramContinuation.isContinue()) && (paramClass.isInstance(localObject)))
/* 513 */         localResolveResult = new ResolveResult(localObject, _EMPTY_NAME); break;
/*     */     case 2:
/* 518 */       paramContinuation.setSuccess();
/* 519 */       break;
/*     */     }
/*     */ 
/* 526 */     return localResolveResult;
/*     */   }
/*     */ 
/*     */   protected Object p_lookup(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 532 */     Object localObject = null;
/* 533 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 534 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 536 */       localObject = c_lookup_nns(localHeadTail.getHead(), paramContinuation);
/* 537 */       if ((localObject instanceof LinkRef)) {
/* 538 */         paramContinuation.setContinue(localObject, localHeadTail.getHead(), this);
/* 539 */         localObject = null; } break;
/*     */     case 2:
/* 544 */       localObject = c_lookup(localHeadTail.getHead(), paramContinuation);
/* 545 */       if ((localObject instanceof LinkRef)) {
/* 546 */         paramContinuation.setContinue(localObject, localHeadTail.getHead(), this);
/* 547 */         localObject = null; } break;
/*     */     }
/*     */ 
/* 556 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration p_list(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 561 */     NamingEnumeration localNamingEnumeration = null;
/* 562 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 563 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 565 */       if (debug > 0)
/* 566 */         System.out.println("c_list_nns(" + localHeadTail.getHead() + ")");
/* 567 */       localNamingEnumeration = c_list_nns(localHeadTail.getHead(), paramContinuation);
/* 568 */       break;
/*     */     case 2:
/* 571 */       if (debug > 0)
/* 572 */         System.out.println("c_list(" + localHeadTail.getHead() + ")");
/* 573 */       localNamingEnumeration = c_list(localHeadTail.getHead(), paramContinuation);
/* 574 */       break;
/*     */     }
/*     */ 
/* 581 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration p_listBindings(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 586 */     NamingEnumeration localNamingEnumeration = null;
/* 587 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 588 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 590 */       localNamingEnumeration = c_listBindings_nns(localHeadTail.getHead(), paramContinuation);
/* 591 */       break;
/*     */     case 2:
/* 594 */       localNamingEnumeration = c_listBindings(localHeadTail.getHead(), paramContinuation);
/* 595 */       break;
/*     */     }
/*     */ 
/* 602 */     return localNamingEnumeration;
/*     */   }
/*     */ 
/*     */   protected void p_bind(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 607 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 608 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 610 */       c_bind_nns(localHeadTail.getHead(), paramObject, paramContinuation);
/* 611 */       break;
/*     */     case 2:
/* 614 */       c_bind(localHeadTail.getHead(), paramObject, paramContinuation);
/* 615 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_rebind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 626 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 627 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 629 */       c_rebind_nns(localHeadTail.getHead(), paramObject, paramContinuation);
/* 630 */       break;
/*     */     case 2:
/* 633 */       c_rebind(localHeadTail.getHead(), paramObject, paramContinuation);
/* 634 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_unbind(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 645 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 646 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 648 */       c_unbind_nns(localHeadTail.getHead(), paramContinuation);
/* 649 */       break;
/*     */     case 2:
/* 652 */       c_unbind(localHeadTail.getHead(), paramContinuation);
/* 653 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void p_destroySubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 664 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 665 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 667 */       c_destroySubcontext_nns(localHeadTail.getHead(), paramContinuation);
/* 668 */       break;
/*     */     case 2:
/* 671 */       c_destroySubcontext(localHeadTail.getHead(), paramContinuation);
/* 672 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Context p_createSubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 683 */     Context localContext = null;
/* 684 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 685 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 687 */       localContext = c_createSubcontext_nns(localHeadTail.getHead(), paramContinuation);
/* 688 */       break;
/*     */     case 2:
/* 691 */       localContext = c_createSubcontext(localHeadTail.getHead(), paramContinuation);
/* 692 */       break;
/*     */     }
/*     */ 
/* 699 */     return localContext;
/*     */   }
/*     */ 
/*     */   protected void p_rename(Name paramName1, Name paramName2, Continuation paramContinuation) throws NamingException
/*     */   {
/* 704 */     HeadTail localHeadTail = p_resolveIntermediate(paramName1, paramContinuation);
/* 705 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 707 */       c_rename_nns(localHeadTail.getHead(), paramName2, paramContinuation);
/* 708 */       break;
/*     */     case 2:
/* 711 */       c_rename(localHeadTail.getHead(), paramName2, paramContinuation);
/* 712 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected NameParser p_getNameParser(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 723 */     NameParser localNameParser = null;
/* 724 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 725 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 727 */       localNameParser = c_getNameParser_nns(localHeadTail.getHead(), paramContinuation);
/* 728 */       break;
/*     */     case 2:
/* 731 */       localNameParser = c_getNameParser(localHeadTail.getHead(), paramContinuation);
/* 732 */       break;
/*     */     }
/*     */ 
/* 739 */     return localNameParser;
/*     */   }
/*     */ 
/*     */   protected Object p_lookupLink(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 744 */     Object localObject = null;
/* 745 */     HeadTail localHeadTail = p_resolveIntermediate(paramName, paramContinuation);
/* 746 */     switch (localHeadTail.getStatus()) {
/*     */     case 3:
/* 748 */       localObject = c_lookupLink_nns(localHeadTail.getHead(), paramContinuation);
/* 749 */       break;
/*     */     case 2:
/* 752 */       localObject = c_lookupLink(localHeadTail.getHead(), paramContinuation);
/* 753 */       break;
/*     */     }
/*     */ 
/* 760 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.ComponentContext
 * JD-Core Version:    0.6.2
 */