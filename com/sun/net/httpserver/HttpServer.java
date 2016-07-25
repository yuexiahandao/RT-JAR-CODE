/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.spi.HttpServerProvider;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ public abstract class HttpServer
/*     */ {
/*     */   public static HttpServer create()
/*     */     throws IOException
/*     */   {
/* 104 */     return create(null, 0);
/*     */   }
/*     */ 
/*     */   public static HttpServer create(InetSocketAddress paramInetSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 128 */     HttpServerProvider localHttpServerProvider = HttpServerProvider.provider();
/* 129 */     return localHttpServerProvider.createHttpServer(paramInetSocketAddress, paramInt);
/*     */   }
/*     */ 
/*     */   public abstract void bind(InetSocketAddress paramInetSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void start();
/*     */ 
/*     */   public abstract void setExecutor(Executor paramExecutor);
/*     */ 
/*     */   public abstract Executor getExecutor();
/*     */ 
/*     */   public abstract void stop(int paramInt);
/*     */ 
/*     */   public abstract HttpContext createContext(String paramString, HttpHandler paramHttpHandler);
/*     */ 
/*     */   public abstract HttpContext createContext(String paramString);
/*     */ 
/*     */   public abstract void removeContext(String paramString)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract void removeContext(HttpContext paramHttpContext);
/*     */ 
/*     */   public abstract InetSocketAddress getAddress();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpServer
 * JD-Core Version:    0.6.2
 */