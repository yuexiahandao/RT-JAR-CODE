/*    */ package com.sun.net.httpserver;
/*    */ 
/*    */ import com.sun.net.httpserver.spi.HttpServerProvider;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ public abstract class HttpsServer extends HttpServer
/*    */ {
/*    */   public static HttpsServer create()
/*    */     throws IOException
/*    */   {
/* 63 */     return create(null, 0);
/*    */   }
/*    */ 
/*    */   public static HttpsServer create(InetSocketAddress paramInetSocketAddress, int paramInt)
/*    */     throws IOException
/*    */   {
/* 88 */     HttpServerProvider localHttpServerProvider = HttpServerProvider.provider();
/* 89 */     return localHttpServerProvider.createHttpsServer(paramInetSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public abstract void setHttpsConfigurator(HttpsConfigurator paramHttpsConfigurator);
/*    */ 
/*    */   public abstract HttpsConfigurator getHttpsConfigurator();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpsServer
 * JD-Core Version:    0.6.2
 */