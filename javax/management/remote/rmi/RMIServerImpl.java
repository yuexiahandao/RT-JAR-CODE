/*     */ package javax.management.remote.rmi;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.ArrayNotificationBuffer;
/*     */ import com.sun.jmx.remote.internal.NotificationBuffer;
/*     */ import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.server.RemoteServer;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.remote.JMXAuthenticator;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public abstract class RMIServerImpl
/*     */   implements Closeable, RMIServer
/*     */ {
/* 531 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIServerImpl");
/*     */ 
/* 537 */   private final List<WeakReference<RMIConnection>> clientList = new ArrayList();
/*     */   private ClassLoader cl;
/*     */   private MBeanServer mbeanServer;
/*     */   private final Map<String, ?> env;
/*     */   private RMIConnectorServer connServer;
/*     */   private static int connectionIdNumber;
/*     */   private NotificationBuffer notifBuffer;
/*     */ 
/*     */   public RMIServerImpl(Map<String, ?> paramMap)
/*     */   {
/*  78 */     this.env = (paramMap == null ? Collections.emptyMap() : paramMap);
/*     */   }
/*     */ 
/*     */   void setRMIConnectorServer(RMIConnectorServer paramRMIConnectorServer) throws IOException
/*     */   {
/*  83 */     this.connServer = paramRMIConnectorServer;
/*     */   }
/*     */ 
/*     */   protected abstract void export()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Remote toStub()
/*     */     throws IOException;
/*     */ 
/*     */   public synchronized void setDefaultClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 112 */     this.cl = paramClassLoader;
/*     */   }
/*     */ 
/*     */   public synchronized ClassLoader getDefaultClassLoader()
/*     */   {
/* 125 */     return this.cl;
/*     */   }
/*     */ 
/*     */   public synchronized void setMBeanServer(MBeanServer paramMBeanServer)
/*     */   {
/* 140 */     this.mbeanServer = paramMBeanServer;
/*     */   }
/*     */ 
/*     */   public synchronized MBeanServer getMBeanServer()
/*     */   {
/* 155 */     return this.mbeanServer;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/*     */     try {
/* 161 */       return "1.0 java_runtime_" + System.getProperty("java.runtime.version");
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/* 164 */     return "1.0 ";
/*     */   }
/*     */ 
/*     */   public RMIConnection newClient(Object paramObject)
/*     */     throws IOException
/*     */   {
/* 199 */     return doNewClient(paramObject);
/*     */   }
/*     */ 
/*     */   RMIConnection doNewClient(Object paramObject)
/*     */     throws IOException
/*     */   {
/* 208 */     boolean bool = logger.traceOn();
/*     */ 
/* 210 */     if (bool) logger.trace("newClient", "making new client");
/*     */ 
/* 212 */     if (getMBeanServer() == null) {
/* 213 */       throw new IllegalStateException("Not attached to an MBean server");
/*     */     }
/* 215 */     Subject localSubject = null;
/* 216 */     Object localObject1 = (JMXAuthenticator)this.env.get("jmx.remote.authenticator");
/*     */ 
/* 218 */     if (localObject1 == null)
/*     */     {
/* 223 */       if ((this.env.get("jmx.remote.x.password.file") != null) || (this.env.get("jmx.remote.x.login.config") != null))
/*     */       {
/* 225 */         localObject1 = new JMXPluggableAuthenticator(this.env);
/*     */       }
/*     */     }
/* 228 */     if (localObject1 != null) {
/* 229 */       if (bool) logger.trace("newClient", "got authenticator: " + localObject1.getClass().getName());
/*     */       try
/*     */       {
/* 232 */         localSubject = ((JMXAuthenticator)localObject1).authenticate(paramObject);
/*     */       } catch (SecurityException localSecurityException) {
/* 234 */         logger.trace("newClient", "Authentication failed: " + localSecurityException);
/* 235 */         throw localSecurityException;
/*     */       }
/*     */     }
/*     */ 
/* 239 */     if (bool) {
/* 240 */       if (localSubject != null)
/* 241 */         logger.trace("newClient", "subject is not null");
/* 242 */       else logger.trace("newClient", "no subject");
/*     */     }
/*     */ 
/* 245 */     String str = makeConnectionId(getProtocol(), localSubject);
/*     */ 
/* 247 */     if (bool) {
/* 248 */       logger.trace("newClient", "making new connection: " + str);
/*     */     }
/* 250 */     RMIConnection localRMIConnection = makeClient(str, localSubject);
/*     */ 
/* 252 */     dropDeadReferences();
/* 253 */     WeakReference localWeakReference = new WeakReference(localRMIConnection);
/* 254 */     synchronized (this.clientList) {
/* 255 */       this.clientList.add(localWeakReference);
/*     */     }
/*     */ 
/* 258 */     this.connServer.connectionOpened(str, "Connection opened", null);
/*     */ 
/* 260 */     synchronized (this.clientList) {
/* 261 */       if (!this.clientList.contains(localWeakReference))
/*     */       {
/* 263 */         throw new IOException("The connection is refused.");
/*     */       }
/*     */     }
/*     */ 
/* 267 */     if (bool) {
/* 268 */       logger.trace("newClient", "new connection done: " + str);
/*     */     }
/* 270 */     return localRMIConnection;
/*     */   }
/*     */ 
/*     */   protected abstract RMIConnection makeClient(String paramString, Subject paramSubject)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void closeClient(RMIConnection paramRMIConnection)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract String getProtocol();
/*     */ 
/*     */   protected void clientClosed(RMIConnection paramRMIConnection)
/*     */     throws IOException
/*     */   {
/* 338 */     boolean bool = logger.debugOn();
/*     */ 
/* 340 */     if (bool) logger.trace("clientClosed", "client=" + paramRMIConnection);
/*     */ 
/* 342 */     if (paramRMIConnection == null) {
/* 343 */       throw new NullPointerException("Null client");
/*     */     }
/* 345 */     synchronized (this.clientList) {
/* 346 */       dropDeadReferences();
/* 347 */       Iterator localIterator = this.clientList.iterator();
/* 348 */       while (localIterator.hasNext()) {
/* 349 */         WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 350 */         if (localWeakReference.get() == paramRMIConnection) {
/* 351 */           localIterator.remove();
/* 352 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 360 */     if (bool) logger.trace("clientClosed", "closing client.");
/* 361 */     closeClient(paramRMIConnection);
/*     */ 
/* 363 */     if (bool) logger.trace("clientClosed", "sending notif");
/* 364 */     this.connServer.connectionClosed(paramRMIConnection.getConnectionId(), "Client connection closed", null);
/*     */ 
/* 367 */     if (bool) logger.trace("clientClosed", "done");
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 399 */     boolean bool1 = logger.traceOn();
/* 400 */     boolean bool2 = logger.debugOn();
/*     */ 
/* 402 */     if (bool1) logger.trace("close", "closing");
/*     */ 
/* 404 */     Object localObject1 = null;
/*     */     try {
/* 406 */       if (bool2) logger.debug("close", "closing Server");
/* 407 */       closeServer();
/*     */     } catch (IOException localIOException1) {
/* 409 */       if (bool1) logger.trace("close", "Failed to close server: " + localIOException1);
/* 410 */       if (bool2) logger.debug("close", localIOException1);
/* 411 */       localObject1 = localIOException1;
/*     */     }
/*     */ 
/* 414 */     if (bool2) logger.debug("close", "closing Clients");
/*     */     while (true)
/*     */     {
/* 417 */       synchronized (this.clientList) {
/* 418 */         if (bool2) logger.debug("close", "droping dead references");
/* 419 */         dropDeadReferences();
/*     */ 
/* 421 */         if (bool2) logger.debug("close", "client count: " + this.clientList.size());
/* 422 */         if (this.clientList.size() == 0)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 428 */         Iterator localIterator = this.clientList.iterator();
/* 429 */         if (localIterator.hasNext()) {
/* 430 */           WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 431 */           RMIConnection localRMIConnection = (RMIConnection)localWeakReference.get();
/* 432 */           localIterator.remove();
/* 433 */           if (localRMIConnection != null) {
/*     */             try {
/* 435 */               localRMIConnection.close();
/*     */             } catch (IOException localIOException2) {
/* 437 */               if (bool1)
/* 438 */                 logger.trace("close", "Failed to close client: " + localIOException2);
/* 439 */               if (bool2) logger.debug("close", localIOException2);
/* 440 */               if (localObject1 == null) {
/* 441 */                 localObject1 = localIOException2;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 449 */     if (this.notifBuffer != null) {
/* 450 */       this.notifBuffer.dispose();
/*     */     }
/* 452 */     if (localObject1 != null) {
/* 453 */       if (bool1) logger.trace("close", "close failed.");
/* 454 */       throw localObject1;
/*     */     }
/*     */ 
/* 457 */     if (bool1) logger.trace("close", "closed.");
/*     */   }
/*     */ 
/*     */   protected abstract void closeServer()
/*     */     throws IOException;
/*     */ 
/*     */   private static synchronized String makeConnectionId(String paramString, Subject paramSubject)
/*     */   {
/* 472 */     connectionIdNumber += 1;
/*     */ 
/* 474 */     String str1 = "";
/*     */     try {
/* 476 */       str1 = RemoteServer.getClientHost();
/*     */ 
/* 483 */       if (str1.contains(":"))
/* 484 */         str1 = "[" + str1 + "]";
/*     */     }
/*     */     catch (ServerNotActiveException localServerNotActiveException) {
/* 487 */       logger.trace("makeConnectionId", "getClientHost", localServerNotActiveException);
/*     */     }
/*     */ 
/* 490 */     StringBuilder localStringBuilder = new StringBuilder();
/* 491 */     localStringBuilder.append(paramString).append(":");
/* 492 */     if (str1.length() > 0)
/* 493 */       localStringBuilder.append("//").append(str1);
/* 494 */     localStringBuilder.append(" ");
/*     */     String str2;
/*     */     Iterator localIterator;
/* 495 */     if (paramSubject != null) {
/* 496 */       Set localSet = paramSubject.getPrincipals();
/* 497 */       str2 = "";
/* 498 */       for (localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/* 499 */         Principal localPrincipal = (Principal)localIterator.next();
/* 500 */         String str3 = localPrincipal.getName().replace(' ', '_').replace(';', ':');
/* 501 */         localStringBuilder.append(str2).append(str3);
/* 502 */         str2 = ";";
/*     */       }
/*     */     }
/* 505 */     localStringBuilder.append(" ").append(connectionIdNumber);
/* 506 */     if (logger.traceOn())
/* 507 */       logger.trace("newConnectionId", "connectionId=" + localStringBuilder);
/* 508 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void dropDeadReferences() {
/* 512 */     synchronized (this.clientList) {
/* 513 */       Iterator localIterator = this.clientList.iterator();
/* 514 */       while (localIterator.hasNext()) {
/* 515 */         WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 516 */         if (localWeakReference.get() == null)
/* 517 */           localIterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized NotificationBuffer getNotifBuffer()
/*     */   {
/* 524 */     if (this.notifBuffer == null) {
/* 525 */       this.notifBuffer = ArrayNotificationBuffer.getNotificationBuffer(this.mbeanServer, this.env);
/*     */     }
/*     */ 
/* 528 */     return this.notifBuffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIServerImpl
 * JD-Core Version:    0.6.2
 */