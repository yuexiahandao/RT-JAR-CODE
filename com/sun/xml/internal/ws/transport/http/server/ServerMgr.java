/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.xml.internal.ws.server.ServerRtException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class ServerMgr
/*     */ {
/*  47 */   private static final ServerMgr serverMgr = new ServerMgr();
/*  48 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
/*     */ 
/*  51 */   private final Map<InetSocketAddress, ServerState> servers = new HashMap();
/*     */ 
/*     */   static ServerMgr getInstance()
/*     */   {
/*  60 */     return serverMgr; } 
/*     */   HttpContext createContext(String address) { // Byte code:
/*     */     //   0: new 99	java/net/URL
/*     */     //   3: dup
/*     */     //   4: aload_1
/*     */     //   5: invokespecial 197	java/net/URL:<init>	(Ljava/lang/String;)V
/*     */     //   8: astore 4
/*     */     //   10: aload 4
/*     */     //   12: invokevirtual 194	java/net/URL:getPort	()I
/*     */     //   15: istore 5
/*     */     //   17: iload 5
/*     */     //   19: iconst_m1
/*     */     //   20: if_icmpne +10 -> 30
/*     */     //   23: aload 4
/*     */     //   25: invokevirtual 193	java/net/URL:getDefaultPort	()I
/*     */     //   28: istore 5
/*     */     //   30: new 97	java/net/InetSocketAddress
/*     */     //   33: dup
/*     */     //   34: aload 4
/*     */     //   36: invokevirtual 195	java/net/URL:getHost	()Ljava/lang/String;
/*     */     //   39: iload 5
/*     */     //   41: invokespecial 191	java/net/InetSocketAddress:<init>	(Ljava/lang/String;I)V
/*     */     //   44: astore 6
/*     */     //   46: aload_0
/*     */     //   47: getfield 168	com/sun/xml/internal/ws/transport/http/server/ServerMgr:servers	Ljava/util/Map;
/*     */     //   50: dup
/*     */     //   51: astore 7
/*     */     //   53: monitorenter
/*     */     //   54: aload_0
/*     */     //   55: getfield 168	com/sun/xml/internal/ws/transport/http/server/ServerMgr:servers	Ljava/util/Map;
/*     */     //   58: aload 6
/*     */     //   60: invokeinterface 203 2 0
/*     */     //   65: checkcast 91	com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState
/*     */     //   68: astore_3
/*     */     //   69: aload_3
/*     */     //   70: ifnonnull +145 -> 215
/*     */     //   73: getstatic 169	com/sun/xml/internal/ws/transport/http/server/ServerMgr:logger	Ljava/util/logging/Logger;
/*     */     //   76: new 95	java/lang/StringBuilder
/*     */     //   79: dup
/*     */     //   80: invokespecial 187	java/lang/StringBuilder:<init>	()V
/*     */     //   83: ldc 2
/*     */     //   85: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   88: aload 6
/*     */     //   90: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   93: invokevirtual 188	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   96: invokevirtual 201	java/util/logging/Logger:fine	(Ljava/lang/String;)V
/*     */     //   99: aload 6
/*     */     //   101: iconst_0
/*     */     //   102: invokestatic 178	com/sun/net/httpserver/HttpServer:create	(Ljava/net/InetSocketAddress;I)Lcom/sun/net/httpserver/HttpServer;
/*     */     //   105: astore_2
/*     */     //   106: aload_2
/*     */     //   107: invokestatic 200	java/util/concurrent/Executors:newCachedThreadPool	()Ljava/util/concurrent/ExecutorService;
/*     */     //   110: invokevirtual 176	com/sun/net/httpserver/HttpServer:setExecutor	(Ljava/util/concurrent/Executor;)V
/*     */     //   113: aload 4
/*     */     //   115: invokevirtual 198	java/net/URL:toURI	()Ljava/net/URI;
/*     */     //   118: invokevirtual 192	java/net/URI:getPath	()Ljava/lang/String;
/*     */     //   121: astore 8
/*     */     //   123: getstatic 169	com/sun/xml/internal/ws/transport/http/server/ServerMgr:logger	Ljava/util/logging/Logger;
/*     */     //   126: new 95	java/lang/StringBuilder
/*     */     //   129: dup
/*     */     //   130: invokespecial 187	java/lang/StringBuilder:<init>	()V
/*     */     //   133: ldc 1
/*     */     //   135: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   138: aload 8
/*     */     //   140: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   143: invokevirtual 188	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   146: invokevirtual 201	java/util/logging/Logger:fine	(Ljava/lang/String;)V
/*     */     //   149: aload_2
/*     */     //   150: aload 8
/*     */     //   152: invokevirtual 177	com/sun/net/httpserver/HttpServer:createContext	(Ljava/lang/String;)Lcom/sun/net/httpserver/HttpContext;
/*     */     //   155: astore 9
/*     */     //   157: aload_2
/*     */     //   158: invokevirtual 171	com/sun/net/httpserver/HttpServer:start	()V
/*     */     //   161: getstatic 169	com/sun/xml/internal/ws/transport/http/server/ServerMgr:logger	Ljava/util/logging/Logger;
/*     */     //   164: new 95	java/lang/StringBuilder
/*     */     //   167: dup
/*     */     //   168: invokespecial 187	java/lang/StringBuilder:<init>	()V
/*     */     //   171: ldc 3
/*     */     //   173: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   176: aload 6
/*     */     //   178: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   181: invokevirtual 188	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   184: invokevirtual 201	java/util/logging/Logger:fine	(Ljava/lang/String;)V
/*     */     //   187: new 91	com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState
/*     */     //   190: dup
/*     */     //   191: aload_2
/*     */     //   192: invokespecial 185	com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState:<init>	(Lcom/sun/net/httpserver/HttpServer;)V
/*     */     //   195: astore_3
/*     */     //   196: aload_0
/*     */     //   197: getfield 168	com/sun/xml/internal/ws/transport/http/server/ServerMgr:servers	Ljava/util/Map;
/*     */     //   200: aload 6
/*     */     //   202: aload_3
/*     */     //   203: invokeinterface 205 3 0
/*     */     //   208: pop
/*     */     //   209: aload 9
/*     */     //   211: aload 7
/*     */     //   213: monitorexit
/*     */     //   214: areturn
/*     */     //   215: aload 7
/*     */     //   217: monitorexit
/*     */     //   218: goto +11 -> 229
/*     */     //   221: astore 10
/*     */     //   223: aload 7
/*     */     //   225: monitorexit
/*     */     //   226: aload 10
/*     */     //   228: athrow
/*     */     //   229: aload_3
/*     */     //   230: invokevirtual 184	com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState:getServer	()Lcom/sun/net/httpserver/HttpServer;
/*     */     //   233: astore_2
/*     */     //   234: getstatic 169	com/sun/xml/internal/ws/transport/http/server/ServerMgr:logger	Ljava/util/logging/Logger;
/*     */     //   237: new 95	java/lang/StringBuilder
/*     */     //   240: dup
/*     */     //   241: invokespecial 187	java/lang/StringBuilder:<init>	()V
/*     */     //   244: ldc 1
/*     */     //   246: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   249: aload 4
/*     */     //   251: invokevirtual 196	java/net/URL:getPath	()Ljava/lang/String;
/*     */     //   254: invokevirtual 190	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   257: invokevirtual 188	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   260: invokevirtual 201	java/util/logging/Logger:fine	(Ljava/lang/String;)V
/*     */     //   263: aload_2
/*     */     //   264: aload 4
/*     */     //   266: invokevirtual 196	java/net/URL:getPath	()Ljava/lang/String;
/*     */     //   269: invokevirtual 177	com/sun/net/httpserver/HttpServer:createContext	(Ljava/lang/String;)Lcom/sun/net/httpserver/HttpContext;
/*     */     //   272: astore 7
/*     */     //   274: aload_3
/*     */     //   275: invokevirtual 183	com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState:oneMoreContext	()V
/*     */     //   278: aload 7
/*     */     //   280: areturn
/*     */     //   281: astore_2
/*     */     //   282: new 89	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   285: dup
/*     */     //   286: ldc 5
/*     */     //   288: iconst_1
/*     */     //   289: anewarray 93	java/lang/Object
/*     */     //   292: dup
/*     */     //   293: iconst_0
/*     */     //   294: aload_2
/*     */     //   295: aastore
/*     */     //   296: invokespecial 179	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   299: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   54	214	221	finally
/*     */     //   215	218	221	finally
/*     */     //   221	226	221	finally
/*     */     //   0	214	281	java/lang/Exception
/*     */     //   215	280	281	java/lang/Exception } 
/* 111 */   void removeContext(HttpContext context) { InetSocketAddress inetAddress = context.getServer().getAddress();
/* 112 */     synchronized (this.servers) {
/* 113 */       ServerState state = (ServerState)this.servers.get(inetAddress);
/* 114 */       int instances = state.noOfContexts();
/* 115 */       if (instances < 2) {
/* 116 */         ((ExecutorService)state.getServer().getExecutor()).shutdown();
/* 117 */         state.getServer().stop(0);
/* 118 */         this.servers.remove(inetAddress);
/*     */       } else {
/* 120 */         state.getServer().removeContext(context);
/* 121 */         state.oneLessContext();
/*     */       }
/*     */     } }
/*     */ 
/*     */   private static final class ServerState {
/*     */     private final HttpServer server;
/*     */     private int instances;
/*     */ 
/*     */     ServerState(HttpServer server) {
/* 131 */       this.server = server;
/* 132 */       this.instances = 1;
/*     */     }
/*     */ 
/*     */     public HttpServer getServer() {
/* 136 */       return this.server;
/*     */     }
/*     */ 
/*     */     public void oneMoreContext() {
/* 140 */       this.instances += 1;
/*     */     }
/*     */ 
/*     */     public void oneLessContext() {
/* 144 */       this.instances -= 1;
/*     */     }
/*     */ 
/*     */     public int noOfContexts() {
/* 148 */       return this.instances;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.ServerMgr
 * JD-Core Version:    0.6.2
 */