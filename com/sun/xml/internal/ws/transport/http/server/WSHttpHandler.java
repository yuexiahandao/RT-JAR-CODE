/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.xml.internal.ws.resources.HttpserverMessages;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class WSHttpHandler
/*     */   implements HttpHandler
/*     */ {
/*     */   private static final String GET_METHOD = "GET";
/*     */   private static final String POST_METHOD = "POST";
/*     */   private static final String HEAD_METHOD = "HEAD";
/*     */   private static final String PUT_METHOD = "PUT";
/*     */   private static final String DELETE_METHOD = "DELETE";
/*  56 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
/*     */   private final HttpAdapter adapter;
/*     */   private final Executor executor;
/*     */ 
/*     */   public WSHttpHandler(@NotNull HttpAdapter adapter, @Nullable Executor executor)
/*     */   {
/*  64 */     assert (adapter != null);
/*  65 */     this.adapter = adapter;
/*  66 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   public void handle(HttpExchange msg)
/*     */   {
/*     */     try
/*     */     {
/*  74 */       logger.fine("Received HTTP request:" + msg.getRequestURI());
/*  75 */       if (this.executor != null)
/*     */       {
/*  78 */         this.executor.execute(new HttpHandlerRunnable(msg));
/*     */       }
/*  80 */       else handleExchange(msg);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  84 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleExchange(HttpExchange msg) throws IOException {
/*  89 */     WSHTTPConnection con = new ServerConnectionImpl(this.adapter, msg);
/*     */     try {
/*  91 */       logger.fine("Received HTTP request:" + msg.getRequestURI());
/*  92 */       String method = msg.getRequestMethod();
/*  93 */       if ((method.equals("GET")) || (method.equals("POST")) || (method.equals("HEAD")) || (method.equals("PUT")) || (method.equals("DELETE")))
/*     */       {
/*  95 */         this.adapter.handle(con);
/*     */       }
/*  97 */       else logger.warning(HttpserverMessages.UNEXPECTED_HTTP_METHOD(method)); 
/*     */     }
/*     */     finally
/*     */     {
/* 100 */       msg.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   class HttpHandlerRunnable
/*     */     implements Runnable
/*     */   {
/*     */     final HttpExchange msg;
/*     */ 
/*     */     HttpHandlerRunnable(HttpExchange msg)
/*     */     {
/* 112 */       this.msg = msg;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 117 */         WSHttpHandler.this.handleExchange(this.msg);
/*     */       }
/*     */       catch (Throwable e) {
/* 120 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.WSHttpHandler
 * JD-Core Version:    0.6.2
 */