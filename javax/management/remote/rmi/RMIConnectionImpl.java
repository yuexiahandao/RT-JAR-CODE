/*      */ package javax.management.remote.rmi;
/*      */ 
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import com.sun.jmx.remote.internal.ServerCommunicatorAdmin;
/*      */ import com.sun.jmx.remote.internal.ServerNotifForwarder;
/*      */ import com.sun.jmx.remote.security.JMXSubjectDomainCombiner;
/*      */ import com.sun.jmx.remote.security.SubjectDelegator;
/*      */ import com.sun.jmx.remote.util.ClassLoaderWithRepository;
/*      */ import com.sun.jmx.remote.util.ClassLogger;
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import com.sun.jmx.remote.util.OrderClassLoaders;
/*      */ import java.io.IOException;
/*      */ import java.rmi.MarshalledObject;
/*      */ import java.rmi.UnmarshalException;
/*      */ import java.rmi.server.Unreferenced;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.security.Permissions;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.management.Attribute;
/*      */ import javax.management.AttributeList;
/*      */ import javax.management.AttributeNotFoundException;
/*      */ import javax.management.InstanceAlreadyExistsException;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.IntrospectionException;
/*      */ import javax.management.InvalidAttributeValueException;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanInfo;
/*      */ import javax.management.MBeanPermission;
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.ObjectInstance;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.QueryExp;
/*      */ import javax.management.ReflectionException;
/*      */ import javax.management.RuntimeOperationsException;
/*      */ import javax.management.remote.JMXServerErrorException;
/*      */ import javax.management.remote.NotificationResult;
/*      */ import javax.management.remote.TargetedNotification;
/*      */ import javax.security.auth.Subject;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class RMIConnectionImpl
/*      */   implements RMIConnection, Unreferenced
/*      */ {
/* 1656 */   private static final Object[] NO_OBJECTS = new Object[0];
/* 1657 */   private static final String[] NO_STRINGS = new String[0];
/*      */   private final Subject subject;
/*      */   private final SubjectDelegator subjectDelegator;
/*      */   private final boolean removeCallerContext;
/*      */   private final AccessControlContext acc;
/*      */   private final RMIServerImpl rmiServer;
/*      */   private final MBeanServer mbeanServer;
/*      */   private final ClassLoader defaultClassLoader;
/*      */   private final ClassLoader defaultContextClassLoader;
/*      */   private final ClassLoaderWithRepository classLoaderWithRepository;
/* 1712 */   private boolean terminated = false;
/*      */   private final String connectionId;
/*      */   private final ServerCommunicatorAdmin serverCommunicatorAdmin;
/*      */   private static final int ADD_NOTIFICATION_LISTENERS = 1;
/*      */   private static final int ADD_NOTIFICATION_LISTENER_OBJECTNAME = 2;
/*      */   private static final int CREATE_MBEAN = 3;
/*      */   private static final int CREATE_MBEAN_PARAMS = 4;
/*      */   private static final int CREATE_MBEAN_LOADER = 5;
/*      */   private static final int CREATE_MBEAN_LOADER_PARAMS = 6;
/*      */   private static final int GET_ATTRIBUTE = 7;
/*      */   private static final int GET_ATTRIBUTES = 8;
/*      */   private static final int GET_DEFAULT_DOMAIN = 9;
/*      */   private static final int GET_DOMAINS = 10;
/*      */   private static final int GET_MBEAN_COUNT = 11;
/*      */   private static final int GET_MBEAN_INFO = 12;
/*      */   private static final int GET_OBJECT_INSTANCE = 13;
/*      */   private static final int INVOKE = 14;
/*      */   private static final int IS_INSTANCE_OF = 15;
/*      */   private static final int IS_REGISTERED = 16;
/*      */   private static final int QUERY_MBEANS = 17;
/*      */   private static final int QUERY_NAMES = 18;
/*      */   private static final int REMOVE_NOTIFICATION_LISTENER = 19;
/*      */   private static final int REMOVE_NOTIFICATION_LISTENER_OBJECTNAME = 20;
/*      */   private static final int REMOVE_NOTIFICATION_LISTENER_OBJECTNAME_FILTER_HANDBACK = 21;
/*      */   private static final int SET_ATTRIBUTE = 22;
/*      */   private static final int SET_ATTRIBUTES = 23;
/*      */   private static final int UNREGISTER_MBEAN = 24;
/*      */   private ServerNotifForwarder serverNotifForwarder;
/*      */   private Map<String, ?> env;
/* 1790 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnectionImpl");
/*      */ 
/*      */   public RMIConnectionImpl(RMIServerImpl paramRMIServerImpl, String paramString, ClassLoader paramClassLoader, Subject paramSubject, Map<String, ?> paramMap)
/*      */   {
/*  127 */     if ((paramRMIServerImpl == null) || (paramString == null))
/*  128 */       throw new NullPointerException("Illegal null argument");
/*  129 */     if (paramMap == null)
/*  130 */       paramMap = Collections.emptyMap();
/*  131 */     this.rmiServer = paramRMIServerImpl;
/*  132 */     this.connectionId = paramString;
/*  133 */     this.defaultClassLoader = paramClassLoader;
/*      */ 
/*  135 */     this.subjectDelegator = new SubjectDelegator();
/*  136 */     this.subject = paramSubject;
/*  137 */     if (paramSubject == null) {
/*  138 */       this.acc = null;
/*  139 */       this.removeCallerContext = false;
/*      */     } else {
/*  141 */       this.removeCallerContext = SubjectDelegator.checkRemoveCallerContext(paramSubject);
/*      */ 
/*  143 */       if (this.removeCallerContext) {
/*  144 */         this.acc = JMXSubjectDomainCombiner.getDomainCombinerContext(paramSubject);
/*      */       }
/*      */       else {
/*  147 */         this.acc = JMXSubjectDomainCombiner.getContext(paramSubject);
/*      */       }
/*      */     }
/*      */ 
/*  151 */     this.mbeanServer = paramRMIServerImpl.getMBeanServer();
/*      */ 
/*  153 */     final ClassLoader localClassLoader = paramClassLoader;
/*      */ 
/*  155 */     this.classLoaderWithRepository = ((ClassLoaderWithRepository)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ClassLoaderWithRepository run()
/*      */       {
/*  159 */         return new ClassLoaderWithRepository(RMIConnectionImpl.this.mbeanServer.getClassLoaderRepository(), localClassLoader);
/*      */       }
/*      */     }
/*      */     , withPermissions(new Permission[] { new MBeanPermission("*", "getClassLoaderRepository"), new RuntimePermission("createClassLoader") })));
/*      */ 
/*  170 */     this.defaultContextClassLoader = ((ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ClassLoader run()
/*      */       {
/*  175 */         return new RMIConnectionImpl.CombinedClassLoader(Thread.currentThread().getContextClassLoader(), localClassLoader, null);
/*      */       }
/*      */     }));
/*  180 */     this.serverCommunicatorAdmin = new RMIServerCommunicatorAdmin(EnvHelp.getServerConnectionTimeout(paramMap));
/*      */ 
/*  183 */     this.env = paramMap;
/*      */   }
/*      */ 
/*      */   private static AccessControlContext withPermissions(Permission[] paramArrayOfPermission) {
/*  187 */     Permissions localPermissions = new Permissions();
/*      */ 
/*  189 */     for (Permission localPermission : paramArrayOfPermission) {
/*  190 */       localPermissions.add(localPermission);
/*      */     }
/*      */ 
/*  193 */     ??? = new ProtectionDomain(null, localPermissions);
/*  194 */     return new AccessControlContext(new ProtectionDomain[] { ??? });
/*      */   }
/*      */ 
/*      */   private synchronized ServerNotifForwarder getServerNotifFwd()
/*      */   {
/*  200 */     if (this.serverNotifForwarder == null) {
/*  201 */       this.serverNotifForwarder = new ServerNotifForwarder(this.mbeanServer, this.env, this.rmiServer.getNotifBuffer(), this.connectionId);
/*      */     }
/*      */ 
/*  206 */     return this.serverNotifForwarder;
/*      */   }
/*      */ 
/*      */   public String getConnectionId() throws IOException
/*      */   {
/*  211 */     return this.connectionId;
/*      */   }
/*      */ 
/*      */   public void close() throws IOException {
/*  215 */     boolean bool = logger.debugOn();
/*  216 */     String str = bool ? "[" + toString() + "]" : null;
/*      */ 
/*  218 */     synchronized (this) {
/*  219 */       if (this.terminated) {
/*  220 */         if (bool) logger.debug("close", str + " already terminated.");
/*  221 */         return;
/*      */       }
/*      */ 
/*  224 */       if (bool) logger.debug("close", str + " closing.");
/*      */ 
/*  226 */       this.terminated = true;
/*      */ 
/*  228 */       if (this.serverCommunicatorAdmin != null) {
/*  229 */         this.serverCommunicatorAdmin.terminate();
/*      */       }
/*      */ 
/*  232 */       if (this.serverNotifForwarder != null) {
/*  233 */         this.serverNotifForwarder.terminate();
/*      */       }
/*      */     }
/*      */ 
/*  237 */     this.rmiServer.clientClosed(this);
/*      */ 
/*  239 */     if (bool) logger.debug("close", str + " closed."); 
/*      */   }
/*      */ 
/*      */   public void unreferenced()
/*      */   {
/*  243 */     logger.debug("unreferenced", "called");
/*      */     try {
/*  245 */       close();
/*  246 */       logger.debug("unreferenced", "done");
/*      */     } catch (IOException localIOException) {
/*  248 */       logger.fine("unreferenced", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Subject paramSubject)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  267 */       Object[] arrayOfObject = { paramString, paramObjectName };
/*      */ 
/*  270 */       if (logger.debugOn()) {
/*  271 */         logger.debug("createMBean(String,ObjectName)", "connectionId=" + this.connectionId + ", className=" + paramString + ", name=" + paramObjectName);
/*      */       }
/*      */ 
/*  275 */       return (ObjectInstance)doPrivilegedOperation(3, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  281 */       Exception localException = extractException(localPrivilegedActionException);
/*  282 */       if ((localException instanceof ReflectionException))
/*  283 */         throw ((ReflectionException)localException);
/*  284 */       if ((localException instanceof InstanceAlreadyExistsException))
/*  285 */         throw ((InstanceAlreadyExistsException)localException);
/*  286 */       if ((localException instanceof MBeanRegistrationException))
/*  287 */         throw ((MBeanRegistrationException)localException);
/*  288 */       if ((localException instanceof MBeanException))
/*  289 */         throw ((MBeanException)localException);
/*  290 */       if ((localException instanceof NotCompliantMBeanException))
/*  291 */         throw ((NotCompliantMBeanException)localException);
/*  292 */       if ((localException instanceof IOException))
/*  293 */         throw ((IOException)localException);
/*  294 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  311 */       Object[] arrayOfObject = { paramString, paramObjectName1, paramObjectName2 };
/*      */ 
/*  314 */       if (logger.debugOn()) {
/*  315 */         logger.debug("createMBean(String,ObjectName,ObjectName)", "connectionId=" + this.connectionId + ", className=" + paramString + ", name=" + paramObjectName1 + ", loaderName=" + paramObjectName2);
/*      */       }
/*      */ 
/*  321 */       return (ObjectInstance)doPrivilegedOperation(5, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  327 */       Exception localException = extractException(localPrivilegedActionException);
/*  328 */       if ((localException instanceof ReflectionException))
/*  329 */         throw ((ReflectionException)localException);
/*  330 */       if ((localException instanceof InstanceAlreadyExistsException))
/*  331 */         throw ((InstanceAlreadyExistsException)localException);
/*  332 */       if ((localException instanceof MBeanRegistrationException))
/*  333 */         throw ((MBeanRegistrationException)localException);
/*  334 */       if ((localException instanceof MBeanException))
/*  335 */         throw ((MBeanException)localException);
/*  336 */       if ((localException instanceof NotCompliantMBeanException))
/*  337 */         throw ((NotCompliantMBeanException)localException);
/*  338 */       if ((localException instanceof InstanceNotFoundException))
/*  339 */         throw ((InstanceNotFoundException)localException);
/*  340 */       if ((localException instanceof IOException))
/*  341 */         throw ((IOException)localException);
/*  342 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
/*      */   {
/*  361 */     boolean bool = logger.debugOn();
/*      */ 
/*  363 */     if (bool) logger.debug("createMBean(String,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", unwrapping parameters using classLoaderWithRepository.");
/*      */ 
/*  368 */     Object[] arrayOfObject1 = nullIsEmpty((Object[])unwrap(paramMarshalledObject, this.classLoaderWithRepository, [Ljava.lang.Object.class));
/*      */     try
/*      */     {
/*  372 */       Object[] arrayOfObject2 = { paramString, paramObjectName, arrayOfObject1, nullIsEmpty(paramArrayOfString) };
/*      */ 
/*  376 */       if (bool) {
/*  377 */         logger.debug("createMBean(String,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", className=" + paramString + ", name=" + paramObjectName + ", params=" + objects(arrayOfObject1) + ", signature=" + strings(paramArrayOfString));
/*      */       }
/*      */ 
/*  384 */       return (ObjectInstance)doPrivilegedOperation(4, arrayOfObject2, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  390 */       Exception localException = extractException(localPrivilegedActionException);
/*  391 */       if ((localException instanceof ReflectionException))
/*  392 */         throw ((ReflectionException)localException);
/*  393 */       if ((localException instanceof InstanceAlreadyExistsException))
/*  394 */         throw ((InstanceAlreadyExistsException)localException);
/*  395 */       if ((localException instanceof MBeanRegistrationException))
/*  396 */         throw ((MBeanRegistrationException)localException);
/*  397 */       if ((localException instanceof MBeanException))
/*  398 */         throw ((MBeanException)localException);
/*  399 */       if ((localException instanceof NotCompliantMBeanException))
/*  400 */         throw ((NotCompliantMBeanException)localException);
/*  401 */       if ((localException instanceof IOException))
/*  402 */         throw ((IOException)localException);
/*  403 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
/*      */   {
/*  424 */     boolean bool = logger.debugOn();
/*      */ 
/*  426 */     if (bool) logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", unwrapping params with MBean extended ClassLoader.");
/*      */ 
/*  431 */     Object[] arrayOfObject1 = nullIsEmpty((Object[])unwrap(paramMarshalledObject, getClassLoader(paramObjectName2), this.defaultClassLoader, [Ljava.lang.Object.class));
/*      */     try
/*      */     {
/*  437 */       Object[] arrayOfObject2 = { paramString, paramObjectName1, paramObjectName2, arrayOfObject1, nullIsEmpty(paramArrayOfString) };
/*      */ 
/*  441 */       if (bool) logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", className=" + paramString + ", name=" + paramObjectName1 + ", loaderName=" + paramObjectName2 + ", params=" + objects(arrayOfObject1) + ", signature=" + strings(paramArrayOfString));
/*      */ 
/*  450 */       return (ObjectInstance)doPrivilegedOperation(6, arrayOfObject2, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  456 */       Exception localException = extractException(localPrivilegedActionException);
/*  457 */       if ((localException instanceof ReflectionException))
/*  458 */         throw ((ReflectionException)localException);
/*  459 */       if ((localException instanceof InstanceAlreadyExistsException))
/*  460 */         throw ((InstanceAlreadyExistsException)localException);
/*  461 */       if ((localException instanceof MBeanRegistrationException))
/*  462 */         throw ((MBeanRegistrationException)localException);
/*  463 */       if ((localException instanceof MBeanException))
/*  464 */         throw ((MBeanException)localException);
/*  465 */       if ((localException instanceof NotCompliantMBeanException))
/*  466 */         throw ((NotCompliantMBeanException)localException);
/*  467 */       if ((localException instanceof InstanceNotFoundException))
/*  468 */         throw ((InstanceNotFoundException)localException);
/*  469 */       if ((localException instanceof IOException))
/*  470 */         throw ((IOException)localException);
/*  471 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unregisterMBean(ObjectName paramObjectName, Subject paramSubject)
/*      */     throws InstanceNotFoundException, MBeanRegistrationException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  481 */       Object[] arrayOfObject = { paramObjectName };
/*      */ 
/*  483 */       if (logger.debugOn()) logger.debug("unregisterMBean", "connectionId=" + this.connectionId + ", name=" + paramObjectName);
/*      */ 
/*  487 */       doPrivilegedOperation(24, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  492 */       Exception localException = extractException(localPrivilegedActionException);
/*  493 */       if ((localException instanceof InstanceNotFoundException))
/*  494 */         throw ((InstanceNotFoundException)localException);
/*  495 */       if ((localException instanceof MBeanRegistrationException))
/*  496 */         throw ((MBeanRegistrationException)localException);
/*  497 */       if ((localException instanceof IOException))
/*  498 */         throw ((IOException)localException);
/*  499 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance getObjectInstance(ObjectName paramObjectName, Subject paramSubject)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/*  509 */     checkNonNull("ObjectName", paramObjectName);
/*      */     try
/*      */     {
/*  512 */       Object[] arrayOfObject = { paramObjectName };
/*      */ 
/*  514 */       if (logger.debugOn()) logger.debug("getObjectInstance", "connectionId=" + this.connectionId + ", name=" + paramObjectName);
/*      */ 
/*  518 */       return (ObjectInstance)doPrivilegedOperation(13, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  524 */       Exception localException = extractException(localPrivilegedActionException);
/*  525 */       if ((localException instanceof InstanceNotFoundException))
/*  526 */         throw ((InstanceNotFoundException)localException);
/*  527 */       if ((localException instanceof IOException))
/*  528 */         throw ((IOException)localException);
/*  529 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
/*      */     throws IOException
/*      */   {
/*  540 */     boolean bool = logger.debugOn();
/*      */ 
/*  542 */     if (bool) logger.debug("queryMBeans", "connectionId=" + this.connectionId + " unwrapping query with defaultClassLoader.");
/*      */ 
/*  546 */     QueryExp localQueryExp = (QueryExp)unwrap(paramMarshalledObject, this.defaultContextClassLoader, QueryExp.class);
/*      */     try
/*      */     {
/*  549 */       Object[] arrayOfObject = { paramObjectName, localQueryExp };
/*      */ 
/*  551 */       if (bool) logger.debug("queryMBeans", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", query=" + paramMarshalledObject);
/*      */ 
/*  555 */       return (Set)Util.cast(doPrivilegedOperation(17, arrayOfObject, paramSubject));
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  561 */       Exception localException = extractException(localPrivilegedActionException);
/*  562 */       if ((localException instanceof IOException))
/*  563 */         throw ((IOException)localException);
/*  564 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Set<ObjectName> queryNames(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
/*      */     throws IOException
/*      */   {
/*  575 */     boolean bool = logger.debugOn();
/*      */ 
/*  577 */     if (bool) logger.debug("queryNames", "connectionId=" + this.connectionId + " unwrapping query with defaultClassLoader.");
/*      */ 
/*  581 */     QueryExp localQueryExp = (QueryExp)unwrap(paramMarshalledObject, this.defaultContextClassLoader, QueryExp.class);
/*      */     try
/*      */     {
/*  584 */       Object[] arrayOfObject = { paramObjectName, localQueryExp };
/*      */ 
/*  586 */       if (bool) logger.debug("queryNames", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", query=" + paramMarshalledObject);
/*      */ 
/*  590 */       return (Set)Util.cast(doPrivilegedOperation(18, arrayOfObject, paramSubject));
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  596 */       Exception localException = extractException(localPrivilegedActionException);
/*  597 */       if ((localException instanceof IOException))
/*  598 */         throw ((IOException)localException);
/*  599 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isRegistered(ObjectName paramObjectName, Subject paramSubject) throws IOException
/*      */   {
/*      */     try {
/*  606 */       Object[] arrayOfObject = { paramObjectName };
/*  607 */       return ((Boolean)doPrivilegedOperation(16, arrayOfObject, paramSubject)).booleanValue();
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  613 */       Exception localException = extractException(localPrivilegedActionException);
/*  614 */       if ((localException instanceof IOException))
/*  615 */         throw ((IOException)localException);
/*  616 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Integer getMBeanCount(Subject paramSubject) throws IOException
/*      */   {
/*      */     try {
/*  623 */       Object[] arrayOfObject = new Object[0];
/*      */ 
/*  625 */       if (logger.debugOn()) logger.debug("getMBeanCount", "connectionId=" + this.connectionId);
/*      */ 
/*  628 */       return (Integer)doPrivilegedOperation(11, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  634 */       Exception localException = extractException(localPrivilegedActionException);
/*  635 */       if ((localException instanceof IOException))
/*  636 */         throw ((IOException)localException);
/*  637 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getAttribute(ObjectName paramObjectName, String paramString, Subject paramSubject)
/*      */     throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  651 */       Object[] arrayOfObject = { paramObjectName, paramString };
/*  652 */       if (logger.debugOn()) logger.debug("getAttribute", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", attribute=" + paramString);
/*      */ 
/*  657 */       return doPrivilegedOperation(7, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  663 */       Exception localException = extractException(localPrivilegedActionException);
/*  664 */       if ((localException instanceof MBeanException))
/*  665 */         throw ((MBeanException)localException);
/*  666 */       if ((localException instanceof AttributeNotFoundException))
/*  667 */         throw ((AttributeNotFoundException)localException);
/*  668 */       if ((localException instanceof InstanceNotFoundException))
/*  669 */         throw ((InstanceNotFoundException)localException);
/*  670 */       if ((localException instanceof ReflectionException))
/*  671 */         throw ((ReflectionException)localException);
/*  672 */       if ((localException instanceof IOException))
/*  673 */         throw ((IOException)localException);
/*  674 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString, Subject paramSubject)
/*      */     throws InstanceNotFoundException, ReflectionException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  686 */       Object[] arrayOfObject = { paramObjectName, paramArrayOfString };
/*      */ 
/*  688 */       if (logger.debugOn()) logger.debug("getAttributes", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", attributes=" + strings(paramArrayOfString));
/*      */ 
/*  693 */       return (AttributeList)doPrivilegedOperation(8, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  699 */       Exception localException = extractException(localPrivilegedActionException);
/*  700 */       if ((localException instanceof InstanceNotFoundException))
/*  701 */         throw ((InstanceNotFoundException)localException);
/*  702 */       if ((localException instanceof ReflectionException))
/*  703 */         throw ((ReflectionException)localException);
/*  704 */       if ((localException instanceof IOException))
/*  705 */         throw ((IOException)localException);
/*  706 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAttribute(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
/*      */     throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException
/*      */   {
/*  722 */     boolean bool = logger.debugOn();
/*      */ 
/*  724 */     if (bool) logger.debug("setAttribute", "connectionId=" + this.connectionId + " unwrapping attribute with MBean extended ClassLoader.");
/*      */ 
/*  728 */     Attribute localAttribute = (Attribute)unwrap(paramMarshalledObject, getClassLoaderFor(paramObjectName), this.defaultClassLoader, Attribute.class);
/*      */     try
/*      */     {
/*  734 */       Object[] arrayOfObject = { paramObjectName, localAttribute };
/*      */ 
/*  736 */       if (bool) logger.debug("setAttribute", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", attribute=" + localAttribute);
/*      */ 
/*  741 */       doPrivilegedOperation(22, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  746 */       Exception localException = extractException(localPrivilegedActionException);
/*  747 */       if ((localException instanceof InstanceNotFoundException))
/*  748 */         throw ((InstanceNotFoundException)localException);
/*  749 */       if ((localException instanceof AttributeNotFoundException))
/*  750 */         throw ((AttributeNotFoundException)localException);
/*  751 */       if ((localException instanceof InvalidAttributeValueException))
/*  752 */         throw ((InvalidAttributeValueException)localException);
/*  753 */       if ((localException instanceof MBeanException))
/*  754 */         throw ((MBeanException)localException);
/*  755 */       if ((localException instanceof ReflectionException))
/*  756 */         throw ((ReflectionException)localException);
/*  757 */       if ((localException instanceof IOException))
/*  758 */         throw ((IOException)localException);
/*  759 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributeList setAttributes(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
/*      */     throws InstanceNotFoundException, ReflectionException, IOException
/*      */   {
/*  772 */     boolean bool = logger.debugOn();
/*      */ 
/*  774 */     if (bool) logger.debug("setAttributes", "connectionId=" + this.connectionId + " unwrapping attributes with MBean extended ClassLoader.");
/*      */ 
/*  778 */     AttributeList localAttributeList = (AttributeList)unwrap(paramMarshalledObject, getClassLoaderFor(paramObjectName), this.defaultClassLoader, AttributeList.class);
/*      */     try
/*      */     {
/*  785 */       Object[] arrayOfObject = { paramObjectName, localAttributeList };
/*      */ 
/*  787 */       if (bool) logger.debug("setAttributes", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", attributes=" + localAttributeList);
/*      */ 
/*  792 */       return (AttributeList)doPrivilegedOperation(23, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  798 */       Exception localException = extractException(localPrivilegedActionException);
/*  799 */       if ((localException instanceof InstanceNotFoundException))
/*  800 */         throw ((InstanceNotFoundException)localException);
/*  801 */       if ((localException instanceof ReflectionException))
/*  802 */         throw ((ReflectionException)localException);
/*  803 */       if ((localException instanceof IOException))
/*  804 */         throw ((IOException)localException);
/*  805 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object invoke(ObjectName paramObjectName, String paramString, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
/*      */     throws InstanceNotFoundException, MBeanException, ReflectionException, IOException
/*      */   {
/*  821 */     checkNonNull("ObjectName", paramObjectName);
/*  822 */     checkNonNull("Operation name", paramString);
/*      */ 
/*  825 */     boolean bool = logger.debugOn();
/*      */ 
/*  827 */     if (bool) logger.debug("invoke", "connectionId=" + this.connectionId + " unwrapping params with MBean extended ClassLoader.");
/*      */ 
/*  831 */     Object[] arrayOfObject1 = nullIsEmpty((Object[])unwrap(paramMarshalledObject, getClassLoaderFor(paramObjectName), this.defaultClassLoader, [Ljava.lang.Object.class));
/*      */     try
/*      */     {
/*  837 */       Object[] arrayOfObject2 = { paramObjectName, paramString, arrayOfObject1, nullIsEmpty(paramArrayOfString) };
/*      */ 
/*  841 */       if (bool) logger.debug("invoke", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", operationName=" + paramString + ", params=" + objects(arrayOfObject1) + ", signature=" + strings(paramArrayOfString));
/*      */ 
/*  848 */       return doPrivilegedOperation(14, arrayOfObject2, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  854 */       Exception localException = extractException(localPrivilegedActionException);
/*  855 */       if ((localException instanceof InstanceNotFoundException))
/*  856 */         throw ((InstanceNotFoundException)localException);
/*  857 */       if ((localException instanceof MBeanException))
/*  858 */         throw ((MBeanException)localException);
/*  859 */       if ((localException instanceof ReflectionException))
/*  860 */         throw ((ReflectionException)localException);
/*  861 */       if ((localException instanceof IOException))
/*  862 */         throw ((IOException)localException);
/*  863 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getDefaultDomain(Subject paramSubject) throws IOException
/*      */   {
/*      */     try {
/*  870 */       Object[] arrayOfObject = new Object[0];
/*      */ 
/*  872 */       if (logger.debugOn()) logger.debug("getDefaultDomain", "connectionId=" + this.connectionId);
/*      */ 
/*  875 */       return (String)doPrivilegedOperation(9, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  881 */       Exception localException = extractException(localPrivilegedActionException);
/*  882 */       if ((localException instanceof IOException))
/*  883 */         throw ((IOException)localException);
/*  884 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getDomains(Subject paramSubject) throws IOException {
/*      */     try {
/*  890 */       Object[] arrayOfObject = new Object[0];
/*      */ 
/*  892 */       if (logger.debugOn()) logger.debug("getDomains", "connectionId=" + this.connectionId);
/*      */ 
/*  895 */       return (String[])doPrivilegedOperation(10, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  901 */       Exception localException = extractException(localPrivilegedActionException);
/*  902 */       if ((localException instanceof IOException))
/*  903 */         throw ((IOException)localException);
/*  904 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public MBeanInfo getMBeanInfo(ObjectName paramObjectName, Subject paramSubject)
/*      */     throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException
/*      */   {
/*  915 */     checkNonNull("ObjectName", paramObjectName);
/*      */     try
/*      */     {
/*  918 */       Object[] arrayOfObject = { paramObjectName };
/*      */ 
/*  920 */       if (logger.debugOn()) logger.debug("getMBeanInfo", "connectionId=" + this.connectionId + ", name=" + paramObjectName);
/*      */ 
/*  924 */       return (MBeanInfo)doPrivilegedOperation(12, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  930 */       Exception localException = extractException(localPrivilegedActionException);
/*  931 */       if ((localException instanceof InstanceNotFoundException))
/*  932 */         throw ((InstanceNotFoundException)localException);
/*  933 */       if ((localException instanceof IntrospectionException))
/*  934 */         throw ((IntrospectionException)localException);
/*  935 */       if ((localException instanceof ReflectionException))
/*  936 */         throw ((ReflectionException)localException);
/*  937 */       if ((localException instanceof IOException))
/*  938 */         throw ((IOException)localException);
/*  939 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isInstanceOf(ObjectName paramObjectName, String paramString, Subject paramSubject)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/*  948 */     checkNonNull("ObjectName", paramObjectName);
/*      */     try
/*      */     {
/*  951 */       Object[] arrayOfObject = { paramObjectName, paramString };
/*      */ 
/*  953 */       if (logger.debugOn()) logger.debug("isInstanceOf", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", className=" + paramString);
/*      */ 
/*  958 */       return ((Boolean)doPrivilegedOperation(15, arrayOfObject, paramSubject)).booleanValue();
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  964 */       Exception localException = extractException(localPrivilegedActionException);
/*  965 */       if ((localException instanceof InstanceNotFoundException))
/*  966 */         throw ((InstanceNotFoundException)localException);
/*  967 */       if ((localException instanceof IOException))
/*  968 */         throw ((IOException)localException);
/*  969 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Integer[] addNotificationListeners(ObjectName[] paramArrayOfObjectName, MarshalledObject[] paramArrayOfMarshalledObject, Subject[] paramArrayOfSubject)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/*  979 */     if ((paramArrayOfObjectName == null) || (paramArrayOfMarshalledObject == null)) {
/*  980 */       throw new IllegalArgumentException("Got null arguments.");
/*      */     }
/*      */ 
/*  983 */     Subject[] arrayOfSubject = paramArrayOfSubject != null ? paramArrayOfSubject : new Subject[paramArrayOfObjectName.length];
/*      */ 
/*  985 */     if ((paramArrayOfObjectName.length != paramArrayOfMarshalledObject.length) || (paramArrayOfMarshalledObject.length != arrayOfSubject.length))
/*      */     {
/*  988 */       throw new IllegalArgumentException("The value lengths of 3 parameters are not same.");
/*      */     }
/*      */ 
/*  991 */     for (int i = 0; i < paramArrayOfObjectName.length; i++) {
/*  992 */       if (paramArrayOfObjectName[i] == null) {
/*  993 */         throw new IllegalArgumentException("Null Object name.");
/*      */       }
/*      */     }
/*      */ 
/*  997 */     i = 0;
/*      */ 
/*  999 */     NotificationFilter[] arrayOfNotificationFilter = new NotificationFilter[paramArrayOfObjectName.length];
/*      */ 
/* 1001 */     Integer[] arrayOfInteger = new Integer[paramArrayOfObjectName.length];
/* 1002 */     boolean bool = logger.debugOn();
/*      */     try
/*      */     {
/* 1005 */       for (; i < paramArrayOfObjectName.length; i++) {
/* 1006 */         ClassLoader localClassLoader = getClassLoaderFor(paramArrayOfObjectName[i]);
/*      */ 
/* 1008 */         if (bool) logger.debug("addNotificationListener(ObjectName,NotificationFilter)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
/*      */ 
/* 1013 */         arrayOfNotificationFilter[i] = ((NotificationFilter)unwrap(paramArrayOfMarshalledObject[i], localClassLoader, this.defaultClassLoader, NotificationFilter.class));
/*      */ 
/* 1017 */         if (bool) logger.debug("addNotificationListener(ObjectName,NotificationFilter)", "connectionId=" + this.connectionId + ", name=" + paramArrayOfObjectName[i] + ", filter=" + arrayOfNotificationFilter[i]);
/*      */ 
/* 1023 */         arrayOfInteger[i] = ((Integer)doPrivilegedOperation(1, new Object[] { paramArrayOfObjectName[i], arrayOfNotificationFilter[i] }, arrayOfSubject[i]));
/*      */       }
/*      */ 
/* 1030 */       return arrayOfInteger;
/*      */     }
/*      */     catch (Exception localException1) {
/* 1033 */       for (int j = 0; j < i; j++)
/*      */         try {
/* 1035 */           getServerNotifFwd().removeNotificationListener(paramArrayOfObjectName[j], arrayOfInteger[j]);
/*      */         }
/*      */         catch (Exception localException3)
/*      */         {
/*      */         }
/*      */       Exception localException2;
/* 1042 */       if ((localException1 instanceof PrivilegedActionException)) {
/* 1043 */         localException2 = extractException(localException1);
/*      */       }
/*      */ 
/* 1046 */       if ((localException2 instanceof ClassCastException))
/* 1047 */         throw ((ClassCastException)localException2);
/* 1048 */       if ((localException2 instanceof IOException))
/* 1049 */         throw ((IOException)localException2);
/* 1050 */       if ((localException2 instanceof InstanceNotFoundException))
/* 1051 */         throw ((InstanceNotFoundException)localException2);
/* 1052 */       if ((localException2 instanceof RuntimeException)) {
/* 1053 */         throw ((RuntimeException)localException2);
/*      */       }
/* 1055 */       throw newIOException("Got unexpected server exception: " + localException2, localException2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/* 1068 */     checkNonNull("Target MBean name", paramObjectName1);
/* 1069 */     checkNonNull("Listener MBean name", paramObjectName2);
/*      */ 
/* 1073 */     boolean bool = logger.debugOn();
/*      */ 
/* 1075 */     ClassLoader localClassLoader = getClassLoaderFor(paramObjectName1);
/*      */ 
/* 1077 */     if (bool) logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
/*      */ 
/* 1082 */     NotificationFilter localNotificationFilter = (NotificationFilter)unwrap(paramMarshalledObject1, localClassLoader, this.defaultClassLoader, NotificationFilter.class);
/*      */ 
/* 1085 */     if (bool) logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping handback with target extended ClassLoader.");
/*      */ 
/* 1090 */     Object localObject = unwrap(paramMarshalledObject2, localClassLoader, this.defaultClassLoader, Object.class);
/*      */     try
/*      */     {
/* 1094 */       Object[] arrayOfObject = { paramObjectName1, paramObjectName2, localNotificationFilter, localObject };
/*      */ 
/* 1097 */       if (bool) logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + ", name=" + paramObjectName1 + ", listenerName=" + paramObjectName2 + ", filter=" + localNotificationFilter + ", handback=" + localObject);
/*      */ 
/* 1105 */       doPrivilegedOperation(2, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1110 */       Exception localException = extractException(localPrivilegedActionException);
/* 1111 */       if ((localException instanceof InstanceNotFoundException))
/* 1112 */         throw ((InstanceNotFoundException)localException);
/* 1113 */       if ((localException instanceof IOException))
/* 1114 */         throw ((IOException)localException);
/* 1115 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotificationListeners(ObjectName paramObjectName, Integer[] paramArrayOfInteger, Subject paramSubject)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */   {
/* 1127 */     if ((paramObjectName == null) || (paramArrayOfInteger == null)) {
/* 1128 */       throw new IllegalArgumentException("Illegal null parameter");
/*      */     }
/* 1130 */     for (int i = 0; i < paramArrayOfInteger.length; i++) {
/* 1131 */       if (paramArrayOfInteger[i] == null)
/* 1132 */         throw new IllegalArgumentException("Null listener ID");
/*      */     }
/*      */     try
/*      */     {
/* 1136 */       Object[] arrayOfObject = { paramObjectName, paramArrayOfInteger };
/*      */ 
/* 1138 */       if (logger.debugOn()) logger.debug("removeNotificationListener(ObjectName,Integer[])", "connectionId=" + this.connectionId + ", name=" + paramObjectName + ", listenerIDs=" + objects(paramArrayOfInteger));
/*      */ 
/* 1144 */       doPrivilegedOperation(19, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1149 */       Exception localException = extractException(localPrivilegedActionException);
/* 1150 */       if ((localException instanceof InstanceNotFoundException))
/* 1151 */         throw ((InstanceNotFoundException)localException);
/* 1152 */       if ((localException instanceof ListenerNotFoundException))
/* 1153 */         throw ((ListenerNotFoundException)localException);
/* 1154 */       if ((localException instanceof IOException))
/* 1155 */         throw ((IOException)localException);
/* 1156 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */   {
/* 1168 */     checkNonNull("Target MBean name", paramObjectName1);
/* 1169 */     checkNonNull("Listener MBean name", paramObjectName2);
/*      */     try
/*      */     {
/* 1172 */       Object[] arrayOfObject = { paramObjectName1, paramObjectName2 };
/*      */ 
/* 1174 */       if (logger.debugOn()) logger.debug("removeNotificationListener(ObjectName,ObjectName)", "connectionId=" + this.connectionId + ", name=" + paramObjectName1 + ", listenerName=" + paramObjectName2);
/*      */ 
/* 1180 */       doPrivilegedOperation(20, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1185 */       Exception localException = extractException(localPrivilegedActionException);
/* 1186 */       if ((localException instanceof InstanceNotFoundException))
/* 1187 */         throw ((InstanceNotFoundException)localException);
/* 1188 */       if ((localException instanceof ListenerNotFoundException))
/* 1189 */         throw ((ListenerNotFoundException)localException);
/* 1190 */       if ((localException instanceof IOException))
/* 1191 */         throw ((IOException)localException);
/* 1192 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */   {
/* 1207 */     checkNonNull("Target MBean name", paramObjectName1);
/* 1208 */     checkNonNull("Listener MBean name", paramObjectName2);
/*      */ 
/* 1212 */     boolean bool = logger.debugOn();
/*      */ 
/* 1214 */     ClassLoader localClassLoader = getClassLoaderFor(paramObjectName1);
/*      */ 
/* 1216 */     if (bool) logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
/*      */ 
/* 1221 */     NotificationFilter localNotificationFilter = (NotificationFilter)unwrap(paramMarshalledObject1, localClassLoader, this.defaultClassLoader, NotificationFilter.class);
/*      */ 
/* 1224 */     if (bool) logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping handback with target extended ClassLoader.");
/*      */ 
/* 1229 */     Object localObject = unwrap(paramMarshalledObject2, localClassLoader, this.defaultClassLoader, Object.class);
/*      */     try
/*      */     {
/* 1233 */       Object[] arrayOfObject = { paramObjectName1, paramObjectName2, localNotificationFilter, localObject };
/*      */ 
/* 1236 */       if (bool) logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + ", name=" + paramObjectName1 + ", listenerName=" + paramObjectName2 + ", filter=" + localNotificationFilter + ", handback=" + localObject);
/*      */ 
/* 1244 */       doPrivilegedOperation(21, arrayOfObject, paramSubject);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1249 */       Exception localException = extractException(localPrivilegedActionException);
/* 1250 */       if ((localException instanceof InstanceNotFoundException))
/* 1251 */         throw ((InstanceNotFoundException)localException);
/* 1252 */       if ((localException instanceof ListenerNotFoundException))
/* 1253 */         throw ((ListenerNotFoundException)localException);
/* 1254 */       if ((localException instanceof IOException))
/* 1255 */         throw ((IOException)localException);
/* 1256 */       throw newIOException("Got unexpected server exception: " + localException, localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NotificationResult fetchNotifications(long paramLong1, int paramInt, long paramLong2)
/*      */     throws IOException
/*      */   {
/* 1265 */     if (logger.debugOn()) logger.debug("fetchNotifications", "connectionId=" + this.connectionId + ", timeout=" + paramLong2);
/*      */ 
/* 1269 */     if ((paramInt < 0) || (paramLong2 < 0L)) {
/* 1270 */       throw new IllegalArgumentException("Illegal negative argument");
/*      */     }
/* 1272 */     boolean bool = this.serverCommunicatorAdmin.reqIncoming();
/*      */     try
/*      */     {
/* 1275 */       if (bool)
/*      */       {
/* 1279 */         return new NotificationResult(0L, 0L, new TargetedNotification[0]);
/*      */       }
/*      */ 
/* 1283 */       final long l1 = paramLong1;
/* 1284 */       final int i = paramInt;
/* 1285 */       long l2 = paramLong2;
/* 1286 */       PrivilegedAction local3 = new PrivilegedAction()
/*      */       {
/*      */         public NotificationResult run() {
/* 1289 */           return RMIConnectionImpl.this.getServerNotifFwd().fetchNotifs(l1, i, this.val$mn);
/*      */         }
/*      */       };
/*      */       NotificationResult localNotificationResult2;
/* 1292 */       if (this.acc == null) {
/* 1293 */         return (NotificationResult)local3.run();
/*      */       }
/* 1295 */       return (NotificationResult)AccessController.doPrivileged(local3, this.acc);
/*      */     } finally {
/* 1297 */       this.serverCommunicatorAdmin.rspOutgoing();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1312 */     return super.toString() + ": connectionId=" + this.connectionId;
/*      */   }
/*      */ 
/*      */   private ClassLoader getClassLoader(final ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/*      */     try
/*      */     {
/* 1363 */       return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public ClassLoader run() throws InstanceNotFoundException
/*      */         {
/* 1367 */           return RMIConnectionImpl.this.mbeanServer.getClassLoader(paramObjectName);
/*      */         }
/*      */       }
/*      */       , withPermissions(new Permission[] { new MBeanPermission("*", "getClassLoader") }));
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1373 */       throw ((InstanceNotFoundException)extractException(localPrivilegedActionException));
/*      */     }
/*      */   }
/*      */ 
/*      */   private ClassLoader getClassLoaderFor(final ObjectName paramObjectName) throws InstanceNotFoundException
/*      */   {
/*      */     try {
/* 1380 */       return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Object run() throws InstanceNotFoundException
/*      */         {
/* 1384 */           return RMIConnectionImpl.this.mbeanServer.getClassLoaderFor(paramObjectName);
/*      */         }
/*      */       }
/*      */       , withPermissions(new Permission[] { new MBeanPermission("*", "getClassLoaderFor") }));
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 1390 */       throw ((InstanceNotFoundException)extractException(localPrivilegedActionException));
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object doPrivilegedOperation(int paramInt, Object[] paramArrayOfObject, Subject paramSubject)
/*      */     throws PrivilegedActionException, IOException
/*      */   {
/* 1399 */     this.serverCommunicatorAdmin.reqIncoming();
/*      */     try
/*      */     {
/*      */       AccessControlContext localAccessControlContext;
/* 1403 */       if (paramSubject == null) {
/* 1404 */         localAccessControlContext = this.acc;
/*      */       } else {
/* 1406 */         if (this.subject == null)
/*      */         {
/* 1410 */           throw new SecurityException("Subject delegation cannot be enabled unless an authenticated subject is put in place");
/*      */         }
/* 1412 */         localAccessControlContext = this.subjectDelegator.delegatedContext(this.acc, paramSubject, this.removeCallerContext);
/*      */       }
/*      */ 
/* 1416 */       PrivilegedOperation localPrivilegedOperation = new PrivilegedOperation(paramInt, paramArrayOfObject);
/*      */ 
/* 1418 */       if (localAccessControlContext == null) {
/*      */         try {
/* 1420 */           return localPrivilegedOperation.run();
/*      */         } catch (Exception localException) {
/* 1422 */           if ((localException instanceof RuntimeException))
/* 1423 */             throw ((RuntimeException)localException);
/* 1424 */           throw new PrivilegedActionException(localException);
/*      */         }
/*      */       }
/* 1427 */       return AccessController.doPrivileged(localPrivilegedOperation, localAccessControlContext);
/*      */     }
/*      */     catch (Error localError) {
/* 1430 */       throw new JMXServerErrorException(localError.toString(), localError);
/*      */     } finally {
/* 1432 */       this.serverCommunicatorAdmin.rspOutgoing();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object doOperation(int paramInt, Object[] paramArrayOfObject)
/*      */     throws Exception
/*      */   {
/* 1439 */     switch (paramInt)
/*      */     {
/*      */     case 3:
/* 1442 */       return this.mbeanServer.createMBean((String)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1]);
/*      */     case 5:
/* 1446 */       return this.mbeanServer.createMBean((String)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1], (ObjectName)paramArrayOfObject[2]);
/*      */     case 4:
/* 1451 */       return this.mbeanServer.createMBean((String)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1], (Object[])paramArrayOfObject[2], (String[])paramArrayOfObject[3]);
/*      */     case 6:
/* 1457 */       return this.mbeanServer.createMBean((String)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1], (ObjectName)paramArrayOfObject[2], (Object[])paramArrayOfObject[3], (String[])paramArrayOfObject[4]);
/*      */     case 7:
/* 1464 */       return this.mbeanServer.getAttribute((ObjectName)paramArrayOfObject[0], (String)paramArrayOfObject[1]);
/*      */     case 8:
/* 1468 */       return this.mbeanServer.getAttributes((ObjectName)paramArrayOfObject[0], (String[])paramArrayOfObject[1]);
/*      */     case 9:
/* 1472 */       return this.mbeanServer.getDefaultDomain();
/*      */     case 10:
/* 1475 */       return this.mbeanServer.getDomains();
/*      */     case 11:
/* 1478 */       return this.mbeanServer.getMBeanCount();
/*      */     case 12:
/* 1481 */       return this.mbeanServer.getMBeanInfo((ObjectName)paramArrayOfObject[0]);
/*      */     case 13:
/* 1484 */       return this.mbeanServer.getObjectInstance((ObjectName)paramArrayOfObject[0]);
/*      */     case 14:
/* 1487 */       return this.mbeanServer.invoke((ObjectName)paramArrayOfObject[0], (String)paramArrayOfObject[1], (Object[])paramArrayOfObject[2], (String[])paramArrayOfObject[3]);
/*      */     case 15:
/* 1493 */       return this.mbeanServer.isInstanceOf((ObjectName)paramArrayOfObject[0], (String)paramArrayOfObject[1]) ? Boolean.TRUE : Boolean.FALSE;
/*      */     case 16:
/* 1498 */       return this.mbeanServer.isRegistered((ObjectName)paramArrayOfObject[0]) ? Boolean.TRUE : Boolean.FALSE;
/*      */     case 17:
/* 1502 */       return this.mbeanServer.queryMBeans((ObjectName)paramArrayOfObject[0], (QueryExp)paramArrayOfObject[1]);
/*      */     case 18:
/* 1506 */       return this.mbeanServer.queryNames((ObjectName)paramArrayOfObject[0], (QueryExp)paramArrayOfObject[1]);
/*      */     case 22:
/* 1510 */       this.mbeanServer.setAttribute((ObjectName)paramArrayOfObject[0], (Attribute)paramArrayOfObject[1]);
/*      */ 
/* 1512 */       return null;
/*      */     case 23:
/* 1515 */       return this.mbeanServer.setAttributes((ObjectName)paramArrayOfObject[0], (AttributeList)paramArrayOfObject[1]);
/*      */     case 24:
/* 1519 */       this.mbeanServer.unregisterMBean((ObjectName)paramArrayOfObject[0]);
/* 1520 */       return null;
/*      */     case 1:
/* 1523 */       return getServerNotifFwd().addNotificationListener((ObjectName)paramArrayOfObject[0], (NotificationFilter)paramArrayOfObject[1]);
/*      */     case 2:
/* 1528 */       this.mbeanServer.addNotificationListener((ObjectName)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1], (NotificationFilter)paramArrayOfObject[2], paramArrayOfObject[3]);
/*      */ 
/* 1532 */       return null;
/*      */     case 19:
/* 1535 */       getServerNotifFwd().removeNotificationListener((ObjectName)paramArrayOfObject[0], (Integer[])paramArrayOfObject[1]);
/*      */ 
/* 1538 */       return null;
/*      */     case 20:
/* 1541 */       this.mbeanServer.removeNotificationListener((ObjectName)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1]);
/*      */ 
/* 1543 */       return null;
/*      */     case 21:
/* 1546 */       this.mbeanServer.removeNotificationListener((ObjectName)paramArrayOfObject[0], (ObjectName)paramArrayOfObject[1], (NotificationFilter)paramArrayOfObject[2], paramArrayOfObject[3]);
/*      */ 
/* 1551 */       return null;
/*      */     }
/*      */ 
/* 1554 */     throw new IllegalArgumentException("Invalid operation");
/*      */   }
/*      */ 
/*      */   private static <T> T unwrap(MarshalledObject<?> paramMarshalledObject, ClassLoader paramClassLoader, Class<T> paramClass)
/*      */     throws IOException
/*      */   {
/* 1577 */     if (paramMarshalledObject == null)
/* 1578 */       return null;
/*      */     try
/*      */     {
/* 1581 */       ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new SetCcl(paramClassLoader));
/*      */       try {
/* 1583 */         return paramClass.cast(paramMarshalledObject.get());
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 1585 */         throw new UnmarshalException(localClassNotFoundException.toString(), localClassNotFoundException);
/*      */       } finally {
/* 1587 */         AccessController.doPrivileged(new SetCcl(localClassLoader));
/*      */       }
/*      */     } catch (PrivilegedActionException localPrivilegedActionException) {
/* 1590 */       Exception localException = extractException(localPrivilegedActionException);
/* 1591 */       if ((localException instanceof IOException)) {
/* 1592 */         throw ((IOException)localException);
/*      */       }
/* 1594 */       if ((localException instanceof ClassNotFoundException)) {
/* 1595 */         throw new UnmarshalException(localException.toString(), localException);
/*      */       }
/* 1597 */       logger.warning("unwrap", "Failed to unmarshall object: " + localException);
/* 1598 */       logger.debug("unwrap", localException);
/*      */     }
/* 1600 */     return null;
/*      */   }
/*      */ 
/*      */   private static <T> T unwrap(MarshalledObject<?> paramMarshalledObject, ClassLoader paramClassLoader1, final ClassLoader paramClassLoader2, Class<T> paramClass)
/*      */     throws IOException
/*      */   {
/* 1608 */     if (paramMarshalledObject == null)
/* 1609 */       return null;
/*      */     try
/*      */     {
/* 1612 */       ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public ClassLoader run() throws Exception {
/* 1615 */           return new RMIConnectionImpl.CombinedClassLoader(Thread.currentThread().getContextClassLoader(), new OrderClassLoaders(this.val$cl1, paramClassLoader2), null);
/*      */         }
/*      */       });
/* 1620 */       return unwrap(paramMarshalledObject, localClassLoader, paramClass);
/*      */     } catch (PrivilegedActionException localPrivilegedActionException) {
/* 1622 */       Exception localException = extractException(localPrivilegedActionException);
/* 1623 */       if ((localException instanceof IOException)) {
/* 1624 */         throw ((IOException)localException);
/*      */       }
/* 1626 */       if ((localException instanceof ClassNotFoundException)) {
/* 1627 */         throw new UnmarshalException(localException.toString(), localException);
/*      */       }
/* 1629 */       logger.warning("unwrap", "Failed to unmarshall object: " + localException);
/* 1630 */       logger.debug("unwrap", localException);
/*      */     }
/* 1632 */     return null;
/*      */   }
/*      */ 
/*      */   private static IOException newIOException(String paramString, Throwable paramThrowable)
/*      */   {
/* 1641 */     IOException localIOException = new IOException(paramString);
/* 1642 */     return (IOException)EnvHelp.initCause(localIOException, paramThrowable);
/*      */   }
/*      */ 
/*      */   private static Exception extractException(Exception paramException)
/*      */   {
/* 1650 */     while ((paramException instanceof PrivilegedActionException)) {
/* 1651 */       paramException = ((PrivilegedActionException)paramException).getException();
/*      */     }
/* 1653 */     return paramException;
/*      */   }
/*      */ 
/*      */   private static Object[] nullIsEmpty(Object[] paramArrayOfObject)
/*      */   {
/* 1668 */     return paramArrayOfObject == null ? NO_OBJECTS : paramArrayOfObject;
/*      */   }
/*      */ 
/*      */   private static String[] nullIsEmpty(String[] paramArrayOfString) {
/* 1672 */     return paramArrayOfString == null ? NO_STRINGS : paramArrayOfString;
/*      */   }
/*      */ 
/*      */   private static void checkNonNull(String paramString, Object paramObject)
/*      */   {
/* 1683 */     if (paramObject == null) {
/* 1684 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(paramString + " must not be null");
/*      */ 
/* 1686 */       throw new RuntimeOperationsException(localIllegalArgumentException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String objects(Object[] paramArrayOfObject)
/*      */   {
/* 1780 */     if (paramArrayOfObject == null) {
/* 1781 */       return "null";
/*      */     }
/* 1783 */     return Arrays.asList(paramArrayOfObject).toString();
/*      */   }
/*      */ 
/*      */   private static String strings(String[] paramArrayOfString) {
/* 1787 */     return objects(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   private static final class CombinedClassLoader extends ClassLoader
/*      */   {
/*      */     final ClassLoaderWrapper defaultCL;
/*      */ 
/*      */     private CombinedClassLoader(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*      */     {
/* 1810 */       super();
/* 1811 */       this.defaultCL = new ClassLoaderWrapper(paramClassLoader2);
/*      */     }
/*      */ 
/*      */     protected Class<?> loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 1817 */       ReflectUtil.checkPackageAccess(paramString);
/*      */       Object localObject;
/*      */       try {
/* 1819 */         super.loadClass(paramString, paramBoolean);
/*      */       } catch (Exception localException) {
/* 1821 */         localObject = localException; } for (; localObject != null; localObject = ((Throwable)localObject).getCause()) {
/* 1822 */         if ((localObject instanceof SecurityException)) {
/* 1823 */           throw (localObject == localException ? (SecurityException)localObject : new SecurityException(((Throwable)localObject).getMessage(), localException));
/*      */         }
/*      */       }
/*      */ 
/* 1827 */       Class localClass = this.defaultCL.loadClass(paramString, paramBoolean);
/* 1828 */       return localClass;
/*      */     }
/*      */ 
/*      */     private static final class ClassLoaderWrapper extends ClassLoader
/*      */     {
/*      */       ClassLoaderWrapper(ClassLoader paramClassLoader)
/*      */       {
/* 1797 */         super();
/*      */       }
/*      */ 
/*      */       protected Class<?> loadClass(String paramString, boolean paramBoolean)
/*      */         throws ClassNotFoundException
/*      */       {
/* 1803 */         return super.loadClass(paramString, paramBoolean);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrivilegedOperation
/*      */     implements PrivilegedExceptionAction<Object>
/*      */   {
/*      */     private int operation;
/*      */     private Object[] params;
/*      */ 
/*      */     public PrivilegedOperation(int paramArrayOfObject, Object[] arg3)
/*      */     {
/* 1323 */       this.operation = paramArrayOfObject;
/*      */       Object localObject;
/* 1324 */       this.params = localObject;
/*      */     }
/*      */ 
/*      */     public Object run() throws Exception {
/* 1328 */       return RMIConnectionImpl.this.doOperation(this.operation, this.params);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RMIServerCommunicatorAdmin extends ServerCommunicatorAdmin
/*      */   {
/*      */     public RMIServerCommunicatorAdmin(long arg2)
/*      */     {
/* 1340 */       super();
/*      */     }
/*      */ 
/*      */     protected void doStop() {
/*      */       try {
/* 1345 */         RMIConnectionImpl.this.close();
/*      */       } catch (IOException localIOException) {
/* 1347 */         RMIConnectionImpl.logger.warning("RMIServerCommunicatorAdmin-doStop", "Failed to close: " + localIOException);
/*      */ 
/* 1349 */         RMIConnectionImpl.logger.debug("RMIServerCommunicatorAdmin-doStop", localIOException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SetCcl
/*      */     implements PrivilegedExceptionAction<ClassLoader>
/*      */   {
/*      */     private final ClassLoader classLoader;
/*      */ 
/*      */     SetCcl(ClassLoader paramClassLoader)
/*      */     {
/* 1562 */       this.classLoader = paramClassLoader;
/*      */     }
/*      */ 
/*      */     public ClassLoader run() {
/* 1566 */       Thread localThread = Thread.currentThread();
/* 1567 */       ClassLoader localClassLoader = localThread.getContextClassLoader();
/* 1568 */       localThread.setContextClassLoader(this.classLoader);
/* 1569 */       return localClassLoader;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIConnectionImpl
 * JD-Core Version:    0.6.2
 */