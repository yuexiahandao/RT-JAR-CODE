/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.rmi.server.RMISocketFactory;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetLongAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class RMIMasterSocketFactory extends RMISocketFactory
/*     */ {
/*  50 */   static int logLevel = LogStream.parseLevel(getLogLevel());
/*     */ 
/*  58 */   static final Log proxyLog = Log.getLog("sun.rmi.transport.tcp.proxy", "transport", logLevel);
/*     */ 
/*  63 */   private static long connectTimeout = getConnectTimeout();
/*     */ 
/*  72 */   private static final boolean eagerHttpFallback = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.rmi.transport.proxy.eagerHttpFallback"))).booleanValue();
/*     */ 
/*  77 */   private Hashtable<String, RMISocketFactory> successTable = new Hashtable();
/*     */   private static final int MaxRememberedHosts = 64;
/*  84 */   private Vector<String> hostList = new Vector(64);
/*     */ 
/*  87 */   protected RMISocketFactory initialFactory = new RMIDirectSocketFactory();
/*     */   protected Vector<RMISocketFactory> altFactoryList;
/*     */ 
/*     */   private static String getLogLevel()
/*     */   {
/*  53 */     return (String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.proxy.logLevel"));
/*     */   }
/*     */ 
/*     */   private static long getConnectTimeout()
/*     */   {
/*  66 */     return ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.proxy.connectTimeout", 15000L))).longValue();
/*     */   }
/*     */ 
/*     */   public RMIMasterSocketFactory()
/*     */   {
/*  99 */     this.altFactoryList = new Vector(2);
/* 100 */     int i = 0;
/*     */     try
/*     */     {
/* 104 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("http.proxyHost"));
/*     */ 
/* 107 */       if (str == null) {
/* 108 */         str = (String)AccessController.doPrivileged(new GetPropertyAction("proxyHost"));
/*     */       }
/*     */ 
/* 111 */       Boolean localBoolean = (Boolean)AccessController.doPrivileged(new GetBooleanAction("java.rmi.server.disableHttp"));
/*     */ 
/* 114 */       if ((!localBoolean.booleanValue()) && (str != null) && (str.length() > 0))
/*     */       {
/* 116 */         i = 1;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 120 */       i = 1;
/*     */     }
/*     */ 
/* 123 */     if (i != 0) {
/* 124 */       this.altFactoryList.addElement(new RMIHttpToPortSocketFactory());
/* 125 */       this.altFactoryList.addElement(new RMIHttpToCGISocketFactory());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 138 */     if (proxyLog.isLoggable(Log.BRIEF)) {
/* 139 */       proxyLog.log(Log.BRIEF, "host: " + paramString + ", port: " + paramInt);
/*     */     }
/*     */ 
/* 146 */     if (this.altFactoryList.size() == 0) {
/* 147 */       return this.initialFactory.createSocket(paramString, paramInt);
/*     */     }
/*     */ 
/* 156 */     RMISocketFactory localRMISocketFactory = (RMISocketFactory)this.successTable.get(paramString);
/* 157 */     if (localRMISocketFactory != null) {
/* 158 */       if (proxyLog.isLoggable(Log.BRIEF)) {
/* 159 */         proxyLog.log(Log.BRIEF, "previously successful factory found: " + localRMISocketFactory);
/*     */       }
/*     */ 
/* 162 */       return localRMISocketFactory.createSocket(paramString, paramInt);
/*     */     }
/*     */ 
/* 170 */     Socket localSocket1 = null;
/* 171 */     Socket localSocket2 = null;
/* 172 */     AsyncConnector localAsyncConnector = new AsyncConnector(this.initialFactory, paramString, paramInt, AccessController.getContext());
/*     */ 
/* 177 */     Object localObject1 = null;
/*     */     try
/*     */     {
/* 180 */       synchronized (localAsyncConnector)
/*     */       {
/* 182 */         Thread localThread = (Thread)AccessController.doPrivileged(new NewThreadAction(localAsyncConnector, "AsyncConnector", true));
/*     */ 
/* 184 */         localThread.start();
/*     */         try
/*     */         {
/* 187 */           long l1 = System.currentTimeMillis();
/* 188 */           long l2 = l1 + connectTimeout;
/*     */           do {
/* 190 */             localAsyncConnector.wait(l2 - l1);
/* 191 */             localSocket1 = checkConnector(localAsyncConnector);
/* 192 */             if (localSocket1 != null)
/*     */               break;
/* 194 */             l1 = System.currentTimeMillis();
/* 195 */           }while (l1 < l2);
/*     */         } catch (InterruptedException localInterruptedException) {
/* 197 */           throw new InterruptedIOException("interrupted while waiting for connector");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 203 */       if (localSocket1 == null) {
/* 204 */         throw new NoRouteToHostException("connect timed out: " + paramString);
/*     */       }
/*     */ 
/* 207 */       proxyLog.log(Log.BRIEF, "direct socket connection successful");
/*     */ 
/* 209 */       ??? = localSocket1;
/*     */ 
/* 220 */       if (localObject1 != null)
/*     */       {
/* 222 */         if (proxyLog.isLoggable(Log.BRIEF)) {
/* 223 */           proxyLog.log(Log.BRIEF, "direct socket connection failed: ", (Throwable)localObject1);
/*     */         }
/*     */ 
/* 228 */         for (int i = 0; i < this.altFactoryList.size(); i++) {
/* 229 */           localRMISocketFactory = (RMISocketFactory)this.altFactoryList.elementAt(i);
/* 230 */           if (proxyLog.isLoggable(Log.BRIEF))
/* 231 */             proxyLog.log(Log.BRIEF, "trying with factory: " + localRMISocketFactory);
/*     */           try
/*     */           {
/* 234 */             Socket localSocket5 = localRMISocketFactory.createSocket(paramString, paramInt); localObject3 = null;
/*     */             try
/*     */             {
/* 241 */               InputStream localInputStream2 = localSocket5.getInputStream();
/* 242 */               int k = localInputStream2.read();
/*     */             }
/*     */             catch (Throwable localThrowable6)
/*     */             {
/* 234 */               localObject3 = localThrowable6;
/*     */               throw localThrowable6;
/*     */             }
/*     */             finally
/*     */             {
/* 243 */               if (localSocket5 != null) if (localObject3 != null) try { localSocket5.close(); } catch (Throwable localThrowable7) { ((Throwable)localObject3).addSuppressed(localThrowable7); } else localSocket5.close();  
/*     */             } } catch (IOException localIOException5) { if (proxyLog.isLoggable(Log.BRIEF)) {
/* 245 */               proxyLog.log(Log.BRIEF, "factory failed: ", localIOException5);
/*     */             }
/*     */ 
/* 248 */             continue;
/*     */           }
/* 250 */           proxyLog.log(Log.BRIEF, "factory succeeded");
/*     */           try
/*     */           {
/* 254 */             localSocket2 = localRMISocketFactory.createSocket(paramString, paramInt);
/*     */           }
/*     */           catch (IOException localIOException6)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/* 228 */       return ???;
/*     */     }
/*     */     catch (UnknownHostException|NoRouteToHostException )
/*     */     {
/*     */       Object localObject3;
/* 212 */       localObject1 = ???;
/*     */ 
/* 220 */       if (localObject1 != null)
/*     */       {
/* 222 */         if (proxyLog.isLoggable(Log.BRIEF)) {
/* 223 */           proxyLog.log(Log.BRIEF, "direct socket connection failed: ", (Throwable)localObject1);
/*     */         }
/*     */ 
/* 228 */         for (??? = 0; ??? < this.altFactoryList.size(); ???++) {
/* 229 */           localRMISocketFactory = (RMISocketFactory)this.altFactoryList.elementAt(???);
/* 230 */           if (proxyLog.isLoggable(Log.BRIEF))
/* 231 */             proxyLog.log(Log.BRIEF, "trying with factory: " + localRMISocketFactory);
/*     */           try
/*     */           {
/* 234 */             Socket localSocket3 = localRMISocketFactory.createSocket(paramString, paramInt); localObject2 = null;
/*     */             try
/*     */             {
/* 241 */               localObject3 = localSocket3.getInputStream();
/* 242 */               j = ((InputStream)localObject3).read();
/*     */             }
/*     */             catch (Throwable localThrowable2)
/*     */             {
/* 234 */               localObject2 = localThrowable2;
/*     */               throw localThrowable2;
/*     */             }
/*     */             finally
/*     */             {
/* 243 */               if (localSocket3 != null) if (localObject2 != null) try { localSocket3.close(); } catch (Throwable localThrowable8) { localObject2.addSuppressed(localThrowable8); } else localSocket3.close();  
/*     */             } } catch (IOException localIOException1) { if (proxyLog.isLoggable(Log.BRIEF)) {
/* 245 */               proxyLog.log(Log.BRIEF, "factory failed: ", localIOException1);
/*     */             }
/*     */ 
/* 248 */             continue;
/*     */           }
/* 250 */           proxyLog.log(Log.BRIEF, "factory succeeded");
/*     */           try
/*     */           {
/* 254 */             localSocket2 = localRMISocketFactory.createSocket(paramString, paramInt);
/*     */           }
/*     */           catch (IOException localIOException2)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SocketException )
/*     */     {
/*     */       Object localObject2;
/*     */       int j;
/* 214 */       if (eagerHttpFallback)
/* 215 */         localObject1 = ???;
/*     */       else {
/* 217 */         throw ???;
/*     */       }
/*     */ 
/* 220 */       if (localObject1 != null)
/*     */       {
/* 222 */         if (proxyLog.isLoggable(Log.BRIEF)) {
/* 223 */           proxyLog.log(Log.BRIEF, "direct socket connection failed: ", (Throwable)localObject1);
/*     */         }
/*     */ 
/* 228 */         for (??? = 0; ??? < this.altFactoryList.size(); ???++) {
/* 229 */           localRMISocketFactory = (RMISocketFactory)this.altFactoryList.elementAt(???);
/* 230 */           if (proxyLog.isLoggable(Log.BRIEF))
/* 231 */             proxyLog.log(Log.BRIEF, "trying with factory: " + localRMISocketFactory);
/*     */           try
/*     */           {
/* 234 */             Socket localSocket4 = localRMISocketFactory.createSocket(paramString, paramInt); localObject2 = null;
/*     */             try
/*     */             {
/* 241 */               InputStream localInputStream1 = localSocket4.getInputStream();
/* 242 */               j = localInputStream1.read();
/*     */             }
/*     */             catch (Throwable localThrowable4)
/*     */             {
/* 234 */               localObject2 = localThrowable4;
/*     */               throw localThrowable4;
/*     */             }
/*     */             finally
/*     */             {
/* 243 */               if (localSocket4 != null) if (localObject2 != null) try { localSocket4.close(); } catch (Throwable localThrowable9) { localObject2.addSuppressed(localThrowable9); } else localSocket4.close();  
/*     */             } } catch (IOException localIOException3) { if (proxyLog.isLoggable(Log.BRIEF)) {
/* 245 */               proxyLog.log(Log.BRIEF, "factory failed: ", localIOException3);
/*     */             }
/*     */ 
/* 248 */             continue;
/*     */           }
/* 250 */           proxyLog.log(Log.BRIEF, "factory succeeded");
/*     */           try
/*     */           {
/* 254 */             localSocket2 = localRMISocketFactory.createSocket(paramString, paramInt);
/*     */           }
/*     */           catch (IOException localIOException4)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 220 */       if (localObject1 != null)
/*     */       {
/* 222 */         if (proxyLog.isLoggable(Log.BRIEF)) {
/* 223 */           proxyLog.log(Log.BRIEF, "direct socket connection failed: ", (Throwable)localObject1);
/*     */         }
/*     */ 
/* 228 */         for (int m = 0; m < this.altFactoryList.size(); m++) {
/* 229 */           localRMISocketFactory = (RMISocketFactory)this.altFactoryList.elementAt(m);
/* 230 */           if (proxyLog.isLoggable(Log.BRIEF))
/* 231 */             proxyLog.log(Log.BRIEF, "trying with factory: " + localRMISocketFactory);
/*     */           try
/*     */           {
/* 234 */             Socket localSocket6 = localRMISocketFactory.createSocket(paramString, paramInt); Object localObject9 = null;
/*     */             try
/*     */             {
/* 241 */               InputStream localInputStream3 = localSocket6.getInputStream();
/* 242 */               int n = localInputStream3.read();
/*     */             }
/*     */             catch (Throwable localThrowable11)
/*     */             {
/* 234 */               localObject9 = localThrowable11; throw localThrowable11;
/*     */             }
/*     */             finally
/*     */             {
/* 243 */               if (localSocket6 != null) if (localObject9 != null) try { localSocket6.close(); } catch (Throwable localThrowable12) { localObject9.addSuppressed(localThrowable12); } else localSocket6.close();  
/*     */             } } catch (IOException localIOException7) { if (proxyLog.isLoggable(Log.BRIEF)) {
/* 245 */               proxyLog.log(Log.BRIEF, "factory failed: ", localIOException7);
/*     */             }
/*     */ 
/* 248 */             continue;
/*     */           }
/* 250 */           proxyLog.log(Log.BRIEF, "factory succeeded");
/*     */           try
/*     */           {
/* 254 */             localSocket2 = localRMISocketFactory.createSocket(paramString, paramInt);
/*     */           }
/*     */           catch (IOException localIOException8)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 262 */     synchronized (this.successTable)
/*     */     {
/*     */       try {
/* 265 */         synchronized (localAsyncConnector) {
/* 266 */           localSocket1 = checkConnector(localAsyncConnector);
/*     */         }
/* 268 */         if (localSocket1 != null)
/*     */         {
/* 270 */           if (localSocket2 != null)
/* 271 */             localSocket2.close();
/* 272 */           return localSocket1;
/*     */         }
/*     */ 
/* 275 */         localAsyncConnector.notUsed();
/*     */       } catch (UnknownHostException|NoRouteToHostException localUnknownHostException) {
/* 277 */         localObject1 = localUnknownHostException;
/*     */       } catch (SocketException localSocketException) {
/* 279 */         if (eagerHttpFallback)
/* 280 */           localObject1 = localSocketException;
/*     */         else {
/* 282 */           throw localSocketException;
/*     */         }
/*     */       }
/*     */ 
/* 286 */       if (localSocket2 != null)
/*     */       {
/* 288 */         rememberFactory(paramString, localRMISocketFactory);
/* 289 */         return localSocket2;
/*     */       }
/* 291 */       throw ((Throwable)localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   void rememberFactory(String paramString, RMISocketFactory paramRMISocketFactory)
/*     */   {
/* 301 */     synchronized (this.successTable) {
/* 302 */       while (this.hostList.size() >= 64) {
/* 303 */         this.successTable.remove(this.hostList.elementAt(0));
/* 304 */         this.hostList.removeElementAt(0);
/*     */       }
/* 306 */       this.hostList.addElement(paramString);
/* 307 */       this.successTable.put(paramString, paramRMISocketFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   Socket checkConnector(AsyncConnector paramAsyncConnector)
/*     */     throws IOException
/*     */   {
/* 318 */     Exception localException = paramAsyncConnector.getException();
/* 319 */     if (localException != null) {
/* 320 */       localException.fillInStackTrace();
/*     */ 
/* 327 */       if ((localException instanceof IOException))
/* 328 */         throw ((IOException)localException);
/* 329 */       if ((localException instanceof RuntimeException)) {
/* 330 */         throw ((RuntimeException)localException);
/*     */       }
/* 332 */       throw new Error("internal error: unexpected checked exception: " + localException.toString());
/*     */     }
/*     */ 
/* 336 */     return paramAsyncConnector.getSocket();
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 344 */     return this.initialFactory.createServerSocket(paramInt);
/*     */   }
/*     */ 
/*     */   private class AsyncConnector
/*     */     implements Runnable
/*     */   {
/*     */     private RMISocketFactory factory;
/*     */     private String host;
/*     */     private int port;
/*     */     private AccessControlContext acc;
/* 368 */     private Exception exception = null;
/*     */ 
/* 371 */     private Socket socket = null;
/*     */ 
/* 374 */     private boolean cleanUp = false;
/*     */ 
/*     */     AsyncConnector(RMISocketFactory paramString, String paramInt, int paramAccessControlContext, AccessControlContext arg5)
/*     */     {
/* 382 */       this.factory = paramString;
/* 383 */       this.host = paramInt;
/* 384 */       this.port = paramAccessControlContext;
/*     */       Object localObject;
/* 385 */       this.acc = localObject;
/* 386 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 387 */       if (localSecurityManager != null)
/* 388 */         localSecurityManager.checkConnect(paramInt, paramAccessControlContext);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/* 408 */           Socket localSocket = this.factory.createSocket(this.host, this.port);
/* 409 */           synchronized (this) {
/* 410 */             this.socket = localSocket;
/* 411 */             notify();
/*     */           }
/* 413 */           RMIMasterSocketFactory.this.rememberFactory(this.host, this.factory);
/* 414 */           synchronized (this) {
/* 415 */             if (this.cleanUp)
/*     */               try {
/* 417 */                 this.socket.close();
/*     */               }
/*     */               catch (IOException localIOException)
/*     */               {
/*     */               }
/*     */           }
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/* 426 */           synchronized (this) {
/* 427 */             this.exception = localException;
/* 428 */             notify();
/*     */           }
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     private synchronized Exception getException()
/*     */     {
/* 447 */       return this.exception;
/*     */     }
/*     */ 
/*     */     private synchronized Socket getSocket()
/*     */     {
/* 454 */       return this.socket;
/*     */     }
/*     */ 
/*     */     synchronized void notUsed()
/*     */     {
/* 462 */       if (this.socket != null)
/*     */         try {
/* 464 */           this.socket.close();
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/* 468 */       this.cleanUp = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.RMIMasterSocketFactory
 * JD-Core Version:    0.6.2
 */