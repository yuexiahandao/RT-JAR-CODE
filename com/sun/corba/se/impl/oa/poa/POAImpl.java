/*      */ package com.sun.corba.se.impl.oa.poa;
/*      */ 
/*      */ import com.sun.corba.se.impl.ior.ObjectAdapterIdArray;
/*      */ import com.sun.corba.se.impl.ior.POAObjectKeyTemplate;
/*      */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*      */ import com.sun.corba.se.impl.logging.POASystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.orbutil.concurrent.CondVar;
/*      */ import com.sun.corba.se.impl.orbutil.concurrent.ReentrantMutex;
/*      */ import com.sun.corba.se.impl.orbutil.concurrent.Sync;
/*      */ import com.sun.corba.se.impl.orbutil.concurrent.SyncUtil;
/*      */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*      */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.IORFactories;
/*      */ import com.sun.corba.se.spi.ior.IORTemplateList;
/*      */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*      */ import com.sun.corba.se.spi.ior.ObjectId;
/*      */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*      */ import com.sun.corba.se.spi.ior.TaggedProfile;
/*      */ import com.sun.corba.se.spi.oa.OADestroyed;
/*      */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*      */ import com.sun.corba.se.spi.oa.ObjectAdapterBase;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import com.sun.corba.se.spi.orb.ORBData;
/*      */ import com.sun.corba.se.spi.protocol.ForwardException;
/*      */ import com.sun.corba.se.spi.protocol.PIHandler;
/*      */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.omg.CORBA.Policy;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.PortableInterceptor.ObjectReferenceFactory;
/*      */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*      */ import org.omg.PortableServer.AdapterActivator;
/*      */ import org.omg.PortableServer.ForwardRequest;
/*      */ import org.omg.PortableServer.IdAssignmentPolicy;
/*      */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*      */ import org.omg.PortableServer.IdUniquenessPolicy;
/*      */ import org.omg.PortableServer.IdUniquenessPolicyValue;
/*      */ import org.omg.PortableServer.ImplicitActivationPolicy;
/*      */ import org.omg.PortableServer.ImplicitActivationPolicyValue;
/*      */ import org.omg.PortableServer.LifespanPolicy;
/*      */ import org.omg.PortableServer.LifespanPolicyValue;
/*      */ import org.omg.PortableServer.POA;
/*      */ import org.omg.PortableServer.POAManager;
/*      */ import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
/*      */ import org.omg.PortableServer.POAPackage.AdapterNonExistent;
/*      */ import org.omg.PortableServer.POAPackage.InvalidPolicy;
/*      */ import org.omg.PortableServer.POAPackage.NoServant;
/*      */ import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
/*      */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*      */ import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
/*      */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*      */ import org.omg.PortableServer.POAPackage.WrongAdapter;
/*      */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*      */ import org.omg.PortableServer.RequestProcessingPolicy;
/*      */ import org.omg.PortableServer.RequestProcessingPolicyValue;
/*      */ import org.omg.PortableServer.Servant;
/*      */ import org.omg.PortableServer.ServantManager;
/*      */ import org.omg.PortableServer.ServantRetentionPolicy;
/*      */ import org.omg.PortableServer.ServantRetentionPolicyValue;
/*      */ import org.omg.PortableServer.ThreadPolicy;
/*      */ import org.omg.PortableServer.ThreadPolicyValue;
/*      */ 
/*      */ public class POAImpl extends ObjectAdapterBase
/*      */   implements POA
/*      */ {
/*      */   private boolean debug;
/*      */   private static final int STATE_START = 0;
/*      */   private static final int STATE_INIT = 1;
/*      */   private static final int STATE_INIT_DONE = 2;
/*      */   private static final int STATE_RUN = 3;
/*      */   private static final int STATE_DESTROYING = 4;
/*      */   private static final int STATE_DESTROYED = 5;
/*      */   private int state;
/*      */   private POAPolicyMediator mediator;
/*      */   private int numLevels;
/*      */   private ObjectAdapterId poaId;
/*      */   private String name;
/*      */   private POAManagerImpl manager;
/*      */   private int uniquePOAId;
/*      */   private POAImpl parent;
/*      */   private Map children;
/*      */   private AdapterActivator activator;
/*      */   private int invocationCount;
/*      */   Sync poaMutex;
/*      */   private CondVar adapterActivatorCV;
/*      */   private CondVar invokeCV;
/*      */   private CondVar beingDestroyedCV;
/*      */   protected ThreadLocal isDestroying;
/*      */ 
/*      */   private String stateToString()
/*      */   {
/*  179 */     switch (this.state) {
/*      */     case 0:
/*  181 */       return "START";
/*      */     case 1:
/*  183 */       return "INIT";
/*      */     case 2:
/*  185 */       return "INIT_DONE";
/*      */     case 3:
/*  187 */       return "RUN";
/*      */     case 4:
/*  189 */       return "DESTROYING";
/*      */     case 5:
/*  191 */       return "DESTROYED";
/*      */     }
/*  193 */     return "UNKNOWN(" + this.state + ")";
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  245 */     return "POA[" + this.poaId.toString() + ", uniquePOAId=" + this.uniquePOAId + ", state=" + stateToString() + ", invocationCount=" + this.invocationCount + "]";
/*      */   }
/*      */ 
/*      */   boolean getDebug()
/*      */   {
/*  254 */     return this.debug;
/*      */   }
/*      */ 
/*      */   static POAFactory getPOAFactory(ORB paramORB)
/*      */   {
/*  260 */     return (POAFactory)paramORB.getRequestDispatcherRegistry().getObjectAdapterFactory(32);
/*      */   }
/*      */ 
/*      */   static POAImpl makeRootPOA(ORB paramORB)
/*      */   {
/*  267 */     POAManagerImpl localPOAManagerImpl = new POAManagerImpl(getPOAFactory(paramORB), paramORB.getPIHandler());
/*      */ 
/*  270 */     POAImpl localPOAImpl = new POAImpl("RootPOA", null, paramORB, 0);
/*      */ 
/*  272 */     localPOAImpl.initialize(localPOAManagerImpl, Policies.rootPOAPolicies);
/*      */ 
/*  274 */     return localPOAImpl;
/*      */   }
/*      */ 
/*      */   int getPOAId()
/*      */   {
/*  280 */     return this.uniquePOAId;
/*      */   }
/*      */ 
/*      */   void lock()
/*      */   {
/*  287 */     SyncUtil.acquire(this.poaMutex);
/*      */ 
/*  289 */     if (this.debug)
/*  290 */       ORBUtility.dprint(this, "LOCKED poa " + this);
/*      */   }
/*      */ 
/*      */   void unlock()
/*      */   {
/*  297 */     if (this.debug) {
/*  298 */       ORBUtility.dprint(this, "UNLOCKED poa " + this);
/*      */     }
/*      */ 
/*  301 */     this.poaMutex.release();
/*      */   }
/*      */ 
/*      */   Policies getPolicies()
/*      */   {
/*  307 */     return this.mediator.getPolicies();
/*      */   }
/*      */ 
/*      */   private POAImpl(String paramString, POAImpl paramPOAImpl, ORB paramORB, int paramInt)
/*      */   {
/*  313 */     super(paramORB);
/*      */ 
/*  315 */     this.debug = paramORB.poaDebugFlag;
/*      */ 
/*  317 */     if (this.debug) {
/*  318 */       ORBUtility.dprint(this, "Creating POA with name=" + paramString + " parent=" + paramPOAImpl);
/*      */     }
/*      */ 
/*  322 */     this.state = paramInt;
/*  323 */     this.name = paramString;
/*  324 */     this.parent = paramPOAImpl;
/*  325 */     this.children = new HashMap();
/*  326 */     this.activator = null;
/*      */ 
/*  330 */     this.uniquePOAId = getPOAFactory(paramORB).newPOAId();
/*      */ 
/*  332 */     if (paramPOAImpl == null)
/*      */     {
/*  334 */       this.numLevels = 1;
/*      */     }
/*      */     else {
/*  337 */       paramPOAImpl.numLevels += 1;
/*      */ 
/*  339 */       paramPOAImpl.children.put(paramString, this);
/*      */     }
/*      */ 
/*  344 */     String[] arrayOfString = new String[this.numLevels];
/*  345 */     POAImpl localPOAImpl = this;
/*  346 */     int i = this.numLevels - 1;
/*  347 */     while (localPOAImpl != null) {
/*  348 */       arrayOfString[(i--)] = localPOAImpl.name;
/*  349 */       localPOAImpl = localPOAImpl.parent;
/*      */     }
/*      */ 
/*  352 */     this.poaId = new ObjectAdapterIdArray(arrayOfString);
/*      */ 
/*  354 */     this.invocationCount = 0;
/*      */ 
/*  356 */     this.poaMutex = new ReentrantMutex(paramORB.poaConcurrencyDebugFlag);
/*      */ 
/*  358 */     this.adapterActivatorCV = new CondVar(this.poaMutex, paramORB.poaConcurrencyDebugFlag);
/*      */ 
/*  360 */     this.invokeCV = new CondVar(this.poaMutex, paramORB.poaConcurrencyDebugFlag);
/*      */ 
/*  362 */     this.beingDestroyedCV = new CondVar(this.poaMutex, paramORB.poaConcurrencyDebugFlag);
/*      */ 
/*  365 */     this.isDestroying = new ThreadLocal() {
/*      */       protected java.lang.Object initialValue() {
/*  367 */         return Boolean.FALSE;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private void initialize(POAManagerImpl paramPOAManagerImpl, Policies paramPolicies)
/*      */   {
/*  375 */     if (this.debug) {
/*  376 */       ORBUtility.dprint(this, "Initializing poa " + this + " with POAManager=" + paramPOAManagerImpl + " policies=" + paramPolicies);
/*      */     }
/*      */ 
/*  380 */     this.manager = paramPOAManagerImpl;
/*  381 */     paramPOAManagerImpl.addPOA(this);
/*      */ 
/*  383 */     this.mediator = POAPolicyMediatorFactory.create(paramPolicies, this);
/*      */ 
/*  386 */     int i = this.mediator.getServerId();
/*  387 */     int j = this.mediator.getScid();
/*  388 */     String str = getORB().getORBData().getORBId();
/*      */ 
/*  390 */     POAObjectKeyTemplate localPOAObjectKeyTemplate = new POAObjectKeyTemplate(getORB(), j, i, str, this.poaId);
/*      */ 
/*  393 */     if (this.debug) {
/*  394 */       ORBUtility.dprint(this, "Initializing poa: oktemp=" + localPOAObjectKeyTemplate);
/*      */     }
/*      */ 
/*  400 */     boolean bool = true;
/*      */ 
/*  404 */     initializeTemplate(localPOAObjectKeyTemplate, bool, paramPolicies, null, null, localPOAObjectKeyTemplate.getObjectAdapterId());
/*      */ 
/*  411 */     if (this.state == 0)
/*  412 */       this.state = 3;
/*  413 */     else if (this.state == 1)
/*  414 */       this.state = 2;
/*      */     else
/*  416 */       throw lifecycleWrapper().illegalPoaStateTrans();
/*      */   }
/*      */ 
/*      */   private boolean waitUntilRunning()
/*      */   {
/*  422 */     if (this.debug) {
/*  423 */       ORBUtility.dprint(this, "Calling waitUntilRunning on poa " + this);
/*      */     }
/*      */ 
/*  427 */     while (this.state < 3) {
/*      */       try {
/*  429 */         this.adapterActivatorCV.await();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException)
/*      */       {
/*      */       }
/*      */     }
/*  435 */     if (this.debug) {
/*  436 */       ORBUtility.dprint(this, "Exiting waitUntilRunning on poa " + this);
/*      */     }
/*      */ 
/*  442 */     return this.state == 3;
/*      */   }
/*      */ 
/*      */   private boolean destroyIfNotInitDone()
/*      */   {
/*      */     try
/*      */     {
/*  459 */       lock();
/*      */ 
/*  461 */       if (this.debug) {
/*  462 */         ORBUtility.dprint(this, "Calling destroyIfNotInitDone on poa " + this);
/*      */       }
/*      */ 
/*  466 */       DestroyThread localDestroyThread1 = this.state == 2 ? 1 : 0;
/*      */       DestroyThread localDestroyThread2;
/*  468 */       if (localDestroyThread1 != 0) {
/*  469 */         this.state = 3;
/*      */       }
/*      */       else
/*      */       {
/*  474 */         localDestroyThread2 = new DestroyThread(false, this.debug);
/*  475 */         localDestroyThread2.doIt(this, true);
/*      */       }
/*      */ 
/*  478 */       return localDestroyThread1;
/*      */     } finally {
/*  480 */       this.adapterActivatorCV.broadcast();
/*      */ 
/*  482 */       if (this.debug) {
/*  483 */         ORBUtility.dprint(this, "Exiting destroyIfNotInitDone on poa " + this);
/*      */       }
/*      */ 
/*  487 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private byte[] internalReferenceToId(org.omg.CORBA.Object paramObject)
/*      */     throws WrongAdapter
/*      */   {
/*  494 */     IOR localIOR = ORBUtility.getIOR(paramObject);
/*  495 */     IORTemplateList localIORTemplateList1 = localIOR.getIORTemplates();
/*      */ 
/*  497 */     ObjectReferenceFactory localObjectReferenceFactory = getCurrentFactory();
/*  498 */     IORTemplateList localIORTemplateList2 = IORFactories.getIORTemplateList(localObjectReferenceFactory);
/*      */ 
/*  501 */     if (!localIORTemplateList2.isEquivalent(localIORTemplateList1)) {
/*  502 */       throw new WrongAdapter();
/*      */     }
/*      */ 
/*  508 */     Iterator localIterator = localIOR.iterator();
/*  509 */     if (!localIterator.hasNext())
/*  510 */       throw iorWrapper().noProfilesInIor();
/*  511 */     TaggedProfile localTaggedProfile = (TaggedProfile)localIterator.next();
/*  512 */     ObjectId localObjectId = localTaggedProfile.getObjectId();
/*      */ 
/*  514 */     return localObjectId.getId();
/*      */   }
/*      */ 
/*      */   void etherealizeAll()
/*      */   {
/*      */     try
/*      */     {
/*  728 */       lock();
/*      */ 
/*  730 */       if (this.debug) {
/*  731 */         ORBUtility.dprint(this, "Calling etheralizeAll on poa " + this);
/*      */       }
/*      */ 
/*  735 */       this.mediator.etherealizeAll();
/*      */     } finally {
/*  737 */       if (this.debug) {
/*  738 */         ORBUtility.dprint(this, "Exiting etheralizeAll on poa " + this);
/*      */       }
/*      */ 
/*  742 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public POA create_POA(String paramString, POAManager paramPOAManager, Policy[] paramArrayOfPolicy)
/*      */     throws AdapterAlreadyExists, InvalidPolicy
/*      */   {
/*      */     try
/*      */     {
/*  759 */       lock();
/*      */ 
/*  761 */       if (this.debug) {
/*  762 */         ORBUtility.dprint(this, "Calling create_POA(name=" + paramString + " theManager=" + paramPOAManager + " policies=" + paramArrayOfPolicy + ") on poa " + this);
/*      */       }
/*      */ 
/*  769 */       if (this.state > 3) {
/*  770 */         throw omgLifecycleWrapper().createPoaDestroy();
/*      */       }
/*  772 */       POAImpl localPOAImpl1 = (POAImpl)this.children.get(paramString);
/*      */ 
/*  774 */       if (localPOAImpl1 == null) {
/*  775 */         localPOAImpl1 = new POAImpl(paramString, this, getORB(), 0);
/*      */       }
/*      */       try
/*      */       {
/*  779 */         localPOAImpl1.lock();
/*      */ 
/*  781 */         if (this.debug) {
/*  782 */           ORBUtility.dprint(this, "Calling create_POA: new poa is " + localPOAImpl1);
/*      */         }
/*      */ 
/*  786 */         if ((localPOAImpl1.state != 0) && (localPOAImpl1.state != 1)) {
/*  787 */           throw new AdapterAlreadyExists();
/*      */         }
/*  789 */         POAManagerImpl localPOAManagerImpl = (POAManagerImpl)paramPOAManager;
/*  790 */         if (localPOAManagerImpl == null) {
/*  791 */           localPOAManagerImpl = new POAManagerImpl(this.manager.getFactory(), this.manager.getPIHandler());
/*      */         }
/*      */ 
/*  794 */         int i = getORB().getCopierManager().getDefaultId();
/*      */ 
/*  796 */         Policies localPolicies = new Policies(paramArrayOfPolicy, i);
/*      */ 
/*  799 */         localPOAImpl1.initialize(localPOAManagerImpl, localPolicies);
/*      */ 
/*  801 */         return localPOAImpl1;
/*      */       } finally {
/*      */       }
/*      */     }
/*      */     finally {
/*  806 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public POA find_POA(String paramString, boolean paramBoolean)
/*      */     throws AdapterNonExistent
/*      */   {
/*  817 */     POAImpl localPOAImpl = null;
/*  818 */     AdapterActivator localAdapterActivator = null;
/*      */ 
/*  820 */     lock();
/*      */ 
/*  822 */     if (this.debug) {
/*  823 */       ORBUtility.dprint(this, "Calling find_POA(name=" + paramString + " activate=" + paramBoolean + ") on poa " + this);
/*      */     }
/*      */ 
/*  827 */     localPOAImpl = (POAImpl)this.children.get(paramString);
/*      */ 
/*  829 */     if (localPOAImpl != null) {
/*  830 */       if (this.debug) {
/*  831 */         ORBUtility.dprint(this, "Calling find_POA: found poa " + localPOAImpl);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  836 */         localPOAImpl.lock();
/*      */ 
/*  840 */         unlock();
/*      */ 
/*  846 */         if (!localPOAImpl.waitUntilRunning()) {
/*  847 */           throw omgLifecycleWrapper().poaDestroyed();
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  853 */         localPOAImpl.unlock();
/*      */       }
/*      */     } else {
/*      */       try {
/*  857 */         if (this.debug) {
/*  858 */           ORBUtility.dprint(this, "Calling find_POA: no poa found");
/*      */         }
/*      */ 
/*  862 */         if ((paramBoolean) && (this.activator != null))
/*      */         {
/*  876 */           localPOAImpl = new POAImpl(paramString, this, getORB(), 1);
/*      */ 
/*  878 */           if (this.debug) {
/*  879 */             ORBUtility.dprint(this, "Calling find_POA: created poa " + localPOAImpl);
/*      */           }
/*      */ 
/*  883 */           localAdapterActivator = this.activator;
/*      */         } else {
/*  885 */           throw new AdapterNonExistent();
/*      */         }
/*      */       } finally {
/*  888 */         unlock();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  899 */     if (localAdapterActivator != null) {
/*  900 */       boolean bool1 = false;
/*  901 */       boolean bool2 = false;
/*      */ 
/*  903 */       if (this.debug) {
/*  904 */         ORBUtility.dprint(this, "Calling find_POA: calling AdapterActivator");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  911 */         synchronized (localAdapterActivator) {
/*  912 */           bool1 = localAdapterActivator.unknown_adapter(this, paramString);
/*      */         }
/*      */       } catch (SystemException localSystemException) {
/*  915 */         throw omgLifecycleWrapper().adapterActivatorException(localSystemException, paramString, this.poaId.toString());
/*      */       }
/*      */       catch (Throwable localThrowable)
/*      */       {
/*  920 */         lifecycleWrapper().unexpectedException(localThrowable, toString());
/*      */ 
/*  922 */         if ((localThrowable instanceof ThreadDeath)) {
/*  923 */           throw ((ThreadDeath)localThrowable);
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  930 */         bool2 = localPOAImpl.destroyIfNotInitDone();
/*      */       }
/*      */ 
/*  933 */       if (bool1) {
/*  934 */         if (!bool2)
/*  935 */           throw omgLifecycleWrapper().adapterActivatorException(paramString, this.poaId.toString());
/*      */       }
/*      */       else {
/*  938 */         if (this.debug) {
/*  939 */           ORBUtility.dprint(this, "Calling find_POA: AdapterActivator returned false");
/*      */         }
/*      */ 
/*  945 */         throw new AdapterNonExistent();
/*      */       }
/*      */     }
/*      */ 
/*  949 */     return localPOAImpl;
/*      */   }
/*      */ 
/*      */   public void destroy(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  959 */     if ((paramBoolean2) && (getORB().isDuringDispatch())) {
/*  960 */       throw lifecycleWrapper().destroyDeadlock();
/*      */     }
/*      */ 
/*  963 */     DestroyThread localDestroyThread = new DestroyThread(paramBoolean1, this.debug);
/*  964 */     localDestroyThread.doIt(this, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public ThreadPolicy create_thread_policy(ThreadPolicyValue paramThreadPolicyValue)
/*      */   {
/*  974 */     return new ThreadPolicyImpl(paramThreadPolicyValue);
/*      */   }
/*      */ 
/*      */   public LifespanPolicy create_lifespan_policy(LifespanPolicyValue paramLifespanPolicyValue)
/*      */   {
/*  984 */     return new LifespanPolicyImpl(paramLifespanPolicyValue);
/*      */   }
/*      */ 
/*      */   public IdUniquenessPolicy create_id_uniqueness_policy(IdUniquenessPolicyValue paramIdUniquenessPolicyValue)
/*      */   {
/*  994 */     return new IdUniquenessPolicyImpl(paramIdUniquenessPolicyValue);
/*      */   }
/*      */ 
/*      */   public IdAssignmentPolicy create_id_assignment_policy(IdAssignmentPolicyValue paramIdAssignmentPolicyValue)
/*      */   {
/* 1004 */     return new IdAssignmentPolicyImpl(paramIdAssignmentPolicyValue);
/*      */   }
/*      */ 
/*      */   public ImplicitActivationPolicy create_implicit_activation_policy(ImplicitActivationPolicyValue paramImplicitActivationPolicyValue)
/*      */   {
/* 1014 */     return new ImplicitActivationPolicyImpl(paramImplicitActivationPolicyValue);
/*      */   }
/*      */ 
/*      */   public ServantRetentionPolicy create_servant_retention_policy(ServantRetentionPolicyValue paramServantRetentionPolicyValue)
/*      */   {
/* 1024 */     return new ServantRetentionPolicyImpl(paramServantRetentionPolicyValue);
/*      */   }
/*      */ 
/*      */   public RequestProcessingPolicy create_request_processing_policy(RequestProcessingPolicyValue paramRequestProcessingPolicyValue)
/*      */   {
/* 1034 */     return new RequestProcessingPolicyImpl(paramRequestProcessingPolicyValue);
/*      */   }
/*      */ 
/*      */   public String the_name()
/*      */   {
/*      */     try
/*      */     {
/* 1044 */       lock();
/*      */ 
/* 1046 */       return this.name;
/*      */     } finally {
/* 1048 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public POA the_parent()
/*      */   {
/*      */     try
/*      */     {
/* 1059 */       lock();
/*      */ 
/* 1061 */       return this.parent;
/*      */     } finally {
/* 1063 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public POA[] the_children()
/*      */   {
/*      */     try
/*      */     {
/* 1073 */       lock();
/*      */ 
/* 1075 */       Collection localCollection = this.children.values();
/* 1076 */       int i = localCollection.size();
/* 1077 */       POA[] arrayOfPOA = new POA[i];
/* 1078 */       int j = 0;
/* 1079 */       Iterator localIterator = localCollection.iterator();
/*      */       java.lang.Object localObject1;
/* 1080 */       while (localIterator.hasNext()) {
/* 1081 */         localObject1 = (POA)localIterator.next();
/* 1082 */         arrayOfPOA[(j++)] = localObject1;
/*      */       }
/*      */ 
/* 1085 */       return arrayOfPOA;
/*      */     } finally {
/* 1087 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public POAManager the_POAManager()
/*      */   {
/*      */     try
/*      */     {
/* 1098 */       lock();
/*      */ 
/* 1100 */       return this.manager;
/*      */     } finally {
/* 1102 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public AdapterActivator the_activator()
/*      */   {
/*      */     try
/*      */     {
/* 1113 */       lock();
/*      */ 
/* 1115 */       return this.activator;
/*      */     } finally {
/* 1117 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void the_activator(AdapterActivator paramAdapterActivator)
/*      */   {
/*      */     try
/*      */     {
/* 1128 */       lock();
/*      */ 
/* 1130 */       if (this.debug) {
/* 1131 */         ORBUtility.dprint(this, "Calling the_activator on poa " + this + " activator=" + paramAdapterActivator);
/*      */       }
/*      */ 
/* 1135 */       this.activator = paramAdapterActivator;
/*      */     } finally {
/* 1137 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ServantManager get_servant_manager()
/*      */     throws WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1148 */       lock();
/*      */ 
/* 1150 */       return this.mediator.getServantManager();
/*      */     } finally {
/* 1152 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_servant_manager(ServantManager paramServantManager)
/*      */     throws WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1164 */       lock();
/*      */ 
/* 1166 */       if (this.debug) {
/* 1167 */         ORBUtility.dprint(this, "Calling set_servant_manager on poa " + this + " servantManager=" + paramServantManager);
/*      */       }
/*      */ 
/* 1171 */       this.mediator.setServantManager(paramServantManager);
/*      */     } finally {
/* 1173 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Servant get_servant()
/*      */     throws NoServant, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1184 */       lock();
/*      */ 
/* 1186 */       return this.mediator.getDefaultServant();
/*      */     } finally {
/* 1188 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_servant(Servant paramServant)
/*      */     throws WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1200 */       lock();
/*      */ 
/* 1202 */       if (this.debug) {
/* 1203 */         ORBUtility.dprint(this, "Calling set_servant on poa " + this + " defaultServant=" + paramServant);
/*      */       }
/*      */ 
/* 1207 */       this.mediator.setDefaultServant(paramServant);
/*      */     } finally {
/* 1209 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] activate_object(Servant paramServant)
/*      */     throws ServantAlreadyActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1221 */       lock();
/*      */ 
/* 1223 */       if (this.debug) {
/* 1224 */         ORBUtility.dprint(this, "Calling activate_object on poa " + this + " (servant=" + paramServant + ")");
/*      */       }
/*      */ 
/* 1232 */       byte[] arrayOfByte1 = this.mediator.newSystemId();
/*      */       try
/*      */       {
/* 1235 */         this.mediator.activateObject(arrayOfByte1, paramServant);
/*      */       }
/*      */       catch (ObjectAlreadyActive localObjectAlreadyActive)
/*      */       {
/*      */       }
/*      */ 
/* 1242 */       return arrayOfByte1;
/*      */     } finally {
/* 1244 */       if (this.debug) {
/* 1245 */         ORBUtility.dprint(this, "Exiting activate_object on poa " + this);
/*      */       }
/*      */ 
/* 1249 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void activate_object_with_id(byte[] paramArrayOfByte, Servant paramServant)
/*      */     throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1262 */       lock();
/*      */ 
/* 1264 */       if (this.debug) {
/* 1265 */         ORBUtility.dprint(this, "Calling activate_object_with_id on poa " + this + " (servant=" + paramServant + " id=" + paramArrayOfByte + ")");
/*      */       }
/*      */ 
/* 1272 */       byte[] arrayOfByte = (byte[])paramArrayOfByte.clone();
/*      */ 
/* 1274 */       this.mediator.activateObject(arrayOfByte, paramServant);
/*      */     } finally {
/* 1276 */       if (this.debug) {
/* 1277 */         ORBUtility.dprint(this, "Exiting activate_object_with_id on poa " + this);
/*      */       }
/*      */ 
/* 1281 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void deactivate_object(byte[] paramArrayOfByte)
/*      */     throws ObjectNotActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1293 */       lock();
/*      */ 
/* 1295 */       if (this.debug) {
/* 1296 */         ORBUtility.dprint(this, "Calling deactivate_object on poa " + this + " (id=" + paramArrayOfByte + ")");
/*      */       }
/*      */ 
/* 1301 */       this.mediator.deactivateObject(paramArrayOfByte);
/*      */     } finally {
/* 1303 */       if (this.debug) {
/* 1304 */         ORBUtility.dprint(this, "Exiting deactivate_object on poa " + this);
/*      */       }
/*      */ 
/* 1308 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object create_reference(String paramString)
/*      */     throws WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1320 */       lock();
/*      */ 
/* 1322 */       if (this.debug) {
/* 1323 */         ORBUtility.dprint(this, "Calling create_reference(repId=" + paramString + ") on poa " + this);
/*      */       }
/*      */ 
/* 1327 */       return makeObject(paramString, this.mediator.newSystemId());
/*      */     } finally {
/* 1329 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object create_reference_with_id(byte[] paramArrayOfByte, String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1341 */       lock();
/*      */ 
/* 1343 */       if (this.debug) {
/* 1344 */         ORBUtility.dprint(this, "Calling create_reference_with_id(oid=" + paramArrayOfByte + " repId=" + paramString + ") on poa " + this);
/*      */       }
/*      */ 
/* 1351 */       byte[] arrayOfByte = (byte[])paramArrayOfByte.clone();
/*      */ 
/* 1353 */       return makeObject(paramString, arrayOfByte);
/*      */     } finally {
/* 1355 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] servant_to_id(Servant paramServant)
/*      */     throws ServantNotActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1367 */       lock();
/*      */ 
/* 1369 */       if (this.debug) {
/* 1370 */         ORBUtility.dprint(this, "Calling servant_to_id(servant=" + paramServant + ") on poa " + this);
/*      */       }
/*      */ 
/* 1374 */       return this.mediator.servantToId(paramServant);
/*      */     } finally {
/* 1376 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object servant_to_reference(Servant paramServant)
/*      */     throws ServantNotActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1388 */       lock();
/*      */ 
/* 1390 */       if (this.debug) {
/* 1391 */         ORBUtility.dprint(this, "Calling servant_to_reference(servant=" + paramServant + ") on poa " + this);
/*      */       }
/*      */ 
/* 1396 */       byte[] arrayOfByte = this.mediator.servantToId(paramServant);
/* 1397 */       String str = paramServant._all_interfaces(this, arrayOfByte)[0];
/* 1398 */       return create_reference_with_id(arrayOfByte, str);
/*      */     } finally {
/* 1400 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Servant reference_to_servant(org.omg.CORBA.Object paramObject)
/*      */     throws ObjectNotActive, WrongPolicy, WrongAdapter
/*      */   {
/*      */     try
/*      */     {
/* 1412 */       lock();
/*      */ 
/* 1414 */       if (this.debug) {
/* 1415 */         ORBUtility.dprint(this, "Calling reference_to_servant(reference=" + paramObject + ") on poa " + this);
/*      */       }
/*      */ 
/* 1420 */       if (this.state >= 4) {
/* 1421 */         throw lifecycleWrapper().adapterDestroyed();
/*      */       }
/*      */ 
/* 1426 */       byte[] arrayOfByte = internalReferenceToId(paramObject);
/*      */ 
/* 1428 */       return this.mediator.idToServant(arrayOfByte);
/*      */     } finally {
/* 1430 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] reference_to_id(org.omg.CORBA.Object paramObject)
/*      */     throws WrongAdapter, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1442 */       lock();
/*      */ 
/* 1444 */       if (this.debug) {
/* 1445 */         ORBUtility.dprint(this, "Calling reference_to_id(reference=" + paramObject + ") on poa " + this);
/*      */       }
/*      */ 
/* 1449 */       if (this.state >= 4) {
/* 1450 */         throw lifecycleWrapper().adapterDestroyed();
/*      */       }
/*      */ 
/* 1453 */       return internalReferenceToId(paramObject);
/*      */     } finally {
/* 1455 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Servant id_to_servant(byte[] paramArrayOfByte)
/*      */     throws ObjectNotActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1467 */       lock();
/*      */ 
/* 1469 */       if (this.debug) {
/* 1470 */         ORBUtility.dprint(this, "Calling id_to_servant(id=" + paramArrayOfByte + ") on poa " + this);
/*      */       }
/*      */ 
/* 1474 */       if (this.state >= 4) {
/* 1475 */         throw lifecycleWrapper().adapterDestroyed();
/*      */       }
/* 1477 */       return this.mediator.idToServant(paramArrayOfByte);
/*      */     } finally {
/* 1479 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object id_to_reference(byte[] paramArrayOfByte)
/*      */     throws ObjectNotActive, WrongPolicy
/*      */   {
/*      */     try
/*      */     {
/* 1492 */       lock();
/*      */ 
/* 1494 */       if (this.debug) {
/* 1495 */         ORBUtility.dprint(this, "Calling id_to_reference(id=" + paramArrayOfByte + ") on poa " + this);
/*      */       }
/*      */ 
/* 1499 */       if (this.state >= 4) {
/* 1500 */         throw lifecycleWrapper().adapterDestroyed();
/*      */       }
/*      */ 
/* 1503 */       Servant localServant = this.mediator.idToServant(paramArrayOfByte);
/* 1504 */       String str = localServant._all_interfaces(this, paramArrayOfByte)[0];
/* 1505 */       return makeObject(str, paramArrayOfByte);
/*      */     } finally {
/* 1507 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] id()
/*      */   {
/*      */     try
/*      */     {
/* 1518 */       lock();
/*      */ 
/* 1520 */       return getAdapterId();
/*      */     } finally {
/* 1522 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Policy getEffectivePolicy(int paramInt)
/*      */   {
/* 1532 */     return this.mediator.getPolicies().get_effective_policy(paramInt);
/*      */   }
/*      */ 
/*      */   public int getManagerId()
/*      */   {
/* 1537 */     return this.manager.getManagerId();
/*      */   }
/*      */ 
/*      */   public short getState()
/*      */   {
/* 1542 */     return this.manager.getORTState();
/*      */   }
/*      */ 
/*      */   public String[] getInterfaces(java.lang.Object paramObject, byte[] paramArrayOfByte)
/*      */   {
/* 1547 */     Servant localServant = (Servant)paramObject;
/* 1548 */     return localServant._all_interfaces(this, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   protected ObjectCopierFactory getObjectCopierFactory()
/*      */   {
/* 1553 */     int i = this.mediator.getPolicies().getCopierId();
/* 1554 */     CopierManager localCopierManager = getORB().getCopierManager();
/* 1555 */     return localCopierManager.getObjectCopierFactory(i);
/*      */   }
/*      */ 
/*      */   public void enter() throws OADestroyed
/*      */   {
/*      */     try {
/* 1561 */       lock();
/*      */ 
/* 1563 */       if (this.debug) {
/* 1564 */         ORBUtility.dprint(this, "Calling enter on poa " + this);
/*      */       }
/*      */ 
/* 1572 */       while ((this.state == 4) && (this.isDestroying.get() == Boolean.FALSE)) {
/*      */         try
/*      */         {
/* 1575 */           this.beingDestroyedCV.await();
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*      */         }
/*      */       }
/* 1581 */       if (!waitUntilRunning()) {
/* 1582 */         throw new OADestroyed();
/*      */       }
/* 1584 */       this.invocationCount += 1;
/*      */     } finally {
/* 1586 */       if (this.debug) {
/* 1587 */         ORBUtility.dprint(this, "Exiting enter on poa " + this);
/*      */       }
/*      */ 
/* 1590 */       unlock();
/*      */     }
/*      */ 
/* 1593 */     this.manager.enter();
/*      */   }
/*      */ 
/*      */   public void exit()
/*      */   {
/*      */     try {
/* 1599 */       lock();
/*      */ 
/* 1601 */       if (this.debug) {
/* 1602 */         ORBUtility.dprint(this, "Calling exit on poa " + this);
/*      */       }
/*      */ 
/* 1605 */       this.invocationCount -= 1;
/*      */ 
/* 1607 */       if ((this.invocationCount == 0) && (this.state == 4))
/* 1608 */         this.invokeCV.broadcast();
/*      */     }
/*      */     finally {
/* 1611 */       if (this.debug) {
/* 1612 */         ORBUtility.dprint(this, "Exiting exit on poa " + this);
/*      */       }
/*      */ 
/* 1615 */       unlock();
/*      */     }
/*      */ 
/* 1618 */     this.manager.exit();
/*      */   }
/*      */ 
/*      */   public void getInvocationServant(OAInvocationInfo paramOAInvocationInfo)
/*      */   {
/*      */     try {
/* 1624 */       lock();
/*      */ 
/* 1626 */       if (this.debug) {
/* 1627 */         ORBUtility.dprint(this, "Calling getInvocationServant on poa " + this);
/*      */       }
/*      */ 
/* 1631 */       java.lang.Object localObject1 = null;
/*      */       try
/*      */       {
/* 1634 */         localObject1 = this.mediator.getInvocationServant(paramOAInvocationInfo.id(), paramOAInvocationInfo.getOperation());
/*      */       }
/*      */       catch (ForwardRequest localForwardRequest) {
/* 1637 */         throw new ForwardException(getORB(), localForwardRequest.forward_reference);
/*      */       }
/*      */ 
/* 1640 */       paramOAInvocationInfo.setServant(localObject1);
/*      */     } finally {
/* 1642 */       if (this.debug) {
/* 1643 */         ORBUtility.dprint(this, "Exiting getInvocationServant on poa " + this);
/*      */       }
/*      */ 
/* 1647 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object getLocalServant(byte[] paramArrayOfByte)
/*      */   {
/* 1653 */     return null;
/*      */   }
/*      */ 
/*      */   public void returnServant()
/*      */   {
/*      */     try
/*      */     {
/* 1664 */       lock();
/*      */ 
/* 1666 */       if (this.debug) {
/* 1667 */         ORBUtility.dprint(this, "Calling returnServant on poa " + this);
/*      */       }
/*      */ 
/* 1671 */       this.mediator.returnServant();
/*      */     } catch (Throwable localThrowable) {
/* 1673 */       if (this.debug) {
/* 1674 */         ORBUtility.dprint(this, "Exception " + localThrowable + " in returnServant on poa " + this);
/*      */       }
/*      */ 
/* 1678 */       if ((localThrowable instanceof Error))
/* 1679 */         throw ((Error)localThrowable);
/* 1680 */       if ((localThrowable instanceof RuntimeException))
/* 1681 */         throw ((RuntimeException)localThrowable);
/*      */     }
/*      */     finally {
/* 1684 */       if (this.debug) {
/* 1685 */         ORBUtility.dprint(this, "Exiting returnServant on poa " + this);
/*      */       }
/*      */ 
/* 1689 */       unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DestroyThread extends Thread
/*      */   {
/*      */     private boolean wait;
/*      */     private boolean etherealize;
/*      */     private boolean debug;
/*      */     private POAImpl thePoa;
/*      */ 
/*      */     public DestroyThread(boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  527 */       this.etherealize = paramBoolean1;
/*  528 */       this.debug = paramBoolean2;
/*      */     }
/*      */ 
/*      */     public void doIt(POAImpl paramPOAImpl, boolean paramBoolean)
/*      */     {
/*  533 */       if (this.debug) {
/*  534 */         ORBUtility.dprint(this, "Calling DestroyThread.doIt(thePOA=" + paramPOAImpl + " wait=" + paramBoolean + " etherealize=" + this.etherealize);
/*      */       }
/*      */ 
/*  539 */       this.thePoa = paramPOAImpl;
/*  540 */       this.wait = paramBoolean;
/*      */ 
/*  542 */       if (paramBoolean) {
/*  543 */         run();
/*      */       }
/*      */       else
/*      */       {
/*      */         try {
/*  548 */           setDaemon(true); } catch (Exception localException) {
/*  549 */         }start();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*  555 */       HashSet localHashSet = new HashSet();
/*      */ 
/*  557 */       performDestroy(this.thePoa, localHashSet);
/*      */ 
/*  559 */       Iterator localIterator = localHashSet.iterator();
/*  560 */       ObjectReferenceTemplate[] arrayOfObjectReferenceTemplate = new ObjectReferenceTemplate[localHashSet.size()];
/*      */ 
/*  562 */       int i = 0;
/*  563 */       while (localIterator.hasNext()) {
/*  564 */         arrayOfObjectReferenceTemplate[(i++)] = ((ObjectReferenceTemplate)localIterator.next());
/*      */       }
/*  566 */       this.thePoa.getORB().getPIHandler().adapterStateChanged(arrayOfObjectReferenceTemplate, (short)4);
/*      */     }
/*      */ 
/*      */     private boolean prepareForDestruction(POAImpl paramPOAImpl, Set paramSet)
/*      */     {
/*  576 */       POAImpl[] arrayOfPOAImpl = null;
/*      */       try
/*      */       {
/*  581 */         paramPOAImpl.lock();
/*      */ 
/*  583 */         if (this.debug) {
/*  584 */           ORBUtility.dprint(this, "Calling performDestroy on poa " + paramPOAImpl);
/*      */         }
/*      */ 
/*  588 */         if (paramPOAImpl.state <= 3) {
/*  589 */           paramPOAImpl.state = 4;
/*      */         }
/*      */         else
/*      */         {
/*  599 */           if (this.wait) {
/*  600 */             while (paramPOAImpl.state != 5)
/*      */               try {
/*  602 */                 paramPOAImpl.beingDestroyedCV.await();
/*      */               }
/*      */               catch (InterruptedException localInterruptedException)
/*      */               {
/*      */               }
/*      */           }
/*  608 */           return 0;
/*      */         }
/*      */ 
/*  611 */         paramPOAImpl.isDestroying.set(Boolean.TRUE);
/*      */ 
/*  615 */         arrayOfPOAImpl = (POAImpl[])paramPOAImpl.children.values().toArray(new POAImpl[0]);
/*      */       }
/*      */       finally {
/*  618 */         paramPOAImpl.unlock();
/*      */       }
/*      */ 
/*  625 */       for (int i = 0; i < arrayOfPOAImpl.length; i++) {
/*  626 */         performDestroy(arrayOfPOAImpl[i], paramSet);
/*      */       }
/*      */ 
/*  629 */       return true;
/*      */     }
/*      */ 
/*      */     public void performDestroy(POAImpl paramPOAImpl, Set paramSet)
/*      */     {
/*  634 */       if (!prepareForDestruction(paramPOAImpl, paramSet)) {
/*  635 */         return;
/*      */       }
/*      */ 
/*  643 */       POAImpl localPOAImpl = paramPOAImpl.parent;
/*  644 */       int i = localPOAImpl == null ? 1 : 0;
/*      */       try
/*      */       {
/*  650 */         if (i == 0)
/*  651 */           localPOAImpl.lock();
/*      */         try
/*      */         {
/*  654 */           paramPOAImpl.lock();
/*      */ 
/*  656 */           completeDestruction(paramPOAImpl, localPOAImpl, paramSet);
/*      */         }
/*      */         finally {
/*  659 */           paramPOAImpl.unlock();
/*      */ 
/*  661 */           if (i != 0)
/*      */           {
/*  666 */             paramPOAImpl.manager.getFactory().registerRootPOA();
/*      */           }
/*      */         }
/*      */       } finally { if (i == 0) {
/*  670 */           localPOAImpl.unlock();
/*  671 */           paramPOAImpl.parent = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void completeDestruction(POAImpl paramPOAImpl1, POAImpl paramPOAImpl2, Set paramSet)
/*      */     {
/*  679 */       if (this.debug) {
/*  680 */         ORBUtility.dprint(this, "Calling completeDestruction on poa " + paramPOAImpl1);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  685 */         while (paramPOAImpl1.invocationCount != 0) {
/*      */           try {
/*  687 */             paramPOAImpl1.invokeCV.await();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */         }
/*  693 */         if (paramPOAImpl1.mediator != null) {
/*  694 */           if (this.etherealize) {
/*  695 */             paramPOAImpl1.mediator.etherealizeAll();
/*      */           }
/*  697 */           paramPOAImpl1.mediator.clearAOM();
/*      */         }
/*      */ 
/*  700 */         if (paramPOAImpl1.manager != null) {
/*  701 */           paramPOAImpl1.manager.removePOA(paramPOAImpl1);
/*      */         }
/*  703 */         if (paramPOAImpl2 != null) {
/*  704 */           paramPOAImpl2.children.remove(paramPOAImpl1.name);
/*      */         }
/*  706 */         paramSet.add(paramPOAImpl1.getAdapterTemplate());
/*      */       } catch (Throwable localThrowable) {
/*  708 */         if ((localThrowable instanceof ThreadDeath)) {
/*  709 */           throw ((ThreadDeath)localThrowable);
/*      */         }
/*  711 */         paramPOAImpl1.lifecycleWrapper().unexpectedException(localThrowable, paramPOAImpl1.toString());
/*      */       } finally {
/*  713 */         paramPOAImpl1.state = 5;
/*  714 */         paramPOAImpl1.beingDestroyedCV.broadcast();
/*  715 */         paramPOAImpl1.isDestroying.set(Boolean.FALSE);
/*      */ 
/*  717 */         if (this.debug)
/*  718 */           ORBUtility.dprint(this, "Exiting completeDestruction on poa " + paramPOAImpl1);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAImpl
 * JD-Core Version:    0.6.2
 */