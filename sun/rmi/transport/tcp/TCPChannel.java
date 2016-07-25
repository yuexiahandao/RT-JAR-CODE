/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.Socket;
/*     */ import java.rmi.ConnectIOException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.RuntimeUtil;
/*     */ import sun.rmi.runtime.RuntimeUtil.GetInstanceAction;
/*     */ import sun.rmi.transport.Channel;
/*     */ import sun.rmi.transport.Connection;
/*     */ import sun.rmi.transport.Endpoint;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.action.GetLongAction;
/*     */ 
/*     */ public class TCPChannel
/*     */   implements Channel
/*     */ {
/*     */   private final TCPEndpoint ep;
/*     */   private final TCPTransport tr;
/*  66 */   private final List<TCPConnection> freeList = new ArrayList();
/*     */ 
/*  69 */   private Future<?> reaper = null;
/*     */ 
/*  72 */   private boolean usingMultiplexer = false;
/*     */ 
/*  74 */   private ConnectionMultiplexer multiplexer = null;
/*     */   private ConnectionAcceptor acceptor;
/*     */   private AccessControlContext okContext;
/*     */   private WeakHashMap<AccessControlContext, Reference<AccessControlContext>> authcache;
/*  86 */   private SecurityManager cacheSecurityManager = null;
/*     */ 
/*  89 */   private static final long idleTimeout = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.connectionTimeout", 15000L))).longValue();
/*     */ 
/*  94 */   private static final int handshakeTimeout = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.handshakeTimeout", 60000))).intValue();
/*     */ 
/* 100 */   private static final int responseTimeout = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.responseTimeout", 0))).intValue();
/*     */ 
/* 105 */   private static final ScheduledExecutorService scheduler = ((RuntimeUtil)AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
/*     */ 
/*     */   TCPChannel(TCPTransport paramTCPTransport, TCPEndpoint paramTCPEndpoint)
/*     */   {
/* 113 */     this.tr = paramTCPTransport;
/* 114 */     this.ep = paramTCPEndpoint;
/*     */   }
/*     */ 
/*     */   public Endpoint getEndpoint()
/*     */   {
/* 121 */     return this.ep;
/*     */   }
/*     */ 
/*     */   private void checkConnectPermission()
/*     */     throws SecurityException
/*     */   {
/* 131 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 132 */     if (localSecurityManager == null) {
/* 133 */       return;
/*     */     }
/* 135 */     if (localSecurityManager != this.cacheSecurityManager)
/*     */     {
/* 137 */       this.okContext = null;
/* 138 */       this.authcache = new WeakHashMap();
/*     */ 
/* 140 */       this.cacheSecurityManager = localSecurityManager;
/*     */     }
/*     */ 
/* 143 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */ 
/* 147 */     if ((this.okContext == null) || ((!this.okContext.equals(localAccessControlContext)) && (!this.authcache.containsKey(localAccessControlContext))))
/*     */     {
/* 150 */       localSecurityManager.checkConnect(this.ep.getHost(), this.ep.getPort());
/* 151 */       this.authcache.put(localAccessControlContext, new SoftReference(localAccessControlContext));
/*     */     }
/*     */ 
/* 155 */     this.okContext = localAccessControlContext;
/*     */   }
/*     */ 
/*     */   public Connection newConnection()
/*     */     throws RemoteException
/*     */   {
/*     */     TCPConnection localTCPConnection;
/*     */     do
/*     */     {
/* 170 */       localTCPConnection = null;
/*     */ 
/* 172 */       synchronized (this.freeList) {
/* 173 */         int i = this.freeList.size() - 1;
/*     */ 
/* 175 */         if (i >= 0)
/*     */         {
/* 179 */           checkConnectPermission();
/* 180 */           localTCPConnection = (TCPConnection)this.freeList.get(i);
/* 181 */           this.freeList.remove(i);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 189 */       if (localTCPConnection != null)
/*     */       {
/* 191 */         if (!localTCPConnection.isDead()) {
/* 192 */           TCPTransport.tcpLog.log(Log.BRIEF, "reuse connection");
/* 193 */           return localTCPConnection;
/*     */         }
/*     */ 
/* 197 */         free(localTCPConnection, false);
/*     */       }
/*     */     }
/* 199 */     while (localTCPConnection != null);
/*     */ 
/* 202 */     return createConnection();
/*     */   }
/*     */ 
/*     */   private Connection createConnection()
/*     */     throws RemoteException
/*     */   {
/* 213 */     TCPTransport.tcpLog.log(Log.BRIEF, "create connection");
/*     */     TCPConnection localTCPConnection;
/* 215 */     if (!this.usingMultiplexer) {
/* 216 */       Socket localSocket = this.ep.newSocket();
/* 217 */       localTCPConnection = new TCPConnection(this, localSocket);
/*     */       try
/*     */       {
/* 220 */         DataOutputStream localDataOutputStream = new DataOutputStream(localTCPConnection.getOutputStream());
/*     */ 
/* 222 */         writeTransportHeader(localDataOutputStream);
/*     */ 
/* 225 */         if (!localTCPConnection.isReusable()) {
/* 226 */           localDataOutputStream.writeByte(76);
/*     */         } else {
/* 228 */           localDataOutputStream.writeByte(75);
/* 229 */           localDataOutputStream.flush();
/*     */ 
/* 236 */           int i = 0;
/*     */           try {
/* 238 */             i = localSocket.getSoTimeout();
/* 239 */             localSocket.setSoTimeout(handshakeTimeout);
/*     */           }
/*     */           catch (Exception localException1)
/*     */           {
/*     */           }
/* 244 */           DataInputStream localDataInputStream = new DataInputStream(localTCPConnection.getInputStream());
/*     */ 
/* 246 */           int j = localDataInputStream.readByte();
/* 247 */           if (j != 78) {
/* 248 */             throw new ConnectIOException(j == 79 ? "JRMP StreamProtocol not supported by server" : "non-JRMP server at remote endpoint");
/*     */           }
/*     */ 
/* 254 */           String str = localDataInputStream.readUTF();
/* 255 */           int k = localDataInputStream.readInt();
/* 256 */           if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 257 */             TCPTransport.tcpLog.log(Log.VERBOSE, "server suggested " + str + ":" + k);
/*     */           }
/*     */ 
/* 263 */           TCPEndpoint.setLocalHost(str);
/*     */ 
/* 269 */           TCPEndpoint localTCPEndpoint = TCPEndpoint.getLocalEndpoint(0, null, null);
/*     */ 
/* 271 */           localDataOutputStream.writeUTF(localTCPEndpoint.getHost());
/* 272 */           localDataOutputStream.writeInt(localTCPEndpoint.getPort());
/* 273 */           if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 274 */             TCPTransport.tcpLog.log(Log.VERBOSE, "using " + localTCPEndpoint.getHost() + ":" + localTCPEndpoint.getPort());
/*     */           }
/*     */ 
/*     */           try
/*     */           {
/* 291 */             localSocket.setSoTimeout(i != 0 ? i : responseTimeout);
/*     */           }
/*     */           catch (Exception localException2)
/*     */           {
/*     */           }
/*     */ 
/* 298 */           localDataOutputStream.flush();
/*     */         }
/*     */       } catch (IOException localIOException2) {
/* 301 */         if ((localIOException2 instanceof RemoteException)) {
/* 302 */           throw ((RemoteException)localIOException2);
/*     */         }
/* 304 */         throw new ConnectIOException("error during JRMP connection establishment", localIOException2);
/*     */       }
/*     */     }
/*     */     else {
/*     */       try {
/* 309 */         localTCPConnection = this.multiplexer.openConnection();
/*     */       } catch (IOException localIOException1) {
/* 311 */         synchronized (this) {
/* 312 */           this.usingMultiplexer = false;
/* 313 */           this.multiplexer = null;
/*     */         }
/* 315 */         throw new ConnectIOException("error opening virtual connection over multiplexed connection", localIOException1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 320 */     return localTCPConnection;
/*     */   }
/*     */ 
/*     */   public void free(Connection paramConnection, boolean paramBoolean)
/*     */   {
/* 330 */     if (paramConnection == null) return;
/*     */ 
/* 332 */     if ((paramBoolean) && (paramConnection.isReusable())) {
/* 333 */       long l = System.currentTimeMillis();
/* 334 */       TCPConnection localTCPConnection = (TCPConnection)paramConnection;
/*     */ 
/* 336 */       TCPTransport.tcpLog.log(Log.BRIEF, "reuse connection");
/*     */ 
/* 342 */       synchronized (this.freeList) {
/* 343 */         this.freeList.add(localTCPConnection);
/* 344 */         if (this.reaper == null) {
/* 345 */           TCPTransport.tcpLog.log(Log.BRIEF, "create reaper");
/*     */ 
/* 347 */           this.reaper = scheduler.scheduleWithFixedDelay(new Runnable()
/*     */           {
/*     */             public void run() {
/* 350 */               TCPTransport.tcpLog.log(Log.VERBOSE, "wake up");
/*     */ 
/* 352 */               TCPChannel.this.freeCachedConnections();
/*     */             }
/*     */           }
/*     */           , idleTimeout, idleTimeout, TimeUnit.MILLISECONDS);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 358 */       localTCPConnection.setLastUseTime(l);
/* 359 */       localTCPConnection.setExpiration(l + idleTimeout);
/*     */     } else {
/* 361 */       TCPTransport.tcpLog.log(Log.BRIEF, "close connection");
/*     */       try
/*     */       {
/* 364 */         paramConnection.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeTransportHeader(DataOutputStream paramDataOutputStream)
/*     */     throws RemoteException
/*     */   {
/*     */     try
/*     */     {
/* 378 */       DataOutputStream localDataOutputStream = new DataOutputStream(paramDataOutputStream);
/*     */ 
/* 380 */       localDataOutputStream.writeInt(1246907721);
/* 381 */       localDataOutputStream.writeShort(2);
/*     */     } catch (IOException localIOException) {
/* 383 */       throw new ConnectIOException("error writing JRMP transport header", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void useMultiplexer(ConnectionMultiplexer paramConnectionMultiplexer)
/*     */   {
/* 394 */     this.multiplexer = paramConnectionMultiplexer;
/*     */ 
/* 396 */     this.usingMultiplexer = true;
/*     */   }
/*     */ 
/*     */   void acceptMultiplexConnection(Connection paramConnection)
/*     */   {
/* 403 */     if (this.acceptor == null) {
/* 404 */       this.acceptor = new ConnectionAcceptor(this.tr);
/* 405 */       this.acceptor.startNewAcceptor();
/*     */     }
/* 407 */     this.acceptor.accept(paramConnection);
/*     */   }
/*     */ 
/*     */   public void shedCache()
/*     */   {
/*     */     Connection[] arrayOfConnection;
/* 417 */     synchronized (this.freeList) {
/* 418 */       arrayOfConnection = (Connection[])this.freeList.toArray(new Connection[this.freeList.size()]);
/* 419 */       this.freeList.clear();
/*     */     }
/*     */ 
/* 423 */     int i = arrayOfConnection.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 424 */       Connection localConnection = arrayOfConnection[i];
/* 425 */       arrayOfConnection[i] = null;
/*     */       try {
/* 427 */         localConnection.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void freeCachedConnections()
/*     */   {
/* 438 */     synchronized (this.freeList) {
/* 439 */       int i = this.freeList.size();
/*     */ 
/* 441 */       if (i > 0) {
/* 442 */         long l = System.currentTimeMillis();
/* 443 */         ListIterator localListIterator = this.freeList.listIterator(i);
/*     */ 
/* 445 */         while (localListIterator.hasPrevious()) {
/* 446 */           TCPConnection localTCPConnection = (TCPConnection)localListIterator.previous();
/* 447 */           if (localTCPConnection.expired(l)) {
/* 448 */             TCPTransport.tcpLog.log(Log.VERBOSE, "connection timeout expired");
/*     */             try
/*     */             {
/* 452 */               localTCPConnection.close();
/*     */             }
/*     */             catch (IOException localIOException) {
/*     */             }
/* 456 */             localListIterator.remove();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 461 */       if (this.freeList.isEmpty()) {
/* 462 */         this.reaper.cancel(false);
/* 463 */         this.reaper = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.TCPChannel
 * JD-Core Version:    0.6.2
 */