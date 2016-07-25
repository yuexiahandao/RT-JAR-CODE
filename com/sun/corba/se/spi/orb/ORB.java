/*     */ package com.sun.corba.se.spi.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.TypeCodeFactory;
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
/*     */ import com.sun.corba.se.impl.presentation.rmi.PresentationManagerImpl;
/*     */ import com.sun.corba.se.impl.transport.ByteBufferPoolImpl;
/*     */ import com.sun.corba.se.pept.broker.Broker;
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.logging.LogWrapperBase;
/*     */ import com.sun.corba.se.spi.logging.LogWrapperFactory;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManagerFactory;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*     */ import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import com.sun.corba.se.spi.resolver.Resolver;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Logger;
/*     */ import sun.awt.AppContext;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public abstract class ORB extends com.sun.corba.se.org.omg.CORBA.ORB
/*     */   implements Broker, TypeCodeFactory
/*     */ {
/* 119 */   public static boolean ORBInitDebug = false;
/*     */ 
/* 125 */   public boolean transportDebugFlag = false;
/* 126 */   public boolean subcontractDebugFlag = false;
/* 127 */   public boolean poaDebugFlag = false;
/* 128 */   public boolean poaConcurrencyDebugFlag = false;
/* 129 */   public boolean poaFSMDebugFlag = false;
/* 130 */   public boolean orbdDebugFlag = false;
/* 131 */   public boolean namingDebugFlag = false;
/* 132 */   public boolean serviceContextDebugFlag = false;
/* 133 */   public boolean transientObjectManagerDebugFlag = false;
/* 134 */   public boolean giopVersionDebugFlag = false;
/* 135 */   public boolean shutdownDebugFlag = false;
/* 136 */   public boolean giopDebugFlag = false;
/* 137 */   public boolean invocationTimingDebugFlag = false;
/*     */   protected static ORBUtilSystemException staticWrapper;
/*     */   protected ORBUtilSystemException wrapper;
/*     */   protected OMGSystemException omgWrapper;
/*     */   private Map typeCodeMap;
/*     */   private TypeCodeImpl[] primitiveTypeCodeConstants;
/*     */   ByteBufferPool byteBufferPool;
/*     */   private Map wrapperMap;
/* 177 */   private static final Object pmLock = new Object();
/*     */ 
/* 179 */   private static Map staticWrapperMap = new ConcurrentHashMap();
/*     */   protected MonitoringManager monitoringManager;
/*     */ 
/*     */   public abstract boolean isLocalHost(String paramString);
/*     */ 
/*     */   public abstract boolean isLocalServerId(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract OAInvocationInfo peekInvocationInfo();
/*     */ 
/*     */   public abstract void pushInvocationInfo(OAInvocationInfo paramOAInvocationInfo);
/*     */ 
/*     */   public abstract OAInvocationInfo popInvocationInfo();
/*     */ 
/*     */   public abstract CorbaTransportManager getCorbaTransportManager();
/*     */ 
/*     */   public abstract LegacyServerSocketManager getLegacyServerSocketManager();
/*     */ 
/*     */   private static PresentationManager setupPresentationManager()
/*     */   {
/* 184 */     staticWrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */ 
/* 187 */     boolean bool = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 191 */         return Boolean.valueOf(Boolean.getBoolean("com.sun.CORBA.ORBUseDynamicStub"));
/*     */       }
/*     */     })).booleanValue();
/*     */ 
/* 197 */     PresentationManager.StubFactoryFactory localStubFactoryFactory = (PresentationManager.StubFactoryFactory)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 201 */         PresentationManager.StubFactoryFactory localStubFactoryFactory = PresentationDefaults.getProxyStubFactoryFactory();
/*     */ 
/* 204 */         String str = System.getProperty("com.sun.CORBA.ORBDynamicStubFactoryFactoryClass", "com.sun.corba.se.impl.presentation.rmi.bcel.StubFactoryFactoryBCELImpl");
/*     */         try
/*     */         {
/* 210 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/* 211 */           localStubFactoryFactory = (PresentationManager.StubFactoryFactory)localClass.newInstance();
/*     */         }
/*     */         catch (Exception localException) {
/* 214 */           ORB.staticWrapper.errorInSettingDynamicStubFactoryFactory(localException, str);
/*     */         }
/*     */ 
/* 218 */         return localStubFactoryFactory;
/*     */       }
/*     */     });
/* 223 */     PresentationManagerImpl localPresentationManagerImpl = new PresentationManagerImpl(bool);
/* 224 */     localPresentationManagerImpl.setStubFactoryFactory(false, PresentationDefaults.getStaticStubFactoryFactory());
/*     */ 
/* 226 */     localPresentationManagerImpl.setStubFactoryFactory(true, localStubFactoryFactory);
/* 227 */     return localPresentationManagerImpl;
/*     */   }
/*     */ 
/*     */   public void destroy() {
/* 231 */     this.wrapper = null;
/* 232 */     this.omgWrapper = null;
/* 233 */     this.typeCodeMap = null;
/* 234 */     this.primitiveTypeCodeConstants = null;
/* 235 */     this.byteBufferPool = null;
/*     */   }
/*     */ 
/*     */   public static PresentationManager getPresentationManager()
/*     */   {
/* 244 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 245 */     if ((localSecurityManager != null) && (AppContext.getAppContexts().size() > 0)) {
/* 246 */       AppContext localAppContext = AppContext.getAppContext();
/* 247 */       if (localAppContext != null) {
/* 248 */         synchronized (pmLock) {
/* 249 */           PresentationManager localPresentationManager = (PresentationManager)localAppContext.get(PresentationManager.class);
/*     */ 
/* 251 */           if (localPresentationManager == null) {
/* 252 */             localPresentationManager = setupPresentationManager();
/* 253 */             localAppContext.put(PresentationManager.class, localPresentationManager);
/*     */           }
/* 255 */           return localPresentationManager;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 261 */     return Holder.defaultPresentationManager;
/*     */   }
/*     */ 
/*     */   public static PresentationManager.StubFactoryFactory getStubFactoryFactory()
/*     */   {
/* 271 */     PresentationManager localPresentationManager = getPresentationManager();
/* 272 */     boolean bool = localPresentationManager.useDynamicStubs();
/* 273 */     return localPresentationManager.getStubFactoryFactory(bool);
/*     */   }
/*     */ 
/*     */   protected ORB()
/*     */   {
/* 280 */     this.wrapperMap = new ConcurrentHashMap();
/* 281 */     this.wrapper = ORBUtilSystemException.get(this, "rpc.presentation");
/*     */ 
/* 283 */     this.omgWrapper = OMGSystemException.get(this, "rpc.presentation");
/*     */ 
/* 286 */     this.typeCodeMap = new HashMap();
/*     */ 
/* 288 */     this.primitiveTypeCodeConstants = new TypeCodeImpl[] { new TypeCodeImpl(this, 0), new TypeCodeImpl(this, 1), new TypeCodeImpl(this, 2), new TypeCodeImpl(this, 3), new TypeCodeImpl(this, 4), new TypeCodeImpl(this, 5), new TypeCodeImpl(this, 6), new TypeCodeImpl(this, 7), new TypeCodeImpl(this, 8), new TypeCodeImpl(this, 9), new TypeCodeImpl(this, 10), new TypeCodeImpl(this, 11), new TypeCodeImpl(this, 12), new TypeCodeImpl(this, 13), new TypeCodeImpl(this, 14), null, null, null, new TypeCodeImpl(this, 18), null, null, null, null, new TypeCodeImpl(this, 23), new TypeCodeImpl(this, 24), new TypeCodeImpl(this, 25), new TypeCodeImpl(this, 26), new TypeCodeImpl(this, 27), new TypeCodeImpl(this, 28), new TypeCodeImpl(this, 29), new TypeCodeImpl(this, 30), new TypeCodeImpl(this, 31), new TypeCodeImpl(this, 32) };
/*     */ 
/* 324 */     this.monitoringManager = MonitoringFactories.getMonitoringManagerFactory().createMonitoringManager("orb", "ORB Management and Monitoring Root");
/*     */   }
/*     */ 
/*     */   public TypeCodeImpl get_primitive_tc(int paramInt)
/*     */   {
/* 334 */     synchronized (this) {
/* 335 */       checkShutdownState();
/*     */     }
/*     */     try {
/* 338 */       return this.primitiveTypeCodeConstants[paramInt];
/*     */     } catch (Throwable localThrowable) {
/* 340 */       throw this.wrapper.invalidTypecodeKind(localThrowable, new Integer(paramInt));
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setTypeCode(String paramString, TypeCodeImpl paramTypeCodeImpl)
/*     */   {
/* 346 */     checkShutdownState();
/* 347 */     this.typeCodeMap.put(paramString, paramTypeCodeImpl);
/*     */   }
/*     */ 
/*     */   public synchronized TypeCodeImpl getTypeCode(String paramString)
/*     */   {
/* 352 */     checkShutdownState();
/* 353 */     return (TypeCodeImpl)this.typeCodeMap.get(paramString);
/*     */   }
/*     */ 
/*     */   public MonitoringManager getMonitoringManager() {
/* 357 */     synchronized (this) {
/* 358 */       checkShutdownState();
/*     */     }
/* 360 */     return this.monitoringManager;
/*     */   }
/*     */ 
/*     */   public abstract void set_parameters(Properties paramProperties);
/*     */ 
/*     */   public abstract ORBVersion getORBVersion();
/*     */ 
/*     */   public abstract void setORBVersion(ORBVersion paramORBVersion);
/*     */ 
/*     */   public abstract IOR getFVDCodeBaseIOR();
/*     */ 
/*     */   public abstract void handleBadServerId(ObjectKey paramObjectKey);
/*     */ 
/*     */   public abstract void setBadServerIdHandler(BadServerIdHandler paramBadServerIdHandler);
/*     */ 
/*     */   public abstract void initBadServerIdHandler();
/*     */ 
/*     */   public abstract void notifyORB();
/*     */ 
/*     */   public abstract PIHandler getPIHandler();
/*     */ 
/*     */   public abstract void checkShutdownState();
/*     */ 
/*     */   public abstract boolean isDuringDispatch();
/*     */ 
/*     */   public abstract void startingDispatch();
/*     */ 
/*     */   public abstract void finishedDispatch();
/*     */ 
/*     */   public abstract int getTransientServerId();
/*     */ 
/*     */   public abstract ServiceContextRegistry getServiceContextRegistry();
/*     */ 
/*     */   public abstract RequestDispatcherRegistry getRequestDispatcherRegistry();
/*     */ 
/*     */   public abstract ORBData getORBData();
/*     */ 
/*     */   public abstract void setClientDelegateFactory(ClientDelegateFactory paramClientDelegateFactory);
/*     */ 
/*     */   public abstract ClientDelegateFactory getClientDelegateFactory();
/*     */ 
/*     */   public abstract void setCorbaContactInfoListFactory(CorbaContactInfoListFactory paramCorbaContactInfoListFactory);
/*     */ 
/*     */   public abstract CorbaContactInfoListFactory getCorbaContactInfoListFactory();
/*     */ 
/*     */   public abstract void setResolver(Resolver paramResolver);
/*     */ 
/*     */   public abstract Resolver getResolver();
/*     */ 
/*     */   public abstract void setLocalResolver(LocalResolver paramLocalResolver);
/*     */ 
/*     */   public abstract LocalResolver getLocalResolver();
/*     */ 
/*     */   public abstract void setURLOperation(Operation paramOperation);
/*     */ 
/*     */   public abstract Operation getURLOperation();
/*     */ 
/*     */   public abstract void setINSDelegate(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher);
/*     */ 
/*     */   public abstract TaggedComponentFactoryFinder getTaggedComponentFactoryFinder();
/*     */ 
/*     */   public abstract IdentifiableFactoryFinder getTaggedProfileFactoryFinder();
/*     */ 
/*     */   public abstract IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder();
/*     */ 
/*     */   public abstract ObjectKeyFactory getObjectKeyFactory();
/*     */ 
/*     */   public abstract void setObjectKeyFactory(ObjectKeyFactory paramObjectKeyFactory);
/*     */ 
/*     */   public Logger getLogger(String paramString)
/*     */   {
/* 474 */     synchronized (this) {
/* 475 */       checkShutdownState();
/*     */     }
/* 477 */     ??? = getORBData();
/*     */     String str;
/* 489 */     if (??? == null) {
/* 490 */       str = "_INITIALIZING_";
/*     */     } else {
/* 492 */       str = ((ORBData)???).getORBId();
/* 493 */       if (str.equals("")) {
/* 494 */         str = "_DEFAULT_";
/*     */       }
/*     */     }
/* 497 */     return getCORBALogger(str, paramString);
/*     */   }
/*     */ 
/*     */   public static Logger staticGetLogger(String paramString)
/*     */   {
/* 502 */     return getCORBALogger("_CORBA_", paramString);
/*     */   }
/*     */ 
/*     */   private static Logger getCORBALogger(String paramString1, String paramString2)
/*     */   {
/* 507 */     String str = "javax.enterprise.resource.corba." + paramString1 + "." + paramString2;
/*     */ 
/* 510 */     return Logger.getLogger(str, "com.sun.corba.se.impl.logging.LogStrings");
/*     */   }
/*     */ 
/*     */   public LogWrapperBase getLogWrapper(String paramString1, String paramString2, LogWrapperFactory paramLogWrapperFactory)
/*     */   {
/* 519 */     StringPair localStringPair = new StringPair(paramString1, paramString2);
/*     */ 
/* 521 */     LogWrapperBase localLogWrapperBase = (LogWrapperBase)this.wrapperMap.get(localStringPair);
/* 522 */     if (localLogWrapperBase == null) {
/* 523 */       localLogWrapperBase = paramLogWrapperFactory.create(getLogger(paramString1));
/* 524 */       this.wrapperMap.put(localStringPair, localLogWrapperBase);
/*     */     }
/*     */ 
/* 527 */     return localLogWrapperBase;
/*     */   }
/*     */ 
/*     */   public static LogWrapperBase staticGetLogWrapper(String paramString1, String paramString2, LogWrapperFactory paramLogWrapperFactory)
/*     */   {
/* 536 */     StringPair localStringPair = new StringPair(paramString1, paramString2);
/*     */ 
/* 538 */     LogWrapperBase localLogWrapperBase = (LogWrapperBase)staticWrapperMap.get(localStringPair);
/* 539 */     if (localLogWrapperBase == null) {
/* 540 */       localLogWrapperBase = paramLogWrapperFactory.create(staticGetLogger(paramString1));
/* 541 */       staticWrapperMap.put(localStringPair, localLogWrapperBase);
/*     */     }
/*     */ 
/* 544 */     return localLogWrapperBase;
/*     */   }
/*     */ 
/*     */   public ByteBufferPool getByteBufferPool()
/*     */   {
/* 553 */     synchronized (this) {
/* 554 */       checkShutdownState();
/*     */     }
/* 556 */     if (this.byteBufferPool == null) {
/* 557 */       this.byteBufferPool = new ByteBufferPoolImpl(this);
/*     */     }
/* 559 */     return this.byteBufferPool;
/*     */   }
/*     */ 
/*     */   public abstract void setThreadPoolManager(ThreadPoolManager paramThreadPoolManager);
/*     */ 
/*     */   public abstract ThreadPoolManager getThreadPoolManager();
/*     */ 
/*     */   public abstract CopierManager getCopierManager();
/*     */ 
/*     */   static class Holder
/*     */   {
/* 174 */     static final PresentationManager defaultPresentationManager = ORB.access$000();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ORB
 * JD-Core Version:    0.6.2
 */