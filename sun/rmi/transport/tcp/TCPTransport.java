/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.BindException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.ExportException;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.rmi.server.RMIFailureHandler;
/*     */ import java.rmi.server.RMISocketFactory;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.rmi.server.UID;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.logging.Level;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.rmi.transport.Connection;
/*     */ import sun.rmi.transport.DGCAckHandler;
/*     */ import sun.rmi.transport.Endpoint;
/*     */ import sun.rmi.transport.StreamRemoteCall;
/*     */ import sun.rmi.transport.Target;
/*     */ import sun.rmi.transport.Transport;
/*     */ import sun.rmi.transport.proxy.HttpReceiveSocket;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.action.GetLongAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class TCPTransport extends Transport
/*     */ {
/*     */   static final Log tcpLog;
/*     */   private static final int maxConnectionThreads;
/*     */   private static final long threadKeepAliveTime;
/*     */   private static final ExecutorService connectionThreadPool;
/*     */   private static final AtomicInteger connectionCount;
/*     */   private static final ThreadLocal<ConnectionHandler> threadConnectionHandler;
/* 133 */   private static final AccessControlContext NOPERMS_ACC = new AccessControlContext(arrayOfProtectionDomain);
/*     */   private final LinkedList<TCPEndpoint> epList;
/* 139 */   private int exportCount = 0;
/*     */ 
/* 141 */   private ServerSocket server = null;
/*     */ 
/* 143 */   private final Map<TCPEndpoint, Reference<TCPChannel>> channelTable = new WeakHashMap();
/*     */ 
/* 146 */   static final RMISocketFactory defaultSocketFactory = RMISocketFactory.getDefaultSocketFactory();
/*     */ 
/* 155 */   private static final int connectionReadTimeout = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.readTimeout", 7200000))).intValue();
/*     */ 
/*     */   TCPTransport(LinkedList<TCPEndpoint> paramLinkedList)
/*     */   {
/* 165 */     this.epList = paramLinkedList;
/* 166 */     if (tcpLog.isLoggable(Log.BRIEF))
/* 167 */       tcpLog.log(Log.BRIEF, "Version = 2, ep = " + getEndpoint());
/*     */   }
/*     */ 
/*     */   public void shedConnectionCaches()
/*     */   {
/*     */     ArrayList localArrayList;
/*     */     Object localObject1;
/* 178 */     synchronized (this.channelTable) {
/* 179 */       localArrayList = new ArrayList(this.channelTable.values().size());
/* 180 */       for (localObject1 = this.channelTable.values().iterator(); ((Iterator)localObject1).hasNext(); ) { Reference localReference = (Reference)((Iterator)localObject1).next();
/* 181 */         TCPChannel localTCPChannel = (TCPChannel)localReference.get();
/* 182 */         if (localTCPChannel != null) {
/* 183 */           localArrayList.add(localTCPChannel);
/*     */         }
/*     */       }
/*     */     }
/* 187 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { localObject1 = (TCPChannel)((Iterator)???).next();
/* 188 */       ((TCPChannel)localObject1).shedCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   public TCPChannel getChannel(Endpoint paramEndpoint)
/*     */   {
/* 202 */     TCPChannel localTCPChannel = null;
/* 203 */     if ((paramEndpoint instanceof TCPEndpoint)) {
/* 204 */       synchronized (this.channelTable) {
/* 205 */         Reference localReference = (Reference)this.channelTable.get(paramEndpoint);
/* 206 */         if (localReference != null) {
/* 207 */           localTCPChannel = (TCPChannel)localReference.get();
/*     */         }
/* 209 */         if (localTCPChannel == null) {
/* 210 */           TCPEndpoint localTCPEndpoint = (TCPEndpoint)paramEndpoint;
/* 211 */           localTCPChannel = new TCPChannel(this, localTCPEndpoint);
/* 212 */           this.channelTable.put(localTCPEndpoint, new WeakReference(localTCPChannel));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 217 */     return localTCPChannel;
/*     */   }
/*     */ 
/*     */   public void free(Endpoint paramEndpoint)
/*     */   {
/* 225 */     if ((paramEndpoint instanceof TCPEndpoint))
/* 226 */       synchronized (this.channelTable) {
/* 227 */         Reference localReference = (Reference)this.channelTable.remove(paramEndpoint);
/* 228 */         if (localReference != null) {
/* 229 */           TCPChannel localTCPChannel = (TCPChannel)localReference.get();
/* 230 */           if (localTCPChannel != null)
/* 231 */             localTCPChannel.shedCache();
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void exportObject(Target paramTarget)
/*     */     throws RemoteException
/*     */   {
/* 247 */     synchronized (this) {
/* 248 */       listen();
/* 249 */       this.exportCount += 1;
/*     */     }
/*     */ 
/* 257 */     int i = 0;
/*     */     try {
/* 259 */       super.exportObject(paramTarget);
/* 260 */       i = 1;
/*     */     } finally {
/* 262 */       if (i == 0)
/* 263 */         synchronized (this) {
/* 264 */           decrementExportCount();
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void targetUnexported()
/*     */   {
/* 271 */     decrementExportCount();
/*     */   }
/*     */ 
/*     */   private void decrementExportCount()
/*     */   {
/* 279 */     assert (Thread.holdsLock(this));
/* 280 */     this.exportCount -= 1;
/* 281 */     if ((this.exportCount == 0) && (getEndpoint().getListenPort() != 0)) {
/* 282 */       ServerSocket localServerSocket = this.server;
/* 283 */       this.server = null;
/*     */       try {
/* 285 */         localServerSocket.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkAcceptPermission(AccessControlContext paramAccessControlContext)
/*     */   {
/* 296 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 297 */     if (localSecurityManager == null) {
/* 298 */       return;
/*     */     }
/* 300 */     ConnectionHandler localConnectionHandler = (ConnectionHandler)threadConnectionHandler.get();
/* 301 */     if (localConnectionHandler == null) {
/* 302 */       throw new Error("checkAcceptPermission not in ConnectionHandler thread");
/*     */     }
/*     */ 
/* 305 */     localConnectionHandler.checkAcceptPermission(localSecurityManager, paramAccessControlContext);
/*     */   }
/*     */ 
/*     */   private TCPEndpoint getEndpoint() {
/* 309 */     synchronized (this.epList) {
/* 310 */       return (TCPEndpoint)this.epList.getLast();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void listen()
/*     */     throws RemoteException
/*     */   {
/* 318 */     assert (Thread.holdsLock(this));
/* 319 */     TCPEndpoint localTCPEndpoint = getEndpoint();
/* 320 */     int i = localTCPEndpoint.getPort();
/*     */ 
/* 322 */     if (this.server == null) {
/* 323 */       if (tcpLog.isLoggable(Log.BRIEF)) {
/* 324 */         tcpLog.log(Log.BRIEF, "(port " + i + ") create server socket");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 329 */         this.server = localTCPEndpoint.newServerSocket();
/*     */ 
/* 335 */         Thread localThread = (Thread)AccessController.doPrivileged(new NewThreadAction(new AcceptLoop(this.server), "TCP Accept-" + i, true));
/*     */ 
/* 338 */         localThread.start();
/*     */       } catch (BindException localBindException) {
/* 340 */         throw new ExportException("Port already in use: " + i, localBindException);
/*     */       } catch (IOException localIOException) {
/* 342 */         throw new ExportException("Listen failed on port: " + i, localIOException);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 347 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 348 */       if (localSecurityManager != null)
/* 349 */         localSecurityManager.checkListen(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void closeSocket(Socket paramSocket)
/*     */   {
/*     */     try
/*     */     {
/* 532 */       paramSocket.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void handleMessages(Connection paramConnection, boolean paramBoolean)
/*     */   {
/* 544 */     int i = getEndpoint().getPort();
/*     */     try
/*     */     {
/* 547 */       DataInputStream localDataInputStream = new DataInputStream(paramConnection.getInputStream());
/*     */       do {
/* 549 */         int j = localDataInputStream.read();
/* 550 */         if (j == -1) {
/* 551 */           if (!tcpLog.isLoggable(Log.BRIEF)) break;
/* 552 */           tcpLog.log(Log.BRIEF, "(port " + i + ") connection closed"); break;
/*     */         }
/*     */ 
/* 558 */         if (tcpLog.isLoggable(Log.BRIEF)) {
/* 559 */           tcpLog.log(Log.BRIEF, "(port " + i + ") op = " + j);
/*     */         }
/*     */ 
/* 563 */         switch (j)
/*     */         {
/*     */         case 80:
/* 566 */           StreamRemoteCall localStreamRemoteCall = new StreamRemoteCall(paramConnection);
/* 567 */           if (!serviceCall(localStreamRemoteCall))
/*     */           {
/*     */             return;
/*     */           }
/*     */           break;
/*     */         case 82:
/* 573 */           DataOutputStream localDataOutputStream = new DataOutputStream(paramConnection.getOutputStream());
/*     */ 
/* 575 */           localDataOutputStream.writeByte(83);
/* 576 */           paramConnection.releaseOutputStream();
/* 577 */           break;
/*     */         case 84:
/* 580 */           DGCAckHandler.received(UID.read(localDataInputStream));
/* 581 */           break;
/*     */         case 81:
/*     */         case 83:
/*     */         default:
/* 584 */           throw new IOException("unknown transport op " + j);
/*     */         }
/*     */       }
/* 586 */       while (paramBoolean);
/*     */     }
/*     */     catch (IOException localIOException2)
/*     */     {
/* 590 */       if (tcpLog.isLoggable(Log.BRIEF))
/* 591 */         tcpLog.log(Log.BRIEF, "(port " + i + ") exception: ", localIOException2);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/* 596 */         paramConnection.close();
/*     */       }
/*     */       catch (IOException localIOException5)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getClientHost()
/*     */     throws ServerNotActiveException
/*     */   {
/* 608 */     ConnectionHandler localConnectionHandler = (ConnectionHandler)threadConnectionHandler.get();
/* 609 */     if (localConnectionHandler != null) {
/* 610 */       return localConnectionHandler.getClientHost();
/*     */     }
/* 612 */     throw new ServerNotActiveException("not in a remote call");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  93 */     tcpLog = Log.getLog("sun.rmi.transport.tcp", "tcp", LogStream.parseLevel((String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.tcp.logLevel"))));
/*     */ 
/*  98 */     maxConnectionThreads = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.maxConnectionThreads", 2147483647))).intValue();
/*     */ 
/* 104 */     threadKeepAliveTime = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.tcp.threadKeepAliveTime", 60000L))).longValue();
/*     */ 
/* 110 */     connectionThreadPool = new ThreadPoolExecutor(0, maxConnectionThreads, threadKeepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue(), new ThreadFactory()
/*     */     {
/*     */       public Thread newThread(Runnable paramAnonymousRunnable)
/*     */       {
/* 116 */         return (Thread)AccessController.doPrivileged(new NewThreadAction(paramAnonymousRunnable, "TCP Connection(idle)", true, true));
/*     */       }
/*     */     });
/* 122 */     connectionCount = new AtomicInteger(0);
/*     */ 
/* 126 */     threadConnectionHandler = new ThreadLocal();
/*     */ 
/* 131 */     Permissions localPermissions = new Permissions();
/* 132 */     ProtectionDomain[] arrayOfProtectionDomain = { new ProtectionDomain(null, localPermissions) };
/*     */   }
/*     */ 
/*     */   private class AcceptLoop
/*     */     implements Runnable
/*     */   {
/*     */     private final ServerSocket serverSocket;
/* 362 */     private long lastExceptionTime = 0L;
/*     */     private int recentExceptionCount;
/*     */ 
/*     */     AcceptLoop(ServerSocket arg2)
/*     */     {
/*     */       Object localObject;
/* 366 */       this.serverSocket = localObject;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 371 */         executeAcceptLoop();
/*     */       }
/*     */       finally
/*     */       {
/*     */         try
/*     */         {
/* 380 */           this.serverSocket.close();
/*     */         }
/*     */         catch (IOException localIOException2)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void executeAcceptLoop()
/*     */     {
/* 391 */       if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 392 */         TCPTransport.tcpLog.log(Log.BRIEF, "listening on port " + TCPTransport.this.getEndpoint().getPort());
/*     */       }
/*     */ 
/*     */       while (true)
/*     */       {
/* 397 */         Socket localSocket = null;
/*     */         try {
/* 399 */           localSocket = this.serverSocket.accept();
/*     */ 
/* 404 */           InetAddress localInetAddress = localSocket.getInetAddress();
/* 405 */           String str = localInetAddress != null ? localInetAddress.getHostAddress() : "0.0.0.0";
/*     */           try
/*     */           {
/* 414 */             TCPTransport.connectionThreadPool.execute(new TCPTransport.ConnectionHandler(TCPTransport.this, localSocket, str));
/*     */           }
/*     */           catch (RejectedExecutionException localRejectedExecutionException) {
/* 417 */             TCPTransport.closeSocket(localSocket);
/* 418 */             TCPTransport.tcpLog.log(Log.BRIEF, "rejected connection from " + str);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/*     */           try
/*     */           {
/* 430 */             if (this.serverSocket.isClosed())
/*     */             {
/* 448 */               if (localSocket != null)
/* 449 */                 TCPTransport.closeSocket(localSocket); break;
/*     */             }
/*     */             try
/*     */             {
/* 435 */               if (TCPTransport.tcpLog.isLoggable(Level.WARNING)) {
/* 436 */                 TCPTransport.tcpLog.log(Level.WARNING, "accept loop for " + this.serverSocket + " throws", localThrowable1);
/*     */               }
/*     */ 
/*     */             }
/*     */             catch (Throwable localThrowable2)
/*     */             {
/*     */             }
/*     */ 
/*     */           }
/*     */           finally
/*     */           {
/* 448 */             if (localSocket != null) {
/* 449 */               TCPTransport.closeSocket(localSocket);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 457 */           if (!(localThrowable1 instanceof SecurityException)) {
/*     */             try {
/* 459 */               TCPEndpoint.shedConnectionCaches();
/*     */             }
/*     */             catch (Throwable localThrowable3)
/*     */             {
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 469 */           if (((localThrowable1 instanceof Exception)) || ((localThrowable1 instanceof OutOfMemoryError)) || ((localThrowable1 instanceof NoClassDefFoundError)))
/*     */           {
/* 473 */             if (continueAfterAcceptFailure(localThrowable1));
/*     */           }
/*     */           else
/*     */           {
/* 477 */             if ((localThrowable1 instanceof Error)) {
/* 478 */               throw ((Error)localThrowable1);
/*     */             }
/* 480 */             throw new UndeclaredThrowableException(localThrowable1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private boolean continueAfterAcceptFailure(Throwable paramThrowable)
/*     */     {
/* 496 */       RMIFailureHandler localRMIFailureHandler = RMISocketFactory.getFailureHandler();
/* 497 */       if (localRMIFailureHandler != null) {
/* 498 */         return localRMIFailureHandler.failure((paramThrowable instanceof Exception) ? (Exception)paramThrowable : new InvocationTargetException(paramThrowable));
/*     */       }
/*     */ 
/* 501 */       throttleLoopOnException();
/* 502 */       return true;
/*     */     }
/*     */ 
/*     */     private void throttleLoopOnException()
/*     */     {
/* 512 */       long l = System.currentTimeMillis();
/* 513 */       if ((this.lastExceptionTime == 0L) || (l - this.lastExceptionTime > 5000L))
/*     */       {
/* 515 */         this.lastExceptionTime = l;
/* 516 */         this.recentExceptionCount = 0;
/*     */       }
/* 519 */       else if (++this.recentExceptionCount >= 10) {
/*     */         try {
/* 521 */           Thread.sleep(10000L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ConnectionHandler
/*     */     implements Runnable
/*     */   {
/*     */     private static final int POST = 1347375956;
/*     */     private AccessControlContext okContext;
/*     */     private Map<AccessControlContext, Reference<AccessControlContext>> authCache;
/* 631 */     private SecurityManager cacheSecurityManager = null;
/*     */     private Socket socket;
/*     */     private String remoteHost;
/*     */ 
/*     */     ConnectionHandler(Socket paramString, String arg3)
/*     */     {
/* 637 */       this.socket = paramString;
/*     */       Object localObject;
/* 638 */       this.remoteHost = localObject;
/*     */     }
/*     */ 
/*     */     String getClientHost() {
/* 642 */       return this.remoteHost;
/*     */     }
/*     */ 
/*     */     void checkAcceptPermission(SecurityManager paramSecurityManager, AccessControlContext paramAccessControlContext)
/*     */     {
/* 656 */       if (paramSecurityManager != this.cacheSecurityManager) {
/* 657 */         this.okContext = null;
/* 658 */         this.authCache = new WeakHashMap();
/*     */ 
/* 660 */         this.cacheSecurityManager = paramSecurityManager;
/*     */       }
/* 662 */       if ((paramAccessControlContext.equals(this.okContext)) || (this.authCache.containsKey(paramAccessControlContext))) {
/* 663 */         return;
/*     */       }
/* 665 */       InetAddress localInetAddress = this.socket.getInetAddress();
/* 666 */       String str = localInetAddress != null ? localInetAddress.getHostAddress() : "*";
/*     */ 
/* 668 */       paramSecurityManager.checkAccept(str, this.socket.getPort());
/*     */ 
/* 670 */       this.authCache.put(paramAccessControlContext, new SoftReference(paramAccessControlContext));
/* 671 */       this.okContext = paramAccessControlContext;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 675 */       Thread localThread = Thread.currentThread();
/* 676 */       String str = localThread.getName();
/*     */       try {
/* 678 */         localThread.setName("RMI TCP Connection(" + TCPTransport.connectionCount.incrementAndGet() + ")-" + this.remoteHost);
/*     */ 
/* 681 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 684 */             TCPTransport.ConnectionHandler.this.run0();
/* 685 */             return null;
/*     */           }
/*     */         }
/*     */         , TCPTransport.NOPERMS_ACC);
/*     */       }
/*     */       finally
/*     */       {
/* 689 */         localThread.setName(str);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void run0() {
/* 694 */       TCPEndpoint localTCPEndpoint1 = TCPTransport.this.getEndpoint();
/* 695 */       int i = localTCPEndpoint1.getPort();
/*     */ 
/* 697 */       TCPTransport.threadConnectionHandler.set(this);
/*     */       try
/*     */       {
/* 703 */         this.socket.setTcpNoDelay(true);
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */       try {
/* 709 */         if (TCPTransport.connectionReadTimeout > 0)
/* 710 */           this.socket.setSoTimeout(TCPTransport.connectionReadTimeout);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */       try {
/* 716 */         InputStream localInputStream = this.socket.getInputStream();
/* 717 */         BufferedInputStream localBufferedInputStream = localInputStream.markSupported() ? localInputStream : new BufferedInputStream(localInputStream);
/*     */ 
/* 722 */         localBufferedInputStream.mark(4);
/* 723 */         DataInputStream localDataInputStream = new DataInputStream(localBufferedInputStream);
/* 724 */         int j = localDataInputStream.readInt();
/*     */ 
/* 726 */         if (j == 1347375956) {
/* 727 */           TCPTransport.tcpLog.log(Log.BRIEF, "decoding HTTP-wrapped call");
/*     */ 
/* 732 */           localBufferedInputStream.reset();
/*     */           try
/*     */           {
/* 735 */             this.socket = new HttpReceiveSocket(this.socket, localBufferedInputStream, null);
/* 736 */             this.remoteHost = "0.0.0.0";
/* 737 */             localInputStream = this.socket.getInputStream();
/* 738 */             localBufferedInputStream = new BufferedInputStream(localInputStream);
/* 739 */             localDataInputStream = new DataInputStream(localBufferedInputStream);
/* 740 */             j = localDataInputStream.readInt();
/*     */           }
/*     */           catch (IOException localIOException2) {
/* 743 */             throw new RemoteException("Error HTTP-unwrapping call", localIOException2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 751 */         int k = localDataInputStream.readShort();
/* 752 */         if ((j != 1246907721) || (k != 2))
/*     */         {
/* 758 */           TCPTransport.closeSocket(this.socket);
/*     */         }
/*     */         else
/*     */         {
/* 762 */           OutputStream localOutputStream = this.socket.getOutputStream();
/* 763 */           BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localOutputStream);
/*     */ 
/* 765 */           DataOutputStream localDataOutputStream = new DataOutputStream(localBufferedOutputStream);
/*     */ 
/* 767 */           int m = this.socket.getPort();
/*     */ 
/* 769 */           if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 770 */             TCPTransport.tcpLog.log(Log.BRIEF, "accepted socket from [" + this.remoteHost + ":" + m + "]");
/*     */           }
/*     */ 
/* 779 */           int n = localDataInputStream.readByte();
/*     */           TCPEndpoint localTCPEndpoint2;
/*     */           TCPChannel localTCPChannel;
/*     */           TCPConnection localTCPConnection;
/* 780 */           switch (n)
/*     */           {
/*     */           case 76:
/* 785 */             localTCPEndpoint2 = new TCPEndpoint(this.remoteHost, this.socket.getLocalPort(), localTCPEndpoint1.getClientSocketFactory(), localTCPEndpoint1.getServerSocketFactory());
/*     */ 
/* 788 */             localTCPChannel = new TCPChannel(TCPTransport.this, localTCPEndpoint2);
/* 789 */             localTCPConnection = new TCPConnection(localTCPChannel, this.socket, localBufferedInputStream, localBufferedOutputStream);
/*     */ 
/* 792 */             TCPTransport.this.handleMessages(localTCPConnection, false);
/* 793 */             break;
/*     */           case 75:
/* 797 */             localDataOutputStream.writeByte(78);
/*     */ 
/* 800 */             if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 801 */               TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + i + ") " + "suggesting " + this.remoteHost + ":" + m);
/*     */             }
/*     */ 
/* 806 */             localDataOutputStream.writeUTF(this.remoteHost);
/* 807 */             localDataOutputStream.writeInt(m);
/* 808 */             localDataOutputStream.flush();
/*     */ 
/* 812 */             String str = localDataInputStream.readUTF();
/* 813 */             int i1 = localDataInputStream.readInt();
/* 814 */             if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 815 */               TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + i + ") client using " + str + ":" + i1);
/*     */             }
/*     */ 
/* 821 */             localTCPEndpoint2 = new TCPEndpoint(this.remoteHost, this.socket.getLocalPort(), localTCPEndpoint1.getClientSocketFactory(), localTCPEndpoint1.getServerSocketFactory());
/*     */ 
/* 824 */             localTCPChannel = new TCPChannel(TCPTransport.this, localTCPEndpoint2);
/* 825 */             localTCPConnection = new TCPConnection(localTCPChannel, this.socket, localBufferedInputStream, localBufferedOutputStream);
/*     */ 
/* 828 */             TCPTransport.this.handleMessages(localTCPConnection, true);
/* 829 */             break;
/*     */           case 77:
/* 832 */             if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 833 */               TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + i + ") accepting multiplex protocol");
/*     */             }
/*     */ 
/* 838 */             localDataOutputStream.writeByte(78);
/*     */ 
/* 841 */             if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 842 */               TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + i + ") suggesting " + this.remoteHost + ":" + m);
/*     */             }
/*     */ 
/* 846 */             localDataOutputStream.writeUTF(this.remoteHost);
/* 847 */             localDataOutputStream.writeInt(m);
/* 848 */             localDataOutputStream.flush();
/*     */ 
/* 851 */             localTCPEndpoint2 = new TCPEndpoint(localDataInputStream.readUTF(), localDataInputStream.readInt(), localTCPEndpoint1.getClientSocketFactory(), localTCPEndpoint1.getServerSocketFactory());
/*     */ 
/* 854 */             if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE))
/* 855 */               TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + i + ") client using " + localTCPEndpoint2.getHost() + ":" + localTCPEndpoint2.getPort());
/*     */             ConnectionMultiplexer localConnectionMultiplexer;
/* 861 */             synchronized (TCPTransport.this.channelTable)
/*     */             {
/* 863 */               localTCPChannel = TCPTransport.this.getChannel(localTCPEndpoint2);
/* 864 */               localConnectionMultiplexer = new ConnectionMultiplexer(localTCPChannel, localBufferedInputStream, localOutputStream, false);
/*     */ 
/* 867 */               localTCPChannel.useMultiplexer(localConnectionMultiplexer);
/*     */             }
/* 869 */             localConnectionMultiplexer.run();
/* 870 */             break;
/*     */           default:
/* 874 */             localDataOutputStream.writeByte(79);
/* 875 */             localDataOutputStream.flush();
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/* 881 */         TCPTransport.tcpLog.log(Log.BRIEF, "terminated with exception:", localIOException1);
/*     */       } finally {
/* 883 */         TCPTransport.closeSocket(this.socket);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.TCPTransport
 * JD-Core Version:    0.6.2
 */