/*      */ package com.sun.corba.se.impl.orb;
/*      */ 
/*      */ import com.sun.corba.se.impl.copyobject.CopierManagerImpl;
/*      */ import com.sun.corba.se.impl.corba.AnyImpl;
/*      */ import com.sun.corba.se.impl.corba.AsynchInvoke;
/*      */ import com.sun.corba.se.impl.corba.ContextListImpl;
/*      */ import com.sun.corba.se.impl.corba.EnvironmentImpl;
/*      */ import com.sun.corba.se.impl.corba.ExceptionListImpl;
/*      */ import com.sun.corba.se.impl.corba.NVListImpl;
/*      */ import com.sun.corba.se.impl.corba.NamedValueImpl;
/*      */ import com.sun.corba.se.impl.corba.RequestImpl;
/*      */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*      */ import com.sun.corba.se.impl.encoding.CachedCodeBase;
/*      */ import com.sun.corba.se.impl.interceptors.PIHandlerImpl;
/*      */ import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;
/*      */ import com.sun.corba.se.impl.ior.TaggedComponentFactoryFinderImpl;
/*      */ import com.sun.corba.se.impl.ior.TaggedProfileFactoryFinderImpl;
/*      */ import com.sun.corba.se.impl.ior.TaggedProfileTemplateFactoryFinderImpl;
/*      */ import com.sun.corba.se.impl.legacy.connection.LegacyServerSocketManagerImpl;
/*      */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
/*      */ import com.sun.corba.se.impl.oa.poa.POAFactory;
/*      */ import com.sun.corba.se.impl.oa.toa.TOA;
/*      */ import com.sun.corba.se.impl.oa.toa.TOAFactory;
/*      */ import com.sun.corba.se.impl.orbutil.ORBConstants;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.orbutil.StackImpl;
/*      */ import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolManagerImpl;
/*      */ import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
/*      */ import com.sun.corba.se.impl.protocol.RequestDispatcherRegistryImpl;
/*      */ import com.sun.corba.se.impl.transport.CorbaTransportManagerImpl;
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
/*      */ import com.sun.corba.se.pept.transport.TransportManager;
/*      */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.IORFactories;
/*      */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*      */ import com.sun.corba.se.spi.ior.ObjectKey;
/*      */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*      */ import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
/*      */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*      */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*      */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*      */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*      */ import com.sun.corba.se.spi.orb.DataCollector;
/*      */ import com.sun.corba.se.spi.orb.ORBConfigurator;
/*      */ import com.sun.corba.se.spi.orb.ORBData;
/*      */ import com.sun.corba.se.spi.orb.ORBVersion;
/*      */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*      */ import com.sun.corba.se.spi.orb.Operation;
/*      */ import com.sun.corba.se.spi.orb.OperationFactory;
/*      */ import com.sun.corba.se.spi.orb.ParserImplBase;
/*      */ import com.sun.corba.se.spi.orb.PropertyParser;
/*      */ import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*      */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*      */ import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
/*      */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*      */ import com.sun.corba.se.spi.protocol.PIHandler;
/*      */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*      */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*      */ import com.sun.corba.se.spi.resolver.Resolver;
/*      */ import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*      */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*      */ import com.sun.org.omg.SendingContext.CodeBase;
/*      */ import java.applet.Applet;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.rmi.CORBA.Util;
/*      */ import javax.rmi.CORBA.ValueHandler;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.BAD_PARAM;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.Context;
/*      */ import org.omg.CORBA.ContextList;
/*      */ import org.omg.CORBA.Current;
/*      */ import org.omg.CORBA.Environment;
/*      */ import org.omg.CORBA.ExceptionList;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.NVList;
/*      */ import org.omg.CORBA.NamedValue;
/*      */ import org.omg.CORBA.ORBPackage.InvalidName;
/*      */ import org.omg.CORBA.Policy;
/*      */ import org.omg.CORBA.PolicyError;
/*      */ import org.omg.CORBA.Request;
/*      */ import org.omg.CORBA.StructMember;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.UnionMember;
/*      */ import org.omg.CORBA.ValueMember;
/*      */ import org.omg.CORBA.WrongTransaction;
/*      */ import org.omg.CORBA.portable.OutputStream;
/*      */ import org.omg.CORBA.portable.ValueFactory;
/*      */ import org.omg.PortableServer.Servant;
/*      */ import sun.corba.OutputStreamFactory;
/*      */ 
/*      */ public class ORBImpl extends com.sun.corba.se.spi.orb.ORB
/*      */ {
/*      */   protected TransportManager transportManager;
/*      */   protected LegacyServerSocketManager legacyServerSocketManager;
/*      */   private ThreadLocal OAInvocationInfoStack;
/*      */   private ThreadLocal clientInvocationInfoStack;
/*      */   private static IOR codeBaseIOR;
/*      */   private Vector dynamicRequests;
/*      */   private SynchVariable svResponseReceived;
/*  188 */   private java.lang.Object runObj = new java.lang.Object();
/*  189 */   private java.lang.Object shutdownObj = new java.lang.Object();
/*  190 */   private java.lang.Object waitForCompletionObj = new java.lang.Object();
/*      */   private static final byte STATUS_OPERATING = 1;
/*      */   private static final byte STATUS_SHUTTING_DOWN = 2;
/*      */   private static final byte STATUS_SHUTDOWN = 3;
/*      */   private static final byte STATUS_DESTROYED = 4;
/*  195 */   private byte status = 1;
/*      */ 
/*  198 */   private java.lang.Object invocationObj = new java.lang.Object();
/*  199 */   private int numInvocations = 0;
/*      */ 
/*  203 */   private ThreadLocal isProcessingInvocation = new ThreadLocal() {
/*      */     protected java.lang.Object initialValue() {
/*  205 */       return Boolean.FALSE;
/*      */     }
/*  203 */   };
/*      */   private Map typeCodeForClassMap;
/*  214 */   private Hashtable valueFactoryCache = new Hashtable();
/*      */   private ThreadLocal orbVersionThreadLocal;
/*      */   private RequestDispatcherRegistry requestDispatcherRegistry;
/*      */   private CopierManager copierManager;
/*      */   private int transientServerId;
/*      */   private ServiceContextRegistry serviceContextRegistry;
/*      */   private TOAFactory toaFactory;
/*      */   private POAFactory poaFactory;
/*      */   private PIHandler pihandler;
/*      */   private ORBData configData;
/*      */   private BadServerIdHandler badServerIdHandler;
/*      */   private ClientDelegateFactory clientDelegateFactory;
/*      */   private CorbaContactInfoListFactory corbaContactInfoListFactory;
/*      */   private Resolver resolver;
/*      */   private LocalResolver localResolver;
/*      */   private Operation urlOperation;
/*  267 */   private final java.lang.Object urlOperationLock = new java.lang.Object();
/*      */   private CorbaServerRequestDispatcher insNamingDelegate;
/*  275 */   private final java.lang.Object resolverLock = new java.lang.Object();
/*      */   private TaggedComponentFactoryFinder taggedComponentFactoryFinder;
/*      */   private IdentifiableFactoryFinder taggedProfileFactoryFinder;
/*      */   private IdentifiableFactoryFinder taggedProfileTemplateFactoryFinder;
/*      */   private ObjectKeyFactory objectKeyFactory;
/*  285 */   private boolean orbOwnsThreadPoolManager = false;
/*      */   private ThreadPoolManager threadpoolMgr;
/* 1560 */   private java.lang.Object badServerIdHandlerAccessLock = new java.lang.Object();
/*      */ 
/* 1734 */   private static String localHostString = null;
/*      */ 
/* 1853 */   private java.lang.Object clientDelegateFactoryAccessorLock = new java.lang.Object();
/*      */ 
/* 1875 */   private java.lang.Object corbaContactInfoListFactoryAccessLock = new java.lang.Object();
/*      */ 
/* 2005 */   private java.lang.Object objectKeyFactoryAccessLock = new java.lang.Object();
/*      */ 
/* 2027 */   private java.lang.Object transportManagerAccessorLock = new java.lang.Object();
/*      */ 
/* 2044 */   private java.lang.Object legacyServerSocketManagerAccessLock = new java.lang.Object();
/*      */ 
/* 2059 */   private java.lang.Object threadPoolManagerAccessLock = new java.lang.Object();
/*      */ 
/*      */   private void dprint(String paramString)
/*      */   {
/*  291 */     ORBUtility.dprint(this, paramString);
/*      */   }
/*      */ 
/*      */   public ORBData getORBData()
/*      */   {
/*  311 */     return this.configData;
/*      */   }
/*      */ 
/*      */   public PIHandler getPIHandler()
/*      */   {
/*  316 */     return this.pihandler;
/*      */   }
/*      */ 
/*      */   public ORBVersion getORBVersion()
/*      */   {
/*  330 */     synchronized (this) {
/*  331 */       checkShutdownState();
/*      */     }
/*  333 */     return (ORBVersion)this.orbVersionThreadLocal.get();
/*      */   }
/*      */ 
/*      */   public void setORBVersion(ORBVersion paramORBVersion)
/*      */   {
/*  338 */     synchronized (this) {
/*  339 */       checkShutdownState();
/*      */     }
/*  341 */     this.orbVersionThreadLocal.set(paramORBVersion);
/*      */   }
/*      */ 
/*      */   private void preInit(String[] paramArrayOfString, Properties paramProperties)
/*      */   {
/*  359 */     this.pihandler = new PINoOpHandlerImpl();
/*      */ 
/*  374 */     this.transientServerId = ((int)System.currentTimeMillis());
/*      */ 
/*  376 */     this.orbVersionThreadLocal = new ThreadLocal()
/*      */     {
/*      */       protected java.lang.Object initialValue() {
/*  379 */         return ORBVersionFactory.getORBVersion();
/*      */       }
/*      */     };
/*  384 */     this.requestDispatcherRegistry = new RequestDispatcherRegistryImpl(this, 2);
/*      */ 
/*  386 */     this.copierManager = new CopierManagerImpl(this);
/*      */ 
/*  388 */     this.taggedComponentFactoryFinder = new TaggedComponentFactoryFinderImpl(this);
/*      */ 
/*  390 */     this.taggedProfileFactoryFinder = new TaggedProfileFactoryFinderImpl(this);
/*      */ 
/*  392 */     this.taggedProfileTemplateFactoryFinder = new TaggedProfileTemplateFactoryFinderImpl(this);
/*      */ 
/*  395 */     this.dynamicRequests = new Vector();
/*  396 */     this.svResponseReceived = new SynchVariable();
/*      */ 
/*  398 */     this.OAInvocationInfoStack = new ThreadLocal()
/*      */     {
/*      */       protected java.lang.Object initialValue()
/*      */       {
/*  402 */         return new StackImpl();
/*      */       }
/*      */     };
/*  406 */     this.clientInvocationInfoStack = new ThreadLocal()
/*      */     {
/*      */       protected java.lang.Object initialValue() {
/*  409 */         return new StackImpl();
/*      */       }
/*      */     };
/*  413 */     this.serviceContextRegistry = new ServiceContextRegistry(this);
/*      */   }
/*      */ 
/*      */   protected void setDebugFlags(String[] paramArrayOfString)
/*      */   {
/*  418 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  419 */       String str = paramArrayOfString[i];
/*      */       try
/*      */       {
/*  424 */         Field localField = getClass().getField(str + "DebugFlag");
/*  425 */         int j = localField.getModifiers();
/*  426 */         if ((Modifier.isPublic(j)) && (!Modifier.isStatic(j)) && 
/*  427 */           (localField.getType() == Boolean.TYPE))
/*  428 */           localField.setBoolean(this, true);
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void postInit(String[] paramArrayOfString, DataCollector paramDataCollector)
/*      */   {
/*  456 */     this.configData = new ORBDataParserImpl(this, paramDataCollector);
/*      */ 
/*  460 */     setDebugFlags(this.configData.getORBDebugFlags());
/*      */ 
/*  464 */     getTransportManager();
/*  465 */     getLegacyServerSocketManager();
/*      */ 
/*  468 */     ConfigParser localConfigParser = new ConfigParser(null);
/*  469 */     localConfigParser.init(paramDataCollector);
/*      */ 
/*  471 */     ORBConfigurator localORBConfigurator = null;
/*      */     try {
/*  473 */       localORBConfigurator = (ORBConfigurator)localConfigParser.configurator.newInstance();
/*      */     }
/*      */     catch (Exception localException1) {
/*  476 */       throw this.wrapper.badOrbConfigurator(localException1, localConfigParser.configurator.getName());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  483 */       localORBConfigurator.configure(paramDataCollector, this);
/*      */     } catch (Exception localException2) {
/*  485 */       throw this.wrapper.orbConfiguratorError(localException2);
/*      */     }
/*      */ 
/*  489 */     this.pihandler = new PIHandlerImpl(this, paramArrayOfString);
/*  490 */     this.pihandler.initialize();
/*      */ 
/*  494 */     getThreadPoolManager();
/*      */ 
/*  496 */     super.getByteBufferPool();
/*      */   }
/*      */ 
/*      */   private synchronized POAFactory getPOAFactory()
/*      */   {
/*  501 */     if (this.poaFactory == null) {
/*  502 */       this.poaFactory = ((POAFactory)this.requestDispatcherRegistry.getObjectAdapterFactory(32));
/*      */     }
/*      */ 
/*  506 */     return this.poaFactory;
/*      */   }
/*      */ 
/*      */   private synchronized TOAFactory getTOAFactory()
/*      */   {
/*  511 */     if (this.toaFactory == null) {
/*  512 */       this.toaFactory = ((TOAFactory)this.requestDispatcherRegistry.getObjectAdapterFactory(2));
/*      */     }
/*      */ 
/*  516 */     return this.toaFactory;
/*      */   }
/*      */ 
/*      */   public void set_parameters(Properties paramProperties)
/*      */   {
/*  521 */     synchronized (this) {
/*  522 */       checkShutdownState();
/*      */     }
/*  524 */     preInit(null, paramProperties);
/*  525 */     ??? = DataCollectorFactory.create(paramProperties, getLocalHostName());
/*      */ 
/*  527 */     postInit(null, (DataCollector)???);
/*      */   }
/*      */ 
/*      */   protected void set_parameters(Applet paramApplet, Properties paramProperties)
/*      */   {
/*  532 */     preInit(null, paramProperties);
/*  533 */     DataCollector localDataCollector = DataCollectorFactory.create(paramApplet, paramProperties, getLocalHostName());
/*      */ 
/*  535 */     postInit(null, localDataCollector);
/*      */   }
/*      */ 
/*      */   protected void set_parameters(String[] paramArrayOfString, Properties paramProperties)
/*      */   {
/*  540 */     preInit(paramArrayOfString, paramProperties);
/*  541 */     DataCollector localDataCollector = DataCollectorFactory.create(paramArrayOfString, paramProperties, getLocalHostName());
/*      */ 
/*  543 */     postInit(paramArrayOfString, localDataCollector);
/*      */   }
/*      */ 
/*      */   public synchronized OutputStream create_output_stream()
/*      */   {
/*  552 */     checkShutdownState();
/*  553 */     return OutputStreamFactory.newEncapsOutputStream(this);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public synchronized Current get_current()
/*      */   {
/*  568 */     checkShutdownState();
/*      */ 
/*  580 */     throw this.wrapper.genericNoImpl();
/*      */   }
/*      */ 
/*      */   public synchronized NVList create_list(int paramInt)
/*      */   {
/*  593 */     checkShutdownState();
/*  594 */     return new NVListImpl(this, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized NVList create_operation_list(org.omg.CORBA.Object paramObject)
/*      */   {
/*  607 */     checkShutdownState();
/*  608 */     throw this.wrapper.genericNoImpl();
/*      */   }
/*      */ 
/*      */   public synchronized NamedValue create_named_value(String paramString, Any paramAny, int paramInt)
/*      */   {
/*  618 */     checkShutdownState();
/*  619 */     return new NamedValueImpl(this, paramString, paramAny, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized ExceptionList create_exception_list()
/*      */   {
/*  629 */     checkShutdownState();
/*  630 */     return new ExceptionListImpl();
/*      */   }
/*      */ 
/*      */   public synchronized ContextList create_context_list()
/*      */   {
/*  640 */     checkShutdownState();
/*  641 */     return new ContextListImpl(this);
/*      */   }
/*      */ 
/*      */   public synchronized Context get_default_context()
/*      */   {
/*  651 */     checkShutdownState();
/*  652 */     throw this.wrapper.genericNoImpl();
/*      */   }
/*      */ 
/*      */   public synchronized Environment create_environment()
/*      */   {
/*  662 */     checkShutdownState();
/*  663 */     return new EnvironmentImpl();
/*      */   }
/*      */ 
/*      */   public synchronized void send_multiple_requests_oneway(Request[] paramArrayOfRequest)
/*      */   {
/*  668 */     checkShutdownState();
/*      */ 
/*  671 */     for (int i = 0; i < paramArrayOfRequest.length; i++)
/*  672 */       paramArrayOfRequest[i].send_oneway();
/*      */   }
/*      */ 
/*      */   public synchronized void send_multiple_requests_deferred(Request[] paramArrayOfRequest)
/*      */   {
/*  683 */     checkShutdownState();
/*      */ 
/*  686 */     for (int i = 0; i < paramArrayOfRequest.length; i++) {
/*  687 */       this.dynamicRequests.addElement(paramArrayOfRequest[i]);
/*      */     }
/*      */ 
/*  691 */     for (i = 0; i < paramArrayOfRequest.length; i++) {
/*  692 */       AsynchInvoke localAsynchInvoke = new AsynchInvoke(this, (RequestImpl)paramArrayOfRequest[i], true);
/*      */ 
/*  694 */       new Thread(localAsynchInvoke).start();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean poll_next_response()
/*      */   {
/*  703 */     checkShutdownState();
/*      */ 
/*  708 */     Enumeration localEnumeration = this.dynamicRequests.elements();
/*  709 */     while (localEnumeration.hasMoreElements() == true) {
/*  710 */       Request localRequest = (Request)localEnumeration.nextElement();
/*  711 */       if (localRequest.poll_response() == true) {
/*  712 */         return true;
/*      */       }
/*      */     }
/*  715 */     return false;
/*      */   }
/*      */ 
/*      */   public Request get_next_response()
/*      */     throws WrongTransaction
/*      */   {
/*  726 */     synchronized (this) {
/*  727 */       checkShutdownState();
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  732 */       synchronized (this.dynamicRequests) {
/*  733 */         Enumeration localEnumeration = this.dynamicRequests.elements();
/*  734 */         if (localEnumeration.hasMoreElements()) {
/*  735 */           Request localRequest = (Request)localEnumeration.nextElement();
/*  736 */           if (localRequest.poll_response())
/*      */           {
/*  738 */             localRequest.get_response();
/*  739 */             this.dynamicRequests.removeElement(localRequest);
/*  740 */             return localRequest;
/*      */           }
/*  742 */           continue;
/*      */         }
/*      */       }
/*      */ 
/*  746 */       synchronized (this.svResponseReceived) {
/*  747 */         while (!this.svResponseReceived.value()) {
/*      */           try {
/*  749 */             this.svResponseReceived.wait();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */         }
/*  755 */         this.svResponseReceived.reset();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notifyORB()
/*      */   {
/*  765 */     synchronized (this) {
/*  766 */       checkShutdownState();
/*      */     }
/*  768 */     synchronized (this.svResponseReceived) {
/*  769 */       this.svResponseReceived.set();
/*  770 */       this.svResponseReceived.notify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized String object_to_string(org.omg.CORBA.Object paramObject)
/*      */   {
/*  781 */     checkShutdownState();
/*      */ 
/*  784 */     if (paramObject == null) {
/*  785 */       localIOR = IORFactories.makeIOR(this);
/*  786 */       return localIOR.stringify();
/*      */     }
/*      */ 
/*  789 */     IOR localIOR = null;
/*      */     try
/*      */     {
/*  792 */       localIOR = ORBUtility.connectAndGetIOR(this, paramObject);
/*      */     }
/*      */     catch (BAD_PARAM localBAD_PARAM) {
/*  795 */       if (localBAD_PARAM.minor == 1398079694) {
/*  796 */         throw this.omgWrapper.notAnObjectImpl(localBAD_PARAM);
/*      */       }
/*      */ 
/*  801 */       throw localBAD_PARAM;
/*      */     }
/*      */ 
/*  804 */     return localIOR.stringify();
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object string_to_object(String paramString)
/*      */   {
/*      */     Operation localOperation;
/*  816 */     synchronized (this) {
/*  817 */       checkShutdownState();
/*  818 */       localOperation = this.urlOperation;
/*      */     }
/*      */ 
/*  821 */     if (paramString == null) {
/*  822 */       throw this.wrapper.nullParam();
/*      */     }
/*  824 */     synchronized (this.urlOperationLock) {
/*  825 */       org.omg.CORBA.Object localObject = (org.omg.CORBA.Object)localOperation.operate(paramString);
/*  826 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized IOR getFVDCodeBaseIOR()
/*      */   {
/*  834 */     checkShutdownState();
/*      */ 
/*  836 */     if (codeBaseIOR != null) {
/*  837 */       return codeBaseIOR;
/*      */     }
/*      */ 
/*  842 */     ValueHandler localValueHandler = ORBUtility.createValueHandler();
/*      */ 
/*  844 */     CodeBase localCodeBase = (CodeBase)localValueHandler.getRunTimeCodeBase();
/*  845 */     return ORBUtility.connectAndGetIOR(this, localCodeBase);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode get_primitive_tc(TCKind paramTCKind)
/*      */   {
/*  856 */     checkShutdownState();
/*  857 */     return get_primitive_tc(paramTCKind.value());
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_struct_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember)
/*      */   {
/*  872 */     checkShutdownState();
/*  873 */     return new TypeCodeImpl(this, 15, paramString1, paramString2, paramArrayOfStructMember);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_union_tc(String paramString1, String paramString2, TypeCode paramTypeCode, UnionMember[] paramArrayOfUnionMember)
/*      */   {
/*  891 */     checkShutdownState();
/*  892 */     return new TypeCodeImpl(this, 16, paramString1, paramString2, paramTypeCode, paramArrayOfUnionMember);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_enum_tc(String paramString1, String paramString2, String[] paramArrayOfString)
/*      */   {
/*  912 */     checkShutdownState();
/*  913 */     return new TypeCodeImpl(this, 17, paramString1, paramString2, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_alias_tc(String paramString1, String paramString2, TypeCode paramTypeCode)
/*      */   {
/*  929 */     checkShutdownState();
/*  930 */     return new TypeCodeImpl(this, 21, paramString1, paramString2, paramTypeCode);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_exception_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember)
/*      */   {
/*  945 */     checkShutdownState();
/*  946 */     return new TypeCodeImpl(this, 22, paramString1, paramString2, paramArrayOfStructMember);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_interface_tc(String paramString1, String paramString2)
/*      */   {
/*  959 */     checkShutdownState();
/*  960 */     return new TypeCodeImpl(this, 14, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_string_tc(int paramInt)
/*      */   {
/*  971 */     checkShutdownState();
/*  972 */     return new TypeCodeImpl(this, 18, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_wstring_tc(int paramInt)
/*      */   {
/*  983 */     checkShutdownState();
/*  984 */     return new TypeCodeImpl(this, 27, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_sequence_tc(int paramInt, TypeCode paramTypeCode)
/*      */   {
/*  998 */     checkShutdownState();
/*  999 */     return new TypeCodeImpl(this, 19, paramInt, paramTypeCode);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_recursive_sequence_tc(int paramInt1, int paramInt2)
/*      */   {
/* 1014 */     checkShutdownState();
/* 1015 */     return new TypeCodeImpl(this, 19, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_array_tc(int paramInt, TypeCode paramTypeCode)
/*      */   {
/* 1030 */     checkShutdownState();
/* 1031 */     return new TypeCodeImpl(this, 20, paramInt, paramTypeCode);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_native_tc(String paramString1, String paramString2)
/*      */   {
/* 1038 */     checkShutdownState();
/* 1039 */     return new TypeCodeImpl(this, 31, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_abstract_interface_tc(String paramString1, String paramString2)
/*      */   {
/* 1046 */     checkShutdownState();
/* 1047 */     return new TypeCodeImpl(this, 32, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_fixed_tc(short paramShort1, short paramShort2)
/*      */   {
/* 1052 */     checkShutdownState();
/* 1053 */     return new TypeCodeImpl(this, 28, paramShort1, paramShort2);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_value_tc(String paramString1, String paramString2, short paramShort, TypeCode paramTypeCode, ValueMember[] paramArrayOfValueMember)
/*      */   {
/* 1062 */     checkShutdownState();
/* 1063 */     return new TypeCodeImpl(this, 29, paramString1, paramString2, paramShort, paramTypeCode, paramArrayOfValueMember);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_recursive_tc(String paramString)
/*      */   {
/* 1068 */     checkShutdownState();
/* 1069 */     return new TypeCodeImpl(this, paramString);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCode create_value_box_tc(String paramString1, String paramString2, TypeCode paramTypeCode)
/*      */   {
/* 1076 */     checkShutdownState();
/* 1077 */     return new TypeCodeImpl(this, 30, paramString1, paramString2, paramTypeCode);
/*      */   }
/*      */ 
/*      */   public synchronized Any create_any()
/*      */   {
/* 1088 */     checkShutdownState();
/* 1089 */     return new AnyImpl(this);
/*      */   }
/*      */ 
/*      */   public synchronized void setTypeCodeForClass(Class paramClass, TypeCodeImpl paramTypeCodeImpl)
/*      */   {
/* 1100 */     checkShutdownState();
/*      */ 
/* 1102 */     if (this.typeCodeForClassMap == null) {
/* 1103 */       this.typeCodeForClassMap = Collections.synchronizedMap(new WeakHashMap(64));
/*      */     }
/*      */ 
/* 1106 */     if (!this.typeCodeForClassMap.containsKey(paramClass))
/* 1107 */       this.typeCodeForClassMap.put(paramClass, paramTypeCodeImpl);
/*      */   }
/*      */ 
/*      */   public synchronized TypeCodeImpl getTypeCodeForClass(Class paramClass)
/*      */   {
/* 1112 */     checkShutdownState();
/*      */ 
/* 1114 */     if (this.typeCodeForClassMap == null)
/* 1115 */       return null;
/* 1116 */     return (TypeCodeImpl)this.typeCodeForClassMap.get(paramClass);
/*      */   }
/*      */ 
/*      */   public String[] list_initial_services()
/*      */   {
/*      */     Resolver localResolver1;
/* 1137 */     synchronized (this) {
/* 1138 */       checkShutdownState();
/* 1139 */       localResolver1 = this.resolver;
/*      */     }
/*      */ 
/* 1142 */     synchronized (this.resolverLock) {
/* 1143 */       Set localSet = localResolver1.list();
/* 1144 */       return (String[])localSet.toArray(new String[localSet.size()]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object resolve_initial_references(String paramString)
/*      */     throws InvalidName
/*      */   {
/*      */     Resolver localResolver1;
/* 1163 */     synchronized (this) {
/* 1164 */       checkShutdownState();
/* 1165 */       localResolver1 = this.resolver;
/*      */     }
/*      */ 
/* 1168 */     synchronized (this.resolverLock) {
/* 1169 */       org.omg.CORBA.Object localObject = localResolver1.resolve(paramString);
/*      */ 
/* 1171 */       if (localObject == null) {
/* 1172 */         throw new InvalidName();
/*      */       }
/* 1174 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void register_initial_reference(String paramString, org.omg.CORBA.Object paramObject)
/*      */     throws InvalidName
/*      */   {
/* 1196 */     synchronized (this) {
/* 1197 */       checkShutdownState();
/*      */     }
/*      */ 
/* 1200 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1201 */       throw new InvalidName();
/*      */     }
/* 1203 */     synchronized (this) {
/* 1204 */       checkShutdownState();
/*      */     }
/*      */     CorbaServerRequestDispatcher localCorbaServerRequestDispatcher;
/* 1207 */     synchronized (this.resolverLock) {
/* 1208 */       localCorbaServerRequestDispatcher = this.insNamingDelegate;
/*      */ 
/* 1210 */       org.omg.CORBA.Object localObject = this.localResolver.resolve(paramString);
/* 1211 */       if (localObject != null) {
/* 1212 */         throw new InvalidName(paramString + " already registered");
/*      */       }
/* 1214 */       this.localResolver.register(paramString, ClosureFactory.makeConstant(paramObject));
/*      */     }
/*      */ 
/* 1217 */     synchronized (this) {
/* 1218 */       if (StubAdapter.isStub(paramObject))
/*      */       {
/* 1220 */         this.requestDispatcherRegistry.registerServerRequestDispatcher(localCorbaServerRequestDispatcher, paramString);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/* 1232 */     synchronized (this) {
/* 1233 */       checkShutdownState();
/*      */     }
/*      */ 
/* 1236 */     synchronized (this.runObj) {
/*      */       try {
/* 1238 */         this.runObj.wait(); } catch (InterruptedException localInterruptedException) {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void shutdown(boolean paramBoolean) {
/* 1244 */     int i = 0;
/*      */ 
/* 1246 */     synchronized (this) {
/* 1247 */       checkShutdownState();
/*      */ 
/* 1253 */       if ((paramBoolean) && (this.isProcessingInvocation.get() == Boolean.TRUE))
/*      */       {
/* 1255 */         throw this.omgWrapper.shutdownWaitForCompletionDeadlock();
/*      */       }
/*      */ 
/* 1258 */       if (this.status == 2) {
/* 1259 */         if (paramBoolean)
/* 1260 */           i = 1;
/*      */         else {
/* 1262 */           return;
/*      */         }
/*      */       }
/*      */ 
/* 1266 */       this.status = 2;
/*      */     }
/*      */ 
/* 1270 */     synchronized (this.shutdownObj)
/*      */     {
/* 1274 */       if (i != 0) {
/*      */         while (true) {
/* 1276 */           synchronized (this) {
/* 1277 */             if (this.status == 3)
/* 1278 */               break;
/*      */           }
/*      */           try
/*      */           {
/* 1282 */             this.shutdownObj.wait();
/*      */           }
/*      */           catch (InterruptedException localObject1)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/* 1289 */       shutdownServants(paramBoolean);
/*      */ 
/* 1291 */       if (paramBoolean)
/* 1292 */         synchronized (this.waitForCompletionObj) {
/* 1293 */           while (this.numInvocations > 0)
/*      */             try {
/* 1295 */               this.waitForCompletionObj.wait();
/*      */             }
/*      */             catch (InterruptedException localInterruptedException)
/*      */             {
/*      */             }
/*      */         }
/* 1301 */       synchronized (this.runObj) {
/* 1302 */         this.runObj.notifyAll();
/*      */       }
/*      */ 
/* 1305 */       this.status = 3;
/*      */ 
/* 1307 */       this.shutdownObj.notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void shutdownServants(boolean paramBoolean)
/*      */   {
/*      */     HashSet localHashSet;
/* 1317 */     synchronized (this) {
/* 1318 */       localHashSet = new HashSet(this.requestDispatcherRegistry.getObjectAdapterFactories());
/*      */     }
/*      */ 
/* 1321 */     for (??? = localHashSet.iterator(); ((Iterator)???).hasNext(); ) { ObjectAdapterFactory localObjectAdapterFactory = (ObjectAdapterFactory)((Iterator)???).next();
/* 1322 */       localObjectAdapterFactory.shutdown(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void checkShutdownState()
/*      */   {
/* 1328 */     if (this.status == 4) {
/* 1329 */       throw this.wrapper.orbDestroyed();
/*      */     }
/*      */ 
/* 1332 */     if (this.status == 3)
/* 1333 */       throw this.omgWrapper.badOperationAfterShutdown();
/*      */   }
/*      */ 
/*      */   public boolean isDuringDispatch()
/*      */   {
/* 1339 */     synchronized (this) {
/* 1340 */       checkShutdownState();
/*      */     }
/* 1342 */     ??? = (Boolean)this.isProcessingInvocation.get();
/* 1343 */     return ((Boolean)???).booleanValue();
/*      */   }
/*      */ 
/*      */   public void startingDispatch()
/*      */   {
/* 1348 */     synchronized (this) {
/* 1349 */       checkShutdownState();
/*      */     }
/* 1351 */     synchronized (this.invocationObj) {
/* 1352 */       this.isProcessingInvocation.set(Boolean.TRUE);
/* 1353 */       this.numInvocations += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void finishedDispatch()
/*      */   {
/* 1359 */     synchronized (this) {
/* 1360 */       checkShutdownState();
/*      */     }
/* 1362 */     synchronized (this.invocationObj) {
/* 1363 */       this.numInvocations -= 1;
/* 1364 */       this.isProcessingInvocation.set(Boolean.valueOf(false));
/* 1365 */       if (this.numInvocations == 0)
/* 1366 */         synchronized (this.waitForCompletionObj) {
/* 1367 */           this.waitForCompletionObj.notifyAll();
/*      */         }
/* 1369 */       else if (this.numInvocations < 0)
/* 1370 */         throw this.wrapper.numInvocationsAlreadyZero(CompletionStatus.COMPLETED_YES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/* 1383 */     int i = 0;
/*      */ 
/* 1385 */     synchronized (this) {
/* 1386 */       i = this.status == 1 ? 1 : 0;
/*      */     }
/*      */ 
/* 1389 */     if (i != 0) {
/* 1390 */       shutdown(true);
/*      */     }
/*      */ 
/* 1393 */     synchronized (this) {
/* 1394 */       if (this.status < 4) {
/* 1395 */         getCorbaTransportManager().close();
/* 1396 */         getPIHandler().destroyInterceptors();
/* 1397 */         this.status = 4;
/*      */       }
/*      */     }
/* 1400 */     synchronized (this.threadPoolManagerAccessLock) {
/* 1401 */       if (this.orbOwnsThreadPoolManager) {
/*      */         try {
/* 1403 */           this.threadpoolMgr.close();
/* 1404 */           this.threadpoolMgr = null;
/*      */         } catch (IOException localIOException3) {
/* 1406 */           this.wrapper.ioExceptionOnClose(localIOException3);
/*      */         }
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1412 */       this.monitoringManager.close();
/* 1413 */       this.monitoringManager = null;
/*      */     } catch (IOException localIOException1) {
/* 1415 */       this.wrapper.ioExceptionOnClose(localIOException1);
/*      */     }
/*      */ 
/* 1418 */     CachedCodeBase.cleanCache(this);
/*      */     try {
/* 1420 */       this.pihandler.close();
/*      */     } catch (IOException localIOException2) {
/* 1422 */       this.wrapper.ioExceptionOnClose(localIOException2);
/*      */     }
/*      */ 
/* 1425 */     super.destroy();
/*      */ 
/* 1427 */     this.badServerIdHandlerAccessLock = null;
/* 1428 */     this.clientDelegateFactoryAccessorLock = null;
/* 1429 */     this.corbaContactInfoListFactoryAccessLock = null;
/*      */ 
/* 1431 */     this.objectKeyFactoryAccessLock = null;
/* 1432 */     this.legacyServerSocketManagerAccessLock = null;
/* 1433 */     this.threadPoolManagerAccessLock = null;
/* 1434 */     this.transportManager = null;
/* 1435 */     this.legacyServerSocketManager = null;
/* 1436 */     this.OAInvocationInfoStack = null;
/* 1437 */     this.clientInvocationInfoStack = null;
/* 1438 */     codeBaseIOR = null;
/* 1439 */     this.dynamicRequests = null;
/* 1440 */     this.svResponseReceived = null;
/* 1441 */     this.runObj = null;
/* 1442 */     this.shutdownObj = null;
/* 1443 */     this.waitForCompletionObj = null;
/* 1444 */     this.invocationObj = null;
/* 1445 */     this.isProcessingInvocation = null;
/* 1446 */     this.typeCodeForClassMap = null;
/* 1447 */     this.valueFactoryCache = null;
/* 1448 */     this.orbVersionThreadLocal = null;
/* 1449 */     this.requestDispatcherRegistry = null;
/* 1450 */     this.copierManager = null;
/* 1451 */     this.toaFactory = null;
/* 1452 */     this.poaFactory = null;
/* 1453 */     this.pihandler = null;
/* 1454 */     this.configData = null;
/* 1455 */     this.badServerIdHandler = null;
/* 1456 */     this.clientDelegateFactory = null;
/* 1457 */     this.corbaContactInfoListFactory = null;
/* 1458 */     this.resolver = null;
/* 1459 */     this.localResolver = null;
/* 1460 */     this.insNamingDelegate = null;
/* 1461 */     this.urlOperation = null;
/* 1462 */     this.taggedComponentFactoryFinder = null;
/* 1463 */     this.taggedProfileFactoryFinder = null;
/* 1464 */     this.taggedProfileTemplateFactoryFinder = null;
/* 1465 */     this.objectKeyFactory = null;
/*      */   }
/*      */ 
/*      */   public synchronized ValueFactory register_value_factory(String paramString, ValueFactory paramValueFactory)
/*      */   {
/* 1480 */     checkShutdownState();
/*      */ 
/* 1482 */     if ((paramString == null) || (paramValueFactory == null)) {
/* 1483 */       throw this.omgWrapper.unableRegisterValueFactory();
/*      */     }
/* 1485 */     return (ValueFactory)this.valueFactoryCache.put(paramString, paramValueFactory);
/*      */   }
/*      */ 
/*      */   public synchronized void unregister_value_factory(String paramString)
/*      */   {
/* 1495 */     checkShutdownState();
/*      */ 
/* 1497 */     if (this.valueFactoryCache.remove(paramString) == null)
/* 1498 */       throw this.wrapper.nullParam();
/*      */   }
/*      */ 
/*      */   public synchronized ValueFactory lookup_value_factory(String paramString)
/*      */   {
/* 1512 */     checkShutdownState();
/*      */ 
/* 1514 */     ValueFactory localValueFactory = (ValueFactory)this.valueFactoryCache.get(paramString);
/*      */ 
/* 1517 */     if (localValueFactory == null) {
/*      */       try {
/* 1519 */         localValueFactory = Utility.getFactory(null, null, null, paramString);
/*      */       } catch (MARSHAL localMARSHAL) {
/* 1521 */         throw this.wrapper.unableFindValueFactory(localMARSHAL);
/*      */       }
/*      */     }
/*      */ 
/* 1525 */     return localValueFactory;
/*      */   }
/*      */ 
/*      */   public OAInvocationInfo peekInvocationInfo()
/*      */   {
/* 1530 */     synchronized (this) {
/* 1531 */       checkShutdownState();
/*      */     }
/* 1533 */     ??? = (StackImpl)this.OAInvocationInfoStack.get();
/* 1534 */     return (OAInvocationInfo)((StackImpl)???).peek();
/*      */   }
/*      */ 
/*      */   public void pushInvocationInfo(OAInvocationInfo paramOAInvocationInfo)
/*      */   {
/* 1539 */     synchronized (this) {
/* 1540 */       checkShutdownState();
/*      */     }
/* 1542 */     ??? = (StackImpl)this.OAInvocationInfoStack.get();
/* 1543 */     ((StackImpl)???).push(paramOAInvocationInfo);
/*      */   }
/*      */ 
/*      */   public OAInvocationInfo popInvocationInfo()
/*      */   {
/* 1548 */     synchronized (this) {
/* 1549 */       checkShutdownState();
/*      */     }
/* 1551 */     ??? = (StackImpl)this.OAInvocationInfoStack.get();
/* 1552 */     return (OAInvocationInfo)((StackImpl)???).pop();
/*      */   }
/*      */ 
/*      */   public void initBadServerIdHandler()
/*      */   {
/* 1564 */     synchronized (this) {
/* 1565 */       checkShutdownState();
/*      */     }
/* 1567 */     synchronized (this.badServerIdHandlerAccessLock) {
/* 1568 */       Class localClass = this.configData.getBadServerIdHandler();
/* 1569 */       if (localClass != null)
/*      */         try {
/* 1571 */           Class[] arrayOfClass = { org.omg.CORBA.ORB.class };
/* 1572 */           java.lang.Object[] arrayOfObject = { this };
/* 1573 */           Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/* 1574 */           this.badServerIdHandler = ((BadServerIdHandler)localConstructor.newInstance(arrayOfObject));
/*      */         }
/*      */         catch (Exception localException) {
/* 1577 */           throw this.wrapper.errorInitBadserveridhandler(localException);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBadServerIdHandler(BadServerIdHandler paramBadServerIdHandler)
/*      */   {
/* 1585 */     synchronized (this) {
/* 1586 */       checkShutdownState();
/*      */     }
/* 1588 */     synchronized (this.badServerIdHandlerAccessLock) {
/* 1589 */       this.badServerIdHandler = paramBadServerIdHandler;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleBadServerId(ObjectKey paramObjectKey)
/*      */   {
/* 1595 */     synchronized (this) {
/* 1596 */       checkShutdownState();
/*      */     }
/* 1598 */     synchronized (this.badServerIdHandlerAccessLock) {
/* 1599 */       if (this.badServerIdHandler == null) {
/* 1600 */         throw this.wrapper.badServerId();
/*      */       }
/* 1602 */       this.badServerIdHandler.handle(paramObjectKey);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Policy create_policy(int paramInt, Any paramAny)
/*      */     throws PolicyError
/*      */   {
/* 1609 */     checkShutdownState();
/*      */ 
/* 1611 */     return this.pihandler.create_policy(paramInt, paramAny);
/*      */   }
/*      */ 
/*      */   public synchronized void connect(org.omg.CORBA.Object paramObject)
/*      */   {
/* 1619 */     checkShutdownState();
/* 1620 */     if (getTOAFactory() == null)
/* 1621 */       throw this.wrapper.noToa();
/*      */     try
/*      */     {
/* 1624 */       String str = Util.getCodebase(paramObject.getClass());
/* 1625 */       getTOAFactory().getTOA(str).connect(paramObject);
/*      */     } catch (Exception localException) {
/* 1627 */       throw this.wrapper.orbConnectError(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void disconnect(org.omg.CORBA.Object paramObject)
/*      */   {
/* 1633 */     checkShutdownState();
/* 1634 */     if (getTOAFactory() == null)
/* 1635 */       throw this.wrapper.noToa();
/*      */     try
/*      */     {
/* 1638 */       getTOAFactory().getTOA().disconnect(paramObject);
/*      */     } catch (Exception localException) {
/* 1640 */       throw this.wrapper.orbConnectError(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getTransientServerId()
/*      */   {
/* 1646 */     synchronized (this) {
/* 1647 */       checkShutdownState();
/*      */     }
/* 1649 */     if (this.configData.getORBServerIdPropertySpecified())
/*      */     {
/* 1651 */       return this.configData.getPersistentServerId();
/*      */     }
/* 1653 */     return this.transientServerId;
/*      */   }
/*      */ 
/*      */   public RequestDispatcherRegistry getRequestDispatcherRegistry()
/*      */   {
/* 1658 */     synchronized (this) {
/* 1659 */       checkShutdownState();
/*      */     }
/* 1661 */     return this.requestDispatcherRegistry;
/*      */   }
/*      */ 
/*      */   public ServiceContextRegistry getServiceContextRegistry()
/*      */   {
/* 1666 */     synchronized (this) {
/* 1667 */       checkShutdownState();
/*      */     }
/* 1669 */     return this.serviceContextRegistry;
/*      */   }
/*      */ 
/*      */   public boolean isLocalHost(String paramString)
/*      */   {
/* 1686 */     synchronized (this) {
/* 1687 */       checkShutdownState();
/*      */     }
/* 1689 */     return (paramString.equals(this.configData.getORBServerHost())) || (paramString.equals(getLocalHostName()));
/*      */   }
/*      */ 
/*      */   public boolean isLocalServerId(int paramInt1, int paramInt2)
/*      */   {
/* 1695 */     synchronized (this) {
/* 1696 */       checkShutdownState();
/*      */     }
/* 1698 */     if ((paramInt1 < 32) || (paramInt1 > 63))
/*      */     {
/* 1700 */       return paramInt2 == getTransientServerId();
/*      */     }
/*      */ 
/* 1703 */     if (ORBConstants.isTransient(paramInt1))
/* 1704 */       return paramInt2 == getTransientServerId();
/* 1705 */     if (this.configData.getPersistentServerIdInitialized()) {
/* 1706 */       return paramInt2 == this.configData.getPersistentServerId();
/*      */     }
/* 1708 */     return false;
/*      */   }
/*      */ 
/*      */   private String getHostName(String paramString)
/*      */     throws UnknownHostException
/*      */   {
/* 1718 */     return InetAddress.getByName(paramString).getHostAddress();
/*      */   }
/*      */ 
/*      */   private synchronized String getLocalHostName()
/*      */   {
/* 1738 */     if (localHostString == null) {
/*      */       try {
/* 1740 */         localHostString = InetAddress.getLocalHost().getHostAddress();
/*      */       } catch (Exception localException) {
/* 1742 */         throw this.wrapper.getLocalHostFailed(localException);
/*      */       }
/*      */     }
/* 1745 */     return localHostString;
/*      */   }
/*      */ 
/*      */   public synchronized boolean work_pending()
/*      */   {
/* 1758 */     checkShutdownState();
/* 1759 */     throw this.wrapper.genericNoImpl();
/*      */   }
/*      */ 
/*      */   public synchronized void perform_work()
/*      */   {
/* 1766 */     checkShutdownState();
/* 1767 */     throw this.wrapper.genericNoImpl();
/*      */   }
/*      */ 
/*      */   public synchronized void set_delegate(java.lang.Object paramObject) {
/* 1771 */     checkShutdownState();
/*      */ 
/* 1773 */     POAFactory localPOAFactory = getPOAFactory();
/* 1774 */     if (localPOAFactory != null) {
/* 1775 */       ((Servant)paramObject)._set_delegate(localPOAFactory.getDelegateImpl());
/*      */     }
/*      */     else
/* 1778 */       throw this.wrapper.noPoa();
/*      */   }
/*      */ 
/*      */   public ClientInvocationInfo createOrIncrementInvocationInfo()
/*      */   {
/* 1788 */     synchronized (this) {
/* 1789 */       checkShutdownState();
/*      */     }
/* 1791 */     ??? = (StackImpl)this.clientInvocationInfoStack.get();
/*      */ 
/* 1793 */     java.lang.Object localObject2 = null;
/* 1794 */     if (!((StackImpl)???).empty()) {
/* 1795 */       localObject2 = (ClientInvocationInfo)((StackImpl)???).peek();
/*      */     }
/*      */ 
/* 1798 */     if ((localObject2 == null) || (!((ClientInvocationInfo)localObject2).isRetryInvocation()))
/*      */     {
/* 1802 */       localObject2 = new CorbaInvocationInfo(this);
/* 1803 */       startingDispatch();
/* 1804 */       ((StackImpl)???).push(localObject2);
/*      */     }
/*      */ 
/* 1807 */     ((ClientInvocationInfo)localObject2).setIsRetryInvocation(false);
/* 1808 */     ((ClientInvocationInfo)localObject2).incrementEntryCount();
/* 1809 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public void releaseOrDecrementInvocationInfo()
/*      */   {
/* 1814 */     synchronized (this) {
/* 1815 */       checkShutdownState();
/*      */     }
/* 1817 */     int i = -1;
/* 1818 */     ClientInvocationInfo localClientInvocationInfo = null;
/* 1819 */     StackImpl localStackImpl = (StackImpl)this.clientInvocationInfoStack.get();
/*      */ 
/* 1821 */     if (!localStackImpl.empty()) {
/* 1822 */       localClientInvocationInfo = (ClientInvocationInfo)localStackImpl.peek();
/*      */     }
/*      */     else {
/* 1825 */       throw this.wrapper.invocationInfoStackEmpty();
/*      */     }
/* 1827 */     localClientInvocationInfo.decrementEntryCount();
/* 1828 */     i = localClientInvocationInfo.getEntryCount();
/* 1829 */     if (localClientInvocationInfo.getEntryCount() == 0)
/*      */     {
/* 1831 */       if (!localClientInvocationInfo.isRetryInvocation()) {
/* 1832 */         localStackImpl.pop();
/*      */       }
/* 1834 */       finishedDispatch();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ClientInvocationInfo getInvocationInfo()
/*      */   {
/* 1840 */     synchronized (this) {
/* 1841 */       checkShutdownState();
/*      */     }
/* 1843 */     ??? = (StackImpl)this.clientInvocationInfoStack.get();
/*      */ 
/* 1845 */     return (ClientInvocationInfo)((StackImpl)???).peek();
/*      */   }
/*      */ 
/*      */   public void setClientDelegateFactory(ClientDelegateFactory paramClientDelegateFactory)
/*      */   {
/* 1857 */     synchronized (this) {
/* 1858 */       checkShutdownState();
/*      */     }
/* 1860 */     synchronized (this.clientDelegateFactoryAccessorLock) {
/* 1861 */       this.clientDelegateFactory = paramClientDelegateFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ClientDelegateFactory getClientDelegateFactory()
/*      */   {
/* 1867 */     synchronized (this) {
/* 1868 */       checkShutdownState();
/*      */     }
/* 1870 */     synchronized (this.clientDelegateFactoryAccessorLock) {
/* 1871 */       return this.clientDelegateFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setCorbaContactInfoListFactory(CorbaContactInfoListFactory paramCorbaContactInfoListFactory)
/*      */   {
/* 1879 */     synchronized (this) {
/* 1880 */       checkShutdownState();
/*      */     }
/* 1882 */     synchronized (this.corbaContactInfoListFactoryAccessLock) {
/* 1883 */       this.corbaContactInfoListFactory = paramCorbaContactInfoListFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized CorbaContactInfoListFactory getCorbaContactInfoListFactory()
/*      */   {
/* 1889 */     checkShutdownState();
/* 1890 */     return this.corbaContactInfoListFactory;
/*      */   }
/*      */ 
/*      */   public void setResolver(Resolver paramResolver)
/*      */   {
/* 1898 */     synchronized (this) {
/* 1899 */       checkShutdownState();
/*      */     }
/* 1901 */     synchronized (this.resolverLock) {
/* 1902 */       this.resolver = paramResolver;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Resolver getResolver()
/*      */   {
/* 1911 */     synchronized (this) {
/* 1912 */       checkShutdownState();
/*      */     }
/* 1914 */     synchronized (this.resolverLock) {
/* 1915 */       return this.resolver;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLocalResolver(LocalResolver paramLocalResolver)
/*      */   {
/* 1924 */     synchronized (this) {
/* 1925 */       checkShutdownState();
/*      */     }
/* 1927 */     synchronized (this.resolverLock) {
/* 1928 */       this.localResolver = paramLocalResolver;
/*      */     }
/*      */   }
/*      */ 
/*      */   public LocalResolver getLocalResolver()
/*      */   {
/* 1937 */     synchronized (this) {
/* 1938 */       checkShutdownState();
/*      */     }
/* 1940 */     synchronized (this.resolverLock) {
/* 1941 */       return this.localResolver;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setURLOperation(Operation paramOperation)
/*      */   {
/* 1950 */     synchronized (this) {
/* 1951 */       checkShutdownState();
/*      */     }
/* 1953 */     synchronized (this.urlOperationLock) {
/* 1954 */       this.urlOperation = paramOperation;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Operation getURLOperation()
/*      */   {
/* 1963 */     synchronized (this) {
/* 1964 */       checkShutdownState();
/*      */     }
/* 1966 */     synchronized (this.urlOperationLock) {
/* 1967 */       return this.urlOperation;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setINSDelegate(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher)
/*      */   {
/* 1973 */     synchronized (this) {
/* 1974 */       checkShutdownState();
/*      */     }
/* 1976 */     synchronized (this.resolverLock) {
/* 1977 */       this.insNamingDelegate = paramCorbaServerRequestDispatcher;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TaggedComponentFactoryFinder getTaggedComponentFactoryFinder()
/*      */   {
/* 1983 */     synchronized (this) {
/* 1984 */       checkShutdownState();
/*      */     }
/* 1986 */     return this.taggedComponentFactoryFinder;
/*      */   }
/*      */ 
/*      */   public IdentifiableFactoryFinder getTaggedProfileFactoryFinder()
/*      */   {
/* 1991 */     synchronized (this) {
/* 1992 */       checkShutdownState();
/*      */     }
/* 1994 */     return this.taggedProfileFactoryFinder;
/*      */   }
/*      */ 
/*      */   public IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder()
/*      */   {
/* 1999 */     synchronized (this) {
/* 2000 */       checkShutdownState();
/*      */     }
/* 2002 */     return this.taggedProfileTemplateFactoryFinder;
/*      */   }
/*      */ 
/*      */   public ObjectKeyFactory getObjectKeyFactory()
/*      */   {
/* 2009 */     synchronized (this) {
/* 2010 */       checkShutdownState();
/*      */     }
/* 2012 */     synchronized (this.objectKeyFactoryAccessLock) {
/* 2013 */       return this.objectKeyFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setObjectKeyFactory(ObjectKeyFactory paramObjectKeyFactory)
/*      */   {
/* 2019 */     synchronized (this) {
/* 2020 */       checkShutdownState();
/*      */     }
/* 2022 */     synchronized (this.objectKeyFactoryAccessLock) {
/* 2023 */       this.objectKeyFactory = paramObjectKeyFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TransportManager getTransportManager()
/*      */   {
/* 2031 */     synchronized (this.transportManagerAccessorLock) {
/* 2032 */       if (this.transportManager == null) {
/* 2033 */         this.transportManager = new CorbaTransportManagerImpl(this);
/*      */       }
/* 2035 */       return this.transportManager;
/*      */     }
/*      */   }
/*      */ 
/*      */   public CorbaTransportManager getCorbaTransportManager()
/*      */   {
/* 2041 */     return (CorbaTransportManager)getTransportManager();
/*      */   }
/*      */ 
/*      */   public LegacyServerSocketManager getLegacyServerSocketManager()
/*      */   {
/* 2048 */     synchronized (this) {
/* 2049 */       checkShutdownState();
/*      */     }
/* 2051 */     synchronized (this.legacyServerSocketManagerAccessLock) {
/* 2052 */       if (this.legacyServerSocketManager == null) {
/* 2053 */         this.legacyServerSocketManager = new LegacyServerSocketManagerImpl(this);
/*      */       }
/* 2055 */       return this.legacyServerSocketManager;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setThreadPoolManager(ThreadPoolManager paramThreadPoolManager)
/*      */   {
/* 2063 */     synchronized (this) {
/* 2064 */       checkShutdownState();
/*      */     }
/* 2066 */     synchronized (this.threadPoolManagerAccessLock) {
/* 2067 */       this.threadpoolMgr = paramThreadPoolManager;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ThreadPoolManager getThreadPoolManager()
/*      */   {
/* 2073 */     synchronized (this) {
/* 2074 */       checkShutdownState();
/*      */     }
/* 2076 */     synchronized (this.threadPoolManagerAccessLock) {
/* 2077 */       if (this.threadpoolMgr == null) {
/* 2078 */         this.threadpoolMgr = new ThreadPoolManagerImpl();
/* 2079 */         this.orbOwnsThreadPoolManager = true;
/*      */       }
/* 2081 */       return this.threadpoolMgr;
/*      */     }
/*      */   }
/*      */ 
/*      */   public CopierManager getCopierManager()
/*      */   {
/* 2087 */     synchronized (this) {
/* 2088 */       checkShutdownState();
/*      */     }
/* 2090 */     return this.copierManager;
/*      */   }
/*      */ 
/*      */   private static class ConfigParser extends ParserImplBase
/*      */   {
/*  440 */     public Class configurator = ORBConfiguratorImpl.class;
/*      */ 
/*      */     public PropertyParser makeParser()
/*      */     {
/*  444 */       PropertyParser localPropertyParser = new PropertyParser();
/*  445 */       localPropertyParser.add("com.sun.CORBA.ORBConfigurator", OperationFactory.classAction(), "configurator");
/*      */ 
/*  447 */       return localPropertyParser;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ORBImpl
 * JD-Core Version:    0.6.2
 */