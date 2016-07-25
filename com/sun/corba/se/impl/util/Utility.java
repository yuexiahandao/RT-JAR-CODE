/*     */ package com.sun.corba.se.impl.util;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ import org.omg.CORBA.portable.ValueFactory;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.stub.java.rmi._Remote_Stub;
/*     */ 
/*     */ public final class Utility
/*     */ {
/*     */   public static final String STUB_PREFIX = "_";
/*     */   public static final String RMI_STUB_SUFFIX = "_Stub";
/*     */   public static final String DYNAMIC_STUB_SUFFIX = "_DynamicStub";
/*     */   public static final String IDL_STUB_SUFFIX = "Stub";
/*     */   public static final String TIE_SUFIX = "_Tie";
/*  90 */   private static IdentityHashtable tieCache = new IdentityHashtable();
/*  91 */   private static IdentityHashtable tieToStubCache = new IdentityHashtable();
/*  92 */   private static IdentityHashtable stubToTieCache = new IdentityHashtable();
/*  93 */   private static java.lang.Object CACHE_MISS = new java.lang.Object();
/*  94 */   private static UtilSystemException wrapper = UtilSystemException.get("util");
/*     */ 
/*  96 */   private static OMGSystemException omgWrapper = OMGSystemException.get("util");
/*     */ 
/*     */   public static java.lang.Object autoConnect(java.lang.Object paramObject, org.omg.CORBA.ORB paramORB, boolean paramBoolean)
/*     */   {
/* 113 */     if (paramObject == null) {
/* 114 */       return paramObject;
/*     */     }
/*     */ 
/* 117 */     if (StubAdapter.isStub(paramObject)) {
/*     */       try {
/* 119 */         StubAdapter.getDelegate(paramObject);
/*     */       } catch (BAD_OPERATION localBAD_OPERATION) {
/*     */         try {
/* 122 */           StubAdapter.connect(paramObject, paramORB);
/*     */         }
/*     */         catch (RemoteException localRemoteException)
/*     */         {
/* 126 */           throw wrapper.objectNotConnected(localRemoteException, paramObject.getClass().getName());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 131 */       return paramObject;
/*     */     }
/*     */ 
/* 134 */     if ((paramObject instanceof Remote)) {
/* 135 */       Remote localRemote1 = (Remote)paramObject;
/* 136 */       Tie localTie = Util.getTie(localRemote1);
/* 137 */       if (localTie != null) {
/*     */         try {
/* 139 */           localTie.orb();
/*     */         } catch (SystemException localSystemException) {
/* 141 */           localTie.orb(paramORB);
/*     */         }
/*     */ 
/* 144 */         if (paramBoolean) {
/* 145 */           Remote localRemote2 = loadStub(localTie, null, null, true);
/* 146 */           if (localRemote2 != null) {
/* 147 */             return localRemote2;
/*     */           }
/* 149 */           throw wrapper.couldNotLoadStub(paramObject.getClass().getName());
/*     */         }
/*     */ 
/* 152 */         return StubAdapter.activateTie(localTie);
/*     */       }
/*     */ 
/* 158 */       throw wrapper.objectNotExported(paramObject.getClass().getName());
/*     */     }
/*     */ 
/* 164 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public static Tie loadTie(Remote paramRemote)
/*     */   {
/* 172 */     Tie localTie = null;
/* 173 */     Class localClass = paramRemote.getClass();
/*     */ 
/* 177 */     synchronized (tieCache)
/*     */     {
/* 179 */       java.lang.Object localObject1 = tieCache.get(paramRemote);
/*     */ 
/* 181 */       if (localObject1 == null)
/*     */       {
/*     */         try
/*     */         {
/* 189 */           localTie = loadTie(localClass);
/*     */ 
/* 198 */           while ((localTie == null) && ((localClass = localClass.getSuperclass()) != null) && (localClass != PortableRemoteObject.class) && (localClass != java.lang.Object.class))
/*     */           {
/* 201 */             localTie = loadTie(localClass);
/*     */           }
/*     */         } catch (Exception localException1) {
/* 204 */           wrapper.loadTieFailed(localException1, localClass.getName());
/*     */         }
/*     */ 
/* 209 */         if (localTie == null)
/*     */         {
/* 213 */           tieCache.put(paramRemote, CACHE_MISS);
/*     */         }
/*     */         else
/*     */         {
/* 219 */           tieCache.put(paramRemote, localTie);
/*     */         }
/*     */ 
/*     */       }
/* 226 */       else if (localObject1 != CACHE_MISS) {
/*     */         try {
/* 228 */           localTie = (Tie)localObject1.getClass().newInstance();
/*     */         }
/*     */         catch (Exception localException2)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 235 */     return localTie;
/*     */   }
/*     */ 
/*     */   private static Tie loadTie(Class paramClass)
/*     */   {
/* 243 */     return com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory().getTie(paramClass);
/*     */   }
/*     */ 
/*     */   public static void clearCaches()
/*     */   {
/* 252 */     synchronized (tieToStubCache) {
/* 253 */       tieToStubCache.clear();
/*     */     }
/* 255 */     synchronized (tieCache) {
/* 256 */       tieCache.clear();
/*     */     }
/* 258 */     synchronized (stubToTieCache) {
/* 259 */       stubToTieCache.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   static Class loadClassOfType(String paramString1, String paramString2, ClassLoader paramClassLoader1, Class paramClass, ClassLoader paramClassLoader2)
/*     */     throws ClassNotFoundException
/*     */   {
/* 275 */     Class localClass = null;
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/* 286 */         if (!PackagePrefixChecker.hasOffendingPrefix(PackagePrefixChecker.withoutPackagePrefix(paramString1)))
/*     */         {
/* 288 */           localClass = Util.loadClass(PackagePrefixChecker.withoutPackagePrefix(paramString1), paramString2, paramClassLoader1);
/*     */         }
/*     */         else
/*     */         {
/* 293 */           localClass = Util.loadClass(paramString1, paramString2, paramClassLoader1);
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException1) {
/* 297 */         localClass = Util.loadClass(paramString1, paramString2, paramClassLoader1);
/*     */       }
/*     */ 
/* 300 */       if (paramClass == null)
/* 301 */         return localClass;
/*     */     } catch (ClassNotFoundException localClassNotFoundException2) {
/* 303 */       if (paramClass == null) {
/* 304 */         throw localClassNotFoundException2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 314 */     if ((localClass == null) || (!paramClass.isAssignableFrom(localClass))) {
/* 315 */       if (paramClass.getClassLoader() != paramClassLoader2) {
/* 316 */         throw new IllegalArgumentException("expectedTypeClassLoader not class loader of expected Type.");
/*     */       }
/*     */ 
/* 320 */       if (paramClassLoader2 != null) {
/* 321 */         localClass = paramClassLoader2.loadClass(paramString1);
/*     */       } else {
/* 323 */         ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 324 */         if (localClassLoader == null) {
/* 325 */           localClassLoader = ClassLoader.getSystemClassLoader();
/*     */         }
/* 327 */         localClass = localClassLoader.loadClass(paramString1);
/*     */       }
/*     */     }
/*     */ 
/* 331 */     return localClass;
/*     */   }
/*     */ 
/*     */   public static Class loadClassForClass(String paramString1, String paramString2, ClassLoader paramClassLoader1, Class paramClass, ClassLoader paramClassLoader2)
/*     */     throws ClassNotFoundException
/*     */   {
/* 349 */     if (paramClass == null) {
/* 350 */       return Util.loadClass(paramString1, paramString2, paramClassLoader1);
/*     */     }
/* 352 */     Class localClass = null;
/*     */     try {
/* 354 */       localClass = Util.loadClass(paramString1, paramString2, paramClassLoader1);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 356 */       if (paramClass.getClassLoader() == null) {
/* 357 */         throw localClassNotFoundException;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 367 */     if ((localClass == null) || ((localClass.getClassLoader() != null) && (localClass.getClassLoader().loadClass(paramClass.getName()) != paramClass)))
/*     */     {
/* 372 */       if (paramClass.getClassLoader() != paramClassLoader2) {
/* 373 */         throw new IllegalArgumentException("relatedTypeClassLoader not class loader of relatedType.");
/*     */       }
/*     */ 
/* 376 */       if (paramClassLoader2 != null) {
/* 377 */         localClass = paramClassLoader2.loadClass(paramString1);
/*     */       }
/*     */     }
/* 380 */     return localClass;
/*     */   }
/*     */ 
/*     */   public static BoxedValueHelper getHelper(Class paramClass, String paramString1, String paramString2)
/*     */   {
/* 391 */     String str = null;
/* 392 */     if (paramClass != null) {
/* 393 */       str = paramClass.getName();
/* 394 */       if (paramString1 == null)
/* 395 */         paramString1 = Util.getCodebase(paramClass);
/*     */     } else {
/* 397 */       if (paramString2 != null)
/* 398 */         str = RepositoryId.cache.getId(paramString2).getClassName();
/* 399 */       if (str == null) {
/* 400 */         throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 405 */       ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*     */ 
/* 407 */       Class localClass = loadClassForClass(str + "Helper", paramString1, localClassLoader, paramClass, localClassLoader);
/*     */ 
/* 410 */       return (BoxedValueHelper)localClass.newInstance();
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 413 */       throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 416 */       throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 419 */       throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, localInstantiationException);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/* 422 */       throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ValueFactory getFactory(Class paramClass, String paramString1, org.omg.CORBA.ORB paramORB, String paramString2)
/*     */   {
/* 435 */     ValueFactory localValueFactory = null;
/* 436 */     if ((paramORB != null) && (paramString2 != null)) {
/*     */       try {
/* 438 */         localValueFactory = ((org.omg.CORBA_2_3.ORB)paramORB).lookup_value_factory(paramString2);
/*     */       }
/*     */       catch (BAD_PARAM localBAD_PARAM)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 445 */     String str = null;
/* 446 */     if (paramClass != null) {
/* 447 */       str = paramClass.getName();
/* 448 */       if (paramString1 == null)
/* 449 */         paramString1 = Util.getCodebase(paramClass);
/*     */     } else {
/* 451 */       if (paramString2 != null)
/* 452 */         str = RepositoryId.cache.getId(paramString2).getClassName();
/* 453 */       if (str == null) {
/* 454 */         throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 460 */     if ((localValueFactory != null) && ((!localValueFactory.getClass().getName().equals(str + "DefaultFactory")) || ((paramClass == null) && (paramString1 == null))))
/*     */     {
/* 463 */       return localValueFactory;
/*     */     }
/*     */     try {
/* 466 */       ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*     */ 
/* 468 */       Class localClass = loadClassForClass(str + "DefaultFactory", paramString1, localClassLoader, paramClass, localClassLoader);
/*     */ 
/* 471 */       return (ValueFactory)localClass.newInstance();
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 474 */       throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 477 */       throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 480 */       throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, localInstantiationException);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/* 483 */       throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Remote loadStub(Tie paramTie, PresentationManager.StubFactory paramStubFactory, String paramString, boolean paramBoolean)
/*     */   {
/* 503 */     StubEntry localStubEntry = null;
/*     */ 
/* 506 */     synchronized (tieToStubCache) {
/* 507 */       java.lang.Object localObject1 = tieToStubCache.get(paramTie);
/* 508 */       if (localObject1 == null)
/*     */       {
/* 510 */         localStubEntry = loadStubAndUpdateCache(paramTie, paramStubFactory, paramString, paramBoolean);
/*     */       }
/* 515 */       else if (localObject1 != CACHE_MISS)
/*     */       {
/* 517 */         localStubEntry = (StubEntry)localObject1;
/*     */ 
/* 525 */         if ((!localStubEntry.mostDerived) && (paramBoolean))
/*     */         {
/* 531 */           localStubEntry = loadStubAndUpdateCache(paramTie, null, paramString, true);
/*     */         }
/* 533 */         else if ((paramStubFactory != null) && (!StubAdapter.getTypeIds(localStubEntry.stub)[0].equals(paramStubFactory.getTypeIds()[0])))
/*     */         {
/* 540 */           localStubEntry = loadStubAndUpdateCache(paramTie, null, paramString, true);
/*     */ 
/* 545 */           if (localStubEntry == null)
/* 546 */             localStubEntry = loadStubAndUpdateCache(paramTie, paramStubFactory, paramString, paramBoolean);
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 552 */             org.omg.CORBA.portable.Delegate localDelegate1 = StubAdapter.getDelegate(localStubEntry.stub);
/*     */           }
/*     */           catch (Exception localException1)
/*     */           {
/*     */             try {
/* 557 */               org.omg.CORBA.portable.Delegate localDelegate2 = StubAdapter.getDelegate(paramTie);
/*     */ 
/* 559 */               StubAdapter.setDelegate(localStubEntry.stub, localDelegate2);
/*     */             }
/*     */             catch (Exception localException2)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 568 */     if (localStubEntry != null) {
/* 569 */       return (Remote)localStubEntry.stub;
/*     */     }
/* 571 */     return null;
/*     */   }
/*     */ 
/*     */   private static StubEntry loadStubAndUpdateCache(Tie paramTie, PresentationManager.StubFactory paramStubFactory, String paramString, boolean paramBoolean)
/*     */   {
/* 589 */     java.lang.Object localObject1 = null;
/* 590 */     StubEntry localStubEntry = null;
/* 591 */     boolean bool1 = StubAdapter.isStub(paramTie);
/*     */     java.lang.Object localObject2;
/* 593 */     if (paramStubFactory != null) {
/*     */       try {
/* 595 */         localObject1 = paramStubFactory.makeStub();
/*     */       } catch (Throwable localThrowable) {
/* 597 */         wrapper.stubFactoryCouldNotMakeStub(localThrowable);
/* 598 */         if ((localThrowable instanceof ThreadDeath))
/* 599 */           throw ((ThreadDeath)localThrowable);
/*     */       }
/*     */     }
/*     */     else {
/* 603 */       localObject2 = null;
/* 604 */       if (bool1) {
/* 605 */         localObject2 = StubAdapter.getTypeIds(paramTie);
/*     */       }
/*     */       else
/*     */       {
/* 609 */         localObject2 = ((Servant)paramTie)._all_interfaces(null, null);
/*     */       }
/*     */ 
/* 613 */       if (paramString == null) {
/* 614 */         paramString = Util.getCodebase(paramTie.getClass());
/*     */       }
/*     */ 
/* 617 */       if (localObject2.length == 0) {
/* 618 */         localObject1 = new _Remote_Stub();
/*     */       }
/*     */       else {
/* 621 */         for (int i = 0; i < localObject2.length; i++) {
/* 622 */           if (localObject2[i].length() == 0) {
/* 623 */             localObject1 = new _Remote_Stub();
/* 624 */             break;
/*     */           }
/*     */           try
/*     */           {
/* 628 */             PresentationManager.StubFactoryFactory localStubFactoryFactory = com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory();
/*     */ 
/* 630 */             RepositoryId localRepositoryId = RepositoryId.cache.getId(localObject2[i]);
/* 631 */             String str = localRepositoryId.getClassName();
/* 632 */             boolean bool2 = localRepositoryId.isIDLType();
/* 633 */             paramStubFactory = localStubFactoryFactory.createStubFactory(str, bool2, paramString, null, paramTie.getClass().getClassLoader());
/*     */ 
/* 636 */             localObject1 = paramStubFactory.makeStub();
/*     */           }
/*     */           catch (Exception localException3) {
/* 639 */             wrapper.errorInMakeStubFromRepositoryId(localException3);
/*     */ 
/* 642 */             if (!paramBoolean) continue; 
/* 643 */           }break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 648 */     if (localObject1 == null)
/*     */     {
/* 650 */       tieToStubCache.put(paramTie, CACHE_MISS);
/*     */     } else {
/* 652 */       if (bool1)
/*     */         try {
/* 654 */           localObject2 = StubAdapter.getDelegate(paramTie);
/* 655 */           StubAdapter.setDelegate(localObject1, (org.omg.CORBA.portable.Delegate)localObject2);
/*     */         }
/*     */         catch (Exception localException1)
/*     */         {
/* 662 */           synchronized (stubToTieCache) {
/* 663 */             stubToTieCache.put(localObject1, paramTie);
/*     */           }
/*     */         }
/*     */       else {
/*     */         try
/*     */         {
/* 669 */           org.omg.CORBA.portable.Delegate localDelegate = StubAdapter.getDelegate(paramTie);
/* 670 */           StubAdapter.setDelegate(localObject1, localDelegate);
/*     */         } catch (BAD_INV_ORDER localBAD_INV_ORDER) {
/* 672 */           synchronized (stubToTieCache) {
/* 673 */             stubToTieCache.put(localObject1, paramTie);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (Exception localException2)
/*     */         {
/* 681 */           throw wrapper.noPoa(localException2);
/*     */         }
/*     */       }
/*     */ 
/* 685 */       localStubEntry = new StubEntry((org.omg.CORBA.Object)localObject1, paramBoolean);
/* 686 */       tieToStubCache.put(paramTie, localStubEntry);
/*     */     }
/*     */ 
/* 689 */     return localStubEntry;
/*     */   }
/*     */ 
/*     */   public static Tie getAndForgetTie(org.omg.CORBA.Object paramObject)
/*     */   {
/* 698 */     synchronized (stubToTieCache) {
/* 699 */       return (Tie)stubToTieCache.remove(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void purgeStubForTie(Tie paramTie)
/*     */   {
/*     */     StubEntry localStubEntry;
/* 708 */     synchronized (tieToStubCache) {
/* 709 */       localStubEntry = (StubEntry)tieToStubCache.remove(paramTie);
/*     */     }
/* 711 */     if (localStubEntry != null)
/* 712 */       synchronized (stubToTieCache) {
/* 713 */         stubToTieCache.remove(localStubEntry.stub);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void purgeTieAndServant(Tie paramTie)
/*     */   {
/* 722 */     synchronized (tieCache) {
/* 723 */       Remote localRemote = paramTie.getTarget();
/* 724 */       if (localRemote != null)
/* 725 */         tieCache.remove(localRemote);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String stubNameFromRepID(String paramString)
/*     */   {
/* 737 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 738 */     String str = localRepositoryId.getClassName();
/*     */ 
/* 740 */     if (localRepositoryId.isIDLType())
/* 741 */       str = idlStubName(str);
/*     */     else {
/* 743 */       str = stubName(str);
/*     */     }
/* 745 */     return str;
/*     */   }
/*     */ 
/*     */   public static Remote loadStub(org.omg.CORBA.Object paramObject, Class paramClass)
/*     */   {
/* 755 */     Remote localRemote = null;
/*     */     try
/*     */     {
/* 760 */       String str = null;
/*     */       try
/*     */       {
/* 765 */         org.omg.CORBA.portable.Delegate localDelegate = StubAdapter.getDelegate(paramObject);
/* 766 */         str = ((org.omg.CORBA_2_3.portable.Delegate)localDelegate).get_codebase(paramObject);
/*     */       }
/*     */       catch (ClassCastException localClassCastException)
/*     */       {
/* 770 */         wrapper.classCastExceptionInLoadStub(localClassCastException);
/*     */       }
/*     */ 
/* 773 */       PresentationManager.StubFactoryFactory localStubFactoryFactory = com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory();
/*     */ 
/* 775 */       PresentationManager.StubFactory localStubFactory = localStubFactoryFactory.createStubFactory(paramClass.getName(), false, str, paramClass, paramClass.getClassLoader());
/*     */ 
/* 778 */       localRemote = (Remote)localStubFactory.makeStub();
/* 779 */       StubAdapter.setDelegate(localRemote, StubAdapter.getDelegate(paramObject));
/*     */     }
/*     */     catch (Exception localException) {
/* 782 */       wrapper.exceptionInLoadStub(localException);
/*     */     }
/*     */ 
/* 785 */     return localRemote;
/*     */   }
/*     */ 
/*     */   public static Class loadStubClass(String paramString1, String paramString2, Class paramClass)
/*     */     throws ClassNotFoundException
/*     */   {
/* 801 */     if (paramString1.length() == 0) {
/* 802 */       throw new ClassNotFoundException();
/*     */     }
/*     */ 
/* 808 */     String str = stubNameFromRepID(paramString1);
/* 809 */     ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*     */     try
/*     */     {
/* 813 */       return loadClassOfType(str, paramString2, localClassLoader, paramClass, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/*     */ 
/* 819 */     return loadClassOfType(PackagePrefixChecker.packagePrefix() + str, paramString2, localClassLoader, paramClass, localClassLoader);
/*     */   }
/*     */ 
/*     */   public static String stubName(String paramString)
/*     */   {
/* 832 */     return stubName(paramString, false);
/*     */   }
/*     */ 
/*     */   public static String dynamicStubName(String paramString)
/*     */   {
/* 837 */     return stubName(paramString, true);
/*     */   }
/*     */ 
/*     */   private static String stubName(String paramString, boolean paramBoolean)
/*     */   {
/* 843 */     String str = stubNameForCompiler(paramString, paramBoolean);
/* 844 */     if (PackagePrefixChecker.hasOffendingPrefix(str))
/* 845 */       str = PackagePrefixChecker.packagePrefix() + str;
/* 846 */     return str;
/*     */   }
/*     */ 
/*     */   public static String stubNameForCompiler(String paramString)
/*     */   {
/* 851 */     return stubNameForCompiler(paramString, false);
/*     */   }
/*     */ 
/*     */   private static String stubNameForCompiler(String paramString, boolean paramBoolean)
/*     */   {
/* 857 */     int i = paramString.indexOf('$');
/* 858 */     if (i < 0) {
/* 859 */       i = paramString.lastIndexOf('.');
/*     */     }
/*     */ 
/* 862 */     String str = paramBoolean ? "_DynamicStub" : "_Stub";
/*     */ 
/* 865 */     if (i > 0) {
/* 866 */       return paramString.substring(0, i + 1) + "_" + paramString.substring(i + 1) + str;
/*     */     }
/*     */ 
/* 869 */     return "_" + paramString + str;
/*     */   }
/*     */ 
/*     */   public static String tieName(String paramString)
/*     */   {
/* 878 */     return PackagePrefixChecker.hasOffendingPrefix(tieNameForCompiler(paramString)) ? PackagePrefixChecker.packagePrefix() + tieNameForCompiler(paramString) : tieNameForCompiler(paramString);
/*     */   }
/*     */ 
/*     */   public static String tieNameForCompiler(String paramString)
/*     */   {
/* 886 */     int i = paramString.indexOf('$');
/* 887 */     if (i < 0) {
/* 888 */       i = paramString.lastIndexOf('.');
/*     */     }
/* 890 */     if (i > 0) {
/* 891 */       return paramString.substring(0, i + 1) + "_" + paramString.substring(i + 1) + "_Tie";
/*     */     }
/*     */ 
/* 896 */     return "_" + paramString + "_Tie";
/*     */   }
/*     */ 
/*     */   public static void throwNotSerializableForCorba(String paramString)
/*     */   {
/* 906 */     throw omgWrapper.notSerializable(CompletionStatus.COMPLETED_MAYBE, paramString);
/*     */   }
/*     */ 
/*     */   public static String idlStubName(String paramString)
/*     */   {
/* 915 */     String str = null;
/* 916 */     int i = paramString.lastIndexOf('.');
/* 917 */     if (i > 0) {
/* 918 */       str = paramString.substring(0, i + 1) + "_" + paramString.substring(i + 1) + "Stub";
/*     */     }
/*     */     else
/*     */     {
/* 923 */       str = "_" + paramString + "Stub";
/*     */     }
/*     */ 
/* 927 */     return str;
/*     */   }
/*     */ 
/*     */   public static void printStackTrace()
/*     */   {
/* 932 */     Throwable localThrowable = new Throwable("Printing stack trace:");
/* 933 */     localThrowable.fillInStackTrace();
/* 934 */     localThrowable.printStackTrace();
/*     */   }
/*     */ 
/*     */   public static java.lang.Object readObjectAndNarrow(org.omg.CORBA.portable.InputStream paramInputStream, Class paramClass)
/*     */     throws ClassCastException
/*     */   {
/* 947 */     org.omg.CORBA.Object localObject = paramInputStream.read_Object();
/* 948 */     if (localObject != null) {
/* 949 */       return PortableRemoteObject.narrow(localObject, paramClass);
/*     */     }
/* 951 */     return null;
/*     */   }
/*     */ 
/*     */   public static java.lang.Object readAbstractAndNarrow(org.omg.CORBA_2_3.portable.InputStream paramInputStream, Class paramClass)
/*     */     throws ClassCastException
/*     */   {
/* 964 */     java.lang.Object localObject = paramInputStream.read_abstract_interface();
/* 965 */     if (localObject != null) {
/* 966 */       return PortableRemoteObject.narrow(localObject, paramClass);
/*     */     }
/* 968 */     return null;
/*     */   }
/*     */ 
/*     */   static int hexOf(char paramChar)
/*     */   {
/* 978 */     int i = paramChar - '0';
/* 979 */     if ((i >= 0) && (i <= 9)) {
/* 980 */       return i;
/*     */     }
/* 982 */     i = paramChar - 'a' + 10;
/* 983 */     if ((i >= 10) && (i <= 15)) {
/* 984 */       return i;
/*     */     }
/* 986 */     i = paramChar - 'A' + 10;
/* 987 */     if ((i >= 10) && (i <= 15)) {
/* 988 */       return i;
/*     */     }
/* 990 */     throw wrapper.badHexDigit();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.Utility
 * JD-Core Version:    0.6.2
 */