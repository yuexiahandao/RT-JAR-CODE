/*      */ package javax.management.remote.rmi;
/*      */ 
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;
/*      */ import com.sun.jmx.remote.internal.ClientListenerInfo;
/*      */ import com.sun.jmx.remote.internal.ClientNotifForwarder;
/*      */ import com.sun.jmx.remote.internal.IIOPHelper;
/*      */ import com.sun.jmx.remote.internal.ProxyRef;
/*      */ import com.sun.jmx.remote.util.ClassLogger;
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamClass;
/*      */ import java.io.Serializable;
/*      */ import java.io.WriteAbortedException;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.MalformedURLException;
/*      */ import java.rmi.MarshalException;
/*      */ import java.rmi.MarshalledObject;
/*      */ import java.rmi.NoSuchObjectException;
/*      */ import java.rmi.Remote;
/*      */ import java.rmi.RemoteException;
/*      */ import java.rmi.ServerException;
/*      */ import java.rmi.UnmarshalException;
/*      */ import java.rmi.server.RMIClientSocketFactory;
/*      */ import java.rmi.server.RemoteObject;
/*      */ import java.rmi.server.RemoteObjectInvocationHandler;
/*      */ import java.rmi.server.RemoteRef;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
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
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServerConnection;
/*      */ import javax.management.MBeanServerDelegate;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationFilterSupport;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectInstance;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.QueryExp;
/*      */ import javax.management.ReflectionException;
/*      */ import javax.management.remote.JMXAddressable;
/*      */ import javax.management.remote.JMXConnectionNotification;
/*      */ import javax.management.remote.JMXConnector;
/*      */ import javax.management.remote.JMXServiceURL;
/*      */ import javax.management.remote.NotificationResult;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.rmi.ssl.SslRMIClientSocketFactory;
/*      */ import javax.security.auth.Subject;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.rmi.server.UnicastRef2;
/*      */ import sun.rmi.transport.LiveRef;
/*      */ 
/*      */ public class RMIConnector
/*      */   implements JMXConnector, Serializable, JMXAddressable
/*      */ {
/*  123 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnector");
/*      */   private static final long serialVersionUID = 817323035842634473L;
/* 2091 */   private static final String rmiServerImplStubClassName = RMIServer.class.getName() + "Impl_Stub";
/*      */   private static final Class<?> rmiServerImplStubClass;
/* 2094 */   private static final String rmiConnectionImplStubClassName = RMIConnection.class.getName() + "Impl_Stub";
/*      */   private static final Class<?> rmiConnectionImplStubClass;
/*      */   private static final String pRefClassName = "com.sun.jmx.remote.internal.PRef";
/*      */   private static final Constructor<?> proxyRefConstructor;
/*      */   private static final String iiopConnectionStubClassName = "org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub";
/*      */   private static final String proxyStubClassName = "com.sun.jmx.remote.protocol.iiop.ProxyStub";
/*      */   private static final String ProxyInputStreamClassName = "com.sun.jmx.remote.protocol.iiop.ProxyInputStream";
/*      */   private static final String pInputStreamClassName = "com.sun.jmx.remote.protocol.iiop.PInputStream";
/*      */   private static final Class<?> proxyStubClass;
/* 2500 */   private static final byte[] base64ToInt = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*      */   private final RMIServer rmiServer;
/*      */   private final JMXServiceURL jmxServiceURL;
/*      */   private transient Map<String, Object> env;
/*      */   private transient ClassLoader defaultClassLoader;
/*      */   private transient RMIConnection connection;
/*      */   private transient String connectionId;
/* 2572 */   private transient long clientNotifSeqNo = 0L;
/*      */   private transient WeakHashMap<Subject, WeakReference<MBeanServerConnection>> rmbscMap;
/* 2575 */   private transient WeakReference<MBeanServerConnection> nullSubjectConnRef = null;
/*      */   private transient RMINotifClient rmiNotifClient;
/* 2580 */   private transient long clientNotifCounter = 0L;
/*      */   private transient boolean connected;
/*      */   private transient boolean terminated;
/*      */   private transient Exception closeException;
/*      */   private transient NotificationBroadcasterSupport connectionBroadcaster;
/*      */   private transient ClientCommunicatorAdmin communicatorAdmin;
/* 2597 */   private static volatile WeakReference<Object> orb = null;
/*      */ 
/*      */   private RMIConnector(RMIServer paramRMIServer, JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*      */   {
/*  130 */     if ((paramRMIServer == null) && (paramJMXServiceURL == null)) throw new IllegalArgumentException("rmiServer and jmxServiceURL both null");
/*      */ 
/*  132 */     initTransients();
/*      */ 
/*  134 */     this.rmiServer = paramRMIServer;
/*  135 */     this.jmxServiceURL = paramJMXServiceURL;
/*  136 */     if (paramMap == null) {
/*  137 */       this.env = Collections.emptyMap();
/*      */     } else {
/*  139 */       EnvHelp.checkAttributes(paramMap);
/*  140 */       this.env = Collections.unmodifiableMap(paramMap);
/*      */     }
/*      */   }
/*      */ 
/*      */   public RMIConnector(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*      */   {
/*  186 */     this(null, paramJMXServiceURL, paramMap);
/*      */   }
/*      */ 
/*      */   public RMIConnector(RMIServer paramRMIServer, Map<String, ?> paramMap)
/*      */   {
/*  201 */     this(paramRMIServer, null, paramMap);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  215 */     StringBuilder localStringBuilder = new StringBuilder(getClass().getName());
/*  216 */     localStringBuilder.append(":");
/*  217 */     if (this.rmiServer != null) {
/*  218 */       localStringBuilder.append(" rmiServer=").append(this.rmiServer.toString());
/*      */     }
/*  220 */     if (this.jmxServiceURL != null) {
/*  221 */       if (this.rmiServer != null) localStringBuilder.append(",");
/*  222 */       localStringBuilder.append(" jmxServiceURL=").append(this.jmxServiceURL.toString());
/*      */     }
/*  224 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public JMXServiceURL getAddress()
/*      */   {
/*  236 */     return this.jmxServiceURL;
/*      */   }
/*      */ 
/*      */   public void connect()
/*      */     throws IOException
/*      */   {
/*  249 */     connect(null);
/*      */   }
/*      */ 
/*      */   public synchronized void connect(Map<String, ?> paramMap)
/*      */     throws IOException
/*      */   {
/*  259 */     boolean bool1 = logger.traceOn();
/*  260 */     String str1 = bool1 ? "[" + toString() + "]" : null;
/*      */ 
/*  262 */     if (this.terminated) {
/*  263 */       logger.trace("connect", str1 + " already closed.");
/*  264 */       throw new IOException("Connector closed");
/*      */     }
/*  266 */     if (this.connected) {
/*  267 */       logger.trace("connect", str1 + " already connected.");
/*  268 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  272 */       if (bool1) logger.trace("connect", str1 + " connecting...");
/*      */ 
/*  274 */       HashMap localHashMap = new HashMap(this.env == null ? Collections.emptyMap() : this.env);
/*      */ 
/*  279 */       if (paramMap != null) {
/*  280 */         EnvHelp.checkAttributes(paramMap);
/*  281 */         localHashMap.putAll(paramMap);
/*      */       }
/*      */ 
/*  285 */       if (bool1) logger.trace("connect", str1 + " finding stub...");
/*  286 */       localObject1 = this.rmiServer != null ? this.rmiServer : findRMIServer(this.jmxServiceURL, localHashMap);
/*      */ 
/*  292 */       String str2 = (String)localHashMap.get("jmx.remote.x.check.stub");
/*  293 */       boolean bool2 = EnvHelp.computeBooleanFromString(str2);
/*      */ 
/*  295 */       if (bool2) checkStub((Remote)localObject1, rmiServerImplStubClass);
/*      */ 
/*  298 */       if (bool1) logger.trace("connect", str1 + " connecting stub...");
/*  299 */       localObject1 = connectStub((RMIServer)localObject1, localHashMap);
/*  300 */       str1 = bool1 ? "[" + toString() + "]" : null;
/*      */ 
/*  303 */       if (bool1)
/*  304 */         logger.trace("connect", str1 + " getting connection...");
/*  305 */       Object localObject2 = localHashMap.get("jmx.remote.credentials");
/*      */       try
/*      */       {
/*  308 */         this.connection = getConnection((RMIServer)localObject1, localObject2, bool2);
/*      */       } catch (RemoteException localRemoteException) {
/*  310 */         if (this.jmxServiceURL != null) {
/*  311 */           String str3 = this.jmxServiceURL.getProtocol();
/*  312 */           localObject3 = this.jmxServiceURL.getURLPath();
/*      */ 
/*  314 */           if (("rmi".equals(str3)) && (((String)localObject3).startsWith("/jndi/iiop:")))
/*      */           {
/*  316 */             MalformedURLException localMalformedURLException = new MalformedURLException("Protocol is rmi but JNDI scheme is iiop: " + this.jmxServiceURL);
/*      */ 
/*  318 */             localMalformedURLException.initCause(localRemoteException);
/*  319 */             throw localMalformedURLException;
/*      */           }
/*      */         }
/*  322 */         throw localRemoteException;
/*      */       }
/*      */ 
/*  328 */       if (bool1)
/*  329 */         logger.trace("connect", str1 + " getting class loader...");
/*  330 */       this.defaultClassLoader = EnvHelp.resolveClientClassLoader(localHashMap);
/*      */ 
/*  332 */       localHashMap.put("jmx.remote.default.class.loader", this.defaultClassLoader);
/*      */ 
/*  335 */       this.rmiNotifClient = new RMINotifClient(this.defaultClassLoader, localHashMap);
/*      */ 
/*  337 */       this.env = localHashMap;
/*  338 */       long l = EnvHelp.getConnectionCheckPeriod(localHashMap);
/*  339 */       this.communicatorAdmin = new RMIClientCommunicatorAdmin(l);
/*      */ 
/*  341 */       this.connected = true;
/*      */ 
/*  346 */       this.connectionId = getConnectionId();
/*      */ 
/*  348 */       Object localObject3 = new JMXConnectionNotification("jmx.remote.connection.opened", this, this.connectionId, this.clientNotifSeqNo++, "Successful connection", null);
/*      */ 
/*  355 */       sendNotification((Notification)localObject3);
/*      */ 
/*  357 */       if (bool1) logger.trace("connect", str1 + " done..."); 
/*      */     }
/*  359 */     catch (IOException localIOException) { if (bool1)
/*  360 */         logger.trace("connect", str1 + " failed to connect: " + localIOException);
/*  361 */       throw localIOException;
/*      */     } catch (RuntimeException localRuntimeException) {
/*  363 */       if (bool1)
/*  364 */         logger.trace("connect", str1 + " failed to connect: " + localRuntimeException);
/*  365 */       throw localRuntimeException;
/*      */     } catch (NamingException localNamingException) {
/*  367 */       Object localObject1 = "Failed to retrieve RMIServer stub: " + localNamingException;
/*  368 */       if (bool1) logger.trace("connect", str1 + " " + (String)localObject1);
/*  369 */       throw ((IOException)EnvHelp.initCause(new IOException((String)localObject1), localNamingException));
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized String getConnectionId() throws IOException {
/*  374 */     if ((this.terminated) || (!this.connected)) {
/*  375 */       if (logger.traceOn()) {
/*  376 */         logger.trace("getConnectionId", "[" + toString() + "] not connected.");
/*      */       }
/*      */ 
/*  379 */       throw new IOException("Not connected");
/*      */     }
/*      */ 
/*  384 */     return this.connection.getConnectionId();
/*      */   }
/*      */ 
/*      */   public synchronized MBeanServerConnection getMBeanServerConnection() throws IOException
/*      */   {
/*  389 */     return getMBeanServerConnection(null);
/*      */   }
/*      */ 
/*      */   public synchronized MBeanServerConnection getMBeanServerConnection(Subject paramSubject)
/*      */     throws IOException
/*      */   {
/*  396 */     if (this.terminated) {
/*  397 */       if (logger.traceOn()) {
/*  398 */         logger.trace("getMBeanServerConnection", "[" + toString() + "] already closed.");
/*      */       }
/*  400 */       throw new IOException("Connection closed");
/*  401 */     }if (!this.connected) {
/*  402 */       if (logger.traceOn()) {
/*  403 */         logger.trace("getMBeanServerConnection", "[" + toString() + "] is not connected.");
/*      */       }
/*  405 */       throw new IOException("Not connected");
/*      */     }
/*      */ 
/*  408 */     return getConnectionWithSubject(paramSubject);
/*      */   }
/*      */ 
/*      */   public void addConnectionNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */   {
/*  415 */     if (paramNotificationListener == null)
/*  416 */       throw new NullPointerException("listener");
/*  417 */     this.connectionBroadcaster.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   public void removeConnectionNotificationListener(NotificationListener paramNotificationListener)
/*      */     throws ListenerNotFoundException
/*      */   {
/*  424 */     if (paramNotificationListener == null)
/*  425 */       throw new NullPointerException("listener");
/*  426 */     this.connectionBroadcaster.removeNotificationListener(paramNotificationListener);
/*      */   }
/*      */ 
/*      */   public void removeConnectionNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws ListenerNotFoundException
/*      */   {
/*  434 */     if (paramNotificationListener == null)
/*  435 */       throw new NullPointerException("listener");
/*  436 */     this.connectionBroadcaster.removeNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   private void sendNotification(Notification paramNotification)
/*      */   {
/*  441 */     this.connectionBroadcaster.sendNotification(paramNotification);
/*      */   }
/*      */ 
/*      */   public synchronized void close() throws IOException {
/*  445 */     close(false);
/*      */   }
/*      */ 
/*      */   private synchronized void close(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  451 */     boolean bool1 = logger.traceOn();
/*  452 */     boolean bool2 = logger.debugOn();
/*  453 */     String str1 = bool1 ? "[" + toString() + "]" : null;
/*      */ 
/*  455 */     if (!paramBoolean)
/*      */     {
/*  458 */       if (this.terminated) {
/*  459 */         if (this.closeException == null) {
/*  460 */           if (bool1) logger.trace("close", str1 + " already closed.");
/*      */         }
/*      */       }
/*      */       else {
/*  464 */         this.terminated = true;
/*      */       }
/*      */     }
/*      */ 
/*  468 */     if ((this.closeException != null) && (bool1))
/*      */     {
/*  471 */       if (bool1) {
/*  472 */         logger.trace("close", str1 + " had failed: " + this.closeException);
/*  473 */         logger.trace("close", str1 + " attempting to close again.");
/*      */       }
/*      */     }
/*      */ 
/*  477 */     String str2 = null;
/*  478 */     if (this.connected) {
/*  479 */       str2 = this.connectionId;
/*      */     }
/*      */ 
/*  482 */     this.closeException = null;
/*      */ 
/*  484 */     if (bool1) logger.trace("close", str1 + " closing.");
/*      */ 
/*  486 */     if (this.communicatorAdmin != null) {
/*  487 */       this.communicatorAdmin.terminate();
/*      */     }
/*      */ 
/*  490 */     if (this.rmiNotifClient != null) {
/*      */       try {
/*  492 */         this.rmiNotifClient.terminate();
/*  493 */         if (bool1) logger.trace("close", str1 + " RMI Notification client terminated."); 
/*      */       }
/*      */       catch (RuntimeException localRuntimeException)
/*      */       {
/*  496 */         this.closeException = localRuntimeException;
/*  497 */         if (bool1) logger.trace("close", str1 + " Failed to terminate RMI Notification client: " + localRuntimeException);
/*      */ 
/*  499 */         if (bool2) logger.debug("close", localRuntimeException);
/*      */       }
/*      */     }
/*      */ 
/*  503 */     if (this.connection != null) {
/*      */       try {
/*  505 */         this.connection.close();
/*  506 */         if (bool1) logger.trace("close", str1 + " closed."); 
/*      */       }
/*      */       catch (NoSuchObjectException localNoSuchObjectException) {
/*      */       }
/*  510 */       catch (IOException localIOException) { this.closeException = localIOException;
/*  511 */         if (bool1) logger.trace("close", str1 + " Failed to close RMIServer: " + localIOException);
/*      */ 
/*  513 */         if (bool2) logger.debug("close", localIOException);
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  519 */     this.rmbscMap.clear();
/*      */     Object localObject;
/*  525 */     if (str2 != null) {
/*  526 */       localObject = new JMXConnectionNotification("jmx.remote.connection.closed", this, str2, this.clientNotifSeqNo++, "Client has been closed", null);
/*      */ 
/*  533 */       sendNotification((Notification)localObject);
/*      */     }
/*      */ 
/*  538 */     if (this.closeException != null) {
/*  539 */       if (bool1) logger.trace("close", str1 + " failed to close: " + this.closeException);
/*      */ 
/*  541 */       if ((this.closeException instanceof IOException))
/*  542 */         throw ((IOException)this.closeException);
/*  543 */       if ((this.closeException instanceof RuntimeException))
/*  544 */         throw ((RuntimeException)this.closeException);
/*  545 */       localObject = new IOException("Failed to close: " + this.closeException);
/*      */ 
/*  547 */       throw ((IOException)EnvHelp.initCause((Throwable)localObject, this.closeException));
/*      */     }
/*      */   }
/*      */ 
/*      */   private Integer addListenerWithSubject(ObjectName paramObjectName, MarshalledObject<NotificationFilter> paramMarshalledObject, Subject paramSubject, boolean paramBoolean)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/*  558 */     boolean bool = logger.debugOn();
/*  559 */     if (bool) {
/*  560 */       logger.debug("addListenerWithSubject", "(ObjectName,MarshalledObject,Subject)");
/*      */     }
/*      */ 
/*  563 */     ObjectName[] arrayOfObjectName = { paramObjectName };
/*  564 */     MarshalledObject[] arrayOfMarshalledObject = (MarshalledObject[])Util.cast(new MarshalledObject[] { paramMarshalledObject });
/*      */ 
/*  566 */     Subject[] arrayOfSubject = { paramSubject };
/*      */ 
/*  570 */     Integer[] arrayOfInteger = addListenersWithSubjects(arrayOfObjectName, arrayOfMarshalledObject, arrayOfSubject, paramBoolean);
/*      */ 
/*  574 */     if (bool) logger.debug("addListenerWithSubject", "listenerID=" + arrayOfInteger[0]);
/*      */ 
/*  576 */     return arrayOfInteger[0];
/*      */   }
/*      */ 
/*      */   private Integer[] addListenersWithSubjects(ObjectName[] paramArrayOfObjectName, MarshalledObject<NotificationFilter>[] paramArrayOfMarshalledObject, Subject[] paramArrayOfSubject, boolean paramBoolean)
/*      */     throws InstanceNotFoundException, IOException
/*      */   {
/*  586 */     boolean bool = logger.debugOn();
/*  587 */     if (bool) {
/*  588 */       logger.debug("addListenersWithSubjects", "(ObjectName[],MarshalledObject[],Subject[])");
/*      */     }
/*      */ 
/*  591 */     ClassLoader localClassLoader = pushDefaultClassLoader();
/*  592 */     Integer[] arrayOfInteger = null;
/*      */     try
/*      */     {
/*  595 */       arrayOfInteger = this.connection.addNotificationListeners(paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject);
/*      */     }
/*      */     catch (NoSuchObjectException localNoSuchObjectException)
/*      */     {
/*  600 */       if (paramBoolean) {
/*  601 */         this.communicatorAdmin.gotIOException(localNoSuchObjectException);
/*      */ 
/*  603 */         arrayOfInteger = this.connection.addNotificationListeners(paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject);
/*      */       }
/*      */       else
/*      */       {
/*  607 */         throw localNoSuchObjectException;
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  611 */       this.communicatorAdmin.gotIOException(localIOException);
/*      */     } finally {
/*  613 */       popDefaultClassLoader(localClassLoader);
/*      */     }
/*      */ 
/*  616 */     if (bool) logger.debug("addListenersWithSubjects", "registered " + (arrayOfInteger == null ? 0 : arrayOfInteger.length) + " listener(s)");
/*      */ 
/*  619 */     return arrayOfInteger;
/*      */   }
/*      */ 
/*      */   static RMIServer connectStub(RMIServer paramRMIServer, Map<String, ?> paramMap)
/*      */     throws IOException
/*      */   {
/* 1714 */     if (IIOPHelper.isStub(paramRMIServer)) {
/*      */       try {
/* 1716 */         IIOPHelper.getOrb(paramRMIServer);
/*      */       }
/*      */       catch (UnsupportedOperationException localUnsupportedOperationException) {
/* 1719 */         IIOPHelper.connect(paramRMIServer, resolveOrb(paramMap));
/*      */       }
/*      */     }
/* 1722 */     return paramRMIServer;
/*      */   }
/*      */ 
/*      */   static Object resolveOrb(Map<String, ?> paramMap)
/*      */     throws IOException
/*      */   {
/* 1748 */     if (paramMap != null) {
/* 1749 */       localObject1 = paramMap.get("java.naming.corba.orb");
/* 1750 */       if ((localObject1 != null) && (!IIOPHelper.isOrb(localObject1))) {
/* 1751 */         throw new IllegalArgumentException("java.naming.corba.orb must be an instance of org.omg.CORBA.ORB.");
/*      */       }
/* 1753 */       if (localObject1 != null) return localObject1;
/*      */     }
/* 1755 */     Object localObject1 = orb == null ? null : orb.get();
/*      */ 
/* 1757 */     if (localObject1 != null) return localObject1;
/*      */ 
/* 1759 */     Object localObject2 = IIOPHelper.createOrb((String[])null, (Properties)null);
/*      */ 
/* 1761 */     orb = new WeakReference(localObject2);
/* 1762 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1778 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1780 */     if ((this.rmiServer == null) && (this.jmxServiceURL == null)) throw new InvalidObjectException("rmiServer and jmxServiceURL both null");
/*      */ 
/* 1783 */     initTransients();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1819 */     if ((this.rmiServer == null) && (this.jmxServiceURL == null)) throw new InvalidObjectException("rmiServer and jmxServiceURL both null.");
/*      */ 
/* 1821 */     connectStub(this.rmiServer, this.env);
/* 1822 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void initTransients()
/*      */   {
/* 1827 */     this.rmbscMap = new WeakHashMap();
/* 1828 */     this.connected = false;
/* 1829 */     this.terminated = false;
/*      */ 
/* 1831 */     this.connectionBroadcaster = new NotificationBroadcasterSupport();
/*      */   }
/*      */ 
/*      */   private static void checkStub(Remote paramRemote, Class<?> paramClass)
/*      */   {
/* 1843 */     if (paramRemote.getClass() != paramClass) {
/* 1844 */       if (!Proxy.isProxyClass(paramRemote.getClass())) {
/* 1845 */         throw new SecurityException("Expecting a " + paramClass.getName() + " stub!");
/*      */       }
/*      */ 
/* 1848 */       localObject = Proxy.getInvocationHandler(paramRemote);
/* 1849 */       if (localObject.getClass() != RemoteObjectInvocationHandler.class) {
/* 1850 */         throw new SecurityException("Expecting a dynamic proxy instance with a " + RemoteObjectInvocationHandler.class.getName() + " invocation handler!");
/*      */       }
/*      */ 
/* 1855 */       paramRemote = (Remote)localObject;
/*      */     }
/*      */ 
/* 1862 */     Object localObject = ((RemoteObject)paramRemote).getRef();
/* 1863 */     if (localObject.getClass() != UnicastRef2.class) {
/* 1864 */       throw new SecurityException("Expecting a " + UnicastRef2.class.getName() + " remote reference in stub!");
/*      */     }
/*      */ 
/* 1871 */     LiveRef localLiveRef = ((UnicastRef2)localObject).getLiveRef();
/* 1872 */     RMIClientSocketFactory localRMIClientSocketFactory = localLiveRef.getClientSocketFactory();
/* 1873 */     if ((localRMIClientSocketFactory == null) || (localRMIClientSocketFactory.getClass() != SslRMIClientSocketFactory.class))
/* 1874 */       throw new SecurityException("Expecting a " + SslRMIClientSocketFactory.class.getName() + " RMI client socket factory in stub!");
/*      */   }
/*      */ 
/*      */   private RMIServer findRMIServer(JMXServiceURL paramJMXServiceURL, Map<String, Object> paramMap)
/*      */     throws NamingException, IOException
/*      */   {
/* 1886 */     boolean bool = RMIConnectorServer.isIiopURL(paramJMXServiceURL, true);
/* 1887 */     if (bool)
/*      */     {
/* 1889 */       paramMap.put("java.naming.corba.orb", resolveOrb(paramMap));
/*      */     }
/*      */ 
/* 1892 */     String str1 = paramJMXServiceURL.getURLPath();
/* 1893 */     int i = str1.indexOf(';');
/* 1894 */     if (i < 0) i = str1.length();
/* 1895 */     if (str1.startsWith("/jndi/"))
/* 1896 */       return findRMIServerJNDI(str1.substring(6, i), paramMap, bool);
/* 1897 */     if (str1.startsWith("/stub/"))
/* 1898 */       return findRMIServerJRMP(str1.substring(6, i), paramMap, bool);
/* 1899 */     if (str1.startsWith("/ior/")) {
/* 1900 */       if (!IIOPHelper.isAvailable())
/* 1901 */         throw new IOException("iiop protocol not available");
/* 1902 */       return findRMIServerIIOP(str1.substring(5, i), paramMap, bool);
/*      */     }
/* 1904 */     String str2 = "URL path must begin with /jndi/ or /stub/ or /ior/: " + str1;
/*      */ 
/* 1906 */     throw new MalformedURLException(str2);
/*      */   }
/*      */ 
/*      */   private RMIServer findRMIServerJNDI(String paramString, Map<String, ?> paramMap, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/* 1927 */     InitialContext localInitialContext = new InitialContext(EnvHelp.mapToHashtable(paramMap));
/*      */ 
/* 1929 */     Object localObject = localInitialContext.lookup(paramString);
/* 1930 */     localInitialContext.close();
/*      */ 
/* 1932 */     if (paramBoolean) {
/* 1933 */       return narrowIIOPServer(localObject);
/*      */     }
/* 1935 */     return narrowJRMPServer(localObject);
/*      */   }
/*      */ 
/*      */   private static RMIServer narrowJRMPServer(Object paramObject)
/*      */   {
/* 1940 */     return (RMIServer)paramObject;
/*      */   }
/*      */ 
/*      */   private static RMIServer narrowIIOPServer(Object paramObject) {
/*      */     try {
/* 1945 */       return (RMIServer)IIOPHelper.narrow(paramObject, RMIServer.class);
/*      */     } catch (ClassCastException localClassCastException) {
/* 1947 */       if (logger.traceOn()) {
/* 1948 */         logger.trace("narrowIIOPServer", "Failed to narrow objref=" + paramObject + ": " + localClassCastException);
/*      */       }
/* 1950 */       if (logger.debugOn()) logger.debug("narrowIIOPServer", localClassCastException); 
/*      */     }
/* 1951 */     return null;
/*      */   }
/*      */ 
/*      */   private RMIServer findRMIServerIIOP(String paramString, Map<String, ?> paramMap, boolean paramBoolean)
/*      */   {
/* 1957 */     Object localObject1 = paramMap.get("java.naming.corba.orb");
/* 1958 */     Object localObject2 = IIOPHelper.stringToObject(localObject1, paramString);
/* 1959 */     return (RMIServer)IIOPHelper.narrow(localObject2, RMIServer.class);
/*      */   }
/*      */ 
/*      */   private RMIServer findRMIServerJRMP(String paramString, Map<String, ?> paramMap, boolean paramBoolean) throws IOException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/* 1967 */       arrayOfByte = base64ToByteArray(paramString);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 1969 */       throw new MalformedURLException("Bad BASE64 encoding: " + localIllegalArgumentException.getMessage());
/*      */     }
/*      */ 
/* 1972 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/*      */ 
/* 1974 */     ClassLoader localClassLoader = EnvHelp.resolveClientClassLoader(paramMap);
/* 1975 */     ObjectInputStreamWithLoader localObjectInputStreamWithLoader = localClassLoader == null ? new ObjectInputStream(localByteArrayInputStream) : new ObjectInputStreamWithLoader(localByteArrayInputStream, localClassLoader);
/*      */     Object localObject;
/*      */     try {
/* 1981 */       localObject = localObjectInputStreamWithLoader.readObject();
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 1983 */       throw new MalformedURLException("Class not found: " + localClassNotFoundException);
/*      */     }
/* 1985 */     return (RMIServer)localObject;
/*      */   }
/*      */ 
/*      */   private MBeanServerConnection getConnectionWithSubject(Subject paramSubject)
/*      */   {
/* 2008 */     Object localObject = null;
/*      */ 
/* 2010 */     if (paramSubject == null) {
/* 2011 */       if ((this.nullSubjectConnRef == null) || ((localObject = (MBeanServerConnection)this.nullSubjectConnRef.get()) == null))
/*      */       {
/* 2013 */         localObject = new RemoteMBeanServerConnection(null);
/* 2014 */         this.nullSubjectConnRef = new WeakReference(localObject);
/*      */       }
/*      */     } else {
/* 2017 */       WeakReference localWeakReference = (WeakReference)this.rmbscMap.get(paramSubject);
/* 2018 */       if ((localWeakReference == null) || ((localObject = (MBeanServerConnection)localWeakReference.get()) == null)) {
/* 2019 */         localObject = new RemoteMBeanServerConnection(paramSubject);
/* 2020 */         this.rmbscMap.put(paramSubject, new WeakReference(localObject));
/*      */       }
/*      */     }
/* 2023 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static RMIConnection shadowJrmpStub(RemoteObject paramRemoteObject)
/*      */     throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException
/*      */   {
/* 2167 */     RemoteRef localRemoteRef1 = paramRemoteObject.getRef();
/* 2168 */     RemoteRef localRemoteRef2 = (RemoteRef)proxyRefConstructor.newInstance(new Object[] { localRemoteRef1 });
/*      */ 
/* 2170 */     Constructor localConstructor = rmiConnectionImplStubClass.getConstructor(new Class[] { RemoteRef.class });
/*      */ 
/* 2172 */     Object[] arrayOfObject = { localRemoteRef2 };
/* 2173 */     RMIConnection localRMIConnection = (RMIConnection)localConstructor.newInstance(arrayOfObject);
/*      */ 
/* 2175 */     return localRMIConnection;
/*      */   }
/*      */ 
/*      */   private static RMIConnection shadowIiopStub(Object paramObject)
/*      */     throws InstantiationException, IllegalAccessException
/*      */   {
/* 2387 */     Object localObject = null;
/*      */     try {
/* 2389 */       localObject = AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*      */         public Object run() throws Exception {
/* 2391 */           return RMIConnector.proxyStubClass.newInstance();
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 2395 */       throw new InternalError();
/*      */     }
/* 2397 */     IIOPHelper.setDelegate(localObject, IIOPHelper.getDelegate(paramObject));
/* 2398 */     return (RMIConnection)localObject;
/*      */   }
/*      */ 
/*      */   private static RMIConnection getConnection(RMIServer paramRMIServer, Object paramObject, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 2404 */     RMIConnection localRMIConnection = paramRMIServer.newClient(paramObject);
/* 2405 */     if (paramBoolean) checkStub(localRMIConnection, rmiConnectionImplStubClass); try
/*      */     {
/* 2407 */       if (localRMIConnection.getClass() == rmiConnectionImplStubClass)
/* 2408 */         return shadowJrmpStub((RemoteObject)localRMIConnection);
/* 2409 */       if (localRMIConnection.getClass().getName().equals("org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub"))
/* 2410 */         return shadowIiopStub(localRMIConnection);
/* 2411 */       logger.trace("getConnection", "Did not wrap " + localRMIConnection.getClass() + " to foil " + "stack search for classes: class loading semantics " + "may be incorrect");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 2416 */       logger.error("getConnection", "Could not wrap " + localRMIConnection.getClass() + " to foil " + "stack search for classes: class loading semantics " + "may be incorrect: " + localException);
/*      */ 
/* 2420 */       logger.debug("getConnection", localException);
/*      */     }
/*      */ 
/* 2424 */     return localRMIConnection;
/*      */   }
/*      */ 
/*      */   private static byte[] base64ToByteArray(String paramString) {
/* 2428 */     int i = paramString.length();
/* 2429 */     int j = i / 4;
/* 2430 */     if (4 * j != i) {
/* 2431 */       throw new IllegalArgumentException("String length must be a multiple of four.");
/*      */     }
/* 2433 */     int k = 0;
/* 2434 */     int m = j;
/* 2435 */     if (i != 0) {
/* 2436 */       if (paramString.charAt(i - 1) == '=') {
/* 2437 */         k++;
/* 2438 */         m--;
/*      */       }
/* 2440 */       if (paramString.charAt(i - 2) == '=')
/* 2441 */         k++;
/*      */     }
/* 2443 */     byte[] arrayOfByte = new byte[3 * j - k];
/*      */ 
/* 2446 */     int n = 0; int i1 = 0;
/*      */     int i3;
/*      */     int i4;
/* 2447 */     for (int i2 = 0; i2 < m; i2++) {
/* 2448 */       i3 = base64toInt(paramString.charAt(n++));
/* 2449 */       i4 = base64toInt(paramString.charAt(n++));
/* 2450 */       int i5 = base64toInt(paramString.charAt(n++));
/* 2451 */       int i6 = base64toInt(paramString.charAt(n++));
/* 2452 */       arrayOfByte[(i1++)] = ((byte)(i3 << 2 | i4 >> 4));
/* 2453 */       arrayOfByte[(i1++)] = ((byte)(i4 << 4 | i5 >> 2));
/* 2454 */       arrayOfByte[(i1++)] = ((byte)(i5 << 6 | i6));
/*      */     }
/*      */ 
/* 2458 */     if (k != 0) {
/* 2459 */       i2 = base64toInt(paramString.charAt(n++));
/* 2460 */       i3 = base64toInt(paramString.charAt(n++));
/* 2461 */       arrayOfByte[(i1++)] = ((byte)(i2 << 2 | i3 >> 4));
/*      */ 
/* 2463 */       if (k == 1) {
/* 2464 */         i4 = base64toInt(paramString.charAt(n++));
/* 2465 */         arrayOfByte[(i1++)] = ((byte)(i3 << 4 | i4 >> 2));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2470 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private static int base64toInt(char paramChar)
/*      */   {
/*      */     int i;
/* 2483 */     if (paramChar >= base64ToInt.length)
/* 2484 */       i = -1;
/*      */     else {
/* 2486 */       i = base64ToInt[paramChar];
/*      */     }
/* 2488 */     if (i < 0)
/* 2489 */       throw new IllegalArgumentException("Illegal character " + paramChar);
/* 2490 */     return i;
/*      */   }
/*      */ 
/*      */   private ClassLoader pushDefaultClassLoader()
/*      */   {
/* 2514 */     final Thread localThread = Thread.currentThread();
/* 2515 */     ClassLoader localClassLoader = localThread.getContextClassLoader();
/* 2516 */     if (this.defaultClassLoader != null)
/* 2517 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Void run() {
/* 2519 */           localThread.setContextClassLoader(RMIConnector.this.defaultClassLoader);
/* 2520 */           return null;
/*      */         }
/*      */       });
/* 2523 */     return localClassLoader;
/*      */   }
/*      */ 
/*      */   private void popDefaultClassLoader(final ClassLoader paramClassLoader) {
/* 2527 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Void run() {
/* 2529 */         Thread.currentThread().setContextClassLoader(paramClassLoader);
/* 2530 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static String objects(Object[] paramArrayOfObject)
/*      */   {
/* 2602 */     if (paramArrayOfObject == null) {
/* 2603 */       return "null";
/*      */     }
/* 2605 */     return Arrays.asList(paramArrayOfObject).toString();
/*      */   }
/*      */ 
/*      */   private static String strings(String[] paramArrayOfString) {
/* 2609 */     return objects(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 2113 */     byte[] arrayOfByte = NoCallStackClassLoader.stringToBytes("");
/*      */ 
/* 2115 */     Object localObject1 = new PrivilegedExceptionAction()
/*      */     {
/*      */       public Constructor<?> run() throws Exception {
/* 2118 */         RMIConnector localRMIConnector = RMIConnector.class;
/* 2119 */         ClassLoader localClassLoader = localRMIConnector.getClassLoader();
/* 2120 */         ProtectionDomain localProtectionDomain = localRMIConnector.getProtectionDomain();
/*      */ 
/* 2122 */         String[] arrayOfString = { ProxyRef.class.getName() };
/* 2123 */         NoCallStackClassLoader localNoCallStackClassLoader = new NoCallStackClassLoader("com.sun.jmx.remote.internal.PRef", this.val$pRefByteCode, arrayOfString, localClassLoader, localProtectionDomain);
/*      */ 
/* 2129 */         Class localClass = localNoCallStackClassLoader.loadClass("com.sun.jmx.remote.internal.PRef");
/* 2130 */         return localClass.getConstructor(new Class[] { RemoteRef.class });
/*      */       }
/*      */ 
/*      */     };
/*      */     try
/*      */     {
/* 2136 */       localObject2 = Class.forName(rmiServerImplStubClassName);
/*      */     } catch (Exception localException1) {
/* 2138 */       logger.error("<clinit>", "Failed to instantiate " + rmiServerImplStubClassName + ": " + localException1);
/*      */ 
/* 2141 */       logger.debug("<clinit>", localException1);
/* 2142 */       localObject2 = null;
/*      */     }
/* 2144 */     rmiServerImplStubClass = (Class)localObject2;
/*      */     try
/*      */     {
/* 2149 */       localObject3 = Class.forName(rmiConnectionImplStubClassName);
/* 2150 */       localObject4 = (Constructor)AccessController.doPrivileged((PrivilegedExceptionAction)localObject1);
/*      */     } catch (Exception localException2) {
/* 2152 */       logger.error("<clinit>", "Failed to initialize proxy reference constructor for " + rmiConnectionImplStubClassName + ": " + localException2);
/*      */ 
/* 2155 */       logger.debug("<clinit>", localException2);
/* 2156 */       localObject3 = null;
/* 2157 */       localObject4 = null;
/*      */     }
/* 2159 */     rmiConnectionImplStubClass = (Class)localObject3;
/* 2160 */     proxyRefConstructor = (Constructor)localObject4;
/*      */ 
/* 2343 */     localObject1 = NoCallStackClassLoader.stringToBytes("");
/*      */ 
/* 2345 */     Object localObject2 = NoCallStackClassLoader.stringToBytes("");
/*      */ 
/* 2347 */     Object localObject3 = { "com.sun.jmx.remote.protocol.iiop.ProxyStub", "com.sun.jmx.remote.protocol.iiop.PInputStream" };
/* 2348 */     Object localObject4 = { localObject1, localObject2 };
/* 2349 */     final String[] arrayOfString = { "org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub", "com.sun.jmx.remote.protocol.iiop.ProxyInputStream" };
/*      */ 
/* 2353 */     if (IIOPHelper.isAvailable()) {
/* 2354 */       PrivilegedExceptionAction local2 = new PrivilegedExceptionAction()
/*      */       {
/*      */         public Class<?> run() throws Exception {
/* 2357 */           RMIConnector localRMIConnector = RMIConnector.class;
/* 2358 */           ClassLoader localClassLoader = localRMIConnector.getClassLoader();
/* 2359 */           ProtectionDomain localProtectionDomain = localRMIConnector.getProtectionDomain();
/*      */ 
/* 2361 */           NoCallStackClassLoader localNoCallStackClassLoader = new NoCallStackClassLoader(this.val$classNames, this.val$byteCodes, arrayOfString, localClassLoader, localProtectionDomain);
/*      */ 
/* 2367 */           return localNoCallStackClassLoader.loadClass("com.sun.jmx.remote.protocol.iiop.ProxyStub");
/*      */         } } ;
/*      */       Class localClass;
/*      */       try {
/* 2372 */         localClass = (Class)AccessController.doPrivileged(local2);
/*      */       } catch (Exception localException3) {
/* 2374 */         logger.error("<clinit>", "Unexpected exception making shadow IIOP stub class: " + localException3);
/*      */ 
/* 2376 */         logger.debug("<clinit>", localException3);
/* 2377 */         localClass = null;
/*      */       }
/* 2379 */       proxyStubClass = localClass;
/*      */     } else {
/* 2381 */       proxyStubClass = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ObjectInputStreamWithLoader extends ObjectInputStream
/*      */   {
/*      */     private final ClassLoader loader;
/*      */ 
/*      */     ObjectInputStreamWithLoader(InputStream paramInputStream, ClassLoader paramClassLoader)
/*      */       throws IOException
/*      */     {
/* 1992 */       super();
/* 1993 */       this.loader = paramClassLoader;
/*      */     }
/*      */ 
/*      */     protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/* 1999 */       String str = paramObjectStreamClass.getName();
/* 2000 */       ReflectUtil.checkPackageAccess(str);
/* 2001 */       return Class.forName(str, false, this.loader);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RMIClientCommunicatorAdmin extends ClientCommunicatorAdmin
/*      */   {
/*      */     public RMIClientCommunicatorAdmin(long arg2)
/*      */     {
/* 1467 */       super();
/*      */     }
/*      */ 
/*      */     public void gotIOException(IOException paramIOException) throws IOException
/*      */     {
/* 1472 */       if ((paramIOException instanceof NoSuchObjectException))
/*      */       {
/* 1474 */         super.gotIOException(paramIOException);
/*      */ 
/* 1476 */         return;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1481 */         RMIConnector.this.connection.getDefaultDomain(null);
/*      */       } catch (IOException localIOException) {
/* 1483 */         int i = 0;
/*      */ 
/* 1485 */         synchronized (this) {
/* 1486 */           if (!RMIConnector.this.terminated) {
/* 1487 */             RMIConnector.this.terminated = true;
/*      */ 
/* 1489 */             i = 1;
/*      */           }
/*      */         }
/*      */ 
/* 1493 */         if (i != 0)
/*      */         {
/* 1496 */           ??? = new JMXConnectionNotification("jmx.remote.connection.failed", this, RMIConnector.this.connectionId, RMIConnector.access$1308(RMIConnector.this), "Failed to communicate with the server: " + paramIOException.toString(), paramIOException);
/*      */ 
/* 1505 */           RMIConnector.this.sendNotification((Notification)???);
/*      */           try
/*      */           {
/* 1508 */             RMIConnector.this.close(true);
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1517 */       if ((paramIOException instanceof ServerException))
/*      */       {
/* 1527 */         Throwable localThrowable = ((ServerException)paramIOException).detail;
/*      */ 
/* 1529 */         if ((localThrowable instanceof IOException))
/* 1530 */           throw ((IOException)localThrowable);
/* 1531 */         if ((localThrowable instanceof RuntimeException)) {
/* 1532 */           throw ((RuntimeException)localThrowable);
/*      */         }
/*      */       }
/*      */ 
/* 1536 */       throw paramIOException;
/*      */     }
/*      */ 
/*      */     public void reconnectNotificationListeners(ClientListenerInfo[] paramArrayOfClientListenerInfo) throws IOException {
/* 1540 */       int i = paramArrayOfClientListenerInfo.length;
/*      */ 
/* 1543 */       ClientListenerInfo[] arrayOfClientListenerInfo1 = new ClientListenerInfo[i];
/*      */ 
/* 1545 */       Subject[] arrayOfSubject = new Subject[i];
/* 1546 */       ObjectName[] arrayOfObjectName = new ObjectName[i];
/* 1547 */       NotificationListener[] arrayOfNotificationListener = new NotificationListener[i];
/* 1548 */       NotificationFilter[] arrayOfNotificationFilter = new NotificationFilter[i];
/* 1549 */       MarshalledObject[] arrayOfMarshalledObject = (MarshalledObject[])Util.cast(new MarshalledObject[i]);
/*      */ 
/* 1551 */       Object[] arrayOfObject = new Object[i];
/*      */ 
/* 1553 */       for (int j = 0; j < i; j++) {
/* 1554 */         arrayOfSubject[j] = paramArrayOfClientListenerInfo[j].getDelegationSubject();
/* 1555 */         arrayOfObjectName[j] = paramArrayOfClientListenerInfo[j].getObjectName();
/* 1556 */         arrayOfNotificationListener[j] = paramArrayOfClientListenerInfo[j].getListener();
/* 1557 */         arrayOfNotificationFilter[j] = paramArrayOfClientListenerInfo[j].getNotificationFilter();
/* 1558 */         arrayOfMarshalledObject[j] = new MarshalledObject(arrayOfNotificationFilter[j]);
/* 1559 */         arrayOfObject[j] = paramArrayOfClientListenerInfo[j].getHandback();
/*      */       }
/*      */       try
/*      */       {
/* 1563 */         Integer[] arrayOfInteger = RMIConnector.this.addListenersWithSubjects(arrayOfObjectName, arrayOfMarshalledObject, arrayOfSubject, false);
/*      */ 
/* 1565 */         for (j = 0; j < i; j++) {
/* 1566 */           arrayOfClientListenerInfo1[j] = new ClientListenerInfo(arrayOfInteger[j], arrayOfObjectName[j], arrayOfNotificationListener[j], arrayOfNotificationFilter[j], arrayOfObject[j], arrayOfSubject[j]);
/*      */         }
/*      */ 
/* 1574 */         RMIConnector.this.rmiNotifClient.postReconnection(arrayOfClientListenerInfo1);
/*      */ 
/* 1576 */         return;
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException1)
/*      */       {
/* 1581 */         int k = 0;
/* 1582 */         for (j = 0; j < i; j++) {
/*      */           try {
/* 1584 */             Integer localInteger = RMIConnector.this.addListenerWithSubject(arrayOfObjectName[j], new MarshalledObject(arrayOfNotificationFilter[j]), arrayOfSubject[j], false);
/*      */ 
/* 1589 */             arrayOfClientListenerInfo1[(k++)] = new ClientListenerInfo(localInteger, arrayOfObjectName[j], arrayOfNotificationListener[j], arrayOfNotificationFilter[j], arrayOfObject[j], arrayOfSubject[j]);
/*      */           }
/*      */           catch (InstanceNotFoundException localInstanceNotFoundException2)
/*      */           {
/* 1596 */             RMIConnector.logger.warning("reconnectNotificationListeners", "Can't reconnect listener for " + arrayOfObjectName[j]);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1602 */         if (k != i) {
/* 1603 */           ClientListenerInfo[] arrayOfClientListenerInfo2 = arrayOfClientListenerInfo1;
/* 1604 */           arrayOfClientListenerInfo1 = new ClientListenerInfo[k];
/* 1605 */           System.arraycopy(arrayOfClientListenerInfo2, 0, arrayOfClientListenerInfo1, 0, k);
/*      */         }
/*      */ 
/* 1608 */         RMIConnector.this.rmiNotifClient.postReconnection(arrayOfClientListenerInfo1);
/*      */       }
/*      */     }
/*      */ 
/* 1612 */     protected void checkConnection() throws IOException { if (RMIConnector.logger.debugOn()) {
/* 1613 */         RMIConnector.logger.debug("RMIClientCommunicatorAdmin-checkConnection", "Calling the method getDefaultDomain.");
/*      */       }
/*      */ 
/* 1616 */       RMIConnector.this.connection.getDefaultDomain(null); }
/*      */ 
/*      */     protected void doStart()
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/* 1623 */         localRMIServer = RMIConnector.this.rmiServer != null ? RMIConnector.this.rmiServer : RMIConnector.this.findRMIServer(RMIConnector.this.jmxServiceURL, RMIConnector.this.env);
/*      */       }
/*      */       catch (NamingException localNamingException) {
/* 1626 */         throw new IOException("Failed to get a RMI stub: " + localNamingException);
/*      */       }
/*      */ 
/* 1630 */       RMIServer localRMIServer = RMIConnector.connectStub(localRMIServer, RMIConnector.this.env);
/*      */ 
/* 1633 */       Object localObject = RMIConnector.this.env.get("jmx.remote.credentials");
/* 1634 */       RMIConnector.this.connection = localRMIServer.newClient(localObject);
/*      */ 
/* 1637 */       ClientListenerInfo[] arrayOfClientListenerInfo = RMIConnector.this.rmiNotifClient.preReconnection();
/*      */ 
/* 1639 */       reconnectNotificationListeners(arrayOfClientListenerInfo);
/*      */ 
/* 1641 */       RMIConnector.this.connectionId = RMIConnector.this.getConnectionId();
/*      */ 
/* 1643 */       JMXConnectionNotification localJMXConnectionNotification = new JMXConnectionNotification("jmx.remote.connection.opened", this, RMIConnector.this.connectionId, RMIConnector.access$1308(RMIConnector.this), "Reconnected to server", null);
/*      */ 
/* 1650 */       RMIConnector.this.sendNotification(localJMXConnectionNotification);
/*      */     }
/*      */ 
/*      */     protected void doStop()
/*      */     {
/*      */       try {
/* 1656 */         RMIConnector.this.close();
/*      */       } catch (IOException localIOException) {
/* 1658 */         RMIConnector.logger.warning("RMIClientCommunicatorAdmin-doStop", "Failed to call the method close():" + localIOException);
/*      */ 
/* 1660 */         RMIConnector.logger.debug("RMIClientCommunicatorAdmin-doStop", localIOException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RMINotifClient extends ClientNotifForwarder
/*      */   {
/*      */     public RMINotifClient(Map<String, ?> arg2)
/*      */     {
/* 1331 */       super(localMap);
/*      */     }
/*      */ 
/*      */     protected NotificationResult fetchNotifs(long paramLong1, int paramInt, long paramLong2)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/*      */       try
/*      */       {
/* 1342 */         return RMIConnector.this.connection.fetchNotifications(paramLong1, paramInt, paramLong2);
/*      */       } catch (IOException localIOException2) {
/*      */         IOException localIOException1;
/*      */         while (true) {
/* 1346 */           localIOException1 = localIOException2;
/*      */           try
/*      */           {
/* 1350 */             RMIConnector.this.communicatorAdmin.gotIOException(localIOException2);
/*      */           }
/*      */           catch (IOException localIOException3)
/*      */           {
/*      */             Object localObject;
/* 1362 */             if ((localIOException1 instanceof UnmarshalException)) {
/* 1363 */               localObject = (UnmarshalException)localIOException1;
/*      */ 
/* 1365 */               if ((((UnmarshalException)localObject).detail instanceof ClassNotFoundException)) {
/* 1366 */                 throw ((ClassNotFoundException)((UnmarshalException)localObject).detail);
/*      */               }
/*      */ 
/* 1381 */               if ((((UnmarshalException)localObject).detail instanceof WriteAbortedException)) {
/* 1382 */                 WriteAbortedException localWriteAbortedException = (WriteAbortedException)((UnmarshalException)localObject).detail;
/*      */ 
/* 1384 */                 if ((localWriteAbortedException.detail instanceof IOException))
/* 1385 */                   throw ((IOException)localWriteAbortedException.detail);
/*      */               }
/* 1387 */             } else if ((localIOException1 instanceof MarshalException))
/*      */             {
/* 1390 */               localObject = (MarshalException)localIOException1;
/* 1391 */               if ((((MarshalException)localObject).detail instanceof NotSerializableException)) {
/* 1392 */                 throw ((NotSerializableException)((MarshalException)localObject).detail);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1397 */         throw localIOException1; }  } 
/* 1402 */     protected Integer addListenerForMBeanRemovedNotif() throws IOException, InstanceNotFoundException { NotificationFilterSupport localNotificationFilterSupport = new NotificationFilterSupport();
/*      */ 
/* 1404 */       localNotificationFilterSupport.enableType("JMX.mbean.unregistered");
/*      */ 
/* 1406 */       MarshalledObject localMarshalledObject = new MarshalledObject(localNotificationFilterSupport);
/*      */ 
/* 1410 */       ObjectName[] arrayOfObjectName = { MBeanServerDelegate.DELEGATE_NAME };
/*      */ 
/* 1412 */       MarshalledObject[] arrayOfMarshalledObject = (MarshalledObject[])Util.cast(new MarshalledObject[] { localMarshalledObject });
/*      */ 
/* 1414 */       Subject[] arrayOfSubject = { null };
/*      */       Integer[] arrayOfInteger;
/*      */       try { arrayOfInteger = RMIConnector.this.connection.addNotificationListeners(arrayOfObjectName, arrayOfMarshalledObject, arrayOfSubject); }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1422 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1424 */         arrayOfInteger = RMIConnector.this.connection.addNotificationListeners(arrayOfObjectName, arrayOfMarshalledObject, arrayOfSubject);
/*      */       }
/*      */ 
/* 1429 */       return arrayOfInteger[0]; }
/*      */ 
/*      */     protected void removeListenerForMBeanRemovedNotif(Integer paramInteger)
/*      */       throws IOException, InstanceNotFoundException, ListenerNotFoundException
/*      */     {
/*      */       try
/*      */       {
/* 1436 */         RMIConnector.this.connection.removeNotificationListeners(MBeanServerDelegate.DELEGATE_NAME, new Integer[] { paramInteger }, null);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1441 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1443 */         RMIConnector.this.connection.removeNotificationListeners(MBeanServerDelegate.DELEGATE_NAME, new Integer[] { paramInteger }, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void lostNotifs(String paramString, long paramLong)
/*      */     {
/* 1454 */       JMXConnectionNotification localJMXConnectionNotification = new JMXConnectionNotification("jmx.remote.connection.notifs.lost", RMIConnector.this, RMIConnector.this.connectionId, RMIConnector.access$1008(RMIConnector.this), paramString, Long.valueOf(paramLong));
/*      */ 
/* 1461 */       RMIConnector.this.sendNotification(localJMXConnectionNotification);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RemoteMBeanServerConnection
/*      */     implements MBeanServerConnection
/*      */   {
/*      */     private Subject delegationSubject;
/*      */ 
/*      */     public RemoteMBeanServerConnection()
/*      */     {
/*  629 */       this(null);
/*      */     }
/*      */ 
/*      */     public RemoteMBeanServerConnection(Subject arg2)
/*      */     {
/*      */       Object localObject;
/*  633 */       this.delegationSubject = localObject;
/*      */     }
/*      */ 
/*      */     public ObjectInstance createMBean(String paramString, ObjectName paramObjectName)
/*      */       throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
/*      */     {
/*  644 */       if (RMIConnector.logger.debugOn()) {
/*  645 */         RMIConnector.logger.debug("createMBean(String,ObjectName)", "className=" + paramString + ", name=" + paramObjectName);
/*      */       }
/*      */ 
/*  649 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  651 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  655 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  657 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  661 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2)
/*      */       throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
/*      */     {
/*  676 */       if (RMIConnector.logger.debugOn()) {
/*  677 */         RMIConnector.logger.debug("createMBean(String,ObjectName,ObjectName)", "className=" + paramString + ", name=" + paramObjectName1 + ", loaderName=" + paramObjectName2 + ")");
/*      */       }
/*      */ 
/*  682 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  684 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName1, paramObjectName2, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  690 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  692 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName1, paramObjectName2, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  698 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */       throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
/*      */     {
/*  712 */       if (RMIConnector.logger.debugOn()) {
/*  713 */         RMIConnector.logger.debug("createMBean(String,ObjectName,Object[],String[])", "className=" + paramString + ", name=" + paramObjectName + ", params=" + RMIConnector.objects(paramArrayOfObject) + ", signature=" + RMIConnector.strings(paramArrayOfString));
/*      */       }
/*      */ 
/*  719 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramArrayOfObject);
/*      */ 
/*  721 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  723 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  729 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  731 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  737 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */       throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
/*      */     {
/*  753 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "className=" + paramString + ", name=" + paramObjectName1 + ", loaderName=" + paramObjectName2 + ", params=" + RMIConnector.objects(paramArrayOfObject) + ", signature=" + RMIConnector.strings(paramArrayOfString));
/*      */ 
/*  759 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramArrayOfObject);
/*      */ 
/*  761 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  763 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName1, paramObjectName2, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  770 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  772 */         return RMIConnector.this.connection.createMBean(paramString, paramObjectName1, paramObjectName2, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  779 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void unregisterMBean(ObjectName paramObjectName)
/*      */       throws InstanceNotFoundException, MBeanRegistrationException, IOException
/*      */     {
/*  787 */       if (RMIConnector.logger.debugOn()) {
/*  788 */         RMIConnector.logger.debug("unregisterMBean", "name=" + paramObjectName);
/*      */       }
/*  790 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  792 */         RMIConnector.this.connection.unregisterMBean(paramObjectName, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  794 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  796 */         RMIConnector.this.connection.unregisterMBean(paramObjectName, this.delegationSubject);
/*      */       } finally {
/*  798 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public ObjectInstance getObjectInstance(ObjectName paramObjectName)
/*      */       throws InstanceNotFoundException, IOException
/*      */     {
/*  805 */       if (RMIConnector.logger.debugOn()) {
/*  806 */         RMIConnector.logger.debug("getObjectInstance", "name=" + paramObjectName);
/*      */       }
/*  808 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  810 */         return RMIConnector.this.connection.getObjectInstance(paramObjectName, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  812 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  814 */         return RMIConnector.this.connection.getObjectInstance(paramObjectName, this.delegationSubject);
/*      */       } finally {
/*  816 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, QueryExp paramQueryExp)
/*      */       throws IOException
/*      */     {
/*  823 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("queryMBeans", "name=" + paramObjectName + ", query=" + paramQueryExp);
/*      */ 
/*  826 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramQueryExp);
/*      */ 
/*  828 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  830 */         return RMIConnector.this.connection.queryMBeans(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  832 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  834 */         return RMIConnector.this.connection.queryMBeans(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } finally {
/*  836 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Set<ObjectName> queryNames(ObjectName paramObjectName, QueryExp paramQueryExp)
/*      */       throws IOException
/*      */     {
/*  843 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("queryNames", "name=" + paramObjectName + ", query=" + paramQueryExp);
/*      */ 
/*  846 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramQueryExp);
/*      */ 
/*  848 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  850 */         return RMIConnector.this.connection.queryNames(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  852 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  854 */         return RMIConnector.this.connection.queryNames(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } finally {
/*  856 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isRegistered(ObjectName paramObjectName) throws IOException
/*      */     {
/*  862 */       if (RMIConnector.logger.debugOn()) {
/*  863 */         RMIConnector.logger.debug("isRegistered", "name=" + paramObjectName);
/*      */       }
/*  865 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  867 */         return RMIConnector.this.connection.isRegistered(paramObjectName, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  869 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  871 */         return RMIConnector.this.connection.isRegistered(paramObjectName, this.delegationSubject);
/*      */       } finally {
/*  873 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Integer getMBeanCount() throws IOException
/*      */     {
/*  879 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getMBeanCount", "");
/*      */ 
/*  881 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  883 */         return RMIConnector.this.connection.getMBeanCount(this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  885 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  887 */         return RMIConnector.this.connection.getMBeanCount(this.delegationSubject);
/*      */       } finally {
/*  889 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object getAttribute(ObjectName paramObjectName, String paramString)
/*      */       throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException
/*      */     {
/*  900 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getAttribute", "name=" + paramObjectName + ", attribute=" + paramString);
/*      */ 
/*  904 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  906 */         return RMIConnector.this.connection.getAttribute(paramObjectName, paramString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  910 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  912 */         return RMIConnector.this.connection.getAttribute(paramObjectName, paramString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  916 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString)
/*      */       throws InstanceNotFoundException, ReflectionException, IOException
/*      */     {
/*  925 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getAttributes", "name=" + paramObjectName + ", attributes=" + RMIConnector.strings(paramArrayOfString));
/*      */ 
/*  929 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  931 */         return RMIConnector.this.connection.getAttributes(paramObjectName, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  936 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  938 */         return RMIConnector.this.connection.getAttributes(paramObjectName, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  942 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setAttribute(ObjectName paramObjectName, Attribute paramAttribute)
/*      */       throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException
/*      */     {
/*  956 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("setAttribute", "name=" + paramObjectName + ", attribute=" + paramAttribute);
/*      */ 
/*  960 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramAttribute);
/*      */ 
/*  962 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  964 */         RMIConnector.this.connection.setAttribute(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/*  966 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  968 */         RMIConnector.this.connection.setAttribute(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       } finally {
/*  970 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public AttributeList setAttributes(ObjectName paramObjectName, AttributeList paramAttributeList)
/*      */       throws InstanceNotFoundException, ReflectionException, IOException
/*      */     {
/*  980 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("setAttributes", "name=" + paramObjectName + ", attributes=" + paramAttributeList);
/*      */ 
/*  984 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramAttributeList);
/*      */ 
/*  986 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/*  988 */         return RMIConnector.this.connection.setAttributes(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  992 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/*  994 */         return RMIConnector.this.connection.setAttributes(paramObjectName, localMarshalledObject, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/*  998 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object invoke(ObjectName paramObjectName, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */       throws InstanceNotFoundException, MBeanException, ReflectionException, IOException
/*      */     {
/* 1012 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("invoke", "name=" + paramObjectName + ", operationName=" + paramString + ", params=" + RMIConnector.objects(paramArrayOfObject) + ", signature=" + RMIConnector.strings(paramArrayOfString));
/*      */ 
/* 1018 */       MarshalledObject localMarshalledObject = new MarshalledObject(paramArrayOfObject);
/*      */ 
/* 1020 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1022 */         return RMIConnector.this.connection.invoke(paramObjectName, paramString, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1028 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1030 */         return RMIConnector.this.connection.invoke(paramObjectName, paramString, localMarshalledObject, paramArrayOfString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1036 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public String getDefaultDomain()
/*      */       throws IOException
/*      */     {
/* 1043 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getDefaultDomain", "");
/*      */ 
/* 1045 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1047 */         return RMIConnector.this.connection.getDefaultDomain(this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/* 1049 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1051 */         return RMIConnector.this.connection.getDefaultDomain(this.delegationSubject);
/*      */       } finally {
/* 1053 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public String[] getDomains() throws IOException {
/* 1058 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getDomains", "");
/*      */ 
/* 1060 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1062 */         return RMIConnector.this.connection.getDomains(this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/* 1064 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1066 */         return RMIConnector.this.connection.getDomains(this.delegationSubject);
/*      */       } finally {
/* 1068 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public MBeanInfo getMBeanInfo(ObjectName paramObjectName)
/*      */       throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException
/*      */     {
/* 1078 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("getMBeanInfo", "name=" + paramObjectName);
/* 1079 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1081 */         return RMIConnector.this.connection.getMBeanInfo(paramObjectName, this.delegationSubject);
/*      */       } catch (IOException localIOException) {
/* 1083 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1085 */         return RMIConnector.this.connection.getMBeanInfo(paramObjectName, this.delegationSubject);
/*      */       } finally {
/* 1087 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isInstanceOf(ObjectName paramObjectName, String paramString)
/*      */       throws InstanceNotFoundException, IOException
/*      */     {
/* 1096 */       if (RMIConnector.logger.debugOn()) {
/* 1097 */         RMIConnector.logger.debug("isInstanceOf", "name=" + paramObjectName + ", className=" + paramString);
/*      */       }
/*      */ 
/* 1100 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1102 */         return RMIConnector.this.connection.isInstanceOf(paramObjectName, paramString, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1106 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1108 */         return RMIConnector.this.connection.isInstanceOf(paramObjectName, paramString, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1112 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */       throws InstanceNotFoundException, IOException
/*      */     {
/* 1123 */       if (RMIConnector.logger.debugOn()) {
/* 1124 */         RMIConnector.logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "name=" + paramObjectName1 + ", listener=" + paramObjectName2 + ", filter=" + paramNotificationFilter + ", handback=" + paramObject);
/*      */       }
/*      */ 
/* 1129 */       MarshalledObject localMarshalledObject1 = new MarshalledObject(paramNotificationFilter);
/*      */ 
/* 1131 */       MarshalledObject localMarshalledObject2 = new MarshalledObject(paramObject);
/*      */ 
/* 1133 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1135 */         RMIConnector.this.connection.addNotificationListener(paramObjectName1, paramObjectName2, localMarshalledObject1, localMarshalledObject2, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1141 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1143 */         RMIConnector.this.connection.addNotificationListener(paramObjectName1, paramObjectName2, localMarshalledObject1, localMarshalledObject2, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1149 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2)
/*      */       throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */     {
/* 1159 */       if (RMIConnector.logger.debugOn()) RMIConnector.logger.debug("removeNotificationListener(ObjectName,ObjectName)", "name=" + paramObjectName1 + ", listener=" + paramObjectName2);
/*      */ 
/* 1164 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1166 */         RMIConnector.this.connection.removeNotificationListener(paramObjectName1, paramObjectName2, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1170 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1172 */         RMIConnector.this.connection.removeNotificationListener(paramObjectName1, paramObjectName2, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1176 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */       throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */     {
/* 1187 */       if (RMIConnector.logger.debugOn()) {
/* 1188 */         RMIConnector.logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "name=" + paramObjectName1 + ", listener=" + paramObjectName2 + ", filter=" + paramNotificationFilter + ", handback=" + paramObject);
/*      */       }
/*      */ 
/* 1195 */       MarshalledObject localMarshalledObject1 = new MarshalledObject(paramNotificationFilter);
/*      */ 
/* 1197 */       MarshalledObject localMarshalledObject2 = new MarshalledObject(paramObject);
/*      */ 
/* 1199 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1201 */         RMIConnector.this.connection.removeNotificationListener(paramObjectName1, paramObjectName2, localMarshalledObject1, localMarshalledObject2, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1207 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1209 */         RMIConnector.this.connection.removeNotificationListener(paramObjectName1, paramObjectName2, localMarshalledObject1, localMarshalledObject2, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1215 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */       throws InstanceNotFoundException, IOException
/*      */     {
/* 1228 */       boolean bool = RMIConnector.logger.debugOn();
/*      */ 
/* 1230 */       if (bool) {
/* 1231 */         RMIConnector.logger.debug("addNotificationListener(ObjectName,NotificationListener,NotificationFilter,Object)", "name=" + paramObjectName + ", listener=" + paramNotificationListener + ", filter=" + paramNotificationFilter + ", handback=" + paramObject);
/*      */       }
/*      */ 
/* 1239 */       Integer localInteger = RMIConnector.this.addListenerWithSubject(paramObjectName, new MarshalledObject(paramNotificationFilter), this.delegationSubject, true);
/*      */ 
/* 1243 */       RMIConnector.this.rmiNotifClient.addNotificationListener(localInteger, paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject, this.delegationSubject);
/*      */     }
/*      */ 
/*      */     public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
/*      */       throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */     {
/* 1254 */       boolean bool = RMIConnector.logger.debugOn();
/*      */ 
/* 1256 */       if (bool) RMIConnector.logger.debug("removeNotificationListener(ObjectName,NotificationListener)", "name=" + paramObjectName + ", listener=" + paramNotificationListener);
/*      */ 
/* 1261 */       Integer[] arrayOfInteger = RMIConnector.this.rmiNotifClient.removeNotificationListener(paramObjectName, paramNotificationListener);
/*      */ 
/* 1264 */       if (bool) RMIConnector.logger.debug("removeNotificationListener", "listenerIDs=" + RMIConnector.objects(arrayOfInteger));
/*      */ 
/* 1267 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try
/*      */       {
/* 1270 */         RMIConnector.this.connection.removeNotificationListeners(paramObjectName, arrayOfInteger, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1274 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1276 */         RMIConnector.this.connection.removeNotificationListeners(paramObjectName, arrayOfInteger, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1280 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */       throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*      */     {
/* 1292 */       boolean bool = RMIConnector.logger.debugOn();
/*      */ 
/* 1294 */       if (bool) {
/* 1295 */         RMIConnector.logger.debug("removeNotificationListener(ObjectName,NotificationListener,NotificationFilter,Object)", "name=" + paramObjectName + ", listener=" + paramNotificationListener + ", filter=" + paramNotificationFilter + ", handback=" + paramObject);
/*      */       }
/*      */ 
/* 1303 */       Integer localInteger = RMIConnector.this.rmiNotifClient.removeNotificationListener(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject);
/*      */ 
/* 1307 */       if (bool) RMIConnector.logger.debug("removeNotificationListener", "listenerID=" + localInteger);
/*      */ 
/* 1310 */       ClassLoader localClassLoader = RMIConnector.this.pushDefaultClassLoader();
/*      */       try {
/* 1312 */         RMIConnector.this.connection.removeNotificationListeners(paramObjectName, new Integer[] { localInteger }, this.delegationSubject);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1316 */         RMIConnector.this.communicatorAdmin.gotIOException(localIOException);
/*      */ 
/* 1318 */         RMIConnector.this.connection.removeNotificationListeners(paramObjectName, new Integer[] { localInteger }, this.delegationSubject);
/*      */       }
/*      */       finally
/*      */       {
/* 1322 */         RMIConnector.this.popDefaultClassLoader(localClassLoader);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIConnector
 * JD-Core Version:    0.6.2
 */