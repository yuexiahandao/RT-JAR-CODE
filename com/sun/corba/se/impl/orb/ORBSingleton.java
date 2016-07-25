/*     */ package com.sun.corba.se.impl.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.AnyImpl;
/*     */ import com.sun.corba.se.impl.corba.ContextListImpl;
/*     */ import com.sun.corba.se.impl.corba.EnvironmentImpl;
/*     */ import com.sun.corba.se.impl.corba.ExceptionListImpl;
/*     */ import com.sun.corba.se.impl.corba.NVListImpl;
/*     */ import com.sun.corba.se.impl.corba.NamedValueImpl;
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
/*     */ import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
/*     */ import com.sun.corba.se.pept.transport.ConnectionCache;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.pept.transport.Selector;
/*     */ import com.sun.corba.se.pept.transport.TransportManager;
/*     */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import com.sun.corba.se.spi.orb.Operation;
/*     */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
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
/*     */ import java.applet.Applet;
/*     */ import java.net.URL;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ContextList;
/*     */ import org.omg.CORBA.Current;
/*     */ import org.omg.CORBA.Environment;
/*     */ import org.omg.CORBA.ExceptionList;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.ORBPackage.InvalidName;
/*     */ import org.omg.CORBA.Object;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.PolicyError;
/*     */ import org.omg.CORBA.Request;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.UnionMember;
/*     */ import org.omg.CORBA.ValueMember;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ValueFactory;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class ORBSingleton extends ORB
/*     */ {
/*     */   private ORB fullORB;
/* 139 */   private static PresentationManager.StubFactoryFactory staticStubFactoryFactory = PresentationDefaults.getStaticStubFactoryFactory();
/*     */ 
/*     */   public void set_parameters(Properties paramProperties)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void set_parameters(Applet paramApplet, Properties paramProperties) {
/*     */   }
/*     */ 
/*     */   protected void set_parameters(String[] paramArrayOfString, Properties paramProperties) {
/*     */   }
/*     */ 
/*     */   public OutputStream create_output_stream() {
/* 152 */     return OutputStreamFactory.newEncapsOutputStream(this);
/*     */   }
/*     */ 
/*     */   public TypeCode create_struct_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember)
/*     */   {
/* 159 */     return new TypeCodeImpl(this, 15, paramString1, paramString2, paramArrayOfStructMember);
/*     */   }
/*     */ 
/*     */   public TypeCode create_union_tc(String paramString1, String paramString2, TypeCode paramTypeCode, UnionMember[] paramArrayOfUnionMember)
/*     */   {
/* 167 */     return new TypeCodeImpl(this, 16, paramString1, paramString2, paramTypeCode, paramArrayOfUnionMember);
/*     */   }
/*     */ 
/*     */   public TypeCode create_enum_tc(String paramString1, String paramString2, String[] paramArrayOfString)
/*     */   {
/* 179 */     return new TypeCodeImpl(this, 17, paramString1, paramString2, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public TypeCode create_alias_tc(String paramString1, String paramString2, TypeCode paramTypeCode)
/*     */   {
/* 186 */     return new TypeCodeImpl(this, 21, paramString1, paramString2, paramTypeCode);
/*     */   }
/*     */ 
/*     */   public TypeCode create_exception_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember)
/*     */   {
/* 193 */     return new TypeCodeImpl(this, 22, paramString1, paramString2, paramArrayOfStructMember);
/*     */   }
/*     */ 
/*     */   public TypeCode create_interface_tc(String paramString1, String paramString2)
/*     */   {
/* 199 */     return new TypeCodeImpl(this, 14, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public TypeCode create_string_tc(int paramInt) {
/* 203 */     return new TypeCodeImpl(this, 18, paramInt);
/*     */   }
/*     */ 
/*     */   public TypeCode create_wstring_tc(int paramInt) {
/* 207 */     return new TypeCodeImpl(this, 27, paramInt);
/*     */   }
/*     */ 
/*     */   public TypeCode create_sequence_tc(int paramInt, TypeCode paramTypeCode)
/*     */   {
/* 213 */     return new TypeCodeImpl(this, 19, paramInt, paramTypeCode);
/*     */   }
/*     */ 
/*     */   public TypeCode create_recursive_sequence_tc(int paramInt1, int paramInt2)
/*     */   {
/* 219 */     return new TypeCodeImpl(this, 19, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public TypeCode create_array_tc(int paramInt, TypeCode paramTypeCode)
/*     */   {
/* 225 */     return new TypeCodeImpl(this, 20, paramInt, paramTypeCode);
/*     */   }
/*     */ 
/*     */   public TypeCode create_native_tc(String paramString1, String paramString2)
/*     */   {
/* 231 */     return new TypeCodeImpl(this, 31, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public TypeCode create_abstract_interface_tc(String paramString1, String paramString2)
/*     */   {
/* 238 */     return new TypeCodeImpl(this, 32, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public TypeCode create_fixed_tc(short paramShort1, short paramShort2)
/*     */   {
/* 243 */     return new TypeCodeImpl(this, 28, paramShort1, paramShort2);
/*     */   }
/*     */ 
/*     */   public TypeCode create_value_tc(String paramString1, String paramString2, short paramShort, TypeCode paramTypeCode, ValueMember[] paramArrayOfValueMember)
/*     */   {
/* 254 */     return new TypeCodeImpl(this, 29, paramString1, paramString2, paramShort, paramTypeCode, paramArrayOfValueMember);
/*     */   }
/*     */ 
/*     */   public TypeCode create_recursive_tc(String paramString)
/*     */   {
/* 259 */     return new TypeCodeImpl(this, paramString);
/*     */   }
/*     */ 
/*     */   public TypeCode create_value_box_tc(String paramString1, String paramString2, TypeCode paramTypeCode)
/*     */   {
/* 266 */     return new TypeCodeImpl(this, 30, paramString1, paramString2, paramTypeCode);
/*     */   }
/*     */ 
/*     */   public TypeCode get_primitive_tc(TCKind paramTCKind)
/*     */   {
/* 271 */     return get_primitive_tc(paramTCKind.value());
/*     */   }
/*     */ 
/*     */   public Any create_any() {
/* 275 */     return new AnyImpl(this);
/*     */   }
/*     */ 
/*     */   public NVList create_list(int paramInt)
/*     */   {
/* 286 */     return new NVListImpl(this, paramInt);
/*     */   }
/*     */ 
/*     */   public NVList create_operation_list(Object paramObject)
/*     */   {
/* 291 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public NamedValue create_named_value(String paramString, Any paramAny, int paramInt)
/*     */   {
/* 296 */     return new NamedValueImpl(this, paramString, paramAny, paramInt);
/*     */   }
/*     */ 
/*     */   public ExceptionList create_exception_list() {
/* 300 */     return new ExceptionListImpl();
/*     */   }
/*     */ 
/*     */   public ContextList create_context_list() {
/* 304 */     return new ContextListImpl(this);
/*     */   }
/*     */ 
/*     */   public Context get_default_context()
/*     */   {
/* 309 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public Environment create_environment()
/*     */   {
/* 314 */     return new EnvironmentImpl();
/*     */   }
/*     */ 
/*     */   public Current get_current()
/*     */   {
/* 319 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public String[] list_initial_services()
/*     */   {
/* 328 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public Object resolve_initial_references(String paramString)
/*     */     throws InvalidName
/*     */   {
/* 334 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public void register_initial_reference(String paramString, Object paramObject)
/*     */     throws InvalidName
/*     */   {
/* 340 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ 
/*     */   public void send_multiple_requests_oneway(Request[] paramArrayOfRequest) {
/* 344 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void send_multiple_requests_deferred(Request[] paramArrayOfRequest) {
/* 348 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public boolean poll_next_response() {
/* 352 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public Request get_next_response() {
/* 356 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public String object_to_string(Object paramObject) {
/* 360 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public Object string_to_object(String paramString) {
/* 364 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public Remote string_to_remote(String paramString)
/*     */     throws RemoteException
/*     */   {
/* 370 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void connect(Object paramObject) {
/* 374 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void disconnect(Object paramObject) {
/* 378 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 383 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void shutdown(boolean paramBoolean)
/*     */   {
/* 388 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   protected void shutdownServants(boolean paramBoolean) {
/* 392 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   protected void destroyConnections() {
/* 396 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void destroy() {
/* 400 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public boolean work_pending()
/*     */   {
/* 405 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void perform_work()
/*     */   {
/* 410 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public ValueFactory register_value_factory(String paramString, ValueFactory paramValueFactory)
/*     */   {
/* 416 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void unregister_value_factory(String paramString)
/*     */   {
/* 421 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public ValueFactory lookup_value_factory(String paramString)
/*     */   {
/* 426 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public TransportManager getTransportManager()
/*     */   {
/* 431 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public CorbaTransportManager getCorbaTransportManager()
/*     */   {
/* 436 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public LegacyServerSocketManager getLegacyServerSocketManager()
/*     */   {
/* 441 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   private synchronized ORB getFullORB()
/*     */   {
/* 450 */     if (this.fullORB == null) {
/* 451 */       Properties localProperties = new Properties();
/* 452 */       this.fullORB = new ORBImpl();
/* 453 */       this.fullORB.set_parameters(localProperties);
/*     */     }
/*     */ 
/* 456 */     return this.fullORB;
/*     */   }
/*     */ 
/*     */   public RequestDispatcherRegistry getRequestDispatcherRegistry()
/*     */   {
/* 463 */     return getFullORB().getRequestDispatcherRegistry();
/*     */   }
/*     */ 
/*     */   public ServiceContextRegistry getServiceContextRegistry()
/*     */   {
/* 471 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getTransientServerId()
/*     */   {
/* 479 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getORBInitialPort()
/*     */   {
/* 487 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public String getORBInitialHost()
/*     */   {
/* 495 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public String getORBServerHost()
/*     */   {
/* 500 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getORBServerPort()
/*     */   {
/* 505 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo getCodeSetComponentInfo()
/*     */   {
/* 510 */     return new CodeSetComponentInfo();
/*     */   }
/*     */ 
/*     */   public boolean isLocalHost(String paramString)
/*     */   {
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isLocalServerId(int paramInt1, int paramInt2)
/*     */   {
/* 522 */     return false;
/*     */   }
/*     */ 
/*     */   public ORBVersion getORBVersion()
/*     */   {
/* 532 */     return ORBVersionFactory.getORBVersion();
/*     */   }
/*     */ 
/*     */   public void setORBVersion(ORBVersion paramORBVersion)
/*     */   {
/* 537 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public String getAppletHost()
/*     */   {
/* 542 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public URL getAppletCodeBase()
/*     */   {
/* 547 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getHighWaterMark() {
/* 551 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getLowWaterMark() {
/* 555 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getNumberToReclaim() {
/* 559 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public int getGIOPFragmentSize() {
/* 563 */     return 1024;
/*     */   }
/*     */ 
/*     */   public int getGIOPBuffMgrStrategy(GIOPVersion paramGIOPVersion) {
/* 567 */     return 0;
/*     */   }
/*     */ 
/*     */   public IOR getFVDCodeBaseIOR() {
/* 571 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public Policy create_policy(int paramInt, Any paramAny) throws PolicyError
/*     */   {
/* 576 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public LegacyServerSocketEndPointInfo getServerEndpoint()
/*     */   {
/* 581 */     return null;
/*     */   }
/*     */ 
/*     */   public void setPersistentServerId(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public TypeCodeImpl getTypeCodeForClass(Class paramClass)
/*     */   {
/* 590 */     return null;
/*     */   }
/*     */ 
/*     */   public void setTypeCodeForClass(Class paramClass, TypeCodeImpl paramTypeCodeImpl)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean alwaysSendCodeSetServiceContext()
/*     */   {
/* 599 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isDuringDispatch()
/*     */   {
/* 604 */     return false;
/*     */   }
/*     */ 
/*     */   public void notifyORB() {
/*     */   }
/*     */ 
/*     */   public PIHandler getPIHandler() {
/* 611 */     return null;
/*     */   }
/*     */ 
/*     */   public void checkShutdownState()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startingDispatch()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void finishedDispatch()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void registerInitialReference(String paramString, Closure paramClosure)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ORBData getORBData()
/*     */   {
/* 632 */     return getFullORB().getORBData();
/*     */   }
/*     */ 
/*     */   public void setClientDelegateFactory(ClientDelegateFactory paramClientDelegateFactory)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ClientDelegateFactory getClientDelegateFactory()
/*     */   {
/* 641 */     return getFullORB().getClientDelegateFactory();
/*     */   }
/*     */ 
/*     */   public void setCorbaContactInfoListFactory(CorbaContactInfoListFactory paramCorbaContactInfoListFactory)
/*     */   {
/*     */   }
/*     */ 
/*     */   public CorbaContactInfoListFactory getCorbaContactInfoListFactory()
/*     */   {
/* 650 */     return getFullORB().getCorbaContactInfoListFactory();
/*     */   }
/*     */ 
/*     */   public Operation getURLOperation()
/*     */   {
/* 655 */     return null;
/*     */   }
/*     */ 
/*     */   public void setINSDelegate(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher)
/*     */   {
/*     */   }
/*     */ 
/*     */   public TaggedComponentFactoryFinder getTaggedComponentFactoryFinder()
/*     */   {
/* 664 */     return getFullORB().getTaggedComponentFactoryFinder();
/*     */   }
/*     */ 
/*     */   public IdentifiableFactoryFinder getTaggedProfileFactoryFinder()
/*     */   {
/* 669 */     return getFullORB().getTaggedProfileFactoryFinder();
/*     */   }
/*     */ 
/*     */   public IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder()
/*     */   {
/* 674 */     return getFullORB().getTaggedProfileTemplateFactoryFinder();
/*     */   }
/*     */ 
/*     */   public ObjectKeyFactory getObjectKeyFactory()
/*     */   {
/* 679 */     return getFullORB().getObjectKeyFactory();
/*     */   }
/*     */ 
/*     */   public void setObjectKeyFactory(ObjectKeyFactory paramObjectKeyFactory)
/*     */   {
/* 684 */     throw new SecurityException("ORBSingleton: access denied");
/*     */   }
/*     */ 
/*     */   public void handleBadServerId(ObjectKey paramObjectKey)
/*     */   {
/*     */   }
/*     */ 
/*     */   public OAInvocationInfo peekInvocationInfo()
/*     */   {
/* 693 */     return null;
/*     */   }
/*     */ 
/*     */   public void pushInvocationInfo(OAInvocationInfo paramOAInvocationInfo)
/*     */   {
/*     */   }
/*     */ 
/*     */   public OAInvocationInfo popInvocationInfo()
/*     */   {
/* 702 */     return null;
/*     */   }
/*     */ 
/*     */   public ClientInvocationInfo createOrIncrementInvocationInfo()
/*     */   {
/* 707 */     return null;
/*     */   }
/*     */ 
/*     */   public void releaseOrDecrementInvocationInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ClientInvocationInfo getInvocationInfo()
/*     */   {
/* 716 */     return null;
/*     */   }
/*     */ 
/*     */   public ConnectionCache getConnectionCache(ContactInfo paramContactInfo)
/*     */   {
/* 721 */     return null;
/*     */   }
/*     */ 
/*     */   public void setResolver(Resolver paramResolver)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Resolver getResolver()
/*     */   {
/* 730 */     return null;
/*     */   }
/*     */ 
/*     */   public void setLocalResolver(LocalResolver paramLocalResolver)
/*     */   {
/*     */   }
/*     */ 
/*     */   public LocalResolver getLocalResolver()
/*     */   {
/* 739 */     return null;
/*     */   }
/*     */ 
/*     */   public void setURLOperation(Operation paramOperation)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setBadServerIdHandler(BadServerIdHandler paramBadServerIdHandler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initBadServerIdHandler()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Selector getSelector(int paramInt)
/*     */   {
/* 758 */     return null;
/*     */   }
/*     */ 
/*     */   public void setThreadPoolManager(ThreadPoolManager paramThreadPoolManager) {
/*     */   }
/*     */ 
/*     */   public ThreadPoolManager getThreadPoolManager() {
/* 765 */     return null;
/*     */   }
/*     */ 
/*     */   public CopierManager getCopierManager() {
/* 769 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ORBSingleton
 * JD-Core Version:    0.6.2
 */