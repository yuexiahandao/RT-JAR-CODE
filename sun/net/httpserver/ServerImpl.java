/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.Filter.Chain;
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.net.httpserver.HttpsConfigurator;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.BindException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ 
/*     */ class ServerImpl
/*     */   implements TimeSource
/*     */ {
/*     */   private String protocol;
/*     */   private boolean https;
/*     */   private Executor executor;
/*     */   private HttpsConfigurator httpsConfig;
/*     */   private SSLContext sslContext;
/*     */   private ContextList contexts;
/*     */   private InetSocketAddress address;
/*     */   private ServerSocketChannel schan;
/*     */   private Selector selector;
/*     */   private SelectionKey listenerKey;
/*     */   private Set<HttpConnection> idleConnections;
/*     */   private Set<HttpConnection> allConnections;
/*     */   private Set<HttpConnection> reqConnections;
/*     */   private Set<HttpConnection> rspConnections;
/*     */   private List<Event> events;
/*  65 */   private Object lolock = new Object();
/*  66 */   private volatile boolean finished = false;
/*  67 */   private volatile boolean terminating = false;
/*  68 */   private boolean bound = false;
/*  69 */   private boolean started = false;
/*     */   private volatile long time;
/*  71 */   private volatile long subticks = 0L;
/*     */   private volatile long ticks;
/*     */   private HttpServer wrapper;
/*  75 */   static final int CLOCK_TICK = ServerConfig.getClockTick();
/*  76 */   static final long IDLE_INTERVAL = ServerConfig.getIdleInterval();
/*  77 */   static final int MAX_IDLE_CONNECTIONS = ServerConfig.getMaxIdleConnections();
/*  78 */   static final long TIMER_MILLIS = ServerConfig.getTimerMillis();
/*  79 */   static final long MAX_REQ_TIME = getTimeMillis(ServerConfig.getMaxReqTime());
/*  80 */   static final long MAX_RSP_TIME = getTimeMillis(ServerConfig.getMaxRspTime());
/*  81 */   static final boolean timer1Enabled = (MAX_REQ_TIME != -1L) || (MAX_RSP_TIME != -1L);
/*     */   private Timer timer;
/*     */   private Timer timer1;
/*     */   private Logger logger;
/*     */   Dispatcher dispatcher;
/* 444 */   static boolean debug = ServerConfig.debugEnabled();
/*     */ 
/* 758 */   private int exchangeCount = 0;
/*     */ 
/*     */   ServerImpl(HttpServer paramHttpServer, String paramString, InetSocketAddress paramInetSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/*  90 */     this.protocol = paramString;
/*  91 */     this.wrapper = paramHttpServer;
/*  92 */     this.logger = Logger.getLogger("com.sun.net.httpserver");
/*  93 */     ServerConfig.checkLegacyProperties(this.logger);
/*  94 */     this.https = paramString.equalsIgnoreCase("https");
/*  95 */     this.address = paramInetSocketAddress;
/*  96 */     this.contexts = new ContextList();
/*  97 */     this.schan = ServerSocketChannel.open();
/*  98 */     if (paramInetSocketAddress != null) {
/*  99 */       ServerSocket localServerSocket = this.schan.socket();
/* 100 */       localServerSocket.bind(paramInetSocketAddress, paramInt);
/* 101 */       this.bound = true;
/*     */     }
/* 103 */     this.selector = Selector.open();
/* 104 */     this.schan.configureBlocking(false);
/* 105 */     this.listenerKey = this.schan.register(this.selector, 16);
/* 106 */     this.dispatcher = new Dispatcher();
/* 107 */     this.idleConnections = Collections.synchronizedSet(new HashSet());
/* 108 */     this.allConnections = Collections.synchronizedSet(new HashSet());
/* 109 */     this.reqConnections = Collections.synchronizedSet(new HashSet());
/* 110 */     this.rspConnections = Collections.synchronizedSet(new HashSet());
/* 111 */     this.time = System.currentTimeMillis();
/* 112 */     this.timer = new Timer("server-timer", true);
/* 113 */     this.timer.schedule(new ServerTimerTask(), CLOCK_TICK, CLOCK_TICK);
/* 114 */     if (timer1Enabled) {
/* 115 */       this.timer1 = new Timer("server-timer1", true);
/* 116 */       this.timer1.schedule(new ServerTimerTask1(), TIMER_MILLIS, TIMER_MILLIS);
/* 117 */       this.logger.config("HttpServer timer1 enabled period in ms:  " + TIMER_MILLIS);
/* 118 */       this.logger.config("MAX_REQ_TIME:  " + MAX_REQ_TIME);
/* 119 */       this.logger.config("MAX_RSP_TIME:  " + MAX_RSP_TIME);
/*     */     }
/* 121 */     this.events = new LinkedList();
/* 122 */     this.logger.config("HttpServer created " + paramString + " " + paramInetSocketAddress);
/*     */   }
/*     */ 
/*     */   public void bind(InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
/* 126 */     if (this.bound) {
/* 127 */       throw new BindException("HttpServer already bound");
/*     */     }
/* 129 */     if (paramInetSocketAddress == null) {
/* 130 */       throw new NullPointerException("null address");
/*     */     }
/* 132 */     ServerSocket localServerSocket = this.schan.socket();
/* 133 */     localServerSocket.bind(paramInetSocketAddress, paramInt);
/* 134 */     this.bound = true;
/*     */   }
/*     */ 
/*     */   public void start() {
/* 138 */     if ((!this.bound) || (this.started) || (this.finished)) {
/* 139 */       throw new IllegalStateException("server in wrong state");
/*     */     }
/* 141 */     if (this.executor == null) {
/* 142 */       this.executor = new DefaultExecutor(null);
/*     */     }
/* 144 */     Thread localThread = new Thread(this.dispatcher);
/* 145 */     this.started = true;
/* 146 */     localThread.start();
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor paramExecutor) {
/* 150 */     if (this.started) {
/* 151 */       throw new IllegalStateException("server already started");
/*     */     }
/* 153 */     this.executor = paramExecutor;
/*     */   }
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 163 */     return this.executor;
/*     */   }
/*     */ 
/*     */   public void setHttpsConfigurator(HttpsConfigurator paramHttpsConfigurator) {
/* 167 */     if (paramHttpsConfigurator == null) {
/* 168 */       throw new NullPointerException("null HttpsConfigurator");
/*     */     }
/* 170 */     if (this.started) {
/* 171 */       throw new IllegalStateException("server already started");
/*     */     }
/* 173 */     this.httpsConfig = paramHttpsConfigurator;
/* 174 */     this.sslContext = paramHttpsConfigurator.getSSLContext();
/*     */   }
/*     */ 
/*     */   public HttpsConfigurator getHttpsConfigurator() {
/* 178 */     return this.httpsConfig;
/*     */   }
/*     */ 
/*     */   public void stop(int paramInt) {
/* 182 */     if (paramInt < 0) {
/* 183 */       throw new IllegalArgumentException("negative delay parameter");
/*     */     }
/* 185 */     this.terminating = true;
/*     */     try { this.schan.close(); } catch (IOException localIOException) {
/* 187 */     }this.selector.wakeup();
/* 188 */     long l = System.currentTimeMillis() + paramInt * 1000;
/* 189 */     while (System.currentTimeMillis() < l) {
/* 190 */       delay();
/* 191 */       if (this.finished) {
/* 192 */         break;
/*     */       }
/*     */     }
/* 195 */     this.finished = true;
/* 196 */     this.selector.wakeup();
/* 197 */     synchronized (this.allConnections) {
/* 198 */       for (HttpConnection localHttpConnection : this.allConnections) {
/* 199 */         localHttpConnection.close();
/*     */       }
/*     */     }
/* 202 */     this.allConnections.clear();
/* 203 */     this.idleConnections.clear();
/* 204 */     this.timer.cancel();
/* 205 */     if (timer1Enabled)
/* 206 */       this.timer1.cancel();
/*     */   }
/*     */ 
/*     */   public synchronized HttpContextImpl createContext(String paramString, HttpHandler paramHttpHandler)
/*     */   {
/* 213 */     if ((paramHttpHandler == null) || (paramString == null)) {
/* 214 */       throw new NullPointerException("null handler, or path parameter");
/*     */     }
/* 216 */     HttpContextImpl localHttpContextImpl = new HttpContextImpl(this.protocol, paramString, paramHttpHandler, this);
/* 217 */     this.contexts.add(localHttpContextImpl);
/* 218 */     this.logger.config("context created: " + paramString);
/* 219 */     return localHttpContextImpl;
/*     */   }
/*     */ 
/*     */   public synchronized HttpContextImpl createContext(String paramString) {
/* 223 */     if (paramString == null) {
/* 224 */       throw new NullPointerException("null path parameter");
/*     */     }
/* 226 */     HttpContextImpl localHttpContextImpl = new HttpContextImpl(this.protocol, paramString, null, this);
/* 227 */     this.contexts.add(localHttpContextImpl);
/* 228 */     this.logger.config("context created: " + paramString);
/* 229 */     return localHttpContextImpl;
/*     */   }
/*     */ 
/*     */   public synchronized void removeContext(String paramString) throws IllegalArgumentException {
/* 233 */     if (paramString == null) {
/* 234 */       throw new NullPointerException("null path parameter");
/*     */     }
/* 236 */     this.contexts.remove(this.protocol, paramString);
/* 237 */     this.logger.config("context removed: " + paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void removeContext(HttpContext paramHttpContext) throws IllegalArgumentException {
/* 241 */     if (!(paramHttpContext instanceof HttpContextImpl)) {
/* 242 */       throw new IllegalArgumentException("wrong HttpContext type");
/*     */     }
/* 244 */     this.contexts.remove((HttpContextImpl)paramHttpContext);
/* 245 */     this.logger.config("context removed: " + paramHttpContext.getPath());
/*     */   }
/*     */ 
/*     */   public InetSocketAddress getAddress() {
/* 249 */     return (InetSocketAddress)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public InetSocketAddress run() {
/* 252 */         return (InetSocketAddress)ServerImpl.this.schan.socket().getLocalSocketAddress();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   Selector getSelector()
/*     */   {
/* 260 */     return this.selector;
/*     */   }
/*     */ 
/*     */   void addEvent(Event paramEvent) {
/* 264 */     synchronized (this.lolock) {
/* 265 */       this.events.add(paramEvent);
/* 266 */       this.selector.wakeup();
/*     */     }
/*     */   }
/*     */ 
/*     */   static synchronized void dprint(String paramString)
/*     */   {
/* 447 */     if (debug)
/* 448 */       System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   static synchronized void dprint(Exception paramException)
/*     */   {
/* 453 */     if (debug) {
/* 454 */       System.out.println(paramException);
/* 455 */       paramException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   Logger getLogger() {
/* 460 */     return this.logger;
/*     */   }
/*     */ 
/*     */   private void closeConnection(HttpConnection paramHttpConnection) {
/* 464 */     paramHttpConnection.close();
/* 465 */     this.allConnections.remove(paramHttpConnection);
/* 466 */     switch (2.$SwitchMap$sun$net$httpserver$HttpConnection$State[paramHttpConnection.getState().ordinal()]) {
/*     */     case 1:
/* 468 */       this.reqConnections.remove(paramHttpConnection);
/* 469 */       break;
/*     */     case 2:
/* 471 */       this.rspConnections.remove(paramHttpConnection);
/* 472 */       break;
/*     */     case 3:
/* 474 */       this.idleConnections.remove(paramHttpConnection);
/*     */     }
/*     */ 
/* 477 */     assert (!this.reqConnections.remove(paramHttpConnection));
/* 478 */     assert (!this.rspConnections.remove(paramHttpConnection));
/* 479 */     assert (!this.idleConnections.remove(paramHttpConnection));
/*     */   }
/*     */ 
/*     */   void logReply(int paramInt, String paramString1, String paramString2)
/*     */   {
/* 726 */     if (!this.logger.isLoggable(Level.FINE)) {
/* 727 */       return;
/*     */     }
/* 729 */     if (paramString2 == null)
/* 730 */       paramString2 = "";
/*     */     String str1;
/* 733 */     if (paramString1.length() > 80)
/* 734 */       str1 = paramString1.substring(0, 80) + "<TRUNCATED>";
/*     */     else {
/* 736 */       str1 = paramString1;
/*     */     }
/* 738 */     String str2 = str1 + " [" + paramInt + " " + Code.msg(paramInt) + "] (" + paramString2 + ")";
/*     */ 
/* 740 */     this.logger.fine(str2);
/*     */   }
/*     */ 
/*     */   long getTicks() {
/* 744 */     return this.ticks;
/*     */   }
/*     */ 
/*     */   public long getTime() {
/* 748 */     return this.time;
/*     */   }
/*     */ 
/*     */   void delay() {
/* 752 */     Thread.yield();
/*     */     try {
/* 754 */       Thread.sleep(200L);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void startExchange() {
/* 761 */     this.exchangeCount += 1;
/*     */   }
/*     */ 
/*     */   synchronized int endExchange() {
/* 765 */     this.exchangeCount -= 1;
/* 766 */     assert (this.exchangeCount >= 0);
/* 767 */     return this.exchangeCount;
/*     */   }
/*     */ 
/*     */   HttpServer getWrapper() {
/* 771 */     return this.wrapper;
/*     */   }
/*     */ 
/*     */   void requestStarted(HttpConnection paramHttpConnection) {
/* 775 */     paramHttpConnection.creationTime = getTime();
/* 776 */     paramHttpConnection.setState(HttpConnection.State.REQUEST);
/* 777 */     this.reqConnections.add(paramHttpConnection);
/*     */   }
/*     */ 
/*     */   void requestCompleted(HttpConnection paramHttpConnection)
/*     */   {
/* 788 */     assert (paramHttpConnection.getState() == HttpConnection.State.REQUEST);
/* 789 */     this.reqConnections.remove(paramHttpConnection);
/* 790 */     paramHttpConnection.rspStartedTime = getTime();
/* 791 */     this.rspConnections.add(paramHttpConnection);
/* 792 */     paramHttpConnection.setState(HttpConnection.State.RESPONSE);
/*     */   }
/*     */ 
/*     */   void responseCompleted(HttpConnection paramHttpConnection)
/*     */   {
/* 797 */     assert (paramHttpConnection.getState() == HttpConnection.State.RESPONSE);
/* 798 */     this.rspConnections.remove(paramHttpConnection);
/* 799 */     paramHttpConnection.setState(HttpConnection.State.IDLE);
/*     */   }
/*     */ 
/*     */   void logStackTrace(String paramString)
/*     */   {
/* 866 */     this.logger.finest(paramString);
/* 867 */     StringBuilder localStringBuilder = new StringBuilder();
/* 868 */     StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
/* 869 */     for (int i = 0; i < arrayOfStackTraceElement.length; i++) {
/* 870 */       localStringBuilder.append(arrayOfStackTraceElement[i].toString()).append("\n");
/*     */     }
/* 872 */     this.logger.finest(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   static long getTimeMillis(long paramLong) {
/* 876 */     if (paramLong == -1L) {
/* 877 */       return -1L;
/*     */     }
/* 879 */     return paramLong * 1000L;
/*     */   }
/*     */ 
/*     */   private static class DefaultExecutor
/*     */     implements Executor
/*     */   {
/*     */     public void execute(Runnable paramRunnable)
/*     */     {
/* 158 */       paramRunnable.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   class Dispatcher
/*     */     implements Runnable
/*     */   {
/* 310 */     final LinkedList<HttpConnection> connsToRegister = new LinkedList();
/*     */ 
/*     */     Dispatcher()
/*     */     {
/*     */     }
/*     */ 
/*     */     private void handleEvent(Event paramEvent)
/*     */     {
/* 275 */       ExchangeImpl localExchangeImpl = paramEvent.exchange;
/* 276 */       HttpConnection localHttpConnection = localExchangeImpl.getConnection();
/*     */       try {
/* 278 */         if ((paramEvent instanceof WriteFinishedEvent))
/*     */         {
/* 280 */           int i = ServerImpl.this.endExchange();
/* 281 */           if ((ServerImpl.this.terminating) && (i == 0)) {
/* 282 */             ServerImpl.this.finished = true;
/*     */           }
/* 284 */           ServerImpl.this.responseCompleted(localHttpConnection);
/* 285 */           LeftOverInputStream localLeftOverInputStream = localExchangeImpl.getOriginalInputStream();
/* 286 */           if (!localLeftOverInputStream.isEOF()) {
/* 287 */             localExchangeImpl.close = true;
/*     */           }
/* 289 */           if ((localExchangeImpl.close) || (ServerImpl.this.idleConnections.size() >= ServerImpl.MAX_IDLE_CONNECTIONS)) {
/* 290 */             localHttpConnection.close();
/* 291 */             ServerImpl.this.allConnections.remove(localHttpConnection);
/*     */           }
/* 293 */           else if (localLeftOverInputStream.isDataBuffered())
/*     */           {
/* 295 */             ServerImpl.this.requestStarted(localHttpConnection);
/* 296 */             handle(localHttpConnection.getChannel(), localHttpConnection);
/*     */           } else {
/* 298 */             this.connsToRegister.add(localHttpConnection);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException) {
/* 303 */         ServerImpl.this.logger.log(Level.FINER, "Dispatcher (1)", localIOException);
/*     */ 
/* 306 */         localHttpConnection.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     void reRegister(HttpConnection paramHttpConnection)
/*     */     {
/*     */       try
/*     */       {
/* 316 */         SocketChannel localSocketChannel = paramHttpConnection.getChannel();
/* 317 */         localSocketChannel.configureBlocking(false);
/* 318 */         SelectionKey localSelectionKey = localSocketChannel.register(ServerImpl.this.selector, 1);
/* 319 */         localSelectionKey.attach(paramHttpConnection);
/* 320 */         paramHttpConnection.selectionKey = localSelectionKey;
/* 321 */         paramHttpConnection.time = (ServerImpl.this.getTime() + ServerImpl.IDLE_INTERVAL);
/* 322 */         ServerImpl.this.idleConnections.add(paramHttpConnection);
/*     */       } catch (IOException localIOException) {
/* 324 */         ServerImpl.dprint(localIOException);
/* 325 */         ServerImpl.this.logger.log(Level.FINER, "Dispatcher(8)", localIOException);
/* 326 */         paramHttpConnection.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void run() {
/* 331 */       while (!ServerImpl.this.finished)
/*     */         try {
/* 333 */           ListIterator localListIterator = this.connsToRegister.listIterator();
/*     */ 
/* 335 */           for (Object localObject1 = this.connsToRegister.iterator(); ((Iterator)localObject1).hasNext(); ) { HttpConnection localHttpConnection1 = (HttpConnection)((Iterator)localObject1).next();
/* 336 */             reRegister(localHttpConnection1);
/*     */           }
/* 338 */           this.connsToRegister.clear();
/*     */ 
/* 340 */           localObject1 = null;
/* 341 */           ServerImpl.this.selector.select(1000L);
/* 342 */           synchronized (ServerImpl.this.lolock) {
/* 343 */             if (ServerImpl.this.events.size() > 0) {
/* 344 */               localObject1 = ServerImpl.this.events;
/* 345 */               ServerImpl.this.events = new LinkedList();
/*     */             }
/*     */           }
/*     */ 
/* 349 */           if (localObject1 != null) {
/* 350 */             for (??? = ((List)localObject1).iterator(); ((Iterator)???).hasNext(); ) { localObject3 = (Event)((Iterator)???).next();
/* 351 */               handleEvent((Event)localObject3);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 357 */           ??? = ServerImpl.this.selector.selectedKeys();
/* 358 */           Object localObject3 = ((Set)???).iterator();
/* 359 */           while (((Iterator)localObject3).hasNext()) {
/* 360 */             SelectionKey localSelectionKey = (SelectionKey)((Iterator)localObject3).next();
/* 361 */             ((Iterator)localObject3).remove();
/*     */             Object localObject4;
/*     */             HttpConnection localHttpConnection2;
/* 362 */             if (localSelectionKey.equals(ServerImpl.this.listenerKey)) {
/* 363 */               if (!ServerImpl.this.terminating)
/*     */               {
/* 366 */                 SocketChannel localSocketChannel = ServerImpl.this.schan.accept();
/*     */ 
/* 369 */                 if (ServerConfig.noDelay()) {
/* 370 */                   localSocketChannel.socket().setTcpNoDelay(true);
/*     */                 }
/*     */ 
/* 373 */                 if (localSocketChannel != null)
/*     */                 {
/* 376 */                   localSocketChannel.configureBlocking(false);
/* 377 */                   localObject4 = localSocketChannel.register(ServerImpl.this.selector, 1);
/* 378 */                   localHttpConnection2 = new HttpConnection();
/* 379 */                   localHttpConnection2.selectionKey = ((SelectionKey)localObject4);
/* 380 */                   localHttpConnection2.setChannel(localSocketChannel);
/* 381 */                   ((SelectionKey)localObject4).attach(localHttpConnection2);
/* 382 */                   ServerImpl.this.requestStarted(localHttpConnection2);
/* 383 */                   ServerImpl.this.allConnections.add(localHttpConnection2);
/*     */                 }
/*     */               }
/*     */             } else try { if (localSelectionKey.isReadable())
/*     */                 {
/* 388 */                   localObject4 = (SocketChannel)localSelectionKey.channel();
/* 389 */                   localHttpConnection2 = (HttpConnection)localSelectionKey.attachment();
/*     */ 
/* 391 */                   localSelectionKey.cancel();
/* 392 */                   ((SocketChannel)localObject4).configureBlocking(true);
/* 393 */                   if (ServerImpl.this.idleConnections.remove(localHttpConnection2))
/*     */                   {
/* 396 */                     ServerImpl.this.requestStarted(localHttpConnection2);
/*     */                   }
/* 398 */                   handle((SocketChannel)localObject4, localHttpConnection2);
/*     */                 }
/* 400 */                 else if (!$assertionsDisabled) { throw new AssertionError(); }
/*     */               } catch (CancelledKeyException localCancelledKeyException)
/*     */               {
/* 403 */                 handleException(localSelectionKey, null);
/*     */               } catch (IOException localIOException2) {
/* 405 */                 handleException(localSelectionKey, localIOException2);
/*     */               }
/*     */ 
/*     */           }
/*     */ 
/* 410 */           ServerImpl.this.selector.selectNow();
/*     */         } catch (IOException localIOException1) {
/* 412 */           ServerImpl.this.logger.log(Level.FINER, "Dispatcher (4)", localIOException1);
/*     */         } catch (Exception localException) {
/* 414 */           localException.printStackTrace();
/* 415 */           ServerImpl.this.logger.log(Level.FINER, "Dispatcher (7)", localException);
/*     */         }
/*     */     }
/*     */ 
/*     */     private void handleException(SelectionKey paramSelectionKey, Exception paramException)
/*     */     {
/* 421 */       HttpConnection localHttpConnection = (HttpConnection)paramSelectionKey.attachment();
/* 422 */       if (paramException != null) {
/* 423 */         ServerImpl.this.logger.log(Level.FINER, "Dispatcher (2)", paramException);
/*     */       }
/* 425 */       ServerImpl.this.closeConnection(localHttpConnection);
/*     */     }
/*     */ 
/*     */     public void handle(SocketChannel paramSocketChannel, HttpConnection paramHttpConnection) throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 432 */         ServerImpl.Exchange localExchange = new ServerImpl.Exchange(ServerImpl.this, paramSocketChannel, ServerImpl.this.protocol, paramHttpConnection);
/* 433 */         ServerImpl.this.executor.execute(localExchange);
/*     */       } catch (HttpError localHttpError) {
/* 435 */         ServerImpl.this.logger.log(Level.FINER, "Dispatcher (4)", localHttpError);
/* 436 */         ServerImpl.this.closeConnection(paramHttpConnection);
/*     */       } catch (IOException localIOException) {
/* 438 */         ServerImpl.this.logger.log(Level.FINER, "Dispatcher (5)", localIOException);
/* 439 */         ServerImpl.this.closeConnection(paramHttpConnection);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class Exchange
/*     */     implements Runnable
/*     */   {
/*     */     SocketChannel chan;
/*     */     HttpConnection connection;
/*     */     HttpContextImpl context;
/*     */     InputStream rawin;
/*     */     OutputStream rawout;
/*     */     String protocol;
/*     */     ExchangeImpl tx;
/*     */     HttpContextImpl ctx;
/* 493 */     boolean rejected = false;
/*     */ 
/*     */     Exchange(SocketChannel paramString, String paramHttpConnection, HttpConnection arg4) throws IOException {
/* 496 */       this.chan = paramString;
/*     */       Object localObject;
/* 497 */       this.connection = localObject;
/* 498 */       this.protocol = paramHttpConnection;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 503 */       this.context = this.connection.getHttpContext();
/*     */ 
/* 505 */       SSLEngine localSSLEngine = null;
/* 506 */       String str1 = null;
/* 507 */       SSLStreams localSSLStreams = null;
/*     */       try
/*     */       {
/*     */         int i;
/* 509 */         if (this.context != null) {
/* 510 */           this.rawin = this.connection.getInputStream();
/* 511 */           this.rawout = this.connection.getRawOutputStream();
/* 512 */           i = 0;
/*     */         }
/*     */         else {
/* 515 */           i = 1;
/* 516 */           if (ServerImpl.this.https) {
/* 517 */             if (ServerImpl.this.sslContext == null) {
/* 518 */               ServerImpl.this.logger.warning("SSL connection received. No https contxt created");
/* 519 */               throw new HttpError("No SSL context established");
/*     */             }
/* 521 */             localSSLStreams = new SSLStreams(ServerImpl.this, ServerImpl.this.sslContext, this.chan);
/* 522 */             this.rawin = localSSLStreams.getInputStream();
/* 523 */             this.rawout = localSSLStreams.getOutputStream();
/* 524 */             localSSLEngine = localSSLStreams.getSSLEngine();
/* 525 */             this.connection.sslStreams = localSSLStreams;
/*     */           } else {
/* 527 */             this.rawin = new BufferedInputStream(new Request.ReadStream(ServerImpl.this, this.chan));
/*     */ 
/* 531 */             this.rawout = new Request.WriteStream(ServerImpl.this, this.chan);
/*     */           }
/*     */ 
/* 535 */           this.connection.raw = this.rawin;
/* 536 */           this.connection.rawout = this.rawout;
/*     */         }
/* 538 */         Request localRequest = new Request(this.rawin, this.rawout);
/* 539 */         str1 = localRequest.requestLine();
/* 540 */         if (str1 == null)
/*     */         {
/* 542 */           ServerImpl.this.closeConnection(this.connection);
/* 543 */           return;
/*     */         }
/* 545 */         int j = str1.indexOf(' ');
/* 546 */         if (j == -1) {
/* 547 */           reject(400, str1, "Bad request line");
/*     */ 
/* 549 */           return;
/*     */         }
/* 551 */         String str2 = str1.substring(0, j);
/* 552 */         int k = j + 1;
/* 553 */         j = str1.indexOf(' ', k);
/* 554 */         if (j == -1) {
/* 555 */           reject(400, str1, "Bad request line");
/*     */ 
/* 557 */           return;
/*     */         }
/* 559 */         String str3 = str1.substring(k, j);
/* 560 */         URI localURI = new URI(str3);
/* 561 */         k = j + 1;
/* 562 */         String str4 = str1.substring(k);
/* 563 */         Headers localHeaders1 = localRequest.headers();
/* 564 */         String str5 = localHeaders1.getFirst("Transfer-encoding");
/* 565 */         long l = 0L;
/* 566 */         if ((str5 != null) && (str5.equalsIgnoreCase("chunked"))) {
/* 567 */           l = -1L;
/*     */         } else {
/* 569 */           str5 = localHeaders1.getFirst("Content-Length");
/* 570 */           if (str5 != null) {
/* 571 */             l = Long.parseLong(str5);
/*     */           }
/* 573 */           if (l == 0L) {
/* 574 */             ServerImpl.this.requestCompleted(this.connection);
/*     */           }
/*     */         }
/* 577 */         this.ctx = ServerImpl.this.contexts.findContext(this.protocol, localURI.getPath());
/* 578 */         if (this.ctx == null) {
/* 579 */           reject(404, str1, "No context found for request");
/*     */ 
/* 581 */           return;
/*     */         }
/* 583 */         this.connection.setContext(this.ctx);
/* 584 */         if (this.ctx.getHandler() == null) {
/* 585 */           reject(500, str1, "No handler for context");
/*     */ 
/* 587 */           return;
/*     */         }
/* 589 */         this.tx = new ExchangeImpl(str2, localURI, localRequest, l, this.connection);
/*     */ 
/* 592 */         String str6 = localHeaders1.getFirst("Connection");
/* 593 */         Headers localHeaders2 = this.tx.getResponseHeaders();
/*     */ 
/* 595 */         if ((str6 != null) && (str6.equalsIgnoreCase("close"))) {
/* 596 */           this.tx.close = true;
/*     */         }
/* 598 */         if (str4.equalsIgnoreCase("http/1.0")) {
/* 599 */           this.tx.http10 = true;
/* 600 */           if (str6 == null) {
/* 601 */             this.tx.close = true;
/* 602 */             localHeaders2.set("Connection", "close");
/* 603 */           } else if (str6.equalsIgnoreCase("keep-alive")) {
/* 604 */             localHeaders2.set("Connection", "keep-alive");
/* 605 */             int m = (int)ServerConfig.getIdleInterval() / 1000;
/* 606 */             int n = ServerConfig.getMaxIdleConnections();
/* 607 */             localObject = "timeout=" + m + ", max=" + n;
/* 608 */             localHeaders2.set("Keep-Alive", (String)localObject);
/*     */           }
/*     */         }
/*     */ 
/* 612 */         if (i != 0) {
/* 613 */           this.connection.setParameters(this.rawin, this.rawout, this.chan, localSSLEngine, localSSLStreams, ServerImpl.this.sslContext, this.protocol, this.ctx, this.rawin);
/*     */         }
/*     */ 
/* 623 */         String str7 = localHeaders1.getFirst("Expect");
/* 624 */         if ((str7 != null) && (str7.equalsIgnoreCase("100-continue"))) {
/* 625 */           ServerImpl.this.logReply(100, str1, null);
/* 626 */           sendReply(100, false, null);
/*     */         }
/*     */ 
/* 637 */         List localList = this.ctx.getSystemFilters();
/* 638 */         Object localObject = this.ctx.getFilters();
/*     */ 
/* 640 */         Filter.Chain localChain1 = new Filter.Chain(localList, this.ctx.getHandler());
/* 641 */         Filter.Chain localChain2 = new Filter.Chain((List)localObject, new LinkHandler(localChain1));
/*     */ 
/* 644 */         this.tx.getRequestBody();
/* 645 */         this.tx.getResponseBody();
/* 646 */         if (ServerImpl.this.https)
/* 647 */           localChain2.doFilter(new HttpsExchangeImpl(this.tx));
/*     */         else
/* 649 */           localChain2.doFilter(new HttpExchangeImpl(this.tx));
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 653 */         ServerImpl.this.logger.log(Level.FINER, "ServerImpl.Exchange (1)", localIOException);
/* 654 */         ServerImpl.this.closeConnection(this.connection);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 656 */         reject(400, str1, "NumberFormatException thrown");
/*     */       }
/*     */       catch (URISyntaxException localURISyntaxException) {
/* 659 */         reject(400, str1, "URISyntaxException thrown");
/*     */       }
/*     */       catch (Exception localException) {
/* 662 */         ServerImpl.this.logger.log(Level.FINER, "ServerImpl.Exchange (2)", localException);
/* 663 */         ServerImpl.this.closeConnection(this.connection);
/*     */       }
/*     */     }
/*     */ 
/*     */     void reject(int paramInt, String paramString1, String paramString2)
/*     */     {
/* 682 */       this.rejected = true;
/* 683 */       ServerImpl.this.logReply(paramInt, paramString1, paramString2);
/* 684 */       sendReply(paramInt, false, "<h1>" + paramInt + Code.msg(paramInt) + "</h1>" + paramString2);
/*     */ 
/* 687 */       ServerImpl.this.closeConnection(this.connection);
/*     */     }
/*     */ 
/*     */     void sendReply(int paramInt, boolean paramBoolean, String paramString)
/*     */     {
/*     */       try
/*     */       {
/* 694 */         StringBuilder localStringBuilder = new StringBuilder(512);
/* 695 */         localStringBuilder.append("HTTP/1.1 ").append(paramInt).append(Code.msg(paramInt)).append("\r\n");
/*     */ 
/* 698 */         if ((paramString != null) && (paramString.length() != 0)) {
/* 699 */           localStringBuilder.append("Content-Length: ").append(paramString.length()).append("\r\n").append("Content-Type: text/html\r\n");
/*     */         }
/*     */         else
/*     */         {
/* 703 */           localStringBuilder.append("Content-Length: 0\r\n");
/* 704 */           paramString = "";
/*     */         }
/* 706 */         if (paramBoolean) {
/* 707 */           localStringBuilder.append("Connection: close\r\n");
/*     */         }
/* 709 */         localStringBuilder.append("\r\n").append(paramString);
/* 710 */         String str = localStringBuilder.toString();
/* 711 */         byte[] arrayOfByte = str.getBytes("ISO8859_1");
/* 712 */         this.rawout.write(arrayOfByte);
/* 713 */         this.rawout.flush();
/* 714 */         if (paramBoolean)
/* 715 */           ServerImpl.this.closeConnection(this.connection);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 718 */         ServerImpl.this.logger.log(Level.FINER, "ServerImpl.sendReply", localIOException);
/* 719 */         ServerImpl.this.closeConnection(this.connection);
/*     */       }
/*     */     }
/*     */ 
/*     */     class LinkHandler
/*     */       implements HttpHandler
/*     */     {
/*     */       Filter.Chain nextChain;
/*     */ 
/*     */       LinkHandler(Filter.Chain arg2)
/*     */       {
/*     */         Object localObject;
/* 673 */         this.nextChain = localObject;
/*     */       }
/*     */ 
/*     */       public void handle(HttpExchange paramHttpExchange) throws IOException {
/* 677 */         this.nextChain.doFilter(paramHttpExchange);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class ServerTimerTask extends TimerTask
/*     */   {
/*     */     ServerTimerTask()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 807 */       LinkedList localLinkedList = new LinkedList();
/* 808 */       ServerImpl.this.time = System.currentTimeMillis();
/* 809 */       ServerImpl.access$1808(ServerImpl.this);
/*     */       Iterator localIterator;
/*     */       HttpConnection localHttpConnection;
/* 810 */       synchronized (ServerImpl.this.idleConnections) {
/* 811 */         for (localIterator = ServerImpl.this.idleConnections.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 812 */           if (localHttpConnection.time <= ServerImpl.this.time) {
/* 813 */             localLinkedList.add(localHttpConnection);
/*     */           }
/*     */         }
/* 816 */         for (localIterator = localLinkedList.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 817 */           ServerImpl.this.idleConnections.remove(localHttpConnection);
/* 818 */           ServerImpl.this.allConnections.remove(localHttpConnection);
/* 819 */           localHttpConnection.close(); }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class ServerTimerTask1 extends TimerTask {
/*     */     ServerTimerTask1() {
/*     */     }
/*     */ 
/*     */     public void run() {
/* 829 */       LinkedList localLinkedList = new LinkedList();
/* 830 */       ServerImpl.this.time = System.currentTimeMillis();
/*     */       Iterator localIterator;
/*     */       HttpConnection localHttpConnection;
/* 831 */       synchronized (ServerImpl.this.reqConnections) {
/* 832 */         if (ServerImpl.MAX_REQ_TIME != -1L) {
/* 833 */           for (localIterator = ServerImpl.this.reqConnections.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 834 */             if (localHttpConnection.creationTime + ServerImpl.TIMER_MILLIS + ServerImpl.MAX_REQ_TIME <= ServerImpl.this.time) {
/* 835 */               localLinkedList.add(localHttpConnection);
/*     */             }
/*     */           }
/* 838 */           for (localIterator = localLinkedList.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 839 */             ServerImpl.this.logger.log(Level.FINE, "closing: no request: " + localHttpConnection);
/* 840 */             ServerImpl.this.reqConnections.remove(localHttpConnection);
/* 841 */             ServerImpl.this.allConnections.remove(localHttpConnection);
/* 842 */             localHttpConnection.close();
/*     */           }
/*     */         }
/*     */       }
/* 846 */       localLinkedList = new LinkedList();
/* 847 */       synchronized (ServerImpl.this.rspConnections) {
/* 848 */         if (ServerImpl.MAX_RSP_TIME != -1L) {
/* 849 */           for (localIterator = ServerImpl.this.rspConnections.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 850 */             if (localHttpConnection.rspStartedTime + ServerImpl.TIMER_MILLIS + ServerImpl.MAX_RSP_TIME <= ServerImpl.this.time) {
/* 851 */               localLinkedList.add(localHttpConnection);
/*     */             }
/*     */           }
/* 854 */           for (localIterator = localLinkedList.iterator(); localIterator.hasNext(); ) { localHttpConnection = (HttpConnection)localIterator.next();
/* 855 */             ServerImpl.this.logger.log(Level.FINE, "closing: no response: " + localHttpConnection);
/* 856 */             ServerImpl.this.rspConnections.remove(localHttpConnection);
/* 857 */             ServerImpl.this.allConnections.remove(localHttpConnection);
/* 858 */             localHttpConnection.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ServerImpl
 * JD-Core Version:    0.6.2
 */