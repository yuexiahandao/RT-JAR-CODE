/*     */ package sun.rmi.registry;
/*     */ 
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.UnknownHostException;
/*     */ import java.rmi.AccessException;
/*     */ import java.rmi.AlreadyBoundException;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.RMISecurityManager;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RemoteServer;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Permissions;
/*     */ import java.security.Policy;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.security.cert.Certificate;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import sun.misc.URLClassPath;
/*     */ import sun.rmi.server.LoaderHandler;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.rmi.server.UnicastServerRef2;
/*     */ import sun.rmi.transport.LiveRef;
/*     */ 
/*     */ public class RegistryImpl extends RemoteServer
/*     */   implements Registry
/*     */ {
/*     */   private static final long serialVersionUID = 4666870661827494597L;
/*  79 */   private Hashtable<String, Remote> bindings = new Hashtable(101);
/*     */ 
/*  81 */   private static Hashtable<InetAddress, InetAddress> allowedAccessCache = new Hashtable(3);
/*     */   private static RegistryImpl registry;
/*  84 */   private static ObjID id = new ObjID(0);
/*     */ 
/*  86 */   private static ResourceBundle resources = null;
/*     */ 
/*     */   public RegistryImpl(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/*  97 */     LiveRef localLiveRef = new LiveRef(id, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*  98 */     setup(new UnicastServerRef2(localLiveRef));
/*     */   }
/*     */ 
/*     */   public RegistryImpl(int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 107 */     LiveRef localLiveRef = new LiveRef(id, paramInt);
/* 108 */     setup(new UnicastServerRef(localLiveRef));
/*     */   }
/*     */ 
/*     */   private void setup(UnicastServerRef paramUnicastServerRef)
/*     */     throws RemoteException
/*     */   {
/* 121 */     this.ref = paramUnicastServerRef;
/* 122 */     paramUnicastServerRef.exportObject(this, null, true);
/*     */   }
/*     */ 
/*     */   public Remote lookup(String paramString)
/*     */     throws RemoteException, NotBoundException
/*     */   {
/* 133 */     synchronized (this.bindings) {
/* 134 */       Remote localRemote = (Remote)this.bindings.get(paramString);
/* 135 */       if (localRemote == null)
/* 136 */         throw new NotBoundException(paramString);
/* 137 */       return localRemote;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Remote paramRemote)
/*     */     throws RemoteException, AlreadyBoundException, AccessException
/*     */   {
/* 149 */     checkAccess("Registry.bind");
/* 150 */     synchronized (this.bindings) {
/* 151 */       Remote localRemote = (Remote)this.bindings.get(paramString);
/* 152 */       if (localRemote != null)
/* 153 */         throw new AlreadyBoundException(paramString);
/* 154 */       this.bindings.put(paramString, paramRemote);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString)
/*     */     throws RemoteException, NotBoundException, AccessException
/*     */   {
/* 166 */     checkAccess("Registry.unbind");
/* 167 */     synchronized (this.bindings) {
/* 168 */       Remote localRemote = (Remote)this.bindings.get(paramString);
/* 169 */       if (localRemote == null)
/* 170 */         throw new NotBoundException(paramString);
/* 171 */       this.bindings.remove(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Remote paramRemote)
/*     */     throws RemoteException, AccessException
/*     */   {
/* 182 */     checkAccess("Registry.rebind");
/* 183 */     this.bindings.put(paramString, paramRemote);
/*     */   }
/*     */ 
/*     */   public String[] list()
/*     */     throws RemoteException
/*     */   {
/*     */     String[] arrayOfString;
/* 194 */     synchronized (this.bindings) {
/* 195 */       int i = this.bindings.size();
/* 196 */       arrayOfString = new String[i];
/* 197 */       Enumeration localEnumeration = this.bindings.keys();
/*     */       while (true) { i--; if (i < 0) break;
/* 199 */         arrayOfString[i] = ((String)localEnumeration.nextElement()); }
/*     */     }
/* 201 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public static void checkAccess(String paramString)
/*     */     throws AccessException
/*     */   {
/*     */     try
/*     */     {
/* 214 */       String str = getClientHost();
/*     */       InetAddress localInetAddress1;
/*     */       try
/*     */       {
/* 218 */         localInetAddress1 = (InetAddress)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public InetAddress run()
/*     */             throws UnknownHostException
/*     */           {
/* 223 */             return InetAddress.getByName(this.val$clientHostName);
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException1) {
/* 227 */         throw ((UnknownHostException)localPrivilegedActionException1.getException());
/*     */       }
/*     */ 
/* 231 */       if (allowedAccessCache.get(localInetAddress1) == null)
/*     */       {
/* 233 */         if (localInetAddress1.isAnyLocalAddress()) {
/* 234 */           throw new AccessException("Registry." + paramString + " disallowed; origin unknown");
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 239 */           InetAddress localInetAddress2 = localInetAddress1;
/*     */ 
/* 241 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Void run()
/*     */               throws IOException
/*     */             {
/* 248 */               new ServerSocket(0, 10, this.val$finalClientHost).close();
/* 249 */               RegistryImpl.allowedAccessCache.put(this.val$finalClientHost, this.val$finalClientHost);
/*     */ 
/* 251 */               return null;
/*     */             }
/*     */           });
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException2)
/*     */         {
/* 257 */           throw new AccessException("Registry." + paramString + " disallowed; origin " + localInetAddress1 + " is non-local host");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ServerNotActiveException localServerNotActiveException)
/*     */     {
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/* 267 */       throw new AccessException("Registry." + paramString + " disallowed; origin is unknown host");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ObjID getID()
/*     */   {
/* 273 */     return id;
/*     */   }
/*     */ 
/*     */   private static String getTextResource(String paramString)
/*     */   {
/* 280 */     if (resources == null) {
/*     */       try {
/* 282 */         resources = ResourceBundle.getBundle("sun.rmi.registry.resources.rmiregistry");
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException1) {
/*     */       }
/* 286 */       if (resources == null)
/*     */       {
/* 288 */         return "[missing resource file: " + paramString + "]";
/*     */       }
/*     */     }
/*     */ 
/* 292 */     String str = null;
/*     */     try {
/* 294 */       str = resources.getString(paramString);
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException2) {
/*     */     }
/* 298 */     if (str == null) {
/* 299 */       return "[missing resource: " + paramString + "]";
/*     */     }
/* 301 */     return str;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 313 */     if (System.getSecurityManager() == null) {
/* 314 */       System.setSecurityManager(new RMISecurityManager());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 331 */       String str = System.getProperty("env.class.path");
/* 332 */       if (str == null) {
/* 333 */         str = ".";
/*     */       }
/* 335 */       URL[] arrayOfURL = URLClassPath.pathToURLs(str);
/* 336 */       URLClassLoader localURLClassLoader = new URLClassLoader(arrayOfURL);
/*     */ 
/* 343 */       LoaderHandler.registerCodebaseLoader(localURLClassLoader);
/*     */ 
/* 345 */       Thread.currentThread().setContextClassLoader(localURLClassLoader);
/*     */ 
/* 347 */       int i = paramArrayOfString.length >= 1 ? Integer.parseInt(paramArrayOfString[0]) : 1099;
/*     */       try
/*     */       {
/* 350 */         registry = (RegistryImpl)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public RegistryImpl run() throws RemoteException {
/* 353 */             return new RegistryImpl(this.val$regPort);
/*     */           }
/*     */         }
/*     */         , getAccessControlContext(i));
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/* 357 */         throw ((RemoteException)localPrivilegedActionException.getException());
/*     */       }
/*     */       while (true)
/*     */       {
/*     */         try
/*     */         {
/* 363 */           Thread.sleep(9223372036854775807L);
/*     */         } catch (InterruptedException localInterruptedException) {
/*     */         }
/*     */       }
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 368 */       System.err.println(MessageFormat.format(getTextResource("rmiregistry.port.badnumber"), new Object[] { paramArrayOfString[0] }));
/*     */ 
/* 371 */       System.err.println(MessageFormat.format(getTextResource("rmiregistry.usage"), new Object[] { "rmiregistry" }));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 375 */       localException.printStackTrace();
/*     */     }
/* 377 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   private static AccessControlContext getAccessControlContext(int paramInt)
/*     */   {
/* 387 */     PermissionCollection localPermissionCollection = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public PermissionCollection run() {
/* 390 */         CodeSource localCodeSource = new CodeSource(null, (Certificate[])null);
/*     */ 
/* 392 */         Policy localPolicy = Policy.getPolicy();
/* 393 */         if (localPolicy != null) {
/* 394 */           return localPolicy.getPermissions(localCodeSource);
/*     */         }
/* 396 */         return new Permissions();
/*     */       }
/*     */     });
/* 406 */     localPermissionCollection.add(new SocketPermission("*", "connect,accept"));
/* 407 */     localPermissionCollection.add(new SocketPermission("localhost:" + paramInt, "listen,accept"));
/*     */ 
/* 409 */     localPermissionCollection.add(new RuntimePermission("accessClassInPackage.sun.jvmstat.*"));
/* 410 */     localPermissionCollection.add(new RuntimePermission("accessClassInPackage.sun.jvm.hotspot.*"));
/*     */ 
/* 412 */     localPermissionCollection.add(new FilePermission("<<ALL FILES>>", "read"));
/*     */ 
/* 418 */     ProtectionDomain localProtectionDomain = new ProtectionDomain(new CodeSource(null, (Certificate[])null), localPermissionCollection);
/*     */ 
/* 421 */     return new AccessControlContext(new ProtectionDomain[] { localProtectionDomain });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.registry.RegistryImpl
 * JD-Core Version:    0.6.2
 */