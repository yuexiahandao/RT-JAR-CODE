/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameAlreadyBoundException;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.AttributeModificationException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SchemaViolationException;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.spi.DirStateFactory.Result;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ public class HierMemDirCtx
/*     */   implements DirContext
/*     */ {
/*     */   private static final boolean debug = false;
/*  43 */   private static final NameParser defaultParser = new HierarchicalNameParser();
/*     */   protected Hashtable myEnv;
/*     */   protected Hashtable bindings;
/*     */   protected Attributes attrs;
/*  48 */   protected boolean ignoreCase = false;
/*  49 */   protected NamingException readOnlyEx = null;
/*  50 */   protected NameParser myParser = defaultParser;
/*     */   private boolean alwaysUseFactory;
/*     */ 
/*     */   public void close()
/*     */     throws NamingException
/*     */   {
/*  55 */     this.myEnv = null;
/*  56 */     this.bindings = null;
/*  57 */     this.attrs = null;
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace() throws NamingException {
/*  61 */     throw new OperationNotSupportedException("Cannot determine full name");
/*     */   }
/*     */ 
/*     */   public HierMemDirCtx()
/*     */   {
/*  66 */     this(null, false, false);
/*     */   }
/*     */ 
/*     */   public HierMemDirCtx(boolean paramBoolean) {
/*  70 */     this(null, paramBoolean, false);
/*     */   }
/*     */ 
/*     */   public HierMemDirCtx(Hashtable paramHashtable, boolean paramBoolean) {
/*  74 */     this(paramHashtable, paramBoolean, false);
/*     */   }
/*     */ 
/*     */   protected HierMemDirCtx(Hashtable paramHashtable, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  79 */     this.myEnv = paramHashtable;
/*  80 */     this.ignoreCase = paramBoolean1;
/*  81 */     init();
/*  82 */     this.alwaysUseFactory = paramBoolean2;
/*     */   }
/*     */ 
/*     */   private void init() {
/*  86 */     this.attrs = new BasicAttributes(this.ignoreCase);
/*  87 */     this.bindings = new Hashtable(11, 0.75F);
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString) throws NamingException {
/*  91 */     return lookup(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/*  95 */     return doLookup(paramName, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   public Object doLookup(Name paramName, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 101 */     Object localObject = null;
/* 102 */     paramName = canonizeName(paramName);
/*     */ 
/* 104 */     switch (paramName.size())
/*     */     {
/*     */     case 0:
/* 107 */       localObject = this;
/* 108 */       break;
/*     */     case 1:
/* 112 */       localObject = this.bindings.get(paramName);
/* 113 */       break;
/*     */     default:
/* 117 */       HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)this.bindings.get(paramName.getPrefix(1));
/* 118 */       if (localHierMemDirCtx == null)
/* 119 */         localObject = null;
/*     */       else {
/* 121 */         localObject = localHierMemDirCtx.doLookup(paramName.getSuffix(1), false);
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 126 */     if (localObject == null) {
/* 127 */       throw new NameNotFoundException(paramName.toString());
/*     */     }
/*     */ 
/* 130 */     if (paramBoolean) {
/*     */       try {
/* 132 */         return DirectoryManager.getObjectInstance(localObject, paramName, this, this.myEnv, (localObject instanceof HierMemDirCtx) ? ((HierMemDirCtx)localObject).attrs : null);
/*     */       }
/*     */       catch (NamingException localNamingException1)
/*     */       {
/* 137 */         throw localNamingException1;
/*     */       } catch (Exception localException) {
/* 139 */         NamingException localNamingException2 = new NamingException("Problem calling getObjectInstance");
/*     */ 
/* 141 */         localNamingException2.setRootCause(localException);
/* 142 */         throw localNamingException2;
/*     */       }
/*     */     }
/* 145 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 150 */     bind(this.myParser.parse(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException {
/* 154 */     doBind(paramName, paramObject, null, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 159 */     bind(this.myParser.parse(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 164 */     doBind(paramName, paramObject, paramAttributes, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   protected void doBind(Name paramName, Object paramObject, Attributes paramAttributes, boolean paramBoolean) throws NamingException
/*     */   {
/* 169 */     if (paramName.isEmpty()) {
/* 170 */       throw new InvalidNameException("Cannot bind empty name");
/*     */     }
/*     */ 
/* 173 */     if (paramBoolean) {
/* 174 */       localObject = DirectoryManager.getStateToBind(paramObject, paramName, this, this.myEnv, paramAttributes);
/*     */ 
/* 176 */       paramObject = ((DirStateFactory.Result)localObject).getObject();
/* 177 */       paramAttributes = ((DirStateFactory.Result)localObject).getAttributes();
/*     */     }
/*     */ 
/* 180 */     Object localObject = (HierMemDirCtx)doLookup(getInternalName(paramName), false);
/* 181 */     ((HierMemDirCtx)localObject).doBindAux(getLeafName(paramName), paramObject);
/*     */ 
/* 183 */     if ((paramAttributes != null) && (paramAttributes.size() > 0))
/* 184 */       modifyAttributes(paramName, 1, paramAttributes);
/*     */   }
/*     */ 
/*     */   protected void doBindAux(Name paramName, Object paramObject) throws NamingException
/*     */   {
/* 189 */     if (this.readOnlyEx != null) {
/* 190 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/*     */ 
/* 193 */     if (this.bindings.get(paramName) != null) {
/* 194 */       throw new NameAlreadyBoundException(paramName.toString());
/*     */     }
/* 196 */     if ((paramObject instanceof HierMemDirCtx))
/* 197 */       this.bindings.put(paramName, paramObject);
/*     */     else
/* 199 */       throw new SchemaViolationException("This context only supports binding objects of it's own kind");
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 205 */     rebind(this.myParser.parse(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 209 */     doRebind(paramName, paramObject, null, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 214 */     rebind(this.myParser.parse(paramString), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 219 */     doRebind(paramName, paramObject, paramAttributes, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   protected void doRebind(Name paramName, Object paramObject, Attributes paramAttributes, boolean paramBoolean) throws NamingException
/*     */   {
/* 224 */     if (paramName.isEmpty()) {
/* 225 */       throw new InvalidNameException("Cannot rebind empty name");
/*     */     }
/*     */ 
/* 228 */     if (paramBoolean) {
/* 229 */       localObject = DirectoryManager.getStateToBind(paramObject, paramName, this, this.myEnv, paramAttributes);
/*     */ 
/* 231 */       paramObject = ((DirStateFactory.Result)localObject).getObject();
/* 232 */       paramAttributes = ((DirStateFactory.Result)localObject).getAttributes();
/*     */     }
/*     */ 
/* 235 */     Object localObject = (HierMemDirCtx)doLookup(getInternalName(paramName), false);
/* 236 */     ((HierMemDirCtx)localObject).doRebindAux(getLeafName(paramName), paramObject);
/*     */ 
/* 246 */     if ((paramAttributes != null) && (paramAttributes.size() > 0))
/* 247 */       modifyAttributes(paramName, 1, paramAttributes);
/*     */   }
/*     */ 
/*     */   protected void doRebindAux(Name paramName, Object paramObject) throws NamingException
/*     */   {
/* 252 */     if (this.readOnlyEx != null) {
/* 253 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/* 255 */     if ((paramObject instanceof HierMemDirCtx)) {
/* 256 */       this.bindings.put(paramName, paramObject);
/*     */     }
/*     */     else
/* 259 */       throw new SchemaViolationException("This context only supports binding objects of it's own kind");
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString)
/*     */     throws NamingException
/*     */   {
/* 265 */     unbind(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/* 269 */     if (paramName.isEmpty()) {
/* 270 */       throw new InvalidNameException("Cannot unbind empty name");
/*     */     }
/* 272 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(getInternalName(paramName), false);
/*     */ 
/* 274 */     localHierMemDirCtx.doUnbind(getLeafName(paramName));
/*     */   }
/*     */ 
/*     */   protected void doUnbind(Name paramName) throws NamingException
/*     */   {
/* 279 */     if (this.readOnlyEx != null) {
/* 280 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/*     */ 
/* 283 */     this.bindings.remove(paramName);
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException
/*     */   {
/* 288 */     rename(this.myParser.parse(paramString1), this.myParser.parse(paramString2));
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 294 */     if ((paramName2.isEmpty()) || (paramName1.isEmpty())) {
/* 295 */       throw new InvalidNameException("Cannot rename empty name");
/*     */     }
/*     */ 
/* 298 */     if (!getInternalName(paramName2).equals(getInternalName(paramName1))) {
/* 299 */       throw new InvalidNameException("Cannot rename across contexts");
/*     */     }
/*     */ 
/* 302 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(getInternalName(paramName2), false);
/*     */ 
/* 304 */     localHierMemDirCtx.doRename(getLeafName(paramName1), getLeafName(paramName2));
/*     */   }
/*     */ 
/*     */   protected void doRename(Name paramName1, Name paramName2) throws NamingException {
/* 308 */     if (this.readOnlyEx != null) {
/* 309 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/*     */ 
/* 312 */     paramName1 = canonizeName(paramName1);
/* 313 */     paramName2 = canonizeName(paramName2);
/*     */ 
/* 316 */     if (this.bindings.get(paramName2) != null) {
/* 317 */       throw new NameAlreadyBoundException(paramName2.toString());
/*     */     }
/*     */ 
/* 321 */     Object localObject = this.bindings.remove(paramName1);
/* 322 */     if (localObject == null) {
/* 323 */       throw new NameNotFoundException(paramName1.toString());
/*     */     }
/*     */ 
/* 326 */     this.bindings.put(paramName2, localObject);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(String paramString) throws NamingException {
/* 330 */     return list(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(Name paramName) throws NamingException {
/* 334 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/* 335 */     return localHierMemDirCtx.doList();
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration doList() throws NamingException {
/* 339 */     return new FlatNames(this.bindings.keys());
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(String paramString) throws NamingException
/*     */   {
/* 344 */     return listBindings(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(Name paramName) throws NamingException {
/* 348 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/* 349 */     return localHierMemDirCtx.doListBindings(this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration doListBindings(boolean paramBoolean) throws NamingException
/*     */   {
/* 354 */     return new FlatBindings(this.bindings, this.myEnv, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException {
/* 358 */     destroySubcontext(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 362 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(getInternalName(paramName), false);
/*     */ 
/* 364 */     localHierMemDirCtx.doDestroySubcontext(getLeafName(paramName));
/*     */   }
/*     */ 
/*     */   protected void doDestroySubcontext(Name paramName) throws NamingException
/*     */   {
/* 369 */     if (this.readOnlyEx != null) {
/* 370 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/* 372 */     paramName = canonizeName(paramName);
/* 373 */     this.bindings.remove(paramName);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 377 */     return createSubcontext(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 381 */     return createSubcontext(paramName, null);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 386 */     return createSubcontext(this.myParser.parse(paramString), paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 391 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(getInternalName(paramName), false);
/*     */ 
/* 393 */     return localHierMemDirCtx.doCreateSubcontext(getLeafName(paramName), paramAttributes);
/*     */   }
/*     */ 
/*     */   protected DirContext doCreateSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 398 */     if (this.readOnlyEx != null) {
/* 399 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/*     */ 
/* 402 */     paramName = canonizeName(paramName);
/*     */ 
/* 404 */     if (this.bindings.get(paramName) != null) {
/* 405 */       throw new NameAlreadyBoundException(paramName.toString());
/*     */     }
/* 407 */     HierMemDirCtx localHierMemDirCtx = createNewCtx();
/* 408 */     this.bindings.put(paramName, localHierMemDirCtx);
/* 409 */     if (paramAttributes != null) {
/* 410 */       localHierMemDirCtx.modifyAttributes("", 1, paramAttributes);
/*     */     }
/* 412 */     return localHierMemDirCtx;
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString)
/*     */     throws NamingException
/*     */   {
/* 418 */     return lookupLink(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException
/*     */   {
/* 423 */     return lookup(paramName);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException {
/* 427 */     return this.myParser;
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 431 */     return this.myParser;
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2) throws NamingException
/*     */   {
/* 436 */     Name localName = composeName(new CompositeName(paramString1), new CompositeName(paramString2));
/*     */ 
/* 438 */     return localName.toString();
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 443 */     paramName1 = canonizeName(paramName1);
/* 444 */     paramName2 = canonizeName(paramName2);
/* 445 */     Name localName = (Name)paramName2.clone();
/* 446 */     localName.addAll(paramName1);
/* 447 */     return localName;
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 452 */     this.myEnv = (this.myEnv == null ? new Hashtable(11, 0.75F) : (Hashtable)this.myEnv.clone());
/*     */ 
/* 455 */     return this.myEnv.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString) throws NamingException
/*     */   {
/* 460 */     if (this.myEnv == null) {
/* 461 */       return null;
/*     */     }
/* 463 */     this.myEnv = ((Hashtable)this.myEnv.clone());
/* 464 */     return this.myEnv.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 468 */     if (this.myEnv == null) {
/* 469 */       return new Hashtable(5, 0.75F);
/*     */     }
/* 471 */     return (Hashtable)this.myEnv.clone();
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString)
/*     */     throws NamingException
/*     */   {
/* 477 */     return getAttributes(this.myParser.parse(paramString));
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException
/*     */   {
/* 482 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/* 483 */     return localHierMemDirCtx.doGetAttributes();
/*     */   }
/*     */ 
/*     */   protected Attributes doGetAttributes() throws NamingException {
/* 487 */     return (Attributes)this.attrs.clone();
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 492 */     return getAttributes(this.myParser.parse(paramString), paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 497 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/* 498 */     return localHierMemDirCtx.doGetAttributes(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   protected Attributes doGetAttributes(String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 504 */     if (paramArrayOfString == null) {
/* 505 */       return doGetAttributes();
/*     */     }
/* 507 */     BasicAttributes localBasicAttributes = new BasicAttributes(this.ignoreCase);
/* 508 */     Attribute localAttribute = null;
/* 509 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 510 */       localAttribute = this.attrs.get(paramArrayOfString[i]);
/* 511 */       if (localAttribute != null) {
/* 512 */         localBasicAttributes.put(localAttribute);
/*     */       }
/*     */     }
/* 515 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 520 */     modifyAttributes(this.myParser.parse(paramString), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 526 */     if ((paramAttributes == null) || (paramAttributes.size() == 0)) {
/* 527 */       throw new IllegalArgumentException("Cannot modify without an attribute");
/*     */     }
/*     */ 
/* 532 */     NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 533 */     ModificationItem[] arrayOfModificationItem = new ModificationItem[paramAttributes.size()];
/* 534 */     for (int i = 0; (i < arrayOfModificationItem.length) && (localNamingEnumeration.hasMoreElements()); i++) {
/* 535 */       arrayOfModificationItem[i] = new ModificationItem(paramInt, (Attribute)localNamingEnumeration.next());
/*     */     }
/*     */ 
/* 538 */     modifyAttributes(paramName, arrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 543 */     modifyAttributes(this.myParser.parse(paramString), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 548 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/* 549 */     localHierMemDirCtx.doModifyAttributes(paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   protected void doModifyAttributes(ModificationItem[] paramArrayOfModificationItem)
/*     */     throws NamingException
/*     */   {
/* 555 */     if (this.readOnlyEx != null) {
/* 556 */       throw ((NamingException)this.readOnlyEx.fillInStackTrace());
/*     */     }
/*     */ 
/* 559 */     applyMods(paramArrayOfModificationItem, this.attrs);
/*     */   }
/*     */ 
/*     */   protected static Attributes applyMods(ModificationItem[] paramArrayOfModificationItem, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 569 */     for (int i = 0; i < paramArrayOfModificationItem.length; i++) {
/* 570 */       ModificationItem localModificationItem = paramArrayOfModificationItem[i];
/* 571 */       Attribute localAttribute2 = localModificationItem.getAttribute();
/*     */       Attribute localAttribute1;
/*     */       NamingEnumeration localNamingEnumeration;
/* 573 */       switch (localModificationItem.getModificationOp())
/*     */       {
/*     */       case 1:
/* 579 */         localAttribute1 = paramAttributes.get(localAttribute2.getID());
/* 580 */         if (localAttribute1 == null) {
/* 581 */           paramAttributes.put((Attribute)localAttribute2.clone());
/*     */         }
/*     */         else
/* 584 */           localNamingEnumeration = localAttribute2.getAll(); break;
/*     */       case 2:
/*     */       case 3:
/*     */       default:
/* 585 */         while (localNamingEnumeration.hasMore()) {
/* 586 */           localAttribute1.add(localNamingEnumeration.next()); continue;
/*     */ 
/* 591 */           if (localAttribute2.size() == 0) {
/* 592 */             paramAttributes.remove(localAttribute2.getID());
/*     */           } else {
/* 594 */             paramAttributes.put((Attribute)localAttribute2.clone());
/*     */ 
/* 596 */             break;
/*     */ 
/* 598 */             localAttribute1 = paramAttributes.get(localAttribute2.getID());
/* 599 */             if (localAttribute1 != null)
/* 600 */               if (localAttribute2.size() == 0) {
/* 601 */                 paramAttributes.remove(localAttribute2.getID());
/*     */               }
/*     */               else {
/* 604 */                 localNamingEnumeration = localAttribute2.getAll();
/* 605 */                 while (localNamingEnumeration.hasMore()) {
/* 606 */                   localAttribute1.remove(localNamingEnumeration.next());
/*     */                 }
/* 608 */                 if (localAttribute1.size() == 0) {
/* 609 */                   paramAttributes.remove(localAttribute2.getID()); break;
/*     */ 
/* 615 */                   throw new AttributeModificationException("Unknown mod_op");
/*     */                 }
/*     */               } 
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 619 */     return paramAttributes;
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 625 */     return search(paramString, paramAttributes, null);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 631 */     return search(paramName, paramAttributes, null);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 638 */     return search(this.myParser.parse(paramString), paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 647 */     HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)doLookup(paramName, false);
/*     */ 
/* 649 */     SearchControls localSearchControls = new SearchControls();
/* 650 */     localSearchControls.setReturningAttributes(paramArrayOfString);
/*     */ 
/* 652 */     return new LazySearchEnumerationImpl(localHierMemDirCtx.doListBindings(false), new ContainmentFilter(paramAttributes), localSearchControls, this, this.myEnv, false);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 663 */     DirContext localDirContext = (DirContext)doLookup(paramName, false);
/*     */ 
/* 665 */     SearchFilter localSearchFilter = new SearchFilter(paramString);
/* 666 */     return new LazySearchEnumerationImpl(new HierContextEnumerator(localDirContext, paramSearchControls != null ? paramSearchControls.getSearchScope() : 1), localSearchFilter, paramSearchControls, this, this.myEnv, this.alwaysUseFactory);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 680 */     String str = SearchFilter.format(paramString, paramArrayOfObject);
/* 681 */     return search(paramName, str, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 688 */     return search(this.myParser.parse(paramString1), paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 696 */     return search(this.myParser.parse(paramString1), paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   protected HierMemDirCtx createNewCtx()
/*     */     throws NamingException
/*     */   {
/* 703 */     return new HierMemDirCtx(this.myEnv, this.ignoreCase);
/*     */   }
/*     */ 
/*     */   protected Name canonizeName(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 709 */     Object localObject = paramName;
/*     */ 
/* 711 */     if (!(paramName instanceof HierarchicalName))
/*     */     {
/* 713 */       localObject = new HierarchicalName();
/* 714 */       int i = paramName.size();
/* 715 */       for (int j = 0; j < i; j++) {
/* 716 */         ((Name)localObject).add(j, paramName.get(j));
/*     */       }
/*     */     }
/*     */ 
/* 720 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected Name getInternalName(Name paramName) throws NamingException {
/* 724 */     return paramName.getPrefix(paramName.size() - 1);
/*     */   }
/*     */ 
/*     */   protected Name getLeafName(Name paramName) throws NamingException {
/* 728 */     return paramName.getSuffix(paramName.size() - 1);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException
/*     */   {
/* 733 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException {
/* 737 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString) throws NamingException
/*     */   {
/* 742 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException
/*     */   {
/* 747 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public void setReadOnly(NamingException paramNamingException)
/*     */   {
/* 752 */     this.readOnlyEx = paramNamingException;
/*     */   }
/*     */ 
/*     */   public void setIgnoreCase(boolean paramBoolean)
/*     */   {
/* 757 */     this.ignoreCase = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setNameParser(NameParser paramNameParser) {
/* 761 */     this.myParser = paramNameParser;
/*     */   }
/*     */ 
/*     */   private final class FlatBindings extends HierMemDirCtx.FlatNames
/*     */   {
/*     */     private Hashtable bds;
/*     */     private Hashtable env;
/*     */     private boolean useFactory;
/*     */ 
/*     */     FlatBindings(Hashtable paramHashtable1, Hashtable paramBoolean, boolean arg4)
/*     */     {
/* 810 */       super(paramHashtable1.keys());
/* 811 */       this.env = paramBoolean;
/* 812 */       this.bds = paramHashtable1;
/*     */       boolean bool;
/* 813 */       this.useFactory = bool;
/*     */     }
/*     */ 
/*     */     public Object next() throws NamingException {
/* 817 */       Name localName = (Name)this.names.nextElement();
/*     */ 
/* 819 */       HierMemDirCtx localHierMemDirCtx = (HierMemDirCtx)this.bds.get(localName);
/*     */ 
/* 821 */       Object localObject = localHierMemDirCtx;
/* 822 */       if (this.useFactory) {
/* 823 */         Attributes localAttributes = localHierMemDirCtx.getAttributes("");
/*     */         try {
/* 825 */           localObject = DirectoryManager.getObjectInstance(localHierMemDirCtx, localName, HierMemDirCtx.this, this.env, localAttributes);
/*     */         }
/*     */         catch (NamingException localNamingException1) {
/* 828 */           throw localNamingException1;
/*     */         } catch (Exception localException) {
/* 830 */           NamingException localNamingException2 = new NamingException("Problem calling getObjectInstance");
/*     */ 
/* 832 */           localNamingException2.setRootCause(localException);
/* 833 */           throw localNamingException2;
/*     */         }
/*     */       }
/*     */ 
/* 837 */       return new Binding(localName.toString(), localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FlatNames
/*     */     implements NamingEnumeration
/*     */   {
/*     */     Enumeration names;
/*     */ 
/*     */     FlatNames(Enumeration arg2)
/*     */     {
/*     */       Object localObject;
/* 769 */       this.names = localObject;
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/*     */       try {
/* 774 */         return hasMore(); } catch (NamingException localNamingException) {
/*     */       }
/* 776 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean hasMore() throws NamingException
/*     */     {
/* 781 */       return this.names.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/*     */       try {
/* 786 */         return next();
/*     */       } catch (NamingException localNamingException) {
/* 788 */         throw new NoSuchElementException(localNamingException.toString());
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object next() throws NamingException {
/* 793 */       Name localName = (Name)this.names.nextElement();
/* 794 */       String str = HierMemDirCtx.this.bindings.get(localName).getClass().getName();
/* 795 */       return new NameClassPair(localName.toString(), str);
/*     */     }
/*     */ 
/*     */     public void close() {
/* 799 */       this.names = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class HierContextEnumerator extends ContextEnumerator
/*     */   {
/*     */     public HierContextEnumerator(Context paramInt, int arg3)
/*     */       throws NamingException
/*     */     {
/* 844 */       super(i);
/*     */     }
/*     */ 
/*     */     protected HierContextEnumerator(Context paramInt, int paramString, String paramBoolean, boolean arg5) throws NamingException
/*     */     {
/* 849 */       super(paramString, paramBoolean, bool);
/*     */     }
/*     */ 
/*     */     protected NamingEnumeration getImmediateChildren(Context paramContext) throws NamingException
/*     */     {
/* 854 */       return ((HierMemDirCtx)paramContext).doListBindings(false);
/*     */     }
/*     */ 
/*     */     protected ContextEnumerator newEnumerator(Context paramContext, int paramInt, String paramString, boolean paramBoolean) throws NamingException
/*     */     {
/* 859 */       return new HierContextEnumerator(HierMemDirCtx.this, paramContext, paramInt, paramString, paramBoolean);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.HierMemDirCtx
 * JD-Core Version:    0.6.2
 */