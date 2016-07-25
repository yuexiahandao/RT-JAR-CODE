/*      */ package sun.rmi.server;
/*      */ 
/*      */ import com.sun.rmi.rmid.ExecOptionPermission;
/*      */ import com.sun.rmi.rmid.ExecPermission;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.InetAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketException;
/*      */ import java.nio.channels.Channel;
/*      */ import java.nio.channels.ServerSocketChannel;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.rmi.AccessException;
/*      */ import java.rmi.AlreadyBoundException;
/*      */ import java.rmi.ConnectException;
/*      */ import java.rmi.ConnectIOException;
/*      */ import java.rmi.MarshalledObject;
/*      */ import java.rmi.NoSuchObjectException;
/*      */ import java.rmi.NotBoundException;
/*      */ import java.rmi.Remote;
/*      */ import java.rmi.RemoteException;
/*      */ import java.rmi.activation.ActivationDesc;
/*      */ import java.rmi.activation.ActivationException;
/*      */ import java.rmi.activation.ActivationGroup;
/*      */ import java.rmi.activation.ActivationGroupDesc;
/*      */ import java.rmi.activation.ActivationGroupDesc.CommandEnvironment;
/*      */ import java.rmi.activation.ActivationGroupID;
/*      */ import java.rmi.activation.ActivationID;
/*      */ import java.rmi.activation.ActivationInstantiator;
/*      */ import java.rmi.activation.ActivationMonitor;
/*      */ import java.rmi.activation.ActivationSystem;
/*      */ import java.rmi.activation.Activator;
/*      */ import java.rmi.activation.UnknownGroupException;
/*      */ import java.rmi.activation.UnknownObjectException;
/*      */ import java.rmi.registry.Registry;
/*      */ import java.rmi.server.ObjID;
/*      */ import java.rmi.server.RMIClassLoader;
/*      */ import java.rmi.server.RMIClientSocketFactory;
/*      */ import java.rmi.server.RMIServerSocketFactory;
/*      */ import java.rmi.server.RemoteObject;
/*      */ import java.rmi.server.RemoteServer;
/*      */ import java.rmi.server.UnicastRemoteObject;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.AllPermission;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Policy;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.cert.Certificate;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Properties;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import sun.rmi.log.LogHandler;
/*      */ import sun.rmi.log.ReliableLog;
/*      */ import sun.rmi.registry.RegistryImpl;
/*      */ import sun.rmi.transport.LiveRef;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.action.GetIntegerAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.provider.PolicyFile;
/*      */ 
/*      */ public class Activation
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 2921265612698155191L;
/*      */   private static final byte MAJOR_VERSION = 1;
/*      */   private static final byte MINOR_VERSION = 0;
/*      */   private static Object execPolicy;
/*      */   private static Method execPolicyMethod;
/*      */   private static boolean debugExec;
/*  151 */   private Map<ActivationID, ActivationGroupID> idTable = new ConcurrentHashMap();
/*      */ 
/*  154 */   private Map<ActivationGroupID, GroupEntry> groupTable = new ConcurrentHashMap();
/*      */ 
/*  157 */   private byte majorVersion = 1;
/*  158 */   private byte minorVersion = 0;
/*      */   private transient int groupSemaphore;
/*      */   private transient int groupCounter;
/*      */   private transient ReliableLog log;
/*      */   private transient int numUpdates;
/*      */   private transient String[] command;
/*  173 */   private static final long groupTimeout = getInt("sun.rmi.activation.groupTimeout", 60000);
/*      */ 
/*  176 */   private static final int snapshotInterval = getInt("sun.rmi.activation.snapshotInterval", 200);
/*      */ 
/*  179 */   private static final long execTimeout = getInt("sun.rmi.activation.execTimeout", 30000);
/*      */ 
/*  182 */   private static final Object initLock = new Object();
/*  183 */   private static boolean initDone = false;
/*      */   private transient Activator activator;
/*      */   private transient Activator activatorStub;
/*      */   private transient ActivationSystem system;
/*      */   private transient ActivationSystem systemStub;
/*      */   private transient ActivationMonitor monitor;
/*      */   private transient Registry registry;
/*  196 */   private volatile transient boolean shuttingDown = false;
/*      */   private volatile transient Object startupLock;
/*      */   private transient Thread shutdownHook;
/*  200 */   private static ResourceBundle resources = null;
/*      */ 
/*      */   private static int getInt(String paramString, int paramInt)
/*      */   {
/*  187 */     return ((Integer)AccessController.doPrivileged(new GetIntegerAction(paramString, paramInt))).intValue();
/*      */   }
/*      */ 
/*      */   private static void startActivation(int paramInt, RMIServerSocketFactory paramRMIServerSocketFactory, String paramString, String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/*  219 */     ReliableLog localReliableLog = new ReliableLog(paramString, new ActLogHandler());
/*  220 */     Activation localActivation = (Activation)localReliableLog.recover();
/*  221 */     localActivation.init(paramInt, paramRMIServerSocketFactory, localReliableLog, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   private void init(int paramInt, RMIServerSocketFactory paramRMIServerSocketFactory, ReliableLog paramReliableLog, String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/*  235 */     this.log = paramReliableLog;
/*  236 */     this.numUpdates = 0;
/*  237 */     this.shutdownHook = new ShutdownHook();
/*  238 */     this.groupSemaphore = getInt("sun.rmi.activation.groupThrottle", 3);
/*  239 */     this.groupCounter = 0;
/*  240 */     Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*      */ 
/*  244 */     ActivationGroupID[] arrayOfActivationGroupID = (ActivationGroupID[])this.groupTable.keySet().toArray(new ActivationGroupID[0]);
/*      */ 
/*  247 */     synchronized (this.startupLock = new Object())
/*      */     {
/*  252 */       this.activator = new ActivatorImpl(paramInt, paramRMIServerSocketFactory);
/*  253 */       this.activatorStub = ((Activator)RemoteObject.toStub(this.activator));
/*  254 */       this.system = new ActivationSystemImpl(paramInt, paramRMIServerSocketFactory);
/*  255 */       this.systemStub = ((ActivationSystem)RemoteObject.toStub(this.system));
/*  256 */       this.monitor = new ActivationMonitorImpl(paramInt, paramRMIServerSocketFactory);
/*  257 */       initCommand(paramArrayOfString);
/*  258 */       this.registry = new SystemRegistryImpl(paramInt, null, paramRMIServerSocketFactory, this.systemStub);
/*      */ 
/*  260 */       if (paramRMIServerSocketFactory != null) {
/*  261 */         synchronized (initLock) {
/*  262 */           initDone = true;
/*  263 */           initLock.notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*  267 */     this.startupLock = null;
/*      */ 
/*  270 */     int i = arrayOfActivationGroupID.length;
/*      */     while (true) { i--; if (i < 0) break;
/*      */       try {
/*  272 */         getGroupEntry(arrayOfActivationGroupID[i]).restartServices();
/*      */       } catch (UnknownGroupException localUnknownGroupException) {
/*  274 */         System.err.println(getTextResource("rmid.restart.group.warning"));
/*      */ 
/*  276 */         localUnknownGroupException.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  289 */     paramObjectInputStream.defaultReadObject();
/*  290 */     if (!(this.groupTable instanceof ConcurrentHashMap)) {
/*  291 */       this.groupTable = new ConcurrentHashMap(this.groupTable);
/*      */     }
/*  293 */     if (!(this.idTable instanceof ConcurrentHashMap))
/*  294 */       this.idTable = new ConcurrentHashMap(this.idTable);
/*      */   }
/*      */ 
/*      */   private void checkShutdown()
/*      */     throws ActivationException
/*      */   {
/*  612 */     Object localObject1 = this.startupLock;
/*  613 */     if (localObject1 != null) {
/*  614 */       synchronized (localObject1)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  619 */     if (this.shuttingDown == true)
/*  620 */       throw new ActivationException("activation system shutting down");
/*      */   }
/*      */ 
/*      */   private static void unexport(Remote paramRemote)
/*      */   {
/*      */     while (true)
/*      */       try
/*      */       {
/*  628 */         if (UnicastRemoteObject.unexportObject(paramRemote, false) == true) {
/*      */           break;
/*      */         }
/*  631 */         Thread.sleep(100L);
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   private ActivationGroupID getGroupID(ActivationID paramActivationID)
/*      */     throws UnknownObjectException
/*      */   {
/*  720 */     ActivationGroupID localActivationGroupID = (ActivationGroupID)this.idTable.get(paramActivationID);
/*  721 */     if (localActivationGroupID != null) {
/*  722 */       return localActivationGroupID;
/*      */     }
/*  724 */     throw new UnknownObjectException("unknown object: " + paramActivationID);
/*      */   }
/*      */ 
/*      */   private GroupEntry getGroupEntry(ActivationGroupID paramActivationGroupID, boolean paramBoolean)
/*      */     throws UnknownGroupException
/*      */   {
/*  734 */     if (paramActivationGroupID.getClass() == ActivationGroupID.class)
/*      */     {
/*      */       GroupEntry localGroupEntry;
/*  736 */       if (paramBoolean)
/*  737 */         localGroupEntry = (GroupEntry)this.groupTable.remove(paramActivationGroupID);
/*      */       else {
/*  739 */         localGroupEntry = (GroupEntry)this.groupTable.get(paramActivationGroupID);
/*      */       }
/*  741 */       if ((localGroupEntry != null) && (!localGroupEntry.removed)) {
/*  742 */         return localGroupEntry;
/*      */       }
/*      */     }
/*  745 */     throw new UnknownGroupException("group unknown");
/*      */   }
/*      */ 
/*      */   private GroupEntry getGroupEntry(ActivationGroupID paramActivationGroupID)
/*      */     throws UnknownGroupException
/*      */   {
/*  755 */     return getGroupEntry(paramActivationGroupID, false);
/*      */   }
/*      */ 
/*      */   private GroupEntry removeGroupEntry(ActivationGroupID paramActivationGroupID)
/*      */     throws UnknownGroupException
/*      */   {
/*  765 */     return getGroupEntry(paramActivationGroupID, true);
/*      */   }
/*      */ 
/*      */   private GroupEntry getGroupEntry(ActivationID paramActivationID)
/*      */     throws UnknownObjectException
/*      */   {
/*  776 */     ActivationGroupID localActivationGroupID = getGroupID(paramActivationID);
/*  777 */     GroupEntry localGroupEntry = (GroupEntry)this.groupTable.get(localActivationGroupID);
/*  778 */     if ((localGroupEntry != null) && (!localGroupEntry.removed)) {
/*  779 */       return localGroupEntry;
/*      */     }
/*  781 */     throw new UnknownObjectException("object's group removed");
/*      */   }
/*      */ 
/*      */   private String[] activationArgs(ActivationGroupDesc paramActivationGroupDesc)
/*      */   {
/* 1356 */     ActivationGroupDesc.CommandEnvironment localCommandEnvironment = paramActivationGroupDesc.getCommandEnvironment();
/*      */ 
/* 1359 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1362 */     localArrayList.add((localCommandEnvironment != null) && (localCommandEnvironment.getCommandPath() != null) ? localCommandEnvironment.getCommandPath() : this.command[0]);
/*      */ 
/* 1367 */     if ((localCommandEnvironment != null) && (localCommandEnvironment.getCommandOptions() != null)) {
/* 1368 */       localArrayList.addAll(Arrays.asList(localCommandEnvironment.getCommandOptions()));
/*      */     }
/*      */ 
/* 1372 */     Properties localProperties = paramActivationGroupDesc.getPropertyOverrides();
/* 1373 */     if (localProperties != null) {
/* 1374 */       Enumeration localEnumeration = localProperties.propertyNames();
/* 1375 */       while (localEnumeration.hasMoreElements())
/*      */       {
/* 1377 */         String str = (String)localEnumeration.nextElement();
/*      */ 
/* 1383 */         localArrayList.add("-D" + str + "=" + localProperties.getProperty(str));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1390 */     for (int i = 1; i < this.command.length; i++) {
/* 1391 */       localArrayList.add(this.command[i]);
/*      */     }
/*      */ 
/* 1394 */     String[] arrayOfString = new String[localArrayList.size()];
/* 1395 */     System.arraycopy(localArrayList.toArray(), 0, arrayOfString, 0, arrayOfString.length);
/*      */ 
/* 1397 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private void checkArgs(ActivationGroupDesc paramActivationGroupDesc, String[] paramArrayOfString)
/*      */     throws SecurityException, ActivationException
/*      */   {
/* 1406 */     if (execPolicyMethod != null) {
/* 1407 */       if (paramArrayOfString == null)
/* 1408 */         paramArrayOfString = activationArgs(paramActivationGroupDesc);
/*      */       try
/*      */       {
/* 1411 */         execPolicyMethod.invoke(execPolicy, new Object[] { paramActivationGroupDesc, paramArrayOfString });
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1413 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1414 */         if ((localThrowable instanceof SecurityException)) {
/* 1415 */           throw ((SecurityException)localThrowable);
/*      */         }
/* 1417 */         throw new ActivationException(execPolicyMethod.getName() + ": unexpected exception", localInvocationTargetException);
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 1422 */         throw new ActivationException(execPolicyMethod.getName() + ": unexpected exception", localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addLogRecord(LogRecord paramLogRecord)
/*      */     throws ActivationException
/*      */   {
/* 1474 */     synchronized (this.log) {
/* 1475 */       checkShutdown();
/*      */       try {
/* 1477 */         this.log.update(paramLogRecord, true);
/*      */       } catch (Exception localException1) {
/* 1479 */         this.numUpdates = snapshotInterval;
/* 1480 */         System.err.println(getTextResource("rmid.log.update.warning"));
/* 1481 */         localException1.printStackTrace();
/*      */       }
/* 1483 */       if (++this.numUpdates < snapshotInterval)
/* 1484 */         return;
/*      */       try
/*      */       {
/* 1487 */         this.log.snapshot(this);
/* 1488 */         this.numUpdates = 0;
/*      */       } catch (Exception localException2) {
/* 1490 */         System.err.println(getTextResource("rmid.log.snapshot.warning"));
/*      */ 
/* 1492 */         localException2.printStackTrace();
/*      */         try
/*      */         {
/* 1495 */           this.system.shutdown();
/*      */         }
/*      */         catch (RemoteException localRemoteException)
/*      */         {
/*      */         }
/* 1500 */         throw new ActivationException("log snapshot failed", localException2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initCommand(String[] paramArrayOfString)
/*      */   {
/* 1741 */     this.command = new String[paramArrayOfString.length + 2];
/* 1742 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Void run() {
/*      */         try {
/* 1745 */           Activation.this.command[0] = (System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
/*      */         }
/*      */         catch (Exception localException) {
/* 1748 */           System.err.println(Activation.getTextResource("rmid.unfound.java.home.property"));
/*      */ 
/* 1750 */           Activation.this.command[0] = "java";
/*      */         }
/* 1752 */         return null;
/*      */       }
/*      */     });
/* 1755 */     System.arraycopy(paramArrayOfString, 0, this.command, 1, paramArrayOfString.length);
/* 1756 */     this.command[(this.command.length - 1)] = "sun.rmi.server.ActivationGroupInit";
/*      */   }
/*      */ 
/*      */   private static void bomb(String paramString) {
/* 1760 */     System.err.println("rmid: " + paramString);
/* 1761 */     System.err.println(MessageFormat.format(getTextResource("rmid.usage"), new Object[] { "rmid" }));
/*      */ 
/* 1763 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   public static void main(String[] paramArrayOfString)
/*      */   {
/* 1910 */     int i = 0;
/*      */ 
/* 1914 */     if (System.getSecurityManager() == null) {
/* 1915 */       System.setSecurityManager(new SecurityManager());
/*      */     }
/*      */     try
/*      */     {
/* 1919 */       Exception localException1 = 1098;
/* 1920 */       ActivationServerSocketFactory localActivationServerSocketFactory = null;
/*      */ 
/* 1927 */       Channel localChannel = (Channel)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Channel run() throws IOException {
/* 1930 */           return System.inheritedChannel();
/*      */         }
/*      */       });
/* 1934 */       if ((localChannel != null) && ((localChannel instanceof ServerSocketChannel)))
/*      */       {
/* 1940 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Void run() throws IOException {
/* 1943 */             File localFile = Files.createTempFile("rmid-err", null, new FileAttribute[0]).toFile();
/*      */ 
/* 1945 */             PrintStream localPrintStream = new PrintStream(new FileOutputStream(localFile));
/*      */ 
/* 1947 */             System.setErr(localPrintStream);
/* 1948 */             return null;
/*      */           }
/*      */         });
/* 1952 */         localObject = ((ServerSocketChannel)localChannel).socket();
/*      */ 
/* 1954 */         localException1 = ((ServerSocket)localObject).getLocalPort();
/* 1955 */         localActivationServerSocketFactory = new ActivationServerSocketFactory((ServerSocket)localObject);
/*      */ 
/* 1957 */         System.err.println(new Date());
/* 1958 */         System.err.println(getTextResource("rmid.inherited.channel.info") + ": " + localChannel);
/*      */       }
/*      */ 
/* 1963 */       Object localObject = null;
/* 1964 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1969 */       for (int j = 0; j < paramArrayOfString.length; j++) {
/* 1970 */         if (paramArrayOfString[j].equals("-port")) {
/* 1971 */           if (localActivationServerSocketFactory != null) {
/* 1972 */             bomb(getTextResource("rmid.syntax.port.badarg"));
/*      */           }
/* 1974 */           if (j + 1 < paramArrayOfString.length)
/*      */             try {
/* 1976 */               localException1 = Integer.parseInt(paramArrayOfString[(++j)]);
/*      */             } catch (NumberFormatException localNumberFormatException) {
/* 1978 */               bomb(getTextResource("rmid.syntax.port.badnumber"));
/*      */             }
/*      */           else {
/* 1981 */             bomb(getTextResource("rmid.syntax.port.missing"));
/*      */           }
/*      */         }
/* 1984 */         else if (paramArrayOfString[j].equals("-log")) {
/* 1985 */           if (j + 1 < paramArrayOfString.length)
/* 1986 */             localObject = paramArrayOfString[(++j)];
/*      */           else {
/* 1988 */             bomb(getTextResource("rmid.syntax.log.missing"));
/*      */           }
/*      */         }
/* 1991 */         else if (paramArrayOfString[j].equals("-stop")) {
/* 1992 */           i = 1;
/*      */         }
/* 1994 */         else if (paramArrayOfString[j].startsWith("-C")) {
/* 1995 */           localArrayList.add(paramArrayOfString[j].substring(2));
/*      */         }
/*      */         else {
/* 1998 */           bomb(MessageFormat.format(getTextResource("rmid.syntax.illegal.option"), new Object[] { paramArrayOfString[j] }));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2004 */       if (localObject == null) {
/* 2005 */         if (localActivationServerSocketFactory != null)
/* 2006 */           bomb(getTextResource("rmid.syntax.log.required"));
/*      */         else {
/* 2008 */           localObject = "log";
/*      */         }
/*      */       }
/*      */ 
/* 2012 */       debugExec = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.activation.debugExec"))).booleanValue();
/*      */ 
/* 2018 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.activation.execPolicy", null));
/*      */ 
/* 2020 */       if (str == null) {
/* 2021 */         if (i == 0) {
/* 2022 */           DefaultExecPolicy.checkConfiguration();
/*      */         }
/* 2024 */         str = "default";
/*      */       }
/*      */ 
/* 2030 */       if (!str.equals("none")) {
/* 2031 */         if ((str.equals("")) || (str.equals("default")))
/*      */         {
/* 2034 */           str = DefaultExecPolicy.class.getName();
/*      */         }
/*      */         try
/*      */         {
/* 2038 */           Class localClass = getRMIClass(str);
/* 2039 */           execPolicy = localClass.newInstance();
/* 2040 */           execPolicyMethod = localClass.getMethod("checkExecCommand", new Class[] { ActivationGroupDesc.class, [Ljava.lang.String.class });
/*      */         }
/*      */         catch (Exception localException3)
/*      */         {
/* 2045 */           if (debugExec) {
/* 2046 */             System.err.println(getTextResource("rmid.exec.policy.exception"));
/*      */ 
/* 2048 */             localException3.printStackTrace();
/*      */           }
/* 2050 */           bomb(getTextResource("rmid.exec.policy.invalid"));
/*      */         }
/*      */       }
/*      */ 
/* 2054 */       if (i == 1) {
/* 2055 */         localException3 = localException1;
/* 2056 */         AccessController.doPrivileged(new PrivilegedAction() {
/*      */           public Void run() {
/* 2058 */             System.setProperty("java.rmi.activation.port", Integer.toString(this.val$finalPort));
/*      */ 
/* 2060 */             return null;
/*      */           }
/*      */         });
/* 2063 */         ActivationSystem localActivationSystem = ActivationGroup.getSystem();
/* 2064 */         localActivationSystem.shutdown();
/* 2065 */         System.exit(0);
/*      */       }
/*      */ 
/* 2081 */       startActivation(localException1, localActivationServerSocketFactory, (String)localObject, (String[])localArrayList.toArray(new String[localArrayList.size()]));
/*      */       while (true)
/*      */       {
/*      */         try
/*      */         {
/* 2087 */           Thread.sleep(9223372036854775807L);
/*      */         } catch (InterruptedException localInterruptedException) {
/*      */         }
/*      */       }
/*      */     } catch (Exception localException2) {
/* 2092 */       System.err.println(MessageFormat.format(getTextResource("rmid.unexpected.exception"), new Object[] { localException2 }));
/*      */ 
/* 2095 */       localException2.printStackTrace();
/*      */ 
/* 2097 */       System.exit(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String getTextResource(String paramString)
/*      */   {
/* 2104 */     if (resources == null) {
/*      */       try {
/* 2106 */         resources = ResourceBundle.getBundle("sun.rmi.server.resources.rmid");
/*      */       }
/*      */       catch (MissingResourceException localMissingResourceException1) {
/*      */       }
/* 2110 */       if (resources == null)
/*      */       {
/* 2112 */         return "[missing resource file: " + paramString + "]";
/*      */       }
/*      */     }
/*      */ 
/* 2116 */     String str = null;
/*      */     try {
/* 2118 */       str = resources.getString(paramString);
/*      */     }
/*      */     catch (MissingResourceException localMissingResourceException2) {
/*      */     }
/* 2122 */     if (str == null) {
/* 2123 */       return "[missing resource: " + paramString + "]";
/*      */     }
/* 2125 */     return str;
/*      */   }
/*      */ 
/*      */   private static Class<?> getRMIClass(String paramString)
/*      */     throws Exception
/*      */   {
/* 2131 */     return RMIClassLoader.loadClass(paramString);
/*      */   }
/*      */ 
/*      */   private synchronized String Pstartgroup()
/*      */     throws ActivationException
/*      */   {
/*      */     while (true)
/*      */     {
/* 2146 */       checkShutdown();
/*      */ 
/* 2148 */       if (this.groupSemaphore > 0) {
/* 2149 */         this.groupSemaphore -= 1;
/* 2150 */         return "Group-" + this.groupCounter++;
/*      */       }
/*      */       try
/*      */       {
/* 2154 */         wait();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void Vstartgroup()
/*      */   {
/* 2167 */     this.groupSemaphore += 1;
/* 2168 */     notifyAll();
/*      */   }
/*      */ 
/*      */   private static class ActLogHandler extends LogHandler
/*      */   {
/*      */     public Object initialSnapshot()
/*      */     {
/* 1520 */       return new Activation(null);
/*      */     }
/*      */ 
/*      */     public Object applyUpdate(Object paramObject1, Object paramObject2)
/*      */       throws Exception
/*      */     {
/* 1526 */       return ((Activation.LogRecord)paramObject1).apply(paramObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   class ActivationMonitorImpl extends UnicastRemoteObject
/*      */     implements ActivationMonitor
/*      */   {
/*      */     private static final long serialVersionUID = -6214940464757948867L;
/*      */ 
/*      */     ActivationMonitorImpl(int paramRMIServerSocketFactory, RMIServerSocketFactory arg3)
/*      */       throws RemoteException
/*      */     {
/*  418 */       super(null, localRMIServerSocketFactory);
/*      */     }
/*      */ 
/*      */     public void inactiveObject(ActivationID paramActivationID) throws UnknownObjectException, RemoteException
/*      */     {
/*      */       try
/*      */       {
/*  425 */         Activation.this.checkShutdown();
/*      */       } catch (ActivationException localActivationException) {
/*  427 */         return;
/*      */       }
/*  429 */       RegistryImpl.checkAccess("Activator.inactiveObject");
/*  430 */       Activation.this.getGroupEntry(paramActivationID).inactiveObject(paramActivationID);
/*      */     }
/*      */ 
/*      */     public void activeObject(ActivationID paramActivationID, MarshalledObject<? extends Remote> paramMarshalledObject)
/*      */       throws UnknownObjectException, RemoteException
/*      */     {
/*      */       try
/*      */       {
/*  438 */         Activation.this.checkShutdown();
/*      */       } catch (ActivationException localActivationException) {
/*  440 */         return;
/*      */       }
/*  442 */       RegistryImpl.checkAccess("ActivationSystem.activeObject");
/*  443 */       Activation.this.getGroupEntry(paramActivationID).activeObject(paramActivationID, paramMarshalledObject);
/*      */     }
/*      */ 
/*      */     public void inactiveGroup(ActivationGroupID paramActivationGroupID, long paramLong)
/*      */       throws UnknownGroupException, RemoteException
/*      */     {
/*      */       try
/*      */       {
/*  451 */         Activation.this.checkShutdown();
/*      */       } catch (ActivationException localActivationException) {
/*  453 */         return;
/*      */       }
/*  455 */       RegistryImpl.checkAccess("ActivationMonitor.inactiveGroup");
/*  456 */       Activation.this.getGroupEntry(paramActivationGroupID).inactiveGroup(paramLong, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ActivationServerSocketFactory
/*      */     implements RMIServerSocketFactory
/*      */   {
/*      */     private final ServerSocket serverSocket;
/*      */ 
/*      */     ActivationServerSocketFactory(ServerSocket paramServerSocket)
/*      */     {
/* 2191 */       this.serverSocket = paramServerSocket;
/*      */     }
/*      */ 
/*      */     public ServerSocket createServerSocket(int paramInt)
/*      */       throws IOException
/*      */     {
/* 2201 */       return new Activation.DelayedAcceptServerSocket(this.serverSocket);
/*      */     }
/*      */   }
/*      */ 
/*      */   class ActivationSystemImpl extends RemoteServer
/*      */     implements ActivationSystem
/*      */   {
/*      */     private static final long serialVersionUID = 9100152600327688967L;
/*      */ 
/*      */     ActivationSystemImpl(int paramRMIServerSocketFactory, RMIServerSocketFactory arg3)
/*      */       throws RemoteException
/*      */     {
/*      */       RMIServerSocketFactory localRMIServerSocketFactory;
/*  477 */       LiveRef localLiveRef = new LiveRef(new ObjID(4), paramRMIServerSocketFactory, null, localRMIServerSocketFactory);
/*  478 */       UnicastServerRef localUnicastServerRef = new UnicastServerRef(localLiveRef);
/*  479 */       this.ref = localUnicastServerRef;
/*  480 */       localUnicastServerRef.exportObject(this, null);
/*      */     }
/*      */ 
/*      */     public ActivationID registerObject(ActivationDesc paramActivationDesc)
/*      */       throws ActivationException, UnknownGroupException, RemoteException
/*      */     {
/*  486 */       Activation.this.checkShutdown();
/*  487 */       RegistryImpl.checkAccess("ActivationSystem.registerObject");
/*      */ 
/*  489 */       ActivationGroupID localActivationGroupID = paramActivationDesc.getGroupID();
/*  490 */       ActivationID localActivationID = new ActivationID(Activation.this.activatorStub);
/*  491 */       Activation.this.getGroupEntry(localActivationGroupID).registerObject(localActivationID, paramActivationDesc, true);
/*  492 */       return localActivationID;
/*      */     }
/*      */ 
/*      */     public void unregisterObject(ActivationID paramActivationID)
/*      */       throws ActivationException, UnknownObjectException, RemoteException
/*      */     {
/*  498 */       Activation.this.checkShutdown();
/*  499 */       RegistryImpl.checkAccess("ActivationSystem.unregisterObject");
/*  500 */       Activation.this.getGroupEntry(paramActivationID).unregisterObject(paramActivationID, true);
/*      */     }
/*      */ 
/*      */     public ActivationGroupID registerGroup(ActivationGroupDesc paramActivationGroupDesc)
/*      */       throws ActivationException, RemoteException
/*      */     {
/*  506 */       Activation.this.checkShutdown();
/*  507 */       RegistryImpl.checkAccess("ActivationSystem.registerGroup");
/*  508 */       Activation.this.checkArgs(paramActivationGroupDesc, null);
/*      */ 
/*  510 */       ActivationGroupID localActivationGroupID = new ActivationGroupID(Activation.this.systemStub);
/*  511 */       Activation.GroupEntry localGroupEntry = new Activation.GroupEntry(Activation.this, localActivationGroupID, paramActivationGroupDesc);
/*      */ 
/*  513 */       Activation.this.groupTable.put(localActivationGroupID, localGroupEntry);
/*  514 */       Activation.this.addLogRecord(new Activation.LogRegisterGroup(localActivationGroupID, paramActivationGroupDesc));
/*  515 */       return localActivationGroupID;
/*      */     }
/*      */ 
/*      */     public ActivationMonitor activeGroup(ActivationGroupID paramActivationGroupID, ActivationInstantiator paramActivationInstantiator, long paramLong)
/*      */       throws ActivationException, UnknownGroupException, RemoteException
/*      */     {
/*  523 */       Activation.this.checkShutdown();
/*  524 */       RegistryImpl.checkAccess("ActivationSystem.activeGroup");
/*      */ 
/*  526 */       Activation.this.getGroupEntry(paramActivationGroupID).activeGroup(paramActivationInstantiator, paramLong);
/*  527 */       return Activation.this.monitor;
/*      */     }
/*      */ 
/*      */     public void unregisterGroup(ActivationGroupID paramActivationGroupID)
/*      */       throws ActivationException, UnknownGroupException, RemoteException
/*      */     {
/*  533 */       Activation.this.checkShutdown();
/*  534 */       RegistryImpl.checkAccess("ActivationSystem.unregisterGroup");
/*      */ 
/*  538 */       Activation.this.removeGroupEntry(paramActivationGroupID).unregisterGroup(true);
/*      */     }
/*      */ 
/*      */     public ActivationDesc setActivationDesc(ActivationID paramActivationID, ActivationDesc paramActivationDesc)
/*      */       throws ActivationException, UnknownObjectException, RemoteException
/*      */     {
/*  545 */       Activation.this.checkShutdown();
/*  546 */       RegistryImpl.checkAccess("ActivationSystem.setActivationDesc");
/*      */ 
/*  548 */       if (!Activation.this.getGroupID(paramActivationID).equals(paramActivationDesc.getGroupID())) {
/*  549 */         throw new ActivationException("ActivationDesc contains wrong group");
/*      */       }
/*      */ 
/*  552 */       return Activation.this.getGroupEntry(paramActivationID).setActivationDesc(paramActivationID, paramActivationDesc, true);
/*      */     }
/*      */ 
/*      */     public ActivationGroupDesc setActivationGroupDesc(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc)
/*      */       throws ActivationException, UnknownGroupException, RemoteException
/*      */     {
/*  559 */       Activation.this.checkShutdown();
/*  560 */       RegistryImpl.checkAccess("ActivationSystem.setActivationGroupDesc");
/*      */ 
/*  563 */       Activation.this.checkArgs(paramActivationGroupDesc, null);
/*  564 */       return Activation.this.getGroupEntry(paramActivationGroupID).setActivationGroupDesc(paramActivationGroupID, paramActivationGroupDesc, true);
/*      */     }
/*      */ 
/*      */     public ActivationDesc getActivationDesc(ActivationID paramActivationID)
/*      */       throws ActivationException, UnknownObjectException, RemoteException
/*      */     {
/*  570 */       Activation.this.checkShutdown();
/*  571 */       RegistryImpl.checkAccess("ActivationSystem.getActivationDesc");
/*      */ 
/*  573 */       return Activation.this.getGroupEntry(paramActivationID).getActivationDesc(paramActivationID);
/*      */     }
/*      */ 
/*      */     public ActivationGroupDesc getActivationGroupDesc(ActivationGroupID paramActivationGroupID)
/*      */       throws ActivationException, UnknownGroupException, RemoteException
/*      */     {
/*  579 */       Activation.this.checkShutdown();
/*  580 */       RegistryImpl.checkAccess("ActivationSystem.getActivationGroupDesc");
/*      */ 
/*  583 */       return Activation.this.getGroupEntry(paramActivationGroupID).desc;
/*      */     }
/*      */ 
/*      */     public void shutdown()
/*      */       throws AccessException
/*      */     {
/*  591 */       RegistryImpl.checkAccess("ActivationSystem.shutdown");
/*      */ 
/*  593 */       Object localObject1 = Activation.this.startupLock;
/*  594 */       if (localObject1 != null) {
/*  595 */         synchronized (localObject1)
/*      */         {
/*      */         }
/*      */       }
/*      */ 
/*  600 */       synchronized (Activation.this) {
/*  601 */         if (!Activation.this.shuttingDown) {
/*  602 */           Activation.this.shuttingDown = true;
/*  603 */           new Activation.Shutdown(Activation.this).start();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class ActivatorImpl extends RemoteServer
/*      */     implements Activator
/*      */   {
/*      */     private static final long serialVersionUID = -3654244726254566136L;
/*      */ 
/*      */     ActivatorImpl(int paramRMIServerSocketFactory, RMIServerSocketFactory arg3)
/*      */       throws RemoteException
/*      */     {
/*      */       RMIServerSocketFactory localRMIServerSocketFactory;
/*  394 */       LiveRef localLiveRef = new LiveRef(new ObjID(1), paramRMIServerSocketFactory, null, localRMIServerSocketFactory);
/*      */ 
/*  396 */       UnicastServerRef localUnicastServerRef = new UnicastServerRef(localLiveRef);
/*  397 */       this.ref = localUnicastServerRef;
/*  398 */       localUnicastServerRef.exportObject(this, null, false);
/*      */     }
/*      */ 
/*      */     public MarshalledObject<? extends Remote> activate(ActivationID paramActivationID, boolean paramBoolean)
/*      */       throws ActivationException, UnknownObjectException, RemoteException
/*      */     {
/*  405 */       Activation.this.checkShutdown();
/*  406 */       return Activation.this.getGroupEntry(paramActivationID).activate(paramActivationID, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DefaultExecPolicy
/*      */   {
/*      */     public void checkExecCommand(ActivationGroupDesc paramActivationGroupDesc, String[] paramArrayOfString)
/*      */       throws SecurityException
/*      */     {
/* 1776 */       PermissionCollection localPermissionCollection = getExecPermissions();
/*      */ 
/* 1781 */       Properties localProperties = paramActivationGroupDesc.getPropertyOverrides();
/*      */       String str1;
/*      */       Object localObject3;
/* 1782 */       if (localProperties != null) {
/* 1783 */         localObject1 = localProperties.propertyNames();
/* 1784 */         while (((Enumeration)localObject1).hasMoreElements()) {
/* 1785 */           localObject2 = (String)((Enumeration)localObject1).nextElement();
/* 1786 */           str1 = localProperties.getProperty((String)localObject2);
/* 1787 */           localObject3 = "-D" + (String)localObject2 + "=" + str1;
/*      */           try {
/* 1789 */             checkPermission(localPermissionCollection, new ExecOptionPermission((String)localObject3));
/*      */           }
/*      */           catch (AccessControlException localAccessControlException) {
/* 1792 */             if (str1.equals("")) {
/* 1793 */               checkPermission(localPermissionCollection, new ExecOptionPermission("-D" + (String)localObject2));
/*      */             }
/*      */             else {
/* 1796 */               throw localAccessControlException;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1806 */       Object localObject1 = paramActivationGroupDesc.getClassName();
/* 1807 */       if (((localObject1 != null) && (!((String)localObject1).equals(ActivationGroupImpl.class.getName()))) || (paramActivationGroupDesc.getLocation() != null) || (paramActivationGroupDesc.getData() != null))
/*      */       {
/* 1813 */         throw new AccessControlException("access denied (custom group implementation not allowed)");
/*      */       }
/*      */ 
/* 1822 */       Object localObject2 = paramActivationGroupDesc.getCommandEnvironment();
/* 1823 */       if (localObject2 != null) {
/* 1824 */         str1 = ((ActivationGroupDesc.CommandEnvironment)localObject2).getCommandPath();
/* 1825 */         if (str1 != null) {
/* 1826 */           checkPermission(localPermissionCollection, new ExecPermission(str1));
/*      */         }
/*      */ 
/* 1829 */         localObject3 = ((ActivationGroupDesc.CommandEnvironment)localObject2).getCommandOptions();
/* 1830 */         if (localObject3 != null)
/* 1831 */           for (String str2 : localObject3)
/* 1832 */             checkPermission(localPermissionCollection, new ExecOptionPermission(str2));
/*      */       }
/*      */     }
/*      */ 
/*      */     static void checkConfiguration()
/*      */     {
/* 1845 */       Policy localPolicy = (Policy)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Policy run() {
/* 1848 */           return Policy.getPolicy();
/*      */         }
/*      */       });
/* 1851 */       if (!(localPolicy instanceof PolicyFile)) {
/* 1852 */         return;
/*      */       }
/* 1854 */       PermissionCollection localPermissionCollection = getExecPermissions();
/* 1855 */       Enumeration localEnumeration = localPermissionCollection.elements();
/* 1856 */       while (localEnumeration.hasMoreElements())
/*      */       {
/* 1858 */         Permission localPermission = (Permission)localEnumeration.nextElement();
/* 1859 */         if (((localPermission instanceof AllPermission)) || ((localPermission instanceof ExecPermission)) || ((localPermission instanceof ExecOptionPermission)))
/*      */         {
/* 1863 */           return;
/*      */         }
/*      */       }
/* 1866 */       System.err.println(Activation.getTextResource("rmid.exec.perms.inadequate"));
/*      */     }
/*      */ 
/*      */     private static PermissionCollection getExecPermissions()
/*      */     {
/* 1877 */       PermissionCollection localPermissionCollection = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public PermissionCollection run() {
/* 1880 */           CodeSource localCodeSource = new CodeSource(null, (Certificate[])null);
/*      */ 
/* 1882 */           Policy localPolicy = Policy.getPolicy();
/* 1883 */           if (localPolicy != null) {
/* 1884 */             return localPolicy.getPermissions(localCodeSource);
/*      */           }
/* 1886 */           return new Permissions();
/*      */         }
/*      */       });
/* 1891 */       return localPermissionCollection;
/*      */     }
/*      */ 
/*      */     private static void checkPermission(PermissionCollection paramPermissionCollection, Permission paramPermission)
/*      */       throws AccessControlException
/*      */     {
/* 1898 */       if (!paramPermissionCollection.implies(paramPermission))
/* 1899 */         throw new AccessControlException("access denied " + paramPermission.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DelayedAcceptServerSocket extends ServerSocket
/*      */   {
/*      */     private final ServerSocket serverSocket;
/*      */ 
/*      */     DelayedAcceptServerSocket(ServerSocket paramServerSocket)
/*      */       throws IOException
/*      */     {
/* 2219 */       this.serverSocket = paramServerSocket;
/*      */     }
/*      */ 
/*      */     public void bind(SocketAddress paramSocketAddress) throws IOException {
/* 2223 */       this.serverSocket.bind(paramSocketAddress);
/*      */     }
/*      */ 
/*      */     public void bind(SocketAddress paramSocketAddress, int paramInt)
/*      */       throws IOException
/*      */     {
/* 2229 */       this.serverSocket.bind(paramSocketAddress, paramInt);
/*      */     }
/*      */ 
/*      */     public InetAddress getInetAddress() {
/* 2233 */       return (InetAddress)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public InetAddress run()
/*      */         {
/* 2237 */           return Activation.DelayedAcceptServerSocket.this.serverSocket.getInetAddress();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public int getLocalPort() {
/* 2243 */       return this.serverSocket.getLocalPort();
/*      */     }
/*      */ 
/*      */     public SocketAddress getLocalSocketAddress() {
/* 2247 */       return (SocketAddress)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public SocketAddress run()
/*      */         {
/* 2251 */           return Activation.DelayedAcceptServerSocket.this.serverSocket.getLocalSocketAddress();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public Socket accept()
/*      */       throws IOException
/*      */     {
/* 2261 */       synchronized (Activation.initLock) {
/*      */         try {
/* 2263 */           while (!Activation.initDone)
/* 2264 */             Activation.initLock.wait();
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {
/* 2267 */           throw new AssertionError(localInterruptedException);
/*      */         }
/*      */       }
/* 2270 */       return this.serverSocket.accept();
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 2274 */       this.serverSocket.close();
/*      */     }
/*      */ 
/*      */     public ServerSocketChannel getChannel() {
/* 2278 */       return this.serverSocket.getChannel();
/*      */     }
/*      */ 
/*      */     public boolean isBound() {
/* 2282 */       return this.serverSocket.isBound();
/*      */     }
/*      */ 
/*      */     public boolean isClosed() {
/* 2286 */       return this.serverSocket.isClosed();
/*      */     }
/*      */ 
/*      */     public void setSoTimeout(int paramInt)
/*      */       throws SocketException
/*      */     {
/* 2292 */       this.serverSocket.setSoTimeout(paramInt);
/*      */     }
/*      */ 
/*      */     public int getSoTimeout() throws IOException {
/* 2296 */       return this.serverSocket.getSoTimeout();
/*      */     }
/*      */ 
/*      */     public void setReuseAddress(boolean paramBoolean) throws SocketException {
/* 2300 */       this.serverSocket.setReuseAddress(paramBoolean);
/*      */     }
/*      */ 
/*      */     public boolean getReuseAddress() throws SocketException {
/* 2304 */       return this.serverSocket.getReuseAddress();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 2308 */       return this.serverSocket.toString();
/*      */     }
/*      */ 
/*      */     public void setReceiveBufferSize(int paramInt)
/*      */       throws SocketException
/*      */     {
/* 2314 */       this.serverSocket.setReceiveBufferSize(paramInt);
/*      */     }
/*      */ 
/*      */     public int getReceiveBufferSize()
/*      */       throws SocketException
/*      */     {
/* 2320 */       return this.serverSocket.getReceiveBufferSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class GroupEntry
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7222464070032993304L;
/*      */     private static final int MAX_TRIES = 2;
/*      */     private static final int NORMAL = 0;
/*      */     private static final int CREATING = 1;
/*      */     private static final int TERMINATE = 2;
/*      */     private static final int TERMINATING = 3;
/*  805 */     ActivationGroupDesc desc = null;
/*  806 */     ActivationGroupID groupID = null;
/*  807 */     long incarnation = 0L;
/*  808 */     Map<ActivationID, Activation.ObjectEntry> objects = new HashMap();
/*  809 */     Set<ActivationID> restartSet = new HashSet();
/*      */ 
/*  811 */     transient ActivationInstantiator group = null;
/*  812 */     transient int status = 0;
/*  813 */     transient long waitTime = 0L;
/*  814 */     transient String groupName = null;
/*  815 */     transient Process child = null;
/*  816 */     transient boolean removed = false;
/*  817 */     transient Watchdog watchdog = null;
/*      */ 
/*      */     GroupEntry(ActivationGroupID paramActivationGroupDesc, ActivationGroupDesc arg3) {
/*  820 */       this.groupID = paramActivationGroupDesc;
/*      */       Object localObject;
/*  821 */       this.desc = localObject;
/*      */     }
/*      */ 
/*      */     void restartServices() {
/*  825 */       Iterator localIterator = null;
/*      */ 
/*  827 */       synchronized (this) {
/*  828 */         if (this.restartSet.isEmpty()) {
/*  829 */           return;
/*      */         }
/*      */ 
/*  838 */         localIterator = new HashSet(this.restartSet).iterator();
/*      */       }
/*      */ 
/*  841 */       while (localIterator.hasNext()) {
/*  842 */         ??? = (ActivationID)localIterator.next();
/*      */         try {
/*  844 */           activate((ActivationID)???, true);
/*      */         } catch (Exception localException) {
/*  846 */           if (Activation.this.shuttingDown) {
/*  847 */             return;
/*      */           }
/*  849 */           System.err.println(Activation.getTextResource("rmid.restart.service.warning"));
/*      */ 
/*  851 */           localException.printStackTrace();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void activeGroup(ActivationInstantiator paramActivationInstantiator, long paramLong)
/*      */       throws ActivationException, UnknownGroupException
/*      */     {
/*  860 */       if (this.incarnation != paramLong) {
/*  861 */         throw new ActivationException("invalid incarnation");
/*      */       }
/*      */ 
/*  864 */       if (this.group != null) {
/*  865 */         if (this.group.equals(paramActivationInstantiator)) {
/*  866 */           return;
/*      */         }
/*  868 */         throw new ActivationException("group already active");
/*      */       }
/*      */ 
/*  872 */       if ((this.child != null) && (this.status != 1)) {
/*  873 */         throw new ActivationException("group not being created");
/*      */       }
/*      */ 
/*  876 */       this.group = paramActivationInstantiator;
/*  877 */       this.status = 0;
/*  878 */       notifyAll();
/*      */     }
/*      */ 
/*      */     private void checkRemoved() throws UnknownGroupException {
/*  882 */       if (this.removed)
/*  883 */         throw new UnknownGroupException("group removed");
/*      */     }
/*      */ 
/*      */     private Activation.ObjectEntry getObjectEntry(ActivationID paramActivationID)
/*      */       throws UnknownObjectException
/*      */     {
/*  890 */       if (this.removed) {
/*  891 */         throw new UnknownObjectException("object's group removed");
/*      */       }
/*  893 */       Activation.ObjectEntry localObjectEntry = (Activation.ObjectEntry)this.objects.get(paramActivationID);
/*  894 */       if (localObjectEntry == null) {
/*  895 */         throw new UnknownObjectException("object unknown");
/*      */       }
/*  897 */       return localObjectEntry;
/*      */     }
/*      */ 
/*      */     synchronized void registerObject(ActivationID paramActivationID, ActivationDesc paramActivationDesc, boolean paramBoolean)
/*      */       throws UnknownGroupException, ActivationException
/*      */     {
/*  905 */       checkRemoved();
/*  906 */       this.objects.put(paramActivationID, new Activation.ObjectEntry(paramActivationDesc));
/*  907 */       if (paramActivationDesc.getRestartMode() == true) {
/*  908 */         this.restartSet.add(paramActivationID);
/*      */       }
/*      */ 
/*  912 */       Activation.this.idTable.put(paramActivationID, this.groupID);
/*      */ 
/*  914 */       if (paramBoolean)
/*  915 */         Activation.this.addLogRecord(new Activation.LogRegisterObject(paramActivationID, paramActivationDesc));
/*      */     }
/*      */ 
/*      */     synchronized void unregisterObject(ActivationID paramActivationID, boolean paramBoolean)
/*      */       throws UnknownGroupException, ActivationException
/*      */     {
/*  922 */       Activation.ObjectEntry localObjectEntry = getObjectEntry(paramActivationID);
/*  923 */       localObjectEntry.removed = true;
/*  924 */       this.objects.remove(paramActivationID);
/*  925 */       if (localObjectEntry.desc.getRestartMode() == true) {
/*  926 */         this.restartSet.remove(paramActivationID);
/*      */       }
/*      */ 
/*  930 */       Activation.this.idTable.remove(paramActivationID);
/*  931 */       if (paramBoolean)
/*  932 */         Activation.this.addLogRecord(new Activation.LogUnregisterObject(paramActivationID));
/*      */     }
/*      */ 
/*      */     synchronized void unregisterGroup(boolean paramBoolean)
/*      */       throws UnknownGroupException, ActivationException
/*      */     {
/*  939 */       checkRemoved();
/*  940 */       this.removed = true;
/*      */ 
/*  942 */       for (Map.Entry localEntry : this.objects.entrySet())
/*      */       {
/*  944 */         ActivationID localActivationID = (ActivationID)localEntry.getKey();
/*  945 */         Activation.this.idTable.remove(localActivationID);
/*  946 */         Activation.ObjectEntry localObjectEntry = (Activation.ObjectEntry)localEntry.getValue();
/*  947 */         localObjectEntry.removed = true;
/*      */       }
/*  949 */       this.objects.clear();
/*  950 */       this.restartSet.clear();
/*  951 */       reset();
/*  952 */       childGone();
/*      */ 
/*  955 */       if (paramBoolean)
/*  956 */         Activation.this.addLogRecord(new Activation.LogUnregisterGroup(this.groupID));
/*      */     }
/*      */ 
/*      */     synchronized ActivationDesc setActivationDesc(ActivationID paramActivationID, ActivationDesc paramActivationDesc, boolean paramBoolean)
/*      */       throws UnknownObjectException, UnknownGroupException, ActivationException
/*      */     {
/*  966 */       Activation.ObjectEntry localObjectEntry = getObjectEntry(paramActivationID);
/*  967 */       ActivationDesc localActivationDesc = localObjectEntry.desc;
/*  968 */       localObjectEntry.desc = paramActivationDesc;
/*  969 */       if (paramActivationDesc.getRestartMode() == true)
/*  970 */         this.restartSet.add(paramActivationID);
/*      */       else {
/*  972 */         this.restartSet.remove(paramActivationID);
/*      */       }
/*      */ 
/*  975 */       if (paramBoolean) {
/*  976 */         Activation.this.addLogRecord(new Activation.LogUpdateDesc(paramActivationID, paramActivationDesc));
/*      */       }
/*      */ 
/*  979 */       return localActivationDesc;
/*      */     }
/*      */ 
/*      */     synchronized ActivationDesc getActivationDesc(ActivationID paramActivationID)
/*      */       throws UnknownObjectException, UnknownGroupException
/*      */     {
/*  985 */       return getObjectEntry(paramActivationID).desc;
/*      */     }
/*      */ 
/*      */     synchronized ActivationGroupDesc setActivationGroupDesc(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc, boolean paramBoolean)
/*      */       throws UnknownGroupException, ActivationException
/*      */     {
/*  994 */       checkRemoved();
/*  995 */       ActivationGroupDesc localActivationGroupDesc = this.desc;
/*  996 */       this.desc = paramActivationGroupDesc;
/*      */ 
/*  998 */       if (paramBoolean) {
/*  999 */         Activation.this.addLogRecord(new Activation.LogUpdateGroupDesc(paramActivationGroupID, paramActivationGroupDesc));
/*      */       }
/* 1001 */       return localActivationGroupDesc;
/*      */     }
/*      */ 
/*      */     synchronized void inactiveGroup(long paramLong, boolean paramBoolean)
/*      */       throws UnknownGroupException
/*      */     {
/* 1007 */       checkRemoved();
/* 1008 */       if (this.incarnation != paramLong) {
/* 1009 */         throw new UnknownGroupException("invalid incarnation");
/*      */       }
/*      */ 
/* 1012 */       reset();
/* 1013 */       if (paramBoolean) {
/* 1014 */         terminate();
/* 1015 */       } else if ((this.child != null) && (this.status == 0)) {
/* 1016 */         this.status = 2;
/* 1017 */         this.watchdog.noRestart();
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void activeObject(ActivationID paramActivationID, MarshalledObject<? extends Remote> paramMarshalledObject)
/*      */       throws UnknownObjectException
/*      */     {
/* 1025 */       getObjectEntry(paramActivationID).stub = paramMarshalledObject;
/*      */     }
/*      */ 
/*      */     synchronized void inactiveObject(ActivationID paramActivationID)
/*      */       throws UnknownObjectException
/*      */     {
/* 1031 */       getObjectEntry(paramActivationID).reset();
/*      */     }
/*      */ 
/*      */     private synchronized void reset() {
/* 1035 */       this.group = null;
/* 1036 */       for (Activation.ObjectEntry localObjectEntry : this.objects.values())
/* 1037 */         localObjectEntry.reset();
/*      */     }
/*      */ 
/*      */     private void childGone()
/*      */     {
/* 1042 */       if (this.child != null) {
/* 1043 */         this.child = null;
/* 1044 */         this.watchdog.dispose();
/* 1045 */         this.watchdog = null;
/* 1046 */         this.status = 0;
/* 1047 */         notifyAll();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void terminate() {
/* 1052 */       if ((this.child != null) && (this.status != 3)) {
/* 1053 */         this.child.destroy();
/* 1054 */         this.status = 3;
/* 1055 */         this.waitTime = (System.currentTimeMillis() + Activation.groupTimeout);
/* 1056 */         notifyAll();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void await()
/*      */     {
/*      */       while (true)
/* 1067 */         switch (this.status) {
/*      */         case 0:
/* 1069 */           return;
/*      */         case 2:
/* 1071 */           terminate();
/*      */         case 3:
/*      */           try {
/* 1074 */             this.child.exitValue();
/*      */           } catch (IllegalThreadStateException localIllegalThreadStateException) {
/* 1076 */             long l = System.currentTimeMillis();
/* 1077 */             if (this.waitTime > l) {
/*      */               try {
/* 1079 */                 wait(this.waitTime - l);
/*      */               } catch (InterruptedException localInterruptedException2) {
/*      */               }
/* 1082 */               continue;
/*      */             }
/*      */           }
/*      */ 
/* 1086 */           childGone();
/* 1087 */           return;
/*      */         case 1:
/*      */           try {
/* 1090 */             wait();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException1)
/*      */           {
/*      */           }
/*      */         }
/*      */     }
/*      */ 
/*      */     void shutdownFast() {
/* 1099 */       Process localProcess = this.child;
/* 1100 */       if (localProcess != null)
/* 1101 */         localProcess.destroy();
/*      */     }
/*      */ 
/*      */     synchronized void shutdown()
/*      */     {
/* 1106 */       reset();
/* 1107 */       terminate();
/* 1108 */       await();
/*      */     }
/*      */ 
/*      */     MarshalledObject<? extends Remote> activate(ActivationID paramActivationID, boolean paramBoolean)
/*      */       throws ActivationException
/*      */     {
/* 1115 */       Object localObject1 = null;
/*      */ 
/* 1121 */       for (int i = 2; i > 0; i--)
/*      */       {
/*      */         Activation.ObjectEntry localObjectEntry;
/*      */         ActivationInstantiator localActivationInstantiator;
/*      */         long l;
/* 1127 */         synchronized (this) {
/* 1128 */           localObjectEntry = getObjectEntry(paramActivationID);
/*      */ 
/* 1130 */           if ((!paramBoolean) && (localObjectEntry.stub != null)) {
/* 1131 */             return localObjectEntry.stub;
/*      */           }
/* 1133 */           localActivationInstantiator = getInstantiator(this.groupID);
/* 1134 */           l = this.incarnation;
/*      */         }
/*      */ 
/* 1137 */         int j = 0;
/* 1138 */         boolean bool = false;
/*      */         try
/*      */         {
/* 1141 */           return localObjectEntry.activate(paramActivationID, paramBoolean, localActivationInstantiator);
/*      */         } catch (NoSuchObjectException localNoSuchObjectException) {
/* 1143 */           j = 1;
/* 1144 */           localObject1 = localNoSuchObjectException;
/*      */         } catch (ConnectException localConnectException) {
/* 1146 */           j = 1;
/* 1147 */           bool = true;
/* 1148 */           localObject1 = localConnectException;
/*      */         } catch (ConnectIOException localConnectIOException) {
/* 1150 */           j = 1;
/* 1151 */           bool = true;
/* 1152 */           localObject1 = localConnectIOException;
/*      */         } catch (InactiveGroupException localInactiveGroupException) {
/* 1154 */           j = 1;
/* 1155 */           localObject1 = localInactiveGroupException;
/*      */         }
/*      */         catch (RemoteException localRemoteException) {
/* 1158 */           if (localObject1 == null) {
/* 1159 */             localObject1 = localRemoteException;
/*      */           }
/*      */         }
/*      */ 
/* 1163 */         if (j != 0) {
/*      */           try
/*      */           {
/* 1166 */             System.err.println(MessageFormat.format(Activation.getTextResource("rmid.group.inactive"), new Object[] { localObject1.toString() }));
/*      */ 
/* 1170 */             localObject1.printStackTrace();
/* 1171 */             Activation.this.getGroupEntry(this.groupID).inactiveGroup(l, bool);
/*      */           }
/*      */           catch (UnknownGroupException localUnknownGroupException)
/*      */           {
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1184 */       throw new ActivationException("object activation failed after 2 tries", localObject1);
/*      */     }
/*      */ 
/*      */     private ActivationInstantiator getInstantiator(ActivationGroupID paramActivationGroupID)
/*      */       throws ActivationException
/*      */     {
/* 1196 */       assert (Thread.holdsLock(this));
/*      */ 
/* 1198 */       await();
/* 1199 */       if (this.group != null) {
/* 1200 */         return this.group;
/*      */       }
/* 1202 */       checkRemoved();
/* 1203 */       int i = 0;
/*      */       try
/*      */       {
/* 1206 */         this.groupName = Activation.this.Pstartgroup();
/* 1207 */         i = 1;
/* 1208 */         String[] arrayOfString = Activation.this.activationArgs(this.desc);
/* 1209 */         Activation.this.checkArgs(this.desc, arrayOfString);
/*      */         Object localObject1;
/* 1211 */         if (Activation.debugExec) {
/* 1212 */           localObject1 = new StringBuffer(arrayOfString[0]);
/*      */ 
/* 1214 */           for (int j = 1; j < arrayOfString.length; j++) {
/* 1215 */             ((StringBuffer)localObject1).append(' ');
/* 1216 */             ((StringBuffer)localObject1).append(arrayOfString[j]);
/*      */           }
/* 1218 */           System.err.println(MessageFormat.format(Activation.getTextResource("rmid.exec.command"), new Object[] { ((StringBuffer)localObject1).toString() }));
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1225 */           this.child = Runtime.getRuntime().exec(arrayOfString);
/* 1226 */           this.status = 1;
/* 1227 */           this.incarnation += 1L;
/* 1228 */           this.watchdog = new Watchdog();
/* 1229 */           this.watchdog.start();
/* 1230 */           Activation.this.addLogRecord(new Activation.LogGroupIncarnation(paramActivationGroupID, this.incarnation));
/*      */ 
/* 1233 */           PipeWriter.plugTogetherPair(this.child.getInputStream(), System.out, this.child.getErrorStream(), System.err);
/*      */ 
/* 1236 */           localObject1 = new MarshalOutputStream(this.child.getOutputStream()); Object localObject2 = null;
/*      */           try {
/* 1238 */             ((MarshalOutputStream)localObject1).writeObject(paramActivationGroupID);
/* 1239 */             ((MarshalOutputStream)localObject1).writeObject(this.desc);
/* 1240 */             ((MarshalOutputStream)localObject1).writeLong(this.incarnation);
/* 1241 */             ((MarshalOutputStream)localObject1).flush();
/*      */           }
/*      */           catch (Throwable localThrowable2)
/*      */           {
/* 1236 */             localObject2 = localThrowable2; throw localThrowable2;
/*      */           }
/*      */           finally
/*      */           {
/* 1242 */             if (localObject1 != null) if (localObject2 != null) try { ((MarshalOutputStream)localObject1).close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else ((MarshalOutputStream)localObject1).close(); 
/*      */           }
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/* 1246 */           terminate();
/* 1247 */           throw new ActivationException("unable to create activation group", localIOException);
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1252 */           long l1 = System.currentTimeMillis();
/* 1253 */           long l2 = l1 + Activation.execTimeout;
/*      */           do {
/* 1255 */             wait(l2 - l1);
/* 1256 */             if (this.group != null) {
/* 1257 */               return this.group;
/*      */             }
/* 1259 */             l1 = System.currentTimeMillis();
/* 1260 */             if (this.status != 1) break;  } while (l1 < l2);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {
/*      */         }
/* 1264 */         terminate();
/* 1265 */         throw new ActivationException(this.removed ? "activation group unregistered" : "timeout creating child process");
/*      */       }
/*      */       finally
/*      */       {
/* 1270 */         if (i != 0)
/* 1271 */           Activation.this.Vstartgroup();
/*      */       }
/*      */     }
/*      */ 
/*      */     private class Watchdog extends Thread
/*      */     {
/* 1280 */       private final Process groupProcess = Activation.GroupEntry.this.child;
/* 1281 */       private final long groupIncarnation = Activation.GroupEntry.this.incarnation;
/* 1282 */       private boolean canInterrupt = true;
/* 1283 */       private boolean shouldQuit = false;
/* 1284 */       private boolean shouldRestart = true;
/*      */ 
/*      */       Watchdog() {
/* 1287 */         super();
/* 1288 */         setDaemon(true);
/*      */       }
/*      */ 
/*      */       public void run()
/*      */       {
/* 1293 */         if (this.shouldQuit) {
/* 1294 */           return;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1301 */           this.groupProcess.waitFor();
/*      */         } catch (InterruptedException localInterruptedException) {
/* 1303 */           return;
/*      */         }
/*      */ 
/* 1306 */         int i = 0;
/* 1307 */         synchronized (Activation.GroupEntry.this) {
/* 1308 */           if (this.shouldQuit) {
/* 1309 */             return;
/*      */           }
/* 1311 */           this.canInterrupt = false;
/* 1312 */           interrupted();
/*      */ 
/* 1317 */           if (this.groupIncarnation == Activation.GroupEntry.this.incarnation) {
/* 1318 */             i = (this.shouldRestart) && (!Activation.this.shuttingDown) ? 1 : 0;
/* 1319 */             Activation.GroupEntry.this.reset();
/* 1320 */             Activation.GroupEntry.this.childGone();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1328 */         if (i != 0)
/* 1329 */           Activation.GroupEntry.this.restartServices();
/*      */       }
/*      */ 
/*      */       void dispose()
/*      */       {
/* 1339 */         this.shouldQuit = true;
/* 1340 */         if (this.canInterrupt)
/* 1341 */           interrupt();
/*      */       }
/*      */ 
/*      */       void noRestart()
/*      */       {
/* 1349 */         this.shouldRestart = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogGroupIncarnation extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = 4146872747377631897L;
/*      */     private ActivationGroupID id;
/*      */     private long inc;
/*      */ 
/*      */     LogGroupIncarnation(ActivationGroupID paramActivationGroupID, long paramLong)
/*      */     {
/* 1717 */       super();
/* 1718 */       this.id = paramActivationGroupID;
/* 1719 */       this.inc = paramLong;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/*      */       try {
/* 1724 */         Activation.GroupEntry localGroupEntry = ((Activation)paramObject).getGroupEntry(this.id);
/* 1725 */         localGroupEntry.incarnation = this.inc;
/*      */       } catch (Exception localException) {
/* 1727 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogGroupIncarnation" }));
/*      */ 
/* 1731 */         localException.printStackTrace();
/*      */       }
/* 1733 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract class LogRecord
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 8395140512322687529L;
/*      */ 
/*      */     abstract Object apply(Object paramObject)
/*      */       throws Exception;
/*      */   }
/*      */ 
/*      */   private static class LogRegisterGroup extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = -1966827458515403625L;
/*      */     private ActivationGroupID id;
/*      */     private ActivationGroupDesc desc;
/*      */ 
/*      */     LogRegisterGroup(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc)
/*      */     {
/* 1608 */       super();
/* 1609 */       this.id = paramActivationGroupID;
/* 1610 */       this.desc = paramActivationGroupDesc;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject)
/*      */     {
/*      */       Activation tmp19_16 = ((Activation)paramObject); tmp19_16.getClass(); ((Activation)paramObject).groupTable.put(this.id, new Activation.GroupEntry(tmp19_16, this.id, this.desc));
/*      */ 
/* 1618 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogRegisterObject extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = -6280336276146085143L;
/*      */     private ActivationID id;
/*      */     private ActivationDesc desc;
/*      */ 
/*      */     LogRegisterObject(ActivationID paramActivationID, ActivationDesc paramActivationDesc)
/*      */     {
/* 1552 */       super();
/* 1553 */       this.id = paramActivationID;
/* 1554 */       this.desc = paramActivationDesc;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/*      */       try {
/* 1559 */         ((Activation)paramObject).getGroupEntry(this.desc.getGroupID()).registerObject(this.id, this.desc, false);
/*      */       }
/*      */       catch (Exception localException) {
/* 1562 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogRegisterObject" }));
/*      */ 
/* 1566 */         localException.printStackTrace();
/*      */       }
/* 1568 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogUnregisterGroup extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = -3356306586522147344L;
/*      */     private ActivationGroupID id;
/*      */ 
/*      */     LogUnregisterGroup(ActivationGroupID paramActivationGroupID)
/*      */     {
/* 1689 */       super();
/* 1690 */       this.id = paramActivationGroupID;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/* 1694 */       Activation.GroupEntry localGroupEntry = (Activation.GroupEntry)((Activation)paramObject).groupTable.remove(this.id);
/*      */       try {
/* 1696 */         localGroupEntry.unregisterGroup(false);
/*      */       } catch (Exception localException) {
/* 1698 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogUnregisterGroup" }));
/*      */ 
/* 1702 */         localException.printStackTrace();
/*      */       }
/* 1704 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogUnregisterObject extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = 6269824097396935501L;
/*      */     private ActivationID id;
/*      */ 
/*      */     LogUnregisterObject(ActivationID paramActivationID)
/*      */     {
/* 1580 */       super();
/* 1581 */       this.id = paramActivationID;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/*      */       try {
/* 1586 */         ((Activation)paramObject).getGroupEntry(this.id).unregisterObject(this.id, false);
/*      */       }
/*      */       catch (Exception localException) {
/* 1589 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogUnregisterObject" }));
/*      */ 
/* 1593 */         localException.printStackTrace();
/*      */       }
/* 1595 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogUpdateDesc extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = 545511539051179885L;
/*      */     private ActivationID id;
/*      */     private ActivationDesc desc;
/*      */ 
/*      */     LogUpdateDesc(ActivationID paramActivationID, ActivationDesc paramActivationDesc)
/*      */     {
/* 1632 */       super();
/* 1633 */       this.id = paramActivationID;
/* 1634 */       this.desc = paramActivationDesc;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/*      */       try {
/* 1639 */         ((Activation)paramObject).getGroupEntry(this.id).setActivationDesc(this.id, this.desc, false);
/*      */       }
/*      */       catch (Exception localException) {
/* 1642 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogUpdateDesc" }));
/*      */ 
/* 1646 */         localException.printStackTrace();
/*      */       }
/* 1648 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogUpdateGroupDesc extends Activation.LogRecord
/*      */   {
/*      */     private static final long serialVersionUID = -1271300989218424337L;
/*      */     private ActivationGroupID id;
/*      */     private ActivationGroupDesc desc;
/*      */ 
/*      */     LogUpdateGroupDesc(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc) {
/* 1661 */       super();
/* 1662 */       this.id = paramActivationGroupID;
/* 1663 */       this.desc = paramActivationGroupDesc;
/*      */     }
/*      */ 
/*      */     Object apply(Object paramObject) {
/*      */       try {
/* 1668 */         ((Activation)paramObject).getGroupEntry(this.id).setActivationGroupDesc(this.id, this.desc, false);
/*      */       }
/*      */       catch (Exception localException) {
/* 1671 */         System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), new Object[] { "LogUpdateGroupDesc" }));
/*      */ 
/* 1675 */         localException.printStackTrace();
/*      */       }
/* 1677 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ObjectEntry
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -5500114225321357856L;
/*      */     ActivationDesc desc;
/* 1435 */     volatile transient MarshalledObject<? extends Remote> stub = null;
/* 1436 */     volatile transient boolean removed = false;
/*      */ 
/*      */     ObjectEntry(ActivationDesc paramActivationDesc) {
/* 1439 */       this.desc = paramActivationDesc;
/*      */     }
/*      */ 
/*      */     synchronized MarshalledObject<? extends Remote> activate(ActivationID paramActivationID, boolean paramBoolean, ActivationInstantiator paramActivationInstantiator)
/*      */       throws RemoteException, ActivationException
/*      */     {
/* 1448 */       MarshalledObject localMarshalledObject = this.stub;
/* 1449 */       if (this.removed)
/* 1450 */         throw new UnknownObjectException("object removed");
/* 1451 */       if ((!paramBoolean) && (localMarshalledObject != null)) {
/* 1452 */         return localMarshalledObject;
/*      */       }
/*      */ 
/* 1455 */       localMarshalledObject = paramActivationInstantiator.newInstance(paramActivationID, this.desc);
/* 1456 */       this.stub = localMarshalledObject;
/*      */ 
/* 1461 */       return localMarshalledObject;
/*      */     }
/*      */ 
/*      */     void reset() {
/* 1465 */       this.stub = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Shutdown extends Thread
/*      */   {
/*      */     Shutdown()
/*      */     {
/*  644 */       super();
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*      */       try
/*      */       {
/*  652 */         Activation.unexport(Activation.this.activator);
/*  653 */         Activation.unexport(Activation.this.system);
/*      */ 
/*  656 */         for (Activation.GroupEntry localGroupEntry : Activation.this.groupTable.values()) {
/*  657 */           localGroupEntry.shutdown();
/*      */         }
/*      */ 
/*  660 */         Runtime.getRuntime().removeShutdownHook(Activation.this.shutdownHook);
/*      */ 
/*  665 */         Activation.unexport(Activation.this.monitor);
/*      */         try
/*      */         {
/*  676 */           synchronized (Activation.this.log) {
/*  677 */             Activation.this.log.close();
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  689 */         System.err.println(Activation.getTextResource("rmid.daemon.shutdown"));
/*  690 */         System.exit(0);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ShutdownHook extends Thread
/*      */   {
/*      */     ShutdownHook() {
/*  698 */       super();
/*      */     }
/*      */ 
/*      */     public void run() {
/*  702 */       synchronized (Activation.this) {
/*  703 */         Activation.this.shuttingDown = true;
/*      */       }
/*      */ 
/*  707 */       for (??? = Activation.this.groupTable.values().iterator(); ((Iterator)???).hasNext(); ) { Activation.GroupEntry localGroupEntry = (Activation.GroupEntry)((Iterator)???).next();
/*  708 */         localGroupEntry.shutdownFast();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SystemRegistryImpl extends RegistryImpl
/*      */   {
/*  300 */     private static final String NAME = ActivationSystem.class.getName();
/*      */     private static final long serialVersionUID = 4877330021609408794L;
/*      */     private final ActivationSystem systemStub;
/*      */ 
/*      */     SystemRegistryImpl(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory, ActivationSystem paramActivationSystem)
/*      */       throws RemoteException
/*      */     {
/*  310 */       super(paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*  311 */       this.systemStub = paramActivationSystem;
/*      */     }
/*      */ 
/*      */     public Remote lookup(String paramString)
/*      */       throws RemoteException, NotBoundException
/*      */     {
/*  323 */       if (paramString.equals(NAME)) {
/*  324 */         return this.systemStub;
/*      */       }
/*  326 */       return super.lookup(paramString);
/*      */     }
/*      */ 
/*      */     public String[] list() throws RemoteException
/*      */     {
/*  331 */       String[] arrayOfString1 = super.list();
/*  332 */       int i = arrayOfString1.length;
/*  333 */       String[] arrayOfString2 = new String[i + 1];
/*  334 */       if (i > 0) {
/*  335 */         System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, i);
/*      */       }
/*  337 */       arrayOfString2[i] = NAME;
/*  338 */       return arrayOfString2;
/*      */     }
/*      */ 
/*      */     public void bind(String paramString, Remote paramRemote)
/*      */       throws RemoteException, AlreadyBoundException, AccessException
/*      */     {
/*  344 */       if (paramString.equals(NAME)) {
/*  345 */         throw new AccessException("binding ActivationSystem is disallowed");
/*      */       }
/*      */ 
/*  348 */       super.bind(paramString, paramRemote);
/*      */     }
/*      */ 
/*      */     public void unbind(String paramString)
/*      */       throws RemoteException, NotBoundException, AccessException
/*      */     {
/*  355 */       if (paramString.equals(NAME)) {
/*  356 */         throw new AccessException("unbinding ActivationSystem is disallowed");
/*      */       }
/*      */ 
/*  359 */       super.unbind(paramString);
/*      */     }
/*      */ 
/*      */     public void rebind(String paramString, Remote paramRemote)
/*      */       throws RemoteException, AccessException
/*      */     {
/*  367 */       if (paramString.equals(NAME)) {
/*  368 */         throw new AccessException("binding ActivationSystem is disallowed");
/*      */       }
/*      */ 
/*  371 */       super.rebind(paramString, paramRemote);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.Activation
 * JD-Core Version:    0.6.2
 */