/*     */ package com.sun.corba.se.impl.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.dynamicany.DynAnyFactoryImpl;
/*     */ import com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl;
/*     */ import com.sun.corba.se.impl.legacy.connection.SocketFactoryContactInfoListImpl;
/*     */ import com.sun.corba.se.impl.legacy.connection.USLPort;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBConstants;
/*     */ import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.pept.transport.TransportManager;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.EndPointInfo;
/*     */ import com.sun.corba.se.spi.activation.Locator;
/*     */ import com.sun.corba.se.spi.activation.LocatorHelper;
/*     */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*     */ import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.oa.OADefault;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.DataCollector;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBConfigurator;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orb.Operation;
/*     */ import com.sun.corba.se.spi.orb.OperationFactory;
/*     */ import com.sun.corba.se.spi.orb.ParserImplBase;
/*     */ import com.sun.corba.se.spi.orb.PropertyParser;
/*     */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*     */ import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherDefault;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import com.sun.corba.se.spi.resolver.Resolver;
/*     */ import com.sun.corba.se.spi.resolver.ResolverDefault;
/*     */ import com.sun.corba.se.spi.servicecontext.CodeSetServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
/*     */ import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import com.sun.corba.se.spi.transport.TransportDefault;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ 
/*     */ public class ORBConfiguratorImpl
/*     */   implements ORBConfigurator
/*     */ {
/*     */   private ORBUtilSystemException wrapper;
/*     */   private static final int ORB_STREAM = 0;
/*     */ 
/*     */   public void configure(DataCollector paramDataCollector, ORB paramORB)
/*     */   {
/* 139 */     ORB localORB = paramORB;
/* 140 */     this.wrapper = ORBUtilSystemException.get(paramORB, "orb.lifecycle");
/*     */ 
/* 142 */     initObjectCopiers(localORB);
/* 143 */     initIORFinders(localORB);
/*     */ 
/* 145 */     localORB.setClientDelegateFactory(TransportDefault.makeClientDelegateFactory(localORB));
/*     */ 
/* 149 */     initializeTransport(localORB);
/*     */ 
/* 151 */     initializeNaming(localORB);
/* 152 */     initServiceContextRegistry(localORB);
/* 153 */     initRequestDispatcherRegistry(localORB);
/* 154 */     registerInitialReferences(localORB);
/*     */ 
/* 156 */     persistentServerInitialization(localORB);
/*     */ 
/* 158 */     runUserConfigurators(paramDataCollector, localORB);
/*     */   }
/*     */ 
/*     */   private void runUserConfigurators(DataCollector paramDataCollector, ORB paramORB)
/*     */   {
/* 166 */     ConfigParser localConfigParser = new ConfigParser();
/* 167 */     localConfigParser.init(paramDataCollector);
/* 168 */     if (localConfigParser.userConfigurators != null)
/* 169 */       for (int i = 0; i < localConfigParser.userConfigurators.length; i++) {
/* 170 */         Class localClass = localConfigParser.userConfigurators[i];
/*     */         try {
/* 172 */           ORBConfigurator localORBConfigurator = (ORBConfigurator)localClass.newInstance();
/* 173 */           localORBConfigurator.configure(paramDataCollector, paramORB);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void persistentServerInitialization(ORB paramORB)
/*     */   {
/* 184 */     ORBData localORBData = paramORB.getORBData();
/*     */ 
/* 188 */     if (localORBData.getServerIsORBActivated())
/*     */       try {
/* 190 */         Locator localLocator = LocatorHelper.narrow(paramORB.resolve_initial_references("ServerLocator"));
/*     */ 
/* 193 */         Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 196 */         Collection localCollection = paramORB.getCorbaTransportManager().getAcceptors(null, null);
/*     */ 
/* 198 */         EndPointInfo[] arrayOfEndPointInfo = new EndPointInfo[localCollection.size()];
/*     */ 
/* 200 */         Iterator localIterator = localCollection.iterator();
/* 201 */         int i = 0;
/* 202 */         while (localIterator.hasNext()) {
/* 203 */           Object localObject = localIterator.next();
/* 204 */           if ((localObject instanceof LegacyServerSocketEndPointInfo))
/*     */           {
/* 207 */             LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = (LegacyServerSocketEndPointInfo)localObject;
/*     */ 
/* 210 */             int j = localLocator.getEndpoint(localLegacyServerSocketEndPointInfo.getType());
/* 211 */             if (j == -1) {
/* 212 */               j = localLocator.getEndpoint("IIOP_CLEAR_TEXT");
/* 213 */               if (j == -1) {
/* 214 */                 throw new Exception("ORBD must support IIOP_CLEAR_TEXT");
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 219 */             localLegacyServerSocketEndPointInfo.setLocatorPort(j);
/*     */ 
/* 221 */             arrayOfEndPointInfo[(i++)] = new EndPointInfo(localLegacyServerSocketEndPointInfo.getType(), localLegacyServerSocketEndPointInfo.getPort());
/*     */           }
/*     */         }
/*     */ 
/* 225 */         localActivator.registerEndpoints(localORBData.getPersistentServerId(), localORBData.getORBId(), arrayOfEndPointInfo);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 229 */         throw this.wrapper.persistentServerInitError(CompletionStatus.COMPLETED_MAYBE, localException);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void initializeTransport(final ORB paramORB)
/*     */   {
/* 242 */     ORBData localORBData = paramORB.getORBData();
/*     */ 
/* 244 */     Object localObject = localORBData.getCorbaContactInfoListFactory();
/*     */ 
/* 246 */     Acceptor[] arrayOfAcceptor = localORBData.getAcceptors();
/*     */ 
/* 249 */     com.sun.corba.se.spi.legacy.connection.ORBSocketFactory localORBSocketFactory = localORBData.getLegacySocketFactory();
/* 250 */     USLPort[] arrayOfUSLPort1 = localORBData.getUserSpecifiedListenPorts();
/* 251 */     setLegacySocketFactoryORB(paramORB, localORBSocketFactory);
/*     */ 
/* 258 */     if ((localORBSocketFactory != null) && (localObject != null)) {
/* 259 */       throw this.wrapper.socketFactoryAndContactInfoListAtSameTime();
/*     */     }
/*     */ 
/* 262 */     if ((arrayOfAcceptor.length != 0) && (localORBSocketFactory != null)) {
/* 263 */       throw this.wrapper.acceptorsAndLegacySocketFactoryAtSameTime();
/*     */     }
/*     */ 
/* 271 */     localORBData.getSocketFactory().setORB(paramORB);
/*     */ 
/* 277 */     if (localORBSocketFactory != null)
/*     */     {
/* 281 */       localObject = new CorbaContactInfoListFactory() {
/*     */         public void setORB(ORB paramAnonymousORB) {
/*     */         }
/*     */         public CorbaContactInfoList create(IOR paramAnonymousIOR) {
/* 285 */           return new SocketFactoryContactInfoListImpl(paramORB, paramAnonymousIOR);
/*     */         }
/*     */ 
/*     */       };
/*     */     }
/* 290 */     else if (localObject != null)
/*     */     {
/* 292 */       ((CorbaContactInfoListFactory)localObject).setORB(paramORB);
/*     */     }
/*     */     else {
/* 295 */       localObject = TransportDefault.makeCorbaContactInfoListFactory(paramORB);
/*     */     }
/*     */ 
/* 298 */     paramORB.setCorbaContactInfoListFactory((CorbaContactInfoListFactory)localObject);
/*     */ 
/* 315 */     int i = -1;
/* 316 */     if (localORBData.getORBServerPort() != 0)
/* 317 */       i = localORBData.getORBServerPort();
/* 318 */     else if (localORBData.getPersistentPortInitialized())
/* 319 */       i = localORBData.getPersistentServerPort();
/* 320 */     else if (arrayOfAcceptor.length == 0) {
/* 321 */       i = 0;
/*     */     }
/* 323 */     if (i != -1) {
/* 324 */       createAndRegisterAcceptor(paramORB, localORBSocketFactory, i, "DEFAULT_ENDPOINT", "IIOP_CLEAR_TEXT");
/*     */     }
/*     */ 
/* 330 */     for (int j = 0; j < arrayOfAcceptor.length; j++) {
/* 331 */       paramORB.getCorbaTransportManager().registerAcceptor(arrayOfAcceptor[j]);
/*     */     }
/*     */ 
/* 336 */     USLPort[] arrayOfUSLPort2 = localORBData.getUserSpecifiedListenPorts();
/* 337 */     if (arrayOfUSLPort2 != null)
/* 338 */       for (int k = 0; k < arrayOfUSLPort2.length; k++)
/* 339 */         createAndRegisterAcceptor(paramORB, localORBSocketFactory, arrayOfUSLPort2[k].getPort(), "NO_NAME", arrayOfUSLPort2[k].getType());
/*     */   }
/*     */ 
/*     */   private void createAndRegisterAcceptor(ORB paramORB, com.sun.corba.se.spi.legacy.connection.ORBSocketFactory paramORBSocketFactory, int paramInt, String paramString1, String paramString2)
/*     */   {
/*     */     Object localObject;
/* 357 */     if (paramORBSocketFactory == null) {
/* 358 */       localObject = new SocketOrChannelAcceptorImpl(paramORB, paramInt, paramString1, paramString2);
/*     */     }
/*     */     else {
/* 361 */       localObject = new SocketFactoryAcceptorImpl(paramORB, paramInt, paramString1, paramString2);
/*     */     }
/*     */ 
/* 364 */     paramORB.getTransportManager().registerAcceptor((Acceptor)localObject);
/*     */   }
/*     */ 
/*     */   private void setLegacySocketFactoryORB(final ORB paramORB, final com.sun.corba.se.spi.legacy.connection.ORBSocketFactory paramORBSocketFactory)
/*     */   {
/* 370 */     if (paramORBSocketFactory == null) {
/* 371 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 380 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws InstantiationException, IllegalAccessException
/*     */         {
/*     */           try
/*     */           {
/* 386 */             Class[] arrayOfClass = { ORB.class };
/* 387 */             localObject = paramORBSocketFactory.getClass().getMethod("setORB", arrayOfClass);
/*     */ 
/* 390 */             Object[] arrayOfObject = { paramORB };
/* 391 */             ((Method)localObject).invoke(paramORBSocketFactory, arrayOfObject);
/*     */           }
/*     */           catch (NoSuchMethodException localNoSuchMethodException)
/*     */           {
/*     */           }
/*     */           catch (IllegalAccessException localIllegalAccessException) {
/* 397 */             localObject = new RuntimeException();
/* 398 */             ((RuntimeException)localObject).initCause(localIllegalAccessException);
/* 399 */             throw ((Throwable)localObject);
/*     */           } catch (InvocationTargetException localInvocationTargetException) {
/* 401 */             Object localObject = new RuntimeException();
/* 402 */             ((RuntimeException)localObject).initCause(localInvocationTargetException);
/* 403 */             throw ((Throwable)localObject);
/*     */           }
/* 405 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 410 */       throw this.wrapper.unableToSetSocketFactoryOrb(localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeNaming(ORB paramORB)
/*     */   {
/* 416 */     LocalResolver localLocalResolver = ResolverDefault.makeLocalResolver();
/* 417 */     paramORB.setLocalResolver(localLocalResolver);
/*     */ 
/* 419 */     Resolver localResolver1 = ResolverDefault.makeBootstrapResolver(paramORB, paramORB.getORBData().getORBInitialHost(), paramORB.getORBData().getORBInitialPort());
/*     */ 
/* 423 */     Operation localOperation = ResolverDefault.makeINSURLOperation(paramORB, localResolver1);
/*     */ 
/* 425 */     paramORB.setURLOperation(localOperation);
/*     */ 
/* 427 */     Resolver localResolver2 = ResolverDefault.makeORBInitRefResolver(localOperation, paramORB.getORBData().getORBInitialReferences());
/*     */ 
/* 430 */     Resolver localResolver3 = ResolverDefault.makeORBDefaultInitRefResolver(localOperation, paramORB.getORBData().getORBDefaultInitialReference());
/*     */ 
/* 433 */     Resolver localResolver4 = ResolverDefault.makeCompositeResolver(localLocalResolver, ResolverDefault.makeCompositeResolver(localResolver2, ResolverDefault.makeCompositeResolver(localResolver3, localResolver1)));
/*     */ 
/* 438 */     paramORB.setResolver(localResolver4);
/*     */   }
/*     */ 
/*     */   private void initServiceContextRegistry(ORB paramORB)
/*     */   {
/* 443 */     ServiceContextRegistry localServiceContextRegistry = paramORB.getServiceContextRegistry();
/*     */ 
/* 445 */     localServiceContextRegistry.register(UEInfoServiceContext.class);
/* 446 */     localServiceContextRegistry.register(CodeSetServiceContext.class);
/* 447 */     localServiceContextRegistry.register(SendingContextServiceContext.class);
/* 448 */     localServiceContextRegistry.register(ORBVersionServiceContext.class);
/* 449 */     localServiceContextRegistry.register(MaxStreamFormatVersionServiceContext.class);
/*     */   }
/*     */ 
/*     */   private void registerInitialReferences(final ORB paramORB)
/*     */   {
/* 455 */     Closure local3 = new Closure() {
/*     */       public Object evaluate() {
/* 457 */         return new DynAnyFactoryImpl(paramORB);
/*     */       }
/*     */     };
/* 461 */     Closure localClosure = ClosureFactory.makeFuture(local3);
/* 462 */     paramORB.getLocalResolver().register("DynAnyFactory", localClosure);
/*     */   }
/*     */ 
/*     */   private void initObjectCopiers(ORB paramORB)
/*     */   {
/* 471 */     ObjectCopierFactory localObjectCopierFactory = CopyobjectDefaults.makeORBStreamObjectCopierFactory(paramORB);
/*     */ 
/* 474 */     CopierManager localCopierManager = paramORB.getCopierManager();
/* 475 */     localCopierManager.setDefaultId(0);
/*     */ 
/* 477 */     localCopierManager.registerObjectCopierFactory(localObjectCopierFactory, 0);
/*     */   }
/*     */ 
/*     */   private void initIORFinders(ORB paramORB)
/*     */   {
/* 482 */     IdentifiableFactoryFinder localIdentifiableFactoryFinder1 = paramORB.getTaggedProfileFactoryFinder();
/*     */ 
/* 484 */     localIdentifiableFactoryFinder1.registerFactory(IIOPFactories.makeIIOPProfileFactory());
/*     */ 
/* 486 */     IdentifiableFactoryFinder localIdentifiableFactoryFinder2 = paramORB.getTaggedProfileTemplateFactoryFinder();
/*     */ 
/* 488 */     localIdentifiableFactoryFinder2.registerFactory(IIOPFactories.makeIIOPProfileTemplateFactory());
/*     */ 
/* 491 */     TaggedComponentFactoryFinder localTaggedComponentFactoryFinder = paramORB.getTaggedComponentFactoryFinder();
/*     */ 
/* 493 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeCodeSetsComponentFactory());
/*     */ 
/* 495 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeJavaCodebaseComponentFactory());
/*     */ 
/* 497 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeORBTypeComponentFactory());
/*     */ 
/* 499 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeMaxStreamFormatVersionComponentFactory());
/*     */ 
/* 501 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeAlternateIIOPAddressComponentFactory());
/*     */ 
/* 503 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeRequestPartitioningComponentFactory());
/*     */ 
/* 505 */     localTaggedComponentFactoryFinder.registerFactory(IIOPFactories.makeJavaSerializationComponentFactory());
/*     */ 
/* 509 */     IORFactories.registerValueFactories(paramORB);
/*     */ 
/* 512 */     paramORB.setObjectKeyFactory(IORFactories.makeObjectKeyFactory(paramORB));
/*     */   }
/*     */ 
/*     */   private void initRequestDispatcherRegistry(ORB paramORB)
/*     */   {
/* 517 */     RequestDispatcherRegistry localRequestDispatcherRegistry = paramORB.getRequestDispatcherRegistry();
/*     */ 
/* 520 */     ClientRequestDispatcher localClientRequestDispatcher = RequestDispatcherDefault.makeClientRequestDispatcher();
/*     */ 
/* 522 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, 2);
/*     */ 
/* 524 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, 32);
/*     */ 
/* 526 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, ORBConstants.PERSISTENT_SCID);
/*     */ 
/* 528 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, 36);
/*     */ 
/* 530 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, ORBConstants.SC_PERSISTENT_SCID);
/*     */ 
/* 532 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, 40);
/*     */ 
/* 534 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, ORBConstants.IISC_PERSISTENT_SCID);
/*     */ 
/* 536 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, 44);
/*     */ 
/* 538 */     localRequestDispatcherRegistry.registerClientRequestDispatcher(localClientRequestDispatcher, ORBConstants.MINSC_PERSISTENT_SCID);
/*     */ 
/* 542 */     CorbaServerRequestDispatcher localCorbaServerRequestDispatcher1 = RequestDispatcherDefault.makeServerRequestDispatcher(paramORB);
/*     */ 
/* 544 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, 2);
/*     */ 
/* 546 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, 32);
/*     */ 
/* 548 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, ORBConstants.PERSISTENT_SCID);
/*     */ 
/* 550 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, 36);
/*     */ 
/* 552 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, ORBConstants.SC_PERSISTENT_SCID);
/*     */ 
/* 554 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, 40);
/*     */ 
/* 556 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, ORBConstants.IISC_PERSISTENT_SCID);
/*     */ 
/* 558 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, 44);
/*     */ 
/* 560 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher1, ORBConstants.MINSC_PERSISTENT_SCID);
/*     */ 
/* 563 */     paramORB.setINSDelegate(RequestDispatcherDefault.makeINSServerRequestDispatcher(paramORB));
/*     */ 
/* 567 */     LocalClientRequestDispatcherFactory localLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeJIDLLocalClientRequestDispatcherFactory(paramORB);
/*     */ 
/* 570 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, 2);
/*     */ 
/* 573 */     localLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makePOALocalClientRequestDispatcherFactory(paramORB);
/*     */ 
/* 576 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, 32);
/*     */ 
/* 578 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, ORBConstants.PERSISTENT_SCID);
/*     */ 
/* 581 */     localLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeFullServantCacheLocalClientRequestDispatcherFactory(paramORB);
/*     */ 
/* 583 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, 36);
/*     */ 
/* 585 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, ORBConstants.SC_PERSISTENT_SCID);
/*     */ 
/* 588 */     localLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeInfoOnlyServantCacheLocalClientRequestDispatcherFactory(paramORB);
/*     */ 
/* 590 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, 40);
/*     */ 
/* 592 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, ORBConstants.IISC_PERSISTENT_SCID);
/*     */ 
/* 595 */     localLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeMinimalServantCacheLocalClientRequestDispatcherFactory(paramORB);
/*     */ 
/* 597 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, 44);
/*     */ 
/* 599 */     localRequestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localLocalClientRequestDispatcherFactory, ORBConstants.MINSC_PERSISTENT_SCID);
/*     */ 
/* 606 */     CorbaServerRequestDispatcher localCorbaServerRequestDispatcher2 = RequestDispatcherDefault.makeBootstrapServerRequestDispatcher(paramORB);
/*     */ 
/* 609 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher2, "INIT");
/* 610 */     localRequestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher2, "TINI");
/*     */ 
/* 613 */     ObjectAdapterFactory localObjectAdapterFactory = OADefault.makeTOAFactory(paramORB);
/* 614 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, 2);
/*     */ 
/* 616 */     localObjectAdapterFactory = OADefault.makePOAFactory(paramORB);
/* 617 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, 32);
/* 618 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, ORBConstants.PERSISTENT_SCID);
/* 619 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, 36);
/* 620 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, ORBConstants.SC_PERSISTENT_SCID);
/* 621 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, 40);
/* 622 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, ORBConstants.IISC_PERSISTENT_SCID);
/* 623 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, 44);
/* 624 */     localRequestDispatcherRegistry.registerObjectAdapterFactory(localObjectAdapterFactory, ORBConstants.MINSC_PERSISTENT_SCID);
/*     */   }
/*     */ 
/*     */   public static class ConfigParser extends ParserImplBase
/*     */   {
/* 122 */     public Class[] userConfigurators = null;
/*     */ 
/*     */     public PropertyParser makeParser()
/*     */     {
/* 126 */       PropertyParser localPropertyParser = new PropertyParser();
/* 127 */       Operation localOperation = OperationFactory.compose(OperationFactory.suffixAction(), OperationFactory.classAction());
/*     */ 
/* 131 */       localPropertyParser.addPrefix("com.sun.CORBA.ORBUserConfigurators", localOperation, "userConfigurators", Class.class);
/*     */ 
/* 133 */       return localPropertyParser;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ORBConfiguratorImpl
 * JD-Core Version:    0.6.2
 */