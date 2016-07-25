/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.resources.HttpserverMessages;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.spi.http.HttpExchange;
/*     */ import javax.xml.ws.spi.http.HttpHandler;
/*     */ 
/*     */ final class PortableHttpHandler extends HttpHandler
/*     */ {
/*     */   private static final String GET_METHOD = "GET";
/*     */   private static final String POST_METHOD = "POST";
/*     */   private static final String HEAD_METHOD = "HEAD";
/*     */   private static final String PUT_METHOD = "PUT";
/*     */   private static final String DELETE_METHOD = "DELETE";
/*  53 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
/*     */   private final HttpAdapter adapter;
/*     */   private final Executor executor;
/*     */ 
/*     */   public PortableHttpHandler(@NotNull HttpAdapter adapter, @Nullable Executor executor)
/*     */   {
/*  61 */     assert (adapter != null);
/*  62 */     this.adapter = adapter;
/*  63 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   public void handle(HttpExchange msg)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       logger.fine("Received HTTP request:" + msg.getRequestURI());
/*  72 */       if (this.executor != null)
/*     */       {
/*  75 */         this.executor.execute(new HttpHandlerRunnable(msg));
/*     */       }
/*  77 */       else handleExchange(msg);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  81 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleExchange(HttpExchange msg) throws IOException {
/*  86 */     WSHTTPConnection con = new PortableConnectionImpl(this.adapter, msg);
/*     */     try {
/*  88 */       logger.fine("Received HTTP request:" + msg.getRequestURI());
/*  89 */       String method = msg.getRequestMethod();
/*  90 */       if ((method.equals("GET")) || (method.equals("POST")) || (method.equals("HEAD")) || (method.equals("PUT")) || (method.equals("DELETE")))
/*     */       {
/*  92 */         this.adapter.handle(con);
/*     */       }
/*  94 */       else logger.warning(HttpserverMessages.UNEXPECTED_HTTP_METHOD(method)); 
/*     */     }
/*     */     finally
/*     */     {
/*  97 */       msg.close();
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
/* 109 */       this.msg = msg;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 114 */         PortableHttpHandler.this.handleExchange(this.msg);
/*     */       }
/*     */       catch (Throwable e) {
/* 117 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.PortableHttpHandler
 * JD-Core Version:    0.6.2
 */